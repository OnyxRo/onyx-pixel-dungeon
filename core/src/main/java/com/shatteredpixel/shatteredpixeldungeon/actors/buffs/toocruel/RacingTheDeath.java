/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.Collections;

//Racing The Death, a trail of shadows follows the hero and hurts if the hero lingers
public class RacingTheDeath extends Buff implements Hero.Doom {

	{
		type = buffType.NEUTRAL;
	}

	private static final int TRAIL_LENGTH = 10;

	private ArrayList<Integer> trailCells = new ArrayList<>();
	private ArrayList<Image> trail = null;
	private int lastDepth = -1;
	private boolean fx = false;

	public RacingTheDeath() {
		resetCells();
	}

	@Override
	public void detach() {
		//this buff cannot be removed
	}

	@Override
	public boolean attachTo( Char target ) {
		return target instanceof Hero && super.attachTo( target );
	}

	private void resetCells() {
		trailCells = new ArrayList<>();
		for (int i = 0; i < TRAIL_LENGTH; i++) trailCells.add( 0 );
	}

	//called whenever the hero spends time
	public void tick() {
		if (Dungeon.depth != lastDepth) {
			resetCells();
			eraseTrail();
			lastDepth = Dungeon.depth;
			createTrail();
		}
		int damage = (Statistics.deepestFloor + 1) * 3;
		trailCells.remove( TRAIL_LENGTH - 1 );
		int idx = trailCells.indexOf( target.pos );
		if (idx > 0) {
			if (trailCells.get( 3 ) == target.pos) {
				damage *= 4;
				burst( target.pos, 30 );
			} else {
				damage *= 2;
				burst( target.pos, 15 );
			}
		} else {
			int frequency = Collections.frequency( trailCells, target.pos );
			damage = (frequency - 3) * damage * 3 / 2;
			if (damage > 0) burst( target.pos, 7 * (frequency - 3) );
		}
		trailCells.add( 0, target.pos );
		addTrailSegment( target.pos );
		updateTrail();
		if (damage > 0) target.damage( damage, this );
	}

	@Override
	public void fx( boolean on ) {
		fx = on;
		if (on) createTrail();
		else eraseTrail();
	}

	private boolean canDraw() {
		return fx && GameScene.scene != null && Dungeon.level != null && Dungeon.hero != null;
	}

	private void burst( int pos, int amount ) {
		if (trail == null) return;
		int i = trailCells.indexOf( pos );
		if (i < 0 || i >= trail.size()) return;
		Image sprite = trail.get( i );
		Emitter emitter = GameScene.emitter();
		if (emitter == null) return;
		emitter.pos( new PointF( sprite.x + sprite.width / 2, sprite.y + sprite.height / 2 ) );
		emitter.burst( PurpleParticle.BURST, amount );
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
	}

	private void updateTrail() {
		if (trail == null) return;
		for (int i = 0; i < TRAIL_LENGTH && i < trail.size(); i++) {
			Image segment = trail.get( i );
			int pos = trailCells.get( i );
			segment.invert();
			segment.alpha( i == 4 ? 0.9f : 0.3f );
			if (Actor.findChar( pos ) != null) segment.alpha( segment.alpha() / 2 );
			segment.visible = pos != 0 && pos != Dungeon.hero.pos && pos < Dungeon.level.length() && Dungeon.level.visited[pos];
		}
	}

	private void createTrail() {
		if (!canDraw()) return;
		if (trail != null) eraseTrail();
		trail = new ArrayList<>();
		for (int i = 0; i < TRAIL_LENGTH; i++) {
			trail.add( segmentAt( trailCells.get( i ) ) );
		}
		updateTrail();
	}

	private Image segmentAt( int pos ) {
		Image segment = HeroSprite.avatar( Dungeon.hero.heroClass, Dungeon.hero.tier() );
		segment.point( worldToCamera( pos, segment ) );
		GameScene.effect( segment );
		return segment;
	}

	private void addTrailSegment( int pos ) {
		if (trail == null || !canDraw()) return;
		trail.remove( TRAIL_LENGTH - 1 ).killAndErase();
		trail.add( 0, segmentAt( pos ) );
	}

	private void eraseTrail() {
		if (trail == null) return;
		for (Image img : trail) img.killAndErase();
		trail = null;
	}

	private PointF worldToCamera( int cell, Image segment ) {
		int w = Dungeon.level.width();
		float csize = DungeonTilemap.SIZE;
		return new PointF(
				PixelScene.align( Camera.main, ((cell % w) + 0.5f) * csize - segment.width * 0.5f ),
				PixelScene.align( Camera.main, ((cell / w) + 1.0f) * csize - segment.height - csize * (6 / 16f) ) );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		int[] ret = new int[TRAIL_LENGTH];
		for (int i = 0; i < TRAIL_LENGTH; i++) ret[i] = trailCells.get( i );
		bundle.put( "trail_cells", ret );
		bundle.put( "last_depth", lastDepth );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		int[] cells = bundle.getIntArray( "trail_cells" );
		trailCells = new ArrayList<>();
		for (int i = 0; i < TRAIL_LENGTH; i++) {
			trailCells.add( cells != null && i < cells.length ? cells[i] : 0 );
		}
		lastDepth = bundle.getInt( "last_depth" );
	}

	@Override
	public void onDeath() {
		Dungeon.fail( getClass() );
		GLog.n( Messages.get( this, "ondeath" ) );
	}
}
