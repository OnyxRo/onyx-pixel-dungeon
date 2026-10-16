"""Builds the Tower PD world tile sheets in assets/environment/worlds/.
Cells (16x16) that are empty in the mod sheet but drawn in Shattered's sheet for the same stage are filled
from Shattered's, as CLAUDE.md asks for every world sheet.
Run from the repo root, python -I scripts/tower-tiles.py"""
import os
from PIL import Image
SRC = os.path.expanduser('~/mod-sources/tower/pixel-towers-master/core/src/main/assets/environment/')
OUT = 'core/src/main/assets/environment/'
# mod sheet, our file name, Shattered sheet of the same stage
SHEETS = [
    ('sewers', 'towersewers', 'sewers'), ('sewers_oozy', 'toweroozysewers', 'sewers'),
    ('prison', 'towerprison', 'prison'), ('temple', 'towertemple', 'prison'),
    ('caves', 'towercaves', 'caves'), ('gobs', 'towergoblingrotto', 'caves'), ('mines', 'towermines', 'caves'),
    ('city', 'towercity', 'city'), ('gates', 'towergates', 'city'), ('mages', 'towermages', 'city'),
    ('halls', 'towerhalls', 'halls'), ('souldesert', 'towersouldesert', 'halls'),
]
def empty(im, x, y):
    return im.crop((x, y, x + 16, y + 16)).getchannel('A').getbbox() is None
for mod, ours, ref in SHEETS:
    a = Image.open(SRC + 'tiles_%s.png' % mod).convert('RGBA')
    r = Image.open(OUT + 'tiles_%s.png' % ref).convert('RGBA')
    n = 0
    for y in range(0, 256, 16):
        for x in range(0, 256, 16):
            if empty(a, x, y) and not empty(r, x, y):
                a.paste(r.crop((x, y, x + 16, y + 16)), (x, y)); n += 1
    a.save(OUT + 'worlds/tiles_%s.png' % ours)
    print(ours, 'filled', n)
