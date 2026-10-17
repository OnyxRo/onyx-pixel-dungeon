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
"""Adds every Assets constant that new (untracked) Java files use and this game lacks, without compiling.

  python -I scripts/port-assets.py <mod folder or key> <tag>

Reads each new file's Assets.<Section>.<NAME> references, finds the constant in the mod's Assets,
copies the picture or sound into a subfolder named after the tag and appends the constant to our
Assets section. Much faster than port-copier.py, which finds one batch of 100 compile errors per Gradle run.
"""
import sys, os, re, io, shutil, subprocess

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..')).replace(chr(92), '/')
arg, TAG = sys.argv[1], sys.argv[2]
MODS = os.path.expanduser('~/mod-sources')
MOD = arg if os.path.isdir(arg) else os.path.join(MODS, arg)
MOD = MOD.replace(chr(92), '/').rstrip('/')
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import importlib.util
_spec = importlib.util.spec_from_file_location('renames', os.path.join(os.path.dirname(os.path.abspath(__file__)), 'renames.py'))
renames = importlib.util.module_from_spec(_spec); _spec.loader.exec_module(renames)
def LP(p):
    p = os.path.abspath(p)
    bs = chr(92)
    return bs * 2 + '?' + bs + p if os.name == 'nt' and not p.startswith(bs * 2) else p
def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()
def wr(p, s): io.open(LP(p), 'w', encoding='utf-8', newline='').write(s)

MB = MOD + '/' + renames.mod_package_dir(MOD)
MASSETS = MOD + '/core/src/main/assets/'
ASSETS = ROOT + '/core/src/main/assets/'
OURS = ROOT + '/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/Assets.java'
mod_src = rd(MB + 'Assets.java')

out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
refs = set()
for l in out.splitlines():
    if l.startswith('??') and l.strip().endswith('.java'):
        s = rd(ROOT + '/' + l[3:].strip())
        for m in re.finditer(r'\bAssets\.(Sprites|Sounds|Music|Environment|Interfaces|Effects|Splashes)\.(\w+)', s):
            refs.add((m.group(1), m.group(2)))

def has(section, const):
    s = rd(OURS)
    m2 = re.search(r'public static class ' + section + r'\s*\{', s)
    if not m2: return True
    body = s[m2.end():].split('public static class')[0]
    return re.search(r'\b' + re.escape(const) + r'\s*=', body) is not None

added = []; missing = []
for section, const in sorted(refs):
    if has(section, const): continue
    ms = re.search(r'public static class ' + section + r'\s*\{', mod_src)
    body = ''
    if ms:
        nxt = mod_src.find('public static class', ms.end())
        body = mod_src[ms.end(): nxt if nxt != -1 else len(mod_src)]
    m = re.search(r'public static final (?:String|Object)\s+' + re.escape(const) + r'\s*=\s*"([^"]+)"', body)
    if not m:
        flat = ('SND_' + const) if section == 'Sounds' else const
        m = re.search(r'public static final String\s+' + re.escape(flat) + r'\s*=\s*"([^"]+)"', mod_src)
    if not m or not os.path.exists(LP(MASSETS + m.group(1))):
        missing.append(section + '.' + const); continue
    path = m.group(1)
    d, f = os.path.split(path)
    if not d: d = 'sounds' if section == 'Sounds' else 'sprites'
    newpath = d + '/' + TAG + '/' + f if '/' + TAG not in d else path
    os.makedirs(os.path.dirname(ASSETS + newpath), exist_ok=True)
    shutil.copy(LP(MASSETS + path), ASSETS + newpath)
    s = rd(OURS); nl = '\r\n' if '\r\n' in s else '\n'
    m2 = re.search(r'public static class ' + section + r'\s*\{', s)
    s = s[:m2.end()] + nl + '\t\tpublic static final String %s = "%s"; //ported, %s' % (const, newpath, TAG) + s[m2.end():]
    wr(OURS, s)
    added.append(const)
print('added', len(added), 'constants')
if missing: print('missing in the mod:', ', '.join(missing))

# literal file names written straight into new code, like texture("sprites/SRPD/gunman.png")
lit = re.compile(r'"((?:sprites|sounds|effects|environment|interfaces|music)/[^"\s]+\.(?:png|ogg|mp3|wav))"')
moved = 0; lmissing = []
for l in out.splitlines():
    if l.startswith('??') and l.strip().endswith('.java'):
        p = ROOT + '/' + l[3:].strip()
        s = rd(p); t = s
        for m in set(lit.findall(s)):
            if os.path.exists(LP(ASSETS + m)) or ('/' + TAG + '/') in m: continue
            if not os.path.exists(LP(MASSETS + m)):
                lmissing.append(m); continue
            d, f = os.path.split(m)
            newpath = d + '/' + TAG + '/' + f
            os.makedirs(os.path.dirname(ASSETS + newpath), exist_ok=True)
            shutil.copy(LP(MASSETS + m), ASSETS + newpath)
            t = t.replace('"' + m + '"', '"' + newpath + '"'); moved += 1
        if t != s: wr(p, t)
print('literal asset paths moved', moved)
if lmissing: print('literal paths not in the mod:', ', '.join(sorted(set(lmissing))))
