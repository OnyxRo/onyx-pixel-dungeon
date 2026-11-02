"""Builds Shittened PD's item skins for this game, from the mod's own source and art.

  python -I scripts/port-skins.py

Reads ~/mod-sources/shittened (read only) and writes
  core/src/main/assets/sprites/shittened/skins.png   the mod's item sheet from row 43 down (skins, arrows, outlines)
  core/src/main/assets/effects/shittened/specks.png  the mod's speck sheet, it has 11 more specks than ours
  core/.../sprites/SkinSprites.java                  a constant and a size for every picture on that sheet
  core/.../Skin.java                                 the skin list, one entry per skin, in the mod's order
  core/.../effects/SkinSpeck.java                    the mod's Speck class, drawn from its own sheet

Rerun it whenever those files need rebuilding. Everything else about skins is hand written
(items/SkinPaint.java, journal/SkinCatalog.java, ui/SkinJournal.java).
"""
import io, os, re, sys
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..')).replace('\\', '/') + '/'
MOD = os.path.expanduser('~/mod-sources/shittened/').replace('\\', '/')
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
MPK = MOD + PK

FIRST_ROW = 42          # zero based row 42 is the mod's row 43, where the outlines start
CUT = FIRST_ROW * 16    # first mod cell kept

def read(p):
    return io.open(p, encoding='utf-8').read()

def write(p, s):
    os.makedirs(os.path.dirname(p), exist_ok=True)
    with io.open(p, 'w', encoding='utf-8', newline='\n') as f:
        f.write(s)

# ---------- the mod's item sheet ----------
ms = read(MPK + 'sprites/ItemSpriteSheet.java')
body = ms[:ms.find('class Icons')]
body = re.sub(r'//[^\n]*', '', body)   # the mod left old lines commented out, some with the same names
defs = dict(re.findall(r'int\s+(\w+)\s*=\s*([^;]+);', body))

def value(name, depth=0):
    expr = defs[name]
    expr = re.sub(r'xy\(\s*(\d+)\s*,\s*(\d+)\s*\)', lambda m: str(int(m.group(1)) - 1 + 16 * (int(m.group(2)) - 1)), expr)
    expr = re.sub(r'[A-Za-z_]\w*', lambda m: str(value(m.group(0), depth + 1)) if m.group(0) in defs else m.group(0), expr)
    assert re.fullmatch(r'[\d\s+\-*()]+', expr), (name, expr)
    return int(eval(expr))

sizes = {}
for name, w, h in re.findall(r'assignItemRect\(\s*(\w+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)', body):
    sizes[name] = (int(w), int(h))

ours = read(ROOT + PK + 'sprites/ItemSpriteSheet.java')
our_names = set(re.findall(r'public static final int (\w+)\s*=', ours))

# every public picture of the mod at or past the cut goes on our skin sheet
sheet_names = []
for name in re.findall(r'public static final int (\w+)\s*=', body):
    if name in ('SIZE', 'WIDTH'):
        continue
    try:
        v = value(name)
    except Exception:
        continue
    if v >= CUT and name not in [n for n, _ in sheet_names]:
        sheet_names.append((name, v))

src = Image.open(MOD + 'core/src/main/assets/sprites/items.png').convert('RGBA')
crop = src.crop((0, FIRST_ROW * 16, src.width, src.height))
os.makedirs(ROOT + 'core/src/main/assets/sprites/shittened', exist_ok=True)
crop.save(ROOT + 'core/src/main/assets/sprites/shittened/skins.png')
rows = crop.height // 16

out = []
out.append('package com.shatteredpixel.shatteredpixeldungeon.sprites;\n')
out.append('import com.watabou.noosa.TextureFilm;')
out.append('import com.watabou.utils.RectF;\n')
out.append('//Shittened PD item skins. Built by scripts/port-skins.py from the mod\'s item sheet, do not edit by hand.')
out.append('//The pictures live on their own sheet (sprites/shittened/skins.png, the mod\'s rows 43 and down), so items.png')
out.append('//never moves. An item image number at BASE or above, and below BASE + CELLS, draws from that sheet.')
out.append('public final class SkinSprites {\n')
out.append('\tpublic static final String SHEET = "sprites/shittened/skins.png";')
out.append('\tpublic static final int BASE = 0x8000;')
out.append('\tpublic static final int CELLS = %d;' % (rows * 16))
out.append('\tprivate static final int TX_WIDTH = 256;')
out.append('\tprivate static final int TX_HEIGHT = %d;\n' % crop.height)
for name, v in sheet_names:
    out.append('\tpublic static final int %s = BASE + %d;' % (name, v - CUT))
