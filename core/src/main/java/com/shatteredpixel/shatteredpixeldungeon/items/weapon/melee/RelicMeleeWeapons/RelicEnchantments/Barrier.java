package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicEnchantments;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.LoturgosCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RelicMeleeWeapons.RelicMeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Barrier extends RelicEnchantment {

    private static final ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );

    private RelicMeleeWeapon weapon = null;

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {//Practically identical to Blocking, but it's function is different.

        int level = Math.max( 0, weapon.level() );

        Buff.prolong(attacker, BarrierBuff.class, 2 + level/2).setBlocking(level + 1);

        this.weapon = weapon;

        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        this.weapon = weapon;
        GameScene.selectCell(zap);
    }

    public int damageRoll() {
        return weapon.damageRoll(Dungeon.hero)*2;
    }

    private void zapFX(final int cell) {
        MagicMissile.boltFromChar(Dungeon.hero.sprite.parent,
                MagicMissile.MAGIC_MISSILE,
                Dungeon.hero.sprite,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        Char enemy;
                        enemy = Actor.findChar(cell);
                        if ( enemy != null ) {
                            enemy.damage(damageRoll(),Barrier.class);
                            int chance = Random.Int(3);
                            switch (chance) {
                                case 0: default:
                                    Buff.affect(enemy, Burning.class).reignite(enemy);
                                    break;
                                case 1:
                                    Buff.affect(enemy, Paralysis.class, Paralysis.DURATION);
                                    break;
                                case 2:
                                    Buff.affect(enemy, Vertigo.class, Vertigo.DURATION);
                                    break;
                            }
                        }
                    }
                });
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    private CellSelector.Listener zap = new  CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            Char enemy;
            KindOfWeapon cry = Dungeon.hero.belongings.weapon;
            if (target != null && cry instanceof LoturgosCrystal) {
                int cell = target;
                LoturgosCrystal Crystal = (LoturgosCrystal) cry;
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));
                enemy = Actor.findChar(cell);
                if (enemy != null) {
                    final Ballistica shot = new Ballistica( Dungeon.hero.pos, target, Ballistica.PROJECTILE);
                    if (shot.collisionPos == enemy.pos) {
                        zapFX(enemy.pos);
                        Barrier.super.activate(Crystal,Dungeon.hero);
                    } else {
                        GLog.n( Messages.get( Barrier.class, "miss_target") );
                    }
                } else {
                    GLog.w( Messages.get( Barrier.class, "no_enemy") );
                }
            } else {
                GLog.w( Messages.get( Barrier.class, "not_equipped"));
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Damning.class, "prompt");
        }
    };

    public static class BarrierBuff extends FlavourBuff {

        private int blocking = 0;

        public void setBlocking( int blocking ){
            this.blocking = blocking;
        }

        public int blockingRoll(){
            return Random.NormalIntRange(0, blocking);
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.tint(0, 1f, 1.5f, 0.5f);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", blocking, dispTurns());
        }

        private static final String BLOCKING = "blocking";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BLOCKING, blocking);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            blocking = bundle.getInt(BLOCKING);
        }

    }
}
