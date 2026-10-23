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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class BuffIcon extends Image {

	private static TextureFilm smallFilm;
	private static final int SML_SIZE = 7;

	private static TextureFilm largeFilm;
	private static final int LRG_SIZE = 16;

	private final boolean large;

	public BuffIcon(Buff buff, boolean large){
		super( large ? Assets.Interfaces.BUFFS_LARGE : Assets.Interfaces.BUFFS_SMALL );
		this.large = large;
		refresh(buff);
	}

	public BuffIcon(int icon, boolean large){
		super( large ? Assets.Interfaces.BUFFS_LARGE : Assets.Interfaces.BUFFS_SMALL );
		this.large = large;
		refresh(icon);
	}

	public void refresh(Buff buff){
		refresh(buff.icon());
		buff.tintIcon(this);
	}

	private static TextureFilm raSmallFilm, raLargeFilm;

	private static TextureFilm drSmallFilm, drLargeFilm;

	private static TextureFilm baSmallFilm, baLargeFilm;

	private static TextureFilm rbaSmallFilm, rbaLargeFilm;

	public void refresh(int icon){
		//rebluearchive, icons of 50000 and up come from Re Blue Archive PD's own sheets
		if (icon >= 50000){
			texture( large ? Assets.Interfaces.RBA_BUFFS_LARGE : Assets.Interfaces.RBA_BUFFS_SMALL );
			if (large){
				if (rbaLargeFilm == null) rbaLargeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
				frame(rbaLargeFilm.get(icon - 50000));
			} else {
				if (rbaSmallFilm == null) rbaSmallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
				frame(rbaSmallFilm.get(icon - 50000));
			}
			return;
		}
		//bluearchive, icons of 30000 and up come from Blue Archive PD's own sheets
		if (icon >= 30000){
			texture( large ? Assets.Interfaces.BA_BUFFS_LARGE : Assets.Interfaces.BA_BUFFS_SMALL );
			if (large){
				if (baLargeFilm == null) baLargeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
				frame(baLargeFilm.get(icon - 30000));
			} else {
				if (baSmallFilm == null) baSmallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
				frame(baSmallFilm.get(icon - 30000));
			}
			return;
		}
		//deranged, icons of 20000 and up come from DERanged PD's own sheets
		if (icon >= 20000 && icon < 30000){
			texture( large ? Assets.Interfaces.DR_BUFFS_LARGE : Assets.Interfaces.DR_BUFFS_SMALL );
			if (large){
				if (drLargeFilm == null) drLargeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
				frame(drLargeFilm.get(icon - 20000));
			} else {
				if (drSmallFilm == null) drSmallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
				frame(drSmallFilm.get(icon - 20000));
			}
			return;
		}
		//rearranged, icons of 10000 and up come from Re-ARranged PD's own sheets
		if (icon >= 10000){
			texture( large ? Assets.Interfaces.RA_BUFFS_LARGE : Assets.Interfaces.RA_BUFFS_SMALL );
			if (large){
				if (raLargeFilm == null) raLargeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
				frame(raLargeFilm.get(icon - 10000));
			} else {
				if (raSmallFilm == null) raSmallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
				frame(raSmallFilm.get(icon - 10000));
			}
			return;
		}
		texture( large ? Assets.Interfaces.BUFFS_LARGE : Assets.Interfaces.BUFFS_SMALL );
		if (large){
			if (largeFilm == null) largeFilm = new TextureFilm(texture, LRG_SIZE, LRG_SIZE);
			frame(largeFilm.get(icon));
		} else {
			if (smallFilm == null ) smallFilm = new TextureFilm(texture, SML_SIZE, SML_SIZE);
			frame(smallFilm.get(icon));
		}
	}

}
