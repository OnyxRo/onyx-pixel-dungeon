package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

//Deistic Pixel Dungeon's Null enchantment. A hit sometimes silences the enemy.
public class Nullify extends Weapon.Enchantment {

	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF);

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max(0, weapon.buffedLvl());
		if (Random.Int(level + 100) >= 85){
			Buff.affect(defender, Silence.class, Silence.DURATION);
		}
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return WHITE;
	}
}
