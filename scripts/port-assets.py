"""Adds the Assets constants a ported mod needs, straight from a compile log, with no Gradle rounds.

  python -I scripts/port-assets.py <mod key or folder> <tag> [log, default build/fast.log]

Reads "symbol: variable X / location: class Sprites" (or Sounds, Music and so on) pairs from the
javac log, finds X in the mod's Assets.java, copies the file to assets/<folder>/<tag>/ and appends
the constant to this game's Assets. Art kept at a scale (assets/asset_pixel_scale.txt, Pixel Spacebase
is 4) is shrunk with nearest neighbour. Used after scripts/fastjavac.sh.
"""
import sys, os, re, io, shutil
from PIL import Image
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.insert(0, os.path.dirname(__file__))
mod_arg, TAG = sys.argv[1], sys.argv[2]
LOG = sys.argv[3] if len(sys.argv) > 3 else os.path.join(ROOT, 'build', 'fast.log')
MODS = os.path.expanduser('~/mod-sources')
MOD = mod_arg if os.path.isdir(mod_arg) else os.path.join(MODS, mod_arg)
import importlib.util
_spec = importlib.util.spec_from_file_location('renames', os.path.join(os.path.dirname(os.path.abspath(__file__)), 'renames.py'))
renames = importlib.util.module_from_spec(_spec); _spec.loader.exec_module(renames)
MB = os.path.join(MOD, renames.mod_package_dir(MOD))
MASSETS = os.path.join(MOD, 'core', 'src', 'main', 'assets')
ASSETS = os.path.join(ROOT, 'core', 'src', 'main', 'assets')
OURS = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon', 'Assets.java')
msrc = io.open(os.path.join(MB, 'Assets.java'), encoding='utf-8', errors='replace').read()
scale = 1
sf = os.path.join(MASSETS, 'asset_pixel_scale.txt')
if os.path.exists(sf): scale = int(io.open(sf).read().strip() or 1)
log = io.open(LOG, encoding='utf-8', errors='replace').read()
want = set(re.findall(r'symbol:\s+variable (\w+)\s*\n\s*location:\s+class (Sprites|Sounds|Music|Environment|Interfaces|Effects|Splashes)\b', log))
ours = io.open(OURS, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in ours else '\n'
added = 0
for const, section in sorted(want):
    if re.search(r'String\s+' + re.escape(const) + r'\s*=', ours): continue   # already added, the log may be stale
    flat = ('SND_' + const) if section == 'Sounds' else const
    m = re.search(r'public static final String\s+(?:SND_)?' + re.escape(const) + r'\s*=\s*"([^"]+)"', msrc) or re.search(r'String\s+' + re.escape(flat) + r'\s*=\s*"([^"]+)"', msrc)
    if not m:
        print('not in mod', section, const); continue
    path = m.group(1)
    src = os.path.join(MASSETS, path)
    if not os.path.exists(src):
        print('file missing', path); continue
    d, f = os.path.split(path)
    if not d: d = 'sounds' if section == 'Sounds' else 'sprites'
    newpath = d + '/' + TAG + '/' + f
    dst = os.path.join(ASSETS, newpath)
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    shutil.copy(src, dst)
    if scale > 1 and path.endswith('.png'):
        im = Image.open(dst).convert('RGBA')
        im.resize((max(1, im.width // scale), max(1, im.height // scale)), Image.NEAREST).save(dst)
    m2 = re.search(r'public static class ' + section + r'\s*\{', ours)
    if not m2: print('no section', section); continue
    ours = ours[:m2.end()] + nl + '\t\tpublic static final String %s = "%s"; //ported, %s' % (const, newpath, TAG) + ours[m2.end():]
    added += 1
io.open(OURS, 'w', encoding='utf-8', newline='').write(ours)
print('added', added)
