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
# rows 13 and 19 (zero based 12 and 18) and every row from 33 down are kept for other mods' items.
# The sheet grows by itself when they run out, see free_cell.
# ITEM_ROWS=128-133 (one based, like the notes) limits a parallel port to its own rows and grows the sheet to fit
_ER = os.environ.get('ITEM_ROWS')
def mod_rows(height):
    if _ER:
        a, b = [int(x) for x in _ER.split('-')]
        return list(range(a - 1, b))
    # ITEM_ROWS=146-151 (one based, inclusive) limits a parallel run to its own rows
    if os.environ.get('ITEM_ROWS'):
        a, b = os.environ['ITEM_ROWS'].split('-')
        return list(range(int(a) - 1, int(b)))
    #ITEM_ROWS=106-113 (one based, as the briefs name them) limits a run to those rows, the sheet grows to hold them
    er = os.environ.get('ITEM_ROWS')
    if er:
        a, b = [int(x) for x in er.split('-')]
        return [r for r in range(a - 1, b) if (r + 1) * 16 <= height] or [-1]
    # ITEM_ROWS="134-145" (one based, as in xy()) keeps a parallel port inside its own rows, the sheet grows to hold them
    er = os.environ.get('ITEM_ROWS')
    if er:
        a, b = [int(x) for x in er.split('-')]
        return list(range(a - 1, b))
    # ITEM_ROWS=60-67 (one based, set when several agents share the sheet) limits pictures to those rows
    # ITEM_ROWS=68-75 (one based) restricts a parallel agent to its own rows
    if os.environ.get('ITEM_ROWS'):
        a, b = os.environ['ITEM_ROWS'].split('-')
        return list(range(int(a) - 1, int(b)))
    # ONYX_ITEM_ROWS=122-127 (one based, as the brief names them) pins a parallel agent to its own rows
    if os.environ.get('ONYX_ITEM_ROWS'):
        a, b = [int(x) for x in os.environ['ONYX_ITEM_ROWS'].split('-')]
        return list(range(a - 1, b))
    # ITEM_ROWS=114-121 (one based, inclusive) limits a parallel run to its own rows
    if os.environ.get('ITEM_ROWS'):
        a, b = os.environ['ITEM_ROWS'].split('-')
        return list(range(int(a) - 1, int(b)))
    return [12, 18] + list(range(32, height // 16))

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92) * 2) else chr(92) * 2 + '?' + chr(92) + p

# the mod's package folder is not always the same as this game's
sheets = []
_JB = MOD + ('core/src/main/java' if os.path.isdir(MOD + 'core') else 'src/main/java')
for dirpath, dirs, files in os.walk(LP(_JB)):
    if 'ItemSpriteSheet.java' in files: sheets.append(os.path.join(dirpath, 'ItemSpriteSheet.java'))
assert sheets, 'mod has no ItemSpriteSheet'
ms = io.open(sheets[0], encoding='utf-8', errors='replace').read()
# only the item part of the file, before the small icon sheet nested inside it
cut = ms.find('class Icons')
body = ms[:cut] if cut != -1 else ms
defs = dict(re.findall(r'int\s+(\w+)\s*=\s*([^;]+);', body))
mw = re.search(r'private static final int WIDTH\s*=\s*(\d+);', body)
SW = int(mw.group(1)) if mw else 16   # some mods (Magic Ling) use a wider sheet

def value(name, depth=0):
    assert depth < 20, name
    expr = defs[name]
    expr = re.sub(r'xy\(\s*(\d+)\s*,\s*(\d+)\s*\)', lambda m: str(int(m.group(1)) - 1 + MOD_COLS * (int(m.group(2)) - 1)), expr)
    expr = re.sub(r'xy\(\s*(\d+)\s*,\s*(\d+)\s*\)', lambda m: str(int(m.group(1)) - 1 + COLS * (int(m.group(2)) - 1)), expr)
    expr = re.sub(r'xy\(\s*(\d+)\s*,\s*(\d+)\s*\)', lambda m: str(int(m.group(1)) - 1 + SW * (int(m.group(2)) - 1)), expr)
    expr = re.sub(r'[A-Za-z_]\w*', lambda m: str(value(m.group(0), depth + 1)) if m.group(0) in defs else m.group(0), expr)
    assert re.fullmatch(r'[\d\s+\-*()]+', expr), (name, expr)
    return int(eval(expr))

