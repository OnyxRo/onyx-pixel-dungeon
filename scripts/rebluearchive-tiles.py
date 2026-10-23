"""Builds the Re Blue Archive PD world tile sheets and water in assets/environment/worlds/.
The mod redrew Shattered's sewers, prison and caves sheets in place (its city and halls are Shattered's own).
Cells that are empty in the mod sheet but drawn in Shattered's sheet for the same stage are filled
from Shattered's, as CLAUDE.md asks for every world sheet.
Run from the repo root, python -I scripts/rebluearchive-tiles.py"""
import os
from PIL import Image
SRC = os.path.expanduser('~/mod-sources/rebluearchive/core/src/main/assets/environment/')
OUT = 'core/src/main/assets/environment/'
# mod sheet, our file name, Shattered sheet of the same stage, mod water
SHEETS = [
    ('sewers', 'rbasewers', 'sewers', 'water0'),
    ('prison', 'rbaprison', 'prison', 'water1'),
    ('caves', 'rbacaves', 'caves', 'water2'),
]
def empty(im, x, y):
    return im.crop((x, y, x + 16, y + 16)).getchannel('A').getbbox() is None
for mod, ours, ref, water in SHEETS:
    a = Image.open(SRC + 'tiles_%s.png' % mod).convert('RGBA')
    r = Image.open(OUT + 'tiles_%s.png' % ref).convert('RGBA')
    n = 0
    for y in range(0, 256, 16):
        for x in range(0, 256, 16):
            if empty(a, x, y) and not empty(r, x, y):
                a.paste(r.crop((x, y, x + 16, y + 16)), (x, y)); n += 1
    a.save(OUT + 'worlds/tiles_%s.png' % ours)
    Image.open(SRC + water + '.png').save(OUT + 'worlds/water_%s.png' % ours)
    print(ours, 'filled', n)
