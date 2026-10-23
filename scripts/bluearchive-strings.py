"""Strings for the Blue Archive PD port. The mod only wrote its new text in Korean (its *_ko bundles).

  python -I scripts/bluearchive-strings.py extract OUT     writes every needed Korean line, already under this game's keys
  python -I scripts/bluearchive-strings.py append IN       appends English lines (key=text, same keys) to the bundles,
                                                         under a ###Blue Archive Pixel Dungeon comment, skipping keys already there
Keys are renamed to the BA classes (see bluearchive-copy.py). Strings for left out parts are dropped.
"""
import io, os, re, sys, importlib.util
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
spec = importlib.util.spec_from_file_location('bacopy', os.path.join(ROOT, 'scripts', 'bluearchive-copy.py'))
bc = importlib.util.module_from_spec(spec); spec.loader.exec_module(bc)
MM = os.path.join(os.path.expanduser('~'), 'mod-sources', 'bluearchive', 'core', 'src', 'main', 'assets', 'messages')
OM = os.path.join(ROOT, 'core', 'src', 'main', 'assets', 'messages')
BUNDLES = ['actors', 'items', 'misc', 'windows']
STUDENTS = ['aris', 'nonomi', 'miyako', 'hoshino', 'shiroko', 'noa', 'miyu', 'yuzu', 'izuna']

def load(p):
    d = {}
    if not os.path.exists(p): return d
    lines = io.open(p, encoding='utf-8').read().split('\n')
    i = 0
    while i < len(lines):
        l = lines[i].rstrip('\r')
        while l.endswith('\\') and i + 1 < len(lines):
            i += 1; l = l[:-1] + '\\n' + lines[i].rstrip('\r')
        i += 1
        if not l or l.startswith('#') or '=' not in l: continue
        k, v = l.split('=', 1)
        d[k.strip()] = v
    return d

#mod key prefix (class path, lower case) to ours
PREFIX = {}
for f in bc.FILES:
    d, n = f.rsplit('/', 1)
    PREFIX[(d + '/' + n).replace('/', '.').lower()] = (d + '/BA' + n).replace('/', '.').lower()
for n in bc.NESTED:
    PREFIX['actors.hero.talent$' + n.lower()] = 'actors.hero.batalents$' + n.lower()
NEW_TALENTS = {t.lower() for t in bc.TALENTS if '_ABIL_' not in t}

def rename(k):
    for a in sorted(PREFIX, key=len, reverse=True):
        if k == a or k.startswith(a + '.') or k.startswith(a + '$') or (a.endswith('$') and k.startswith(a)):
            if a.endswith('$'): return PREFIX[a] + k[len(a):]
            nested = [x for x in PREFIX if k.startswith(x + '$')]
            return PREFIX[a] + k[len(a):]
    m = re.match(r'actors\.hero\.talent\.([a-z0-9_]+)\.(title|desc)$', k)
    if m:
        return 'actors.hero.talent.ba_%s.%s' % m.groups() if m.group(1) in NEW_TALENTS else None
    m = re.match(r'actors\.hero\.(heroclass|herosubclass)\.((%s)\w*)$' % '|'.join(STUDENTS), k)
    if m:
        return 'actors.hero.%s.ba_%s' % (m.group(1), m.group(2))
    if re.match(r'actors\.hero\.hero\.(%s)_' % '|'.join(STUDENTS), k): return k   #voice lines, keyed without BA
    if re.match(r'actors\.mobs\.npcs\.wandmaker\.intro_(%s)$' % '|'.join(STUDENTS), k): return k
    m = re.match(r'badges\$badge\.mastery_(%s)$' % '|'.join(STUDENTS), k)
    if m: return 'badges$badge.mastery_ba_' + m.group(1)
    if k.startswith('items.weapon.melee.gun.gun$') or k.startswith('items.weapon.melee.gun.gun.'):
        return k.replace('items.weapon.melee.gun.gun', 'items.weapon.melee.gun.bagun', 1)
    return None

def extract(out):
    n = 0
    with io.open(out, 'w', encoding='utf-8') as o:
        for b in BUNDLES:
            ko = load(os.path.join(MM, b, b + '_ko.properties'))
            have = load(os.path.join(OM, b, b + '.properties'))
            o.write('#### %s\n' % b)
            for k, v in ko.items():
                nk = rename(k)
                if nk is None or nk in have: continue
                o.write('%s=%s\n' % (nk, v)); n += 1
    print('lines to translate', n)

def append(inp):
    cur = None; add = {}
    for l in io.open(inp, encoding='utf-8').read().split('\n'):
        l = l.rstrip('\r')
        if l.startswith('#### '): cur = l[5:].strip(); add.setdefault(cur, []); continue
        if not l or l.startswith('#') or '=' not in l or cur is None: continue
        add[cur].append(l)
    for b, lines in add.items():
        p = os.path.join(OM, b, b + '.properties')
        have = load(p)
        lines = [l for l in lines if l.split('=', 1)[0] not in have]
        if not lines: continue
        s = io.open(p, encoding='utf-8', newline='').read()
        nl = '\r\n' if '\r\n' in s else '\n'
        if not s.endswith(nl): s += nl
        s += nl + '###Blue Archive Pixel Dungeon' + nl + nl.join(lines) + nl
        io.open(p, 'w', encoding='utf-8', newline='').write(s)
        print(b, 'added', len(lines))

if __name__ == '__main__':
    {'extract': extract, 'append': append}[sys.argv[1]](sys.argv[2])
