"""Prints build/compile-errors.log as one line per error, 'File:line message | source line'. Optional argument, a file name filter."""
import re, sys
flt = sys.argv[1] if len(sys.argv) > 1 else ''
t = open(sys.argv[2] if len(sys.argv) > 2 else 'build/check.log', encoding='utf-8', errors='replace').read().split('\n')
for i, l in enumerate(t):
    m = re.search(r'shatteredpixeldungeon[\\/](.*?\.java):(\d+): error: (.*)', l)
    if m and flt in m.group(1):
        f = m.group(1).replace('\\', '/').split('/')[-1]
        print('%s:%s %s | %s' % (f, m.group(2), m.group(3)[:80], t[i + 1].strip()[:100] if i + 1 < len(t) else ''))
"""Compact list of javac errors, one line each, from build/compile-errors.log or build/fast.log.
  python -I scripts/errsum.py [log] > build/errs.txt
Each line is File.java:line message, then the missing symbol or the required types."""
import re, sys
log = sys.argv[1] if len(sys.argv) > 1 else 'build/fast.log'
t = open(log, encoding='utf-8', errors='replace').read().split('\n')
n = 0
for i, l in enumerate(t):
    m = re.match(r'.*[\\/]([A-Za-z0-9_]+\.java):(\d+): error: (.*)', l)
    if not m: continue
    sym = ''
    for k in (1, 2, 3, 4, 5):
        if i + k < len(t) and ('symbol:' in t[i + k] or 'required:' in t[i + k]):
            sym = t[i + k].strip(); break
    print('%s:%s %s | %s' % (m.group(1), m.group(2), m.group(3)[:70], sym[:80])); n += 1
print(n, 'errors')
