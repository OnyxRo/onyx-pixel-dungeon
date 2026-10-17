package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCWall;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerRatCamp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTntLog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;

import java.util.HashSet;

public class AbTrHyperats extends HeroSpell{

    {
        image = ItemSpriteSheet.HEROSPELL_TR_CHAMP;
    }

    private static final int TURNS_PER_RATCAMP = 20;

    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof TowerRatCamp){
                ((TowerRatCamp)mob).prepChamps();
            }
        }
    }
    @Override
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "cost", castCooldown(), TURNS_PER_RATCAMP);
    }

    @Override
    protected int castCooldown() {
        if (DeviceCompat.isDebug()) return 0;
        int addturns = 0;

        HashSet<Mob> mobs = new HashSet<>(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs);
        for (Mob mob : mobs) if (mob!= null && mob.isAlive() && mob.alignment != null) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (mob instanceof TowerRatCamp)
                    addturns += TURNS_PER_RATCAMP + ((Arena)Dungeon.level).waveCooldownBoss/3;
            }
        }
        return 100 + addturns;
    }
}
