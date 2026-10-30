/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Cudgel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK ),
	CLERIC( HeroSubClass.PRIEST, HeroSubClass.PALADIN ),
	//from Devoted PD
	ZEALOT( HeroSubClass.CONJURER, HeroSubClass.DEVOTEE ),
	//from Pixel Dungeon Reforged
	ARTIFICER( HeroSubClass.CONSTRUCTOR, HeroSubClass.ARMORER ),
	//from Ryuse's PD
	SUMMONER( HeroSubClass.EIDOLON, HeroSubClass.ATTRACTOR ),
	//from Elemental PD
	ALCHEMIST( HeroSubClass.ELEMENTALIST, HeroSubClass.SCIENTIST ),
	//from Dragonic PD
	VIKING( HeroSubClass.DRUID, HeroSubClass.ICEBREAKER ),
	DRAGONKNIGHT( HeroSubClass.DEVORANDUM, HeroSubClass.MESSOREM ),
	//from Transformation PD
	FRIAR( HeroSubClass.ALCHEMIST, HeroSubClass.PIOUS ),
	SLIMEGIRL( HeroSubClass.WATERSLIME, HeroSubClass.DARKSLIME ),
	NINJA( HeroSubClass.TATTEKI_NINJA, HeroSubClass.NINJA_MASTER ),
	DM400( HeroSubClass.AT400, HeroSubClass.AU400 ),
	PRINCESS( HeroSubClass.RUNEMAGE, HeroSubClass.COMBATMASTER ),
	//rearranged, from Re-ARranged PD
	GUNNER( HeroSubClass.OUTLAW, HeroSubClass.GUNSLINGER, HeroSubClass.SPECIALIST ),
	SAMURAI( HeroSubClass.SLASHER, HeroSubClass.MASTER, HeroSubClass.SLAYER ),
	ADVENTURER( HeroSubClass.ENGINEER, HeroSubClass.EXPLORER, HeroSubClass.RESEARCHER ),
	KNIGHT( HeroSubClass.DEATHKNIGHT, HeroSubClass.HORSEMAN, HeroSubClass.CRUSADER ),
	MEDIC( HeroSubClass.SAVIOR, HeroSubClass.THERAPIST, HeroSubClass.MEDICALOFFICER ),
	ARCHER( HeroSubClass.BOWMASTER, HeroSubClass.JUGGLER, HeroSubClass.SHARPSHOOTER ),
	//summoning, Summoning PD Reincarnated
	CONJURER( HeroSubClass.SOUL_REAVER, HeroSubClass.OCCULTIST ),
	//transformation
	FREEMAN( HeroSubClass.FREEMAN ),
	//tower, Tower PD. Its heroes had no subclasses, these are Shattered's picked to suit them
	NECROHERO( HeroSubClass.WARLOCK, HeroSubClass.ASSASSIN ),
	TANK( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	TOWERPRIEST( HeroSubClass.MONK, HeroSubClass.WARDEN ),
	//bluearchive, Blue Archive PD's students, see BAHeroes
	BA_ARIS( HeroSubClass.BA_ARIS_EX_SUPERNOVA, HeroSubClass.BA_ARIS_EX_CHARGE ),
	BA_NONOMI( HeroSubClass.BA_NONOMI_EX_RIOT, HeroSubClass.BA_NONOMI_EX_SHOOTALL ),
	BA_MIYAKO( HeroSubClass.BA_MIYAKO_EX_STUNDRONE, HeroSubClass.BA_MIYAKO_EX_DRONESTRIKE ),
	BA_HOSHINO( HeroSubClass.BA_HOSHINO_EX_TACTICAL_PRESS, HeroSubClass.BA_HOSHINO_EX_TACTICAL_SHIELD ),
	BA_SHIROKO( HeroSubClass.BA_SHIROKO_EX_ELEMENTAL_BULLET, HeroSubClass.BA_SHIROKO_EX_PROFESSIONAL_RIDE ),
	BA_NOA( HeroSubClass.BA_NOA_EX_LARGE_MAGAZINE, HeroSubClass.BA_NOA_EX_DOUBLE_BARREL ),
	BA_MIYU( HeroSubClass.BA_MIYU_EX_PENETRATION_SHOT, HeroSubClass.BA_MIYU_EX_SNIPING_BULLET ),
	BA_YUZU( HeroSubClass.BA_YUZU_EX_GAME_START, HeroSubClass.BA_YUZU_EX_STICKY_GRENADE ),
	BA_IZUNA( HeroSubClass.BA_IZUNA_EX_TELEPORT, HeroSubClass.BA_IZUNA_EX_EXPLOSION ),
	//rebluearchive, the eleven students of Re Blue Archive PD
	RBA_ARIS( HeroSubClass.RBA_LIGHT_HERO, HeroSubClass.RBA_BATTERY_CHARGE ),
	RBA_NONOMI( HeroSubClass.RBA_SHOOT_ALL, HeroSubClass.RBA_SPREAD_SHOT ),
	RBA_MIYAKO( HeroSubClass.RBA_RABBIT_SQUAD, HeroSubClass.RBA_SUPPORT_DRONE ),
	RBA_HOSHINO( HeroSubClass.RBA_SHIELD_BASH, HeroSubClass.RBA_SWIFT_MOVEMENT ),
	RBA_SHIROKO( HeroSubClass.RBA_PROFESSIONAL_RIDING, HeroSubClass.RBA_BANK_ROBBER ),
	RBA_NOA( HeroSubClass.RBA_DOUBLE_BARREL, HeroSubClass.RBA_CONVERSATION ),
	RBA_MIYU( HeroSubClass.RBA_TELESCOPE, HeroSubClass.RBA_CAMOUFLAGE ),
	RBA_YUZU( HeroSubClass.RBA_AVANT_GARDE_KUN, HeroSubClass.RBA_GAME_START ),
	RBA_IZUNA( HeroSubClass.RBA_SWITCHING, HeroSubClass.RBA_CHASE ),
	RBA_MIKA( HeroSubClass.RBA_CALL_OF_STAR, HeroSubClass.RBA_LITTLE_ANGRY ),
	RBA_MARI( HeroSubClass.RBA_DEVOUT_PRAYER, HeroSubClass.RBA_SYMBOL_OF_PEACE ),
	//deranged, DERanged PD's Rat King, who counts as every class of Shattered and Re-ARranged
	RAT_KING( HeroSubClass.KING ),
	//xun, Xun's PD's three heroes
	EXPLORER( HeroSubClass.WAVECHASER, HeroSubClass.TRAPPER, HeroSubClass.ROCKSY ),
	WRAITH( HeroSubClass.INCUBUS, HeroSubClass.PLAGUEGOD, HeroSubClass.SOULHANDLER ),
	ENGINEER( HeroSubClass.CRAFTSMAN, HeroSubClass.HACKER, HeroSubClass.GRENADIER ),
	//clairvoyant, Clairvoyant PD's Apprentice, its second subclass was the Berserker in the mod too
	APPRENTICE( HeroSubClass.CHANNELLER, HeroSubClass.BERSERKER );

	//deranged, the Rat King counts as the classes he shares, everyone else only as themselves
	public boolean is( HeroClass cls ){
		return this == cls || (this == RAT_KING && DerangedKing.sharesClass(cls));
	}

	//the Summoner has two pictures, one is chosen each time the game starts
	private static final String SUMMONER_SPLASH = Math.random() < 0.5 ? Assets.Splashes.SUMMONER : Assets.Splashes.SORCERESS;

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		//Shittened PD, Finland's independence day comes with a Finnish album
		if (com.shatteredpixel.shatteredpixeldungeon.utils.Holiday.getCurrentHoliday() == com.shatteredpixel.shatteredpixeldungeon.utils.Holiday.FINNISH_INDEPENDENCE_DAY) {
			new com.shatteredpixel.shatteredpixeldungeon.items.albums.FinnishAlbum().collect();
		}

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		if (BAHeroes.isBA(this)) BAHeroes.initHero(this, hero); else //bluearchive
		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case DUELIST:
				initDuelist( hero );
				break;

			case CLERIC:
				initCleric( hero );
				break;

			case ZEALOT:
				initZealot( hero );
				break;

			case ARTIFICER:
				initArtificer( hero );
				break;

			case SUMMONER:
				initSummoner( hero );
				break;

			case ALCHEMIST:
				initAlchemist( hero );
				break;

			case VIKING:
				initViking( hero );
				break;

			case DRAGONKNIGHT:
				initDragonKnight( hero );
				break;

			case FRIAR:
				initFriar( hero );
				break;

			case SLIMEGIRL:
				initSlimeGirl( hero );
				break;

			case NINJA:
				initNinja( hero );
				break;

			case DM400:
				initDM400( hero );
				break;

			case PRINCESS:
				initPrincess( hero );
				break;

			//rearranged
			case GUNNER: case SAMURAI: case ADVENTURER: case KNIGHT: case MEDIC: case ARCHER:
				RearrangedHeroes.init( this, hero );
				break;
			//summoning
			case CONJURER:
				initConjurer( hero );
				break;
			//transformation
			case FREEMAN:
				initFreeman( hero );
				break;
			//tower
			case NECROHERO:
				initNecrohero( hero );
				break;
			case TANK:
				initTank( hero );
				break;
			case APPRENTICE: //clairvoyant
				initApprentice( hero );
				break;
			case TOWERPRIEST:
				initTowerPriest( hero );
				break;
			//rebluearchive, the students of Re Blue Archive PD
			case RBA_ARIS: case RBA_NONOMI: case RBA_MIYAKO: case RBA_HOSHINO: case RBA_SHIROKO: case RBA_NOA:
			case RBA_MIYU: case RBA_YUZU: case RBA_IZUNA: case RBA_MIKA: case RBA_MARI:
				RBAHeroes.init( this, hero );
			//deranged
			case RAT_KING:
				DerangedKing.init( hero );
			//xun
			case EXPLORER: case WRAITH: case ENGINEER:
				XunHeroes.init( this, hero );
				break;
		}

		//Pixel Dungeon Reforged, Trinket Madness starts the hero with three upgraded trinkets that stick
		if (Dungeon.isChallenged(com.shatteredpixel.shatteredpixeldungeon.Challenges.TRINKET_MADNESS)) {
			com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket[] trinkets = new com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket[3];
			for (int j = 0; j < 3; ++j) {
				boolean ok;
				int tries = 0;
				do {
					trinkets[j] = (com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.TRINKET);
					ok = true;
					for (int k = 0; k < j; ++k) {
						//no duplicates, and the clump and the mechanism cancel each other out
						if (trinkets[k].getClass() == trinkets[j].getClass()
								|| (trinkets[k] instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MossyClump && trinkets[j] instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism)
								|| (trinkets[k] instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrapMechanism && trinkets[j] instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MossyClump)) {
							ok = false;
						}
					}
				} while (!ok && ++tries < 50);
			}
			for (int j = 0; j < 3; ++j) {
				trinkets[j].upgrade(3).collect();
				trinkets[j].identify(false);
			}
		}

		//Pixel Dungeon Reforged, the Cube challenge takes every weapon away and hands over one Force Cube
		if (Dungeon.isChallenged(com.shatteredpixel.shatteredpixeldungeon.Challenges.CUBE)) {
			hero.belongings.weapon = null;
			for (com.shatteredpixel.shatteredpixeldungeon.items.Item cubeItem : new java.util.ArrayList<>(hero.belongings.backpack.items)) {
				if (cubeItem instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow
						|| cubeItem instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun
						|| cubeItem instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff) {
					cubeItem.detachAll(hero.belongings.backpack);
					Dungeon.quickslot.clearItem(cubeItem);
				}
			}
			com.shatteredpixel.shatteredpixeldungeon.items.Item fc = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube().upgrade();
			fc.collect();
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, fc);
					break;
				}
			}
		}

		//Shittened PD's prefixes never land on a hero's starting weapon
		if (hero.belongings.weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) {
			((com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) hero.belongings.weapon).reforge(com.shatteredpixel.shatteredpixeldungeon.mechanics.Prefix.NONE);
		}

		if (SPDSettings.quickslotWaterskin()) {
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, waterskin);
					break;
				}
			}
		}

	}

	public Badges.Badge masteryBadge() {
		if (BAHeroes.isBA(this)) return BAHeroes.masteryBadge(this); //bluearchive
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case DUELIST:
				return Badges.Badge.MASTERY_DUELIST;
			case CLERIC:
				return Badges.Badge.MASTERY_CLERIC;
			case ZEALOT:
				return Badges.Badge.MASTERY_ZEALOT;
			case ARTIFICER:
				return Badges.Badge.MASTERY_ARTIFICER;
			case SUMMONER:
				return Badges.Badge.MASTERY_SUMMONER;
			case ALCHEMIST:
				return Badges.Badge.MASTERY_ALCHEMIST;
			case VIKING:
				return Badges.Badge.MASTERY_VIKING;
			case DRAGONKNIGHT:
				return Badges.Badge.MASTERY_DRAGONKNIGHT;
			case FRIAR:
				return Badges.Badge.MASTERY_FRIAR;
			case SLIMEGIRL:
				return Badges.Badge.MASTERY_SLIMEGIRL;
			case NINJA:
				return Badges.Badge.MASTERY_NINJA;
			case DM400:
				return Badges.Badge.MASTERY_DM400;
			case PRINCESS:
				return Badges.Badge.MASTERY_PRINCESS;
			//rearranged
			case GUNNER:
				return Badges.Badge.MASTERY_GUNNER;
			case SAMURAI:
				return Badges.Badge.MASTERY_SAMURAI;
			case ADVENTURER:
				return Badges.Badge.MASTERY_ADVENTURER;
			case KNIGHT:
				return Badges.Badge.MASTERY_KNIGHT;
			case MEDIC:
				return Badges.Badge.MASTERY_MEDIC;
			case ARCHER:
				return Badges.Badge.MASTERY_ARCHER;
			//summoning
			case CONJURER:
				return Badges.Badge.MASTERY_CONJURER;
			case FREEMAN:
				return Badges.Badge.MASTERY_FREEMAN;
			//tower
			case NECROHERO:
				return Badges.Badge.MASTERY_NECROHERO;
			case TANK:
				return Badges.Badge.MASTERY_TANK;
			case APPRENTICE: //clairvoyant
				return Badges.Badge.MASTERY_APPRENTICE;
			case TOWERPRIEST:
				return Badges.Badge.MASTERY_TOWERPRIEST;
			//deranged
			case RAT_KING:
				return Badges.Badge.MASTERY_RAT_KING;
			//xun
			case EXPLORER:
				return Badges.Badge.MASTERY_EXPLORER;
			case WRAITH:
				return Badges.Badge.MASTERY_WRAITH;
			case ENGINEER:
				return Badges.Badge.MASTERY_ENGINEER;
		}
		return null;
	}

	//summoning, Summoning PD's Conjurer. A toy knife, a spellbook with three spells, the froggit staff,
	//a magical holster and the robe she cannot take off. Mana fills when her minions kill.
	private static void initConjurer( Hero hero ) {
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ConjurerKnife()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.bags.ConjurerBook book = new com.shatteredpixel.shatteredpixeldungeon.items.bags.ConjurerBook();
		book.collect();
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.FroggitStaff staff = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.FroggitStaff();
		staff.identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster().collect();
		Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

		(hero.belongings.armor = new com.shatteredpixel.shatteredpixeldungeon.items.armor.ConjurerArmor()).identify();
		hero.belongings.armor.activate(hero);

		new com.shatteredpixel.shatteredpixeldungeon.items.magic.Stars().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.magic.Heal().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.magic.Zap().identify().collect();
		Dungeon.quickslot.setSlot(0, staff);
		Dungeon.quickslot.setSlot(1, book);

		hero.attunement = 1;
		hero.mana = 0;
		hero.maxMana = 20;
		hero.HT = hero.HP = 13;

		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfAttunement().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength().identify();
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.identify().collect();

		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
			Catalog.setSeen(BrokenSeal.class); //as it's not added to the inventory
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.identify().collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initDuelist( Hero hero ) {

		(hero.belongings.weapon = new Rapier()).identify();
		hero.belongings.weapon.activate(hero);

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).identify().collect(); //set quantity is 3, but Duelist starts with 2

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();
	}

	private static void initCleric( Hero hero ) {

		(hero.belongings.weapon = new Cudgel()).identify();
		hero.belongings.weapon.activate(hero);

		HolyTome tome = new HolyTome();
		(hero.belongings.artifact = tome).identify();
		hero.belongings.artifact.activate( hero );

		Dungeon.quickslot.setSlot(0, tome);

		new PotionOfPurity().identify();
		new ScrollOfRemoveCurse().identify();
	}

	private static void initViking( Hero hero ) {
		//Dragonic PD. A viking mace, stones and a mind vision potion. Its own start was a debug kit of 25 scrolls and potions, this is a normal start.
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.VikingMace()).identify();
		hero.belongings.weapon.activate(hero);

		ThrowingStone stones = new ThrowingStone();
		stones.quantity(8).identify().collect();
		Dungeon.quickslot.setSlot(0, stones);

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping().identify();
	}

	private static void initDragonKnight( Hero hero ) {
		//Dragonic PD. A dragon dagger, and the breath that sears raw meat (see Sear). Its own start was a debug kit.
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DragonDagger()).identify();
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect( hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sear.class );
		hero.belongings.weapon.activate(hero);

		ThrowingStone stones = new ThrowingStone();
		stones.quantity(6).identify().collect();
		Dungeon.quickslot.setSlot(0, stones);

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror().identify();
	}

	private static void initFriar( Hero hero ) {
		//Transformation PD. A lather whip, the alchemy spray gun and a mind that can break (the Reason buff)
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LatherWhip()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.weapon.SprayGun sprayGun = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.SprayGun();
		sprayGun.identify().collect();
		Dungeon.quickslot.setSlot(0, sprayGun);

		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason.class);

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror().identify();
	}

	private static void initSlimeGirl( Hero hero ) {
		//Transformation PD. Slime armor she cannot take off, a spear and two slime balls to throw
		hero.STR = 9;
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.SlimeBall ball = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.SlimeBall();
		ball.quantity(2).identify().collect();
		Dungeon.quickslot.setSlot(0, ball);
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SpearOfConqueror()).identify();
		hero.belongings.weapon.activate(hero);
		(hero.belongings.armor = new com.shatteredpixel.shatteredpixeldungeon.items.armor.SlimeArmor()).identify();
	}

	private static void initNinja( Hero hero ) {
		//Transformation PD. A wakizashi, a shuriken box and a toxic gas potion to throw
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Wakizashi()).identify();
		hero.belongings.weapon.activate(hero);
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Shuriken_Box box = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Shuriken_Box();
		com.shatteredpixel.shatteredpixeldungeon.items.Generator.removeArtifact(box.getClass());
		(hero.belongings.artifact = box).identify();
		hero.belongings.artifact.activate( hero );
		Dungeon.quickslot.setSlot(0, box);
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation().identify();
	}

	private static void initDM400( Hero hero ) {
		//Transformation PD. A machine, so it starts with an instruction tool and two damage gears
		hero.updateHT(true);
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool tool = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool();
		com.shatteredpixel.shatteredpixeldungeon.items.Generator.removeArtifact(tool.getClass());
		(hero.belongings.artifact = tool).identify();
		hero.belongings.artifact.activate( hero );
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.DamageGear g = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.DamageGear();
		hero.addProperties(com.shatteredpixel.shatteredpixeldungeon.actors.Char.Property.INORGANIC);
		g.quantity(2).identify().collect();
		Dungeon.quickslot.setSlot(0, tool);
		Dungeon.quickslot.setSlot(1, g);
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging().identify();
	}

	private static void initPrincess( Hero hero ) {
		//Transformation PD. A bracer, the ring of the king she wears as her own, a rune string and a levitation potion
		hero.updateHT(true);
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Bracer()).identify();
		hero.belongings.weapon.activate(hero);
		com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing ring = new com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing();
		(hero.belongings.ring = ring).identify();
		hero.belongings.ring.activate(hero);
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RuneString r = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RuneString();
		r.quantity(1).identify().collect();
		Dungeon.quickslot.setSlot(0, r);
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution().identify();
	}

	//Transformation PD, the Freeman (the mod's gentleman). A walking stick, a lucky coin, the special package,
	//and talents that are blank slots to fill with scrolls of metamorphosis
	//tower, Tower PD's three heroes. Each starts with three hero spells, kept in a spell book,
	//that cast from the quickslot and then cool down
	private static void giveTowerSpells( com.shatteredpixel.shatteredpixeldungeon.items.herospells.HeroSpell... spells ) {
		new com.shatteredpixel.shatteredpixeldungeon.items.bags.HeroSpellbook().collect();
		for (int i = 0; i < spells.length; i++) {
			spells[i].collect();
			Dungeon.quickslot.setSlot( i, spells[i] );
		}
	}

	private static void initNecrohero( Hero hero ) {
		hero.STR = 11;
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger()).identify();
		hero.belongings.weapon.activate( hero );

		giveTowerSpells( new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbNecromancy(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbUndying(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbObelisk() );

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFear().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss.Seed().identify().collect();
	}

	//clairvoyant, the Apprentice's kit from Clairvoyant PD (the mod's test items left out)
	private static void initApprentice( Hero hero ) {
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Eighterstaff()).identify();
		hero.belongings.weapon.activate( hero );
		com.shatteredpixel.shatteredpixeldungeon.items.spells.SpellHand hand = new com.shatteredpixel.shatteredpixeldungeon.items.spells.SpellHand();
		hand.collect();
		Dungeon.quickslot.setSlot(0, hand);
		new com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		new com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience().identify();
	}

	private static void initTank( Hero hero ) {
		hero.STR = 13;
		(hero.belongings.armor = new com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor()).identify();
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield shield = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield();
		shield.upgrade();
		(hero.belongings.weapon = shield).identify();
		hero.belongings.weapon.activate( hero );
		hero.belongings.armor.activate( hero );

		giveTowerSpells( new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbShield(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbWallstance(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTaunt() );

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepSleep().quantity( 3 ).identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.plants.Starflower.Seed().identify().collect();
	}

	private static void initTowerPriest( Hero hero ) {
		hero.STR = 10;
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Cudgel()).identify();
		hero.belongings.weapon.activate( hero );

		giveTowerSpells( new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbPray(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbHolyWater(),
				new com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbWisp() );

		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfHolyNova().identify().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock().identify().collect();
	}

	private static void initFreeman( Hero hero ) {
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WalkStick()).identify();
		hero.belongings.weapon.activate(hero);
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.LuckyCoin coin = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.LuckyCoin();
		coin.quantity(1).identify().collect();
		Dungeon.quickslot.setSlot(0, coin);
		new com.shatteredpixel.shatteredpixeldungeon.items.SpecialPackage().collect();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation().identify();
	}

	//Elemental PD. The Alchemist starts with the alchemist dagger, the alchemist flask (an endless supply of thrown potions)
	//and a potion bandolier, and knows toxic gas and teleportation
	private static void initAlchemist( Hero hero ) {
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AlchemistDagger()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.weapon.AlchemistFlask flask = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.AlchemistFlask();
		flask.identify().collect();
		Dungeon.quickslot.setSlot(0, flask);

		if (Dungeon.LimitedDrops.POTION_BANDOLIER.count == 0){
			new com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier().collect();
			Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		}

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation().identify();
	}

	private static void initSummoner( Hero hero ) {
		//Ryuse's PD. Summoner stats, 15 health that grows by 3 a level, a training whip and a dried rose that holds her lover's ghost
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TrainingWhip()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose rose = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose();
		(hero.belongings.artifact = rose).identify();
		hero.belongings.artifact.activate( hero );
		com.shatteredpixel.shatteredpixeldungeon.items.Generator.removeArtifact(rose.getClass());

		hero.HT = hero.HP = 15;

		ThrowingStone stones = new ThrowingStone();
		stones.quantity(10).identify().collect();
		Dungeon.quickslot.setSlot(0, stones);
		Dungeon.quickslot.setSlot(1, rose);

		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost().identify();
	}

	private static void initArtificer( Hero hero ) {
		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun gun = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun();
		gun.identify().collect();
		Dungeon.quickslot.setSlot(0, gun);

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing().identify();
	}

	private static void initZealot( Hero hero ) {

		(hero.belongings.weapon = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Warpick()).identify();
		hero.belongings.weapon.activate(hero);

		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial vial = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial();
		(hero.belongings.artifact = vial).identify();
		hero.belongings.artifact.activate( hero );

		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.WoodenBoomerang rang = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.WoodenBoomerang();
		rang.quantity(1).identify().collect(); //its stack is 3, but the Zealot starts with 1

		Dungeon.quickslot.setSlot(0, vial);
		Dungeon.quickslot.setSlot(1, rang);

		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience().identify();
		new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return RearrangedRework.subClasses(this, subClasses); //rearranged
	}

	public ArmorAbility[] armorAbilities(){
		if (BAHeroes.isBA(this)) return BAHeroes.armorAbilities(this); //bluearchive
		if (RBAHero.isStudent(this)) return RBAHeroes.armorAbilities(this); //rebluearchive
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case DUELIST:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
			case CLERIC:
				return new ArmorAbility[]{new AscendedForm(), new Trinity(), new PowerOfMany()};
			case ZEALOT:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.zealot.CallingBeyond(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.zealot.EternalBalance(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.zealot.WealAndWoe()};
			case ARTIFICER:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artificer.Quickdraw(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artificer.Truesight(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.artificer.Reflection()};
			case SUMMONER:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.summoner.PhaseShift()};
			case ALCHEMIST:
				//Elemental PD gave its armor one power, Purity. The Rogue's smoke bomb joins it so the crown still gives a choice
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.alchemist.Purity(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb()};
			case VIKING:
				//Dragonic had one ability, Hammerfall. Two of the Warrior's join it so the crown still gives a choice
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.viking.Hammerfall(),
						new Endure(), new HeroicLeap()};
			case DRAGONKNIGHT:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dragonknight.DeafeningRoar(),
						new Shockwave(), new HeroicLeap()};
			case FRIAR:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.friar.ReasonDose(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.friar.ConcentratedPotion(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.friar.HolyPrayer()};
			case SLIMEGIRL:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.slime.SpringSpell(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.slime.RapidWaterfall(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.slime.MadSlime()};
			case NINJA:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.Decoy(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.SpiderJar(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ninja.OneSword()};
			case PRINCESS:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.princess.MarchForward(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.princess.TabooMagic(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.princess.KingBlade()};
			//summoning, Summoning PD had one ability. The Summoner's phase shift and the Huntress's spirit hawk join it
			case CONJURER:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.conjurer.SoulParalysisAbility(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.summoner.PhaseShift(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk()};
			case FREEMAN:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.freeman.SalesContract(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify()};
			//tower, Tower PD had no armor abilities, these are Shattered's picked to suit each hero
			case NECROHERO:
				return new ArmorAbility[]{new DeathMark(), new ShadowClone(), new WarpBeacon()};
			case TANK:
				return new ArmorAbility[]{new Endure(), new Shockwave(), new HeroicLeap()};
			case APPRENTICE: //clairvoyant, the mod's own three
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.apprentice.ApprenticeWildMagic(), new Shockwave(), new Endure()};
			case TOWERPRIEST:
				return new ArmorAbility[]{new SpiritHawk(), new WarpBeacon(), new SmokeBomb()};
			//deranged
			case RAT_KING:
				return DerangedKing.armorAbilities();
			//xun
			case EXPLORER: case WRAITH: case ENGINEER:
				return XunHeroes.armorAbilities( this );
			case DM400:
				return new ArmorAbility[]{new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.Routine(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.Discharge_Gas(),
						new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dm400.HoneyComb()};
			//rearranged
			case GUNNER: case SAMURAI: case ADVENTURER: case KNIGHT: case MEDIC: case ARCHER:
				return RearrangedHeroes.armorAbilities( this );
		}
	}

	public String spritesheet() {
		if (BAHeroes.isBA(this)) return BAHeroes.spritesheet(this); //bluearchive
		if (RBAHero.isStudent(this)) return RBAHeroes.spritesheet(this); //rebluearchive
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case DUELIST:
				return Assets.Sprites.DUELIST;
			case CLERIC:
				return Assets.Sprites.CLERIC;
			case ZEALOT:
				return Assets.Sprites.ZEALOT;
			case ARTIFICER:
				return Assets.Sprites.ARTIFICER;
			case SUMMONER:
				return Assets.Sprites.SUMMONER;
			case ALCHEMIST:
				return Assets.Sprites.ALCHEMIST;
			case VIKING:
				return Assets.Sprites.VIKING;
			case DRAGONKNIGHT:
				return Assets.Sprites.DRAGONKNIGHT;
			case FRIAR:
				return Assets.Sprites.FRIAR;
			case SLIMEGIRL:
				return Assets.Sprites.SLIMEGIRL;
			case NINJA:
				return Assets.Sprites.NINJA;
			case DM400:
				return Assets.Sprites.DM400;
			case PRINCESS:
				return Assets.Sprites.PRINCESS;
			//rearranged
			case GUNNER:
				return Assets.Sprites.GUNNER;
			case SAMURAI:
				return Assets.Sprites.SAMURAI;
			case ADVENTURER:
				return Assets.Sprites.RA_ADVENTURER;
			case KNIGHT:
				return Assets.Sprites.KNIGHT;
			case MEDIC:
				return Assets.Sprites.MEDIC;
			case ARCHER:
				return Assets.Sprites.ARCHER;
			//summoning
			case CONJURER:
				return Assets.Sprites.CONJURER;
			case FREEMAN:
				return Assets.Sprites.FREEMAN;
			//tower
			case NECROHERO:
				return Assets.Sprites.NECROHERO;
			case TANK:
				return Assets.Sprites.TANK;
			case APPRENTICE: //clairvoyant
				return Assets.Sprites.APPRENTICE;
			case TOWERPRIEST:
				return Assets.Sprites.PRIEST;
			//deranged
			case RAT_KING:
				return Assets.Sprites.RAT_KING_HERO;
			//xun
			case EXPLORER:
				return Assets.Sprites.XUN_EXPLORER;
			case WRAITH:
				return Assets.Sprites.XUN_WRAITH;
			case ENGINEER:
				return Assets.Sprites.XUN_ENGINEER;
		}
	}

	//the small picture on the hero select button. Re Blue Archive's sheets keep the standing hero in the top row,
	//the Rat King's frames are 16 wide, every other sheet has Shattered's icon cell
	public com.watabou.noosa.Image selectIcon(){
		if (RBAHero.isStudent(this)) return new com.watabou.noosa.Image(spritesheet(), 0, 0, 12, 17); //rebluearchive
		if (this == RAT_KING) return new com.watabou.noosa.Image(spritesheet(), 0, 0, 16, 17); //deranged
		return new com.watabou.noosa.Image(spritesheet(), 0, 90, 12, 15);
	}

	public String splashArt(){
		if (BAHeroes.isBA(this)) return BAHeroes.splashArt(this); //bluearchive
		if (RBAHero.isStudent(this)) return RBAHeroes.splashArt(this); //rebluearchive
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case DUELIST:
				return Assets.Splashes.DUELIST;
			case CLERIC:
				return Assets.Splashes.CLERIC;
			case ZEALOT:
				return Assets.Splashes.ZEALOT;
			case ARTIFICER:
				return Assets.Splashes.ARTIFICER;
			case SUMMONER:
				return SUMMONER_SPLASH;
			case ALCHEMIST:
				return Assets.Splashes.ALCHEMIST;
			//Dragonic had no splash art, these borrow the Warrior's and the Duelist's
			case VIKING:
				return Assets.Splashes.WARRIOR;
			case DRAGONKNIGHT:
				return Assets.Splashes.DUELIST;
			case FRIAR:
				return Assets.Splashes.FRIAR;
			case SLIMEGIRL:
				return Assets.Splashes.SLIMEGIRL;
			case NINJA:
				return Assets.Splashes.NINJA;
			case DM400:
				return Assets.Splashes.DM400;
			case PRINCESS:
				return Assets.Splashes.PRINCESS;
			//rearranged
			case GUNNER:
				return Assets.Splashes.GUNNER;
			case SAMURAI:
				return Assets.Splashes.SAMURAI;
			case ADVENTURER:
				return Assets.Splashes.ADVENTURER;
			case KNIGHT:
				return Assets.Splashes.KNIGHT;
			case MEDIC:
				return Assets.Splashes.MEDIC;
			case ARCHER:
				return Assets.Splashes.ARCHER;
			//summoning
			case CONJURER:
				return Assets.Splashes.CONJURER;
			case FREEMAN:
				return Assets.Splashes.FREEMAN;
			//tower, Tower PD had no splash art for its heroes, these borrow the closest ones
			case NECROHERO:
				return Assets.Splashes.MAGE;
			case TANK:
				return Assets.Splashes.WARRIOR;
			case APPRENTICE: //clairvoyant
				return Assets.Splashes.APPRENTICE;
			case TOWERPRIEST:
				return Assets.Splashes.CLERIC;
			//deranged
			case RAT_KING:
				return Assets.Splashes.RAT_KING;
			//xun, only the Explorer has splash art in Xun's PD, the other two use its generic hero picture
			case EXPLORER:
				return Assets.Splashes.XUN_EXPLORER;
			case WRAITH: case ENGINEER:
				return Assets.Splashes.XUN_NEWHERO;
		}
	}
	
	public static final boolean UNLOCK_ALL_FOR_NOW = true;

	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		//and in sandbox runs
		if (Dungeon.sandbox) return true;
		//for now every hero is open in normal runs too, delete this line to bring the badge unlocks back
		if (UNLOCK_ALL_FOR_NOW) return true;
		//rebluearchive, the students are open from the start as in the mod
		if (RBAHero.isStudent(this)) return true;

		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case DUELIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
			case CLERIC:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_CLERIC);
			case ZEALOT:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ZEALOT);
			case ARTIFICER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ARTIFICER);
			case SUMMONER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_SUMMONER);
			case ALCHEMIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ALCHEMIST);
			case VIKING:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_VIKING);
			case DRAGONKNIGHT:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DRAGONKNIGHT);
			case FRIAR:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_FRIAR);
			case SLIMEGIRL:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_SLIMEGIRL);
			case NINJA:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_NINJA);
			case DM400:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DM400);
			case PRINCESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_PRINCESS);
			//rearranged
			case GUNNER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_GUNNER);
			case SAMURAI:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_SAMURAI);
			case ADVENTURER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ADVENTURER);
			case KNIGHT:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_KNIGHT);
			case MEDIC:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MEDIC);
			case ARCHER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ARCHER);
			//summoning, open from the start as in Summoning PD
			case CONJURER:
				return true;
			case FREEMAN:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_FREEMAN);
			//tower
			case NECROHERO:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_NECROHERO);
			case TANK:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_TANK);
			case APPRENTICE: //clairvoyant, always open in the mod
				return true;
			case TOWERPRIEST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_TOWERPRIEST);
			//deranged, a score of 250 000 in one run, as the mod's text says
			case RAT_KING:
				return Badges.isUnlocked(Badges.Badge.HIGH_SCORE_4);
			//xun
			case EXPLORER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_EXPLORER);
			case WRAITH:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_WRAITH);
			case ENGINEER:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ENGINEER);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
