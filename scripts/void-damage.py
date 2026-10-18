"""Rewrites old style `public int damage(int dmg, Object src)` overrides to this game's void damage.
Old Shattered (0.8 and 0.9) returned the damage taken, 4.0 returns nothing. Inside the method,
`return super.damage(...)` becomes `{ super.damage(...); return; }` and any other `return x;` becomes `return;`.
Lambdas and inner classes inside the method are left alone only if they have no returns, check the diff.
  python -I scripts/void-damage.py File.java ...
"""
import sys, re, io

SIG = re.compile(r'public\s+int\s+damage\s*\(\s*int\s+\w+\s*,\s*Object\s+\w+\s*\)')

def body_span(s, start):
    i = s.index('{', start)
    depth = 0
    for j in range(i, len(s)):
        if s[j] == '{': depth += 1
        elif s[j] == '}':
            depth -= 1
            if depth == 0: return i, j
    raise ValueError('no end')

def fix(s):
    out, pos, n = [], 0, 0
    for m in SIG.finditer(s):
        if m.start() < pos: continue
        a, b = body_span(s, m.end())
        body = s[a:b+1]
        body = re.sub(r'return\s+super\.damage\s*\(([^;]*)\)\s*;', r'{ super.damage(\1); return; }', body)
        body = re.sub(r'return\s+[^;{}]+;', 'return;', body)
        out.append(s[pos:m.start()])
        out.append(m.group(0).replace('public int', 'public void', 1))
        out.append(s[m.end():a])
        out.append(body)
        pos = b + 1
        n += 1
    out.append(s[pos:])
    return ''.join(out), n

for f in sys.argv[1:]:
    s = io.open(f, encoding='utf-8', newline='').read()
    t, n = fix(s)
    if n:
        io.open(f, 'w', encoding='utf-8', newline='').write(t)
        print(f, n)
