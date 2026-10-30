/*
 * Onyx's Pixel Dungeon
 *
 * Shattered Trap Dungeon by CoralCOasa (CoralCOasa/shattered-trap-dungeon), a mod that makes the game
 * a bit crazier with dozens of new traps and monsters. Its run rules are a game mode here, so normal
 * runs are unchanged. With the mode on, every floor uses the mod's trap list for its region and its
 * monsters join the rotation as in its Bestiary. Its monsters, items and traps are also in the
 * sandbox lists and the catalog without the mode.
 * The hero's trap skill, which the mod kept on Hero, lives here and is saved with the run.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Trapskill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.RatBeacon1;
import com.shatteredpixel.shatteredpixeldungeon.items.RatBeacon2;
import com.shatteredpixel.shatteredpixeldungeon.items.RatDust;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.*;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ExtraShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapDungeonTraps;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TrapDungeon {

	public static final String MOD  = "Shattered Trap Dungeon";
	public static final String MODE = "trap_dungeon";

	public static void register(){
		GameModes.register(MODE, MOD, 1, null);
	}

	public static boolean on(){
		return GameModes.on(MODE);
	}

	/*****************************************************************
	 * Trap skill, raised by the Elixir of Trap Skill and the trapper's leather armor.
	 * Each point gives a Reclaim Trap spell one more use
	 *****************************************************************/

	private static int trapSkill = 0;
	private static int currentTrapSkill = 0;
	private static int currentTrapSkill2 = 0;

	public static int getTrapSkill(){ return trapSkill; }
	public static int getCurrentTrapSkill(){ return currentTrapSkill; }
	public static int getCurrentTrapSkill2(){ return currentTrapSkill2; }
	public static void increaseTrapSkill(){ trapSkill++; }
	public static void decreaseTrapSkill(){ trapSkill--; }
	public static void setCurrentTrapSkill( int v ){ currentTrapSkill = v; }
	public static void setCurrentTrapSkill2( int v ){ currentTrapSkill2 = v; }

	public static void reset(){
		trapSkill = currentTrapSkill = currentTrapSkill2 = 0;
	}

	private static final String TRAPSKILL  = "td_trapskill";
	private static final String CURRENT    = "td_current_trapskill";
	private static final String CURRENT2   = "td_current_trapskill2";

	public static void store( Bundle bundle ){
		bundle.put(TRAPSKILL, trapSkill);
		bundle.put(CURRENT, currentTrapSkill);
		bundle.put(CURRENT2, currentTrapSkill2);
	}

	public static void restore( Bundle bundle ){
		trapSkill = bundle.contains(TRAPSKILL) ? bundle.getInt(TRAPSKILL) : 0;
		currentTrapSkill = bundle.contains(CURRENT) ? bundle.getInt(CURRENT) : 0;
		currentTrapSkill2 = bundle.contains(CURRENT2) ? bundle.getInt(CURRENT2) : 0;
	}

	//ReclaimTrap, storing a trap fills the free uses
	public static void onTrapStored(){
		currentTrapSkill = trapSkill;
	}

	//ReclaimTrap, a free use keeps the spell and the stored trap
	public static boolean freeReclaim( Hero hero, Class<? extends Trap> trap ){
		if (currentTrapSkill <= 0) return false;
		currentTrapSkill--;
		Buff.affect(hero, ReclaimTrap.ReclaimedTrap.class).trap = trap;
		if (currentTrapSkill == 0) Buff.detach(hero, Trapskill.class);
		return true;
	}

	/*****************************************************************
	 * The game mode, monsters and traps
	 *****************************************************************/

	//the monsters the mod's Bestiary added to each floor's rotation
	private static Class<?>[] extraMobs( int depth ){
		switch (depth){
			case 1:  return new Class<?>[]{DancingSkeleton.class, DancingSkeleton.class, DancingSkeleton.class};
			case 2:  return new Class<?>[]{FilthJet.class, DancingSkeleton.class};
			case 3:  return new Class<?>[]{Orc.class, FilthJet.class, FilthJet.class, DancingSkeleton.class};
			case 4:  return new Class<?>[]{Orc.class, Orc.class, FilthJet.class};
			case 6:  return new Class<?>[]{ToughGhost.class};
			case 7:  return new Class<?>[]{Assasin.class, ToughGhost.class};
			case 8:  return new Class<?>[]{Assasin.class, Assasin.class, KeyChain.class};
			case 9:  return new Class<?>[]{Assasin.class, KeyChain.class, KeyChain.class};
			case 11: return new Class<?>[]{BombSpider.class, BombSpider.class, DartSpitter.class};
			case 12: return new Class<?>[]{DartSpitter.class, DartSpitter.class, BombSpider.class, BombSpider.class};
			case 13: return new Class<?>[]{DartSpitter.class, DartSpitter.class, BombSpider.class, ShockerBlock.class};
			case 14: return new Class<?>[]{DartSpitter.class, ShockerBlock.class, ShockerBlock.class, ShockerBlock.class};
			case 16: return new Class<?>[]{FrostMeteor.class, FrostMeteor.class, BombSpider2.class};
			case 17: case 18: return new Class<?>[]{ChaosElemental.class, FrostMeteor.class, BombSpider2.class};
			case 19: return new Class<?>[]{ChaosElemental.class, FrostMeteor.class};
			case 21: case 22: return new Class<?>[]{TDDemonRat.class, TDDemonRat.class, Prismancer.class, Hole.class};
			case 23: case 24: return new Class<?>[]{TDDemonRat.class, TDDemonRat.class, Prismancer.class, Prismancer.class, Hole.class};
			default: return new Class<?>[0];
		}
	}

	//Level.createMob
	@SuppressWarnings("unchecked")
	public static void adjust( ArrayList<Class<? extends Mob>> rotation, Level level ){
		if (!on() || rotation == null || Dungeon.branch != 0) return;
		for (Class<?> c : extraMobs(Dungeon.depth)) rotation.add((Class<? extends Mob>) c);
		Random.shuffle(rotation);
	}

	//RegularPainter.setTraps, the mod's trap list for the floor's region
	public static Class<?>[] trapClasses( Class<?>[] normal ){
		return on() && Dungeon.branch == 0 ? TrapDungeonTraps.CLASSES : normal;
	}

	public static float[] trapChances( float[] normal ){
		if (!on() || Dungeon.branch != 0) return normal;
		int region = (int)GameMath.gate(0, (Dungeon.depth - 1) / 5, 4);
		return TrapDungeonTraps.CHANCES[region];
	}

	//Generator.randomArmor, half of all armor comes as one of the mod's two variants for its tier
	public static Armor armorVariant( Armor a ){
		if (!on()) return a;
		int roll = Random.Int(4);
		Armor v = null;
		if (roll == 0){
			if (a instanceof LeatherArmor) v = new LeatherTrapperArmor();
			else if (a instanceof MailArmor) v = new MailWarArmor();
			else if (a instanceof ScaleArmor) v = new ScaleCrystalArmor();
			else if (a instanceof PlateArmor) v = new PlateBloodArmor();
		} else if (roll == 1){
			if (a instanceof LeatherArmor) v = new LeatherWardenArmor();
			else if (a instanceof MailArmor) v = new MailArcaneArmor();
			else if (a instanceof ScaleArmor) v = new ScaleSolarArmor();
			else if (a instanceof PlateArmor) v = new PlateTimeArmor();
		}
		if (v == null) return a;
		v.random();
		return v;
	}

	//Ghost's reward, the same chance of a variant
	public static Armor ghostArmor( Armor a ){
		return armorVariant(a);
	}

	//the special rooms list
	public static void addSpecialRooms( ArrayList<Class<? extends com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room>> rooms ){
		if (on() && !rooms.contains(ExtraShopRoom.class)) rooms.add(ExtraShopRoom.class);
	}

	/*****************************************************************
	 * Boss changes
	 *****************************************************************/

	//DM300.damage, the goblin protocol at half health
	public static void dm300Damaged( Mob dm ){
		if (!on() || dm.HP > dm.HT / 2 || dm.buff(GoblinProtocol.class) != null) return;
		Buff.affect(dm, GoblinProtocol.class);
		dm.yell(Messages.get(TrapDungeon.class, "goblin_protocol"));
		Buff.affect(dm, Adrenaline.class, 5f);
		for (int i = 0; i < 3; i++){
			TDGoblin g = new TDGoblin();
			g.state = g.WANDERING;
			int tries = 0;
			do {
				g.pos = Random.Int(Dungeon.level.length());
				tries++;
			} while (tries < 500 && (Dungeon.level.solid[g.pos] || !Dungeon.level.passable[g.pos]
					|| Dungeon.level.distance(g.pos, Dungeon.hero.pos) < 8 || Actor.findChar(g.pos) != null));
			if (tries < 500) GameScene.add(g);
		}
	}

	public static class GoblinProtocol extends Buff {
		{
			revivePersists = true;
		}
		@Override
		public boolean act() {
			spend(TICK * 1000);
			return true;
		}
	}

	//RatKing.interact, a Rat Beacon starts the ritual
	public static boolean ratKingRitual( Mob king ){
		RatBeacon1 beacon = Dungeon.hero.belongings.getItem(RatBeacon1.class);
		if (beacon == null) return false;
		beacon.detach(Dungeon.hero.belongings.backpack);
		new RatBeacon2().collect();
		king.yell(Messages.get(TrapDungeon.class, "ritual"));
		if (king.sprite != null) king.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
		king.die(null);
		return true;
	}

	//RatKing woken up, a clown joins in
	public static void ratKingWoken(){
		if (!on()) return;
		Clown clown = new Clown();
		clown.pos = Dungeon.level.randomRespawnCell(clown);
		if (clown.pos != -1) GameScene.add(clown);
	}

	//HallsBossLevel.seal, the ritual ends with the Spectral God and its giant rats
	public static void yogArena(){
		if (Dungeon.hero.belongings.getItem(RatBeacon2.class) == null) return;
		SpectralGod god = new SpectralGod();
		int tries = 0;
		do {
			god.pos = Random.Int(Dungeon.level.length());
			tries++;
		} while (tries < 1000 && (!Dungeon.level.passable[god.pos] || Dungeon.level.heroFOV[god.pos] || Actor.findChar(god.pos) != null));
		if (tries >= 1000) return;
		GameScene.add(god);
		for (int i = 0; i < 3; i++){
			GiantRat rat = new GiantRat();
			for (int n : PathFinder.NEIGHBOURS8){
				int c = god.pos + n;
				if (Dungeon.level.passable[c] && Actor.findChar(c) == null){
					rat.pos = c;
					GameScene.add(rat);
					break;
				}
			}
		}
	}

	//Hero.actTransition, with the ritual started the hero needs rat dust to change floors, and uses it up
	public static boolean blockTransition( Hero hero ){
		if (hero.belongings.getItem(RatBeacon2.class) == null) return false;
		RatDust dust = hero.belongings.getItem(RatDust.class);
		if (dust == null){
			GLog.w(Messages.get(TrapDungeon.class, "no_dust"));
			return true;
		}
		dust.detach(hero.belongings.backpack);
		return false;
	}

	/*****************************************************************
	 * The mod's armors, which the mod handled in Hero
	 *****************************************************************/

	public static float accuracy( Hero hero ){
		return hero.belongings.armor() instanceof ScaleCrystalArmor ? 1.25f : 1f;
	}

	public static int damageRoll( Hero hero, int dmg ){
		return hero.belongings.armor() instanceof MailWarArmor ? Math.round(dmg * 1.15f) : dmg;
	}

	//a free attack one time in five
	public static boolean freeAttack( Hero hero ){
		return hero.belongings.armor() instanceof PlateTimeArmor && Random.Int(5) == 0;
	}

	//the better of two rolls of the damage taken, as a percentage. The mod used whole number
	//division here, which cut almost every hit to zero
	public static int damageTaken( Hero hero, int dmg, Object src ){
		if (!(src instanceof Char) || !(hero.belongings.armor() instanceof ScaleSolarArmor)) return dmg;
		int roll = Math.max(Random.Int(101), Random.Int(101));
		return dmg * roll / 100;
	}

	/*****************************************************************
	 * Helpers the mod added to Shattered's classes
	 *****************************************************************/

	//ScrollOfTeleportation.silencedAppear, a teleport with no sound
	public static void silencedAppear( Char ch, int pos ){
		if (ch.sprite != null) ch.sprite.interruptMotion();
		ch.move( pos );
		if (ch.sprite == null) return;
		if (ch.pos == pos) ch.sprite.place( pos );
		if (ch.invisible == 0 && ch.sprite.parent != null) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}
		ch.sprite.emitter().start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
	}

	//Wraith.spawnAroundCorrupted, wraiths on the four sides that fight for the hero
	public static void spawnAroundCorrupted( int pos ){
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
				Wraith w = Wraith.spawnAt( cell );
				if (w != null) w.alignment = Char.Alignment.ALLY;
			}
		}
	}
}
