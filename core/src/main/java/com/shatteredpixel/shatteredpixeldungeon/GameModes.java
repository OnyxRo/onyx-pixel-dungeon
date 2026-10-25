/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Game modes from Half PD (00-Evan fork), Even Shorter PD, Shattered PD Radar,
 * Custom PD's starting kit mods and Too Cruel PD Trials, ported into Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//Game modes picked on the hero select screen. Each mode is an id with a level, 0 means off.
//The picks are remembered in the settings for the next run and saved with each run.
public class GameModes {

	public static class Mode {
		public final String id;
		public final String mod;
		public final int maxLevel;
		//modes that share a group cannot be on together (the dungeon layouts)
		public final String group;

		Mode( String id, String mod, int maxLevel, String group ){
			this.id = id;
			this.mod = mod;
			this.maxLevel = maxLevel;
			this.group = group;
		}

		public String title(){
			return Messages.get(GameModes.class, id);
		}

		public String desc(){
			return Messages.get(GameModes.class, id + "_desc");
		}
	}

	public static final String HALF         = "half";
	public static final String EVEN_SHORTER = "even_shorter";

	public static final String KIT_CAPE     = "kit_cape";
	public static final String KIT_CURSED   = "kit_cursed";
	public static final String KIT_FACTS    = "kit_facts";
	public static final String KIT_MODERN   = "kit_modern";
	public static final String KIT_MORSTOR  = "kit_morstor";
	public static final String KIT_ROCC     = "kit_rocc";
	public static final String KIT_ROXX     = "kit_roxx";
	public static final String KIT_STARTER  = "kit_starter";
	public static final String KIT_KNOWLEDGE= "kit_knowledge";

	public static final String MOD_HALF     = "Half Pixel Dungeon";
	public static final String MOD_SHORTER  = "Even Shorter Pixel Dungeon";
	public static final String MOD_RADAR    = "Shattered Pixel Dungeon Radar";
	public static final String MOD_CUSTOM   = "Custom Pixel Dungeon";
	public static final String MOD_TOOCRUEL = "Too Cruel Pixel Dungeon Trials";

	//the modes of the current run, or the picks on the hero select screen before a run starts
	public static final LinkedHashMap<String, Integer> active = new LinkedHashMap<>();

	public static final ArrayList<Mode> MODES = new ArrayList<>();

	static {
		MODES.add(new Mode(HALF, MOD_HALF, 1, "layout"));
		MODES.add(new Mode(EVEN_SHORTER, MOD_SHORTER, 1, "layout"));
		for (String style : Playstyles.NAME_IDS){
			MODES.add(new Mode(Playstyles.PREFIX + style, MOD_RADAR, 3, null));
		}
		for (String kit : new String[]{KIT_CAPE, KIT_CURSED, KIT_FACTS, KIT_MODERN, KIT_MORSTOR,
				KIT_ROCC, KIT_ROXX, KIT_STARTER, KIT_KNOWLEDGE}){
			MODES.add(new Mode(kit, MOD_CUSTOM, 1, null));
		}
		TooCruel.register(); //toocruel
	}

	//other ports add their modes here, Too Cruel's modifiers among them
	public static void register( String id, String mod, int maxLevel, String group ){
		MODES.add(new Mode(id, mod, maxLevel, group));
	}

	public static Mode get( String id ){
		for (Mode m : MODES) if (m.id.equals(id)) return m;
		return null;
	}

	public static ArrayList<String> mods(){
		ArrayList<String> mods = new ArrayList<>();
		for (Mode m : MODES) if (!mods.contains(m.mod)) mods.add(m.mod);
		return mods;
	}

	public static int level( String id ){
		Integer lvl = active.get(id);
		return lvl == null ? 0 : lvl;
	}

	public static boolean on( String id ){
		return level(id) > 0;
	}

	public static void set( String id, int lvl ){
		Mode mode = get(id);
		if (mode == null) return;
		lvl = Math.max(0, Math.min(mode.maxLevel, lvl));
		TooCruel.onSet(id, lvl); //toocruel, what a mode needs and what it conflicts with
		if (lvl == 0){
			active.remove(id);
			return;
		}
		if (mode.group != null){
			for (Mode other : MODES){
				if (other != mode && mode.group.equals(other.group)) active.remove(other.id);
			}
		}
		active.put(id, lvl);
	}

	public static int count(){
		return active.size();
	}

	public static int count( String mod ){
		int n = 0;
		for (String id : active.keySet()){
			Mode m = get(id);
			if (m != null && m.mod.equals(mod)) n++;
		}
		return n;
	}

	/*****************************************************************
	 * Saving, in the run and in the settings
	 *****************************************************************/

	private static final String MODES_KEY  = "game_modes";
	private static final String LEVELS_KEY = "game_mode_levels";
	private static final String SETTING    = "onyx_game_modes";

	public static void store( Bundle bundle ){
		String[] ids = active.keySet().toArray(new String[0]);
		int[] levels = new int[ids.length];
		for (int i = 0; i < ids.length; i++) levels[i] = active.get(ids[i]);
		bundle.put(MODES_KEY, ids);
		bundle.put(LEVELS_KEY, levels);
	}

