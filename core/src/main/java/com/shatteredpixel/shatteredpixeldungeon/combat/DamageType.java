/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * The damage type names come from Pixel Dungeon Reforged by nathanlink169
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

package com.shatteredpixel.shatteredpixeldungeon.combat;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import java.util.EnumSet;

/**
 * The damage kinds Pixel Dungeon Reforged tags its attacks with. This game has no damage types, so they only
 * label a hit. Ported Reforged items pass them to Char.Damage, which ignores them and calls damage.
 */
public enum DamageType {
	NONE,
	BLUDGEONING, PIERCING, SLASHING,
	ACID, COLD, ELECTRICITY, EXPLOSIVE, FIRE, POISON, SONIC, WATER, POSITIVE_ENERGY, NEGATIVE_ENERGY, FORCE,
	STARVATION, AMULET, BLEEDING, DEFERRED;

	public static EnumSet<DamageType> of( DamageType first, DamageType... rest ){
		return EnumSet.of( first, rest );
	}

	public static String GetName( DamageType type ){
		String n = type.name().toLowerCase().replace( '_', ' ' );
		return Character.toUpperCase( n.charAt( 0 ) ) + n.substring( 1 );
	}
}
