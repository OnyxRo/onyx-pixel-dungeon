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
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
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
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Abyssalnite's final boss. Abyssalnite left him as a rat with 999999 health, the fight here follows Onyx's animation page.
//He sleeps in the pit until every septium pillar is broken, calling monsters and slamming meanwhile. He picks his targets
//the way every monster does (chooseEnemy), and nothing of his touches an invisible char. Phase 1 has the slam,
//the scream starts phase 2 and adds the mind blast and Void Grasp, the third eye opens phase 3 and adds the eye beam.
//All through the fight he crushes the first debuff on himself now and then. Beating him opens the way to the Yendor hall.
public class Cthulhu extends Mob {

	{
		spriteClass = CthulhuSprite.class;

		HP = HT = MAX_HP;
		defenseSkill = 30;
		EXP = 0;
		maxLvl = -999;
		//he watches the whole arena
		viewDistance = 24;

		flying = true;
		state = PASSIVE;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
		properties.add(Property.BOSS);
		properties.add(Property.LARGE);
	}

	private static final int MAX_HP = 1200;
	//the frame of the strike and the grasp where the tentacle is up, they play at the page's speed
	private static final int STRIKE_RISE = 9, GRASP_RISE = 9, FX_FPS = 12;
	//Onyx turned the tentacle popping out of the floor off for now (2026-10-28), the strike lands at once and leaves its crater
	private static final boolean STRIKE_ANIMATION = false;

	private boolean awake = false;
	private int phase = 1;
	//turns left in a phase change, he cannot be hurt and does nothing
	private int busy = 0;

