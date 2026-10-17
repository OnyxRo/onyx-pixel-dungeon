"""Removes SPS classes that still fail to compile after scripts/sps-fix.py, so the rest can be built and checked.

  python -I scripts/sps-prune.py [log]       log defaults to build/fast.log

For every file with errors in the log
  - a monster loses its sprite class, its ModRoster line and the file itself
  - any other ported SPS class (item, buff, blob...) is deleted
Only files this branch added are ever deleted (git says which), never core classes.
Everything deleted is appended to build/pruned.txt with the first error, for PORTING.md.
Run it, then scripts/sps-cycle.sh --fix again, until the compile is clean."""
import re, os, io, sys, subprocess
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
log = sys.argv[1] if len(sys.argv) > 1 else 'build/fast.log'
PRE = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
base = subprocess.run(['git', 'merge-base', 'HEAD', 'onyx/finish-four-mods'], cwd=ROOT, capture_output=True, text=True).stdout.strip()
added = set(subprocess.run(['git', 'diff', '--name-only', '--diff-filter=A', base, 'HEAD'], cwd=ROOT, capture_output=True, text=True).stdout.split('\n'))
added |= set(subprocess.run(['git', 'ls-files', '--others', '--exclude-standard'], cwd=ROOT, capture_output=True, text=True).stdout.split('\n'))
t = io.open(os.path.join(ROOT, log), encoding='utf-8', errors='replace').read().split('\n')
first = {}
for i, l in enumerate(t):
    m = re.match(r'(.*\.java):(\d+): error: (.*)', l)
    if m:
        p = m.group(1).replace('\\', '/')
        k = p.find('core/src/main/java/')
        if k >= 0: first.setdefault(p[k:], m.group(3)[:80] + ' line ' + m.group(2))
gone = []
for p, why in first.items():
    if p not in added: continue
    full = os.path.join(ROOT, p)
    if not os.path.exists(full): continue
    name = os.path.basename(p)[:-5]
    rel = p[len(PRE):]
    if rel.startswith('actors/mobs/') and not rel.startswith('actors/mobs/ModRoster'):
        src = io.open(full, encoding='utf-8', errors='replace').read()
        m = re.search(r'spriteClass\s*=\s*(\w+)\.class', src)
        if m:
            sp = os.path.join(ROOT, PRE, 'sprites', m.group(1) + '.java')
            if os.path.exists(sp) and (PRE + 'sprites/' + m.group(1) + '.java') in added: os.remove(sp)
        rp = os.path.join(ROOT, PRE, 'actors/mobs/ModRoster.java')
        r = io.open(rp, encoding='utf-8', newline='').read()
        r2 = re.sub(r'[^\n]*\b' + name + r'\.class[^\n]*\n', '', r)
        if r2 != r: io.open(rp, 'w', encoding='utf-8', newline='').write(r2)
    os.remove(full)
    gone.append(name + ' | ' + why)
with io.open(os.path.join(ROOT, 'build/pruned.txt'), 'a', encoding='utf-8') as f:
    for g in gone: f.write(g + '\n')
print('pruned', len(gone))
