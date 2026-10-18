"""Prints a few lines around each place a compile error names.
  python -I scripts/ctx.py [build/fc.sum] [width]
Reads the compact error list from errsum.py, finds each file under core/src/main/java and shows the lines around it."""
import os
import re
import sys

sumfile = sys.argv[1] if len(sys.argv) > 1 else 'build/fc.sum'
w = int(sys.argv[2]) if len(sys.argv) > 2 else 2
index = {}
for d, _, fs in os.walk('core/src/main/java'):
    for f in fs:
        if f.endswith('.java'):
            index.setdefault(f, []).append(os.path.join(d, f))
seen = set()
for l in open(sumfile, encoding='utf-8', errors='replace'):
    m = re.match(r'([A-Za-z0-9_]+\.java):(\d+) (.*)', l.strip())
    if not m or (m.group(1), m.group(2)) in seen:
        continue
    seen.add((m.group(1), m.group(2)))
    for p in index.get(m.group(1), [])[:1]:
        n = int(m.group(2))
        lines = open(p, encoding='utf-8', errors='replace').read().split('\n')
        print('=== %s:%d %s' % (m.group(1), n, m.group(3)[:90]))
        for i in range(max(0, n - 1 - w), min(len(lines), n + w)):
            print('%5d %s' % (i + 1, lines[i].rstrip()[:170]))
