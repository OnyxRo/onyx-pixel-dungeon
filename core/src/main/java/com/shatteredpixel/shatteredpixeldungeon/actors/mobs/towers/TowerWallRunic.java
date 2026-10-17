package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.actors.DamageSource;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.walls.TowerWallObsidianSprite;

public class TowerWallRunic extends TowerCWall{

    {
        spriteClass = TowerWallObsidianSprite.class;
        HP = HT = 1000;
        cost = 1400;

        immunities.add(Burning.class);
        immunities.add(Vulnerable.class);
        immunities.add(Ooze.class);

        damageMin = 0;
        damageMax = 0;
        defMin = 0;
        defMax = 0;
        upgCount = 0;

        state = PASSIVE;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (DamageSource.MAGICAL.contains(src.getClass())){
            super.damage(dmg/2,src);
        } else super.damage(dmg, src);
    }

    @Override
    public CharSprite sprite() { // changes the icon in the mob info window
        TowerWallObsidianSprite sprite = (TowerWallObsidianSprite) super.sprite();
        if (HP>=HT*0.6f) {
            sprite.idle();
        }
        if (HP<HT*0.6f) {
            sprite.cracked();
        }

        if (HP<HT*0.3f) {
            sprite.broken();
        }

        return sprite;
    }

}
