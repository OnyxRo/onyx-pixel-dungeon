"""Bulk copier for Re-ARranged PD (2026-10-18). Re-ARranged is on Shattered 3.2.0, close enough to this game that
copying every new class at once and fixing the compile errors is faster than the per class copier.

  python -I scripts/rearranged-copy.py <list file>     copies each relative path in the list (one per line)
  python -I scripts/rearranged-copy.py --all           copies every class the mod adds that is not here yet

Paths are relative to the game package. A file that already exists here is never overwritten.
Applies the mod's own renames (below) and nothing from renames.py, whose rows for other mods misfire here.
"""
import io, os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
P = os.path.join('core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
OURS = os.path.join(ROOT, P)
MOD = os.path.join(os.path.expanduser('~'), 'mod-sources', 'rearranged', P)
BASE = os.path.join(os.path.expanduser('~'), 'mod-sources', 'shattered-3.2.0', P)
PKG = 'com.shatteredpixel.shatteredpixeldungeon'

#classes the mod moved or renamed out of Shattered, or that this game has under another name
RULES = [
    (r'\bitems\.weapon\.bow\.SpiritBow\b', 'items.weapon.SpiritBow'),
    (r'\bshatteredpixeldungeon\.levels\.LabsLevel\b', 'shatteredpixeldungeon.levels.worlds.LabsLevel'),
    #talent names another port already uses here
    (r'\bTalent\.(FAST_RELOAD|TOUGH_MEAL|ADRENALINE|BULLSEYE)\b', r'Talent.RA_\1'),
]
#never copied, see PORTING.md
SKIP = {
    'GooglePlayGames.java', 'SeedFinder.java', 'scenes/SeedAnalysisScene.java', 'scenes/SeedFindScene.java',
    'items/weapon/bow/SpiritBow.java', 'levels/LabsLevel.java',
}

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p

def fix(src):
    for a, b in RULES:
        src = re.sub(a, b, src)
    return src

def every_new():
    have = set()
    for d, _, fs in os.walk(OURS):
        have.update(fs)
    out = []
    for d, _, fs in os.walk(LP(MOD)):
        for f in fs:
            if not f.endswith('.java'): continue
            rel = os.path.relpath(os.path.join(d, f), LP(MOD)).replace(os.sep, '/')
            if os.path.exists(os.path.join(BASE, rel)): continue
            out.append(rel)
    return sorted(out)

def main():
    rels = every_new() if sys.argv[1] == '--all' else [l.strip() for l in io.open(sys.argv[1], encoding='utf-8') if l.strip() and not l.startswith('#')]
    n = 0
    for rel in rels:
        if rel in SKIP: continue
        dst = os.path.join(OURS, rel)
        if os.path.exists(dst): continue
        src = io.open(LP(os.path.join(MOD, rel)), encoding='utf-8', newline='').read()
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        io.open(dst, 'w', encoding='utf-8', newline='').write(fix(src))
        n += 1
    print('copied', n)

main()
