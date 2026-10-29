/*
 * Onyx's Pixel Dungeon
 *
 * Game modes from three small mods.
 *   Modded Pixel Dungeon by Cocoa (Hoto-Mocha-sub/Modded-Pixel-Dungeon), a test starting kit.
 *   Speedrun Pixel Dungeon by QuasiStellar (QuasiStellar/Speedrun-Pixel-Dungeon), the run timer with
 *   splits and personal bests, the restart button, and Bones% (pick the remains found on floor 1).
 *   Old Speedrun Pixel Dungeon by QuasiStellar (QuasiStellar/Old-Speedrun-Pixel-Dungeon), floor 1 always
 *   holds remains with a cursed +3 wand of lightning.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class SmallModes {

	public static final String MOD_MODDED      = "Modded Pixel Dungeon";
	public static final String MOD_SPEEDRUN    = "Speedrun Pixel Dungeon";
	public static final String MOD_OLDSPEEDRUN = "Old Speedrun Pixel Dungeon";

	public static final String MODDED_KIT      = "modded_kit";
	public static final String SPEEDRUN_TIMER  = "speedrun_timer";
	public static final String SPEEDRUN_BONES  = "speedrun_bones";
	public static final String OLDSPEEDRUN_WAND= "oldspeedrun_wand";

	public static void register(){
		GameModes.register(MODDED_KIT, MOD_MODDED, 1, null);
		GameModes.register(SPEEDRUN_TIMER, MOD_SPEEDRUN, 1, null);
		//one level for each of the ten items Speedrun PD's bones window offered
		GameModes.register(SPEEDRUN_BONES, MOD_SPEEDRUN, 10, "floor1_remains");
		GameModes.register(OLDSPEEDRUN_WAND, MOD_OLDSPEEDRUN, 1, "floor1_remains");
	}

	/*****************************************************************
	 * Modded PD, the test kit its starting code hands every hero
	 *****************************************************************/

	public static void applyStartKits( Hero hero ){
		if (GameModes.on(MODDED_KIT)){
			new PlateArmor().upgrade(100).identify().collect(hero.belongings.backpack);
			new PotionOfMindVision().identify().quantity(20).collect(hero.belongings.backpack);
			if (hero.heroClass == HeroClass.WARRIOR){
				new ScrollOfIdentify().identify().quantity(2).collect(hero.belongings.backpack);
			}
		}
	}

	/*****************************************************************
	 * Remains on floor 1, Speedrun PD's Bones% and Old Speedrun PD's wand
	 *****************************************************************/

	//the item Speedrun PD's bones window offers at each level of the mode
	public static Item bonesItem( int lvl ){
		Item item;
		switch (lvl){
			case 1: default: item = new WandOfCorrosion(); break;
			case 2: item = new WandOfLightning(); break;
			case 3: item = new WandOfFireblast(); break;
			case 4: item = new WandOfMagicMissile(); break;
			case 5: item = new WandOfDisintegration(); break;
			case 6: item = new TimekeepersHourglass(); break;
			case 7: item = new HornOfPlenty(); break;
			case 8: item = new EtherealChains(); break;
			case 9: item = new CapeOfThorns(); break;
			//Speedrun PD offered Lloyd's Beacon, which Shattered turned into the Beacon of Returning
			case 10: item = new BeaconOfReturning(); break;
		}
		if (item.isUpgradable()) item.upgrade(3);
		return item;
	}

	//called from RegularLevel.createItems next to the normal bones drop
	public static ArrayList<Item> floorOneRemains(){
		ArrayList<Item> items = new ArrayList<>();
		if (Dungeon.depth != 1 || Dungeon.branch != 0) return items;
		if (GameModes.on(SPEEDRUN_BONES)){
			items.add(bonesItem(GameModes.level(SPEEDRUN_BONES)));
		}
		if (GameModes.on(OLDSPEEDRUN_WAND)){
			WandOfLightning remains = new WandOfLightning();
			remains.level(3);
			remains.cursed = true;
			items.add(remains);
		}
		return items;
	}

	/*****************************************************************
	 * Speedrun PD's timer. It starts on the first turn, takes a split on each new floor
	 * and stops when the amulet is picked up. Runs are compared to a goal, the average of
	 * finished runs lowered to the personal best, as in the mod
	 *****************************************************************/

	public static final int SPLITS = 27;

	public static long time = 0;          //milliseconds of play in this run
	public static boolean started = false;
	public static boolean finished = false;
	public static long[] splits = new long[SPLITS]; //run time when each floor was first reached
	public static String category = "any";

	private static long lastFrame = -1;

	public static boolean timerOn(){
		return GameModes.on(SPEEDRUN_TIMER);
	}

	public static boolean restartButton(){
		return timerOn();
	}

	//called from Dungeon.init for every new run
	public static void reset(){
		time = 0;
		started = false;
		finished = false;
		splits = new long[SPLITS];
		lastFrame = -1;
		if (GameModes.on(SPEEDRUN_BONES)) category = "bones";
		else if (Integer.bitCount(Dungeon.challenges & 511) >= 9) category = "allchallenges";
		else category = "any";
		if (timerOn()){
			SPDSettings.put(key("attempts"), SPDSettings.getInt(key("attempts"), 0) + 1);
		}
	}

	private static String key( String what ){
		return "speedrun_" + category + "_" + what;
	}

	public static String categoryName(){
		return Messages.get(SmallModes.class, "cat_" + category);
	}

	//called every frame by the timer on the game screen
	public static void tick(){
		long now = Game.realTime;
		if (!started && Dungeon.hero != null && Actor.now() > 0){
			started = true;
		}
		if (started && !finished && lastFrame >= 0){
			//a frame longer than a second is a loading pause or a minimized window
			time += Math.max(0, Math.min(1000, now - lastFrame));
		}
		lastFrame = now;
	}

	//called from Dungeon.switchLevel on every arrival
	public static void onArrive( int depth ){
		if (!timerOn() || finished || Dungeon.branch != 0) return;
		if (depth <= 0 || depth >= SPLITS || splits[depth] != 0 || depth == 1) return;
		splits[depth] = Math.max(1, time);
		GLog.i(Messages.get(SmallModes.class, "split", depth, timeToString(time)));
	}

	//called from Amulet.doPickUp
	public static void onAmulet(){
		if (!timerOn() || finished) return;
		finished = true;
		splits[SPLITS-1] = time;
		int runs = SPDSettings.getInt(key("runs"), 0);
		for (int i = 2; i < SPLITS; i++){
			long split = splits[i];
			if (split == 0) continue;
			long avg = SPDSettings.getLong(key("avg" + i), 0);
			SPDSettings.put(key("avg" + i), (avg * runs + split) / (runs + 1));
		}
		SPDSettings.put(key("runs"), runs + 1);
		long pb = personalBest();
		if (pb <= 0 || time < pb){
			SPDSettings.put(key("pb"), time);
			GLog.p(Messages.get(SmallModes.class, "new_pb", timeToString(time)));
		} else {
			GLog.p(Messages.get(SmallModes.class, "finished", timeToString(time)));
		}
	}

	public static int runs(){
		return SPDSettings.getInt(key("runs"), 0);
	}

	public static int attempts(){
		return SPDSettings.getInt(key("attempts"), 0);
	}

	public static long personalBest(){
		return SPDSettings.getLong(key("pb"), 0);
	}

	//the time to beat for the floor the hero is going for, the average lowered to the personal best
	public static long goal(){
		if (runs() == 0) return 0;
		long avgTotal = SPDSettings.getLong(key("avg" + (SPLITS-1)), 0);
		if (avgTotal <= 0) return 0;
		int next = Math.min(SPLITS-1, Math.max(2, Dungeon.depth + 1));
		if (Dungeon.depth >= 26) next = SPLITS-1;
		long avg = SPDSettings.getLong(key("avg" + next), 0);
		return (long)(avg * (personalBest() / (double)avgTotal));
	}

	public static String timeToString( long time ){
		long minutes = time / 60000;
		time -= minutes * 60000L;
		long seconds = time / 1000;
		time -= seconds * 1000L;
		long hundredths = time / 10;
		return String.format("%02d:%02d.%02d", minutes, seconds, hundredths);
	}

	private static final String TIME     = "speedrun_time";
	private static final String STARTED  = "speedrun_started";
	private static final String FINISHED = "speedrun_finished";
	private static final String SPLITS_K = "speedrun_splits";
	private static final String CATEGORY = "speedrun_category";

	//called from GameModes.store and restore, so the timer is saved with the run
	public static void store( Bundle bundle ){
		if (!timerOn()) return;
		bundle.put(TIME, time);
		bundle.put(STARTED, started);
		bundle.put(FINISHED, finished);
		bundle.put(SPLITS_K, splits);
		bundle.put(CATEGORY, category);
	}

	public static void restore( Bundle bundle ){
		lastFrame = -1;
		if (!bundle.contains(TIME)){
			time = 0; started = false; finished = false; splits = new long[SPLITS];
			return;
		}
		time = bundle.getLong(TIME);
		started = bundle.getBoolean(STARTED);
		finished = bundle.getBoolean(FINISHED);
		long[] s = bundle.getLongArray(SPLITS_K);
		splits = new long[SPLITS];
		if (s != null) System.arraycopy(s, 0, splits, 0, Math.min(s.length, SPLITS));
		category = bundle.getString(CATEGORY);
		if (category == null || category.isEmpty()) category = "any";
	}
}
