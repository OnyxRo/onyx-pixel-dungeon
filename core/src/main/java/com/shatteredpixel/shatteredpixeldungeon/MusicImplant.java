/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Music Implant SPD
 * Copyright (C) TAGS DUEL (CatAzreal, Jinkeloid), https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FellEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Horror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LightsourceLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RegenPerTurn;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Satiation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.WaxNWick;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.Candle;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.FireTorch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Music Implant SPD (key musicimplant). Its light and horror survival rules, its satiation
//system and its 34 character perks, each as a game mode so a normal run is unchanged.
//Every hook in Shattered's classes is a one line call into this class, marked //musicimplant.
public class MusicImplant {

	public static final String MOD = "Music Implant SPD";

	public static final String HORROR    = "mi_horror";
	public static final String SATIATION = "mi_satiation";

	//the mod's own perks, in the order of its character builder (good ones first)
	public static final String[] PERKS = {
			"cats_eyes", "inconspicuous", "quick_draw", "on_diet", "abstinence", "dexterous",
			"organized", "lucky", "quick_learner", "stout", "biologist", "adrenaline", "brave",
			"confident", "sturdy", "very_strong", "gobbler", "mind_compass",
			"short_sighted", "conspicuous", "slowpoke", "big_stomach", "clumsy", "disorganized",
			"unlucky", "illiterate", "pacifist", "nictophobia", "nyctalopia", "amnesia",
			"lack_of_sense", "unresponsive", "frail", "nibbling"
	};

	//pairs that cannot be picked together, from the mod's conflict lists
	private static final String[][] CONFLICTS = {
			{"cats_eyes", "short_sighted"}, {"cats_eyes", "nyctalopia"}, {"short_sighted", "nyctalopia"},
			{"inconspicuous", "conspicuous"}, {"quick_draw", "slowpoke"},
			{"on_diet", "big_stomach"}, {"abstinence", "big_stomach"},
			{"dexterous", "clumsy"}, {"organized", "disorganized"}, {"lucky", "unlucky"},
			{"stout", "very_strong"}, {"biologist", "illiterate"},
			{"brave", "pacifist"}, {"brave", "nictophobia"}, {"confident", "pacifist"}, {"confident", "nictophobia"},
			{"sturdy", "frail"}, {"gobbler", "nibbling"}, {"mind_compass", "amnesia"}
	};

	//perks that only do something with one of the two systems on, picking one turns the system on
	private static final String[][] NEEDS = {
			{"adrenaline", HORROR}, {"brave", HORROR}, {"confident", HORROR}, {"pacifist", HORROR},
			{"nictophobia", HORROR}, {"abstinence", SATIATION}
	};

	public static String perkId( String perk ){
		return "mi_" + perk;
	}

	public static void register(){
		GameModes.register(HORROR, MOD, 1, null);
		GameModes.register(SATIATION, MOD, 1, null);
		for (String p : PERKS) GameModes.register(perkId(p), MOD, 1, null);
	}

	//called from GameModes.set, keeps the mod's conflict rules
	public static void onSet( String id, int lvl ){
		if (!id.startsWith("mi_")) return;
		String perk = id.substring(3);
		if (lvl > 0){
			for (String[] c : CONFLICTS){
				if (c[0].equals(perk) && GameModes.on(perkId(c[1]))) GameModes.set(perkId(c[1]), 0);
				if (c[1].equals(perk) && GameModes.on(perkId(c[0]))) GameModes.set(perkId(c[0]), 0);
			}
			for (String[] n : NEEDS){
				if (n[0].equals(perk) && !GameModes.on(n[1])) GameModes.active.put(n[1], 1);
			}
		} else {
			for (String[] n : NEEDS){
				if (n[1].equals(id) && GameModes.on(perkId(n[0]))) GameModes.active.remove(perkId(n[0]));
			}
		}
	}

	public static boolean horrorOn(){
		return GameModes.on(HORROR);
	}

	public static boolean satiationOn(){
		return GameModes.on(SATIATION);
	}

	public static boolean perk( String perk ){
		return GameModes.on(perkId(perk));
	}

	/*****************************************************************
	 * Start of a run and of a life
	 *****************************************************************/

