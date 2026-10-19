/*
 * Onyx's Pixel Dungeon
 * The transformation trap from Transformation PD (dongyusangyu). Everyone in its 3x3 area but the hero goes amok
 * for 50 turns. A hero caught in it has one common talent turned into another talent of the same tier.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class TransformationTrap extends Trap {

	{
		color = VIOLET;
		shape = DIAMOND;
	}

	@Override
	public void activate() {
		if (Dungeon.level == null) return;
		Hero caught = null;
		for (int offset : PathFinder.NEIGHBOURS9){
			int cell = pos + offset;
			if (cell < 0 || cell >= Dungeon.level.length()) continue;
			Char ch = Actor.findChar(cell);
			if (ch instanceof Hero){
				caught = (Hero) ch;
			} else if (ch != null && ch.alignment != Char.Alignment.NEUTRAL){
				Buff.prolong(ch, Amok.class, 50f);
			}
		}
		if (Dungeon.level.heroFOV != null && Dungeon.level.heroFOV[pos]){
			Sample.INSTANCE.play(Assets.Sounds.CURSED);
		}
		if (caught != null){
			Talent[] change = TransformationTalents.randomTransformation(caught);
			if (change != null){
				GLog.n(Messages.get(this, "changed", change[0].title(), change[1].title()));
			} else {
				GLog.n(Messages.get(this, "unchanged"));
			}
		}
	}
}
