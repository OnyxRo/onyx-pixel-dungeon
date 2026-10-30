"""Resolves merge conflicts by keeping both sides, ours first, for files where both sides only appended lines.
  python -I scripts/keep-both.py FILE...
Read the result afterwards, it is only right for plain appended lines."""
import sys
for p in sys.argv[1:]:
    raw = open(p, 'rb').read().decode('utf-8')
    out = [l for l in raw.splitlines(True) if not l.startswith(('<<<<<<< ', '=======', '>>>>>>> '))
           or (l.startswith('=======') and l.strip() != '=======')]
    open(p, 'wb').write(''.join(out).encode('utf-8'))
    print(p, raw.count('\n<<<<<<< ') + raw.startswith('<<<<<<< '), 'conflicts kept both')
