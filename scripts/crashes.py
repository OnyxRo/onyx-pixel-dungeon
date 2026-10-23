"""Crash list for Onyx's Pixel Dungeon.

The game adds a line to crashes/crashes.jsonl in the main project folder every time it crashes
(desktop/.../CrashLog.java). This script groups the same crash together and tracks which are fixed.

  python -I scripts/crashes.py               open crashes, most frequent first
  python -I scripts/crashes.py all           also list fixed ones
  python -I scripts/crashes.py show ID       full stack of the newest crash in a group
  python -I scripts/crashes.py fixed ID NOTE mark a group fixed (NOTE says what was done, and the commit)

A fixed crash that happens again in a newer game version shows up as open again, marked CAME BACK.
"""
import hashlib
import json
import os
import re
import subprocess
import sys
from datetime import datetime


def main_repo():
    here = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    try:
        common = subprocess.run(["git", "-C", here, "rev-parse", "--path-format=absolute", "--git-common-dir"],
                                capture_output=True, text=True, check=True).stdout.strip()
        return os.path.dirname(common)
    except Exception:
        return here


FOLDER = os.environ.get("ONYX_CRASHES") or os.path.join(main_repo(), "crashes")
LOG = os.path.join(FOLDER, "crashes.jsonl")
FIXED = os.path.join(FOLDER, "fixed.json")

FRAME = re.compile(r"^\s*at ([\w.$<>]+)\(")


def signature(entry):
    """The error type plus the first five game frames, without line numbers, so edits elsewhere keep the same id."""
    stack = entry.get("stack", "")
    error = entry.get("error", "").split(":")[0]
    every = [m.group(1) for m in map(FRAME.match, stack.splitlines()) if m]
    frames = [f for f in every if f.startswith("com.shatteredpixel") or f.startswith("com.watabou")][:5]
    if not frames:
        frames = every[:5]
    return error + "|" + "|".join(frames)


def short(sig):
    return hashlib.sha1(sig.encode()).hexdigest()[:8]


def load():
    entries = []
    if os.path.exists(LOG):
        with open(LOG, encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if line:
                    try:
                        entries.append(json.loads(line))
                    except ValueError:
                        pass
    fixed = {}
    if os.path.exists(FIXED):
        with open(FIXED, encoding="utf-8") as f:
            fixed = json.load(f)
    return entries, fixed


def groups(entries):
    out = {}
    for e in entries:
        sig = signature(e)
        g = out.setdefault(short(sig), {"sig": sig, "entries": []})
        g["entries"].append(e)
    return out


def is_open(gid, g, fixed):
    if gid not in fixed:
        return True, False
    when = fixed[gid].get("time", "")
    newer = [e for e in g["entries"] if e.get("time", "") > when]
    return bool(newer), bool(newer)


def listing(show_all):
    entries, fixed = load()
    if not entries:
        print("No crashes recorded yet (" + LOG + ").")
        return
    gs = groups(entries)
    rows = sorted(gs.items(), key=lambda kv: -len(kv[1]["entries"]))
    shown = 0
    for gid, g in rows:
        op, back = is_open(gid, g, fixed)
        if not op and not show_all:
            continue
        shown += 1
        last = g["entries"][-1]
        state = "CAME BACK" if back else ("open" if op else "fixed: " + fixed[gid].get("note", ""))
        print("%s  %dx  last %s  v%s  %s" % (gid, len(g["entries"]), last.get("time", "?"), last.get("version", "?"), state))
        print("    " + last.get("error", "")[:200])
        for f in g["sig"].split("|")[1:]:
            print("      at " + f.replace("com.shatteredpixel.shatteredpixeldungeon.", ""))
    open_count = sum(1 for gid, g in gs.items() if is_open(gid, g, fixed)[0])
    print("SUMMARY %d crashes recorded, %d kinds, %d open" % (len(entries), len(gs), open_count))


def show(gid):
    entries, fixed = load()
    g = groups(entries).get(gid)
    if not g:
        print("No crash group " + gid)
        return
    for e in g["entries"]:
        print(e.get("time"), "v" + str(e.get("version")), "thread", e.get("thread"))
    print()
    print(g["entries"][-1].get("stack", ""))


def mark_fixed(gid, note):
    entries, fixed = load()
    g = groups(entries).get(gid)
    if not g:
        print("No crash group " + gid)
        sys.exit(1)
    # crash times have seconds, so a crash later today still counts as coming back
    fixed[gid] = {"time": datetime.now().strftime("%Y-%m-%d %H:%M:%S"), "note": note, "sig": g["sig"]}
    os.makedirs(FOLDER, exist_ok=True)
    with open(FIXED, "w", encoding="utf-8") as f:
        json.dump(fixed, f, indent=1)
    print("marked " + gid + " fixed")


if __name__ == "__main__":
    args = sys.argv[1:]
    if not args:
        listing(False)
    elif args[0] == "all":
        listing(True)
    elif args[0] == "show" and len(args) == 2:
        show(args[1])
    elif args[0] == "fixed" and len(args) >= 3:
        mark_fixed(args[1], " ".join(args[2:]))
    else:
        print(__doc__)
