package com.shatteredpixel.shatteredpixeldungeon.items.quest;


import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChaosTime;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DreadPlague;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewTengu;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;


public class MysteryHeart extends Item {

    public static int ritualPos;

    {
        image = ItemSpriteSheet.ARTIFACT_CAPE;

        defaultAction = AC_THROW;

        unique = true;
        stackable = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public void doDrop(Hero hero) {
        super.doDrop(hero);
        checkHearts();
    }

    @Override
    protected void onThrow(int cell) {
        super.onThrow(cell);
        checkHearts();
    }

    private static void checkHearts(){
        Heap heapTop = Dungeon.level.heaps.get(ritualPos - Dungeon.level.width());
        Heap heapRight = Dungeon.level.heaps.get(ritualPos + 1);
        Heap heapBottom = Dungeon.level.heaps.get(ritualPos + Dungeon.level.width());
        Heap heapLeft = Dungeon.level.heaps.get(ritualPos - 1);

        if (heapTop != null &&
                heapRight != null &&
                heapBottom != null &&
                heapLeft != null){

            if (heapTop.peek() instanceof MysteryHeart &&
                    heapRight.peek() instanceof MysteryHeart &&
                    heapBottom.peek() instanceof MysteryHeart &&
                    heapLeft.peek() instanceof MysteryHeart){

                heapTop.pickUp();
                heapRight.pickUp();
                heapBottom.pickUp();
                heapLeft.pickUp();


                for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof NewTengu) {
                        ((NewTengu) boss).canInven = false;
                        boss.yell( Messages.get(boss, "ritsual"));
                        Buff.affect(boss, Paralysis.class,100f);
                        Buff.affect(boss, DreadPlague.class).set(2f);
                        Buff.affect(boss, Poison.class).set(10f);
                        Buff.affect(boss, Bleeding.class).set(5f);
                        Buff.affect(boss, Slow.class,1f);
                        Buff.affect(boss, Weakness.class,100f);
                        Buff.affect(boss, Doom.class);
                        Char ch = Actor.findChar( ritualPos );
                        if (ch != null) {
                            ArrayList<Integer> candidates = new ArrayList<>();
                            for (int n : PathFinder.NEIGHBOURS8) {
                                int cell = ritualPos + n;
                                if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Actor.findChar( cell ) == null) {
                                    candidates.add( cell );
                                }
                            }
                            if (candidates.size() > 0) {
                                boss.pos = Random.element( candidates );
                            } else {
                                boss.pos = ritualPos;
                            }
                        } else {
                            boss.pos = ritualPos;
                        }
                        boss.sprite.place(boss.pos);
                    }
                }

                for (int i : PathFinder.NEIGHBOURS9){
                    CellEmitter.get(ritualPos+i).burst(ElmoParticle.FACTORY, 10);
                }
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
            }
        }

    }
}