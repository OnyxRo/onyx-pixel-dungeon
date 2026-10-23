package com.shatteredpixel.shatteredpixeldungeon.actors.hero.console;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBABlackHole;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAFireBall;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAHydropump;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAIceLance;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAThunder;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBACharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAMoveDown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAMoveLeft;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAMoveRight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAStrongAttack;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAMoveUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fighter.RBAWeakAttack;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBABarricadeBuild;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBABridgeBuild;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBAFarming;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBAWallDig;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.sandbox.RBAWellDig;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAConsole;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAFantasyConsole;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBAFighterConsole;
import com.shatteredpixel.shatteredpixeldungeon.items.active.console.RBASandboxConsole;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuConsole;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class RBAYuzuConsoleContent {
    public void onSelect(Hero hero) {
        if (usesTargeting()) {
            GameScene.selectCell(selector);
        } else {
            if (execute(hero, -1)) {
                onContentExecuted(hero);
            }
        }
    };

    public abstract boolean execute(Hero hero, int target);

    public abstract boolean canSelect( Hero hero );

    public boolean usesTargeting() {
        return false;
    }

    public boolean isEnhanced(Hero hero) {
        boolean enhanced = false;
        for (Buff b: hero.buffs()) {
            if (b instanceof ConsoleBuff) {
                enhanced = ((ConsoleBuff) b).isEnhanced();
                if (enhanced) {
                    break;
                }
            }
        }
        return enhanced;
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String desc() {
        if (isEnhanced(Dungeon.hero)) {
            return Messages.get(this, "desc_enhanced") + "\n\n" + Messages.get(this, "token_cost", countUse());
        }
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "token_cost", countUse());
    }

    public int icon(){
        return HeroIcon.NONE;
    }

    public void onContentSelect(RBAConsole console, Hero hero, boolean info) { //버튼을 눌렀을 때 작동
        if (!hideWindow()) GameScene.show(new RBAWndYuzuConsole(console, hero, info));
    }

    public void onContentExecuted(Hero hero) { //컨텐츠 내용을 성공적으로 실행했을 때 작동
        //no nothing by default
    }

    public int countUse() {
        return 1;
    }

    public boolean hideWindow() {
        return false;
    }

    public static ArrayList<RBAYuzuConsoleContent> getContentList(Hero yuzu, RBAConsole console) {
        ArrayList<RBAYuzuConsoleContent> contents = new ArrayList<>();

        if (console instanceof RBAFighterConsole) {
            contents.add(RBAWeakAttack.INSTANCE);
            contents.add(RBAStrongAttack.INSTANCE);
            contents.add(RBACharge.INSTANCE);
            contents.add(RBAMoveLeft.INSTANCE);
            contents.add(RBAMoveUp.INSTANCE);
            contents.add(RBAMoveDown.INSTANCE);
            contents.add(RBAMoveRight.INSTANCE);
        } else if (console instanceof RBAFantasyConsole) {
            contents.add(RBAFireBall.INSTANCE);
            contents.add(RBAHydropump.INSTANCE);
            contents.add(RBAThunder.INSTANCE);
            contents.add(RBAIceLance.INSTANCE);
            contents.add(RBABlackHole.INSTANCE);
        } else if (console instanceof RBASandboxConsole) {
            contents.add(RBAFarming.INSTANCE);
            contents.add(RBABarricadeBuild.INSTANCE);
            contents.add(RBABridgeBuild.INSTANCE);
            contents.add(RBAWallDig.INSTANCE);
            contents.add(RBAWellDig.INSTANCE);
        }

        return contents;
    }

    protected CellSelector.Listener selector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            if (RBAYuzuConsoleContent.this.execute(Dungeon.hero, cell)) {
                RBAYuzuConsoleContent.this.onContentExecuted(Dungeon.hero);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(RBAYuzuConsoleContent.this, "prompt");
        }
    };

    public static class ConsoleBuff extends CounterBuff {
        public static final int MAX_COUNT = 15;
        private static final int SET_COUNT = 10;
        private boolean enhanced = false;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.RBA_CONSOLE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (MAX_COUNT-count())/(float)MAX_COUNT);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString((int)count());
        }

        public void set() {
            countUp(SET_COUNT);
        }

        public void set(int amount) {
            countUp(amount);
        }

        @Override
        public void countUp(float inc) {
            if ((int)count() > MAX_COUNT) return;
            if ((int)count() + inc > MAX_COUNT) inc = MAX_COUNT - count();
            super.countUp(inc);
        }

        @Override
        public void countDown(float inc) {
            super.countDown(inc);
            enhanced = false;
            if (count() <= 0) detach();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int)count());
        }

        public void enhance() {
            enhanced = true;
        }

        public boolean isEnhanced() {
            return enhanced;
        }

        private static final String ENHANCED = "enhanced";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(ENHANCED, enhanced);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            enhanced = bundle.getBoolean(ENHANCED);
        }
    }

}
