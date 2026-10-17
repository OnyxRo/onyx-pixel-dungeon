package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkullDemonicSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfDemonicSkull extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_LULLABY;
        image = ItemSpriteSheet.EXOTIC_DEMONICSKULLS;
    }

    @Override
    public void doRead() {


        ArrayList<Integer> respawnPoints = new ArrayList<>();
        for (int iz = 0; iz < PathFinder.NEIGHBOURS8.length; iz++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[iz];
            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                respawnPoints.add(p);
            }
        }
        if (!respawnPoints.isEmpty()) {
            ExplosiveDemonicSkull skully = new ExplosiveDemonicSkull();
            skully.pos = Random.element(respawnPoints);
            CellEmitter.get(skully.pos).burst(ElmoParticle.FACTORY, 20);
            skully.state = skully.HUNTING;
            GameScene.add(skully);
        }

        Sample.INSTANCE.play(Assets.Sounds.BONES);

        readAnimation();

    }

    @Override
    public int value() {
        return 100 * quantity;
    }

    public static class ExplosiveDemonicSkull extends Mob {
        {
            HP = HT = 100;
            alignment = Alignment.ALLY;
            spriteClass = SkullDemonicSprite.class;

            defenseSkill = 50;

            viewDistance = 8;

            flying = true;

        }

        @Override
        protected boolean act() {
            beckon(hero.pos);
            return super.act();
        }

        @Override
        public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
            die(Hero.class);
            return super.attack(enemy, dmgMulti, dmgBonus, accMulti);
        }

        @Override
        public void die(Object cause) {
            int cell;
            GameScene.flash(0x00ff00);
            Sample.INSTANCE.play(Assets.Sounds.BLAST, 1f,0.5f);
            Sample.INSTANCE.play(Assets.Sounds.ALERT, 0.5f,1f);
            super.die(cause);
            for (int x = 0; x < level.width(); x++)
                for (int y = 0; y < level.height(); y++) {
                    cell = x + level.width() * y;
                    if (((pos%level.width() - x)*(pos%level.width() - x)) + ((pos/level.width() - y)*(pos/level.width() - y))<=36) {
                        Char ch = Char.findChar(cell);
                        if (ch != null) {
                            ch.damage(Math.round(damageRoll()) - ch.drRoll(), this);
                            Buff.affect(ch, Hex.class, 15);
                            Buff.affect(ch, Poison.class).set(12);
                        }
                        if (level.heroFOV[cell]) {
                            CellEmitter.center(cell).burst(PurpleParticle.BURST, 5);
                            CellEmitter.floor(cell).start(ElmoParticle.FACTORY, 0.2f, 10);
                        }
                    }
                }
        }

        @Override
        public int attackSkill(Char target) {
            return 123456;
        }

        @Override
        public int damageRoll() {
            return Random.Int(hero.lvl * 5 + 15, hero.lvl * 10 + 25);
        }
    }
}