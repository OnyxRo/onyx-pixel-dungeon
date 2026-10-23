/*
 * Onyx's Pixel Dungeon
 * The weapon ability frame DERanged PD's rka weapons use (from Rat King PD 2 by zrp200), GPL-3.0. In the mod it
 * lives inside MeleeWeapon. Here it is its own class, and each rka weapon hands its ability to Shattered's
 * duelistAbility(Hero, Integer).
 */
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class RkaAbility {

	public interface DuelistAbility {
		/** @return whether the ability was used **/
		boolean execute(Hero hero, Integer target, MeleeWeapon weapon);
	}

	public static DuelistAbility activeAbility;

	//Char.spend is protected here, only the hero's is public
	public static void spend( Char ch, float time ){
		if (ch instanceof Hero) ((Hero) ch).spend(time);
	}

	/** a single strong hit on the chosen enemy, the shape most rka abilities take **/
	public static class MeleeAbility implements DuelistAbility {

		protected float delayMulti = 1f;

		@Override
		public boolean execute(Hero hero, Integer target, MeleeWeapon wep) {
			if (target == null) return false;

			Char enemy = Actor.findChar(target);

			if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
				GLog.w(Messages.get(wep, "ability_no_target"));
				return false;
			}
			if (!canAttack(hero, enemy)) {
				GLog.w(Messages.get(wep, "ability_target_range"));
				return false;
			}

			this.abilityWeapon = wep;

			hero.sprite.attack(enemy.pos, () -> {
				beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);
				boolean hit = hero.attack(enemy, dmgMulti(enemy), dmgBoost, accMulti());
				if (hit) {
					onHit(hero, enemy);
				}
				afterHit(enemy, hit);
				afterAbilityUsed(hero);
				Invisibility.dispel();
				if (delayMulti == 0) {
					hero.next();
				} else hero.spendAndNext(hero.attackDelay() * delayMulti);
			});
			return true;
		}

		public int dmgBoost;
		protected float dmgMulti = 1f;

		public MeleeAbility(int dmgBoost) {
			this.dmgBoost = dmgBoost;
		}

		public MeleeAbility() {
			this(0);
		}

		/** damage multiplier passed to Hero.attack **/
		public float dmgMulti(Char enemy) { return dmgMulti; }

		/** acc modifier passed to Hero.attack **/
		public float accMulti() { return Char.INFINITE_ACCURACY; }

		public final void onHit(Hero hero, Char enemy) {
			playSFX();
			if (!enemy.isAlive()) {
				onKill(hero);
				MeleeWeapon.onAbilityKill(hero, enemy);
			} else proc(hero, enemy);
		}

		private MeleeWeapon abilityWeapon;
		public MeleeWeapon weapon() {
			return abilityWeapon;
		}

		public void afterAbilityUsed(Hero hero) {
			abilityWeapon.afterAbilityUsed(hero);
			activeAbility = null;
		}

		protected boolean canAttack(Hero hero, Char enemy) {
			return hero.canAttack(enemy) && !hero.isCharmedBy(enemy);
		}

		protected void beforeAbilityUsed(Hero hero, Char target) {
			activeAbility = this;
			abilityWeapon.beforeAbilityUsed(hero, target);
		}

		protected void playSFX() {
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		}

		/** effect applied when the enemy survives a hit **/
		protected void proc(Hero hero, Char enemy) {/* nothing by default */}

		protected void onKill(Hero hero) {/* nothing by default */}
		public void afterHit(Char enemy, boolean hit) {/* nothing by default */}
	}
}
