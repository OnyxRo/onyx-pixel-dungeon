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
