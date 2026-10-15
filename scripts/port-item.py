"""Ports weapons, armor, rings, wands, artifacts, potions, scrolls, enchantments and other items from a mod.

  python -I scripts/port-item.py <mod key or folder> Item1 Item2 ... [--no-check] [--skip=Perks,Other]

  mod key or folder  a key from scripts/mods.txt (cloned with scripts/get-mod.sh) or a path to the mod's source

What it does, in order
  1. copies each class from the mod, keeping its folder under items/
  2. fixes known drift from other bases (see ITEM_RULES), including mods that use long for damage
     (Experienced), Dungeon.cycle, Dungeon.Float and friends
  3. copies the item pictures the classes use (port-itemsprites.py) into the rows kept for other mods
  4. runs port-copier.py, which compiles and copies the buffs, effects and helper classes still missing.
     Perks, TreasureBag and GambleBag are never copied (they drag in the mod's own systems),
     add more with --skip. Skipped names are printed.
  5. copies the English text (port-strings.py), and writes a plain placeholder for items with no text
  6. adds each item to the journal Catalog in the right category, so it shows in the sandbox item list
  7. runs scripts/check.sh --quick, headless

It does not add items to the loot tables. Do that by hand if Onyx asks, in Generator.java.
Output is long, so it goes to build/port-item.log and only a summary is printed.
"""
import sys, os, re, io, glob, shutil, subprocess

pos = [a for a in sys.argv[1:] if not a.startswith('--')]
opts = {a.split('=')[0]: (a.split('=')[1] if '=' in a else True) for a in sys.argv[1:] if a.startswith('--')}
if len(pos) < 2:
    print(__doc__); sys.exit(1)
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
mod_arg, names = pos[0], pos[1:]
MODS = os.environ.get('MOD_SOURCES', os.path.expanduser('~/mod-sources'))
MOD = mod_arg if os.path.isdir(mod_arg) else os.path.join(MODS, mod_arg)
if not os.path.isdir(MOD):
    print('no mod folder at', MOD, ' try scripts/get-mod.sh', mod_arg); sys.exit(1)
MODNAME = os.path.basename(os.path.normpath(MOD))
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
os.makedirs(ROOT + '/build', exist_ok=True)
LOG = ROOT + '/build/port-item.log'
open(LOG, 'w').close()
SKIP = ','.join(['Perks', 'TreasureBag', 'GambleBag'] + (opts['--skip'].split(',') if '--skip' in opts else []))

def LP(p):
    p = os.path.abspath(p)
    bs = chr(92)
    return bs * 2 + '?' + bs + p if os.name == 'nt' and not p.startswith(bs * 2) else p
def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()
def wr(p, s): io.open(LP(p), 'w', encoding='utf-8', newline='').write(s)
def run(cmd, env=None):
    with open(LOG, 'a') as f:
        f.write('\n$ ' + ' '.join(cmd) + '\n'); f.flush()
        e = dict(os.environ); e.update(env or {})
        return subprocess.run(cmd, cwd=ROOT, stdout=f, stderr=subprocess.STDOUT, env=e).returncode

# (what it fixes, regex, replacement). Run over every file this run copied. Add a rule when a port hits the same error twice.
ITEM_RULES = [
    ('Dungeon.cycle arithmetic is Experienced only, use 0', r'\(?\s*Dungeon\.cycle\s*\*\s*\d+L?\s*\)?', '0'),
    ('Dungeon.cycle is Experienced only, use 0', r'Dungeon\.cycle', '0'),
    ('Dungeon.Float, Int and ranges moved to Random', r'Dungeon\.(Float|Int|IntRange|NormalIntRange)\(', r'Random.\1('),
    ('NormalLongRange became NormalIntRange', r'(?:Dungeon|Random)\.NormalLongRange\(', 'Random.NormalIntRange('),
    ('Random.LongRange became IntRange', r'(?:Dungeon|Random)\.LongRange\(', 'Random.IntRange('),
    ('internalTier does not exist here', r'internalTier\s*=\s*tier\s*=', 'tier ='),
    ('durabilityPerUse takes the level', r'public float durabilityPerUse\(\s*\)\s*\{', 'public float durabilityPerUse( int level ) {'),
    ('abilityChargeUse is final, override baseChargeUse', r'public\s+float\s+abilityChargeUse\(\s*Hero hero,\s*Char target\s*\)\s*\{\s*return\s*\((.*?)\);', r'protected int baseChargeUse(Hero hero, Char target) {\n\t\treturn (int)(\1);'),
    ('attackDelay became public', r'protected(\s+float\s+attackDelay\s*\()', r'public\1'),
    ('Mob.add(Buff) returns boolean', r'public\s+void\s+add\(\s*Buff\s+buff\s*\)\s*\{\s*\}', 'public boolean add( Buff buff ) { return false; }'),
    ('throwChar gained a cause argument', r'(WandOfBlastWave\.throwChar\([^;]*?,\s*(?:true|false),\s*(?:true|false))\)', r'\1, this)'),
    ('TargetedCell(pos, color) is TargetedCell.colored here', r'new TargetedCell\(', 'TargetedCell.colored('),
    ('Buff.prolong on a deferred damage buff is extend', r'(\bdmg\.)prolong\(', r'\1extend('),
]

