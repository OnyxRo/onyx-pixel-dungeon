/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class WndQuest extends WndTitledMessage {

	//Magic Ling PD, an NPC with several lines of talk, shown as one window
	public static void chating( NPC questgiver, java.util.ArrayList<String> text ) {
		if (text != null && !text.isEmpty()) {
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.show( new WndQuest( questgiver, String.join( "\n\n", text ) ) );
		}
	}

	public WndQuest( NPC questgiver, String text ) {
		super( questgiver.sprite(), Messages.titleCase( questgiver.name() ), text );
	}
}
