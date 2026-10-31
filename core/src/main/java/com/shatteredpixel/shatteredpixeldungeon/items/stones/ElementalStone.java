package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndArmorInfusion;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndWeaponInfusion;

import java.util.ArrayList;

//Elemental PD's Elemental Stone. INFUSE gives a weapon or armor an elemental enchantment or glyph,
//TRANSFER moves the scroll upgrades from one weapon to another, or one armor to another.
//The mod's own upgrade counter is this game's upgradeScrollUses.
public class ElementalStone extends Item {

	public static final String AC_INFUSE = "INFUSE";
	public static final String AC_TRANSFER = "TRANSFER";

	{
		image = ItemSpriteSheet.ELEMENTAL_STONE;
		stackable = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_INFUSE);
		actions.add(AC_TRANSFER);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		curItem = this;
		if (action.equals(AC_INFUSE)) {
			GameScene.selectItem(infuser);
		} else if (action.equals(AC_TRANSFER)) {
			GameScene.selectItem(transferFrom);
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	//the infusion windows use up one stone once an element is picked
	public static void consume(){
		Item stone = Dungeon.hero.belongings.getItem(ElementalStone.class);
		if (stone != null) stone.detach(Dungeon.hero.belongings.backpack);
	}

	private static boolean gear( Item item ){
		return item instanceof Armor || item instanceof Weapon;
	}

	private final WndBag.ItemSelector infuser = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(ElementalStone.class, "select");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return gear(item);
		}

		@Override
		public void onSelect( Item item ) {
			if (item instanceof Armor) GameScene.show(new WndArmorInfusion(item));
			else if (item instanceof Weapon) GameScene.show(new WndWeaponInfusion(item));
		}
	};

	private Item source;

	private final WndBag.ItemSelector transferFrom = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(ElementalStone.class, "select_from");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return gear(item) && item.upgradeScrollUses > 0 && item.level() > 0;
		}

		@Override
		public void onSelect( Item item ) {
			if (item == null) return;
			source = item;
			GameScene.selectItem(transferTo);
		}
	};

	private final WndBag.ItemSelector transferTo = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(ElementalStone.class, "select_to");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			if (source == null || item == source) return false;
			return source instanceof Armor ? item instanceof Armor : item instanceof Weapon;
		}

		@Override
		public void onSelect( Item item ) {
			if (item == null || source == null) return;
			int moved = Math.min(source.upgradeScrollUses, source.level());
			for (int i = 0; i < moved; i++){
				source.degrade();
				item.upgrade();
			}
			source.upgradeScrollUses -= moved;
			item.upgradeScrollUses += moved;
			GLog.p(Messages.get(ElementalStone.class, "transferred", moved, item.name()));
			source = null;
			detach(Dungeon.hero.belongings.backpack);
			Dungeon.hero.spendAndNext(1f);
		}
	};
}