	//from GameModes.applyStartKits, every hero starts with a candle and two wax and wicks in the mod
	public static void startKit( Hero hero ){
		if (horrorOn()){
			new Candle().identify().collect();
			new WaxNWick().identify().quantity(2).collect();
		}
	}

	//from Hero.live
	public static void heroLive( Hero hero ){
		if (horrorOn()) Buff.affect(hero, Horror.class);
		if (satiationOn()) Buff.affect(hero, Satiation.class);
		if (perk("dexterous") || perk("quick_draw")) Buff.affect(hero, RegenPerTurn.class);
		quickDraw = perk("quick_draw");
		instantAct = perk("dexterous");
	}

	/*****************************************************************
	 * Light and vision
	 *****************************************************************/

	//from Dungeon.observe, after the floor's own view distance
	public static int viewDistance( Hero hero, int vd ){
		int bonus = LightsourceLight.visionBonus(hero);
		if (bonus > 0) vd = Math.max(8, vd + bonus);
		if (perk("cats_eyes")) vd = Math.max(8, vd + 1);
		if (perk("short_sighted")) vd = Math.max(2, vd - 1);
		if (perk("nyctalopia")){
			vd = (bonus > 0 || hero.buff(Light.class) != null) ? 3 : 1;
		}
		return vd;
	}

	/*****************************************************************
	 * Combat
	 *****************************************************************/

	private static float hpRatio( Hero hero ){
		return hero.HT <= 0 ? 1 : hero.HP / (float)hero.HT;
	}

	//empty offhand in the mod, here no lit light source in the pack
	public static boolean emptyHand( Hero hero ){
		return horrorOn() && LightsourceLight.intensity(hero) == 0;
	}

	public static float accuracyFactor( Hero hero ){
		float f = 1;
		if (horrorOn()) f *= 1 - Horror.accPenalty(hero) / 100f;
		if (perk("nictophobia") && LightsourceLight.intensity(hero) == 0) f *= 0.85f;
		if (perk("confident") && hpRatio(hero) > 0.9f) f *= 1.15f;
		return f;
	}

	public static float evasionFactor( Hero hero ){
		float f = 1;
		if (emptyHand(hero)) f += 0.05f;
		if (horrorOn()) f += Horror.evaBonus(hero) / 100f;
		return f;
	}

	public static float speedFactor( Hero hero ){
		float f = Satiation.speedFactor(hero);
		if (horrorOn()) f += Horror.movBonus(hero) / 100f;
		return f;
	}

	public static float attackDelayFactor( Hero hero ){
		if (!horrorOn()) return 1;
		return 1 - Horror.atkspeedBonus(hero) / 100f;
	}

	public static boolean noSurprise( Hero hero ){
		if (perk("unresponsive")) return true;
		return horrorOn() && Horror.level(hero) > Horror.HORRIFIED;
	}

	//from Hero.attackProc
	public static int attackProc( Hero hero, Char enemy, int dmg ){
		float f = Satiation.damageFactor(hero);
		if (emptyHand(hero)) f += 0.10f;
		if (perk("brave") && hpRatio(hero) < 0.25f) f += 0.15f;
		if (f == 1) return dmg;
		return (int)Math.ceil(dmg * f);
	}

	//from Mob.die, a kill halts horror growth for a while and a strong one lowers it
	public static void onMobKilled( Mob mob ){
		Hero hero = Dungeon.hero;
		if (hero == null || !horrorOn() || perk("pacifist")) return;
		int lvlDiff = mob.maxLvl - (perk("quick_learner") ? hero.lvl - 1 : hero.lvl) - 3;
		if (mob.maxLvl < 0 && mob.EXP > 0) lvlDiff = 3; //bosses and specials without a cap count as strong
		if (lvlDiff > 0){
			Buff.affect(hero, FellEnemy.class, (float)Math.pow(2f, lvlDiff - 1) * 5);
			if (lvlDiff > 2){
				Horror h = hero.buff(Horror.class);
				if (h != null){
					h.mod(-(float)Math.pow(1.5f, lvlDiff - 2));
					GLog.i(Messages.get(Horror.class, "reduced"));
				}
			}
		}
	}

	/*****************************************************************
	 * Body, strength, health and hunger
	 *****************************************************************/

