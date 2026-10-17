"""port-item.py writes placeholder text into items.properties for every copied class, including abilities, buffs
and windows whose real text lives in other bundles. This moves those lines to the bundle that matches the key
(actors.* to actors.properties and so on), unless that bundle already has the key. Run it after port-item.py.
  python -I scripts/drop-placeholders.py
  python -I scripts/drop-placeholders.py --restore-from-head    puts back lines a previous version of this tool deleted
"""
import io
import re
import subprocess
import sys

M = 'core/src/main/assets/messages/'
SRC = M + 'items/items.properties'
BUNDLES = {'actors': M + 'actors/actors.properties', 'windows': M + 'windows/windows.properties',
           'levels': M + 'levels/levels.properties', 'ui': M + 'ui/ui.properties'}


def rd(p):
    return io.open(p, encoding='utf-8', newline='').read()


def keyof(l):
    m = re.match(r'([^#=\s][^=]*)=', l)
    return m.group(1) if m else None


if '--restore-from-head' in sys.argv:
    head = subprocess.run(['git', 'show', 'HEAD:' + SRC], capture_output=True, text=True, encoding='utf-8', errors='replace').stdout
    lines = [l.rstrip('\r') for l in head.split('\n')]
    now = rd(SRC)
else:
    lines = [l.rstrip('\r') for l in rd(SRC).split('\n')]
    now = None
moved = {b: [] for b in BUNDLES}
keep = []
for l in lines:
    k = keyof(l)
    if k and k.split('.')[0] in BUNDLES and 'from transformation. The mod gave it no description' in l or (k and k.split('.')[0] in BUNDLES and k.endswith('.name') and False):
        moved[k.split('.')[0]].append(l)
    else:
        keep.append(l)
    if k and k.split('.')[0] in BUNDLES and k.endswith('.name') and False:
        pass
# also move the matching .name line that goes with each moved .desc placeholder
descs = {keyof(l)[:-5] for b in moved for l in moved[b] if keyof(l).endswith('.desc')}
keep2 = []
for l in keep:
    k = keyof(l)
    if k and k.split('.')[0] in BUNDLES and k.endswith('.name') and k[:-5] in descs:
        moved[k.split('.')[0]].append(l)
    else:
        keep2.append(l)
total = 0
for b, add in moved.items():
    if not add:
        continue
    s = rd(BUNDLES[b])
    nl = '\r\n' if '\r\n' in s else '\n'
    have = set(re.findall(r'^([^#=\s][^=]*)=', s, flags=re.M))
    add = [l for l in add if keyof(l) not in have]
    if add:
        s = s.rstrip('\r\n') + nl + '###placeholders moved from items.properties' + nl + nl.join(add) + nl
        io.open(BUNDLES[b], 'w', encoding='utf-8', newline='').write(s)
        total += len(add)
if now is None:
    io.open(SRC, 'w', encoding='utf-8', newline='').write('\n'.join(keep2))
print('moved', total, 'placeholder lines')
