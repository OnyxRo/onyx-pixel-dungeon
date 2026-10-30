/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KeyChainSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class KeyChain extends Mob {

	{
		spriteClass = KeyChainSprite.class;
		HP = HT = 28;
		defenseSkill = 12;
		flying = true;
		maxLvl = 14;
		properties.add(Property.UNDEAD);
		EXP = 6;

		loot = new CurseInfusion();
		lootChance = 0.1f;

		loot = Generator.Category.SCROLL;
		lootChance = 0.2f;
		//this.sprite.add( CharSprite.State.MARKED );
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 10 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 14;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}

	@Override
	protected boolean act() {

		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 4 );
		if(state == HUNTING){
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					Trap t = Dungeon.level.traps.get(i);
					if (t != null && t.active && t.visible) {
						if (Dungeon.level.heroFOV[pos]) {
							final int ii = i;
							final Trap tt = t;
							Actor.add(new Actor() {

								{
									//it's a visual effect, gets priority no matter what
									actPriority = VFX_PRIO;
								}

								@Override
								protected boolean act() {

									final Actor toRemove = this;
										((MissileSprite) ShatteredPixelDungeon.scene().recycle(MissileSprite.class)).
												reset(pos, ii, new IronKey() {
												}, new Callback() {
													@Override
													public void call() {
														tt.trigger();
														Actor.remove(toRemove);
														next();

													}
												});
									return false;
								}
							});
						}else{
							t.trigger();
						}
						spend(1);
						return true;
						}
					}
				}
			}

		return super.act();
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		int dm = Random.NormalIntRange( 3, 8 );
		enemy.damage( dm, new ToughGhost.Damamdawawd() );
		enemy.sprite.emitter().burst(ShadowParticle.UP, dm);
		if (!enemy.isAlive() && enemy == Dungeon.hero) {
			Dungeon.fail( getClass() );
			GLog.n( "You were killed by dark energy..." );
		}
		damage = super.attackProc( enemy, damage );
		return damage;
	}

}
