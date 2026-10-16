package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;

// The rules of Transformation PD's Scroll of Extraction. The mod's version also kept raid and
// merchant states for thrown sets, this game has neither, so only the set credits are kept.
final class UpgradeExtraction {

	private UpgradeExtraction() {
	}

	static boolean canExtract(Item item, Hero hero) {
		if (!item.isUpgradable()) return false;
		if (item instanceof MissileWeapon) {
			return MissileWeapon.UpgradedSetTracker.availableUpgradeScrollUses(
					hero, (MissileWeapon) item) > 0;
		}
		return item.upgradeScrollUses > 0;
	}

	//takes the item's scroll upgrades back out, returns how many scrolls that is
	static int extractUpgradeUses(Item item, Bag inventory, Hero hero) {
		int extracted = item instanceof MissileWeapon
				? MissileWeapon.UpgradedSetTracker.consumeUpgradeScrollUses(
						hero, (MissileWeapon) item)
				: item.upgradeScrollUses;
		degradeAndClear(item, extracted);

		if (item instanceof MissileWeapon) {
			MissileWeapon selected = (MissileWeapon) item;
			if (inventory != null) degradeMissileSet(inventory, selected, extracted);
			MissileWeapon.UpgradedSetTracker.setCanonicalLevel(
					hero, selected, selected.trueLevel());
		}
		return extracted;
	}

	private static void degradeMissileSet(Bag bag, MissileWeapon selected, int levels) {
		for (Item item : bag.items.toArray(new Item[0])) {
			if (item instanceof MissileWeapon) {
				MissileWeapon missile = (MissileWeapon) item;
				if (missile != selected && missile.setID == selected.setID) {
					degradeAndClear(missile, levels);
				}
			} else if (item instanceof Bag) {
				degradeMissileSet((Bag) item, selected, levels);
			}
		}
	}

	private static void degradeAndClear(Item item, int levels) {
		item.degrade(Math.min(Math.max(0, item.trueLevel()), levels));
		item.upgradeScrollUses = 0;
	}
}
