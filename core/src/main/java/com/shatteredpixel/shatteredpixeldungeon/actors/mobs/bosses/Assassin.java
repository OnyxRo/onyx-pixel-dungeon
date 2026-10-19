/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.sprites.deep.*;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SpearTrap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Assassin extends Mob {

    {
        spriteClass = AssassinSprite.class;

        HP = HT = 300;
        defenseSkill = 32;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(28, 50);
    }

    @Override
    public int attackSkill(Char target) {
        return 35;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }


    @Override
    public void move( int step, boolean travelling ) {
        Dungeon.level.seal();
        super.move( step, travelling );
    }

    @Override
    public void damage(int dmg, Object src) {

        int beforeHitHP = HP;
        super.damage(dmg, src);
        dmg = beforeHitHP - HP;

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = beforeHitHP > HT/2 ? 1 : 4;
            lock.addTime(dmg*multiple);
        }

        int hpBracket = beforeHitHP > HT/2 ? 12 : 20;

        //phase 1 of the fight is over
        if (beforeHitHP > HT/2 && HP <= HT/2){
            HP = (HT/2)-1;
            BossHealthBar.bleed(true);

            //if tengu has lost a certain amount of hp, jump
        } else if (beforeHitHP / hpBracket != HP / hpBracket) {
            jump();
        }

        boolean bleeding = (HP * 2 <= HT);
        super.damage(dmg, src);
        if ((HP * 2 <= HT) && !bleeding) {
            BossHealthBar.bleed(true);
        }

    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        Dungeon.level.unseal();

        GameScene.bossSlain();
        Dungeon.level.drop(new WornKey(Dungeon.depth), pos).sprite.drop();

        Badges.validateBossSlain();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(5);
        }
        return damage;
    }

    private void jump() {

        for (int i = 0; i < 4; i++) {
            int trapPos;
            //a bounded search, a blind hero sees no cell and the old loop never ended
            int tries = 0;
            do {
                trapPos = Random.Int(Dungeon.level.length());
            } while ((!Dungeon.level.heroFOV[trapPos] || Dungeon.level.solid[trapPos]) && ++tries < 500);
            if (tries >= 500) break;

            if (Dungeon.level.map[trapPos] == Terrain.EMPTY) {
                Dungeon.level.setTrap(new SpearTrap().reveal(), trapPos);
                Level.set(trapPos, Terrain.TRAP);
                ScrollOfMagicMapping.discover(trapPos);
            }
        }

        if (enemy == null) enemy = chooseEnemy();

        int newPos;
        //if we're in phase 1, want to warp around within the room
        if (HP > 0) {
            do {
                newPos = Random.Int(Dungeon.level.length());
            } while (
                    !(Dungeon.level.map[newPos] == Terrain.EMPTY || Dungeon.level.map[newPos] == Terrain.EMPTY) ||
                            Dungeon.level.solid[newPos] ||
                            Dungeon.level.adjacent(newPos, enemy.pos) ||
                            Actor.findChar(newPos) != null);

            //otherwise go wherever, as long as it's a little bit away
        } else {
            do {
                newPos = Random.Int(Dungeon.level.length());
            } while (
                    Dungeon.level.solid[newPos] ||
                            Dungeon.level.distance(newPos, enemy.pos) < 8 ||
                            Actor.findChar(newPos) != null);
        }
        sprite.move( pos, newPos );
        move( newPos );

        if (Dungeon.level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
        Sample.INSTANCE.play( Assets.Sounds.PUFF );

        spend( 1 / speed() );

    }
}
