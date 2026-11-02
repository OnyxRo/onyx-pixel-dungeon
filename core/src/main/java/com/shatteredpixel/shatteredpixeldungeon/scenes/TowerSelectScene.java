/*
 * Pixel Towers / Tower Pixel Dungeon (ported to Onyx's Pixel Dungeon)
 * Copyright (C) 2024-2025 FixAkaTheFix
 *
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2026 Evan Debenham
 * and Pixel Dungeon, Copyright (C) 2012-2015 Oleg Dolya
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.TowerDefense;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerIcons;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.Scene;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

/**
 * Tower PD's tower selection screen. The list on the left has every tower, open ones first, then the locked
 * ones with the story arena that opens them, then the towers only found inside arenas. Picking one shows its
 * picture and description on the right. Select puts it in the first of the four slots and moves the others down,
 * the tower shop in each arena sells the four slotted towers. Kept in the settings, see TowerDefense.slot.
 */
public class TowerSelectScene extends PixelScene {

	private NinePatch rightPanel;
	private ScrollPane rightScroll;
	private IconTitle towerInfoTitle;
	private RenderedTextBlock towerInfoBody;

	private TowerEquipButton equippingBtn;
	private Image towerPreview = new Image();

	private TowerSlotButton[] slotButtons = new TowerSlotButton[4];

	private int delta = (int)(landscape() ?
			Math.max( Math.min( Math.max( Camera.main.width * 0.08, Camera.main.height * 0.08 ), 34 ), 14 ) :
			Camera.main.width * 0.1);

	@Override
	public void create() {
		super.create();
		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get( this, "uppertext" ), 9 );
		title.hardlight( Window.TITLE_COLOR );
		title.setPos( (w - title.width()) / 2f, (20 - title.height()) / 2f );
		align( title );
		add( title );

		ExitButton btnExit = new ExitButton(){
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		NinePatch panel = Chrome.get( Chrome.Type.TOAST );

		int pw = TowerSelectionButton.WIDTH + panel.marginLeft() + panel.marginRight();
		int ph = h - 36;
		int rpw = Math.min( w - pw - 5 - 5, 200 );

		panel.size( pw, ph );
		panel.x = (w - pw - rpw) / 2;
		panel.y = title.bottom() + 5;

		rightPanel = Chrome.get( Chrome.Type.TOAST );
		rightPanel.size( rpw, ph - delta );
		rightPanel.x = panel.x + panel.width();
		rightPanel.y = panel.y;
		add( rightPanel );

		rightScroll = new ScrollPane( new Component() );
		add( rightScroll );
		rightScroll.setRect(
				rightPanel.x + rightPanel.marginLeft(),
				rightPanel.y + rightPanel.marginTop() - 1,
				rightPanel.innerWidth() + 2,
				rightPanel.innerHeight() + 2 );
		rightScroll.scrollTo( 0, 0 );

		towerInfoTitle = new IconTitle( TowerIcons.TOWER.get(), Messages.get( this, "welcometitle" ) );
		towerInfoTitle.setPos( 0, 1 );
		towerInfoTitle.setSize( rpw - panel.marginHor(), 20 );
		rightScroll.content().add( towerInfoTitle );
		rightScroll.content().add( towerPreview );

		towerInfoBody = PixelScene.renderTextBlock( Messages.get( this, "welcometext" ), 6 );
		towerInfoBody.maxWidth( rpw - panel.marginHor() );
		towerInfoBody.setPos( 0, towerInfoTitle.bottom() + 2 );
		rightScroll.content().add( towerInfoBody );
		rightScroll.content().setSize( rightScroll.width(), towerInfoBody.bottom() + 2 );

		align( panel );
		add( panel );

		final ArrayList<TowerSelectionButton> towerButtonList = new ArrayList<>();
		ArrayList<TowerInfo.AllTowers> unlockedTowers = new ArrayList<>();
		ArrayList<TowerInfo.AllTowers> lockedTowers = new ArrayList<>();
		ArrayList<TowerInfo.AllTowers> dungeonTowers = new ArrayList<>();
		for (TowerInfo.AllTowers t : TowerInfo.AllTowers.values()) {
			switch (TowerInfo.getTowerLock( t )) {
				case UNLOCKED: unlockedTowers.add( t ); break;
				case LOCKED: lockedTowers.add( t ); break;
				case DUNGEON: dungeonTowers.add( t ); break;
				default: break;
			}
		}

		towerButtonList.add( new TowerSelectionButton( TowerInfo.AllTowers.UNLOCKED ) );
		for (TowerInfo.AllTowers t : unlockedTowers) towerButtonList.add( new TowerSelectionButton( t ) );
		for (TowerInfo.AllTowers t : lockedTowers) {
			int lvl = TowerInfo.getTowerUnlockLevel( t );
			towerButtonList.add( new TowerSelectionButton( TowerInfo.AllTowers.LOCKED, lvl == -1 ? "" : Integer.toString( lvl ) ) );
		}
		towerButtonList.add( new TowerSelectionButton( TowerInfo.AllTowers.DUNGEON ) );
		for (TowerInfo.AllTowers t : dungeonTowers) towerButtonList.add( new TowerSelectionButton( t ) );

		ScrollPane list = new ScrollPane( new Component() ){
			@Override
			public void onClick( float x, float y ) {
				for (TowerSelectionButton button : towerButtonList) {
					if (button.onClick( x, y )) return;
				}
			}
		};
		add( list );

		Component content = list.content();
		content.clear();
		int btny = 5;
		for (TowerSelectionButton button : towerButtonList) {
			button.setPos( 0, btny );
			content.add( button );
			btny += button.height();
		}
		content.setSize( panel.innerWidth(), btny );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth() + 2,
				panel.innerHeight() + 2 );
		list.scrollTo( 0, 0 );

