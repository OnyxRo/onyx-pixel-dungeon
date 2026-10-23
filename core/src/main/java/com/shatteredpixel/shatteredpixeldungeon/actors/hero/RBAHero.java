/*
 * Onyx's Pixel Dungeon
 *
 * Hooks for Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon), ported 2026-10-22.
 * The mod changed Hero itself, here its additions sit in one place and Hero calls them with one line each.
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBADivision;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.LinkedHashMap;

public class RBAHero {

	/** True for the eleven students of Re Blue Archive PD. */
	public static boolean isStudent( HeroClass cls ){
		return cls != null && cls.name().startsWith("RBA_");
	}

	public static boolean isStudent( Hero hero ){
		return hero != null && isStudent(hero.heroClass);
	}

	// ---- speech lines, the mod's yellI, yellP, yellN, yellW and yellH --------------------------

	private static String line( Hero hero, String str ){
		if (!isStudent(hero)) return null;
		if (hero.buff(RBADivision.DivisionBuff.class) != null) str = "...";
		String key = hero.heroClass.name() + "_" + str;
		if (!Messages.get(Hero.class, key).equals(Messages.NO_TEXT_FOUND)) {
			return Messages.get(Hero.class, key);
		}
		return null;
	}

	private static String say( Hero hero, String text ){
		GLog.newLine();
		return Messages.titleCase(hero.name()) + ": \"" + text + "\" ";
	}

	public static void yellI( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.i( "%s", say(hero, t) );
	}

	public static void yellP( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.p( "%s", say(hero, t) );
	}

	public static void yellN( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.n( "%s", say(hero, t) );
	}

	public static void yellW( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.w( "%s", say(hero, t) );
	}

	public static void yellH( Hero hero, String str ){
		String t = line(hero, str);
		if (t != null) GLog.h( "%s", say(hero, t) );
	}

	/** Sets every talent point back to 0, for the Scroll of Talent Reset. */
	public static void resetTalent( Hero hero ){
		for (LinkedHashMap<Talent, Integer> tier : hero.talents){
			for (Talent f : tier.keySet()){
				tier.put(f, 0);
			}
		}
	}
}
