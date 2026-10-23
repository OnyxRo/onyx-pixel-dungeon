"""Static check that every sprite class asks for frames its picture really has.

  python -I scripts/check-sprites.py

Why this exists. A sprite that names frame 42 on a picture that only has 42 frames (0 to 41) crashes the game when
the monster is made, with a null frame deep inside MovieClip. The headless tests cannot see it, they have no
graphics. The OOF Thief shipped like that for two days (it borrowed the normal thief sheet, which is one row shorter).

For every file in sprites/ that picks one texture and builds one or more films with
    film = new TextureFilm( texture, W, H )
it reads the picture's size from the PNG header, works out how many frames fit, and checks every number that is
passed to  something.frames( film, ... ).  Sums like  21 + 21  are worked out. Anything it cannot read is skipped.
Prints one line per problem and exits 1 if there were any.
"""
import os, re, struct, sys, io

ROOT = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..'))
PK = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
ASSETS = os.path.join(ROOT, 'core', 'src', 'main', 'assets')

def rd(p):
    return io.open(p, encoding='utf-8', errors='replace').read()

# Assets.<Group>.<NAME> -> file path
consts = {}
group = None
depth = 0
for line in rd(os.path.join(PK, 'Assets.java')).splitlines():
    m = re.search(r'public static class (\w+)', line)
    if m:
        group = m.group(1)
    m = re.search(r'public static final String (\w+)\s*=\s*"([^"]+)"', line)
    if m and group:
        consts[(group, m.group(1))] = m.group(2)

def png_size(path):
    with open(path, 'rb') as f:
        head = f.read(24)
    if head[:8] != b'\x89PNG\r\n\x1a\n':
        return None
    return struct.unpack('>II', head[16:24])

def number(tok, local=None):
    tok = tok.strip()
    # simple local numbers like  int c = 63;  and  final int offset = 17;  are filled in
    if local and tok:
        tok = re.sub(r'[A-Za-z_]\w*', lambda m: str(local[m.group(0)]) if m.group(0) in local else m.group(0), tok)
    if not tok or re.search(r'[A-Za-z_]', tok):
        return None
    if not re.fullmatch(r'[\d\s+\-]+', tok):
        return None
    try:
        return eval(tok, {'__builtins__': {}}, {})
    except Exception:
        return None

problems = 0
checked = 0
for fn in sorted(os.listdir(os.path.join(PK, 'sprites'))):
    if not fn.endswith('.java'):
        continue
    src = rd(os.path.join(PK, 'sprites', fn))
    # a texture named by a plain path string (Magic Ling's "Boss/SLMKING0.png") must exist too
    for lit in re.findall(r'texture\(\s*"([^"]+)"\s*\)', src):
        if not os.path.exists(os.path.join(ASSETS, lit)):
            print('%s: picture %s is missing' % (fn, lit)); problems += 1
    # only sprites that use exactly one texture, otherwise it is not clear which picture a film belongs to
    refs = set(re.findall(r'texture\(\s*Assets\.(\w+)\.(\w+)\s*\)', src))
    if len(refs) != 1:
        continue
    group, name = list(refs)[0]
    local = {k: int(v) for k, v in re.findall(r'\bint\s+(\w+)\s*=\s*(\d+)\s*;', src)}
    rel = consts.get((group, name))
    if not rel:
        continue
    path = os.path.join(ASSETS, rel)
    if not os.path.exists(path):
        print('%s: picture %s is missing' % (fn, rel)); problems += 1
        continue
    size = png_size(path)
    if not size:
        continue
    for var, w, h in re.findall(r'(\w+)\s*=\s*new TextureFilm\(\s*texture\s*,\s*(\d+)\s*,\s*(\d+)\s*\)', src):
        w, h = int(w), int(h)
        cols, rows = size[0] // w, size[1] // h
        total = cols * rows
        for call in re.finditer(r'(\w+)\.frames\(\s*' + re.escape(var) + r'\s*,([^;]*?)\)\s*;', src, re.S):
            checked += 1
            for tok in call.group(2).split(','):
                v = number(tok, local)
                if v is not None and (v < 0 or v >= total):
                    print('%s: %s.frames uses frame %d but %s (%dx%d) only has %d frames of %dx%d (0 to %d)'
                          % (fn, call.group(1), v, rel, size[0], size[1], total, w, h, total - 1))
                    problems += 1
                    break

print('SUMMARY sprite frames checked: %d animations, problems: %d' % (checked, problems))
sys.exit(1 if problems else 0)