def convert_long(s):
    """Mods built for huge numbers (Experienced) use long for damage. This game uses int."""
    if not re.search(r'\blong\s+(?:min|max|damageRoll|proc|value|STRReq|defenseFactor)\b|long\s+damage\b', s):
        return s
    s = re.sub(r'\blong\b', 'int', s)
    s = re.sub(r'\bLong\b', 'Integer', s)
    return re.sub(r'(\d)L\b', r'\1', s)

def fix_file(path):
    s = rd(path)
    t = convert_long(s)
    for why, pat, rep in ITEM_RULES:
        t = re.sub(pat, rep, t)
    if 'Random.' in t and 'import com.watabou.utils.Random;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport com.watabou.utils.Random;', t, count=1)
    if t != s:
        wr(path, t); return True
    return False

mod_files = {}
for f in glob.glob(LP(MOD) + '/' + PK + '**/*.java', recursive=True):
    mod_files.setdefault(os.path.basename(f)[:-5], []).append(f)

copied, problems = [], []
for n in names:
    c = mod_files.get(n, [])
    if len(c) != 1:
        problems.append('%s: %s' % (n, 'not found in the mod' if not c else 'name is not unique in the mod')); continue
    rel = os.path.relpath(c[0], LP(MOD) + '/' + PK).replace(os.sep, '/')
    dest = ROOT + '/' + PK + rel
    if os.path.exists(dest):
        problems.append(n + ': already in the game at ' + rel); continue
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    shutil.copy(c[0], dest)
    copied.append(rel)
print('copied', len(copied), 'classes from', MODNAME)

def new_java():
    out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
    # only files that are new (??), never a game file that was merely edited
    return [l[3:].strip()[len(PK):] for l in out.split('\n') if l.startswith('??') and l.strip().endswith('.java') and l[3:].strip().startswith(PK)]

def fix_all():
    n = 0
    for rel in new_java():
        if fix_file(ROOT + '/' + PK + rel): n += 1
    return n

print('fixed drift in', fix_all(), 'files')

# item pictures
sheet = rd(ROOT + '/' + PK + 'sprites/ItemSpriteSheet.java')
need = set()
for rel in new_java():
    for m in re.finditer(r'ItemSpriteSheet\.([A-Z0-9_]+)', rd(ROOT + '/' + PK + rel)):
        if not re.search(r'\b' + m.group(1) + r'\s*=', sheet): need.add(m.group(1))
if need:
    print('copying item pictures', ', '.join(sorted(need)))
    run([sys.executable, '-I', 'scripts/port-itemsprites.py', '.', MOD] + sorted(need))

print('closing dependencies with port-copier (compiles several times, a few minutes) ...')
run([sys.executable, '-I', 'scripts/port-copier.py', '.', MOD, MODNAME.lower()[:12], '12'], {'PORT_SKIP': SKIP})
print('fixed drift in', fix_all(), 'more files')
run([sys.executable, '-I', 'scripts/port-copier.py', '.', MOD, MODNAME.lower()[:12], '6'], {'PORT_SKIP': SKIP})
log = rd(LOG)
tail = log[log.rfind('COPIED:'):] if 'COPIED:' in log else ''
skipped = re.search(r'SKIPPED \(mod only systems\): (.*)', tail)
rem = tail[tail.find('REMAINING:'):] if 'REMAINING:' in tail else ''
rem_lines = [l for l in rem.split('\n')[1:] if l.strip()]

