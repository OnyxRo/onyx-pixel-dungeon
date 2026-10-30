"""flatassets.py <mod key> <assets dir in mod> NAME...  copies each flat Assets constant's file of an old mod
to core/src/main/assets/<key>/ and rewrites Assets.NAME and Assets.Sprites.NAME in the worktree's new files to the path."""
import os, re, sys, shutil, subprocess
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
key, adir = sys.argv[1], sys.argv[2]
names = sys.argv[3:]
mod = os.path.expanduser('~/mod-sources/' + key)
assets_java = None
for r, ds, fs in os.walk(os.path.join(mod, 'core')):
    if 'Assets.java' in fs: assets_java = os.path.join(r, 'Assets.java'); break
at = open(assets_java, encoding='utf-8').read()
os.chdir(ROOT)
out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], capture_output=True, text=True).stdout
files = [l[3:].strip() for l in out.splitlines() if l.startswith('??') and l.strip().endswith('.java')]
dst_dir = os.path.join(ROOT, 'core', 'src', 'main', 'assets', key)
os.makedirs(dst_dir, exist_ok=True)
for n in names:
    m = re.search(r'\b' + n + r'\s*=\s*"([^"]+)"', at)
    if not m: print('no constant', n); continue
    val = m.group(1)
    src = os.path.join(adir, *val.split('/'))
    if not os.path.exists(src): print('no file', src); continue
    dst = os.path.join(dst_dir, *val.split('/'))
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    shutil.copy(src, dst)
    path = key + '/' + val
    for f in files:
        t = open(f, encoding='utf-8', newline='').read()
        u = re.sub(r'Assets\.(?:Sprites\.|Environment\.|Interfaces\.)?' + n + r'\b', '"' + path + '"', t)
        if u != t: open(f, 'w', encoding='utf-8', newline='').write(u)
    print(n, '->', path)
