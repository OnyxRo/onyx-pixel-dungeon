/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.toocruel.ExterminationItemLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

//Extermination, the floor stays sealed until every monster that carries this is dead
public class Exterminating extends Buff implements TcBuff.PersistNesting {

	private Indicator sprite;

	@Override
	public void fx( boolean on ) {
		if (on) {
			if (target.sprite == null || GameScene.scene == null) return;
			if (sprite != null) sprite.killAndErase();
			sprite = new Indicator( target.sprite );
			sprite.hardlight( 0.5f, 1f, 2f );
			GameScene.effect( sprite );
		} else if (sprite != null) {
			sprite.killAndErase();
			sprite = null;
		}
	}

	@Override
	public void detach() {
		super.detach();
		if (Dungeon.level != null) processExtermination( Dungeon.level );
	}

	@Override
	public void applyNestingEffect( Char target ) {
		Buff.affect( target, Exterminating.class );
	}

	//a monster still to be slain shows through walls for a moment when the hero tries to leave
	public static class Reveal extends FlavourBuff implements TcBuff.Revealed {
		public static final float DURATION = TICK * 2f;

		@Override
		public void detach() {
			TcTrackers.FullSceneUpdater.requestFog();
			super.detach();
		}
	}

	public static void processExtermination( Level level ) {
		for (Mob mob : level.mobs) {
			if (mob.isAlive() && mob.buff( Exterminating.class ) != null) return;
		}
		ExterminationItemLock lock = (ExterminationItemLock) level.blobs.get( ExterminationItemLock.class );
		if (lock != null && lock.unlockAll( level )) {
			GLog.p( Messages.get( TooCruel.class, "extermination_complete" ) );
			Sample.INSTANCE.play( Assets.Sounds.LEVELUP, 0.5f, 1.5f );
		}
	}

	public static boolean exterminationDone( Level level ) {
		if (!TooCruel.on( TooCruel.EXTERMINATION )) return true;
		int n = 0;
		for (Mob mob : level.mobs) {
			if (mob.isAlive() && mob.buff( Exterminating.class ) != null) {
				n++;
				Buff.affect( mob, Reveal.class, Reveal.DURATION );
			}
		}
		if (n > 0) {
			if (n > 1) GLog.w( Messages.get( TooCruel.class, "extermination_lock", n ) );
			else GLog.w( Messages.get( TooCruel.class, "extermination_lock_last" ) );
			if (GameScene.scene != null) {
				Dungeon.observe();
				GameScene.updateFog();
				Dungeon.hero.checkVisibleMobs();
			}
			return false;
		}
		return true;
	}

	//a target mark that circles the monster
	public static class Indicator extends Image {
		private final CharSprite target;
		private float tMove = Random.Float( 2 * (float) Math.PI );
		private float tScale = Random.Float( 2 * (float) Math.PI );
		private final int xFactor = Random.Int( 1, 7 );
		private final int yFactor;
		private final float tDirection = Random.Int( 2 ) == 0 ? -1 : 1;
		private final float angular;

		public Indicator( CharSprite target ) {
			super();
			this.target = target;
			copy( Icons.get( Icons.TARGET ) );
			originToCenter();
			int n;
			do { n = Random.Int( 1, 7 ); } while (n == xFactor);
			yFactor = n;
			float a = Random.Float( -0.5f, 0.5f );
			angular = a + Math.signum( a ) * 0.75f;
		}

		@Override
		public void update() {
			super.update();
			angle += Game.elapsed * 180f * angular;
			tMove = (tMove + Game.elapsed * tDirection) % (2 * (float) Math.PI);
			tScale = (tScale + Game.elapsed * (float) Math.sqrt( 2 ) * tDirection) % (2 * (float) Math.PI);
			scale.set( (float) Math.cos( tScale ) / 8 + 0.375f );
			visible = target.visible;
			if (visible) {
				PointF p = target.center();
				x = p.x + (float) Math.cos( xFactor * tMove ) * (target.width() - width()) / 2 - width / 2f;
				y = p.y + (float) Math.sin( yFactor * tMove ) * (target.height() - height()) / 2 - height / 2f;
			}
		}
	}
}
