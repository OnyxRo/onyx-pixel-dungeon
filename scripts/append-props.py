"""Appends text lines to a .properties (or any text) file with that file's own line endings.

  python -I scripts/append-props.py <target file> <lines file>
  python -I scripts/append-props.py <target file> <mod properties> --rename old.prefix.=new.prefix. [--rename ...] [--comment "text"]

The first form appends every line of the lines file. The second copies only the lines of a mod's properties file
whose key starts with one of the old prefixes, rewritten to the new prefix (how the Tower PD menu strings were
copied). Keys the target already has are skipped. Several message files here are CRLF, and appending LF lines from
a script left them mixed (2026-11-01).
"""
import io, sys

args = sys.argv[1:]
if len(args) < 2:
    print(__doc__)
    sys.exit(1)
target, source = args[0], args[1]
renames, comment = [], None
i = 2
while i < len(args):
    if args[i] == '--rename':
        old, new = args[i + 1].split('=', 1)
        renames.append((old, new))
        i += 2
    elif args[i] == '--comment':
        comment = args[i + 1]
        i += 2
    else:
        print('unknown option ' + args[i])
        sys.exit(1)

text = io.open(target, encoding='utf-8', newline='').read()
nl = '\r\n' if '\r\n' in text else '\n'
have = set(l.split('=', 1)[0] for l in text.splitlines() if '=' in l and not l.startswith('#'))

lines = io.open(source, encoding='utf-8').read().splitlines()
out = []
for l in lines:
    if renames:
        hit = [r for r in renames if l.startswith(r[0])]
        if not hit:
            continue
        l = hit[0][1] + l[len(hit[0][0]):]
    if '=' in l and not l.startswith('#') and l.split('=', 1)[0] in have:
        continue
    out.append(l)

if not out:
    print('nothing to append')
    sys.exit(0)
if comment:
    out.insert(0, '#' + comment)
if text and not text.endswith(nl):
    text += nl
text += nl + nl.join(out) + nl
io.open(target, 'w', encoding='utf-8', newline='').write(text)
print('appended %d lines with %s endings' % (len(out), 'CRLF' if nl == '\r\n' else 'LF'))
