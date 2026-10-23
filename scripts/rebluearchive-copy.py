"""Bulk copier for Re Blue Archive PD (2026-10-22, key rebluearchive). The mod is on Shattered 3.0.0 and shares
many class names with Blue Archive PD and with Re-ARranged PD (same author), so every class it adds is copied
with an RBA prefix (Gun becomes RBAGun, Division becomes RBADivision) and every enum entry it adds to Shattered's
Talent, HeroClass and HeroSubClass gets RBA_ (ARIS becomes RBA_ARIS). The same goes for the constants it added to
ItemSpriteSheet, BuffIndicator, HeroIcon and Assets.

  python -I scripts/rebluearchive-copy.py --plan        lists what would be copied
  python -I scripts/rebluearchive-copy.py --all         copies every class (never overwrites a file)
  python -I scripts/rebluearchive-copy.py --refix       runs the rename rules again over the copies (overwrites them
                                                         from the mod source, so only before hand edits)
  python -I scripts/rebluearchive-copy.py --names       prints the renamed constant lists as JSON for other tools

Needs Shattered 3.0.0 at <scratchpad>/spd300 or at ~/mod-sources/shattered-3.0.0 (to tell what the mod added).
"""
import io, os, re, sys, json

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
P = os.path.join('core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
OURS = os.path.join(ROOT, P)
H = os.path.expanduser('~')
MOD = os.path.join(H, 'mod-sources', 'rebluearchive', P)
BASE = os.environ.get('SPD300') or os.path.join(H, 'mod-sources', 'shattered-3.0.0', P)
PKG = 'com.shatteredpixel.shatteredpixeldungeon'
PREFIX = 'RBA'

#never copied, see PORTING.md
SKIP = {
    'GameError.java', 'SeedFinder.java', 'scenes/SeedAnalysisScene.java', 'scenes/SeedFindScene.java',
    'ui/TitleBackground.java', 'ui/changelist/ChangeIcons.java', 'ui/changelist/Pixel_Dungeon_Changes.java',
    'ui/changelist/v4_X_Changes.java', 'windows/WndImpOld.java',
}
#classes that exist here already (from Re-ARranged) but that the mod rewrote, copied under the prefix anyway
FORCE = {'items/GunSmithingTool.java', 'actors/buffs/ArmorBreak.java', 'effects/ThunderBolt.java',
    'effects/particles/LightParticle.java', 'effects/particles/YellowParticle.java', 'items/weapon/melee/quick/QuickWeapon.java'}

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p

def rd(p):
    return io.open(LP(p), encoding='utf-8', errors='replace', newline='').read()

def walk(root):
    out = []
    for d, _, fs in os.walk(LP(root)):
        for f in fs:
            if f.endswith('.java'):
                out.append(os.path.relpath(os.path.join(d, f), LP(root)).replace(os.sep, '/'))
    return out

def exists(root, rel):
    return os.path.exists(LP(os.path.join(root, rel.replace('/', os.sep))))

def plan():
    out = []
    for rel in sorted(walk(MOD)):
        if rel in SKIP: continue
        if exists(BASE, rel) and rel not in FORCE: continue
        if exists(OURS, rel) and '/gun/' not in rel and rel not in FORCE: continue
        out.append(rel)
    return out

def newname(rel):
    d, f = os.path.split(rel)
    return (d + '/' if d else '') + PREFIX + f

# ---- constants the mod added to Shattered's classes -------------------------------------------
def enum_consts(src):
    body = src[src.index('{', src.index('enum ')) + 1:]
    body = body[:body.index(';')]
    body = re.sub(r'//[^\n]*', '', body)
    body = re.sub(r'\([^()]*(\([^()]*\)[^()]*)*\)', '', body)
    return [x.strip() for x in body.split(',') if x.strip()]

def ints(src):
    return dict(re.findall(r'(?:static\s+final\s+int|static\s+int|final\s+static\s+int)\s+(\w+)\s*=\s*([^;]+);', src))

def new_enum(rel):
    b = set(enum_consts(rd(os.path.join(BASE, rel))))
    return [x for x in enum_consts(rd(os.path.join(MOD, rel))) if x not in b]

def new_ints(rel, upto=None):
    m, b = rd(os.path.join(MOD, rel)), rd(os.path.join(BASE, rel))
    if upto:
        m = m[:m.find(upto)] if upto in m else m
        b = b[:b.find(upto)] if upto in b else b
    bi = ints(b)
    return [k for k in ints(m) if k not in bi]

def assets_groups():
    """{group: [names]} for String constants the mod added to Assets, by nested class."""
    def groups(src):
        out = {}
        for m in re.finditer(r'public static class (\w+)\s*\{(.*?)\n\t\}', src, re.S):
            out[m.group(1)] = dict(re.findall(r'static\s+final\s+String\s+(\w+)\s*=\s*"([^"]*)"', m.group(2)))
        return out
    m, b = groups(rd(os.path.join(MOD, 'Assets.java'))), groups(rd(os.path.join(BASE, 'Assets.java')))
    return {g: {k: v for k, v in m[g].items() if k not in b.get(g, {})} for g in m}

_names = None
def names():
    global _names
    if _names: return _names
    _names = {
        'talent': new_enum('actors/hero/Talent.java'),
        'heroclass': new_enum('actors/hero/HeroClass.java'),
        'subclass': new_enum('actors/hero/HeroSubClass.java'),
        'items': [k for k in new_ints('sprites/ItemSpriteSheet.java', 'class Icons') if k not in ('TX_WIDTH', 'TX_HEIGHT')],
        'buffs': new_ints('ui/BuffIndicator.java'),
        'heroicon': new_ints('ui/HeroIcon.java'),
        'assets': assets_groups(),
    }
    return _names

# ---- the rewrite ---------------------------------------------------------------------------------
def classes(rels):
    """simple name -> (package, new simple name) for every copied class"""
    out = {}
    for rel in rels:
        d, f = os.path.split(rel)
        n = f[:-5]
        pkg = PKG + ('.' + d.replace('/', '.') if d else '')
        out.setdefault(n, []).append(pkg)
    return out

def rewrite(src, rel, cmap):
    d, f = os.path.split(rel)
    mypkg = PKG + ('.' + d.replace('/', '.') if d else '')
    #which copied classes this file can see by simple name
    visible = set()
    #a dotted name, split where a copied class sits (gun packages are named in capitals, AR, GL)
    def split(dotted):
        parts = dotted.split('.')
        for i in range(1, len(parts)):
            pkg, n = '.'.join(parts[:i]), parts[i]
            if n in cmap and pkg in cmap[n]:
                return '.'.join(parts[:i] + [PREFIX + n] + parts[i + 1:]), n, i == len(parts) - 1
        return None
    imported = set()
    for m in re.finditer(r'^import\s+(com\.shatteredpixel\.shatteredpixeldungeon\.[\w.]+)\s*;', src, re.M):
        imported.add(m.group(1).split('.')[-1])
        s = split(m.group(1))
        if s and s[2]: visible.add(s[1])
    for n, pkgs in cmap.items():
        if mypkg in pkgs and n not in imported: visible.add(n)
    #imports of copied classes, and of their nested classes, and fully qualified uses in code
    def fq(m):
        s = split(m.group(0))
        return s[0] if s else m.group(0)
    src = re.sub(r'\bcom\.shatteredpixel\.shatteredpixeldungeon(?:\.\w+)+', fq, src)
    #simple names, not after a dot (a field or nested class of something else), not inside import lines
    if visible:
        pat = re.compile(r'(?<![\w.])(' + '|'.join(sorted(map(re.escape, visible), key=len, reverse=True)) + r')\b')
        lines = src.split('\n')
        for i, l in enumerate(lines):
            if l.startswith('import ') or l.startswith('package '): continue
            lines[i] = pat.sub(lambda m: PREFIX + m.group(1), l)
        src = '\n'.join(lines)
    N = names()
    def enum_rule(owner, lst):
        nonlocal src
        if not lst: return
        alt = '|'.join(sorted(lst, key=len, reverse=True))
        src = re.sub(r'\b' + owner + r'\.(' + alt + r')\b', owner + '.RBA_' + r'\1', src)
        return alt
    a1 = enum_rule('Talent', N['talent'])
    a2 = enum_rule('HeroClass', N['heroclass'])
    a3 = enum_rule('HeroSubClass', N['subclass'])
    #bare enum names in switch cases
    allenum = sorted(set(N['talent'] + N['heroclass'] + N['subclass']), key=len, reverse=True)
    src = re.sub(r'\bcase\s+(' + '|'.join(allenum) + r')\s*:', r'case RBA_\1:', src)
    enum_rule('ItemSpriteSheet', N['items'])
    enum_rule('BuffIndicator', N['buffs'])
    enum_rule('HeroIcon', N['heroicon'])
    for g, kv in N['assets'].items():
        if kv:
            src = re.sub(r'\bAssets\.' + g + r'\.(' + '|'.join(sorted(kv, key=len, reverse=True)) + r')\b', 'Assets.' + g + r'.RBA_\1', src)
    for a, b in RULES:
        src = re.sub(a, b, src)
    return add_imports(src)

TALENT_NESTED = ['AmbushShotTracker', 'AutoReloadBuff', 'BlessOfPlentyTracker', 'CharmTracker', 'ConfusionTracker',
    'DestructionInstinct', 'FirstAidCooldown', 'HardTackDropped', 'HikariyoTracker', 'InstantFocusingTracker',
    'InstantThrowCooldown', 'InstantThrowTracker', 'IntimidateBonusDamageBuff', 'PerfectPrecisionTracker',
    'PitifulGirlCooldown', 'PushingTracker', 'RiposteTracker', 'SafetyTriggerCooldown', 'ShadowHideTracker',
    'TacticalSuppressTracker', 'TakingAimTracker']

def add_imports(src):
    for n, fq in HELPERS.items():
        if re.search(r'\b' + n + r'\.', src) and ('import ' + fq + ';') not in src and not re.search(r'^package ' + re.escape(fq.rsplit('.', 1)[0]) + ';', src, re.M):
            src = re.sub(r'^(package [\w.]+;\s*\n)', r'\1\nimport ' + fq + ';\n', src, count=1, flags=re.M)
    return src

#the mod's own renames, filled in while fixing compile errors
RULES = [
    (r'\bnew TargetedCell\(', 'TargetedCell.colored('),
    #the students' speech lines, in a helper here so Hero itself stays as it was
    (r'(Dungeon\.hero|\bhero|\(\(Hero\)\s*\w+\))\.yell([WINPH])\(', r'RBAHero.yell\2(\1, '),
    #nested classes the mod added to Talent live in RBATalents here
    (r'\bactors\.hero\.Talent\.(' + '|'.join(TALENT_NESTED) + r')\b', r'actors.hero.RBATalents.\1'),
    (r'(?<![\w.])Talent\.(' + '|'.join(TALENT_NESTED) + r')\b', r'RBATalents.\1'),
    (r'\bDungeon\.hero\.resetTalent\(\)', 'RBAHero.resetTalent(Dungeon.hero)'),
    #members the mod added to Shattered's classes, in RBAShims and RBASprites here
    (r'\bCharSprite\.BLUE\b', 'RBASprites.BLUE'),
    (r'\bDungeon\.level\.(canBreakWall|breakWall)\(', r'RBAShims.\1('),
    (r'\bActor\.enemiesInHeroFOV\(', 'RBAShims.enemiesInHeroFOV('),
    (r'\bBallistica\.WALL_PENETRATION\b', 'RBAShims.WALL_PENETRATION'),
    (r'\bDungeon\.mobsToQuest\b', 'RBAShims.mobsToQuest'),
    (r'\(\(Shopkeeper\)\s*(\w+)\)\.run\(', r'RBAShims.shopkeeperRun((Shopkeeper)\1, '),
    (r'\(\(HeroSprite\)\s*([\w.]+)\)\.(replaceAvantGardeSprite|updateSprite)\(\)', r'RBASprites.\2((HeroSprite)\1)'),
    (r'\b(\w+)\.sprite\.rgb\(', r'RBASprites.rgb(\1.sprite, '),
    (r'\b(\w+)\.sprite\.clearRgb\(\)', r'RBASprites.clearRgb(\1.sprite)'),
    (r'\bnew HeroIcon\((miracle|content)\)', r'HeroIcon.rba(\1.icon())'),
    #the mod's order is attacker, defender, weapon. Only a call that starts with a character is reordered, so the
    #rule can run twice
    (r'\bElastic\.pushEnemy(?:WithoutPit)?\(((?:Dungeon\.hero|hero|curUser|attacker|this|target|owner)\s*),\s*([^,()]+(?:\([^()]*\))?),\s*([^,()]+(?:\([^()]*\))?),', r'Elastic.pushEnemy(\3, \1, \2,'),
    (r'\bPotionOfCleansing\.cleanseDebuff\(', 'RBAShims.cleanseDebuff('),
    (r'\bStoneOfClairvoyance\.mapping\(', 'RBAShims.clairvoyance('),
    (r'\bViscosity\.applyViscosity\(', 'RBAShims.applyViscosity('),
    (r'\bTalent\.hasFoodTalent\(', 'RBATalents.hasFoodTalent('),
    (r'\bactors\.mobs\.Sheep\b', 'actors.mobs.npcs.Sheep'),
    (r'\bIcons\.ACTION_SAKI\.get\(\)', 'RBASprites.icon(16, 144, 9, 9)'),
    (r'\bIcons\.ACTION_MIYU\.get\(\)', 'RBASprites.icon(25, 144, 9, 9)'),
    (r'\bIcons\.ACTION_MOE\.get\(\)', 'RBASprites.icon(34, 144, 9, 9)'),
]
#helpers this game gives the copies, imported wherever a copy names them
HELPERS = {'RBAHero': PKG + '.actors.hero.RBAHero', 'RBATalents': PKG + '.actors.hero.RBATalents',
    'RBAShims': PKG + '.RBAShims', 'RBASprites': PKG + '.sprites.RBASprites'}

def main():
    if not os.path.exists(BASE):
        sys.exit('Shattered 3.0.0 not found, set SPD300 to its core java package folder')
    rels = plan()
    if sys.argv[1] == '--plan':
        for r in rels: print(r)
        print('total', len(rels)); return
    if sys.argv[1] == '--names':
        print(json.dumps(names())); return
    cmap = classes(rels)
    if sys.argv[1] == '--fix':
        #runs the renames over files of this game written in the mod's names (hooks pasted from the mod), in place
        for f in sys.argv[2:]:
            p = os.path.abspath(f)
            rel = os.path.relpath(p, OURS).replace(os.sep, '/')
            s = io.open(p, encoding='utf-8', newline='').read()
            t = rewrite(s, rel, cmap)
            if t != s:
                io.open(p, 'w', encoding='utf-8', newline='').write(t); print('fixed', rel)
        return
    dup = {n: p for n, p in cmap.items() if len(p) > 1}
    if dup: print('same simple name in two packages', dup)
    force = sys.argv[1] == '--refix'
    n = 0
    for rel in rels:
        dst = os.path.join(OURS, newname(rel).replace('/', os.sep))
        if os.path.exists(dst) and not force: continue
        src = rd(os.path.join(MOD, rel))
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        io.open(dst, 'w', encoding='utf-8', newline='').write(rewrite(src, rel, cmap))
        n += 1
    print('copied', n)

if __name__ == '__main__':
    main()
