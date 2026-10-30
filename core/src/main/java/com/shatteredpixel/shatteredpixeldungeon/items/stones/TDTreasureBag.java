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

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfAdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfHolyFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStamina;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAffection;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfConfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPassage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPetrification;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class TDTreasureBag extends InventoryStone {
	
	
	{
		//mode = WndBag.Mode.UNIDED_POTION_OR_SCROLL;
		image = ItemSpriteSheet.LOOTBAG;

		defaultAction = AC_OPEN;
	}

	public static final String AC_OPEN = "OPEN";

	@Override
	protected void onItemSelected(Item item) {
		
		GameScene.show( new WndGuess());
		
	}

	@Override
	public void execute(Hero hero, String action ) {

		super.execute(hero, action);
		if (action.equals(AC_OPEN)){
			GameScene.show( new WndGuess());
			detach( hero.belongings.backpack );
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add(AC_DROP);
		actions.add(AC_THROW);
		actions.add(AC_OPEN);
		return actions;
	}

	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

	@Override
	public ItemSprite.Glowing glowing() {
		return WHITE;
	}

	public static Item[] potions = new Item[]{
			new PotionOfExperience(),
            new PotionOfFrost(),
            new PotionOfHaste(),
            new PotionOfHealing(),
            new PotionOfInvisibility(),
            new PotionOfLevitation(),
            new PotionOfLiquidFlame(),
            new PotionOfMindVision(),
            new PotionOfParalyticGas(),
            new PotionOfPurity(),
            new PotionOfToxicGas(),

            new PotionOfHolyFuror(),
            new PotionOfSnapFreeze(),
            new PotionOfStamina(),
            new PotionOfShielding(),
            new PotionOfShroudingFog(),
			new PotionOfStormClouds(),
			new PotionOfDragonsBreath(),
			new PotionOfMagicalSight(),
			new PotionOfEarthenArmor(),
			new PotionOfCleansing(),
            new PotionOfCorrosiveGas(),

			new ScrollOfIdentify(),
            new ScrollOfLullaby(),
            new ScrollOfMagicMapping(),
            new ScrollOfMirrorImage(),
            new ScrollOfRetribution(),
            new ScrollOfRage(),
            new ScrollOfRecharging(),
            new ScrollOfRemoveCurse(),
            new ScrollOfTeleportation(),
            new ScrollOfTerror(),
            new ScrollOfTransmutation(),

			new ScrollOfDivination(),
			new ScrollOfAffection(),
			new ScrollOfForesight(),
			new ScrollOfPrismaticImage(),
			new ScrollOfPsionicBlast(),
			new ScrollOfConfusion(),
			new ScrollOfMysticalEnergy(),
			new ScrollOfAntiMagic(),
			new ScrollOfPassage(),
            new ScrollOfPetrification(),
 			new ScrollOfTransmutation(),

	};
	
	public class WndGuess extends Window {
		
		private static final int WIDTH = 120;
		private static final int BTN_SIZE = 20;
		
		public WndGuess(){

			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(ItemSpriteSheet.LOOTBAG, null) );
			titlebar.label( Messages.get(TDTreasureBag.class, "name") );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock text = PixelScene.renderTextBlock(6);
			text.text("Select your reward!");
			text.setPos(0, titlebar.bottom());
			text.maxWidth( WIDTH );
			add(text);

			for(int i = 0;i<5;i++) {
				final Item curGuess = potions[Random.Int(44)];
				final RedButton guess = new RedButton(curGuess.trueName()) {
					@Override
					protected void onClick() {
						super.onClick();

						if (curGuess.identify().doPickUp( Dungeon.hero )) {
						} else {
							Dungeon.level.drop( curGuess, Dungeon.hero.pos ).sprite.drop();
						}
						hide();
					}
				};
				guess.setRect(0, 30+25*i, WIDTH, 20);
				add(guess);
			}
			resize(WIDTH, 155);
			
		}
		
		
		@Override
		public void onBackPressed() {
			//nope
		}
	}
}
