"""Shows the changes Re Blue Archive PD made to one of Shattered's classes, only the hunks that name something the
mod added (its classes, talents, subclasses, heroes). For finding the hooks to port.

  python -I scripts/rebluearchive-hunks.py actors/Char.java [more files]     prints to stdout
"""
import io, os, re, sys, difflib, importlib.util
HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location('rbacopy', os.path.join(HERE, 'rebluearchive-copy.py'))
C = importlib.util.module_from_spec(spec); spec.loader.exec_module(C)
N = C.names()
sys.stdout.reconfigure(encoding="utf-8")
words = set(C.classes(C.plan()).keys()) | set(N['talent']) | set(N['heroclass']) | set(N['subclass']) | set(C.TALENT_NESTED)
words |= {'yellW', 'yellI', 'yellN', 'yellP', 'yellH', 'onEnemyKill', 'onDefenseProc', 'onTrampleGrass', 'speedBoost', 'onKill'}
pat = re.compile(r'\b(' + '|'.join(sorted(words, key=len, reverse=True)) + r')\b')
ARGS = [l.strip() for a in sys.argv[1:] for l in (io.open(a[1:]).read().split() if a.startswith("@") else [a])]
for rel in ARGS:
    a = [l.rstrip() for l in C.rd(os.path.join(C.BASE, rel)).splitlines()]
    b = [l.rstrip() for l in C.rd(os.path.join(C.MOD, rel)).splitlines()]
    a = [l for l in a if not l.startswith('import ')]
    b = [l for l in b if not l.startswith('import ')]
    hunk = []
    def flush():
        if hunk and any(pat.search(l) for l in hunk if l[:1] in '+-'):
            print('\n'.join(hunk))
    for l in difflib.unified_diff(a, b, lineterm='', n=3):
        if l.startswith('@@'):
            flush(); hunk = [rel + ' ' + l]
        elif l.startswith('---') or l.startswith('+++'):
            continue
        else:
            hunk.append(l)
    flush()
