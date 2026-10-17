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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SignalSirenSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class SignalSiren extends Mob {

    private static final int BLINK_DELAY = 5;
    private static final String SCARED_BY_STATION_CAT = "scaredByStationCat";

    private int delay = 0;
    private boolean scaredByStationCat;

    {
        spriteClass = SignalSirenSprite.class;

        HP = HT = 80;
        defenseSkill = 25;
        viewDistance = Light.DISTANCE;

        EXP = 12;
        maxLvl = 25;

        loot = Generator.Category.SCROLL;
        lootChance = 0.05f;

        flying = true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(22, 30);
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        // Resolve damage first: do not start a fall/level transition halfway through a hit.
        boolean protectedFromHit = enemy instanceof Hero && false;
        boolean hit = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        if (hit && !protectedFromHit && enemy == Dungeon.hero
                && enemy.isAlive() && Random.Int(3) == 0) {
            displaceHero();
        }
        return hit;
    }

    public static ArrayList<Integer> roomTeleportCells(Level level, Room room, int heroPos) {
        ArrayList<Integer> cells = new ArrayList<>();
        if (room == null) return cells;
        for (int y = room.top + 1; y < room.bottom; y++) {
            for (int x = room.left + 1; x < room.right; x++) {
                int cell = x + y * level.width();
                if (!level.insideMap(cell) || cell == heroPos || Actor.findChar(cell) != null) continue;
                // Deliberately unlike safe teleports: pools and fall hazards remain eligible.
                if (Dungeon.level.passable[cell] || Dungeon.level.pit[cell]
                        || level.map[cell] == Terrain.CHASM) cells.add(cell);
            }
        }
        return cells;
    }

    private void displaceHero() {
        Level level = Dungeon.level;
        if (!(level instanceof RegularLevel)) return;
        Room room = ((RegularLevel) level).room(Dungeon.hero.pos);
        ArrayList<Integer> cells = roomTeleportCells(level, room, Dungeon.hero.pos);
        if (cells.isEmpty()) return;
        Dungeon.hero.interrupt();
        GLog.w(Messages.get(this, "displaced"));
        int cell = Random.element(cells);
        boolean willFall = Dungeon.level.pit[cell] && !Dungeon.hero.flying;
        // appear() calls Hero.move(), which already presses terrain for grounded heroes.
        // Never press twice: that would trigger vents/mines or start a fall twice.
        ScrollOfTeleportation.appear(Dungeon.hero, cell);
        if (willFall || !Dungeon.hero.isAlive()) return;
        Dungeon.observe();
        GameScene.updateFog();
    }

    @Override
    protected boolean getCloser(int target) {
        if (Dungeon.level.heroFOV[target] && Dungeon.level.distance(pos, target) > 2 && delay <= 0) {

            blink(target);
            spend(-1 / speed());
            return true;

        } else {

            delay--;
            return super.getCloser(target);

        }
    }

    private void blink(int target) {

        Ballistica route = new Ballistica(pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar(cell) != null && cell != this.pos)
            cell = route.path.get(route.dist - 1);

        if (Dungeon.level.avoid[cell]) {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                delay = BLINK_DELAY;
                return;
            }
        }

        ScrollOfTeleportation.appear(this, cell);

        delay = BLINK_DELAY;
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    public boolean scareByStationCat(int catId) {
        if (scaredByStationCat || buff(Terror.class) != null) {
            return false;
        }

        scaredByStationCat = true;
        Buff.affect(this, Terror.class, Terror.DURATION).object = catId;
        return true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SCARED_BY_STATION_CAT, scaredByStationCat);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        flying = true; // Older saved Sirens were ground-based.
        scaredByStationCat = bundle.getBoolean(SCARED_BY_STATION_CAT);
    }

    {
        resistances.add(Vampiric.class);
    }

    {
        immunities.add(Sleep.class);
    }
}
