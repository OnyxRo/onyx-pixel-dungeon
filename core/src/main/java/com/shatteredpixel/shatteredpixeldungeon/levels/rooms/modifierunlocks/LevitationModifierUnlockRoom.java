/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Modifier unlock rooms ported from Pixel Dungeon Reforged by nathanlink169
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.modifierunlocks;

import com.shatteredpixel.shatteredpixeldungeon.items.ChallengeCup;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

//A chasm behind a wall of statues. The cup sits on the far side and the only way over is levitation. The level
//also hands out a potion of levitation, three are needed. A gateway trap at the far end sends you back to the door.
public class LevitationModifierUnlockRoom extends ModifierUnlockRoom {

	@Override
	public int minWidth() { return 15; }
	@Override
	public int minHeight() { return 11; }
	@Override
	public int maxWidth() { return 15; }
	@Override
	public int maxHeight() { return 11; }

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.CHASM );

		Painter.set(level, new Point(left + 1, top + 1), Terrain.EMPTY);
		Painter.set(level, new Point(right - 1, bottom - 1), Terrain.EMPTY);
		Painter.set(level, new Point(right - 2, bottom - 1), Terrain.EMPTY);
		Painter.set(level, new Point(right - 3, bottom - 1), Terrain.PEDESTAL);
		Painter.set(level, new Point(right - 4, bottom - 1), Terrain.EMPTY);

		for (int i = left + 1; i < right - 1; ++i) {
			Painter.set(level, new Point(i, top + 2), Terrain.STATUE_SP);
			Painter.set(level, new Point(i + 1, top + 4), Terrain.STATUE_SP);
			Painter.set(level, new Point(i, top + 6), Terrain.STATUE_SP);
			Painter.set(level, new Point(i + 1, top + 8), Terrain.STATUE_SP);
		}

		Painter.fill( level, new Rect(left + 1, top + 2, right - 2, top + 2), Terrain.STATUE_SP);

		GatewayTrap teleporter = new GatewayTrap();
		teleporter.reveal();
		teleporter.SetDestination(level.pointToCell(new Point(left + 1, top + 1)));
		level.setTrap(teleporter, level.pointToCell(new Point(right - 1, bottom - 1)));
		Painter.set(level, new Point(right - 1, bottom - 1), Terrain.TRAP);

		level.drop(new ChallengeCup(), level.pointToCell(new Point(right - 3, bottom - 1)));

		level.addItemToSpawn(new PotionOfLevitation()); // Requires 3

		for (Door d : connected.values()) {
			d.set(Door.Type.REGULAR);
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (!super.canConnect(p)){
			return false;
		}

		boolean valid = false;

		if (p.x == left + 1 && p.y == top) {
			valid = true;
		}
		if (p.x == left && p.y == top + 1) {
			valid = true;
		}
		return valid;
	}
}
