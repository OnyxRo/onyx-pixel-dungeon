# Rebuilds Cthulhu's sprite sheets from the animation page in art-for-design/cthulhu-animations.
# Run it after the page changes:  python -I scripts/cthulhu-art/build.py
# It runs the page's own drawing code in node (export.js), draws every frame on black and on white to get
# the transparency, crops and dedupes the frames (assemble.py), writes the sheets to assets/sprites/cthulhu
# and regenerates sprites/CthulhuArt.java with the positions and frame orders.
import json, os, re, subprocess, sys
from PIL import Image

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.normpath(os.path.join(HERE, '..', '..'))
PAGE = os.path.join(ROOT, 'art-for-design', 'cthulhu-animations')
WORK = os.path.join(ROOT, 'build', 'cthulhu-art')
DEST = os.path.join(ROOT, 'core', 'src', 'main', 'assets', 'sprites', 'cthulhu')
JAVA = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon', 'sprites', 'CthulhuArt.java')

for d in ('raw', 'out'):
    os.makedirs(os.path.join(WORK, d), exist_ok=True)
os.makedirs(DEST, exist_ok=True)

for n in ['tiles_halls_x4', 'pillar', 'pit_alt_52', 'warrior', 'target', 'boss_hp', 'large_buffs']:
    im = Image.open(os.path.join(PAGE, 'art', n + '.png')).convert('RGBA')
    open(os.path.join(WORK, 'raw', n + '.rgba'), 'wb').write(im.tobytes())
    json.dump(im.size, open(os.path.join(WORK, 'raw', n + '.json'), 'w'))

html = open(os.path.join(PAGE, 'Cthulhu Animations.dc.html'), encoding='utf8').read()
js = re.search(r'<script type="text/x-dc" data-dc-script>\n(.*?)</script>', html, re.S).group(1)
open(os.path.join(WORK, 'page.js'), 'w', encoding='utf8').write(js)

subprocess.run(['node', os.path.join(HERE, 'export.js'), WORK], check=True)
subprocess.run([sys.executable, '-I', os.path.join(HERE, 'assemble.py'), WORK, DEST], check=True)

out = os.path.join(WORK, 'out')
x, y, w, h = json.load(open(os.path.join(out, 'pit.json')))
Image.frombytes('RGBA', (w, h), open(os.path.join(out, 'pit.rgba'), 'rb').read()).save(os.path.join(DEST, 'pit.png'), optimize=True)

info = json.load(open(os.path.join(out, 'sheets.json')))
meta = json.load(open(os.path.join(out, 'meta.json')))
L = ['package com.shatteredpixel.shatteredpixeldungeon.sprites;', '',
     '//Made by scripts/cthulhu-art/build.py from the Cthulhu animation page (art-for-design/cthulhu-animations), do not edit by hand.',
     '//Positions are world pixels on the final arena, frames play at 12 a second.', 'public class CthulhuArt {', '']
for g, v in info.items():
    G = g.upper()
    L.append(f'\tpublic static final int {G}_X = {v["x"]}, {G}_Y = {v["y"]}, {G}_W = {v["w"]}, {G}_H = {v["h"]};')
    for n, a in v['anims'].items():
        L.append(f'\tpublic static final int[] {n.upper()} = {{{",".join(map(str, a["frames"]))}}};')
    L.append('')
L.append(f'\tpublic static final int PIT_X = {x}, PIT_Y = {y};')
ex, ey = meta['eyeAt']
L.append(f'\tpublic static final float EYE_X = {ex:.1f}f, EYE_Y = {ey:.1f}f;')
L.append('\t//the floor tentacles, the hole centre of each in world pixels')
L.append('\tpublic static final int[][] TENTACLES = {' + ','.join('{%d,%d}' % (u[0], int(u[1])) for u in meta['units']) + '};')
L.append('	//the four slamming tentacles, lower left, lower right, upper left, upper right. Base x, base y, side, length, tip x, tip y')
L.append('	public static final int[][] SLAMS = {' + ','.join('{%d,%d,%d,%d,%d,%d}' % tuple(int(round(v)) for v in q) for q in meta['slams']) + '};')
sx, sy = meta['spot0']; gx, gy = meta['grasp']; bx, by = meta['bar']
L.append('\t//the strike and the void hole are drawn from the hole centre, cell x*16+8 and y*16+14')
L.append(f'\tpublic static final int STRIKE_DX = STRIKE_X - {sx}, STRIKE_DY = STRIKE_Y - {sy};')
L.append('\t//the grasp is drawn from the hero, cell x*16+8 and y*16+9')
L.append(f'\tpublic static final int GRASP_DX = GRASP_X - {gx}, GRASP_DY = GRASP_Y - {gy};')
L.append('\t//the cleanse is drawn from the top left of the large boss bar')
L.append(f'\tpublic static final int CLEANSE_DX = CLEANSE_X - {bx}, CLEANSE_DY = CLEANSE_Y - {by};')
L.append('\t//the frame of the cleanse where the debuff shatters')
L.append('\tpublic static final int CLEANSE_BREAK = 23;')
L.append('}')
open(JAVA, 'w', encoding='utf8', newline='\n').write('\n'.join(L) + '\n')
print('sheets written to', DEST)
