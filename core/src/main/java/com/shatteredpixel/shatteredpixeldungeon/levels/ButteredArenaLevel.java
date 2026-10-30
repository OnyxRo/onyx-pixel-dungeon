/*
 * Buttered Pixel Dungeon's arena (its ArenaLevel), floor 1 of the Arena game mode, see ButteredArena.
 * Five halls of the Halls of the dungeon around a small room with the stairs. The room's walls burn
 * away when the hero first steps off the stairs. Waves come each time the hero levels up, a cleared
 * wave fills up again. Waves 5 and 25 are Goo and Yog-Dzewa. The mod's Tengu, DM-300 and Dwarf King
 * waves crash away from their own arenas in this Shattered, so waves 10, 15 and 20 bring three
 * champion monsters instead. A boss wave gives a level and the Pixel Mart Key, and after wave 25
 * the locked stairs open to the Amulet floor.
 */
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.PixelMartKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class ButteredArenaLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;
		viewDistance = 3;
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int ROOM_LEFT   = WIDTH / 2 - 1;
	private static final int ROOM_RIGHT  = WIDTH / 2 + 1;
	private static final int ROOM_TOP    = HEIGHT / 2 - 1;
	private static final int ROOM_BOTTOM = HEIGHT / 2 + 1;

	public int wave = 1;
	private int pending = 0;
	private boolean bossWave = false;
	private int bossPos = -1;
	private boolean entered = false;
	private int stairs = -1;
	private int exitCell = -1;

	public boolean bossWave(){
		return bossWave;
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}

	private static final String WAVE    = "wave";
	private static final String PENDING = "pending";
	private static final String BOSS    = "boss_wave";
	private static final String BOSSPOS = "boss_pos";
	private static final String ENTERED = "entered";
	private static final String STAIRS  = "stairs";
	private static final String EXIT    = "exit_cell";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WAVE, wave );
		bundle.put( PENDING, pending );
		bundle.put( BOSS, bossWave );
		bundle.put( BOSSPOS, bossPos );
		bundle.put( ENTERED, entered );
		bundle.put( STAIRS, stairs );
		bundle.put( EXIT, exitCell );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		wave = bundle.getInt( WAVE );
		pending = bundle.getInt( PENDING );
		bossWave = bundle.getBoolean( BOSS );
		bossPos = bundle.getInt( BOSSPOS );
		entered = bundle.getBoolean( ENTERED );
		stairs = bundle.getInt( STAIRS );
		exitCell = bundle.getInt( EXIT );
	}

	@Override
	protected boolean build() {
		setSize(WIDTH, HEIGHT);
		Painter.fill( this, 0, 0, WIDTH, HEIGHT, Terrain.WALL );

		//the mod's five halls, the middle one holds the locked stairs at its top
		for (int i = 0; i < 5; i++) {
			int top = Random.IntRange( 2, ROOM_TOP - 1 );
			int bottom = Random.IntRange( ROOM_BOTTOM + 1, 22 );
			Painter.fill( this, 2 + i * 4, top, 4, bottom - top + 1, Terrain.EMPTY );
			if (i == 2) exitCell = (i * 4 + 3) + top * width();
			for (int j = 0; j < 4; j++) {
				if (Random.Int( 2 ) == 0) {
					int y = Random.IntRange( top + 1, bottom - 1 );
					map[i*4+j + y*width()] = map[i*4+j + 1 + y*width()] = map[i*4+j - 1 + y*width()] = Terrain.WALL_DECO;
				}
			}
		}
		//a cross corridor, so every hall joins the others even when the decorations close one off
		Painter.fill( this, 2, ROOM_BOTTOM + 1, 20, 1, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1, ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP, ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP + 1, Terrain.EMPTY );

		stairs = Random.Int( ROOM_LEFT, ROOM_RIGHT + 1 ) + Random.Int( ROOM_TOP, ROOM_BOTTOM + 1 ) * width();
		map[stairs] = Terrain.ENTRANCE;
		transitions.add(new LevelTransition(this, stairs, LevelTransition.Type.REGULAR_ENTRANCE));

		map[exitCell] = Terrain.LOCKED_EXIT;
		transitions.add(new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT));

		boolean[] patch = Patch.generate( width, height, 0.45f, 6, true );
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) map[i] = Terrain.WATER;
			else if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) map[i] = Terrain.EMPTY_DECO;
		}
		return true;
	}

	@Override
	public Mob createMob() {
		return null;
	}

	@Override
	protected void createMobs() {
		spawnMobs();
	}

	@Override
	protected void createItems() {
		int nItems = 6;
		while (Random.Float() < 0.3f) nItems++;
		for (int i = 0; i < nItems; i++) {
			Item item = Generator.random();
			if (item != null) drop( item, randomDropCell() ).type = Heap.Type.HEAP;
		}
		for (Item item : itemsToSpawn) drop( item, randomDropCell() ).type = Heap.Type.HEAP;
		itemsToSpawn.clear();
	}

	private int randomDropCell() {
		for (int tries = 0; tries < 1000; tries++) {
			int pos = Random.Int( length() );
			if (passable[pos] && map[pos] != Terrain.ENTRANCE && !solid[pos]) return pos;
		}
		return stairs;
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		return -1;
	}

	/*****************************************************************
	 * Waves
	 *****************************************************************/

	@Override
	public Actor addRespawner() {
		Actor waves = new Waves();
		Actor.add( waves );
		return waves;
	}

	public static class Waves extends Actor {
		{
			actPriority = BUFF_PRIO;
		}

		@Override
		protected boolean act() {
			if (Dungeon.level instanceof ButteredArenaLevel && Dungeon.hero != null && Dungeon.hero.isAlive()) {
				((ButteredArenaLevel) Dungeon.level).tick( Dungeon.hero );
			}
			spend( TICK );
			return true;
		}
	}

	public void heroLeveledUp(){
		//a level gained during a boss wave does not skip the boss, the boss's own level starts the next wave
		if (!bossWave) pending++;
	}

	private ArrayList<Mob> enemies(){
		ArrayList<Mob> list = new ArrayList<>();
		for (Mob m : mobs) if (m.alignment == Char.Alignment.ENEMY && m.isAlive()) list.add(m);
		return list;
	}

	public void tick( Hero hero ){
		if (!entered && hero.pos != stairs) {
			entered = true;
			for (int i = ROOM_LEFT - 1; i <= ROOM_RIGHT + 1; i++) {
				burn( (ROOM_TOP - 1) * width() + i );
				burn( (ROOM_BOTTOM + 1) * width() + i );
			}
			for (int i = ROOM_TOP; i <= ROOM_BOTTOM; i++) {
				burn( i * width() + ROOM_LEFT - 1 );
				burn( i * width() + ROOM_RIGHT + 1 );
			}
			GameScene.updateMap();
			Dungeon.observe();
		}

		if (pending > 0 && !bossWave) {
			pending--;
			nextWave();
			return;
		}

		ArrayList<Mob> left = enemies();
		if (bossWave) {
			if (!left.isEmpty()) bossPos = left.get(0).pos;
			else bossCleared( hero );
		} else if (left.isEmpty()) {
			spawnMobs();
		}
	}

	private void burn( int cell ){
		set( cell, Terrain.EMPTY_SP );
		if (GameScene.scene != null) CellEmitter.get( cell ).start( FlameParticle.FACTORY, 0.1f, 3 );
	}

	private void nextWave(){
		wave++;
		clearMobs();
		if (wave % 5 == 0 && wave <= 25) {
			bossWave = true;
			spawnBoss();
			GLog.w( Messages.get(this, "boss_wave", wave) );
		} else {
			createItems();
			spawnMobs();
			GLog.i( Messages.get(this, "wave", wave) );
		}
	}

	private void bossCleared( Hero hero ){
		bossWave = false;
		int cell = bossPos >= 0 && passable[bossPos] ? bossPos : hero.pos;
		drop( new PixelMartKey(), cell ).sprite.drop();
		if (wave >= 25 && exitCell >= 0 && map[exitCell] == Terrain.LOCKED_EXIT) {
			set( exitCell, Terrain.EXIT );
			GameScene.updateMap( exitCell );
			GLog.p( Messages.get(this, "exit_open") );
		}
		int lvl = hero.lvl;
		hero.earnExp( hero.maxExp() - hero.exp, getClass() );
		if (hero.lvl == lvl) pending++;
	}

	private void clearMobs(){
		for (Mob mob : enemies()) {
			mob.destroy();
			if (mob.sprite != null) mob.sprite.killAndErase();
		}
	}

	private boolean freeCell( int pos ){
		if (!passable[pos] || solid[pos] || Actor.findChar( pos ) != null || findMob( pos ) != null) return false;
		if (Dungeon.level == this && heroFOV != null && heroFOV[pos]) return false;
		if (Dungeon.level == this && Dungeon.hero != null) return distance( pos, Dungeon.hero.pos ) > 2;
		return distance( pos, stairs ) > 3;
	}

	private void place( Mob mob ){
		for (int tries = 0; tries < 2000; tries++) {
			int pos = Random.Int( length() );
			//late tries take any open cell, so a wave always arrives
			if (freeCell( pos ) || (tries > 1500 && passable[pos] && Actor.findChar( pos ) == null && findMob( pos ) == null)) {
				mob.pos = pos;
				mob.state = mob.WANDERING;
				if (Dungeon.level == this && GameScene.scene != null) GameScene.add( mob );
				else mobs.add( mob );
				return;
			}
		}
	}

	public void spawnMobs(){
		int n = Random.Int( 5, 10 );
		ArrayList<Class<? extends Mob>> rotation = MobSpawner.getMobRotation( Math.min( wave, 24 ) );
		for (int i = 0; i < n; i++) place( Reflection.newInstance( Random.element( rotation ) ) );
	}

	private void spawnBoss(){
		if (wave == 5) {
			place( new Goo() );
		} else if (wave == 25) {
			place( new YogDzewa() );
		} else {
			ArrayList<Class<? extends Mob>> rotation = MobSpawner.getMobRotation( wave - 1 );
			for (int i = 0; i < 3; i++) {
				Mob m = Reflection.newInstance( Random.element( rotation ) );
				Class<? extends ChampionEnemy> champ = Random.element( CHAMPIONS );
				Buff.affect( m, champ );
				place( m );
			}
		}
		ArrayList<Mob> left = enemies();
		bossPos = left.isEmpty() ? -1 : left.get(0).pos;
	}

	@SuppressWarnings("unchecked")
	private static final Class<? extends ChampionEnemy>[] CHAMPIONS = new Class[]{
			ChampionEnemy.Blazing.class, ChampionEnemy.Projecting.class, ChampionEnemy.AntiMagic.class,
			ChampionEnemy.Giant.class, ChampionEnemy.Blessed.class, ChampionEnemy.Growing.class };

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.LOCKED_EXIT:
				return Messages.get(ButteredArenaLevel.class, "locked_exit_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		HallsLevel.addHallsVisuals( this, visuals );
		return visuals;
	}
}
