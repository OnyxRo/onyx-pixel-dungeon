"""Lists lines of the English message files that still hold Chinese text, the mod's English file left them untranslated.

  python -I scripts/find-untranslated.py [--out=file.txt] [--prefix=actors.mobs.tumulus]

Prints a count per bundle, and with --out writes key=value lines (UTF-8) for translating.
Put the translations back with  python -I scripts/find-untranslated.py --apply=translated.txt
(same key=value format, only keys that exist are replaced).
"""
import glob, re, sys

CJK = re.compile('[぀-ヿ一-鿿]')
FILES = [f for f in glob.glob('core/src/main/assets/messages/*/*.properties') if not re.search(r'_[a-z]{2}(-[a-z]+)?\.properties$', f)]
opts = dict(a[2:].split('=', 1) for a in sys.argv[1:] if a.startswith('--') and '=' in a)

if 'apply' in opts:
    table = {}
    for l in open(opts['apply'], encoding='utf-8'):
        if '=' in l and not l.startswith('#'):
            k, v = l.rstrip('\n').split('=', 1)
            table[k.strip()] = v
    done = 0
    for f in FILES:
        s = open(f, encoding='utf-8', newline='').read()
        nl = '\r\n' if '\r\n' in s else '\n'
        lines = s.split(nl)
        for i, l in enumerate(lines):
            k = l.split('=', 1)[0].strip()
            if k in table and '=' in l:
                lines[i] = k + '=' + table[k]
                done += 1
        open(f, 'w', encoding='utf-8', newline='').write(nl.join(lines))
    print('replaced', done, 'of', len(table))
    sys.exit()

out = []
for f in FILES:
    n = 0
    for l in open(f, encoding='utf-8'):
        if l.lstrip().startswith('#') or '=' not in l:
            continue
        if CJK.search(l) and l.startswith(opts.get('prefix', '')):
            out.append(l.rstrip('\n'))
            n += 1
    if n:
        print('%5d  %s' % (n, f.replace('\\', '/')))
print('total', len(out))
if 'out' in opts:
    open(opts['out'], 'w', encoding='utf-8').write('\n'.join(out) + '\n')