		for (int i = 3; i >= 0; i--) {
			slotButtons[i] = new TowerSlotButton( i + 1 );
			slotButtons[i].setSize( delta, delta );
			float right = i == 3 ? rightPanel.x + rightPanel.width() : slotButtons[i + 1].left();
			slotButtons[i].setPos( right - slotButtons[i].width(), rightPanel.y + rightPanel.height() );
			add( slotButtons[i] );
		}

		placeEquipButton( TowerInfo.AllTowers.UNLOCKED );

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	private void placeEquipButton( TowerInfo.AllTowers tower ){
		if (equippingBtn != null) remove( equippingBtn );
		equippingBtn = new TowerEquipButton( tower );
		equippingBtn.setSize( rightPanel.width() - delta * 4, delta );
		equippingBtn.setPos( rightPanel.x, rightPanel.y + rightPanel.height() );
		add( equippingBtn );
	}

	private void updateTowerDescription( TowerInfo.AllTowers chosenTower ) {
		if (chosenTower == null) return;
		towerInfoTitle.label( TowerInfo.getTowerName( chosenTower ) );
		towerInfoTitle.icon( TowerIcons.forTower( chosenTower ) );
		towerInfoTitle.setPos( towerInfoTitle.left(), towerInfoTitle.top() );

		Image preview = TowerInfo.getTowerPreviewImage( chosenTower );
		rightScroll.content().remove( towerPreview );
		towerPreview = preview;
		towerPreview.scale.set( 0.8f * rightPanel.width() / towerPreview.width );
		towerPreview.x = towerInfoTitle.centerX() - towerPreview.width() / 2;
		towerPreview.y = towerInfoTitle.bottom() + 1;
		align( towerPreview );
		rightScroll.content().add( towerPreview );

		placeEquipButton( chosenTower );

		towerInfoBody.setPos( 0, towerPreview.y + towerPreview.height() + 1 );
		towerInfoBody.text( TowerInfo.getTowerDescription( chosenTower ) );
		rightScroll.content().setSize( rightScroll.width(), towerInfoBody.bottom() + 2 );
		rightScroll.scrollTo( 0, 0 );
	}

	public static void showTowerInfo( TowerInfo.AllTowers chosenTower ) {
		Scene s = ShatteredPixelDungeon.scene();
		if (s instanceof TowerSelectScene) ((TowerSelectScene)s).updateTowerDescription( chosenTower );
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TowerLevelSelectScene.class );
	}

	/** Equips the shown tower. */
	public class TowerEquipButton extends StyledButton {
		protected TowerInfo.AllTowers equipTower;

		public TowerEquipButton( TowerInfo.AllTowers sometower ) {
			super( Chrome.Type.GREY_BUTTON_TR, Messages.get( TowerSelectScene.class, "select" ), 7 );
			if (TowerInfo.getTowerLock( sometower ) == TowerInfo.Lock.UNLOCKED) {
				equipTower = sometower;
				icon( Icons.get( Icons.ARROW ) );
			} else {
				text.hardlight( 0xAAAAAA );
				equipTower = null;
				icon( Icons.get( Icons.CLOSE ) );
			}
		}

		@Override
		protected void onClick() {
			if (equipTower != null) {
				TowerDefense.equip( equipTower );
				for (TowerSlotButton b : slotButtons) b.refresh();
			}
		}
	}

	/** One of the four equipped towers, shown under the description. */
	public class TowerSlotButton extends StyledButton {
		private final int slotNum;

		public TowerSlotButton( int slotnum ) {
			super( Chrome.Type.BLANK, "" );
			slotNum = slotnum;
			setSize( delta, delta );
			refresh();
		}

		public void refresh() {
			Image x = TowerInfo.getTowerIcon( TowerDefense.slot( slotNum ) );
			x.scale.set( delta / x.width );
			icon( x );
		}

		@Override
		protected void onClick() {
			updateTowerDescription( TowerDefense.slot( slotNum ) );
		}
	}

	/** The tower icons in the list on the left. */
	public class TowerSelectionButton extends StyledButton {

		protected Image towerIcon;
		public final static int WIDTH = 27;
		public static final int HEIGHT = 27;
		protected TowerInfo.AllTowers towerch;

		public TowerSelectionButton( TowerInfo.AllTowers tower, String text ) {
			super( Chrome.Type.BLANK, text, 15 );
			textColor( 0xAAAAAA );
			Image x = TowerInfo.getTowerIcon( tower );
			x.scale.set( 1.6f );
			this.towerIcon = x;
			this.setSize( WIDTH, HEIGHT );
			addToBack( this.towerIcon );
			towerch = tower;
			layout();
		}

		public TowerSelectionButton( TowerInfo.AllTowers tower ) {
			this( tower, "" );
		}

		@Override
		protected void onClick() {
			TowerSelectScene.showTowerInfo( towerch );
		}

		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				onClick();
				return true;
			}
			return false;
		}

		@Override
		protected void layout() {
			super.layout();
			if (towerIcon == null) return;
			towerIcon.x = x;
			towerIcon.y = y + (height - towerIcon.height()) / 2f;
			PixelScene.align( towerIcon );
			text.setPos( x + towerIcon.width() / 2 - text.width() / 2, towerIcon.y + towerIcon.height() / 2 - text.height() / 2 );
			PixelScene.align( text );
		}
	}
}
