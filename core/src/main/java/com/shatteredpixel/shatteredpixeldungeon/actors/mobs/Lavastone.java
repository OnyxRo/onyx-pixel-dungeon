/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SheepSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Lavastone extends Mob {

    {
        spriteClass = SheepSprite.class;

        HP = HT = 100;
        EXP = 0;
        maxLvl = -2;

        properties.add(Property.LARGE);
        properties.add(Property.MINIBOSS);

        alignment = Alignment.NEUTRAL;
        state = PASSIVE;
    }



    public float lifespan;

   private boolean initialized = false;

    @Override
    protected boolean act() {
        if (initialized) {
            HP = 0;
            die(null);
            sprite.die();

        } else {
            initialized = true;
            spend( lifespan + 3);
        }
        return true;
    }


    @Override
    public boolean add( Buff buff ) { return false; }

    @Override
    public boolean interact(Char c) {
        if (c == Dungeon.hero) {
            Dungeon.hero.spendAndNext(1f);
            Sample.INSTANCE.play(Assets.Sounds.SHEEP, 1, Random.Float(0.91f, 1.1f));
        }
        return true;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (cause == Chasm.class) return;

        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
            if (ch != null && ch.isAlive()) {
                int damage = Random.NormalIntRange(5 + Dungeon.depth, 10 + Dungeon.depth*2);
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, this );
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (!Dungeon.level.pit[pos]) {
            for (int a : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[pos + a] && !Dungeon.level.water[pos + a]) {
                    GameScene.add(Blob.seed(pos + a, 5, Fire.class));
                }
            }


        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BLAST );

        }

        if (heroKilled) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.get(this, "explo_kill") );
        }
    }

        immunities.add( Terror.class );
        immunities.add( Charm.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Burning.class );
        immunities.add( Weakness.class );
        immunities.add( Blindness.class );
        immunities.add( Bleeding.class );


}}