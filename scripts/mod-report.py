"""Plans a port. Reads a mod's source and says what it adds that this game lacks, and how hard each piece is.

  python -I scripts/mod-report.py <mod folder> [--only mobs|items|weapons|buffs|all] [--top 40]

Writes build/mod-report-<mod folder name>.md and prints a short summary. What it tells you

  * how many new classes the mod has, by kind (monsters, sprites, buffs, blobs, weapons, other items, levels, windows)
  * for every new monster the size of its dependency closure, meaning how many other new classes it drags in
    (the same classes scripts/port-copier.py would copy), and which parts of the mod it touches that this game
    does not have at all (a different stat system, a different combat system, new hero abilities)
  * whether the mod keeps monster stats in Java fields, like Shattered, or in a table (Reforged's Constants.mobs),
    since a table means each monster has to be rebuilt by hand
  * which monsters have a name string and a sprite sheet in the mod, so you know what will look right

Easy monsters (closure of 6 or fewer, nothing foreign) can go straight through scripts/port-mob.py.
"""
import sys, os, re, io, glob, collections

args = [a for a in sys.argv[1:] if not a.startswith('--')]
opts = {a.split('=')[0]: (a.split('=')[1] if '=' in a else True) for a in sys.argv[1:] if a.startswith('--')}
if not args:
    print(__doc__); sys.exit(1)
MOD = os.path.abspath(args[0])
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
TOP = int(opts.get('--top', 40))

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p if os.name == 'nt' else p

def rd(p):
    try: return io.open(LP(p), encoding='utf-8', errors='replace').read()
    except OSError: return ''

def find_pkg(base):
    """The folder that holds the game's packages (the one with Assets.java)."""
    for hit in glob.glob(LP(base) + '/**/Assets.java', recursive=True):
        if '/build/' in hit.replace('\\', '/'): continue
        return os.path.dirname(hit)
    return None

OURS = find_pkg(ROOT + '/core/src/main/java')
THEIRS = find_pkg(MOD + '/core/src/main/java') or find_pkg(MOD)
if not THEIRS:
    print('could not find the game source (a folder with Assets.java) under', MOD); sys.exit(1)
PKG = 'com.shatteredpixel.shatteredpixeldungeon.'

def classes(base):
    out = {}
    for f in glob.glob(LP(base) + '/**/*.java', recursive=True):
        rel = os.path.relpath(f, LP(base)).replace('\\', '/')
        out[rel] = f
    return out

ours = classes(OURS); theirs = classes(THEIRS)
our_names = collections.defaultdict(list)
for rel in ours: our_names[os.path.basename(rel)[:-5]].append(rel)
new = {rel: f for rel, f in theirs.items() if rel not in ours}
by_name = collections.defaultdict(list)
for rel in theirs: by_name[os.path.basename(rel)[:-5]].append(rel)

def kind(rel):
    if rel.startswith('actors/mobs/npcs/'): return 'npcs'
    if rel.startswith('actors/mobs/'): return 'monsters'
    if rel.startswith('sprites/'): return 'sprites'
    if rel.startswith('actors/buffs/'): return 'buffs'
    if rel.startswith('actors/blobs/'): return 'blobs'
    if rel.startswith('items/weapon/'): return 'weapons'
    if rel.startswith('items/'): return 'items'
    if rel.startswith('levels/'): return 'levels'
    if rel.startswith('windows/') or rel.startswith('ui/'): return 'windows and ui'
    if rel.startswith('actors/hero/'): return 'hero'
    return 'other'

counts = collections.Counter(kind(r) for r in new)

def deps(rel):
    """Classes of the mod's that this file names and this game lacks."""
    src = rd(theirs[rel])
    found = set()
    for m in re.finditer(r'^import\s+' + re.escape(PKG) + r'([\w.]+);', src, re.M):
        path = m.group(1).replace('.', '/')
        parts = path.split('/')
        # nested class imports like mobs.Foo.Bar, climb until a file matches
        while parts:
            cand = '/'.join(parts) + '.java'
            if cand in theirs:
                if cand not in ours: found.add(cand)
                break
            parts.pop()
    # same-package classes are used without an import
    folder = os.path.dirname(rel)
    for name in set(re.findall(r'\b([A-Z]\w+)\b', src)):
        cand = (folder + '/' if folder else '') + name + '.java'
        if cand in theirs and cand not in ours and cand != rel: found.add(cand)
    return found

dep_cache = {}
def closure(rel):
    seen, todo = set(), [rel]
    while todo:
        r = todo.pop()
        if r in seen: continue
        seen.add(r)
        if r not in dep_cache: dep_cache[r] = deps(r)
        todo.extend(dep_cache[r])
    seen.discard(rel)
    return seen

# systems of the mod that this game does not have
FOREIGN = [
    (r'GetConstants\s*\(\)|Constants\.mobs\.', 'stats come from a table (Constants.mobs), rebuild by hand'),
    (r'import\s+' + re.escape(PKG) + r'combat\.', 'uses the mod combat system'),
    (r'\bAttackContext\b|\bCombatResolver\b', 'uses the mod combat system'),
    (r'BundleableProperty', 'uses BundleableProperty'),
]

_member_cache = {}
def members_of(path):
    if path not in _member_cache: _member_cache[path] = set(re.findall(r'\b(\w+)\b', rd(path)))
    return _member_cache[path]

def mod_added_members(rel):
    """Core members the mod added, like Dungeon.cycle. The monster cannot compile here without them."""
    src = rd(theirs[rel])
    missing = set()
    for cls, member in re.findall(r'\b([A-Z]\w+)\.([a-z]\w*)\b', src):
        for crel in by_name.get(cls, []):
            if crel in ours and crel in theirs:
                if member not in members_of(ours[crel]) and member in members_of(theirs[crel]):
                    missing.add(cls + '.' + member)
    return sorted(missing)

