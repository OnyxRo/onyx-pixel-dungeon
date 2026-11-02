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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.TowerDefense;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerIcons;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Music;

import java.util.Locale;

/**
 * Tower PD's level select screen, ported for the tower defense mode (see TowerDefense). It shows the chosen arena's
 * picture, name, story and what to expect, with arrows to the neighbouring arenas. The amulet button picks the mode
 * (normal, hard, challenge), the castle button opens the tower selection, and the button at the top left switches
 * between story and endless mode (the mod had a separate screen for that). Start begins the run.
 */
public class TowerLevelSelectScene extends PixelScene {

	private Image mainStagePic;
	private RenderedTextBlock levelName;
	private RenderedTextBlock levelDesc;
	private RenderedTextBlock levelKnowledge;
	private RenderedTextBlock levelNum;
	private RenderedTextBlock lockedText;
	private StyledButton startBtn;
	private IconButton arrowLeftButton;
	private IconButton arrowRightButton;
	private IconButton btnExit;
	private IconButton btnTowers;
	private IconButton btnModes;
	private StyledButton btnBranch;

	/** The button on the title screen that starts setting up a tower defense run. */
	public static StyledButton titleButton( Chrome.Type type ){
		StyledButton b = new StyledButton( type, Messages.get( TowerLevelSelectScene.class, "title_button" ) ){
			@Override
			protected void onClick() {
				int slot = GamesInProgress.firstEmpty();
				if (slot == -1){
					ShatteredPixelDungeon.scene().add( new WndMessage( Messages.get( TitleScene.class, "sandbox_full" ) ) );
					return;
				}
				Dungeon.sandbox = false;
				Dungeon.bossCrawl = false;
				TowerDefense.active = true;
				//start on the newest arena open in story mode
				if (!TowerDefense.endless) TowerDefense.arena = Math.min( TowerDefense.unlocked( WndModes.current ), TowerDefense.STORY.size() );
				GamesInProgress.selectedClass = null;
				GamesInProgress.curSlot = slot;
				ShatteredPixelDungeon.switchScene( HeroSelectScene.class );
			}
		};
		b.icon( TowerIcons.TOWER.get() );
		return b;
	}

	public String backgroundArt() {
		int n = TowerDefense.arena;
		if (!TowerDefense.isUnlocked( n )) return "splashes/tower/levels/locked.png";
		return "splashes/tower/levels/" + (TowerDefense.endless ? "endlessarena" : "arena") + n + ".png";
	}

	private String loadingAsset() {
		int n = TowerDefense.arena;
		String region = n <= 5 ? "sewers" : n <= 10 ? "prison" : n <= 15 ? "caves" : n <= 20 ? "city" : "halls";
		if (TowerDefense.endless) region = n <= 2 ? "sewers" : n == 3 ? "prison" : "caves";
		switch (WndModes.current) {
			case HARDMODE: return "interfaces/tower/loading_" + region + "_hard.png";
			case CHALLENGE: return "interfaces/tower/loading_" + region + "_chal.png";
			default: return "interfaces/tower/loading_" + region + ".png";
		}
	}

	@Override
	public void create() {
		//endless arenas are always normal, as in the mod
		if (TowerDefense.endless) WndModes.current = WndModes.Modes.NORMAL;

		switch (WndModes.current) {
			case NORMAL:
			default:
				Music.INSTANCE.playTracks( new String[]{Assets.Music.GNOLL_GROTTO}, new float[]{1}, false );
				break;
			case HARDMODE:
				Music.INSTANCE.playTracks( new String[]{Assets.Music.HALLS_BOSS}, new float[]{1}, false );
				break;
			case CHALLENGE:
				Music.INSTANCE.playTracks( new String[]{Assets.Music.CITY_1, Assets.Music.CITY_2}, new float[]{1, 1}, false );
				break;
		}

		super.create();

		Class<?> chosenClass = TowerDefense.chosen();
		int chosenLevel = TowerDefense.arena;
		boolean open = TowerDefense.isUnlocked( chosenLevel );

		SkinnedBlock bg = new SkinnedBlock( Camera.main.width, Camera.main.height, loadingAsset() ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset( 0, Game.elapsed );
			}
		};
		bg.scale( 4, 4 );
		bg.autoAdjust = true;
		add( bg );

