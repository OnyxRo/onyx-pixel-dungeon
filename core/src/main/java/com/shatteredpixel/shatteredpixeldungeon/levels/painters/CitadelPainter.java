/*
 * Onyx's Pixel Dungeon
 *
 * Ported from Devoted Pixel Dungeon (https://github.com/Arackulele/devoted-pixel-dungeon),
 * a GPLv3 fork of Shattered Pixel Dungeon. Original painter by that mod's authors.
 * Changes for this port are noted in comments.
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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CitadelPainter extends RegularPainter {

    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {

        int[] map = level.map;
        int w = level.width();
        int l = level.length();

        for (int i=0; i < l - w; i++) {

            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
                map[i] = Terrain.EMPTY_DECO;

            } else if (map[i] == Terrain.WALL
                    && !DungeonTileSheet.wallStitcheable(map[i + w])
                    && Random.Int( Math.max(2, 21 - Dungeon.depth) ) /* clamped for this port, so deep floors do not decorate every wall */ == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

    }
}
