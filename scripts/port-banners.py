"""Copies banner pictures from a mod's banners.png into ours (interfaces/banners.png).

  python -I scripts/port-banners.py <mod banners.png> <y offset> x0,y0,x1,y1 [x0,y0,x1,y1 ...]

Each box is a region of the mod's sheet (left, top, right, bottom as in its uvRect calls). It is pasted into ours at
the same x and at y plus the offset, and the sheet grows downward when needed. Shattered's own banners live in the
top 256 rows of ours, so use an offset of 256 or more for new art, then add the matching rows to BannerSprites with
the offset applied.
Example (Shittened PD)
  python -I scripts/port-banners.py ~/mod-sources/shittened/core/src/main/assets/interfaces/banners.png 256 23,198,100,219
"""
import os, sys
from PIL import Image

src = Image.open(os.path.expanduser(sys.argv[1])).convert('RGBA')
off = int(sys.argv[2])
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
dst_path = os.path.join(ROOT, 'core/src/main/assets/interfaces/banners.png')
dst = Image.open(dst_path).convert('RGBA')
for box in sys.argv[3:]:
    x0, y0, x1, y1 = [int(v) for v in box.split(',')]
    need = y1 + off
    if need > dst.height:
        grown = Image.new('RGBA', (dst.width, need), (0, 0, 0, 0))
        grown.paste(dst, (0, 0))
        dst = grown
    dst.paste(src.crop((x0, y0, x1, y1)), (x0, y0 + off))
dst.save(dst_path)
print('banners.png now', dst.width, 'x', dst.height)
