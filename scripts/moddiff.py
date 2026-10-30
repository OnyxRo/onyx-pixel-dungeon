"""moddiff.py <mod dir> <base dir> <out file> [modpkg]
Compares a mod with its Shattered base. Package renamed back, whitespace ignored.
Writes a summary plus unified diffs of changed java files and the list of new and changed assets."""
import os, re, sys, difflib, hashlib

mod, base, out = sys.argv[1:4]
SH = 'com.shatteredpixel.shatteredpixeldungeon'

def walk(root):
    res = {}
    for r, ds, fs in os.walk(root):
        ds[:] = [d for d in ds if d not in ('.git', 'build', '.gradle', 'android', 'ios')]
        for f in fs:
            p = os.path.join(r, f)
            res[os.path.relpath(p, root).replace('\\', '/')] = p
    return res

def norm_java_key(rel):
    # strip down to the part after java/<package dirs>
    m = re.search(r'/java/(.*)$', rel)
    if not m: return None
    parts = m.group(1).split('/')
    # drop the first three package dirs when they are com/x/y
    if parts[0] in ('com',) and len(parts) > 3:
        if parts[1] == 'watabou':
            return 'watabou/' + '/'.join(parts[2:])
        return '/'.join(parts[3:])
    return '/'.join(parts)

def asset_key(rel):
    m = re.search(r'(?:assets|res)/(.*)$', rel)
    return m.group(1) if m else None

mf, bf = walk(mod), walk(base)
modpkg = sys.argv[4] if len(sys.argv) > 4 else None

def read(p):
    t = open(p, encoding='utf-8', errors='replace').read()
    if modpkg: t = t.replace(modpkg, SH)
    lines = [re.sub(r'\s+', ' ', l).strip() for l in t.splitlines()]
    return [l for l in lines if l]

bj = {}; ba = {}
for rel, p in bf.items():
    if rel.endswith('.java'):
        k = norm_java_key(rel)
        if k: bj[k] = p
    else:
        k = asset_key(rel)
        if k: ba[k] = p

newj, chg, newa, chga = [], [], [], []
diffs = []
for rel, p in sorted(mf.items()):
    if rel.endswith('.java'):
        k = norm_java_key(rel)
        if not k: continue
        if k not in bj:
            newj.append((k, sum(1 for _ in open(p, encoding='utf-8', errors='replace'))))
            continue
        a, b = read(bj[k]), read(p)
        if a != b:
            d = list(difflib.unified_diff(a, b, 'base/' + k, 'mod/' + k, n=1, lineterm=''))
            n = sum(1 for l in d if l[:1] in '+-' and not l.startswith(('+++', '---')))
            chg.append((k, n)); diffs.append('\n'.join(d))
    else:
        k = asset_key(rel)
        if not k: continue
        if k not in ba: newa.append(k)
        elif hashlib.md5(open(p, 'rb').read()).digest() != hashlib.md5(open(ba[k], 'rb').read()).digest():
            chga.append(k)

with open(out, 'w', encoding='utf-8') as f:
    f.write('NEW JAVA %d\n' % len(newj))
    for k, n in newj: f.write('  %s %d\n' % (k, n))
    f.write('CHANGED JAVA %d\n' % len(chg))
    for k, n in sorted(chg, key=lambda x: -x[1]): f.write('  %s %d\n' % (k, n))
    f.write('NEW ASSETS %d\n' % len(newa))
    for k in newa: f.write('  %s\n' % k)
    f.write('CHANGED ASSETS %d\n' % len(chga))
    for k in chga: f.write('  %s\n' % k)
    f.write('\n\n')
    f.write('\n\n'.join(diffs))
print(out, 'new java', len(newj), 'changed', len(chg), 'new assets', len(newa), 'changed assets', len(chga))
