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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FortuneTeller;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WndFortuneTeller extends Window {

	private static final int WIDTH      = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP        = 2;

	public WndFortuneTeller(FortuneTeller teller, Hero hero, int price ) {
		
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( teller.sprite() );
		titlebar.label( Messages.titleCase( teller.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message", price), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );
		
		RedButton btnIdentify = new RedButton( Messages.get(this, "identify", price) ) {
			@Override
			protected void onClick() {
				GameScene.selectItem( itemSelector );
			}
		};
		btnIdentify.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
		btnIdentify.enable( price <= Dungeon.gold );
		add( btnIdentify );
		
		resize( WIDTH, (int)btnIdentify.bottom() );
	}

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(WndFortuneTeller.class, "select");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return !item.isIdentified() && (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem
					|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand);
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Dungeon.gold -= FortuneTeller.price;
				item.identify();
				FortuneTeller.price += 50;

				if (Random.Int(1) == 0) {
					Sample.INSTANCE.play(Assets.Sounds.CURSED);

					hide();
					GameScene.show( new WndQuest( new FortuneTeller(), Messages.get(WndFortuneTeller.class, "curse", item.name()) ) );
					item.cursed = true;
					if (item instanceof MeleeWeapon) {
						Weapon w = (Weapon) item;
						if (w.enchantment != null) {
							w.enchant(Weapon.Enchantment.randomCurse(w.enchantment.getClass()));
						} else {
							w.enchant(Weapon.Enchantment.randomCurse());
						}
						w.curseInfusionBonus = true;
					} else if (item instanceof Armor){
						Armor a = (Armor) item;
						if (a.glyph != null){
							a.inscribe(Armor.Glyph.randomCurse(a.glyph.getClass()));
						} else {
							a.inscribe(Armor.Glyph.randomCurse());
						}
						a.curseInfusionBonus = true;
					} else if (item instanceof Wand){
						((Wand) item).curseInfusionBonus = true;
						((Wand) item).updateLevel();
					}
					QuickSlotButton.refresh();
				}
			}
		}
	};
}
