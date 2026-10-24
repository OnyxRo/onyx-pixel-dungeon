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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FlashBangBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SupplyRation;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.EscapeCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ImpStatue;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.VaultBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BowFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenHilt;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.CloakScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.SealShard;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.TornPage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

//For items, but includes a few item-like effects, such as enchantments
public enum Catalog {

	//EQUIPMENT
	MELEE_WEAPONS,
	ARMOR,
	ENCHANTMENTS,
	GLYPHS,
	THROWN_WEAPONS,
	WANDS,
	RINGS,
	ARTIFACTS,
	TRINKETS,
	MISC_EQUIPMENT,

	//CONSUMABLES
	POTIONS,
	SEEDS,
	SCROLLS,
	STONES,
	FOOD,
	EXOTIC_POTIONS,
	EXOTIC_SCROLLS,
	BOMBS,
	TIPPED_DARTS,
	BREWS_ELIXIRS,
	SPELLS,
	MISC_CONSUMABLES;

	//tracks whether an item has been collected while identified
	private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
	//tracks upgrades spent for equipment, uses for consumables
	private final LinkedHashMap<Class<?>, Integer> useCount = new LinkedHashMap<>();
	
	public Collection<Class<?>> items(){
		return seen.keySet();
	}

	//should only be used when initializing
	private void addItems( Class<?>... items){
		for (Class<?> item : items){
			seen.put(item, false);
			useCount.put(item, 0);
		}
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public int totalItems(){
		return seen.size();
	}

	public int totalSeen(){
		int seenTotal = 0;
		for (boolean itemSeen : seen.values()){
			if (itemSeen) seenTotal++;
		}
		return seenTotal;
	}

	static {

		MELEE_WEAPONS.addItems(Generator.Category.WEP_T1.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T2.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T3.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T4.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_T5.classes);

		ARMOR.addItems(Generator.Category.ARMOR.classes);

		THROWN_WEAPONS.addItems(Generator.Category.MIS_T1.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T2.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T3.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T4.classes);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_T5.classes);

		ENCHANTMENTS.addItems(Weapon.Enchantment.common);
		ENCHANTMENTS.addItems(Weapon.Enchantment.uncommon);
		ENCHANTMENTS.addItems(Weapon.Enchantment.rare);
		ENCHANTMENTS.addItems(Weapon.Enchantment.curses);

		GLYPHS.addItems(Armor.Glyph.common);
		GLYPHS.addItems(Armor.Glyph.uncommon);
		GLYPHS.addItems(Armor.Glyph.rare);
		GLYPHS.addItems(Armor.Glyph.curses);

		WANDS.addItems(Generator.Category.WAND.classes);

		RINGS.addItems(Generator.Category.RING.classes);

		ARTIFACTS.addItems(Generator.Category.ARTIFACT.classes);

		TRINKETS.addItems(Generator.Category.TRINKET.classes);

		MISC_EQUIPMENT.addItems(BrokenSeal.class, SpiritBow.class, Waterskin.class, VelvetPouch.class,
				PotionBandolier.class, ScrollHolder.class, MagicalHolster.class, Amulet.class);



		POTIONS.addItems(Generator.Category.POTION.classes);

		SCROLLS.addItems(Generator.Category.SCROLL.classes);

		SEEDS.addItems(Generator.Category.SEED.classes);

		STONES.addItems(Generator.Category.STONE.classes);

		FOOD.addItems( Food.class, Pasty.class, MysteryMeat.class, ChargrilledMeat.class,
				StewedMeat.class, FrozenCarpaccio.class, SmallRation.class, Berry.class,
				SupplyRation.class, Blandfruit.class, PhantomMeat.class, MeatPie.class );

		EXOTIC_POTIONS.addItems(ExoticPotion.exoToReg.keySet().toArray(new Class[0]));

		EXOTIC_SCROLLS.addItems(ExoticScroll.exoToReg.keySet().toArray(new Class[0]));

		BOMBS.addItems( Bomb.class, FrostBomb.class, Firebomb.class, SmokeBomb.class, RegrowthBomb.class,
				WoollyBomb.class, Noisemaker.class, FlashBangBomb.class, HolyBomb.class, ArcaneBomb.class, ShrapnelBomb.class);

		TIPPED_DARTS.addItems(TippedDart.types.values().toArray(new Class[0]));

		BREWS_ELIXIRS.addItems( UnstableBrew.class, InfernalBrew.class, BlizzardBrew.class,
				ShockingBrew.class, CausticBrew.class, AquaBrew.class, ElixirOfHoneyedHealing.class,
				ElixirOfAquaticRejuvenation.class, ElixirOfArcaneArmor.class, ElixirOfDragonsBlood.class,
				ElixirOfIcyTouch.class, ElixirOfToxicEssence.class, ElixirOfMight.class, ElixirOfFeatherFall.class);

		SPELLS.addItems( UnstableSpell.class, WildEnergy.class, TelekineticGrab.class, PhaseShift.class,
				Alchemize.class, CurseInfusion.class, MagicalInfusion.class, Recycle.class,
				ReclaimTrap.class, SummonElemental.class, BeaconOfReturning.class);

		MISC_CONSUMABLES.addItems( Gold.class, EnergyCrystal.class, Dewdrop.class,
				IronKey.class, GoldenKey.class, CrystalKey.class, WornKey.class,
				TrinketCatalyst.class, Stylus.class, Torch.class, Honeypot.class, Ankh.class,
				CorpseDust.class, Embers.class, CeremonialCandle.class, DarkGold.class, EscapeCrystal.class, VaultBeacon.class, DwarfToken.class, ImpStatue.class,
				GooBlob.class, TengusMask.class, MetalShard.class, KingsCrown.class,
				LiquidMetal.class, ArcaneResin.class,
				SealShard.class, BrokenStaff.class, CloakScrap.class, BowFragment.class, BrokenHilt.class, TornPage.class);

		//Combusted Pixel Dungeon's melee weapons that can also be thrown
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.thrown.ThrowableDagger.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.thrown.ThrowableDirk.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.thrown.ThrowableHandAxe.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.thrown.ThrowableSpear.class);

