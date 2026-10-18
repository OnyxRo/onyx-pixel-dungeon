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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Rat extends Mob {

	{
		spriteClass = RatSprite.class;
		
		HP = HT = 8;
		defenseSkill = 2;

		maxLvl = 5;
	}

	@Override
	protected boolean act() {
		if (alignment != Alignment.ALLY
				&& Dungeon.level.heroFOV[pos]
				&& Dungeon.hero.armorAbility instanceof Ratmogrify){
			alignment = Alignment.NEUTRAL;
			if (enemy == Dungeon.hero) enemy = null;
			if (state == SLEEPING) state = WANDERING;
		}
		//Shittened PD, a rat that has seen you may squeal and speed up
		if (Dungeon.level.heroFOV[pos] && Random.Int(90) == 1 && state == HUNTING) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(this,
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste.class, 1f);
			com.watabou.noosa.audio.Sample.INSTANCE.playForDuration( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.FREE_RAT,
					Random.NormalFloat(0.2f, Random.Float(1f,4f)) );
		}
		return super.act();
	}

	//Shittened PD, counting rats for the rat killer badges, the odd banner, and the rat album for a music player
	@Override
	public void die(Object cause) {
		super.die(cause);
		if (alignment == Alignment.ALLY) return;
		com.shatteredpixel.shatteredpixeldungeon.Statistics.ratsSlain++;
		com.shatteredpixel.shatteredpixeldungeon.Badges.validateRats();
		if (Random.Int(0,18) == 2) {
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.ratSlain();
		}
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer player =
				Dungeon.hero.belongings.getItem( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MusicPlayer.class );
		if (player != null && Dungeon.LimitedDrops.RAT_ALBUM.count == 0 && Random.Int(10) == 5) {
			Dungeon.level.drop( new com.shatteredpixel.shatteredpixeldungeon.items.albums.RatAlbum(), pos ).sprite.drop();
			Dungeon.LimitedDrops.RAT_ALBUM.count++;
		}
	}

	@Override
	public int damageRoll() {
		int dmg = Random.NormalIntRange( 1, 4 );
		//Pixel Dungeon Reforged Randomizer, Nibbling Nuisances
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Rat.class) == 2) dmg /= 2;
		return dmg;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 8;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 1);
	}

	private static final String RAT_ALLY = "rat_ally";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (alignment == Alignment.ALLY) bundle.put(RAT_ALLY, true);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(RAT_ALLY)) alignment = Alignment.ALLY;
	}

	//Pixel Dungeon Reforged Randomizer. Evasive Pests, Toxic Fangs, Meaty Rats, Nibbling Nuisances (above), Frail Vermin
	{
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Rat.class) == 2) defenseSkill = 25;
	}

	@Override
	protected void onAdd() {
		boolean first = firstAdded;
		super.onAdd();
		if (first && com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Rat.class) == 3) {
			HT = Math.max(1, HT / 2);
			HP = Math.min(HP, HT);
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Rat.class) == 3 && Random.Int(3) == 0) {
			int duration = Random.IntRange(1, 3);
			if (Random.Float() > 0.8f) duration++;
			duration = Math.round(duration * (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge.statModifier(this) / 2f + 0.5f));
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison.class).set(duration);
		}
		return damage;
	}

	@Override
	public void rollToDropLoot() {
		super.rollToDropLoot();
		if (Dungeon.hero.lvl > maxLvl + 2) return;
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Rat.class) == 1 && Random.Float() > 0.5f) {
			Dungeon.level.drop(new com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat(), pos).sprite.drop();
		}
	}
}
