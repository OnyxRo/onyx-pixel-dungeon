package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.fragments.AirFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.fragments.EarthFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.fragments.FireFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.fragments.Fragment;
import com.shatteredpixel.shatteredpixeldungeon.items.fragments.WaterFragment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//Elemental PD's Broken Amulet of Yendor. With the four elemental fragments in the pack, FIX joins them into the real Amulet.
public class BrokenAmulet extends Item {

	private static final String AC_FIX = "FIX";

	@SuppressWarnings("unchecked")
	private static final Class<? extends Fragment>[] PIECES = new Class[]{
			AirFragment.class, EarthFragment.class, FireFragment.class, WaterFragment.class};

	{
		image = ItemSpriteSheet.BROKEN_AMULET;
		unique = true;
		defaultAction = AC_FIX;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_FIX);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (!action.equals(AC_FIX)) return;

		int have = 0;
		for (Class<? extends Fragment> c : PIECES) if (hero.belongings.getItem(c) != null) have++;
		if (have < PIECES.length){
			GLog.w(Messages.get(this, "missing", PIECES.length - have));
			return;
		}
		for (Class<? extends Fragment> c : PIECES) hero.belongings.getItem(c).detach(hero.belongings.backpack);
		detach(hero.belongings.backpack);
		GLog.p(Messages.get(this, "fix"));
		Item amulet = new Amulet();
		if (!amulet.collect()) com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.drop(amulet, hero.pos);
		hero.spendAndNext(1f);
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
}
