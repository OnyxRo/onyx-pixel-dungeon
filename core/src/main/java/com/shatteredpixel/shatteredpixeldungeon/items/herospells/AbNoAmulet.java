package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class AbNoAmulet extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_NOAMULET;
    }

    @Override
    public void cast() {
        super.cast();
        int foesAround = 0;

        Mob amuletto = ((Arena)(Dungeon.level)).amuletTower;
        for (int i : PathFinder.NEIGHBOURS25){
            if (Char.findChar(amuletto.pos + i)!=null && Char.findChar(amuletto.pos + i).alignment == Char.Alignment.ENEMY){
                foesAround++;
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.PUFF);
        Buff.affect(amuletto, Invisibility.class, Math.max(3, 10-foesAround*2));

    }

    @Override
    protected int castCooldown() {
        return 100;
    }
}
