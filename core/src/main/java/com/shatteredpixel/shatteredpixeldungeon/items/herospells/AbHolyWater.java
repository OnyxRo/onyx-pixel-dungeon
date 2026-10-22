package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class AbHolyWater extends HeroSpellTargeted{
    {
        image = ItemSpriteSheet.HEROSPELL_HOLY_WATER;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    Ballistica bolt = new Ballistica(Dungeon.hero.pos, cell,Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
                    Dungeon.hero.busy();
                    fx(bolt);
                    cooldown();
                }
            }

            @Override
            public String prompt () {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }

    ConeAOE cone;

    public void onZap(Ballistica bolt) {

        ArrayList<Char> affectedChars = new ArrayList<>();
        ArrayList<Integer> adjacentCells = new ArrayList<>();
        for( int cell : cone.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //knock doors open
            if (Dungeon.level.map[cell] == Terrain.DOOR){
                Level.set(cell, Terrain.OPEN_DOOR);
                GameScene.updateMap(cell);
            }

            if (Dungeon.level.adjacent(bolt.sourcePos, cell)){
                adjacentCells.add(cell);
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        //if wand was shot right at a wall
        if (cone.cells.isEmpty()){
            adjacentCells.add(bolt.sourcePos);
        }

        for ( Char ch : affectedChars ){
            if (ch.properties().contains(Char.Property.UNDEAD)||ch.properties().contains(Char.Property.DEMONIC)) {
                ch.damage(10 + Dungeon.hero.lvl, this);
                Buff.affect(ch, Blindness.class, 15);
                CellEmitter.floor(ch.pos).start(ShadowParticle.UP,0.1f,10);
            } else if (ch.alignment == Char.Alignment.ALLY){
                ch.heal(5 + Dungeon.hero.lvl);
            } else {
                ch.damage(5 + Dungeon.hero.lvl, this);
            }
        }
    }


    public void fx(Ballistica bolt) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.

        // 3 distance
        int maxDist = 6;
        int dist = Math.min(bolt.dist, maxDist);

        cone = new ConeAOE( bolt,
                maxDist,
                50,
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID );

        //cast to cells at the tip, rather than all cells, better performance.
        for (Ballistica ray : cone.outerRays){
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FROST_CONE,
                    curUser.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        Callback callback = new Callback() {
            @Override
            public void call() {
                onZap(bolt);
                Dungeon.hero.spendAndNext(1f);
            }
        };
        //final zap at half distance, for timing of the actual wand effect
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FROST,
                curUser.sprite,
                bolt.path.get(dist/2),
                callback );
        Sample.INSTANCE.play( Assets.Sounds.SHATTER );
        Sample.INSTANCE.play( Assets.Sounds.WATER );

    }



    @Override
    protected int castCooldown() {
        return 30;
    }
}
