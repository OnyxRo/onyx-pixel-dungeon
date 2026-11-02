/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Shittened Pixel Dungeon (Haukii), item skins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.albums.NonsenseAlbum;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.DuelistArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MageArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateCarrier;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.WarriorArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Bread;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CakeBase;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledSmallMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CheeseSlice;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChocolateCake;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CoffeeBean;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cookies;
import com.shatteredpixel.shatteredpixeldungeon.items.food.DanishPastry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Egg;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Fish;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.IceCream;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Milk;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MilkCheese;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Nuts;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Oil;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.TidePod;
import com.shatteredpixel.shatteredpixeldungeon.items.food.WaxBar;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Wheat;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Armadillo;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Car;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.FOFFSDegree;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.GoldBar;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Insulin;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.LootBag;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Painting;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Pillow;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.PinkAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.RatItem;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Saw;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.SilverAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.SprayPaint;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.VeggieBag;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Warriorium;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Willow;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFuel;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.GoblinCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDisarming;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MossyClump;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WondrousResin;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Branch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatgreatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Katana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PileOfSais;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SmallGreatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Trident;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.CoffeeBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.PotatoPlant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.plants.TomatoPlant;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkinSprites;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//Shittened PD item skins. Built by scripts/port-skins.py from the mod's Skin.java (template in
//scripts/port-skins-template.java), so edit the template and rerun the script rather than this file.
//
//Each skin is a new picture for one kind of item, made by using the PAINT action on that item and
//picking an ingredient from the bag. The ingredient is not used up. Rarity 0 is the item's usual look.
//Names and quotes are the mod's own, kept in English as it wrote them.
public enum Skin {

