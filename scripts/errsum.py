"""Prints build/compile-errors.log as one line per error, 'File:line message | source line'. Optional argument, a file name filter."""
import re, sys
flt = sys.argv[1] if len(sys.argv) > 1 else ''
t = open(sys.argv[2] if len(sys.argv) > 2 else 'build/check.log', encoding='utf-8', errors='replace').read().split('\n')
for i, l in enumerate(t):
    m = re.search(r'shatteredpixeldungeon[\\/](.*?\.java):(\d+): error: (.*)', l)
    if m and flt in m.group(1):
        f = m.group(1).replace('\\', '/').split('/')[-1]
        print('%s:%s %s | %s' % (f, m.group(2), m.group(3)[:80], t[i + 1].strip()[:100] if i + 1 < len(t) else ''))
