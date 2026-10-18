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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.adventurer.Root;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.adventurer.Sprout;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.adventurer.TreasureMap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.archer.DashAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.archer.Hunt;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.archer.Snipe;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.FirstAidKit;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.ReinforcedArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.Riot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.knight.HolyShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.knight.StimPack;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.knight.UnstableAnkh;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.medic.AngelWing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.medic.GammaRayEmmit;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.medic.HealingGenerator;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.samurai.Abil_Kunai;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.samurai.Awake;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.samurai.ShadowBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.ArrowBag;
import com.shatteredpixel.shatteredpixeldungeon.items.BulletBelt;
import com.shatteredpixel.shatteredpixeldungeon.items.GammaRayGun;
import com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Sheath;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MedicKit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Machete;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Saber;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scalpel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shovel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornKatana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.WornShortBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.AR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.PocketKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;

/**
 * The six heroes of Re-ARranged PD (Gunner, Samurai, Adventurer, Knight, Medic, Archer), their starting kit and
 * armor abilities, kept out of HeroClass so its switches only gain one line each. Copied from the mod's HeroClass.
 */
public class RearrangedHeroes {

	public static void init( HeroClass cls, Hero hero ){
		switch (cls){
			case GUNNER:     initGunner( hero ); break;
			case SAMURAI:    initSamurai( hero ); break;
			case ADVENTURER: initAdventurer( hero ); break;
			case KNIGHT:     initKnight( hero ); break;
			case MEDIC:      initMedic( hero ); break;
			case ARCHER:     initArcher( hero ); break;
			default: break;
		}
	}

	public static ArmorAbility[] armorAbilities( HeroClass cls ){
		switch (cls){
			case GUNNER: default:
				return new ArmorAbility[]{new Riot(), new ReinforcedArmor(), new FirstAidKit()};
			case SAMURAI:
				return new ArmorAbility[]{new Awake(), new ShadowBlade(), new Abil_Kunai()};
			case ADVENTURER:
				return new ArmorAbility[]{new Sprout(), new TreasureMap(), new Root()};
			case KNIGHT:
				return new ArmorAbility[]{new HolyShield(), new StimPack(), new UnstableAnkh()};
			case MEDIC:
				return new ArmorAbility[]{new HealingGenerator(), new AngelWing(), new GammaRayEmmit()};
			case ARCHER:
				return new ArmorAbility[]{new DashAbility(), new Hunt(), new Snipe()};
		}
	}

	private static void initGunner( Hero hero ) {
		(hero.belongings.weapon = new AR_T1()).identify();
		hero.belongings.weapon.activate(hero);

		BulletBelt bulletBelt = new BulletBelt();
		bulletBelt.quantity(5).collect();

		PocketKnife pocketKnife = new PocketKnife();
		pocketKnife.identify().collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, pocketKnife);
		Dungeon.quickslot.setSlot(2, bulletBelt);

		new PotionOfHaste().identify();
		new ScrollOfTeleportation().identify();
	}

	private static void initSamurai( Hero hero ) {
		WornKatana wornKatana = new WornKatana();
		(hero.belongings.weapon = wornKatana).identify();

		Sheath sheath = new Sheath();
		sheath.collect();

		ThrowingKnife knives = new ThrowingKnife();
		knives.identify().collect();

		Dungeon.quickslot.setSlot(0, sheath);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfRetribution().identify();
		new PotionOfStrength().identify();
	}

	private static void initAdventurer( Hero hero ) {
		Shovel shovel = new Shovel();
		(hero.belongings.weapon = shovel).identify();
		hero.belongings.weapon.activate(hero);

		Machete machete = new Machete();
		machete.identify().collect();

		ThrowingStone stones = new ThrowingStone();
		stones.identify().collect();

		Dungeon.quickslot.setSlot(0, shovel);
		Dungeon.quickslot.setSlot(1, machete);
		Dungeon.quickslot.setSlot(2, stones);

		new ScrollOfMagicMapping().identify();
		new PotionOfPurity().identify();
	}

	private static void initKnight( Hero hero ) {
		Saber saber = new Saber();
		(hero.belongings.weapon = saber).identify();
		hero.belongings.weapon.activate(hero);
		KnightsShield shield = new KnightsShield();
		shield.collect();
		ThrowingStone stones = new ThrowingStone();
		stones.identify().collect();
		Dungeon.quickslot.setSlot(0, stones);

		new ScrollOfRemoveCurse().identify();
		new PotionOfParalyticGas().identify();
	}

	private static void initMedic( Hero hero ) {
		Scalpel scalpel = new Scalpel();
		(hero.belongings.weapon = scalpel).identify();
		hero.belongings.weapon.activate(hero);

		GammaRayGun gammaRayGun = new GammaRayGun();
		gammaRayGun.collect();
		Dungeon.quickslot.setSlot(0, gammaRayGun);

		MedicKit kit = new MedicKit();
		(hero.belongings.artifact = kit).identify();
		hero.belongings.artifact.activate( hero );
		Dungeon.quickslot.setSlot(1, kit);

		new ScrollOfMirrorImage().identify();
		new PotionOfHealing().identify();
	}

	private static void initArcher( Hero hero ) {
		WornShortBow bow = new WornShortBow();
		(hero.belongings.weapon = bow).identify();
		hero.belongings.weapon.activate(hero);

		PocketKnife pocketKnife = new PocketKnife();
		pocketKnife.identify().collect();

		BulletBelt bulletBelt = new BulletBelt();
		bulletBelt.quantity(3).collect();

		ArrowBag arrowBag = new ArrowBag();
		arrowBag.collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, pocketKnife);
		Dungeon.quickslot.setSlot(2, arrowBag);
		Dungeon.quickslot.setSlot(3, bulletBelt);

		new ScrollOfMagicMapping().identify();
		new PotionOfHaste().identify();
	}
}
