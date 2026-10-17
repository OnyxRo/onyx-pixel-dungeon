package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.utils.Timer;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Overcharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannon1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannonMissileLauncher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCannonNuke;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGraveCrypt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWand1;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class AbTrOvercharge extends HeroSpell{
    private static final int TURNS_ADDED_PER_WAND = 40;

    {
        image = ItemSpriteSheet.HEROSPELL_TR_MANABOOM;
    }

    @Override
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "cost", castCooldown(), TURNS_ADDED_PER_WAND);
    }
    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof TowerWand1){
                mob.sprite.emitter().start(MagicMissile.MagicParticle.ATTRACTING, 0.02f, 30);
                Buff.affect(mob, Overcharge.class, 3);
            }
        }
    }

    @Override
    protected int castCooldown() {
        int addturns = 0;

        HashSet<Mob> mobs = new HashSet<>(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs);
        for (Mob mob : mobs) if (mob!= null && mob.isAlive() && mob.alignment != null) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (mob instanceof TowerWand1)
                    addturns += TURNS_ADDED_PER_WAND;
            }
        }
        return 100 + addturns;
    }
}
