"""Ports monsters from a Shattered based mod in one command.

  python -I scripts/port-mob.py <mod key or folder> <roster key> Mob1 Mob2 ... [--weight=1] [--min=1] [--max=99] [--no-check]

  mod key or folder  a key from scripts/mods.txt (cloned with scripts/get-mod.sh) or a path to the mod's source
  roster key         the mod's short key in ModRoster and WorldShuffle.modKey, like "reforged"

What it does, in order
  1. copies each monster class and its sprite class from the mod
  2. runs scripts/port-copier.py, which compiles and copies whatever else is missing (buffs, blobs, items, assets)
  3. runs scripts/port-strings.py to copy the English text of everything new
  4. adds the monsters to ModRoster, so they spawn in the mod's worlds, show in the bestiary and the sandbox list
  5. runs scripts/check.sh --quick on them, headless
  It then prints what is left. The picture check is separate, scripts/arena.sh.

It cannot port a monster whose stats live in a table (Reforged), or one that needs the mod's own systems.
scripts/mod-report.py says which those are first. Run it before this.
Output is long, so it goes to build/port-mob.log and only a summary is printed.
"""
import sys, os, re, io, glob, shutil, subprocess

pos = [a for a in sys.argv[1:] if not a.startswith('--')]
opts = {a.split('=')[0]: (a.split('=')[1] if '=' in a else True) for a in sys.argv[1:] if a.startswith('--')}
if len(pos) < 3:
    print(__doc__); sys.exit(1)
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
mod_arg, KEY, names = pos[0], pos[1], pos[2:]
MODS = os.environ.get('MOD_SOURCES', os.path.expanduser('~/mod-sources'))
MOD = mod_arg if os.path.isdir(mod_arg) else os.path.join(MODS, mod_arg)
if not os.path.isdir(MOD):
    print('no mod folder at', MOD, ' try scripts/get-mod.sh', mod_arg); sys.exit(1)
PK = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
os.makedirs(ROOT + '/build', exist_ok=True)
LOG = ROOT + '/build/port-mob.log'
open(LOG, 'w').close()

def LP(p):
    p = os.path.abspath(p)
    bs = chr(92)
    return bs * 2 + '?' + bs + p if os.name == 'nt' and not p.startswith(bs * 2) else p
def rd(p): return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()
def wr(p, s): io.open(LP(p), 'w', encoding='utf-8', newline='').write(s)
def run(cmd):
    with open(LOG, 'a') as f:
        f.write('\n$ ' + ' '.join(cmd) + '\n'); f.flush()
        return subprocess.run(cmd, cwd=ROOT, stdout=f, stderr=subprocess.STDOUT).returncode

import importlib.util
_spec = importlib.util.spec_from_file_location('renames', os.path.join(os.path.dirname(os.path.abspath(__file__)), 'renames.py'))
renames = importlib.util.module_from_spec(_spec); _spec.loader.exec_module(renames)

def apply_rules(rel_files):
    applied = {}
    for rel in rel_files:
        path = ROOT + '/' + PK + rel
        if not os.path.exists(path): continue
        s = rd(path)
        t, fired = renames.apply(s)
        for why in fired: applied[why] = applied.get(why, 0) + 1
        if t != s: wr(path, t)
    return applied

MPK = renames.mod_package_dir(MOD)
mod_files = {}
for f in glob.glob(LP(MOD) + '/' + MPK + '**/*.java', recursive=True):
    mod_files.setdefault(os.path.basename(f)[:-5], []).append(f)

copied = []
def copy_class(name):
    cands = mod_files.get(name, [])
    if len(cands) != 1:
        return None
    rel = os.path.relpath(cands[0], LP(MOD) + '/' + MPK).replace(os.sep, '/')
    dest = ROOT + '/' + PK + rel
    if os.path.exists(dest):
        return rel
    os.makedirs(os.path.dirname(dest), exist_ok=True)
    shutil.copy(cands[0], dest)
    wr(dest, renames.apply(rd(dest))[0])
    copied.append(rel)
    return rel

problems = []
for n in names:
    rel = copy_class(n)
    if not rel:
        problems.append('%s: %s' % (n, 'not found in the mod' if n not in mod_files else 'name is not unique in the mod'))
        continue
    src = rd(ROOT + '/' + PK + rel)
    if re.search(r'GetConstants\s*\(\)', src):
        problems.append(n + ': stats come from a table, use scripts/reforged-stats.py and port by hand')
    m = re.search(r'spriteClass\s*=\s*(\w+)\.class', src)
    if m: copy_class(m.group(1))
