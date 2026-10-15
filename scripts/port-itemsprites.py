"""Copies item pictures from a mod's item sheet into this game's.

  python port-itemsprites.py <project root> <mod root> NAME [NAME ...]

For each ItemSpriteSheet constant named, works out which 16x16 cell the mod drew it in,
pastes that cell into a free cell of the rows kept for other mods' items (rows 13 and 19 of
sprites/items.png), and adds the constant and its size to this game's ItemSpriteSheet.
"""
import sys, os, io, re
from PIL import Image

ROOT = sys.argv[1].rstrip('/') + '/'
MOD = sys.argv[2].rstrip('/') + '/'
NAMES = sys.argv[3:]
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
MOD_ROWS = [12, 18, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43]   # zero-based rows 13, 19, 33 to 44 (sheet grown to 704 px high)

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92) * 2) else chr(92) * 2 + '?' + chr(92) + p

# the mod's package folder is not always the same as this game's
sheets = []
for dirpath, dirs, files in os.walk(LP(MOD + 'core/src/main/java')):
    if 'ItemSpriteSheet.java' in files: sheets.append(os.path.join(dirpath, 'ItemSpriteSheet.java'))
assert sheets, 'mod has no ItemSpriteSheet'
ms = io.open(sheets[0], encoding='utf-8', errors='replace').read()
# only the item part of the file, before the small icon sheet nested inside it
cut = ms.find('class Icons')
body = ms[:cut] if cut != -1 else ms
defs = dict(re.findall(r'int\s+(\w+)\s*=\s*([^;]+);', body))

def value(name, depth=0):
    assert depth < 20, name
    expr = defs[name]
    expr = re.sub(r'xy\(\s*(\d+)\s*,\s*(\d+)\s*\)', lambda m: str(int(m.group(1)) - 1 + 16 * (int(m.group(2)) - 1)), expr)
    expr = re.sub(r'[A-Za-z_]\w*', lambda m: str(value(m.group(0), depth + 1)) if m.group(0) in defs else m.group(0), expr)
    assert re.fullmatch(r'[\d\s+\-*()]+', expr), (name, expr)
    return int(eval(expr))

sheet_path = LP(MOD + 'core/src/main/assets/sprites/items.png')
if not os.path.exists(sheet_path): sheet_path = LP(MOD + 'core/src/main/assets/items.png')   # pre 0.8 mods keep it flat
src = Image.open(sheet_path).convert('RGBA')
dstp = ROOT + 'core/src/main/assets/sprites/items.png'
dst = Image.open(dstp).convert('RGBA')
p = ROOT + PK + 'sprites/ItemSpriteSheet.java'
s = io.open(p, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in s else '\n'
s = s.replace('\r\n', '\n')

def free_cell():
    for r in MOD_ROWS:
        for c in range(16):
            if dst.crop((c * 16, r * 16, c * 16 + 16, r * 16 + 16)).getbbox() is None: return c, r
    raise SystemExit('no free cells left for mod items, add another row to MOD_ROWS')

consts = ''; rects = ''
for name in NAMES:
    if re.search(r'int ' + name + r'\s*=', s):
        print('already there', name); continue
    if name not in defs:
        print('NOT IN MOD', name); continue
    i = value(name)
    x, y = (i % 16) * 16, (i // 16) * 16
    tile = src.crop((x, y, x + 16, y + 16)); bb = tile.getbbox()
    if not bb:
        print('EMPTY CELL', name); continue
    c, r = free_cell()
    dst.paste(tile, (c * 16, r * 16))
    consts += '	public static final int %s = xy(%d, %d);\n' % (name, c + 1, r + 1)
    rects += '		assignItemRect(%s, %d, %d);\n' % (name, bb[2], bb[3])
    print('added', name, 'at column', c + 1, 'row', r + 1)
if consts:
    dst.save(dstp)
    a = '	public static final int WOODEN_BOOMERANG = MOD_ITEMS+1;\n'
    assert a in s
    s = s.replace(a, a + consts, 1)
    a = '		assignItemRect(WOODEN_BOOMERANG, 12, 12);\n'
    assert a in s
    s = s.replace(a, a + rects, 1)
    io.open(p, 'w', encoding='utf-8', newline='').write(s.replace('\n', nl))
