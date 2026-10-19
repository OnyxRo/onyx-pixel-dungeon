"""Adds an import line to java files that use a class name but do not import it.

  python -I scripts/add-import.py <full.class.Name> <file or folder> [...]

Only files that mention the simple name as a word and lack the import are changed. The line goes right after the
package line, keeping the file's line endings. Folders are searched for .java files.
"""
import io, os, re, sys

full = sys.argv[1]
simple = full.split('.')[-1]
files = []
for a in sys.argv[2:]:
    if os.path.isdir(a):
        for d, _, fs in os.walk(a):
            files += [os.path.join(d, f) for f in fs if f.endswith('.java')]
    else:
        files.append(a)
n = 0
for p in files:
    s = io.open(p, encoding='utf-8', newline='').read()
    if not re.search(r'\b' + simple + r'\b', s): continue
    if re.search(r'import\s+' + re.escape(full) + r'\s*;', s): continue
    pkg = re.search(r'^package\s+([\w.]+)\s*;', s, re.M)
    if pkg and pkg.group(1) + '.' + simple == full: continue
    nl = '\r\n' if '\r\n' in s else '\n'
    s = s[:pkg.end()] + nl + nl + 'import ' + full + ';' + s[pkg.end():]
    io.open(p, 'w', encoding='utf-8', newline='').write(s)
    n += 1
print('added to', n, 'files')
