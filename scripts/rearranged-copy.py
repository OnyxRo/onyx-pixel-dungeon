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
    (r'\bAssets\.Sprites\.(TANK|ADVENTURER)\b', r'Assets.Sprites.RA_\1'),
    #the Wizard and the other reworked subclasses of Shattered's heroes are not here, their talents count as 0
    (r'(?:Dungeon\.)?\bhero\.pointsInTalent\(\s*Talent\.(SPELL_ENHANCE|MAGIC_EMPOWER|SECOND_EFFECT|BRIG_BOOST)\s*\)', '0'),
    (r'\bnew TargetedCell\(', 'TargetedCell.colored('),
    #this game's Spade is Pixel Spacebase's, the mod's own is BattleShovel here
    (r'\bSpade\b', 'BattleShovel'),
    #specks the mod drew on its own speck sheet, close ones here
    (r'\bSpeck\.HUNGER\b', 'Speck.HEALING'),
    (r'\bSpeck\.THUNDER_STORM\b', 'Speck.STORM'),
    #keys here are only numbered by depth, the mod also numbered them by branch
    (r'\bnew (CrystalKey|GoldenKey|IronKey)\(\s*[^,()]+,\s*[^,()]+\)', r'new \1(Dungeon.depth)'),
    #small check box icons the mod drew, the normal ones here
    (r'\bIcons\.MINI_(CHECKED|UNCHECKED)\b', r'Icons.\1'),
    #its five challenges are not here
    (r'Dungeon\.isChallenged\(\s*Challenges\.(SUPERMAN|PYRO|CURSED_DUNGEON|FATIGUE|MUTATION)\s*\)', 'false'),
    (r'\bhero\.subClass\s*==\s*HeroSubClass\.(WIZARD|VETERAN|CHASER|FIGHTER|FENCER|ENCHANTER)\b', 'false'),
    (r'\bTalent\.(FAST_RELOAD|TOUGH_MEAL|ADRENALINE|BULLSEYE)\b', r'Talent.RA_\1'),
]
#never copied, see PORTING.md
SKIP = {
    'GooglePlayGames.java', 'SeedFinder.java', 'scenes/SeedAnalysisScene.java', 'scenes/SeedFindScene.java',
    'items/weapon/bow/SpiritBow.java', 'levels/LabsLevel.java', 'items/weapon/melee/Spade.java',
    #classes this game already had from other mods under the same path, never touched
    'items/weapon/enchantments/Eldritch.java', 'items/weapon/enchantments/Stunning.java',
    'items/weapon/enchantments/Venomous.java', 'items/weapon/enchantments/Vorpal.java',
}
#the mod's rework of Shattered's own six heroes (new talent trees, third subclasses, the Enchanter's spells) and its
#new challenges. Left out, they would replace Shattered's talent trees rather than add to the game.
REWORK = [
    'actors/hero/spells/' + n + '.java' for n in ('ArcaneArmorSpell', 'DivineBlast', 'DivineRay', 'Enchant', 'HolyBombSpell',
        'HolyMantle', 'InduceAggro', 'PowerOfLife', 'Resurrection', 'SpellBurst', 'Stun', 'ThunderImbueSpell', 'TimeAmp', 'WeakeningHex')
] + [
    'actors/buffs/' + n + '.java' for n in ('Tackle', 'SoulCollect', 'EnhancedRingsCombo', 'MagicalCombo',
        'MagicalEmpower', 'MagicalCircle', 'Fatigue', 'GhostSpawner', 'chargearea/RechargeArea', 'chargearea/WandRechargeArea',
        'chargearea/ArtifactRechargeArea', 'chargearea/BarrierRechargeArea', 'chargearea/Judgement', 'chargearea/MutationBuff')
] + ['windows/WndMagicalCombo.java']
SKIP.update(REWORK)

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
    if sys.argv[1] == '--rm-skipped':   #deletes copies of skipped files left from an earlier run
        for rel in sorted(SKIP):
            if os.path.exists(os.path.join(OURS, rel)) and not os.path.exists(os.path.join(BASE, rel)):
                os.remove(os.path.join(OURS, rel)); print('removed', rel)
        return
    if sys.argv[1] == '--refix':   #runs the rules again over files already copied, after a rule was added
        n = 0
        for rel in every_new():
            dst = os.path.join(OURS, rel)
            if not os.path.exists(dst) or rel in SKIP: continue
            s = io.open(dst, encoding='utf-8', newline='').read()
            t = fix(s)
            if t != s:
                io.open(dst, 'w', encoding='utf-8', newline='').write(t); n += 1
        print('refixed', n); return
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
