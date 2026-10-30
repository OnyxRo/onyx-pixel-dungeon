/*
 * Onyx's Pixel Dungeon
 *
 * The Goo's story from Shattered Pixel Lore by ReallyBasicGames (ReallyBasicGames/Shattered-Pixel-Lore).
 * In the mod the Goo was a hero class with a five floor run. Here it is a game mode, so any hero can
 * play it and normal runs are unchanged. The hero becomes the Goo, starts at level 2 with nothing,
 * cannot pick up items, never gets hungry, heals only while not arid, and attacks with its moisture
 * (see GooMoisture). Floors hold no items, monsters drop no loot, there are no special rooms, no
 * ghost quest and no rat king. Killing the Goo boss on floor 5 opens the Goo's ending.
 * Each hook in Shattered's classes is a one line call into this class.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GooMoisture;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GooSlimeArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GooWinScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LoreGoo {

	public static final String MOD  = "Shattered Pixel Lore";
	public static final String GOO  = "lore_goo";

	public static final String SPRITE       = "sprites/lore/goo_hero.png";
	public static final String SKELETON_KEY = "sprites/lore/skeleton_key.png";

	public static void register(){
		GameModes.register(GOO, MOD, 1, null);
	}

	public static boolean on(){
		return GameModes.on(GOO);
	}

	public static boolean isGoo( Char ch ){
		return ch instanceof Hero && ch == Dungeon.hero && on();
	}

	//Dungeon.init, after the hero's class has handed out its items
	public static void initHero( Hero hero ){
		if (!on()) return;
		hero.belongings.clear();
		Dungeon.quickslot.reset();
		//every hero in Shattered Pixel Lore started at level 2
		hero.lvl = 2;
		hero.updateHT( true );
		hero.HP = hero.HT;
		//starts moist, the mod started it dry and let the first pool of water fill it
		Buff.affect(hero, GooMoisture.class).set( (int)GooMoisture.MOIST );
	}

	//Talent.initClassTalents, the Goo has only its own four talents
	public static void initTalents( ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (!on() || talents.isEmpty()) return;
		for (LinkedHashMap<Talent, Integer> tier : talents) tier.clear();
		for (Talent t : new Talent[]{Talent.CAUSTIC_OOZE, Talent.HARD_GOO, Talent.SHARP_GOOP, Talent.SLIPPERY_SLIME}){
			talents.get(0).put(t, 0);
		}
	}

	public static GooMoisture moisture( Hero hero ){
		return Buff.affect(hero, GooMoisture.class);
	}

	//Hero.damageRoll, the Goo hits with its moisture instead of a weapon
	public static int damageRoll( Hero hero ){
		GooMoisture moisture = hero.buff(GooMoisture.class);
		int dmg = 0;
		if (moisture != null){
			if (hero.hasTalent(Talent.SHARP_GOOP)){
				dmg = (int)((hero.pointsInTalent(Talent.SHARP_GOOP) == 1 ? 0.2 : 0.3) * moisture.level());
			} else {
				dmg = (int)(0.1 * moisture.level());
			}
			if (moisture.isArid()) dmg /= 2;
		}
		return dmg + Random.NormalIntRange( 0, 2 );
	}

	//Hero.act, water soaks into the Goo
	public static void onHeroTurn( Hero hero ){
		if (!on() || Dungeon.level == null || hero.pos < 0 || hero.pos >= Dungeon.level.length()) return;
		if (Dungeon.level.water[hero.pos]){
			if (hero.hasTalent(Talent.HARD_GOO)){
				Buff.affect(hero, GooSlimeArmor.class).set( 5, hero.pointsInTalent(Talent.HARD_GOO) == 1 ? 2 : 3 );
			}
			moisture(hero).set( 5 );
		}
	}

	//Regeneration, the Goo heals while not arid instead of while not starving
	public static boolean canRegen( Hero hero ){
		if (!on()) return !hero.isStarving();
		return !moisture(hero).isArid();
	}

	//Hunger does nothing to the Goo
	public static boolean noHunger( Char target ){
		return isGoo(target);
	}

	//Char.drRoll
	public static int slimeArmor( Char ch ){
		GooSlimeArmor slime = ch.buff(GooSlimeArmor.class);
		return slime == null ? 0 : Random.NormalIntRange( slime.level()/2, slime.level() );
	}

	//Hero.attackProc, Caustic Ooze
	public static void attackProc( Hero hero, Char enemy ){
		if (!on() || !hero.hasTalent(Talent.CAUSTIC_OOZE)) return;
		if (Random.Int( 10 ) <= hero.pointsInTalent(Talent.CAUSTIC_OOZE)){
			Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
			if (enemy.sprite != null) enemy.sprite.burst( 0x000000, 5 );
		}
	}

	//Char.speed, Slippery Slime
	public static float speed( Char ch ){
		if (!isGoo(ch)) return 1f;
		Hero hero = (Hero)ch;
		if (!hero.hasTalent(Talent.SLIPPERY_SLIME)) return 1f;
		return hero.pointsInTalent(Talent.SLIPPERY_SLIME) == 1 ? 1.1f : 1.25f;
	}

	//Hero.actPickUp, the Goo cannot carry anything
	public static boolean blockPickUp( Hero hero ){
		if (!on()) return false;
		GLog.w( Messages.get(LoreGoo.class, "no_pickup") );
		return true;
	}

	//no items on the floors, no loot, no special rooms, no ghost and no rat king
	public static boolean noItems(){
		return on();
	}

	//the hero sprite
	public static String spritesheet( String normal ){
		return on() ? SPRITE : normal;
	}

	//Goo.die, the Goo's ending when the boss of its sewer falls
	public static void onGooSlain(){
		if (!on() || Dungeon.hero == null || !Dungeon.hero.isAlive()) return;
		Game.switchScene( GooWinScene.class );
	}
}
