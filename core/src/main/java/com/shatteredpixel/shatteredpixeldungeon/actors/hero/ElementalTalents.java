package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.*;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.plants.*;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//elemental, the Alchemist's talents and subclasses from Elemental PD. Each hook is one line in Shattered's classes.
public class ElementalTalents {

	//Attuned Meal, the next infusion effects after a meal are stronger
	public static int surgeCharges = 0;
	//Fluid Moves, attacks left to dodge on this floor
	public static int fluidMoves = 0;

	public static void store( Bundle b ){
		b.put("elemental_surge", surgeCharges);
		b.put("elemental_fluid", fluidMoves);
	}
	public static void restore( Bundle b ){
		surgeCharges = b.contains("elemental_surge") ? b.getInt("elemental_surge") : 0;
		fluidMoves = b.contains("elemental_fluid") ? b.getInt("elemental_fluid") : 0;
	}

	/** True once for an empowered infusion effect, used up as it is read. */
	public static boolean useSurge(){
		if (surgeCharges > 0){
			surgeCharges--;
			return true;
		}
		return false;
	}

	public static float potionBoost( Hero hero ){
		return hero != null && hero.subClass == HeroSubClass.SCIENTIST ? 1.4f : 1f;
	}

	public static boolean elementalist( Char ch ){
		return ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.ELEMENTALIST;
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (talent == Talent.FREAKS_INTUITION){
			int left = hero.pointsInTalent(Talent.FREAKS_INTUITION) == 1 ? 2 : 1;
			for (Item item : hero.belongings){
				if (left <= 0) break;
				if (item instanceof Potion && !item.isIdentified()){
					item.identify();
					left--;
				}
			}
		}
	}

