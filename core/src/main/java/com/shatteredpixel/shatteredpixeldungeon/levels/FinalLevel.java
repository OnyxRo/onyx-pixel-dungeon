package com.shatteredpixel.shatteredpixeldungeon.levels;


import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ChronosSkull;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.HeavenDoor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomSpirit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GroundStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WaterFlower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WindSwarm;
import com.watabou.utils.Reflection;
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
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CthulhuArt;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CthulhuSprite;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
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
            //abyssalnite, the groups were never saved, so after a reload they were empty and this crashed.
            //The next sleeping pillar of each group is found on the level instead
            case BrokenSeal:
                activateNext(true);
                activateNext(false);
                pro = State.PillarsNext;
                break;
            case PillarsNext:
                activateNext(true);
                activateNext(false);
                activateNext(false);
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
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,C,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,C,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
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
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,C,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,C,V,
            V,V,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,F,F,F,V,V,
            V,V,V,V,F,F,F,F,F,B,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,B,F,F,F,F,F,V,V,V,V,
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
                Mob mob = arenaMob();
                mob.state = mob.WANDERING;
                mob.pos = Dungeon.level.randomRespawnCell(mob);
                //abyssalnite, no free cell gave -1 and the add crashed
                if (mob.pos != -1) GameScene.add(mob);
            }
        }


    }

    //The monsters of this fight, from Abyssalnite's Bestiary.getTownMobRotation. Each broken elemental pillar lets its
    //own monster in (Statistics.FirstActive and the rest), and rarely a chronos skull, heaven door or phantom spirit
    //comes instead. Abyssalnite spawned nothing before the first pillar broke, here the lit pillars' monsters come then
    public static Mob arenaMob(){
        float r = Random.Float();
        if (r < 0.03f) return new ChronosSkull();
        if (r < 0.06f) return new HeavenDoor();
        if (r < 0.09f) return new PhantomSpirit();
        ArrayList<Class<? extends Mob>> pool = new ArrayList<>();
        if (Statistics.FirstActive) pool.add( GroundStatue.class );
        if (Statistics.SecondActive) pool.add( WaterFlower.class );
        if (Statistics.ThirdActive) pool.add( FlameDemon.class );
        if (Statistics.FourthActive) pool.add( WindSwarm.class );
        if (pool.isEmpty()){
            for (Mob m : Dungeon.level.mobs){
                if (m.alignment != Char.Alignment.ENEMY) continue;
                if (m instanceof SeptiumPillar.GroundPillar) pool.add( GroundStatue.class );
                if (m instanceof SeptiumPillar.WaterPillar) pool.add( WaterFlower.class );
                if (m instanceof SeptiumPillar.FlamePillar) pool.add( FlameDemon.class );
                if (m instanceof SeptiumPillar.WindPillar) pool.add( WindSwarm.class );
            }
        }
        if (pool.isEmpty()){
            pool.add( GroundStatue.class ); pool.add( WaterFlower.class ); pool.add( FlameDemon.class ); pool.add( WindSwarm.class );
        }
        return Reflection.newInstance( Random.element( pool ) );
    }

    /** 我们需要存储延时器保证它退出游戏再次进入后也可以正常工作 */
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(cooldowndelay,roundCounter);
        if (pro != null) bundle.put("fight_state", pro); //abyssalnite never saved it, a reload lost the fight's progress
        int[] v = new int[voids.size()];
        for (int i = 0; i < v.length; i++) v[i] = voids.get(i);
        bundle.put(VOIDS, v);

    }
    /** 重新载入游戏后也可以工作 */
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        roundCounter = bundle.getInt(cooldowndelay);
        voids.clear();
        if (bundle.contains(VOIDS)) for (int c : bundle.getIntArray(VOIDS)) voids.add(c);
        if (bundle.contains("fight_state")) pro = bundle.getEnum("fight_state", State.class);
        else if (locked) pro = Statistics.killcount < 2 ? State.BrokenSeal
                : Statistics.killcount < 4 ? State.PillarsNext : State.PillarsFinal; //older saves, judged by pillars killed
        buildFlagMaps(); //again, now the fight state is known
        if (pro == State.Cthulhu || pro == State.End) lightUp();
    }

    private void activateNext( boolean elemental ){
        ArrayList<SeptiumPillar> sleeping = new ArrayList<>();
        for (Mob mob : mobs.toArray(new Mob[0])){
            if (!(mob instanceof SeptiumPillar) || mob.alignment != Char.Alignment.NEUTRAL) continue;
            boolean a = mob instanceof SeptiumPillar.WindPillar || mob instanceof SeptiumPillar.WaterPillar
                    || mob instanceof SeptiumPillar.GroundPillar || mob instanceof SeptiumPillar.FlamePillar;
            if (a == elemental) sleeping.add((SeptiumPillar) mob);
        }
        if (!sleeping.isEmpty()) Random.element(sleeping).activate();
    }

    public ArrayList<SeptiumPillar> groupA = new ArrayList<>();
    public ArrayList<SeptiumPillar> groupB = new ArrayList<>();

    @Override
    public void seal(){
        super.seal();
        //the pillar count is a run statistic, a fight started again in the same run must count from zero
        Statistics.killcount = 0;

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




    //Cthulhu's pit. The 5x5 pit and the cells of his eight floor tentacles cannot be walked on while he lives.
    //His tentacle strikes leave craters drawn from the animation page that act like void, a pit you can fall into,
    //at most MAX_VOIDS, the oldest closes when a new one opens. Falling in while he lives does not leave the fight,
    //the void throws you back hurt.
    private static final String VOIDS = "voids";
    private static final int MAX_VOIDS = 12;
    private static final int CENTRE = WIDTH*21+20;
    private final ArrayList<Integer> voids = new ArrayList<>();
    private final ArrayList<Image> voidImages = new ArrayList<>();

    private static boolean inPit( int cell ){
        int x = cell % WIDTH, y = cell / WIDTH;
        return x >= 18 && x <= 22 && y >= 19 && y <= 23;
    }

    private static boolean isTentacle( int cell ){
        for (int[] t : CthulhuArt.TENTACLES){
            if (cell == (t[1] / 16) * WIDTH + t[0] / 16) return true;
        }
        return false;
    }

    private boolean bossAlive(){
        return pro != State.End;
    }

    //a tap anywhere on his body attacks him
    public static int aimAt( int cell ){
        if (!(Dungeon.level instanceof FinalLevel) || !((FinalLevel) Dungeon.level).bossAlive() || !inPit( cell )) return cell;
        Char ch = Actor.findChar( CENTRE );
        return ch instanceof Cthulhu ? CENTRE : cell;
    }

    @Override
    public void buildFlagMaps() {
        super.buildFlagMaps();
        for (int i = 0; i < length(); i++){
            if (bossAlive() && (inPit( i ) || isTentacle( i ))){
                passable[i] = false;
                avoid[i] = false;
            } else if (voids != null && voids.contains( i )){
                voidFlags( i );
            }
        }
    }

    //the same flags as a chasm, the floor tile stays and the crater art is drawn over it
    private void voidFlags( int cell ){
        passable[cell] = false;
        avoid[cell] = true;
        pit[cell] = true;
    }

    //turns a floor cell into void, returns true when a new hole was made
    public boolean addVoid( int cell, boolean drop ){
        if (!insideMap( cell ) || !passable[cell] || voids.contains( cell ) || cell == entrance) return false;
        if (map[cell] != Terrain.EMPTY && map[cell] != Terrain.EMPTY_DECO && map[cell] != Terrain.EMPTY_SP && map[cell] != Terrain.GRASS) return false;
        if (voids.size() >= MAX_VOIDS){
            int old = voids.remove( 0 );
            if (!voidImages.isEmpty()) voidImages.remove( 0 ).killAndErase();
            //holes from older builds were real chasm tiles
            if (map[old] == Terrain.CHASM){
                set( old, Terrain.EMPTY, this );
                GameScene.updateMap( old );
            } else {
                passable[old] = true;
                avoid[old] = false;
                pit[old] = false;
            }
        }
        voids.add( cell );
        voidFlags( cell );
        showVoid( cell );
        Char ch = Actor.findChar( cell );
        if (ch != null && drop) occupyCell( ch );
        return true;
    }

    private void showVoid( int cell ){
        if (visuals == null) return;
        Image im = CthulhuSprite.Fx.still( "strike", CthulhuArt.STRIKE_W, CthulhuArt.STRIKE_H, CthulhuArt.STRIKE, CthulhuArt.STRIKE.length - 1,
                (cell % WIDTH) * 16 + 8 + CthulhuArt.STRIKE_DX, (cell / WIDTH) * 16 + 14 + CthulhuArt.STRIKE_DY );
        visuals.add( im );
        //holes shown after a reload are made in order, so the oldest is always first
        voidImages.add( im );
    }

    //the void throws the hero back into the fight instead of down a floor, called from Chasm.heroFall
    public boolean voidFall( int pos ){
        if (!locked || !bossAlive()) return false;
        Hero hero = Dungeon.hero;
        ArrayList<Integer> cells = new ArrayList<>();
        for (int i = 0; i < length(); i++){
            if (passable[i] && !avoid[i] && !pit[i] && Actor.findChar( i ) == null && distance( i, CENTRE ) >= 4 && distance( i, CENTRE ) <= 9) cells.add( i );
        }
        if (cells.isEmpty()) return false;
        hero.interrupt();
        ScrollOfTeleportation.appear( hero, Random.element( cells ) );
        Dungeon.observe();
        GLog.n( Messages.get( Cthulhu.class, "voidfall" ) );
        hero.damage( Math.max( 1, hero.HT / 6 ), Chasm.class );
        if (!hero.isAlive()) Dungeon.fail( Chasm.class );
        return true;
    }

    private void lightUp(){
        //the dark of the pillar fight lifts once he wakes
        viewDistance = 8;
        if (Dungeon.hero != null && Dungeon.level == this) Dungeon.hero.viewDistance = viewDistance;
    }

    //every pillar is broken, Cthulhu wakes
    public void checkPillars(){
        if (pro == State.End || pro == State.Cthulhu) return;
        for (Mob m : mobs){
            if (m instanceof SeptiumPillar){
                for (Mob c : mobs.toArray( new Mob[0] )) if (c instanceof Cthulhu) ((Cthulhu) c).pillarBroken();
                return;
            }
        }
        pro = State.Cthulhu;
        lightUp();
        for (Mob m : mobs.toArray( new Mob[0] )){
            if (m instanceof Cthulhu) ((Cthulhu) m).wake();
        }
        Dungeon.observe();
    }

    //opens the pit and puts the way to the Yendor hall where he was
    public void cthulhuDefeated(){
        pro = State.End;
        unseal();
        map[CENTRE] = Terrain.EXIT;
        exit = CENTRE;
        transitions.add( new LevelTransition( this, CENTRE, LevelTransition.Type.REGULAR_EXIT, 26, 0, LevelTransition.Type.REGULAR_ENTRANCE ) );
        buildFlagMaps();
        GameScene.updateMap( CENTRE );
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        visuals.add( new Image( CthulhuSprite.DIR + "pit.png" ) {{ x = CthulhuArt.PIT_X; y = CthulhuArt.PIT_Y; }} );
        voidImages.clear();
        for (int c : voids) if (map[c] != Terrain.CHASM) showVoid( c );
        if (pro == State.End){
            visuals.add( CthulhuSprite.Fx.still( "death", CthulhuArt.BOSS_W, CthulhuArt.BOSS_H, CthulhuArt.DEATH, CthulhuArt.DEATH.length - 1,
                    CthulhuArt.BOSS_X, CthulhuArt.BOSS_Y ) );
        }
        return visuals;
    }
}
