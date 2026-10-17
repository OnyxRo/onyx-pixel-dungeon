"""Copies a mod's badge pictures into our badge sheet (interfaces/badges.png).

  python -I scripts/port-badges.py <mod badges.png> <first mod cell> <last mod cell> <first cell in ours>

Cells are 16x16 and the sheet is 8 cells wide. The sheet grows downward when needed.
Example (Shittened PD, its badges sit in cells 126 to 158)
  python -I scripts/port-badges.py ~/mod-sources/shittened/core/src/main/assets/interfaces/badges.png 126 158 128
"""
import os, sys
from PIL import Image

src_path, a, b, dst_first = sys.argv[1], int(sys.argv[2]), int(sys.argv[3]), int(sys.argv[4])
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
dst_path = os.path.join(ROOT, 'core/src/main/assets/interfaces/badges.png')

src = Image.open(os.path.expanduser(src_path)).convert('RGBA')
dst = Image.open(dst_path).convert('RGBA')
cols = dst.width // 16
need_rows = (dst_first + (b - a) + cols) // cols
if need_rows * 16 > dst.height:
    grown = Image.new('RGBA', (dst.width, need_rows * 16), (0, 0, 0, 0))
    grown.paste(dst, (0, 0))
    dst = grown
for i in range(a, b + 1):
    sx, sy = (i % cols) * 16, (i // cols) * 16
    cell = src.crop((sx, sy, sx + 16, sy + 16))
    j = dst_first + (i - a)
    dx, dy = (j % cols) * 16, (j // cols) * 16
    dst.paste(cell, (dx, dy))
dst.save(dst_path)
print('badges.png now', dst.width, 'x', dst.height)
