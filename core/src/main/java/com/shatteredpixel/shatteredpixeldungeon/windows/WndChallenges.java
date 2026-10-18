/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * Onyx's Pixel Dungeon, the scrolling list and the locked challenges come from Pixel Dungeon Reforged
 * by nathanlink169
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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndChallenges extends Window {

	private static final int WIDTH		= 120;
	private static final int TTL_HEIGHT = 16;
	private static final int BTN_HEIGHT = 16;
	private static final int GAP        = 1;

	private boolean editable;
	private ArrayList<CheckBox> boxes;
	//the mask each box stands for, locked challenges have no box
	private ArrayList<Integer> masks = new ArrayList<>();

	public WndChallenges( int checked, boolean editable ) {

		super();

		this.editable = editable;

		boxes = new ArrayList<>();

		//the list is now long enough to need scrolling, 80% of the screen at most
		int maxHeight = (int)(Camera.main.height * 0.8f);
		resize(WIDTH, maxHeight);

		ScrollPane pane = new ScrollPane( new Component() );
		add(pane);
		pane.setRect(0, 0, WIDTH, maxHeight);

		Component content = pane.content();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		content.add( title );

		int lockedCount = 0;

		float pos = TTL_HEIGHT;
		for (int i=0; i < Challenges.NAME_IDS.length; i++) {

			if (!Challenges.IsChallengeUnlocked(Challenges.MASKS[i])) {
				lockedCount++;
				continue;
			}

			final String challenge = Challenges.NAME_IDS[i];
			masks.add(Challenges.MASKS[i]);

			CheckBox cb = new CheckBox( Messages.titleCase(Messages.get(Challenges.class, challenge)) );
			cb.checked( (checked & Challenges.MASKS[i]) != 0 );
			cb.active = editable;

			if (i > 0) {
				pos += GAP;
			}
			cb.setRect( 0, pos, WIDTH-16, BTN_HEIGHT );

			content.add( cb );
			boxes.add( cb );

			IconButton info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.scene().add(
							new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
					);
				}
			};
			info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
			content.add(info);

			pos = cb.bottom();
		}

		if (lockedCount > 0) {
			pos += GAP * 2;
			RenderedTextBlock locked = PixelScene.renderTextBlock(Messages.get(this, "locked", lockedCount), 6);
			locked.maxWidth(WIDTH);
			locked.setPos(0, pos);
			content.add(locked);
			pos = locked.bottom() + GAP;
		}

		int height = (int)Math.min(maxHeight, pos);
		resize( WIDTH, height );
		content.setSize( WIDTH, pos );
		pane.setRect( 0, 0, WIDTH, height );
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			int value = 0;
			for (int i=0; i < boxes.size(); i++) {
				if (boxes.get( i ).checked()) {
					value |= masks.get(i);
				}
			}
			SPDSettings.challenges( value );
		}

		super.onBackPressed();
	}
}
