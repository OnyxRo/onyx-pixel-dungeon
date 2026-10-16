package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

//from Dragonic PD. The Dragon Knight's breath sears raw meat in the pack into steak every 30 turns.
public class Sear extends Buff {

	public float cook = 30f;

	@Override
	public boolean act() {
		if (target instanceof Hero) {
			Hero hero = (Hero) target;
			if (cook <= 1f) {
				for (Item item : hero.belongings.backpack.items.toArray(new Item[0])) {
					if (item instanceof MysteryMeat) {
						GLog.w(Messages.get(Burning.class, "burnsup", item.toString()));
						item.detach(hero.belongings.backpack);
						ChargrilledMeat steak = new ChargrilledMeat();
						if (!steak.collect(hero.belongings.backpack)) {
							Dungeon.level.drop(steak, hero.pos).sprite.drop();
						}
						Heap.burnFX(hero.pos);
						cook = 30f;
						break;
					}
				}
			} else {
				cook -= TICK;
			}
		}
		spend(TICK);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.NONE;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	private static final String COOK = "cook";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COOK, cook);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		cook = bundle.getFloat(COOK);
	}
}
