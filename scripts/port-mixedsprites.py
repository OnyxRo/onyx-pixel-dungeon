"""Copies item pictures for ItemSpriteSheet constants that the new files name but this game lacks,
including mixed case names (Xun's PD writes ItemSpriteSheet.Bayonet), which port-item.py misses.

  ITEM_ROWS=a-b ITEM_ANCHOR=NAME python -I scripts/port-mixedsprites.py <mod key> [files...]

Without files it looks at every new (untracked) java file. A mixed case name is copied as UPPER_SNAKE,
with XUN_ style prefix (PREFIX env, default the mod key) when this game already has that name, and the
reference in the file is rewritten to match.
"""
import sys, os, re, io, subprocess
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
mod = sys.argv[1]
MOD = os.path.join(os.path.expanduser('~/mod-sources'), mod)
files = sys.argv[2:]
if not files:
    out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
    files = [l[3:].strip() for l in out.split('\n') if l.startswith('??') and l.strip().endswith('.java')]
sheet = io.open(os.path.join(ROOT, PK, 'sprites/ItemSpriteSheet.java'), encoding='utf-8').read()
ours = set(re.findall(r'int\s+(\w+)\s*=', sheet))
prefix = os.environ.get('PREFIX', mod.upper())
def snake(n):
    return re.sub(r'(?<=[a-z0-9])(?=[A-Z])', '_', n).upper()
need = {}
for f in files:
    t = io.open(os.path.join(ROOT, f), encoding='utf-8').read()
    for m in re.finditer(r'ItemSpriteSheet\.(\w+)', t):
        n = m.group(1)
        if n in ours or n in need or n == 'Icons': continue
        if n == n.upper():
            need[n] = n
        else:
            a = snake(n)
            if a in ours: a = prefix + '_' + a
            need[n] = a
if not need:
    print('nothing to copy'); sys.exit(0)
specs = [k if k == v else k + '=' + v for k, v in need.items()]
r = subprocess.run([sys.executable, '-I', os.path.join(ROOT, 'scripts/port-itemsprites.py'), ROOT, MOD] + specs, capture_output=True, text=True)
print(r.stdout.strip()[-3000:], r.stderr.strip()[-2000:])
sheet = io.open(os.path.join(ROOT, PK, 'sprites/ItemSpriteSheet.java'), encoding='utf-8').read()
done = set(re.findall(r'int\s+(\w+)\s*=', sheet))
for f in files:
    p = os.path.join(ROOT, f)
    t = io.open(p, encoding='utf-8', newline='').read()
    u = t
    for k, v in need.items():
        if k != v and v in done:
            u = re.sub(r'ItemSpriteSheet\.' + k + r'\b', 'ItemSpriteSheet.' + v, u)
    if u != t: io.open(p, 'w', encoding='utf-8', newline='').write(u)
print('renamed', sum(1 for k, v in need.items() if k != v and v in done), 'mixed case names')
