"""Copies 16x16 icon tiles from a mod's talent or hero icon sheet into ours.

  python -I scripts/port-icons.py talent  <mod key> 576-601            same cell numbers in our sheet
  python -I scripts/port-icons.py talent  <mod key> 160-170 --to 1184   mod cells land at 1184, 1185, ...
  python -I scripts/port-icons.py hero    <mod key> 120,121,67 --to 128 these three land at 128, 129, 130

The mod sheets are found by name (Talent_icon.png or talent_icons.png, hero_icons.png) under the mod
folder. Our sheets grow downwards when a destination cell is past the bottom. The mod's sheet widths are
read from the picture, so a 1024 wide hero sheet works. The command is safe to run twice. It prints the
cell numbers it wrote, ready to paste into a Java constant.

Layout notes. Our talent_icons.png is 512 wide (32 per row). Our hero_icons.png is 128 wide (8 per row)
and cell 127 is the transparent NONE icon, so new hero icons start at 128.
"""
import os
import sys

from PIL import Image

HOME = os.path.expanduser('~/mod-sources')
SIZE = 16


def find_sheet(mod, names):
    base = os.path.join(HOME, mod)
    for d, _, fs in os.walk(base):
        if os.sep + 'assets' + os.sep + 'interfaces' not in d + os.sep:
            continue
        for n in names:
            if n in fs and 'build' not in d.split(os.sep):
                return os.path.join(d, n)
    raise SystemExit('no sheet %s in %s' % (names, base))


def parse(spec):
    cells = []
    for part in spec.split(','):
        if '-' in part:
            a, b = part.split('-')
            cells.extend(range(int(a), int(b) + 1))
        else:
            cells.append(int(part))
    return cells


def main():
    kind, mod, spec = sys.argv[1], sys.argv[2], sys.argv[3]
    to = None
    if '--to' in sys.argv:
        to = int(sys.argv[sys.argv.index('--to') + 1])
    cells = parse(spec)
    if kind == 'talent':
        src_path = find_sheet(mod, ['Talent_icon.png', 'talent_icons.png'])
        dst_path = 'core/src/main/assets/interfaces/talent_icons.png'
    else:
        src_path = find_sheet(mod, ['hero_icons.png'])
        dst_path = 'core/src/main/assets/interfaces/hero_icons.png'
    src = Image.open(src_path).convert('RGBA')
    dst = Image.open(dst_path).convert('RGBA')
    sw, dw = src.width // SIZE, dst.width // SIZE
    written = []
    for i, c in enumerate(cells):
        d = (to + i) if to is not None else c
        sx, sy = (c % sw) * SIZE, (c // sw) * SIZE
        if sy + SIZE > src.height:
            print('mod cell %d is outside %s' % (c, src_path))
            continue
        need = ((d // dw) + 1) * SIZE
        if need > dst.height:
            grown = Image.new('RGBA', (dst.width, need), (0, 0, 0, 0))
            grown.paste(dst, (0, 0))
            dst = grown
        tile = src.crop((sx, sy, sx + SIZE, sy + SIZE))
        dx, dy = (d % dw) * SIZE, (d // dw) * SIZE
        dst.paste(tile, (dx, dy))
        written.append((c, d))
    dst.save(dst_path)
    print('wrote %d tiles into %s (%s)' % (len(written), dst_path, '%dx%d' % dst.size))
    print(' '.join('%d>%d' % w for w in written))


main()
