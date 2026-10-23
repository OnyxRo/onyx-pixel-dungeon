/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * Summoning Pixel Dungeon Reincarnated
 * Copyright (C) 2023-2026 Trashbox Bobylev
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
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.MAX_TALENT_TIERS;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MysteryMerchantSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.CurrencyIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MysteryMerchant extends NPC {

    {
        spriteClass = MysteryMerchantSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );

        if (c != Dungeon.hero){
            return true;
        }

        if (!Stats.hasSeenBefore){
            final String msg1Final = Messages.get(this, "intro");
            final String msg2Final = Messages.get(this, "suggest", Stats.pricing);

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(MysteryMerchant.this, msg1Final){
                        @Override
                        public void hide() {
                            super.hide();
                            offer();
                        }
                    });
                }
            });

            Stats.hasSeenBefore = true;
        } else {
            Game.runOnRenderThread(this::offer);
        }

        return true;
    }

    protected void offer(){
        final String msg2Final = Messages.get(this, "suggest", Stats.pricing);
        GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), msg2Final,
                Messages.get(MysteryMerchant.class, "degrade_talent"),
                Messages.get(MysteryMerchant.class, "transmute_talent")){
            @Override
            protected void onSelect(int index) {
                hide();
                if (index == 0){
                    GameScene.show(new WndDegradeChoose());
                } else if (index == 1){
                    ScrollOfMetamorphosis.merchant = true;
                    GameScene.show(new ScrollOfMetamorphosis.WndMetamorphChoose());
                }
            }

            @Override
            protected boolean enabled(int index) {
                return Dungeon.gold >= Stats.pricing;
            }
        });
    }

    //a metamorphosis bought from the merchant, called by ScrollOfMetamorphosis
    public static void onMetamorphSold( Talent oldTalent, Talent newTalent ){
        for (Char ch : Actor.chars()){
            if (ch instanceof MysteryMerchant){
                ((MysteryMerchant) ch).yell( Messages.get(MysteryMerchant.class, "transmute_complete", oldTalent.title(), newTalent.title() ));
            }
        }
        Stats.handlePurchase();
    }

    //a talent point taken back, called by TalentButton
    public static void degrade( Talent talent ){
        if (WndDegradeChoose.INSTANCE != null){
            WndDegradeChoose.INSTANCE.hide();
        }
        for (LinkedHashMap<Talent, Integer> tier : Dungeon.hero.talents){
            Integer pts = tier.get(talent);
            if (pts != null) tier.put(talent, Math.max(0, pts - 1));
        }
        Dungeon.observe();
        Item.updateQuickslot();
        Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
        Sample.INSTANCE.play(Assets.Sounds.DEGRADE);
        for (Char ch : Actor.chars()){
            if (ch instanceof MysteryMerchant){
                ((MysteryMerchant) ch).yell( Messages.get(MysteryMerchant.class, "degrade_complete", talent.title() ));
            }
        }
        Stats.handlePurchase();
    }

    private static int chapterNumber(){
        return Math.max(Dungeon.depth / 5, 0);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null || Stats.lastChapter < chapterNumber()){
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void yell(String str) {
        super.yell(str);
        CurrencyIndicator.showGold = false;
    }

    @Override
    public void damage(int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static class Stats {

        private static int pricing;
        private static boolean hasSeenBefore;

        private static int lastChapter;
        private static int rooms;

        public static void reset() {
            pricing = 400;
            lastChapter = 0;
            hasSeenBefore = false;
            rooms = 0;
        }

        //one merchant in each set of five floors of the main dungeon, as in the mod
        public static boolean roomNeeded(){
            if (Dungeon.branch != 0 || Dungeon.depth < 2 || Dungeon.depth > 24 || Dungeon.depth % 5 == 0) return false;
            int leftThisSet = 1 - (rooms - (Dungeon.depth / 5));
            if (leftThisSet <= 0) return false;
            int floorThisSet = Dungeon.depth % 5;
            return com.watabou.utils.Random.Int(5 - floorThisSet) < leftThisSet;
        }

        public static void roomPlaced(){
            rooms++;
        }

        private static final String PRICING = "pricing";
        private static final String LAST_CHAPTER = "last_chapter";
        private static final String SEEN = "seen";
        private static final String ROOMS = "merchant_rooms";

        public static void storeInBundle( Bundle bundle ){
            bundle.put(PRICING, pricing);
            bundle.put(LAST_CHAPTER, lastChapter);
            bundle.put(SEEN, hasSeenBefore);
            bundle.put(ROOMS, rooms);
        }

        public static void restoreFromBundle( Bundle bundle ) {
            pricing = bundle.getInt(PRICING);
            lastChapter = bundle.getInt(LAST_CHAPTER);
            hasSeenBefore = bundle.getBoolean(SEEN);
            rooms = bundle.getInt(ROOMS);
            if (!bundle.contains(PRICING)) reset();
        }

        public static void updateChapter(){
            lastChapter = chapterNumber();
        }

        public static void handlePurchase(){
            CurrencyIndicator.showGold = true;
            Dungeon.gold -= Stats.pricing;
            Catalog.countUses(Gold.class, Stats.pricing);
            Stats.incrementPricing();
        }

        private static void incrementPricing(){
            pricing += 200;
        }
    }

    public static class WndDegradeChoose extends Window {

        public static WndDegradeChoose INSTANCE;

        TalentsPane pane;

        public WndDegradeChoose(){
            super();

            INSTANCE = this;

            float top = 0;

            IconTitle title = new IconTitle( new MysteryMerchantSprite(), Messages.get(MysteryMerchant.class, "degrade_talent") );
            title.color( TITLE_COLOR );
            title.setRect(0, 0, 120, 0);
            add(title);

            top = title.bottom() + 2;

            RenderedTextBlock text = PixelScene.renderTextBlock(Messages.get(MysteryMerchant.class, "choose_degrade_desc"), 6);
            text.maxWidth(120);
            text.setPos(0, top);
            add(text);

            top = text.bottom() + 2;

            ArrayList<LinkedHashMap<Talent, Integer>> initTalents = new ArrayList<>();
            Talent.initClassTalents(Dungeon.hero.heroClass, initTalents, Dungeon.hero.metamorphedTalents);
            Talent.initSubclassTalents(Dungeon.hero.subClass, initTalents);
            Talent.initArmorTalents(Dungeon.hero.armorAbility, initTalents);

            ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
            while (talents.size() < MAX_TALENT_TIERS){
                talents.add(new LinkedHashMap<>());
            }

            for (int i = 0; i < initTalents.size(); i++){
                for (Talent talent : initTalents.get(i).keySet()) {
                    if (Dungeon.hero.pointsInTalent(talent) != 0)
                        talents.get(i).put(talent, Dungeon.hero.pointsInTalent(talent));
                }
            }

            pane = new TalentsPane(TalentButton.Mode.MYSTERY_DEGRADE, talents);
            add(pane);
            pane.setPos(0, top);
            pane.setSize(120, pane.content().height());
            resize((int)pane.width(), (int)pane.bottom());
            pane.setPos(0, top);
        }

        @Override
        public void hide() {
            super.hide();
            INSTANCE = null;
        }

        @Override
        public void offset(int xOffset, int yOffset) {
            super.offset(xOffset, yOffset);
            pane.setPos(pane.left(), pane.top()); //triggers layout
        }
    }
}
