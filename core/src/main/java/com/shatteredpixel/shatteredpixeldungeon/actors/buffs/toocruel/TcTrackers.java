/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

//Too Cruel's small buffs that have no text of their own
public class TcTrackers {

	//marks a monster whose modes were applied, so they are applied once
	public static class ModifiersApplied extends Buff {
		{ revivePersists = true; }
	}

	//Bloodbag, damage the hero takes becomes bleeding
	public static class BloodbagBleeding extends Bleeding {
		public void add( float amount ){
			level += amount;
		}
	}

	//Domain of Hell, all grass keeps burning
	public static class GrassIgniter extends Buff {
		@Override
		public boolean act() {
			TooCruel.igniteAllGrass( Dungeon.level );
			spend( TICK );
			return true;
		}
	}

	//Domain of Hell, monsters take less from fire
	public static class FireResist extends Buff {
		{ resistances.add( Burning.class ); }
	}

	//Boxed, a mimic cannot be hurt until the hero sees it
	public static class InvulnerableUntilSeen extends Buff implements TcBuff.Invulnerable {
		@Override
		public boolean act() {
			if (Dungeon.level.heroFOV[target.pos] || Dungeon.level.adjacent( target.pos, Dungeon.hero.pos )) {
				detach();
			}
			spend( TICK );
			return true;
		}

		@Override
		public boolean isInvulnerable( Class<?> effect ) {
			return true;
		}
	}

	//Domain of Hell, standing in fire with the Brimstone glyph explodes
	public static class BrimstoneNeutralizer extends Buff {
		@Override
		public boolean act() {
			Fire fire = (Fire) Dungeon.level.blobs.get( Fire.class );
			Char t = target;
			if (fire != null && fire.cur != null && fire.cur[t.pos] > 0 && t.glyphLevel( Brimstone.class ) >= 0) {
				Item toCurse = null;
				if (t instanceof Hero) toCurse = ((Hero) t).belongings.armor();
				else if (t instanceof ArmoredStatue) toCurse = ((ArmoredStatue) t).armor();
				TooCruel.scorchedEarth( t, toCurse );
				for (int o : PathFinder.NEIGHBOURS9) {
					fire.clear( t.pos + o );
				}
			}
			spend( TICK );
			return true;
		}
	}

	//Let Them Rest, the hero is invisible while resting or asleep
	public static class InvisibleResting extends Buff {
		@Override
		public boolean act() {
			Char t = target;
			boolean sleeping = (t instanceof Hero && ((Hero) t).resting)
					|| (t instanceof Mob && ((Mob) t).state == ((Mob) t).SLEEPING)
					|| t.buff( MagicalSleep.class ) != null
					|| t.buff( Sleep.class ) != null;
			if (sleeping) {
				Buff.prolong( t, Hiding.class, 1f );
			}
			spend( TICK );
			return true;
		}

		public static class Hiding extends Invisibility {
			{
				announced = false;
				actPriority = BUFF_PRIO - 1;
			}

			@Override
			public int icon() {
				return BuffIndicator.NONE;
			}
		}
	}

	//Forced Pacifism, monsters off boss floors take no damage
	public static class ForcedPacifism extends Buff implements TcBuff.DamageAmp {
		@Override
		public boolean attachTo( Char target ) {
			if (target.properties().contains( Char.Property.BOSS )
					|| target.properties().contains( Char.Property.BOSS_MINION )
					|| target.properties().contains( Char.Property.MINIBOSS )) {
				return false;
			}
			return super.attachTo( target );
		}

		@Override
		public boolean act() {
			if (TooCruel.bossOrSpecial() || target.alignment != Char.Alignment.ENEMY) {
				detach();
			}
			spend( TICK );
			return true;
		}

		@Override
		public float damageMultiplier( Object src ) {
			return 0f;
		}
	}

	//Blindness, open spaces blind and then confuse the hero
	public static class PermaBlind extends Buff {
		{ actPriority = HERO_PRIO - 1; }

