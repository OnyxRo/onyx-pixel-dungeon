"""Compares a mod that was uploaded in one commit against the Shattered tag it was built on.

  python -I scripts/mod-diff.py <shattered tag folder> <mod folder> <out prefix>

Files are paired by their path below the package root (com/shatteredpixel/shatteredpixeldungeon,
or the mod's own package), so a renamed package still lines up. Package names, imports and
whitespace are ignored. Writes <out>-summary.txt (new, changed and removed files with changed
line counts, png files that differ) and <out>.patch (a unified diff of the changed Java files).
"""
import difflib, hashlib, os, re, sys

base, mod, out = sys.argv[1], sys.argv[2], sys.argv[3]

def index(root):
    files = {}
    for d, dirs, fs in os.walk(root):
        dirs[:] = [x for x in dirs if x not in ('.git', 'build', 'PD-classes', 'watabou')]
        for f in fs:
            p = os.path.join(d, f)
            rel = os.path.relpath(p, root).replace('\\', '/')
            if f.endswith('.java'):
                m = re.search(r'src/(?:main/java/)?com/(.*)$', rel)
                if not m: continue
                parts = m.group(1).split('/')[1:]
                if len(parts) > 1 and parts[0].endswith('pixeldungeon'):
                    parts = parts[1:]
                files['java/' + '/'.join(parts)] = p
            elif f.endswith('.png'):
                files['png/' + f] = p
            elif f.endswith('.properties') and 'messages' in rel:
                files['msg/' + f] = p
    return files

def norm(p):
    t = open(p, encoding='utf-8', errors='replace').read()
    lines = []
    for l in t.splitlines():
        s = l.strip()
        if not s or s.startswith(('import ', 'package ', '*', '/*', '//')): continue
        s = re.sub(r'\s+', ' ', s)
        s = re.sub(r'com\.[a-z]+(\.[a-z]+)?pixeldungeon|com\.[a-z]+\.[a-z]+', 'PKG', s)
        lines.append(s)
    return lines

def digest(p):
    return hashlib.md5(open(p, 'rb').read()).hexdigest()

b, m = index(base), index(mod)
summary, patch = [], []
for k in sorted(set(b) | set(m)):
    if k not in b:
        n = len(norm(m[k])) if k.startswith(('java', 'msg')) else 0
        summary.append('NEW      %s %s' % (k, n or ''))
        if k.startswith('java'):
            patch.append('### NEW %s\n%s\n' % (k, '\n'.join(norm(m[k]))))
    elif k not in m:
        summary.append('REMOVED  %s' % k)
    elif k.startswith('png'):
        if digest(b[k]) != digest(m[k]):
            summary.append('PNG      %s' % k)
    else:
        a, c = norm(b[k]), norm(m[k])
        if a != c:
            d = list(difflib.unified_diff(a, c, k, k, n=1, lineterm=''))
            ch = sum(1 for l in d if l[:1] in '+-' and not l.startswith(('+++', '---')))
            summary.append('CHANGED  %s %d' % (k, ch))
            patch.append('\n'.join(d) + '\n')
open(out + '-summary.txt', 'w', encoding='utf-8').write('\n'.join(summary) + '\n')
open(out + '.patch', 'w', encoding='utf-8').write('\n'.join(patch))
kinds = {}
for s in summary:
    kinds[s.split()[0]] = kinds.get(s.split()[0], 0) + 1
print(kinds)