		//Matt Edition's unique blacksmith weapons, and its gun
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.ArrowSign.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.BattleAxeStormbringer.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.EmeraldRoundShield.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.GlovesPurge.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.KatanaDarkness.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.SickleBloodLetter.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.SpearAmethystPiercer.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.SwordMidas.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.WhipFirecracker.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Glock.class);

		//items ported from Devoted Pixel Dungeon
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.EndothermicRing.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMalaise.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfThorns.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Necronomicon.class); //transformation
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.ArcaneRoot.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.LargeRadish.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CrystalApple.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.Meal.class, com.shatteredpixel.shatteredpixeldungeon.items.Wyvernfruit.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.RockBlock.class, com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfOvergrowth.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.RoyalSeal.class, com.shatteredpixel.shatteredpixeldungeon.items.quest.Ruby.class, com.shatteredpixel.shatteredpixeldungeon.items.quest.StaffSplinter.class, com.shatteredpixel.shatteredpixeldungeon.items.quest.EmberEssence.class);

		//items ported from experienced
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.FantasmalStabber.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.FiringSnapper.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.GleamingStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.RegrowingSlasher.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.blacksmith.StarlightSmasher.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CreativeGloves.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Clayball.class);

		//items ported from abyssalnite
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Exhausting.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Fragile.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flute.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.SleepDart.class);

		//items ported from experienced
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.KingBlade.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.RustyShield.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.SuperPickaxe.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Vampirism.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDisarming.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalPorter.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.IdentificationBomb.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfEarthblast.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfUnstable.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RatKingArmor.class);

		//items ported from reforged
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.RatClaw.class);

		//items ported from abyssalnite
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.DewVial.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.MerchantsBeacon.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.AbyssalBottle.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.AbyssalCure.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.AbyssalFruit.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Pudding.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfAdrenalineSurge.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfHolyFuror.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAffection.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfConfusion.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPetrification.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPolymorph.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAffection.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepenedSleep.class);

		//items ported from cursed
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Precise.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shielding.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Swift.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Chainsaw.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.InscribedKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.ChainsawHand.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.LorsionsGreataxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.LoturgosCrystal.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.MaracarsBlades.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.NahusSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.NeptunesTrident.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RaRothsNunchucks.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Damning.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Drawing.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Ethereal.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Vicious.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments.Voltage.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.ThonothsAxe.class);

		//items ported from cursed
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.AmuletSectorWater.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.Soul.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.allies.EarthenDragon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.allies.FireDragon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.allies.PoisonDragon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.allies.VampiricDragon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.allies.WaterDragon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodHolder.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.PowerHolder.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.instruments.Harp.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.MegaExperiencePotion.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.MegaStrengthPotion.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.Blink.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.BubbleShield.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.Energize.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.Greed.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.MoltenEarth.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.PoisonBurst.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.RaiseDead.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.Telekinesis.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.powers.WaterPump.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfLuck.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Resetter.class);

		//items ported from experienced
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Cheese.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CheeseChunk.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ExpGenerator.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.TicketToArena.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.MysteryHeart.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.PsycheChest.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.BlackPsycheChest.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.TenguBomb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.TenguShuriken.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDetermination.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMidas.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfOverload.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.FireBooster.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.RespawnBooster.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.KeyToTruth.class);

		//items ported from experienced
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.CheeseCheest.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.AvaritiaFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.BasicFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.ChaosFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.GoldenFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.NeutroniumFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.fishingrods.SpiritualFishingRod.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.AlchemyBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.BiggerGambleBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.BurntBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.DKTreasureBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.DM300TreasureBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.GambleBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.GooTreasureBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.IdealBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.QualityBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasurebags.TenguTreasureBag.class);

		//items ported from reforged
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.ArtificerArmor.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneClaws.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.LuckyDice.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Ringbox.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollKillAll.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfDecay.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bat.Membrane.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGrowth.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollTeleportToStaircase.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlight.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisplacement.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WanderersMap.class);

		//items ported from ratking
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DreamDart.class);

		//items ported from mattedition
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfElements.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfExplosion.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireball.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.IcyCore.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.augments.IcyCoreArmor.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.augments.IcyCoreWeapon.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ThrowingChains.class);

		//items ported from mattedition
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfVitality.class);

		//items ported from reforged
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun.class);

		//items ported from shittened
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Branch.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatgreatsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PileOfSais.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SilverKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SmallGreatsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spearaxe.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.BeautifulAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.ChairAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.FinnishAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.HogAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.NonsenseAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.PopAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.RatAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.RemixAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.SeaAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.SynthAlbum.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.albums.TwingoAlbum.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.MaleArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateCarrier.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.PostArmor.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodCrate.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.BakedFish.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.BasedCake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.BerryBalls.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.BerryCake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Bread.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Bruschetta.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CakeBase.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledSmallMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CheeseSlice.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.ChocolateCake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Coffee.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CoffeeBean.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CompleteBreakfast.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CookedWarrior.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Cookies.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CrispyBass.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.DanishPastry.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.DeepfriedRat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Egg.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Fish.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Flour.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.FriedEgg.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Hashbrowns.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.IceCream.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Meatballs.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Milk.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Nuts.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Oil.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Omelet.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Pancakes.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.PancakesWithCheese.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.MilkCheese.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Piethon.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Pizza.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Potato.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RatatouilleFood.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Sashimi.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.SmallFrozenCarpaccio.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.SmallMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Spaghetti.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.StewedSmallMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.StirFry.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Sugar.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.TidePod.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Tomato.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.TripleShotEspresso.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.UntaxedFood.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.WaxBar.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Wheat.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Armadillo.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Car.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.FOFFSDegree.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.GoldAmulet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.GoldBar.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Insulin.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.LootBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Painting.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Pillow.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.PinkAmulet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Pug.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Saw.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.SilverAmulet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Sparrow.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.SprayPaint.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.StoneAmulet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.VeggieBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Warriorium.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.misc.Willow.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfAcceleration.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFuel.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfKetchup.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.GoblinCurse.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfFumes.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfFuror.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfKetchupStorm.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfShit.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnshittification.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfReforging.class);

		//items ported from ryuse
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TrainingWhip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SummonerWhip.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.SummonerArmor.class);
		for (Class<?> helmet : Generator.Category.HELMET.classes) ARMOR.addItems(helmet);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EyeOfWisdom.class);

		//items ported from transformation
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Blowpipe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Bracer.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.CorrosionEnchanted.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.DemonTailWhip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.GreatGreatGreatsword.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Gungnir.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Heavy.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LatherWhip.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.LuckyCoin.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.SlimeBall.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SpearOfConqueror.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SteelWhip.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Sweeping.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Wakizashi.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WalkStick.class);

		//items ported from transformation
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.PalermoSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.SakuraBlossomBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.TwoHandedGreatsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.VenomousSickle.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ElfWine.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.DM400Armor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.FreemanArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.FriarArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.NinjaArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.PrincessArmor.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Leaden.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.PrecognitiveEye.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.BottledEnergy.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenPackage.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenTool.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.MatchaPudding.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.MetalFragment.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.RoyalJewelry.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.AlMughiraPyxis.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.BlacasEwer.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.BookOfKells.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.CholaNataraja.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.DjenneTerracottaFigure.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.DojigiriYasutsuna.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.EthiopianProcessionalCross.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.GoryeoMaebyeong.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.GreatKhanPaiza.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.GreatZimbabweBird.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.HarbavilleTriptych.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.ImperialCrown.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.IncaGoldenLlama.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.JavaneseGoldCup.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.LewisChessQueen.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.MuiscaGoldenRaft.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.PakalJadeMask.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.RuWareBowl.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.SuttonHooHelmet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.treasures.TurquoiseSerpent.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.BronzeWatch.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.GoldIngot.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MagicFeather.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SpearShield.class);

		//items ported from transformation
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.RedEnvelope.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.HikingBackpack.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.SlimeArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.SlimeGirlArmor.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.PortableBlackHole.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Tatteki.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.AuxiliaryCore.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfExtraction.class);

		//items ported from transformation
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Tincture.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RitualDagger.class);

		//items ported from shattered-pixel-dungeon-master
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Stunning.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DragonDagger.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Knuckles.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.VikingMace.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CurareDart.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMight.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfLife.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicalInfusion.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfVenom.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.VikingArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.DragonKnightArmor.class);

		//items ported from pixel-towers-master
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Banhammer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BerserkerAxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BlueKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BoneMachete.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DarksteelSaber.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DualHatchet.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GoldenSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Infipike.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.IronWhip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MoltenDagger.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Pike.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PlagueScourge.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.StoneHammer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Trisai.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Degrading.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Unreliable.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Empowering.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Pure.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Holy.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfLethality.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSnakes.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfImmortality.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfDrowsiness.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDiscoveries.class);

		//items ported from lovecraft
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Blowback.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Cowardice.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Menacing.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Exploding.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Friendship.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Glowing.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Holy.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Hunting.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Midas.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Sting.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Stunning.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.TimeFreezing.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.FastFirePistol.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Harpoon.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Luger.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Pistol.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Rifle.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Shotgun.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Axe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Halberd.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HeroicShortsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KnifeGlove.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Knuckles.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Kusarigama.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scythe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SpikedGreatsword.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CurareDart.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.GiantShuriken.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.PrimitiveSpear.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tamahawk.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.Tnt.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.TntBundle.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Clumsy.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Chaotic.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Cloning.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Deflection.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Explosion.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.FreezeTime.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stealth.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Weight.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.LeadKey.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.PlatinKey.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.TitaniumKey.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.WoodenKey.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.ElderSign.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.YellowSign.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.tools.Scissors.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.tools.Tincturebottle.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfVenom.class);

		//items ported from lovecraft
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.Bow.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Teleport.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.BlandfruitHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.BlindweedHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.DewcatcherHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.DreamfoilHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.EarthrootHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.FadeleafHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.FirebloomHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.IcecapHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.RotberryHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SeedpodHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SorrowmossHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.StarflowerHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SteamweedHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.StormvineHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SungrassHerb.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlandfruitItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlindweedItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DewcatcherItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DreamfoilItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.EarthrootItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FadeleafItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FirebloomItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.IcecapItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.RotberryItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SeedpodItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SorrowmossItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StarflowerItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SteamWeedItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StormvineItem.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SungrassItem.class);

		//items ported from lovecraft
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Dividing.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.guns.HeavyCrossbow.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Boomerang.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Chakram.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.Weightstone.class);
		//items ported from unleashed
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Ancient.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Resistance.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.AnkhChain.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.OverpricedRation.class);
		//items ported from spacebase
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Buggy.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Backfiring.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.LowEnergy.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Misfiring.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BrightHammer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BrightSaber.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Drill.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DualBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HoloAxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HoloScimitar.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImperialShield.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LazerSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LazerWhip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MCPickAxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.OrbMelder.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RaiderBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spanner.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.UltonAxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.UltonShield.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HunterJavelin.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Nanobots.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.XenoSkull.class);

		//items ported from spacebase
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.Uniform.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.SynthesizedFood.class);
		//items ported from magicling
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.AltVampiric.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Crushing.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.ExplosionX.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dairikyan.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DragonShiled.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FireFishSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.IceLingSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ImperialExorcism.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.JunglePoison.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MerchantSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Seekingspear.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SkyShield.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WashCrime.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WitheWoodSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.hollow.DeathRongBoat.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.GoldLongGun.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.MoonDao.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.SaiPlus.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.Break.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.DeepseaKnightAnchor.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.DenorScythe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.ExorcistMaul.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.KillKing.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.QuickSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.ReedPipe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.RitualSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.SpikedChakram.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.StaffofMyriadThings.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TheDrip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TreeList.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CrossReback.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RedBlock.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.StreamerKnife.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Bullet.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DwDart.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.MainBooks.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.LamellarArmor.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.GoldIron.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.BookBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.HerbBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.KingBag.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.Books.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.HellFireBooks.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.BzmdrBooks.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.DeYiZiBooks.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.MoneyMoreBooks.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.PinkRandomBooks.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.HollowCityBook.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.TombReachBook.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Cake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.QieCake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RedCrab.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RiceCake.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.SakaMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.BoneSoup.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.RatTail.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.Gelatin.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.Sugar_Block.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.WhiteSugar_B.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.tomb.CookGraveMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.tomb.FrozenGraveMeat.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.tomb.GraveMeat.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.jokings.HornOfPlentyBomb.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater.class);
		EXOTIC_POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfLightStromCloud.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.BlockingDrug.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.BottleWraith.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenBone.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.CatGirlCosplay.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.CloakFragmentsOfBzmdr.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.DeadOrAlive.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.DeliciousRecipe.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.Dirt_KnifeStand.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.DreamSeed.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregation.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregationB.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.HeartOfCrystalFractal.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.HellButterfly.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.KnightStabbingSword.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.LuckyGlove.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.Monocular.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.NoteOfBzmdr.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.PortableWhetstone.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.RustedGoldCoin.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.StarDust.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.StarSachet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDoll.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDollB.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.TheGriefOfSpeechless.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.props.Trash.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.BloodRedFlower.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.ClearCryStal.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.ClearHStal.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.MagicBook.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.DragonHeart.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.RedWhiteRose.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.SoulCrack.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.UnsignedInvitationLetter.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.StarCrystal.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccEvasion.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingoOfReTenacity.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfBlessGoTend.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingArmorBind.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingArtifactBind.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingMiscBind.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingRingBind.class);
		//items ported from deep
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArtifactWeapon.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Rooting.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Turning.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Aegis.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Immortality.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PinLuna.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PinSol.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SacrificialKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sorrowblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TheDebugger.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Cards.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ShadowShard.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.DivineArmor.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Bloodrage.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Fleeing.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.SpiritsChalice.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.keys.LootChestPrize.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.upgrade.ArmourEnhancer.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.upgrade.ArmourGhostShard.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.upgrade.RareGhostShard.class);

		//items ported from transformation
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.SprayGun.class);

		//items ported from transformation
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Shuriken_Box.class);

		//items ported from transformation
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.InstructionTool.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.DamageGear.class);

		//items ported from transformation
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.SourWineAroma.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.GreenGlowFruit.class);

		//items ported from transformation
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.ChainMace.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.HundredTonHammer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.LakeSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.MercuryBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.MountainGuard.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.OracleTerminal.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.RadiantGoldHalberd.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier6.SoulBlade.class);

		//items ported from transformation
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RuneString.class);

		//items ported from transformation
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TwinDemonEyes.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.ElementalResistance.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfUpTier.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.SpecialPackage.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCursedFlame.class);
		//items ported from ryuse
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.EquipmentPage.class);
		//rearranged, Re-ARranged PD. Its tier 1 to 5 weapons and trinkets come in through the Generator lists above
		MELEE_WEAPONS.addItems(Generator.Category.WEP_AL_T3.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_AL_T4.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_AL_T5.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_AL_T6.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_AL_T7.classes);
		MELEE_WEAPONS.addItems(Generator.Category.WEP_SP.classes);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.PocketKnife.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HeroSword.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shovel.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Machete.class);
		THROWN_WEAPONS.addItems(Generator.Category.MIS_SP.classes);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.GunnerArmor.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.SamuraiArmor.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.AdventurerArmor.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.KnightArmor.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.MedicArmor.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.ArcherArmor.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Mirrorimage.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Satisfying.class);
		WANDS.addItems(Generator.Category.SPELLBOOK.classes);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MedicKit.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.KnightsShield.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DeathSword.class,
				com.shatteredpixel.shatteredpixeldungeon.items.Saddle.class,
				com.shatteredpixel.shatteredpixeldungeon.items.Rosary.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.WindBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.TacticalBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.GoldenBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.NaturesBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.CorrosiveBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.ElectricBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.MagicalBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.PhaseBow.class,
				com.shatteredpixel.shatteredpixeldungeon.items.Sheath.class,
				com.shatteredpixel.shatteredpixeldungeon.items.GammaRayGun.class,
				com.shatteredpixel.shatteredpixeldungeon.items.ArrowBag.class,
				com.shatteredpixel.shatteredpixeldungeon.items.changer.OldAmulet.class,
				com.shatteredpixel.shatteredpixeldungeon.items.Teleporter.class);
		POTIONS.addItems(Generator.Category.PILL.classes);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfTalent.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Evolution.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.RapidGrowth.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.IceMaker.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.FireMaker.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.FireImbueSpell.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.ElectricityImbue.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableIdentification.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.HandyBarricade.class,
				com.shatteredpixel.shatteredpixeldungeon.items.spells.UpgradeDust.class,
				com.shatteredpixel.shatteredpixeldungeon.items.changer.BluePrint.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.SpareMagazine.class,
				com.shatteredpixel.shatteredpixeldungeon.items.remains.SheathFragment.class,
				com.shatteredpixel.shatteredpixeldungeon.items.remains.HoledPouch.class,
				com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenShield.class,
				com.shatteredpixel.shatteredpixeldungeon.items.remains.ShatteredPillbox.class,
				com.shatteredpixel.shatteredpixeldungeon.items.Rope.class,
				com.shatteredpixel.shatteredpixeldungeon.items.GunSmithingTool.class,
				com.shatteredpixel.shatteredpixeldungeon.items.BulletBelt.class,
				com.shatteredpixel.shatteredpixeldungeon.items.BulletItem.class,
				com.shatteredpixel.shatteredpixeldungeon.items.ArrowItem.class);
		//summoning, items ported from Summoning PD Reincarnated
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.GuidanceTorch.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.Ropes.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.ConjurerArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.ScoutArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.SyntheticArmor.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.ArcaneElement.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.DruidBag.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.Graveyard.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.PerfumeBlast.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.RevengeWaves.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.TargetedSupport.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BadgeOfBravery.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.HeavyFlail.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MirrorOfFates.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.ParchmentOfElbereth.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SubtilitasSigil.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.ConjurerBook.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.RatBomb.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.SupplyBomb.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.Webbomb.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Barrier.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Field.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Heal.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Necro.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Shocker.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Stars.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Wave.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.Zap.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.Boom.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.Flower.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.KiHealing.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.Punch.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.knight.Shards.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Defense.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Magical.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Offense.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Ranged.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.magic.soulreaver.Support.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.PerfumeBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.RagingBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAttunement.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.Chaosstone.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.CleanWater.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.Scrap.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfAttunement.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSoulEnergy.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Contain.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.EnchantParchment.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Enrage.class);
		STONES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfTargeting.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBounceBeams.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfConjuration.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCrystalBullet.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfStars.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfStench.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.Slingshot.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Broadsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Cleaver.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ConjurerKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.shop.Jjango.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.shop.Stabber.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.BlasterStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.ChickenStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.FroggitStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.FrostElementalStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.GnollHunterStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.GooStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.GreyRatStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.HacatuStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.ImpQueenStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.MagicMissileStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.MimicStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.RoboStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.RoseStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.SheepStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.SkeletonStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.SlimeStaff.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.staffs.WizardStaff.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnive2.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.abilities.SpectralBlades.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.ConjurerClassArmor.class);
		//transformation
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.TransformSpell.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.MetamorphosisPrism.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.RubbingsTome.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ScrollOfSublimation.class);

		//items ported from pixel-towers-master
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbArcanesword.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbBanner.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbBerserk.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbBlessing.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbCharge.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbDisengage.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbEgoist.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbGibberish.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbGlowup.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbGoldarmor.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbHolyWater.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbIceWall.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbLasher.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbNecromancy.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbNoAmulet.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbOakskin.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbObelisk.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbPray.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbProjectiles.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbRegeneration.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbShadowclone.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbShield.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbSwift.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTarget.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTaunt.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTeleport.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTreatwounds.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbUndying.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbVision.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbWallstance.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbWisp.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.HeroSpellbook.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.BloodReaper.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Evasion.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfUnspeakableHorrors.class);

		//items ported from ratking
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.remains.CrownShard.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFirebolt.class);

		//items ported from rerearranged
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.RuinBow.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.LunarBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.ObsidianBow.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.alchemy.ObsidianKnife.class);

		//items ported from polished
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyedMeat.class);
		//items ported from elektro
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corroding.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.GhostDart.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.VoidDart.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.ContravariantAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.CovariantAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.ExistenceAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.FieldOperatorAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.LaplaceAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.NablaAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.PartialAncientRune.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes.PropagationAncientRune.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodOrb.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CrackedGeode.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.FlameShot.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.GreaterTransmutation.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.IceShot.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.InvariantAncientRune.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicBridge.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.PrismaticImageSpell.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Transfiguration.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.VampiricShot.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.VenomShot.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfChaos.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCosmicTravel.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTrauma.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDamnation.class);
		//items ported from sandboxpd
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTime.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfInstability.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSummoning.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfYendor.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfWipeOut.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.PotionCocktail.class);
		//items ported from community
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AlchemicalAkrafena.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.GemstoneDust.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.RepairedRapier.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.NecklaceOfIce.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.CrossBomb.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.FrostfireBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.LeanyElixir.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.RNGManipulator.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.BrokenTreasureBox.class);

		//items ported from community
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.CommunityFury.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.CommunityWakizashi.class);
		//bluearchive, Blue Archive PD. Its guns, the students' gadgets and its throwing set
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BASuperNova.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier1.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier2.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier3.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier4.class, com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier5.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BABicycle.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BABlastGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BAClaymore.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BAFlashGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BAHPGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BAHandGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BAIronHorus.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BALaser.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BALevelAnalyzer.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BARainbowGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BARegrowGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BASmokeGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.BATrashBin.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.BAAmmoBox.class, com.shatteredpixel.shatteredpixeldungeon.items.BAEXSkillDisc.class);
		//rebluearchive, Re Blue Archive PD
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.RBAConversionKit.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.RBAGunSmithingTool.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBABicycle.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBAClaymore.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBAHandGrenade.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBAIronHorus.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBALaptop.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.RBATrashBin.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAContinueConsole.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAFantasyConsole.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAFighterConsole.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBASandboxConsole.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAArisArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAHoshinoArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAIzunaArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMariArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMikaArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMiyakoArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAMiyuArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBANoaArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBANonomiArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAShirokoArmor.class);
		ARMOR.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.RBAYuzuArmor.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBANinjaCape.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBAScrunchie.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RBAHardTack.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RBARollCake.class);
		SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.RBAEmptyScroll.class);
		EXOTIC_SCROLLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.RBAScrollOfTalentReset.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RBASuperNova.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBATacticalTherapy.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAUniqueIdea.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAFunnyFirework.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBASpringPunch.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAChistka.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAPiety.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_SP.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMulligan.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBATrickOrTrick.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MT.RBAFancyLight.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBAMTET.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBAShootingStar.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBABeyondTheLumination.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBATwinDragon.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAJusticeIncarnate.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_SP.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T1.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T2.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T3.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T4.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T5.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAWineRedAdmire.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.RBAAssassinsKunai.class);
		//items ported from deranged
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.AluminumSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.NuclearHatchet.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.RunicBladeMkII.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.ConstructWand.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.BloomingPick.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.LuminousCutlass.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.MoltenStrife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.ElementalDirk.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.DreadSlicer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.SharksTooth.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.ExoKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.TheReaper.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.Starsmasher.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.RoyalBrand.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.DoNotDieElixir.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.KromerPotion.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.KromerScroll.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Pipisfusion.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.ScammingSpell.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.Kromer.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.quest.RedCrystal.class);
		MISC_EQUIPMENT.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.CheesyCheest.class);
		//items ported from xun
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.BarricadeCurse.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Alienating.class);
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Peaceful.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Bayonet.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BoneSpike.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Canopy.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Chixiao.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crabclaw.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crystalsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMdrill.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Daggeraxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darknesssword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Doublesword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dwarvessword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Erlangknife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Fetter.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Fork.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Futsunomitama.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Havoc.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HiddenBlade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Hoe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Ironbar.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Jingubang.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LightFlail.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Lightsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longassassinsblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Miaoblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rake.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Roguedagger.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shangfang.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SleeveArrow.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.StoneCudgel.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Twobroadaxe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Vineshield.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Windblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ZhugeCrossbow.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Antimatter.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.BoneFragment.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Clay.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CoinDart.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FlyingCymbals.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCobble.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Headdart.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.IronThorn.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Ironball.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Lancet.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.LightBoomerang.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Mucus.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Scorpiospike.class);
		THROWN_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.SmokeBombWep.class);

		//items ported from xun
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatknife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Jieniu.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Jiewan.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Ripperclaw.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Chainwhip.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Etherealsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Wicker.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Wolftailgrassspear.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatrunicblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longrunicblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortrunicblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Smallrunicblade.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KiteShield.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darkgoldsword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Zhouyi.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunKnife.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunScythe.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunNunchaku.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunHalberd.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunStoneHammer.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunHeavyFlail.class);

		//items ported from xun
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.DoorPlank.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.EmptyPot.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.GemPowder.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Dizziness.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Freezingglyph.class);
		GLYPHS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Vengeance.class);
		ARTIFACTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask.class);
		MISC_CONSUMABLES.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodPocket.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.AdrenalineBomb.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.PhantomBomb.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.StenchBomb.class);
		BOMBS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.bombs.WarpBomb.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.Tribute.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ConfusionBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.HeatBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.RegrowingBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.TrackingBrew.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAmnesia.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfConcealment.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfCrashCourse.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfOverdraft.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfEnlightening.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMana.class);
		RINGS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfVision.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.CorruptionSpell.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Extract.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicBan.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Scout.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonWraith.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Uptier.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.Watchtower.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.ConstantEnergy.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.ConstantRecharge.class);
		SPELLS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalTran.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.CursedCoin.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MagicalGem.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MemberCard.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SolidifiedMetal.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.StoneofIntelligence.class);
		TRINKETS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WornLock.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAvalanche.class);

		//items ported from magicling
		ENCHANTMENTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.HaloBlazing.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BoomSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KingSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WhiteBlastSword.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.thanks.GrilledHerring.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HaloDart.class);
		TIPPED_DARTS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.LightDart.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.PotionOfLightningShiled.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLightningShiledX.class);
		FOOD.addItems(com.shatteredpixel.shatteredpixeldungeon.items.food.RiceDumplings.class);
		BREWS_ELIXIRS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfNukeCole.class);
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SliverLockSword.class);
		POTIONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun.class);
		WANDS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfHightHunderStorm.class);

		//items ported from magicling
		MELEE_WEAPONS.addItems(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.KingAxe.class);


	}

	public static ArrayList<Catalog> equipmentCatalogs = new ArrayList<>();
	static {
		equipmentCatalogs.add(MELEE_WEAPONS);
		equipmentCatalogs.add(ARMOR);
		equipmentCatalogs.add(ENCHANTMENTS);
		equipmentCatalogs.add(GLYPHS);
		equipmentCatalogs.add(THROWN_WEAPONS);
		equipmentCatalogs.add(WANDS);
		equipmentCatalogs.add(RINGS);
		equipmentCatalogs.add(ARTIFACTS);
		equipmentCatalogs.add(TRINKETS);
		equipmentCatalogs.add(MISC_EQUIPMENT);
	}

	public static ArrayList<Catalog> consumableCatalogs = new ArrayList<>();
	static {
		consumableCatalogs.add(POTIONS);
		consumableCatalogs.add(SCROLLS);
		consumableCatalogs.add(SEEDS);
		consumableCatalogs.add(STONES);
		consumableCatalogs.add(FOOD);
		consumableCatalogs.add(EXOTIC_POTIONS);
		consumableCatalogs.add(EXOTIC_SCROLLS);
		consumableCatalogs.add(BOMBS);
		consumableCatalogs.add(TIPPED_DARTS);
		consumableCatalogs.add(BREWS_ELIXIRS);
		consumableCatalogs.add(SPELLS);
		consumableCatalogs.add(MISC_CONSUMABLES);
	}
	
	public static boolean isSeen(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls)) {
				return cat.seen.get(cls);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<?> cls){
		if (Dungeon.sandbox) return;
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
				cat.seen.put(cls, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateCatalogBadges();
	}

	public static int useCount(Class<?> cls){
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls)) {
				return cat.useCount.get(cls);
			}
		}
		return 0;
	}

	public static void countUse(Class<?> cls){
		countUses(cls, 1);
	}

	public static void countUses(Class<?> cls, int uses){
		if (Dungeon.sandbox) return;
		for (Catalog cat : values()) {
			if (cat.useCount.containsKey(cls) && cat.useCount.get(cls) != Integer.MAX_VALUE) {
				cat.useCount.put(cls, cat.useCount.get(cls)+uses);
				if (cat.useCount.get(cls) < -1_000_000_000){ //to catch cases of overflow
					cat.useCount.put(cls, Integer.MAX_VALUE);
				}
				Journal.saveNeeded = true;
			}
		}
	}

	private static final String CATALOG_CLASSES = "catalog_classes";
	private static final String CATALOG_SEEN    = "catalog_seen";
	private static final String CATALOG_USES    = "catalog_uses";
	
	public static void store( Bundle bundle ){

		ArrayList<Class<?>> classes = new ArrayList<>();
		ArrayList<Boolean> seen = new ArrayList<>();
		ArrayList<Integer> uses = new ArrayList<>();
		
		for (Catalog cat : values()) {
			for (Class<?> item : cat.items()) {
				if (cat.seen.get(item) || cat.useCount.get(item) > 0){
					classes.add(item);
					seen.add(cat.seen.get(item));
					uses.add(cat.useCount.get(item));
				}
			}
		}

		Class<?>[] storeCls = new Class[classes.size()];
		boolean[] storeSeen = new boolean[seen.size()];
		int[] storeUses = new int[uses.size()];

		for (int i = 0; i < storeCls.length; i++){
			storeCls[i] = classes.get(i);
			storeSeen[i] = seen.get(i);
			storeUses[i] = uses.get(i);
		}
		
		bundle.put( CATALOG_CLASSES, storeCls );
		bundle.put( CATALOG_SEEN, storeSeen );
		bundle.put( CATALOG_USES, storeUses );
		
	}
	
	public static void restore( Bundle bundle ){

		if (bundle.contains(CATALOG_CLASSES)){
			Class<?>[] classes = bundle.getClassArray(CATALOG_CLASSES);
			boolean[] seen = bundle.getBooleanArray(CATALOG_SEEN);
			int[] uses = bundle.getIntArray(CATALOG_USES);

			for (int i = 0; i < classes.length; i++){
				for (Catalog cat : values()) {
					if (cat.seen.containsKey(classes[i])) {
						cat.seen.put(classes[i], seen[i]);
						cat.useCount.put(classes[i], uses[i]);
					}
				}

			}
		}

	}
	
}
