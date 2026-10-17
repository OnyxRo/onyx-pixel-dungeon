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
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

public class Bat extends Mob {

	{
		spriteClass = BatSprite.class;
		
		HP = HT = 30;
		defenseSkill = 15;
		baseSpeed = 2f;
		
		EXP = 7;
		maxLvl = 15;
		
		flying = true;
		
		loot = PotionOfHealing.class;
		lootChance = 0.1667f; //by default, see lootChance()
	}
	
	@Override
	public int damageRoll() {
		//Pixel Dungeon Reforged Randomizer, Blunted Fangs
		int dmg = Random.NormalIntRange( 5, 18 );
		return com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Bat.class) == 2 ? dmg / 2 : dmg;
	}
	
	@Override
	public int attackSkill( Char target ) {
		if (attachedId != -1 && target.id() == attachedId) return Char.INFINITE_ACCURACY;
		return 16;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 4);
	}

	@Override
	public void die(Object cause) {
		flying = false;
		super.die(cause);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		if (this instanceof CaveBat) {
			return super.attackProc(enemy,damage);
		}
		damage = super.attackProc( enemy, damage );
		//Blood Lock, once it tastes blood the bat never misses that target again unless they are separated
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Bat.class) == 3) attachedId = enemy.HP > damage ? enemy.id() : -1;
		int reg = Math.min( damage - 4, HT - HP );
		//Weak Regeneration
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Bat.class) == 1) reg /= 4;
		
		if (reg > 0) {
			HP += reg;
			//Draining Bite
			if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Bat.class) == 1) com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(enemy, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness.class, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness.DURATION);
			sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(reg), FloatingText.HEALING);
		}
		
		return damage;
	}
	
	@Override
	public float lootChance(){
		//Membrane Carrier, a bat always drops something
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Bat.class) == 3) return 1f;
		return super.lootChance() * ((7f - Dungeon.LimitedDrops.BAT_HP.count) / 7f);
	}
	
	@Override
	public Item createLoot(){
		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureNerf(Bat.class) == 3) {
			//the usual health potion chance, otherwise a bat wing membrane
			if (Random.Float() < lootChance * ((7f - Dungeon.LimitedDrops.BAT_HP.count) / 7f)) {
				Dungeon.LimitedDrops.BAT_HP.count++;
				return super.createLoot();
			}
			return new Membrane();
		}
		Dungeon.LimitedDrops.BAT_HP.count++;
		return super.createLoot();
	}
	

	//Pixel Dungeon Reforged Randomizer. Draining Bite, Blood Lock, Weak Regeneration, Blunted Fangs and Membrane Carrier
	//are above. Supersonic Speed and the rest of Blood Lock are here.
	private int attachedId = -1;
	private static final String ATTACHED = "attached";

	@Override
	public void storeInBundle( com.watabou.utils.Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ATTACHED, attachedId );
	}

	@Override
	public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( ATTACHED )) attachedId = bundle.getInt( ATTACHED );
	}

	@Override
	public float speed() {
		return super.speed() * (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(Bat.class) == 2 ? 1.75f : 1f);
	}

	@Override
	public void move( int step, boolean travelling ) {
		super.move( step, travelling );
		if (attachedId != -1) {
			Char e = (Char) Actor.findById( attachedId );
			if (e == null || !e.isAlive() || Dungeon.level.distance( pos, e.pos ) > 1) attachedId = -1;
		}
	}

	//Pixel Dungeon Reforged, what a Membrane Carrier drops. Three of them make a potion of healing.
	public static class Membrane extends Item {

		{
			image = com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.BAT_MEMBRANE;
			stackable = true;
		}

		@Override
		public boolean isUpgradable() {
			return false;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}

		@Override
		public int energyVal() {
			return 2 * quantity;
		}
	}
}
