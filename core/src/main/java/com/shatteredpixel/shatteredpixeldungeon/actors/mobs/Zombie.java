/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;

import java.util.HashSet;

public class Zombie extends UMob {

    {
        name = "zombie";
        spriteClass = ZombieSprite.class;

        HP = HT = 22;
        defenseSkill = 9;
        atkSkill = 14;

        EXP = 5;
        maxLvl = 12;

        dmgMin = 3;
        dmgMax = 8;
        dmgRed = 4;
        TYPE_EVIL = true;
        TYPE_UNDEAD = true;
        TYPE_MINDLESS = true;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "Zombies are the corpses of unlucky adventurers and inhabitants of the dungeon, " +
                        "animated by emanations of evil magic from the depths below.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Grim.class );
    }

    { immunities.addAll( IMMUNITIES ); }
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;
import com.watabou.utils.Random;

public class Zombie extends Mob {
	

	{
		spriteClass = ZombieSprite.class;

		HP = HT = 70+(adj(0)*Random.NormalIntRange(3, 7));
		defenseSkill = 9+adj(1);
		baseSpeed = 2f;
		
		
		EXP = 7;
		maxLvl = 18;
		
	    state = WANDERING;	
		
		loot = PotionOfToxicGas.class;
		lootChance = 0.1f;
		
		properties.add(Property.UNDEAD);
	}


	@Override
	public float attackDelay() {
		return 2f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(10+adj(0), 20+adj(0));
	}

	@Override
	public int attackSkill( Char target) {
		return 15 +adj(0);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 8);
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, BeOld.class).set(20);
		}

		return damage;
	}

	{
		weakness.add(Burning.class);
		resistances.add(ToxicGas.class);
	}
}
