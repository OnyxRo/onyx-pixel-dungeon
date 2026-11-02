/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Locked;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ArmorStatue extends Mob {

	{
		spriteClass = StatueSprite.class;

		EXP = 50 + Dungeon.depth * 2;
		state = PASSIVE;
		
		properties.add(Property.ELEMENT);
	}

	private Armor armor;

	public ArmorStatue() {
		super();

		do {
			armor = (Armor) Generator.random(Generator.Category.ARMOR);
		} while (armor.level() < 0);

		armor.identify();
		armor.inscribe(Armor.Glyph.random());

		HP = HT = 15 + Dungeon.depth * 5;
	}

	private static final String ARMOR = "armor";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ARMOR, armor);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		armor = (Armor) bundle.get(ARMOR);
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]) {
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(3/2*Dungeon.depth, 5/2* Dungeon.depth);
	}

	@Override
	public int attackSkill( Char target) {
		return  9 + Dungeon.depth * 3;
	}

	@Override
	public int defenseSkill( Char target) {
		return (int) ((4 + Dungeon.depth) * 1f);
	}


	@Override
	public float attackDelay() {
		return 1;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(armor.DRMin(), armor.DRMax());
	}

	public int magicdrRoll() {
		return Random.NormalIntRange(armor.DRMin(), armor.DRMax());
	}

	//@Override
	//protected boolean canAttack(Char enemy) {
	//	return Dungeon.level.distance( pos, enemy.pos ) <= weapon.RCH;
	//}
	
	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		super.damage( dmg, src );
	}


	@Override
	public int defenseProc(Char enemy, int damage) {
		armor.proc(this, enemy, damage);
		return damage;
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void die(Object cause) {
		Dungeon.level.drop(armor, pos).sprite.drop();
		super.die(cause);
	}

	@Override
	public void destroy() {
		
		super.destroy();
	}

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		String info = Messages.get(this, "desc");
		info += "\n" + Messages.get(this, "armor", armor.name());
		return info;
	}
	
	@Override
	public boolean add( Buff buff ) {
		 if (buff instanceof Locked || buff instanceof Silent) {
				damage(Random.NormalIntRange(1, HT * 2 / 3), buff,3);
		} else {
			return super.add(buff);
		}
		return false;
	}	

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
		//resistances.add(EnchantmentDark.class);
		
	}
}
