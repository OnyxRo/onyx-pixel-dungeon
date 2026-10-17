package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.throwing;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//SPS. A ball of dung. Thrown at nothing it blinds and draws every monster that can see the spot, thrown at a monster it ages it.
//Written on our Dart, SPS's own throwing weapon base is not here.
public class ShitBall extends Dart {

	{
		image = ItemSpriteSheet.SHIT_BOMB;
		tier = 1;
	}

	public ShitBall() {
		this(2);
	}

	public ShitBall(int number) {
		super();
		quantity = number;
	}

	private static ItemSprite.Glowing BROWN = new ItemSprite.Glowing(0xCC6600);

	@Override
	public ItemSprite.Glowing glowing() {
		return BROWN;
	}

	@Override
	protected void onThrow(int cell) {
		Char enemy = Actor.findChar(cell);
		if (enemy == null) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.heroFOV[mob.pos] && Dungeon.level.distance(cell, mob.pos) <= 5) {
					Buff.affect(mob, Blindness.class, 5f);
					mob.beckon(cell);
				}
			}
			Dungeon.level.drop(new ShitBall(1), cell).sprite.drop();
		} else {
			super.onThrow(cell);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		Buff.affect(defender, BeOld.class).set(20f);
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int value() {
		return 5 * quantity;
	}
}