		Image im = new Image( TextureCache.createGradient( 0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000 ) ){
			@Override
			public void update() {
				super.update();
				aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height / 5f;
		im.scale.y = Camera.main.width;
		add( im );

		mainStagePic = new Image( backgroundArt() );
		mainStagePic.scale.set( Math.max( 0.5f, 0.5f * Math.round( Camera.main.height / mainStagePic.height ) ) );
		mainStagePic.x = (Camera.main.width - mainStagePic.width()) / 2f;
		mainStagePic.y = (Camera.main.height - mainStagePic.height()) / 2.2f;
		PixelScene.align( mainStagePic );
		add( mainStagePic );

		String key = chosenClass.getSimpleName().toLowerCase( Locale.ROOT );

		levelName = PixelScene.renderTextBlock( Messages.get( this, key + "name" ), 11 );
		levelName.align( RenderedTextBlock.CENTER_ALIGN );
		align( levelName );
		add( levelName );

		String desc = WndModes.current == WndModes.Modes.CHALLENGE ? Messages.get( this, key + "challenge" ) : Messages.get( this, key + "desc" );
		if (WndModes.current == WndModes.Modes.NORMAL && !TowerDefense.endless
				&& TowerDefense.unlocked( WndModes.Modes.NORMAL ) == chosenLevel
				&& Messages.get( this, key + "prize" ).length() > 3 && !Messages.get( this, key + "prize" ).contains( "!!!" )) {
			desc = desc + "\n\n" + Messages.get( this, "prize" ) + "\n" + Messages.get( this, key + "prize" );
		}
		levelDesc = PixelScene.renderTextBlock( desc, desc.length() > 300 ? 5 : desc.length() > 230 ? 6 : desc.length() > 130 ? 7 : 8 );
		levelDesc.hardlight( Window.WHITE );
		levelDesc.align( RenderedTextBlock.CENTER_ALIGN );
		levelDesc.maxWidth( 100 );
		levelDesc.setSize( levelDesc.maxWidth(), mainStagePic.height );
		levelDesc.setPos( mainStagePic.x / 2 - levelDesc.width() / 2, mainStagePic.center().y - levelDesc.height() / 2f + levelName.height() );
		levelName.setPos( mainStagePic.x / 2 - levelName.width() / 2, mainStagePic.center().y - levelDesc.height() / 2f - levelName.height() );
		align( levelDesc );
		add( levelDesc );

		String knowledge = Messages.get( this, key + "knowledge" );
		levelKnowledge = PixelScene.renderTextBlock( knowledge, knowledge.length() > 150 ? 6 : knowledge.length() > 100 ? 7 : 8 );
		levelKnowledge.hardlight( Window.WHITE );
		levelKnowledge.align( RenderedTextBlock.CENTER_ALIGN );
		levelKnowledge.maxWidth( 100 );
		levelKnowledge.setPos( (mainStagePic.x + mainStagePic.width() + Camera.main.width - levelKnowledge.width()) / 2, mainStagePic.center().y - (levelKnowledge.height() / 2f) );
		align( levelKnowledge );
		add( levelKnowledge );

		lockedText = PixelScene.renderTextBlock( TowerDefense.endless ? Messages.get( this, "lockedtextneedstory" ) : Messages.get( this, "lockedtext" ), 10 );
		lockedText.hardlight( Window.WHITE );
		lockedText.align( RenderedTextBlock.CENTER_ALIGN );
		lockedText.maxWidth( 100 );
		lockedText.setPos( mainStagePic.x / 2 - lockedText.width() / 2 - 2, mainStagePic.center().y - lockedText.height() / 2f );
		align( lockedText );
		add( lockedText );
		if (open) {
			lockedText.visible = lockedText.active = false;
		} else {
			levelName.visible = levelName.active = false;
			levelDesc.visible = levelDesc.active = false;
			levelKnowledge.visible = levelKnowledge.active = false;
		}

		startBtn = new StyledButton( Chrome.Type.GREY_BUTTON_TR, "" ){
			@Override
			protected void onClick() {
				super.onClick();
				TowerDefense.start();
			}
		};
		startBtn.icon( com.shatteredpixel.shatteredpixeldungeon.ui.Icons.get( com.shatteredpixel.shatteredpixeldungeon.ui.Icons.ENTER ) );
		startBtn.textColor( Window.TITLE_COLOR );
		startBtn.text( Messages.titleCase( Messages.get( this, "start" ) ) );
		startBtn.setSize( 80, 20 );
		startBtn.setPos( Camera.main.width / 2f - (startBtn.width() / 2f), Camera.main.height - 2 - startBtn.height() );
		align( startBtn );
		add( startBtn );
		startBtn.visible = startBtn.active = open;

		levelNum = PixelScene.renderTextBlock( Integer.toString( chosenLevel ), 18 );
		levelNum.hardlight( Window.WHITE );
		levelNum.align( RenderedTextBlock.LEFT_ALIGN );
		levelNum.setPos( mainStagePic.center().x - (levelNum.width() / 2f), startBtn.top() - levelNum.height() - 4 );
		align( levelNum );
		add( levelNum );

		arrowLeftButton = new IconButton( TowerIcons.ARROWREVERSE.get() ){
			@Override
			protected void onClick() {
				super.onClick();
				if (TowerDefense.arena > 1) TowerDefense.arena--;
				ShatteredPixelDungeon.switchNoFade( TowerLevelSelectScene.class );
			}
		};
		arrowLeftButton.setSize( 15, 15 );
		arrowLeftButton.setPos( levelNum.centerX() - 0.05f * Camera.main.height - arrowLeftButton.width() / 2f - levelNum.width() / 2, levelNum.centerY() - arrowLeftButton.width() / 2f );
		align( arrowLeftButton );
		add( arrowLeftButton );
		arrowLeftButton.visible = arrowLeftButton.active = chosenLevel > 1;

		arrowRightButton = new IconButton( TowerIcons.ARROW.get() ){
			@Override
			protected void onClick() {
				super.onClick();
				if (TowerDefense.arena < TowerDefense.list().size()) TowerDefense.arena++;
				ShatteredPixelDungeon.switchNoFade( TowerLevelSelectScene.class );
			}
		};
		arrowRightButton.setSize( 15, 15 );
		arrowRightButton.setPos( levelNum.centerX() + 0.05f * Camera.main.height - arrowRightButton.width() / 2f + levelNum.width() / 2, levelNum.centerY() - arrowRightButton.width() / 2f );
		align( arrowRightButton );
		add( arrowRightButton );
		//as in the mod, the arrow only leads one arena past the last one open
		arrowRightButton.visible = arrowRightButton.active = chosenLevel < TowerDefense.list().size() && open;

		if (!landscape()) {
			float areaWithoutEssentialsHeight = levelNum.top() - 8;

			levelName.setPos( Camera.main.width / 2f - levelName.width() / 2f, 22 );

			mainStagePic.scale.set( Math.max( 0.5f, 0.5f * Math.round( Camera.main.width / mainStagePic.width ) ) );
			mainStagePic.x = 3;
			mainStagePic.y = (areaWithoutEssentialsHeight - mainStagePic.height()) / 2;
			PixelScene.align( mainStagePic );
			levelDesc.maxWidth( (int)(Camera.main.width / 2.2f) );
			levelDesc.setPos( (mainStagePic.x + mainStagePic.width() + Camera.main.width - levelDesc.width()) / 2, (areaWithoutEssentialsHeight - levelDesc.height()) / 2 );
			align( levelDesc );

			mainStagePic.y = (levelDesc.centerY() - mainStagePic.height() / 2);
			PixelScene.align( mainStagePic );

			levelKnowledge.maxWidth( (int)(Camera.main.width) - 4 );
			levelKnowledge.setPos( (Camera.main.width - levelKnowledge.width()) / 2f, (mainStagePic.height() + mainStagePic.y + levelNum.top() - levelKnowledge.height()) / 2 );
			align( levelName );
			align( levelKnowledge );

			lockedText.maxWidth( (int)(Camera.main.width / 2.3f) );
			lockedText.setPos( (mainStagePic.x + mainStagePic.width() + Camera.main.width - lockedText.width()) / 2, (areaWithoutEssentialsHeight - lockedText.height()) / 2 );
			align( lockedText );
		}

		btnExit = new ExitButton(){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchScene( HeroSelectScene.class );
			}
		};
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		btnTowers = new IconButton( TowerIcons.GREYTOWER.get() ){
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.switchNoFade( TowerSelectScene.class );
			}
		};
		btnTowers.setSize( 20, 20 );
		btnTowers.setPos( startBtn.right(), startBtn.centerY() - btnTowers.height() / 2 );
		align( btnTowers );
		add( btnTowers );

