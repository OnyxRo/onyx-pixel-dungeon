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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ThunderImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.Rope;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RearrangedWeapons;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.bow.BowWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

/**
 * Re-ARranged PD's additions to Char.attack, as one line calls.
 */
public class RearrangedChar {

	//the Explorer reaches far with rope and uses up as much rope as the extra distance
	public static void useRope( Char attacker, Char enemy ){
		if (!(attacker instanceof Hero)) return;
		Hero hero = (Hero) attacker;
		if (hero.subClass != HeroSubClass.EXPLORER) return;
		Rope rope = hero.belongings.getItem(Rope.class);
		KindOfWeapon wep = hero.belongings.attackingWeapon();
		if (rope == null || !(wep instanceof MeleeWeapon)) return;
		int weaponReach = wep.reachFactor(hero) + RearrangedWeapons.canReachBonus(hero) - rope.quantity();
		int attackReach = weaponReach + rope.quantity();

		boolean[] passable = BArray.not(Dungeon.level.solid, null);
		for (Char ch : Actor.chars()) {
			if (ch != hero) passable[ch.pos] = false;
		}
		PathFinder.buildDistanceMap(enemy.pos, passable, attackReach);
		if (PathFinder.distance[hero.pos] > weaponReach && PathFinder.distance[hero.pos] != Integer.MAX_VALUE) {
			int ropeUse = (PathFinder.distance[hero.pos] - weaponReach);
			if (hero.hasTalent(Talent.DURABLE_ROPE)) {
				ropeUse = (int)(ropeUse*(0.9f-0.1f*hero.pointsInTalent(Talent.DURABLE_ROPE)));
			}
			rope.quantity(rope.quantity() - ropeUse);
			if (rope.quantity() <= 0) {
				rope.detachAll(hero.belongings.backpack);
			}
			Item.updateQuickslot();
		}
	}

	//the Knight's Chivalry, a hit on an ally in sight lands on the hero instead
	public static Char chivalryGuard( Char attacker, Char enemy ){
		Hero hero = Dungeon.hero;
		if (hero != null && enemy != hero && hero.isAlive() && enemy.alignment == Char.Alignment.ALLY
				&& attacker.alignment != Char.Alignment.ALLY
				&& Dungeon.level.heroFOV[enemy.pos] && hero.hasTalent(Talent.CHIVALRY)) {
			return hero;
		}
		return enemy;
	}

	public static int heroArmorPierce( Hero h, Char enemy, int dr ){
		if (h.belongings.attackingWeapon() instanceof BowWeapon.Arrow
				&& h.buff(BowWeapon.PenetrationShotBuff.class) != null
				&& !Dungeon.level.adjacent(h.pos, enemy.pos)){
			dr = 0;
		}
		if (h.belongings.attackingWeapon() instanceof Gun.Bullet) {
			dr *= ((Gun.Bullet) h.belongings.attackingWeapon()).whatBullet().armorFactor();
		}
		return dr;
	}

