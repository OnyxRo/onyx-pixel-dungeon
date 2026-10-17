/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChaosMageSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

//port-oldstats
public class ChaosMage extends Mob {
    {
        spriteClass = ChaosMageSprite.class;

        HP = HT = 125;
        defenseSkill = 28;

        EXP = 15;
        maxLvl = 30;

        viewDistance = Light.DISTANCE;

        baseSpeed = 2f;
        state = HUNTING;
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add( Charm.class );
        resistances.add( Grim.class );
        resistances.add( Vampiric.class );
        resistances.add( WandOfDisintegration.class );
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 35 );
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( 0, 18 );
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Hero){
            corrupt((Hero) enemy);
        }

        if (damage > 0) {
            int healingAmt = Random.Int(0, damage);
            HP = Math.min(HT, HP + healingAmt);
            sprite.emitter().burst(ShadowParticle.UP, 2);
        }

        return damage;
    }

    protected boolean corrupt( Hero hero ) {
        switch (Random.Int(7)) {
            case 0:
                Buff.affect(hero, Vertigo.class);
                Buff.prolong(hero, Vertigo.class, 4);
                return true;
            case 1:
                Buff.affect(hero, Poison.class).set(3 * Poison.durationFactor(hero));
                return true;
            case 2:
                Buff.affect(hero, Burning.class).reignite(hero);
                return true;
            case 3:
                Buff.affect(hero, Blindness.class);
                Buff.prolong(hero, Blindness.class, 4);
                return true;
            case 4:
                Buff.affect(hero, Slow.class);
                Buff.prolong(hero, Slow.class, 6);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.drop(new PotionOfExperience(), pos).sprite.drop();
    }

    @Override
    public void notice() {
        super.notice();
        GLog.i("The Chaos Mage cackles with glee");
    }
}