	private int strikeCD = 2, graspCD = 5, mindCD = 4, beamCD = 2, cleanseCD = 4, slamCD = 1;
	//centres of the tentacle strikes shown with targets last turn
	private int[] strikes = new int[0];
	private int beamTarget = -1;
	//while the pillars stand he calls a monster every few turns, up to a limit
	private static final int SPAWN_EVERY = 6, SPAWN_MAX = 6;
	private int spawnCD = 3;
	private int slamPending = -1;

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
		Music.INSTANCE.play( Assets.Music.HALLS_BOSS, true );
		if (sprite != null){
			Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
			Camera.main.shake( 2, 0.5f );
		}
	}

	private boolean onScreen(){
		return sprite != null && sprite.parent != null;
	}

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		//asleep he only stirs once the arena is sealed
		if (paralysed > 0 || (!awake && !Dungeon.level.locked)){
			spend( TICK );
			return true;
		}
		if (busy > 0){
			busy--;
			spend( TICK );
			return true;
		}

		strikeCD--; graspCD--; mindCD--; beamCD--; cleanseCD--; slamCD--;

		//attacks already marked land even if their target has hidden since
		if (strikes.length > 0) return resolveStrikes();
		if (slamPending >= 0) return resolveSlam();
		if (beamTarget >= 0){
			fireBeam();
			spend( TICK );
			return true;
		}

		//While the pillars stand he cannot be hurt, but he calls monsters of the depth into the arena and slams
		//anyone beside his outer tentacles
		if (!awake){
			spawnCD--;
			if (spawnCD <= 0 && summon()) spawnCD = SPAWN_EVERY;
			Char h = Dungeon.hero;
			if (seen( h )){
				enemy = h;
				int slam = slamFor( h.pos );
				if (slam >= 0 && slamCD <= 0){
					markSlam( slam );
					slamCD = 5;
				}
			}
			spend( TICK );
			return true;
		}

		if (cleanseCD <= 0 && firstDebuff() != null){
			cleanse();
			cleanseCD = 10;
			spend( TICK );
			return true;
		}

		//the usual monster rules, an invisible hero is not seen and allies can be picked
		//chooseEnemy keeps an enemy that has turned invisible, so like Mob.act he only attacks one he can see
		enemy = chooseEnemy();
		enemySeen = seen( enemy );
		if (!enemySeen){
			spend( TICK );
			return true;
		}

		int slam = slamFor( enemy.pos );
		if (phase >= 3 && beamCD <= 0){
			chargeBeam( enemy.pos );
			beamCD = 6;
		} else if (phase >= 2 && mindCD <= 0){
			mindBlast();
			mindCD = phase >= 3 ? 9 : 11;
		} else if (slam >= 0 && slamCD <= 0){
			markSlam( slam );
			slamCD = 4;
		} else if (phase >= 2 && graspCD <= 0 && enemy.buff( Roots.class ) == null && !enemy.flying){
			return grasp( enemy );
		}

		spend( TICK );
		return true;
	}

	private void target( int c, int color ){
		if (!onScreen()) return;
		TargetedCell t = new TargetedCell();
		t.reset( c, Actor.now() + 1 );
		if (color != -1) t.hardlight( color );
		sprite.parent.add( t );
	}

	//the usual monster rule, what he cannot see he cannot hit
	private boolean seen( Char ch ){
		return ch != null && ch.isAlive() && fieldOfView != null && fieldOfView[ch.pos] && ch.invisible <= 0;
	}

	private void hurt( Char ch, int dmg ){
		if (ch == null || ch == this || !ch.isAlive() || ch.invisible > 0) return;
		ch.damage( dmg, this );
		if (ch == Dungeon.hero && !ch.isAlive()){
			Dungeon.fail( this );
			GLog.n( Messages.get( this, "kill" ) );
		}
	}

	//sends a char flying away from a point, or a random way when it stands on it
	//returns true when the char was thrown
	private boolean knockBack( Char ch, int from, int power ){
		if (ch == null || !ch.isAlive() || ch.invisible > 0 || ch.properties().contains( Property.IMMOVABLE )) return false;
		int dir;
		if (ch.pos == from){
			dir = PathFinder.NEIGHBOURS8[Random.Int( 8 )];
		} else {
			int w = Dungeon.level.width();
			int dx = Integer.signum( ch.pos % w - from % w ), dy = Integer.signum( ch.pos / w - from / w );
			dir = dx + dy * w;
		}
		Ballistica trajectory = new Ballistica( ch.pos, ch.pos + dir, Ballistica.MAGIC_BOLT );
		if (trajectory.dist <= 0) return false;
		WandOfBlastWave.throwChar( ch, trajectory, power );
		return true;
	}

	//The tentacle strike, taken out of the fight by Onyx (2026-10-28). Only strikes marked in an older save still land.
	//Red targets on the 3x3 round each centre, next turn a tentacle bursts up there, throws everyone
	//in reach and sinks, leaving a hole of void you can fall into
	//a broken pillar makes him thrash, two monsters come at once and his next slam is ready
	public void pillarBroken(){
		if (awake || Dungeon.hero == null || !Dungeon.hero.isAlive()) return;
		slamCD = 0;
		summon();
		summon();
		spawnCD = SPAWN_EVERY;
		yell( Messages.get( this, "stir" ) );
		if (onScreen()) Camera.main.shake( 3, 0.6f );
	}

	private boolean resolveStrikes(){
		final int[] cells = strikes;
		strikes = new int[0];
		spend( TICK );
		if (!onScreen() || !STRIKE_ANIMATION){
			for (int c : cells) strikeHits( c );
			if (onScreen()) Sample.INSTANCE.play( Assets.Sounds.ROCKS );
			return true;
		}
		int w = Dungeon.level.width();
		for (int k = 0; k < cells.length; k++){
			final int c = cells[k];
			final boolean last = k == 0;
			GameScene.effect( new CthulhuSprite.Fx( "strike", CthulhuArt.STRIKE_W, CthulhuArt.STRIKE_H, CthulhuArt.STRIKE,
					(c % w) * 16 + 8 + CthulhuArt.STRIKE_DX, (c / w) * 16 + 14 + CthulhuArt.STRIKE_DY ).fps( FX_FPS )
					.at( STRIKE_RISE, new Callback() {
						@Override
						public void call() {
							strikeHits( c );
							if (last) next();
						}
					}) );
		}
		Sample.INSTANCE.play( Assets.Sounds.ROCKS );
		return false;
	}

	private void strikeHits( int centre ){
		ArrayList<Char> hit = new ArrayList<>();
		for (int n : PathFinder.NEIGHBOURS9){
			int c = centre + n;
			if (!Dungeon.level.insideMap( c )) continue;
			Char ch = Actor.findChar( c );
			if (ch != null && ch != this) hit.add( ch );
		}
		boolean thrownOff = false;
		for (Char ch : hit){
			hurt( ch, Random.NormalIntRange( 18, 28 ) );
			boolean thrown = knockBack( ch, centre, 3 );
			if (ch.pos == centre && thrown) thrownOff = true;
		}
		//whoever stood on the centre falls in, unless the tentacle threw them clear
		if (Dungeon.level instanceof FinalLevel) ((FinalLevel) Dungeon.level).addVoid( centre, !thrownOff );
		if (onScreen()) Camera.main.shake( 3, 0.4f );
	}

	//Calls a monster of the depth up out of the dark, a few cells from the hero and hunting them,
	//the way the Dwarf King and Yog call theirs. False when the arena is full or no cell is free
	private boolean summon(){
		Char h = Dungeon.hero;
		if (h == null || !h.isAlive()) return false;
		int count = 0;
		for (Mob m : Dungeon.level.mobs){
			if (m != this && m.alignment == Alignment.ENEMY && !(m instanceof SeptiumPillar)) count++;
		}
		if (count >= SPAWN_MAX) return false;
		ArrayList<Integer> spots = new ArrayList<>();
		for (int c = 0; c < Dungeon.level.length(); c++){
			int d = Dungeon.level.distance( c, h.pos );
			if (d >= 3 && d <= 7 && Dungeon.level.passable[c] && !Dungeon.level.solid[c]
					&& !Dungeon.level.avoid[c] && !Dungeon.level.pit[c] && Actor.findChar( c ) == null) spots.add( c );
		}
		if (spots.isEmpty()) return false;
		Mob m = Dungeon.level.createMob();
		if (m == null) return false;
		m.pos = Random.element( spots );
		m.state = m.HUNTING;
		GameScene.add( m );
		if (onScreen()) ScrollOfTeleportation.appear( m, m.pos );
		return true;
	}

	//The slam. One of the four outer floor tentacles whips flat along the floor beside it
	private static ArrayList<Integer> slamCells( int k ){
		ArrayList<Integer> cells = new ArrayList<>();
		int[] s = CthulhuArt.SLAMS[k];
		int w = Dungeon.level.width();
		int x0 = Math.min( s[0], s[4] ) / 16, x1 = Math.max( s[0], s[4] ) / 16;
		if (s[2] < 0) x0--; else x1++;
		int y0 = (s[5] - 4) / 16, y1 = s[1] / 16;
		for (int y = y0; y <= y1; y++) for (int x = x0; x <= x1; x++){
			int c = x + y * w;
			if (Dungeon.level.insideMap( c ) && !Dungeon.level.solid[c] && Dungeon.level.passable[c]) cells.add( c );
		}
		return cells;
	}

	private int slamFor( int cell ){
		for (int k = 0; k < CthulhuArt.SLAMS.length; k++){
			for (int c : slamCells( k )) if (Dungeon.level.distance( c, cell ) <= 1) return k;
		}
		return -1;
	}

	private void markSlam( int k ){
		slamPending = k;
		for (int c : slamCells( k )) target( c, -1 );
		GLog.w( Messages.get( this, "slam" ) );
	}

	private boolean resolveSlam(){
		final int k = slamPending;
		slamPending = -1;
		spend( TICK );
		if (!(sprite instanceof CthulhuSprite) || !onScreen()){
			slamHits( k );
			return true;
		}
		((CthulhuSprite) sprite).slam( k, new Callback() {
			@Override
			public void call() {
				slamHits( k );
				next();
			}
		});
		return false;
	}

	private void slamHits( int k ){
		int w = Dungeon.level.width();
		for (int c : slamCells( k )){
			Char ch = Actor.findChar( c );
			if (ch == null || ch == this) continue;
			hurt( ch, Random.NormalIntRange( 20, 30 ) );
			knockBack( ch, ch.pos - CthulhuArt.SLAMS[k][2], 2 );
		}
		if (onScreen()){
			Camera.main.shake( 3, 0.3f );
			Sample.INSTANCE.play( Assets.Sounds.BLAST );
		}
	}

	//A tentacle comes up under the target and holds it in place
	private boolean grasp( final Char target ){
		graspCD = phase >= 2 ? 10 : 12;
		final int cell = target.pos;
		spend( TICK );
		Callback hold = new Callback() {
			@Override
			public void call() {
				if (!target.isAlive() || target.pos != cell || target.invisible > 0) return;
				hurt( target, Random.NormalIntRange( 6, 10 ) );
				if (target.isAlive()) Buff.prolong( target, Roots.class, 3f );
				if (target == Dungeon.hero) GLog.w( Messages.get( Cthulhu.class, "grasp" ) );
			}
		};
		if (target.sprite == null || target.sprite.parent == null){
			hold.call();
			return true;
		}
		int w = Dungeon.level.width();
		float x = (cell % w) * 16 + 8 + CthulhuArt.GRASP_DX, y = (cell / w) * 16 + 9 + CthulhuArt.GRASP_DY;
		CthulhuSprite.Fx.Hold held = new CthulhuSprite.Fx.Hold() {
			@Override
			public boolean active() {
				return target.isAlive() && target.pos == cell && target.buff( Roots.class ) != null;
			}
		};
		target.sprite.parent.addToBack( new CthulhuSprite.Fx( "grasp_back", CthulhuArt.GRASP_W, CthulhuArt.GRASP_H, CthulhuArt.GRASP_BACK, x, y )
				.fps( FX_FPS ).hold( 20, held ) );
		GameScene.effect( new CthulhuSprite.Fx( "grasp_front", CthulhuArt.GRASP_W, CthulhuArt.GRASP_H, CthulhuArt.GRASP_FRONT, x, y )
				.fps( FX_FPS ).hold( 20, held ).at( GRASP_RISE, new Callback() {
					@Override
					public void call() {
						hold.call();
						next();
					}
				}) );
		return false;
	}

	//Purple rings from his head throw the minds of everything he can see into chaos
	public void mindBlast(){
		if (sprite instanceof CthulhuSprite && onScreen()){
			((CthulhuSprite) sprite).mindBlast();
			ring( 14, 0xBA6EF0, 0.4f );
			ring( 14, 0x7834B8, 0.7f );
			GameScene.flash( 0x1E0B30 );
			Camera.main.shake( 1, 0.6f );
			Sample.INSTANCE.play( Assets.Sounds.CHARMS );
		}
		ArrayList<Char> minds = new ArrayList<>();
		for (Char ch : Actor.chars()){
			if (ch != this && ch.alignment != alignment && seen( ch )) minds.add( ch );
		}
		for (Char ch : minds){
			hurt( ch, Random.NormalIntRange( 6, 12 ) );
			if (!ch.isAlive()) continue;
			if (ch == Dungeon.hero) Buff.affect( ch, ChaosTime.class ).extend( 5f );
			else Buff.prolong( ch, Vertigo.class, 5f );
		}
		if (minds.contains( Dungeon.hero )) GLog.w( Messages.get( this, "mind" ) );
	}

	//rings drawn over the fog, so they show across the dark arena, slower than the blast wave's ripple like the page's rings
	private void ring( float radius, int color, float delay ){
		GameScene.effectOverFog( new Ring( pos, radius, color, delay ) );
	}

	private static class Ring extends Image {
		private static final float LIFE = 1.1f;
		private final float size;
		private float time;

		Ring( int pos, float radius, int color, float delay ){
			super( Effects.get( Effects.Type.RIPPLE ) );
			origin.set( width / 2, height / 2 );
			PointF c = DungeonTilemap.tileCenterToWorld( pos );
			x = c.x - width / 2;
			y = c.y - height / 2;
			hardlight( color );
			size = radius;
			time = -delay;
			scale.set( 0 );
			alpha( 0 );
		}

		@Override
		public void update() {
			super.update();
			time += Game.elapsed;
			if (time < 0) return;
			if (time >= LIFE){
				killAndErase();
				return;
			}
			float p = time / LIFE;
			scale.set( size * (float)Math.sqrt( p ) );
			alpha( 1 - p );
		}
	}

	//The third eye charges on a cell, next turn it fires a beam along that line. It goes through the statues round the pit
	public void chargeBeam( int target ){
		beamTarget = target;
		Ballistica b = new Ballistica( pos, target, Ballistica.STOP_TARGET );
		for (int c : b.subPath( 1, b.dist )) target( c, 0xAA44FF );
		if (sprite instanceof CthulhuSprite && onScreen()) ((CthulhuSprite) sprite).chargeBeam();
		GLog.w( Messages.get( this, "charge" ) );
	}

	public void fireBeam(){
		Ballistica b = new Ballistica( pos, beamTarget, Ballistica.STOP_TARGET );
		beamTarget = -1;
		ArrayList<Char> hit = new ArrayList<>();
		for (int c : b.subPath( 1, b.dist )){
			Char ch = Actor.findChar( c );
			if (ch != null && ch != this) hit.add( ch );
		}
		for (Char ch : hit) hurt( ch, Random.NormalIntRange( 25, 40 ) );
		if (onScreen()){
			Beam ray = new Beam.DeathRay( CthulhuSprite.eye(), DungeonTilemap.raisedTileCenterToWorld( b.collisionPos ) );
			ray.hardlight( 0xC060FF );
			GameScene.effectOverFog( ray );
			if (sprite instanceof CthulhuSprite) ((CthulhuSprite) sprite).fireBeam();
			Camera.main.shake( 2, 0.4f );
			Sample.INSTANCE.play( Assets.Sounds.RAY );
		}
	}

	//the first debuff in the order the boss bar shows them
	private Buff firstDebuff(){
		for (Buff b : buffs()){
			if (b.type == Buff.buffType.NEGATIVE && b.icon() != BuffIndicator.NONE) return b;
		}
		return null;
	}

	//A tentacle comes down over his health bar and crushes the first debuff
	private void cleanse(){
		final Buff b = firstDebuff();
		GLog.w( Messages.get( this, "cleanse", b.name() ) );
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
			slamPending = -1;
			yell( Messages.get( this, "scream" ) );
			if (sprite instanceof CthulhuSprite && onScreen()){
				((CthulhuSprite) sprite).scream();
				Camera.main.shake( 3, 2f );
				ring( 14, 0xB0A0C6, 0.5f );
				ring( 14, 0xB0A0C6, 0.8f );
				ring( 14, 0xB0A0C6, 1.1f );
				ring( 14, 0xB0A0C6, 1.4f );
				Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
			}
		} else if (phase == 2 && HP <= HT / 3){
			HP = Math.max( HP, HT / 3 );
			phase = 3;
			busy = 4;
			strikes = new int[0];
			slamPending = -1;
			yell( Messages.get( this, "eye" ) );
			if (sprite instanceof CthulhuSprite && onScreen()){
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

	private static final String SPAWN = "spawn";
	private static final String AWAKE = "awake";
	private static final String PHASE = "phase";
	private static final String BUSY = "busy";
	private static final String COOLDOWNS = "cooldowns";
	private static final String STRIKES = "strikes";
	private static final String BEAM = "beam_target";
	private static final String SLAM = "slam";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( AWAKE, awake );
		bundle.put( PHASE, phase );
		bundle.put( BUSY, busy );
		bundle.put( COOLDOWNS, new int[]{ strikeCD, graspCD, mindCD, beamCD, cleanseCD, slamCD } );
		bundle.put( STRIKES, strikes );
		bundle.put( BEAM, beamTarget );
		bundle.put( SLAM, slamPending );
		bundle.put( SPAWN, spawnCD );
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
		if (cd != null && cd.length >= 5){
			strikeCD = cd[0]; graspCD = cd[1]; mindCD = cd[2]; beamCD = cd[3]; cleanseCD = cd[4];
			if (cd.length >= 6) slamCD = cd[5];
		}
		strikes = bundle.getIntArray( STRIKES );
		if (strikes == null) strikes = new int[0];
		beamTarget = bundle.getInt( BEAM );
		slamPending = bundle.contains( SLAM ) ? bundle.getInt( SLAM ) : -1;
		if (bundle.contains( SPAWN )) spawnCD = bundle.getInt( SPAWN );
		if (awake) BossHealthBar.assignBoss( this );
	}
}
