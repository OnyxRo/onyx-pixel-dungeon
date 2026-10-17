/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * The difficulty setting is ported from Pixel Dungeon Reforged by nathanlink169
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

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

//Pixel Dungeon Reforged. Easy, medium, hard or impossible. Medium is how Shattered Pixel Dungeon plays.
public class WndDifficulty extends Window {

	private static final int WIDTH		= 120;
	private static final int TTL_HEIGHT = 16;
	private static final int BTN_HEIGHT = 16;
	private static final int GAP        = 1;

	private static final String[] KEYS = { "easy", "medium", "hard", "impossible" };

	private final boolean editable;
	private final ArrayList<CheckBox> boxes = new ArrayList<>();

	//Called when the window closes, so the screen that opened it can change its picture
	public interface Listener {
		void onDifficultyChanged();
	}
	private final Listener listener;

	public WndDifficulty( int currentDifficulty, boolean editable, Listener listener ) {

		super();

		this.editable = editable;
		this.listener = listener;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "difficulty"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		add( title );

		float pos = TTL_HEIGHT;

		for (int i = 0; i < KEYS.length; i++) {
			final String key = KEYS[i];

			CheckBox cb = new CheckBox( Messages.get(WndDifficulty.class, key + ".name") ) {
				@Override
				protected void onClick() {
					if (checked()) {
						//one is always picked, it cannot be unchecked
						return;
					}
					super.onClick();
					handleTap(this);
				}
			};
			cb.checked( currentDifficulty == i + 1 );
			cb.active = editable;
			pos += GAP;
			cb.setRect( 0, pos, WIDTH - 16, BTN_HEIGHT );
			add( cb );
			boxes.add( cb );

			IconButton info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.scene().add(
							new WndMessage(Messages.get(WndDifficulty.class, key + ".desc"))
					);
				}
			};
			info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
			add(info);
			pos = cb.bottom();
		}

		resize( WIDTH, (int)pos );
	}

	private void handleTap(CheckBox current) {
		for (CheckBox box : boxes) {
			if (box != current) {
				box.checked(false);
			}
		}
	}

	public static Image iconFor( int difficulty ) {
		switch (difficulty) {
			case 1: return Icons.get(Icons.EASY);
			case 3: return Icons.get(Icons.HARD);
			case 4: return Icons.get(Icons.IMPOSSIBLE);
			default: return Icons.get(Icons.MEDIUM);
		}
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			for (int i = 0; i < boxes.size(); i++) {
				if (boxes.get( i ).checked()) {
					SPDSettings.difficulty( i + 1 );
				}
			}
		}

		if (listener != null) {
			listener.onDifficultyChanged();
		}
		super.onBackPressed();
	}
}
