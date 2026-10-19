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

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

	public static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Hunger.HUNGRY;
	//SPS compat. The mod's foods set these and override doEat for their effect.
	public int hornValue = 3;
	public float eattime = 3;
	public void doEat() {}

	//from Shittened PD, what is in the food, shown in its description window
	public float carbs = 30f;
	public float fats = 18f;
	public float proteins = 10f;
	public float salt = 1.2f;

	public boolean drink = false;

	public String eatSound;

	//Abyssalnite, how much abyssal infection eating this removes
	public float clean = 0f;
	
	{
		stackable = true;
		image = ItemSpriteSheet.RATION;

		defaultAction = AC_EAT;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );
			Catalog.countUse(getClass());
			
			satisfy(hero);
			cureinfc(hero);
			curUser = hero;
			doEat();
			GLog.i( Messages.get(this, "eat_msg") );
			
			hero.sprite.operate( hero.pos );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			eatSFX();
			
			hero.spend( eatingTime() );

			Dungeon.level.onHeroConsumableUsed(hero, this);
			Talent.onFoodEaten(hero, energy, this);
			hero.lastEaten = this;
			if (hero.subClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.DEVOTEE) {
				com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial ref = hero.belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial.class);
				if (ref != null) ref.DevoteeCharges = Math.min(1 + hero.pointsInTalent(Talent.DEVOUR_RITUAL), 3);
			}
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		}
	}

	protected void eatSFX(){
		if (eatSound != null) {
			Sample.INSTANCE.play(eatSound);
		} else if (drink) {
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
		} else {
			Sample.INSTANCE.play( Assets.Sounds.EAT );
		}
	}

	public String nutritionalInfo() {
		return "NUTRITIONAL FACTS\nper portion\n\n" +
				"Energy:              " + energy + " kcal (" + (int) (energy * 4.184) + " kj)" + "\n" +
				"Fats:                 " + fats + "g\n" +
				"Carbohydrates:  " + carbs + "g\n" +
				"Proteins:            " + proteins + "g\n" +
				"Salt:                   " + salt + "g";
	}

	protected float eatingTime(){
		if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
			|| Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
			|| Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
			|| Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
			|| Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)
			|| Dungeon.hero.hasTalent(Talent.ENLIGHTENING_MEAL)
			|| Dungeon.hero.hasTalent(Talent.WHISPERING_MEAL)
			//rearranged
			|| Dungeon.hero.hasTalent(Talent.INFINITE_BULLET_MEAL)
			|| Dungeon.hero.hasTalent(Talent.CRITICAL_MEAL)
			|| Dungeon.hero.hasTalent(Talent.NATURES_MEAL)
			|| Dungeon.hero.hasTalent(Talent.IMPREGNABLE_MEAL)
			|| Dungeon.hero.hasTalent(Talent.HEALING_MEAL)
			|| Dungeon.hero.hasTalent(Talent.FIGHTING_MEAL)
			|| com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.fastMeal(Dungeon.hero)){
			return com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.eatingTime(Dungeon.hero, TIME_TO_EAT - 2);
		} else {
			return com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.eatingTime(Dungeon.hero, TIME_TO_EAT);
		}
	}
	
	protected void cureinfc( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbyssalInfection inf = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbyssalInfection.class);
		if (inf != null && clean > 0) inf.cureinfc( clean );
	}

	//Transformation PD's Vegetarianism, plant foods feed twice as much, anything else a bit less
	protected boolean isVegetarianFood(){
		return this instanceof Berry || this instanceof SmallRation || this instanceof SupplyRation
				|| this instanceof Blandfruit || this instanceof Pasty || this instanceof Tincture
				|| this instanceof GreenGlowFruit;
	}

	protected void satisfy( Hero hero ){
		float foodVal = com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.foodValue(hero, this, energy); //transformation
		foodVal = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedRework.addedMeal(hero, foodVal); //rearranged
		if (hero.hasTalent(Talent.VEGETARIANISM)){
			if (isVegetarianFood()){
				foodVal *= 2f;
				Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed.class).reset();
				if (hero.pointsInTalent(Talent.VEGETARIANISM) == 2){
					Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing.class).setHeal(hero.HT/4, 0.25f, 0);
				}
			} else {
				foodVal *= 0.7f;
			}
		}
		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			foodVal /= 3f;
		}

		Artifact.ArtifactBuff buff = hero.buff( HornOfPlenty.hornRecharge.class );
		if (buff != null && buff.isCursed()){
			foodVal *= 0.67f;
			GLog.n( Messages.get(Hunger.class, "cursedhorn") );
		}

		Buff.affect(hero, Hunger.class).satisfy(foodVal);
		com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.onEat(hero); //rearranged, food clears Fatigue

		//Experienced PD's Mystical Meal and Magnetic Meal perks
		if (hero.perks.contains(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks.Perk.MYSTICAL_MEAL)){
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge.class).set(6f);
		}
		if (hero.perks.contains(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks.Perk.COLLECT_EVERYTHING)
				&& this instanceof com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation){
			for (com.shatteredpixel.shatteredpixeldungeon.items.Heap h : Dungeon.level.heaps.valueList()){
				if (h.type == com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type.HEAP) {
					com.shatteredpixel.shatteredpixeldungeon.items.Item item = h.peek();
					if (item.doPickUp(hero, h.pos)) {
						h.pickUp();
						hero.spend(-com.shatteredpixel.shatteredpixeldungeon.items.Item.TIME_TO_PICK_UP); //eating already takes a turn
						GLog.i( Messages.capitalize(Messages.get(hero, "you_now_have", item.name())) );
					} else {
						GLog.w( Messages.capitalize(Messages.get(hero, "you_cant_have", item.name())) );
						h.sprite.drop();
					}
				}
			}
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 10 * quantity;
	}
}
