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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WeaponEnhance;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.samurai.ShadowBlade;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Rope;
import com.shatteredpixel.shatteredpixeldungeon.items.spellbook.BookOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.MagicalBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.TrueRunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * The weapon rules Re-ARranged PD added to Shattered's Weapon, MeleeWeapon and KindOfWeapon, as one line hooks.
 */
public class RearrangedWeapons {

	//Weapon.speedMultiplier
	public static float speedFactor( Char owner ){
		if (!(owner instanceof Hero)) return 1f;
		Hero hero = (Hero) owner;
		float multi = 1f;
		if (hero.hasTalent(Talent.ATK_SPEED_ENHANCE)) {
			multi *= 1 + 0.05f * hero.pointsInTalent(Talent.ATK_SPEED_ENHANCE);
		}
		if (hero.buff(DualDagger.ReverseBlade.class) != null) {
			multi *= 2f;
		}
		if (hero.buff(ShadowBlade.shadowBladeTracker.class) != null) {
			multi *= 2f + 0.05f * hero.pointsInTalent(Talent.DOUBLE_BLADE_PRACTICE);
		}
		return multi;
	}

	//Weapon.reachFactor
	public static int bonusReach( Char owner ){
		if (!(owner instanceof Hero)) return 0;
		Hero hero = (Hero) owner;
		int reach = 0;
		if (hero.heroClass != HeroClass.ADVENTURER && hero.hasTalent(Talent.LONG_MACHETE)) {
			reach += hero.pointsInTalent(Talent.LONG_MACHETE);
		}
		return reach;
	}

	//KindOfWeapon.canReach, the Explorer reaches further with ropes, the book of disintegration for anyone
	public static int canReachBonus( Char owner ){
		if (!(owner instanceof Hero)) return 0;
		Hero hero = (Hero) owner;
		int reach = 0;
		if (hero.subClass == HeroSubClass.EXPLORER && hero.belongings.getItem(Rope.class) != null) {
			reach += hero.belongings.getItem(Rope.class).quantity();
		}
		BookOfDisintegration.ReachBuff buff = hero.buff(BookOfDisintegration.ReachBuff.class);
		if (buff != null) {
			reach += 1+buff.getUpgrade();
		}
		return reach;
	}

	//Weapon.buffedLvl
	public static int enhancedLevel( Weapon w, int lvl ){
		Hero hero = Dungeon.hero;
		if (hero != null) {
			WeaponEnhance weaponEnhance = hero.buff(WeaponEnhance.class);
			if (weaponEnhance != null && w.isEquipped(hero)) {
				lvl = weaponEnhance.weaponLevel(lvl);
			}
		}
		return lvl;
	}

	//Weapon.Enchantment.genericProcChanceMultiplier
	public static float procChance( Char attacker, float multi ){
		if (attacker instanceof Hero && ((Hero) attacker).belongings.attackingWeapon() instanceof Gun.Bullet) {
			multi *= ((Gun.Bullet) ((Hero) attacker).belongings.attackingWeapon()).whatEnchant().enchantFactor();
		}
		if (attacker.buff(TrueRunicBlade.TrueRunicSlashTracker.class) != null){
			multi += 3f;
			attacker.buff(TrueRunicBlade.TrueRunicSlashTracker.class).detach();
		}
		if (attacker.buff(MagicalBow.MagicalArrowTracker.class) != null){
			multi += attacker.buff(MagicalBow.MagicalArrowTracker.class).getMulti();
			attacker.buff(MagicalBow.MagicalArrowTracker.class).detach();
		}
		if (attacker instanceof Hero && ((Hero) attacker).hasTalent(Talent.HIGH_POWER) && ((Hero) attacker).heroClass != HeroClass.MEDIC) {
			multi += 0.1f * ((Hero) attacker).pointsInTalent(Talent.HIGH_POWER);
		}
		return multi;
	}

