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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Warlock extends Mob {
	
	private static final float TIME_TO_ZAP	= 1f;
	
	{
		spriteClass = WarlockSprite.class;
		
		HP = HT = 70;
		defenseSkill = 18;
		
		EXP = 11;
		maxLvl = 21;
		
		loot = Generator.Category.POTION;
		lootChance = 0.5f;

		properties.add(Property.UNDEAD);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 12, 18 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 8);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		//Pixel Dungeon Reforged Randomizer, Short Range
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Warlock.class) == 1 && distance(enemy) > 3) return false;
		return super.canAttack(enemy)
				|| (new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos
					&& buff(com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing.Effect.class) == null); //Community PD, silenced casters cannot zap
	}
	
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos )
				|| buff(com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing.Effect.class) != null
				|| new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {
			
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
	
	//used so resistances can differentiate between melee and magical attacks
	public static class DarkBolt{}
	
	protected void zap() {
		//Spell Fatigue takes twice as long
		spend( com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Warlock.class) == 2 ? TIME_TO_ZAP * 2f : TIME_TO_ZAP );

		Invisibility.dispel(this);
		Char enemy = this.enemy;
		if (hit( this, enemy, true )) {
			//TODO would be nice for this to work on ghost/statues too
			if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
				//Arcane Mastery makes the degrade much stronger, Hindering Hex slows as well
				Buff.prolong( enemy, Degrade.class, Degrade.DURATION ).poweredUp = com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Warlock.class) == 3;
				if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Warlock.class) == 1) Buff.prolong( enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow.class, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow.DURATION );
				Sample.INSTANCE.play( Assets.Sounds.DEGRADE );
			}
			
			int dmg = Random.NormalIntRange( 12, 18 );
			dmg = Math.round(dmg * AscensionChallenge.statModifier(this));

			enemy.damage( dmg, new DarkBolt() );
			
			if (enemy == Dungeon.hero && !enemy.isAlive()) {
				Badges.validateDeathFromEnemyMagic();
				Dungeon.fail( this );
				GLog.n( Messages.get(this, "bolt_kill") );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}
	
	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public Item createLoot(){

		// 1/6 chance for healing, scaling to 0 over 8 drops
		if (Random.Int(3) == 0 && Random.Int(8) > Dungeon.LimitedDrops.WARLOCK_HP.count ){
			Dungeon.LimitedDrops.WARLOCK_HP.count++;
			return new PotionOfHealing();
		} else {
			Item i;
			do {
				i = Generator.randomUsingDefaults(Generator.Category.POTION);
			} while (i instanceof PotionOfHealing);
			return i;
		}

	}

	//Pixel Dungeon Reforged Randomizer. Cowardly Caster and Dance Fever
	private boolean dancing = false;
	private static final String DANCING = "dancing";

	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Warlock.class) == 3) WANDERING = new Dancing();
	}

	@Override
	public void storeInBundle( com.watabou.utils.Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DANCING, dancing );
	}

	@Override
	public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		super.restoreFromBundle( bundle );
		dancing = bundle.getBoolean( DANCING );
	}

	private void setIsDancing( boolean isDancing ) {
		dancing = isDancing;
		if (sprite instanceof com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite) {
			if (isDancing) {
				((com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite) sprite).dance();
			} else {
				sprite.idle();
			}
		}
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (dancing) setIsDancing( false );
		super.damage( dmg, src );
	}

	@Override
	public void beckon( int cell ) {
		if (!dancing) super.beckon( cell );
	}

	@Override
	public void notice() {
		if (!dancing) super.notice();
	}

	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Warlock.class) == 2) {
			return enemySeen && getFurther( target );
		}
		return super.getCloser( target );
	}

	//Dance Fever, now and then a warlock out wandering stops and dances
	private class Dancing extends Mob.Wandering {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (!dancing) {
				if (!enemyInFOV && !justAlerted && Random.Int( 50 ) == 0) {
					setIsDancing( true );
					spend( TICK );
					return true;
				}
				return super.act( enemyInFOV, justAlerted );
			}
			if (sprite instanceof com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite
					&& !((com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite) sprite).isDancing()) {
				((com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite) sprite).dance();
			}
			spend( TICK );
			return true;
		}
	}
}
