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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

public class BannerSprites {

	public enum  Type {
		TITLE_PORT,
		TITLE_GLOW_PORT,
		TITLE_LAND,
		TITLE_GLOW_LAND,
		BOSS_SLAIN,
		GAME_OVER,
		//Shittened PD, kept below Shattered's banners (every row is 256 lower than in its own sheet)
		RAT_SLAIN,
		TOURIST_FART,
		FUCK_THE_HERO,
		AMBULANCE,
		SSHHIITT,
		I_NEED_BEANS,
		BEANS_SPILLED,
		LEGO,
		FRIEND_REQUEST,
		SASS_SLAIN,
		SAAB_SLAIN,
		ASS_SLAIN,
		AAAAA_SLAIN,
		ENEMY_FELLED,
		YASS_SLAY,
	}

	public static Image get( Type type ) {
		Image icon = new Image( Assets.Interfaces.BANNERS );
		switch (type) {
			case TITLE_PORT:
				icon.frame( icon.texture.uvRect( 0, 0, 139, 100 ) );
				break;
			case TITLE_GLOW_PORT:
				icon.frame( icon.texture.uvRect( 139, 0, 278, 100 ) );
				break;
			case TITLE_LAND:
				icon.frame( icon.texture.uvRect( 0, 100, 240, 157) );
				break;
			case TITLE_GLOW_LAND:
				icon.frame( icon.texture.uvRect( 240, 100, 480, 157 ) );
				break;
			case BOSS_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 157, 127, 225 ) );
				break;
			case GAME_OVER:
				icon.frame( icon.texture.uvRect( 128, 157, 256, 192 ) );
				break;
			case RAT_SLAIN:
				icon.frame( icon.texture.uvRect( 23, 454, 100, 475 ) );
				break;
			case TOURIST_FART:
				icon.frame( icon.texture.uvRect( 0, 484, 88, 504 ) );
				break;
			case FUCK_THE_HERO:
				icon.frame( icon.texture.uvRect( 0, 512, 96, 532 ) );
				break;
			case AMBULANCE:
				icon.frame( icon.texture.uvRect( 0, 538, 74, 554 ) );
				break;
			case SSHHIITT:
				icon.frame( icon.texture.uvRect( 0, 557, 96, 575 ) );
				break;
			case I_NEED_BEANS:
				icon.frame( icon.texture.uvRect( 0, 579, 81, 594 ) );
				break;
			case BEANS_SPILLED:
				icon.frame( icon.texture.uvRect( 0, 600, 92, 615 ) );
				break;
			case LEGO:
				icon.frame( icon.texture.uvRect( 143, 488, 231, 548 ) );
				break;
			case FRIEND_REQUEST:
				icon.frame( icon.texture.uvRect( 0, 624, 46, 639 ) );
				break;
			case SASS_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 640, 84, 660 ) );
				break;
			case SAAB_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 662, 84, 683 ) );
				break;
			case ASS_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 685, 71, 705 ) );
				break;
			case AAAAA_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 710, 113, 730 ) );
				break;
			case ENEMY_FELLED:
				icon.frame( icon.texture.uvRect( 0, 732, 92, 750 ) );
				break;
			case YASS_SLAY:
				icon.frame( icon.texture.uvRect( 0, 752, 77, 772 ) );
				break;
		}
		return icon;
	}
}
