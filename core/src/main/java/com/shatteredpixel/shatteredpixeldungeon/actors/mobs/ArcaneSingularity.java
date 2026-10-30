package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Deistic Pixel Dungeon's Arcane Singularity. A burning scroll of psionic blast tears one open.
//It never moves, zaps any creature it can see, friend or foe, and bursts when it stabilizes or is destroyed.
//Silence hurts it instead of affecting it.
public class ArcaneSingularity extends Mob {

	{
		spriteClass = Sprite.class;
		HP = HT = 20;
		defenseSkill = 5;
		EXP = 3;
		maxLvl = 30;
		state = HUNTING;
		properties.add(Property.IMMOVABLE);
		properties.add(Property.INORGANIC);
	}

	private int life = 20;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 6 + Dungeon.scalingDepth());
	}

	@Override
	public int attackSkill( Char target ) {
		return 15 + Dungeon.scalingDepth();
	}

	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Silence){
			damage(Random.NormalIntRange(HT / 5, HT / 3), buff);
			return false;
		}
		return super.add(buff);
	}

	@Override
	protected boolean act() {
		if (--life <= 0 && isAlive()){
			die(null);
			return true;
		}
		return super.act();
	}

	@Override
	protected Char chooseEnemy() {
		if (enemy == null || !enemy.isAlive() || !fieldOfView[enemy.pos]){
			ArrayList<Char> seen = new ArrayList<>();
			for (Char ch : Actor.chars()){
				if (ch != this && ch.isAlive() && fieldOfView != null && fieldOfView[ch.pos]) seen.add(ch);
			}
			enemy = seen.isEmpty() ? null : Random.element(seen);
		}
		return enemy;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean getCloser( int target ) {
		return false;
	}

	@Override
	protected boolean getFurther( int target ) {
		return false;
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		if (sprite != null && (sprite.visible || enemy.sprite.visible)){
			sprite.zap(enemy.pos);
			return false;
		}
		zap();
		return true;
	}

	private void zap(){
		spend(1f);
		if (enemy != null && hit(this, enemy, true)){
			enemy.damage(damageRoll(), this);
		} else if (enemy != null && enemy.sprite != null){
			enemy.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete(){
		zap();
		next();
	}

	@Override
	public void die( Object cause ) {
		Sample.INSTANCE.play(Assets.Sounds.BLAST, 2);
		for (int n : PathFinder.NEIGHBOURS9){
			int c = pos + n;
			if (Dungeon.level.heroFOV[c]) CellEmitter.get(c).burst(EnergyParticle.FACTORY, 4);
			if (Dungeon.level.flamable[c]){
				Dungeon.level.set(c, Terrain.EMBERS);
				GameScene.updateMap(c);
			}
			Char ch = Actor.findChar(c);
			if (ch != null && ch != this){
				int dmg = Random.NormalIntRange(1, 10 + Dungeon.scalingDepth() * 2) - ch.drRoll();
				if (dmg > 0) ch.damage(dmg, this);
			}
		}
		super.die(cause);
	}

	private static final String LIFE = "life";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LIFE, life);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		life = bundle.getInt(LIFE);
	}

	public static class Sprite extends MobSprite {
		public Sprite(){
			super();
			texture(Assets.Sprites.DEISTIC_SINGULARITY);
			TextureFilm frames = new TextureFilm(texture, 16, 18);
			idle = new Animation(10, true);
			idle.frames(frames, 0, 1, 2, 3, 4, 5, 6, 7);
			run = new Animation(10, true);
			run.frames(frames, 2, 3, 4);
			attack = new Animation(15, false);
			attack.frames(frames, 8, 9, 10, 11);
			zap = attack.clone();
			die = new Animation(10, false);
			die.frames(frames, 12, 13, 14, 15);
			play(idle);
		}

		@Override
		public void zap( int cell ) {
			super.zap(cell);
			MagicMissile.boltFromChar(parent, MagicMissile.SHAMAN_PURPLE, this, cell, new Callback() {
				@Override
				public void call() {
					((ArcaneSingularity) ch).onZapComplete();
				}
			});
			Sample.INSTANCE.play(Assets.Sounds.ZAP);
		}

		@Override
		public void onComplete( Animation anim ) {
			if (anim == zap) idle();
			super.onComplete(anim);
		}
	}
}
