"""Tiny patch helper. rep(file, old, new) replaces one exact occurrence, skips when new is already there,
fails loudly when old is missing or not unique. Keeps CRLF files CRLF."""
import sys
R = 'core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/'
FAIL = []
def rep(f, old, new, allow_many=False):
    p = R + f
    raw = open(p, encoding='utf-8', newline='').read()
    crlf = '\r\n' in raw
    s = raw.replace('\r\n', '\n')
    if new in s and new.strip():
        print('skip', f); return
    c = s.count(old)
    if c == 0:
        # tolerate trailing whitespace differences, blank lines often hold tabs
        import re
        pat = r'[ \t]*\n'.join(re.escape(l.rstrip(' \t')) for l in old.split('\n'))
        ms = list(re.finditer(pat, s))
        if len(ms) == 1:
            old = ms[0].group(0); c = 1
    if c == 0 or (c > 1 and not allow_many):
        FAIL.append('%s: found %d of %r' % (f, c, old[:80])); print('FAIL', f, c, repr(old[:80])); return
    s = s.replace(old, new) if allow_many else s.replace(old, new, 1)
    if crlf: s = s.replace('\n', '\r\n')
    open(p, 'w', encoding='utf-8', newline='').write(s)
    print('ok', f)
def done():
    print('FAILURES', len(FAIL))
