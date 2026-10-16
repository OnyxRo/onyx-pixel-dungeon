/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * From Dragonic Pixel Dungeon. One strength and five more health for good. Dragonic made it a
 * normal potion, here it is a plain item that is always identified, like the Mega Strength
 * Potion, because a normal potion needs a colour of its own and an exotic version.
 */
public class PotionOfMight extends Item {

	public static final String AC_DRINK = "DRINK";

	{
		image = ItemSpriteSheet.POTION_GOLDEN;
		defaultAction = AC_DRINK;
		stackable = true;
		bones = true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_DRINK );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute( hero, action );
		if (action.equals( AC_DRINK )) {
			detach( hero.belongings.backpack );
			hero.spend( 1f );
			hero.busy();
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			apply( hero );
		}
	}

	public void apply( Hero hero ) {
		hero.STR++;
		hero.HTBoost += 5;
		hero.updateHT( true );
		hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "msg_1") );
		GLog.p( Messages.get(this, "msg_2") );
		Badges.validateStrengthAttained();
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
	public int value() {
		return 100 * quantity;
	}
}
