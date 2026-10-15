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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CodeESprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Yog extends Mob {
	
	{
		spriteClass = CodeESprite.class;
		
		HP = HT = 500;
		
		EXP = 50;
		
		state = HUNTING;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);

		loot = new ScrollOfUpgrade();
		lootChance = 1f;
	}
	


	@Override
	protected boolean act() {
		//heals 1 health per turn
		HP = Math.min( HT, HP+2 );

		if (state == HUNTING){
			rangedCooldown--;
		}

		if (Dungeon.hero.viewDistance >= Dungeon.level.distance(pos,Dungeon.hero.pos)){
			    Dungeon.observe();
		}
		if (Dungeon.level.heroFOV[pos]){
			notice();
		}

		return super.act();
	}



	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 20){
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 19 + (int)(Math.sqrt(8*(dmg - 19) + 1) - 1)/2;
		}

		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;
		int hpBracket = 100;

		if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
	}

	private void jump() {

		Level level = Dungeon.level;


		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}

		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return;

		int newPos;
		do {
			newPos = Random.Int(level.length());
		} while (
				level.solid[newPos] ||
						level.distance(newPos, enemy.pos) < 8 ||
						Actor.findChar(newPos) != null);
		if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.RED_LIGHT	 ), 16 );

		sprite.move( pos, newPos );
		move( newPos );

		if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.RED_LIGHT ), 16);
		Sample.INSTANCE.play( Assets.Sounds.PUFF );

		spend( 1 / speed() );
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 5 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	private int rangedCooldown = Random.NormalIntRange( 3, 5 );


	@Override
	protected boolean canAttack( Char enemy ) {
		if (rangedCooldown <= 0) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ).collisionPos == enemy.pos;
		} else {
			return super.canAttack( enemy );

		}

	}



	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos )) {

			return super.doAttack( enemy );


		} else {

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}

		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		meleeProc( enemy, damage );

		return damage;
	}


	protected void meleeProc( Char enemy, int damage ) {
		cursedBlast( enemy );
	}

	//the old cursedEffect call is a cursed zap from Yog toward the target now
	private void cursedBlast( Char enemy ) {
		CursedWand.cursedZap(null, this, new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica( pos, enemy.pos, com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.MAGIC_BOLT ),
				new com.watabou.utils.Callback() { @Override public void call() {} });
	}


	protected void rangedProc( Char enemy ) {
		cursedBlast( enemy );
	}


	private void zap() {
		spend( 1f );


		if (hit( this, enemy, true )) {

			rangedProc( enemy );


		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}

		rangedCooldown = Random.NormalIntRange( 3, 5 );

	}

	public void onZapComplete() {
		zap();
		next();
	}


	@Override
	public int defenseProc( Char enemy, int damage ) {

		ArrayList<Integer> spawnPoints = new ArrayList<>();
		
		for (int i=0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
				spawnPoints.add( p );
			}
		}
		
		if (spawnPoints.size() > 0) {
			RipperDemon larva = new RipperDemon() {

			};
			larva.pos = Random.element( spawnPoints );
			
			GameScene.add( larva );
			Actor.addDelayed( new Pushing( larva, pos, larva.pos ), -1 );
		}

		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof Elemental) {
				mob.aggro( enemy );
			}
		}

		return super.defenseProc(enemy, damage);
	}
	

	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof RipperDemon) {
				mob.die( cause );
			}
		}


		GameScene.bossSlain();
		Dungeon.level.drop( new WornKey( Dungeon.depth ), pos ).sprite.drop();
		super.die( cause );
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			for (Char ch : Actor.chars()){
				if (ch instanceof DriedRose.GhostHero){
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}
	}
	
	{
		immunities.add( Grim.class );
		immunities.add( GrimTrap.class );
		immunities.add( Amok.class );
		immunities.add( Charm.class );
		immunities.add( Sleep.class );
		immunities.add( Burning.class );
		immunities.add( ToxicGas.class );
		immunities.add( ScrollOfRetribution.class );
		immunities.add( ScrollOfPsionicBlast.class );
		immunities.add( Vertigo.class );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}


}
