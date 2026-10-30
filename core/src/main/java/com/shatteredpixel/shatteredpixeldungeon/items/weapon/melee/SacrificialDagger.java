package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.MonsterSoul;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//Regrown Pixel Dungeon (sldenis09). A dagger that keeps the souls of what it kills.
public class SacrificialDagger extends Dagger {

	{
		image = ItemSpriteSheet.SACRIFICIAL_DAGGER;
	}

	//called from Mob.die, a kill while this is wielded leaves the monster's soul behind
	public static void onMobDie( Mob mob, Object cause ){
		if (Dungeon.hero == null || cause != Dungeon.hero || mob.alignment != Char.Alignment.ENEMY) return;
		if (!(Dungeon.hero.belongings.weapon() instanceof SacrificialDagger)) return;
		if (mob.properties().contains(Char.Property.BOSS) || mob.properties().contains(Char.Property.MINIBOSS)) return;
		{ com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(new MonsterSoul(mob.getClass()), mob.pos); if (dropped.sprite != null) dropped.sprite.drop(); }
	}
}
