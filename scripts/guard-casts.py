"""Guards casts of the current floor to a boss's own arena, so the boss also works on any other floor (the sandbox).

  python -I scripts/guard-casts.py <ArenaClass> file1.java file2.java ... [--default=null]

Rewrites
    ((Arena) Dungeon.level).progress();            ->  if (Dungeon.level instanceof Arena) ((Arena) Dungeon.level).progress();
    x = ((Arena) Dungeon.level).pro();             ->  x = Dungeon.level instanceof Arena ? ((Arena) Dungeon.level).pro() : null;
The fallback value for reads is --default (null unless given, use -1 for cell positions).
A bare `level` (Magic Ling's static import of Dungeon.level) is handled the same way.
Prints what it could not rewrite, those casts need a hand fix.
"""
import re, sys

args = [a for a in sys.argv[1:] if not a.startswith('--')]
default = 'null'
for a in sys.argv[1:]:
    if a.startswith('--default='):
        default = a.split('=', 1)[1]
arena = args[0]
LV = r'(Dungeon\.level|level)'
CAST = r'\(\(\s*' + arena + r'\s*\)\s*' + LV + r'\s*\)'
stmt = re.compile(r'(?m)^([ \t]*)' + CAST + r'(\.\w+\([^;]*\));')
read = re.compile(r'=\s*' + CAST + r'(\.\w+(?:\([^;]*?\))?)\s*;')
for f in args[1:]:
    s = open(f, encoding='utf-8', newline='').read()
    n0 = s
    s = stmt.sub(lambda m: '%sif (%s instanceof %s) ((%s) %s)%s;' % (m.group(1), m.group(2), arena, arena, m.group(2), m.group(3)), s)
    s = read.sub(lambda m: '= %s instanceof %s ? ((%s) %s)%s : %s;' % (m.group(1), arena, arena, m.group(1), m.group(2), default), s)
    left = [l.strip() for l in s.splitlines() if re.search(CAST, l) and 'instanceof ' + arena not in l]
    if s != n0:
        open(f, 'w', encoding='utf-8', newline='').write(s)
    print('%s: %d changes%s' % (f.split('/')[-1].split('\\')[-1], sum(1 for _ in re.finditer('instanceof ' + arena, s)) - sum(1 for _ in re.finditer('instanceof ' + arena, n0)), ('' if not left else ', still to fix by hand:\n    ' + '\n    '.join(left))))
