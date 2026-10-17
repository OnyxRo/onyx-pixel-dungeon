"""First step of the Unleashed port. Copies the chosen classes (any folder) from the mod, applies renames.py,
and for monsters swaps the base class to UMob (see UMob.java). Usage
  python -I scripts/port-unleashed-copy.py Name1 Name2 ...
Names are class names, sprites are copied along with each monster (<Name>Sprite).
"""
import sys, os, io, glob, importlib.util
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
MOD = os.path.expanduser('~/mod-sources/unleashed')
spec = importlib.util.spec_from_file_location('renames', os.path.join(ROOT, 'scripts', 'renames.py'))
renames = importlib.util.module_from_spec(spec); spec.loader.exec_module(renames)
MPK = renames.mod_package_dir(MOD)
PK = ROOT + '/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
def LP(p):
    p = os.path.abspath(p)
    return chr(92) * 2 + '?' + chr(92) + p if os.name == 'nt' else p
files = {}
for f in glob.glob(LP(MOD) + '/' + MPK + '**/*.java', recursive=True):
    files.setdefault(os.path.basename(f)[:-5], []).append(f)
for n in sys.argv[1:]:
    for name in (n, n + 'Sprite'):
        c = files.get(name, [])
        if len(c) != 1:
            print('skip', name, len(c)); continue
        rel = os.path.relpath(c[0], LP(MOD) + '/' + MPK).replace(os.sep, '/')
        dest = PK + rel
        if os.path.exists(dest):
            print('exists', rel); continue
        s = io.open(c[0], encoding='utf-8', errors='replace', newline='').read()
        s = renames.apply(s)[0]
        if rel.startswith('actors/mobs/') and name == n:
            s = s.replace('extends Mob ', 'extends UMob ').replace('extends Mob{', 'extends UMob {')
        os.makedirs(os.path.dirname(dest), exist_ok=True)
        io.open(dest, 'w', encoding='utf-8', newline='').write(s)
        print('copied', rel)
