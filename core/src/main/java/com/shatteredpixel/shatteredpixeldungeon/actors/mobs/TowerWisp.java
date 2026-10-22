package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerWispSprite;
import com.watabou.utils.Random;

//ported from Tower PD, where it is called Wisp. Renamed because Devoted's conjured Wisp has that name here.
//The Priest's Wisp spell summons it as an ally. It hits for up to twice the hero's level.
public class TowerWisp extends Mob {

	{
		spriteClass = TowerWispSprite.class;

		HP = HT = 1;
		defenseSkill = 20;

		flying = true;
	}

	@Override
	public int damageRoll() {
		int lvl = Dungeon.hero != null ? Dungeon.hero.lvl : 1;
		return Random.NormalIntRange( 1, Math.max( 2, lvl * 2 ) );
	}

	@Override
	public int attackSkill( Char target ) {
		return 15;
	}
}
