package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Chainsaw extends MeleeWeapon {
	{
		image = ItemSpriteSheet.CHAINSAW_HAND;

		tier = 1;

		damageMultiplier = 0.33f;
		DLY = 0.2f;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero)) {
			actions = new ArrayList<>();
		} else {
			actions.remove(AC_EQUIP);
		}
		return actions;
	}

	@Override
	public int damageRoll(Char owner) {
		return Random.Int(max());
	}

	@Override
	public int min(int lvl) {
		return 1;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		Buff.affect(defender, Bleeding.class).set(damage*3);
		return super.proc(attacker, defender, damage);
	}
}
