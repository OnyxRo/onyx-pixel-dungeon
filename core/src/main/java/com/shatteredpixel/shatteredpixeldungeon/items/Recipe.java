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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
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
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
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
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class Recipe {
	
	public abstract boolean testIngredients(ArrayList<Item> ingredients);
	
	public abstract int cost(ArrayList<Item> ingredients);
	
	public abstract Item brew(ArrayList<Item> ingredients);
	
	public abstract Item sampleOutput(ArrayList<Item> ingredients);
	
	//subclass for the common situation of a recipe with static inputs and outputs
	//summoning, Summoning PD prices its brews and parchments from their ingredients
	public static int calculatePrice(Recipe recipe){
		if (!(recipe instanceof SimpleRecipe)) return 0;
		SimpleRecipe r = (SimpleRecipe) recipe;
		int cost = 0;
		for (int i = 0; i < r.inputs.length; i++){
			try {
				cost += com.watabou.utils.Reflection.newInstance(r.inputs[i]).quantity(r.inQuantity[i]).value();
			} catch (Throwable e) {
				return 0;
			}
		}
		return cost / Math.max(1, r.outQuantity);
	}

	public static abstract class SimpleRecipe extends Recipe {
		
		//*** These elements must be filled in by subclasses
		protected Class<?extends Item>[] inputs; //each class should be unique
		protected int[] inQuantity;
		
		protected int cost;
		
		protected Class<?extends Item> output;
		protected int outQuantity;
		//***
		
		//gets a simple list of items based on inputs
		public ArrayList<Item> getIngredients() {
			ArrayList<Item> result = new ArrayList<>();
			for (int i = 0; i < inputs.length; i++) {
				Item ingredient = Reflection.newInstance(inputs[i]);
				ingredient.quantity(inQuantity[i]);
				result.add(ingredient);
			}
			return result;
		}
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			
			int[] needed = inQuantity.clone();
			
			for (Item ingredient : ingredients){
				if (!ingredient.isIdentified()) return false;
				for (int i = 0; i < inputs.length; i++){
					if (ingredient.getClass() == inputs[i]){
						needed[i] -= ingredient.quantity();
						break;
					}
				}
			}
			
			for (int i : needed){
				if (i > 0){
					return false;
				}
			}
			
			return true;
		}
		
		public int cost(ArrayList<Item> ingredients){
			return cost;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			int[] needed = inQuantity.clone();
			
			for (Item ingredient : ingredients){
				for (int i = 0; i < inputs.length; i++) {
					if (ingredient.getClass() == inputs[i] && needed[i] > 0) {
						if (needed[i] <= ingredient.quantity()) {
							ingredient.quantity(ingredient.quantity() - needed[i]);
							needed[i] = 0;
						} else {
							needed[i] -= ingredient.quantity();
							ingredient.quantity(0);
						}
					}
				}
			}
			
			//sample output and real output are identical in this case.
			return sampleOutput(null);
		}
		
		//ingredients are ignored, as output doesn't vary
		public Item sampleOutput(ArrayList<Item> ingredients){
			try {
				Item result = Reflection.newInstance(output);
				result.quantity(outQuantity);
				return result;
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException( e );
				return null;
			}
		}
	}
	
	
	//*******
	// Static members
	//*******

	private static Recipe[] variableRecipes = new Recipe[]{
			//none for now
	};
	
	private static Recipe[] oneIngredientRecipes = new Recipe[]{
		//deranged, three red crystals make a potion of strength (one stack, so a one ingredient recipe)
		new com.shatteredpixel.shatteredpixeldungeon.items.quest.RedCrystal.StrengthRecipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.RatBeacon1.Recipe(), //trap dungeon
		new Scroll.ScrollToStone(),
		new ExoticPotion.PotionToExotic(),
		new ExoticScroll.ScrollToExotic(),
		new ArcaneResin.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing.MembraneRecipe(),
		new LiquidMetal.Recipe(),
		new BlizzardBrew.Recipe(),
		new InfernalBrew.Recipe(),
		new AquaBrew.Recipe(),
		new ShockingBrew.Recipe(),
		new ElixirOfDragonsBlood.Recipe(),
		new ElixirOfIcyTouch.Recipe(),
		new ElixirOfToxicEssence.Recipe(),
		new ElixirOfMight.Recipe(),
		new ElixirOfFeatherFall.Recipe(),
		new MagicalInfusion.Recipe(),
		new BeaconOfReturning.Recipe(),
		new PhaseShift.Recipe(),
		new Recycle.Recipe(),
		new TelekineticGrab.Recipe(),
		new SummonElemental.Recipe(),
		new StewedMeat.oneMeat(),
		new TrinketCatalyst.Recipe(),
		new Trinket.UpgradeTrinket(),
		//Shittened PD
		new com.shatteredpixel.shatteredpixeldungeon.items.food.StewedSmallMeat.oneMeat(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.WaxBar.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.FriedEgg.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.MilkCheese.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Flour.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Bread.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Sashimi.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.CheeseSlice.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.UntaxedFood.TaxedRecipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.CookedWarrior.Recipe(),
		//rearranged
		new BulletItem.Recipe(),
		new BulletBelt.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spellbook.SpellBook.WandToSpellBook(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.FireMaker.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.IceMaker.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.RapidGrowth.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.ElectricityImbue.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.PotionToPill(),
		//transformation
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.TransformSpell.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.MetamorphosisPrism.Recipe(),
		//Community PD
		new com.shatteredpixel.shatteredpixeldungeon.items.GemstoneDust.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.RNGManipulator.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.CommunityFury.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.RepairedRapier.Recipe(),
		//xun, Xun's PD
		new com.shatteredpixel.shatteredpixeldungeon.items.GemPowder.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.HeatBrew.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.RegrowingBrew.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.TrackingBrew.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ConfusionBrew.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfEnlightening.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMana.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfConcealment.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Watchtower.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicBan.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Scout.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.ConstantRecharge.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.ConstantEnergy.Recipe(),
	};
	
	private static Recipe[] twoIngredientRecipes = new Recipe[]{
		new Blandfruit.CookFruit(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfTrapSkill.Recipe(), //trap dungeon
		new Bomb.EnhanceBomb(),
		new UnstableBrew.Recipe(),
		new CausticBrew.Recipe(),
		new ElixirOfArcaneArmor.Recipe(),
		new ElixirOfAquaticRejuvenation.Recipe(),
		new ElixirOfHoneyedHealing.Recipe(),
		new UnstableSpell.Recipe(),
		new Alchemize.Recipe(),
		new CurseInfusion.Recipe(),
		new ReclaimTrap.Recipe(),
		new WildEnergy.Recipe(),
		new StewedMeat.twoMeat(),
		//Shittened PD
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Omelet.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Pancakes.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Oil.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Hashbrowns.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Spaghetti.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.StirFry.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.BakedFish.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.BerryBalls.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Meatballs.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.IceCream.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.BerryCake.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.BasedCake.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.DeepfriedRat.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.GoblinCurse.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.misc.FOFFSDegree.Recipe(),
		//rearranged
		new GunSmithingTool.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Evolution.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.FireImbueSpell.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfTalent.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.UpgradeDust.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableIdentification.Recipe(),
		//polished
		new com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyedMeat.Recipe(),
		//elektro, Elektrocheckers PD's spells
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.PrismaticImageSpell.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Transfiguration.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicBridge.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.InvariantAncientRune.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.FlameShot.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.IceShot.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.VenomShot.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.VampiricShot.Recipe(),
		//Sandbox PD, a known potion poured into a potion cocktail
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.PotionCocktail.Recipe(),
		//Community PD
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.FrostfireBrew.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.LeanyElixir.Recipe(),
		//deranged, DERanged PD's Kromer brews
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.ScammingSpell.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.KromerScroll.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.DoNotDieElixir.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.KromerPotion.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Pipisfusion.Recipe(),
		//xun, Xun's PD
		new com.shatteredpixel.shatteredpixeldungeon.items.EmptyPot.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfOverdraft.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfCrashCourse.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAmnesia.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.CorruptionSpell.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalTran.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonWraith.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.Uptier.Recipe(),
	};

	private static Recipe[] threeIngredientRecipes = new Recipe[]{
		new Potion.SeedToPotion(),
		new StewedMeat.threeMeat(),
		new MeatPie.Recipe(),
		//rearranged
		new com.shatteredpixel.shatteredpixeldungeon.items.changer.BluePrint.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.alchemy.Cross.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.alchemy.PotOThunder.Recipe(),
		//Shittened PD
		new com.shatteredpixel.shatteredpixeldungeon.items.misc.GoldBar.Craft(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenMeat.Craft(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.TidePod.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Coffee.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.TripleShotEspresso.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.PancakesWithCheese.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.CompleteBreakfast.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Bruschetta.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Pizza.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.CrispyBass.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.Cookies.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.DanishPastry.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.CakeBase.Recipe(),
		new com.shatteredpixel.shatteredpixeldungeon.items.food.ChocolateCake.Recipe(),
		//transformation, two scrolls and a stylus
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.RubbingsTome.Recipe(),
		//transformation, corpse dust, a rubbings tome and a wand
		new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Necronomicon.Recipe(),
		//elektro, Elektrocheckers PD
		new com.shatteredpixel.shatteredpixeldungeon.items.spells.GreaterTransmutation.Recipe(),
		//Sandbox PD, the Amulet, the King's crown and Tengu's mask
		new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfYendor.Recipe(),
	};
	
	public static ArrayList<Recipe> findRecipes(ArrayList<Item> ingredients){

		ArrayList<Recipe> result = new ArrayList<>();

		for (Recipe recipe : variableRecipes){
			if (recipe.testIngredients(ingredients)){
				result.add(recipe);
			}
		}

		if (ingredients.size() == 1){
			for (Recipe recipe : oneIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
			
		} else if (ingredients.size() == 2){
			for (Recipe recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
			
		} else if (ingredients.size() == 3){
			for (Recipe recipe : threeIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
		}
		com.shatteredpixel.shatteredpixeldungeon.RBAHooks.addRecipes(ingredients, result); //rebluearchive
		//only Shattered's own recipes until Yog is beaten in a copy from GitHub, see ModsUnlock
		if (com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.locked()) result.removeIf(r -> !com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.isOriginal(r.getClass()));

		return result;
	}
	
	public static boolean usableInRecipe(Item item){
		//transformation, corpse dust goes into the Necronomicon while the hero carries a rubbings tome
		if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust && com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero != null
				&& com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.spells.RubbingsTome.class) != null) return true;
		//xun, Xun's PD grinds unequipped rings into gem powder
		if (!com.shatteredpixel.shatteredpixeldungeon.ModsUnlock.locked() && item instanceof com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring && com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero != null
				&& !item.isEquipped(com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero)){
			return item.isIdentified() && !item.cursed;
		}
		//only upgradeable thrown weapons and wands allowed among equipment items
		if (item instanceof EquipableItem){
			return item.cursedKnown && !item.cursed &&
					item instanceof MissileWeapon && item.isUpgradable();
		} else if (item instanceof Wand) {
			return item.cursedKnown && !item.cursed;
		} else {
			//other items can be unidentified, but not cursed (DERanged's Kromer is always cursed)
			return !item.cursed || item instanceof com.shatteredpixel.shatteredpixeldungeon.items.quest.Kromer;
		}
	}
}


