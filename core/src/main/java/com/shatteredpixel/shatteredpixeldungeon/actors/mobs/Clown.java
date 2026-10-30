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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Trapskill;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.WeaponRack;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClownSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Clown extends Mob {
	boolean panic = false;
	{
		spriteClass = ClownSprite.class;
		HP = HT = 65;
		defenseSkill = 6;
		flying = true;
		EXP = 6;
		Buff.prolong( this, Levitation.class, 999999f );
		state = WANDERING;
		loot = new WeaponRack();
		lootChance = 1f;
		properties.add(Property.MINIBOSS);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set( damage );
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		if(Random.Int(3)!=0){
			GameScene.add(Blob.seed(pos, 20, StenchGas.class));
			Sample.INSTANCE.play(Assets.Sounds.PUFF, 1, 1, 0.5f);
			sprite.showStatus( CharSprite.WARNING, "FART" );
		}

		if(HP<35 && !panic){
			state = new Fleeing();
			Statue test = new Statue();
			test.pos = Dungeon.level.randomRespawnCell( null );
			GameScene.add( test );
			test.aggro(enemy);
			panic=true;
		}
		return super.defenseProc(enemy, damage);

	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 9 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 13;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}

	private final String PANIC = "panic";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PANIC, panic);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		panic = bundle.getBoolean(PANIC);
	}

	{
		immunities.add( StenchGas.class );
	}

}
