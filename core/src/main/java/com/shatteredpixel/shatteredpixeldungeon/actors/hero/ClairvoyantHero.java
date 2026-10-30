/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Motivation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

//Clairvoyant PD's Apprentice and Channeller. Every hook in Shattered's classes is one line
//calling into here, marked //clairvoyant.
public class ClairvoyantHero {

	public static boolean isApprentice( Hero hero ){
		return hero != null && hero.heroClass == HeroClass.APPRENTICE;
	}

	public static boolean isChanneller( Char ch ){
		return ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.CHANNELLER;
	}

	//from Item.identify when the hero identifies something
	public static void onItemIdentified( Hero hero, Item item ){
		if (hero == null) return;
		if (hero.hasTalent(Talent.PHILOSOPHILE)){
			int p = hero.pointsInTalent(Talent.PHILOSOPHILE);
			Buff.prolong(hero, Motivation.class, 5f + p * p * 5);
		}
		//a cursed find frightens a young apprentice
		if (isApprentice(hero) && item.cursed && hero.lvl <= 15){
			Buff.affect(hero, Terror.class, 15);
			Buff.affect(hero, Vertigo.class, 8);
			Buff.affect(hero, Paralysis.class, 1);
			Sample.INSTANCE.play(Assets.Sounds.CURSED);
			GLog.n(Messages.get(ClairvoyantHero.class, "overwhelmed"));
		}
		//the Channeller enchants half of what she identifies
		if (isChanneller(hero) && Random.Int(2) == 0){
			if (item instanceof Weapon) ((Weapon) item).enchant();
			else if (item instanceof Armor) ((Armor) item).inscribe();
		}
	}

	public static void onFoodEaten( Hero hero, float foodVal ){
		if (hero.hasTalent(Talent.SNACK_SAVING) && foodVal >= 300){
			switch (Random.Int(4 + hero.pointsInTalent(Talent.SNACK_SAVING))){
				case 3: new ChargrilledMeat().collect(); break;
				case 4: new StewedMeat().collect(); break;
				case 5: new SmallRation().collect(); break;
				default: break;
			}
		}
		if (hero.hasTalent(Talent.UNLEASHING_MEAL)){
			int p = hero.pointsInTalent(Talent.UNLEASHING_MEAL);
			if (Random.Int(2) == 0) Buff.affect(hero, FireImbue.class).set(3 * p);
			else FlavourBuff.prolong(hero, FrostImbue.class, 4 * p);
		}
	}

	public static boolean fastMeal( Hero hero ){
		return hero != null && hero.hasTalent(Talent.UNLEASHING_MEAL);
	}

	public static float idSpeed( Hero hero, Item item ){
		if (item instanceof Ring || item instanceof Wand){
			return 1f + hero.pointsInTalent(Talent.APPRENTICES_INTUITION) * 0.5f;
		}
		return 1f;
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(Talent.APPRENTICES_INTUITION) == 2 && item instanceof Scroll){
			((Scroll) item).setKnown();
		}
	}

	//from Heap.explode, Matter Dissipation leaves smoke where items were blown up
	public static void onItemDestroyed( int pos ){
		Hero hero = Dungeon.hero;
		if (hero != null && hero.hasTalent(Talent.MATTER_DISSIPATION)){
			GameScene.add(Blob.seed(pos, 200 * hero.pointsInTalent(Talent.MATTER_DISSIPATION), SmokeScreen.class));
		}
	}

	//from Char.attack, Force Conversion turns part of a Channeller's blow into damage armor cannot stop
	public static int armorPiercing( Char attacker, int rolled ){
		if (!isChanneller(attacker) || !(attacker instanceof Hero)) return 0;
		Hero h = (Hero) attacker;
		if (h.belongings.weapon() == null) return 0;
		return rolled / (5 - h.pointsInTalent(Talent.FORCE_CONVERSION));
	}

	public static float accuracy( Char attacker ){
		return attacker.buff(Motivation.class) != null ? 1.2f : 1f;
	}

	public static float zapTime( Hero hero, float time ){
		if (hero.buff(Motivation.class) != null) time *= 1f - hero.pointsInTalent(Talent.PHILOSOPHILE) * 0.2f;
		return time;
	}

	//from Wand, a Channeller's full wand can overload and blow up in her hand
	public static void afterZap( Hero hero, Wand wand ){
		if (isChanneller(hero) && wand.curCharges > 4 && Random.Int(5) == 0){
			new Bomb().explode(hero.pos);
			CellEmitter.get(hero.pos).burst(SmokeParticle.FACTORY, 6);
		}
	}

	//from Weapon.Enchantment.genericProcChanceMultiplier
	public static float procBonus( Char attacker ){
		if (!isChanneller(attacker)) return 0;
		return 0.5f + 0.6f * ((Hero) attacker).pointsInTalent(Talent.EXTERNAL_CATALYST);
	}

	//Optimized Transmutation, for Wild Energy, Feather Fall and Aqua Blast
	public static int transmutation(){
		return Dungeon.hero == null ? 0 : Dungeon.hero.pointsInTalent(Talent.OPTIMIZED_TRANSMUTATION);
	}
}
