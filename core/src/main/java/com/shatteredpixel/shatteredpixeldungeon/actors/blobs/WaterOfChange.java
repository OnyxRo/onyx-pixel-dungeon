/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Ported from Polished Pixel Dungeon (GPL-3.0).
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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PolishedPD;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes.Landmark;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

//Polished PD. The Well of Change transmutes an item thrown into it, or metamorphs two random talents of
//the hero who steps in. Found in magic well rooms when the Polished PD changes option is on.
public class WaterOfChange extends WellWater {

	@Override
	protected boolean affectHero( Hero hero ) {

		if (!hero.isAlive()) return false;

		Talent[] first = PolishedPD.randomMetamorph(hero, null);
		if (first == null) return false;
		Talent[] second = PolishedPD.randomMetamorph(hero, first[1]);

		Sample.INSTANCE.play( Assets.Sounds.DRINK );
		CellEmitter.get( hero.pos ).start( Speck.factory( Speck.CHANGE ), 0.2f, 8 );

		//this game's Transmuting has no chained callback, so only the first change is animated
		if (hero.sprite != null && hero.sprite.parent != null) {
			Transmuting.show(hero, first[0], first[1]);
		}

		hero.interrupt();

		GLog.p( Messages.get(this, "procced") );
		GLog.i( Messages.get(this, "changed", first[0].title(), first[1].title()) );
		if (second != null) GLog.i( Messages.get(this, "changed", second[0].title(), second[1].title()) );

		return true;
	}

	@Override
	protected Item affectItem( Item item, int pos ) {
		Item newItem = ScrollOfTransmutation.changeItem(item);
		if (newItem != null) {
			CellEmitter.get( pos ).start( Speck.factory( Speck.CHANGE ), 0.2f, 8 );
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			Transmuting.show(Dungeon.hero, item, newItem);
		}
		return newItem;
	}

	@Override
	public Landmark landmark() {
		return Landmark.WELL_OF_CHANGE;
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.CHANGE ), 0.35f, 0 );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
