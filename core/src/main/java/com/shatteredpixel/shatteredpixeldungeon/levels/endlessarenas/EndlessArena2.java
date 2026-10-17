package com.shatteredpixel.shatteredpixeldungeon.levels.endlessarenas;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossOoze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossRatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CausticSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ChiefRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollThrower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.HermitCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatArcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatKnife;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatMage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRatShield;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.TowerCrossbow1;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaSewers;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class EndlessArena2 extends ArenaSewers {

    //the default map is similar to sewer entrance.
    {
        name = "Rat Kingdom";

        color1 = 0x48763c;
        color2 = 0x59994a;
        viewDistance = 15;
        WIDTH = 49;
        HEIGHT = 79;

        amuletCell = 9 + WIDTH*39;
        exitCell = amuletCell;
        towerShopKeeperCell = 2 + 41*WIDTH;
        normalShopKeeperCell = 2 + 37*WIDTH;

        towerShopKeeper.vertical = NewShopKeeper.ShopDirection.RIGHT;
        normalShopKeeper.vertical = NewShopKeeper.ShopDirection.RIGHT;

        arenaDepth = 5;

        startGold = 600;
        maxWaves = 10000;
        waveCooldownNormal = 10;
        waveCooldownBoss = 150;
        if (slot1.equals(TowerInfo.AllTowers.RATCAMP)) slot1 = TowerInfo.AllTowers.WALL;
        if (slot2.equals(TowerInfo.AllTowers.RATCAMP)) slot2 = TowerInfo.AllTowers.WALL;
        if (slot3.equals(TowerInfo.AllTowers.RATCAMP)) slot3 = TowerInfo.AllTowers.WALL;
        if (slot4.equals(TowerInfo.AllTowers.RATCAMP)) slot4 = TowerInfo.AllTowers.WALL;

    }


    @Override
    public int mobsToDeploy(int wave) {
        int num = 0;
        switch (wave % 15){
            case 1: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 10;break;
            case 2: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 5;break;
            case 3: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 10;break;
            case 4: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 9;break;
            case 5: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 2;break;
            case 6: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5) * 3;break;
            case 7: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5) * 2;break;
            case 8: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5) * 4;break;
            case 9: num = (wave*  (int)Math.sqrt(Math.sqrt(wave))/5) * 2;break;
            case 10: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5) * 2;break;
            case 11: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 10;break;
            case 12: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 3;break;
            case 13: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 7;break;
            case 14: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 2;break;
            case 0: num = (wave* (int)Math.sqrt(Math.sqrt(wave))/5 + 1) * 2;break;
        }
        return num;
    }
    @Override
    public Mob chooseMob(int wave) {
        Mob mob = new Rat();
        switch (wave % 15){
            case 1: mob = new Rat(); break;
            case 2: mob = new Albino(); break;
            case 3: mob = new Rat(); break;
            case 4: mob = Random.oneOf(new CampRatKnife(), new Rat()); break;
            case 5: mob = new FetidRat(); break;
            case 6: mob = new CampRatArcher(); break;
            case 7: mob = new CampRatShield(); break;
            case 8: mob = Random.oneOf(new CampRatArcher(), new Albino()); break;
            case 9: mob = Random.oneOf(new CampRatMage(), new CampRatShield()); break;
            case 10: mob = new ChiefRat(); break;
            case 11: mob = new CampRatKnife(); break;
            case 12: mob = new CampRatArcher(); break;
            case 13: mob = new Albino(); break;
            case 14: mob = new CampRatMage(); break;
            case 0:{
                if (!bossSpawned) {
                    bossSpawned = true;
                    mob = new BossRatKing();
                }
                else mob = new CampRatShield();
            }  break;
        }
        if (level.mode == WndModes.Modes.CHALLENGE){
            affectMob(mob);
        }
        mob.alignment = Char.Alignment.ENEMY;
        return mob;
    }




    public void addDestinations() {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int m = 0; m < WIDTH * HEIGHT; m++) {
            if (this.passable[m] && this.distance(amuletCell, m) > 10) candidates.add(m);
        }
        for (int i = 0;i<5;i++){
            this.drop(new Gold(Random.Int(30, 50)), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(Generator.random(Generator.Category.POTION), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(Generator.randomUsingDefaults(Generator.Category.SCROLL), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(new SmallRation(), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(Generator.random(Generator.Category.STONE), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(Generator.random(Generator.Category.SEED), Random.element(candidates)).type = Heap.Type.CHEST;
            this.drop(Generator.random(Generator.Category.MISSILE), Random.element(candidates)).type = Heap.Type.CHEST;
        }
        this.drop(new IronKey(this.arenaDepth), Random.element(candidates));
        this.drop(new IronKey(this.arenaDepth), Random.element(candidates));this.drop(new IronKey(this.arenaDepth), Random.element(candidates));

        this.drop(Generator.random(Generator.Category.RING), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(new Honeypot(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(new PotionOfLiquidFlame(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(new PotionOfFrost(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(new Honeypot(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(new PotionOfLiquidFlame(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(new PotionOfFrost(), Random.element(candidates)).type = Heap.Type.CHEST;;
        this.drop(Generator.random(Generator.Category.RING), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.WAND), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.STONE), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(Generator.random(Generator.Category.SEED), Random.element(candidates)).type = Heap.Type.CHEST;

        candidates.clear();
        for (int m = 0; m < WIDTH * HEIGHT; m++) {
            if (this.passable[m] && this.distance(amuletCell, m) > 15 && this.map[m] == Terrain.EMPTY_SP) candidates.add(m);
        }
        MeleeWeapon someCursedWeapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
        someCursedWeapon.cursed = true;
        this.drop(someCursedWeapon, Random.element(candidates)).type = Heap.Type.SKELETON;
        this.drop(Generator.random(Generator.Category.ARTIFACT), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.ARTIFACT), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.RING), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.RING), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.WAND), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.WAND), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(com.watabou.utils.Random.oneOf(new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerCrossbow(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWall(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerGuard(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWand()), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(com.watabou.utils.Random.oneOf(new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerCrossbow(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWall(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerGuard(), new com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerWand()), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(Generator.random(Generator.Category.WEAPON), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(new com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb(), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(Generator.random(Generator.Category.WEAPON), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(Generator.random(Generator.Category.WEAPON), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(new ElixirOfToxicEssence(), Random.element(candidates)).type = Heap.Type.LOCKED_CHEST;
        this.drop(new PotionOfCorrosiveGas(), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(new PotionOfStrength(), Random.element(candidates)).type = Heap.Type.CHEST;
        this.drop(new ElixirOfAquaticRejuvenation(), Random.element(candidates)).type = Heap.Type.CHEST;

        super.addDestinations();
    }



    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.RAT_PUNCHER_SPEDUP},
                new float[]{1},
                false);
    }

    @Override
    public void doStuffEndwave(int wave) {
        int goldAdd = 50;
        Dungeon.gold+=goldAdd;
        GLog.w(Messages.get(Arena.class, "goldaddendwave", goldAdd));
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int m = 0; m<WIDTH*HEIGHT;m++){
            if (this.passable[m] && distance(amuletCell, m)>12) candidates.add(m);
        }

        if (wave % 5 == 4) {
            GLog.p(Messages.get(Arena.class, "lootdrop", goldAdd));
            for (int i = 0; i < 5; i++) {
                this.drop(new Gold(Random.Int(10, 40)),  Random.element(candidates));
                this.drop(Random.oneOf(
                        Generator.random(Generator.Category.POTION),
                        Generator.random(Generator.Category.SCROLL),
                        Generator.random(Generator.Category.STONE),
                        Generator.random(Generator.Category.SEED)),  Random.element(candidates));
                this.drop(Generator.random(),  Random.element(candidates));

                for (Heap heap : Dungeon.level.heaps.valueList()) {
                    heap.sprite.link(heap);
                    heap.sprite.update();
                }
            }
        }


        super.doStuffEndwave(wave);
    }


    @Override
    protected boolean build() {

        setSize(WIDTH,HEIGHT);
        //base room


        map = new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 20, 1, 20, 1, 20, 1, 1, 20, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 20, 20, 1, 1, 20, 1, 1, 1, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 20, 20, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 20, 20, 20, 1, 20, 1, 1, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 20, 1, 20, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 20, 1, 1, 1, 1, 20, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 20, 1, 20, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 27, 1, 27, 27, 27, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 27, 1, 1, 1, 1, 1, 4, 26, 14, 14, 14, 14, 26, 4, 26, 14, 14, 14, 14, 26, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 27, 1, 27, 27, 27, 1, 4, 14, 14, 14, 14, 14, 14, 4, 14, 14, 14, 14, 14, 14, 4, 1, 1, 1, 1, 20, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 14, 14, 14, 14, 14, 14, 10, 14, 14, 14, 14, 14, 14, 10, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 27, 1, 27, 27, 27, 1, 4, 14, 14, 14, 14, 14, 14, 4, 14, 14, 14, 14, 14, 14, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 20, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 27, 1, 1, 1, 1, 1, 4, 26, 14, 14, 14, 14, 26, 4, 26, 14, 14, 14, 14, 26, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 27, 1, 27, 27, 27, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 20, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 20, 1, 1, 1, 1, 1, 20, 1, 1, 11, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 12, 1, 1, 12, 1, 1, 12, 1, 1, 1, 1, 1, 1, 1, 1, 1, 12, 1, 1, 12, 1, 1, 12, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 29, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 29, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 1, 1, 1, 12, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 29, 11, 11, 29, 11, 11, 29, 11, 11, 11, 11, 11, 11, 11, 11, 11, 29, 11, 11, 29, 11, 11, 29, 11, 11, 1, 1, 1, 29, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 29, 29, 29, 29, 29, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 29, 1, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 4, 5, 27, 4, 4, 21, 1, 11, 1, 1, 1, 29, 1, 11, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 4, 1, 27, 4, 4, 4, 1, 11, 1, 1, 1, 29, 1, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 4, 10, 27, 4, 4, 4, 1, 11, 1, 1, 1, 29, 29, 29, 29, 29, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 1, 1, 4, 4, 4, 1, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 29, 11, 11, 29, 11, 11, 29, 11, 11, 11, 11, 11, 11, 11, 11, 11, 29, 11, 11, 29, 11, 11, 29, 11, 11, 1, 1, 1, 29, 1, 1, 4, 4, 4, 4, 4, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 1, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 29, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 29, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 4, 1, 1, 4, 1, 1, 20, 1, 1, 1, 1, 1, 1, 4, 1, 1, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 25, 25, 25, 25, 1, 1, 25, 1, 4, 16, 4, 4, 4, 4, 4, 16, 4, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 25, 25, 9, 25, 28, 25, 25, 25, 25, 25, 4, 16, 16, 14, 14, 14, 16, 16, 4, 1, 2, 2, 15, 15, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 25, 25, 25, 25, 25, 25, 25, 1, 4, 16, 16, 14, 14, 14, 16, 16, 4, 1, 2, 15, 2, 15, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 1, 25, 25, 25, 9, 9, 25, 25, 25, 9, 4, 16, 16, 14, 14, 14, 16, 16, 4, 1, 2, 2, 2, 15, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 25, 9, 25, 25, 25, 25, 25, 25, 9, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 15, 2, 15, 15, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 1, 1, 1, 1, 1, 1, 20, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 15, 2, 2, 15, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 15, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 25, 4, 4, 4, 4, 4, 25, 11, 12, 11, 1, 1, 1, 11, 12, 11, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 11, 11, 11, 1, 1, 1, 11, 11, 11, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 15, 15, 15, 15, 1, 1, 1, 15, 15, 15, 15, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 15, 15, 15, 15, 1, 1, 1, 15, 15, 15, 15, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 15, 15, 15, 15, 1, 1, 1, 15, 15, 15, 15, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 11, 12, 11, 1, 1, 1, 11, 12, 11, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 11, 29, 11, 1, 1, 1, 11, 29, 11, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 25, 11, 11, 11, 1, 1, 1, 11, 11, 11, 25, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);

        transitions.add(exit);

        this.map[exitCell] = Terrain.EXIT;
        this.map[amuletCell] = Terrain.PEDESTAL;

        return true;
    }


    @Override
    public void initNpcs() {
        TowerCrossbow1 tower = new TowerCrossbow1();
        tower.pos = amuletCell+WIDTH;
        GameScene.add(tower);
        super.initNpcs();
    }

    public void deployMobs(int wave) {

        if (wave % 5 == 2) deploymobs(wave, Direction.UP, 2);
        else if (wave % 5 == 4) deploymobs(wave, Direction.DOWN, 2);
        else deploymobs(wave, Direction.TOORIGHT, 2);
    }

}
