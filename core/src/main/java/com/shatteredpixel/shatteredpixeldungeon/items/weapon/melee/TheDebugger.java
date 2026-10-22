/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class TheDebugger extends MeleeWeapon {

    {
        image = ItemSpriteSheet.TheDebugger;

        //tier 5 for prices and formulas, the numbers below used to overflow and roll negative damage
        tier = 5;
        ACC = 10f; //1000% boost to accuracy
        RCH = 256;
    }

    @Override
    public int min(int lvl) {
        return 1000000 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 1000000 + lvl * 10;
    }

    @Override
    public int STRReq(int lvl) {
        return 10;
    }
}
