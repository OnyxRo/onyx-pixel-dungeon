"""Read each mod's own monster spawn table from its source and write ModSpawns.java, so a floor built in a mod's
world spawns what that mod spawned on that floor.

    python -I scripts/mod-spawns.py            print the tables and any monster names not found in this game
    python -I scripts/mod-spawns.py --write    also write core/.../actors/mobs/ModSpawns.java

Mod sources come from ~/mod-sources/<key> (scripts/get-mod.sh). Two table styles are read.
Newer mods (Shattered 0.7 and later) list a rotation per floor, `case 3: return new ArrayList<>(Arrays.asList(Rat.class, ...))`,
read from standardMobRotation or getMobRotation. Older mods give each monster a chance per floor,
`chances = new float[]{...}; classes = new Class<?>[]{...}`, and roll one per spawn. Both are kept as written.
Rare additions (addRareMobs, swapMobAlts) are left out.
"""
import os, re, sys
from collections import OrderedDict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.expanduser(os.environ.get("MOD_SOURCES", "~/mod-sources"))
JAVA = os.path.join(ROOT, "core/src/main/java")
PKG = "com.shatteredpixel.shatteredpixeldungeon"

#mod key (as in WorldShuffle.modKey) -> source folder and the spawn table file name. Devoted, Matt, Lovecraft and
#Magic Ling have their own spawner classes and are not handled here.
MODS = OrderedDict([
    ("abyssalnite", ("abyssalnite", "Bestiary.java")), ("cursed", ("cursed", "Bestiary.java")),
    ("deep", ("deep", "Bestiary.java")), ("dragonic", ("dragonic", "Bestiary.java")),
    ("experienced", ("experienced", "MobSpawner.java")), ("rearranged", ("rearranged", "MobSpawner.java")),
    ("reforged", ("reforged", "MobSpawner.java")), ("ryuse", ("ryuse", "Bestiary.java")),
    ("shittened", ("shittened", "MobSpawner.java")), ("summoning", ("summoning", "Bestiary.java")),
    ("tower", ("tower", "MobSpawner.java")), ("transformation", ("transformation", "MobSpawner.java")),
    ("unleashed", ("unleashed", "Bestiary.java")), ("spacebase", ("spacebase", "Bestiary.java")),
    ("sps", ("sps", "Bestiary.java")), ("rkpd2", ("ratking", "MobSpawner.java")),
    ("bluearchive", ("bluearchive", "MobSpawner.java")), ("rebluearchive", ("rebluearchive", "MobSpawner.java")),
    ("combusted", ("combusted", "MobSpawner.java")),
])

def find_file(folder, name):
    hits = []
    for dp, dn, fn in os.walk(os.path.join(SRC, folder)):
        if name in fn and "actors" in dp and "test" not in dp.lower(): hits.append(os.path.join(dp, name))
    #prefer the real source tree, some repos keep an old copy at the top
    hits.sort(key=lambda h: ("src" + os.sep + "main" not in h and "core" + os.sep + "src" not in h, len(h)))
    return hits[0] if hits else None

def strip_comments(t):
    t = re.sub(r"/\*.*?\*/", "", t, flags=re.S)
    return re.sub(r"//[^\n]*", "", t)

def body_of(text, names):
    """The body of the first method in names that exists in the file."""
    for n in names:
        m = re.search(r"\b" + n + r"\s*\([^)]*\)\s*\{", text)
        if not m: continue
        i, depth = m.end(), 1
        while depth and i < len(text):
            depth += {"{": 1, "}": -1}.get(text[i], 0); i += 1
        return text[m.end():i]
    return None

def cases(body):
    """[(depths, chunk)] for each group of case labels."""
    parts = re.split(r"((?:\s*case\s+\d+\s*:)+|\bdefault\s*:)", body)
    out = []
    for k in range(1, len(parts), 2):
        depths = [int(x) for x in re.findall(r"case\s+(\d+)", parts[k])]
        if depths: out.append((depths, parts[k + 1]))
    return out

