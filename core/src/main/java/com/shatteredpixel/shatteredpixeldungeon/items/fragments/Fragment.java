package com.shatteredpixel.shatteredpixeldungeon.items.fragments;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;


public class Fragment extends Item {
    {
        stackable = false;

        bones = false;
        unique = true;
    }

    @Override
    public boolean isUpgradable() { return false; }

    @Override
    public boolean isIdentified() { return true; }
}
