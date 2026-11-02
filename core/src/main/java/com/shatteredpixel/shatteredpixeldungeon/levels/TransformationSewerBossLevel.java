/*
 * Onyx's Pixel Dungeon
 * Transformation PD (dongyusangyu) puts the Warrior in Goo's room on floor 5 for half of all seeds.
 * This floor is the sewer boss floor with the Warrior in place of Goo. Sandbox only until Onyx plays it.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WarriorBoss;
import com.watabou.noosa.audio.Music;

public class TransformationSewerBossLevel extends SewerBossLevel {

	@Override
	protected boolean build() {
		if (!super.build()) return false;
		//the Goo room places Goo (or Shittened's Green Goo) while it is painted, swap it for the Warrior
		for (Mob m : mobs.toArray(new Mob[0])) {
			if (m instanceof Goo || m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreenGoo) {
				WarriorBoss boss = new WarriorBoss();
				boss.pos = m.pos;
				mobs.remove(m);
				mobs.add(boss);
			}
		}
		return true;
	}

	@Override
	public void playLevelMusic() {
		if (!locked) {
			for (Mob m : mobs) {
				if (m instanceof WarriorBoss) {
					Music.INSTANCE.end();
					return;
				}
			}
		} else {
			Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
			return;
		}
		super.playLevelMusic();
	}
}
