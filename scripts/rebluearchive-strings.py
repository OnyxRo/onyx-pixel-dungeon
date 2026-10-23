"""Copies Re Blue Archive PD's English text for everything rebluearchive-copy.py brought in, under the renamed keys
(a class Gun becomes RBAGun, so items.weapon.melee.gun.gun.x becomes items.weapon.melee.gun.rbagun.x, and the talent
ARIS_T1_1 becomes RBA_ARIS_T1_1). Reads the mod's new_messages first, then messages, like the mod.
Appends to this game's English bundles under a comment, never overwrites a key that is already there.

  python -I scripts/rebluearchive-strings.py            appends
  python -I scripts/rebluearchive-strings.py --dry      counts only
"""
import io, os, re, sys, importlib.util
HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location('rbacopy', os.path.join(HERE, 'rebluearchive-copy.py'))
C = importlib.util.module_from_spec(spec); spec.loader.exec_module(C)
ASSETS = os.path.join(os.path.expanduser('~'), 'mod-sources', 'rebluearchive', 'core', 'src', 'main', 'assets')
OURS = os.path.join(C.ROOT, 'core', 'src', 'main', 'assets', 'messages')
CATS = ['actors', 'items', 'journal', 'levels', 'misc', 'plants', 'scenes', 'ui', 'windows']

def logical(path):
    """key -> raw value text (continuation lines joined back with their backslashes kept)"""
    out = {}
    if not os.path.exists(path): return out
    lines = io.open(path, encoding='utf-8', errors='replace').read().splitlines()
    i = 0
    while i < len(lines):
        l = lines[i]
        full = l
        while full.endswith('\\') and not full.endswith('\\\\') and i + 1 < len(lines):
            i += 1
            full = full[:-1] + lines[i].lstrip()
        i += 1
        s = full.strip()
        if not s or s.startswith('#') or '=' not in s: continue
        k, v = s.split('=', 1)
        out[k.strip()] = v
    return out

def main():
    dry = '--dry' in sys.argv
    rels = C.plan()
    N = C.names()
    #class key prefixes, old to new
    pre = {}
    for rel in rels:
        old = rel[:-5].replace('/', '.').lower()
        d, f = os.path.split(rel)
        new = ((d.replace('/', '.') + '.') if d else '') + (C.PREFIX + f[:-5])
        pre[old] = new.lower()
    for n in C.TALENT_NESTED:
        pre['actors.hero.talent.' + n.lower()] = 'actors.hero.rbatalents.' + n.lower()
    for k in ('yell', 'yell_akira', 'flee_warning', 'flee_warning_akira', 'flee', 'flee_akira'):
        pre['actors.mobs.npcs.shopkeeper.' + k] = 'rbashims.' + k
    talents = {t.lower() for t in N['talent']}
    classes = {c.lower() for c in N['heroclass']}
    subs = {c.lower() for c in N['subclass']}

    def rename(k):
        parts = k.split('.')
        for i in range(len(parts), 0, -1):
            p = '.'.join(parts[:i])
            if p in pre:
                return '.'.join([pre[p]] + parts[i:])
        if k.startswith('actors.hero.talent.'):
            t = parts[3]
            if t in talents: return 'actors.hero.talent.rba_' + '.'.join(parts[3:])
        if k.startswith('actors.hero.heroclass.'):
            c = parts[3].split('_')[0]
            if c in classes: return 'actors.hero.heroclass.rba_' + '.'.join(parts[3:])
        if k.startswith('actors.hero.herosubclass.'):
            rest = parts[3]
            for s in sorted(subs, key=len, reverse=True):
                if rest == s or rest.startswith(s + '_'):
                    return 'actors.hero.herosubclass.rba_' + rest
        if k.startswith('actors.hero.hero.'):
            c = parts[3].split('_')[0]
            if c in classes: return 'actors.hero.hero.rba_' + '.'.join(parts[3:])
        return None

    total = 0
    for cat in CATS:
        mod = {}
        for folder in ('messages', 'new_messages'):   #new_messages wins
            mod.update(logical(os.path.join(ASSETS, folder, cat, cat + '.properties')))
        ours_path = os.path.join(OURS, cat, cat + '.properties')
        ours = logical(ours_path)
        add = []
        for k, v in mod.items():
            nk = rename(k)
            if nk is None or nk in ours: continue
            ours[nk] = v
            add.append('%s=%s' % (nk, v))
        total += len(add)
        print(cat, len(add))
        if add and not dry:
            s = io.open(ours_path, encoding='utf-8', newline='').read()
            nl = '\r\n' if '\r\n' in s else '\n'
            if not s.endswith('\n'): s += nl
            s += nl + '#rebluearchive, Re Blue Archive Pixel Dungeon' + nl + nl.join(add) + nl
            io.open(ours_path, 'w', encoding='utf-8', newline='').write(s)
    print('total', total)

main()