		TowerIcons icon;
		switch (WndModes.current) {
			case NORMAL: default: icon = TowerIcons.AMULET; break;
			case HARDMODE: icon = TowerIcons.BLOODAMULET; break;
			case CHALLENGE: icon = TowerIcons.ETHERIALAMULET; break;
		}
		btnModes = new IconButton( icon.get() ){
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.scene().addToFront( new WndModes( mode -> ShatteredPixelDungeon.switchNoFade( TowerLevelSelectScene.class ) ) );
			}
		};
		btnModes.setSize( 20, 20 );
		btnModes.setPos( startBtn.left() - btnModes.width(), startBtn.centerY() - btnModes.height() / 2 );
		align( btnModes );
		//the mod showed the modes once the prison was reached, and never for endless arenas
		if (TowerDefense.unlocked( WndModes.Modes.NORMAL ) >= 6 && !TowerDefense.endless) add( btnModes );

		btnBranch = new StyledButton( Chrome.Type.GREY_BUTTON_TR, Messages.get( this, TowerDefense.endless ? "endless_name" : "story_name" ), 7 ){
			@Override
			protected void onClick() {
				super.onClick();
				if (!TowerDefense.endless && TowerDefense.unlocked( WndModes.Modes.NORMAL ) < 6) {
					ShatteredPixelDungeon.scene().addToFront( new WndMessage( Messages.get( TowerLevelSelectScene.class, "endless_locked" ) ) );
					return;
				}
				TowerDefense.endless = !TowerDefense.endless;
				TowerDefense.arena = TowerDefense.endless ? 1 : Math.min( TowerDefense.unlocked( WndModes.current ), TowerDefense.STORY.size() );
				ShatteredPixelDungeon.switchNoFade( TowerLevelSelectScene.class );
			}
		};
		btnBranch.icon( TowerDefense.endless ? TowerIcons.SLOT4.get() : TowerIcons.TOWER.get() );
		btnBranch.setSize( 70, 18 );
		btnBranch.setPos( 2, 2 );
		align( btnBranch );
		add( btnBranch );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchScene( HeroSelectScene.class );
	}
}
