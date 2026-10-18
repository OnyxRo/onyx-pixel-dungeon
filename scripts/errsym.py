"""Groups javac 'cannot find symbol' errors by the missing symbol and where it was looked up.
  python -I scripts/errsym.py [log]        log defaults to build/fc.log
Prints count, symbol, location, and up to three files, most common first. Other errors are counted by message."""
import re, sys, collections
log = sys.argv[1] if len(sys.argv) > 1 else 'build/fc.log'
t = open(log, encoding='utf-8', errors='replace').read().split('\n')
syms = collections.OrderedDict()
other = collections.Counter()
for i, l in enumerate(t):
    m = re.match(r'.*[\\/]([A-Za-z0-9_]+\.java):(\d+): error: (.*)', l)
    if not m: continue
    if 'cannot find symbol' not in m.group(3):
        other[m.group(3)[:90]] += 1
        continue
    sym = loc = ''
    for j in range(i + 1, min(i + 6, len(t))):
        s = t[j].strip()
        if s.startswith('symbol:'): sym = re.sub(r'\s+', ' ', s[7:].strip())
        if s.startswith('location:'):
            loc = re.sub(r'\s+', ' ', s[9:].strip())
            loc = re.sub(r'com\.shatteredpixel\.shatteredpixeldungeon\.', '', loc)
            break
    if loc.startswith('class ') and sym.startswith('class '): loc = 'import or name'
    key = (sym, re.sub(r'^(variable|class|interface) \w+ of type ', 'type ', loc))
    syms.setdefault(key, []).append(m.group(1))
for (sym, loc), files in sorted(syms.items(), key=lambda kv: -len(kv[1])):
    print('%3d  %-40s %-45s %s' % (len(files), sym[:40], loc[:45], ' '.join(sorted(set(files))[:3])))
print('---- other errors')
for k, v in other.most_common(): print('%3d  %s' % (v, k))