	//Mob.die, kills that feed Re-ARranged's talents and gear
	public static void onMobDeath( com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null) return;
		if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FirstAidBuff.FirstAidBuffCooldown.class) != null) {
			hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FirstAidBuff.FirstAidBuffCooldown.class).kill();
		}
		if (mob.alignment != Char.Alignment.ALLY && (cause == hero || cause instanceof com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand
				|| cause instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly)) {
			if (hero.hasTalent(Talent.SOUL_BULLET)) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfiniteBullet.class, hero.pointsInTalent(Talent.SOUL_BULLET));
			}
			if (hero.hasTalent(Talent.ADRENALINE_SURGE)) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.prolong(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline.class, 1+2*hero.pointsInTalent(Talent.ADRENALINE_SURGE));
			}
			if (hero.hasTalent(Talent.PRAY_FOR_DEAD)) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, Talent.PrayForDeadTracker.class, Talent.PrayForDeadTracker.DURATION);
			}
			if (hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding.RidingCooldown.class) != null) {
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding.RidingCooldown.class).kill();
			}
			if (hero.hasTalent(Talent.KINETIC_BATTLE)) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, Talent.KineticBattle.class).set();
			}
			if (hero.subClass == HeroSubClass.MEDICALOFFICER) {
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Command.class).kill();
			}
			if (hero.subClass == HeroSubClass.DEATHKNIGHT){
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulCollect.class).killMob(mob);
			}
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Juggling.kill();
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SharpShooterBuff.kill();
			com.shatteredpixel.shatteredpixeldungeon.items.Saddle.kill(mob);
		}
		//air support kills also give the Medical Officer a command, even past the limit
		if (cause instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Command.CASBomb && hero.subClass == HeroSubClass.MEDICALOFFICER) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Command.class).kill(true);
		}
	}

	//GameScene on a new floor, the Archer's Archer's Foresight may show every trap on the floor
	public static void archersForesight(){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.ARCHERS_FORESIGHT)
				|| !(Dungeon.level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel) || Dungeon.branch != 0) return;
		com.watabou.utils.Random.pushGenerator(Dungeon.seedCurDepth()+1);
		if (com.watabou.utils.Random.Float() < 0.2f+0.3f*hero.pointsInTalent(Talent.ARCHERS_FORESIGHT)){
			int length = Dungeon.level.length();
			for (int i=0; i < length; i++) {
				if (Dungeon.level.traps.get( i ) != null) {
					Dungeon.level.mapped[i] = true;
					Dungeon.level.discover( i );
				}
			}
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.updateFog();
			com.shatteredpixel.shatteredpixeldungeon.utils.GLog.p(Messages.get(com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.class, "trap_discover"));
		}
		com.watabou.utils.Random.popGenerator();
	}

	//HighGrass.trample, the Gunner's Camouflage, the Researcher's Bio Energy, the Archer's Nature Friendly
	public static void onGrassTrampled( Hero hero ){
		if (hero.hasTalent(Talent.CAMOUFLAGE)) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.prolong(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility.class, 1+hero.pointsInTalent(Talent.CAMOUFLAGE));
			com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.MELD );
		}
		if (hero.hasTalent(Talent.BIO_ENERGY)) {
			for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : hero.buffs()){
				if (b instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact.ArtifactBuff
						&& !((com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact.ArtifactBuff) b).isCursed() ) {
					((com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact.ArtifactBuff) b).charge(hero, 0.2f*hero.pointsInTalent(Talent.BIO_ENERGY));
				}
			}
		}
		if (hero.hasTalent(Talent.NATURE_FRIENDLY)) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin.conditionallyAppend(hero, 1+2*hero.pointsInTalent(Talent.NATURE_FRIENDLY), 3);
		}
	}

	//Level.updateFieldOfView, the Samurai's Dragon's Eye and Inner Eye, the Gunner's Tactical Sight
	public static int mindVisionRange( Hero h, int range ){
		if (h.hasTalent(Talent.DRAGONS_EYE) && h.heroClass != HeroClass.SAMURAI){
			range = Math.max(range, 1+h.pointsInTalent(Talent.DRAGONS_EYE));
		}
		if (h.hasTalent(Talent.DRAGONS_EYE) && h.buff(com.shatteredpixel.shatteredpixeldungeon.items.Sheath.Sheathing.class) != null) {
			range += 2+h.pointsInTalent(Talent.DRAGONS_EYE);
		}
		if (h.hasTalent(Talent.TACTICAL_SIGHT) && h.buff(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.gunner.ReinforcedArmor.ReinforcedArmorTracker.class) != null) {
			range += 1+h.pointsInTalent(Talent.TACTICAL_SIGHT);
		}
		if (h.hasTalent(Talent.INNER_EYE) && h.buff(com.shatteredpixel.shatteredpixeldungeon.items.Sheath.DashAttackVision.class) != null) {
			range += 3*h.pointsInTalent(Talent.INNER_EYE);
		}
		return range;
	}

	//the Crusader's judgement executes a marked enemy that is low on health
	public static void judgement( Char attacker, Char enemy ){
		Talent.JudgementTracker judgement = enemy.buff(Talent.JudgementTracker.class);
		if (judgement == null) return;
		if (enemy.isAlive() && enemy.alignment != attacker.alignment && !Char.hasProp(enemy, Char.Property.BOSS)
				&& !Char.hasProp(enemy, Char.Property.MINIBOSS) && attacker instanceof Hero &&
				(enemy.HP/(float)enemy.HT) <= 0.4f*((Hero)attacker).pointsInTalent(Talent.GODS_JUDGEMENT)/3f) {
			enemy.HP = 0;
			if (!enemy.isAlive()) {
				enemy.die(attacker);
			} else {
				enemy.damage(-1, attacker);
				DeathMark.processFearTheReaper(enemy);
			}
			if (enemy.sprite != null) {
				enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Talent.JudgementTracker.class, "executed"));
				ThunderImbue.thunderEffect(enemy.sprite);
			}
		}
		judgement.detach();
	}
}
