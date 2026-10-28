/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChaosTime;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.FinalLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CthulhuArt;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CthulhuSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Abyssalnite's final boss. Abyssalnite left him as a rat with 999999 health, the fight here follows Onyx's animation page.
//He sleeps in the pit until every septium pillar is broken. Phase 1 has the tentacle strike and the Void Grasp,
//the scream starts phase 2 and adds the mind blast, the third eye opens phase 3 and adds the eye beam.
//All through the fight he crushes the first debuff on himself now and then.
public class Cthulhu extends Mob {

	{
		spriteClass = CthulhuSprite.class;

		HP = HT = MAX_HP;
		defenseSkill = 30;
		EXP = 0;
		maxLvl = -999;

		flying = true;
		state = PASSIVE;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
		properties.add(Property.BOSS);
		properties.add(Property.LARGE);
	}

	private static final int MAX_HP = 1200;

	private boolean awake = false;
	private int phase = 1;
	//turns left in a phase change, he cannot be hurt and does nothing
	private int busy = 0;

	private int strikeCD = 2, graspCD = 5, mindCD = 4, beamCD = 2, cleanseCD = 4;
	//centres of the tentacle strikes shown with targets last turn
	private int[] strikes = new int[0];
	private int beamTarget = -1;

	public int phase(){
		return phase;
	}

	public boolean awake(){
		return awake;
	}

	public void wake(){
		if (awake) return;
		awake = true;
		state = HUNTING;
		BossHealthBar.assignBoss( this );
		yell( Messages.get( this, "notice" ) );
		Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
		if (sprite != null) Camera.main.shake( 2, 0.5f );
	}

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		Hero hero = Dungeon.hero;
		if (!awake || hero == null || !hero.isAlive() || paralysed > 0){
			spend( TICK );
			return true;
		}
		if (busy > 0){
			busy--;
			spend( TICK );
			return true;
		}

		strikeCD--; graspCD--; mindCD--; beamCD--; cleanseCD--;

		if (strikes.length > 0){
			for (int c : strikes) strike( c );
			strikes = new int[0];
		} else if (beamTarget >= 0){
			fireBeam();
		} else if (cleanseCD <= 0 && firstDebuff() != null){
			cleanse();
			cleanseCD = 10;
		} else if (phase >= 3 && beamCD <= 0){
			chargeBeam( hero.pos );
			beamCD = 6;
		} else if (phase >= 2 && mindCD <= 0){
			mindBlast();
			mindCD = phase >= 3 ? 9 : 11;
		} else if (graspCD <= 0 && hero.buff( Roots.class ) == null){
			grasp();
			graspCD = phase >= 2 ? 10 : 13;
		} else if (strikeCD <= 0){
			markStrikes( hero.pos );
			strikeCD = phase == 1 ? 3 : 2;
		}

