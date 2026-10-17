/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class UnBlessAnkh extends Item {

	public static final String AC_BLESS = "BLESS";

	{
		//name = "Ankh";
		image = ItemSpriteSheet.ANKH;
        stackable = false;

		// You tell the ankh no, don't revive me, and then it comes back to
		// revive you again in another run.
		// I'm not sure if that's enthusiasm or passive-aggression.
		 
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		DewVial vial = hero.belongings.getItem(DewVial.class);
		if (vial != null && vial.isFullBless())
			actions.add(AC_BLESS);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {
		if (action.equals(AC_BLESS)) {

			DewVial vial = hero.belongings.getItem(DewVial.class);
			if (vial != null) {
				this.detach(hero.belongings.backpack);
                new Ankh().collect();
				vial.upbook(100);
				GLog.p(Messages.get(this, "bless"));
				hero.spend(1f);
				hero.busy();
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT),
						0.2f, 3);
				hero.sprite.operate(hero.pos);

			}
		} else {

			super.execute(hero, action);

		}

	}

	@Override
	public String desc() {
			return super.desc();
	}

	@Override
	public int value() {
		return 25 * quantity;
	}
}
