/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shadows;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.DivineSense;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WindParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.DimensionalSundial;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.EyeOfNewt;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MossyClump;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.HighGrass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Level implements Bundlable {
	//Tower PD, passable but not on the border ring of the map
	public boolean passableChecked(int cell) {
		if (cellAdjacentToBorderCells(cell)) return false;
		return passable[cell];
	}

	
	public static enum Feeling {
		NONE,
		CHASM,
		WATER,
		GRASS,
		DARK,
		LARGE,
		TRAPS,
		SECRETS,
		//Transformation PD's tower floors
		SKY_ISLAND,
		BARREN,
		CHAOS;

		public String title(){
			return Messages.get(this, name()+"_title");
		}

		public String desc() {
			return Messages.get(this, name()+"_desc");
		}
	}

	protected int width;
	protected int height;
	protected int length;
	
	protected static final float TIME_TO_RESPAWN	= 50;

	public int version;

	//Tower PD shims. Normal floors never touch these, the tower defense arenas read and write them.
	public com.shatteredpixel.shatteredpixeldungeon.windows.WndModes.Modes mode = com.shatteredpixel.shatteredpixeldungeon.windows.WndModes.Modes.NORMAL;
	public int wave = 0;
	public void initNpcs(){
	}
	public com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic lastSavedWndDialogueWithPic;
	public com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers slot1 = com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers.WALL;
	public com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers slot2 = com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers.CROSSBOW;
	public com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers slot3 = com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers.GUARD;
	public com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers slot4 = com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo.AllTowers.MAGICMISSILE;

	public boolean cellAdjacentToBorderCells(int cell){
		return (cell / width() <= 1 || cell / width() >= height()-1 || cell % width() <= 1 || cell % width() >= width()-1 ) ;
	}
	
	public int[] map;
	public boolean[] visited;
	public boolean[] mapped;
	public boolean[] discoverable;

	//the shuffle world whose mod supplied this boss floor, -1 for any other floor. Shows that mod's logo on the boss floor
	public int modWorld = -1;

	public int viewDistance = Dungeon.isChallenged( Challenges.DARKNESS ) ? 2 : (Dungeon.isChallenged( Challenges.FORGET_PATH ) ? 6 : 8); //deranged
	
	public boolean[] heroFOV;
	
	public boolean[] passable;
	public boolean[] losBlocking;
	public boolean[] flamable;
	public boolean[] secret;
	public boolean[] solid;
	public boolean[] avoid;
	//dry ground, as Devoted Pixel Dungeon marks it. See dryTerrain
	public boolean[] dry;
	public boolean[] water;
	public boolean[] pit;

	public boolean[] openSpace;
	
	public Feeling feeling = Feeling.NONE;
	
	public int entrance;
	public int exit;

	public ArrayList<LevelTransition> transitions;

	//when a boss level has become locked.
	public boolean locked = false;
	//magicling, set on the hollow's box pushing floor, see Hero.getCloser
	public boolean onlyBoxMove = false;
	
	public HashSet<Mob> mobs;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>,Blob> blobs;
	public SparseArray<Plant> plants;
	public SparseArray<Trap> traps;
	public ArrayList<CustomTilemap> customTiles;
	public ArrayList<CustomTilemap> customTerrain;
	public ArrayList<CustomTilemap> customWalls;
	
	protected ArrayList<Item> itemsToSpawn = new ArrayList<>();

	protected Group visuals;
	protected Group wallVisuals;
	
	public int color1 = 0x004400;
	public int color2 = 0x88CC44;

	//Matt Edition. Branch floors turn these off to control what they spawn. Branch 0 floors leave them on.
	public boolean canSpawnItems = true;
	public boolean canSpawnGurItems = true;
	public boolean canSpawnFood = true;
	public boolean canFeeling = true;
	public int splashRegionOverride = -1; //loading screen region, -1 means from the depth

	private static final String VERSION     = "version";
	private static final String WIDTH       = "width";
	private static final String HEIGHT      = "height";
	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String TRANSITIONS	= "transitions";
	private static final String LOCKED      = "locked";
	private static final String HEAPS		= "heaps";
	private static final String PLANTS		= "plants";
	private static final String TRAPS       = "traps";
	private static final String CUSTOM_TILES= "customTiles";
	private static final String CUSTOM_TERRAIN= "customTerrain";
	private static final String CUSTOM_WALLS= "customWalls";
	private static final String MOBS		= "mobs";
	private static final String BLOBS		= "blobs";
	private static final String FEELING		= "feeling";

	public void create() {

		mode = com.shatteredpixel.shatteredpixeldungeon.windows.WndModes.current;
		TargetedCell.cells.clear();
		Random.pushGenerator( Dungeon.seedCurDepth() );

		//TODO maybe just make this part of RegularLevel?
		if (!Dungeon.bossLevel() && Dungeon.branch == 0 && !com.shatteredpixel.shatteredpixeldungeon.RemixedLayout.skipsDrops()) {

			if (canSpawnFood) addItemToSpawn(Generator.random(Generator.Category.FOOD));

			if (Dungeon.posNeeded()) {
				Dungeon.LimitedDrops.STRENGTH_POTIONS.count++;
				addItemToSpawn( new PotionOfStrength() );
			}
			if (Dungeon.souNeeded()) {
				Dungeon.LimitedDrops.UPGRADE_SCROLLS.count++;
				//every 2nd scroll of upgrade is removed with forbidden runes challenge on
				//TODO while this does significantly reduce this challenge's levelgen impact, it doesn't quite remove it
				//for 0 levelgen impact, we need to do something like give the player all SOU, but nerf them
				//or give a random scroll (from a separate RNG) instead of every 2nd SOU
				if (!Dungeon.isChallenged(Challenges.NO_SCROLLS) || Dungeon.LimitedDrops.UPGRADE_SCROLLS.count%2 != 0){
					addItemToSpawn(new ScrollOfUpgrade());
				}
			}
			if (Dungeon.asNeeded()) {
				Dungeon.LimitedDrops.ARCANE_STYLI.count++;
				addItemToSpawn( new Stylus() );
			}
			if ( Dungeon.enchStoneNeeded() ){
				Dungeon.LimitedDrops.ENCH_STONE.drop();
				addItemToSpawn( new StoneOfEnchantment() );
			}
			if ( Dungeon.intStoneNeeded() ){
				Dungeon.LimitedDrops.INT_STONE.drop();
				addItemToSpawn( new StoneOfIntuition() );
			}
			if ( Dungeon.trinketCataNeeded() ){
				Dungeon.LimitedDrops.TRINKET_CATA.drop();
				addItemToSpawn( new TrinketCatalyst());
			}
			com.shatteredpixel.shatteredpixeldungeon.GameModes.topUpDrops(this); //half, the drops of skipped floors
			for (Item mi : com.shatteredpixel.shatteredpixeldungeon.MusicImplant.levelDrops()) addItemToSpawn(mi); //musicimplant, torches in the halls
			//rearranged, a bullet belt for each set of five floors
			if ( Dungeon.beltNeeded() ) {
				Dungeon.LimitedDrops.BULLET_BELT.drop();
				addItemToSpawn( new com.shatteredpixel.shatteredpixeldungeon.items.BulletBelt());
			}
			
			if (shouldGenerateLevelFeeling()) {
				feeling = withLevelFeelingEffects( randomLevelFeeling() );
			}
		}
		
		do {
			width = height = length = 0;

			transitions = new ArrayList<>();

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();
			traps = new SparseArray<>();
			customTiles = new ArrayList<>();
			customTerrain = new ArrayList<>();
			customWalls = new ArrayList<>();
			
		} while (!build());

		//levels written for older Shattered, ported from mods, set the entrance and exit cells directly.
		//Give them transitions so stairs, sealing and key locks work the way they do in current levels.
		if (transitions.isEmpty() && (entrance > 0 || exit > 0)) {
			if (entrance > 0) transitions.add( new LevelTransition( this, entrance, LevelTransition.Type.REGULAR_ENTRANCE ) );
			if (exit > 0) transitions.add( new LevelTransition( this, exit, LevelTransition.Type.REGULAR_EXIT ) );
		}
		
		buildFlagMaps();
		cleanWalls();
		
		createMobs();
		createItems();
		com.shatteredpixel.shatteredpixeldungeon.TooCruel.postCreate(this); //toocruel

		Random.popGenerator();
	}
	
	//Abyssalnite's boss levels swap the whole map in the middle of a fight, for example Tengu's maze and arena.
	//The entrance and exit are found again in the new map and given new transitions.
	public void changeMap( int[] newMap ){
		this.map = newMap.clone();
		buildFlagMaps();
		cleanWalls();

		for (Heap heap : heaps.valueList()){
			heap.destroy();
		}

		transitions = new ArrayList<>();
		entrance = exit = 0;
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.ENTRANCE) {
				entrance = i;
				transitions.add( new LevelTransition( this, i, LevelTransition.Type.REGULAR_ENTRANCE ) );
			} else if (map[i] == Terrain.EXIT) {
				exit = i;
				transitions.add( new LevelTransition( this, i, LevelTransition.Type.REGULAR_EXIT ) );
			}
		}

		BArray.setFalse( visited );
		BArray.setFalse( mapped );

		for (Blob blob : blobs.values()) {
			blob.fullyClear();
		}
		addVisuals();

		GameScene.resetMap();
		Dungeon.observe();
	}

	public void setSize(int w, int h){
		
		width = w;
		height = h;
		length = w * h;
		
		map = new int[length];
		Arrays.fill( map, feeling == Level.Feeling.CHASM ? Terrain.CHASM : Terrain.WALL );
		
		visited     = new boolean[length];
		mapped      = new boolean[length];
		
		heroFOV     = new boolean[length];
		
		passable	= new boolean[length];
		losBlocking	= new boolean[length];
		flamable	= new boolean[length];
		secret		= new boolean[length];
		solid		= new boolean[length];
		avoid		= new boolean[length];
		dry			= new boolean[length];
		water		= new boolean[length];
		pit			= new boolean[length];

		openSpace   = new boolean[length];
		
		PathFinder.setMapSize(w, h);
	}
	
	public void reset() {
		
		for (Mob mob : mobs.toArray( new Mob[0] )) {
			if (!mob.reset()) {
				mobs.remove( mob );
			}
		}
		createMobs();
	}

	//Transformation PD split the level feeling roll into hooks so its tower floors can use their own
	protected boolean shouldGenerateLevelFeeling(){
		return Dungeon.depth > 1;
	}

	protected Feeling randomLevelFeeling(){
		//50% chance of getting a level feeling
		//~7.15% chance for each feeling
		switch (Random.Int( 14 )) {
			case 0:
				return Feeling.CHASM;
			case 1:
				return Feeling.WATER;
			case 2:
				return Feeling.GRASS;
			case 3:
				return Feeling.DARK;
			case 4:
				return Feeling.LARGE;
			case 5:
				return Feeling.TRAPS;
			case 6:
				return Feeling.SECRETS;
			default:
				//if-else statements are fine here as only one chance can be above 0 at a time
				// we pre-generate the floats to ensure Random is called consistently
				float mossyChance = Random.Float();
				float trapMechChance = Random.Float();
				if (mossyChance < MossyClump.overrideNormalLevelChance()){
					return MossyClump.getNextFeeling();
				} else if (trapMechChance < TrapMechanism.overrideNormalLevelChance()) {
					return TrapMechanism.getNextFeeling();
				} else {
					return com.shatteredpixel.shatteredpixeldungeon.actors.XunHooks.wornLockFeeling(); //xun, broken lock, NONE without it
				}
		}
	}

	protected Feeling withLevelFeelingEffects( Feeling f ){
		if (f == Feeling.DARK){
			viewDistance = Math.round(5*viewDistance/8f);
			if (com.shatteredpixel.shatteredpixeldungeon.MusicImplant.darkFloorTorch()) addItemToSpawn(new com.shatteredpixel.shatteredpixeldungeon.items.lightsource.FireTorch()); //musicimplant
		} else if (f == Feeling.LARGE){
			addItemToSpawn(Generator.random(Generator.Category.FOOD));
		}
		return f;
	}

	//called once at the start of each hero turn, and when the hero uses a potion, scroll or food (Transformation PD boss arenas)
	public void onHeroTurnStarted( Hero hero ){
	}

	public void onHeroWaited( Hero hero ){
	}

	/**
	 * Selects a safe landing cell for arrivals from another level. Combat respawns
	 * and teleports should continue to use randomRespawnCell. (Transformation PD)
	 */
	public int safeArrivalCell( Char ch ){
		return randomRespawnCell(ch);
	}

	public boolean shouldResetForSafeArrival(){
		return false;
	}

	public int unblessedAnkhSafeRespawnCell( Char ch ){
		return -1;
	}

	public void onSealedResurrectionReset(){
	}

	public void onBeforeSealedResurrectionReset(){
	}

	public void onHeroConsumableUsed( Hero hero, Item item ){
	}

	public void updateLevelMusic( float elapsed ){
	}

	public void playLevelMusic(){
		//do nothing by default
	}

	//magicling, Magic Ling's boss arenas move the hero on once a chest or heap at heappos is emptied
	public void teleportHeroIfHeapEmpty( Char ch, int targetPos, int heappos ) {
		com.shatteredpixel.shatteredpixeldungeon.items.Heap heap = heaps.get( heappos );
		if (heap == null || heap.isEmpty()) {
			com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear( ch, targetPos );
			if (ch instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) {
				((com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero) ch).interrupt();
			}
			Dungeon.observe();
			GameScene.updateFog();
		}
	}

	//magicling, Magic Ling's boss levels pick their fight music here, GameScene.bossReady calls it. By default the stage's Shattered boss track
	public void playBossMusic(){
		String track;
		if (Dungeon.depth <= 5)       track = Assets.Music.SEWERS_BOSS;
		else if (Dungeon.depth <= 10) track = Assets.Music.PRISON_BOSS;
		else if (Dungeon.depth <= 15) track = Assets.Music.CAVES_BOSS;
		else if (Dungeon.depth <= 20) track = Assets.Music.CITY_BOSS;
		else                          track = Assets.Music.HALLS_BOSS;
		com.watabou.noosa.audio.Music.INSTANCE.play( track, true );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {

		version = bundle.getInt( VERSION );
		modWorld = bundle.contains( "mod_world" ) ? bundle.getInt( "mod_world" ) : -1;
		wave = bundle.getInt( "tower_wave" );
		mode = com.shatteredpixel.shatteredpixeldungeon.windows.WndModes.Modes.values()[bundle.getInt( "tower_mode" )];
		
		//saves from before v3.1.1 are not supported
		if (version < ShatteredPixelDungeon.v3_1_1){
			throw new RuntimeException("old save");
		}

		setSize( bundle.getInt(WIDTH), bundle.getInt(HEIGHT));
		
		mobs = new HashSet<>();
		heaps = new SparseArray<>();
		blobs = new HashMap<>();
		plants = new SparseArray<>();
		traps = new SparseArray<>();
		customTiles = new ArrayList<>();
		customTerrain = new ArrayList<>();
		customWalls = new ArrayList<>();
		
		map		= bundle.getIntArray( MAP );

		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );

		transitions = new ArrayList<>();
		for (Bundlable b : bundle.getCollection( TRANSITIONS )){
			transitions.add((LevelTransition) b);
		}

		locked      = bundle.getBoolean( LOCKED );
		
		Collection<Bundlable> collection = bundle.getCollection( HEAPS );
		for (Bundlable h : collection) {
			Heap heap = (Heap)h;
			if (!heap.isEmpty())
				heaps.put( heap.pos, heap );
		}
		
		collection = bundle.getCollection( PLANTS );
		for (Bundlable p : collection) {
			Plant plant = (Plant)p;
			plants.put( plant.pos, plant );
		}

		collection = bundle.getCollection( TRAPS );
		for (Bundlable p : collection) {
			Trap trap = (Trap)p;
			traps.put( trap.pos, trap );
		}

		collection = bundle.getCollection( CUSTOM_TILES );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customTiles.add(vis);
		}

		collection = bundle.getCollection( CUSTOM_TERRAIN );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customTerrain.add(vis);
		}

		collection = bundle.getCollection( CUSTOM_WALLS );
		for (Bundlable p : collection) {
			CustomTilemap vis = (CustomTilemap)p;
			customWalls.add(vis);
		}
		
		collection = bundle.getCollection( MOBS );
		for (Bundlable m : collection) {
			Mob mob = (Mob)m;
			if (mob != null) {
				mobs.add( mob );
			}
		}
		
		collection = bundle.getCollection( BLOBS );
		for (Bundlable b : collection) {
			Blob blob = (Blob)b;
			blobs.put( blob.getClass(), blob );
		}

		feeling = bundle.getEnum( FEELING, Feeling.class );
		if (feeling == Feeling.DARK) {
			viewDistance = Math.round(5 * viewDistance / 8f);
		}

		if (bundle.contains( "mobs_to_spawn" )) {
			for (Class<? extends Mob> mob : bundle.getClassArray("mobs_to_spawn")) {
				if (mob != null) mobsToSpawn.add(mob);
			}
		}

		if (bundle.contains( "respawner" )){
			respawner = (MobSpawner) bundle.get("respawner");
		}

		TargetedCell.cells.clear();
		if (bundle.contains( "targeted_cells" )){
			collection = bundle.getCollection( "targeted_cells" );
			for (Bundlable c : collection) {
				TargetedCell cell = (TargetedCell)c;
				if (cell != null) {
					TargetedCell.cells.put(cell.pos, cell);
				}
			}
		}

		buildFlagMaps();
		cleanWalls();

	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( VERSION, Game.versionCode );
		bundle.put( "mod_world", modWorld );
		if (wave != 0) bundle.put( "tower_wave", wave );
		if (mode != com.shatteredpixel.shatteredpixeldungeon.windows.WndModes.Modes.NORMAL) bundle.put( "tower_mode", mode.ordinal() );
		bundle.put( WIDTH, width );
		bundle.put( HEIGHT, height );
		bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		bundle.put( TRANSITIONS, transitions );
		bundle.put( LOCKED, locked );
		bundle.put( HEAPS, heaps.valueList() );
		bundle.put( PLANTS, plants.valueList() );
		bundle.put( TRAPS, traps.valueList() );
		bundle.put( CUSTOM_TILES, customTiles );
		bundle.put( CUSTOM_TERRAIN, customTerrain);
		bundle.put( CUSTOM_WALLS, customWalls );
		bundle.put( MOBS, mobs );
		bundle.put( BLOBS, blobs.values() );
		bundle.put( FEELING, feeling );
		bundle.put( "mobs_to_spawn", mobsToSpawn.toArray(new Class[0]));
		bundle.put( "respawner", respawner );
		bundle.put( "targeted_cells", TargetedCell.cells.valueList() );
	}
	
	public int tunnelTile() {
		return feeling == Feeling.CHASM ? Terrain.EMPTY_SP : Terrain.EMPTY;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int length() {
		return length;
	}
	
	public String tilesTex() {
		return null;
	}
	
	public String waterTex() {
		return null;
	}
	
	//terrain Devoted Pixel Dungeon treats as dry ground, which some of its monsters need to dig or charge
	public static boolean dryTerrain( int terrain ){
		return terrain == Terrain.EMPTY || terrain == Terrain.GRASS || terrain == Terrain.ENTRANCE
				|| terrain == Terrain.EMBERS || terrain == Terrain.HIGH_GRASS;
	}

	abstract protected boolean build();
	
	private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();
	
	public Mob createMob() {
		if (mobsToSpawn == null || mobsToSpawn.isEmpty()) {
			//ported worlds can bring their own mod's monsters, see WorldShuffle
			mobsToSpawn = WorldShuffle.mobRotation(this, com.shatteredpixel.shatteredpixeldungeon.TooCruel.mobDepth(Dungeon.depth)); //toocruel, Invasion
			//Community PD has no worlds, its monsters join any floor (level feelings, rare alts)
			com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CommunitySpawner.adjust(mobsToSpawn, this);
			com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.adjust(mobsToSpawn, this); //trap dungeon game mode
		}

		Mob m = Reflection.newInstance(mobsToSpawn.remove(0));
		ChampionEnemy.rollForChampion(m);
		if (m.buff(ChampionEnemy.class) == null) com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAQuestEnemy.rollForQuest(m); //bluearchive
		com.shatteredpixel.shatteredpixeldungeon.RBAHooks.rollForQuest(m); //rebluearchive
		return m;
	}

	abstract protected void createMobs();

	abstract protected void createItems();

	public int entrance(){
		LevelTransition l = getTransition(null);
		if (l != null){
			return l.cell();
		}
		return 0;
	}

	public int exit(){
		LevelTransition l = getTransition(LevelTransition.Type.REGULAR_EXIT);
		if (l != null){
			return l.cell();
		}
		return 0;
	}

	public LevelTransition getTransition(LevelTransition.Type type){
		return getTransition(type, null);
	}

	//Matt Edition. With no target id, plain (unnamed) transitions are preferred and named ones are a fallback.
	//With a target id, the transition with that id is returned, preferring one of the right type.
	public LevelTransition getTransition(LevelTransition.Type type, String targetId){
		syncLegacyTransitions();
		if (transitions.isEmpty()){
			return null;
		}
		if (targetId == null) {
			for (int pass = 0; pass < 2; pass++) {
				for (LevelTransition transition : transitions) {
					if (pass == 0 && transition.transitionId != null) continue;
					//if we don't specify a type, prefer to return any entrance
					if (type == null &&
							(transition.type == LevelTransition.Type.REGULAR_ENTRANCE
									|| transition.type == LevelTransition.Type.BRANCH_ENTRANCE
									|| transition.type == LevelTransition.Type.SURFACE)) {
						return transition;
					} else if (transition.type == type) {
						return transition;
					}
				}
			}
			return type != null ? getTransition(null, null) : transitions.get(0);
		}
		for (LevelTransition transition : transitions) {
			if (transition.type == type && targetId.equals(transition.transitionId)) {
				return transition;
			}
		}
		for (LevelTransition transition : transitions) {
			if (targetId.equals(transition.transitionId)) {
				return transition;
			}
		}
		throw new RuntimeException("No transition with id '" + targetId + "' on depth " + Dungeon.depth + " branch " + Dungeon.branch);
	}

	//levels from mods on older Shattered set the entrance and exit cells directly, and their multi phase boss
	//floors change them mid fight (a boss floor's exit only appears once the boss dies). The build time
	//conversion in create misses those, so a missing one is added whenever transitions are looked up
	private void syncLegacyTransitions(){
		if (transitions == null) return;
		boolean hasEntrance = false, hasExit = false;
		for (LevelTransition t : transitions){
			if (t.type == LevelTransition.Type.REGULAR_EXIT) hasExit = true;
			if (t.type == LevelTransition.Type.REGULAR_ENTRANCE || t.type == LevelTransition.Type.BRANCH_ENTRANCE
					|| t.type == LevelTransition.Type.SURFACE) hasEntrance = true;
		}
		if (!hasEntrance && entrance > 0 && entrance < length()) transitions.add( new LevelTransition( this, entrance, LevelTransition.Type.REGULAR_ENTRANCE ) );
		if (!hasExit && exit > 0 && exit < length()) transitions.add( new LevelTransition( this, exit, LevelTransition.Type.REGULAR_EXIT ) );
	}

	public LevelTransition getTransition(int cell){
		syncLegacyTransitions();
		for (LevelTransition transition : transitions){
			if (transition.inside(cell)){
				return transition;
			}
		}
		return blockTransition(cell);
	}

	//levels from older mods paint a block of stair tiles but only name one cell as the entrance or exit,
	//so any tile of that block uses the transition that sits in the same block
	private LevelTransition blockTransition(int cell){
		if (cell < 0 || cell >= length()) return null;
		int tile = map[cell];
		if (tile != Terrain.EXIT && tile != Terrain.UNLOCKED_EXIT && tile != Terrain.ENTRANCE && tile != Terrain.ENTRANCE_SP) return null;
		boolean[] seen = new boolean[length()];
		java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
		queue.add(cell);
		seen[cell] = true;
		int searched = 0;
		while (!queue.isEmpty() && searched++ < 64){
			int c = queue.poll();
			for (LevelTransition transition : transitions){
				if (transition.inside(c)) return transition;
			}
			for (int n : PathFinder.NEIGHBOURS8){
				int next = c + n;
				if (next < 0 || next >= length() || seen[next] || map[next] != tile) continue;
				if (Math.abs(next % width() - c % width()) > 1) continue;
				seen[next] = true;
				queue.add(next);
			}
		}
		return null;
	}

	//returns true if we immediately transition, false otherwise
	public boolean activateTransition(Hero hero, LevelTransition transition){
		if (locked){
			return false;
		}
		if (!com.shatteredpixel.shatteredpixeldungeon.TooCruel.canTransition(this, hero, transition)) return false; //toocruel

		beforeTransition();
		InterlevelScene.curTransition = transition;
		if (transition.type == LevelTransition.Type.REGULAR_EXIT
				|| transition.type == LevelTransition.Type.BRANCH_EXIT) {
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		} else {
			InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
		}
		Game.switchScene(InterlevelScene.class);
		return true;
	}

	//some buff effects have special logic or are cancelled from the hero before transitioning levels
	public static void beforeTransition(){
		com.shatteredpixel.shatteredpixeldungeon.TooCruel.beforeTransition(); //toocruel, Thoughtless

		//time freeze effects need to resolve their pressed cells before transitioning
		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (timeFreeze != null) timeFreeze.disarmPresses();
		Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
		if (timeBubble != null) timeBubble.disarmPresses();

		//iron stomach and challenge arena do not persist between floors
		Talent.WarriorFoodImmunity foodImmune = Dungeon.hero.buff(Talent.WarriorFoodImmunity.class);
		if (foodImmune != null) foodImmune.detach();
		ScrollOfChallenge.ChallengeArena arena = Dungeon.hero.buff(ScrollOfChallenge.ChallengeArena.class);
		if (arena != null) arena.detach();
		//awareness also doesn't, honestly it's weird that it's a buff
		Awareness awareness = Dungeon.hero.buff(Awareness.class);
		if (awareness != null) awareness.detach();

		Char ally = Stasis.getStasisAlly();
		if (Char.hasProp(ally, Char.Property.IMMOVABLE)){
			Dungeon.hero.buff(Stasis.StasisBuff.class).act();
			GLog.w(Messages.get(Stasis.StasisBuff.class, "left_behind"));
		}

		//spend the hero's partial turns,  so the hero cannot take partial turns between floors
		Dungeon.hero.spendToWhole();
		for (Actor a : Actor.all()){
			//also adjust any other actors that are now ahead of the hero due to this
			if (a.cooldown() < Dungeon.hero.cooldown()){
				a.spendToWhole();
			}
		}
	}

	//from Transformation PD, bosses that were sealed in once seal the floor again after their death. Nothing to do here.
	public void reunseal(){}
	public void reseal(){}

	public void seal(){
		if (!locked) {
			locked = true;
			Buff.affect(Dungeon.hero, LockedFloor.class);
		}
	}

	public void unseal(){
		if (locked) {
			locked = false;
			if (Dungeon.hero.buff(LockedFloor.class) != null){
				Dungeon.hero.buff(LockedFloor.class).detach();
			}
		}
	}

	public ArrayList<Item> getItemsToPreserveFromSealedResurrect(){
		ArrayList<Item> items = new ArrayList<>();
		for (Heap h : heaps.valueList()){
			if (h.type == Heap.Type.HEAP) {
				for (Item i : h.items){
					if (i instanceof Bomb){
						((Bomb) i).fuse = null;
					}
					items.add(i);
				}
			}
		}
		for (Mob m : mobs){
			for (PinCushion b : m.buffs(PinCushion.class)){
				items.addAll(b.getStuckItems());
			}
		}
		for (HeavyBoomerang.CircleBack b : Dungeon.hero.buffs(HeavyBoomerang.CircleBack.class)){
			if (b.activeDepth() == Dungeon.depth) items.add(b.cancel());
		}
		return items;
	}

	public Group addVisuals() {
		if (visuals == null || visuals.parent == null){
			visuals = new Group();
		} else {
			visuals.clear();
			visuals.camera = null;
		}
		for (int i=0; i < length(); i++) {
			if (pit[i]) {
				visuals.add( new WindParticle.Wind( i ) );
				if (i >= width() && water[i-width()]) {
					visuals.add( new FlowParticle.Flow( i - width() ) );
				}
			}
		}
		return visuals;
	}

	//for visual effects that should render above wall overhang tiles
	public Group addWallVisuals(){
		if (wallVisuals == null || wallVisuals.parent == null){
			wallVisuals = new Group();
		} else {
			wallVisuals.clear();
			wallVisuals.camera = null;
		}
		return wallVisuals;
	}

	
	public int mobLimit() {
		return 0;
	}

	public int mobCount(){
		float count = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && !mob.properties().contains(Char.Property.MINIBOSS)) {
				count += mob.spawningWeight();
			}
		}
		return Math.round(count);
	}

	public Mob findMob( int pos ){
		for (Mob mob : mobs){
			if (mob.pos == pos){
				return mob;
			}
		}
		return null;
	}

	protected MobSpawner respawner;

	public Actor addRespawner() {
		if (respawner == null){
			respawner = new MobSpawner();
			Actor.addDelayed(respawner, respawnCooldown());
		} else {
			Actor.add(respawner);
			if (respawner.cooldown() > respawnCooldown()){
				respawner.resetCooldown();
			}
		}
		return respawner;
	}

	public float respawnCooldown(){
		float cooldown;
		if (Statistics.amuletObtained){
			if (Dungeon.depth == 1){
				//very fast spawns on floor 1! 0/2/4/6/8/10/12, etc.
				cooldown = (Dungeon.level.mobCount()) * (TIME_TO_RESPAWN / 25f);
			} else {
				//respawn time is 5/5/10/15/20/25/25, etc.
				cooldown = Math.round(GameMath.gate( TIME_TO_RESPAWN/10f, Dungeon.level.mobCount() * (TIME_TO_RESPAWN / 10f), TIME_TO_RESPAWN / 2f));
			}
		} else if (Dungeon.level.feeling == Feeling.DARK){
			cooldown = 2*TIME_TO_RESPAWN/3f;
		} else {
			cooldown = TIME_TO_RESPAWN;
		}
		//Pixel Dungeon Reforged, the Horde challenge brings wandering monsters twice as often
		if (Dungeon.isChallenged(Challenges.HORDE)) cooldown /= 2f;
		cooldown = com.shatteredpixel.shatteredpixeldungeon.TooCruel.respawnCooldown(this, cooldown); //toocruel
		return cooldown * Dungeon.respawnBoost * com.shatteredpixel.shatteredpixeldungeon.RBAHooks.respawnFactor() / DimensionalSundial.spawnMultiplierAtCurrentTime(); //rebluearchive, Mari
	}

	//from Shittened PD, spawns a given mob far from the hero
	public boolean spawnMob(int disLimit, Mob mob){
		PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.or(passable, avoid, null));

		mob.state = mob.WANDERING;
		int tries = 30;
		do {
			mob.pos = randomRespawnCell(mob);
			tries--;
		} while ((mob.pos == -1 || PathFinder.distance[mob.pos] < disLimit) && tries > 0);

		if (Dungeon.hero.isAlive() && mob.pos != -1 && PathFinder.distance[mob.pos] >= disLimit) {
			GameScene.add( mob );
			if (!mob.buffs(ChampionEnemy.class).isEmpty()){
				GLog.w(Messages.get(ChampionEnemy.class, "warn"));
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean spawnMob(int disLimit){
		PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.or(passable, avoid, null));

		Mob mob = createMob();
		if (mob.state != mob.PASSIVE) {
			mob.state = mob.WANDERING;
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.lullaby(mob); //xun
		}
		int tries = 30;
		do {
			mob.pos = randomRespawnCell(mob);
			tries--;
		} while ((mob.pos == -1 || PathFinder.distance[mob.pos] < disLimit) && tries > 0);

		if (Dungeon.hero.isAlive() && mob.pos != -1 && PathFinder.distance[mob.pos] >= disLimit) {
			GameScene.add( mob );
			if (!mob.buffs(ChampionEnemy.class).isEmpty()){
				GLog.w(Messages.get(ChampionEnemy.class, "warn"));
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int randomRespawnCell( Char ch ) {
		int cell;
		int count = 0;
		do {

			if (++count > 30) {
				return -1;
			}

			cell = Random.Int( length() );

		} while ((Dungeon.level == this && heroFOV[cell])
				|| !passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar( cell ) != null);
		return cell;
	}
	
	public int randomDestination( Char ch ) {
		int cell;
		do {
			cell = Random.Int( length() );
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell]));
		return cell;
	}
	
	public void addItemToSpawn( Item item ) {
		if (com.shatteredpixel.shatteredpixeldungeon.LoreGoo.noItems()) return; //lore
		if (item != null) {
			itemsToSpawn.add( item );
		}
	}

	public Item findPrizeItem(){ return findPrizeItem(null); }

	public Item findPrizeItem(Class<?extends Item> match){
		if (itemsToSpawn.size() == 0)
			return null;

		if (match == null){
			//if we have a trinket catalyst, always return that first
			for (Item item : itemsToSpawn){
				if (item instanceof TrinketCatalyst){
					itemsToSpawn.remove(item);
					return item;
				}
			}

			Item item = Random.element(itemsToSpawn);
			itemsToSpawn.remove(item);
			return item;
		}

		for (Item item : itemsToSpawn){
			if (match.isInstance(item)){
				itemsToSpawn.remove( item );
				return item;
			}
		}

		return null;
	}

	public void buildFlagMaps() {
		
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			passable[i]     = (flags & Terrain.PASSABLE) != 0;
			losBlocking[i]  = (flags & Terrain.LOS_BLOCKING) != 0;
			flamable[i]     = (flags & Terrain.FLAMABLE) != 0;
			secret[i]       = (flags & Terrain.SECRET) != 0;
			solid[i]        = (flags & Terrain.SOLID) != 0;
			avoid[i]        = (flags & Terrain.AVOID) != 0;
			dry[i]          = dryTerrain(map[i]);
			water[i]        = (flags & Terrain.LIQUID) != 0;
			pit[i]          = (flags & Terrain.PIT) != 0;
		}

		for (Blob b : blobs.values()){
			b.onBuildFlagMaps(this);
		}
		
		int lastRow = length() - width();
		for (int i=0; i < width(); i++) {
			passable[i] = avoid[i] = false;
			losBlocking[i] = solid[i] = true;
			passable[lastRow + i] = avoid[lastRow + i] = false;
			losBlocking[lastRow + i] = solid[lastRow + i] = true;
		}
		for (int i=width(); i < lastRow; i += width()) {
			passable[i] = avoid[i] = false;
			losBlocking[i] = solid[i] = true;
			passable[i + width()-1] = avoid[i + width()-1] = false;
			losBlocking[i + width()-1] = solid[i + width()-1] = true;
		}

		//an open space is large enough to fit large mobs. A space is open when it is not solid
		// and there is an open corner with both adjacent cells opens
		for (int i=0; i < length(); i++) {
			if (solid[i]){
				openSpace[i] = false;
			} else {
				for (int j = 1; j < PathFinder.CIRCLE8.length; j += 2){
					if (solid[i+PathFinder.CIRCLE8[j]]) {
						openSpace[i] = false;
					} else if (!solid[i+PathFinder.CIRCLE8[(j+1)%8]]
							&& !solid[i+PathFinder.CIRCLE8[(j+2)%8]]){
						openSpace[i] = true;
						break;
					}
				}
			}
		}

	}

	//updates open space both on the cell itself and adjacent cells
	public void updateOpenSpace(int cell){
		for (int i : PathFinder.NEIGHBOURS9) {
			if (insideMap(cell + i)) {
				if (solid[cell + i]) {
					openSpace[cell + i] = false;
				} else {
					for (int j = 1; j < PathFinder.CIRCLE8.length; j += 2) {
						if (solid[cell + i + PathFinder.CIRCLE8[j]]) {
							openSpace[cell + i] = false;
						} else if (!solid[cell + i + PathFinder.CIRCLE8[(j + 1) % 8]]
								&& !solid[cell + i + PathFinder.CIRCLE8[(j + 2) % 8]]) {
							openSpace[cell + i] = true;
							break;
						}
					}
				}
			}
		}
	}

	public void destroy( int pos ) {
		//if raw tile type is flammable or empty
		int terr = map[pos];
		if (terr == Terrain.EMPTY || terr == Terrain.EMPTY_DECO
				|| (Terrain.flags[map[pos]] & Terrain.FLAMABLE) != 0) {
			set(pos, Terrain.EMBERS);
		}
		Blob web = blobs.get(Web.class);
		if (web != null){
			web.clear(pos);
		}
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero.onLevelEnter(); //bluearchive, Yuzu's game start, the mod put it where terrain is destroyed
	}

	//Transformation PD, bombs knock down region decorations
	public boolean destroyRegionDeco(int cell) {
		if (!insideMap(cell) || this instanceof SewerLevel) return false;
		if (map[cell] != Terrain.REGION_DECO && map[cell] != Terrain.REGION_DECO_ALT) {
			return false;
		}
		set(cell, Terrain.EMPTY_DECO, this);
		return true;
	}

	public void cleanWalls() {
		if (discoverable == null || discoverable.length != length) {
			discoverable = new boolean[length()];
		}

		for (int i=0; i < length(); i++) {
			
			boolean d = false;
			
			for (int j=0; j < PathFinder.NEIGHBOURS9.length; j++) {
				int n = i + PathFinder.NEIGHBOURS9[j];
				if (n >= 0 && n < length() && map[n] != Terrain.WALL && map[n] != Terrain.WALL_DECO) {
					d = true;
					break;
				}
			}
			
			discoverable[i] = d;
		}
	}
	
	public static void set( int cell, int terrain ){
		set( cell, terrain, Dungeon.level );
	}
	
	public static void set( int cell, int terrain, Level level ) {
		Painter.set(level, cell, terrain);

		if (terrain != Terrain.TRAP && terrain != Terrain.SECRET_TRAP && terrain != Terrain.INACTIVE_TRAP) {
			level.traps.remove(cell);
		}

		level.updateCellFlags(cell);
	}

	public void updateCellFlags( int cell ){
		int terrain = map[cell];

		int flags = Terrain.flags[terrain];
		passable[cell]      = (flags & Terrain.PASSABLE) != 0;
		losBlocking[cell]   = (flags & Terrain.LOS_BLOCKING) != 0;
		flamable[cell]      = (flags & Terrain.FLAMABLE) != 0;
		secret[cell]        = (flags & Terrain.SECRET) != 0;
		solid[cell]         = (flags & Terrain.SOLID) != 0;
		avoid[cell]         = (flags & Terrain.AVOID) != 0;
		dry[cell]           = dryTerrain(map[cell]);
		pit[cell]           = (flags & Terrain.PIT) != 0;
		water[cell]         = terrain == Terrain.WATER;

		if (this instanceof SewerLevel){
			if (map[cell] == Terrain.REGION_DECO || map[cell] == Terrain.REGION_DECO_ALT){
				flamable[cell] = true;
			}
		}

		for (Blob b : blobs.values()){
			b.onUpdateCellFlags(this, cell);
		}

		updateOpenSpace(cell);
	}
	
	//Tower PD, drops each item on a random cell of the list
	public void dropMany(ArrayList<Integer> candidates, Item... items ) {
		dropMany(Heap.Type.HEAP, candidates, items);
	}
	public void dropMany(Heap.Type heaptype, ArrayList<Integer> candidates, Item... items ) {
		if (!candidates.isEmpty()) for (Item item : items){
			drop(item, Random.element(candidates)).type = heaptype;
		}
	}

	public Heap drop( Item item, int cell ) {

		if (item == null || Challenges.isItemBlocked(item)){

			//create a dummy heap, give it a dummy sprite, don't add it to the game, and return it.
			//effectively nullifies whatever the logic calling this wants to do, including dropping items.
			Heap heap = new Heap();
			ItemSprite sprite = heap.sprite = new ItemSprite();
			sprite.link(heap);
			return heap;

		}
		
		Heap heap = heaps.get( cell );
		if (heap == null) {
			
			heap = new Heap();
			heap.seen = Dungeon.level == this && heroFOV[cell];
			heap.pos = cell;
			heap.drop(item);
			if (map[cell] == Terrain.CHASM || (Dungeon.level != null && pit[cell])) {
				Dungeon.dropToChasm( item );
				GameScene.discard( heap );
			} else {
				heaps.put( cell, heap );
				GameScene.add( heap );
			}
			
		} else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST
				|| heap.type == Heap.Type.EBONY_CHEST || heap.type == Heap.Type.HOWARD_CHEST
				|| heap.type == Heap.Type.EVAN_CHEST || heap.type == Heap.Type.GOLD_CHEST) {
			
			int n;
			do {
				n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!passable[n] && !avoid[n]);
			return drop( item, n );
			
		} else {
			heap.drop(item);
		}
		
		if (Dungeon.level != null && com.watabou.noosa.Game.instance != null && ShatteredPixelDungeon.scene() instanceof GameScene) {
			pressCell( cell );
		}
		
		return heap;
	}
	
	public Plant plant( Plant.Seed seed, int pos ) {

		Plant plant = plants.get( pos );
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == Terrain.HIGH_GRASS ||
				map[pos] == Terrain.FURROWED_GRASS ||
				map[pos] == Terrain.EMPTY ||
				map[pos] == Terrain.EMBERS ||
				map[pos] == Terrain.EMPTY_DECO) {
			set(pos, Terrain.GRASS, this);
			GameScene.updateMap(pos);
		}

		//we have to get this far as grass placement has RNG implications in levelgen
		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)){
			return null;
		}
		
		plant = seed.couch( pos, this );
		plants.put( pos, plant );
		
		GameScene.plantSeed( pos );

		//rearranged, the Adventurer's Root talent
		Char raRooted = Actor.findChar(pos);
		if (raRooted != null && raRooted.alignment == Char.Alignment.ENEMY && Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.ROOT)) {
			Buff.affect(raRooted, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots.class, 1+Dungeon.hero.pointsInTalent(Talent.ROOT));
		}

		for (Char ch : Actor.chars()){
			if (ch instanceof WandOfRegrowth.Lotus
					&& ((WandOfRegrowth.Lotus) ch).inRange(pos)
					&& Actor.findChar(pos) != null){
				plant.trigger();
				return null;
			}
		}
		
		return plant;
	}
	
	public void uproot( int pos ) {
		plants.remove(pos);
		GameScene.updateMap( pos );
	}

	public Trap setTrap( Trap trap, int pos ){
		Trap existingTrap = traps.get(pos);
		if (existingTrap != null){
			traps.remove( pos );
		}
		trap.set( pos );
		traps.put( pos, trap );
		GameScene.updateMap( pos );
		return trap;
	}

	public void disarmTrap( int pos ) {
		set(pos, Terrain.INACTIVE_TRAP);
		GameScene.updateMap(pos);
	}

	public void discover( int cell ) {
		set( cell, Terrain.discover( map[cell] ) );
		Trap trap = traps.get( cell );
		if (trap != null)
			trap.reveal();
		GameScene.updateMap( cell );
	}

	public boolean setCellToWater( boolean includeTraps, int cell ){
		Point p = cellToPoint(cell);

		//if a custom tilemap is over that cell, check if it allows water
		for (CustomTilemap cust : customTiles){
			Point custPoint = new Point(p);
			custPoint.x -= cust.tileX;
			custPoint.y -= cust.tileY;
			if (custPoint.x >= 0 && custPoint.y >= 0
					&& custPoint.x < cust.tileW && custPoint.y < cust.tileH){
				if (!cust.allowWater(custPoint.x, custPoint.y)){
					return false;
				}
			}
		}

		int terr = map[cell];
		if (terr == Terrain.EMPTY || terr == Terrain.GRASS ||
				terr == Terrain.EMBERS || terr == Terrain.EMPTY_SP ||
				terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS
				|| terr == Terrain.EMPTY_DECO){
			set(cell, Terrain.WATER);
			GameScene.updateMap(cell);
			return true;
		} else if (includeTraps && (terr == Terrain.SECRET_TRAP ||
				terr == Terrain.TRAP || terr == Terrain.INACTIVE_TRAP || terr == Terrain.WIRE)){
			set(cell, Terrain.WATER);
			Dungeon.level.traps.remove(cell);
			GameScene.updateMap(cell);
			return true;
		}

		return false;
	}
	
	public int fallCell( boolean fallIntoPit ) {
		int result;
		do {
			result = randomRespawnCell( null );
			if (result == -1) return -1;
		} while (traps.get(result) != null
				|| findMob(result) != null);
		return result;
	}
	
	public void occupyCell( Char ch ){
		if (!ch.isImmune(Web.class) && Blob.volumeAt(ch.pos, Web.class) > 0){
			blobs.get(Web.class).clear(ch.pos);
			Web.affectChar( ch );
		}

		if (Blob.volumeAt(ch.pos, SacrificialFire.class) > 0 && ch.buff( SacrificialFire.Marked.class ) == null){
			if (Dungeon.level.heroFOV[ch.pos]) {
				CellEmitter.get(ch.pos).burst( SacrificialParticle.FACTORY, 5 );
			}
			Buff.prolong( ch, SacrificialFire.Marked.class, SacrificialFire.Marked.DURATION );
		}

		if (!ch.flying){

			//we call act here instead of detach in case the debuffs haven't managed to deal dmg once yet
			if (map[ch.pos] == Terrain.WATER){
				if (ch.buff(Burning.class) != null){
					if (ch == Dungeon.hero) com.shatteredpixel.shatteredpixeldungeon.Badges.validateXunUnlock(com.shatteredpixel.shatteredpixeldungeon.Badges.Badge.UNLOCK_EXPLORER); //xun
					ch.buff(Burning.class).act();
				}
				if (ch.buff(Ooze.class) != null){
					if (ch == Dungeon.hero) com.shatteredpixel.shatteredpixeldungeon.Badges.validateXunUnlock(com.shatteredpixel.shatteredpixeldungeon.Badges.Badge.UNLOCK_EXPLORER); //xun
					ch.buff(Ooze.class).act();
				}
			}

			if ( (map[ch.pos] == Terrain.GRASS || map[ch.pos] == Terrain.EMBERS)
					&& ch == Dungeon.hero && Dungeon.hero.hasTalent(Talent.REJUVENATING_STEPS)
					&& ch.buff(Talent.RejuvenatingStepsCooldown.class) == null){

				if (!Regeneration.regenOn()){
					set(ch.pos, Terrain.FURROWED_GRASS);
				} else if (ch.buff(Talent.RejuvenatingStepsFurrow.class) != null && ch.buff(Talent.RejuvenatingStepsFurrow.class).count() >= 200) {
					set(ch.pos, Terrain.FURROWED_GRASS);
				} else {
					set(ch.pos, Terrain.HIGH_GRASS);
					Buff.count(ch, Talent.RejuvenatingStepsFurrow.class, 3 - Dungeon.hero.pointsInTalent(Talent.REJUVENATING_STEPS));
				}
				GameScene.updateMap(ch.pos);
				Buff.affect(ch, Talent.RejuvenatingStepsCooldown.class, 15f - 5f*Dungeon.hero.pointsInTalent(Talent.REJUVENATING_STEPS));
			}
			
			if (pit[ch.pos]){
				if (ch == Dungeon.hero) {
					Chasm.heroFall(ch.pos);
				} else if (ch instanceof Mob) {
					Chasm.mobFall( (Mob)ch );
				}
				return;
			}
			
			//characters which are not the hero or a sheep 'soft' press cells
			pressCell( ch.pos, ch instanceof Hero || ch instanceof Sheep || com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.pressesHard(ch)); //xun, Sensitive Pedal
		} else {
			if (map[ch.pos] == Terrain.DOOR){
				Door.enter( ch.pos );
			}
		}

		if (ch.isAlive() && ch instanceof Piranha && !water[ch.pos]){
			((Piranha) ch).dieOnLand();
		}
	}
	
	//public method for forcing the hard press of a cell. e.g. when an item lands on it
	public void pressCell( int cell ){
		pressCell( cell, true );
	}
	
	//a 'soft' press ignores hidden traps
	//a 'hard' press triggers all things
	private void pressCell( int cell, boolean hard ) {

		Trap trap = null;
		
		switch (map[cell]) {
		
		case Terrain.SECRET_TRAP:
			if (hard) {
				trap = traps.get( cell );
				GLog.i(Messages.get(Level.class, "hidden_trap", trap.name()));
			}
			break;
			
		case Terrain.TRAP:
			trap = traps.get( cell );
			break;
			
		case Terrain.HIGH_GRASS:
		case Terrain.FURROWED_GRASS:
			HighGrass.trample( this, cell);
			break;
			
		case Terrain.WELL:
			WellWater.affectCell( cell );
			break;

		case Terrain.EMPTY_WELL:
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.digTheWell( cell ); //xun
			break;
			
		case Terrain.DOOR:
			Door.enter( cell );
			break;

		case Terrain.WIRE: //rearranged
			com.shatteredpixel.shatteredpixeldungeon.levels.features.Wire.trample( this, cell);
			break;
		}

		TimekeepersHourglass.timeFreeze timeFreeze =
				Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);

		Swiftthistle.TimeBubble bubble =
				Dungeon.hero.buff(Swiftthistle.TimeBubble.class);

		if (trap != null) {
			if (bubble != null){
				Sample.INSTANCE.play(Assets.Sounds.TRAP);
				discover(cell);
				bubble.setDelayedPress(cell);
				
			} else if (timeFreeze != null){
				Sample.INSTANCE.play(Assets.Sounds.TRAP);
				discover(cell);
				timeFreeze.setDelayedPress(cell);
				
			} else {
				if (Dungeon.hero.pos == cell) {
					Dungeon.hero.interrupt();
				}
				trap.trigger();

			}
		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			if (bubble != null){
				Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				bubble.setDelayedPress(cell);

			} else if (timeFreeze != null){
				Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				timeFreeze.setDelayedPress(cell);

			} else {
				plant.trigger();

			}
		}

		if (hard && Blob.volumeAt(cell, Web.class) > 0){
			blobs.get(Web.class).clear(cell);
		}
	}

	private static boolean[] heroMindFov;

	private static boolean[] modifiableBlocking;

	public void updateFieldOfView( Char c, boolean[] fieldOfView ) {

		int cx = c.pos % width();
		int cy = c.pos / width();
		
		//rearranged, a Samurai with her blade sheathed fights by feel and not by sight
		boolean sighted = c.buff( com.shatteredpixel.shatteredpixeldungeon.items.Sheath.Sheathing.class ) == null
				&& c.buff( Blindness.class ) == null && c.buff( Shadows.class ) == null
						&& c.isAlive();
		//Pixel Dungeon Reforged Randomizer, Hollow Sockets leave skeletons blind for good
		if (c instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton.class) == 2) {
			sighted = false;
		}
		if (sighted) {
			boolean[] blocking = null;

			if (modifiableBlocking == null || modifiableBlocking.length != Dungeon.level.losBlocking.length){
				modifiableBlocking = new boolean[Dungeon.level.losBlocking.length];
			}

			//grass is see-through by some specific entities, but not during the fungi quest
			if (!(Dungeon.level instanceof  MiningLevel) || Blacksmith.Quest.Type() != Blacksmith.Quest.FUNGI){
				if ((c instanceof Hero && ((Hero) c).subClass.is(HeroSubClass.WARDEN)) || com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero.seesThroughGrass(c) //bluearchive
						|| com.shatteredpixel.shatteredpixeldungeon.RBAHooks.seesThroughGrass(c) //rebluearchive, Miyu
						|| (c instanceof Hero && ((Hero) c).pointsInTalent(Talent.MECHANICAL_GOGGLES) >= 2)
						|| c instanceof YogFist.SoiledFist || c instanceof GnollGeomancer) {
					if (blocking == null) {
						System.arraycopy(Dungeon.level.losBlocking, 0, modifiableBlocking, 0, modifiableBlocking.length);
						blocking = modifiableBlocking;
					}
					for (int i = 0; i < blocking.length; i++) {
						if (blocking[i] && (Dungeon.level.map[i] == Terrain.HIGH_GRASS || Dungeon.level.map[i] == Terrain.FURROWED_GRASS)) {
							blocking[i] = false;
						}
					}
				}
			}

			//allies and specific enemies can see through shrouding fog
			if ((c.alignment != Char.Alignment.ALLY && !(c instanceof GnollGeomancer))
					&& Dungeon.level.blobs.containsKey(SmokeScreen.class)
					&& Dungeon.level.blobs.get(SmokeScreen.class).volume > 0) {
				if (blocking == null) {
					System.arraycopy(Dungeon.level.losBlocking, 0, modifiableBlocking, 0, modifiableBlocking.length);
					blocking = modifiableBlocking;
				}
				Blob s = Dungeon.level.blobs.get(SmokeScreen.class);
				for (int i = 0; i < blocking.length; i++){
					if (!blocking[i] && s.cur[i] > 0){
						blocking[i] = true;
					}
				}
			}

			blocking = com.shatteredpixel.shatteredpixeldungeon.TooCruel.blockingFov(Dungeon.level, c, modifiableBlocking, blocking); //toocruel
			if (blocking == null){
				blocking = Dungeon.level.losBlocking;
			}

			float viewDist = c.viewDistance;
			if (c instanceof Hero){
				viewDist *= 1f + 0.25f*((Hero) c).pointsInTalent(Talent.FARSIGHT);
				viewDist *= com.shatteredpixel.shatteredpixeldungeon.RBAHooks.viewFactor((Hero) c); //rebluearchive, Miyu
				viewDist *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero.viewDistMulti((Hero) c); //bluearchive
				if (((Hero) c).hasTalent(Talent.MECHANICAL_GOGGLES)) {
					viewDist *= 1.25f;
				}
				viewDist *= EyeOfNewt.visionRangeMultiplier();
				viewDist *= 1f + 0.25f*((Hero) c).pointsInTalent(Talent.TELESCOPE); //rearranged
				viewDist *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.viewDistanceFactor((Hero) c); //transformation, Shortsighted
				viewDist = Math.max(1, viewDist + com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet.viewDelta((Hero) c));
				viewDist = Math.max(1, viewDist + com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfVision.visionBonus()); //xun, ring of vision
				viewDist = com.shatteredpixel.shatteredpixeldungeon.Playstyles.vision((int) viewDist); //radar
			}
			
			ShadowCaster.castShadow( cx, cy, width(), fieldOfView, blocking, Math.round(viewDist) );
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.falconEye(this, c, fieldOfView, viewDist); //transformation, Falcon Eye
		} else {
			BArray.setFalse(fieldOfView);
		}
		
		int sense = 1;
		//Currently only the hero can get mind vision
		if (c.isAlive() && c == Dungeon.hero) {
			for (Buff b : c.buffs( MindVision.class )) {
				sense = Math.max( ((MindVision)b).distance, sense );
			}
			if (c.buff(MagicalSight.class) != null){
				sense = Math.max( MagicalSight.DISTANCE, sense );
			}
			sense = com.shatteredpixel.shatteredpixeldungeon.RBAHooks.senseRange(c, sense); //rebluearchive, Mari
		}
		
		//uses rounding
		if (!sighted || sense > 1) {
			
			int[][] rounding = ShadowCaster.rounding;
			
			int left, right;
			int pos;
			for (int y = Math.max(0, cy - sense); y <= Math.min(height()-1, cy + sense); y++) {
				if (rounding[sense][Math.abs(cy - y)] < Math.abs(cy - y)) {
					left = cx - rounding[sense][Math.abs(cy - y)];
				} else {
					left = sense;
					while (rounding[sense][left] < rounding[sense][Math.abs(cy - y)]){
						left--;
					}
					left = cx - left;
				}
				right = Math.min(width()-1, cx + cx - left);
				left = Math.max(0, left);
				pos = left + y * width();
				System.arraycopy(discoverable, pos, fieldOfView, pos, right - left + 1);
			}
		}

		if (c instanceof SpiritHawk.HawkAlly && Dungeon.hero.pointsInTalent(Talent.EAGLE_EYE) >= 3){
			int range = 1+(Dungeon.hero.pointsInTalent(Talent.EAGLE_EYE)-2);
			for (Mob mob : mobs) {
				int p = mob.pos;
				if (!fieldOfView[p] && distance(c.pos, p) <= range) {
					for (int i : PathFinder.NEIGHBOURS9) {
						fieldOfView[mob.pos + i] = true;
					}
				}
			}
		}

		//Currently only the hero can get mind vision or awareness
		if (c.isAlive() && c == Dungeon.hero) {

			if (heroMindFov == null || heroMindFov.length != length()){
				heroMindFov = new boolean[length];
			} else {
				BArray.setFalse(heroMindFov);
			}

			Dungeon.hero.mindVisionEnemies.clear();
			if (com.shatteredpixel.shatteredpixeldungeon.MusicImplant.mindCompass() && exit() >= 0 && exit() < length()) heroMindFov[exit()] = true; //musicimplant, mind compass

			int mindVisRange = 0;
			if (c.buff(MindVision.class) != null) {
				mindVisRange = Integer.MAX_VALUE;
			} else {
				if (((Hero) c).hasTalent(Talent.HEIGHTENED_SENSES)) {
					mindVisRange = 1 + ((Hero) c).pointsInTalent(Talent.HEIGHTENED_SENSES);
				}
				mindVisRange = Math.max(mindVisRange, com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.windingPorch((Hero) c)); //xun
				if (c.buff(DivineSense.DivineSenseTracker.class) != null) {
					if (((Hero) c).heroClass.is(HeroClass.CLERIC)) {
						mindVisRange = 4 + 4 * ((Hero) c).pointsInTalent(Talent.DIVINE_SENSE);
					} else {
						mindVisRange = 1 + 2 * ((Hero) c).pointsInTalent(Talent.DIVINE_SENSE);
					}
				}
				mindVisRange = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedChar.mindVisionRange((Hero) c, mindVisRange); //rearranged
				mindVisRange = com.shatteredpixel.shatteredpixeldungeon.RBAHooks.mindVisionRange((Hero) c, mindVisRange); //rebluearchive
				mindVisRange = com.shatteredpixel.shatteredpixeldungeon.actors.hero.BAHero.mindVisionRange((Hero) c, mindVisRange); //bluearchive
				mindVisRange = Math.max(mindVisRange, EyeOfNewt.mindVisionRange());
				mindVisRange = com.shatteredpixel.shatteredpixeldungeon.Playstyles.xray(mindVisRange); //radar
			}

			if (mindVisRange >= 1) {

				//power of many's life link spell allows allies to get divine sense
				Char ally = PowerOfMany.getPoweredAlly();
				if (ally != null && ally.buff(DivineSense.DivineSenseTracker.class) == null) {
					ally = null;
				}

				for (Mob mob : mobs) {
					if ((mob instanceof Mimic && mob.alignment == Char.Alignment.NEUTRAL && ((Mimic) mob).stealthy())
						|| Char.hasProp(mob, Char.Property.OBJECT)){
						continue;
					}
					int p = mob.pos;
					//a mod monster summoned with no room can be left at an invalid cell
					if (p < 0 || p >= length()) continue;
					if (!fieldOfView[p] && (distance(c.pos, p) <= mindVisRange || (ally != null && distance(ally.pos, p) <= mindVisRange))) {
						for (int i : PathFinder.NEIGHBOURS9) {
							if (mob.pos + i >= 0 && mob.pos + i < length()) heroMindFov[mob.pos + i] = true;
						}
					}
				}
			}
			
			if (c.buff( Awareness.class ) != null) {
				for (Heap heap : heaps.valueList()) {
					int p = heap.pos;
					for (int i : PathFinder.NEIGHBOURS9) heroMindFov[p+i] = true;
				}
			}

			for (TalismanOfForesight.CharAwareness a : c.buffs(TalismanOfForesight.CharAwareness.class)){
				Char ch = (Char) Actor.findById(a.charID);
				if (ch == null || !ch.isAlive() || Char.hasProp(ch, Char.Property.OBJECT)) {
					continue;
				}
				int p = ch.pos;
				for (int i : PathFinder.NEIGHBOURS9) heroMindFov[p+i] = true;
			}

			for (TalismanOfForesight.HeapAwareness h : c.buffs(TalismanOfForesight.HeapAwareness.class)){
				if (Dungeon.depth != h.depth || Dungeon.branch != h.branch) continue;
				for (int i : PathFinder.NEIGHBOURS9) heroMindFov[h.pos+i] = true;
			}

			for (Mob m : mobs){
				if (m instanceof WandOfWarding.Ward
						|| m instanceof WandOfRegrowth.Lotus
						|| m instanceof SpiritHawk.HawkAlly
						|| (m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ConstructHero && Dungeon.hero.hasTalent(Talent.CONSTRUCT_VISION))
						|| m.buff(PowerOfMany.PowerBuff.class) != null
						|| m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.building.WatchTower){
					if (m.fieldOfView == null || m.fieldOfView.length != length()){
						m.fieldOfView = new boolean[length()];
						Dungeon.level.updateFieldOfView( m, m.fieldOfView );
					}
					BArray.or(heroMindFov, m.fieldOfView, heroMindFov);
				}
			}

			for (RevealedArea a : c.buffs(RevealedArea.class)){
				if (Dungeon.depth != a.depth || Dungeon.branch != a.branch) continue;
				for (int i : PathFinder.NEIGHBOURS9) heroMindFov[a.pos+i] = true;
			}
			com.shatteredpixel.shatteredpixeldungeon.RBAHooks.markMindVision(c, heroMindFov); //rebluearchive
			com.shatteredpixel.shatteredpixeldungeon.TooCruel.markMindVision(this, heroMindFov); //toocruel

			//set mind vision chars
			for (Mob mob : mobs) {
				if (heroMindFov[mob.pos] && !fieldOfView[mob.pos]){
					Dungeon.hero.mindVisionEnemies.add(mob);
				}
			}

			//Ryuse's PD, a Summoner always sees what she summoned, an Eidolon sees all her allies
			if (c instanceof Hero && ((Hero)c).heroClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.SUMMONER) {
				for (Mob mob : mobs) {
					if (fieldOfView[mob.pos]) continue;
					boolean summoned = mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost
							|| mob instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose.GhostHero
							|| mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
					boolean eidolonAlly = ((Hero)c).subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.EIDOLON
							&& mob.alignment == Char.Alignment.ALLY;
					if (summoned || eidolonAlly) {
						Dungeon.hero.mindVisionEnemies.add(mob);
					}
				}
			}

			//summoning, Summoning PD's Soul Wielder sees her minions everywhere
			if (c instanceof Hero && ((Hero)c).subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.SOUL_REAVER) {
				for (Mob mob : mobs) {
					if (!fieldOfView[mob.pos] && mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.Minion) {
						Dungeon.hero.mindVisionEnemies.add(mob);
					}
				}
			}

			BArray.or(heroMindFov, fieldOfView, fieldOfView);

		}

		if (c == Dungeon.hero) {
			for (Heap heap : heaps.valueList())
				if (!heap.seen && fieldOfView[heap.pos])
					heap.seen = true;
		}

	}

	public float levelExplorePercent( int depth ){
		return 0;
	}
	
	//summoning, free cells next to pos that the hero is not on
	public static java.util.ArrayList<Integer> getSpawningPoints( int pos ) {
		java.util.ArrayList<Integer> points = new java.util.ArrayList<>();
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null && Dungeon.level.passable[p] && Dungeon.hero.pos != p) {
				points.add(p);
			}
		}
		return points;
	}

	public int distance( int a, int b ) {
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return Math.max( Math.abs( ax - bx ), Math.abs( ay - by ) );
	}
	
	public boolean adjacent( int a, int b ) {
		return distance( a, b ) == 1;
	}
	
	//uses Pythagorean theorem for true distance, as if there was no movement grid
	//from Devoted PD, nearest first
	public java.util.ArrayList<Char> sortByDist(java.util.ArrayList<Char> l, Char source) {
		java.util.ArrayList<Char> sorted = new java.util.ArrayList<>(l);
		sorted.sort((x, y) -> Integer.compare(x.distance(source), y.distance(source)));
		return sorted;
	}

	public float trueDistance(int a, int b){
		int ax = a % width();
		int ay = a / width();
		int bx = b % width();
		int by = b / width();
		return (float)Math.sqrt(Math.pow(Math.abs( ax - bx ), 2) + Math.pow(Math.abs( ay - by ), 2));
	}

	//usually just if the base terrain of a cell is solid, but other cases exist too
	//only check on base terrain, we want to ignore temporary changes from blobs (e.g. light wall)
	public boolean invalidHeroPos( int tile ){
		int flags = Terrain.flags[map[tile]];
		return (flags & Terrain.PASSABLE) != 0 && (flags & Terrain.AVOID) != 0;
	}

	//returns true if the input is a valid tile within the level
	public boolean insideMap( int tile ){
				//top and bottom row and beyond
		return !((tile < width || tile >= length - width) ||
				//left and right column
				(tile % width == 0 || tile % width == width-1));
	}

	public Point cellToPoint( int cell ){
		return new Point(cell % width(), cell / width());
	}

	public int pointToCell( Point p ){
		return p.x + p.y*width();
	}
	
	public String tileName( int tile ) {
		
		switch (tile) {
			case Terrain.CHASM:
				return Messages.get(Level.class, "chasm_name");
			case Terrain.EMPTY:
			case Terrain.EMPTY_SP:
			case Terrain.EMPTY_DECO:
			case Terrain.CUSTOM_DECO_EMPTY:
			case Terrain.SECRET_TRAP:
				return Messages.get(Level.class, "floor_name");
			case Terrain.GRASS:
				return Messages.get(Level.class, "grass_name");
			case Terrain.WATER:
				return Messages.get(Level.class, "water_name");
			case Terrain.WALL:
			case Terrain.WALL_DECO:
			case Terrain.SECRET_DOOR:
				return Messages.get(Level.class, "wall_name");
			case Terrain.DOOR:
				return Messages.get(Level.class, "closed_door_name");
			case Terrain.OPEN_DOOR:
				return Messages.get(Level.class, "open_door_name");
			case Terrain.ENTRANCE:
			case Terrain.ENTRANCE_SP:
				return Messages.get(Level.class, "entrace_name");
			case Terrain.EXIT:
				return Messages.get(Level.class, "exit_name");
			case Terrain.EMBERS:
				return Messages.get(Level.class, "embers_name");
			case Terrain.FURROWED_GRASS:
				return Messages.get(Level.class, "furrowed_grass_name");
			case Terrain.LOCKED_DOOR:
			case Terrain.HERO_LKD_DR:
				return Messages.get(Level.class, "locked_door_name");
			case Terrain.CRYSTAL_DOOR:
				return Messages.get(Level.class, "crystal_door_name");
			case Terrain.PEDESTAL:
				return Messages.get(Level.class, "pedestal_name");
			case Terrain.BARRICADE:
				return Messages.get(Level.class, "barricade_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(Level.class, "high_grass_name");
			case Terrain.LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_name");
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(Level.class, "unlocked_exit_name");
			case Terrain.WELL:
				return Messages.get(Level.class, "well_name");
			case Terrain.EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(Level.class, "statue_name");
			case Terrain.INACTIVE_TRAP:
				return Messages.get(Level.class, "inactive_trap_name");
			case Terrain.WIRE:
				return Messages.get(Level.class, "wire_name");
			case Terrain.BOOKSHELF:
				return Messages.get(Level.class, "bookshelf_name");
			case Terrain.ALCHEMY:
				return Messages.get(Level.class, "alchemy_name");
			default:
				return Messages.get(Level.class, "default_name");
		}
	}
	
	public String tileDesc( int tile ) {
		
		switch (tile) {
			case Terrain.CHASM:
				return Messages.get(Level.class, "chasm_desc");
			case Terrain.WATER:
				return Messages.get(Level.class, "water_desc");
			case Terrain.ENTRANCE:
			case Terrain.ENTRANCE_SP:
				return Messages.get(Level.class, "entrance_desc");
			case Terrain.EXIT:
			case Terrain.UNLOCKED_EXIT:
				return Messages.get(Level.class, "exit_desc");
			case Terrain.EMBERS:
				return Messages.get(Level.class, "embers_desc");
			case Terrain.HIGH_GRASS:
			case Terrain.FURROWED_GRASS:
				return Messages.get(Level.class, "high_grass_desc");
			case Terrain.LOCKED_DOOR:
			case Terrain.HERO_LKD_DR:
				return Messages.get(Level.class, "locked_door_desc");
			case Terrain.CRYSTAL_DOOR:
				return Messages.get(Level.class, "crystal_door_desc");
			case Terrain.LOCKED_EXIT:
				return Messages.get(Level.class, "locked_exit_desc");
			case Terrain.BARRICADE:
				return Messages.get(Level.class, "barricade_desc");
			case Terrain.INACTIVE_TRAP:
				return Messages.get(Level.class, "inactive_trap_desc");
			case Terrain.WIRE:
				return Messages.get(Level.class, "wire_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(Level.class, "statue_desc");
			case Terrain.ALCHEMY:
				return Messages.get(Level.class, "alchemy_desc");
			case Terrain.EMPTY_WELL:
				return Messages.get(Level.class, "empty_well_desc");
			default:
				return "";
		}
	}
}
