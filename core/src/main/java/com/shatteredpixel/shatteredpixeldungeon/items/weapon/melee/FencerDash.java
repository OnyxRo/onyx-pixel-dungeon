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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

/**
 * Re-ARranged PD's Fencer, the Duelist's third subclass. Her weapon charges buy a short dash instead of the
 * Champion's weapon swap. MeleeWeapon.Charger calls these from one line each.
 */
public class FencerDash {

	public static boolean isFencer(){
		return Dungeon.hero != null && Dungeon.hero.subClass.is(HeroSubClass.FENCER);
	}

	//the Charger shows its action for the Champion and the Fencer
	public static boolean showsAction(){
		return Dungeon.hero != null && (Dungeon.hero.subClass.is(HeroSubClass.CHAMPION) || isFencer());
	}

	public static float chargeMulti( Hero hero ){
		float multi = 1f;
		if (hero.hasTalent(Talent.FASTER_CHARGE)) {
			multi *= 1 + hero.pointsInTalent(Talent.FASTER_CHARGE)/12f;
		}
		if (hero.hasTalent(Talent.TWIN_SWORD) && hero.belongings.weapon != null && hero.belongings.secondWep != null
				&& hero.belongings.weapon.getClass() == hero.belongings.secondWep.getClass()) {
			multi *= 1.25f;
		}
		return multi;
	}

	public static Image icon(){
		Image ico = new BuffIcon(BuffIndicator.HASTE, true);
		ico.hardlight(0x5A00B2);
		return ico;
	}

	static float chargeToUse( Hero hero, boolean consume ){
		float chargeToUse = 1f;
		if (hero.buff(DashTracker.class) != null) {
			chargeToUse *= 0.5f;
			if (consume) hero.buff(DashTracker.class).detach();
		}
		if (hero.hasTalent(Talent.CLAM_STEPS)) {
			chargeToUse *= Math.pow(0.795, hero.pointsInTalent(Talent.CLAM_STEPS));
		}
		Talent.AgressiveMovementAbilityTracker tracker = hero.buff(Talent.AgressiveMovementAbilityTracker.class);
		if (tracker != null && tracker.wepAbilUsed) {
			chargeToUse *= 0.5f;
			if (consume) tracker.detach();
		}
		return chargeToUse;
	}

	public static void doAction( MeleeWeapon.Charger charger ){
		Hero hero = Dungeon.hero;
		if (charger.charges + charger.partialCharge < chargeToUse(hero, false)) {
			GLog.w(Messages.get(MeleeWeapon.class, "ability_no_charge"));
		} else {
			GameScene.selectCell(dasher);
		}
	}

	//Champion's weapon swap with Quick Followup
	public static void onSwap( Hero hero ){
		if (hero.buff(Talent.QuickFollowupTracker.class) != null) {
			hero.buff(Talent.QuickFollowupTracker.class).detach();
		}
		if (hero.buff(Talent.QuickFollowupCooldown.class) == null && hero.hasTalent(Talent.QUICK_FOLLOWUP)) {
			Buff.affect(hero, Talent.QuickFollowupTracker.class);
			Buff.affect(hero, Talent.QuickFollowupCooldown.class, 10f);
		}
	}

	private static final CellSelector.Listener dasher = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			final Hero hero = Dungeon.hero;
			if (target == null || target == -1 || (!Dungeon.level.visited[target] && !Dungeon.level.mapped[target])){
				return;
			}

			PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE){
				GLog.w( Messages.get(MeleeWeapon.class, "dash_bad_position") );
				return;
			}

			if (hero.rooted){
				GLog.w( Messages.get(MeleeWeapon.class, "rooted") );
				return;
			}

			int range = 2;
			if (hero.hasTalent(Talent.SOULIZE)) {
				range += 1;
			}

			if (Dungeon.level.distance(hero.pos, target) > range){
				GLog.w(Messages.get(MeleeWeapon.class, "dash_bad_position"));
				return;
			}

			Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);
			if (hero.pointsInTalent(Talent.SOULIZE) > 1) {
				dash = new Ballistica(hero.pos, target, Ballistica.DASH);
				if (hero.pointsInTalent(Talent.SOULIZE) > 2) {
					dash = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET);
				}
			}

			if (!dash.collisionPos.equals(target)
					|| Actor.findChar(target) != null
					|| (Dungeon.level.solid[target] && !Dungeon.level.passable[target])
					|| Dungeon.level.map[target] == Terrain.CHASM){
				GLog.w(Messages.get(MeleeWeapon.class, "dash_bad_position"));
				return;
			}

			hero.busy();
			Sample.INSTANCE.play(Assets.Sounds.MISS);
			hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, target)));
			hero.sprite.jump(hero.pos, target, 0, 0.1f, new Callback() {
				@Override
				public void call() {
					if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
						Door.leave( hero.pos );
					}
					hero.pos = target;
					if (hero.pointsInTalent(Talent.AGGRESSIVE_MOVEMENT) > 1) {
						for (int i : PathFinder.NEIGHBOURS8){
							Char ch = Actor.findChar( target+i );
							if (ch != null && ch.alignment != Char.Alignment.ALLY && hero.belongings.weapon != null){
								ch.damage(Math.round(hero.belongings.weapon.damageRoll(hero) * 0.1f), hero);
								if (hero.pointsInTalent(Talent.AGGRESSIVE_MOVEMENT) > 2) {
									Buff.affect(ch, Vulnerable.class, 3f);
								}
							}
						}
						Sample.INSTANCE.play(Assets.Sounds.BLAST);
						WandOfBlastWave.BlastWave.blast(target);
						Camera.main.shake(0.5f, 0.5f);
					}
					Dungeon.level.occupyCell(hero);
					hero.next();
				}
			});

			Buff.affect(hero, DashAttack.class).set();

			MeleeWeapon.Charger charger = hero.buff(MeleeWeapon.Charger.class);
			if (charger != null) charger.useCharges(chargeToUse(hero, true));

			if (hero.hasTalent(Talent.KINETIC_MOVEMENT)) {
				Buff.affect(hero, DashTracker.class).set();
			}

			Item.updateQuickslot();
			AttackIndicator.updateState();
		}
		@Override
		public String prompt() {
			return Messages.get(SpiritBow.class, "prompt");
		}
	};

	//the next hit after a dash never misses and hits harder
	public static class DashAttack extends Buff {
		{
			announced = false;
			type = buffType.POSITIVE;
		}

		int duration = 0;
		float dmgMulti = 1.1f;

		public void set() {
			dmgMulti = 1.1f * (float)Math.pow(1.05, Dungeon.hero.pointsInTalent(Talent.CRITICAL_MOMENTUM));
		}

		public float getDmgMulti() {
			return dmgMulti;
		}

		@Override
		public boolean act() {
			duration --;
			if (duration <= 0) {
				detach();
			}
			spend(TICK);
			return true;
		}

		public static final String DURATION          = "duration";
		public static final String DMG_MULTI         = "dmgMulti";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DURATION, duration);
			bundle.put(DMG_MULTI, dmgMulti);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			duration = bundle.getInt(DURATION);
			dmgMulti = bundle.getFloat(DMG_MULTI);
		}
	}

	//Kinetic Movement, the next dash costs half
	public static class DashTracker extends Buff {
		{
			announced = false;
			type = buffType.POSITIVE;
		}

		float maxDuration;
		float duration;

		public void set() {
			maxDuration = Dungeon.hero.cooldown()+(Dungeon.hero.pointsInTalent(Talent.KINETIC_MOVEMENT));
			duration = maxDuration;
		}

		@Override
		public boolean act() {
			duration -= 1;
			if (duration <= 0) {
				detach();
			}
			spend(TICK);
			return true;
		}

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public float iconFadePercent() {
			return maxDuration <= 0 ? 0 : Math.max(0, 1-duration / maxDuration);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", Messages.decimalFormat("#.##", duration));
		}

		public static final String DURATION          = "duration";
		public static final String MAX_DURATION      = "maxDuration";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DURATION, duration);
			bundle.put(MAX_DURATION, maxDuration);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			duration = bundle.getFloat(DURATION);
			maxDuration = bundle.getFloat(MAX_DURATION);
		}
	}
}
