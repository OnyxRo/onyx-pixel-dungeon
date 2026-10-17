"""Copies message keys from a mod's English bundle by key pattern, for text that does not belong to one class
(talents, hero classes, subclasses, ability groups).

  python -I scripts/port-keys.py <mod key> <bundle> <label> <regex> [<regex> ...]

  bundle   the bundle path without extension, for example actors/actors or items/items
  regex    matched against the whole key, case insensitive, for example
           "actors\\.hero\\.talent\\.(tranquil_tincture|ravens_eye)\\."

Keys we already have are never touched. Copied lines go at the end of our bundle under a ### label line.
Prints how many keys were copied and the key prefixes that matched nothing, so a typo is easy to see.
"""
import io
import os
import re
import sys

MOD, BUNDLE, LABEL = sys.argv[1], sys.argv[2], sys.argv[3]
_raw = sys.argv[4:]
if _raw and _raw[0].startswith('@'):
    # patterns from a file, one per line, so the shell never sees the dollar signs and backslashes
    _raw = [l.strip() for l in open(_raw[0][1:], encoding='utf-8') if l.strip() and not l.startswith('#')]
PATS = [re.compile(p, re.I) for p in _raw]
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
HOME = os.path.expanduser('~/mod-sources')
PFX = '\\\\?\\'


def LP(p):
    p = os.path.abspath(p)
    return PFX + p if os.name == 'nt' and not p.startswith(PFX) else p


def rd(p):
    return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()


src = None
for d, _, fs in os.walk(LP(os.path.join(HOME, MOD))):
    parts = d.replace('\\', '/')
    if parts.endswith('/assets/messages/' + os.path.dirname(BUNDLE)):
        n = os.path.basename(BUNDLE)
        for cand in (n + '_en.properties', n + '.properties'):
            if cand in fs:
                src = os.path.join(d, cand)
                break
        if src:
            break
if not src:
    raise SystemExit('no bundle %s in %s' % (BUNDLE, MOD))
dst = os.path.join(ROOT, 'core/src/main/assets/messages', BUNDLE + '.properties')
ours = rd(dst)
nl = '\r\n' if '\r\n' in ours else '\n'
have = set(re.findall(r'^([^#=\s][^=]*)=', ours, flags=re.M))
add, hit = [], [0] * len(PATS)
lines = rd(src).replace('\r\n', '\n').split('\n')
for line in lines:
    m = re.match(r'([^#=\s][^=]*)=', line)
    if not m:
        continue
    k = m.group(1)
    for i, p in enumerate(PATS):
        if p.search(k):
            hit[i] += 1
            if k not in have:
                add.append(line)
                have.add(k)
            break
if add:
    ours = ours.rstrip('\r\n') + nl + nl + '###' + LABEL + nl + nl.join(add) + nl
    io.open(dst, 'w', encoding='utf-8', newline='').write(ours)
print('copied %d keys into %s' % (len(add), BUNDLE))
for p, h in zip(PATS, hit):
    if h == 0:
        print('pattern matched nothing:', p.pattern)
