package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Drawing extends RelicEnchantment {
    private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage ) {

        //heals for up to 30% of damage dealt, based on missing HP, ultimately normally distributed
        float 	missingPercent = (attacker.HT - attacker.HP) / (float)attacker.HT,
                maxHeal = (.025f + missingPercent * .125f) * 2, // min max heal is .025%, consistent with shattered.
                healPercent = 0;
        int tries = 1 + weapon.level()/5;
        do {
            healPercent = Math.max(healPercent, Random.NormalFloat(0,maxHeal));
        } while(tries-- > 0);
        ArrayList<Char> toHeal = new ArrayList<>();
        toHeal.add(attacker);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                toHeal.add(mob);
            }
        }
        for (Char ch : toHeal) {
            int healAmt = Math.min(Math.round(healPercent * damage), ch.HT - ch.HP);

            if (healAmt > 0 && ch.isAlive()) {
                ch.HP += healAmt;
                if (attacker.fieldOfView[ch.pos]) {
                    ch.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
                    ch.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healAmt));
                }
            }
        }

        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon, owner);
        owner.damage(owner.HP/3,this);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (owner.fieldOfView[mob.pos]) {
                    mob.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
                    mob.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(mob.HT - mob.HP));
                }
                mob.HP = mob.HT;
                Buff.prolong(mob, Adrenaline.class, Adrenaline.DURATION);
            } else {
                Buff.prolong(mob, Blindness.class, 10f);
            }
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}