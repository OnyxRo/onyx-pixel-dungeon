package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TestFireChalBoss extends Mob {
	{
		spriteClass = StatueSprite.class;

		HP = HT = 2000;
		EXP = 30;
		defenseSkill = 18;

		viewDistance = 20;
		state = HUNTING;
		properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);
	}

	private int time_to_summon = 0;
	private int MAX_COOLDOWN = 10;

	private int numBolts() {
		return (int) (6 + (1 - (HP/(float)HT))*8);
	}

	private boolean canSummon() {
		return time_to_summon < 1;
	}

	@Override
	public int damageRoll() {
		return (int) (Random.NormalIntRange( 30, 40 ));
	}

	@Override
	public int attackSkill( Char target ) {
		return(50);
	}

	@Override
	public int drRoll() {
		return (int) (Random.NormalIntRange(0, 10));
	}

	@Override
	protected boolean act() {
		time_to_summon--;
		if (canSummon()) {
			zap();
			notice();
		}
		return super.act();
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}


	public void zap() {
		time_to_summon = MAX_COOLDOWN;
		int[] positions = new int[numBolts()];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = Dungeon.level.randomRespawnCell( null );
		}
		for (final int i : positions) {
			sprite.zap(i);
			MagicMissile.boltFromChar( sprite.parent,
					MagicMissile.SHADOW,
					this.sprite,
					i,
					new Callback() {
						@Override
						public void call() {
							Mob.spawnAt(Tower.class, i);
						}
					} );
			Sample.INSTANCE.play( Assets.Sounds.ZAP );
		}
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		GameScene.bossSlain();
		Dungeon.level.drop( new WornKey( Dungeon.depth  ), pos ).sprite.drop();

		//60% chance of 2 shards, 30% chance of 3, 10% chance for 4. Average of 2.5
		int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < blobs; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable[pos + ofs]);
			Dungeon.level.drop( new ScrollOfUpgrade(), pos + ofs ).sprite.drop( pos );
		}


		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
		}

		yell( Messages.get(this, "defeated") );
	}

	static public class Tower extends Mob {

		@Override
		public String name() {
			return "lightning tower";
		}


		private static final float TIME_TO_ZAP = 2f;

		private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

		{
			spriteClass = LitTowerSprite.class;

			HP = HT = 600;
			defenseSkill = 1000;

			EXP = 0;
			maxLvl = -2;
			state = PASSIVE;
			properties.add(Property.IMMOVABLE);
		}

		@Override
		public void beckon(int cell) {
			// Do nothing
		}

		@Override
		public int damageRoll() {
			return 0;
		}


		@Override
		public int attackSkill(Char target) {
			return 100;
		}


		@Override
		public void damage(int dmg, Object src) {
		}

		@Override
		protected boolean act() {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob instanceof Tower) {
					FX(mob.pos);
				}
			}
			die(this);
			sprite.kill();
			return true;
		}

		private void FX(int cell) {
			sprite.parent.add(new Beam.DeathRay(this.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
			zap(pos);
		}

		private void zap(int cell) {
			Ballistica shot = new Ballistica(this.pos, cell, Ballistica.WONT_STOP);
			for (int c : shot.path) {
				Char ch = Actor.findChar(c);
				if (ch != null && !(ch instanceof TestFireChalBoss)) {
					ch.damage(10, this);
				}
			}
		}
		@Override
		protected boolean doAttack(Char enemy) {
			return true;
		}

		@Override
		public String description() {
			return "The lightning shell crackles with electric power. "
					+ "It's powerful lightning attack is drawn to all living things in the lair. ";
		}

		@Override
		public boolean add( Buff buff ) { return false; }
	}

	static public class LitTowerSprite extends MobSprite {

		private int[] points = new int[2];

		public LitTowerSprite() {
			super();

			texture(Assets.Sprites.LITTOWER);
			TextureFilm frames = new TextureFilm(texture, 16, 16);

			idle = new MovieClip.Animation(10, true);
			idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0);

			run = idle.clone();
			die = idle.clone();
			attack = idle.clone();

			zap = attack.clone();

			idle();
		}

		@Override
		public void zap(int pos) {

			points[0] = ch.pos;
			points[1] = pos;

			turnTo(ch.pos, pos);
			play(zap);
		}

		@Override
		public void play(Animation anim) {
			if (anim.equals(idle)) {
				emitter().burst(SparkParticle.FACTORY, 20);
			}
			super.play(anim);
		}
	}
}
