"""copyall.py <diff file> <mod java root> <mod package> [skip names...]
Copies every NEW JAVA class of a moddiff report whose simple name this game lacks, package renamed."""
import os, re, sys
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
OURS = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
diff, modroot, modpkg = sys.argv[1:4]
skip = set(sys.argv[4:])
have = set()
for r, ds, fs in os.walk(os.path.join(ROOT, 'core', 'src', 'main', 'java')):
    for f in fs:
        if f.endswith('.java'): have.add(f[:-5])
lines = open(diff, encoding='utf-8').read().split('\n')
i = lines.index('NEW JAVA ' + lines[0].split()[-1]) + 1 if lines[0].startswith('NEW JAVA') else 1
copied, skipped = [], []
for l in lines[1:]:
    if not l.startswith('  '): break
    rel = l.split()[0]
    name = os.path.basename(rel)[:-5]
    if name in have or name in skip or rel.startswith('ui/changelist'):
        skipped.append(name); continue
    src = os.path.join(modroot, *rel.split('/'))
    if not os.path.exists(src):
        skipped.append(name + '(missing)'); continue
    t = open(src, encoding='utf-8', errors='replace').read()
    t = t.replace(modpkg, 'com.shatteredpixel.shatteredpixeldungeon')
    dst = os.path.join(OURS, *rel.split('/'))
    os.makedirs(os.path.dirname(dst), exist_ok=True)
    open(dst, 'w', encoding='utf-8', newline='').write(t)
    copied.append(name)
print('copied', len(copied), ' '.join(copied))
print('skipped', len(skipped), ' '.join(skipped))
