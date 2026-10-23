"""Wires Blue Archive PD's hooks (BAHero, BATalents) into Shattered's classes, one line each, marked //bluearchive.
Safe to run again, a hook already in place is skipped. Run from the repo root, python -I scripts/bluearchive-hooks.py"""
import io, os, sys
P = os.path.join('core', 'src', 'main', 'java', 'com', 'shatteredpixel', 'shatteredpixeldungeon')
BA = 'com.shatteredpixel.shatteredpixeldungeon.actors.hero.'
#file, anchor (must appear once), text, 'before' or 'after' the anchor
HOOKS = [
    ('actors/hero/Hero.java', '\t\t\tHT = 20 + 5*(lvl-1) + HTBoost;\n\t\t}', '\t\tHT += BAHero.bonusHT(this); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\taccuracy = RearrangedHero.accuracy(this, wep, accuracy); //rearranged',
        '\t\tif (BAHero.doubleTap(this, target, wep)) return INFINITE_ACCURACY; //bluearchive\n\t\taccuracy = BAHero.accuracy(this, target, wep, accuracy); //bluearchive', 'before'),
    ('actors/hero/Hero.java', '\t\tspeed = AscensionChallenge.modifyHeroSpeed(speed);', '\t\tspeed *= BAHero.speedMulti(this); //bluearchive', 'before'),
    ('actors/hero/Hero.java', '\t\tif (w instanceof Flail)                 return false;',
        '\t\tif (w instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG.SGBullet) return false; //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\tpublic float attackDelay() {', '\t\tif (BAHero.freeAttack(this)) return 0; //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\tpublic int defenseProc( Char enemy, int damage ) {', '\t\tdamage = BAHero.defenseProc(this, enemy, damage); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\t\t\t\tSample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1/3f + flashIntensity * 2f);',
        '\t\t\t\t\tBAHero.healthYell(this, true); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\t\t\t\tSample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, 1/3f + flashIntensity * 4f);',
        '\t\t\t\t\tBAHero.healthYell(this, false); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\tint pos = Dungeon.hero.pos;', '\t\tBAHero.dieYell(Dungeon.hero); //bluearchive', 'before'),
    ('actors/hero/Hero.java', '\t\tif (hasTalent(Talent.WIDE_SEARCH)) distance++;',
        '\t\tdistance += BAHero.searchBonus(this); //bluearchive\n\t\tif (pointsInTalent(Talent.BA_WIDER_SEARCH) == 1) circular = true; //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\tevasion *= Talent.stanceFactor(this, 1);', '\t\tevasion *= BAHero.evasionMulti(this); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\tdr += RearrangedHero.bonusDR(this); //rearranged', '\t\tdr += BAHero.drBonus(this); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\treturn actResult;', '\t\tBAHero.onAct(this); //bluearchive', 'before'),
    ('actors/hero/Hero.java', '\t\tdamage = RearrangedHero.attackProc(this, enemy, wep, damage); //rearranged',
        '\t\tdamage = BAHero.attackProc(this, enemy, damage, wep); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\tdmg = RearrangedHero.damageTaken(this, dmg, src); //rearranged', '\t\tdmg = BAHero.magicShield(this, dmg, src); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\t\tspend( delay / speed() );', '\t\t\tif (BAHero.onMove(this)) spend( -delay / speed() ); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\t\t\tsprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );', '\t\t\t\tBAHero.levelUpYell(this); //bluearchive', 'after'),
    ('actors/hero/Hero.java', '\t\treturn RearrangedHero.stealth(this, super.stealth());', '\t\treturn RearrangedHero.stealth(this, super.stealth()) + BAHero.stealthBonus(this); //bluearchive', 'replace'),
    ('actors/hero/Hero.java', '\t\t\t\t\t\t//don\'t want to let the player search though hidden doors in tutorial',
        '\t\t\t\t\t\tif (!foresight && !intentional) chance *= BAHero.searchChance(this); //bluearchive', 'before'),
    #Char
    ('actors/Char.java', '\t\t\t\tdr = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedChar.heroArmorPierce(h, enemy, dr);',
        '\t\t\t\tdr = ' + BA + 'BAHero.enemyDR(this, enemy, dr); //bluearchive', 'after'),
    ('actors/Char.java', '\t\t\tfor (ChampionEnemy buff : buffs(ChampionEnemy.class)){\n\t\t\t\tdmg *= buff.meleeDamageFactor();',
        '\t\t\tdmg *= ' + BA + 'BAHero.damageMulti(this, enemy); //bluearchive', 'before'),
    ('actors/Char.java', '\t\t\tif (enemy instanceof Hero) com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedRework.onHeroDodged((Hero) enemy); //rearranged',
        '\t\t\t' + BA + 'BAHero.onDodge(enemy); //bluearchive', 'after'),
    ('actors/Char.java', '\t\t\tsprite.showStatusWithIcon(CharSprite.NEGATIVE, Integer.toString(dmg + shielded), icon);',
        '\t\t\tsprite.showStatusWithIcon(CharSprite.NEGATIVE, ' + BA + 'BAHero.damageText(Integer.toString(dmg + shielded)), icon); //bluearchive', 'replace'),
    ('actors/Char.java', '\tpublic void destroy() {\n\t\tHP = 0;\n\t\tActor.remove( this );',
        '\t\t' + BA + 'BAHero.onCharDie(this); //bluearchive', 'after'),
    ('actors/Char.java', '\t\ttimeScale = TowerChar.timeScale(this, timeScale); //tower', '\t\ttimeScale *= ' + BA + 'BAHero.timeScale(this); //bluearchive', 'before'),
    #Mob
    ('actors/mobs/Mob.java', '\t\t\t\tDungeon.hero.earnExp(exp, getClass());', '\t\t\t\texp = ' + BA + 'BAHero.expMulti(this, exp); //bluearchive', 'before'),
    ('actors/mobs/Mob.java', '\t\tsuper.die( cause );', '\t\t' + BA + 'BAHero.onMobDie(this, cause); //bluearchive', 'before'),
    ('actors/mobs/Mob.java', '\tpublic void rollToDropLoot(){', '\t\t' + BA + 'BAHero.questLoot(this); //bluearchive', 'after'),
    ('actors/mobs/Mob.java', "\t\t\t\t\t\t//silent steps rogue talent, which also applies to rogue's shadow clone",
        '\t\t\t\t\t\t//bluearchive, Izuna\'s quiet move works like silent steps\n\t\t\t\t\t\tif (ch instanceof Hero && Dungeon.hero.hasTalent(Talent.BA_QUIET_MOVE) && distance(ch) >= 4 - Dungeon.hero.pointsInTalent(Talent.BA_QUIET_MOVE)) bestChance = Float.POSITIVE_INFINITY;', 'before'),
    #Level, grass, regeneration, food
    ('levels/Level.java', '\t\t\tweb.clear(pos);\n\t\t}\n\t}', '\t\t' + BA + 'BAHero.onLevelEnter(); //bluearchive, Yuzu\'s game start, the mod put it where terrain is destroyed', 'before_end'),
    ('levels/Level.java', '\t\tChampionEnemy.rollForChampion(m);', '\t\tif (m.buff(ChampionEnemy.class) == null) com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAQuestEnemy.rollForQuest(m); //bluearchive', 'after'),
    ('levels/Level.java', '\t\t\t\tif ((c instanceof Hero && ((Hero) c).subClass == HeroSubClass.WARDEN)',
        '\t\t\t\tif ((c instanceof Hero && ((Hero) c).subClass == HeroSubClass.WARDEN) || ' + BA + 'BAHero.seesThroughGrass(c) //bluearchive', 'replace'),
    ('levels/Level.java', '\t\t\t\tviewDist *= 1f + 0.25f*((Hero) c).pointsInTalent(Talent.FARSIGHT);', '\t\t\t\tviewDist *= ' + BA + 'BAHero.viewDistMulti((Hero) c); //bluearchive', 'after'),
    ('levels/Level.java', '\t\t\t\tmindVisRange = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedChar.mindVisionRange((Hero) c, mindVisRange); //rearranged',
        '\t\t\t\tmindVisRange = ' + BA + 'BAHero.mindVisionRange((Hero) c, mindVisRange); //bluearchive', 'after'),
    ('levels/features/HighGrass.java', '\t\t\t\tcom.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedChar.onGrassTrampled((Hero) ch); //rearranged',
        '\t\t\t\t' + BA + 'BAHero.onGrassTrampled((Hero) ch); //bluearchive', 'after'),
    ('actors/buffs/Regeneration.java', '\t\t\t\tpartialRegen += 1f / delay;', '\t\t\t\tdelay *= ' + BA + 'BAHero.regenDelay(target); //bluearchive', 'before'),
    ('items/food/Food.java', '\t\tif (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)', '\t\tif (' + BA + 'BATalents.fastMeal(Dungeon.hero)) return TIME_TO_EAT - 2; //bluearchive', 'before'),
    ('items/food/Berry.java', '\t\tif (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)', '\t\tif (' + BA + 'BATalents.fastMeal(Dungeon.hero)) return 0; //bluearchive', 'before'),
    ('items/armor/Armor.java', 'Dungeon.hero.heroClass != HeroClass.WARRIOR && Dungeon.hero.hasTalent(Talent.RUNIC_TRANSFERENCE)',
        'Dungeon.hero.heroClass != HeroClass.WARRIOR && Dungeon.hero.heroClass != HeroClass.BA_ARIS && Dungeon.hero.hasTalent(Talent.RUNIC_TRANSFERENCE)', 'replace'),
    ('actors/mobs/npcs/Shopkeeper.java', '\t\treturn item.value() * 5 * (Dungeon.depth / 5 + 1);',
        '\t\treturn item.value() * (5 - (Dungeon.hero == null ? 0 : Dungeon.hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.BA_GOLD_CARD))) * (Dungeon.depth / 5 + 1); //bluearchive, Nonomi\'s gold card', 'replace'),
    #weapons
    ('items/weapon/missiles/MissileWeapon.java', '\t\tint req = STRReq(tier, lvl) - 1; //1 less str than normal for their tier',
        '\t\treq -= ' + BA + 'BAHero.throwMasterStr(); //bluearchive', 'after'),
    ('items/weapon/missiles/MissileWeapon.java', '\t\tif (projecting > 0\n\t\t\t\t&& (Dungeon.level.passable[dst]',
        '\t\tif (' + BA + 'BAHero.noaThrow(user, dst, projecting > 0)) return dst; //bluearchive', 'before'),
    ('items/weapon/missiles/MissileWeapon.java', '\t\t\t//metamorphed seer shot logic', '\t\t\tif (enemy == null) ' + BA + 'BAHero.missedThrow(curUser, cell, this); //bluearchive', 'before'),
    ('items/weapon/missiles/MissileWeapon.java', '\tpublic int proc(Char attacker, Char defender, int damage) {', '\t\t' + BA + 'BAHero.missileProc(attacker, defender); //bluearchive', 'after'),
    ('items/weapon/missiles/MissileWeapon.java', '\t\t\tusages *= 1.25f + (0.25f*Dungeon.hero.pointsInTalent(Talent.DURABLE_PROJECTILES));\n\t\t}',
        '\t\tusages *= ' + BA + 'BAHero.durability(); //bluearchive', 'after'),
    ('items/weapon/melee/MeleeWeapon.java', 'isEquipped(Dungeon.hero) && Dungeon.hero.hasTalent(Talent.TWIN_UPGRADES)){',
        'isEquipped(Dungeon.hero) && (Dungeon.hero.hasTalent(Talent.TWIN_UPGRADES) || Dungeon.hero.hasTalent(Talent.BA_DOUBLE_BARREL_1))){ //bluearchive, Noa\'s double barrel', 'replace'),
    ('items/weapon/melee/MeleeWeapon.java', 'if ((tier + (3 - Dungeon.hero.pointsInTalent(Talent.TWIN_UPGRADES))) <= ((MeleeWeapon) other).tier',
        'if ((tier + (3 - Math.max(Dungeon.hero.pointsInTalent(Talent.TWIN_UPGRADES), Dungeon.hero.pointsInTalent(Talent.BA_DOUBLE_BARREL_1)))) <= ((MeleeWeapon) other).tier', 'replace'),
    ('items/weapon/melee/MeleeWeapon.java', '\t\t\t}\n\t\t}\n\t\treturn super.buffedLvl();\n\t}',
        '\t\t\t}\n\t\t}\n\t\treturn super.buffedLvl() + ' + BA + 'BAHero.hgBonus(this); //bluearchive\n\t}', 'replace'),
    ('items/KindOfWeapon.java', '\t\tif (hero.subClass == HeroSubClass.CHAMPION && action.equals(AC_EQUIP)){',
        '\t\tif ((hero.subClass == HeroSubClass.CHAMPION || (hero.subClass == HeroSubClass.BA_NOA_EX_DOUBLE_BARREL && this instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG)) && action.equals(AC_EQUIP)){ //bluearchive', 'replace'),
    ('sprites/MissileSprite.java', '\t\t} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun.Bullet\n',
        '\t\t} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.BAGun.Bullet //bluearchive\n'
        '\t\t\t\t|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet.SpiritKunai\n'
        '\t\t\t\t|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet.SpiritShuriken) {\n'
        '\t\t\tspeed *= ' + BA + 'BAHero.missileSpeed(item);', 'before'),
    ('sprites/MissileSprite.java', '\t\tANGULAR_SPEEDS.put(SpiritBow.SpiritArrow.class,       0);',
        '\t\tANGULAR_SPEEDS.put(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.BAGun.Bullet.class, 0); //bluearchive\n'
        '\t\tANGULAR_SPEEDS.put(com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet.SpiritKunai.class, 0); //bluearchive\n'
        '\t\tANGULAR_SPEEDS.put(com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet.SpiritShuriken.class, 2160); //bluearchive', 'after'),
    #students other than Aris find guns where others find melee weapons
    ('items/Generator.java', '\t\tfloorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);\n\n\t\tMeleeWeapon w;',
        '\t\tif (' + BA + 'BAHeroes.usesGuns()) return ' + BA + 'BAHero.randomGun(1 + Random.chances(floorSetTierProbs[floorSet])); //bluearchive', 'after'),
    ('actors/mobs/npcs/Ghost.java', '\t\t\t\tweapon = (Weapon) Generator.random(Generator.wepTiers[wepTier - 1]);',
        '\t\t\t\tif (' + BA + 'BAHeroes.usesGuns()) weapon = ' + BA + 'BAHero.randomGun(wepTier); //bluearchive', 'after'),
    ('actors/mobs/Slime.java', '\t\tMeleeWeapon w = (MeleeWeapon)Generator.randomUsingDefaults(Generator.Category.WEP_T2);',
        '\t\tif (' + BA + 'BAHeroes.usesGuns()) w = ' + BA + 'BAHero.randomGun(2); //bluearchive', 'after'),
    ('items/scrolls/exotic/ExoticScroll.java', '\t\texoToReg.put(ScrollOfMetamorphosis.class, ScrollOfTransmutation.class);',
        '\t\texoToReg.put(BAScrollOfTalentReset.class, ScrollOfTransmutation.class); //bluearchive, its exotic transmutation, reached from the catalog only', 'after'),
    ('items/scrolls/exotic/ScrollOfEnchantment.java', '&& (item.isUpgradable() || item instanceof SpiritBow);',
        '&& (item.isUpgradable() || item instanceof SpiritBow || item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet); //bluearchive', 'replace'),
    #Talent
    ('actors/hero/Talent.java', '\tpublic static void onTalentUpgraded( Hero hero, Talent talent ){', '\t\tBATalents.onTalentUpgraded(hero, talent); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){', '\t\tBATalents.onFoodEaten(hero); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static void onScrollUsed( Hero hero, int pos, float factor, Class<?extends Item> cls ){', '\t\tBATalents.onScrollUsed(hero); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static void onItemEquipped( Hero hero, Item item ){', '\t\tBATalents.onItemEquipped(hero, item); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static void onItemCollected( Hero hero, Item item ){', '\t\tBATalents.onItemCollected(hero, item); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static int onAttackProc( Hero hero, Char enemy, int dmg ){', '\t\tdmg = BATalents.onAttackProc(hero, enemy, dmg); //bluearchive', 'after'),
    ('actors/hero/Talent.java', '\tpublic static float itemIDSpeedFactor( Hero hero, Item item ){', '\t\tif (BAHeroes.isBA(hero)) return BATalents.itemIDSpeedFactor(hero, item); //bluearchive', 'after'),
]

def run():
    n = 0
    for f, anchor, text, where in HOOKS:
        p = os.path.join(P, f)
        s = io.open(p, encoding='utf-8', newline='').read()
        nl = '\r\n' if '\r\n' in s else '\n'
        a = anchor.replace('\n', nl); t = text.replace('\n', nl)
        if t in s: continue
        c = s.count(a)
        if c != 1:
            print('ANCHOR %d times in %s: %s' % (c, f, anchor.strip()[:70])); continue
        if where == 'replace': s = s.replace(a, t)
        elif where == 'before_end': s = s.replace(a, a[:-2] + t + nl + '\t}')  #anchor ends with a tab and a brace
        elif where == 'after': s = s.replace(a, a + nl + t)
        else: s = s.replace(a, t + nl + a)
        io.open(p, 'w', encoding='utf-8', newline='').write(s)
        n += 1
    print('hooks added', n)

if __name__ == '__main__':
    run()
