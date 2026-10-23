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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class TalentIcon extends Image {

	private static TextureFilm film;
	private static TextureFilm raFilm;
	private static final int SIZE = 16;

	public TalentIcon(Talent talent){
		this(talent.icon());
	}

	//Transformation PD's talent catalog, icons from 4096 up come from the mod's own sheet
	public static final int TRANSFORMATION_BASE = 4096;
	private static TextureFilm transFilm;
	private static TextureFilm xunFilm;

	public TalentIcon(int icon){
		super( icon >= 40000 ? Assets.Interfaces.XUN_TALENT_ICONS : icon >= 10000 ? Assets.Interfaces.RA_TALENT_ICONS
				: icon >= TRANSFORMATION_BASE ? Assets.Interfaces.TALENT_ICONS_TRANSFORMATION : Assets.Interfaces.TALENT_ICONS );

		//xun, icons of 40000 and up come from Xun's PD's own sheet
		if (icon >= 40000){
			if (xunFilm == null) xunFilm = new TextureFilm(texture, SIZE, SIZE);
			frame(xunFilm.get(icon - 40000));
			return;
		}

		//rearranged, icons of 10000 and up come from Re-ARranged PD's own sheet
		if (icon >= 10000){
			if (raFilm == null) raFilm = new TextureFilm(texture, SIZE, SIZE);
			frame(raFilm.get(icon - 10000));
			return;
		}

		//transformation, icons from 4096 come from Transformation PD's sheet
		if (icon >= TRANSFORMATION_BASE){
			if (transFilm == null) transFilm = new TextureFilm(texture, SIZE, SIZE);
			frame(transFilm.get(icon - TRANSFORMATION_BASE));
			return;
		}

		if (film == null) film = new TextureFilm(texture, SIZE, SIZE);

		frame(film.get(icon));
	}

}
