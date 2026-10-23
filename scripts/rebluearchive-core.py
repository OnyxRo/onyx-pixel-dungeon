"""Builds the pieces of Re Blue Archive PD that live in Shattered's own classes in the mod (2026-10-22).

  python -I scripts/rebluearchive-core.py talents     RBATalents.java (the mod's nested talent buffs) and the enum lines
  python -I scripts/rebluearchive-core.py icons       HeroIcon and BuffIndicator constant lines, numbers from 50000 up

Output lines go to build/rba-*.txt for pasting, RBATalents.java is written in place (only if missing).
"""
import io, os, re, sys, importlib.util
HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location('rbacopy', os.path.join(HERE, 'rebluearchive-copy.py'))
C = importlib.util.module_from_spec(spec); spec.loader.exec_module(C)
OFFSET = 50000

def block(src, start):
    """text of the braced block that opens at or after start, through its closing brace"""
    i = src.index('{', start)
    depth = 0
    for j in range(i, len(src)):
        if src[j] == '{': depth += 1
        elif src[j] == '}':
            depth -= 1
            if depth == 0: return src[start:j + 1]
    raise ValueError('unbalanced')

def talents():
    src = C.rd(os.path.join(C.MOD, 'actors/hero/Talent.java'))
    #nested classes
    parts = []
    for n in C.TALENT_NESTED:
        m = re.search(r'\n(\t(?:public )?static class ' + n + r'\b)', src)
        b = block(src, m.start(1))
        if not b.rstrip().endswith(';'): pass
        parts.append(b)
    imports = re.findall(r'^import [^\n]+;', src, re.M)
    body = ('package com.shatteredpixel.shatteredpixeldungeon.actors.hero;\n\n' + '\n'.join(imports) +
            '\n\n/** Re Blue Archive PD, buffs and trackers the mod kept inside Talent, and its talent hooks. */\n'
            'public class RBATalents {\n\n' + '\n\n'.join(parts) + '\n\n}\n')
    rels = C.plan()
    body = C.rewrite(body, 'actors/hero/RBATalents.java', C.classes(rels))
    #inside the class, its own trackers need no prefix, and the mod's talent names need Talent.
    body = re.sub(r'(?<![\w.])RBATalents\.', '', body)
    N = C.names()
    body = re.sub(r'(?<![\w.])(' + '|'.join(sorted(N['talent'], key=len, reverse=True)) + r')\b(?!\s*\()', r'Talent.RBA_\1', body)
    dst = os.path.join(C.OURS, 'actors', 'hero', 'RBATalents.java')
    if not os.path.exists(dst):
        io.open(dst, 'w', encoding='utf-8', newline='\n').write(body)
        print('wrote', dst)
    #enum lines
    out = []
    for m in re.finditer(r'^\s*([A-Z0-9_]+)\s*\(\s*(-?\d+)\s*,\s*(-?\d+)\s*(?:,\s*(\d+)\s*)?\)', src, re.M):
        n = m.group(1)
        if n not in N['talent']: continue
        x, y, mx = int(m.group(2)), int(m.group(3)), int(m.group(4) or 2)
        out.append('\tRBA_%s(%d, %d),' % (n, OFFSET + x + 32 * y if x >= 0 else OFFSET + 31 + 32 * y, mx))
    io.open('build/rba-talent-enum.txt', 'w', encoding='utf-8').write('\n'.join(out) + '\n')
    print('talents', len(out))

def evalints(src, upto=None):
    if upto and upto in src: src = src[:src.index(upto)]
    vals = {}
    for k, v in re.findall(r'(?:static\s+final\s+int|static\s+int|final\s+static\s+int)\s+(\w+)\s*=\s*([^;]+);', src):
        try:
            vals[k] = eval(re.sub(r'\b([A-Za-z_]\w*)\b', lambda m: str(vals.get(m.group(1), m.group(1))), v))
        except Exception:
            vals[k] = None
    return vals

def icons():
    N = C.names()
    for rel, key, out in [('ui/HeroIcon.java', 'heroicon', 'build/rba-heroicon.txt'), ('ui/BuffIndicator.java', 'buffs', 'build/rba-buffs.txt')]:
        vals = evalints(C.rd(os.path.join(C.MOD, rel)))
        lines = []
        for k in N[key]:
            v = vals.get(k)
            if v is None: print('cannot evaluate', rel, k); continue
            lines.append('\tpublic static final int RBA_%s = RBA + %d;' % (k, v))
        io.open(out, 'w', encoding='utf-8').write('\n'.join(lines) + '\n')
        print(rel, len(lines))

{'talents': talents, 'icons': icons}[sys.argv[1]]()
