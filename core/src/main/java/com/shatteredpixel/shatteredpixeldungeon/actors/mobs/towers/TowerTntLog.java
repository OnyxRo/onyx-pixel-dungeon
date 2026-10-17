package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.herospells.AbTrPlanB;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerTntLogSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class TowerTntLog extends TowerCWall{

    {
        spriteClass = TowerTntLogSprite.class;
        HP = HT = 175;
        cost = 400;
        upgrade1Cost = 150;
        damageMin = 10;
        damageMax = 50;
        defMin = 1;
        defMax = 2;
        upgCount = 0;
        state = PASSIVE;
    }

    @Override
    public String info() {
        StringBuilder info = new StringBuilder();
        info.append(description());
        info.append(Messages.get(this, "stats", HT , damageMin, damageMax));
        info.append(Messages.get(this, "descstats"));
        return info.toString();
    }

    @Override
    public void die(Object cause) {
        int cell;
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        for (int i : PathFinder.NEIGHBOURS25){
            cell = pos + i;
            Char ch = Char.findChar(cell);
            if (ch!=null){
                if (ch.alignment == Alignment.ALLY){
                    //friends receive 0 damage
                } else {
                    int dam = Math.round(damageRoll()) - ch.drRoll();
                    ch.damage (cause instanceof AbTrPlanB ? dam*2 : dam,this);
                    Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                    throwChar(ch, trajectory, cause instanceof AbTrPlanB ? 6 : 3, false, true, getClass());
                };//damages foes nearby and throws them away
            }
            if (level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }
        }


        super.die(cause);
    }
}
