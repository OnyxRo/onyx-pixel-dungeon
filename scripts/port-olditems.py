"""Moves item names and descriptions written in code (Shattered 0.3 era mods) into items.properties, in place.

  python -I scripts/port-olditems.py items/food/Yumyuck items/rings/RingOfMagic ...

Paths are relative to the game's package folder, without .java. For each class it
  - turns `name = "..."` in the initializer into the .name property and removes the line
  - turns a desc() or info() whose body is only `return <string literals>;` into the .desc property and removes the method
  - turns enchDesc() the same way (Shattered 4 enchantments use the .desc key)
Strings built with code stay in place, fix those by hand. Safe to run twice.
"""
import sys, os, re, io
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
PK = ROOT + '/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
PROPS = ROOT + '/core/src/main/assets/messages/items/items.properties'
def rd(p): return io.open(p, encoding='utf-8', newline='').read()
def wr(p, s): io.open(p, 'w', encoding='utf-8', newline='').write(s)

def jstr(expr):
    parts = re.findall(r'"((?:[^"\\]|\\.)*)"', expr)
    out = ''.join(parts)
    out = out.replace('\\"', '"').replace("\\'", "'").replace('\\\\', '\\')
    return out.replace('\r\n', '\\n')

def set_prop(props, key, val):
    pat = re.compile(r'^' + re.escape(key) + r'=.*$', re.M)
    line = key + '=' + val
    if pat.search(props):
        return pat.sub(lambda m: line, props, count=1)
    return props.rstrip('\n') + '\n' + line + '\n'

def block_end(s, i):
    j = s.index('{', i); d = 0
    while True:
        c = s[j]
        if c == '{': d += 1
        elif c == '}':
            d -= 1
            if d == 0: return j + 1
        j += 1

def convert(rel, props):
    path = PK + rel + '.java'
    if not os.path.exists(path): print('missing', rel); return props
    s = rd(path)
    nl = '\r\n' if '\r\n' in s else '\n'
    s = s.replace('\r\n', '\n')
    key = 'items.' + rel[len('items/'):].lower().replace('/', '.')
    done = []
    m = re.search(r'\n[ \t]*name\s*=\s*("(?:[^"\\]|\\.)*"(?:\s*\+\s*"(?:[^"\\]|\\.)*")*)\s*;', s)
    if m:
        props = set_prop(props, key + '.name', jstr(m.group(1)))
        s = s.replace(m.group(0), '', 1); done.append('name')
    for meth in ('desc', 'info', 'enchDesc'):
        m = re.search(r'\n[ \t]*(?:@Override\s*\n[ \t]*)?public\s+String\s+' + meth + r'\(\s*\)\s*\{', s)
        if not m: continue
        e = block_end(s, m.end() - 1)
        body = s[m.end():e - 1].strip()
        r = re.fullmatch(r'return\s*((?:"(?:[^"\\]|\\.)*"\s*\+?\s*)+);', body, re.S)
        if r:
            props = set_prop(props, key + '.desc', jstr(r.group(1)))
            s = s[:m.start()] + s[e:]; done.append(meth)
    if done:
        wr(path, s.replace('\n', nl)); print('converted', rel, done)
    return props

if __name__ == '__main__':
    props = rd(PROPS)
    for n in sys.argv[1:]:
        props = convert(n, props)
    wr(PROPS, props)
