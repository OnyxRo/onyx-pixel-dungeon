"""Copier for DERanged PD (2026-10-22). DERanged is Re-ARranged 3.39.0 plus a small set of classes, listed below.
Copies each one that is not here yet, with the renames this game needs (generic names get a mod prefix).

  python -I scripts/deranged-copy.py            copies the list
  python -I scripts/deranged-copy.py --refix    runs the rules again over the copied files
"""
import io, os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
P = os.path.join('core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
OURS = os.path.join(ROOT, P)
MOD = os.path.join(os.path.expanduser('~'), 'mod-sources', 'deranged', P)

#mod path -> our path, when the class is renamed
RENAMED = {
    'actors/buffs/Warp.java': 'actors/buffs/WarpDebuff.java',
    'actors/buffs/Scam.java': 'actors/buffs/ScamDebuff.java',
    'actors/buffs/NoDeath.java': 'actors/buffs/KromerNoDeath.java',
    'actors/hero/abilities/ratking/Wrath.java': 'actors/hero/abilities/ratking/KingsWrath.java',
}
LIST = [
    'actors/buffs/AmnesiaBuff.java', 'actors/buffs/DamageOverTimeEffect.java', 'actors/buffs/HPDebuff.java',
    'actors/buffs/NoDeath.java', 'actors/buffs/PowerfulDegrade.java', 'actors/buffs/Scam.java', 'actors/buffs/Warp.java',
    'actors/buffs/WarpedEnemy.java', 'actors/buffs/WarriorParry.java',
    'actors/hero/abilities/TrinityStorage.java', 'actors/hero/abilities/ratking/OmniAbility.java',
    'actors/hero/abilities/ratking/Wrath.java',
    'actors/mobs/DarkSlime.java', 'actors/mobs/Ech.java', 'actors/mobs/npcs/MysteryMerchant.java',
    'effects/particles/ExoParticle.java', 'effects/particles/GodfireParticle.java', 'effects/particles/KromerParticle.java',
    'effects/particles/RunicParticle.java', 'effects/particles/VineParticle.java',
    'items/bags/CheesyCheest.java', 'items/potions/elixirs/DoNotDieElixir.java', 'items/potions/elixirs/KromerPotion.java',
    'items/quest/Kromer.java', 'items/quest/RedCrystal.java', 'items/spells/KromerScroll.java',
    'items/spells/Pipisfusion.java', 'items/spells/ScammingSpell.java',
    'levels/rooms/special/MysteryMerchantRoom.java',
    'sprites/EchSprite.java', 'sprites/InWorldWeaponSprite.java', 'sprites/MysteryMerchantSprite.java',
    'sprites/RatKingHeroSprite.java', 'utils/SafeCast.java', 'utils/WarpPile.java',
] + ['items/weapon/melee/rka/' + n + '.java' for n in (
    'AluminumSword', 'BloomingPick', 'ConstructWand', 'DreadSlicer', 'ElementalDirk', 'ExoKnife', 'LuminousCutlass',
    'MoltenStrife', 'NuclearHatchet', 'RoyalBrand', 'RunicBladeMkII', 'SharksTooth', 'Starsmasher', 'TheReaper')]

RULES = [
    (r'\bWarp\b', 'WarpDebuff'),
    (r'\bScam\b', 'ScamDebuff'),
    (r'\bNoDeath\b', 'KromerNoDeath'),
    (r'\bratking\.Wrath\b', 'ratking.KingsWrath'),
    (r'\bBuffIndicator\.(SCAM|WARP)\b', r'BuffIndicator.DR_\1'),
    (r'\bHeroIcon\.(KING|WRATH|BLOCKING)\b', r'HeroIcon.DR_\1'),
    (r'\bnew TargetedCell\(', 'TargetedCell.colored('),
    (r'\bitems\.weapon\.bow\.SpiritBow\b', 'items.weapon.SpiritBow'),
    (r'\bSpade\b', 'BattleShovel'),
    (r'\bTalent\.(FAST_RELOAD|TOUGH_MEAL|ADRENALINE|BULLSEYE|ENDURANCE|COUNTER_ATTACK|RESURRECTION)\b', r'Talent.RA_\1'),
]

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p

def fix(src, rel):
    for a, b in RULES:
        src = re.sub(a, b, src)
    if rel.endswith('ratking/Wrath.java'):
        src = re.sub(r'\bWrath\b', 'KingsWrath', src)
    return src

def main():
    refix = len(sys.argv) > 1 and sys.argv[1] == '--refix'
    n = 0
    for rel in LIST:
        dst = os.path.join(OURS, RENAMED.get(rel, rel))
        if refix:
            if not os.path.exists(dst): continue
            s = io.open(dst, encoding='utf-8', newline='').read()
        else:
            if os.path.exists(dst): continue
            s = io.open(LP(os.path.join(MOD, rel)), encoding='utf-8', newline='').read()
        t = fix(s, rel)
        if refix and t == s: continue
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        io.open(dst, 'w', encoding='utf-8', newline='').write(t)
        n += 1
    print('refixed' if refix else 'copied', n)

main()
