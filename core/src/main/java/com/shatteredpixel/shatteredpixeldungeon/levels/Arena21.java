package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossRatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Brute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollBlind;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollThrower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SkeletonArmored;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SkeletonArmoredShielded;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlmostEmptySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BossRatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Arena21 extends ArenaHalls {

    {
        name = "Collapsed halls";
        WIDTH = 60;
        HEIGHT = 60;
        viewDistance = 11;

        startGold = 3000;
        startLvl = 20;
        arenaDepth = 21;
        maxWaves = 14;

        normalShopKeeper.vertical = NewShopKeeper.ShopDirection.DOWN;
        towerShopKeeper.vertical = NewShopKeeper.ShopDirection.RIGHT;

        amuletCell = 29 + WIDTH*30;
        exitCell = amuletCell;
        towerShopKeeperCell = 23 + 23*WIDTH;
        normalShopKeeperCell = 21 + 25*WIDTH;

        waveCooldownNormal = 30;
        waveCooldownBoss = 200;
    }
    @Override
    protected boolean build() {


        setSize(WIDTH, HEIGHT);

        map = new int[]{
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 20, 1, 1, 1, 20, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 20, 1, 1, 20, 20, 20, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 1, 25, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 25, 1, 1, 1, 25, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 1, 25, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 25, 1, 1, 1, 25, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 4, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 1, 4, 4, 4, 4, 1, 1, 4, 1, 1, 1, 1, 4, 4, 4, 1, 1, 1, 4, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 4, 1, 1, 1, 1, 4, 1, 4, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 13, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 1, 4, 1, 4, 1, 4, 1, 1, 4, 1, 1, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 13, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 1, 4, 4, 1, 1, 13, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 4, 1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 13, 1, 4, 4, 4, 1, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 4, 1, 4, 1, 1, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 4, 1, 1, 1, 1, 11, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 4, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 1, 11, 1, 4, 4, 1, 4, 1, 4, 1, 1, 1, 4, 1, 1, 1, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 1, 1, 4, 1, 1, 4, 1, 1, 1, 13, 1, 1, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 11, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 13, 1, 1, 1, 1, 1, 11, 1, 1, 4, 4, 1, 4, 1, 4, 1, 4, 4, 4, 1, 4, 4, 13, 4, 4, 1, 4, 29, 29, 15, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 11, 11, 11, 11, 11, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 15, 15, 29, 15, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 26, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 1, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 15, 15, 15, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 29, 29, 29, 29, 29, 1, 1, 1, 1, 1, 11, 1, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1, 1, 11, 11, 11, 11, 11, 11, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 1, 29, 1, 1, 1, 1, 14, 14, 14, 14, 14, 14, 14, 1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 4, 4, 21, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 11, 29, 29, 29, 0, 1, 1, 1, 1, 1, 1, 1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 1, 14, 14, 14, 14, 14, 14, 14, 14, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 1, 1, 29, 1, 1, 1, 1, 1, 1, 1, 11, 1, 11, 11, 11, 11, 1, 1, 11, 11, 11, 11, 11, 11, 11, 11, 1, 11, 11, 11, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 29, 1, 29, 29, 1, 1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 29, 1, 29, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 26, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 13, 1, 4, 13, 1, 1, 1, 1, 4, 4, 4, 1, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 1, 4, 1, 4, 1, 4, 1, 4, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 4, 1, 4, 1, 1, 4, 1, 4, 1, 4, 1, 1, 1, 13, 4, 1, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 4, 1, 1, 1, 4, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 4, 4, 1, 1, 1, 4, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 4, 1, 4, 1, 1, 13, 1, 4, 1, 1, 1, 4, 4, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 13, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1, 1, 4, 4, 1, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 1, 1, 4, 1, 4, 1, 4, 1, 4, 1, 1, 4, 1, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 13, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 4, 4, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 25, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 25, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 25, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 4, 1, 1, 4, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 4, 4, 1, 1, 1, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4
        };

        buildFlagMaps();

        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        this.map[exitCell] = Terrain.EXIT;
        this.map[amuletCell] = Terrain.PEDESTAL;

        return true;
    }
    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.HALLS_1, Assets.Music.HALLS_2},
                new float[]{1, 1},
                false);
    }
    @Override
    public Mob chooseMob(int wave) {
        Mob mob = new Rat();
        switch (wave){
            case 1:
                mob = new Ghoul(); break;
            case 2:
                mob = new RipperDemon(); break;
            case 3:
                mob = new Skeleton(); break;
            case 4:
                mob = new RipperDemon(); break;
            case 5:
                mob = new Senior(); break;
            case 6:
                if (!bossSpawned) {
                    bossSpawned = true;
                    mob = new Succubus();
                } else mob = new RipperDemon();
                break;
            case 7:
                mob = new SkeletonArmored(); break;
            case 8:
                mob = Random.oneOf(new Succubus(), new RipperDemon()); break;
            case 9:
                mob = new Ghoul(); break;
            case 10:
                mob = new Succubus();
                break;
            case 11:
                mob = new SkeletonArmoredShielded(); break;
            case 12:
                mob = new RipperDemon();
                break;
            case 13:
                mob = new Ghoul();
                break;
            case 14:
                if (!bossSpawned) {
                    bossSpawned = true;
                    mob = new Eye();
                } else mob = new RipperDemon();
                break;
        }
        if (mode == WndModes.Modes.CHALLENGE){
            affectMob(mob);
        }
        return mob;
    }
    @Override
    public int mobsToDeploy(int wave) {
        switch (wave){
            case 1: return 3;
            case 2: return 3;
            case 3: return 15;
            case 4: return 4;
            case 5: return 4;
            case 6: return 5;
            case 7: return 6;
            case 8: return 6;
            case 9: return 5;
            case 10: return 10;
            case 11: return 6;
            case 12: return 15;
            case 13: return 8;
            case 14: return 20;
        } return 1;
    }

    @Override
    public void doStuffEndwave(int wave) {
        int goldAdd = 400;
        Dungeon.gold+=goldAdd;
        GLog.w(Messages.get(Arena.class, "goldaddendwave", goldAdd));
        super.doStuffEndwave(wave);
    }

    public void deployMobs(int wave) {
        if (wave % 2 == 0)
            deploymobs(wave, Direction.EXACTLYRIGHT, 1);
        else if (wave % 4 == 1) deploymobs(wave, Direction.EXACTLYUP, 1);
        else if (wave % 4 == 3) deploymobs(wave, Direction.EXACTLYDOWN, 1);
    }

    @Override
    public void addDestinations() {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int m = 0; m<WIDTH*HEIGHT;m++){
            if (passable[m] && !cellAdjacentToBorderCells(m) && distance(m, amuletCell)>10) candidates.add(m);
        }

        for (int i = 0;i < 10; i++) dropMany(Heap.Type.SKELETON, candidates,
                Generator.random(Generator.Category.POTION),
                Generator.random(Generator.Category.SCROLL),
                Generator.random(Generator.Category.STONE),
                Generator.random(Generator.Category.WEAPON)
        );
        super.addDestinations();
    }
}

