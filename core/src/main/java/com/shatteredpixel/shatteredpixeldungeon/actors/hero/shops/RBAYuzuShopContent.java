package com.shatteredpixel.shatteredpixeldungeon.actors.hero.shops;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.active.RBALaptop;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.windows.RBAWndYuzuShop;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class RBAYuzuShopContent {
    public abstract void onSelect(Hero hero);

    public abstract int creditUse(Hero hero);

    public boolean canSelect( Hero hero ){
        return Dungeon.gold >= creditUse(hero) || Dungeon.level instanceof VaultLevel;
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String shortDesc(){
        return Messages.get(this, "short_desc") + ".\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    public String desc(){
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "credit_cost", creditUse(Dungeon.hero));
    }

    public int icon(){
        return HeroIcon.NONE;
    }

    public void beforeContentSelect(Hero hero) {
        if (Dungeon.level instanceof VaultLevel) return;
        Dungeon.gold -= creditUse(hero);
    }

    public void onContentSelect(RBALaptop laptop, Hero hero, boolean info) {
        if (!(Dungeon.level instanceof VaultLevel)) {
            if (hero.hasTalent(Talent.RBA_YUZU_T2_5)) {
                Item i = new Gold(Math.round(creditUse(hero)*0.05f*hero.pointsInTalent(Talent.RBA_YUZU_T2_5)));
                hero.spend(-i.pickupDelay()); //골드 획득에 턴 소모를 제거
                i.doPickUp(hero, hero.pos);
            } else {
                Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
            }
        }
        if (!hideWindow()) GameScene.show(new RBAWndYuzuShop(laptop, hero, info));
    }

    public boolean hideWindow() {
        return false;
    }

    public static int inflationParameter() {
        //based on the depth of deepest floor that has been visited
        return 1+(Math.min(Statistics.deepestFloor, 25)/5);
    }

    public static ArrayList<RBAYuzuShopContent> getContentList(Hero yuzu, int tier) {
        ArrayList<RBAYuzuShopContent> contents = new ArrayList<>();

        if (tier == 1) {
            contents.add(RBARandomConsumable.INSTANCE);
            contents.add(RBARandomEquipment.INSTANCE);
            contents.add(RBASellItem.INSTANCE);
            contents.add(RBAPayToWin.INSTANCE);
            contents.add(RBAQuestTracker.INSTANCE);
        } else if (tier == 2) {
            contents.add(RBABuyCritChance.INSTANCE);
            contents.add(RBABuyCritDmgMulti.INSTANCE);
            contents.add(RBABuyCreditMulti.INSTANCE);
            contents.add(RBABuyDropMulti.INSTANCE);
            contents.add(RBABuySearchChance.INSTANCE);
        } else if (tier == 3) {
            if (yuzu.subClass == HeroSubClass.RBA_GAME_START) {
                contents.add(RBABuyContinueConsole.INSTANCE);
                contents.add(RBABuyFighterConsole.INSTANCE);
                contents.add(RBABuyFantasyConsole.INSTANCE);
                contents.add(RBABuySandboxConsole.INSTANCE);
            }
        } else if (tier == 4) {
            if (yuzu.hasTalent(Talent.RBA_YUZU_ARMOR3_1)) {
                contents.add(RBAInvulnerable.INSTANCE);
            }
            if (yuzu.hasTalent(Talent.RBA_YUZU_ARMOR3_2)) {
                contents.add(RBAInfiniteAmmo.INSTANCE);
            }
            if (yuzu.hasTalent(Talent.RBA_YUZU_ARMOR3_3)) {
                contents.add(RBACreditDuplicate.INSTANCE);
            }
        }

        return contents;
    }

    public static ArrayList<RBAYuzuShopContent> getAllContents() {
        ArrayList<RBAYuzuShopContent> contents = new ArrayList<>();

        return contents;
    }

    public static void showFlareForBonusDrop( Visual vis, int tier ){
        if (vis == null || vis.parent == null) return;
        switch (tier){
            default:
                break; //do nothing
            case 1:
                new Flare(6, 20).color(0x00FF00, true).show(vis, 3f);
                break;
            case 2:
                new Flare(6, 24).color(0x00AAFF, true).show(vis, 3.33f);
                break;
            case 3:
                new Flare(6, 28).color(0xAA00FF, true).show(vis, 3.67f);
                break;
            case 4:
                new Flare(6, 32).color(0xFFAA00, true).show(vis, 4f);
                break;
            case 5:
                new Flare(6, 36).color(0xFF0000, true).show(vis, 4.33f);
                break;
            case 6:
                new Flare(6, 40).color(0x0000FF, true).show(vis, 4.67f);
                break;
        }
    }
}
