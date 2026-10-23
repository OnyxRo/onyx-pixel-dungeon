/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//Sandbox PD's unstable trap, which goes off as a random other trap.
//The mod picked from its editor's trap list, this port picks from the journal's.
public class UnstableTrap extends Trap {

	{
		color = GREY;
		shape = WAVES;
	}

	@Override
	public void activate() {
		ArrayList<Class<?>> choices = new ArrayList<>();
		for (Class<?> cls : Bestiary.TRAP.entities()) {
			if (Trap.class.isAssignableFrom(cls) && cls != UnstableTrap.class) choices.add(cls);
		}
		if (choices.isEmpty()) return;
		Trap t = (Trap) Reflection.newInstance(Random.element(choices));
		if (t == null) return;
		t.pos = pos;
		GLog.newLine();
		GLog.i(Messages.get(this, "activates_as", t.name()));
		t.activate();
	}
}
