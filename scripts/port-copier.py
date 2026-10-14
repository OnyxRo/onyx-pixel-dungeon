"""Dependency closure for porting a mod's classes.

  python porter.py <project root> <mod root> <asset tag> [max rounds]

Compiles the project, and for every class the compiler cannot find, copies that class from
the mod if the mod has exactly one file of that name and the project has none. Also adds
missing Assets constants (sprites, sounds and so on) by copying the mod's file into a
subfolder named after the asset tag. Repeats until a round changes nothing, then prints the
errors that are left for fixing by hand.
"""
import sys, os, re, io, subprocess, shutil, glob

ROOT = sys.argv[1].rstrip('/') + '/'
MOD = sys.argv[2].rstrip('/') + '/'
TAG = sys.argv[3]
ROUNDS = int(sys.argv[4]) if len(sys.argv) > 4 else 12
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
B = ROOT + PK
MB = MOD + PK
MASSETS = MOD + 'core/src/main/assets/'
ASSETS = ROOT + 'core/src/main/assets/'

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith(chr(92)*2) else chr(92)*2 + '?' + chr(92) + p

def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()
def wr(p, s): io.open(LP(p), 'w', encoding='utf-8', newline='').write(s)

# index of the mod's classes by simple name
mod_index = {}
for f in glob.glob(MB + '**/*.java', recursive=True):
    mod_index.setdefault(os.path.basename(f)[:-5], []).append(f)

def compile_errors():
    r = subprocess.run([ROOT + 'gradlew.bat', 'core:compileJava', '--console=plain', '-q'], cwd=ROOT,
                       capture_output=True, text=True, errors='replace')
    return (r.stdout or '') + (r.stderr or '')

mod_assets_src = rd(MB + 'Assets.java') if os.path.exists(MB + 'Assets.java') else ''

def add_asset(const, section):
    """section is the nested class in Assets, like Sprites or Sounds."""
    ms = re.search(r'public static class ' + section + r'\s*\{', mod_assets_src)
    if not ms: return False
    nxt = mod_assets_src.find('public static class', ms.end())
    body = mod_assets_src[ms.end(): nxt if nxt != -1 else len(mod_assets_src)]
    m = re.search(r'public static final (?:String|Object)\s+' + re.escape(const) + r'\s*=\s*"([^"]+)"', body)
    if not m: return False
    path = m.group(1)
    if not os.path.exists(LP(MASSETS + path)): return False
    d, f = os.path.split(path)
    newpath = d + '/' + TAG + '/' + f if '/' + TAG not in d else path
    os.makedirs(os.path.dirname(ASSETS + newpath), exist_ok=True)
    shutil.copy(LP(MASSETS + path), ASSETS + newpath)
    p = B + 'Assets.java'
    s = rd(p); nl = '\r\n' if '\r\n' in s else '\n'
    m2 = re.search(r'public static class ' + section + r'\s*\{', s)
    if not m2 or re.search(r'\b' + re.escape(const) + r'\s*=', s[m2.end():m2.end() + 200000].split('public static class')[0]):
        return False
    line = nl + '\t\tpublic static final String %s = "%s"; //ported, %s' % (const, newpath, TAG)
    s = s[:m2.end()] + line + s[m2.end():]
    wr(p, s)
    return True

copied_total = []
for rnd in range(ROUNDS):
    out = compile_errors()
    blocks = re.split(r'(?=^\S.*?: error: )', out, flags=re.M)
    changed = False
    want = set()
    for b in blocks:
        if ': error: ' not in b: continue
        m = re.search(r'symbol:\s+class (\w+)', b)
        if m: want.add(m.group(1))
        m = re.search(r'error: package ([\w.]+) does not exist', b)
        if m:
            # the offending import line names the class
            m2 = re.search(r'import (?:static )?[\w.]*?\.([A-Z]\w+)[\w.]*;', b)
            if m2: want.add(m2.group(1))
        m = re.search(r'symbol:\s+variable (\w+)\s*\n\s*location:\s+class (\w+)', b)
        if m and m.group(2) in ('Sprites', 'Sounds', 'Music', 'Environment', 'Interfaces', 'Effects', 'Splashes', 'Messages', 'Fonts'):
            if add_asset(m.group(1), m.group(2)):
                changed = True
                copied_total.append('asset ' + m.group(1))
    for name in sorted(want):
        cands = mod_index.get(name, [])
        if len(cands) != 1: continue
        rel = os.path.relpath(cands[0], MB).replace(os.sep, '/')
        if os.path.exists(B + rel): continue
        # never shadow a class the project already has somewhere else
        if glob.glob(B + '**/' + name + '.java', recursive=True): continue
        os.makedirs(os.path.dirname(B + rel), exist_ok=True)
        shutil.copy(LP(cands[0]), B + rel)
        copied_total.append(rel)
        changed = True
    n = out.count(': error: ')
    print('round %d: %d error lines, copied so far %d' % (rnd, n, len(copied_total)))
    if not changed: break

io.open(ROOT + '../porter_last.txt', 'w', encoding='utf-8').write(out) if False else None
print('COPIED:')
for c in copied_total: print('  ' + c)
print('REMAINING:')
seen = set()
for b in re.split(r'(?=^\S.*?: error: )', out, flags=re.M):
    if ': error: ' not in b: continue
    first = b.split('\n')[0]
    first = re.sub(r'^.*shatteredpixeldungeon.', '', first)
    sym = re.search(r'symbol:\s+(.*)', b)
    loc = re.search(r'location:\s+(.*)', b)
    line = first + ' || ' + (sym.group(1).strip() if sym else '') + ' || ' + (loc.group(1).strip() if loc else '')
    if line not in seen:
        seen.add(line); print('  ' + line[:230])
