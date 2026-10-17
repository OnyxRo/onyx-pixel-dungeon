"""Writes a plain name and description for every new monster class that has no text.

  python -I scripts/port-placeholders.py <mod name> [since commit, default HEAD~1]

Looks at java files under actors/mobs that are new (untracked or added since the commit), and
for each class with no actors.<path>.name key in actors.properties, appends a name made from the
class name and a description that says the mod gave it none. port-mob.py does this for the classes
it was asked about, this one covers everything a copier brought along.
"""
import sys, os, io, re, subprocess
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
MODNAME = sys.argv[1]
SINCE = sys.argv[2] if len(sys.argv) > 2 else 'HEAD~1'
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
props = ROOT + '/core/src/main/assets/messages/actors/actors.properties'
s = io.open(props, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in s else '\n'
files = set()
st = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
for l in st.splitlines():
    if l.startswith('??'): files.add(l[3:].strip())
d = subprocess.run(['git', 'diff', '--name-only', '--diff-filter=A', SINCE], cwd=ROOT, capture_output=True, text=True).stdout
files |= {l.strip() for l in d.splitlines()}
add = []
for f in sorted(files):
    if not f.startswith(PK + 'actors/mobs/') or not f.endswith('.java'): continue
    src = io.open(ROOT + '/' + f, encoding='utf-8', errors='replace').read()
    name = os.path.basename(f)[:-5]
    if not re.search(r'\bclass\s+' + name + r'\b[^{]*\bextends\s+\w+', src): continue
    if 'Sprite' in name or name.endswith('Sprites'): continue
    key = f[len(PK):-5].replace('/', '.').lower()
    if re.search(r'^' + re.escape(key) + r'\.name=', s, flags=re.M): continue
    words = re.sub(r'(?<=[a-z0-9])(?=[A-Z])', ' ', name.replace('_', ' ')).lower()
    add.append('%s.name=%s' % (key, words))
    add.append('%s.desc=The %s, from %s. The mod gave it no description.' % (key, words, MODNAME))
if add:
    s = s.rstrip('\r\n') + nl + nl + '###placeholders for ' + MODNAME + nl + nl.join(add) + nl
    io.open(props, 'w', encoding='utf-8', newline='').write(s)
print('placeholder texts written for', len(add) // 2, 'classes')
