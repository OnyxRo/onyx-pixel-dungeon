/*
 * Onyx's Pixel Dungeon
 * Transformation PD (dongyusangyu) leaves the Great Imp below the exit once Yog-Dzewa is beaten. A hero who
 * comes back up with the Amulet can challenge him, and he turns into the Great Demon. This floor is the halls
 * boss floor with the Great Imp added. In a sandbox run he fights without the Amulet.
 * Sandbox only until Onyx plays it.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GreatImp;
import com.watabou.utils.Bundle;

public class TransformationHallsBossLevel extends HallsBossLevel {

	private boolean impPlaced = false;

	@Override
	public void unseal() {
		super.unseal();
		if (impPlaced) return;
		impPlaced = true;
		TransformationCityBossLevel.placeAt(new GreatImp(), exit() + width() * 3, this);
	}

	private static final String IMP = "great_imp_placed";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(IMP, impPlaced);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		impPlaced = bundle.getBoolean(IMP);
	}
}
