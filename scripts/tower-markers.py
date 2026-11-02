"""Turns Tower PD's colour markers (#1text#1 .. #9text#9) into this game's highlight (_text_).

  python -I scripts/tower-markers.py [--apply] <properties files...>

This game never read Tower PD's markers, so they showed as text (2026-11-02, about 90 lines). Names lose the
markers, since a name is shown in titles. Colours written as <#52B9D8> (another mod) are left alone. Without
--apply it only lists what would change.
"""
import io, re, sys

PAT = re.compile(r'(?<!<)#([0-9])([^#<>]+?)#\1(?![0-9A-Fa-f])')

args = sys.argv[1:]
apply = '--apply' in args
files = [a for a in args if a != '--apply']
total = 0
for path in files:
    raw = io.open(path, encoding='utf-8', newline='').read()
    out, changed = [], 0
    for line in raw.splitlines(True):
        body = line.rstrip('\r\n')
        end = line[len(body):]
        if '=' in body and not body.lstrip().startswith('#'):
            key, val = body.split('=', 1)
            if key.endswith('.name'):
                new = PAT.sub(lambda m: m.group(2), val)
            else:
                new = PAT.sub(lambda m: '_' + m.group(2).strip() + '_', val)
            if new != val:
                changed += 1
                if not apply and changed <= 3:
                    print(path, key, '->', new[:120])
                body = key + '=' + new
        out.append(body + end)
    total += changed
    print(path, changed, 'lines')
    if apply and changed:
        io.open(path, 'w', encoding='utf-8', newline='').write(''.join(out))
print('total', total)
