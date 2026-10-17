/*
 * Onyx's Pixel Dungeon
 * The monsters a floor of one of Magic Ling PD's worlds spawns, following the rotation in the mod's own
 * MobSpawner (its standard mode) with the Shattered monsters it names. Classes are looked up by name, so a
 * monster the mod names that is not in this game is just left out of the rotation.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagicLingSpawner {

	private static final String[] PACKAGES = {
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.",
			"com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ancity.",
	};

	@SuppressWarnings("unchecked")
	private static Class<? extends Mob> find( String name ){
		for (String p : PACKAGES) {
			try {
				return (Class<? extends Mob>) Class.forName( p + name );
			} catch (Throwable ignored) {
			}
		}
		return null;
	}

	private static void add( ArrayList<Class<? extends Mob>> list, String... names ){
		for (String n : names) {
			Class<? extends Mob> c = find( n );
			if (c != null) list.add( c );
		}
	}

	//the mod's alternates, one in fifty of a regular monster
	private static final String[][] ALTS = {
			{"Rat", "Albino"}, {"FlowerSlime", "GiantFlowerSlime"}, {"Katydid", "Salamander"}, {"Crab", "NewBornCrab"},
			{"Guard", "GuardCapital"}, {"Thief", "Bandit"}, {"BrownBat", "NewBlackHost"}, {"Brute", "ArmoredBrute"},
			{"DM100", "DM111"}, {"ColdMagicRat", "VeryColdRat"}, {"Warlock", "WarlockHead"}, {"Ghoul", "GhoulPlus"},
			{"Scorpio", "Acidic"}, {"RedSwarm", "BloodsSwarm"}, {"FireGhost", "MolotovHuntsman"}, {"Eye", "DeadEye"},
			{"SmallSkeletonDemon", "SkeletonDemon"}, {"Worm", "WormWhyHuman"}, {"NecroScout", "NecroPioneer"},
	};

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> res = new ArrayList<>();
		switch (depth) {
			case 1:
				add( res, "Rat", "Rat" ); break;
			case 2:
				add( res, "FlowerSlime", "FlowerSlime", "FlowerSlime", "Gnoll", "Gnoll", "Gnoll", "Gnoll" ); break;
			case 3:
				add( res, "FlowerSlime", "FlowerSlime", "Katydid", "Katydid", "FlowerSlime", "FlowerSlime", "ClearElemental", "Crab", "Swarm" ); break;
			case 4:
				add( res, "Katydid", "ClearElemental", "Slime_Red", "Slime_Orange", "Swarm", "Crab" ); break;
			case 5:
				add( res, "FlowerSlime", "FlowerSlime", "Slime", "Slime", "Swarm", "Crab" ); break;
			case 6:
				add( res, "Skeleton", "Skeleton", "BrownBat", "BrownBat", "Thief", "Thief" ); break;
			case 7:
				add( res, "Skeleton", "Thief", "DM100", "Necromancer", "Guard", "DM100" ); break;
			case 8:
				add( res, "Skeleton", "Skeleton", "Thief", "Guard", "Necromancer", "DM100" ); break;
			case 9:
				add( res, "Skeleton", "Skeleton", "Skeleton", "Thief", "Thief", "BrownBat", "BrownBat", "DM100", "BrownBat", "Thief", "Thief" ); break;
			case 10:
				add( res, "Bat", "Brute", "Brute", "Necromancer", "Necromancer" ); break;
			case 11:
				add( res, "Bat", "DM200", "DM200", "ColdMagicRat", "ColdMagicRat" ); break;
			case 12:
				add( res, "Bat", "ColdMagicRat", "FireGhost", "ColdMagicRat" ); break;
			case 13:
				add( res, "Bat", "Brute", "Spinner", "DM200", "RedSwarm" ); break;
			case 14:
				add( res, "Bat", "Spinner", "DM200" );
				res.add( Shaman.random() ); break;
			case 15:
				add( res, "Bat", "Spinner", "Brute", "FireGhost", "ColdMagicRat", "RedSwarm" ); break;
			case 16:
				add( res, "Monk", "Ghoul" ); break;
			case 17:
				res.add( Elemental.random() );
				add( res, "Monk", "Ghoul" ); break;
			case 18:
				res.add( Elemental.random() );
				add( res, Random.NormalFloat( 1, 10 ) >= 3 ? "BruteBot" : "Warlock", "Monk", "Golem", "FireGhost" ); break;
			case 19:
				add( res, "Monk", "Golem", "Warlock", "ShieldHuntsman", Random.NormalFloat( 1, 10 ) > 3 ? "BruteBot" : "Warlock" ); break;
			case 20:
				res.add( Elemental.random() );
				add( res, Random.NormalFloat( 1, 10 ) > 3 ? "BruteBot" : "Warlock", "Monk", "Golem", "Golem", "Ice_Scorpio" ); break;
			case 21:
				add( res, "Eye", "Scorpio", "Fire_Scorpio" ); break;
			case 22:
				add( res, "Eye", "Ice_Scorpio", "Fire_Scorpio" ); break;
			case 23:
				add( res, "Eye", "ShieldHuntsman", "Ice_Scorpio" ); break;
			case 24:
				add( res, "Succubus", "Eye", "Scorpio", "Succubus", "Fire_Scorpio", "Ice_Scorpio", "ShieldHuntsman" ); break;
			default:
				add( res, "Frankenstein", "Crumb", "Ghost_Halloween", "Pumking_Ghost", "Butcher", "ApprenticeWitch", "Eye" ); break;
		}
		//rare extras the mod keeps
		if (depth == 4 && Random.Float() < 0.025f) add( res, "Thief" );
		if (depth == 9 && Random.Float() < 0.025f) add( res, "Bat" );
		if (depth == 14 && Random.Float() < 0.025f) add( res, "Ghoul" );
		if (depth == 19 && Random.Float() < 0.025f) add( res, "Succubus" );
		//alternates
		for (int i = 0; i < res.size(); i++) {
			if (Random.Float() < 1 / 50f) {
				for (String[] alt : ALTS) {
					if (res.get( i ).getSimpleName().equals( alt[0] )) {
						Class<? extends Mob> c = find( alt[1] );
						if (c != null) res.set( i, c );
						break;
					}
				}
			}
		}
		if (res.isEmpty()) return MobSpawner.getMobRotation( depth );
		Random.shuffle( res );
		return res;
	}
}
