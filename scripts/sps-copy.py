"""Copies SPS classes (and re-copies them after a rule change) with scripts/renames.py applied.

  python -I scripts/sps-copy.py actors/damagetype/DamageType actors/blobs/effectblobs/AcidWater ...   copy by path under the package
  python -I scripts/sps-copy.py --redo                 copy again every untracked java file that has an SPS twin (after a rule change)
  python -I scripts/sps-copy.py --dir actors/damagetype  copy a whole folder
Files are read from the SPS clone (~/mod-sources/sps) and written to the same relative path in this game.
A class is never copied over a tracked file."""
import sys, os, io, subprocess, importlib.util

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
SRC = os.path.expanduser('~/mod-sources/sps/java/com/hmdzl/spspd/')
DST = os.path.join(ROOT, 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon') + os.sep
spec = importlib.util.spec_from_file_location('renames', os.path.join(ROOT, 'scripts', 'renames.py'))
renames = importlib.util.module_from_spec(spec); spec.loader.exec_module(renames)

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92) * 2) else chr(92) * 2 + '?' + chr(92) + p

def tracked(rel):
    r = subprocess.run(['git', 'ls-files', '--error-unmatch', 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/' + rel],
                       cwd=ROOT, capture_output=True)
    return r.returncode == 0

def copy(rel):
    s = os.path.join(SRC, rel + '.java'); d = DST + rel + '.java'
    if not os.path.exists(LP(s)): print('no SPS file', rel); return False
    if tracked(rel + '.java'): print('tracked, skipped', rel); return False
    t = io.open(LP(s), encoding='utf-8', errors='replace', newline='').read()
    t, fired = renames.apply(t)
    os.makedirs(os.path.dirname(d), exist_ok=True)
    io.open(LP(d), 'w', encoding='utf-8', newline='').write(t)
    return True

args = sys.argv[1:]
if args and args[0] == '--redo':
    out = subprocess.run(['git', 'ls-files', '--others', '--exclude-standard', 'core/src/main/java'], cwd=ROOT, capture_output=True, text=True).stdout.split('\n')
    n = 0
    pre = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
    for f in out:
        if f.startswith(pre) and f.endswith('.java'):
            rel = f[len(pre):-5]
            if os.path.exists(LP(os.path.join(SRC, rel + '.java'))) and copy(rel): n += 1
    print('recopied', n)
elif args and args[0] == '--dir':
    n = 0
    for f in sorted(os.listdir(LP(SRC + args[1]))):
        if f.endswith('.java') and copy(args[1].rstrip('/') + '/' + f[:-5]): n += 1
    print('copied', n)
else:
    print('copied', sum(1 for a in args if copy(a)))