out.append('')
out.append('\tprivate static TextureFilm film;\n')
out.append('\tprivate static TextureFilm film() {')
out.append('\t\tif (film == null) {')
out.append('\t\t\tfilm = new TextureFilm(TX_WIDTH, TX_HEIGHT, ItemSpriteSheet.SIZE, ItemSpriteSheet.SIZE);')
for name, v in sheet_names:
    if name in sizes:
        out.append('\t\t\tsize(%s, %d, %d);' % (name, sizes[name][0], sizes[name][1]))
out.append('\t\t}')
out.append('\t\treturn film;')
out.append('\t}\n')
out.append('\tprivate static void size(int image, int w, int h) {')
out.append('\t\tint i = image - BASE;')
out.append('\t\tint x = (i % 16) * ItemSpriteSheet.SIZE;')
out.append('\t\tint y = (i / 16) * ItemSpriteSheet.SIZE;')
out.append('\t\tfilm.add(i, x, y, x + w, y + h);')
out.append('\t}\n')
out.append('\tpublic static boolean is(int image) {')
out.append('\t\treturn image >= BASE && image < BASE + CELLS;')
out.append('\t}\n')
out.append('\tpublic static RectF frame(int image) {')
out.append('\t\treturn film().get(image - BASE);')
out.append('\t}\n')
out.append('\tpublic static float width(int image) {')
out.append('\t\treturn is(image) ? film().width(frame(image)) : ItemSpriteSheet.film.width(image);')
out.append('\t}\n')
out.append('\tpublic static float height(int image) {')
out.append('\t\treturn is(image) ? film().height(frame(image)) : ItemSpriteSheet.film.height(image);')
out.append('\t}\n')
out.append('\tpublic static int cellX(int image) {')
out.append('\t\treturn ((image - BASE) % 16) * ItemSpriteSheet.SIZE;')
out.append('\t}\n')
out.append('\tpublic static int cellY(int image) {')
out.append('\t\treturn ((image - BASE) / 16) * ItemSpriteSheet.SIZE;')
out.append('\t}\n')
out.append('\tprivate SkinSprites() {}')
out.append('}')
write(ROOT + PK + 'sprites/SkinSprites.java', '\n'.join(out) + '\n')
sheet_set = dict(sheet_names)

def sprite_ref(name):
    if name in sheet_set:
        return 'SkinSprites.' + name
    assert name in our_names, 'no picture ' + name
    return 'ItemSpriteSheet.' + name

# ---------- the skin list ----------
sk = read(MPK + 'Skin.java')
imports = {}
for full in re.findall(r'import (com\.shatteredpixel\.shatteredpixeldungeon\.[\w.]+);', sk):
    imports[full.split('.')[-1]] = full
RENAMES = {'Cheese': 'MilkCheese'}   # Experienced's Cheese owns the name here

enum_body = sk[sk.index('//SEALS'):sk.index('    ;\n')]
pat = re.compile(r'^\s+([A-Z_]+)\s*\((\w+)\.class,\s*ItemSpriteSheet\.(\w+),\s*(\d),\s*"([^"]*)"\s*,\s*(new [\w.]+\(\)|null)\s*(?:,\s*"((?:[^"\\]|\\.)*)")?\s*(?:,\s*(\d+))?\s*\)', re.M)
entries = pat.findall(enum_body)
names_in_body = re.findall(r'^\s+([A-Z_]+)\s*\(', enum_body, re.M)
assert len(entries) == len(names_in_body), (len(entries), len(names_in_body))

# legendary names, from each item's name() in the mod
legend = {}
for f in ['items/artifacts/CloakOfShadows', 'items/weapon/melee/Glaive', 'items/weapon/melee/Longsword',
          'items/weapon/melee/WarHammer', 'items/weapon/melee/RunicBlade', 'items/weapon/melee/BattleAxe',
          'items/weapon/melee/Greatshield', 'items/weapon/melee/Scimitar', 'items/weapon/melee/MagesStaff',
          'items/artifacts/UnstableSpellbook', 'items/armor/LeatherArmor', 'items/armor/PlateArmor', 'items/armor/ClothArmor']:
    s = read(MPK + f + '.java')
    m = re.search(r'public String name\(\)\s*\{(.*?)\n\t\}', s, re.S)
    if m:
        for k, v in re.findall(r'case ([A-Z_]+):\s*return "([^"]+)";', m.group(1)):
            legend[k] = v

