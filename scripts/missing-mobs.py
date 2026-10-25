"""Lists every monster class in a mod (anything that extends Mob, directly or through other classes) that this game lacks.

  python -I scripts/missing-mobs.py <mod key>

mod-report.py can miss monsters (Magic Ling's Fire Dragon), this walks the class tree instead.
A class counts as present when this game has a file with the same simple name anywhere.
Prints one line per missing monster with its path in the mod and its line count.
"""
import os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
mod = os.path.expanduser('~/mod-sources/' + sys.argv[1])
src = None
for d, dirs, _ in os.walk(mod):
    if d.replace('\\', '/').endswith('core/src/main/java'):
        src = d
        break
ours = set()
ourpaths = {}
OURSRC = os.path.join(ROOT, 'core', 'src', 'main', 'java')
for d, _, files in os.walk(OURSRC):
    for f in files:
        if f.endswith('.java'):
            ours.add(f[:-5])
            ourpaths.setdefault(f[:-5], []).append(os.path.relpath(os.path.join(d, f), OURSRC).replace('\\', '/'))

parent, path, abstract, commented = {}, {}, set(), set()
for d, _, files in os.walk(src):
    for f in files:
        if not f.endswith('.java'):
            continue
        p = os.path.join(d, f)
        s = open(p, encoding='utf-8', errors='replace').read()
        for m in re.finditer(r'^(?!\s*//)\s*(?:public\s+)?(?:static\s+)?(abstract\s+)?class\s+(\w+)\s+extends\s+([\w.]+)', s, re.M):
            name = m.group(2)
            parent[name] = m.group(3).split('.')[-1]
            path.setdefault(name, os.path.relpath(p, src))
            if m.group(1):
                abstract.add(name)
        if f[:-5] not in parent and re.search(r'^\s*//.*class\s+' + f[:-5] + r'\b', s, re.M):
            commented.add(f[:-5])

def is_mob(n, seen=()):
    if n in ('Mob', 'Boss', 'NPC', 'NTNPC'):
        return True
    if n in seen or n not in parent:
        return False
    return is_mob(parent[n], seen + (n,))

missing = sorted(n for n in parent if is_mob(n) and n not in ours and n not in abstract and n not in commented
                 and os.path.basename(path[n])[:-5] == n)
for n in missing:
    p = os.path.join(src, path[n])
    print('%-28s %5d  %s' % (n, sum(1 for _ in open(p, encoding='utf-8', errors='replace')), path[n].replace('\\', '/')))
print('missing monsters:', len(missing))

#monsters skipped because this game already has a class of that name somewhere else (another mod's Fire Dragon).
#port them with port-prefix.py
clashes = sorted(n for n in parent if is_mob(n) and n in ours and n not in abstract and os.path.basename(path[n])[:-5] == n
                 and path[n].replace('\\', '/') not in ourpaths[n])
for n in clashes:
    print('%-28s same name as ours, mod %s | ours %s' % (n, path[n].replace('\\', '/').split('shatteredpixeldungeon/')[-1],
          ' '.join(p.split('shatteredpixeldungeon/')[-1] for p in ourpaths[n])))
print('name clashes:', len(clashes))