	public static int strBonus( Hero hero ){
		int b = Satiation.strBonus(hero);
		if (perk("very_strong")) b += 1 + hero.lvl / 7;
		if (perk("stout")) b += hero.lvl / 10;
		return b;
	}

	public static int maxHP( Hero hero, int ht ){
		if (perk("sturdy")) ht += 3 + 2 * (hero.lvl - 1);
		if (perk("frail")) ht -= 2 + 2 * (hero.lvl - 1);
		ht = Math.round(ht * Satiation.hpFactor(hero));
		return Math.max(1, ht);
	}

	public static float hungerRate(){
		if (perk("on_diet")) return 0.7f;
		if (perk("big_stomach")) return 1.5f;
		return 1f;
	}

	//from Hunger.affectHunger, food goes to satiation while the mode is on
	public static boolean feedSatiation( Char target, float energy ){
		if (!satiationOn() || !(target instanceof Hero)) return false;
		Buff.affect(target, Satiation.class).affect(energy);
		return true;
	}

	public static float eatingTime( float time ){
		if (perk("gobbler")) return Math.min(time, 1f);
		if (perk("nibbling")) return time * 2;
		return time;
	}

	public static float regenDelay( Char target, float delay ){
		if (!(target instanceof Hero)) return delay;
		return delay * Satiation.regenFactor((Hero)target);
	}

	public static float stealthBonus(){
		float s = 0;
		if (perk("inconspicuous")) s += 1.5f;
		if (perk("conspicuous")) s -= 2f;
		return s;
	}

	public static int backpackBonus(){
		if (perk("organized")) return 5;
		if (perk("disorganized")) return -5;
		return 0;
	}

	public static int expGain( int exp, Class source ){
		if (perk("quick_learner") && source != null && Mob.class.isAssignableFrom(source)){
			return (int)Math.floor(exp * 1.25f);
		}
		return exp;
	}

	/*****************************************************************
	 * Quick draw, dexterous and clumsy
	 *****************************************************************/

	public static boolean quickDraw = false;
	public static boolean instantAct = false;
	private static int switchCounter = 0;
	private static int actCounter = 0;

	//once a hero turn, from Horror's and Satiation's neighbour, the turn tracker in Hero.spend
	public static void heroTurn(){
		if (perk("dexterous") && !instantAct && ++actCounter >= 2){
			instantAct = true;
			actCounter = 0;
			GLog.i(Messages.get(MusicImplant.class, "actrestore"));
		}
		if (perk("quick_draw") && !quickDraw && ++switchCounter >= 5){
			quickDraw = true;
			switchCounter = 0;
			GLog.i(Messages.get(MusicImplant.class, "switchrestore"));
		}
	}

	//from KindOfWeapon and the light sources, the time it takes to swap a weapon
	public static float equipTime( EquipableItem item, float time ){
		if (perk("slowpoke")) return time * 10;
		if (perk("quick_draw") && quickDraw && time > 0){
			quickDraw = false;
			return 0;
		}
		return time;
	}

	//from Item.doThrow and Potion.drink, true when the clumsy hero fumbles
	public static boolean clumsyFails( Hero hero, Item item, boolean drinking ){
		if (!perk("clumsy")) return false;
		float chance = Random.Float();
		if (chance >= 0.23f) return false;
		GLog.w(Messages.get(MusicImplant.class, drinking ? "clumsydrink" : "clumsythrow", item.name()));
		if (chance < 0.03f){
			GLog.w(Messages.get(MusicImplant.class, "clumsycrit"));
			item.doDrop(hero);
			return true;
		}
		hero.spendAndNext(1f);
		return true;
	}

	//from Item.cast and Potion.drink
	public static float actTime( Item item, float time, boolean drinking ){
		if (perk("dexterous") && instantAct){
			instantAct = false;
			GLog.i(Messages.get(MusicImplant.class, drinking ? "instantdrink" : "instantthrow", item.name()));
			return 0;
		}
		return time;
	}

	/*****************************************************************
	 * Luck, knowledge and the map
	 *****************************************************************/

	public static int wealthBonus(){
		return perk("lucky") ? 2 : 0;
	}