# arrows the spirit bow shoots for each skin
bow = read(MPK + 'items/weapon/SpiritBow.java')
arrows = dict(re.findall(r'case ([A-Z_]+):\s*arrow\.image = ItemSpriteSheet\.(\w+);', bow))

used_imports = set(['Item'])
lines = []
def empty_art(spr):
    if spr not in sheet_set:
        return False
    v = sheet_set[spr] - CUT
    w, h = sizes.get(spr, (16, 16))
    return crop.crop(((v % 16) * 16, (v // 16) * 16, (v % 16) * 16 + w, (v // 16) * 16 + h)).getbbox() is None

skipped = []
for (name, cls, spr, rar, disp, ing, desc, _id) in entries:
    if empty_art(spr):
        skipped.append(name)   # the mod never drew it (its golden katana), so it is left out
        continue
    used_imports.add(cls)
    if ing == 'null':
        ingr = 'null'
    else:
        c = ing[4:-2]
        base = c.split('.')[0]
        base2 = RENAMES.get(base, base)
        c = base2 + c[len(base):]
        used_imports.add(base2 if base2 != base else base)
        ingr = c + '.class'
        if rar == '0' and cls == 'Katana':
            ingr = 'Katana.class'   # the mod's katana default named the greatshield, a copy and paste slip
    rar = int(rar)
    if rar >= 4 and cls == 'BrokenSeal':
        leg = disp + ' Seal'
    elif rar >= 4 and cls == 'SpiritBow':
        leg = disp + ' Bow'
    else:
        leg = legend.get(name)
    legs = 'null' if leg is None else '"%s"' % leg
    arrow = sprite_ref(arrows[name]) if name in arrows else '-1'
    d = '"%s"' % desc if desc else '""'
    lines.append('\t%s(%s.class, %s, %d, "%s", %s, %s, %s, %s)' % (
        name, cls, sprite_ref(spr), rar, disp, ingr, d, legs, arrow))

def imp_for(n):
    if n == 'MilkCheese':
        return 'com.shatteredpixel.shatteredpixeldungeon.items.food.MilkCheese'
    full = imports.get(n)
    assert full, n
    assert os.path.exists(ROOT + 'core/src/main/java/' + full.replace('.', '/') + '.java'), full
    return full

imps = sorted(set(imp_for(n) for n in used_imports))

tpl = read(ROOT + 'scripts/port-skins-template.java')
code = tpl.replace('/*IMPORTS*/', '\n'.join('import %s;' % i for i in imps))
code = code.replace('/*ENTRIES*/', ',\n'.join(lines) + ';')
write(ROOT + PK + 'Skin.java', code)

# ---------- specks ----------
sp = Image.open(MOD + 'core/src/main/assets/effects/specks.png')
os.makedirs(ROOT + 'core/src/main/assets/effects/shittened', exist_ok=True)
sp.save(ROOT + 'core/src/main/assets/effects/shittened/specks.png')
speck = read(MPK + 'effects/Speck.java')
speck = speck.replace('public class Speck extends Image', '//Shittened PD\'s Speck, copied by scripts/port-skins.py so the skin effects draw from the mod\'s own speck sheet.\n//Our Speck stays as it is. Do not edit by hand, rerun the script.\npublic class SkinSpeck extends Image')
speck = speck.replace('public Speck()', 'public SkinSpeck()')
speck = speck.replace('public Speck image(', 'public SkinSpeck image(')
speck = speck.replace('texture( Assets.Effects.SPECKS );', 'texture( "effects/shittened/specks.png" );')
speck = re.sub(r'\(Speck\)', '(SkinSpeck)', speck)
speck = re.sub(r'\bnew Speck\(\)', 'new SkinSpeck()', speck)
speck = re.sub(r'\bSpeck\.class\b', 'SkinSpeck.class', speck)
speck = re.sub(r'\bSpeck ([a-z])', r'SkinSpeck \1', speck)
write(ROOT + PK + 'effects/SkinSpeck.java', speck)

print("left out, no picture", skipped); print("skins", len(lines), 'sheet pictures', len(sheet_names), 'legend names', len(legend), 'arrows', len(arrows))
