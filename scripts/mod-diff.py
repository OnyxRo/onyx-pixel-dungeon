"""moddiff.py <mod dir> <base dir> <mod package path> <out file>
Lists new and changed files of a mod against its Shattered base, with diff sizes.
Writes full unified diffs of changed java files to <out file>.diff"""
import sys, os, difflib, re

mod, base, pkg, out = sys.argv[1:5]
SP = 'com/shatteredpixel/shatteredpixeldungeon'
pkgdot = pkg.replace('/', '.')

def walk(root):
    res = {}
    for d, _, fs in os.walk(root):
        if '.git' in d or os.sep + 'build' + os.sep in d + os.sep:
            continue
        for f in fs:
            p = os.path.relpath(os.path.join(d, f), root).replace(os.sep, '/')
            res[p.replace(pkg, SP)] = p
    return res

m = walk(mod)
b = walk(base)
lines = []
diffs = []
new, changed = [], []
for k in sorted(m):
    mp = os.path.join(mod, m[k])
    if k not in b:
        new.append(k)
        continue
    bp = os.path.join(base, b[k])
    if k.endswith(('.java', '.properties', '.gradle', '.xml', '.txt')):
        try:
            mt = open(mp, encoding='utf-8', errors='replace').read().replace(pkgdot, 'com.shatteredpixel.shatteredpixeldungeon').replace('\r\n', '\n').splitlines()
            bt = open(bp, encoding='utf-8', errors='replace').read().replace('\r\n', '\n').splitlines()
        except Exception as e:
            continue
        if mt != bt:
            d = list(difflib.unified_diff(bt, mt, 'base/' + k, 'mod/' + k, n=2, lineterm=''))
            n = sum(1 for x in d if x[:1] in '+-' and not x.startswith(('+++', '---')))
            changed.append((k, n))
            if k.endswith('.java') or k.endswith('.properties'):
                diffs.extend(d)
    else:
        if open(mp, 'rb').read() != open(bp, 'rb').read():
            changed.append((k, -1))
removed = [k for k in b if k not in m and '/java/' in k]
with open(out, 'w', encoding='utf-8') as f:
    f.write('NEW %d\n' % len(new))
    for k in new: f.write('  + ' + k + '\n')
    f.write('CHANGED %d (total diff lines %d)\n' % (len(changed), sum(max(0, n) for _, n in changed)))
    for k, n in changed: f.write('  ~ %s %d\n' % (k, n))
    f.write('REMOVED java %d\n' % len(removed))
    for k in removed: f.write('  - ' + k + '\n')
with open(out + '.diff', 'w', encoding='utf-8') as f:
    f.write('\n'.join(diffs))
print(out, 'new', len(new), 'changed', len(changed), 'difflines', sum(max(0, n) for _, n in changed), 'removed', len(removed))
