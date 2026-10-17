"""Gives ported sprite classes their own copy of the mod's sprite sheet (mods keep extra frames on shared sheets).

  python -I scripts/port-oldsheets.py <mod folder with core/src/main layout> <subfolder> <PREFIX> Sprite1 Sprite2 ...

  mod folder   a mod whose assets are flat in core/src/main/assets (Goblins, via a restructured copy)
  subfolder    assets/sprites/<subfolder>/ gets the sheet
  PREFIX       the new Assets.Sprites constant is <PREFIX>_<NAME>
For each sprite class already in our sprites folder that names Assets.Sprites.X (or Assets.X in the mod's source), it
reads the mod's Assets.java for the file, copies it, adds the constant to Assets.Sprites and points the class at it.
Safe to run twice.
"""
import sys, os, re, io, shutil
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
PK = ROOT + '/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
mod, sub, prefix = sys.argv[1], sys.argv[2], sys.argv[3]
names = sys.argv[4:]
def rd(p): return io.open(p, encoding='utf-8', newline='').read()
def wr(p, s): io.open(p, 'w', encoding='utf-8', newline='').write(s)
mj = None
for dp, dirs, files in os.walk(mod + '/core/src/main/java'):
    if 'Assets.java' in files: mj = dp + '/Assets.java'; break
amap = dict(re.findall(r'public static final String (\w+)\s*=\s*"([^"]+)"', rd(mj)))
A = PK + 'Assets.java'
a = rd(A)
os.makedirs(ROOT + '/core/src/main/assets/sprites/' + sub, exist_ok=True)
for n in names:
    p = PK + 'sprites/' + n + '.java'
    if not os.path.exists(p): print('missing', n); continue
    s = rd(p)
    def fix(m):
        global a
        X = m.group(1)
        if X.startswith(prefix + '_') or X not in amap: return m.group(0)
        f = amap[X]
        src = mod + '/core/src/main/assets/' + f
        if not os.path.exists(src): print('no sheet', f); return m.group(0)
        dst = 'sprites/' + sub + '/' + os.path.basename(f)
        shutil.copyfile(src, ROOT + '/core/src/main/assets/' + dst)
        const = prefix + '_' + X
        if 'String ' + const + ' ' not in a and 'String ' + const + '=' not in a:
            i = a.rindex('\t}\n}') if '\t}\n}' in a else a.rindex('\t}\r\n}')
            nl = '\r\n' if '\r\n' in a[i:i + 5] else '\n'
            a = a[:i] + '\t\tpublic static final String %s = "%s"; //ported, %s%s' % (const, dst, sub, nl) + a[i:]
        return 'Assets.Sprites.' + const
    t = re.sub(r'Assets\.(?:Sprites\.)?([A-Z][A-Z0-9_]+)', fix, s)
    if t != s: wr(p, t); print('sheet for', n)
wr(A, a)
