/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Half Ripper quest from Pixel Dungeon Reforged, adapted
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HalfRipperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHalfRipperRewards;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Pixel Dungeon Reforged's escort quest. A half turned ripper demon on floor 24 asks to be taken up the stairs
 * to floor 20, one floor at a time. On the way it follows the hero and fights beside them. At floor 20 it gives
 * a choice of six rewards. Leaving a floor without it fails the quest, and the ripper comes back as an enemy.
 * Reforged's combat system and damage types are not here, so its stats sit on Shattered's own.
 */
public class HalfRipper extends NPC {

    {
        spriteClass = HalfRipperSprite.class;

        HP = HT = 60;
        defenseSkill = 16;
        EXP = 1;
        maxLvl = 29;
        viewDistance = 8;

        properties.add(Property.DEMONIC);

        WANDERING = new HalfRipperWandering();
        state = WANDERING;
    }

    //where the ripper is heading to when it follows the hero
    private int followPos = -1;

    protected class HalfRipperWandering extends Mob.Wandering {
        @Override
        protected int randomDestination() {
            if (Dungeon.depth == 20) {
                return pos;
            }

            int cell = super.randomDestination();
            //cannot wander onto heaps or the level exit, and stays in the exit room if it has one
            if (cell < 0 || Dungeon.level.heaps.get(cell) != null || cell == Dungeon.level.exit()) {
                return -1;
            }
            if (Dungeon.level instanceof RegularLevel) {
                Room exitRoom = ((RegularLevel) Dungeon.level).GetExitRoom();
                if (exitRoom != null && !exitRoom.inside(Dungeon.level.cellToPoint(cell))) {
                    return -1;
                }
            }
            return cell;
        }

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if (Quest.started() && !Quest.failed && !Quest.completed()) {
                state = new HalfRipperEscaping();
                alignment = Alignment.ALLY;
                return true;
            }
            return super.act(enemyInFOV, justAlerted);
        }
    }

    protected class HalfRipperEscaping implements AiState {

        public static final String TAG	= "ESCAPING";

        private boolean goingForStairs = false;

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            int stairs = Dungeon.level.entrance();
            if (pos == stairs) {
                if (Quest.corrupted && Dungeon.depth != 24) {
                    yell(Messages.get(HalfRipper.class, "ascend_corrupted"));
                }
                else {
                    yell(Messages.get(HalfRipper.class, "ascend"));
                }
                ascend();
                return true;
            }

            int oldPos = pos;
            followPos = Dungeon.hero.pos;
            PathFinder.buildDistanceMap(stairs, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

            if (PathFinder.distance[pos] <= 5){
                if (getCloser(stairs)) {
                    if (!goingForStairs) {
                        if (Quest.corrupted && Dungeon.depth != 24) {
                            yell(Messages.get(HalfRipper.class, "stairs_corrupted"));
                        }
                        else {
                            yell(Messages.get(HalfRipper.class, "stairs"));
                        }
                        goingForStairs = true;
                    }
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                }
            }

            goingForStairs = false;
            // if we get here, we couldn't find or move towards the stairs. Follow the hero instead
            if (getCloser( followPos )) {
                spend( 1 / speed() );
                return moveSprite( oldPos, pos );
            } else {
                //if it can't move closer to hero, then try to attack something nearby
                ArrayList<Char> enemies = new ArrayList<>();
                for (Mob otherMob : Dungeon.level.mobs) {
                    if (otherMob.alignment == Alignment.ENEMY && Dungeon.level.adjacent( pos, otherMob.pos )
                            && otherMob.invisible <= 0 && !otherMob.isInvulnerable(getClass()) && !isCharmedBy(otherMob)
                            && otherMob.state != otherMob.PASSIVE) {
                        enemies.add(otherMob);
                    }
                }
                if (!enemies.isEmpty()) {
                    doAttack(enemies.get(Random.Int(enemies.size())));
                }
                spend( TICK );
            }
            return true;
        }
    }

    @Override
    public Notes.Landmark landmark() {
        return (!Quest.completed() && !Quest.failed()) ? Notes.Landmark.HALF_RIPPER : null;
    }

    public void ascend() {
        Notes.remove(Notes.Landmark.HALF_RIPPER, Quest.depth());
        if (Dungeon.depth == 21) {
            // Finished!
            Statistics.questScores[4] += 2000;
            Quest.complete();
        } else {
            Statistics.questScores[4] += 1000;
            Quest.depth--;
        }
        destroy();
        if (sprite != null) ((HalfRipperSprite)sprite).ascend();
    }

    private Item[] generateRewards() {
        Item[] toReturn = new Item[6];

        // Melee weapons
        MeleeWeapon w1 = (MeleeWeapon) Generator.random(Generator.Category.WEP_T5);
        MeleeWeapon w2;
        do {
            w2 = (MeleeWeapon) Generator.random(Generator.Category.WEP_T5);
        } while(w2.isSimilar(w1));

        toReturn[0] = upgradeRewardWeapon(w1);
        toReturn[1] = upgradeRewardWeapon(w2);

        // Armour
        PlateArmor armour = new PlateArmor();
        armour.level(0);
        armour.cursed = false;

        // 20%: +4, 50%: +5, 30%: +6
        float itemLevelRoll = Random.Float();
        if (itemLevelRoll < 0.2f){
            armour.upgrade(4);
        } else if (itemLevelRoll < 0.7f){
            armour.upgrade(5);
        } else {
            armour.upgrade(6);
        }

        float itemEnchantRoll = Random.Float();
        if (itemEnchantRoll < 0.5f) {
            armour.inscribe(Armor.Glyph.randomUncommon());
        } else {
            armour.inscribe(Armor.Glyph.randomRare());
        }
        toReturn[2] = armour;

        // Artifact
        toReturn[3] = Generator.randomArtifact();
        if (toReturn[3] == null) {
            toReturn[3] = Generator.random(Generator.Category.RING);
        }
        toReturn[3].level(0);

        itemLevelRoll = Random.Float();
        if (toReturn[3] instanceof EtherealChains || toReturn[3] instanceof TimekeepersHourglass) {
            if (itemLevelRoll < 0.5f){
                toReturn[3].upgrade(2);
            } else {
                toReturn[3].upgrade(3);
            }
        } else if (toReturn[3] instanceof SandalsOfNature) {
            toReturn[3].upgrade(2);
        } else {
            if (itemLevelRoll < 0.2f) {
                toReturn[3].upgrade(4);
            } else if (itemLevelRoll < 0.7f) {
                toReturn[3].upgrade(5);
            } else {
                toReturn[3].upgrade(6);
            }
        }

        // Ring
        do {
            toReturn[4] = Generator.random(Generator.Category.RING);
        } while(toReturn[4].isSimilar(toReturn[3]));
        toReturn[4].level(0);
        itemLevelRoll = Random.Float();
        if (itemLevelRoll < 0.2f){
            toReturn[4].upgrade(4);
        } else if (itemLevelRoll < 0.7f){
            toReturn[4].upgrade(5);
        } else {
            toReturn[4].upgrade(6);
        }

        // Scrolls of Transmutation
        toReturn[5] = new ScrollOfTransmutation().quantity(5);

        for (Item item : toReturn) {
            item.identify(false);
        }

        return toReturn;
    }

    private MeleeWeapon upgradeRewardWeapon(MeleeWeapon input) {
        input.level(0);
        input.enchant(null);
        input.cursed = false;

        // 20%: +4, 50%: +5, 30%: +6
        float itemLevelRoll = Random.Float();
        int itemLevel;
        if (itemLevelRoll < 0.2f){
            itemLevel = 4;
        } else if (itemLevelRoll < 0.7f){
            itemLevel = 5;
        } else {
            itemLevel = 6;
        }
        input.upgrade(itemLevel);

        float itemEnchantRoll = Random.Float();
        if (itemEnchantRoll < 0.5f) {
            input.enchant(Weapon.Enchantment.randomUncommon());
        } else {
            input.enchant(Weapon.Enchantment.randomRare());
        }

        return input;
    }

    public void forceKill() {
        destroy();
        if (sprite != null) {
            ((HalfRipperSprite) sprite).killInstant();
        }
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null || Quest.failed){
            die(null);
            Notes.remove( landmark() );
            return true;
        }
        Quest.pos = pos;
        Quest.hp = HP;
        return super.act();
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero) {
            return true;
        }

        if (Quest.completed()) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndHalfRipperRewards(generateRewards()));
                }
            });
        }
        else if (!Quest.given) {
            String msg = "";
            switch (Dungeon.hero.heroClass){
                case WARRIOR:   msg += Messages.get(HalfRipper.this, "intro_quest_warrior"); break;
                case MAGE:      msg += Messages.get(HalfRipper.this, "intro_quest_mage"); break;
                case ROGUE:     msg += Messages.get(HalfRipper.this, "intro_quest_rogue"); break;
                case HUNTRESS:  msg += Messages.get(HalfRipper.this, "intro_quest_huntress"); break;
                case DUELIST:   msg += Messages.get(HalfRipper.this, "intro_quest_duelist"); break;
                case CLERIC:    msg += Messages.get(HalfRipper.this, "intro_quest_cleric"); break;
                default:        msg += Messages.get(HalfRipper.this, "intro_quest_warrior"); break;
            }
            msg += "\n\n" + Messages.get(HalfRipper.this, "intro_quest");

            final String msgFinal = msg;
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(HalfRipper.this, msgFinal) {
                        @Override
                        public void hide() {
                            super.hide();

                            HalfRipper.Quest.given = true;
                            HalfRipper.Quest.completed = false;
                            HalfRipper.Quest.started = false;
                            HalfRipper.Quest.failed = false;
                        }
                    } );
                }
            });
        } else if (!Quest.started) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(HalfRipper.this, Messages.get(HalfRipper.this, "quest_started")) {
                        @Override
                        public void hide() {
                            super.hide();
                            HalfRipper.Quest.started = true;
                            HalfRipper.this.state = new HalfRipperEscaping();
                            HalfRipper.this.alignment = Alignment.ALLY;
                            Quest.depth = 24;
                        }
                    } );
                }
            });
        } else {
            return super.interact(c);
        }

        return true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 15 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*0.5f;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }

    @Override
    public void damage( int dmg, Object src ) {
        if (!Quest.started()) {
            return;
        }
        if (dmg > 0) {
            if (Quest.corrupted() && Quest.depth != 24) {
                yell(Messages.get(this, "take_damage_corrupted"));
            } else {
                yell(Messages.get(this, "take_damage"));
            }
        }
        super.damage(dmg, src);
    }

    @Override
    public boolean add( Buff buff ) {
        if (alignment == Alignment.ALLY) {
            return super.add(buff);
        }
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static class Quest {

        //quest state information
        private static boolean spawned;
        private static boolean given;
        private static boolean started;
        private static boolean failed;
        private static boolean completed;
        private static boolean givenRewards;
        public static void SetRewardsGiven() {
            givenRewards = true;
            checkNeedToKill();
        }
        private static int depth;
        private static int hp;
        private static boolean corrupted;
        private static int pos;
        private static boolean spawnedAbandonedRipper;
        private static boolean abandoned;

        private static final String NODE	= "halfripper";

        private static final String SPAWNED		= "spawned";
        private static final String GIVEN		= "given";
        private static final String STARTED		= "started";
        private static final String FAILED  	= "failed";
        private static final String ABANDONED   = "abandoned";
        private static final String COMPLETED	= "completed";
        private static final String DEPTH       = "depth";
        private static final String HP          = "hp";
        private static final String CORRUPTED   = "corrupted";
        private static final String GIVEN_REWARDS = "given_rewards";
        private static final String POS         = "pos";
        private static final String SPAWNED_ABANDONED_RIPPER = "spawned abandoned ripper";

        public static void storeInBundle( Bundle bundle ) {

            Bundle node = new Bundle();

            node.put( SPAWNED, spawned );

            if (spawned) {
                node.put( GIVEN, given );
                node.put( STARTED, started );
                node.put( FAILED, failed );
                node.put( COMPLETED, completed );
                node.put( DEPTH, depth );
                node.put( HP, hp );
                node.put( CORRUPTED, corrupted );
                node.put( POS, pos );
                node.put( SPAWNED_ABANDONED_RIPPER, spawnedAbandonedRipper);
                node.put( ABANDONED, abandoned);
                node.put( GIVEN_REWARDS, givenRewards);
            }

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {

            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
                given = node.getBoolean( GIVEN );
                started = node.getBoolean( STARTED );
                failed = node.getBoolean( FAILED );
                completed = node.getBoolean( COMPLETED );
                depth = node.getInt( DEPTH );
                hp = node.getInt( HP );
                corrupted = node.getBoolean( CORRUPTED );
                pos = node.getInt(POS);
                spawnedAbandonedRipper = node.getBoolean(SPAWNED_ABANDONED_RIPPER);
                abandoned = node.getBoolean(ABANDONED);
                givenRewards = node.getBoolean(GIVEN_REWARDS);
            } else {
                reset();
            }
        }

        public static void reset() {
            given = false;
            started = false;
            spawned = false;
            failed = false;
            completed = false;
            corrupted = false;
            depth = -1;
            hp = -1;
            pos = -1;
            spawnedAbandonedRipper = false;
            abandoned = false;
            givenRewards = false;
        }

        private static boolean ripperOnLevel( Level level ) {
            if (level.mobs != null) {
                for (Mob m : level.mobs) {
                    if (m instanceof HalfRipper) return true;
                }
            }
            return false;
        }

        //a free open cell, inside the room when there is one. Gives up after many tries and takes any open cell.
        private static int openCell( Level level, Room room ) {
            int cell = -1;
            for (int tries = 0; tries < 200; tries++) {
                Point p = room != null && tries < 100 ? room.random() : level.cellToPoint(Random.Int(level.length()));
                cell = level.pointToCell(p);
                if (cell < 0 || cell >= level.length()) { cell = -1; continue; }
                if (!level.solid[cell] && level.passable[cell] && level.openSpace[cell]
                        && cell != level.exit() && cell != level.entrance() && level.findMob(cell) == null) {
                    return cell;
                }
            }
            return -1;
        }

        /**
         * Called whenever the hero arrives on a floor (Dungeon.switchLevel), for floors that were just built
         * and for floors that were loaded. Puts the ripper where the quest needs it, once.
         */
        public static void spawn( Level level ) {
            if (Dungeon.branch != 0 || level == null) return;

            Room exitRoom = level instanceof RegularLevel ? ((RegularLevel) level).GetExitRoom() : null;

            if (!Quest.spawned && Dungeon.depth == 24) {

                int cell = openCell(level, exitRoom);
                if (cell == -1) return;

                HalfRipper hr = new HalfRipper();
                hr.pos = cell;
                level.mobs.add( hr );

                spawned = true;
                given = false;
                started = false;
                completed = false;
                failed = false;
                corrupted = (Random.Int(6) == 0);

            } else if (Quest.completed() && Dungeon.depth == 20 && !givenRewards) {
                if (!ripperOnLevel(level)) {
                    //beside the entrance, where the hero arrives
                    int cell = -1;
                    for (int n : PathFinder.NEIGHBOURS8) {
                        int c = level.entrance() + n;
                        if (c >= 0 && c < level.length() && level.passable[c] && !level.solid[c] && level.findMob(c) == null) {
                            cell = c;
                            break;
                        }
                    }
                    if (cell == -1) return;
                    HalfRipper hr = new HalfRipper();
                    hr.pos = cell;
                    hr.HP = Math.max(1, Quest.hp);
                    level.mobs.add( hr );
                }
            } else if (Quest.spawned && Dungeon.depth == Quest.depth && Quest.started() && !failed && !completed) {
                if (!ripperOnLevel(level)) {
                    HalfRipper hr = new HalfRipper();
                    hr.pos = level.exit(); // when the player goes up, they will bump us out of the way
                    hr.HP = Math.max(1, Quest.hp);
                    hr.alignment = Char.Alignment.ALLY;
                    hr.state = hr.new HalfRipperEscaping();
                    level.mobs.add( hr );
                }
            } else if (Quest.abandoned && !Quest.spawnedAbandonedRipper && Dungeon.depth == Quest.depth) {
                Quest.spawnedAbandonedRipper = true;
                RipperDemon theOneYouAbandonedYouMonster = new RipperDemon();
                theOneYouAbandonedYouMonster.HP = Math.max(1, hp);
                theOneYouAbandonedYouMonster.pos = Quest.pos >= 0 ? Quest.pos : level.exit();
                theOneYouAbandonedYouMonster.state = theOneYouAbandonedYouMonster.HUNTING;
                level.mobs.add(theOneYouAbandonedYouMonster);
            }
        }

        //Reforged's twist. A corrupted ripper that was escorted out waits on floor 1 when the hero climbs back with the Amulet.
        public static void spawnCorruptedRipper( Level level ) {
            if (Dungeon.depth != 1 || !corrupted || !completed()) return;
            int cell = openCell(level, level instanceof RegularLevel ? ((RegularLevel) level).GetEntranceRoom() : null);
            if (cell == -1) return;
            RipperDemon r = new RipperDemon();
            r.pos = cell;
            r.state = r.HUNTING;
            level.mobs.add(r);
        }

        public static String GetAbandonTitleKey() {
            if (corrupted && depth != 24) {
                return "abandon_title_corrupted";
            }
            return "abandon_title";
        }

        public static String GetAbandonDescriptionKey(boolean isGoingUp) {
            if (isGoingUp) {
                if (corrupted && depth != 24) {
                    return "abandon_up_corrupted_desc";
                }
                return "abandon_up_desc";
            }
            if (corrupted && depth != 24) {
                return "abandon_down_corrupted_desc";
            }
            return "abandon_down_desc";
        }

        public static void checkNeedToKill() {
            if (Quest.started() && (Quest.failed() || Quest.completed())) {
                if (Dungeon.depth == 20 && !Quest.givenRewards) {
                    return;
                }
                if (Dungeon.level != null && Dungeon.level.mobs != null) {
                    for (Mob m : Dungeon.level.mobs) {
                        if (m instanceof HalfRipper) {
                            HalfRipper hr = (HalfRipper) m;
                            hr.forceKill();
                            break;
                        }
                    }
                }
            }
        }

        public static void abandon() {
            GLog.newLine();
            if (Quest.corrupted) {
                GLog.n("%s", Messages.get(HalfRipper.class, "abandon_corrupted_reaction"));
            }
            else {
                GLog.n("%s: \"%s\" ", Messages.titleCase(Messages.get(HalfRipper.class, "name")), Messages.get(HalfRipper.class, "abandon_reaction"));
            }
            Quest.failed = true;
            for (int i = 21; i <= 25; ++i) {
                Notes.remove(Notes.Landmark.HALF_RIPPER, i);
            }
            Quest.abandoned = true;
        }

        public static boolean given() { return given; }
        public static boolean started() { return started; }
        public static void start() { started = true; }
        public static boolean completed() { return given && completed; }
        public static boolean failed() { return failed; }
        public static void complete() {
            completed = true;
            depth = 20;
            pos = -1;
        }
        public static int depth() { return depth; }

        public static boolean corrupted() { return corrupted; }
    }
}
