"""For a set of talents, prints each mod code block that references them, grouped by file and enclosing method.
usage: dossier.py <tsv> <types comma list or ALL> [file filter] > out.txt"""
import re, os, sys
sys.stdout.reconfigure(encoding='utf-8')
MOD = os.path.expanduser('~/mod-sources/transformation/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon')
rows = [l.rstrip('\n').split('\t') for l in open(sys.argv[1], encoding='utf-8')]
types = sys.argv[2].split(',')
flt = sys.argv[3] if len(sys.argv) > 3 else ''
names = [r[1] for r in rows if types == ['ALL'] or r[0] in types]
pat = re.compile(r'\b(' + '|'.join(names) + r')\b')
meth = re.compile(r'^\s*(public|private|protected|static)[^;=]*\(([^)]*)\)\s*(throws [\w, ]+)?\s*\{?\s*$')
def block(lines, i):
    # expand from line i to the end of the statement starting there
    start = i
    depth = 0; j = i; seen = False
    while j < len(lines):
        for ch in lines[j]:
            if ch == '{': depth += 1; seen = True
            elif ch == '}': depth -= 1
        if (seen and depth <= 0) or (not seen and lines[j].rstrip().endswith(';')) or j - i > 40:
            break
        j += 1
    if depth < 0: pass
    return start, j
for d, _, fs in os.walk(MOD):
    for f in sorted(fs):
        if not f.endswith('.java') or flt and flt not in f: continue
        p = os.path.join(d, f)
        lines = open(p, encoding='utf-8', errors='replace').read().split('\n')
        out = []; last = -1; curm = '?'
        mlines = {}
        for i, l in enumerate(lines):
            if meth.match(l) and not l.strip().startswith(('if', 'for', 'while', 'switch', 'else', 'return', 'new ')): curm = l.strip()[:90]
            mlines[i] = curm
        for i, l in enumerate(lines):
            if i <= last: continue
            if f == 'Talent.java' and re.match(r'\s*[A-Z_0-9]+\(\d+,', l): continue
            if not pat.search(l): continue
            s, e = block(lines, i)
            out.append((mlines[i], s, e))
            last = e
        if not out: continue
        print('=' * 20, os.path.relpath(p, MOD).replace('\\', '/'))
        prevm = None
        for m, s, e in out:
            if m != prevm: print('  ## ' + m); prevm = m
            print('\n'.join('%5d %s' % (k + 1, lines[k].rstrip()[:200]) for k in range(s, e + 1)))
