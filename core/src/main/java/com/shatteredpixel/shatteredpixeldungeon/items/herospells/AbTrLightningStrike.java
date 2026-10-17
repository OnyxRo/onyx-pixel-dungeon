package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbilityCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGraveCrypt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerLightning1;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class AbTrLightningStrike extends HeroSpellTargeted {
    private static int MIN_COST = 10;

    private static int mindam(){
        if (hero == null) return 7;
        return hero.lvl  + 5;
    }
    private static int maxdam(){
        if (hero == null) return 15;
        return hero.lvl * 3 + 9;
    }

    {
        image = ItemSpriteSheet.HEROSPELL_TR_ORBITALLIGHTNING;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    ArrayList<Lightning.Arc> arcs = new ArrayList<>();
                    PointF source = DungeonTilemap.tileCenterToWorld(cell);
                    source.y-=200;
                    PointF dest = Char.findChar(cell)== null ? DungeonTilemap.raisedTileCenterToWorld(cell) : DungeonTilemap.tileCenterToWorld(cell);
                    Sample.INSTANCE.play(Assets.Sounds.LIGHTNING, 2f, 0.8f);
                    arcs.add(new Lightning.Arc(source, dest));
                    Dungeon.hero.sprite.parent.addToFront(new Lightning(arcs, null));
                    WandOfBlastWave.BlastWave.blast(cell);
                    Char ch = Char.findChar(cell);
                    if (ch!=null){
                        ch.damage(Random.Int(mindam(), maxdam()), AbTrLightningStrike.this);
                        Buff.affect(ch, Paralysis.class, 2);
                    }
                    cooldown();
                }

            }

            @Override
            public String prompt () {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }

    @Override
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "damage", mindam(), maxdam()) + "\n\n" + Messages.get(this, "cost", castCooldown(), MIN_COST);
    }

    @Override
    protected int castCooldown() {
        int lightcost = 0;


        HashSet<Mob> mobs = new HashSet<>(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs);
        for (Mob mob : mobs) if (mob!= null && mob.isAlive() && mob.alignment != null) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (mob instanceof TowerLightning1)
                    lightcost += ((Tower)mob).cost;
            }
        }
        return (int)Math.max(100 - Math.sqrt(lightcost), 10);
    }
}
