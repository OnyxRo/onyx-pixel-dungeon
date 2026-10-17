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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {
	
	{
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;
		defenseSkill = 4;
		
		EXP = 2;
		maxLvl = 8;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	
	@Override
	public int drRoll() {
		//Pixel Dungeon Reforged Randomizer, Brittle Armour
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Gnoll.class) == 2) return super.drRoll() - 1;
		return super.drRoll() + Random.NormalIntRange(0, 2);
	}

	//Pixel Dungeon Reforged Randomizer. Pack Hunters, Sling Masters, Alarm Network, Battle Scarred, Brittle Armour (above),
	//Pacifist Patrol
	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Gnoll.class) == 3) HUNTING = new Fleeing();
	}

	private int partnerId = -1;
	private boolean seenPlayer = false;
	private static final String PARTNER_ID = "partner_id";
	private static final String SEEN_PLAYER = "seen_player";

	@Override
	public void storeInBundle( com.watabou.utils.Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PARTNER_ID, partnerId );
		bundle.put( SEEN_PLAYER, seenPlayer );
	}

	@Override
	public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( PARTNER_ID )) partnerId = bundle.getInt( PARTNER_ID );
		seenPlayer = bundle.getBoolean( SEEN_PLAYER );
	}

	@Override
	protected void onAdd() {
		boolean first = firstAdded;
		super.onAdd();
		if (first && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Gnoll.class) == 1) {
			HP = (int) (HT * Random.Float(0.5f, 1.0f));
		}
	}

	@Override
	protected boolean act() {
		//a pack hunter brings a partner along, who does not bring another
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Gnoll.class) == 1 && partnerId == -1 && !(this instanceof GnollExile)) {
			partnerId = -2; //only tries once
			java.util.ArrayList<Integer> candidates = new java.util.ArrayList<>();
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (Dungeon.level.passable[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}
			if (!candidates.isEmpty()) {
				Gnoll child = new Gnoll();
				child.partnerId = this.id();
				this.partnerId = child.id();
				if (state != SLEEPING) {
					child.state = child.WANDERING;
				}
				child.pos = Random.element( candidates );
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.add( child );
				Dungeon.level.occupyCell( child );
				if (sprite.visible) {
					Actor.add( new com.shatteredpixel.shatteredpixeldungeon.effects.Pushing( child, pos, child.pos ) );
				}
				for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : buffs()) {
					if (b.revivePersists) {
						com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(child, b.getClass());
					}
				}
			}
		}

		//a wandering hunter keeps close to its partner
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Gnoll.class) == 1 && state == WANDERING && partnerId >= 0) {
			Char partner = (Char) Actor.findById( partnerId );
			if (partner instanceof Gnoll && partner.isAlive() && Dungeon.level.distance( pos, partner.pos ) > 2) {
				target = partner.pos;
			}
		}
		return super.act();
	}

	@Override
	public void notice() {
		super.notice();
		if (!seenPlayer && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Gnoll.class) == 3 && fieldOfView[Dungeon.hero.pos] && Dungeon.hero.invisible <= 0) {
			seenPlayer = true;
			com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter.center( pos ).start( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.factory( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.SCREAM ), 0.3f, 3 );
			com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.ALERT );
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				mob.beckon( pos );
			}
		}
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Gnoll.class) == 2) {
			return super.canAttack( enemy )
					|| new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica( pos, enemy.pos, com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.PROJECTILE ).collisionPos == enemy.pos;
		}
		return super.canAttack( enemy );
	}
}
