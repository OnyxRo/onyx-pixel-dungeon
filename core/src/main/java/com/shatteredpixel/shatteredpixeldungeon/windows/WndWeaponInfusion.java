/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.ElementalStone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Air;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Earth;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Fire;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Water;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;


public class WndWeaponInfusion extends Window {

    private static final int WIDTH		= 120;
    private static final int BTN_HEIGHT	= 18;
    private static final float GAP		= 2;

    public WndWeaponInfusion(Item item) {

        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon( new ItemSprite(ItemSpriteSheet.STONE_BLINK, null ) );
        titlebar.label("Offensive Elemental Infusion");
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock textBlock = PixelScene.renderTextBlock( 6 );
        textBlock.text( "What element do you want to infuse your weapon with?\n\n", WIDTH );
        textBlock.setPos( titlebar.left(), titlebar.bottom() + GAP );
        add( textBlock );

        RedButton btnFire = new RedButton( Messages.get(this,"fire")) {
            @Override
            protected void onClick() {
                hide();
                ((Weapon)item).enchant(new Fire());
                ElementalStone.consume();
            }
        };
        btnFire.setRect( 0, textBlock.bottom() + GAP, WIDTH, BTN_HEIGHT );
        add( btnFire );

        RedButton btnAir = new RedButton( Messages.get(this, "air" )) {
            @Override
            protected void onClick() {
                hide();
                ((Weapon)item).enchant(new Air());
                ElementalStone.consume();
            }
        };
        btnAir.setRect( 0, btnFire.bottom() + GAP, WIDTH, BTN_HEIGHT );
        add( btnAir );

        RedButton btnWater = new RedButton( Messages.get(this, "water" )) {
            @Override
            protected void onClick() {
                hide();
                ((Weapon)item).enchant(new Water());
                ElementalStone.consume();
            }
        };
        btnWater.setRect( 0, btnAir.bottom() + GAP, WIDTH, BTN_HEIGHT );
        add( btnWater );

        RedButton btnEarth = new RedButton( Messages.get(this, "earth" )) {
            @Override
            protected void onClick() {
                hide();
                ((Weapon)item).enchant(new Earth());
                ElementalStone.consume();
            }
        };
        btnEarth.setRect( 0, btnWater.bottom() + GAP, WIDTH, BTN_HEIGHT );
        add( btnEarth );


        RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        btnCancel.setRect( 0, btnEarth.bottom() + (GAP * 2), WIDTH, BTN_HEIGHT );
        add( btnCancel );

        resize( WIDTH, (int)btnCancel.bottom() );


        IconButton btnInfoFire = new IconButton(Icons.get(Icons.INFO)) {
            @Override
            protected void onClick() {
                GameScene.show(new WndMessage(new Fire().desc()));
            }
        };
        btnInfoFire.setRect(WIDTH-20, btnFire.top() + (btnFire.height()-20)/2, 20, 20);
        add(btnInfoFire);

        IconButton btnInfoAir = new IconButton(Icons.get(Icons.INFO)) {
            @Override
            protected void onClick() {
                GameScene.show(new WndMessage(new Air().desc()));
            }
        };
        btnInfoAir.setRect(WIDTH-20, btnAir.top() + (btnAir.height()-20)/2, 20, 20);
        add(btnInfoAir);

        IconButton btnWaterInfo = new IconButton(Icons.get(Icons.INFO)) {
            @Override
            protected void onClick() {
                GameScene.show(new WndMessage(new Water().desc()));
            }
        };
        btnWaterInfo.setRect(WIDTH-20, btnWater.top() + (btnWater.height()-20)/2, 20, 20);
        add(btnWaterInfo);

        IconButton btnEarthInfo = new IconButton(Icons.get(Icons.INFO)) {
            @Override
            protected void onClick() {
                GameScene.show(new WndMessage(new Earth().desc()));
            }
        };
        btnEarthInfo.setRect(WIDTH-20, btnEarth.top() + (btnEarth.height()-20)/2, 20, 20);
        add(btnEarthInfo);


    }
}
