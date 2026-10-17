package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Animated;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCannonNukeSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TowerCannonNuke extends TowerCannon1{

    {
        HP = HT = 400;
        spriteClass = TowerCannonNukeSprite.class;

        attackRange = 12;//DPT =32.5*0.66 = 21.45;DPT/C=21,45:1150=0.0187
        baseAttackDelay = 25f;

        upgCount = 0;

        cost = 2800;
        upgrade1Cost = 1500;
        damageMin = 20;
        damageMax = 35;

    }
    public float damageExplosionMult = 1f;

    @Override
    public int attackSkill(Char target) {//Nope, no dodging an explosion at its epicentre((
        return 1000000;
    }

    @Override
    public void boom(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.BLAST, 1.2f, 0.7f);
        Sample.INSTANCE.play(Assets.Sounds.BURNING, 1f, 0.3f);
        Sample.INSTANCE.play(Assets.Sounds.BURNING, 1f, 0.8f);
        Sample.INSTANCE.play(Assets.Sounds.BURNING, 1f, 1f);
        for (int i : PathFinder.NEIGHBOURS25) for (int i2 : PathFinder.NEIGHBOURS4){
            int cell2 = cell + i + i2;
            if (!level.cellAdjacentToBorderCells(cell2)){
                Char ch = Actor.findChar(cell2);
                if (ch!=null){
                    if (ch.alignment == Alignment.ALLY){
                     //to not double-damage the foe
                    } else {
                        Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i + i2, Ballistica.MAGIC_BOLT);
                        throwChar(ch, trajectory, 3, false, true, getClass());
                        int drroll = ch.buff(PotionOfCleansing.Cleanse.class)==null ? ch.drRoll() : 0;
                        ch.damage(Math.round(damageRoll() * damageExplosionMult) - drroll, this);//damages foes nearby, with lowered damage
                    }



                }

                CellEmitter.floor(cell2).burst(SacrificialParticle.FACTORY, Random.Int(1,3));


                if (Dungeon.level.flamable[cell2]) {//affects terrain
                    Dungeon.level.destroy(cell2);
                    GameScene.updateMap(cell2);
                }
                if (Dungeon.level.map[cell2]== Terrain.EMPTY){
                    if (Math.random()<0.5) {
                        Level.set(cell2, Terrain.EMBERS);
                        GameScene.updateMap(cell2);
                    };
                }
                Heap heap = Dungeon.level.heaps.get(cell2);//explodes bombs and affects heaps nearby
                if (heap != null) heap.explode();
            }
        }
        GameScene.updateFog();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (buff(Animated.class)!=null && (Dungeon.level.distance( pos, Dungeon.hero.pos )>2)) return false;
        return (new Ballistica( pos, enemy.pos, Ballistica.TARGETING_BOLT).collisionPos == enemy.pos && level.distance(enemy.pos, this.pos)<=attackRange);
    }

}