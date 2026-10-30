package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArcaneSingularity;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TomeOfSpecialty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AresChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CommRelay;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.WoodenEffigy;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.WraithAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

//Deistic Pixel Dungeon (BrightBotTeam). Its four races are game modes in one group, so only one can be on.
//The specialty from the Tome of Specialty is a saved buff on the hero. Every hook does nothing unless a race is on.
public class DeisticPD {

	public static final String MOD = "Deistic Pixel Dungeon";
	public static final String GROUP = "deistic_race";

	public static final String HUMAN  = "deistic_human";
	public static final String DWARF  = "deistic_dwarf";
	public static final String WRAITH = "deistic_wraith";
	public static final String GNOLL  = "deistic_gnoll";

	public static final String[] RACES = {HUMAN, DWARF, WRAITH, GNOLL};

	public static void register(){
		for (String id : RACES) GameModes.register(id, MOD, 1, GROUP);
	}

	public static String race(){
		for (String id : RACES) if (GameModes.on(id)) return id;
		return null;
	}

	public static boolean is( String race ){
		return GameModes.on(race);
	}

	//the race's own artifact, the one only it can use in full
	public static Class<? extends Item> raceArtifact(){
		if (is(DWARF)) return AresChains.class;
		if (is(HUMAN)) return CommRelay.class;
		if (is(WRAITH)) return WraithAmulet.class;
		if (is(GNOLL)) return WoodenEffigy.class;
		return null;
	}

	public static void onStart( Hero hero ){
		if (is(HUMAN)){
			hero.HTBoost -= 2;
			hero.updateHT(true);
			hero.HP = hero.HT;
		}
		//the mod gave a scroll of regrowth, which this game lacks, sungrass seeds are the nearest gift from the gnoll homeland
		if (is(GNOLL)){
			new Sungrass.Seed().quantity(2).identify().collect();
		}
	}

	//the floor 5 boss leaves the race's artifact, Tengu leaves the Tome of Specialty
	public static void onBossSlain(){
		if (race() == null || Dungeon.hero == null || Dungeon.level == null) return;
		if (Dungeon.depth == 5 && raceArtifact() != null){
			Item a = com.watabou.utils.Reflection.newInstance(raceArtifact());
			if (a != null) { com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(a, Dungeon.hero.pos); if (dropped.sprite != null) dropped.sprite.drop(); }
		} else if (Dungeon.depth == 10 && specialty() == null){
			{ com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(new TomeOfSpecialty(), Dungeon.hero.pos); if (dropped.sprite != null) dropped.sprite.drop(); }
		}
	}