	public static void restore( Bundle bundle ){
		active.clear();
		if (!bundle.contains(MODES_KEY)) return;
		String[] ids = bundle.getStringArray(MODES_KEY);
		int[] levels = bundle.getIntArray(LEVELS_KEY);
		for (int i = 0; i < ids.length && i < levels.length; i++){
			if (levels[i] > 0) active.put(ids[i], levels[i]);
		}
		Playstyles.refresh();
		TooCruel.refresh(); //toocruel
	}

	public static void loadSettings(){
		active.clear();
		String s = SPDSettings.getString(SETTING, "");
		for (String part : s.split(",")){
			int eq = part.indexOf('=');
			if (eq <= 0) continue;
			try {
				set(part.substring(0, eq), Integer.parseInt(part.substring(eq+1)));
			} catch (NumberFormatException ignored){ }
		}
		Playstyles.refresh();
	}

	public static void saveSettings(){
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Integer> e : active.entrySet()){
			if (sb.length() > 0) sb.append(',');
			sb.append(e.getKey()).append('=').append(e.getValue());
		}
		SPDSettings.put(SETTING, sb.toString());
		Playstyles.refresh();
	}

	/*****************************************************************
	 * Half PD and Even Shorter PD, shorter dungeons
	 *****************************************************************/

	//Half PD keeps three floors of each region, the first floor, the quest floor and the boss
	private static final int[] HALF_FLOORS = {1, 4, 5, 6, 9, 10, 11, 14, 15, 16, 19, 20, 21, 24, 25, 26};
	//Even Shorter PD is the sewers and the prison, then the amulet
	private static final int[] SHORTER_FLOORS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 26};

	public static int[] floors(){
		if (on(HALF)) return HALF_FLOORS;
		if (on(EVEN_SHORTER)) return SHORTER_FLOORS;
		return null;
	}

	public static int nextDepth( int depth ){
		int[] floors = floors();
		if (floors == null || Dungeon.branch != 0) return depth + 1;
		for (int f : floors) if (f > depth) return f;
		return depth + 1;
	}

	public static int prevDepth( int depth ){
		int[] floors = floors();
		if (floors == null || Dungeon.branch != 0) return depth - 1;
		int prev = depth - 1;
		for (int f : floors){
			if (f < depth) prev = f;
			else break;
		}
		return prev;
	}

	//how many floors a run has before the amulet floor, for the arrival text
	public static int floorCount(){
		int[] floors = floors();
		return floors == null ? 25 : floors.length - 1;
	}

	//Half PD levels up faster, as its floors hold fewer monsters
	public static int maxExp( int lvl, int normal ){
		if (on(HALF)) return 3 + lvl * 3;
		return normal;
	}

	//Even Shorter PD caps the hero at level 12
	public static int maxLevel(){
		return on(EVEN_SHORTER) ? 12 : Hero.MAX_LEVEL;
	}

	public static float foodMult(){
		return on(EVEN_SHORTER) ? 0.5f : 1f;
	}

	public static float healingMult(){
		return on(EVEN_SHORTER) ? 0.5f : 1f;
	}

	//Even Shorter PD's armor works like the Faith is my Armor challenge
	public static boolean weakArmor(){
		return on(EVEN_SHORTER);
	}

	private static final float[][] SHORTER_TIERS = {
			{0, 75, 25, 0, 0},
			{0, 35, 65, 0, 0}
	};

	//Even Shorter PD only finds weapons and armor up to tier 3
	public static float[] tierProbs( int floorSet ){
		if (!on(EVEN_SHORTER)) return null;
		return SHORTER_TIERS[Math.min(floorSet, SHORTER_TIERS.length-1)];
	}

	//Half PD skips two floors of every region, so the strength potions, upgrade scrolls and styli of
	//those floors are added to the floors that are left, as in Half PD (two scrolls on the first floor, one on the second)
	public static void topUpDrops( Level level ){
		if (!on(HALF)) return;
		int depth = Dungeon.depth;
		int set = depth / 5;
		int floorThisSet = depth % 5;
		if (floorThisSet == 0) return;
		boolean last = floorThisSet >= 4;

		int pos = 2*set + (last ? 2 : 1);
		while (Dungeon.LimitedDrops.STRENGTH_POTIONS.count < pos){
			Dungeon.LimitedDrops.STRENGTH_POTIONS.count++;
			level.addItemToSpawn(new PotionOfStrength());
		}
		int sou = 3*set + (last ? 3 : 2);
		while (Dungeon.LimitedDrops.UPGRADE_SCROLLS.count < sou){
			Dungeon.LimitedDrops.UPGRADE_SCROLLS.count++;
			if (!Dungeon.isChallenged(Challenges.NO_SCROLLS) || Dungeon.LimitedDrops.UPGRADE_SCROLLS.count%2 != 0){
				level.addItemToSpawn(new ScrollOfUpgrade());
			}
		}
		if (last){
			while (Dungeon.LimitedDrops.ARCANE_STYLI.count < set + 1){
				Dungeon.LimitedDrops.ARCANE_STYLI.count++;
				level.addItemToSpawn(new com.shatteredpixel.shatteredpixeldungeon.items.Stylus());
			}
		}
		//Half PD hands out a random weapon or armor on the first floor
		if (depth == 1){
			level.addItemToSpawn(Random.Int(2) == 0 ? Generator.randomWeapon(0) : Generator.randomArmor(0));
		}
	}

	/*****************************************************************
	 * Custom PD's starting kit mods
	 *****************************************************************/

	public static void applyStartKits( Hero hero ){
		if (on(KIT_KNOWLEDGE)){
			for (Generator.Category cat : new Generator.Category[]{Generator.Category.POTION, Generator.Category.SCROLL, Generator.Category.RING}){
				for (Class<?> cls : cat.classes){
					Item i = (Item) Reflection.newInstance(cls);
					if (i != null) i.identify(false);
				}
			}
		}

		if (on(KIT_CAPE)){
			CapeOfThorns cape = new CapeOfThorns();
			cape.identify(false);
			equipArtifact(hero, cape);
		}

		if (on(KIT_CURSED)){
			Artifact a;
			switch (hero.heroClass){
				case WARRIOR: a = new HornOfPlenty(); break;
				case MAGE: a = new UnstableSpellbook(); break;
				case ROGUE: a = new MasterThievesArmband(); break;
				case HUNTRESS: a = new EtherealChains(); break;
				case DUELIST: a = new ChaliceOfBlood(); break;
				default:
					a = (Artifact) Reflection.newInstance(Random.oneOf(HornOfPlenty.class, UnstableSpellbook.class,
							MasterThievesArmband.class, EtherealChains.class, ChaliceOfBlood.class));
			}
			a.cursed = a.cursedKnown = true;
			equipArtifact(hero, a);
		}

		if (on(KIT_FACTS)){
			Class<?>[] all = {AlchemistsToolkit.class, CapeOfThorns.class, ChaliceOfBlood.class, CloakOfShadows.class,
					DriedRose.class, EtherealChains.class, HornOfPlenty.class, MasterThievesArmband.class,
					SandalsOfNature.class, TalismanOfForesight.class, TimekeepersHourglass.class, UnstableSpellbook.class};
			for (Class<?> cls : all){
				give(hero, (Item) Reflection.newInstance(cls));
			}
		}

		if (on(KIT_MODERN)){
			give(hero, new SmallRation().quantity(3));
			give(hero, new PotionOfHealing());
			give(hero, new PotionOfMastery());
			give(hero, new ScrollOfUpgrade());
			give(hero, new ScrollOfIdentify());
			switch (hero.heroClass){
				case WARRIOR: default:
					give(hero, new RingOfMight()); give(hero, new PotionOfStrength()); give(hero, new Bomb().quantity(3)); break;
				case MAGE:
					give(hero, new RingOfEnergy()); give(hero, new PotionOfMindVision()); give(hero, new Firebomb().quantity(3)); break;
				case ROGUE:
					give(hero, new RingOfEvasion()); give(hero, new PotionOfInvisibility()); give(hero, new ShockBomb()); break;
				case HUNTRESS:
					give(hero, new RingOfAccuracy()); give(hero, new PotionOfHaste()); give(hero, new Flashbang()); break;
				case DUELIST:
					give(hero, new RingOfForce()); give(hero, new PotionOfExperience()); give(hero, new HolyBomb()); break;
			}
			new PotionOfHealing().identify(false);
			new PotionOfStrength().identify(false);
		}

		if (on(KIT_MORSTOR)){
			if (hero.belongings.getItem(MagicalHolster.class) == null) give(hero, new MagicalHolster());
			if (hero.belongings.getItem(PotionBandolier.class) == null) give(hero, new PotionBandolier());
			if (hero.belongings.getItem(ScrollHolder.class) == null) give(hero, new ScrollHolder());
		}

		if (on(KIT_ROCC)){
			give(hero, new ThrowingStone().quantity(1).upgrade(16));
		}

		if (on(KIT_ROXX)){
			give(hero, new ThrowingStone().quantity(20).upgrade(20));
		}

		if (on(KIT_STARTER)){
			give(hero, new Torch());
			give(hero, new PotionOfHealing());
			give(hero, new PotionOfMastery());
			give(hero, new Alchemize().quantity(4));
			give(hero, new StoneOfIntuition());
			give(hero, TippedDart.randomTipped(2));
			give(hero, new Bomb());
			give(hero, new Honeypot());
			give(hero, new ScrollOfIdentify());
			new PotionOfHealing().identify(false);
			new PotionOfStrength().identify(false);
		}
	}

	private static void give( Hero hero, Item item ){
		if (item != null) item.collect(hero.belongings.backpack);
	}

	private static void equipArtifact( Hero hero, Artifact a ){
		if (hero.belongings.artifact == null){
			hero.belongings.artifact = a;
		} else if (hero.belongings.misc == null){
			hero.belongings.misc = a;
		} else {
			give(hero, a);
			return;
		}
		a.activate(hero);
	}
}