def parse(path):
    text = strip_comments(open(path, encoding="utf-8", errors="replace").read())
    table = {}  #depth -> ("list" | "chance", [(name, weight)])
    body = body_of(text, ["standardMobRotation", "getMobRotation", "mobClass", "mob"]) or text
    #Summoning keeps a small and a big dungeon table before its normal one, the last switch is the normal game
    sw = [m.start() for m in re.finditer(r"switch\s*\(\s*depth\s*\)", body)]
    if len(sw) > 1: body = body[sw[-1]:]
    for depths, chunk in cases(body):
        ch = re.search(r"chances\s*=\s*new\s+float\s*\[\s*\]\s*\{([^}]*)\}", chunk)
        cl = re.search(r"classes\s*=\s*new\s+Class\s*<[^>]*>\s*\[\s*\]\s*\{([^}]*)\}", chunk)
        if ch and cl:
            ws = [float(x.strip().rstrip("fF")) for x in ch.group(1).split(",") if x.strip()]
            ns = [x.strip().replace(".class", "").split(".")[-1] for x in cl.group(1).split(",") if x.strip()]
            entry = ("chance", [(n, w) for n, w in zip(ns, ws) if w > 0])
        else:
            #Shaman.random() and Elemental.random() pick a kind when the rotation is made
            names = [a.split(".")[-1] if a else b + ".random" for a, b in re.findall(r"([\w.]+)\.class|(\w+)\.random\(\)", chunk)]
            if not names: continue
            entry = ("list", [(n, 1.0) for n in names])
        for d in depths: table.setdefault(d, entry)
    return table

#--- our classes, simple name -> full names
OURS = {}
for dp, dn, fn in os.walk(os.path.join(JAVA, PKG.replace(".", "/"), "actors/mobs")):
    for f in fn:
        if f.endswith(".java"):
            rel = os.path.relpath(os.path.join(dp, f[:-5]), JAVA).replace(os.sep, ".")
            OURS.setdefault(f[:-5], []).append(rel)
            #nested mob classes, like GnollGeomancer's or a mod's Guardian inside a level, are written Outer$Inner
            for m in re.finditer(r"public\s+static\s+class\s+(\w+)\s+extends\s+\w+", open(os.path.join(dp, f), encoding="utf-8", errors="replace").read()):
                OURS.setdefault(m.group(1), []).append(rel + "$" + m.group(1))
ROSTER = {}
for m in re.finditer(r'add\(\s*"(\w+)"\s*,\s*([\w.]+)\.class', open(os.path.join(JAVA, PKG.replace(".", "/"), "actors/mobs/ModRoster.java"), encoding="utf-8").read()):
    ROSTER.setdefault(m.group(1), set()).add(m.group(2).split(".")[-1])
#Shattered's own monsters, the files in actors/mobs at the first commit of this game
import subprocess
SHATTERED_MOBS = {os.path.basename(f)[:-5] for f in subprocess.run(["git", "ls-tree", "-r", "--name-only", "0e810728",
    "core/src/main/java/" + PKG.replace(".", "/") + "/actors/mobs/"], cwd=ROOT, capture_output=True, text=True).stdout.split()
    if f.endswith(".java")}
#names a mod used for monsters that are in this game under another name
RENAMES = {}

#bosses never spawn as regular monsters, they crash outside their arenas (Deep's unfinished table lists Tengu on 12)
BOSSES = {"Goo", "Tengu", "DM300", "King", "DwarfKing", "Yog", "YogDzewa", "NewTengu", "NewDM300", "NewDwarfKing"}
#abstract in this game, picked at random when the rotation is made, as Shattered's own spawner does
RANDOM = {"Shaman", "Elemental"}

def resolve(mod, name):
    if name.endswith(".random"):
        return name if name[:-7] in RANDOM else resolve(mod, name[:-7])
    if name in BOSSES: return "BOSS"
    #older mods had one gnoll shaman and one fire elemental under these names
    if name == "Shaman": return "Shaman.random"
    if name == "Elemental": return PKG + ".actors.mobs.Elemental$FireElemental"
    name = RENAMES.get((mod, name), RENAMES.get((None, name), name))
    #another mod's monster with the same name is a different monster (Unleashed's air elemental is not Cursed's,
    #which hits for 60), so it counts as not in the game
    if name not in ROSTER.get(mod, ()) and name not in SHATTERED_MOBS and any(name in r for k, r in ROSTER.items() if k != mod):
        return None
    opts = OURS.get(name, [])
    if not opts: return None
    base = [o for o in opts if o.count(".") == PKG.count(".") + 3]  #directly in actors.mobs
    if name in ROSTER.get(mod, ()):
        #a mod's own monster, prefer the copy in a mod folder over Shattered's of the same name
        sub = [o for o in opts if o not in base]
        #a copy in the mod's own subpackage (actors.mobs.sps.FireElemental) beats a nested class of the same name elsewhere
        own = [o for o in sub if (".mobs." + mod + ".") in o]
        return (own or sub or opts)[0]
    return (base or opts)[0]