	//from Generator.random, the unlucky hero finds no upgraded gear and more curses
	public static Item unlucky( Item item ){
		if (!perk("unlucky") || item == null) return item;
		boolean gear = item instanceof Weapon || item instanceof Armor || item instanceof Ring || item instanceof Wand;
		if (!gear) return item;
		if (item.level() > 0 && !item.cursed) item.level(0);
		if (!item.cursed && Random.Float() < 0.29f){
			item.cursed = true;
			if (item instanceof Weapon && ((Weapon) item).enchantment == null) ((Weapon) item).enchant(Weapon.Enchantment.randomCurse());
			if (item instanceof Armor && ((Armor) item).glyph == null) ((Armor) item).inscribe(Armor.Glyph.randomCurse());
			if (item.level() == 0) item.level(-Random.IntRange(1, 3));
		}
		return item;
	}

	//from Scroll.execute, true when an illiterate hero read the wrong spell
	public static boolean illiterateRead( Hero hero, Scroll scroll ){
		if (!perk("illiterate")) return false;
		if (scroll instanceof ScrollOfUpgrade || scroll instanceof ScrollOfTransmutation) return false;
		if (Random.Int(9) > 3) return false;
		Scroll other;
		do {
			other = (Scroll) Generator.randomUsingDefaults(Generator.Category.SCROLL);
		} while (other == null
				|| ((other instanceof ScrollOfIdentify || other instanceof ScrollOfRemoveCurse
					|| other instanceof ScrollOfMagicMapping) && Random.Int(2) == 0)
				|| (other instanceof ScrollOfTeleportation && Dungeon.bossLevel())
				|| other instanceof ScrollOfTransmutation);
		GLog.w(Messages.get(MusicImplant.class, "illiterate"));
		scroll.detach(hero.belongings.backpack);
		Item.setCurUser(hero);
		other.anonymize();
		other.doRead();
		return true;
	}

	public static boolean noJournal(){
		if (!perk("illiterate")) return false;
		GLog.w(Messages.get(MusicImplant.class, "nojournal"));
		return true;
	}

	public static boolean mindCompass(){
		return perk("mind_compass");
	}

	public static boolean amnesia(){
		return perk("amnesia");
	}

	//from StatusPane, the hero with no sense of their body sees a word instead of numbers
	public static String hpText( int health, int max, String exact ){
		if (!perk("lack_of_sense")) return exact;
		int ratio = max <= 0 ? 0 : (int)(5 * health / (float)max);
		if (health <= 0) return Messages.get(MusicImplant.class, "hp_dead");
		switch (ratio){
			case 5: case 4: return Messages.get(MusicImplant.class, "hp_healthy");
			case 3: return Messages.get(MusicImplant.class, "hp_fine");
			case 2: return Messages.get(MusicImplant.class, "hp_hurt");
			case 1: return Messages.get(MusicImplant.class, "hp_wounded");
			default: return Messages.get(MusicImplant.class, "hp_dying");
		}
	}

	//from Mob.info, the biologist reads a monster's numbers
	public static String biologist( Mob mob ){
		if (!perk("biologist") || Dungeon.hero == null) return "";
		return "\n\n" + Messages.get(MusicImplant.class, "biologist", mob.HP, mob.HT,
				mob.attackSkill(Dungeon.hero), mob.defenseSkill(Dungeon.hero));
	}

	/*****************************************************************
	 * Loot
	 *****************************************************************/

	//from Level.create, two torches in the halls
	public static ArrayList<Item> levelDrops(){
		ArrayList<Item> items = new ArrayList<>();
		if (horrorOn() && Dungeon.depth > 20 && Dungeon.depth <= 26){
			items.add(new FireTorch());
			items.add(new FireTorch());
		}
		return items;
	}

	public static boolean darkFloorTorch(){
		return horrorOn();
	}

	//from ShopRoom.generateItems, the mod's shops sell torches and wax by depth
	public static ArrayList<Item> shopItems(){
		ArrayList<Item> items = new ArrayList<>();
		if (!horrorOn()) return items;
		int torches = Dungeon.depth <= 6 ? 1 : Dungeon.depth <= 16 ? 2 : 3;
		for (int i = 0; i < torches; i++) items.add(new FireTorch());
		items.add(new WaxNWick().quantity(Random.NormalIntRange(2, 4)));
		return items;
	}
}
