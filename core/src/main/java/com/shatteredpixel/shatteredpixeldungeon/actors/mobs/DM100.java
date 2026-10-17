/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM100Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DM100 extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	
	{
		spriteClass = DM100Sprite.class;
		
		HP = HT = 20;
		defenseSkill = 8;
		
		EXP = 6;
		maxLvl = 13;
		
		loot = Generator.Category.SCROLL;
		lootChance = 0.25f;
		
		properties.add(Property.ELECTRIC);
		properties.add(Property.INORGANIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 8 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 4);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		//Pixel Dungeon Reforged Randomizer, Passive Protocol only attacks once hurt
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(DM100.class) == 2 && HP == HT) return false;
		boolean canRanged = new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		//Power Conservation allows a single zap
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(DM100.class) == 3 && hasZapped) canRanged = false;
		return super.canAttack(enemy) || canRanged;
	}
	
	//used so resistances can differentiate between melee and magical attacks
	public static class LightningBolt{}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos )
				|| new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {
			
			return super.doAttack( enemy );
			
		} else {

			if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(DM100.class) == 3 && hasZapped) {
				return super.doAttack( enemy );
			}

			spend( TIME_TO_ZAP );
			//Faulty Batteries cost half of the DM-100's health with every zap
			if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(DM100.class) == 1 && HP > 1) {
				HP /= 2;
			}
			hasZapped = true;

			Invisibility.dispel(this);
			if (hit( this, enemy, true )) {
				int dmg = Random.NormalIntRange(3, 10);
				dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
				enemy.damage( dmg, new LightningBolt() );

				if (enemy.sprite.visible) {
					enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
					enemy.sprite.flash();
				}
				
				if (enemy == Dungeon.hero) {
					
					PixelScene.shake( 2, 0.3f );
					
					if (!enemy.isAlive()) {
						Badges.validateDeathFromEnemyMagic();
						Dungeon.fail( this );
						GLog.n( Messages.get(this, "zap_kill") );
					}
				}
			} else {
				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}
			
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				return true;
			}
		}
	}
	

	//Pixel Dungeon Reforged Randomizer. Electrical Aura, Combat Ready, Security Network, Faulty Batteries,
	//Passive Protocol and Power Conservation (the last three in canAttack and doAttack above)
	private boolean seenPlayer = false;
	private boolean hasZapped = false;
	private static final String SEEN_PLAYER = "seen_player";
	private static final String HAS_ZAPPED = "has_zapped";

	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(DM100.class) == 2) immunities.add(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep.class);
	}

	@Override
	public void storeInBundle( com.watabou.utils.Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( SEEN_PLAYER, seenPlayer );
		bundle.put( HAS_ZAPPED, hasZapped );
	}

	@Override
	public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		super.restoreFromBundle( bundle );
		seenPlayer = bundle.getBoolean( SEEN_PLAYER );
		hasZapped = bundle.getBoolean( HAS_ZAPPED );
	}

	@Override
	protected void onAdd() {
		boolean first = firstAdded;
		super.onAdd();
		//Combat Ready, spawns awake
		if (first && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(DM100.class) == 2 && state == SLEEPING) {
			state = WANDERING;
		}
	}

	@Override
	public void notice() {
		super.notice();
		if (!seenPlayer && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(DM100.class) == 3 && fieldOfView[Dungeon.hero.pos] && Dungeon.hero.invisible <= 0) {
			seenPlayer = true;
			com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter.center( pos ).start( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.factory( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.SCREAM ), 0.3f, 3 );
			com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.ALERT );
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				mob.beckon( pos );
			}
		}
	}

	@Override
	protected boolean act() {
		//Electrical Aura, a little shock to everything on the other side adjacent, every turn
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(DM100.class) == 1 && isAlive()) {
			for (int n : com.watabou.utils.PathFinder.NEIGHBOURS8) {
				if (pos + n < 0 || pos + n >= Dungeon.level.length()) continue;
				Char ch = findChar( pos + n );
				if (ch != null && ch.isAlive() && ch.alignment != alignment
						&& !ch.isImmune( com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity.class )) {
					ch.damage( Random.NormalIntRange( 1, 8 ), new LightningBolt() );
					if (ch == Dungeon.hero && !ch.isAlive()) {
						Dungeon.fail( this );
						GLog.n( Messages.get(this, "zap_kill") );
					}
				}
			}
		}
		return super.act();
	}
}
