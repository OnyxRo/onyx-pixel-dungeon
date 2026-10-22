package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Protected;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCSpawning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCWall;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTntLog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerTotem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AbEgoist extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_EGOIST;
    }

    @Override
    public void cast() {
        super.cast();
        ArrayList<Mob> mobsaffected = new ArrayList<>();
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Tower
                    && !(mob instanceof TowerCWall)
                    && !(mob instanceof TowerTntLog)
                    && !(mob instanceof TowerTotem)
                    && !(mob instanceof TowerCSpawning)
                    && mob.alignment == Char.Alignment.ALLY){
                mobsaffected.add(mob);
            }
        }
        for (Mob mob : mobsaffected) {
           Buff.affect(mob, Hex.class, 100);
           Buff.affect(Dungeon.hero, Random.oneOf(Bless.class, Stamina.class, Protected.class, Strength.class, Battlecry.class, Invisibility.class, MindVision.class, Levitation.class, Foresight.class ), 50);
        }
        Buff.affect(Dungeon.hero, Healing.class).setHeal(1+ mobsaffected.size()*20, 0.05f,5);
        Sample.INSTANCE.play(Assets.Sounds.CURSED);
        cooldown();
    }

    @Override
    protected int castCooldown() {
        return 100;
    }
}
