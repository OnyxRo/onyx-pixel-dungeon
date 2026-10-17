package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public abstract class HeroSpellTargeted extends HeroSpell {
    {
        usesTargeting = true;
    }

    public CellSelector.Listener cellCaster = new CellSelector.Listener(){
        @Override
        public void onSelect(Integer cell) {
            ///
        }
        @Override
        public String prompt() {
            return null;
        }
    };
    @Override
    public void cast() {
        curUser = Dungeon.hero;
        curItem = this;
        GameScene.selectCell(cellCaster);
    }

    protected void activate(int cell){
        //nothing by def
    }

}
