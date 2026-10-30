/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StableElementalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Clairvoyant PD, an elemental ally the Apprentice's class armor called four at a time.
//Here the Apprentice's Wild Magic can call one. It shocks what it hits, zaps from range,
//and any buff or debuff placed on it tears it apart.
public class StableElemental extends Mob {

	{
		HP = HT = 60;
		defenseSkill = 20;
		spriteClass = StableElementalSprite.class;
		properties.add(Property.ELECTRIC);
		alignment = Alignment.ALLY;
		intelligentAlly = true;
		actPriority = MOB_PRIO + 1;
		state = HUNTING;
		EXP = 0;
		maxLvl = -2;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 35);
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 5);
	}

	private int rangedCooldown = Random.NormalIntRange(3, 5);

	@Override
	protected boolean act() {
		if (state == HUNTING) rangedCooldown--;
		return super.act();
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		if (Dungeon.level.adjacent(pos, enemy.pos) || rangedCooldown > 0) {
			return super.doAttack(enemy);
		} else if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
			sprite.zap(enemy.pos);
			return false;
		} else {
			zap();
			return true;
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc(enemy, damage);
		meleeProc(enemy, damage);
		return damage;
	}

	private void zap() {
		spend(1f);
		if (hit(this, enemy, true)) {
			if (enemy == Dungeon.hero) GameScene.flash(0x80FFFFFF);
		} else if (enemy.sprite != null) {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
		rangedCooldown = Random.NormalIntRange(3, 5);
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public boolean add( Buff buff ) {
		damage(Random.NormalIntRange(HT / 2, HT * 3 / 5), buff);
		return false;
	}

	private static final String COOLDOWN = "cooldown";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(COOLDOWN)) rangedCooldown = bundle.getInt(COOLDOWN);
	}

	protected void meleeProc( Char enemy, int damage ) {
		ArrayList<Char> affected = new ArrayList<>();
		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		Shocking.arc(this, enemy, 2, affected, arcs);
		affected.remove(Dungeon.hero);
		affected.remove(this);
		if (!Dungeon.level.water[enemy.pos]) affected.remove(enemy);
		for (Char ch : affected) ch.damage(Math.round(damage * 0.4f), this);

		if (sprite == null || sprite.parent == null) return;
		boolean visible = sprite.visible || (enemy.sprite != null && enemy.sprite.visible);
		if (visible) {
			sprite.parent.addToFront(new Lightning(arcs, null));
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
		}
	}

	public static StableElemental summonAt( int pos ) {
		if (pos < 0 || pos >= Dungeon.level.length() || Dungeon.level.solid[pos] || Actor.findChar(pos) != null) return null;
		StableElemental w = new StableElemental();
		w.pos = pos;
		w.state = w.HUNTING;
		GameScene.add(w, 2);
		if (w.sprite != null) {
			w.sprite.alpha(0);
			w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 1.5f));
			w.sprite.emitter().burst(EnergyParticle.FACTORY, 5);
		}
		return w;
	}

	public static void summonAround( int pos ) {
		for (int n : PathFinder.NEIGHBOURS4) summonAt(pos + n);
	}
}