	SEAL(BrokenSeal.class, ItemSpriteSheet.SEAL, 0, "Red", BrokenSeal.class, "", null, -1),
	YELLOWSEAL(BrokenSeal.class, SkinSprites.YELLOWSEAL, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENSEAL(BrokenSeal.class, SkinSprites.GREENSEAL, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUESEAL(BrokenSeal.class, SkinSprites.BLUESEAL, 1, "Blue", Icecap.Seed.class, "", null, -1),
	PURPLESEAL(BrokenSeal.class, SkinSprites.PURPLESEAL, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	BLACKSEAL(BrokenSeal.class, SkinSprites.BLACKSEAL, 1, "Black", Starflower.Seed.class, "", null, -1),
	WHITESEAL(BrokenSeal.class, SkinSprites.WHITESEAL, 1, "White", Blindweed.Seed.class, "", null, -1),
	GRAYSEAL(BrokenSeal.class, SkinSprites.GRAYSEAL, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	TURQUOISESEAL(BrokenSeal.class, SkinSprites.TURQUOISESEAL, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	BROWNSEAL(BrokenSeal.class, SkinSprites.BROWNSEAL, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	PINKSEAL(BrokenSeal.class, SkinSprites.PINKSEAL, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	ORANGESEAL(BrokenSeal.class, SkinSprites.ORANGESEAL, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	WOODSEAL(BrokenSeal.class, SkinSprites.WOODSEAL, 2, "Wooden", SandalsOfNature.class, "", null, -1),
	MEATSEAL(BrokenSeal.class, SkinSprites.MEATSEAL, 2, "Meat", MysteryMeat.class, "", null, -1),
	TRANSPARENTSEAL(BrokenSeal.class, SkinSprites.TRANSPARENTSEAL, 2, "Transparent", PotionOfInvisibility.class, "", null, -1),
	HEALINGSEAL(BrokenSeal.class, SkinSprites.HEALINGSEAL, 2, "Healing", PotionOfHealing.class, "Violates Geneva conventions.", null, -1),
	CUTSEAL(BrokenSeal.class, SkinSprites.CUTSEAL, 2, "Sawn-off", Saw.class, "To demonstrate the power the Saw, I'm going to cut this seal in half!", null, -1),
	THIEFSEAL(BrokenSeal.class, SkinSprites.THIEFSEAL, 2, "Thief", MasterThievesArmband.class, "The seal is carrying a _Master Thieves Armband_. Stolen obviously.", null, -1),
	COPPERSEAL(BrokenSeal.class, SkinSprites.COPPERSEAL, 2, "Copper", Pickaxe.class, "", null, -1),
	CUBESEAL(BrokenSeal.class, SkinSprites.CUBESEAL, 2, "Cube", ForceCube.class, "", null, -1),
	RINGSEAL(BrokenSeal.class, SkinSprites.RINGSEAL, 3, "Ring", Ring.class, "", null, -1),
	RAINBOWSEAL(BrokenSeal.class, SkinSprites.RAINBOWSEAL, 3, "Rainbow", UnstableBrew.class, "Not for boring people.", null, -1),
	GOLDSEAL(BrokenSeal.class, SkinSprites.GOLDSEAL, 3, "Golden", GoldBar.class, "", null, -1),
	CORPSESEAL(BrokenSeal.class, SkinSprites.CORPSESEAL, 3, "Skull", CorpseDust.class, "", null, -1),
	HEARTSEAL(BrokenSeal.class, SkinSprites.HEARTSEAL, 3, "Heart", Rotberry.Seed.class, "", null, -1),
	BURNTSEAL(BrokenSeal.class, SkinSprites.BURNTSEAL, 3, "Burnt", Embers.class, "", null, -1),
	GOOSEAL(BrokenSeal.class, SkinSprites.GOOSEAL, 3, "Goo", GooBlob.class, "", null, -1),
	WATERSEAL(BrokenSeal.class, SkinSprites.WATERSEAL, 3, "Water", Waterskin.class, "", null, -1),
	WARRIORSEAL(BrokenSeal.class, SkinSprites.WARRIORSEAL, 3, "Warriorium", Warriorium.class, "It consumes all.", null, -1),
	COWBOYSEAL(BrokenSeal.class, SkinSprites.COWBOYSEAL, 3, "Cowboy", TengusMask.class, "'Yeehaw' -Tengu.", null, -1),
	HONEYSEAL(BrokenSeal.class, SkinSprites.HONEYSEAL, 3, "Honey", Honeypot.class, "", null, -1),
	ICESEAL(BrokenSeal.class, SkinSprites.ICESEAL, 3, "Ice", FrostBomb.class, "", null, -1),
	EYESEAL(BrokenSeal.class, SkinSprites.EYESEAL, 3, "Eye", TalismanOfForesight.class, "The Eye is watching.", null, -1),
	SNAKESEAL(BrokenSeal.class, SkinSprites.SNAKESEAL, 3, "Snake", EtherealChains.class, "", null, -1),
	CANDLESEAL(BrokenSeal.class, SkinSprites.CANDLESEAL, 3, "Candle", CeremonialCandle.class, "", null, -1),
	BOMBSEAL(BrokenSeal.class, SkinSprites.BOMBSEAL, 3, "Bomb", Bomb.class, "Be careful.", null, -1),
	SHEEPSEAL(BrokenSeal.class, SkinSprites.SHEEPSEAL, 3, "Sheep", WoollyBomb.class, "", null, -1),
	DMSEAL(BrokenSeal.class, SkinSprites.DMSEAL, 4, "Electric", MetalShard.class, "", "Electric Seal", -1),
	KINGSEAL(BrokenSeal.class, SkinSprites.KINGSEAL, 4, "Royal", KingsCrown.class, "", "Royal Seal", -1),
	FIERYSEAL(BrokenSeal.class, SkinSprites.FIERYSEAL, 5, "Fiery", Torch.class, "", "Fiery Seal", -1),
	PUREGOLDSEAL(BrokenSeal.class, SkinSprites.PUREGOLDSEAL, 4, "Pure Gold", GoldenMeat.class, "", "Pure Gold Seal", -1),
	AMULETSEAL(BrokenSeal.class, SkinSprites.AMULETSEAL, 4, "Amulet", Amulet.class, "", "Amulet Seal", -1),
	BLOWNUPSEAL(BrokenSeal.class, SkinSprites.BLOWNUPSEAL, 5, "Blown up", Torch.class, "", "Blown up Seal", -1),
	CLOAK(CloakOfShadows.class, ItemSpriteSheet.ARTIFACT_CLOAK, 0, "Dark", null, "", null, -1),
	ORANGECLOAK(CloakOfShadows.class, SkinSprites.ORANGECLOAK, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWCLOAK(CloakOfShadows.class, SkinSprites.YELLOWCLOAK, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENCLOAK(CloakOfShadows.class, SkinSprites.GREENCLOAK, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUECLOAK(CloakOfShadows.class, SkinSprites.BLUECLOAK, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISECLOAK(CloakOfShadows.class, SkinSprites.TURQUOISECLOAK, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLECLOAK(CloakOfShadows.class, SkinSprites.PURPLECLOAK, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKCLOAK(CloakOfShadows.class, SkinSprites.PINKCLOAK, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNCLOAK(CloakOfShadows.class, SkinSprites.BROWNCLOAK, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKCLOAK(CloakOfShadows.class, SkinSprites.BLACKCLOAK, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYCLOAK(CloakOfShadows.class, SkinSprites.GRAYCLOAK, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITECLOAK(CloakOfShadows.class, SkinSprites.WHITECLOAK, 1, "White", Blindweed.Seed.class, "", null, -1),
	WOODENCLOAK(CloakOfShadows.class, SkinSprites.WOODENCLOAK, 2, "Wooden", SandalsOfNature.class, "", null, -1),
	LEATHERCLOAK(CloakOfShadows.class, SkinSprites.LEATHERCLOAK, 2, "Leather", LeatherArmor.class, "", null, -1),
	RATIONCLOAK(CloakOfShadows.class, SkinSprites.RATIONCLOAK, 2, "Ration", SmallRation.class, "", null, -1),
	PINKAMULETCLOAK(CloakOfShadows.class, SkinSprites.PINKAMULETCLOAK, 2, "Extra Pink", PinkAmulet.class, "", null, -1),
	RAINBOWCLOAK(CloakOfShadows.class, SkinSprites.RAINBOWCLOAK, 2, "Rainbow", UnstableBrew.class, "", null, -1),
	TINYCLOAK(CloakOfShadows.class, SkinSprites.TINYCLOAK, 2, "Tiny", SmallGreatsword.class, "What's this? A cloak for ants?", null, -1),
	WAXCLOAK(CloakOfShadows.class, SkinSprites.WAXCLOAK, 2, "Wax", WaxBar.class, "", null, -1),
	CANDYCLOAK(CloakOfShadows.class, SkinSprites.CANDYCLOAK, 2, "Candy", Pasty.class, "", null, -1),
	COOKEDCLOAK(CloakOfShadows.class, SkinSprites.COOKEDCLOAK, 2, "Cooked", StewedMeat.class, "High protein", null, -1),
	CHAINMAILCLOAK(CloakOfShadows.class, SkinSprites.CHAINMAILCLOAK, 2, "Chainmail", MailArmor.class, "Close up you can see it's just a cheap print.", null, -1),
	TACTICALCLOAK(CloakOfShadows.class, SkinSprites.TACTICALCLOAK, 2, "Tactical", PlateCarrier.class, "M05 pattern", null, -1),
	GOLDENCLOAK(CloakOfShadows.class, SkinSprites.GOLDENCLOAK, 3, "Golden", GoldBar.class, "", null, -1),
	WARRIORCLOAK(CloakOfShadows.class, SkinSprites.WARRIORCLOAK, 3, "Warrior", Warriorium.class, "", null, -1),
	METALCLOAK(CloakOfShadows.class, SkinSprites.METALCLOAK, 3, "Metal", MetalShard.class, "", null, -1),
	BOTTLECLOAK(CloakOfShadows.class, SkinSprites.BOTTLECLOAK, 3, "Bottle", Waterskin.class, "Leaks a little when turned upside down", null, -1),
	CHEESECLOAK(CloakOfShadows.class, SkinSprites.CHEESECLOAK, 3, "Cheese", MilkCheese.class, "Too cheesy for some people", null, -1),
	PAINTCLOAK(CloakOfShadows.class, SkinSprites.PAINTCLOAK, 3, "Painted", Painting.class, "", null, -1),
	DRACULACLOAK(CloakOfShadows.class, SkinSprites.DRACULACLOAK, 3, "Dracula's", null, "", null, -1),
	FIERYCLOAK(CloakOfShadows.class, SkinSprites.FIERYCLOAK, 4, "Fiery", Torch.class, "Warms your back nicely", "Cloak of Fire", -1),
	TRUEGOLDENCLOAK(CloakOfShadows.class, SkinSprites.TRUEGOLDENCLOAK, 4, "True Golden", GoldenMeat.class, "", "True Golden Cloak", -1),
	DARKNESSCLOAK(CloakOfShadows.class, SkinSprites.DARKNESSCLOAK, 4, "Darkness", PotionOfShroudingFog.class, "", "Cloak of Darkness", -1),
	BOW(SpiritBow.class, ItemSpriteSheet.SPIRIT_BOW, 0, "Spirit", null, "", null, -1),
	ORANGEBOW(SpiritBow.class, SkinSprites.ORANGEBOW, 1, "Orange", Firebloom.Seed.class, "", null, SkinSprites.ORANGE_ARROW),
	YELLOWBOW(SpiritBow.class, SkinSprites.YELLOWBOW, 1, "Yellow", Swiftthistle.Seed.class, "", null, SkinSprites.YELLOW_ARROW),
	GREENBOW(SpiritBow.class, SkinSprites.GREENBOW, 1, "Green", Sungrass.Seed.class, "", null, SkinSprites.GREEN_ARROW),
	BLUEBOW(SpiritBow.class, SkinSprites.BLUEBOW, 1, "Blue", Icecap.Seed.class, "", null, SkinSprites.BLUE_ARROW),
	TURQUOISEBOW(SpiritBow.class, SkinSprites.TURQUOISEBOW, 1, "Turquoise", Stormvine.Seed.class, "", null, SkinSprites.TURQUOISE_ARROW),
	PURPLEBOW(SpiritBow.class, SkinSprites.PURPLEBOW, 1, "Purple", Sorrowmoss.Seed.class, "", null, SkinSprites.PURPLE_ARROW),
	BROWNBOW(SpiritBow.class, SkinSprites.BROWNBOW, 1, "Brown", Earthroot.Seed.class, "", null, SkinSprites.BROWN_ARROW),
	PINKBOW(SpiritBow.class, SkinSprites.PINKBOW, 1, "Pink", Mageroyal.Seed.class, "", null, SkinSprites.PINK_ARROW),
	BLACKBOW(SpiritBow.class, SkinSprites.BLACKBOW, 1, "Black", Starflower.Seed.class, "", null, SkinSprites.BLACK_ARROW),
	GRAYBOW(SpiritBow.class, SkinSprites.GRAYBOW, 1, "Grey", Fadeleaf.Seed.class, "", null, SkinSprites.GRAY_ARROW),
	WHITEBOW(SpiritBow.class, SkinSprites.WHITEBOW, 1, "White", Blindweed.Seed.class, "", null, SkinSprites.WHITE_ARROW),
	WOODENBOW(SpiritBow.class, SkinSprites.WOODENBOW, 2, "Wooden", SandalsOfNature.class, "", null, SkinSprites.WOODEN_ARROW),
	WALNUTBOW(SpiritBow.class, SkinSprites.WALNUTBOW, 2, "Walnut", Nuts.class, "", null, SkinSprites.WALNUT_ARROW),
	GOOBOW(SpiritBow.class, SkinSprites.GOOBOW, 2, "Goo", GooBlob.class, "", null, SkinSprites.GOO_ARROW),
	CATALYSTBOW(SpiritBow.class, SkinSprites.CATALYSTBOW, 2, "Catalyst", TrinketCatalyst.class, "The catalyst has somehow formed a stable substance.", null, SkinSprites.CATALYST_ARROW),
	TAXEDBOW(SpiritBow.class, SkinSprites.TAXEDBOW, 2, "Taxed", Food.class, "You paid your fair share.", null, SkinSprites.TAXED_ARROW),
	MEATBOW(SpiritBow.class, SkinSprites.MEATBOW, 2, "Meat", MysteryMeat.class, "", null, SkinSprites.MEAT_ARROW),
	RAINBOWBOW(SpiritBow.class, SkinSprites.RAINBOWBOW, 2, "Rainbow", UnstableBrew.class, "", null, SkinSprites.RAINBOW_ARROW),
	LOVEBOW(SpiritBow.class, SkinSprites.LOVEBOW, 2, "Love", Rotberry.Seed.class, "Kill them with love.", null, SkinSprites.LOVE_ARROW),
	EGGBOW(SpiritBow.class, SkinSprites.EGGBOW, 2, "Egg", Egg.class, "Shoots eggs instead of arrows!", null, SkinSprites.EGG_ARROW),
	WATERBOW(SpiritBow.class, SkinSprites.WATERBOW, 2, "Water", Waterskin.class, "Shoots dewdrops instead of arrows.", null, SkinSprites.WATER_ARROW),
	GOLDENBOW(SpiritBow.class, SkinSprites.GOLDENBOW, 3, "Golden", GoldBar.class, "", null, SkinSprites.GOLDEN_ARROW),
	NATUREBOW(SpiritBow.class, SkinSprites.NATUREBOW, 3, "Nature", SandalsOfNature.class, "", null, SkinSprites.NATURE_ARROW),
	METALBOW(SpiritBow.class, SkinSprites.METALBOW, 3, "Metal", MetalShard.class, "", null, SkinSprites.METAL_ARROW),
	ROYALBOW(SpiritBow.class, SkinSprites.ROYALBOW, 3, "Royal", KingsCrown.class, "", null, SkinSprites.ROYAL_ARROW),
	CROSSBOWBOW(SpiritBow.class, SkinSprites.CROSSBOWBOW, 3, "Crossbow", Crossbow.class, "", null, SkinSprites.CROSSBOW_ARROW),
	EARTHBOW(SpiritBow.class, SkinSprites.EARTHBOW, 3, "Earthen", WandOfLivingEarth.class, "", null, SkinSprites.EARTH_ARROW),
	TENGUBOW(SpiritBow.class, SkinSprites.TENGUBOW, 3, "Western", TengusMask.class, "", null, SkinSprites.TENGU_ARROW),
	WILLOWBOW(SpiritBow.class, SkinSprites.WILLOWBOW, 3, "Willow", Willow.class, "", null, SkinSprites.WILLOW_ARROW),
	RINGBOW(SpiritBow.class, SkinSprites.RINGBOW, 3, "Ring", Ring.class, "", null, SkinSprites.RING_ARROW),
	FLAMINGBOW(SpiritBow.class, SkinSprites.FLAMINGBOW, 4, "Flaming", Torch.class, "", "Flaming Bow", SkinSprites.FLAMING_ARROW),
	ASHBOW(SpiritBow.class, SkinSprites.ASHBOW, 5, "Ash", Waterskin.class, "", "Ash Bow", SkinSprites.ASH_ARROW),
	WAND(MagesStaff.class, ItemSpriteSheet.MAGES_STAFF, 0, "Mage's", null, "", null, -1),
	ORANGEWAND(MagesStaff.class, SkinSprites.ORANGEWAND, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWWAND(MagesStaff.class, SkinSprites.YELLOWWAND, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENWAND(MagesStaff.class, SkinSprites.GREENWAND, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEWAND(MagesStaff.class, SkinSprites.BLUEWAND, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEWAND(MagesStaff.class, SkinSprites.TURQUOISEWAND, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEWAND(MagesStaff.class, SkinSprites.PURPLEWAND, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKWAND(MagesStaff.class, SkinSprites.PINKWAND, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNWAND(MagesStaff.class, SkinSprites.BROWNWAND, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKWAND(MagesStaff.class, SkinSprites.BLACKWAND, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYWAND(MagesStaff.class, SkinSprites.GRAYWAND, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEWAND(MagesStaff.class, SkinSprites.WHITEWAND, 1, "White", Blindweed.Seed.class, "", null, -1),
	AMULETWAND(MagesStaff.class, SkinSprites.AMULETWAND, 2, "Amulet", SilverAmulet.class, "", null, -1),
	BENTWAND(MagesStaff.class, SkinSprites.BENTWAND, 2, "Bent", null, "Oh crap...", null, -1),
	LOVEWAND(MagesStaff.class, SkinSprites.LOVEWAND, 2, "Love", Rotberry.Seed.class, "Poke your loved ones with this", null, -1),
	GOOWAND(MagesStaff.class, SkinSprites.GOOWAND, 2, "Goo", GooBlob.class, "", null, -1),
	MISTWAND(MagesStaff.class, SkinSprites.MISTWAND, 2, "Mist", PotionOfLevitation.class, "", null, -1),
	MEATWAND(MagesStaff.class, SkinSprites.MEATWAND, 2, "Meat", MysteryMeat.class, "", null, -1),
	GOLDENWAND(MagesStaff.class, SkinSprites.GOLDENWAND, 3, "Golden", GoldBar.class, "", null, -1),
	BAGWAND(MagesStaff.class, SkinSprites.BAGWAND, 3, "Bag", VelvetPouch.class, "Best for concealed carry", null, -1),
	PICKAXEWAND(MagesStaff.class, SkinSprites.PICKAXEWAND, 3, "Pickaxe", Pickaxe.class, "", null, -1),
	ROYALWAND(MagesStaff.class, SkinSprites.ROYALWAND, 3, "Royal", KingsCrown.class, "It's heavy", null, -1),
	WALKINGWAND(MagesStaff.class, SkinSprites.WALKINGWAND, 3, "Walking Stick", TengusMask.class, "", null, -1),
	GUNWAND(MagesStaff.class, SkinSprites.GUNWAND, 3, "Gunstaff", null, "I may have run out of spells, but I haven't ran out of bullets", null, -1),
	SPLITWAND(MagesStaff.class, SkinSprites.SPLITWAND, 3, "Split", Saw.class, "What's better than a wand? Two wands!", null, -1),
	WARRIORWAND(MagesStaff.class, SkinSprites.WARRIORWAND, 3, "Warrior", Warriorium.class, "", null, -1),
	SNAKEWAND(MagesStaff.class, SkinSprites.SNAKEWAND, 3, "Snake", EtherealChains.class, "It's a smol snek", null, -1),
	METALWAND(MagesStaff.class, SkinSprites.METALWAND, 3, "Metal", MetalShard.class, "", null, -1),
	TRUEGOLDENWAND(MagesStaff.class, SkinSprites.TRUEGOLDENWAND, 4, "True Gold", GoldenMeat.class, "", null, -1),
	FIERYWAND(MagesStaff.class, SkinSprites.FIERYWAND, 4, "Fiery", PotionOfDragonsBreath.class, "", null, -1),
	MADNESSWAND(MagesStaff.class, SkinSprites.MADNESSWAND, 4, "Madness", PotionOfShroudingFog.class, "", null, -1),
	GANDALFWAND(MagesStaff.class, SkinSprites.GANDALFWAND, 4, "Gandalf's", Longsword.class, "The legendary staff of Gandalf the Grey.\n\n'Cry you fools' -Gandalf the Gay", null, -1),
	CLOUDWAND(MagesStaff.class, SkinSprites.CLOUDWAND, 5, "Cloud", PotionOfStormClouds.class, "It feels funny to move your hand through the small clouds", null, -1),
	LEATHER(LeatherArmor.class, ItemSpriteSheet.ARMOR_LEATHER, 0, "Default", LeatherArmor.class, "", null, -1),
	ORANGELEATHER(LeatherArmor.class, SkinSprites.ORANGELEATHER, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWLEATHER(LeatherArmor.class, SkinSprites.YELLOWLEATHER, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENLEATHER(LeatherArmor.class, SkinSprites.GREENLEATHER, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUELEATHER(LeatherArmor.class, SkinSprites.BLUELEATHER, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISELEATHER(LeatherArmor.class, SkinSprites.TURQUOISELEATHER, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLELEATHER(LeatherArmor.class, SkinSprites.PURPLELEATHER, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKLEATHER(LeatherArmor.class, SkinSprites.PINKLEATHER, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNLEATHER(LeatherArmor.class, SkinSprites.BROWNLEATHER, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKLEATHER(LeatherArmor.class, SkinSprites.BLACKLEATHER, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYLEATHER(LeatherArmor.class, SkinSprites.GRAYLEATHER, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITELEATHER(LeatherArmor.class, SkinSprites.WHITELEATHER, 1, "White", Blindweed.Seed.class, "", null, -1),
	PLATE(PlateArmor.class, ItemSpriteSheet.ARMOR_PLATE, 0, "Default", PlateArmor.class, "", null, -1),
	METALPLATE(PlateArmor.class, SkinSprites.METALPLATE, 1, "Metal", LiquidMetal.class, "", null, -1),
	COPPERPLATE(PlateArmor.class, SkinSprites.COPPERPLATE, 2, "Copper", Pickaxe.class, "", null, -1),
	CURSEDPLATE(PlateArmor.class, SkinSprites.CURSEDPLATE, 2, "Cursed", MetalShard.class, "", null, -1),
	GOLDPLATE(PlateArmor.class, SkinSprites.GOLDPLATE, 3, "Golden", GoldBar.class, "", null, -1),
	ROYALPLATE(PlateArmor.class, SkinSprites.ROYALPLATE, 3, "Royal", KingsCrown.class, "", null, -1),
	CLOTH(ClothArmor.class, ItemSpriteSheet.ARMOR_CLOTH, 0, "Default", ClothArmor.class, "", null, -1),
	ORANGECLOTH(ClothArmor.class, SkinSprites.ORANGECLOTH, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWCLOTH(ClothArmor.class, SkinSprites.YELLOWCLOTH, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENCLOTH(ClothArmor.class, SkinSprites.GREENCLOTH, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUECLOTH(ClothArmor.class, SkinSprites.BLUECLOTH, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISECLOTH(ClothArmor.class, SkinSprites.TURQUOISECLOTH, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLECLOTH(ClothArmor.class, SkinSprites.PURPLECLOTH, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKCLOTH(ClothArmor.class, SkinSprites.PINKCLOTH, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNCLOTH(ClothArmor.class, SkinSprites.BROWNCLOTH, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKCLOTH(ClothArmor.class, SkinSprites.BLACKCLOTH, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYCLOTH(ClothArmor.class, SkinSprites.GRAYCLOTH, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITECLOTH(ClothArmor.class, SkinSprites.WHITECLOTH, 1, "White", Blindweed.Seed.class, "", null, -1),
	LIGHTBROWNCLOTH(ClothArmor.class, SkinSprites.LIGHTBROWNCLOTH, 2, "Light Brown", PotatoPlant.Seed.class, "", null, -1),
	REDCLOTH(ClothArmor.class, SkinSprites.REDCLOTH, 2, "Red", TomatoPlant.Seed.class, "", null, -1),
	COFFEECLOTH(ClothArmor.class, SkinSprites.COFFEECLOTH, 2, "Coffee", CoffeeBush.Seed.class, "", null, -1),
	CARDINALCLOTH(ClothArmor.class, SkinSprites.CARDINALCLOTH, 2, "Cardinal Red", Rotberry.Seed.class, "", null, -1),
	GOLDENCLOTH(ClothArmor.class, SkinSprites.GOLDENCLOTH, 3, "Golden", GoldBar.class, "", null, -1),
	BOOK(UnstableSpellbook.class, ItemSpriteSheet.ARTIFACT_SPELLBOOK, 0, "Default", UnstableSpellbook.class, "", null, -1),
	ORANGEBOOK(UnstableSpellbook.class, SkinSprites.ORANGEBOOK, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWBOOK(UnstableSpellbook.class, SkinSprites.YELLOWBOOK, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENBOOK(UnstableSpellbook.class, SkinSprites.GREENBOOK, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEBOOK(UnstableSpellbook.class, SkinSprites.BLUEBOOK, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEBOOK(UnstableSpellbook.class, SkinSprites.TURQUOISEBOOK, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEBOOK(UnstableSpellbook.class, SkinSprites.PURPLEBOOK, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKBOOK(UnstableSpellbook.class, SkinSprites.PINKBOOK, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNBOOK(UnstableSpellbook.class, SkinSprites.BROWNBOOK, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKBOOK(UnstableSpellbook.class, SkinSprites.BLACKBOOK, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYBOOK(UnstableSpellbook.class, SkinSprites.GRAYBOOK, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEBOOK(UnstableSpellbook.class, SkinSprites.WHITEBOOK, 1, "White", Blindweed.Seed.class, "", null, -1),
	MASTERYBOOK(UnstableSpellbook.class, SkinSprites.MASTERYBOOK, 2, "Mastery", TengusMask.class, "", null, -1),
	LEATHERBOOK(UnstableSpellbook.class, SkinSprites.LEATHERBOOK, 2, "Leather", LeatherArmor.class, "", null, -1),
	POCKETBOOK(UnstableSpellbook.class, SkinSprites.POCKETBOOK, 2, "Pocket", SmallGreatsword.class, "", null, -1),
	BEEBOOK(UnstableSpellbook.class, SkinSprites.BEEBOOK, 2, "Save the bees!", Honeypot.class, "", null, -1),
	EGGBOOK(UnstableSpellbook.class, SkinSprites.EGGBOOK, 2, "Chicken guide", Egg.class, "", null, -1),
	BAKINGBOOK(UnstableSpellbook.class, SkinSprites.BAKINGBOOK, 2, "Baking guide", ChocolateCake.class, "", null, -1),
	LONGBOOK(UnstableSpellbook.class, SkinSprites.LONGBOOK, 2, "Large", Greatgreatsword.class, "", null, -1),
	TALLBOOK(UnstableSpellbook.class, SkinSprites.TALLBOOK, 2, "Tall", MagicalHolster.class, "Not very ergonomic", null, -1),
	WETBOOK(UnstableSpellbook.class, SkinSprites.WETBOOK, 2, "Slightly wet", Waterskin.class, "", null, -1),
	WARRIORBOOK(UnstableSpellbook.class, SkinSprites.WARRIORBOOK, 2, "Cursed", Warriorium.class, "It seems to have dried blood drops all over it", null, -1),
	PISSBOOK(UnstableSpellbook.class, SkinSprites.PISSBOOK, 2, "Unstable Bladder", ChaliceOfBlood.class, "Why the fuck did you take a piss on this rare artifact?", null, -1),
	TREEBOOK(UnstableSpellbook.class, SkinSprites.TREEBOOK, 2, "Foraging Guide", Saw.class, "", null, -1),
	RAINBOWBOOK(UnstableSpellbook.class, SkinSprites.RAINBOWBOOK, 2, "Rainbow", UnstableBrew.class, "", null, -1),
	CARBOOK(UnstableSpellbook.class, SkinSprites.CARBOOK, 2, "Car magazine", Car.class, "", null, -1),
	WESTERNBOOK(UnstableSpellbook.class, SkinSprites.WESTERNBOOK, 2, "Western magazine", TengusMask.class, "Featuring the famous Texture Will", null, -1),
	FISHBOOK(UnstableSpellbook.class, SkinSprites.FISHBOOK, 2, "Fishing guide", Fish.class, "", null, -1),
	BOWBOOK(UnstableSpellbook.class, SkinSprites.BOWBOOK, 2, "Archery guide", SpiritBow.class, "", null, -1),
	TOOLBOOK(UnstableSpellbook.class, SkinSprites.TOOLBOOK, 2, "DIY guide", null, "", null, -1),
	DVDBOOK(UnstableSpellbook.class, SkinSprites.DVDBOOK, 2, "DVD storage bag", MusicPlayer.class, "Conveniently stores up to 20 CDs", null, -1),
	OFFBOOK(UnstableSpellbook.class, SkinSprites.OFFBOOK, 2, "F off", FOFFSDegree.class, "A guide for telling people to fuck off.", null, -1),
	CRYSTALBOOK(UnstableSpellbook.class, SkinSprites.CRYSTALBOOK, 2, "Crystal guide", ExoticCrystals.class, "Reveals the secret powers of crystals.", null, -1),
	RINGBOOK(UnstableSpellbook.class, SkinSprites.RINGBOOK, 3, "Book of Rings", Ring.class, "", null, -1),
	MANIACBOOK(UnstableSpellbook.class, SkinSprites.MANIACBOOK, 3, "Maniac", TrinketCatalyst.class, "", null, -1),
	INSTRUCTIONBOOK(UnstableSpellbook.class, SkinSprites.INSTRUCTIONBOOK, 3, "Instruction pamphlet", TidePod.class, "User guide to a washing machine", null, -1),
	GOOBOOK(UnstableSpellbook.class, SkinSprites.GOOBOOK, 3, "Goobook", GooBlob.class, "", null, -1),
	GRILLBOOK(UnstableSpellbook.class, SkinSprites.GRILLBOOK, 3, "Grilling guide", ChargrilledSmallMeat.class, "", null, -1),
	GOLDBOOK(UnstableSpellbook.class, SkinSprites.GOLDBOOK, 3, "Golden spellbook", GoldBar.class, "", null, -1),
	RUNIC(RunicBlade.class, ItemSpriteSheet.RUNIC_BLADE, 0, "Default", RunicBlade.class, "", null, -1),
	ORANGERUNIC(RunicBlade.class, SkinSprites.ORANGERUNIC, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWRUNIC(RunicBlade.class, SkinSprites.YELLOWRUNIC, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENRUNIC(RunicBlade.class, SkinSprites.GREENRUNIC, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUERUNIC(RunicBlade.class, SkinSprites.BLUERUNIC, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISERUNIC(RunicBlade.class, SkinSprites.TURQUOISERUNIC, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLERUNIC(RunicBlade.class, SkinSprites.PURPLERUNIC, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKRUNIC(RunicBlade.class, SkinSprites.PINKRUNIC, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNRUNIC(RunicBlade.class, SkinSprites.BROWNRUNIC, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKRUNIC(RunicBlade.class, SkinSprites.BLACKRUNIC, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYRUNIC(RunicBlade.class, SkinSprites.GRAYRUNIC, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITERUNIC(RunicBlade.class, SkinSprites.WHITERUNIC, 1, "White", Blindweed.Seed.class, "", null, -1),
	REDRUNIC(RunicBlade.class, SkinSprites.REDRUNIC, 2, "Red", Rotberry.Seed.class, "", null, -1),
	BROKENRUNIC(RunicBlade.class, SkinSprites.BROKENRUNIC, 2, "Shattered", null, "Well...", null, -1),
	TIDEPODRUNIC(RunicBlade.class, SkinSprites.TIDEPODRUNIC, 2, "Tide pod", TidePod.class, "", null, -1),
	RAINBOWRUNIC(RunicBlade.class, SkinSprites.RAINBOWRUNIC, 2, "Rainbow", UnstableBrew.class, "", null, -1),
	OBSIDIANRUNIC(RunicBlade.class, SkinSprites.OBSIDIANRUNIC, 2, "Obsidian", AssassinsBlade.class, "", null, -1),
	CANDLERUNIC(RunicBlade.class, SkinSprites.CANDLERUNIC, 2, "Candle", CeremonialCandle.class, "", null, -1),
	MEATRUNIC(RunicBlade.class, SkinSprites.MEATRUNIC, 2, "Meaty", MysteryMeat.class, "", null, -1),
	GOORUNIC(RunicBlade.class, SkinSprites.GOORUNIC, 2, "Goo", GooBlob.class, "", null, -1),
	METALRUNIC(RunicBlade.class, SkinSprites.METALRUNIC, 2, "Metal", MetalShard.class, "", null, -1),
	CHEESERUNIC(RunicBlade.class, SkinSprites.CHEESERUNIC, 2, "Cheese", MilkCheese.class, "", null, -1),
	RESINRUNIC(RunicBlade.class, SkinSprites.RESINRUNIC, 2, "Arcane", ArcaneResin.class, "", null, -1),
	CATALYSTRUNIC(RunicBlade.class, SkinSprites.CATALYSTRUNIC, 2, "Catalyst", TrinketCatalyst.class, "", null, -1),
	EMBERRUNIC(RunicBlade.class, SkinSprites.EMBERRUNIC, 2, "Ember", Embers.class, "", null, -1),
	RUSTEDRUNIC(RunicBlade.class, SkinSprites.RUSTEDRUNIC, 2, "Oxidized", Saw.class, "", null, -1),
	SEALRUNIC(RunicBlade.class, SkinSprites.SEALRUNIC, 2, "Seal", BrokenSeal.class, "", null, -1),
	LEATHERRUNIC(RunicBlade.class, SkinSprites.LEATHERRUNIC, 2, "Leather", LeatherArmor.class, "", null, -1),
	WONDERRUNIC(RunicBlade.class, SkinSprites.WONDERRUNIC, 2, "Wondorous", WondrousResin.class, "", null, -1),
	GOBLINRUNIC(RunicBlade.class, SkinSprites.GOBLINRUNIC, 2, "Goblin", GoblinCurse.class, "", null, -1),
	PAPERRUNIC(RunicBlade.class, SkinSprites.PAPERRUNIC, 2, "Paper", ParchmentScrap.class, "", null, -1),
	PASTRYRUNIC(RunicBlade.class, SkinSprites.PASTRYRUNIC, 2, "Pastry", DanishPastry.class, "You left it in the open for too long, and now it's as hard as a rock.", null, -1),
	MOSSRUNIC(RunicBlade.class, SkinSprites.MOSSRUNIC, 2, "Mossy", MossyClump.class, "It's uncomfortably moist.", null, -1),
	GOLDENRUNIC(RunicBlade.class, SkinSprites.GOLDENRUNIC, 3, "Golden", GoldBar.class, "", null, -1),
	INSULINRUNIC(RunicBlade.class, SkinSprites.INSULINRUNIC, 3, "Insulin", Insulin.class, "", null, -1),
	ICERUNIC(RunicBlade.class, SkinSprites.ICERUNIC, 3, "Frozen", PotionOfSnapFreeze.class, "", null, -1),
	SNAKERUNIC(RunicBlade.class, SkinSprites.SNAKERUNIC, 3, "Snake", EtherealChains.class, "", null, -1),
	RINGRUNIC(RunicBlade.class, SkinSprites.RINGRUNIC, 3, "Ring", Ring.class, "The blade is made out of thin, razor sharp rings", null, -1),
	WHEATRUNIC(RunicBlade.class, SkinSprites.WHEATRUNIC, 3, "Wheat", Wheat.class, "Almost as effective as a pillow", null, -1),
	ICECREAMRUNIC(RunicBlade.class, SkinSprites.ICECREAMRUNIC, 3, "Ice cream", IceCream.class, "", null, -1),
	RATRUNIC(RunicBlade.class, SkinSprites.RATRUNIC, 3, "Rat", RatItem.class, "Rat.", null, -1),
	CRYSTALRUNIC(RunicBlade.class, SkinSprites.CRYSTALRUNIC, 3, "Crystal", ExoticCrystals.class, "", null, -1),
	TRUEGOLDRUNIC(RunicBlade.class, SkinSprites.TRUEGOLDRUNIC, 4, "True Golden", GoldenMeat.class, "", "Golden Blade", -1),
	AMULETRUNIC(RunicBlade.class, SkinSprites.AMULETRUNIC, 4, "Amulet", Amulet.class, "", "The Amulet Blade", -1),
	NATURERUNIC(RunicBlade.class, SkinSprites.NATURERUNIC, 4, "Nature", SandalsOfNature.class, "", "The Blade of Nature", -1),
	WATERRUNIC(RunicBlade.class, SkinSprites.WATERRUNIC, 4, "Water", Waterskin.class, "", "Blade of Water", -1),
	EARTHRUNIC(RunicBlade.class, SkinSprites.EARTHRUNIC, 4, "Earth", WandOfLivingEarth.class, "", "Blade of Earth", -1),
	FIRERUNIC(RunicBlade.class, SkinSprites.FIRERUNIC, 4, "Fire", PotionOfDragonsBreath.class, "", "Blade of Fire", -1),
	WINDRUNIC(RunicBlade.class, SkinSprites.WINDRUNIC, 4, "Wind", PotionOfStormClouds.class, "", "Blade of Air", -1),
	LSWORD(Longsword.class, ItemSpriteSheet.LONGSWORD, 0, "Default", Longsword.class, "", null, -1),
	ORANGELSWORD(Longsword.class, SkinSprites.ORANGELSWORD, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWLSWORD(Longsword.class, SkinSprites.YELLOWLSWORD, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENLSWORD(Longsword.class, SkinSprites.GREENLSWORD, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUELSWORD(Longsword.class, SkinSprites.BLUELSWORD, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISELSWORD(Longsword.class, SkinSprites.TURQUOISELSWORD, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLELSWORD(Longsword.class, SkinSprites.PURPLELSWORD, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKLSWORD(Longsword.class, SkinSprites.PINKLSWORD, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNLSWORD(Longsword.class, SkinSprites.BROWNLSWORD, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKLSWORD(Longsword.class, SkinSprites.BLACKLSWORD, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYLSWORD(Longsword.class, SkinSprites.GRAYLSWORD, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITELSWORD(Longsword.class, SkinSprites.WHITELSWORD, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETLSWORD(Longsword.class, SkinSprites.RUSSETLSWORD, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONLSWORD(Longsword.class, SkinSprites.MAROONLSWORD, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIALSWORD(Longsword.class, SkinSprites.SEPIALSWORD, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALLSWORD(Longsword.class, SkinSprites.CARDINALLSWORD, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	CURSEDLSWORD(Longsword.class, SkinSprites.CURSEDLSWORD, 2, "Cursed", MetalShard.class, "", null, -1),
	RUSTYLSWORD(Longsword.class, SkinSprites.RUSTYLSWORD, 2, "Rusty", Saw.class, "", null, -1),
	COPPERLSWORD(Longsword.class, SkinSprites.COPPERLSWORD, 2, "Copper", Pickaxe.class, "", null, -1),
	RAINBOWLSWORD(Longsword.class, SkinSprites.RAINBOWLSWORD, 2, "Rainbow", UnstableBrew.class, "", null, -1),
	BENTLSWORD(Longsword.class, SkinSprites.BENTLSWORD, 2, "Bent", null, "Well...", null, -1),
	RESINLSWORD(Longsword.class, SkinSprites.RESINLSWORD, 2, "Resin", ArcaneResin.class, "", null, -1),
	CATALYSTLSWORD(Longsword.class, SkinSprites.CATALYSTLSWORD, 2, "Catalyst", TrinketCatalyst.class, "", null, -1),
	EMBERLSWORD(Longsword.class, SkinSprites.EMBERLSWORD, 2, "Ember", Embers.class, "", null, -1),
	WOODENLSWORD(Longsword.class, SkinSprites.WOODENLSWORD, 2, "Wooden", MagesStaff.class, "", null, -1),
	SEALLSWORD(Longsword.class, SkinSprites.SEALLSWORD, 2, "Seal", BrokenSeal.class, "", null, -1),
	GOLDLSWORD(Longsword.class, SkinSprites.GOLDLSWORD, 3, "Golden", GoldBar.class, "", null, -1),
	RINGLSWORD(Longsword.class, SkinSprites.RINGLSWORD, 3, "Ring", Ring.class, "", null, -1),
	TINYLSWORD(Longsword.class, SkinSprites.TINYLSWORD, 3, "Tiny", SmallGreatsword.class, "So smol!", null, -1),
	MOSSYLSWORD(Longsword.class, SkinSprites.MOSSYLSWORD, 3, "Overgrown", MossyClump.class, "", null, -1),
	TRUEGOLDLSWORD(Longsword.class, SkinSprites.TRUEGOLDLSWORD, 4, "True golden", GoldenMeat.class, "", "Golden Longsword", -1),
	STARRYLSWORD(Longsword.class, SkinSprites.STARRYLSWORD, 4, "Starry", Painting.class, "", "Starry Nightsword", -1),
	WAVELSWORD(Longsword.class, SkinSprites.WAVELSWORD, 4, "Great Wave", Painting.class, "", "Great Wavesword", -1),
	GLAIVE(Glaive.class, ItemSpriteSheet.GLAIVE, 0, "Default", Glaive.class, "", null, -1),
	ORANGEGLAIVE(Glaive.class, SkinSprites.ORANGEGLAIVE, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWGLAIVE(Glaive.class, SkinSprites.YELLOWGLAIVE, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENGLAIVE(Glaive.class, SkinSprites.GREENGLAIVE, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEGLAIVE(Glaive.class, SkinSprites.BLUEGLAIVE, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEGLAIVE(Glaive.class, SkinSprites.TURQUOISEGLAIVE, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEGLAIVE(Glaive.class, SkinSprites.PURPLEGLAIVE, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKGLAIVE(Glaive.class, SkinSprites.PINKGLAIVE, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNGLAIVE(Glaive.class, SkinSprites.BROWNGLAIVE, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKGLAIVE(Glaive.class, SkinSprites.BLACKGLAIVE, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYGLAIVE(Glaive.class, SkinSprites.GRAYGLAIVE, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEGLAIVE(Glaive.class, SkinSprites.WHITEGLAIVE, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETGLAIVE(Glaive.class, SkinSprites.RUSSETGLAIVE, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONGLAIVE(Glaive.class, SkinSprites.MAROONGLAIVE, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIAGLAIVE(Glaive.class, SkinSprites.SEPIAGLAIVE, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALGLAIVE(Glaive.class, SkinSprites.CARDINALGLAIVE, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	BERRYGLAIVE(Glaive.class, SkinSprites.BERRYGLAIVE, 1, "Berry", Berry.class, "", null, -1),
	METALGLAIVE(Glaive.class, SkinSprites.METALGLAIVE, 2, "Liquid Metal", LiquidMetal.class, "", null, -1),
	RAINBOWGLAIVE(Glaive.class, SkinSprites.RAINBOWGLAIVE, 2, "Rainbow", UnstableBrew.class, "Apply again for even more rainbow.", null, -1),
	FULLRAINBOWGLAIVE(Glaive.class, SkinSprites.FULLRAINBOWGLAIVE, 2, "Extra rainbow", UnstableBrew.class, "", null, -1),
	RESINGLAIVE(Glaive.class, SkinSprites.RESINGLAIVE, 2, "Resin", ArcaneResin.class, "", null, -1),
	CATALYSTGLAIVE(Glaive.class, SkinSprites.CATALYSTGLAIVE, 2, "Catalyst", TrinketCatalyst.class, "", null, -1),
	EMBERGLAIVE(Glaive.class, SkinSprites.EMBERGLAIVE, 2, "Ember", Embers.class, "", null, -1),
	NUTGLAIVE(Glaive.class, SkinSprites.NUTGLAIVE, 2, "Peanut", Nuts.class, "", null, -1),
	STYLUSGLAIVE(Glaive.class, SkinSprites.STYLUSGLAIVE, 2, "Stylus", Stylus.class, "", null, -1),
	GOOGLAIVE(Glaive.class, SkinSprites.GOOGLAIVE, 2, "Goo", GooBlob.class, "", null, -1),
	CURSEDGLAIVE(Glaive.class, SkinSprites.CURSEDGLAIVE, 2, "Cursed", MetalShard.class, "", null, -1),
	ICECREAMGLAIVE(Glaive.class, SkinSprites.ICECREAMGLAIVE, 3, "Ice cream", IceCream.class, "", null, -1),
	NATUREGLAIVE(Glaive.class, SkinSprites.NATUREGLAIVE, 3, "Nature", SandalsOfNature.class, "", null, -1),
	ARROWGLAIVE(Glaive.class, SkinSprites.ARROWGLAIVE, 3, "Bolt", Crossbow.class, "", null, -1),
	CHAINGLAIVE(Glaive.class, SkinSprites.CHAINGLAIVE, 3, "Chain", MailArmor.class, "Not very practical.", null, -1),
	VEGGIEGLAIVE(Glaive.class, SkinSprites.VEGGIEGLAIVE, 3, "Veggie", VeggieBag.class, "", null, -1),
	TINYGLAIVE(Glaive.class, SkinSprites.TINYGLAIVE, 3, "Tiny", SmallGreatsword.class, "", null, -1),
	FROZENGLAIVE(Glaive.class, SkinSprites.FROZENGLAIVE, 3, "Frozen", PotionOfFrost.class, "", null, -1),
	SEALGLAIVE(Glaive.class, SkinSprites.SEALGLAIVE, 3, "Seal", BrokenSeal.class, "", null, -1),
	AXEGLAIVE(Glaive.class, SkinSprites.AXEGLAIVE, 3, "Axeglaive", Greataxe.class, "", null, -1),
	GREATGLAIVE(Glaive.class, SkinSprites.GREATGLAIVE, 3, "Greatglaive", Greatsword.class, "", null, -1),
	GUARDIANGLAIVE(Glaive.class, SkinSprites.GUARDIANGLAIVE, 3, "Guardian", Greatsword.class, "", null, -1),
	CLOTHGLAIVE(Glaive.class, SkinSprites.CLOTHGLAIVE, 3, "Cloth wrap", ClothArmor.class, "", null, -1),
	GOLDENGLAIVE(Glaive.class, SkinSprites.GOLDENGLAIVE, 3, "Golden", GoldBar.class, "", null, -1),
	BOMBGLAIVE(Glaive.class, SkinSprites.BOMBGLAIVE, 3, "Bomb", Bomb.class, "Light up with a torch in a last resort situation.", null, -1),
	CHARGEGLAIVE(Glaive.class, SkinSprites.CHARGEGLAIVE, 3, "CHAAARGE!", null, "", null, -1),
	CDGLAIVE(Glaive.class, SkinSprites.CDGLAIVE, 3, "Shattered CD", MusicPlayer.class, "Warning: Very sharp", null, -1),
	TRUEGOLDGLAIVE(Glaive.class, SkinSprites.TRUEGOLDGLAIVE, 4, "True golden", GoldenMeat.class, "", "True Golden Glaive", -1),
	BEEGLAIVE(Glaive.class, SkinSprites.BEEGLAIVE, 4, "Honey", Honeypot.ShatteredPot.class, "", "Glaive of Bees", -1),
	SNAKEGLAIVE(Glaive.class, SkinSprites.SNAKEGLAIVE, 4, "Snake", EtherealChains.class, "", "Medusa's Glaive", -1),
	SHROUDEDGLAIVE(Glaive.class, SkinSprites.SHROUDEDGLAIVE, 4, "Shrouded", PotionOfShroudingFog.class, "", "Glaive of Darkness", -1),
	BLOWNUPGLAIVE(Glaive.class, SkinSprites.BLOWNUPGLAIVE, 4, "Blown up", Torch.class, "", "Blown Up Glaive", -1),
	FIERYGLAIVE(Glaive.class, SkinSprites.FIERYGLAIVE, 5, "Fiery", Torch.class, "", "Fiery Glaive", -1),
	AXE(BattleAxe.class, ItemSpriteSheet.BATTLE_AXE, 0, "Default", BattleAxe.class, "", null, -1),
	ORANGEAXE(BattleAxe.class, SkinSprites.ORANGEAXE, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWAXE(BattleAxe.class, SkinSprites.YELLOWAXE, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENAXE(BattleAxe.class, SkinSprites.GREENAXE, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEAXE(BattleAxe.class, SkinSprites.BLUEAXE, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEAXE(BattleAxe.class, SkinSprites.TURQUOISEAXE, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEAXE(BattleAxe.class, SkinSprites.PURPLEAXE, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKAXE(BattleAxe.class, SkinSprites.PINKAXE, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNAXE(BattleAxe.class, SkinSprites.BROWNAXE, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKAXE(BattleAxe.class, SkinSprites.BLACKAXE, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYAXE(BattleAxe.class, SkinSprites.GRAYAXE, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEAXE(BattleAxe.class, SkinSprites.WHITEAXE, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETAXE(BattleAxe.class, SkinSprites.RUSSETAXE, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONAXE(BattleAxe.class, SkinSprites.MAROONAXE, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIAAXE(BattleAxe.class, SkinSprites.SEPIAAXE, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALAXE(BattleAxe.class, SkinSprites.CARDINALAXE, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	BERRYAXE(BattleAxe.class, SkinSprites.BERRYAXE, 1, "Berry", Berry.class, "", null, -1),
	COPPERAXE(BattleAxe.class, SkinSprites.COPPERAXE, 2, "Copper", Pickaxe.class, "", null, -1),
	RAINBOWAXE(BattleAxe.class, SkinSprites.RAINBOWAXE, 2, "Rainbow", UnstableBrew.class, "", null, -1),
	RESINAXE(BattleAxe.class, SkinSprites.RESINAXE, 2, "Resin", ArcaneResin.class, "", null, -1),
	CATALYSTAXE(BattleAxe.class, SkinSprites.CATALYSTAXE, 2, "Catalyst", TrinketCatalyst.class, "", null, -1),
	EMBERAXE(BattleAxe.class, SkinSprites.EMBERAXE, 2, "Ember", Embers.class, "", null, -1),
	NUTAXE(BattleAxe.class, SkinSprites.NUTAXE, 2, "Nutwood", Nuts.class, "", null, -1),
	GOLDAXE(BattleAxe.class, SkinSprites.GOLDAXE, 3, "Golden", GoldBar.class, "", null, -1),
	CHEESEAXE(BattleAxe.class, SkinSprites.CHEESEAXE, 3, "Melted Cheese", CheeseSlice.class, "", null, -1),
	NATUREAXE(BattleAxe.class, SkinSprites.NATUREAXE, 3, "Nature", SandalsOfNature.class, "", null, -1),
	COOKIEAXE(BattleAxe.class, SkinSprites.COOKIEAXE, 3, "Cookie cutter", Cookies.class, "", null, -1),
	MESSEDAXE(BattleAxe.class, SkinSprites.MESSEDAXE, 3, "Messed up", NonsenseAlbum.class, "", null, -1),
	KINGSAXE(BattleAxe.class, SkinSprites.KINGSAXE, 3, "King's", KingsCrown.class, "", null, -1),
	SPLITTINGAXE(BattleAxe.class, SkinSprites.SPLITTINGAXE, 3, "Splitting Axe", Saw.class, "", null, -1),
	TRUEGOLDAXE(BattleAxe.class, SkinSprites.TRUEGOLDAXE, 4, "True Golden", GoldenMeat.class, "", "True Golden Axe", -1),
	CHAINAXE(BattleAxe.class, SkinSprites.CHAINAXE, 4, "Chainaxe", MailArmor.class, "", "Chainaxe", -1),
	PILEAXE(BattleAxe.class, SkinSprites.PILEAXE, 4, "Pileaxe", PileOfSais.class, "", "Pile of Battleaxes", -1),
	HAMMER(WarHammer.class, ItemSpriteSheet.WAR_HAMMER, 0, "Default", WarHammer.class, "", null, -1),
	ORANGEHAMMER(WarHammer.class, SkinSprites.ORANGEHAMMER, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWHAMMER(WarHammer.class, SkinSprites.YELLOWHAMMER, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENHAMMER(WarHammer.class, SkinSprites.GREENHAMMER, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEHAMMER(WarHammer.class, SkinSprites.BLUEHAMMER, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEHAMMER(WarHammer.class, SkinSprites.TURQUOISEHAMMER, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEHAMMER(WarHammer.class, SkinSprites.PURPLEHAMMER, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKHAMMER(WarHammer.class, SkinSprites.PINKHAMMER, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNHAMMER(WarHammer.class, SkinSprites.BROWNHAMMER, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKHAMMER(WarHammer.class, SkinSprites.BLACKHAMMER, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYHAMMER(WarHammer.class, SkinSprites.GRAYHAMMER, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEHAMMER(WarHammer.class, SkinSprites.WHITEHAMMER, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETHAMMER(WarHammer.class, SkinSprites.RUSSETHAMMER, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONHAMMER(WarHammer.class, SkinSprites.MAROONHAMMER, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIAHAMMER(WarHammer.class, SkinSprites.SEPIAHAMMER, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALHAMMER(WarHammer.class, SkinSprites.CARDINALHAMMER, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	BERRYHAMMER(WarHammer.class, SkinSprites.BERRYHAMMER, 1, "Berry", Berry.class, "", null, -1),
	OTSUCHIHAMMER(WarHammer.class, SkinSprites.OTSUCHIHAMMER, 2, "Otsuchi", Kunai.class, "", null, -1),
	SHEEPHAMMER(WarHammer.class, SkinSprites.SHEEPHAMMER, 2, "Sheep", StoneOfFlock.class, "Baaa!", null, -1),
	BREADHAMMER(WarHammer.class, SkinSprites.BREADHAMMER, 2, "Bread", Bread.class, "Stale bread makes for an excellent blunt weapon.", null, -1),
	FREEDOMHAMMER(WarHammer.class, SkinSprites.FREEDOMHAMMER, 2, "MURICAHAMMER!!", Oil.class, "*gunshots* THEY'VE GOT OIL!!! *eagle scream*", null, -1),
	NUTHAMMER(WarHammer.class, SkinSprites.NUTHAMMER, 2, "Nut", Nuts.class, "", null, -1),
	BLASTHAMMER(WarHammer.class, SkinSprites.BLASTHAMMER, 2, "Blast", WandOfBlastWave.class, "", null, -1),
	ARCANEHAMMER(WarHammer.class, SkinSprites.ARCANEHAMMER, 2, "Arcane", TrinketCatalyst.class, "", null, -1),
	RESINHAMMER(WarHammer.class, SkinSprites.RESINHAMMER, 2, "Resin", ArcaneResin.class, "", null, -1),
	EMBERHAMMER(WarHammer.class, SkinSprites.EMBERHAMMER, 2, "Ember", Embers.class, "", null, -1),
	INVISIBLEHAMMER(WarHammer.class, SkinSprites.INVISIBLEHAMMER, 2, "Invisible", PotionOfInvisibility.class, "", null, -1),
	CAKEBASEHAMMER(WarHammer.class, SkinSprites.CAKEBASEHAMMER, 2, "Cake base", CakeBase.class, "Add a coffee bean to make a chocolate cake.", null, -1),
	OREHAMMER(WarHammer.class, SkinSprites.OREHAMMER, 2, "Gold ore", DarkGold.class, "", null, -1),
	GOLDHAMMER(WarHammer.class, SkinSprites.GOLDHAMMER, 3, "Golden", GoldBar.class, "", null, -1),
	ROYALHAMMER(WarHammer.class, SkinSprites.ROYALHAMMER, 3, "Royal", KingsCrown.class, "", null, -1),
	NAILHAMMER(WarHammer.class, SkinSprites.NAILHAMMER, 3, "Regular", Saw.class, "", null, -1),
	AXEHAMMER(WarHammer.class, SkinSprites.AXEHAMMER, 3, "Axehammer", BattleAxe.class, "", null, -1),
	STAMPHAMMER(WarHammer.class, SkinSprites.STAMPHAMMER, 3, "Seal stamp", BrokenSeal.class, "", null, -1),
	PAINTHAMMER(WarHammer.class, SkinSprites.PAINTHAMMER, 3, "Painting", Painting.class, "", null, -1),
	FROZENHAMMER(WarHammer.class, SkinSprites.FROZENHAMMER, 3, "Frozen", PotionOfFrost.class, "", null, -1),
	NATUREHAMMER(WarHammer.class, SkinSprites.NATUREHAMMER, 3, "Nature", SandalsOfNature.class, "", null, -1),
	BAGHAMMER(WarHammer.class, SkinSprites.BAGHAMMER, 3, "Baghammer", LootBag.class, "", null, -1),
	CHOCOHAMMER(WarHammer.class, SkinSprites.CHOCOHAMMER, 3, "Chocolate", CoffeeBean.class, "Add milk for a layer of cream.", null, -1),
	BITTENHAMMER(WarHammer.class, SkinSprites.BITTENHAMMER, 3, "Bitten", null, "MMMMMM... Tasty!", null, -1),
	FISHHAMMER(WarHammer.class, SkinSprites.FISHHAMMER, 3, "Fishwhacker", Fish.class, "", null, -1),
	UNSTABLEHAMMER(WarHammer.class, SkinSprites.UNSTABLEHAMMER, 3, "Unstable", UnstableSpell.class, "", "Unstable Hammer", -1),
	SICKLEHAMMER(WarHammer.class, SkinSprites.SICKLEHAMMER, 3, "Sicklehammer", Sickle.class, "Doesn't seem like a very functional pair.", null, -1),
	TRUEGOLDHAMMER(WarHammer.class, SkinSprites.TRUEGOLDHAMMER, 4, "True gold", GoldenMeat.class, "", "True Golden Hammer", -1),
	AMULETHAMMER(WarHammer.class, SkinSprites.AMULETHAMMER, 4, "Amulet", Amulet.class, "", "Amulet Hammer", -1),
	CREAMHAMMER(WarHammer.class, SkinSprites.CREAMHAMMER, 4, "Cream cake", Milk.class, "A berry on the top would make this perfect...", null, -1),
	SKULLHAMMER(WarHammer.class, SkinSprites.SKULLHAMMER, 4, "Skullcrusher", RatSkull.class, "", "Skullcrusher", -1),
	GOOLDENHAMMER(WarHammer.class, SkinSprites.GOOLDENHAMMER, 5, "Goolden", GooBlob.class, "", "Goolden Hammer", -1),
	CAKEHAMMER(WarHammer.class, SkinSprites.CAKEHAMMER, 5, "Perfect cake", Berry.class, "It looks so tasty!", "Hammer of Cake", -1),
	SHIELD(Greatshield.class, ItemSpriteSheet.GREATSHIELD, 0, "Default", Greatshield.class, "", null, -1),
	ORANGESHIELD(Greatshield.class, SkinSprites.ORANGESHIELD, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWSHIELD(Greatshield.class, SkinSprites.YELLOWSHIELD, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENSHIELD(Greatshield.class, SkinSprites.GREENSHIELD, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUESHIELD(Greatshield.class, SkinSprites.BLUESHIELD, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISESHIELD(Greatshield.class, SkinSprites.TURQUOISESHIELD, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLESHIELD(Greatshield.class, SkinSprites.PURPLESHIELD, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKSHIELD(Greatshield.class, SkinSprites.PINKSHIELD, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNSHIELD(Greatshield.class, SkinSprites.BROWNSHIELD, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKSHIELD(Greatshield.class, SkinSprites.BLACKSHIELD, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYSHIELD(Greatshield.class, SkinSprites.GRAYSHIELD, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITESHIELD(Greatshield.class, SkinSprites.WHITESHIELD, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETSHIELD(Greatshield.class, SkinSprites.RUSSETSHIELD, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONSHIELD(Greatshield.class, SkinSprites.MAROONSHIELD, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIASHIELD(Greatshield.class, SkinSprites.SEPIASHIELD, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALSHIELD(Greatshield.class, SkinSprites.CARDINALSHIELD, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	BERRYSHIELD(Greatshield.class, SkinSprites.BERRYSHIELD, 1, "Berry", Berry.class, "", null, -1),
	ANKHSHIELD(Greatshield.class, SkinSprites.ANKHSHIELD, 2, "Ankh", Ankh.class, "", null, -1),
	SWORDSHIELD(Greatshield.class, SkinSprites.SWORDSHIELD, 2, "Sword", Sword.class, "", null, -1),
	SEALSHIELD(Greatshield.class, SkinSprites.SEALSHIELD, 2, "Seal", BrokenSeal.class, "", null, -1),
	MEATSHIELD(Greatshield.class, SkinSprites.MEATSHIELD, 2, "Meaty", MysteryMeat.class, "", null, -1),
	HOLESHIELD(Greatshield.class, SkinSprites.HOLESHIELD, 2, "Peephole", Shuriken.class, "", null, -1),
	GOOSHIELD(Greatshield.class, SkinSprites.GOOSHIELD, 2, "Goo", GooBlob.class, "", null, -1),
	METALSHIELD(Greatshield.class, SkinSprites.METALSHIELD, 2, "Cursed metal", MetalShard.class, "", null, -1),
	CHEESESHIELD(Greatshield.class, SkinSprites.CHEESESHIELD, 2, "Cheeshield", CheeseSlice.class, "Very cheesy...", null, -1),
	AMULETSHIELD(Greatshield.class, SkinSprites.AMULETSHIELD, 2, "Amulet", Amulet.class, "", null, -1),
	MISSILESHIELD(Greatshield.class, SkinSprites.MISSILESHIELD, 2, "Missile", WandOfMagicMissile.class, "", null, -1),
	FIRESHIELD(Greatshield.class, SkinSprites.FIRESHIELD, 2, "Fire pattern", WandOfFireblast.class, "", null, -1),
	ICESHIELD(Greatshield.class, SkinSprites.ICESHIELD, 2, "Ice", WandOfFrost.class, "Has a translucent surface.", null, -1),
	LIGHTNINGSHIELD(Greatshield.class, SkinSprites.LIGHTNINGSHIELD, 2, "Lightning", WandOfLightning.class, "", null, -1),
	EARTHSHIELD(Greatshield.class, SkinSprites.EARTHSHIELD, 2, "Earth", WandOfLivingEarth.class, "", null, -1),
	SHATTEREDSHIELD(Greatshield.class, SkinSprites.SHATTEREDSHIELD, 2, "Shattered", Greataxe.class, "Even the greatest shields have limits.", null, -1),
	TRIDENTSHIELD(Greatshield.class, SkinSprites.TRIDENTSHIELD, 2, "Tridented", Trident.class, "", null, -1),
	LIQUIDSHIELD(Greatshield.class, SkinSprites.LIQUIDSHIELD, 2, "Liquidated", LiquidMetal.class, "", null, -1),
	CAMOSHIELD(Greatshield.class, SkinSprites.CAMOSHIELD, 2, "Multicam", PlateCarrier.class, "", null, -1),
	GOLDSHIELD(Greatshield.class, SkinSprites.GOLDSHIELD, 3, "Golden", GoldBar.class, "", null, -1),
	CANDLESHIELD(Greatshield.class, SkinSprites.CANDLESHIELD, 3, "Candle", CeremonialCandle.class, "", null, -1),
	EGGSHIELD(Greatshield.class, SkinSprites.EGGSHIELD, 3, "Egged", Egg.class, "", null, -1),
	LIGHTSHIELD(Greatshield.class, SkinSprites.LIGHTSHIELD, 3, "Shield of Light", WandOfPrismaticLight.class, "", null, -1),
	NATURESHIELD(Greatshield.class, SkinSprites.NATURESHIELD, 3, "Nature", SandalsOfNature.class, "", null, -1),
	GRASSSHIELD(Greatshield.class, SkinSprites.GRASSSHIELD, 3, "Shield of Growth", WandOfRegrowth.class, "", null, -1),
	FUSIONSHIELD(Greatshield.class, SkinSprites.FUSIONSHIELD, 3, "Shield of Transfusion", WandOfTransfusion.class, "", null, -1),
	SHIELDSHIELD(Greatshield.class, SkinSprites.SHIELDSHIELD, 3, "Shield of Warding", WandOfWarding.class, "", null, -1),
	TRUEGOLDSHIELD(Greatshield.class, SkinSprites.TRUEGOLDSHIELD, 4, "True Golden Shield", GoldenMeat.class, "", "True Golden Shield", -1),
	SCIMITAR(Scimitar.class, ItemSpriteSheet.SCIMITAR, 0, "Default", Scimitar.class, "", null, -1),
	ORANGESCIMITAR(Scimitar.class, SkinSprites.ORANGESCIMITAR, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWSCIMITAR(Scimitar.class, SkinSprites.YELLOWSCIMITAR, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENSCIMITAR(Scimitar.class, SkinSprites.GREENSCIMITAR, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUESCIMITAR(Scimitar.class, SkinSprites.BLUESCIMITAR, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISESCIMITAR(Scimitar.class, SkinSprites.TURQUOISESCIMITAR, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLESCIMITAR(Scimitar.class, SkinSprites.PURPLESCIMITAR, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKSCIMITAR(Scimitar.class, SkinSprites.PINKSCIMITAR, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNSCIMITAR(Scimitar.class, SkinSprites.BROWNSCIMITAR, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKSCIMITAR(Scimitar.class, SkinSprites.BLACKSCIMITAR, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYSCIMITAR(Scimitar.class, SkinSprites.GRAYSCIMITAR, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITESCIMITAR(Scimitar.class, SkinSprites.WHITESCIMITAR, 1, "White", Blindweed.Seed.class, "", null, -1),
	RUSSETSCIMITAR(Scimitar.class, SkinSprites.RUSSETSCIMITAR, 1, "Russet", CoffeeBush.Seed.class, "", null, -1),
	MAROONSCIMITAR(Scimitar.class, SkinSprites.MAROONSCIMITAR, 1, "Maroon", TomatoPlant.Seed.class, "", null, -1),
	SEPIASCIMITAR(Scimitar.class, SkinSprites.SEPIASCIMITAR, 1, "Sepia", PotatoPlant.Seed.class, "", null, -1),
	CARDINALSCIMITAR(Scimitar.class, SkinSprites.CARDINALSCIMITAR, 1, "Cardinal", Rotberry.Seed.class, "", null, -1),
	BERRYSCIMITAR(Scimitar.class, SkinSprites.BERRYSCIMITAR, 1, "Berry", Berry.class, "", null, -1),
	BIGSCIMITAR(Scimitar.class, SkinSprites.BIGSCIMITAR, 2, "Bigitar", PotionOfStrength.class, "Damn boi it thicc.", null, -1),
	CUTSCIMITAR(Scimitar.class, SkinSprites.CUTSCIMITAR, 2, "Cut", Saw.class, "", null, -1),
	MEATSCIMITAR(Scimitar.class, SkinSprites.MEATSCIMITAR, 2, "Meatimitar", MysteryMeat.class, "", null, -1),
	GOOSCIMITAR(Scimitar.class, SkinSprites.GOOSCIMITAR, 2, "Gooitar", GooBlob.class, "", null, -1),
	ICESCIMITAR(Scimitar.class, SkinSprites.ICESCIMITAR, 2, "Icemitar", PotionOfFrost.class, "", null, -1),
	GOLDSCIMITAR(Scimitar.class, SkinSprites.GOLDSCIMITAR, 3, "Gold", GoldBar.class, "", null, -1),
	BENTSCIMITAR(Scimitar.class, SkinSprites.BENTSCIMITAR, 3, "Curved", null, "", null, -1),
	RINGSCIMITAR(Scimitar.class, SkinSprites.RINGSCIMITAR, 3, "Ringitar", Ring.class, "", null, -1),
	DUELISTSCIMITAR(Scimitar.class, SkinSprites.DUELISTSCIMITAR, 3, "Duelist", DuelistArmor.class, "Looks like a miniature version of you.", null, -1),
	WARRIORSCIMITAR(Scimitar.class, SkinSprites.WARRIORSCIMITAR, 3, "Warrior", WarriorArmor.class, "As heroic as you.", null, -1),
	MAGESCIMITAR(Scimitar.class, SkinSprites.MAGESCIMITAR, 3, "Mage", MageArmor.class, "It even knows how to cast firebolt!", null, -1),
	RIPOSTESCIMITAR(Scimitar.class, SkinSprites.RIPOSTESCIMITAR, 3, "Riposte", DriedRose.class, "", null, -1),
	TRUEGOLDSCIMITAR(Scimitar.class, SkinSprites.TRUEGOLDSCIMITAR, 4, "True Gold", GoldenMeat.class, "", "True Golden Scimitar", -1),
	FEATHERSCIMITAR(Scimitar.class, SkinSprites.FEATHERSCIMITAR, 4, "Feather", Pillow.class, "", "Feather", -1),
	KATANA(Katana.class, ItemSpriteSheet.KATANA, 0, "Default", Katana.class, "", null, -1),
	ORANGEKATANA(Katana.class, SkinSprites.ORANGEKATANA, 1, "Orange", Firebloom.Seed.class, "", null, -1),
	YELLOWKATANA(Katana.class, SkinSprites.YELLOWKATANA, 1, "Yellow", Swiftthistle.Seed.class, "", null, -1),
	GREENKATANA(Katana.class, SkinSprites.GREENKATANA, 1, "Green", Sungrass.Seed.class, "", null, -1),
	BLUEKATANA(Katana.class, SkinSprites.BLUEKATANA, 1, "Blue", Icecap.Seed.class, "", null, -1),
	TURQUOISEKATANA(Katana.class, SkinSprites.TURQUOISEKATANA, 1, "Turquoise", Stormvine.Seed.class, "", null, -1),
	PURPLEKATANA(Katana.class, SkinSprites.PURPLEKATANA, 1, "Purple", Sorrowmoss.Seed.class, "", null, -1),
	PINKKATANA(Katana.class, SkinSprites.PINKKATANA, 1, "Pink", Mageroyal.Seed.class, "", null, -1),
	BROWNKATANA(Katana.class, SkinSprites.BROWNKATANA, 1, "Brown", Earthroot.Seed.class, "", null, -1),
	BLACKKATANA(Katana.class, SkinSprites.BLACKKATANA, 1, "Black", Starflower.Seed.class, "", null, -1),
	GRAYKATANA(Katana.class, SkinSprites.GRAYKATANA, 1, "Grey", Fadeleaf.Seed.class, "", null, -1),
	WHITEKATANA(Katana.class, SkinSprites.WHITEKATANA, 1, "White", Blindweed.Seed.class, "", null, -1),
	DILLOKATANA(Katana.class, SkinSprites.DILLOKATANA, 2, "Dillo Slasher", Armadillo.class, "", null, -1),
	CHEESEKATANA(Katana.class, SkinSprites.CHEESEKATANA, 2, "Cheese", CheeseSlice.class, "", null, -1),
	OILKATANA(Katana.class, SkinSprites.OILKATANA, 2, "Oil", PotionOfFuel.class, "Did someone say OIL?!? *freedom noises*", null, -1),
	SICKLEKATANA(Katana.class, SkinSprites.SICKLEKATANA, 2, "Sickle", Sickle.class, "", null, -1),
	GOOKATANA(Katana.class, SkinSprites.GOOKATANA, 2, "Goo", GooBlob.class, "", null, -1),
	ICECREAMKATANA(Katana.class, SkinSprites.ICECREAMKATANA, 2, "Ice cream", IceCream.class, "", null, -1),
	STYLUSKATANA(Katana.class, SkinSprites.STYLUSKATANA, 2, "Stylus", Stylus.class, "", null, -1),
	SAIKATANA(Katana.class, SkinSprites.SAIKATANA, 2, "Dual Katana", Sai.class, "", null, -1),
	AXEKATANA(Katana.class, SkinSprites.AXEKATANA, 2, "Katanaxe", BattleAxe.class, "", null, -1),
	OBSIDIANKATANA(Katana.class, SkinSprites.OBSIDIANKATANA, 2, "Obsidian", AssassinsBlade.class, "", null, -1),
	BRANCHKATANA(Katana.class, SkinSprites.BRANCHKATANA, 3, "Branch", Branch.class, "", null, -1),
	MESSEDKATANA(Katana.class, SkinSprites.MESSEDKATANA, 3, "Messed up", null, "Uhh...", null, -1),
	CHAOSKATANA(Katana.class, SkinSprites.CHAOSKATANA, 3, "Chaos", UnstableBrew.class, "", null, -1),
	PAINTKATANA(Katana.class, SkinSprites.PAINTKATANA, 3, "Spray paint", SprayPaint.class, "", null, -1),
	SNAKEKATANA(Katana.class, SkinSprites.SNAKEKATANA, 3, "Snake", EtherealChains.class, "", null, -1),
	DISARMEDKATANA(Katana.class, SkinSprites.DISARMEDKATANA, 3, "Disarmed", StoneOfDisarming.class, "", null, -1);

	private final Class<? extends Item> skinFor;
	private final int sprite;
	private final int rarity;
	private final String name;
	private final Class<? extends Item> ingredient;
	private final String desc;
	private final String legendName;
	private final int arrow;

	Skin(Class<? extends Item> skinFor, int sprite, int rarity, String name, Class<? extends Item> ingredient,
	     String desc, String legendName, int arrow) {
		this.skinFor = skinFor;
		this.sprite = sprite;
		this.rarity = rarity;
		this.name = name;
		this.ingredient = ingredient;
		this.desc = desc;
		this.legendName = legendName;
		this.arrow = arrow;
	}

	public Class<? extends Item> skinFor() { return skinFor; }
	public int sprite() { return sprite; }
	public int rarityTier() { return rarity; }
	public String skinName() { return name; }
	public String desc() { return desc; }
	public Class<? extends Item> ingredient() { return ingredient; }
	//the name a legendary or mythic skin gives its item, null keeps the item's own name
	public String legendName() { return legendName; }

	//the arrow a spirit bow with this skin shoots
	public int arrowImage() {
		return arrow != -1 ? arrow : ItemSpriteSheet.SPIRIT_ARROW;
	}

	public String rarityName() {
		return Messages.get(Skin.class, "rarity_" + rarity);
	}

	public int rarityColor() {
		switch (rarity) {
			case 1: return 0xBFBFBF;
			case 2: return 0x4F8DEE;
			case 3: return 0xC54EED;
			case 4: return 0xFBB727;
			case 5: return 0xFF1717;
			default: return 0xFFFFFF;
		}
	}

	public String fullSkinName() {
		String n = Messages.titleCase(name.toLowerCase());
		String low = name.toLowerCase();
		if (skinFor == BrokenSeal.class) {
			return low.contains("seal") ? n : n + " Seal";
		} else if (skinFor == CloakOfShadows.class) {
			return low.contains("cloak") ? n : n + " Cloak";
		} else if (skinFor == SpiritBow.class) {
			return low.contains("bow") ? n : n + " Bow";
		} else if (skinFor == MagesStaff.class) {
			return low.contains("staff") || low.contains("wand") ? n : n + " Wand";
		} else if (skinFor == LeatherArmor.class) {
			return n + " Leather Armor";
		} else if (skinFor == PlateArmor.class) {
			return n + " Plate Armor";
		} else if (skinFor == UnstableSpellbook.class) {
			return low.contains("book") || low.contains("guide") ? n : n + " Spellbook";
		} else if (skinFor == ClothArmor.class) {
			return n + " Cloth Armor";
		} else if (skinFor == RunicBlade.class) {
			return low.contains("blade") || low.contains("runic") ? n : n + " Runic Blade";
		} else if (skinFor == Longsword.class) {
			return low.contains("sword") ? n : n + " Longsword";
		} else if (skinFor == Glaive.class) {
			return low.contains("glaive") ? n : n + " Glaive";
		} else if (skinFor == BattleAxe.class) {
			return low.contains("axe") ? n : n + " Battleaxe";
		} else if (skinFor == WarHammer.class) {
			return low.contains("war") || low.contains("hammer") ? n : n + " Warhammer";
		} else if (skinFor == Greatshield.class) {
			return low.contains("shield") ? n : n + " Shield";
		} else if (skinFor == Scimitar.class) {
			return low.contains("itar") ? n : n + " Scimitar";
		} else if (skinFor == Katana.class) {
			return low.contains("katana") ? n : n + " Katana";
		}
		return n;
	}

	//the empty outline the journal shows for a skin not found yet
	public int spriteOutline() {
		if (skinFor == BrokenSeal.class) return SkinSprites.EMPTY_SEAL;
		if (skinFor == CloakOfShadows.class) return SkinSprites.EMPTY_CLOAK;
		if (skinFor == SpiritBow.class) return SkinSprites.EMPTY_BOW;
		if (skinFor == MagesStaff.class) return SkinSprites.EMPTY_WAND;
		if (skinFor == LeatherArmor.class) return SkinSprites.EMPTY_LEATHER;
		if (skinFor == PlateArmor.class) return SkinSprites.EMPTY_PLATE;
		if (skinFor == ClothArmor.class) return SkinSprites.EMPTY_CLOTH;
		if (skinFor == UnstableSpellbook.class) return SkinSprites.EMPTY_BOOK;
		if (skinFor == RunicBlade.class) return SkinSprites.EMPTY_RUNIC;
		if (skinFor == Longsword.class) return SkinSprites.EMPTY_LSWORD;
		if (skinFor == Glaive.class) return SkinSprites.EMPTY_GLAIVE;
		if (skinFor == BattleAxe.class) return SkinSprites.EMPTY_AXE;
		if (skinFor == WarHammer.class) return SkinSprites.EMPTY_HAMMER;
		if (skinFor == Greatshield.class) return SkinSprites.EMPTY_SHIELD;
		if (skinFor == Scimitar.class) return SkinSprites.EMPTY_SCIMITAR;
		return sprite;
	}

	private static final HashMap<Class<?>, List<Skin>> BY_CLASS = new HashMap<>();
	static {
		for (Skin s : values()) {
			List<Skin> list = BY_CLASS.get(s.skinFor);
			if (list == null) {
				list = new ArrayList<>();
				BY_CLASS.put(s.skinFor, list);
			}
			list.add(s);
		}
	}

	public static List<Skin> all() {
		return new ArrayList<>(Arrays.asList(values()));
	}

	public static boolean hasSkins(Class<?> itemClass) {
		return BY_CLASS.containsKey(itemClass);
	}

	public static List<Skin> allFor(Class<?> itemClass) {
		List<Skin> list = BY_CLASS.get(itemClass);
		return list == null ? Collections.<Skin>emptyList() : list;
	}

	//the usual look, rarity 0
	public static Skin defaultFor(Class<?> itemClass) {
		for (Skin s : allFor(itemClass)) {
			if (s.rarity == 0) return s;
		}
		return null;
	}

	//the skin an item shows now. Items that were never painted have no skin saved and show the usual look.
	public static Skin current(Item item) {
		if (item == null) return null;
		if (item.skin != null && item.skin.skinFor == item.getClass()) return item.skin;
		return defaultFor(item.getClass());
	}

	private static boolean matches(Class<? extends Item> ingredient, Item item) {
		if (ingredient == null || item == null) return false;
		if (ingredient == item.getClass()) return true;
		//the mod named the plain Ring class, which no ring in the dungeon is, so any ring counts here
		return ingredient == Ring.class && item instanceof Ring;
	}

	public static boolean containsIngredient(Item item, Class<?> skinFor) {
		return fromIngredient(item, skinFor) != null;
	}

	public static Skin fromIngredient(Item item, Class<?> skinFor) {
		for (Skin s : allFor(skinFor)) {
			if (matches(s.ingredient, item)) return s;
		}
		return null;
	}

	public static Skin random(Class<?> itemClass) {
		List<Skin> list = allFor(itemClass);
		return list.isEmpty() ? null : list.get(Random.Int(list.size()));
	}
}
