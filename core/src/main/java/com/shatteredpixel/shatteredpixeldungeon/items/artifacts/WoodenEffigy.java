package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blastseed;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//Deistic's Wooden Effigy (MysticBranch in its code), the gnoll artifact. While worn you sense every
//creature standing in grass. PLANT (gnolls) grows a blastseed on a grass cell, which explodes when stepped on.
public class WoodenEffigy extends DeisticArtifact {

	private static final String AC_PLANT = "PLANT";

	{
		image = ItemSpriteSheet.WOODEN_EFFIGY;
		defaultAction = AC_PLANT;
		chargeCap = 1;
		charge = 1;
	}

	@Override
	protected String race() {
		return DeisticPD.GNOLL;
	}

	@Override
	protected float rechargeTime() {
		return Math.max(20f, 100f - 15f * level());
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !cursed && full()) actions.add(AC_PLANT);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_PLANT) && ready(hero) && full()){
			GameScene.selectCell(planter);
		}
	}

	@Override
	protected void onTurn() {
		Hero hero = Dungeon.hero;
		if (hero == null || cursed || !basic() || Dungeon.level == null) return;
		int range = 4 + 2 * level();
		for (Mob mob : Dungeon.level.mobs){
			int t = Dungeon.level.map[mob.pos];
			if ((t == Terrain.GRASS || t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS)
					&& Dungeon.level.distance(hero.pos, mob.pos) <= range){
				Buff.append(hero, TalismanOfForesight.CharAwareness.class, 1.5f).charID = mob.id();
			}
		}
	}

	private final CellSelector.Listener planter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;
			int t = Dungeon.level.map[target];
			if (Actor.findChar(target) != null || !Dungeon.hero.fieldOfView[target]
					|| !(t == Terrain.GRASS || t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS || t == Terrain.EMPTY)){
				GLog.i(Messages.get(WoodenEffigy.class, "bad_cell"));
				return;
			}
			Dungeon.level.plant(new Blastseed.Seed(), target);
			charge--;
			used(5);
			Dungeon.hero.spendAndNext(1f);
		}

		@Override
		public String prompt() {
			return Messages.get(WoodenEffigy.class, "prompt");
		}
	};
}
