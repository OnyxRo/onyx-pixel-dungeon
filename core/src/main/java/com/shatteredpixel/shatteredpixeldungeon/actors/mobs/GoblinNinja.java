package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoblinNinjaSprite;

public class GoblinNinja extends Goblin{
    {
        spriteClass = GoblinNinjaSprite.class;

        HP = HT = 30;
        defenseSkill = 10;

        viewDistance = 20;
        baseSpeed = 1.4f;

        EXP = 10;
        maxLvl = 15;

        targetingPreference = TargetingPreference.NOT_AMULET;
    }
}
