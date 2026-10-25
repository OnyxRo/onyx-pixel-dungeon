package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.Kusumi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ATRI;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.FireMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.IceMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.List;

//ported from Magic Ling PD, the hotel beside its town, a special floor here
public class HotelNoramlLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
        ;
    }


    private static final int WIDTH = 14;
    private static final int HEIGHT = 14;

    private static final int W = WALL;
    private static final int D = Terrain.EMPTY_DECO;
    private static final int T = WATER;
    private static final int X = EXIT;
    private static final int S = CHASM;
    private static final int E = EMPTY_SP;
    private static final int K = DOOR;
    private static final int B = BOOKSHELF;
    private static final int M = CRYSTAL_DOOR;


    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,W,W,W,W,W,W,W,W,W,W,W,W,S,
            S,W,D,D,D,E,W,E,D,X,E,E,W,S,
            S,W,E,E,E,E,K,E,E,E,E,E,W,S,
            S,W,E,E,E,E,W,E,E,E,B,B,W,S,
            S,W,W,W,W,W,W,E,E,E,E,D,W,S,
            S,W,D,D,D,E,W,E,E,E,E,D,W,D,
            S,W,T,T,E,E,K,E,E,E,E,D,W,D,
            S,W,T,T,E,E,W,E,E,W,W,W,W,D,
            S,W,W,W,W,W,W,E,E,W,E,E,E,D,
            S,W,D,D,D,E,W,E,E,W,E,E,E,D,
            S,W,E,E,E,E,K,E,E,M,E,E,E,D,
            S,W,E,E,E,E,W,E,E,W,E,E,E,D,
            S,W,W,W,W,W,W,W,W,W,W,W,W,D,
    };

    //magicling, the hotel's door leads back to the town (another special floor)
    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        com.shatteredpixel.shatteredpixeldungeon.levels.SpecialFloors.travel(com.shatteredpixel.shatteredpixeldungeon.levels.NormalZeroFiveLevel::new);
        return false;
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 37;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        return true;
    }

    @Override
    public void playLevelMusic() {

        switch (com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.currentBGM()){
            case 0:
                Music.INSTANCE.play(Assets.Music.MORP_BOSS, true);
                break;
            case 1:
                Music.INSTANCE.play(Assets.Music.SAND, true);
                break;
            case 2:
                Music.INSTANCE.play(Assets.Music.PRACH, true);
                break;
            default:
                Music.INSTANCE.play(Assets.Music.TOWN, true);
                break;
        }

    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.HOTEL_ZERO;

            tileW = 14;
            tileH = 14;
        }

        final int TEX_WIDTH = 14*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }
    }

    @Override
    protected void createMobs() {

        if(!com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.onlyLing){
            if (true || false) {
                if (Random.Float() < 0.65f) {
                    WhiteLing n = new WhiteLing();
                    n.pos= 80;
                    mobs.add(n);
                }
            } else if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
                if (Random.Float() < 0.45f) {
                    WhiteLing n = new WhiteLing();
                    n.pos = 80;
                    mobs.add(n);
                }
            } else {
                if (Random.Float() < 0.1f) {
                    WhiteLing n = new WhiteLing();
                    n.pos = 80;
                    mobs.add(n);
                }
            }
        }

        /** 旅馆组 **/
        ATRI atri = new ATRI();
        atri.pos = 102;
        mobs.add(atri);

        if(Random.Float() >= 0.5f){
            IceMagicGirl iceMagicGirl = new IceMagicGirl();
            iceMagicGirl.pos = 158;
            mobs.add(iceMagicGirl);
        }

        if(Random.Float() >= 0.5f){
            FireMagicGirl fireMagicGirl = new FireMagicGirl();
            fireMagicGirl.pos = 46;
            mobs.add(fireMagicGirl);
        }

        if( Random.Float() <= 0.2f){
            Kusumi kusumi = new Kusumi();
            kusumi.pos = 137;
            mobs.add(kusumi);
        }

    }

    @Override
    protected void createItems() {

    }

    public String tilesTex() {
        return Assets.Environment.TILES_ZERO;
    }

    public String waterTex() {
        return Assets.Environment.WATER_ZERO;
    }


}