	//Armor.DRMax, each afterimage glyph (on the armor or the Knight's shield) lowers the armor's tier by one
	public static int armorUpgradeFactor( com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor armor, int tier ){
		Hero hero = Dungeon.hero;
		if (hero == null) return tier;
		if (armor.hasGlyph(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage.class, hero)) tier--;
		com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield shield = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield.class);
		if (shield != null && shield.hasGlyph(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage.class, hero)) tier--;
		return tier;
	}

	public static int armorAdaption( com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor armor ){
		Hero hero = Dungeon.hero;
		if (hero != null && hero.hasTalent(Talent.ARMOR_ADAPTION) && armor.STRReq() < hero.STR()) {
			return Math.round((hero.STR() - armor.STRReq())*(0.5f+0.5f*hero.pointsInTalent(Talent.ARMOR_ADAPTION)));
		}
		return 0;
	}

	//Armor AC_SCRAP, the Gunner melts armor into liquid metal, half for cursed or unknown armor
	public static void scrapArmor( final com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor armor, final Hero hero ){
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				int level = armor.isIdentified() ? armor.level() : 0;
				final int amount = Math.round(5 * (armor.tier+1) * (float)Math.pow(2, Math.min(3, level)));
				GameScene.show(
						new WndOptions( new ItemSprite(armor),
								Messages.get(MeleeWeapon.class, "scrap_title"),
								Messages.get(MeleeWeapon.class, "scrap_desc", amount),
								Messages.get(MeleeWeapon.class, "scrap_yes"),
								Messages.get(MeleeWeapon.class, "scrap_no") ) {
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
								if (index == 0 && elapsed > 0.2f) {
									LiquidMetal metal = new LiquidMetal();
									int q = amount;
									if (armor.cursed || !armor.isIdentified()) q /= 2;
									metal.quantity(q);
									if (!metal.doPickUp(hero)) {
										Dungeon.level.drop( metal, hero.pos ).sprite.drop();
									}
									EnergyCrystal crystal = new EnergyCrystal();
									crystal.quantity(Random.IntRange(1, 2));
									crystal.doPickUp(hero);
									armor.detach(hero.belongings.backpack);
									hero.sprite.operate(hero.pos);
									GLog.p(Messages.get(MeleeWeapon.class, "scrap", q));
									Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
									Item.updateQuickslot();
								}
							}
						}
				);
			}
		});
	}

	//Pickaxe AC_INFUSE, a battle shovel and the pickaxe become the miner's tool
	public static void infuseShovel( final com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe pickaxe, final Hero hero ){

		GameScene.selectItem( new com.shatteredpixel.shatteredpixeldungeon.windows.WndBag.ItemSelector() {
			@Override
			public String textPrompt() {
				return Messages.get(com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe.class, "infuse_title");
			}

			@Override
			public Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag> preferredBag(){
				return null;
			}

			@Override
			public boolean itemSelectable(Item item) {
				return item instanceof BattleShovel && item.isIdentified();
			}

			@Override
			public void onSelect( Item item ) {
				if (!(item instanceof Weapon)) return;
				Weapon w = (Weapon) item;
				Weapon n = new MinersTool();
				int level = w.level();
				if (w.curseInfusionBonus) level--;
				if (level > 0) {
					n.upgrade( level );
				} else if (level < 0) {
					n.degrade( -level );
				}
				n.enchantment = w.enchantment;
				n.curseInfusionBonus = w.curseInfusionBonus;
				n.masteryPotionBonus = w.masteryPotionBonus;
				n.levelKnown = w.levelKnown;
				n.cursedKnown = w.cursedKnown;
				n.cursed = w.cursed;
				n.augment = w.augment;

				if (item.isEquipped(hero)){
					item.cursed = false;
					((com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem)item).doUnequip(hero, false);
					n.doEquip(hero);
				} else {
					item.detach(hero.belongings.backpack);
					if (!n.collect()){
						Dungeon.level.drop(n, hero.pos).sprite.drop();
					}
				}
				if (n.isIdentified()){
					com.shatteredpixel.shatteredpixeldungeon.journal.Catalog.setSeen(n.getClass());
				}
				Sample.INSTANCE.play( Assets.Sounds.EVOKE );
				hero.sprite.operate( hero.pos );
				CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.STAR ), 10 );
				GLog.p( Messages.get(com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe.class, "infuse") );
				pickaxe.detach(hero.belongings.backpack);
				Item.updateQuickslot();
			}
		});
	}

	//MeleeWeapon AC_SCRAP. The Gunner turns a weapon into liquid metal and energy, the Archer into a thrown weapon
	public static void scrap( final MeleeWeapon wep, final Hero hero ){
		if (hero.heroClass == HeroClass.GUNNER) {
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					int level = wep.isIdentified() ? wep.level() : 0;
					final int amount = Math.round(5 * (wep.tier+1) * (float)Math.pow(2, Math.min(3, level)));
					GameScene.show(
							new WndOptions( new ItemSprite(wep),
									Messages.get(MeleeWeapon.class, "scrap_title"),
									Messages.get(MeleeWeapon.class, "scrap_desc", amount),
									Messages.get(MeleeWeapon.class, "scrap_yes"),
									Messages.get(MeleeWeapon.class, "scrap_no") ) {

								private float elapsed = 0f;

								@Override
								public synchronized void update() {
									super.update();
									elapsed += Game.elapsed;
								}

								@Override
								public void hide() {
									if (elapsed > 0.2f){
										super.hide();
									}
								}

								@Override
								protected void onSelect( int index ) {
									if (index == 0 && elapsed > 0.2f) {
										LiquidMetal metal = new LiquidMetal();
										metal.quantity(amount);
										if (!metal.doPickUp(hero)) {
											Dungeon.level.drop( metal, hero.pos ).sprite.drop();
										}

										EnergyCrystal crystal = new EnergyCrystal();
										crystal.quantity(Random.IntRange(1, 2));
										crystal.doPickUp(hero);

										wep.detach(hero.belongings.backpack);

										hero.sprite.operate(hero.pos);
										GLog.p(Messages.get(MeleeWeapon.class, "scrap", amount));
										Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

										Item.updateQuickslot();
									}
								}
							}
					);
				}
			});
		} else if (hero.heroClass == HeroClass.ARCHER) {
			wep.detach(hero.belongings.backpack);
			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.EVOKE);
			CellEmitter.center( hero.pos ).burst( Speck.factory( Speck.STAR ), 7 );
			GLog.p(Messages.get(MeleeWeapon.class, "scrap_archer"));
			MissileWeapon result = Generator.randomMissile(true);
			if (!result.doPickUp(hero, hero.pos)) {
				Dungeon.level.drop(result, hero.pos).sprite.drop();
			} else {
				GLog.i(Messages.get(Dungeon.hero, "you_now_have", result.name()));
				hero.spend(-1);
			}
			Item.updateQuickslot();
		}
	}
}
