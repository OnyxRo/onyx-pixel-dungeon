package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulBleeding;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ObeliskBloodstoneSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ObeliskNecroticSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class ObeliskNecrotic extends Obelisk{
    {
        HP = HT = 15;
        spriteClass = ObeliskNecroticSprite.class;

        attackRange = 8;
        baseAttackDelay = 2f;

        cost = 0;
        upgrade1Cost = 0;
        damageMin = 1;
        damageMax = 2;
        upgCount = 0;
        sellable = false;
        if (Dungeon.hero != null){
            HP = HT = Dungeon.depth * 5 + 15;
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 1000000;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        for (int i : PathFinder.NEIGHBOURS25){
            int cell = i + pos;
            CellEmitter.floor(cell).start(Speck.factory(Speck.TOXIC), (float)(Math.random()* 0.3f), 3);
            if (Char.findChar(cell)!= null && Char.findChar(cell).alignment==Alignment.ENEMY) Buff.affect(Char.findChar(cell), Poison.class).extend(Math.round( Math.random() * (1 + Math.sqrt(Dungeon.depth))));
        }
        Buff.affect(enemy, Poison.class).extend(1);
        return super.attackProc(enemy, damage);
    }
    @Override
    public void hitSound(float pitch) {
        Sample.INSTANCE.play(Assets.Sounds.GRASS, 1, pitch);
    }

    @Override
    protected boolean canAttack( Char enemy ) {//does not attack close foes in melee
        return new Ballistica( pos, enemy.pos, Ballistica.TARGETING_BOLT).collisionPos == enemy.pos && Dungeon.level.distance(enemy.pos, this.pos)<=attackRange;
    }

}
