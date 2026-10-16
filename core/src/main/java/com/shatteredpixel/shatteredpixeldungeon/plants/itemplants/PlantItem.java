/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2017 Leon Horn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.shatteredpixel.shatteredpixeldungeon.plants.itemplants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

import java.util.ArrayList;

public class PlantItem extends Item {

    {
        stackable = true;
        defaultAction = AC_PLANT;
    }

    public Plant.Seed seed;

    public static final String AC_PLANT = "PLANT";

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_PLANT );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_PLANT )) {
            GameScene.selectCell(informer);
        }
    }

    private CellSelector.Listener informer = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer cell ) {
            if(cell != null){
                if(Dungeon.level.passable[cell] && !Dungeon.level.pit[cell] && Dungeon.level.traps.get(cell) == null){
                    Dungeon.level.plant(seed, cell);
                    curItem.detach(curUser.belongings.backpack);
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(PlantItem.class, "plant");
        }
    };

}
