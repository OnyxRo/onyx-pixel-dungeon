package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//Deistic's R.A.P.P.E.R., a launcher Goo dropped in December. Feed it a stack of thrown weapons to learn
//that kind and level it up, then it launches upgraded copies of the last kind it learned for charge.
//Any race can use it in full.
public class Rapper extends DeisticArtifact {

	private static final String AC_LAUNCH = "LAUNCH";
	private static final String AC_ADD = "ADD";

	{
		image = ItemSpriteSheet.RAPPER;
		defaultAction = AC_LAUNCH;
	}

	private Class<? extends MissileWeapon> known = null;

	@Override
	protected String race() {
		return null;
	}

	@Override
	protected boolean full() {
		return true;
	}

	@Override
	protected float rechargeTime() {
		return 25f;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !cursed && known != null) actions.add(AC_LAUNCH);
		if (isEquipped(hero) && !cursed) actions.add(AC_ADD);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_LAUNCH) && ready(hero)){
			if (known == null) GLog.i(Messages.get(this, "empty"));
			else GameScene.selectCell(launcher);
		} else if (action.equals(AC_ADD) && isEquipped(hero) && !cursed){
			GameScene.selectItem(adder);
		}
	}

	private final WndBag.ItemSelector adder = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Rapper.class, "add_prompt");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return item instanceof MissileWeapon && !((MissileWeapon) item).spawnedForEffect && !item.isEquipped(Dungeon.hero);
		}

		@Override
		public void onSelect( Item item ) {
			if (!(item instanceof MissileWeapon)) return;
			known = ((MissileWeapon) item).getClass();
			int count = item.quantity();
			item.detachAll(Dungeon.hero.belongings.backpack);
			used(2 * count);
			GLog.i(Messages.get(Rapper.class, "added", item.name()));
			Dungeon.hero.spendAndNext(1f);
		}
	};

	private final CellSelector.Listener launcher = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (cell == null || known == null) return;
			MissileWeapon shot = Reflection.newInstance(known);
			if (shot == null) return;
			shot.quantity(1);
			shot.level(level());
			shot.identify();
			shot.spawnedForEffect = true;
			charge--;
			updateQuickslot();
			shot.cast(Dungeon.hero, cell);
		}

		@Override
		public String prompt() {
			return Messages.get(Rapper.class, "launch_prompt");
		}
	};

	private static final String KNOWN = "known";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		if (known != null) bundle.put(KNOWN, known);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(KNOWN)){
			Class<?> c = bundle.getClass(KNOWN);
			if (c != null && MissileWeapon.class.isAssignableFrom(c)) known = c.asSubclass(MissileWeapon.class);
		}
	}
}