MODNAME = os.path.basename(os.path.normpath(MOD))
print('copied', len(copied), 'files from', MODNAME)

print('closing dependencies with port-copier (this compiles several times, a few minutes) ...')
tag = KEY
rc = run([sys.executable, '-I', 'scripts/port-copier.py', '.', MOD, tag, '12'])
log = rd(LOG)
tail = log[log.rfind('COPIED:'):] if 'COPIED:' in log else ''
remaining = tail[tail.find('REMAINING:'):] if 'REMAINING:' in tail else ''
rem_lines = [l for l in remaining.split('\n')[1:] if l.strip()]

run([sys.executable, '-I', 'scripts/port-strings.py', '.', MOD, MODNAME])

# some mods never wrote text for a monster, so give those a plain name and description, and say so
props = ROOT + '/core/src/main/assets/messages/actors/actors.properties'
ptext = rd(props)
placeholders = []
for n in names:
    key = 'actors.mobs.' + n.lower()
    if key + '.name=' in ptext or not os.path.exists(ROOT + '/' + PK + 'actors/mobs/' + n + '.java'): continue
    words = re.sub(r'(?<=[a-z0-9])(?=[A-Z])', ' ', n).lower()
    ptext += '\n%s.name=%s\n%s.desc=The %s, from %s. The mod gave it no description.' % (key, words, key, words, MODNAME)
    placeholders.append(n)
if placeholders:
    wr(props, ptext.rstrip('\n') + '\n')
    print('no text in the mod for', len(placeholders), 'monsters, wrote placeholders for', ', '.join(placeholders))

# fix known drift in everything that is new
new_files = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], cwd=ROOT, capture_output=True, text=True).stdout
new_rel = [l[3:].strip()[len(PK):] for l in new_files.split('\n') if l.startswith('??') and l.strip().endswith('.java') and l[3:].strip().startswith(PK)]
for why, n in apply_rules(new_rel).items(): print('fixed', n, 'x', why)
if new_rel:
    # compile again now the rules ran, so the errors reported below are the ones that remain
    run([sys.executable, '-I', 'scripts/port-copier.py', '.', MOD, tag, '6'])
    log = rd(LOG)
    tail = log[log.rfind('COPIED:'):] if 'COPIED:' in log else ''
    remaining = tail[tail.find('REMAINING:'):] if 'REMAINING:' in tail else ''
    rem_lines = [l for l in remaining.split(chr(10))[1:] if l.strip()]

# register in ModRoster, once per monster
rost = ROOT + '/' + PK + 'actors/mobs/ModRoster.java'
s = rd(rost)
nl = '\r\n' if '\r\n' in s else '\n'
added = []
for n in names:
    if n in problems or any(p.startswith(n + ':') for p in problems): continue
    if re.search(r'add\(\s*"%s",\s*%s\.class' % (re.escape(KEY), n), s): continue
    line = '\t\tadd( "%s", %s.class, %s, %s, %s );' % (KEY, n, opts.get('--weight', 1), opts.get('--min', 1), opts.get('--max', 99))
    added.append(line)
if added:
    anchor = nl + '\t}' + nl + nl + '\t/** Every monster on the roster'
    i = s.index(anchor)
    s = s[:i] + nl + nl.join(added) + s[i:]
    wr(rost, s)
print('added to ModRoster:', len(added))

print('compile errors left:', len(rem_lines))
_log = rd(LOG)
_sum = _log[_log.rfind('ERRORS BY TYPE:'):].splitlines()[1:] if 'ERRORS BY TYPE:' in _log else []
for l in [x for x in _sum if x.startswith('  ')][:14]: print('  ', l.strip()[:160])
if rem_lines: print('   full errors in build/compile-errors.log')
if problems:
    print('could not port:')
    for p in problems: print('   ', p)
if '--no-check' not in opts and not rem_lines:
    rc = run(['bash', 'scripts/check.sh', '--quick'] + [n for n in names if not any(p.startswith(n + ':') for p in problems)])
    out = rd(ROOT + '/build/check.log')
    for l in out.split('\n'):
        if l.startswith(('FAIL', 'WARN', 'SUMMARY')): print('  ', l[:200])
    print('check', 'passed' if rc == 0 else 'FAILED')
print('log:', os.path.relpath(LOG, ROOT), ' next: scripts/arena.sh', ' '.join(names))
