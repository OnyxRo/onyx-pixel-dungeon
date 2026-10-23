"""Builds the Blue Archive PD world sheet (its white and red caves) in assets/environment/worlds/.
Cells that are empty in the mod sheet but drawn in Shattered's caves sheet are filled from Shattered's, as CLAUDE.md asks.
Also copies the icon for the mod logo. Run from the repo root, python -I scripts/bluearchive-tiles.py"""
import os, hashlib
from PIL import Image
SRC = os.path.expanduser('~/mod-sources/bluearchive/')
OUT = 'core/src/main/assets/'
def empty(im, x, y):
    return im.crop((x, y, x + 16, y + 16)).getchannel('A').getbbox() is None
a = Image.open(SRC + 'core/src/main/assets/environment/tiles_caves2.png').convert('RGBA')
r = Image.open(OUT + 'environment/tiles_caves.png').convert('RGBA')
n = 0
for y in range(0, 256, 16):
    for x in range(0, 256, 16):
        if empty(a, x, y) and not empty(r, x, y):
            a.paste(r.crop((x, y, x + 16, y + 16)), (x, y)); n += 1
a.save(OUT + 'environment/worlds/tiles_bluearchivecaves.png')
print('tiles filled', n)
#the mod's caves water, only kept when it differs from Shattered's
w = open(SRC + 'core/src/main/assets/environment/water2.png', 'rb').read()
if hashlib.md5(w).digest() != hashlib.md5(open(OUT + 'environment/water2.png', 'rb').read()).digest():
    open(OUT + 'environment/worlds/water_bluearchivecaves.png', 'wb').write(w)
    print('water copied')
Image.open(SRC + 'desktop/src/main/assets/icons/icon_32.png').convert('RGBA').save(OUT + 'interfaces/mods/bluearchive.png')
#icon sheets (drawn through the 30000 offset), student sprites and splashes
import shutil
MA = SRC + 'core/src/main/assets/'
for d in ('interfaces/bluearchive', 'sprites/bluearchive', 'splashes/bluearchive'):
    os.makedirs(OUT + d, exist_ok=True)
for f in ('talent_icons', 'hero_icons', 'buffs', 'large_buffs'):
    shutil.copyfile(MA + 'interfaces/%s.png' % f, OUT + 'interfaces/bluearchive/%s.png' % f)
for s in ('aris', 'nonomi', 'miyako', 'hoshino', 'shiroko', 'noa', 'miyu', 'yuzu', 'izuna'):
    shutil.copyfile(MA + 'sprites/%s.png' % s, OUT + 'sprites/bluearchive/%s.png' % s)
    shutil.copyfile(MA + 'splashes/%s.jpg' % s, OUT + 'splashes/bluearchive/%s.jpg' % s)
print('icons, sprites and splashes copied')
#the sniper shot ray the mod added to its effects sheet (Effects.Type.SHOT_RAY), on its own file here
Image.open(MA + 'effects/effects.png').convert('RGBA').crop((16, 37, 32, 45)).save(OUT + 'effects/bluearchive_shotray.png')

#item pictures. The mod's rows 33 to 39 (one based) go to rows 256 to 262 of ours, its nine student armors
#(cells 6 to 14 of its armor row 12) to row 263. Writes the ItemSpriteSheet constants to build/ba-itemsheet.java
import re
ISS = open(SRC + 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/sprites/ItemSpriteSheet.java', encoding='utf-8').read()
bases = {m.group(1): (int(m.group(2)), int(m.group(3))) for m in re.finditer(r'private static final int (\w+)\s*=\s*xy\((\d+),\s*(\d+)\)', ISS)}
consts = re.findall(r'public static final int (\w+)\s*=\s*(\w+)\s*\+\s*(\d+)\s*;', ISS)
rects = {m.group(1): (int(m.group(2)), int(m.group(3))) for m in re.finditer(r'assignItemRect\(\s*(\w+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)', ISS)}
mod_sheet = Image.open(MA + 'sprites/items.png').convert('RGBA')
ours = Image.open(OUT + 'sprites/items.png').convert('RGBA')
java = ['\t//bluearchive, Blue Archive PD items, rows 256 to 263 of items.png']
n = 0
for name, base, k in consts:
    if base not in bases: continue
    bx, by = bases[base]
    idx = (bx - 1) + (by - 1) * 16 + int(k)
    x, y = idx % 16, idx // 16 + 1          #one based row
    if base == 'ARMOR' and 6 <= x + 1 <= 14 and name.startswith('ARMOR_'):
        ny = 263
    elif 33 <= y <= 39:
        ny = 256 + (y - 33)
    else:
        continue
    cell = mod_sheet.crop((x * 16, (y - 1) * 16, x * 16 + 16, y * 16))
    ours.paste(cell, (x * 16, (ny - 1) * 16))
    java.append('\tpublic static final int BA_%s = xy(%d, %d);' % (name, x + 1, ny))
    if name in rects:
        java.append('\tstatic{ assignItemRect(BA_%s, %d, %d); }' % (name, rects[name][0], rects[name][1]))
    n += 1
ours.save(OUT + 'sprites/items.png')
os.makedirs('build', exist_ok=True)
open('build/ba-itemsheet.java', 'w', encoding='utf-8').write('\n'.join(java) + '\n')
print('item pictures', n)