_AB = MOD + ('core/src/main/assets/' if os.path.isdir(MOD + 'core') else 'src/main/assets/')
sheet_path = LP(_AB + 'sprites/items.png')
if not os.path.exists(sheet_path): sheet_path = LP(_AB + 'items.png')   # pre 0.8 mods keep it flat
if not os.path.exists(sheet_path): sheet_path = LP(_AB + 'ui/items.png')   # 0.5 and 0.6 mods keep it under ui
src = Image.open(sheet_path).convert('RGBA')
# art kept at a multiple of 16 px a cell (Pixel Spacebase is 4x, see assets/asset_pixel_scale.txt), shrink it with nearest neighbour
_sc = LP(MOD + 'core/src/main/assets/asset_pixel_scale.txt')
if os.path.exists(_sc):
    _n = int(io.open(_sc).read().strip() or 1)
    if _n > 1: src = src.resize((src.width // _n, src.height // _n), Image.NEAREST)
MOD_COLS = src.width // 16
sheet_path = LP(MOD + 'core/src/main/assets/sprites/items.png')
if not os.path.exists(sheet_path): sheet_path = LP(MOD + 'core/src/main/assets/sprites/items/items.png')   # Magic Ling keeps it in a folder
if not os.path.exists(sheet_path): sheet_path = LP(MOD + 'core/src/main/assets/items.png')   # pre 0.8 mods keep it flat
if not os.path.exists(sheet_path): sheet_path = LP(MOD + 'core/src/main/assets/sprites/items/items.png')   # Magic Ling keeps it in an items folder
if not os.path.exists(sheet_path): sheet_path = LP(MOD + 'core/src/main/assets/ui/items.png')   # 0.5 and 0.6 mods keep it under ui
src = Image.open(sheet_path).convert('RGBA')
COLS = src.width // 16   # Magic Ling's sheet is 32 cells wide, Shattered's is 16
dstp = ROOT + 'core/src/main/assets/sprites/items.png'
dst = Image.open(dstp).convert('RGBA')
if _ER:
    _need = int(_ER.split('-')[1]) * 16
    if dst.height < _need:
        _g = Image.new('RGBA', (dst.width, _need), (0, 0, 0, 0)); _g.paste(dst, (0, 0)); dst = _g
p = ROOT + PK + 'sprites/ItemSpriteSheet.java'
s = io.open(p, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in s else '\n'
s = s.replace('\r\n', '\n')

def free_cell():
    global dst
    # ITEM_ROWS can name rows below the end of the sheet, grow it to hold them
    if os.environ.get('ITEM_ROWS'):
        need = int(os.environ['ITEM_ROWS'].split('-')[1]) * 16
        if dst.height < need:
            grown = Image.new('RGBA', (dst.width, need), (0, 0, 0, 0))
            grown.paste(dst, (0, 0))
            dst = grown
            print('item sheet grown to', dst.height, 'px high')
    while True:
        if os.environ.get('ITEM_ROWS') and dst.height < max(mod_rows(0)) * 16 + 16:
            need = max(mod_rows(0)) * 16 + 16
            grown = Image.new('RGBA', (dst.width, need), (0, 0, 0, 0))
            grown.paste(dst, (0, 0))
            dst = grown
        elif os.environ.get('ITEM_ROWS') and getattr(free_cell, 'full', False):
            return None
        for r in mod_rows(dst.height):
            if r < 0: break
            if (r + 1) * 16 > dst.height:
                grown = Image.new('RGBA', (dst.width, (r + 1) * 16), (0, 0, 0, 0))
                grown.paste(dst, (0, 0))
                dst = grown
                print('item sheet grown to', dst.height, 'px high')
            if r * 16 + 16 > dst.height: break
            for c in range(16):
                if r * 16 + 16 > dst.height: continue
                if dst.crop((c * 16, r * 16, c * 16 + 16, r * 16 + 16)).getbbox() is None: return c, r
        if os.environ.get('ITEM_ROWS'):
            return None
        if os.environ.get('ONYX_ITEM_ROWS') and max(mod_rows(0)) * 16 + 16 <= dst.height: raise SystemExit('the pinned item rows are full')
                if dst.crop((c * 16, r * 16, c * 16 + 16, r * 16 + 16)).getbbox() is None:
                    if r * 16 + 16 > dst.height:   # a fixed row range can lie below the sheet
                        grown = Image.new('RGBA', (dst.width, r * 16 + 16), (0, 0, 0, 0))
                        grown.paste(dst, (0, 0))
                        dst = grown
                    return c, r
        # nothing free, add eight rows (128 px) at the bottom and look again
        grown = Image.new('RGBA', (dst.width, dst.height + 128), (0, 0, 0, 0))
        grown.paste(dst, (0, 0))
        dst = grown
        print('item sheet grown to', dst.height, 'px high')

consts = ''; rects = ''
for name in NAMES:
    if re.search(r'int ' + name + r'\s*=', s):
        print('already there', name); continue
    if name not in defs:
        print('NOT IN MOD', name); continue
    i = value(name)
    x, y = (i % MOD_COLS) * 16, (i // MOD_COLS) * 16
    x, y = (i % COLS) * 16, (i // COLS) * 16
    x, y = (i % SW) * 16, (i // SW) * 16
    tile = src.crop((x, y, x + 16, y + 16)); bb = tile.getbbox()
    if not bb:
        print('EMPTY CELL', name); continue
    cell = free_cell()
    if cell is None:
        print('NO FREE CELL in ITEM_ROWS for', name); continue
    c, r = cell
    dst.paste(tile, (c * 16, r * 16))
    consts += '	public static final int %s = xy(%d, %d);\n' % (name, c + 1, r + 1)
    rects += '		assignItemRect(%s, %d, %d);\n' % (name, bb[2], bb[3])
    print('added', name, 'at column', c + 1, 'row', r + 1)
if consts:
    dst.save(dstp)
    a = '	public static final int WOODEN_BOOMERANG = MOD_ITEMS+1;\n'
    assert a in s
    # ITEM_BEFORE=1 puts the new lines before the anchor, so two ports that add lines do not touch the same spot
    s = s.replace(a, (consts + a) if os.environ.get('ITEM_BEFORE') else (a + consts), 1)
    anchor = os.environ.get('ITEM_ANCHOR')   # a constant name, the block goes before its line so parallel merges do not touch
    if anchor:
        ca = '	public static final int %s = ' % anchor
        assert ca in s, anchor
        s = s.replace(ca, consts + ca, 1)
    else:
        s = s.replace(a, a + consts, 1)
    # TX_HEIGHT must equal the sheet height or every picture is stretched
    s = re.sub(r'(int TX_HEIGHT = )\d+;', lambda m: m.group(1) + '%d;' % dst.height, s, count=1)
    a = '		assignItemRect(WOODEN_BOOMERANG, 12, 12);\n'
    assert a in s
    s = s.replace(a, (rects + a) if os.environ.get('ITEM_BEFORE') else (a + rects), 1)
    if anchor:
        ra = '		assignItemRect(%s, ' % anchor
        assert ra in s, anchor
        s = s.replace(ra, rects + ra, 1)
    else:
        s = s.replace(a, a + rects, 1)
    io.open(p, 'w', encoding='utf-8', newline='').write(s.replace('\n', nl))
