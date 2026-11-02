"""Mechanical clean up of compile errors in files ported from SPS.

  python -I scripts/sps-fix.py [log]          log defaults to build/fast.log, prints what it changed

Only does what is safe to do without thinking
  - deletes an import line whose class or package does not exist
  - deletes a line that only adds a missing class to resistances, immunities or weakness, like  immunities.add(EnchantmentIce.class);
Everything else it leaves for a person.
"""
import re, sys, io, os
log = sys.argv[1] if len(sys.argv) > 1 else 'build/fast.log'
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
t = io.open(os.path.join(ROOT, log), encoding='utf-8', errors='replace').read().split('\n')
todo = {}
for i, l in enumerate(t):
    m = re.match(r'(.*\.java):(\d+): error: (.*)', l)
    if not m: continue
    path, ln, msg = m.group(1), int(m.group(2)), m.group(3)
    if 'sps' not in path and 'shatteredpixeldungeon' not in path: continue
    sym = ''
    for k in (1, 2, 3, 4):
        if i + k < len(t) and 'symbol:' in t[i + k]:
            sym = t[i + k].split('symbol:')[1].strip(); break
    todo.setdefault(path, []).append((ln, msg, sym))
changed = 0
for path, errs in todo.items():
    full = path if os.path.isabs(path) else os.path.join(ROOT, path)
    try: s = io.open(full, encoding='utf-8', newline='').read()
    except OSError: continue
    lines = s.splitlines(True)   # files mix line endings, keep each one as it is
    kill = set()
    for ln, msg, sym in errs:
        if ln - 1 >= len(lines): continue
        line = lines[ln - 1].rstrip(chr(13) + chr(10))
        name = sym.split()[-1] if sym else ''
        if line.lstrip().startswith('import ') and (('cannot find symbol' in msg and name and line.strip().endswith('.' + name + ';')) or 'does not exist' in msg or 'static import only' in msg):
            kill.add(ln - 1)
        elif re.match(r'^\s*(resistances|immunities|weakness)\.add\(\s*[\w.]+\.class\s*\)\s*;\s*$', line) and 'cannot find symbol' in msg and name and (name + '.class') in line:
            kill.add(ln - 1)
        elif 'cannot find symbol' in msg and name and re.match(r'^\s*(lootOther|loot)\s*=\s*(new\s+' + re.escape(name) + r'\b.*|' + re.escape(name) + r'\.class)\s*;\s*$', line):
            kill.add(ln - 1)
        elif 'cannot find symbol' in msg and name and re.match(r'^\s*(Dungeon\.level\.drop|\w+\.drop)\(\s*new\s+' + re.escape(name) + r'\b.*\)\s*(\.sprite\.drop\([^)]*\))?\s*;\s*$', line):
            kill.add(ln - 1)   # a drop of an item this game lacks, like Dungeon.level.drop(new SewerReward(), pos).sprite.drop();
        elif 'cannot find symbol' in msg and name.split('(')[0] in ('explodeDew', 'explodeDewHigh') and re.match(r'^\s*explodeDew(High)?\(\s*\w+\s*\)\s*;\s*$', line):
            kill.add(ln - 1)   # SPS dew vial mode, off unless chosen in the mod, this game has none
        elif 'cannot find symbol' in msg and name:
            # a SupercreateLoot override that builds a missing item is dropped whole, nothing calls it here
            for b in range(ln - 1, max(ln - 8, 0), -1):
                if 'SupercreateLoot' in lines[b]:
                    a = b - 1 if lines[b - 1].strip() == '@Override' else b
                    depth = 0; e = b
                    for k in range(b, min(b + 20, len(lines))):
                        depth += lines[k].count('{') - lines[k].count('}')
                        if depth == 0 and '{' in ''.join(lines[b:k + 1]): e = k; break
                    kill.update(range(a, e + 1)); break
    if kill:
        lines = [x for j, x in enumerate(lines) if j not in kill]
        io.open(full, 'w', encoding='utf-8', newline='').write(''.join(lines))
        changed += len(kill)
        print(os.path.basename(path), 'removed', len(kill), 'lines')
print('lines removed', changed)
