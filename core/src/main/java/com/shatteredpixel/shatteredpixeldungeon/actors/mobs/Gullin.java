package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.watabou.utils.Random;

public class Gullin extends Kupua {
    {
        loot = Generator.random(Generator.Category.WEAPON);
        lootChance = 0.5f;
        HP = HT = 300;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(enemy, Bleeding.class).set(Dungeon.depth*2);//Heavy bleeding
        return super.attackProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (src instanceof Hero) {
            KindOfWeapon wep = ((Hero)src).belongings.weapon;
            if (wep instanceof MeleeWeapon & !(wep instanceof RelicMeleeWeapon)) {
                dmg = Random.Int(dmg);
            }
        } else if (src instanceof Item & !(src instanceof RelicMeleeWeapon)) {
            dmg = Random.Int(dmg);
        }
        super.damage(dmg, src);
    }
}
