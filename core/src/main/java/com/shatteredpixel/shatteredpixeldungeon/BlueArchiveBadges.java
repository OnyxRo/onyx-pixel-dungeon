/*
 * Onyx's Pixel Dungeon
 *
 * The students' badges from Blue Archive PD and Re Blue Archive PD. Both mods kept Shattered's hidden
 * per class badges for their students (first boss slain, victory, and in Re Blue Archive mastery and
 * the third boss slain per subclass), so the students count toward Shattered's badges for slaying a
 * boss or winning with every class and every subclass, as the other ported heroes do. Re Blue
 * Archive left Mari's two subclasses out of its list, so they are left out here too.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;

import java.util.Map;

public class BlueArchiveBadges {

	private static boolean student( String name ){
		return name.startsWith("BA_") || name.startsWith("RBA_");
	}

	private static Badges.Badge badge( String name ){
		try {
			return Badges.Badge.valueOf(name);
		} catch (IllegalArgumentException e){
			return null;
		}
	}

	//Badges, after its class and subclass maps are filled
	public static void register( Map<HeroClass, Badges.Badge> firstBoss,
	                             Map<HeroClass, Badges.Badge> victory,
	                             Map<HeroSubClass, Badges.Badge> thirdBoss ){
		for (HeroClass cls : HeroClass.values()){
			if (!student(cls.name())) continue;
			Badges.Badge b = badge("BOSS_SLAIN_1_" + cls.name());
			if (b != null) firstBoss.put(cls, b);
			b = badge("VICTORY_" + cls.name());
			if (b != null) victory.put(cls, b);
		}
		for (HeroSubClass sub : HeroSubClass.values()){
			if (!student(sub.name())) continue;
			Badges.Badge b = badge("BOSS_SLAIN_3_" + sub.name());
			if (b != null) thirdBoss.put(sub, b);
		}
	}

	//HeroClass.masteryBadge, Re Blue Archive's students
	public static Badges.Badge mastery( HeroClass cls ){
		return badge("MASTERY_" + cls.name());
	}
}
