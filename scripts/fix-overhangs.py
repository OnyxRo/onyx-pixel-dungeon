"""Give world tile sheets proper wall overhangs (cells 192 to 207).

  python -I scripts/fix-overhangs.py            fix every sheet that needs it
  python -I scripts/fix-overhangs.py --check    only list them, exit 1 if any (check.sh runs this)

Why this exists. This game draws the wall overhang (cells 192 to 207, DungeonTileSheet.WALLS_OVERHANG) over every
floor tile that has a wall below it. Shattered's overhang is see-through in its top half. Sheets from older Shattered
bases keep solid inner wall art in that row, so the floor along the bottom of every room, and any stairs or items on
it, drew as a block of wall. The Dragon boss's exit was invisible because of it (2026-10-27).

The fix takes Shattered's overhang from the region sheet whose walls look most like the world's, and recolours it
with the world's own wall colours, matched pixel by pixel over the wall cells (48 to 127) both sheets share.
"""
import os, sys, glob
from collections import Counter, defaultdict
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..'))
ENV = os.path.join(ROOT, 'core', 'src', 'main', 'assets', 'environment')
C = 16
OVERHANG = range(192, 208)
WALLS = range(48, 128)

def cell(im, i):
    x, y = (i % 16) * C, (i // 16) * C
    return im.crop((x, y, x + C, y + C))

def broken(im):
    return sum(1 for i in range(192, 204) if all(p[3] == 255 for p in cell(im, i).getdata())) >= 6

def luma(p):
    return (p[0] * 3 + p[1] * 6 + p[2]) // 10 if p[3] else -1

def likeness(a, b):
    d = 0
    for i in WALLS:
        for p, q in zip(cell(a, i).getdata(), cell(b, i).getdata()):
            lp, lq = luma(p), luma(q)
            d += 255 if (lp < 0) != (lq < 0) else abs(lp - lq)
    return d

def recolour(world, base):
    pairs = defaultdict(Counter)
    for i in WALLS:
        for p, q in zip(cell(base, i).getdata(), cell(world, i).getdata()):
            if p[3] and q[3]:
                pairs[p[:3]][q[:3]] += 1
    known = {k: v.most_common(1)[0][0] for k, v in pairs.items()}
    def map_colour(rgb):
        if rgb in known: return known[rgb]
        near = min(known, key=lambda k: sum((a - b) ** 2 for a, b in zip(k, rgb)))
        return known[near]
    out = world.copy()
    for i in OVERHANG:
        src = cell(base, i)
        dst = Image.new('RGBA', (C, C))
        dst.putdata([(map_colour(p[:3]) + (p[3],)) if p[3] else (0, 0, 0, 0) for p in src.getdata()])
        out.paste(dst, ((i % 16) * C, (i // 16) * C))
    return out

bases = {k: Image.open(os.path.join(ENV, 'tiles_%s.png' % k)).convert('RGBA') for k in ('sewers', 'prison', 'caves', 'city', 'halls')}
check = '--check' in sys.argv
found = []
for f in sorted(glob.glob(os.path.join(ENV, 'worlds', 'tiles_*.png'))):
    im = Image.open(f).convert('RGBA')
    if im.size != (256, 256) or not broken(im):
        continue
    name = os.path.basename(f)
    found.append(name)
    if check:
        print('%s: solid wall overhangs in cells 192 to 207, run python -I scripts/fix-overhangs.py' % name)
        continue
    region = min(bases, key=lambda k: likeness(im, bases[k]))
    recolour(im, bases[region]).save(f)
    print('%s fixed from %s' % (name, region))
print('SUMMARY overhang sheets %s: %d' % ('needing a fix' if check else 'fixed', len(found)))
sys.exit(1 if check and found else 0)