		private static final int CONFUSE_TIMEOUT = 3;
		private int turnsToConfuse = CONFUSE_TIMEOUT;

		@Override
		public boolean act() {
			boolean weak = true;
			for (int c : PathFinder.NEIGHBOURS8) {
				int cell = target.pos + c;
				if (cell < 0 || cell >= Dungeon.level.length()) continue;
				if (Dungeon.level.solid[cell] || Dungeon.level.pit[cell]) {
					weak = false;
					turnsToConfuse = Math.min( turnsToConfuse + 1, CONFUSE_TIMEOUT );
				}
			}
			if (weak) {
				Buff.prolong( target, Blindness.class, 1f );
				if (--turnsToConfuse <= 0) {
					Buff.prolong( target, Vertigo.class, 1f );
				}
			}
			spend( TICK );
			return true;
		}

		private static final String CONFUSE = "confuse";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( CONFUSE, turnsToConfuse );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			turnsToConfuse = bundle.getInt( CONFUSE );
		}
	}

	//Blessing for the Worthy, a boss kill heals, cleanses and blesses
	public static class BlessingWorthy extends Buff implements TcBuff.OnBossSlain {
		@Override
		public void onBossSlain() {
			if (!(target instanceof Hero)) return;
			Hero hero = (Hero) target;
			PotionOfHealing.heal( hero );
			PotionOfCleansing.cleanse( hero );
			Buff.prolong( hero, Bless.class, 100f );
			if (TooCruel.on( TooCruel.SAFETY_BUFFER )) {
				Buff.affect( hero, SafetyBuffer.class ).refresh().set( SafetyBuffer.SMITE_DAMAGE * (Dungeon.scalingDepth() / 5 + 1) );
			}
		}
	}

	//Uninspired to Learn, monsters give no experience, bosses give three levels
	public static class UninspiredToLearn extends Buff implements TcBuff.XpMultiplier, TcBuff.OnBossSlain {
		@Override
		public float xpMultiplier( Class<?> source ) {
			return source != null && Char.class.isAssignableFrom( source ) ? 0f : 1f;
		}

		@Override
		public void onBossSlain() {
			if (!(target instanceof Hero)) return;
			Hero hero = (Hero) target;
			int exp = Hero.maxExp( hero.lvl ) + Hero.maxExp( hero.lvl + 1 ) + Hero.maxExp( hero.lvl + 2 );
			if (hero.sprite != null) hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString( exp ), com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText.EXPERIENCE );
			hero.earnExp( exp, PotionOfExperience.class );
		}
	}

	//Hydra, each kill brings two more monsters
	public static class Hydra extends Buff {
		private int wantSpawn = 0;

		@Override
		public boolean act() {
			doSpawns();
			if (wantSpawn == 0) {
				diactivate();
			} else {
				spend( TICK );
			}
			return true;
		}

		public void addSpawns( int count ) {
			wantSpawn += count;
			doSpawns();
			if (wantSpawn > 0) {
				spend( -cooldown() );
			}
		}

		private void doSpawns() {
			if (Dungeon.level == null || TooCruel.levelBuilding) return;
			int tries = 0;
			while (wantSpawn > 0 && tries++ < 10 && Dungeon.level.spawnMob( 6 )) wantSpawn--;
		}

		private static final String WANT_SPAWN = "want_spawn";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( WANT_SPAWN, wantSpawn );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			wantSpawn = bundle.getInt( WANT_SPAWN );
		}

		public static class EnemyTracker extends Buff implements TcBuff.OnDeath {
			@Override
			public void onDeathProc() {
				if (Dungeon.hero == null || !Dungeon.hero.isAlive()) return;
				Buff.affect( Dungeon.hero, Hydra.class ).addSpawns( 2 );
			}
		}
	}

	//Perfect Information, mind vision and awareness always, magic mapping on each floor
	public static class PerfectInformation extends Buff implements TcBuff.SwitchLevel {
		private boolean wantMM = true;

		@Override
		public boolean act() {
			Buff.prolong( target, MindVision.class, MindVision.DURATION );
			Buff.prolong( target, Awareness.class, Awareness.DURATION );
			if (wantMM && target instanceof Hero && GameScene.scene != null) {
				ScrollOfMagicMapping s = new ScrollOfMagicMapping();
				s.anonymize();
				s.setCurrent( (Hero) target );
				s.doRead();
				((Hero) target).spend( -1f );
				wantMM = false;
			}
			spend( TICK );
			return true;
		}

		public void pushBackInTime() {
			clearTime();
			spend( -TICK );
		}

		@Override
		public void onSwitchLevel() {
			pushBackInTime();
			wantMM = true;
		}

		private static final String WANT_MM = "want_magic_mapping";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( WANT_MM, wantMM );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			wantMM = bundle.getBoolean( WANT_MM );
		}
	}

	//redraws the map and fog after walls change
	public static class FullSceneUpdater extends Buff {
		{ actPriority = VFX_PRIO + 1; }

		public boolean walls = false;
		public boolean fog = false;

		public static void requestFog() {
			request( false, true );
		}

		public static void requestFull() {
			request( true, true );
		}

		private static void request( boolean walls, boolean fog ) {
			if (Dungeon.hero == null) return;
			FullSceneUpdater buff = Buff.affect( Dungeon.hero, FullSceneUpdater.class );
			buff.walls = buff.walls || walls;
			buff.fog = buff.fog || fog;
			buff.timeToNow();
			buff.spendConstant( Math.min( 0.1f, Dungeon.hero.cooldown() - 0.1f ) );
		}

		@Override
		public boolean act() {
			if (walls) {
				Dungeon.level.buildFlagMaps();
				Dungeon.level.cleanWalls();
			}
			if (fog) {
				Dungeon.observe( Dungeon.level.length() );
				GameScene.updateMap();
			}
			walls = fog = false;
			diactivate();
			return true;
		}
	}

	//kills the holder after a delay, used by Insomnia's wraiths
	public static class DelayedDecay extends Buff implements TcBuff.Invulnerable {
		public boolean fx = false;
		public boolean invulnerable = false;

		public void set( float delay, boolean fx, boolean invulnerable ) {
			timeToNow();
			postpone( delay );
			this.invulnerable = invulnerable;
			this.fx = fx;
		}

		@Override
		public boolean act() {
			if (fx && Dungeon.level.heroFOV[target.pos] && target.sprite != null) {
				Sample.INSTANCE.play( Assets.Sounds.BURNING, 0.25f );
				target.sprite.emitter().burst( ShadowParticle.UP, 10 );
			}
			invulnerable = false;
			if (target instanceof Mob) ((Mob) target).EXP = 0;
			target.destroy();
			if (target.sprite != null) target.sprite.die();
			spend( TICK );
			return true;
		}

		@Override
		public boolean isInvulnerable( Class<?> effect ) {
			return invulnerable;
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "show_death_fx", fx );
			bundle.put( "invulnerable", invulnerable );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			fx = bundle.getBoolean( "show_death_fx" );
			invulnerable = bundle.getBoolean( "invulnerable" );
		}
	}

	//Unstable Accessories, a growing chance to transmute worn rings and artifacts when hit
	public static class ControlledRandomness extends Buff implements TcBuff.DefenseProc {
		private static final float CHANCE = 0.05f;
		private float rerollChance = CHANCE;

		@Override
		public boolean attachTo( Char target ) {
			return target instanceof Hero && super.attachTo( target );
		}

		@Override
		public void defenseProc( Char enemy, int damage ) {
			if (Random.Float() < rerollChance) {
				rerollChance = CHANCE;
				Hero hero = (Hero) target;
				if (hero.belongings.ring() != null) TooCruel.transmute( hero.belongings.ring(), hero );
				if (hero.belongings.misc() != null) TooCruel.transmute( hero.belongings.misc(), hero );
				if (hero.belongings.artifact() != null) TooCruel.transmute( hero.belongings.artifact(), hero );
			} else {
				rerollChance += CHANCE;
			}
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "reroll_chance", rerollChance );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			rerollChance = bundle.getFloat( "reroll_chance" );
		}
	}

	//Casual Approach, each monster the hero sees is chained to the hero once
	public static class CasualApproach extends Buff {
		{ actPriority = HERO_PRIO + 1; }

		@Override
		public boolean act() {
			boolean[] fov = TooCruel.fovOf( target );
			ArrayList<Mob> valid = new ArrayList<>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob != target && fov[mob.pos] && mob.alignment != Char.Alignment.ALLY
						&& !(mob instanceof NPC) && mob.buff( ApproachUsed.class ) == null) {
					valid.add( mob );
				}
			}
			Mob m = valid.isEmpty() ? null : Random.element( valid );
			if (m != null && chain( m )) return false;
			spend( TICK );
			return true;
		}

		private boolean chain( final Char enemy ) {
			if (Dungeon.level.adjacent( target.pos, enemy.pos ) || enemy.properties().contains( Char.Property.IMMOVABLE )) {
				if (enemy instanceof Mob) ((Mob) enemy).beckon( target.pos );
				Buff.affect( enemy, ApproachUsed.class );
				return false;
			}
			int pos = target.pos;
			Ballistica chain = new Ballistica( pos, enemy.pos, Ballistica.STOP_TARGET );
			if (chain.collisionPos != enemy.pos || chain.path.size() < 2 || Dungeon.level.pit[chain.path.get( 1 )]) {
				return false;
			}
			int newPos = -1;
			for (int i : chain.subPath( 1, chain.dist )) {
				if (!Dungeon.level.solid[i] && Actor.findChar( i ) == null) {
					newPos = i;
					break;
				}
			}
			if (newPos == -1) return false;

			final int newPosFinal = newPos;
			if (target.sprite == null || target.sprite.parent == null || enemy.sprite == null) {
				pullEnemy( enemy, newPosFinal );
				spend( TICK );
				return false;
			}
			new Item().throwSound();
			Sample.INSTANCE.play( Assets.Sounds.CHAINS );
			target.sprite.parent.add( new Chains( DungeonTilemap.raisedTileCenterToWorld( pos ),
					enemy.sprite.destinationCenter(), Effects.Type.CHAIN, new Callback() {
				@Override
				public void call() {
					Actor.add( new Pushing( enemy, enemy.pos, newPosFinal, new Callback() {
						@Override
						public void call() {
							pullEnemy( enemy, newPosFinal );
						}
					} ) );
					spend( TICK );
					next();
				}
			} ) );
			return true;
		}

		private void pullEnemy( Char enemy, int pullPos ) {
			Buff.affect( enemy, ApproachUsed.class );
			enemy.pos = pullPos;
			if (enemy.sprite != null) enemy.sprite.place( pullPos );
			Dungeon.level.occupyCell( enemy );
			if (enemy instanceof Mob) ((Mob) enemy).beckon( target.pos );
			FullSceneUpdater.requestFog();
		}

		public static class ApproachUsed extends Buff {
		}
	}

	//Jack-in-the-box and In Your Face, a monster wakes and hunts a few turns after it notices the hero
	public static class DelayedBeckon extends Buff implements TcBuff.OnDamageTaken {
		public static final int NOTICE_DISTANCE = 2;
		public static final int NOTICE_TIMEOUT = 2;

		private int ticker = -1;

		@Override
		public boolean act() {
			if (ticker == 0) {
				TooCruel.forceBeckon( target, Dungeon.hero.pos );
				diactivate();
				return true;
			}
			if (ticker < 0) {
				if (Dungeon.level.distance( target.pos, Dungeon.hero.pos ) <= NOTICE_DISTANCE && hasFov( target.pos, Dungeon.hero.pos )) {
					start();
				}
			}
			if (ticker > 0) {
				if (target.sprite != null) target.sprite.showStatus( CharSprite.NEUTRAL, ticker + "..." );
				ticker--;
			}
			spend( TICK );
			return true;
		}

		public void start() {
			start( NOTICE_TIMEOUT );
		}

		public void start( int delay ) {
			if (ticker < 0 || ticker > delay) ticker = delay;
		}

		public int getTicker() {
			return ticker;
		}

		@Override
		public void onDamageTaken( int damage, Object src ) {
			for (Mob mob : Dungeon.level.mobs) {
				if (Dungeon.level.distance( target.pos, mob.pos ) <= NOTICE_DISTANCE && hasFov( target.pos, mob.pos )) {
					DelayedBeckon b = mob.buff( DelayedBeckon.class );
					if (b != null) b.start();
				}
			}
		}

		private static boolean hasFov( int a, int b ) {
			return new Ballistica( a, b, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET ).collisionPos == b;
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "ticker", ticker );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			ticker = bundle.getInt( "ticker" );
		}
	}

	//Recursive Hierarchy, each layer of mimic makes what comes out a little larger
	public static class RecursiveResizing extends Buff implements TcBuff.PersistNesting {
		private float step = 1f;

		public RecursiveResizing set( float step ) {
			this.step = step;
			return this;
		}

		@Override
		public void applyNestingEffect( Char target ) {
			Buff.affect( target, Resizing.class ).multiply( step );
			Buff.affect( target, RecursiveResizing.class ).set( step );
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "step", step );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			step = bundle.getFloat( "step" );
		}
	}

	//Insomnia, monsters out of a fight act at half speed for a while after seeing the hero
	public static class InsomniaSlowdown extends FlavourBuff implements TcBuff.Timescale {
		public static final float DURATION = 5f;

		@Override
		public float speedFactor() {
			return 0.5f;
		}
	}

	//Insomnia, monsters never sleep and move at double speed
	public static class InsomniaSpeed extends Buff implements TcBuff.Timescale {
		{ revivePersists = true; }

		@Override
		public float speedFactor() {
			return 2f;
		}

		@Override
		public boolean act() {
			if (target instanceof Mob) {
				Mob mob = (Mob) target;
				if (mob.state == mob.SLEEPING) mob.beckon( mob.pos );
			}
			spend( TICK );
			return true;
		}
	}

	//Insomnia, resting calls short lived wraiths
	public static class Insomnia extends Buff {
		@Override
		public boolean act() {
			if (target instanceof Hero) {
				Hero hero = (Hero) target;
				if (hero.resting) {
					hero.interrupt();
					Sample.INSTANCE.play( Assets.Sounds.CURSED );
					for (int i = 0; i < 4; i++) {
						Wraith w = Wraith.spawnAt( hero.pos );
						if (w != null) {
							Buff.affect( w, DelayedDecay.class ).set( Random.IntRange( 2, 6 ) * TICK, true, true );
							Buff.affect( w, AscensionChallenge.AscensionBuffBlocker.class );
						}
					}
					GLog.n( Messages.get( TooCruel.class, "insomnia_wakeup" ) );
				}
			} else {
				detach();
			}
			spend( TICK );
			return true;
		}
	}

	//helper for Thoughtless, shows the message one moment after the floor changes
	public static class BurstVisual extends Buff {
		{ actPriority = VFX_PRIO; }

		public boolean heaps = false;
		public boolean inventory = false;

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				timeToNow();
				spend( -1f );
				return true;
			}
			return false;
		}

		@Override
		public boolean act() {
			if (heaps && !inventory) {
				GLog.n( Messages.get( TooCruel.class, "thoughtless_destroyed_heaps" ) );
			} else if (inventory) {
				GLog.n( Messages.get( TooCruel.class, "thoughtless_destroyed_inventory" ) );
				if (target.isAlive() && target.sprite != null) {
					target.sprite.emitter().burst( com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle.FACTORY, 6 );
					Sample.INSTANCE.play( Assets.Sounds.BURNING );
				}
			}
			detach();
			return true;
		}
	}
}
