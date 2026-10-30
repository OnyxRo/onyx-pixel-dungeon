package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RegrownMachine;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

//Regrown Pixel Dungeon placed its machines with a debug item. This kit does the same, one machine per kit.
public class RegrownMachineKit extends Item {

	private static final String AC_COLLECTOR = "COLLECTOR";
	private static final String AC_HARVESTER = "HARVESTER";
	private static final String AC_WATERER   = "WATERER";

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;
		stackable = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_COLLECTOR);
		actions.add(AC_HARVESTER);
		actions.add(AC_WATERER);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		RegrownMachine m = null;
		if (action.equals(AC_COLLECTOR)) m = new RegrownMachine.Collector();
		else if (action.equals(AC_HARVESTER)) m = new RegrownMachine.Harvester();
		else if (action.equals(AC_WATERER)) m = new RegrownMachine.Waterer();
		if (m == null) return;

		int cell = -1;
		for (int n : PathFinder.NEIGHBOURS8){
			int c = hero.pos + n;
			if (Dungeon.level.passable[c] && Actor.findChar(c) == null){
				cell = c;
				break;
			}
		}
		if (cell == -1){
			GLog.w(Messages.get(this, "no_room"));
			return;
		}
		m.pos = cell;
		GameScene.add(m);
		detach(hero.belongings.backpack);
		hero.spendAndNext(1f);
	}

	@Override
	public int value() {
		return 30 * quantity;
	}
}
