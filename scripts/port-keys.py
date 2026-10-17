r"""Copies English message lines from a mod into our message files, by key pattern.

  python -I scripts/port-keys.py <mod key> <regex> [<regex> ...]

Every key in the mod's English .properties files that matches one of the regexes (searched, not anchored)
and is missing here is appended to the file of the same name in ours. Existing keys are never touched.
Use single quotes in the shell so the dollar sign and backslashes reach Python untouched.
Examples
  python -I scripts/port-keys.py shittened '^actors\.hero\.hero\.wait_'
  python -I scripts/port-keys.py shittened '^badges\$badge\.(turd|rats)_'
Prints how many lines went into each file. Use --list to see the matches without writing.
"""
import os, re, sys, glob, io

args = [a for a in sys.argv[1:] if a != '--list']
listing = '--list' in sys.argv
mod, pats = args[0], [re.compile(p) for p in args[1:]]
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
MODS = os.path.expanduser('~/mod-sources')
mod_dir = mod if os.path.isdir(mod) else os.path.join(MODS, mod)
mod_msgs = glob.glob(mod_dir + '/core/src/main/assets/messages/**/*.properties', recursive=True)
our_root = os.path.join(ROOT, 'core/src/main/assets/messages')

def read_keys(path):
    keys = {}
    if not os.path.exists(path):
        return keys
    for line in io.open(path, encoding='utf-8', errors='replace'):
        if '=' in line and not line.lstrip().startswith('#'):
            keys[line.split('=', 1)[0].strip()] = line.rstrip('\r\n')
    return keys

added = {}
for f in sorted(mod_msgs):
    base = os.path.basename(f)
    if re.search(r'_[a-z]{2,3}(_[A-Za-z]+)?\.properties$', base):
        continue
    rel = os.path.relpath(f, mod_dir + '/core/src/main/assets/messages')
    target = os.path.join(our_root, rel)
    have = read_keys(target)
    out = []
    for k, line in read_keys(f).items():
        if k in have:
            continue
        if any(p.search(k) for p in pats):
            out.append(line)
    if out:
        added[rel] = len(out)
        if listing:
            print('\n'.join(out))
            continue
        with io.open(target, 'rb') as fh:
            data = fh.read()
        nl = '\r\n' if b'\r\n' in data else '\n'
        with io.open(target, 'ab') as fh:
            if data and not data.endswith(b'\n'):
                fh.write(nl.encode())
            fh.write((nl.join(out) + nl).encode('utf-8'))
print(added if added else 'nothing to add')
