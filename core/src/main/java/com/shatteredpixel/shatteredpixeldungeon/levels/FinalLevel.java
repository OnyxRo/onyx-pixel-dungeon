package com.shatteredpixel.shatteredpixeldungeon.levels;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cthulhu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeptiumPillar;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;


public class FinalLevel extends Level{

    public State pro;

    public State pro(){
        return pro;
    }

    private static final String cooldowndelay = "cooldowndelay";



    //地图状态
    public enum State {
        Default,
        BrokenSeal,
        PillarsNext,
        PillarsFinal,
        Cthulhu,
        End,
        WHAT,
    }

    public void progress() {
        switch (pro) {
            case Default:
                seal();
                pro = State.BrokenSeal;
                break;
            case BrokenSeal:
                groupA.get(2).activate();
                groupB.get(0).activate();
                pro = State.PillarsNext;
                break;
            case PillarsNext:
                groupA.get(3).activate();
                groupB.get(1).activate();
                groupB.get(2).activate();
                pro = State.PillarsFinal;
                break;
            case Cthulhu:
        }
    }


    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = Math.min(4, viewDistance);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HALLS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    private static final int F = Terrain.EMPTY;
    private static final int V = Terrain.CHASM;
    private static final int S = Terrain.ENTRANCE;
    private static final int B = Terrain.STATUE;
    private static final int C = Terrain.PEDESTAL;


    public static int finalphase = 1;
    private static final int WIDTH = 41;
    private static final int HEIGHT = 57;

    private static final int[] code_map = {
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,C,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,C,F,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,F,C,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,F,F,F,F,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,F,F,F,F,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,V,F,F,F,F,B,F,F,F,B,F,F,F,F,V,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,B,F,F,F,F,F,F,B,B,B,F,F,F,F,F,F,B,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,
            V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,
            V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,C,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,C,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,
            V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,
            V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,B,F,F,F,F,F,F,B,B,B,F,F,F,F,F,F,B,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,V,F,F,F,F,B,F,F,F,B,F,F,F,F,V,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,F,F,F,F,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,F,F,F,F,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,C,F,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,F,C,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,S,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
    };

    private static final int[] fight_map = {
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,C,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,C,F,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,F,C,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,F,F,F,F,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,F,F,F,F,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,V,F,F,F,F,B,F,F,F,B,F,F,F,F,V,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,B,F,F,F,F,F,F,B,B,B,F,F,F,F,F,F,B,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,
            V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,
            V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,C,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,C,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,V,V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,
            V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,
            V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,B,F,F,F,F,F,F,B,B,B,F,F,F,F,F,F,B,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,F,F,F,F,F,V,F,F,F,F,B,F,F,F,B,F,F,F,F,V,F,F,F,F,F,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,F,F,F,F,V,V,V,F,F,F,F,F,F,F,F,F,F,F,F,F,V,V,V,F,F,F,F,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,C,F,V,V,V,V,V,V,B,F,F,F,F,F,F,F,F,F,B,V,V,V,V,V,V,F,C,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,B,F,F,F,F,F,B,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,F,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,F,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
            V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,V,
    };


    public static int getWidth() {
        return WIDTH;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        this.entrance = WIDTH*52 + 20;
        exit = 0;
        pro = State.Default;
        return true;
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }

    /** 存储一个回合计数器，毕竟这里不是buff，无法调用spend来偷懒 */
    private int roundCounter = 0; // 回合计数器


    @Override
    public void occupyCell( Char ch) {

        super.occupyCell( ch );

        if (map[entrance] == Terrain.ENTRANCE && ch == Dungeon.hero
                && Dungeon.level.distance(ch.pos,entrance) >= 13) {
            progress();
        }


        float count = 0;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob.alignment == Char.Alignment.ENEMY && !mob.properties().contains(Char.Property.MINIBOSS)) {
                count += mob.spawningWeight();
            }
        }

        roundCounter++; // 每回合递增1

        if (roundCounter >= 25) {
            roundCounter = 0; // 重置计数器
            /** 避免养蛊 限制在20以下 */
            // 生成一个随机怪物
            if (count < 21 && Statistics.killcount > 0) {
                Mob mob = Dungeon.level.createMob();
                mob.state = mob.WANDERING;
                mob.pos = Dungeon.level.randomRespawnCell(mob);
                GameScene.add(mob);
            }
        }


    }

    /** 我们需要存储延时器保证它退出游戏再次进入后也可以正常工作 */
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(cooldowndelay,roundCounter);

    }
    /** 重新载入游戏后也可以工作 */
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        roundCounter = bundle.getInt(cooldowndelay);
    }

    public ArrayList<SeptiumPillar> groupA = new ArrayList<>();
    public ArrayList<SeptiumPillar> groupB = new ArrayList<>();

    @Override
    public void seal(){
        super.seal();

        /** 我们使用了一个全局统计类来为A组添加怪组，同时通过直接指定怪组添加进去 */
        //1
        SeptiumPillar.GroundPillar ground= new SeptiumPillar.GroundPillar();
        ground.pos = WIDTH*8+33;
        GameScene.add(ground);


        //2
        SeptiumPillar.WaterPillar water= new SeptiumPillar.WaterPillar();
        water.pos = WIDTH*8+7;
        GameScene.add(water);

        //4
        SeptiumPillar.FlamePillar fire= new SeptiumPillar.FlamePillar();
        fire.pos = WIDTH*34+7;
        GameScene.add(fire);

        //3
        SeptiumPillar.WindPillar wind= new SeptiumPillar.WindPillar();
        wind.pos = WIDTH*34+33;
        GameScene.add(wind);

        groupA.add(wind);
        groupA.add(water);
        groupA.add(ground);
        groupA.add(fire);

        SeptiumPillar.ChronosPillar chronos = new SeptiumPillar.ChronosPillar();
        chronos.pos = WIDTH*2+20;
        GameScene.add(chronos);

        SeptiumPillar.HeavenPillar heaven= new SeptiumPillar.HeavenPillar();
        heaven.pos = WIDTH*21+1;
        GameScene.add(heaven);

        SeptiumPillar.PhantomPillar phantom= new SeptiumPillar.PhantomPillar();
        phantom.pos = WIDTH*21+39;
        GameScene.add(phantom);

        Cthulhu god= new Cthulhu();
        god.pos = WIDTH*21+20;
        GameScene.add(god);

        groupB.add(chronos);
        groupB.add(heaven);
        groupB.add(phantom);


        changeMap(fight_map);

        Random.shuffle(groupA);
        Random.shuffle(groupB);
        groupA.get(0).activate(); // 激活第一个塔
        groupA.get(1).activate(); // 激活第二个塔

    }



}
