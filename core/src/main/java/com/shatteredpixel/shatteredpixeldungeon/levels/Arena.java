package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.win;
import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Faint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GoldArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Minion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Prediction;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WaveBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WaveCooldownBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossDwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossOoze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossYog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DMWMinion;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NormalShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TowerShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.EnemyPortal;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.SubAmuletTower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerGuard1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerWave;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GoblinFatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PortalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerCrossbow1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGuard1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.walls.TowerWall1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Compass;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class Arena extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = 15;


    }
    public int startGold = 5;
    public int startLvl = 6;
    public int arenaDepth= 0;

    /**
     * Wave system works like that:
     * wave 1, cooldown 1 (=waveCooldownNormal), wave 2, ..., cooldown time 4 (=waveCooldownBoss), wave 5 (boss or miniboss wave) etc.
     */


    public AmuletTower amuletTower = new AmuletTower();
    protected NewShopKeeper towerShopKeeper = new TowerShopKeeper();
    protected NormalShopKeeper normalShopKeeper = new NormalShopKeeper();

    public boolean shopkeepVertical = false;

    public int WIDTH = 101;
    public int HEIGHT = 101;

    public String name = "Testers' Chamber";

    public int exitCell = Math.round(WIDTH*0.5f)+ Math.round(HEIGHT*0.5f)*WIDTH+1;//center
    public int amuletCell = Math.round(WIDTH*0.5f)+ Math.round(HEIGHT*0.5f)*WIDTH;//where is the amulet mob
    public int towerShopKeeperCell = Math.round(WIDTH*0.5f)+ Math.round(HEIGHT*0.5f)*WIDTH - WIDTH*3;//3 cells under the amulet for now
    public int normalShopKeeperCell = Math.round(WIDTH*0.5f)+ Math.round(HEIGHT*0.5f)*WIDTH - WIDTH*3-6;//3 cells under the amulet for now

    private Rect fullArena = new Rect(2,2,WIDTH-1,HEIGHT-1);//the whole arena rect
    private Rect barrierArena = new Rect(9,9,WIDTH-8,HEIGHT-8);//used for restricting, to be placed around the playerArena, where the enemies can't spawn
    private Rect playerArena = new Rect(10,10,WIDTH-9,HEIGHT-9);//places with no barriers, where the player can walk

    public final int cornersize = 38;//width and length of a cornercube
    public final int pathWidth = 2;

    public Rect upleftcorner = new Rect(2,2,2+cornersize,2 + cornersize);//TODO change the logic from cornersize to centersize
    public Rect downleftcorner = new Rect(2,HEIGHT - 1 - cornersize,2 + cornersize,HEIGHT-1);
    public Rect uprightcorner = new Rect(WIDTH - 1 - cornersize,2,WIDTH-1,2 + cornersize);
    public Rect downrightcorner = new Rect(WIDTH - 1 - cornersize,HEIGHT - 1 - cornersize,WIDTH - 1,HEIGHT - 1);

    public Rect horizontalPath = new Rect(2, (int) (Math.round(HEIGHT*0.5) - pathWidth),WIDTH - 1, (int) (Math.round(HEIGHT*0.5) + pathWidth));

    public Rect verticalPath = new Rect((int)Math.round(WIDTH*0.5)-pathWidth,2,(int)Math.round(WIDTH*0.5)+pathWidth, 2);


    public Rect middle = new Rect(2 + cornersize, 2 + cornersize, WIDTH - 1 - cornersize, HEIGHT - 1 - cornersize);
    public Rect middle1less = new Rect(3 + cornersize, 3 + cornersize, WIDTH - 2 - cornersize, HEIGHT - 2 - cornersize);//a shape inside the middle rect

    public HashSet<Rect> deployingRects = new HashSet<>();




    /**
     * Stuff you can change probably. All turns handled by the amulet.
     */

    public int maxWaves = 15;

    public boolean waterIsToxic = false;




    @Override
    public void playLevelMusic() {
        if (locked){
            Music.INSTANCE.play(Assets.Music.CAVES_BOSS, true);
            return;
        }
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.SEWERS_BOSS},
                new float[]{1},
                false);
    }

    public boolean bossSpawned = false;



    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HALLS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }



    @Override
    protected boolean build() {

        setSize(WIDTH,HEIGHT);

        //base room
        Painter.fill(this, fullArena, Terrain.EMPTY);
        Painter.fill(this, barrierArena, Terrain.ADDITIONALBARRIER);
        Painter.fill(this, playerArena, Terrain.EMPTY);

        Painter.fill(this, upleftcorner, Terrain.WALL);
        Painter.fill(this, uprightcorner, Terrain.WALL);
        Painter.fill(this, downleftcorner, Terrain.WALL);
        Painter.fill(this, downrightcorner, Terrain.WALL);

        Painter.fill(this, middle, Terrain.EMPTY_SP);
        Painter.fillEllipse(this, middle1less, Terrain.EMPTY);



        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);


        exit.set(Math.round(WIDTH*0.5f),Math.round(HEIGHT*0.5f),Math.round(WIDTH*0.5f),Math.round(HEIGHT*0.5f));//under the amulet so the hero may pathetically leave after it is destroyed
        transitions.add(exit);
        this.map[exitCell] = Terrain.EXIT;
        this.map[amuletCell] = Terrain.PEDESTAL;

        return true;
    }

    /** Mob deploying system
     * On a wave start choose a random cell which is canDeployOnCell, choose a random of some mobs and summon them, so each run is kind of unique.
     */

    public int waveCooldownNormal = 20;
    public int waveCooldownBoss   = 50;

    public int mobsToDeploy(int wave){//amount of mobs deployed
        return 1;
    }

    public void doStuffStartwave(int wave){}/// does something in the beginning of each wave
    public void doStuffEndwave(int wave){}/// does something in the end of each wave
    public void addDestinations(){}//for stuff placing around the map after it is generated. Allows to create both mobs, structures, and loot, yet may be laggy
    public Mob chooseMob(int wave){ //Chooses a mob to spawn on a wave;
        Mob mob = new Rat();


        if (mode == WndModes.Modes.CHALLENGE){
            ((Arena)level).affectMob(mob);
        }

        return mob;
    }

    public Actor addRespawner() {
        return null;
    }

    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT,

        TOOUP,
        TOODOWN,
        TOOLEFT,
        TOORIGHT,
        RANDOM,
        CENTRAL,
        EVENMORECENTRAL,
        NOTONEDGE,
        EXACTLYRIGHT,
        EXACTLYLEFT,
        EXACTLYUP,
        EXACTLYDOWN,
        EXACTLYUPRIGHTDOWN
    }

    public void deploymobs(int wave, Direction direction, int groupnum){

        ArrayList<Integer> candidatecells = new ArrayList<>();
        for (int x = 0; x < WIDTH;x++) for (int y = 0; y < HEIGHT; y++){
            switch (direction) {
                case RANDOM: {
                    if (((x >= WIDTH - 5) || (x <= 6) || (y >= HEIGHT - 5) || (y <= 6)) && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case UP: {
                    if ((y <= 7) && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case DOWN: {
                    if ((y >= HEIGHT - 5) && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case LEFT: {
                    if ((x <= 7)  && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case RIGHT: {
                    if ((x >= WIDTH - 6) && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case TOOUP: {
                    if ((y <= 7) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+5 && x > WIDTH/2-5) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case TOODOWN: {
                    if ((y >= HEIGHT - 5) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+5 && x > WIDTH/2-5) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case TOOLEFT: {
                    if ((x <= 7)  && (level.passable[x + WIDTH * y]) && y < HEIGHT/2+5 && y > HEIGHT/2-5 ) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case TOORIGHT: {
                    if ((x >= WIDTH - 6) && (level.passable[x + WIDTH * y])&& y < HEIGHT/2+5 && y > HEIGHT/2-5 ) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case NOTONEDGE: {
                    if ( ( x > 6) && (x < WIDTH - 6) && ( y > 6) && (y < HEIGHT - 6) && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case CENTRAL: {
                    if ( Math.abs(x-WIDTH/2f)<10 && Math.abs(y-HEIGHT/2f)<10 && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EVENMORECENTRAL: {
                    if ( Math.abs(x-WIDTH/2f)<5 && Math.abs(y-HEIGHT/2f)<5 && (level.passable[x + WIDTH * y])) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EXACTLYUP: {
                    if ((y <= 7) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+2 && x > WIDTH/2-2) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EXACTLYDOWN: {
                    if ((y >= HEIGHT - 5) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+2 && x > WIDTH/2-2) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EXACTLYLEFT: {
                    if ((x <= 7)  && (level.passable[x + WIDTH * y]) && y < HEIGHT/2+2 && y > HEIGHT/2-2 ) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EXACTLYRIGHT: {
                    if ((x >= WIDTH - 6) && (level.passable[x + WIDTH * y])&& y < HEIGHT/2+2 && y > HEIGHT/2-2 ) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
                case EXACTLYUPRIGHTDOWN:{
                    if (
                            ((y >= HEIGHT - 5) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+2 && x > WIDTH/2-2) ||
                                    ((x >= WIDTH - 6) && (level.passable[x + WIDTH * y])&& y < HEIGHT/2+2 && y > HEIGHT/2-2 ) ||
                                    ((y <= 7) && (level.passable[x + WIDTH * y]) && x < WIDTH/2+2 && x > WIDTH/2-2)
                    ) {
                        candidatecells.add(x + WIDTH * y);
                    }
                    break;
                }
            }
        }
        bossSpawned = false;
        int grouppos = 0;
        int mobsDeployed = 0;
        int mobsToDeployFinal = mobsToDeploy(wave);
        if (Dungeon.level.mode == WndModes.Modes.HARDMODE ) {
            mobsToDeployFinal = mobsToDeployFinal + mobsToDeployFinal*2*(level.wave*level.wave/maxWaves/maxWaves);
        }
        float onexd = ((float)mobsToDeployFinal/groupnum);
        float onexdsum = onexd;
        while (mobsDeployed<mobsToDeployFinal) {
            grouppos = Random.element(candidatecells);

            while ((float)mobsDeployed<onexdsum) {
                Mob mob = chooseMob(wave);
                if (Dungeon.level.mode == WndModes.Modes.HARDMODE && mobsDeployed % 8 == 1 && !(mob instanceof BossOoze)){
                    int r = Random.Int(9);
                    switch (r){
                        case 0: Buff.affect(mob, ChampionEnemy.Blazing.class); break;
                        case 1: Buff.affect(mob, ChampionEnemy.Projecting.class); break;
                        case 2: Buff.affect(mob, ChampionEnemy.AntiMagic.class); break;
                        case 3: Buff.affect(mob, ChampionEnemy.Blessed.class); break;
                        case 4: Buff.affect(mob, ChampionEnemy.Growing.class); break;
                        case 5: Buff.affect(mob, ChampionEnemy.Giant.class); break;
                        case 6: Buff.affect(mob, ChampionEnemy.Copying.class); break;
                        case 7: Buff.affect(mob, ChampionEnemy.Rejuvenating.class); break;
                        case 8: Buff.affect(mob, ChampionEnemy.Destructive.class); break;
                    }
                }
                if (depth == 14 && mobsDeployed == 0 && mode == WndModes.Modes.CHALLENGE){
                    Bandit bandit = new Bandit();
                    Buff.affect(bandit, Invisibility.class, 1000);
                    bandit.spriteClass = Random.oneOf(TowerCrossbow1Sprite.class, TowerWall1Sprite.class, TowerGuard1Sprite.class, GoblinFatSprite.class);
                    bandit.targetingPreference = Mob.TargetingPreference.ONLY_AMULET;
                    Buff.affect(bandit, Strength.class, 100000);
                    bandit.pos = grouppos;
                    GameScene.add(bandit);
                    bandit.state = bandit.HUNTING;
                }
                mob.pos = grouppos;
                GameScene.add(mob);
                mob.state = mob.HUNTING;
                mobsDeployed++;

            }
            onexdsum+=onexd;
        }
    }
    public void deployMobs(int wave) {
        deploymobs(wave, Direction.RANDOM,0);
    }

    @Override
    public int mobCount() {//an important thing to count mobs on level.
        return super.mobCount();
    }

    public void createMapGuard(ArrayList<Integer> candidates, Mob mob){
        mob.pos = Random.element(candidates);
        mob.state = mob.HUNTING;
        mob.mapGuard = true;
        candidates.remove((Integer) mob.pos);
        GameScene.add(mob);
    }



    @Override
    public void initNpcs() {
        hero.lvl = startLvl;
        hero.updateHT(true);
        Dungeon.gold += startGold;
        updateQuickslot();
        amuletTower.pos = amuletCell;
        amuletTower.HP = amuletTower.HT;
        if (towerShopKeeper!=null){
            towerShopKeeper.pos = towerShopKeeperCell;
        }
        if (normalShopKeeper!=null){
            normalShopKeeper.pos = normalShopKeeperCell;
        }

        level = this;
        GameScene.add(amuletTower);
        this.occupyCell(amuletTower);
        if (towerShopKeeper!=null){
            GameScene.add(towerShopKeeper);
            this.occupyCell(towerShopKeeper);
            towerShopKeeper.placeItems();
        }
        if (normalShopKeeper!=null){
            GameScene.add(normalShopKeeper);
            this.occupyCell(normalShopKeeper);
            normalShopKeeper.placeItems();
        }
    }


    public void startWave() {
        wave++;
        Buff.detach(hero, WaveCooldownBuff.class);
        int coldow = ((Arena)Dungeon.level).width() + ((Arena)Dungeon.level).height();
        if (((Arena)Dungeon.level).WIDTH < 70 || ((Arena)Dungeon.level).HEIGHT < 70) coldow += 50;
        Buff.affect(hero, WaveBuff.class,   ((Arena) level).maxWaves==((Arena) level).wave ? 10000 : coldow);
        deployMobs(wave);
        doStuffStartwave(wave);
        HashSet<Mob> mobsnoconcurrent = new HashSet<>();
        mobsnoconcurrent.addAll(mobs);

        for (Mob mob : mobsnoconcurrent){
            if (mob instanceof EnemyPortal){
                EnemyPortal portal = (EnemyPortal) mob;
                portal.startMobSpawnTimer(portal.countDownToSpawn);
            }
            if (mob instanceof TowerWave) ((TowerWave)mob).prepareMobSpawn();
        }
        /**
         * working on probability:
         * By possible mob classes in wave (failure probability):
         * 1 - 0%;
         * 2 - (1/2)^20
         * 3 - (2/3)^20 etc
         * each level has its own description generated only by the arena class
         */



        Arena arenacopy = Reflection.newInstance(this.getClass());
        StringBuilder waveDesc = new StringBuilder();
        HashSet<Class<? extends Mob>> classesinawave = new HashSet<>();

        for (int randomo = 0; randomo < 20; randomo++){
            classesinawave.add(arenacopy.chooseMob(wave).getClass());
        }
        int howmanyclasses = classesinawave.size();
        //there are no rats in Arena 20 so we just write ???
        if (this instanceof Arena20 && classesinawave.contains(Rat.class)|| this instanceof Arena18) {
            GLog.i(Messages.get(this, "wavestart", wave, "???"));
        } else {
            for (Class<? extends Mob> aClass : classesinawave) {
                waveDesc.append(Messages.get(aClass, "name"));
                howmanyclasses--;
                if (howmanyclasses!=0) waveDesc.append(", ");
            }
            GLog.i(Messages.get(this, "wavestart", wave, waveDesc.toString()));
        }
    };
    public void destroyShopItems(){
        for (Heap heap: Dungeon.level.heaps.valueList()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                if (ShatteredPixelDungeon.scene() instanceof GameScene) {
                    CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
                }
                //heap.destroy();//FIXME the sale heaps either double when they have something on them or just steal these items
                if (heap.size() == 1) {
                    heap.destroy();
                } else {
                    heap.items.remove(heap.size()-1);
                    heap.type = Heap.Type.HEAP;
                }
            }
        }
    }
    public void endWave() {
        destroyShopItems();
        if (towerShopKeeper!=null) towerShopKeeper.placeItems();
        if (normalShopKeeper!=null) normalShopKeeper.placeItems();
        doStuffEndwave(wave);


        for (Mob mob : mobs){
            if (mob instanceof TowerGuard1 && mob.alignment == Char.Alignment.ALLY){
                ((TowerGuard1)mob).turnsUntilRegen = 0;
            }
        }

        //FIXME swap to Arena.class derivatives
        if ((wave==maxWaves) && (depth!=6) && (depth!=17) && (depth!=20) && (depth!=1) && (depth!=25)){
            completeStage();
            return;
        }
        if (wave > maxWaves+2){
        }
        if (wave > maxWaves+8){
            completeStage();
            return;
        } else if (wave > maxWaves+7){
            GLog.i("Ok, so a couple minutes left, I'll count the round as a win. Losing progress after you've done so much... is not something we like to see");
        } else if (wave > maxWaves+6){
            GLog.i("Was the game broken? Let me help... Sorry, I don't know which language you speak...");
        } else if (wave > maxWaves+5){
            GLog.i("Wait why are you here?");
        }
        //attempts to save each fifth (5) wave, starting from wave 3.
        if (wave%3==0)try {
            Dungeon.saveAll();
        } catch (Exception ignored) {}
        Buff.detach(hero, WaveBuff.class);
        Buff.affect(hero, WaveCooldownBuff.class, (wave % 5 == 4 ? waveCooldownBoss : waveCooldownNormal));

        //same that for start wave for prediction buff
        if (hero.buff(Prediction.class)!=null){
            Arena arenacopy = Reflection.newInstance(this.getClass());
            if (wave<maxWaves) {
                StringBuilder waveNextDesc = new StringBuilder();
                HashSet<Class<? extends Mob>> classesinawave = new HashSet<>();
                for (int randomo = 0; randomo < 20; randomo++){
                    classesinawave.add(arenacopy.chooseMob(wave+1).getClass());
                }
                int howmanyclasses = classesinawave.size();
                for (Class<? extends Mob> aClass : classesinawave) {

                    waveNextDesc.append(Messages.get(aClass, "name"));
                    howmanyclasses--;
                    if (howmanyclasses!=0) waveNextDesc.append(", ");
                }
                if (!(this instanceof Arena18))GLog.i(Messages.get(Prediction.class, "nextwave", waveNextDesc.toString()));
                if (wave<maxWaves-1) {
                    StringBuilder waveAfterThatDesc = new StringBuilder();
                    HashSet<Class<? extends Mob>> classesinanafterwave = new HashSet<>();
                    for (int randomo = 0; randomo < 20; randomo++){
                        classesinanafterwave.add(arenacopy.chooseMob(wave+2).getClass());
                    }
                    int howmanyclasses2 = classesinanafterwave.size();
                    for (Class<? extends Mob> aClass : classesinanafterwave) {

                        waveAfterThatDesc.append(Messages.get(aClass, "name"));
                        howmanyclasses2--;
                        if (howmanyclasses2!=0) waveAfterThatDesc.append(", ");
                    }
                    if (!(this instanceof Arena18))GLog.i(Messages.get(Prediction.class, "afternextwave", waveAfterThatDesc.toString()));
                }
            }



        }

    };
    public static void completeStage(){
        //the mod ended the whole run here (it won the game and deleted the save), this game keeps the run going.
        //The arena is cleared, the hero may leave through the exit under the portal.
        GLog.p("The portal holds. The arena is cleared!");
        Dungeon.level.locked = false;
        Buff.detach(hero, WaveBuff.class);
        Buff.detach(hero, WaveCooldownBuff.class);
        if (Dungeon.level instanceof Arena) { ((Arena)Dungeon.level).stageCompleted = true; ((Arena)Dungeon.level).amuletTower.stalled = true; }
    }

    public boolean stageCompleted = false;

    //a method used for challenges with mobs being affected
    public void affectMob(Mob mob){
        //nothing by default
    }
    @Override
    protected void createMobs() {
        Dungeon.level = this;//the arena code reads the level through Dungeon while it is still being built
        initNpcs();
        addDestinations();
    }
    @Override
    protected void createItems() {
    }

    @Override
    public int randomRespawnCell( Char ch ) {
            return -1;
    }


    public static class AmuletTower extends Mob {//this is the must-kill tower and the wave handler class at the same time

        {
            spriteClass = PortalSprite.class;

            HP = HT = 10;

            viewDistance = 3;

            defenseSkill = 0;

            EXP = 0;

            state = PASSIVE;

            flying = true;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.BOSS);
            properties.add(Property.INORGANIC);


            alignment = Alignment.ALLY;
        }
        public int counter = 0;

        @Override
        public int heal(int healing) {
            return 0;//no healing
        }

        @Override
        public void die(Object cause) {
            if (cause == AmuletTower.class) super.die(cause);
            else {
                Camera.main.panFollow(sprite, 2f);
                CellEmitter.get(pos).start(SacrificialParticle.FACTORY, 0.01f, 300);
                GameScene.updateFog(pos,5);
                hero.die(AmuletTower.class);
                hero.sprite.play(hero.sprite.idle, true);
                sprite.play(((PortalSprite)sprite).collapse, true);
                GameScene.updateFog(pos,5);

                this.sprite.jump(pos, pos, 0, 3f, new Callback() {
                    @Override
                    public void call() {
                        Game.runOnRenderThread(new Callback() {
                            @Override
                            public void call() {

                                GameScene.gameOver();
                                GameScene.updateFog(pos,5);
                                GLog.cursed("The portal was destroyed...");
                                Sample.INSTANCE.play(Assets.Sounds.DEATH);
                                Hero.reallyDie(cause);
                                (AmuletTower.this).die(AmuletTower.class);
                                GameScene.updateFog(pos,5);
                                CellEmitter.get(pos).burst(SacrificialParticle.FACTORY,  300);
                            }
                        });
                    }
                });
            }
        }

        @Override
        public boolean isImmune(Class effect) {
            if (Buff.class.isAssignableFrom(effect)) return true;
            return super.isImmune(effect);
        }

        //a mechanism to see whether there has been a wave or a wave cooldown
        public boolean itWasAWave = false;

        //if true, the portal won't start or end waves through act() method. Other actions are still being done.
        public boolean stalled = false;

        @Override
        protected boolean act() {
            boolean enemyspotted = false;


            counter++;
            if (counter == 7 && level instanceof Arena25){
                ((Arena25)level).startDialogue();
            }

            if (Dungeon.depth == 18) GameScene.updateFog(pos, 8);
            else GameScene.updateFog(pos, 5);

            //acid water actions
            for (Mob mob : level.mobs.toArray( new Mob[0] )) {
                if (((Arena)level).waterIsToxic && level.map[mob.pos] == Terrain.WATER && !mob.flying && !(mob instanceof DMWMinion)){
                    if (!(level.mode == WndModes.Modes.CHALLENGE && depth==15)) mob.damage(1, Corrosion.class);
                    else if (mob.alignment == Alignment.ALLY) mob.damage(3, Corrosion.class);
                }
            }
            if (((Arena)level).waterIsToxic && level.map[hero.pos] == Terrain.WATER && hero.buff(Levitation.class)==null){
                hero.damage((level.mode == WndModes.Modes.CHALLENGE && depth==15) ? 3 : 1, Corrosion.class);
            }

            //chal depth-specific actions:
            if (branch == 0 && depth==5 && level.mode == WndModes.Modes.CHALLENGE){
                for (Mob mob : level.mobs){
                    if (mob.alignment==Alignment.ALLY && level.map[pos]==Terrain.WATER){
                        Buff.affect(mob, Burning.class);
                    }
                }
            }
            if (level instanceof Arena22 && level.mode == WndModes.Modes.CHALLENGE){
                for (Mob mob : level.mobs){
                    if (mob.alignment==Alignment.ALLY && (mob instanceof TowerCrossbow1 || mob instanceof TowerGuard1) && mob.buff(Bless.class) == null){
                        Buff.affect(mob, Bless.class, 10000f);
                    }
                }
            }
            if (branch == 0 && depth==8 && level.mode == WndModes.Modes.CHALLENGE){
                for (Mob mob : level.mobs){
                    if (mob.alignment==Alignment.ALLY && mob.buff(GoldArmor.class)==null){
                        Buff.affect(mob, GoldArmor.class);
                    }
                }
            }

            //not arena related necessary actions
            if (hero.buff(Faint.class)!=null) BuffIndicator.refreshHero();


            //every arena necessary actions
            state = PASSIVE;
            ArrayList<Integer> posesToBeckon = new ArrayList<>();

            for (Mob mob2 : com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs){
                if (mob2 instanceof AmuletTower || mob2 instanceof SubAmuletTower) posesToBeckon.add(mob2.pos);

            }


            for (Mob mob : com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs.toArray( new Mob[0] )) {

                int beckoncell = this.pos;
                for (int posvar : posesToBeckon){
                    if (Dungeon.level.distance(mob.pos, posvar) < Dungeon.level.distance(mob.pos, beckoncell)) beckoncell = posvar;
                }

                if (mob.alignment==Alignment.ENEMY &&
                        !(mob.mapGuard)&&
                        !(mob instanceof Tower) &&
                        !(mob instanceof SubAmuletTower) &&
                        !(mob instanceof Piranha) &&
                        !(mob instanceof RotLasher) &&
                        !(mob instanceof Mimic) &&
                        !(mob instanceof Bee) &&
                        !(mob instanceof Arena6.SleepyThief) &&
                        !(mob instanceof EnemyPortal) &&
                        !(mob instanceof BossDwarfKing && ((BossDwarfKing)mob).battleMode == 0) &&
                        !(mob instanceof BossYog))
                {
                    mob.beckon( beckoncell );
                    if (mob.buff(Minion.class) == null) enemyspotted = true;
                }
            }
            //non-chal depth specific actions
            if (Dungeon.depth==11 && Math.random()*1000+level.wave>999){
                if (com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs!=null && hero.buff(WaveCooldownBuff.class)==null) Arena11.dropRock(Random.element(com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs));
            }
            if (Dungeon.depth!=18 && !(depth==20 && level.wave == 25) && !stalled){
                if (hero.buff(WaveBuff.class) != null && !enemyspotted) {
                    ((Arena) level).endWave();
                    itWasAWave = false;
                }
                if (hero.buff(WaveCooldownBuff.class) == null && hero.buff(WaveBuff.class) == null) {//starts the wave at cooldowns end
                    if (!itWasAWave) {
                        ((Arena) level).startWave();
                        itWasAWave = true;
                    } else {
                        ((Arena) level).endWave();
                        itWasAWave = false;}
                }
            }

            //some arena necessary actions







            //adjusting the projectile speed for the game to run smoothly
            if (com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs!=null) {
            }




            alerted = false;
            return super.act();
        }

        @Override
        public void damage(int dmg, Object src) {

            if(src == SubAmuletTower.class){
                super.damage(dmg,src);
            } else if (src instanceof Char && ((Char)src).alignment==Alignment.ENEMY) {
                Char chsrc = (Char)src;
                if (chsrc.properties().contains(Property.BOSS)){
                    super.damage(100, src);
                    System.out.println("000000000000");
                } else if (chsrc.properties().contains(Property.MINIBOSS)){
                    super.damage(3, src);
                    System.out.println("3333333333333");
                } else {
                    super.damage(1, src);
                    System.out.println("1111111111");
                }
                chsrc.damagePortal(pos);
            } else {
                super.damage(1, src);
                System.out.println("010101010101");
            }
        }

        @Override
        public boolean reset() {
            return true;
        }

        @Override
        public void beckon(int cell) {
            //do nothing
        }

        @Override
        public boolean interact(Char c) {
            if (c == Dungeon.hero && Dungeon.depth!=18) {
                if (level.wave==0){
                    ((Arena) level).startWave();
                }
                return true;
            } else return true;
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return false;
        }

        private String COUNTER = "counter";
        private String LASTWASCOOLDOWN= "lastwascooldown";
        private String STALLED= "stalled";
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(COUNTER, counter);
            bundle.put(LASTWASCOOLDOWN, itWasAWave);
            bundle.put(STALLED, stalled);
        }
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            counter = bundle.getInt(COUNTER);
            itWasAWave = bundle.getBoolean(LASTWASCOOLDOWN);
            stalled = bundle.getBoolean(STALLED);
        }
    }

    private String WAVE = "wave";

    private String SHOPKEEPER = "shopkeeper";
    private String TOWERSHOPKEEPER = "towershopkeeper";
    private String PORTAL = "portal";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WAVE, wave);
        bundle.put(SHOPKEEPER,normalShopKeeper);
        bundle.put(TOWERSHOPKEEPER,towerShopKeeper);
        bundle.put(PORTAL, amuletTower.id());
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wave = bundle.getInt(WAVE);
        normalShopKeeper = (NormalShopKeeper) bundle.get(SHOPKEEPER);
        towerShopKeeper = (TowerShopKeeper) bundle.get(TOWERSHOPKEEPER);
        for (Mob mob : com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.mobs){
            if (mob.id() == bundle.getInt(PORTAL)) amuletTower = (AmuletTower) mob;
        }

    }

}
