package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class MegaStrengthPotion extends Item {
    {

        bones = false;

        image = ItemSpriteSheet.POTION_AZURE;
    }

    @Override
    public String name() {
        return "MEGA Potion of Strength";
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public static final String AC_DRINK = "DRINK";
    public void apply(Hero hero) {

        hero.STR += 10;
        hero.increaseMagicSkill(10);
        hero.sprite.showStatus( CharSprite.POSITIVE, "+10 STR" );
        hero.sprite.showStatus( CharSprite.POSITIVE, "+10 Magic Skill" );
        GLog.p( "You feel a rush of power in your body..." );

        Badges.validateStrengthAttained();
        detach(hero.belongings.backpack);
    }

    @Override
    public String desc() {
        return "Drink this for cool stuff like much strength. It is good.";
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DRINK);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals(AC_DRINK)) {
            apply(hero);
        } else {
            super.execute(hero,action);
        }
    }
}