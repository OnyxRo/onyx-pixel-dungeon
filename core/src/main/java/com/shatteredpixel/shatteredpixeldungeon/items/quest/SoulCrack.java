package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class SoulCrack extends Item {

    {
        image = ItemSpriteSheet.SOUL_CRACK_A;
        cursed = false;
        unique = true;
        stackable = true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF4444, 3f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static class RedSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_A;
        }
    }

    public static class YellowSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_B;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(0xFFFF44, 3f);
        }
    }

    public static class BlueSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_C;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(0x4488FF, 3f);
        }
    }

    public static class GreenSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_D;
        }
        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(0x44FF44, 3f);
        }
    }


    public static class PinkSoulCrack extends SoulCrack {
        {
            image = ItemSpriteSheet.SOUL_CRACK_E;
        }
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(0xFF88CC, 3f);
        }
    }

}
