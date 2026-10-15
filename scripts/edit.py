"""Exact text edits that keep each file's line endings. Use this instead of shell heredocs or sed.

  python -I scripts/edit.py spec.txt [--dry]

The spec file is plain text, so it never needs backslash escaping. Write it with the Write tool.

  @@ core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/Dungeon.java
  <<<<
  old text, any number of lines, tabs kept as typed
  ====
  new text
  >>>>
  <<<<
  second edit in the same file
  ====
  its replacement
  >>>>
  @@ another/file.java
  ...

Rules
  - Every old text must appear exactly once, or the whole run stops before anything is written.
  - Lines are matched with their own endings, so LF specs work on CRLF files and the endings are kept.
  - Add the line  ++ all  right after <<<< to replace every occurrence instead.
  - A file named in the spec that does not exist stops the run too.
  - To insert, repeat an anchor line in both old and new. To delete, leave the new text empty.
"""
import sys, os, io, re

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

def read(path):
    return io.open(path, encoding='utf-8', newline='').read()

def parse(spec):
    files = []        # (path, [(old, new, all)])
    cur = None
    mode = None
    old = new = None
    every = False
    for raw in spec.replace('\r\n', '\n').split('\n'):
        if mode is None:
            if raw.startswith('@@ '):
                cur = (raw[3:].strip(), [])
                files.append(cur)
            elif raw.strip() == '<<<<':
                if cur is None: raise SystemExit('edit before any @@ file line')
                mode, old, new, every = 'old', [], [], False
            continue
        if mode == 'old':
            if raw.strip() == '++ all' and not old: every = True
            elif raw.strip() == '====': mode = 'new'
            else: old.append(raw)
        elif mode == 'new':
            if raw.strip() == '>>>>':
                cur[1].append(('\n'.join(old), '\n'.join(new), every))
                mode = None
            else: new.append(raw)
    if mode is not None: raise SystemExit('a <<<< block was never closed with >>>>')
    return files

def fuzzy_replace(t, o, n):
    """Replaces the one block of lines that equals o when trailing whitespace is ignored, or returns None."""
    lines = t.split('\n')
    old = [x.rstrip() for x in o.split('\n')]
    k = len(old)
    hits = [i for i in range(len(lines) - k + 1) if [x.rstrip() for x in lines[i:i + k]] == old]
    if len(hits) != 1: return None
    i = hits[0]
    return '\n'.join(lines[:i] + n.split('\n') + lines[i + k:])

def main():
    args = [a for a in sys.argv[1:] if not a.startswith('--')]
    dry = '--dry' in sys.argv
    if not args: raise SystemExit(__doc__)
    files = parse(read(args[0]))
    results = []
    for rel, edits in files:
        path = rel if os.path.isabs(rel) else os.path.join(ROOT, rel)
        if not os.path.exists(path): raise SystemExit('no such file: ' + rel)
        text = read(path)
        crlf = text.count('\r\n') > text.count('\n') / 2
        t = text.replace('\r\n', '\n')
        for i, (o, n, every) in enumerate(edits):
            c = t.count(o)
            if c == 0:
                # a block that differs only in trailing spaces, or in blank lines that hold spaces, still counts if it is unique
                fixed = fuzzy_replace(t, o, n)
                if fixed is None: raise SystemExit('%s edit %d: old text not found:\n%s' % (rel, i + 1, o[:300]))
                t = fixed
                continue
            if c > 1 and not every: raise SystemExit('%s edit %d: old text appears %d times, add more lines or ++ all' % (rel, i + 1, c))
            t = t.replace(o, n)
        results.append((path, t.replace('\n', '\r\n') if crlf else t, len(edits)))
    if dry:
        print('ok, %d files, %d edits (dry run)' % (len(results), sum(r[2] for r in results))); return
    for path, t, n in results:
        io.open(path, 'w', encoding='utf-8', newline='').write(t)
    print('edited %d files, %d edits' % (len(results), sum(r[2] for r in results)))

main()
