"""Removes the @Override that javac flags with "method does not override or implement a method from a supertype".
A mod method that no longer overrides anything (a hook the mod added to a core class) stays as a plain method.
Read the list it prints, a method that should have been wired into a core hook needs a shim instead.
  python -I scripts/drop-override.py build/fj.log
"""
import re, sys, io, os
pat = re.compile(r'^(.*\.java):(\d+): error: method does not override or implement a method from a supertype')
hits = {}
for l in io.open(sys.argv[1], encoding='utf-8', errors='replace'):
    m = pat.match(l.strip())
    if m: hits.setdefault(m.group(1), set()).add(int(m.group(2)))
for f, lines in hits.items():
    s = io.open(f, encoding='utf-8', newline='').read()
    rows = s.split('\n')
    for ln in sorted(lines, reverse=True):
        for k in (ln - 1, ln - 2):
            if 0 <= k < len(rows) and '@Override' in rows[k]:
                print('%s:%d %s' % (os.path.basename(f), ln, rows[k + 1].strip()[:80] if k + 1 < len(rows) else ''))
                rows[k] = rows[k].replace('@Override', '', 1)
                if not rows[k].strip(): rows[k] = rows[k].rstrip(' \t')
                break
    io.open(f, 'w', encoding='utf-8', newline='').write('\n'.join(rows))
