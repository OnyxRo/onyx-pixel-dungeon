"""Unleashed PD has no message files, its text sits in the classes. This reads name and description() from each
mob class in the mod and appends actors.mobs.<name>.name and .desc to actors.properties.
  python -I scripts/port-unleashed-strings.py Mob1 Mob2 ...
"""
import sys, re, io, os
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
MOD = os.path.expanduser('~/mod-sources/unleashed/src/main/java/com/shatteredpixel/pixeldungeonunleashed/actors/mobs/')
props = ROOT + '/core/src/main/assets/messages/actors/actors.properties'
def LP(p):
    return chr(92) * 2 + '?' + chr(92) + os.path.abspath(p) if os.name == 'nt' else p
text = io.open(LP(props), encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in text else '\n'
add = []
for n in sys.argv[1:]:
    src = io.open(LP(MOD + n + '.java'), encoding='utf-8', errors='replace').read()
    m = re.search(r'\bname\s*=\s*"([^"]*)"', src)
    name = m.group(1) if m else re.sub(r'(?<=[a-z])(?=[A-Z])', ' ', n).lower()
    d = re.search(r'String\s+description\(\s*\)\s*\{\s*return\s*(.*?);\s*\}', src, re.S)
    desc = ''
    if d:
        desc = ''.join(re.findall(r'"((?:[^"\\]|\\.)*)"', d.group(1)))
    if not desc:
        d = re.search(r'TXT_DESC\w*\s*=\s*((?:"(?:[^"\\]|\\.)*"\s*\+?\s*)+);', src)
        if d: desc = ''.join(re.findall(r'"((?:[^"\\]|\\.)*)"', d.group(1)))
    if not desc: desc = 'The %s, from Unleashed Pixel Dungeon. The mod gave it no description.' % name
    desc = desc.replace('\\n', '\\n')
    key = 'actors.mobs.' + n.lower()
    if key + '.name=' in text: continue
    add.append('%s.name=%s' % (key, name))
    add.append('%s.desc=%s' % (key, desc))
if add:
    io.open(LP(props), 'w', encoding='utf-8', newline='').write(text.rstrip('\r\n') + nl + nl.join(add) + nl)
print('added', len(add) // 2)
