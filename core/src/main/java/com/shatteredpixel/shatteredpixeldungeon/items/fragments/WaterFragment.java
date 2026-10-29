package com.shatteredpixel.shatteredpixeldungeon.items.fragments;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WaterFragment extends Fragment {
    {
        image = ItemSpriteSheet.WATER_FRAGMENT;
        unique = true;
        stackable = false;
        bones = false;
    }

    public WaterFragment() {
        super();
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        if (super.doPickUp(hero, pos)) {
            return true;
        } else {
            return false;
        }
    }
}