package com.shatteredpixel.shatteredpixeldungeon.items.towerspawners;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.SentientTower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWall1;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena12;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena14;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena22;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

public abstract class TowerSpawner extends Item {
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public Tower instanceOfTower = new TowerWall1();

    @Override
    public String desc() {
        return instanceOfTower.info();
    }

    @Override
    protected void onThrow( int cell ) {
        Char enemy = Actor.findChar( cell );
        boolean towerhere = false;
        if (Dungeon.level instanceof Arena12 && Dungeon.level.mode== WndModes.Modes.CHALLENGE){

            for (int i : PathFinder.NEIGHBOURS25){
                if (Char.findChar(cell + i) != null && Char.findChar(cell + i) instanceof Tower){
                    towerhere = true;
                }
            }
        }
        if (Dungeon.level instanceof Arena14 && Dungeon.level.mode== WndModes.Modes.CHALLENGE){

            for (int i : PathFinder.NEIGHBOURS4){
                if (Char.findChar(cell + i) != null && Char.findChar(cell + i) instanceof Tower){
                    towerhere = true;
                }
            }
        }
        if (enemy == null && !towerhere) {
            Tower tower = instanceOfTower;
            tower.pos = cell;
            GameScene.add(tower);
            if (Dungeon.level instanceof Arena22 && Dungeon.level.mode == WndModes.Modes.CHALLENGE && (tower instanceof TowerCrossbow1 || tower instanceof TowerGuard1)){
                Buff.affect(tower, Bless.class, 10000);
            }
            Statistics.towersbuilt++;
            Dungeon.level.occupyCell(tower);
            if (tower instanceof SentientTower){
                ((SentientTower)tower).defendPos(cell);
                tower.beckon(cell);
                tower.state = tower.HUNTING;
            }

        } else {
            if (towerhere) GLog.w(Messages.get(this, "towersnear"));
            super.onThrow( cell );
        }
    }
}
