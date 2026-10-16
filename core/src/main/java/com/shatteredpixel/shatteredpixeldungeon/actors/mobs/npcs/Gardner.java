/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2017 Leon Horn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.tools.Scissors;
import com.shatteredpixel.shatteredpixeldungeon.items.tools.Tincturebottle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GardnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPicture;
import com.watabou.utils.Bundle;

public class Gardner extends NPC {

	{
		spriteClass = GardnerSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	public boolean hasgivenitems = false;
	
	@Override
	protected boolean act() {
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public boolean add( Buff buff ) { return false; }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact( Char c ) {

		boolean scissors = false;
		boolean tincture = false;
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		GameScene.show(new WndPicture(Assets.Sprites.HELP_GRADNER, 1f));
		if(!hasgivenitems){
			for (Item i : Dungeon.hero.belongings.backpack.items){
				if(i instanceof Scissors){
					scissors = true;
				}
			}
			for (Item i : Dungeon.hero.belongings.backpack.items){
				if(i instanceof Tincturebottle){
					tincture = true;
				}
			}

			if(scissors == false){
				new Scissors().collect();
			}
			if(tincture == false){
				new Tincturebottle().collect();
			}
			if(scissors == false && tincture == false){
				hasgivenitems = true;
			}
		}

		return false;
	}

	private static final String HASGIVENITEMS = "hasgivenitems";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( HASGIVENITEMS, hasgivenitems );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		hasgivenitems = bundle.getBoolean(HASGIVENITEMS);
	}
}
