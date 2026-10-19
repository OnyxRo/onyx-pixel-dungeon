/*
 * Onyx's Pixel Dungeon
 * Hooks for content ported from Summoning Pixel Dungeon Reincarnated (TrashboxBobylev).
 * Every call site in a Shattered class is one line marked //summoning that calls a method here.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;

public class SummoningHooks {

	//Summoning PD's two guide pages are found by play, the first summon and the first tier change
	public static void findGuidePage( String page ){
		if (Dungeon.sandbox) return;
		if (com.shatteredpixel.shatteredpixeldungeon.journal.Document.ADVENTURERS_GUIDE.findPage(page)) {
			try {
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.flashForDocument(
						com.shatteredpixel.shatteredpixeldungeon.journal.Document.ADVENTURERS_GUIDE, page);
			} catch (Exception e) {
				//no game scene in headless tests
			}
		}
	}

	//==== the ring artifacts, each hook only acts while its artifact is worn ====

	private static com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff boots( Hero hero ){
		return hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff.class);
	}

	//movement speed, the boots learn from speed boosts, freerun faster, and a cursed pair caps speed at normal
	public static float speed( Hero hero, float speed ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff b = boots(hero);
		if (b == null) return speed;
		if (b.isCursed()) return Math.min(1f, speed);
		if (speed > 1) b.getExp(speed);
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum m =
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class);
		if (m != null) speed *= m.speedMultiplier();
		return speed;
	}

	public static float attackDelay( Hero hero, float delay ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff b = boots(hero);
		if (b == null) return delay;
		if (b.isCursed()) return Math.max(1f, delay);
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum m =
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class);
		if (m != null) delay /= m.furorMultiplier();
		return delay;
	}

	public static void onStep( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff b = boots(hero);
		if (b != null && !b.isCursed()) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero,
					com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class).gainStack();
		}
	}

	public static int evasion( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum m =
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class);
		return m == null ? 0 : m.evasionBonus(hero.lvl, 0);
	}

	private static com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff quiver( Hero hero ){
		return hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff.class);
	}

	//thrown weapon accuracy, the quiver's mark makes them never miss and a cursed quiver spoils the aim
	public static float accuracy( Hero hero, com.shatteredpixel.shatteredpixeldungeon.actors.Char target,
	                              com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon wep, float accuracy ){
		if (!(wep instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon)) return accuracy;
		if (target != null && target.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.powers.QuiverMark.class) != null) return Float.POSITIVE_INFINITY;
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff q = quiver(hero);
		if (q != null && q.isCursed()) accuracy *= 0.33f;
		return accuracy;
	}

	public static boolean blocksSurpriseAttack( Hero hero ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff q = quiver(hero);
		return q != null && q.isCursed()
				&& hero.belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
	}

	//the quiver's mark lets thrown weapons pierce armor
	public static boolean ignoresArmor( com.shatteredpixel.shatteredpixeldungeon.actors.Char attacker, com.shatteredpixel.shatteredpixeldungeon.actors.Char enemy ){
		return attacker instanceof Hero && enemy != null
				&& enemy.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.powers.QuiverMark.class) != null
				&& ((Hero) attacker).belongings.attackingWeapon() instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
	}

	//thrown weapons charge the quiver, spirit arrows a little less
	public static void onMissileUsed( float amount ){
		if (Dungeon.hero == null) return;
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff q = quiver(Dungeon.hero);
		if (q != null) q.gainCharge(amount);
	}

	private static com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff fuel( com.shatteredpixel.shatteredpixeldungeon.actors.Char ch ){
		return ch == null ? null : ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff.class);
	}

	//a wand out of charges can still zap from the fuel container
	public static boolean fuelCanZap( Hero hero, com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand wand, int charges ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff f = fuel(hero);
		return f != null && !f.isCursed() && f.canUseCharge(wand, wand.cursed ? 1 : charges);
	}

	//called after a zap took its charges, a wand that went below zero takes the rest from the container
	public static void fuelAfterZap( com.shatteredpixel.shatteredpixeldungeon.actors.Char user, com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand wand ){
		if (wand.curCharges >= 0) return;
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff f = fuel(user);
		if (f != null && !f.isCursed()) f.useCharge(wand, -wand.curCharges);
		wand.curCharges = 0;
	}

	//a cursed fuel container stops wands from recharging
	public static boolean wandCanRecharge( com.shatteredpixel.shatteredpixeldungeon.actors.Char owner ){
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff f = fuel(owner);
		return f == null || !f.isCursed();
	}

	//the Conjurer heals 5 health whenever she eats, as in the mod (not in the Hell mode)
	public static void onFoodEaten( Hero hero ){
		if (hero.heroClass == HeroClass.CONJURER && hero.HP < hero.HT && Dungeon.mode != Dungeon.GameMode.HELL) {
			hero.HP = Math.min( hero.HP + 5, hero.HT );
			if (hero.sprite != null) hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		}
	}

}
