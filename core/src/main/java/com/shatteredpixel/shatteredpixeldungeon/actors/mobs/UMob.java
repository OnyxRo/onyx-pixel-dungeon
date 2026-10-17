/*
 * Onyx's Pixel Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Base class for monsters ported from Unleashed Pixel Dungeon (a Shattered 0.3 mod).
 * That base described a monster with atkSkill, dmgMin, dmgMax, dmgRed, mobType and TYPE_ flags
 * and a plain name field. This class gives those fields back and turns them into the
 * current Mob methods, so the ported classes need almost no edits.
 */
public abstract class UMob extends Mob {

	public final int MOBTYPE_NORMAL = 0;
	public final int MOBTYPE_NIMBLE = 1;
	public final int MOBTYPE_TOUGH  = 2;
	public final int MOBTYPE_RANGED = 3;
	public final int MOBTYPE_DEBUFF = 4;
	public final int MOBTYPE_SPECIAL = 5;

	public String name = "mob";
	public int atkSkill = 1;
	public int dmgRed = 0;
	public int dmgMin = 1;
	public int dmgMax = 3;
	public int mobType = MOBTYPE_NORMAL;

	public boolean TYPE_ANIMAL = false;
	public boolean TYPE_EVIL = false;
	public boolean TYPE_UNDEAD = false;
	public boolean TYPE_MINDLESS = false;

	@Override
	public int attackSkill( com.shatteredpixel.shatteredpixeldungeon.actors.Char target ) {
		return atkSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( dmgMin, dmgMax );
	}

	@Override
	public int drRoll() {
		return super.drRoll() + (dmgRed > 0 ? Random.NormalIntRange( dmgRed / 2, dmgRed ) : 0);
	}

	@Override
	public HashSet<Property> properties() {
		HashSet<Property> p = super.properties();
		if (TYPE_UNDEAD) p.add( Property.UNDEAD );
		if (TYPE_EVIL) p.add( Property.DEMONIC );
		if (TYPE_MINDLESS) p.add( Property.INORGANIC );
		return p;
	}
}
