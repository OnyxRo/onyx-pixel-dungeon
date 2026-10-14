/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Pixel Dungeon, Copyright (C) 2012-2015 Oleg Dolya
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.Locale;

/**
 * What a mod is and what it changed, in the words of its creator's page on GitHub,
 * shown when the mod's icon at the top left of the game screen is clicked.
 * The summaries live in windows.properties under windows.wndmodinfo, keyed by the mod's
 * name with everything but letters and digits taken out.
 */
public class WndModInfo extends WndTitledMessage {

	public WndModInfo( Level level ) {
		super( icon(level), mod(level), text(level) );
	}

	private static String mod( Level level ){
		return Messages.get(level.getClass(), "mod");
	}

	private static Image icon( Level level ){
		Image image = new Image( WorldShuffle.modLogo(level) );
		image.scale.set( 16f / image.height );
		return image;
	}

	private static String text( Level level ){
		String mod = mod(level);
		String key = mod.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");

		String desc = Messages.get(WndModInfo.class, key);
		if (desc.equals(Messages.NO_TEXT_FOUND)) desc = Messages.get(WndModInfo.class, "unknown");

		ArrayList<String> names = new ArrayList<>();
		for (int world = 0; world < WorldShuffle.worldCount(); world++) {
			if (WorldShuffle.modName(world).equals(mod)) {
				names.add( Messages.titleCase(WorldShuffle.menuName(world)) );
			}
		}

		return desc + "\n\n" + Messages.get(WndModInfo.class, "worlds", String.join(", ", names));
	}

}