		spend( TICK );
		return true;
	}

	//The tentacle strike. Red targets on the 3x3 round the hero, next turn a tentacle bursts up there and sinks, leaving a void hole
	private void markStrikes( int around ){
		ArrayList<Integer> centres = new ArrayList<>();
		centres.add( around );
		if (phase >= 3){
			ArrayList<Integer> near = new ArrayList<>();
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (Dungeon.level.distance( i, around ) >= 2 && Dungeon.level.distance( i, around ) <= 3 && Dungeon.level.passable[i]) near.add( i );
			}
			if (!near.isEmpty()) centres.add( Random.element( near ) );
		}
		strikes = new int[centres.size()];
		for (int k = 0; k < strikes.length; k++){
			strikes[k] = centres.get( k );
			for (int n : PathFinder.NEIGHBOURS9){
				int c = strikes[k] + n;
				if (!Dungeon.level.insideMap( c ) || Dungeon.level.solid[c]) continue;
				if (sprite != null && sprite.parent != null){
					TargetedCell t = new TargetedCell();
					t.reset( c, Actor.now() + 1 );
					sprite.parent.add( t );
				}
			}
		}
		if (Dungeon.level.heroFOV[around] || around == Dungeon.hero.pos) GLog.w( Messages.get( this, "rumble" ) );
	}

	private void strike( final int centre ){
		for (int n : PathFinder.NEIGHBOURS9){
			int c = centre + n;
			if (!Dungeon.level.insideMap( c )) continue;
			Char ch = Actor.findChar( c );
			if (ch != null && ch != this && ch.isAlive()){
				ch.damage( Random.NormalIntRange( 18, 28 ), this );
				if (ch == Dungeon.hero && !ch.isAlive()){
					Dungeon.fail( this );
					GLog.n( Messages.get( this, "kill" ) );
				}
			}
		}
		final FinalLevel level = Dungeon.level instanceof FinalLevel ? (FinalLevel) Dungeon.level : null;
		final boolean hole = level != null && level.addVoid( centre );
		if (sprite != null && sprite.parent != null){
			int w = Dungeon.level.width();
			CthulhuSprite.Fx fx = new CthulhuSprite.Fx( "strike", CthulhuArt.STRIKE_W, CthulhuArt.STRIKE_H, CthulhuArt.STRIKE,
					(centre % w) * 16 + 8 + CthulhuArt.STRIKE_DX, (centre / w) * 16 + 14 + CthulhuArt.STRIKE_DY );
			if (hole) fx.onEnd( new Callback() {
				@Override
				public void call() {
					level.showVoid( centre );
				}
			});
			GameScene.effect( fx );
			Camera.main.shake( 2, 0.3f );
			Sample.INSTANCE.play( Assets.Sounds.ROCKS );
		}
	}

	//A tentacle comes up under the hero and holds them in place
	private void grasp(){
		final Hero hero = Dungeon.hero;
		final int cell = hero.pos;
		hero.damage( Random.NormalIntRange( 6, 10 ), this );
		if (!hero.isAlive()){
			Dungeon.fail( this );
			GLog.n( Messages.get( this, "kill" ) );
			return;
		}
		Buff.prolong( hero, Roots.class, 3f );
		GLog.w( Messages.get( this, "grasp" ) );
		if (hero.sprite != null && hero.sprite.parent != null){
			int w = Dungeon.level.width();
			float x = (cell % w) * 16 + 8 + CthulhuArt.GRASP_DX, y = (cell / w) * 16 + 9 + CthulhuArt.GRASP_DY;
			CthulhuSprite.Fx.Hold held = new CthulhuSprite.Fx.Hold() {
				@Override
				public boolean active() {
					return hero.isAlive() && hero.pos == cell && hero.buff( Roots.class ) != null;
				}
			};
			hero.sprite.parent.addToBack( new CthulhuSprite.Fx( "grasp_back", CthulhuArt.GRASP_W, CthulhuArt.GRASP_H, CthulhuArt.GRASP_BACK, x, y ).hold( 20, held ) );
			GameScene.effect( new CthulhuSprite.Fx( "grasp_front", CthulhuArt.GRASP_W, CthulhuArt.GRASP_H, CthulhuArt.GRASP_FRONT, x, y ).hold( 20, held ) );
		}
	}

	//Purple rings from his head, the hero's mind is thrown into chaos
	private void mindBlast(){
		if (sprite instanceof CthulhuSprite){
			((CthulhuSprite) sprite).mindBlast();
			WandOfBlastWave.BlastWave.blast( pos, 8, 0xBA6EF0 );
			GameScene.flash( 0x1E0B30 );
		}
		Hero hero = Dungeon.hero;
		hero.damage( Random.NormalIntRange( 6, 12 ), this );
		if (!hero.isAlive()){
			Dungeon.fail( this );
			GLog.n( Messages.get( this, "kill" ) );
			return;
		}
		Buff.affect( hero, ChaosTime.class ).extend( 5f );
		GLog.w( Messages.get( this, "mind" ) );
	}

	//The third eye charges on the hero's cell, next turn it fires a beam along that line
	private void chargeBeam( int target ){
		beamTarget = target;
		Ballistica b = new Ballistica( pos, target, Ballistica.STOP_SOLID );
		if (sprite != null && sprite.parent != null){
			for (int c : b.subPath( 1, b.dist )){
				TargetedCell t = new TargetedCell();
				t.reset( c, Actor.now() + 1 );
				t.hardlight( 0xAA44FF );
				sprite.parent.add( t );
			}
			if (sprite instanceof CthulhuSprite) ((CthulhuSprite) sprite).chargeBeam();
		}
		GLog.w( Messages.get( this, "charge" ) );
	}

	private void fireBeam(){
		Ballistica b = new Ballistica( pos, beamTarget, Ballistica.STOP_SOLID );
		beamTarget = -1;
		for (int c : b.subPath( 1, b.dist )){
			Char ch = Actor.findChar( c );
			if (ch != null && ch != this && ch.isAlive()){
				ch.damage( Random.NormalIntRange( 25, 40 ), this );
				if (ch == Dungeon.hero && !ch.isAlive()){
					Dungeon.fail( this );
					GLog.n( Messages.get( this, "kill" ) );
				}
			}
		}
		if (sprite != null && sprite.parent != null){
			Beam ray = new Beam.DeathRay( CthulhuSprite.eye(), DungeonTilemap.raisedTileCenterToWorld( b.collisionPos ) );
			ray.hardlight( 0xAA44FF );
			sprite.parent.add( ray );
			if (sprite instanceof CthulhuSprite) ((CthulhuSprite) sprite).fireBeam();
			Camera.main.shake( 2, 0.3f );
			Sample.INSTANCE.play( Assets.Sounds.RAY );
		}
	}

	//the first debuff in the order the boss bar shows them
	private Buff firstDebuff(){
		for (Buff b : buffs()){
			if (b.type == Buff.buffType.NEGATIVE && b.icon() != BuffIndicator.NONE && !(b instanceof LockedFloor)) return b;
		}
		return null;
	}

	//A tentacle comes down over his health bar and crushes the first debuff
	private void cleanse(){
		final Buff b = firstDebuff();
		GLog.w( Messages.get( this, "cleanse", b.toString() ) );
		BossHealthBar.cleanse( new Callback() {
			@Override
			public void call() {
				if (b.target != null) b.detach();
			}
		});
	}

	@Override
	public boolean isInvulnerable( Class effect ) {
		return !awake || busy > 0 || super.isInvulnerable( effect );
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (!awake || busy > 0) return;
		int before = HP;
		super.damage( dmg, src );

		LockedFloor lock = Dungeon.hero.buff( LockedFloor.class );
		if (lock != null) lock.addTime( before - HP );

		if (phase == 1 && HP <= 2 * HT / 3){
			HP = Math.max( HP, 2 * HT / 3 );
			phase = 2;
			busy = 3;
			strikes = new int[0];
			yell( Messages.get( this, "scream" ) );
			if (sprite instanceof CthulhuSprite){
				((CthulhuSprite) sprite).scream();
				Camera.main.shake( 3, 2f );
				WandOfBlastWave.BlastWave.blast( pos, 10, 0xB0A0C6 );
				Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
			}
		} else if (phase == 2 && HP <= HT / 3){
			HP = Math.max( HP, HT / 3 );
			phase = 3;
			busy = 4;
			strikes = new int[0];
			yell( Messages.get( this, "eye" ) );
			if (sprite instanceof CthulhuSprite){
				((CthulhuSprite) sprite).phase( 3 );
				((CthulhuSprite) sprite).openEye();
				Camera.main.shake( 1, 2f );
			}
		}
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );
		GameScene.bossSlain();
		yell( Messages.get( this, "defeated" ) );
		if (Dungeon.level instanceof FinalLevel) ((FinalLevel) Dungeon.level).cthulhuDefeated();
	}

	@Override
	public void notice() {
		//only the broken pillars wake him
	}

	@Override
	public boolean reset() {
		return true;
	}

	private static final String AWAKE = "awake";
	private static final String PHASE = "phase";
	private static final String BUSY = "busy";
	private static final String COOLDOWNS = "cooldowns";
	private static final String STRIKES = "strikes";
	private static final String BEAM = "beam_target";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( AWAKE, awake );
		bundle.put( PHASE, phase );
		bundle.put( BUSY, busy );
		bundle.put( COOLDOWNS, new int[]{ strikeCD, graspCD, mindCD, beamCD, cleanseCD } );
		bundle.put( STRIKES, strikes );
		bundle.put( BEAM, beamTarget );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		//saves from before the fight was finished had none of this, and 999999 health
		if (!bundle.contains( PHASE )){
			HP = HT = MAX_HP;
			return;
		}
		awake = bundle.getBoolean( AWAKE );
		phase = bundle.getInt( PHASE );
		busy = bundle.getInt( BUSY );
		int[] cd = bundle.getIntArray( COOLDOWNS );
		if (cd != null && cd.length == 5){
			strikeCD = cd[0]; graspCD = cd[1]; mindCD = cd[2]; beamCD = cd[3]; cleanseCD = cd[4];
		}
		strikes = bundle.getIntArray( STRIKES );
		if (strikes == null) strikes = new int[0];
		beamTarget = bundle.getInt( BEAM );
		if (awake) BossHealthBar.assignBoss( this );
	}
}
