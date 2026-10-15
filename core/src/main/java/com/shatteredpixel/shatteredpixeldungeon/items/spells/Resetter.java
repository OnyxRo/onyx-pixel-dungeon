/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfConfusion;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Resetter extends Spell {
	
	{
		image = ItemSpriteSheet.AQUA_BLAST;
	}

	
	@Override
	public int value() {
		return Math.round(quantity * 40);
	}

	@Override
	protected void onCast(Hero hero) {
		new DistortionTrap().activate();
		detach( curUser.belongings.backpack );
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfConfusion.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 8;
			
			output = Resetter.class;
			outQuantity = 2;
		}
		
	}
}