run([sys.executable, '-I', 'scripts/port-strings.py', '.', MOD, MODNAME])

# placeholder text for items the mod never described
props = ROOT + '/core/src/main/assets/messages/items/items.properties'
ptext = rd(props)
ph = []
for rel in copied:
    key = rel[:-5].replace('/', '.').lower()
    if key + '.name=' in ptext: continue
    words = re.sub(r'(?<=[a-z0-9])(?=[A-Z])', ' ', os.path.basename(rel)[:-5]).lower()
    ptext += '\n%s.name=%s\n%s.desc=The %s, from %s. The mod gave it no description.' % (key, words, key, words, MODNAME)
    ph.append(os.path.basename(rel)[:-5])
if ph:
    wr(props, ptext.rstrip('\n') + '\n'); print('no text in the mod for', ', '.join(ph), ', wrote placeholders')

# journal catalog
CATS = [  # (path prefix under items/, catalog), first match wins
    ('weapon/melee/', 'MELEE_WEAPONS'), ('weapon/missiles/darts/', 'TIPPED_DARTS'), ('weapon/missiles/', 'THROWN_WEAPONS'),
    ('weapon/thrown/', 'THROWN_WEAPONS'), ('weapon/enchantments/', 'ENCHANTMENTS'), ('weapon/curses/', 'ENCHANTMENTS'),
    ('weapon/', 'MISC_EQUIPMENT'), ('armor/glyphs/', 'GLYPHS'), ('armor/curses/', 'GLYPHS'), ('armor/', 'ARMOR'),
    ('wands/', 'WANDS'), ('rings/', 'RINGS'), ('artifacts/', 'ARTIFACTS'), ('trinkets/', 'TRINKETS'),
    ('potions/exotic/', 'EXOTIC_POTIONS'), ('potions/brews/', 'BREWS_ELIXIRS'), ('potions/elixirs/', 'BREWS_ELIXIRS'),
    ('potions/', 'POTIONS'), ('scrolls/exotic/', 'EXOTIC_SCROLLS'), ('scrolls/', 'SCROLLS'), ('stones/', 'STONES'),
    ('plants/', 'SEEDS'), ('food/', 'FOOD'), ('bombs/', 'BOMBS'), ('spells/', 'SPELLS'),
]
cat_path = ROOT + '/' + PK + 'journal/Catalog.java'
cs = rd(cat_path)
nl = '\r\n' if '\r\n' in cs else '\n'
lines, regd = [], []
for rel in copied:
    if not rel.startswith('items/'): continue
    n = os.path.basename(rel)[:-5]
    src = rd(ROOT + '/' + PK + rel)
    if re.search(r'\b(abstract\s+class|interface)\s+' + n + r'\b', src) or not re.search(r'class\s+' + n + r'\b', src): continue
    if re.search(r'\bclass\s+' + n + r'\s+extends\s+(Buff|FlavourBuff)\b', src) or '/traits/' in rel: continue
    sub = rel[len('items/'):]
    cat = next((c for p, c in CATS if sub.startswith(p)), 'MISC_CONSUMABLES')
    fq = 'com.shatteredpixel.shatteredpixeldungeon.' + rel[:-5].replace('/', '.')
    if fq + '.class' in cs: continue
    lines.append('\t\t%s.addItems(%s.class);' % (cat, fq)); regd.append(n)
if lines:
    anchor = re.search(r'\r?\n\s*\}\r?\n\r?\n\tpublic static ArrayList<Catalog> equipmentCatalogs', cs)
    block = nl + nl + '\t\t//items ported from ' + MODNAME + nl + nl.join(lines)
    cs = cs[:anchor.start()] + block + cs[anchor.start():]
    wr(cat_path, cs)
print('added to the journal catalog:', len(regd))

print('compile errors left:', len(rem_lines))
for l in rem_lines[:15]: print('   ', l[:200])
if skipped: print('not copied, mod only systems:', skipped.group(1), '(code that uses them has to be edited out by hand)')
for p in problems: print('could not port:', p)
if '--no-check' not in opts and not rem_lines:
    rc = run(['bash', 'scripts/check.sh', '--quick'])
    for l in rd(ROOT + '/build/check.log').split('\n'):
        if l.startswith(('FAIL', 'WARN', 'SUMMARY')): print('  ', l[:200])
    print('check', 'passed' if rc == 0 else 'FAILED')
print('log:', os.path.relpath(LOG, ROOT))
