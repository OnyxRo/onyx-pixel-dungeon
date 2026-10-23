"""Bulk copier for Blue Archive PD (2026-10-22, key bluearchive). The mod is on Shattered 2.3.2.
Every class the mod adds is copied under a BA prefix, because Re Blue Archive PD (prefix RBA) shares most class names,
and Re-ARranged PD already gave this game Gun, AR, SG and the rest.

  python -I scripts/bluearchive-copy.py            copies every file in FILES that is not here yet
  python -I scripts/bluearchive-copy.py --refix    runs the rules again over the copies (after adding a rule)
  python -I scripts/bluearchive-copy.py --rules F  runs the rules over one more file (for hand copied code)
"""
import io, os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
P = os.path.join('core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
OURS = os.path.join(ROOT, P)
MOD = os.path.join(os.path.expanduser('~'), 'mod-sources', 'bluearchive', P)

GUNS = ['AR', 'GL', 'HG', 'MG', 'SG', 'SMG', 'SR']
#mod path -> simple class name. The copy keeps the mod's folder and gets BA in front of the file name
FILES = ['items/weapon/melee/gun/Gun']
for g in GUNS:
    FILES.append('items/weapon/melee/gun/%s/%s' % (g, g))
    FILES += ['items/weapon/melee/gun/%s/%s_tier%d' % (g, g, t) for t in range(1, 6)]
FILES += ['items/weapon/melee/SuperNova', 'items/weapon/ThrowingSet', 'items/AmmoBox', 'items/EXSkillDisc',
          'items/scrolls/exotic/ScrollOfTalentReset']
FILES += ['items/active/' + n for n in ('Bicycle', 'BlastGrenade', 'Claymore', 'FlashGrenade', 'Grenade', 'HPGrenade',
          'HandGrenade', 'IronHorus', 'Laser', 'LevelAnalyzer', 'RainbowGrenade', 'RegrowGrenade', 'SmokeGrenade', 'TrashBin')]
STUDENTS = ['Aris', 'Nonomi', 'Miyako', 'Hoshino', 'Shiroko', 'Noa', 'Miyu', 'Yuzu', 'Izuna']
FILES += ['items/armor/%sArmor' % s for s in STUDENTS]
FILES += ['actors/buffs/' + n for n in ('BlastMissile', 'BulletMomentumTracker', 'CriticalAttack', 'DoubleBarrelMark',
          'DroneStrike', 'DroneStrikeCooldown', 'ElementalBullet', 'FlashGrenade', 'QuestEnemy', 'ShockBulletCooldown',
          'SnipingBullet', 'StickyGrenade', 'StunDrone', 'StunDroneCooldown', 'Teleport', 'InfiniteBullet')]
FILES += ['actors/buffs/elementals/' + n for n in ('APBullet', 'ElectricBullet', 'ElementalBulletBuff', 'ExplosiveBullet',
          'IceBullet', 'IncendiaryBullet')]
FILES += ['windows/WndElementalBullet', 'windows/WndProfessionalRide']

NAMES = sorted({f.split('/')[-1] for f in FILES}, key=len, reverse=True)

#talents the mod added (not Shattered's), these get BA_ in front. Filled from the mod's Talent enum
def mod_new_talents():
    def names(path):
        t = io.open(path, encoding='utf-8').read()
        body = t[t.index('public enum Talent'):]
        body = re.sub(r'//[^\n]*', '', body[:body.index(';')])
        return re.findall(r'(?:^|[,\s])([A-Z][A-Z0-9_]+)\s*\(', body)
    base = os.path.join(os.path.expanduser('~'), 'mod-sources', 'shattered-2.5.4', P, 'actors', 'hero', 'Talent.java')
    vanilla = set(names(base))
    #Shattered talents 2.5.4 dropped or renamed since 2.3.2, all used by the mod with their old meaning
    vanilla |= {'ALLY_WARP', 'SECONDARY_CHARGE'}
    return sorted({n for n in names(os.path.join(MOD, 'actors', 'hero', 'Talent.java')) if n not in vanilla}, key=len, reverse=True)
TALENTS = mod_new_talents()
NESTED = ['BulletTimeCooldown', 'DefensiveMatrixTracker', 'DoubleTapTracker', 'EmergencyHealingCooldown', 'EmpoweringMagic',
          'FastHandTracker', 'ForLightTracker', 'NoWayCooldown', 'PerfectHandTracker', 'ScanCooldown',
          'ShootingWeaknessCooldown', 'SuppressingFireCooldown', 'TacticalInvisibilityTracker']
HERO = r'(ARIS|NONOMI|MIYAKO|HOSHINO|SHIROKO|NOA|MIYU|YUZU|IZUNA)'
SUBS = HERO + r'_EX_[A-Z_]+'
#icons from the mod's ItemSpriteSheet, HeroIcon and BuffIndicator, renamed so they never clash
ITEM_ICONS = ['ARMOR_' + s.upper() for s in STUDENTS] + ['SUPER_NOVA', 'SMOKE_GRENADE', 'HAND_GRENADE', 'CLAYMORE',
    'BLAST_GRENADE', 'IRON_HORUS', 'HP_GRENADE', 'BICYCLE', 'LASER', 'FLASH_GRENADE', 'REGROW_GRENADE', 'TRASH_BIN',
    'RAINBOW_GRENADE', 'ANALYZER', 'AMMO_BOX', 'THROWING_SET', 'SINGLE_BULLET', 'TRIPLE_BULLET', 'SNIPER_BULLET',
    'GRENADE_BULLET', 'ROCKET_BULLET', 'NOTHING_BULLET', 'EXPLOSIVE_BULLET', 'SPIRIT_SHURIKEN', 'SPIRIT_KUNAI', 'EX_BD',
    'TELEPORTER'] + ['%s_TIER_%d' % (g, t) for g in GUNS for t in range(1, 6)]
HERO_ICONS = r'(' + SUBS + r'|' + HERO + r'_[123]|STUNDRONE|DRONESTRIKE|ELEMENTAL_BULLET|PROFESSIONAL_RIDE|DOUBLE_BARREL|TELEPORT|BLAST_MISSILE)'

RULES = [
    (r'\b(' + '|'.join(NAMES) + r')\b', r'BA\1'),
    (r'\bTalent\.(' + '|'.join(NESTED) + r')\b', r'BATalents.\1'),
    (r'\bTalent\.(' + '|'.join(TALENTS) + r')\b', r'Talent.BA_\1'),
    (r'\bHeroClass\.' + HERO + r'\b', r'HeroClass.BA_\1'),
    (r'\bHeroSubClass\.(' + SUBS + r')\b', r'HeroSubClass.BA_\1'),
    (r'\bHeroIcon\.' + HERO_ICONS + r'\b', r'HeroIcon.BA_\1'),
    (r'\bBuffIndicator\.(ARMOR_LIGHT|BULLET|SNIPING)\b', r'BuffIndicator.BA_\1'),
    (r'\bItemSpriteSheet\.(' + '|'.join(sorted(ITEM_ICONS, key=len, reverse=True)) + r')\b', r'ItemSpriteSheet.BA_\1'),
    (r'\bAssets\.(Sprites|Splashes)\.' + HERO + r'\b', r'Assets.\1.BA_\2'),
    #the mod's yell helpers on Hero live in BAHero here
    (r'\b((?:Dungeon\.)?hero|Hero\.this|curUser|this)\.yell([IPNWH])\(', r'BAHero.yell\2(\1, '),
    (r'\bDungeon\.mobsToQuest\b', 'BAQuestEnemy.mobsToQuest'),
    (r'\b((?:Dungeon\.)?hero)\.heal\(', r'BAHero.heal(\1, '),
    #voice lines are keyed by the hero class name, which is BA_ARIS here, the strings keep the mod's ARIS
    (r'\bheroClass\.name\(\)(?:\.replace\("BA_", ""\))*', 'heroClass.name().replace("BA_", "")'),
    (r'\bnew TargetedCell\(', 'TargetedCell.colored('),
    #the gun folders keep their names, only the classes get BA
    (r'\bgun\.BA(AR|GL|HG|MG|SG|SMG|SR)(?=[;.])', r'gun.\1'),
    #mod additions to Shattered classes, done here without touching those classes
    (r'\bnew Beam\.ShotRay\(', 'new BAShotRay('),
    (r'\bPathFinder\.NEIGHBOURS(21|49)\b', r'BAHero.neighbours\1()'),
    (r'\bnew CheckedCell\(\s*([^,()]+),\s*([^()]+?)\s*\)', r'BAHero.checkedCell(\1, \2)'),
    (r'\bRainbowParticle\.FACTORY\b', 'RainbowParticle.BURST'),
    (r'\b((?:Dungeon\.)?\w+)\.resetTalent\(\)', r'BAHero.resetTalents(\1)'),
]
IMPORTS = {
    'BAHero': 'com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero',
    'BATalents': 'com.shatteredpixel.shatteredpixeldungeon.actors.hero.BATalents',
    'BAShotRay': 'com.shatteredpixel.shatteredpixeldungeon.effects.BAShotRay',
}

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p

def fix(src):
    for a, b in RULES:
        src = re.sub(a, b, src)
    for cls, imp in IMPORTS.items():
        if re.search(r'\b' + cls + r'[.(]', src) and ('import ' + imp + ';') not in src and not re.search(r'^package com\.shatteredpixel\.shatteredpixeldungeon\.actors\.hero;', src, re.M):
            src = re.sub(r'^(package [^;]+;\s*\n)', r'\1\nimport ' + imp + ';\n', src, count=1, flags=re.M)
    return src

def dest(f):
    d, n = f.rsplit('/', 1)
    return os.path.join(OURS, d, 'BA' + n + '.java')

def main():
    if len(sys.argv) > 1 and sys.argv[1] == '--rules':
        for p in sys.argv[2:]:
            s = io.open(p, encoding='utf-8', newline='').read()
            io.open(p, 'w', encoding='utf-8', newline='').write(fix(s))
        return
    refix = len(sys.argv) > 1 and sys.argv[1] == '--refix'
    n = 0
    for f in FILES:
        dst = dest(f)
        if refix:
            if not os.path.exists(dst): continue
            s = io.open(dst, encoding='utf-8', newline='').read()
        else:
            if os.path.exists(dst): continue
            s = io.open(LP(os.path.join(MOD, f + '.java')), encoding='utf-8', newline='').read()
        t = fix(s)
        if refix and t == s: continue
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        io.open(dst, 'w', encoding='utf-8', newline='').write(t)
        n += 1
    print(('refixed' if refix else 'copied'), n, 'of', len(FILES), '| talents renamed', len(TALENTS))

if __name__ == '__main__':
    main()
