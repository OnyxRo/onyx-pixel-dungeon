package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class RBAGunSmithingTool extends Item {

    public static final String AC_USE		= "USE";

    {
        image = ItemSpriteSheet.RBA_GUNSMITHING_TOOL;
        defaultAction = AC_USE;
        stackable = true;

        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_USE)) {
            GameScene.selectItem( itemSelector );
        }
    }

    private String inventoryTitle(){
        return Messages.get(this, "inv_title");
    }

    protected Class<?extends Bag> preferredBag = Belongings.Backpack.class;

    protected boolean usableOnItem( Item item ){
        return item instanceof RBAGun;
    }

    protected static void onItemSelected() {
        curUser.busy();
        curUser.spend( 1f );
        (curUser.sprite).operate( curUser.pos );

        Sample.INSTANCE.play( Assets.Sounds.EVOKE );
        CellEmitter.center( curUser.pos ).burst( Speck.factory( Speck.STAR ), 7 );
        Invisibility.dispel();

        updateQuickslot();

        Item tool = Dungeon.hero.belongings.getItem(RBAGunSmithingTool.class);
        if (tool != null) {
            tool.detach(Dungeon.hero.belongings.backpack);
        }
    };

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30*quantity;
    }

    public static class WndModSelect extends WndOptions {

        private static RBAGun gun;
        private static String[] mods = {"barrel", "magazine", "bullet", "weight", "attach", "enchant"};

        public WndModSelect(RBAGun gun){
            super(new ItemSprite(new RBAGunSmithingTool()),
                    Messages.titleCase(new RBAGunSmithingTool().name()),
                    Messages.get(RBAGunSmithingTool.class, "mod_select"),
                    Messages.get(RBAGunSmithingTool.class, mods[0]),
                    Messages.get(RBAGunSmithingTool.class, mods[1]),
                    Messages.get(RBAGunSmithingTool.class, mods[2]),
                    Messages.get(RBAGunSmithingTool.class, mods[3]),
                    Messages.get(RBAGunSmithingTool.class, mods[4]),
                    Messages.get(RBAGunSmithingTool.class, mods[5]),
                    Messages.get(RBAGunSmithingTool.class, "cancel"));
            this.gun = gun;
        }

        @Override
        protected void onSelect(int index) {
            if (index < 6) {
                GameScene.show(new WndMod(gun, mods[index]));
            } else {
                hide();
            }
        }

        @Override
        protected boolean hasInfo(int index) {
            return index < 6;
        }

        @Override
        protected void onInfo( int index ) {
            GameScene.show(new WndTitledMessage(
                    Icons.get(Icons.INFO),
                    Messages.titleCase(Messages.get(RBAGunSmithingTool.class, mods[index])),
                    Messages.get(RBAGunSmithingTool.class, mods[index] + "_desc")));
        }

    }

    public static class WndMod extends Window {

        protected static final int WIDTH_P = 120;
        protected static final int WIDTH_L = 144;

        private static final int MARGIN 		= 2;
        private static final int BUTTON_HEIGHT	= 20;
        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        public WndMod( final RBAGun toMod, final String key ) {
            super();

            IconTitle titlebar = new IconTitle( toMod );
            titlebar.setRect( 0, 0, width, 0 );
            add( titlebar );

            RenderedTextBlock tfMesage = PixelScene.renderTextBlock( Messages.get(RBAGunSmithingTool.class, key + "_desc"), 6 );
            tfMesage.maxWidth(width - MARGIN * 2);
            tfMesage.setPos(MARGIN, titlebar.bottom() + MARGIN);
            add( tfMesage );

            float pos = tfMesage.top() + tfMesage.height();

            switch (key) {
                case "barrel":
                    for (final RBAGun.BarrelMod barrel : RBAGun.BarrelMod.values()) {
                        if (toMod.barrelMod != barrel) {
                            RedButton btnMod = new RedButton( Messages.get(this, barrel.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.barrelMod=barrel;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
                case "magazine":
                    for (final RBAGun.MagazineMod magazine : RBAGun.MagazineMod.values()) {
                        if (toMod.magazineMod != magazine) {
                            RedButton btnMod = new RedButton( Messages.get(this, magazine.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.magazineMod=magazine;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
                case "bullet":
                    for (final RBAGun.BulletMod bullet : RBAGun.BulletMod.values()) {
                        if (toMod.bulletMod != bullet) {
                            RedButton btnMod = new RedButton( Messages.get(this, bullet.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.bulletMod=bullet;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
                case "weight":
                    for (final RBAGun.WeightMod weight : RBAGun.WeightMod.values()) {
                        if (toMod.weightMod != weight) {
                            RedButton btnMod = new RedButton( Messages.get(this, weight.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.weightMod = weight;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
                case "attach":
                    for (final RBAGun.AttachMod attach : RBAGun.AttachMod.values()) {
                        if (toMod.attachMod != attach) {
                            RedButton btnMod = new RedButton( Messages.get(this, attach.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.attachMod=attach;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
                case "enchant":
                    for (final RBAGun.EnchantMod enchant : RBAGun.EnchantMod.values()) {
                        if (toMod.enchantMod != enchant) {
                            RedButton btnMod = new RedButton( Messages.get(this, enchant.name()) ) {
                                @Override
                                protected void onClick() {
                                    hide();
                                    toMod.enchantMod=enchant;
                                    onItemSelected();
                                }
                            };
                            btnMod.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
                            add( btnMod );

                            pos = btnMod.bottom();
                        }
                    }
                    break;
            }

            RedButton btnCancel = new RedButton( Messages.get(RBAGunSmithingTool.class, "cancel") ) {
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setRect( MARGIN, pos + MARGIN, width - MARGIN * 2, BUTTON_HEIGHT );
            add( btnCancel );

            resize( width, (int)btnCancel.bottom() + MARGIN );
        }
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return inventoryTitle();
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return preferredBag;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
        public void onSelect( Item item ) {

            //FIXME this safety check shouldn't be necessary
            //it would be better to eliminate the curItem static variable.
            if (!(curItem instanceof RBAGunSmithingTool)){
                return;
            }

            if (item != null && itemSelectable(item)) {
                GameScene.show(new WndModSelect((RBAGun)item));
            }
        }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{StoneOfAugmentation.class};
            inQuantity = new int[]{1};

            cost = 4;

            output = RBAGunSmithingTool.class;
            outQuantity = 1;
        }

    }
}
