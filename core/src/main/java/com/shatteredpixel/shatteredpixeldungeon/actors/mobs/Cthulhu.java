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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.FinalLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Cthulhu extends Mob {

    {
        spriteClass = RatSprite.class;

        HP = HT = 999999;
        defenseSkill = 99999;

        flying = true;

        maxLvl = -999;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);
        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.LARGE);

    }

    private static int skillcooldown = 20;
    final int WIDTH = FinalLevel.getWidth();
    private static boolean fireSkill = false;
    private static boolean waterSkill = false;
    private static boolean groundSkill = false;
    private static boolean windSkill = false;
    private static boolean chronosSkill = false;
    private static boolean heavenSkill = false;
    private static boolean phantomSkill = false;

    @Override
    protected boolean act() {
        return super.act();
    }

    public void flamegeuse(){

        Dungeon.hero.interrupt();
        GLog.w(Messages.get( this, "message") );

        final int flameCenter;
        flameCenter = WIDTH*21+20;

        Actor a = new Actor() {

            {
                actPriority = HERO_PRIO+1;
            }

            @Override
            protected boolean act() {

                PathFinder.buildDistanceMap( flameCenter, BArray.not( Dungeon.level.solid, null ), 8 );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    int pos = i;
                    if (PathFinder.distance[i] <= 8 && Dungeon.level.insideMap(i)) {
                        if (!Dungeon.level.solid[pos] && Dungeon.level.flamable[pos]) {

                            sprite.parent.add(TargetedCell.colored(pos, 0xFF0000));

                            Dungeon.level.destroy(pos);
                            GameScene.updateMap(pos);
                            Dungeon.observe();


                            GameScene.add(Blob.seed(pos, 1, Fire.class));
                        }
                        else {

                            sprite.parent.add(TargetedCell.colored(pos, 0xFF0000));

                            GameScene.add(Blob.seed(pos, 1, Fire.class));
                        }
                    }
                }
              
                Actor.remove(this);
                return true;
            }
        };

    }





}
