"""Adds the counters and settings a Magic Ling port is missing to MagicLingStats, taken from the mod's own declarations.

  python -I scripts/stats-shim.py build/fj.log

Reads javac errors for `variable X ... location: class Statistics` and `method X(...) ... location: class SPDSettings`.
For Statistics fields it copies the mod's declaration (type and default) into MagicLingStats. For SPDSettings methods
it writes in-memory stand ins (a getter returns the stored value or the mod's default, a setter stores it), since
this game has no such settings. Then it rewrites `Statistics.X` and `SPDSettings.X(` in every file under the game
package to point at MagicLingStats, and adds the names to renames.py so later copies get the same treatment.
"""
import os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PK = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
MOD = os.path.expanduser('~/mod-sources/magicling/core/src/main/java/com/shatteredpixel/shatteredpixeldungeon')
STATS = os.path.join(PK, 'MagicLingStats.java')

log = open(sys.argv[1], encoding='utf-8', errors='replace').read()
fields, methods = set(), set()
for m in re.finditer(r'symbol:\s+variable (\w+)\s*\n\s+location: class Statistics', log):
    fields.add(m.group(1))
for m in re.finditer(r'symbol:\s+static (\w+)\s*\n\s+location: class Statistics', log):
    fields.add(m.group(1))
for m in re.finditer(r'symbol:\s+method (\w+)\([^)]*\)\s*\n\s+location: class SPDSettings', log):
    methods.add(m.group(1))

stats = open(STATS, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in stats else '\n'
modstats = open(os.path.join(MOD, 'Statistics.java'), encoding='utf-8', errors='replace').read()
modset = open(os.path.join(MOD, 'SPDSettings.java'), encoding='utf-8', errors='replace').read()
add = []
for f in sorted(fields):
    if re.search(r'\b(?:int|long|float|boolean|String)\s+' + f + r'\b', stats):
        continue
    m = re.search(r'public\s+static\s+(int|long|float|boolean|String)\s+' + f + r'\b\s*(=\s*[^;]+)?;', modstats)
    if not m:
        print('no declaration in the mod for Statistics.' + f)
        continue
    add.append('\tpublic static %s %s%s;' % (m.group(1), f, (' ' + m.group(2)) if m.group(2) else ''))
for name in sorted(methods):
    if re.search(r'\bstatic\s+\w+\s+' + name + r'\s*\(', stats):
        continue
    sigs = re.findall(r'public\s+static\s+(\w+)\s+' + name + r'\s*\(([^)]*)\)\s*\{(.*?)\n\t\}', modset, re.S)
    if not sigs:
        print('no declaration in the mod for SPDSettings.' + name)
        continue
    get = [s for s in sigs if s[0] != 'void' and not s[1].strip()]
    put = [s for s in sigs if s[0] == 'void' and s[1].strip()]
    gtype = get[0][0] if get else (put[0][1].split()[0] if put else 'boolean')
    dflt = {'boolean': 'false', 'int': '0', 'long': '0', 'float': '0', 'String': '""'}.get(gtype, 'null')
    if get:
        dm = re.search(r',\s*([^)]+)\)\s*;', get[0][2])
        if dm and re.fullmatch(r'-?\d+|true|false|-?\d+\.?\d*f?|"[^"]*"', dm.group(1).strip()):
            dflt = dm.group(1).strip()
    var = '_' + name[0].lower() + name[1:]
    add.append('\tprivate static %s %s = %s;' % (gtype, var, dflt))
    add.append('\tpublic static %s %s() { return %s; }' % (gtype, name, var))
    for s in put:
        pname = s[1].split()[-1]
        add.append('\tpublic static void %s( %s ) { %s = %s; }' % (name, s[1].strip(), var, pname))
if add:
    anchor = stats.rindex('}')
    stats = stats[:anchor] + nl + '\t//added by scripts/stats-shim.py from the mod\'s Statistics and SPDSettings' + nl + nl.join(add) + nl + stats[anchor:]
    open(STATS, 'w', encoding='utf-8', newline='').write(stats)

names_f = sorted(f for f in fields if re.search(r'\b' + f + r'\b', stats))
names_m = sorted(m for m in methods if re.search(r'\b' + m + r'\s*\(', stats))
FQ = 'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.'
changed = 0
for d, _, files in os.walk(PK):
    for fn in files:
        if not fn.endswith('.java') or fn == 'MagicLingStats.java':
            continue
        p = os.path.join(d, fn)
        s = open(p, encoding='utf-8', errors='replace', newline='').read()
        t = s
        if names_f:
            alt = '|'.join(names_f)
            t = re.sub(r'\bimport\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.Statistics\.(' + alt + r');', r'import static ' + FQ + r'\1;', t)
            t = re.sub(r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(' + alt + r')\b', FQ + r'\1', t)
        if names_m:
            t = re.sub(r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?SPDSettings\.(' + '|'.join(names_m) + r')\(', FQ + r'\1(', t)
        if t != s:
            open(p, 'w', encoding='utf-8', newline='').write(t)
            changed += 1

#keep later copies in step
rp = os.path.join(ROOT, 'scripts', 'renames.py')
r = open(rp, encoding='utf-8', newline='').read()
rnl = '\r\n' if '\r\n' in r else '\n'
anchor = "    ('Magic Ling blue potion icon'"
rows = []
for kind, names, pat, rep in (
        ('fields', names_f, r"r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(%s)\b'", "r'%s\\1'" % FQ),
        ('settings', names_m, r"r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?SPDSettings\.(%s)\('", "r'%s\\1('" % FQ)):
    if names:
        label = "Magic Ling %s from stats-shim %d" % (kind, abs(hash(tuple(names))) % 100000)
        rows.append("    ('%s', %s, %s)," % (label, pat % '|'.join(names), rep))
if rows and anchor in r:
    r = r.replace(anchor, rnl.join(rows) + rnl + anchor, 1)
    open(rp, 'w', encoding='utf-8', newline='').write(r)
print('fields', len(names_f), 'settings', len(names_m), 'lines added', len(add), 'files changed', changed)
