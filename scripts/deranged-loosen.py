"""DERanged PD's Rat King counts as every class and, through his talents, as every subclass (2026-10-22).
This rewrites game logic checks like  hero.heroClass == HeroClass.MAGE  into  hero.heroClass.is(HeroClass.MAGE)
and  hero.subClass == HeroSubClass.SNIPER  into  hero.subClass.is(HeroSubClass.SNIPER), for the classes and subclasses
the King shares (Shattered's six and Re-ARranged's six). For every other hero is() is plain equality, so normal runs
do not change. Windows, scenes, badges and rankings keep exact checks.

  python -I scripts/deranged-loosen.py [--dry]      prints how many checks changed per file
"""
import io, os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PKG = os.path.join(ROOT, 'core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
DIRS = ['actors', 'items', 'levels', 'mechanics', 'plants']
SKIP = {'HeroClass.java', 'HeroSubClass.java', 'DerangedKing.java', 'Badges.java'}

CLASSES = 'WARRIOR MAGE ROGUE HUNTRESS DUELIST CLERIC GUNNER SAMURAI ADVENTURER KNIGHT MEDIC ARCHER'.split()
SUBS = ('BERSERKER GLADIATOR VETERAN BATTLEMAGE WARLOCK WIZARD ASSASSIN FREERUNNER CHASER SNIPER WARDEN FIGHTER '
        'CHAMPION MONK FENCER PRIEST PALADIN ENCHANTER OUTLAW GUNSLINGER SPECIALIST SLASHER MASTER SLAYER '
        'ENGINEER EXPLORER RESEARCHER DEATHKNIGHT HORSEMAN CRUSADER SAVIOR THERAPIST MEDICALOFFICER BOWMASTER '
        'JUGGLER SHARPSHOOTER').split()

#an expression ending in .heroClass or .subClass, or the bare field
PRE = r'((?:\(\([A-Za-z.]+\)\s*\w+\)|[A-Za-z_][\w.]*(?:\(\))?)\.)?'

def rewrite(src, field, enum, names):
    alt = '|'.join(names)
    pq = r'((?:[\w.]+\.)?' + enum + ')'
    def eq(m):
        return (m.group(1) or '') + field + '.is(' + m.group(2) + '.' + m.group(3) + ')'
    def ne(m):
        return '!' + (m.group(1) or '') + field + '.is(' + m.group(2) + '.' + m.group(3) + ')'
    src, a = re.subn(r'(?<![\w.])' + PRE + field + r'\s*==\s*' + pq + r'\.(' + alt + r')\b', eq, src)
    src, b = re.subn(r'(?<![\w.])' + PRE + field + r'\s*!=\s*' + pq + r'\.(' + alt + r')\b', ne, src)
    return src, a + b

def main():
    dry = '--dry' in sys.argv
    total = 0
    for d in DIRS:
        for root, _, fs in os.walk(os.path.join(PKG, d)):
            for f in fs:
                if not f.endswith('.java') or f in SKIP: continue
                p = os.path.join(root, f)
                s = io.open(p, encoding='utf-8', newline='').read()
                t, n1 = rewrite(s, 'heroClass', 'HeroClass', CLASSES)
                t, n2 = rewrite(t, 'subClass', 'HeroSubClass', SUBS)
                if n1 + n2:
                    total += n1 + n2
                    print(n1 + n2, os.path.relpath(p, PKG))
                    if not dry: io.open(p, 'w', encoding='utf-8', newline='').write(t)
    print('total', total)

main()
