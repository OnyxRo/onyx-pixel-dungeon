/*
 * Onyx's Pixel Dungeon
 *
 * The eleven students of Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon) as hero
 * classes, ported 2026-10-22. Starting kits, armor abilities, pictures. HeroClass calls these with one line each.
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;


import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBABatteryChange;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBADivision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.aris.RBAExtendedLaser;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBAChase;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBAShieldParry;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.hoshino.RBASpikeShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.izuna.RBABlink;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.izuna.RBASmokeSpread;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.izuna.RBAThrowingThunder;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAIdolConcert;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBALightMagazineReload;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBAPerfectDeception;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBARollCakeThrow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mika.RBASuperMeteor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.RBACloseAirSupport;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.RBAHelicopter;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyako.RBAWireHook;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAAntiMaterialRifle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAFlashbang;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.miyu.RBAHPBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.RBAHologram;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.RBASuperNovaBeam;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.noa.RBATrapDuplicate;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBABipod;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBABlackCard;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.nonomi.RBAPresent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAGPSRoute;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAPenetrationShot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAWeakeningGrenade;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.RBACabinet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.RBAPVP;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.yuzu.RBAVIPMembership;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.RBAConversionKit;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAClaymore;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAHandGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBALaptop;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAArisArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAHoshinoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAIzunaArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMariArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMikaArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMiyakoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMiyuArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBANoaArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBANonomiArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAShirokoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAYuzuArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBANinjaCape;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBAScrunchie;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RBASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.RBAAssassinsKunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RBAHeroes {

	public static void init( HeroClass cls, Hero hero ){
		switch (cls){
			case RBA_ARIS: default: initAris(hero); break;
			case RBA_NONOMI:  initNonomi(hero); break;
			case RBA_MIYAKO:  initMiyako(hero); break;
			case RBA_HOSHINO: initHoshino(hero); break;
			case RBA_SHIROKO: initShiroko(hero); break;
			case RBA_NOA:     initNoa(hero); break;
			case RBA_MIYU:    initMiyu(hero); break;
			case RBA_YUZU:    initYuzu(hero); break;
			case RBA_IZUNA:   initIzuna(hero); break;
			case RBA_MIKA:    initMika(hero); break;
			case RBA_MARI:    initMari(hero); break;
		}
	}

	private static void initAris(Hero hero) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		RBASuperNova superNova = new RBASuperNova();
		superNova.collect();
		Dungeon.quickslot.setSlot(0, superNova);

		ThrowingStone stones = new ThrowingStone();
		stones.identify().quantity(3).collect();
		Dungeon.quickslot.setSlot(1, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
			Catalog.setSeen(BrokenSeal.class);
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initNonomi(Hero hero) {
		RBAMG_T1 mgT1 = new RBAMG_T1();
		(hero.belongings.weapon = mgT1).identify();
		Dungeon.quickslot.setSlot(0, mgT1);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMiyako(Hero hero) {
		RBASMG_T1 smgT1 = new RBASMG_T1();
		(hero.belongings.weapon = smgT1).identify();
		Dungeon.quickslot.setSlot(0, smgT1);
		RBAClaymore claymore = new RBAClaymore();
		claymore.collect();
		Dungeon.quickslot.setSlot(1, claymore);
		RBAHandGrenade handGrenade = new RBAHandGrenade();
		handGrenade.collect();
		Dungeon.quickslot.setSlot(2, handGrenade);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initHoshino(Hero hero) {
		RBASG_T1 sgT1 = new RBASG_T1();
		(hero.belongings.weapon = sgT1).identify();
		Dungeon.quickslot.setSlot(0, sgT1);
		RBAIronHorus ironHorus = new RBAIronHorus();
		ironHorus.collect();
		Dungeon.quickslot.setSlot(1, ironHorus);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initShiroko(Hero hero) {
		RBAAR_T1 arT1 = new RBAAR_T1();
		(hero.belongings.weapon = arT1).identify();
		Dungeon.quickslot.setSlot(0, arT1);

		RBABicycle bicycle = new RBABicycle();
		bicycle.collect();
		Dungeon.quickslot.setSlot(1, bicycle);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initNoa(Hero hero) {
		RBAHG_T1 hgT1 = new RBAHG_T1();
		(hero.belongings.weapon = hgT1).identify();
		Dungeon.quickslot.setSlot(0, hgT1);

		((RBAGun) hero.belongings.weapon).affixKit(new RBAConversionKit());
		((RBAGun) hero.belongings.weapon).quickReload();
		Catalog.setSeen(RBAConversionKit.class);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMiyu(Hero hero) {
		RBASR_T1 srT1 = new RBASR_T1();
		(hero.belongings.weapon = srT1).identify();

		ThrowingKnife knives = new ThrowingKnife();
		knives.identify().quantity(3).collect();

		RBATrashBin bin = new RBATrashBin();
		bin.collect();

		Dungeon.quickslot.setSlot(0, srT1);
		Dungeon.quickslot.setSlot(1, bin);
		Dungeon.quickslot.setSlot(2, knives);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initYuzu(Hero hero) {
		RBAGL_T1 glT1 = new RBAGL_T1();
		(hero.belongings.weapon = glT1).identify();
		RBALaptop laptop = new RBALaptop();
		laptop.collect();

		Dungeon.quickslot.setSlot(0, glT1);
		Dungeon.quickslot.setSlot(1, laptop);

		Buff.affect(hero, RBAYuzuStatus.class);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initIzuna(Hero hero) {
		RBASMG_T1 smgT1 = new RBASMG_T1();
		(hero.belongings.weapon = smgT1).identify();

		RBANinjaCape cape = new RBANinjaCape();
		(hero.belongings.artifact = cape).identify();
		hero.belongings.artifact.activate( hero );

		RBAAssassinsKunai kunai = new RBAAssassinsKunai();
		kunai.identify().collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.identify().quantity(3).collect();

		Dungeon.quickslot.setSlot(0, smgT1);
		Dungeon.quickslot.setSlot(1, cape);
		Dungeon.quickslot.setSlot(2, kunai);
		Dungeon.quickslot.setSlot(3, knives);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMika(Hero hero) {
		RBASMG_T1 smgT1 = new RBASMG_T1();
		(hero.belongings.weapon = smgT1).identify();

		RBAScrunchie scrunchie = new RBAScrunchie();
		(hero.belongings.artifact = scrunchie).identify();
		hero.belongings.artifact.activate( hero );

		Dungeon.quickslot.setSlot(0, smgT1);
		Dungeon.quickslot.setSlot(1, scrunchie);

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMari(Hero hero) {
		RBAHG_T1 hgT1 = new RBAHG_T1();
		(hero.belongings.weapon = hgT1).identify();

		RBACrossNecklace necklace = new RBACrossNecklace();
		(hero.belongings.artifact = necklace).identify();
		hero.belongings.artifact.activate( hero );

		Dungeon.quickslot.setSlot(0, necklace);
		Dungeon.quickslot.setSlot(1, hgT1);

		new PotionOfPurity().identify();
		new ScrollOfRemoveCurse().identify();
	}

	public static ArmorAbility[] armorAbilities( HeroClass cls ){
		switch (cls) {
			case RBA_ARIS: default:
				return new ArmorAbility[]{new RBAExtendedLaser(), new RBADivision(), new RBABatteryChange()};
			case RBA_NONOMI:
				return new ArmorAbility[]{new RBAPresent(), new RBABlackCard(), new RBABipod()};
			case RBA_MIYAKO:
				return new ArmorAbility[]{new RBAWireHook(), new RBAHelicopter(), new RBACloseAirSupport()};
			case RBA_HOSHINO:
				return new ArmorAbility[]{new RBAChase(), new RBASpikeShield(), new RBAShieldParry()};
			case RBA_SHIROKO:
				return new ArmorAbility[]{new RBAPenetrationShot(), new RBAWeakeningGrenade(), new RBAGPSRoute()};
			case RBA_NOA:
				return new ArmorAbility[]{new RBATrapDuplicate(), new RBAHologram(), new RBASuperNovaBeam()};
			case RBA_MIYU:
				return new ArmorAbility[]{new RBAFlashbang(), new RBAHPBullet(), new RBAAntiMaterialRifle()};
			case RBA_YUZU:
				return new ArmorAbility[]{new RBAPVP(), new RBACabinet(), new RBAVIPMembership()};
			case RBA_IZUNA:
				return new ArmorAbility[]{new RBASmokeSpread(), new RBABlink(), new RBAThrowingThunder()};
			case RBA_MIKA:
				return new ArmorAbility[]{new RBARollCakeThrow(), new RBASuperMeteor(), new RBAPerfectDeception()};
			case RBA_MARI:
				return new ArmorAbility[]{new RBALightMagazineReload(), new RBAMariTrinity(), new RBAIdolConcert()};
		}
	}

	public static String spritesheet( HeroClass cls ){
		switch (cls) {
			case RBA_ARIS: default: return Assets.Sprites.RBA_ARIS;
			case RBA_NONOMI:  return Assets.Sprites.RBA_NONOMI;
			case RBA_MIYAKO:  return Assets.Sprites.RBA_MIYAKO;
			case RBA_HOSHINO: return Assets.Sprites.RBA_HOSHINO;
			case RBA_SHIROKO: return Assets.Sprites.RBA_SHIROKO;
			case RBA_NOA:     return Assets.Sprites.RBA_NOA;
			case RBA_MIYU:    return Assets.Sprites.RBA_MIYU;
			case RBA_YUZU:    return Assets.Sprites.RBA_YUZU;
			case RBA_IZUNA:   return Assets.Sprites.RBA_IZUNA;
			case RBA_MIKA:    return Assets.Sprites.RBA_MIKA;
			case RBA_MARI:    return Assets.Sprites.RBA_MARI;
		}
	}

	public static String splashArt( HeroClass cls ){
		switch (cls) {
			case RBA_ARIS: default: return Assets.Splashes.RBA_ARIS;
			case RBA_NONOMI:  return Assets.Splashes.RBA_NONOMI;
			case RBA_MIYAKO:  return Assets.Splashes.RBA_MIYAKO;
			case RBA_HOSHINO: return Assets.Splashes.RBA_HOSHINO;
			case RBA_SHIROKO: return Assets.Splashes.RBA_SHIROKO;
			case RBA_NOA:     return Assets.Splashes.RBA_NOA;
			case RBA_MIYU:    return Assets.Splashes.RBA_MIYU;
			case RBA_YUZU:    return Assets.Splashes.RBA_YUZU;
			case RBA_IZUNA:   return Assets.Splashes.RBA_IZUNA;
			case RBA_MIKA:    return Assets.Splashes.RBA_MIKA;
			case RBA_MARI:    return Assets.Splashes.RBA_MARI;
		}
	}

	/** The student's class armor, made from her armor when she uses a Tengu mask's chosen path later. */
	public static ClassArmor classArmor( HeroClass cls ){
		switch (cls) {
			case RBA_ARIS: default: return new RBAArisArmor();
			case RBA_NONOMI:  return new RBANonomiArmor();
			case RBA_MIYAKO:  return new RBAMiyakoArmor();
			case RBA_HOSHINO: return new RBAHoshinoArmor();
			case RBA_SHIROKO: return new RBAShirokoArmor();
			case RBA_NOA:     return new RBANoaArmor();
			case RBA_MIYU:    return new RBAMiyuArmor();
			case RBA_YUZU:    return new RBAYuzuArmor();
			case RBA_IZUNA:   return new RBAIzunaArmor();
			case RBA_MIKA:    return new RBAMikaArmor();
			case RBA_MARI:    return new RBAMariArmor();
		}
	}

	/** The icon for the student on the hero select screen and in rankings, her signature item. */
	public static int icon( HeroClass cls ){
		switch (cls) {
			case RBA_ARIS: default: return ItemSpriteSheet.SEAL;
			case RBA_NONOMI:  return ItemSpriteSheet.GOLD;
			case RBA_MIYAKO:  return ItemSpriteSheet.RBA_CLAYMORE;
			case RBA_HOSHINO: return ItemSpriteSheet.RBA_IRON_HORUS;
			case RBA_SHIROKO: return ItemSpriteSheet.RBA_BICYCLE;
			case RBA_NOA:     return ItemSpriteSheet.RBA_CONVERSION_KIT;
			case RBA_MIYU:    return ItemSpriteSheet.RBA_TRASH_BIN;
			case RBA_YUZU:    return ItemSpriteSheet.RBA_LAPTOP;
			case RBA_IZUNA:   return ItemSpriteSheet.RBA_ASSASSIN_KUNAI;
			case RBA_MIKA:    return ItemSpriteSheet.RBA_SCRUNCHIE;
			case RBA_MARI:    return ItemSpriteSheet.RBA_CROSS;
		}
	}
	/** The pictures beside each paragraph of the student's description in the hero info window. */
	public static com.watabou.noosa.Image[] infoIcons( HeroClass cls ){
		switch (cls){
			case RBA_ARIS: default:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.SEAL),
						new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD),
						new ItemSprite(ItemSpriteSheet.RBA_SUPER_NOVA),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_NONOMI:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.GOLD),
						new TalentIcon(Talent.RBA_NONOMI_T1_2),
						new ItemSprite(ItemSpriteSheet.RBA_MG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_MIYAKO:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_CLAYMORE),
						new TalentIcon(Talent.RBA_MIYAKO_T1_2),
						new ItemSprite(ItemSpriteSheet.RBA_SMG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_HOSHINO:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_IRON_HORUS),
						new TalentIcon(Talent.RBA_HOSHINO_T1_2),
						new ItemSprite(ItemSpriteSheet.RBA_SG_TIER_2),
						new ItemSprite(ItemSpriteSheet.RBA_SG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_SHIROKO:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_BICYCLE),
						new TalentIcon(Talent.RBA_SHIROKO_T1_2),
						new TalentIcon(Talent.RBA_SHIROKO_T1_3),
						new ItemSprite(ItemSpriteSheet.RBA_AR_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_NOA:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_CONVERSION_KIT),
						new TalentIcon(Talent.RBA_NOA_T1_2),
						new TalentIcon(Talent.RBA_NOA_T2_3),
						new ItemSprite(ItemSpriteSheet.RBA_HG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_MIYU:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_TRASH_BIN),
						new TalentIcon(Talent.RBA_MIYU_T1_2),
						com.shatteredpixel.shatteredpixeldungeon.ui.Icons.GRASS.get(),
						new ItemSprite(ItemSpriteSheet.RBA_SR_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_YUZU:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_LAPTOP),
						new TalentIcon(Talent.RBA_YUZU_T1_2),
						new HeroIcon(new RBAVIPMembership()),
						new ItemSprite(ItemSpriteSheet.RBA_GL_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_IZUNA:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_NINJA_CAPE),
						new ItemSprite(ItemSpriteSheet.MISSILE_HOLDER),
						new ItemSprite(ItemSpriteSheet.RBA_ASSASSIN_KUNAI),
						new ItemSprite(ItemSpriteSheet.RBA_SMG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_MIKA:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_SCRUNCHIE),
						new ItemSprite(ItemSpriteSheet.RBA_SMG_PLACEHOLDER),
						new ItemSprite(ItemSpriteSheet.RBA_SMG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
			case RBA_MARI:
				return new com.watabou.noosa.Image[]{ new ItemSprite(ItemSpriteSheet.RBA_CROSS),
						new ItemSprite(ItemSpriteSheet.RBA_HG_PLACEHOLDER),
						new ItemSprite(ItemSpriteSheet.RBA_HG_TIER_1),
						new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
		}
	}
}
