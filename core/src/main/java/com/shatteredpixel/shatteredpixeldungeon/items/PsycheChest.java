/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Experienced Pixel Dungeon
 * Copyright (C) 2019-2024 Trashbox Bobylev
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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.util.ArrayList;

//Experienced PD's Fate Lock. In grinding mode experience turns into scrolls of upgrade and monsters drop extra
//loot. Reset builds the current floor again for a price in health. Experienced's quest resets, its
//escalating damage and its cycles were left out.
public class PsycheChest extends Item {

	{
		image = ItemSpriteSheet.PSYCHE_CHEST;
		unique = true;
		identify();
	}

	private static final String AC_ACTIVATE = "ACTIVATE";
	private static final String AC_DEACTIVATE = "DEACTIVATE";
	private static final String AC_RESET = "RESET";

	private static final ItemSprite.Glowing BLOODY = new ItemSprite.Glowing( 0x550000 );

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (!hero.grinding) actions.add( AC_ACTIVATE );
		else actions.add( AC_DEACTIVATE );
		actions.add( AC_RESET );
		actions.remove( AC_DROP );
		actions.remove( AC_THROW );
		return actions;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", neededExp() );
	}

	//experience for each scroll of upgrade
	public static int neededExp() {
		int neededExp = 100;
		if (Dungeon.isChallenged( Challenges.NO_SCROLLS )) {
			neededExp *= 2.5f;
		}
		return neededExp;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute( hero, action );

		if (action.equals( AC_ACTIVATE )) {
			hero.grinding = true;
			GLog.w( Messages.get( this, "activated" ) );
		}
		if (action.equals( AC_DEACTIVATE )) {
			hero.grinding = false;
			GLog.w( Messages.get( this, "deactivated" ) );
		}
		if (action.equals( AC_RESET )) {
			if (hero.HP > hero.HT * 0.55f) {
				hero.HP -= Math.round( hero.HT * 0.55f );
				InterlevelScene.mode = InterlevelScene.Mode.RESET;
				Game.switchScene( InterlevelScene.class );
				Catalog.countUse( getClass() );
			} else {
				GLog.w( Messages.get( this, "no_reset" ) );
			}
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return Dungeon.hero != null && Dungeon.hero.grinding ? BLOODY : null;
	}
}
