"""Convert an old 16x4 tile sheet (Shattered 0.6 and older, 256x64) into this game's 256x256 layout.

Usage
  python -I scripts/convert-oldtiles.py OLD.png BASE.png OUT.png

OLD is the mod's sheet, BASE is Shattered's sheet for the same stage (core/src/main/assets/environment/tiles_sewers.png
and so on). Every old cell is copied to the new cell that art-for-design/target-layout/LAYOUT.md gives it. Every new cell
with no old source (raised walls, internal walls, overhangs, raised doors and decorations, mine rocks and so on) keeps
Shattered's shape and is recoloured by histogram matching to the old cell it belongs with, so walls get the mod's wall
colours, doors the mod's door colours. This is a stand in until a sheet is redrawn by hand.
"""
import sys
from PIL import Image

C = 16

# old cell -> new flat cells (copied pixel for pixel)
FLAT = {0: [24], 1: [0], 2: [2], 3: [19], 4: [48], 5: [56], 6: [57], 7: [16], 8: [17], 9: [3],
        10: [58], 11: [20], 12: [49], 13: [65], 14: [4], 15: [66], 16: [1], 17: [61], 18: [60],
        20: [18], 21: [72], 22: [73], 23: [50], 24: [64], 32: [25], 33: [26], 34: [27], 35: [28],
        38: [6], 39: [8], 40: [52], 41: [9], 42: [53], 43: [10], 44: [69], 45: [7], 46: [54], 47: [12]}
for old in range(48, 64):
    FLAT[old] = [32 + (63 - old)]
# the old wall faces also serve as the raised wall faces, which keeps sharp patterns (stripes, bricks) intact
for old, news in {4: list(range(80, 84)) + list(range(88, 92)) + list(range(104, 108)), 40: list(range(96, 100)),
                  12: list(range(84, 88)), 42: list(range(100, 104)), 23: list(range(92, 96)),
                  46: list(range(108, 112)), 5: [112], 6: [113], 10: [114, 115]}.items():
    FLAT[old] = FLAT[old] + news

# new cells with no old source -> old cell whose colours they take
def rng(a, b):
    return list(range(a, b + 1))

RECOLOR = {}
def add(cells, old):
    for c in cells:
        RECOLOR[c] = old

add([5, 11], 16)                         # mine floor, from floor decoration
add([22], 7)                             # special entrance, from entrance
add([59], 10)                            # crystal door, from locked door
add([67, 70], 15)                        # furrowed grass, from high grass
add([74, 75, 76, 77, 78], 21)            # region decoration and mine rocks, from statue
add(rng(80, 83) + rng(88, 91), 4)        # raised wall and wall behind a doorway
add(rng(84, 87), 12)                     # raised wall decoration
add(rng(92, 95), 23)                     # raised bookshelf
add(rng(96, 99), 40)                     # raised wall alternate
add(rng(100, 103), 42)                   # raised wall decoration alternate
add(rng(104, 107), 4)
add(rng(108, 111), 46)                   # raised bookshelf alternate
add([112], 5); add([113], 6); add([114], 10); add([115], 10); add([116], 1)
add([120], 24); add([121], 13); add([122, 123, 125, 126], 15)
add([128], 21); add([129], 22); add(rng(130, 137), 21)
add(rng(144, 159), 4)                    # internal walls
add(rng(160, 175), 12)                   # internal wall decoration
add(rng(176, 191), 23)                   # internal wooden walls
add(rng(192, 195), 4); add(rng(196, 199), 12); add(rng(200, 203), 23)
add(rng(208, 211), 6); add(rng(212, 215), 5); add(rng(216, 223), 10)
add([224], 5); add([225], 6); add([226], 10); add([227], 5); add([228], 10); add([229], 10); add([230], 8)
add([232], 24); add([233], 13); add([234, 235, 237, 238], 15)
add([240], 21); add([241], 22); add(rng(242, 246), 21)


def cell(img, n):
    x, y = (n % 16) * C, (n // 16) * C
    return img.crop((x, y, x + C, y + C))


def lum(p):
    return 0.299 * p[0] + 0.587 * p[1] + 0.114 * p[2]


def recolor(target, source):
    """Give target the colours of source, matched by brightness rank. Alpha and shape stay the target's."""
    src = sorted((p for p in source.getdata() if p[3] > 0), key=lum)
    if not src:
        return target
    px = list(target.getdata())
    opaque = sorted((i for i, p in enumerate(px) if p[3] > 0), key=lambda i: lum(px[i]))
    out = list(px)
    n = len(opaque)
    for rank, i in enumerate(opaque):
        s = src[min(len(src) - 1, int(rank * len(src) / max(1, n)))]
        out[i] = (s[0], s[1], s[2], px[i][3])
    res = Image.new('RGBA', target.size)
    res.putdata(out)
    return res


def main(old_path, base_path, out_path):
    old = Image.open(old_path).convert('RGBA')
    base = Image.open(base_path).convert('RGBA')
    if old.size != (256, 64):
        sys.exit('old sheet must be 256x64, got %s' % (old.size,))
    if base.size != (256, 256):
        sys.exit('base sheet must be 256x256, got %s' % (base.size,))
    out = base.copy()
    for new, src in RECOLOR.items():
        out.paste(recolor(cell(base, new), cell(old, src)), ((new % 16) * C, (new // 16) * C))
    for src, news in FLAT.items():
        for new in news:
            out.paste(cell(old, src), ((new % 16) * C, (new // 16) * C))
    out.save(out_path)
    print('wrote', out_path)


if __name__ == '__main__':
    if len(sys.argv) != 4:
        sys.exit(__doc__)
    main(*sys.argv[1:])
