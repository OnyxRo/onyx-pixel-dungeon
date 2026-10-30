package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.RegrownMachineKit;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.HighGrass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Regrown Pixel Dungeon's machines. Each one works an area every turn, in the mod they came from a debug item.
//Here they come from the Machine Kit. Talking to one turns it, packs it up or (the Collector) hands over what it picked up.
public abstract class RegrownMachine extends NPC {

	{
		spriteClass = MachineSprite.class;
		properties.add(Property.IMMOVABLE);
		properties.add(Property.INORGANIC);
	}

	public int orientation = 0;

	protected int direction(){
		int w = Dungeon.level.width();
		switch (orientation){
			case 1: return w;
			case 2: return -1;
			case 3: return -w;
			default: return 1;
		}
	}

	//the 3x3 area two cells in front of the machine
	protected ArrayList<Integer> area(){
		ArrayList<Integer> cells = new ArrayList<>();
		int centre = pos + 2 * direction();
		for (int n : PathFinder.NEIGHBOURS9){
			int c = centre + n;
			if (c >= 0 && c < Dungeon.level.length()) cells.add(c);
		}
		return cells;
	}

	protected abstract void doWork();

	@Override
	protected boolean act() {
		if (Dungeon.level != null) doWork();
		spend(TICK);
		return true;
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public boolean add( Buff buff ) {
		return false;
	}

	@Override
	public boolean reset() {
		return true;
	}

	protected String[] options(){
		return new String[]{ Messages.get(RegrownMachine.class, "turn"), Messages.get(RegrownMachine.class, "pack") };
	}

	protected void onOption( int index ){
		if (index == 0){
			orientation = (orientation + 1) % 4;
		} else if (index == 1){
			destroy();
			if (sprite != null) sprite.killAndErase();
			Item kit = new RegrownMachineKit();
			if (!kit.collect()) Dungeon.level.drop(kit, Dungeon.hero.pos);
		}
	}

	@Override
	public boolean interact( Char c ) {
		if (c != Dungeon.hero) return true;
		GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), description(), options()){
			@Override
			protected void onSelect( int index ) {
				onOption(index);
			}
		});
		return true;
	}

	private static final String ORIENTATION = "orientation";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(ORIENTATION, orientation);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		orientation = bundle.getInt(ORIENTATION);
	}

	public static class Collector extends RegrownMachine {

		private ArrayList<Item> collected = new ArrayList<>();

		@Override
		protected void doWork() {
			for (int c : area()){
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null && heap.type == Heap.Type.HEAP && !heap.isEmpty()){
					collected.add(heap.pickUp());
				}
			}
		}

		@Override
		protected String[] options() {
			return new String[]{ Messages.get(RegrownMachine.class, "turn"), Messages.get(RegrownMachine.class, "pack"),
					Messages.get(RegrownMachine.class, "take", collected.size()) };
		}

		@Override
		protected void onOption( int index ) {
			if (index == 2){
				for (Item i : collected){
					if (!i.collect()) { com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(i, Dungeon.hero.pos); if (dropped.sprite != null) dropped.sprite.drop(); }
				}
				collected.clear();
				Dungeon.hero.spendAndNext(1f);
			} else {
				if (index == 1){
					for (Item i : collected) Dungeon.level.drop(i, pos);
					collected.clear();
				}
				super.onOption(index);
			}
		}

		private static final String ITEMS = "items";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle(bundle);
			bundle.put(ITEMS, collected);
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle(bundle);
			collected = new ArrayList<>();
			for (com.watabou.utils.Bundlable b : bundle.getCollection(ITEMS)) collected.add((Item) b);
		}
	}

	//tramples the tall grass all around it, which drops seeds and dew as walking through it would
	public static class Harvester extends RegrownMachine {
		@Override
		protected void doWork() {
			for (int n : PathFinder.NEIGHBOURS8){
				int c = pos + n;
				int t = Dungeon.level.map[c];
				if (t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS){
					HighGrass.trample(Dungeon.level, c);
				}
			}
		}
	}

	//the mod's waterer seeded Sprouted's water, which grows grass. Here short grass in front of it slowly grows tall
	public static class Waterer extends RegrownMachine {
		@Override
		protected void doWork() {
			for (int c : area()){
				if (Dungeon.level.map[c] == Terrain.GRASS && Random.Int(20) == 0){
					Dungeon.level.set(c, Terrain.HIGH_GRASS);
					GameScene.updateMap(c);
				}
			}
		}
	}

	public static class MachineSprite extends MobSprite {
		public MachineSprite(){
			super();
			texture(Assets.Sprites.REGROWN_MACHINE);
			TextureFilm frames = new TextureFilm(texture, 16, 16);
			idle = new Animation(1, true);
			idle.frames(frames, 0);
			run = idle.clone();
			attack = idle.clone();
			die = idle.clone();
			play(idle);
		}
	}
}
