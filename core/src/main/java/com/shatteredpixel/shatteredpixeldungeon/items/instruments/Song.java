package com.shatteredpixel.shatteredpixeldungeon.items.instruments;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;




public enum Song {
	SONG_OF_DELIVERANCE {
		@Override
		public Integer[] melody() {
			return new Integer[] {1, 2, 1, 3, 1, 2, 1, 3};
		}

		@Override
		public String getName() {
			return "Song of Deliverance";
		}

		@Override
		public void onPlay() {
			boolean procced = false;
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob.properties().contains(Char.Property.UNDEAD) && !(mob.properties().contains(Char.Property.MINIBOSS) || mob.properties().contains(Char.Property.BOSS))) {
					if (mob instanceof Wraith) {
						mob.die(null);
					} else {
						mob.damage(mob.HT/2, new Grim());
					}
					if (!mob.isAlive()) {
						procced = true;
					}
					mob.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
					new Flare( 6, 32 ).show( mob.sprite, 2f ) ;
				}
			}
			if (procced) {
				GLog.p("The spirits of the dead thank you silently as they are released into the afterlife...");
			}
		}
	},
	SONG_OF_PEACE {
		@Override
		public Integer[] melody() {
			return new Integer[] {1, 3, 4, 1, 2, 1, 3};
		}

		@Override
		public String getName() {
			return "Song of Peace";
		}

		@Override
		public void onPlay() {
			Buff.affect(Dungeon.hero, Drowsy.class);
			GLog.p("You drift into a magical sleep...");
		}

		@Override
		public float[] noteLengths() {
			return new float[] {0.5f, 0.5f, 0.5f, 0.5f, 1f, 0.5f, 0.5f};
		}
	},
	SONG_OF_HEALING {
		@Override
		public Integer[] melody() {
			return new Integer[] {1, 2, 4, 1, 2, 4};
		}

		@Override
		public String getName() {
			return "Song of Healing";
		}

		@Override
		public void onPlay() {}

		@Override
		public boolean unlocked() {
			return false;
		}
	};

	public abstract Integer[] melody();

	public float[] noteLengths() {
		float[] lengths = new float[melody().length];
		for (int i = 0; i < lengths.length; i++) {
			lengths[i] = 0.5f;
		}
		return lengths;
	}

	public abstract String getName();

	public abstract void onPlay();

	public boolean unlocked() {
		return true;
	}

	public static String getNote(Integer note) {
		switch (note) {
			case 1:
				return "A";
			case 2:
				return "B";
			case 3:
				return "C";
			case 4:
				return "D";
			case 5:
				return "E";
		}
		return null;
	}

	public static Sheet createSheet(int x, int y, int width, int height, int songLength, Integer[] melody) {
		return new Sheet(x, y, width, height, songLength, melody);
	}

	public void play(Instrument instrument, Hero hero) {
		play(instrument, 0, hero);
	}

	private void play(final Instrument instrument, final int index, final Hero hero) {
		final Integer[] melody = melody();
		if (index < melody.length) {
			ShatteredPixelDungeon.scene().add(new Delayer(noteLengths()[index]) {
				@Override
				protected void onComplete() {
					Sample.INSTANCE.play(instrument.noteSFX(melody[index]));
					play(instrument, index+1, hero);
					hero.sprite.emitter().burst(Speck.factory(Speck.NOTE), 1);
				}
			});
		} else {
			hero.ready = true;
			hero.ready();
		}
	}

	public static class Sheet extends Group {
		private int x;
		private int y;
		private int width;
		private int height;
		private int songLength;
		private Integer[] melody;

		Sheet(int x, int y, int width, int height, int songLength, Integer[] melody) {
			update(x, y, width, height, songLength, melody);
		}

		private void update(int x, int y, int width, int height, int songLength, Integer[] melody) {
			clear();
			Image bg = new Image(Assets.Sprites.NOTES);
			bg.x = x;
			bg.y = y;
			bg.frame(0, 0, width, height);
			add(bg);
			if (melody != null) {
				for (int i = 0; i < melody.length; i++) {
					int note = melody[i];
					RenderedTextBlock text = PixelScene.renderTextBlock(getNote(note), 7);
					float tx = x + (width / songLength) * i;
					text.setPos(tx, y + (height / 5) * (note-1));
					add(text);
				}
			}
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
			this.melody = melody;
			this.songLength = songLength;
		}

		@Override
		public synchronized void update() {
			update(x, y, width, height, songLength, melody);
		}

		public void update(Integer[] melody) {
			update(x, y, width, height, songLength, melody);
		}
	}
}