	//dwarves turn extra strength into extra damage, brutes hit harder with enemies around them
	public static int damageRoll( Hero hero, int dmg ){
		if (is(DWARF)){
			int excess = hero.STR() - (hero.belongings.weapon() instanceof MeleeWeapon ? ((MeleeWeapon) hero.belongings.weapon()).STRReq() : 10);
			if (excess > 0) dmg += Random.NormalIntRange(0, excess);
		}
		if (has(Way.MONK) && hero.belongings.weapon() == null){
			dmg += Random.NormalIntRange(0, hero.lvl / 2);
		}
		if (has(Way.BRUTE)){
			int around = 0;
			for (int n : PathFinder.NEIGHBOURS8){
				Char ch = Actor.findChar(hero.pos + n);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY) around++;
			}
			dmg = Math.round(dmg * (1f + 0.15f * around));
		}
		return dmg;
	}

	public static int defenseSkill( int skill ){
		if (is(WRAITH)) return Math.round(skill * 1.15f);
		return skill;
	}

	public static int damageTaken( Hero hero, int dmg, Object src ){
		if (is(DWARF) && src instanceof Char && (Char.hasProp((Char) src, Char.Property.DEMONIC) || Char.hasProp((Char) src, Char.Property.UNDEAD))){
			dmg = Math.round(dmg * 1.1f);
		}
		if (is(GNOLL) && src instanceof Hunger) dmg *= 2;
		return dmg;
	}

	//dwarves shrug off a little fire, frost, ooze and poison, wraiths a little fire and frost, gnolls resist charm
	public static float resist( Char ch, Class effect ){
		if (!(ch instanceof Hero) || race() == null) return 1f;
		boolean elemental = effect == Burning.class || effect == Frost.class || effect == Chill.class;
		if (is(DWARF) && (elemental || effect == Ooze.class || effect == Poison.class)) return 0.75f;
		if (is(WRAITH) && elemental) return 0.75f;
		if (is(GNOLL) && effect == Charm.class) return 0.5f;
		return 1f;
	}

	//the light of a remove curse scroll stings a wraith
	public static void onRemoveCurse( Hero hero ){
		if (is(WRAITH)){
			hero.damage(Math.max(1, hero.HT / 10), ScrollOfRemoveCurse.class);
			hero.sprite.emitter().burst(com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle.UP, 5);
		}
	}

	public static void onHeroHit( Hero hero, Char enemy ){
		if (has(Way.BLUE) && enemy.isAlive() && enemy.alignment == Char.Alignment.ENEMY){
			Buff.append(hero, TalismanOfForesight.CharAwareness.class, 20f).charID = enemy.id();
		}
	}

	public static float lootChance( float chance ){
		return has(Way.RED) ? Math.min(1f, chance * 1.5f) : chance;
	}

	//shamans heal a little from each patch of tall grass they walk through
	public static void onTrample( Char ch ){
		if (ch instanceof Hero && has(Way.SHAMAN) && ch.HP < ch.HT){
			ch.HP = Math.min(ch.HT, ch.HP + 1 + ((Hero) ch).lvl / 6);
			ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
		}
	}

	//the warlock's wand effects last twice as long, set around Wand.onZap
	public static boolean warlockZap = false;

	public static float durationScale( Char target ){
		return warlockZap && target != Dungeon.hero && has(Way.WARLOCK) ? 2f : 1f;
	}

	//the mercenary reads an enemy's miss and strikes back at twice the speed
	public static void onHeroDodged( Hero hero ){
		if (has(Way.MERCENARY)) Buff.affect(hero, Counter.class, 2f);
	}

	public static float attackDelay( Hero hero, float delay ){
		if (has(Way.MERCENARY) && hero.buff(Counter.class) != null){
			hero.buff(Counter.class).detach();
			return delay / 2f;
		}
		return delay;
	}

	//demolitionists get a stronger blast from every bomb as they level up
	public static int bombBonus(){
		if (!has(Way.DEMOLITIONIST) || Dungeon.hero == null) return 0;
		return Dungeon.hero.lvl / 5;
	}

	public static class Counter extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff {
	}

	//the eight subraces, two per race
	public enum Way {
		WARLOCK, MONK, DEMOLITIONIST, MERCENARY, RED, BLUE, SHAMAN, BRUTE;

		public String title(){
			return Messages.get(DeisticPD.class, "way_" + name().toLowerCase());
		}

		public String desc(){
			return Messages.get(DeisticPD.class, "way_" + name().toLowerCase() + "_desc");
		}
	}

	public static Way[] waysFor( String race ){
		if (DWARF.equals(race)) return new Way[]{Way.DEMOLITIONIST, Way.MERCENARY};
		if (GNOLL.equals(race)) return new Way[]{Way.SHAMAN, Way.BRUTE};
		if (HUMAN.equals(race)) return new Way[]{Way.WARLOCK, Way.MONK};
		if (WRAITH.equals(race)) return new Way[]{Way.RED, Way.BLUE};
		return new Way[0];
	}

	public static Way specialty(){
		if (Dungeon.hero == null) return null;
		Specialty s = Dungeon.hero.buff(Specialty.class);
		return s == null ? null : s.way;
	}

	public static boolean has( Way way ){
		return specialty() == way;
	}

	public static class Specialty extends Buff {
		{
			revivePersists = true;
		}

		public Way way;

		@Override
		public boolean act() {
			spend(TICK * 100);
			return true;
		}

		private static final String WAY = "way";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle(bundle);
			bundle.put(WAY, way);
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle(bundle);
			way = bundle.getEnum(WAY, Way.class);
		}
	}

	//Deistic made burning scrolls go off with their own effect instead of turning to ash
	public static void burnScroll( Heap heap, Scroll scroll ){
		if (Dungeon.level == null) return;
		int pos = heap.pos;
		boolean seen = Dungeon.level.heroFOV[pos];
		if (seen) GLog.i(Messages.get(DeisticPD.class, "detonates", scroll.name()));

		if (scroll instanceof ScrollOfIdentify){
			for (Item i : heap.items) if (!i.isIdentified()) { i.identify(); break; }
		} else if (scroll instanceof ScrollOfUpgrade){
			for (Item i : heap.items) if (i != scroll && i.isUpgradable()) { i.upgrade(); break; }
		} else if (scroll instanceof ScrollOfRemoveCurse){
			for (Item i : heap.items) i.cursed = false;
		} else if (scroll instanceof ScrollOfMagicMapping){
			if (Dungeon.hero != null) Buff.affect(Dungeon.hero, Awareness.class, Awareness.DURATION);
		} else if (scroll instanceof ScrollOfPsionicBlast){
			if (Actor.findChar(pos) == null){
				ArcaneSingularity s = new ArcaneSingularity();
				s.pos = pos;
				GameScene.add(s);
			}
		} else {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (Dungeon.level.distance(pos, mob.pos) > 4) continue;
				if (scroll instanceof ScrollOfLullaby) Buff.affect(mob, Drowsy.class, Drowsy.DURATION);
				else if (scroll instanceof ScrollOfRage) Buff.prolong(mob, Amok.class, 5f);
				else if (scroll instanceof ScrollOfTerror) Buff.affect(mob, Terror.class, Terror.DURATION).object = pos;
				else if (scroll instanceof ScrollOfRetribution) mob.damage(Random.NormalIntRange(1, 3 + Dungeon.scalingDepth()), scroll);
			}
		}
		if (Dungeon.hero != null) Dungeon.observe();
	}
}
