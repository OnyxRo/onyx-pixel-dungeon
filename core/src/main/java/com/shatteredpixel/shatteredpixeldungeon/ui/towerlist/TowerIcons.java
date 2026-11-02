/*
 * Pixel Towers / Tower Pixel Dungeon (ported to Onyx's Pixel Dungeon)
 * Copyright (C) 2024-2025 FixAkaTheFix
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

package com.shatteredpixel.shatteredpixeldungeon.ui.towerlist;

import com.watabou.noosa.Image;

/**
 * The icons Tower PD's menus use, cut from the mod's own icon sheet (interfaces/tower/icons.png) so this game's
 * Icons sheet and enum stay as they are.
 */
public enum TowerIcons {
	TOWER( 138, 0, 16, 16 ),
	GREYTOWER( 154, 0, 16, 16 ),
	AMULET( 170, 0, 16, 16 ),
	BLOODAMULET( 186, 0, 16, 16 ),
	ETHERIALAMULET( 202, 0, 16, 16 ),
	ARROW( 64, 48, 11, 11 ),
	ARROWREVERSE( 224, 48, 11, 11 ),
	SLOT1( 192, 112, 16, 16 ),
	SLOT2( 208, 112, 16, 16 ),
	SLOT3( 224, 112, 16, 16 ),
	SLOT4( 240, 112, 16, 16 ),
	CROSSBOW( 0, 128, 16, 16 ),
	MAGICMISSILE( 16, 128, 16, 16 ),
	WALL( 32, 128, 16, 16 ),
	LIGHTNING( 48, 128, 16, 16 ),
	DISINTEGRATION( 64, 128, 16, 16 ),
	GRAVE( 80, 128, 16, 16 ),
	GUARD( 96, 128, 16, 16 ),
	CANNON( 112, 128, 16, 16 ),
	DARTGUN( 128, 128, 16, 16 ),
	TNTLOG( 144, 128, 16, 16 ),
	TOTEM( 160, 128, 16, 16 ),
	GREENLOCK( 192, 128, 16, 16 ),
	DUNGEON( 208, 128, 16, 16 ),
	GREYLOCK( 224, 128, 16, 16 ),
	MINER( 0, 144, 16, 16 ),
	PYLON( 16, 144, 16, 16 ),
	RATCAMP( 64, 144, 16, 16 );

	public static final String SHEET = "interfaces/tower/icons.png";

	private final int x, y, w, h;

	TowerIcons( int x, int y, int w, int h ){
		this.x = x; this.y = y; this.w = w; this.h = h;
	}

	public Image get(){
		Image icon = new Image( SHEET );
		icon.frame( icon.texture.uvRectBySize( x, y, w, h ) );
		return icon;
	}

	/** The mod's icon for a tower, or for the locked, unlocked and dungeon headings of the tower list. */
	public static Image forTower( TowerInfo.AllTowers t ){
		switch (t) {
			case UNLOCKED: return GREENLOCK.get();
			case LOCKED: return GREYLOCK.get();
			default: return valueOf( t.name() ).get();
		}
	}
}
