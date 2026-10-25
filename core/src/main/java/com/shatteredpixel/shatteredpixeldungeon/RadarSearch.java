/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Seed radar from Shattered Pixel Dungeon Radar, ported into Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Radar's seed search. Builds the first floors of random seeds without showing them and keeps the first seed
//whose items include every name written for each floor. Item names are matched without identifying anything,
//so the journal and the known potions of the real run are not touched.
public class RadarSearch {

	public static final int[] FLOORS = {1, 2, 5, 10};
	public static final int MAX_SEEDS = 2000;

	private static final String SETTING = "onyx_radar_floor";

	public static String items( int slot ){
		return SPDSettings.getString(SETTING + FLOORS[slot], "", 400);
	}

	public static void items( int slot, String text ){
		SPDSettings.put(SETTING + FLOORS[slot], text == null ? "" : text);
	}

	public static boolean hasRequirements(){
		for (int i = 0; i < FLOORS.length; i++){
			if (!parse(items(i)).isEmpty()) return true;
		}
		return false;
	}

	public volatile int checked = 0;
	public volatile boolean cancelled = false;

	//returns the seed found, or -1
	public long search(){
		ArrayList<ArrayList<String>> wanted = new ArrayList<>();
		int maxDepth = 0;
		for (int i = 0; i < FLOORS.length; i++){
			ArrayList<String> frags = parse(items(i));
			wanted.add(frags);
			if (!frags.isEmpty()) maxDepth = FLOORS[i];
		}
		if (maxDepth == 0) return -1;

		HeroClass cls = GamesInProgress.selectedClass;
		String customSeed = SPDSettings.customSeed();
		Random.pushGenerator();
		try {
			for (int attempt = 0; attempt < MAX_SEEDS && !cancelled; attempt++){
				checked = attempt + 1;
				long seed = Random.Long(DungeonSeed.TOTAL_SEEDS);
				ArrayList<String>[] found = itemsBy(seed, maxDepth, cls);
				if (found == null) continue;

				boolean ok = true;
				for (int i = 0; i < FLOORS.length && ok; i++){
					StringBuilder block = new StringBuilder();
					for (int d = 0; d <= FLOORS[i] && d < found.length; d++){
						for (String s : found[d]) block.append(s).append('\n');
					}
					String b = block.toString();
					for (String frag : wanted.get(i)){
						if (!b.contains(frag)){
							ok = false;
							break;
						}
					}
				}
				if (ok) return seed;
			}
		} finally {
			Random.popGenerator();
			SPDSettings.customSeed(customSeed);
			GamesInProgress.selectedClass = cls;
		}
		return -1;
	}

	//the names of the items on each floor of a seed, built the way a new run builds them
	@SuppressWarnings("unchecked")
	private ArrayList<String>[] itemsBy( long seed, int maxDepth, HeroClass cls ){
		ArrayList<String>[] found = new ArrayList[maxDepth + 1];
		for (int i = 0; i <= maxDepth; i++) found[i] = new ArrayList<>();
		try {
			Dungeon.daily = Dungeon.dailyReplay = false;
			Dungeon.seed = seed;
			Dungeon.customSeedText = "";
			GamesInProgress.selectedClass = cls;
			Dungeon.init();

			boolean ghostListed = false, wandListed = false;
			while (Dungeon.depth <= maxDepth && !cancelled){
				int depth = Dungeon.depth;
				Level l = Dungeon.newLevel();
				for (Heap h : l.heaps.valueList()){
					if (h.type == Heap.Type.FOR_SALE) continue;
					for (Item item : h.items) found[depth].add(name(item));
				}
				for (Mob m : l.mobs){
					if (m instanceof ArmoredStatue){
						found[depth].add(name(((ArmoredStatue) m).armor()));
					}
					if (m instanceof Statue && ((Statue) m).weapon() != null){
						found[depth].add(name(((Statue) m).weapon()));
					} else if (m instanceof Mimic && ((Mimic) m).items != null){
						for (Item item : ((Mimic) m).items) found[depth].add(name(item));
					}
				}
				if (!ghostListed && Ghost.Quest.armor != null){
					ghostListed = true;
					found[depth].add(name(Ghost.Quest.armor));
					if (Ghost.Quest.weapon != null) found[depth].add(name(Ghost.Quest.weapon));
				}
				if (!wandListed && Wandmaker.Quest.wand1 != null){
					wandListed = true;
					found[depth].add(name(Wandmaker.Quest.wand1));
					if (Wandmaker.Quest.wand2 != null) found[depth].add(name(Wandmaker.Quest.wand2));
				}
				Dungeon.depth = GameModes.nextDepth(depth);
			}
		} catch (Exception e){
			//a floor that fails to build only costs this seed
			return null;
		}
		return found;
	}

	private static String name( Item item ){
		if (item == null) return "";
		String n = item.trueName();
		if (item instanceof Weapon && ((Weapon) item).enchantment != null){
			n = ((Weapon) item).enchantment.name(n);
		} else if (item instanceof Armor && ((Armor) item).glyph != null){
			n = ((Armor) item).glyph.name(n);
		}
		if (item.isUpgradable() && item.level() != 0){
			n += (item.level() > 0 ? " +" : " ") + item.level();
		}
		return n.toLowerCase();
	}

	private static ArrayList<String> parse( String text ){
		ArrayList<String> list = new ArrayList<>();
		if (text == null) return list;
		for (String s : text.toLowerCase().split("[\\r\\n,]+")){
			s = s.trim();
			if (!s.isEmpty()) list.add(s);
		}
		return list;
	}
}
