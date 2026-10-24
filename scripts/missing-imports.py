"""List what a mod class needs that this game lacks, before copying it.

python -I scripts/missing-imports.py <mod key> Class1 Class2 ... [--depth=2]

For each class, walks its imports (and same package names it uses) and prints
the classes this game has no file for, nested by depth. A boss, level or window
deep in the list means the copy will drag that whole system in.
"""
import os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OURS = os.path.join(ROOT, 'core', 'src', 'main', 'java')
PKG = 'com/shatteredpixel/shatteredpixeldungeon'

def index(base):
    out = {}
    for d, _, files in os.walk(base):
        for f in files:
            if f.endswith('.java'):
                out.setdefault(f[:-5], []).append(os.path.join(d, f))
    return out

def main():
    args = [a for a in sys.argv[1:] if not a.startswith('--')]
    depth = 2
    for a in sys.argv[1:]:
        if a.startswith('--depth='):
            depth = int(a.split('=')[1])
    mod = os.path.expanduser('~/mod-sources/' + args[0])
    modsrc = None
    for d, dirs, _ in os.walk(mod):
        if d.replace('\\', '/').endswith('src/main/java') and 'core' in d:
            modsrc = d
            break
    ours = index(OURS)
    theirs = index('\\\\?\\' + os.path.abspath(modsrc) if os.name == 'nt' else modsrc)
    seen = set()

    def deps(name):
        paths = theirs.get(name)
        if not paths:
            return []
        src = open(paths[0], encoding='utf-8', errors='replace').read()
        names = set(re.findall(r'^import\s+com\.shatteredpixel\.shatteredpixeldungeon\.[\w.]*?\.?(\w+);', src, re.M))
        here = os.path.dirname(paths[0])
        for f in os.listdir(here):
            n = f[:-5]
            if f.endswith('.java') and n != name and re.search(r'\b' + n + r'\b', src):
                names.add(n)
        return sorted(n for n in names if n not in ours and n in theirs)

    def walk(name, lvl):
        for n in deps(name):
            mark = '' if n not in seen else ' (seen)'
            print('  ' * lvl + '- ' + n + mark)
            if n not in seen:
                seen.add(n)
                if lvl < depth:
                    walk(n, lvl + 1)

    for c in args[1:]:
        print(c + ('' if c in theirs else ' (not in mod)'))
        seen.add(c)
        walk(c, 1)

main()
