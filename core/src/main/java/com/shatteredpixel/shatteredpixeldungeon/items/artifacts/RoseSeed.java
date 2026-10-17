package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerRoseBush;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.TowerSpawner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class RoseSeed extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_ROSESEED;
        stackable = true;
        defaultAction = AC_THROW;

    }

    public Class <? extends Tower> towerClass = TowerRoseBush.class;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add( AC_DROP );
        actions.add( AC_THROW );
        return actions;
    }
    @Override
    protected void onThrow( int cell ) {
        Char enemy = Actor.findChar( cell );
        boolean towerhere = false;
        if (Dungeon.depth==12 && Dungeon.level.mode== WndModes.Modes.CHALLENGE){

            for (int i : PathFinder.NEIGHBOURS25){
                if (Char.findChar(i) != null && Char.findChar(i) instanceof Tower){
                    towerhere = true;
                }
            }
        }
        if (enemy == null && !towerhere) {
            Tower tower = Reflection.newInstance(towerClass);
            tower.pos = cell;
            GameScene.add(tower);
            Dungeon.level.occupyCell(tower);

        } else {
            if (towerhere) GLog.w(Messages.get(TowerSpawner.class, "towersnear"));
            super.onThrow( cell );
        }
    }

    @Override
    public int value() {
        return 100;
    }


}
