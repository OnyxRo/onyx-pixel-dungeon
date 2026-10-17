package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;

import java.util.ArrayList;

public class ArenaEndless extends Arena {
    public static final ArrayList<Class<? extends Mob>> spamMobs = new ArrayList<Class<? extends Mob>>(){
            {
                add(Rat.class);
            }
    };
}