def foreign(rel):
    src = rd(theirs[rel])
    notes = []
    if any(kind(d) == 'levels' for d in closure(rel)):
        notes.append('needs its own level (a boss), port it with its level or skip')
    if 'Property.BOSS' in src:
        notes.append('is a boss, needs its boss level or a special spawn')
    added = mod_added_members(rel)
    if added:
        notes.append('needs members the mod added to core classes (' + ', '.join(added[:4]) + ')')
    for pat, why in FOREIGN:
        if re.search(pat, src) and why not in notes: notes.append(why)
    for d in closure(rel):
        if kind(d) in ('hero', 'windows and ui'):
            n = 'drags in ' + kind(d) + ' code (' + os.path.basename(d)[:-5] + ')'
            if n not in notes: notes.append(n)
    return notes

# mod strings, to know which monsters have a name
strings = ''
for f in glob.glob(LP(MOD) + '/core/src/main/assets/messages/actors/actors.properties') + glob.glob(LP(MOD) + '/**/messages/actors/actors.properties', recursive=True):
    strings += rd(f); break

def has_name(rel):
    key = ('actors.' + rel[len('actors/'):-5].replace('/', '.')).lower()
    return (key + '.name') in strings.lower()

def sprite_of(rel):
    src = rd(theirs[rel])
    m = re.search(r'spriteClass\s*=\s*(\w+)\.class', src) or re.search(r'return\s+(\w+Sprite)\.class', src)
    return m.group(1) if m else None

mob_rows = []
for rel in sorted(new):
    if kind(rel) != 'monsters': continue
    src = rd(theirs[rel])
    if not re.search(r'\bclass\s+\w+\s+extends\s+\w+', src): continue
    clo = closure(rel)
    notes = foreign(rel)
    spr = sprite_of(rel)
    sprite_rel = None
    if spr and by_name.get(spr): sprite_rel = by_name[spr][0]
    extends = re.search(r'class\s+\w+\s+extends\s+(\w+)', src).group(1)
    mob_rows.append(dict(rel=rel, name=os.path.basename(rel)[:-5], loc=src.count('\n'), closure=len(clo),
                         notes=notes, sprite=spr, has_sprite=bool(sprite_rel), has_name=has_name(rel), extends=extends,
                         in_ours_base=bool(our_names.get(extends))))

def tier(r):
    if r['notes']: return 'hard'
    if r['closure'] <= 6: return 'easy'
    return 'medium'

lines = []
out = lines.append
out('# Port report for ' + os.path.basename(MOD))
out('')
out('Mod source `' + THEIRS + '`')
out('')
stat_table = any('table' in n for r in mob_rows for n in r['notes'])
out('New classes this game lacks (' + str(len(new)) + ' in all)')
out('')
for k, v in counts.most_common(): out('- ' + k + ' ' + str(v))
out('')
if stat_table:
    out('**Stats live in a table, not in the monster classes.** Every monster needs its numbers read out of the table and put into the usual fields (HP, defenseSkill, EXP, maxLvl, damageRoll, attackSkill, drRoll, loot). scripts/reforged-stats.py does this for Reforged style tables.')
    out('')
tiers = collections.Counter(tier(r) for r in mob_rows)
out('Monsters, ' + str(len(mob_rows)) + ' in all. Easy ' + str(tiers['easy']) + ', medium ' + str(tiers['medium']) + ', hard ' + str(tiers['hard']))
out('')
out('| Monster | Lines | Closure | Tier | Sprite | Name string | Notes |')
out('| --- | --- | --- | --- | --- | --- | --- |')
order = {'easy': 0, 'medium': 1, 'hard': 2}
for r in sorted(mob_rows, key=lambda r: (order[tier(r)], r['closure'], r['name']))[:TOP if 'all' not in opts else None]:
    out('| %s | %d | %d | %s | %s | %s | %s |' % (r['name'], r['loc'], r['closure'], tier(r),
        (r['sprite'] or '?') + ('' if r['has_sprite'] else ' (missing)'), 'yes' if r['has_name'] else 'no', '; '.join(r['notes'])))
out('')
if len(mob_rows) > TOP and 'all' not in opts:
    out('Showing ' + str(TOP) + ' of ' + str(len(mob_rows)) + ', pass --all for the rest.')
    out('')
for k in ('weapons', 'items', 'buffs', 'blobs', 'levels', 'npcs'):
    names = [os.path.basename(r)[:-5] for r in sorted(new) if kind(r) == k]
    if names:
        out('**' + k + '** (' + str(len(names)) + ') ' + ', '.join(names[:60]) + (' ...' if len(names) > 60 else ''))
        out('')
text = '\n'.join(lines)
os.makedirs(ROOT + '/build', exist_ok=True)
path = ROOT + '/build/mod-report-' + os.path.basename(MOD.rstrip('/\\')) + '.md'
io.open(path, 'w', encoding='utf-8').write(text + '\n')

print('mod:', os.path.basename(MOD), ' new classes:', len(new), ' ', dict(counts))
print('monsters:', len(mob_rows), ' easy', tiers['easy'], 'medium', tiers['medium'], 'hard', tiers['hard'],
      ' stats in a table' if stat_table else '')
for r in sorted(mob_rows, key=lambda r: (order[tier(r)], r['closure']))[:12]:
    print('  %-7s %-24s closure %-3d %s' % (tier(r), r['name'], r['closure'], '; '.join(r['notes'])))
print('full report:', os.path.relpath(path, ROOT))