	public static void onFoodEaten( Hero hero ){
		if (hero.hasTalent(Talent.ATTUNED_MEAL)){
			surgeCharges = hero.pointsInTalent(Talent.ATTUNED_MEAL);
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.FRENZIED_FINISH)){
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon
					|| hero.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.AlchemistFlask.Flask){
				Buff.affect(enemy, FrenziedFinishTracker.class);
			} else if (enemy.buff(FrenziedFinishTracker.class) != null){
				dmg += 1 + hero.pointsInTalent(Talent.FRENZIED_FINISH);
				enemy.buff(FrenziedFinishTracker.class).detach();
			}
		}
		return dmg;
	}

	public static class FrenziedFinishTracker extends Buff {}

	public static boolean isImmune( Hero hero, Class effect ){
		if (hero.subClass != HeroSubClass.ELEMENTALIST) return false;
		if (effect == Paralysis.class || effect == ParalyticGas.class || effect == Vertigo.class) return true;
		int chem = hero.pointsInTalent(Talent.CHEMICAL_RESISTANCE);
		if (chem >= 1 && effect == Chill.class) return true;
		if (chem >= 2 && effect == Electricity.class) return true;
		if (chem >= 3 && effect == Corrosion.class) return true;
		return false;
	}

	public static void onDefense( Hero hero, Char enemy ){
		int pts = hero.pointsInTalent(Talent.CHARMING_CHEMICALS);
		if (pts > 0 && enemy != null && enemy.isAlive() && Random.Float() < 0.05f + 0.10f * pts){
			Buff.affect(enemy, Charm.class, Charm.DURATION / 2f).object = hero.id();
		}
	}

	public static boolean fluidMovesDodge( Hero hero ){
		if (fluidMoves > 0 && hero.hasTalent(Talent.FLUID_MOVES)){
			fluidMoves--;
			return true;
		}
		return false;
	}

	/** Called when a floor is first built, Fluid Moves fills up. */
	public static void onNewFloor( Hero hero ){
		if (hero != null && hero.hasTalent(Talent.FLUID_MOVES)){
			fluidMoves = 2 * hero.pointsInTalent(Talent.FLUID_MOVES);
		}
	}

	public static float drinkTime( Hero hero, float time ){
		return hero.hasTalent(Talent.SWIFT_LOBBING) ? time / 2f : time;
	}

	public static void onPotionDrunk( Hero hero, Potion potion ){
		if (hero.hasTalent(Talent.VITAL_BREWS)){
			Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY / 4f * hero.pointsInTalent(Talent.VITAL_BREWS));
		}
		if (hero.pointsInTalent(Talent.SWIFT_LOBBING) >= 2){
			Buff.affect(hero, Haste.class, 1f);
		}
		onPotionUsed(hero, potion);
	}

	public static void onPotionThrown( Hero hero, Potion potion, int cell ){
		if (hero == null) return;
		if (hero.hasTalent(Talent.VOLATILE_POTIONS)
				&& (potion instanceof PotionOfToxicGas || potion instanceof PotionOfFrost || potion instanceof PotionOfParalyticGas
				|| potion instanceof PotionOfLiquidFlame || potion instanceof PotionOfLevitation
				|| potion instanceof PotionOfCorrosiveGas || potion instanceof PotionOfShroudingFog)){
			new Bomb().explode(cell);
			if (hero.pointsInTalent(Talent.VOLATILE_POTIONS) >= 2){
				Char target = com.shatteredpixel.shatteredpixeldungeon.actors.Actor.findChar(cell);
				if (target != null) Buff.affect(target, Poison.class).set(3 + Dungeon.depth / 3);
			}
		}
		int renew = hero.pointsInTalent(Talent.RENEWABLE_POTIONS);
		if (renew > 0 && !(potion instanceof PotionOfStrength) && !(potion instanceof PotionOfExperience)
				&& Random.Float() < (renew == 1 ? 0.1f : renew == 2 ? 0.2f : 0.25f)){
			Item copy = com.watabou.utils.Reflection.newInstance(potion.getClass());
			if (copy != null && !copy.collect()) Dungeon.level.drop(copy, hero.pos);
		}
		onPotionUsed(hero, potion);
	}

	private static void onPotionUsed( Hero hero, Potion potion ){
		if (hero.hasTalent(Talent.REVITALISING_CONCOCTION) && hero.HP < hero.HT / 2){
			hero.HP = Math.min(hero.HT, hero.HP + 2 * hero.pointsInTalent(Talent.REVITALISING_CONCOCTION));
		}
		if (hero.hasTalent(Talent.TRANSMUTERS_TECHNIQUE) && Random.Float() < 0.25f * hero.pointsInTalent(Talent.TRANSMUTERS_TECHNIQUE)){
			Item seed = null;
			if (potion instanceof PotionOfFrost) seed = new Icecap.Seed();
			else if (potion instanceof PotionOfHaste) seed = new Swiftthistle.Seed();
			else if (potion instanceof PotionOfHealing) seed = new Sungrass.Seed();
			else if (potion instanceof PotionOfInvisibility) seed = new Blindweed.Seed();
			else if (potion instanceof PotionOfLevitation) seed = new Stormvine.Seed();
			else if (potion instanceof PotionOfLiquidFlame) seed = new Firebloom.Seed();
			else if (potion instanceof PotionOfMindVision) seed = new Fadeleaf.Seed();
			else if (potion instanceof PotionOfParalyticGas) seed = new Earthroot.Seed();
			else if (potion instanceof PotionOfPurity) seed = new Dreamfoil.Seed();
			else if (potion instanceof PotionOfToxicGas) seed = new Sorrowmoss.Seed();
			if (seed != null && !seed.collect()) Dungeon.level.drop(seed, hero.pos);
		}
	}

	/** Familiar Face, potions cost less in shops. */
	public static int potionPrice( Item item, int price ){
		if (item instanceof Potion && Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.FAMILIAR_FACE)){
			return Dungeon.hero.pointsInTalent(Talent.FAMILIAR_FACE) == 1 ? price * 3 / 4 : price / 2;
		}
		return price;
	}

	/** Possessive Poison. The mod corrupted the dying monster, which did nothing, so here a corrupted wraith rises in its place. */
	public static void onMobDeath( Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null || cause != hero || mob instanceof Wraith || mob.alignment != Char.Alignment.ENEMY) return;
		int pts = hero.pointsInTalent(Talent.POSSESSIVE_POISON);
		if (pts > 0 && Random.Float() < (pts == 1 ? 0.1f : pts == 2 ? 0.2f : 0.25f)){
			Wraith w = Wraith.spawnAt(mob.pos, Wraith.class);
			if (w != null) Buff.affect(w, Corruption.class);
		}
	}

	/** Backup Supplies, extra potions on each new floor. */
	public static int extraPotions(){
		Hero hero = Dungeon.hero;
		if (hero == null) return 0;
		int pts = hero.pointsInTalent(Talent.BACKUP_SUPPLIES);
		if (pts == 0) return 0;
		if (pts == 1) return Random.Int(2);
		if (pts == 2) return 1;
		return 1 + Random.Int(2);
	}
}