#first floor of each region (sewers, prison, caves, city, halls) for mods whose regions do not start where ours do,
#from each mod's Dungeon.bossLevel. Unleashed has six floor regions and a frozen region (25 to 29) before its halls
MOD_STARTS = {"unleashed": [1, 7, 13, 19, 31]}

#levels from a part of the mod's dungeon this game has no region for, keyed by level class, with the mod and the
#first floor of that part. Unleashed's frozen region, and Cursed's four challenge floors (sandbox special floors here)
WORLD_TABLES = {"FrozenCavesLevel": ("unleashed", 25), "WaterChallengeLevel": ("cursed", 31), "EarthChallengeLevel": ("cursed", 36),
                "FireChallengeLevel": ("cursed", 41), "AirChallengeLevel": ("cursed", 46)}

def mod_depth(key, depth):
    """The mod's floor at the same place in the same region as one of our floors."""
    if key == "ryuse": return depth + 5  #Ryuse's only world is its Polluted Sewers, its floors 6 to 9
    if key in WORLD_TABLES: return WORLD_TABLES[key][1] + min((depth - 1) % 5, 3)
    starts = MOD_STARTS.get(key)
    if not starts: return depth
    return starts[(depth - 1) // 5] + (depth - 1) % 5

#Ryuse works its rotation out from a formula per region, this is its polluted sewers rotation for floors 6 to 9
def _ryuse():
    out = {}
    for d in range(6, 10):
        counts = [("PollutedRat", 10 - d), ("Swarm", 9 - d), ("PollutedCrab", d - 6), ("Slime", d - 7), ("CausticSlime", d - 8)]
        out[d] = ("list", [(n, 1.0) for n, c in counts for _ in range(max(0, c))])
    return out
EXTRA = {"ryuse": _ryuse()}

def main():
    write = "--write" in sys.argv
    tables, missing = OrderedDict(), {}
    jobs = list(MODS.items()) + [(w, MODS[m]) for w, (m, _) in WORLD_TABLES.items()]
    for key, (folder, fname) in jobs:
        mod = WORLD_TABLES[key][0] if key in WORLD_TABLES else key
        path = find_file(folder, fname)
        if not path:
            print("%-14s no %s found" % (key, fname)); continue
        raw = parse(path)
        if mod in EXTRA: raw.update(EXTRA[mod])
        t = {}
        for d in range(1, 25):
            if d % 5 == 0: continue
            md = mod_depth(key, d)
            if md not in raw: continue
            kind, ents = raw[md]
            res = []
            for n, w in ents:
                c = resolve(mod, n)
                if c == "BOSS": continue
                if c: res.append((c, w))
                else: missing.setdefault(key, set()).add(n)
            if res: t[d] = (kind, res)
        tables[key] = t
        print("%-14s %-9s %2d floors  %s" % (key, (raw[min(raw)][0] if raw else "-"), len(t), os.path.relpath(path, SRC)))
    if "-v" in sys.argv:
        for key, t in tables.items():
            print("\n==", key)
            for d, (kind, ents) in t.items():
                print("  %2d (mod %2d) %s %s" % (d, mod_depth(key, d), kind[0], ", ".join("%s%s" % (c.split(".")[-1], "" if w == 1 else "x%g" % w) for c, w in ents)))
    for key, ns in missing.items():
        print("NOT IN GAME %-12s %s" % (key, ", ".join(sorted(ns))))
    if write: write_java(tables)

def write_java(tables):
    #one method per table, a single static block this size would pass Java's 64 KB method limit
    calls, methods, lines = [], [], []
    for key, t in tables.items():
        if not t: continue
        body = []
        for d, (kind, ents) in t.items():
            body.append('\t\tput( "%s", %d, %s, %s );' % (key, d, "true" if kind == "chance" else "false",
                ", ".join('"%s", %sf' % (c[len(PKG) + 1:] if c.startswith(PKG) else "actors.mobs." + c, ("%g" % w)) for c, w in ents)))
        calls.append("\t\t%s();" % key)
        methods.append("\tprivate static void %s(){\n%s\n\t}\n" % (key, "\n".join(body)))
        lines += body
    src = open(os.path.join(ROOT, "scripts/ModSpawns.java.in"), encoding="utf-8").read()
    src = src.replace("\t\t//TABLES\n", "\n".join(calls) + "\n").replace("\t//METHODS\n", "\n".join(methods))
    out = os.path.join(JAVA, PKG.replace(".", "/"), "actors/mobs/ModSpawns.java")
    open(out, "w", encoding="utf-8", newline="\n").write(src)
    print("wrote", os.path.relpath(out, ROOT), len(lines), "floor tables")

main()
