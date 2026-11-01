"""Rebuild world tile sheets that were drawn for Shattered 0.6 to 1.1 in this game's tile layout.

  python -I scripts/remap-oldsheets.py            rebuild every sheet in SHEETS from the mod's original
  python -I scripts/remap-oldsheets.py --detect   score every world sheet, old layout or new, and list the old ones

Why this exists. Around Shattered 2.0 the tile grid was rearranged (DungeonTileSheet). Walls moved up a row, doors up
two rows, the chasm moved into the ground rows, and some groups grew. Sheets from mods on older bases kept the old grid,
so this game drew doors where walls belong, which showed as rows of locked doors and odd walls (found 2026-11-01).
Earlier sessions only filled empty cells and redrew the overhang row, which wrote over some old tiles, so this starts
from each mod's original sheet in ~/mod-sources.

Each old tile goes to the cell the same DungeonTileSheet constant has now (old names from shattered-1.1.1). Cells the old
grid never had are filled from Shattered's sheet for the same stage, as CLAUDE.md asks for every world sheet.
"""
import os, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..'))
ENV = os.path.join(ROOT, 'core', 'src', 'main', 'assets', 'environment')
MODS = os.path.expanduser('~/mod-sources')
STAGES = ['sewers', 'prison', 'caves', 'city', 'halls']

# (old first cell, new first cell, count)
MOVES = [
    (0, 0, 5), (6, 6, 5), (12, 12, 10),          # ground, stairs, wells, pedestal
    (32, 32, 16),                                # water
    (48, 24, 5),                                 # chasm
    (64, 48, 8),                                 # flat walls and bookshelves
    (80, 56, 3), (83, 60, 2),                    # flat doors, exits
    (97, 72, 2), (99, 64, 4), (104, 69, 2),      # flat statues, pot, barricade, grass
    (112, 80, 32),                               # raised walls
    (144, 128, 3), (147, 132, 1),                # raised doors, sideways door
    (161, 144, 2), (163, 136, 4), (169, 141, 2), # raised statues, pot, barricade, grass
    (176, 144, 16), (176, 160, 16),              # internal walls, and the same for the newer decorated ones
    (192, 176, 16),                              # wooden internal walls
    (208, 192, 4), (208, 196, 4), (224, 200, 4), # wall overhangs, decorated, wooden
    (216, 208, 4), (212, 212, 4), (220, 216, 4), # sideways door overhangs, open, closed, locked
    (229, 224, 2), (231, 227, 2),                # door overhangs, sideways doors
    (240, 240, 1), (241, 232, 4), (246, 237, 2), # statue, pot, barricade and grass overhangs
]

# our sheet name, mod folder, the mod's sheet name, stage
SHEETS = [
    ('ice_caves', 'reremixed', 'ice_caves', 2), ('necropolis', 'reremixed', 'necropolis', 1),
    ('spider_nest', 'reremixed', 'spider_nest', 3),
    ('pollutedsewers', 'ryuse', 'polluted_sewers', 0),
    ('beach', 'cursed', 'beach', 0), ('surface', 'cursed', 'surface', 0),
    ('cursedtemple', 'cursed', 'temple', 2), ('dolyahaven', 'cursed', 'dolyahaven_new', 3),
    ('abyssalsewers', 'abyssalnite', 'sewers', 0), ('abyssalprison', 'abyssalnite', 'prison', 1),
    ('abyssalcaves', 'abyssalnite', 'caves', 2), ('abyssalcity', 'abyssalnite', 'city', 3),
    ('abyssalhalls', 'abyssalnite', 'halls', 4),
    ('dragonlair', 'dragonic', 'lair', 4),
    ('goldencity', 'magicling', 'gold', 3),
    ('towersewers', 'tower', 'sewers', 0), ('toweroozysewers', 'tower', 'sewers_oozy', 0),
    ('towerprison', 'tower', 'prison', 1), ('towertemple', 'tower', 'temple', 1),
    ('towercaves', 'tower', 'caves', 2), ('towergoblingrotto', 'tower', 'gobs', 2), ('towermines', 'tower', 'mines', 2),
    ('towercity', 'tower', 'city', 3), ('towergates', 'tower', 'gates', 3), ('towermages', 'tower', 'mages', 3),
    ('towerhalls', 'tower', 'halls', 4), ('towersouldesert', 'tower', 'souldesert', 4),
    ('lovecraft_overworld', 'lovecraft', 'overworld', 0), ('lovecraft_sewers', 'lovecraft', 'sewers', 0),
    ('lovecraft_prison', 'lovecraft', 'prison', 1), ('lovecraft_caves', 'lovecraft', 'caves', 2),
    ('lovecraft_city', 'lovecraft', 'city', 3), ('lovecraft_halls', 'lovecraft', 'halls', 4),
]

def cell(im, i):
    x, y = (i % 16) * 16, (i // 16) * 16
    return im.crop((x, y, x + 16, y + 16))

def put(im, i, tile):
    im.paste(tile, ((i % 16) * 16, (i // 16) * 16))

def empty(tile):
    return tile.getchannel('A').getbbox() is None

def find(mod, name):
    want = 'tiles_%s.png' % name
    for dp, dn, fn in os.walk(os.path.join(MODS, mod)):
        if 'build' in dp.split(os.sep) or ('environment' not in dp and not dp.endswith('assets') and not dp.endswith('tiles')): continue
        if want in fn: return os.path.join(dp, want)
    raise SystemExit('no %s in %s' % (want, mod))

def remap(old, ref):
    new = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
    for o, n, c in MOVES:
        for k in range(c):
            t = cell(old, o + k)
            if not empty(t): put(new, n + k, t)
    filled = 0
    for i in range(256):
        if empty(cell(new, i)) and not empty(cell(ref, i)):
            put(new, i, cell(ref, i)); filled += 1
    return new, filled

def likeness(a, b, cells):
    d = 0
    for i in cells:
        for p, q in zip(cell(a, i).getdata(), cell(b, i).getdata()):
            d += 255 if (p[3] == 0) != (q[3] == 0) else 0
    return d

def detect():
    """An old sheet has its raised doors on row 9 and its wall overhang on row 13 where the new grid has internal walls."""
    refs = [Image.open(os.path.join(ENV, 'tiles_%s.png' % s)).convert('RGBA') for s in STAGES]
    rows = range(64, 208)
    for f in sorted(os.listdir(os.path.join(ENV, 'worlds'))):
        if not f.startswith('tiles_') or not f.endswith('.png'): continue
        im = Image.open(os.path.join(ENV, 'worlds', f)).convert('RGBA')
        if im.size != (256, 256): continue
        as_new = min(likeness(im, r, rows) for r in refs)
        as_old = min(likeness(remap(im, Image.new('RGBA', (256, 256)))[0], r, rows) for r in refs)
        if as_old < as_new * 0.8: print('OLD', f, as_new, as_old)

if __name__ == '__main__':
    if '--detect' in sys.argv:
        detect(); sys.exit(0)
    for ours, mod, name, stage in SHEETS:
        old = Image.open(find(mod, name)).convert('RGBA')
        ref = Image.open(os.path.join(ENV, 'tiles_%s.png' % STAGES[stage])).convert('RGBA')
        new, filled = remap(old, ref)
        new.save(os.path.join(ENV, 'worlds', 'tiles_%s.png' % ours))
        print(ours, 'filled', filled)
