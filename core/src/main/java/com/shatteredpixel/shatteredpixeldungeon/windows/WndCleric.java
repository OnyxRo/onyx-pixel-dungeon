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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Cleric;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FortuneTeller;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WndCleric extends Window {

	private static final int WIDTH      = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP        = 2;

	public WndCleric(Cleric cleric, final Hero hero, int price, int uncurse_price ) {
		
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( cleric.sprite() );
		titlebar.label( Messages.titleCase( cleric.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );
		
		RedButton btnHeal = new RedButton( Messages.get(this, "heal", price) ) {
			@Override
			protected void onClick() {
				Dungeon.gold -= Cleric.price;
				hide();
				hero.HP = hero.HT;
				hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );

				PotionOfHealing.cure( hero );
				((Hunger)hero.buff( Hunger.class )).satisfy( Hunger.STARVING );

				Dungeon.hero.interrupt();

				GLog.p( Messages.get(WndCleric.class, "procced") );
				Cleric.price += 100;
			}
		};
		btnHeal.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
		btnHeal.enable( price <= Dungeon.gold );
		add( btnHeal );

		RedButton btnUncurse = new RedButton( Messages.get(this, "uncurse", uncurse_price) ) {
			@Override
			protected void onClick() {
				GameScene.selectItem( itemSelector );
			}
		};
		btnUncurse.setRect( 0, btnHeal.top() + btnHeal.height() + GAP, WIDTH, BTN_HEIGHT );
		btnUncurse.enable( uncurse_price <= Dungeon.gold );
		add( btnUncurse );
		
		resize( WIDTH, (int)btnUncurse.bottom() );
	}

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(WndCleric.class, "select");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return ScrollOfRemoveCurse.uncursable(item);
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Dungeon.gold -= Cleric.uncurse_price;
				Cleric.uncurse_price += 100;
				ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);
			}
		}
	};
}
