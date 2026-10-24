package com.shatteredpixel.shatteredpixeldungeon.levels.tomb;

//ported from Magic Ling PD, Roger's floor 15 arena in the ancient tomb

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_WELL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Roger;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RogerBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 8;
    }

    private static final int WIDTH = 19;
    private static final int HEIGHT = 19;

    private static final int S = CHASM;
    private static final int R = WATER;
    private static final int E = EMPTY_SP;
    private static final int A = ENTRANCE;
    private static final int B = EXIT;
    private static final int W = WALL;
    private static final int H = GRASS;
    private static final int P = STATUE;
    private static final int K = STATUE_SP;
    private static final int G = EMPTY_WELL;
    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,E,E,E,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,B,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,E,E,E,W,S,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,E,E,E,W,W,W,S,S,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,W,W,E,K,E,H,E,R,E,K,E,W,W,S,S,S,
            S,S,S,W,P,E,E,H,R,R,R,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,E,E,R,E,E,H,E,E,E,W,S,S,S,
            S,S,S,W,E,E,H,E,E,E,E,H,H,E,E,W,S,S,S,
            S,S,S,W,P,H,H,E,E,E,E,E,E,E,P,W,S,S,S,
            S,S,S,W,E,E,R,R,E,E,E,E,E,E,E,W,S,S,S,
            S,S,S,W,E,E,E,R,E,E,H,H,R,E,E,W,S,S,S,
            S,S,S,W,P,H,E,E,R,E,H,R,R,E,P,W,S,S,S,
            S,S,S,W,W,E,K,E,E,E,E,R,K,E,W,W,S,S,S,
            S,S,S,S,W,W,E,E,E,E,E,E,E,W,W,S,S,S,S,
            S,S,S,S,S,W,W,W,E,A,E,W,W,W,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,E,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    //magicling, the stage's boss track from the mod
    @Override
    public void playBossMusic() {
        Music.INSTANCE.play("music/magicling/royal.ogg", true);
    }

    @Override
    public void seal() {
        super.seal();

        Level.set(entrance(), STATUE);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        Level.set(exit(), STATUE);
        GameScene.updateMap(exit());
        Dungeon.observe();
    }

    @Override
    public void unseal() {
        super.unseal();
        Level.set(entrance(), ENTRANCE);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        Level.set(exit(), EXIT);
        GameScene.updateMap(exit());
        Dungeon.observe();
    }

    private int status = 0;

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put("level_status", status);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        status = bundle.getInt("level_status");
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);

        if (status == 0 && hero != null && ch == hero && level == this && level.distance(hero.pos,294) >= 2) {
            seal();
            status++;
            CellEmitter.get(entrance).start(FlameParticle.FACTORY, 0.1f, 10);
            Roger roger = new Roger();
            roger.pos = 180;
            roger.state = roger.HUNTING;
            GameScene.add(roger);
            roger.notice();
        }
    }

    @Override
    protected boolean build() {
        feeling = Feeling.NONE;
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        //magicling, the hero arrives at the bottom and leaves by the top, the mod had the two swapped
        transitions.add(new LevelTransition(this, 294, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, 28, LevelTransition.Type.REGULAR_EXIT));

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = "environment/custom_tiles/magicling/tomb_hall.png";

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    public String tilesTex() {
        return "environment/worlds/tiles_tomb.png";
    }

    @Override
    public String waterTex() {
        return "environment/worlds/water_tomb.png";
    }

    public static class townAbove extends CustomTilemap {

        {
            texture = "environment/custom_tiles/magicling/tomb_hall.png";

            tileW = 19;
            tileH = 19;
        }

        final int TEX_WIDTH = 19*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

}
