package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TestFireChalBoss;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class FireChallengeBossLevel extends Level {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}

	public final int ENTRANCE_LOCATION = 23 + width * 11;

	public boolean bossSpawned = false;

	@Override
	public String tilesTex() {
		return Assets.Sprites.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.Sprites.WATER_CITY;
	}

	@Override
	protected boolean build() {

		setSize(48, 48);

		map = Layouts.FIRE_CHALLENGE_BOSS_START.clone();

		buildFlagMaps();
		cleanWalls();

		entrance = 23 + width * 6;
		exit = 0;

		return true;
	}

	private boolean insideRoom(int cell) {
		return (new EmptyRoom().set(17, 11, 32, 27)).inside(cellToPoint(cell));
	}

	@Override
	public void occupyCell( Char ch ) {
		super.occupyCell( ch );
		int cell = ch.pos;
		if (ch == Dungeon.hero && insideRoom(cell) && !bossSpawned) {
			TestFireChalBoss boss = new TestFireChalBoss();
			boss.state = boss.WANDERING;
			do {
				boss.pos = randomRespawnCell( null );
			} while (heroFOV[boss.pos]);
			GameScene.add( boss );
			bossSpawned = true;
			Level.set(ENTRANCE_LOCATION, Terrain.WALL);
		}
	}

	@Override
	public void unseal() {
		super.unseal();
		Level.set(ENTRANCE_LOCATION, Terrain.DOOR);
	}

	@Override
	public Mob createMob() {
		return null;
	}

	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		java.util.ArrayList<Item> itemList = Bones.get(); Item item = (itemList == null || itemList.isEmpty()) ? null : itemList.get(0);
		if (item != null) {
			drop(item, exit - 1).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public String tileName(int tile) {
		if (tile == Terrain.WATER) {
			return Messages.get(SewerLevel.class, "water_name");
		}
		return super.tileName(tile);
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		SewerLevel.addSewerVisuals(this, visuals);
		return visuals;
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int cell;
		do {
			cell = Random.Int(length());
		} while (!insideRoom(cell) && solid[cell] && Actor.findChar(cell) == null);
		return cell;
 	}
	public static final String BOSS = "boss";
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BOSS, bossSpawned);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		bossSpawned = bundle.getBoolean(BOSS);
	}
}

