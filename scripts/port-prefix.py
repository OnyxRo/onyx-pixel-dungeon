"""Ports classes from a mod under a new name, for mod classes that share a name with one of Shattered's own.

  python -I scripts/port-prefix.py <mod key or folder> <Prefix> Name1 Name2 ... [--no-deps]

Why. Every other port tool skips a class when this game already has one with the same name, so a mod's own
CavesBossLevel, King or Yog is never copied and never shows in mod-report. This tool copies them as
<Prefix>Name (CursedCavesBossLevel), and renames every reference to them inside the copied files, so they sit
next to Shattered's version. It also copies each class's text from the mod's message files under the new key
(levels.cursedcavesbosslevel.*), and then runs port-copier.py for whatever else the copies need.

A name can be given as a path under the mod's package to pick one when the mod has several files of that name,
for example levels/CavesBossLevel. The copies get renames.py applied, like every other port.
After it runs, register the classes yourself (WorldShuffle.modBossLevel for boss levels, ModRoster for monsters).
"""
import sys, os, io, re, glob, subprocess

args = [a for a in sys.argv[1:] if not a.startswith('--')]
opts = [a for a in sys.argv[1:] if a.startswith('--')]
if len(args) < 3:
    raise SystemExit(__doc__)
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..')).replace(chr(92), '/') + '/'
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import renames

mod_arg, PREFIX, wanted = args[0], args[1], args[2:]
MODS = os.environ.get('MOD_SOURCES', os.path.expanduser('~/mod-sources'))
MOD = mod_arg if os.path.isdir(mod_arg) else os.path.join(MODS, mod_arg)
if not os.path.isdir(MOD):
    raise SystemExit('no mod folder at ' + MOD + ', try scripts/get-mod.sh ' + mod_arg)
MOD = MOD.replace(chr(92), '/').rstrip('/') + '/'
MODNAME = os.path.basename(MOD.rstrip('/'))
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
MSG = 'core/src/main/assets/messages/'
MB = MOD + renames.mod_package_dir(MOD)

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92) * 2) else chr(92) * 2 + '?' + chr(92) + p
def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()
def wr(p, s):
    os.makedirs(os.path.dirname(LP(p)), exist_ok=True)
    io.open(LP(p), 'w', encoding='utf-8', newline='').write(s)

# find the mod file for each wanted name
index = {}
for f in glob.glob(LP(MB) + '/**/*.java', recursive=True):
    rel = os.path.relpath(f, LP(MB)).replace(os.sep, '/')
    index.setdefault(os.path.basename(rel)[:-5], []).append(rel)
picked = {}   # simple name -> relative path in the mod
for w in wanted:
    name = os.path.basename(w)
    cands = index.get(name, [])
    if '/' in w: cands = [c for c in cands if c[:-5].endswith(w)]
    if not cands: raise SystemExit('not found in the mod: ' + w)
    if len(cands) > 1: raise SystemExit('several files named %s, give a path like levels/%s. Found: %s' % (name, name, ', '.join(cands)))
    picked[name] = cands[0]

names = sorted(picked, key=len, reverse=True)
word = re.compile(r'"(?:\\.|[^"\\\n])*"|\b(' + '|'.join(re.escape(n) for n in names) + r')\b')
def rename_refs(src):
    # keep string literals as they are, rename identifiers
    return word.sub(lambda m: m.group(0) if m.group(1) is None else PREFIX + m.group(1), src)

copied = []
for name, rel in picked.items():
    dst = ROOT + PK + os.path.dirname(rel) + ('/' if os.path.dirname(rel) else '') + PREFIX + name + '.java'
    if os.path.exists(LP(dst)):
        print('already there, skipped', PREFIX + name); continue
    s = rd(MB + rel)
    s = re.sub(r'package\s+[\w.]+;', 'package com.shatteredpixel.shatteredpixeldungeon.' + os.path.dirname(rel).replace('/', '.') + ';', s, count=1)
    s, _ = renames.apply(s)
    s = rename_refs(s)
    wr(dst, s)
    copied.append(rel)
    print('copied', rel, '->', PREFIX + name)

# text, copied under the new key
total = 0
for name, rel in picked.items():
    if rel not in copied: continue
    parts = rel[:-5].split('/')
    bundle = parts[0]
    old_key = rel[:-5].replace('/', '.').lower()
    new_key = (rel[:-5][:-len(name)] + PREFIX + name).replace('/', '.').lower()
    src = MOD + MSG + bundle + '/' + bundle + '.properties'
    if not os.path.exists(LP(src)):
        hits = glob.glob(LP(MOD + 'core/src/main/resources') + '/**/messages/' + bundle + '/' + bundle + '.properties', recursive=True)
        if hits: src = hits[0]
    dst = ROOT + MSG + bundle + '/' + bundle + '.properties'
    if not os.path.exists(LP(src)) or not os.path.exists(LP(dst)): continue
    cur = rd(dst); nl = '\r\n' if '\r\n' in cur else '\n'
    have = set(re.findall(r'^([^#=\s][^=]*)=', cur, flags=re.M))
    add = []
    for line in rd(src).replace('\r\n', '\n').split('\n'):
        m = re.match(r'([^#=\s][^=]*)=(.*)', line)
        if not m: continue
        k = m.group(1)
        if k == old_key or k.startswith(old_key + '.') or k.startswith(old_key + '$'):
            nk = new_key + k[len(old_key):]
            if nk not in have:
                add.append(nk + '=' + m.group(2)); have.add(nk)
    if add:
        wr(dst, cur.rstrip('\r\n') + nl + nl + '###' + MODNAME + ' ' + PREFIX + name + nl + nl.join(add) + nl)
        total += len(add)
print('strings added', total)

if copied and '--no-deps' not in opts:
    print('closing dependencies with port-copier ...')
    r = subprocess.run([sys.executable, '-I', 'scripts/port-copier.py', '.', MOD, MODNAME, '8'], cwd=ROOT, capture_output=True, text=True, errors='replace')
    tail = r.stdout[r.stdout.rfind('COPIED:'):] if 'COPIED:' in r.stdout else r.stdout[-1500:]
    print(tail[-2500:])
