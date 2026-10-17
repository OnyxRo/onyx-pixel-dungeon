package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tmobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.sprites.tmobs.MyriadBlackShadowSprite;

/** Rare, fully black guard variant whose split offspring are fragile tower echoes. */
public class MyriadBlackShadow extends AlienatedPrismaticGuard {

	{
		spriteClass = MyriadBlackShadowSprite.class;
		loot = ScrollOfTransmutation.class;
		lootChance = 1f;
	}

	@Override
	protected Mob createSplitOffspring() {
		Mob mob;
		switch (Random.Int(7)) {
			case 0: mob = new CamouflageGnoll(); break;
			case 1: mob = new Corpse(); break;
			case 2: mob = new EarthlySerpent(); break;
			case 3: mob = new MechanicalFist(); break;
			case 4: mob = new MimicCrocodile(); break;
			case 5: mob = new HeavyCrabification(); break;
			default: mob = new MarshSlime(); break;
		}
		//a fragile echo, one health point
		mob.HP = mob.HT = 1;
		return mob;
	}

	@Override
	public float lootChance() {
		return 1f;
	}
}
