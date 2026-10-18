/*
 * Onyx's Pixel Dungeon
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Item actions Re-ARranged PD added to Shattered's own items, called from one line each.
 */
public class RearrangedItems {

	//Waterskin, the Adventurer's Herb Extraction. A seed steeped in the dew gives its plant's effect, scaled by
	//the dew used, and at +2 the seed is kept half the time
	public static void herbExtraction( final Waterskin skin, final Hero hero ){
		GameScene.selectItem( new com.shatteredpixel.shatteredpixeldungeon.windows.WndBag.ItemSelector() {
			@Override
			public String textPrompt() {
				return Messages.get(Waterskin.class, "seed");
			}
			@Override
			public Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag> preferredBag(){
				return com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch.class;
			}
			@Override
			public boolean itemSelectable(Item item) {
				return item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed;
			}
			@Override
			public void onSelect( Item item ) {
				if (!(item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed)) return;
				int volume = skin.volume();
				boolean keep = hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.HERB_EXTRACTION) == 2 && Random.Int(2) < 1;
				boolean used = true;
				if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility.class, volume*2);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin.class).set(Math.round(((hero.lvl)*volume)/20f + (Dungeon.depth*volume)/4f), 1);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision.class, volume);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue.class).set(volume/4f);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Icecap.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue.class, Math.round(volume/2f));
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity.class, volume*2);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge.class).reset(1, (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge.DURATION/20)*volume);
					used = false;
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue.class).set(volume/2f);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Starflower.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless.class, volume*3);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation.class, volume*2);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing.class).setHeal((hero.HT/10)*volume, 0, 1);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle.Seed) {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste.class, volume*2);
				} else {
					used = false;
				}
				if (used && !keep) item.detach(hero.belongings.backpack);
				skin.raSeedChosen = true;
				skin.execute(hero, "DRINK");
			}
		});
	}

	private static int gunCost(){
		return 25 + 25 * (1+Dungeon.depth/5);
	}

	//LiquidMetal AC_MAKE, a random gun, or a better uncursed one for twice the metal
	public static void makeGun( final LiquidMetal metal, final Hero hero ){
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(
						new WndOptions( new ItemSprite(metal),
								Messages.get(LiquidMetal.class, "make_title"),
								Messages.get(LiquidMetal.class, "make_desc", metal.quantity(), gunCost()),
								Messages.get(LiquidMetal.class, "make_no_boost"),
								Messages.get(LiquidMetal.class, "make_boost"),
								Messages.get(LiquidMetal.class, "make_cancel") ) {
							private float elapsed = 0f;
							@Override
							public synchronized void update() {
								super.update();
								elapsed += Game.elapsed;
							}
							@Override
							public void hide() {
								if (elapsed > 0.2f) super.hide();
							}
							@Override
							protected void onSelect( int index ) {
								if (elapsed <= 0.2f || index > 1) return;
								MeleeWeapon item = null;
								int tries = 0;
								if (index == 0) {
									while (!(item instanceof Gun) && tries++ < 1000) item = Generator.randomWeapon();
								} else {
									while (!(item instanceof Gun && !item.cursed) && tries++ < 1000) item = Generator.randomWeapon(Dungeon.depth / 5 + 1);
									if (item != null) {
										item.cursedKnown = true;
										if (Random.Float() < 0.33f) item.upgrade(true);
									}
								}
								if (!(item instanceof Gun)) return;
								if (!item.doPickUp(hero)) {
									Dungeon.level.drop( item, hero.pos ).sprite.drop();
								} else {
									hero.spend(-1);
								}
								hero.busy();
								metal.quantity(metal.quantity() - gunCost() * (index + 1));
								if (metal.quantity() <= 0) {
									metal.detach(hero.belongings.backpack);
								}
								hero.sprite.operate(hero.pos);
								Sample.INSTANCE.play(Assets.Sounds.EVOKE);
								CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.STAR ), 7 );
								Item.updateQuickslot();
							}
							@Override
							protected boolean hasInfo(int index) {
								return index < 2;
							}
							@Override
							protected void onInfo( int index ) {
								GameScene.show(new WndTitledMessage(
										Icons.get(Icons.INFO),
										Messages.titleCase(Messages.get(LiquidMetal.class, "make_info_title_" + (index+1))),
										Messages.get(LiquidMetal.class, "make_info_desc_" + (index+1))));
							}
							@Override
							protected boolean enabled( int index ){
								if (index == 0) return metal.quantity() >= gunCost();
								if (index == 1) return metal.quantity() >= gunCost() * 2;
								return true;
							}
						}
				);
			}
		});
	}
}
