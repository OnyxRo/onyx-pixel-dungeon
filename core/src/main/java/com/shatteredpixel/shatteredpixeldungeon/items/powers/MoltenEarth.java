package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class MoltenEarth extends ActivatedPower {

    {
        image = ItemSpriteSheet.MOLTENEARTH;
        mp_cost = 3;
    }

    @Override
    public boolean usesTargeting() {
        return false;
    }

    @Override
    public void affectCell(int pos) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                Buff.affect( mob, Burning.class ).reignite( mob );
                Buff.prolong( mob, Roots.class, 3 );
            }
        }

        curUser.sprite.centerEmitter().start( ElmoParticle.FACTORY, 0.15f, 4 );
        Sample.INSTANCE.play( Assets.Sounds.READ );
    }
}
