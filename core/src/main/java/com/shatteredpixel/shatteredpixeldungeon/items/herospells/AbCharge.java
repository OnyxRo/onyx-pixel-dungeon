package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class AbCharge extends HeroSpellTargeted {
    {
        image = ItemSpriteSheet.HEROSPELL_CHARGE;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell!=null) {
                    if (Char.findChar(cell) != null) {

                        int target = cell;
                        Hero hero = Dungeon.hero;

                        Ballistica route = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                        int finalcell = route.collisionPos;

                        int backTrace = route.dist - 1;
                        while (Actor.findChar(finalcell) != null && finalcell != hero.pos) {
                            finalcell = route.path.get(backTrace);
                            backTrace--;
                        }
                        final int dest = finalcell;
                        hero.busy();
                        int finalCell = finalcell;
                        hero.sprite.jump(hero.pos, finalcell, 0f, 0.2f, new Callback() {
                            @Override
                            public void call() {
                                hero.move(dest);
                                Dungeon.level.occupyCell(hero);
                                Dungeon.observe();
                                GameScene.updateFog();

                                WandOfBlastWave.BlastWave.blast(dest);
                                for (int i : PathFinder.NEIGHBOURS8) {
                                    Char ch = Actor.findChar(dest + i);

                                    if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                                        if (ch.pos == dest + i) {
                                            Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                                            int strength = 1 + Math.round(buffedLvl() / 2f);
                                            throwChar(ch, trajectory, strength, false, true, getClass());
                                        }

                                    }
                                }
                                Camera.main.shake(2, 0.5f);

                                Char ch = Char.findChar(finalCell);

                                if (ch != null)
                                    Buff.affect(ch, Vertigo.class, ch.properties().contains(Char.Property.BOSS) ? 2 : Math.max(2, Math.min(10, 10 * (hero.HT / ch.HT))));

                                Invisibility.dispel();
                                hero.spendAndNext(Actor.TICK);

                            }
                        });
                    }
                    Dungeon.gold -= castCooldown();
                    updateQuickslot();
                    Dungeon.hero.spendAndNext(1f);
                }
            }
            @Override
            public String prompt() {
                return "Choose a cell target";
            }
        };
    }



    @Override
    protected int castCooldown() {
        return 30+Dungeon.depth*9;
    }
}
