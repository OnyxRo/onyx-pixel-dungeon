"""Copies the songs found by fetch-mod-music.py into core/src/main/assets/music/mods and writes
music/mods/index.txt (path, mod, song name, tab separated) for the Songs window.
Skips duplicates (by hash, also against the game's own music), tiny files that are sound effects,
. Usage: python -I scripts/import-mod-music.py <mods-music dir>"""
import json, os, re, sys, shutil, hashlib, urllib.parse
src = sys.argv[1]
root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
music = os.path.join(root, 'core', 'src', 'main', 'assets', 'music')
out = os.path.join(music, 'mods')
seen = set()
stems = set()
for d, _, fs in os.walk(music):
    if os.path.abspath(d).startswith(os.path.abspath(out)): continue
    for f in fs:
        seen.add(hashlib.sha1(open(os.path.join(d, f), 'rb').read()).hexdigest())
        stems.add(os.path.splitext(f)[0].lower())
if os.path.isdir(out): shutil.rmtree(out)
os.makedirs(out)
def words(s):
    s = re.sub(r'^[0-9.]+[-_ ]*', '', s).replace('_', ' ').replace('-', ' ')
    s = re.sub(r'(?<=[a-z])(?=[A-Z])', ' ', s)
    s = re.sub(r'\s+', ' ', s).strip()
    return s[:1].upper() + s[1:] if s else 'Song'
lines = []; used = {}
for m in json.load(open(os.path.join(src, '_result.json'))):
    for f in m['files']:
        p = os.path.join(src, m['slug'], f)
        if not os.path.isfile(p) or os.path.getsize(p) < 150000: continue
        h = hashlib.sha1(open(p, 'rb').read()).hexdigest()
        if h in seen: continue
        #the mp3 copies of the game's own ogg songs
        if os.path.splitext(os.path.basename(f))[0].lower() in stems: continue
        seen.add(h)
        stem, ext = os.path.splitext(os.path.basename(f))
        name = words(stem)
        parent = os.path.basename(os.path.dirname(f))
        if parent.lower() not in ('music', 'assets', 'ogg', 'raw', 'sounds', 'mp3', ''):
            name += ' (' + words(parent) + ')'
        key = (m['slug'], name)
        used[key] = used.get(key, 0) + 1
        if used[key] > 1: name += ' ' + str(used[key])
        safe = re.sub(r'[^A-Za-z0-9]+', '_', stem).strip('_')[:40] or 'song'
        folder = os.path.join(out, m['slug']); os.makedirs(folder, exist_ok=True)
        dest = os.path.join(folder, '%s_%s%s' % (safe, h[:6], ext.lower()))
        shutil.copyfile(p, dest)
        rel = 'music/mods/%s/%s' % (m['slug'], os.path.basename(dest))
        lines.append('%s\t%s\t%s' % (rel, m['title'], name))
open(os.path.join(out, 'index.txt'), 'w', encoding='utf8').write('\n'.join(lines) + '\n')
print(len(lines), 'songs')
