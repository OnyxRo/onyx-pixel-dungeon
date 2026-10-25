/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.Playstyles;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//the game modes from other mods, one button per mod, each opening that mod's list
public class WndGameModes extends Window {

	public interface Listener {
		void onChanged();
	}

	private static final int WIDTH      = 130;
	private static final int BTN_HEIGHT = 18;
	private static final int GAP        = 2;

	private final Listener listener;
	private final ArrayList<RedButton> modButtons = new ArrayList<>();
	private final ArrayList<String> mods = GameModes.mods();

	public WndGameModes( Listener listener ){
		super();
		this.listener = listener;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align( title );
		add( title );

		RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get(this, "desc"), 6 );
		desc.maxWidth( WIDTH );
		desc.setPos( 0, title.bottom() + GAP );
		add( desc );

		float pos = desc.bottom() + 2*GAP;
		for (final String mod : mods){
			RedButton btn = new RedButton( "" ){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront( new WndModeList( mod ){
						@Override
						public void hide() {
							super.hide();
							refresh();
						}
					});
				}
			};
			btn.setRect( 0, pos, WIDTH, BTN_HEIGHT );
			add( btn );
			modButtons.add( btn );
			pos = btn.bottom() + GAP;
		}

		RedButton clear = new RedButton( Messages.get(this, "clear") ){
			@Override
			protected void onClick() {
				GameModes.active.clear();
				refresh();
			}
		};
		clear.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( clear );

		resize( WIDTH, (int) clear.bottom() );
		refresh();
	}

	private void refresh(){
		for (int i = 0; i < mods.size(); i++){
			String mod = mods.get(i);
			int n = GameModes.count(mod);
			String name = mod.replace(" Pixel Dungeon", " PD");
			modButtons.get(i).text( n == 0 ? name : Messages.get(this, "mod_on", name, n) );
			modButtons.get(i).textColor( n == 0 ? 0xFFFFFF : TITLE_COLOR );
		}
	}

	@Override
	public void hide() {
		GameModes.saveSettings();
		super.hide();
		if (listener != null) listener.onChanged();
	}

	public static String label(){
		int n = GameModes.count();
		return n == 0 ? Messages.get(WndGameModes.class, "button_none") : Messages.get(WndGameModes.class, "button_some", n);
	}

	//one mod's modes, a check box for each, or a button that steps through the levels for Radar's playstyles
	public static class WndModeList extends Window {

		private final ArrayList<Runnable> refreshers = new ArrayList<>();
		private RenderedTextBlock summary;

		public WndModeList( String mod ){
			super();

			int maxHeight = (int)(Camera.main.height * 0.8f);
			resize( WIDTH, maxHeight );

			ScrollPane pane = new ScrollPane( new Component() );
			add( pane );
			pane.setRect( 0, 0, WIDTH, maxHeight );
			Component content = pane.content();

			RenderedTextBlock title = PixelScene.renderTextBlock( mod, 9 );
			title.hardlight( TITLE_COLOR );
			title.maxWidth( WIDTH );
			title.setPos( (WIDTH - title.width()) / 2, 0 );
			PixelScene.align( title );
			content.add( title );

			float pos = title.bottom() + 2*GAP;

			if (mod.equals(GameModes.MOD_RADAR)){
				RedButton items = new RedButton( Messages.get(WndGameModes.class, "radar_items") ){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront( new WndRadarItems() );
					}
				};
				items.setRect( 0, pos, WIDTH, BTN_HEIGHT );
				content.add( items );
				pos = items.bottom() + 2*GAP;
			}

			//toocruel, the mod's built in trials turn on a set of its modes
			if (mod.equals(GameModes.MOD_TOOCRUEL)){
				RedButton trials = new RedButton( Messages.get(WndGameModes.class, "trials") ){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront( new WndTrials( new Runnable() {
							@Override
							public void run() {
								refreshAll();
							}
						} ) );
					}
				};
				trials.setRect( 0, pos, WIDTH, BTN_HEIGHT );
				content.add( trials );
				pos = trials.bottom() + 2*GAP;
			}

			for (final GameModes.Mode mode : GameModes.MODES){
				if (!mode.mod.equals(mod)) continue;

				Component box;
				if (mode.maxLevel == 1){
					final CheckBox cb = new CheckBox( Messages.titleCase(mode.title()) ){
						@Override
						protected void onClick() {
							super.onClick();
							GameModes.set( mode.id, checked() ? 1 : 0 );
							refreshAll();
						}
					};
					cb.checked( GameModes.on(mode.id) );
					refreshers.add( new Runnable() {
						@Override
						public void run() {
							cb.checked( GameModes.on(mode.id) );
						}
					});
					box = cb;
				} else {
					final RedButton step = new RedButton( "" ){
						@Override
						protected void onClick() {
							int lvl = GameModes.level(mode.id) + 1;
							GameModes.set( mode.id, lvl > mode.maxLevel ? 0 : lvl );
							refreshAll();
						}
					};
					Runnable r = new Runnable() {
						@Override
						public void run() {
							int lvl = GameModes.level(mode.id);
							step.text( Messages.get(WndGameModes.class, "level", Messages.titleCase(mode.title()), lvl, mode.maxLevel) );
							step.textColor( lvl == 0 ? 0xFFFFFF : TITLE_COLOR );
						}
					};
					r.run();
					refreshers.add( r );
					box = step;
				}
				box.setRect( 0, pos, WIDTH - 16, 16 );
				content.add( box );

				IconButton info = new IconButton( Icons.get(Icons.INFO) ){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront( new WndTitledMessage(
								Icons.get(Icons.INFO), Messages.titleCase(mode.title()), mode.desc() ) );
					}
				};
				info.setRect( box.right(), pos, 16, 16 );
				content.add( info );
				pos = box.bottom() + 1;
			}

			if (mod.equals(GameModes.MOD_RADAR)){
				summary = PixelScene.renderTextBlock( 6 );
				summary.maxWidth( WIDTH );
				summary.setPos( 0, pos + GAP );
				content.add( summary );
				refreshSummary();
				pos = summary.bottom() + 40;
			}

			int height = (int)Math.min(maxHeight, pos);
			resize( WIDTH, height );
			content.setSize( WIDTH, pos );
			pane.setRect( 0, 0, WIDTH, height );
		}

		private void refreshAll(){
			for (Runnable r : refreshers) r.run();
			if (summary != null) refreshSummary();
		}

		private void refreshSummary(){
			Playstyles.refresh();
			StringBuilder sb = new StringBuilder();
			for (int s = 0; s < Playstyles.STAT_COUNT; s++){
				int p = Playstyles.points(s);
				if (p == 0) continue;
				String name = Messages.get(WndGameModes.class, "stat_" + Playstyles.STAT_IDS[s]);
				if (s == Playstyles.VISION || s == Playstyles.XRAY){
					sb.append(Messages.get(WndGameModes.class, "stat_flat", name, (p > 0 ? "+" : "") + p));
				} else {
					sb.append(Messages.get(WndGameModes.class, "stat_mult", name, Math.round(Playstyles.value(s) * 100)));
				}
				sb.append('\n');
			}
			summary.text( sb.length() == 0 ? Messages.get(WndGameModes.class, "no_playstyle") : sb.toString().trim(), WIDTH );
		}
	}

	//toocruel, the list of Too Cruel PD's trials, grouped by difficulty
	public static class WndTrials extends Window {

		public WndTrials( final Runnable onApplied ){
			super();

			int maxHeight = (int)(Camera.main.height * 0.8f);
			resize( WIDTH, maxHeight );

			ScrollPane pane = new ScrollPane( new Component() );
			add( pane );
			pane.setRect( 0, 0, WIDTH, maxHeight );
			Component content = pane.content();

			RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndGameModes.class, "trials"), 9 );
			title.hardlight( TITLE_COLOR );
			title.setPos( (WIDTH - title.width()) / 2, 0 );
			PixelScene.align( title );
			content.add( title );

			RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get(WndGameModes.class, "trials_desc"), 6 );
			desc.maxWidth( WIDTH );
			desc.setPos( 0, title.bottom() + GAP );
			content.add( desc );

			float pos = desc.bottom() + 2*GAP;
			for (String group : com.shatteredpixel.shatteredpixeldungeon.TooCruel.TRIAL_GROUPS){
				RenderedTextBlock head = PixelScene.renderTextBlock( Messages.get(WndGameModes.class, "trials_" + group), 8 );
				head.hardlight( TITLE_COLOR );
				head.setPos( 0, pos + GAP );
				content.add( head );
				pos = head.bottom() + GAP;

				for (final com.shatteredpixel.shatteredpixeldungeon.TooCruel.Trial t : com.shatteredpixel.shatteredpixeldungeon.TooCruel.TRIALS){
					if (!t.group.equals(group)) continue;
					RedButton btn = new RedButton( t.name ){
						@Override
						protected void onClick() {
							ShatteredPixelDungeon.scene().addToFront( new WndOptions( t.name,
									Messages.get(WndGameModes.class, "trial_confirm", com.shatteredpixel.shatteredpixeldungeon.TooCruel.trialSummary(t)),
									Messages.get(WndGameModes.class, "trial_yes"), Messages.get(WndGameModes.class, "trial_no") ){
								@Override
								protected void onSelect( int index ) {
									if (index == 0){
										com.shatteredpixel.shatteredpixeldungeon.TooCruel.applyTrial( t );
										if (onApplied != null) onApplied.run();
										WndTrials.this.hide();
									}
								}
							});
						}
					};
					btn.setRect( 0, pos, WIDTH, BTN_HEIGHT );
					content.add( btn );
					pos = btn.bottom() + 1;
				}
			}

			int height = (int)Math.min(maxHeight, pos);
			resize( WIDTH, height );
			content.setSize( WIDTH, pos );
			pane.setRect( 0, 0, WIDTH, height );
		}
	}
}
