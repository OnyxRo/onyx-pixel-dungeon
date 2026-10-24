"""Adds rename rows to scripts/renames.py from a text file, without the shell touching backslashes.

  python -I scripts/add-renames.py rows.txt [--before="('Magic Ling blue potion icon'"]

rows.txt holds one Python tuple per line, written exactly as in renames.py, for example
    ('Magic Ling sign tile', r'\bSIGN_SP\b', 'EMPTY_DECO'),
Rows whose label is already in renames.py are skipped. The file is checked to still import afterwards.
"""
import importlib.util, sys

args = [a for a in sys.argv[1:] if not a.startswith('--')]
before = "    ('Magic Ling blue potion icon'"
for a in sys.argv[1:]:
    if a.startswith('--before='):
        before = a.split('=', 1)[1]
p = 'scripts/renames.py'
s = open(p, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in s else '\n'
rows = [r.rstrip() for r in open(args[0], encoding='utf-8').read().splitlines() if r.strip()]
new = []
for r in rows:
    label = r.split("'")[1]
    if ("('" + label + "'") in s:
        continue
    new.append('    ' + r.strip())
if new:
    if before not in s:
        sys.exit('anchor not found: ' + before)
    s2 = s.replace(before, nl.join(new) + nl + before, 1)
    open(p, 'w', encoding='utf-8', newline='').write(s2)
    try:
        spec = importlib.util.spec_from_file_location('r', p)
        spec.loader.exec_module(importlib.util.module_from_spec(spec))
    except Exception as e:
        open(p, 'w', encoding='utf-8', newline='').write(s)
        sys.exit('renames.py broke, change undone: %s' % e)
print('added', len(new), 'skipped', len(rows) - len(new))
