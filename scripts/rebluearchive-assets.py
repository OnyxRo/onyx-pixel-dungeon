"""Adds Assets.<Group>.RBA_X for every RBA_ asset a copy names, copies the file under <dir>/rebluearchive/."""
import io, os, re, shutil, sys
sys.path.insert(0, 'scripts')
import importlib.util
spec = importlib.util.spec_from_file_location('rbacopy', 'scripts/rebluearchive-copy.py')
C = importlib.util.module_from_spec(spec); spec.loader.exec_module(C)
A = C.names()['assets']
B = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
MA = os.path.expanduser('~/mod-sources/rebluearchive/core/src/main/assets/')
OA = 'core/src/main/assets/'
used = set()
for d, _, fs in os.walk(B):
    for f in fs:
        if f.endswith('.java'):
            s = io.open(os.path.join(d, f), encoding='utf-8', errors='replace').read()
            used.update(re.findall(r'Assets\.(\w+)\.RBA_(\w+)', s))
p = B + 'Assets.java'
s = io.open(p, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in s else '\n'
add = {}
for g, k in sorted(used):
    if ('RBA_' + k) in s: continue
    v = A.get(g, {}).get(k)
    if v is None: print('no mod value for', g, k); continue
    parts = v.split('/')
    nv = '/'.join([parts[0], 'rebluearchive'] + parts[1:])
    if not os.path.exists(MA + v): print('missing file', v); continue
    os.makedirs(os.path.dirname(OA + nv), exist_ok=True)
    shutil.copyfile(MA + v, OA + nv)
    add.setdefault(g, []).append('\t\tpublic static final String RBA_%s = "%s"; //rebluearchive' % (k, nv))
for g, lines in add.items():
    m = re.search(r'public static class ' + g + r'\s*\{[^\n]*\n', s)
    s = s[:m.end()] + nl.join(lines) + nl + s[m.end():]
    print(g, len(lines))
io.open(p, 'w', encoding='utf-8', newline='').write(s)
