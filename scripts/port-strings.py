"""Copies the English text for newly ported classes from a mod's message files.

  python strings.py <project root> <mod root> <label for the section comment>

Looks at every java file git sees as new or changed under the game package, works out the
message key prefix for each class (and its inner classes), and appends any keys with that
prefix that the project's own bundle does not have yet.
"""
import sys, os, io, re, glob, subprocess
ROOT = sys.argv[1].rstrip('/') + '/'
MOD = sys.argv[2].rstrip('/') + '/'
LABEL = sys.argv[3]
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
MSG = 'core/src/main/assets/messages/'

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92) * 2) else chr(92) * 2 + '?' + chr(92) + p
def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()

out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
files = [l[3:].strip() for l in out.split('\n') if l.strip().endswith('.java') and PK in l]
extra = sys.argv[4:]
for e in extra: files += [p.replace(os.sep, '/')[len(ROOT):] for p in glob.glob(ROOT + PK + e)]
prefixes = {}
for f in sorted(set(files)):
    rel = f[len(PK):-5]
    key = rel.replace('/', '.').lower()
    prefixes.setdefault(key.split('.')[0], set()).add(key)

total = 0
for bundle, keys in prefixes.items():
    src = MOD + MSG + bundle + '/' + bundle + '.properties'
    if not os.path.exists(LP(src)):
        # older mods keep text under resources/<package path>/messages
        hits = glob.glob(LP(MOD + 'core/src/main/resources') + '/**/messages/' + bundle + '/' + bundle + '.properties', recursive=True)
        if hits: src = hits[0]
    dst = ROOT + MSG + bundle + '/' + bundle + '.properties'
    if not os.path.exists(LP(src)) or not os.path.exists(dst): continue
    s = rd(dst); nl = '\r\n' if '\r\n' in s else '\n'
    have = set(re.findall(r'^([^#=\s][^=]*)=', s, flags=re.M))
    add = []
    for line in rd(src).replace('\r\n', '\n').split('\n'):
        m = re.match(r'([^#=\s][^=]*)=', line)
        if not m: continue
        k = m.group(1)
        if k in have: continue
        if any(k.startswith(p + '.') or k.startswith(p + '$') for p in keys):
            add.append(line); have.add(k)
    if add:
        s = s.rstrip('\r\n') + nl + nl + '###' + LABEL + nl + nl.join(add) + nl
        io.open(dst, 'w', encoding='utf-8', newline='').write(s)
        total += len(add)
        print(bundle, len(add))
print('strings added', total)
