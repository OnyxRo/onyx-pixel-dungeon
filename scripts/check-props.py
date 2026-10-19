"""Finds .properties lines that end in a lone backslash (a continuation) and are followed by a new key=,
which swallows that key. With --fix, removes the trailing backslash."""
import io, re, sys, glob
fix = '--fix' in sys.argv
key = re.compile(r'^[a-z][\w$]*(\.[\w$]+)+=')
for p in glob.glob('core/src/main/assets/messages/*/*.properties'):
    if '_' in p.split('/')[-1].split('\\')[-1]: continue
    s = io.open(p, encoding='utf-8', newline='').read()
    nl = '\r\n' if '\r\n' in s else '\n'
    rows = s.split(nl)
    n = 0
    for i in range(len(rows) - 1):
        r = rows[i]
        trailing = len(r) - len(r.rstrip('\\'))
        if trailing % 2 == 1 and key.match(rows[i + 1]):
            print(p, i + 1, r[-60:])
            n += 1
            if fix: rows[i] = r[:-1]
    if fix and n:
        io.open(p, 'w', encoding='utf-8', newline='').write(nl.join(rows))
