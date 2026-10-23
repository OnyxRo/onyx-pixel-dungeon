/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

/** Sandbox PD's Item.getCopy, a full copy made by saving and loading the object. */
public class SandboxPDCopy {

	@SuppressWarnings("unchecked")
	public static <T extends Bundlable> T copy( T original ) {
		Bundle bundle = new Bundle();
		bundle.put( "copy", original );
		return (T) bundle.get( "copy" );
	}
}
