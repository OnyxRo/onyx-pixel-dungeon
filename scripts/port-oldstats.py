"""Converts monsters written for the Shattered 0.3 era (Goblins PD, Unleashed PD) to this game's Mob API, in place.

  python -I scripts/port-oldstats.py <mobs folder> Mob1 Mob2 ...

For each class it
  - moves `name = "..."` and the description() text into actors.properties (replacing the placeholder lines port-mob.py wrote)
  - turns atkSkill, dmgRed, dmgMin, dmgMax into attackSkill(), drRoll() and damageRoll() overrides
  - drops mobType, TYPE_EVIL, TYPE_ANIMAL, TYPE_MINDLESS, TYPE_UNDEAD (UNDEAD becomes Property.UNDEAD)
  - folds the static IMMUNITIES / RESISTANCES sets and their immunities() / resistances() overrides into
    immunities.add(...) and resistances.add(...) lines of the initializer
  - changes bare `name` uses in the body into name()
Safe to run twice. Classes already converted are skipped.
"""
import sys, os, re, io

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
PK = ROOT + '/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
PROPS = ROOT + '/core/src/main/assets/messages/actors/actors.properties'

def rd(p): return io.open(p, encoding='utf-8', newline='').read()
def wr(p, s): io.open(p, 'w', encoding='utf-8', newline='').write(s)

def jstr(expr):
    """Joins the pieces of a Java string concatenation into one plain string."""
    parts = re.findall(r'"((?:[^"\\]|\\.)*)"', expr)
    out = ''.join(parts)
    out = out.replace('\\n', '\\n').replace('\\"', '"').replace("\\'", "'").replace('\\\\', '\\')
    return out

def set_prop(props, key, val):
    pat = re.compile(r'^' + re.escape(key) + r'=.*$', re.M)
    line = key + '=' + val
    if pat.search(props):
        return pat.sub(lambda m: line, props, count=1)
    return props.rstrip('\n') + '\n' + line + '\n'

def block_end(s, i):
    """index just after the brace block whose '{' is at or after i"""
    j = s.index('{', i); d = 0
    while True:
        c = s[j]
        if c == '{': d += 1
        elif c == '}':
            d -= 1
            if d == 0: return j + 1
        j += 1

def convert(name, props):
    path = PK + 'actors/mobs/' + name + '.java'
    if not os.path.exists(path): print('missing', name); return props
    s = rd(path)
    if 'port-oldstats' in s: return props
    nl = '\r\n' if '\r\n' in s else '\n'
    s = s.replace('\r\n', '\n')
    key = 'actors.mobs.' + name.lower()
    m = re.search(r'\n\s*name\s*=\s*([^;]+);', s)
    if m:
        props = set_prop(props, key + '.name', jstr(m.group(1)))
        s = s.replace(m.group(0), '', 1)
    m = re.search(r'\n\s*@Override\s*\n\s*public\s+String\s+description\(\s*\)\s*\{', s)
    if m:
        e = block_end(s, m.end() - 1)
        body = s[m.end():e]
        r = re.search(r'return\s+([^;]+);', body, re.S)
        if r:
            props = set_prop(props, key + '.desc', jstr(r.group(1)))
        s = s[:m.start()] + s[e:]
    stats = {}
    for f in ('atkSkill', 'dmgRed', 'dmgMin', 'dmgMax'):
        m = re.search(r'\n[ \t]*' + f + r'\s*=\s*([^;]+);', s)
        if m:
            stats[f] = m.group(1).strip(); s = s.replace(m.group(0), '', 1)
    extra = []
    for t in ('TYPE_EVIL', 'TYPE_ANIMAL', 'TYPE_MINDLESS', 'TYPE_UNDEAD'):
        m = re.search(r'\n[ \t]*' + t + r'\s*=\s*true;', s)
        if m:
            s = s.replace(m.group(0), '', 1)
            if t == 'TYPE_UNDEAD': extra.append('properties.add( Property.UNDEAD );')
    m = re.search(r'\n[ \t]*mobType\s*=\s*\w+;', s)
    if m: s = s.replace(m.group(0), '', 1)
    # resistance sets
    sets = {}
    for kind in ('IMMUNITIES', 'RESISTANCES'):
        m = re.search(r'\n\s*(?:private\s+)?static\s+final\s+HashSet<Class<\?>>\s+' + kind + r'\s*=\s*new[^;]*;\s*\n\s*static\s*\{', s)
        if m:
            e = block_end(s, m.end() - 1)
            sets[kind] = re.findall(kind + r'\.add\(\s*([\w.]+)\.class\s*\)', s[m.end():e])
            s = s[:m.start()] + s[e:]
        m = re.search(r'\n\s*@Override\s*\n\s*public\s+HashSet<Class<\?>>\s+' + kind.lower() + r'\(\s*\)\s*\{', s)
        if m:
            e = block_end(s, m.end() - 1)
            s = s[:m.start()] + s[e:]
    for kind, cl in sets.items():
        for c in cl:
            extra.append(kind.lower() + '.add( ' + c + '.class );')
    # insert into the initializer, the first '{' block directly inside the class
    ci = re.search(r'public\s+(?:abstract\s+)?class\s+' + name + r'\b[^{]*\{', s)
    ib = re.compile(r'\n[ \t]*\{').search(s, ci.end() - 1)
    ie = block_end(s, ib.start())
    init = s[ib.start():ie - 1].rstrip() + '\n'
    for x in extra: init += '        ' + x + '\n'
    init += '    }\n'
    meths = ''
    if 'atkSkill' in stats and 'attackSkill(' not in s:
        meths += '\n    @Override\n    public int attackSkill( Char target ) {\n        return %s;\n    }\n' % stats['atkSkill']
    if 'dmgMin' in stats or 'dmgMax' in stats:
        meths += '\n    @Override\n    public int damageRoll() {\n        return Random.NormalIntRange( %s, %s );\n    }\n' % (stats.get('dmgMin', '1'), stats.get('dmgMax', '3'))
    if 'dmgRed' in stats:
        meths += '\n    @Override\n    public int drRoll() {\n        return Random.NormalIntRange( 0, %s );\n    }\n' % stats['dmgRed']
    s = s[:ib.start()] + init + meths + s[ie:]
    s = re.sub(r'(?<![\w."])name(?=\s*[,)])', 'name()', s)
    for imp in ('actors.Char', 'actors.mobs.Mob'):
        pass
    def addimp(cls, pkg):
        nonlocal s
        if re.search(r'\b' + cls + r'\b', s) and 'import ' + pkg + '.' + cls + ';' not in s and not ('package ' + pkg + ';') in s:
            s = re.sub(r'(package [\w.]+;\n)', r'\1\nimport ' + pkg + '.' + cls + ';', s, count=1)
    addimp('Char', 'com.shatteredpixel.shatteredpixeldungeon.actors')
    addimp('Random', 'com.watabou.utils')
    s = s.replace('public class ' + name, '//port-oldstats\npublic class ' + name, 1)
    wr(path, s.replace('\n', nl))
    print('converted', name, list(stats))
    return props

if __name__ == '__main__':
    props = rd(PROPS)
    for n in sys.argv[1:]:
        props = convert(n, props)
    wr(PROPS, props)
