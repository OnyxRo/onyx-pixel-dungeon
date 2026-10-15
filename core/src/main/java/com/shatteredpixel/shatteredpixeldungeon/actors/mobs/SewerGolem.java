/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SpawnerGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.AmuletSectorWater;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SewerGolemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SewerGolem extends Mob implements Callback  {
    private static final float TIME_TO_ZAP	= 2f;
    {
        spriteClass = SewerGolemSprite.class;

        HP = HT = 2000;
        defenseSkill = 30;

        EXP = 22;

        loot = Generator.random(Generator.Category.WEAPON);
        lootChance = 1f;

        baseSpeed = 1.5f;//Faster than the hero
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        resistances.add(ScrollOfRetribution.class);
        immunities.add(ScrollOfPsionicBlast.class);
        immunities.add(Grim.class);
    }

    public static class LightningBolt{}

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( pos, enemy.pos ) <= 1) {

            return super.doAttack( enemy );

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap( enemy.pos );
            }

            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                int dmg = Random.NormalIntRange(20, 60);
                if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 2f;
                }
                enemy.damage( dmg, new LightningBolt() );

                enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake( 2, 0.3f );

                    if (!enemy.isAlive()) {
                        Dungeon.fail( getClass() );
                        GLog.n( Messages.get(this, "zap_kill") );
                    }
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            return !visible;
        }
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    @Override
    public void damage( int dmg, Object src ) {
        this.notice();
        if (Random.Int(2) == 1 & dmg > HT/100f) {
            GameScene.add(Blob.seed(Dungeon.hero.pos, 300, SpawnerGas.class));
            int newPos = -1;
            for (int i = 0; i < 20; i++) {
                newPos = Dungeon.level.randomRespawnCell( null );
                if (newPos != -1) {
                    break;
                }
            }
            if (newPos != -1) {
                CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                pos = newPos;
                sprite.place(pos);
                sprite.visible = Dungeon.hero.fieldOfView[pos];
            }
        }
        if (src instanceof Wand & !(src instanceof WandOfPrismaticLight)) {
            dmg = Random.Int(dmg);
        }
        boolean bleeding = (HP*2 <= HT);
        super.damage(dmg, src);
        if ((HP*2 <= HT) && !bleeding){
            BossHealthBar.bleed(true);
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 40, 85);
    }

    @Override
    public int attackSkill( Char target ) {
        return 36;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        sprite.zap( enemy.pos );
        enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
        enemy.sprite.flash();
        if (Random.Int( 2 ) == 0) {
            Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
        }

        return damage;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        GameScene.bossSlain();
        //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new ScrollOfUpgrade(), pos + ofs ).sprite.drop( pos );
        }
        Dungeon.level.drop(new PotionOfStrength(), pos);

        Dungeon.level.drop(new AmuletSectorWater(), pos).sprite.drop(pos);

        Badges.validateBossSlain();

        yell( Messages.get(this, "defeated") );
    }



    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }


    @Override
    public void call() {
        next();
    }
}
