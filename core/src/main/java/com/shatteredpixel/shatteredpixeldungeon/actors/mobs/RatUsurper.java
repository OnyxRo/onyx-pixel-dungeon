/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Rat Usurper from Pixel Dungeon Reforged, rebuilt on Shattered's damage and boss flow.
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RatClaw;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatUsurperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RatUsurper extends Mob {

	{
		spriteClass = RatUsurperSprite.class;

		HP = HT = 80;
		defenseSkill = 8;

		EXP = 10;

		properties.add(Property.BOSS);
	}

	public RatUsurper() {
		super();
		if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
			HP = HT = 120;
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 10 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 16;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange( 0, 2 );
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			Dungeon.level.seal();
			yell(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (m instanceof RURat) {
					m.beckon(Dungeon.hero.pos);
				}
			}
		}
	}

	@Override
	public void damage(int dmg, Object src) {
		if (!BossHealthBar.isAssigned()){
			BossHealthBar.assignBoss( this );
			Dungeon.level.seal();
		}
		int lastHP = HP;
		boolean bleeding = (HP*2 <= HT);
		super.damage(dmg, src);
		boolean stronger = Dungeon.isChallenged(Challenges.STRONGER_BOSSES);

		if (lastHP != HP && HP > 0) {
			//only calls rats if it actually took damage
			if (bleeding) {
				int hpBracket = Math.max(1, HT / 8);
				if (lastHP / hpBracket != HP / hpBracket) {
					teleportRat();
				}
				RURat.spawnAround(pos);
				if (stronger) RURat.spawnAround(pos);
			}
			RURat.spawnAround(pos);
			if (stronger) RURat.spawnAround(pos);

			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (m instanceof RURat) {
					m.beckon(pos);
				}
			}
		} else if (!isAlive()) {
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (m instanceof RURat) {
					Dread d = Buff.affect( m, Dread.class );
					d.object = Dungeon.hero.id();
					d.extend(1000);
				}
			}
		}

		if ((HP*2 <= HT) && !bleeding){
			BossHealthBar.bleed(true);
			yell(Messages.get(this, "help"));
		}
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())){
			if (stronger)   lock.addTime(dmg);
			else            lock.addTime(dmg*1.5f);
		}
	}

	//swaps a rat in beside the hero and moves the usurper out the other way
	private void teleportRat() {
		ArrayList<RURat> rats = new ArrayList<>();
		for (Mob m : Dungeon.level.mobs) {
			if (m instanceof RURat) rats.add((RURat) m);
		}
		if (rats.isEmpty() || enemy == null) return;

		RURat toTeleport = Random.element(rats);
		yell(Messages.get(this, "protect_me"));

		int oldPos = pos;
		boolean anyValidPositions = false;
		for (int neighbour : PathFinder.NEIGHBOURS8) {
			if (Actor.findChar(pos+neighbour) == null && Dungeon.level.passable[pos+neighbour]) {
				anyValidPositions = true;
				break;
			}
		}
		if (!anyValidPositions) {
			ScrollOfTeleportation.teleportChar(this);
			ScrollOfTeleportation.appear(toTeleport, oldPos);
			return;
		}

		int bestPos = pos;
		Ballistica trajectory = new Ballistica(enemy.pos, pos, Ballistica.STOP_TARGET);
		int targetCell = trajectory.path.get(trajectory.dist + 1);
		//if the cell opposite the hero is open, go there
		if (Actor.findChar(targetCell) == null && !Dungeon.level.solid[targetCell]) {
			bestPos = targetCell;
		} else {
			//otherwise the open neighbour that is furthest from the hero
			float bestDist = Dungeon.level.trueDistance(pos, enemy.pos);
			for (int i : PathFinder.NEIGHBOURS8) {
				if (Actor.findChar(pos + i) == null
						&& !Dungeon.level.solid[pos + i]
						&& Dungeon.level.trueDistance(pos + i, enemy.pos) > bestDist) {
					bestPos = pos + i;
					bestDist = Dungeon.level.trueDistance(pos + i, enemy.pos);
				}
			}
		}

		Actor.add(new Pushing(this, pos, bestPos));
		pos = bestPos;

		if (oldPos != enemy.pos) ScrollOfTeleportation.appear(toTeleport, oldPos);
	}

	@Override
	public boolean act() {
		boolean toReturn = super.act();

		if (state != SLEEPING && !Dungeon.hero.fieldOfView[this.pos]) {
			Statistics.qualifiedForBossChallengeBadge = false;
		}

		return toReturn;
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		Dungeon.level.unseal();

		GameScene.bossSlain();
		Dungeon.level.drop( new WornKey( Dungeon.depth ), pos ).sprite.drop();
		Dungeon.level.drop( new CrystalKey(Dungeon.depth), pos).sprite.drop();

		//60% chance of 2 claws, 30% chance of 3, 10% chance for 4. Average of 2.5
		int claws = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < claws; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable[pos + ofs]);
			Dungeon.level.drop( new RatClaw(), pos + ofs ).sprite.drop( pos );
		}

		Badges.validateBossSlain();
		if (Statistics.qualifiedForBossChallengeBadge){
			Badges.validateBossChallengeCompleted();
		}
		Statistics.bossScores[0] += 1000;

		yell( Messages.get( this, "defeated" ) );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		if ((HP*2 <= HT)) BossHealthBar.bleed(true);
	}

	public static class RURat extends Rat {
		{
			properties.add(Property.BOSS_MINION);
			state = HUNTING;
		}

		public static void spawnAround(int pos) {
			ArrayList<Integer> offsets = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) offsets.add(n);
			Random.shuffle(offsets);

			for (int n : offsets) {
				int cell = pos + n;
				if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
					RURat r = new RURat();
					r.pos = cell;
					r.state = r.HUNTING;
					GameScene.add( r );
					Dungeon.level.occupyCell(r);
					return;
				}
			}

			//nowhere around works, so spawn it anywhere it can go
			RURat r = new RURat();
			for (int tries = 30; tries > 0; tries--) {
				r.pos = Dungeon.level.randomRespawnCell(r);
				if (r.pos != -1) {
					r.state = r.HUNTING;
					GameScene.add( r );
					Dungeon.level.occupyCell(r);
					return;
				}
			}
		}
	}
}
