/*
 * Onyx's Pixel Dungeon
 * Copyright (C) 2026 Onyx Rothman
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

/**
 * The sandbox panel, opened from the in-game menu during a sandbox run.
 * Works like a creative mode. God mode, full sight, floor skipping, forced worlds, free items,
 * monster spawning and stats.
 */
public class WndSandbox extends Window {

	private static final int WIDTH      = 124;
	private static final int GAP        = 2;
	private static final int BTN_HEIGHT = 16;

	private float pos;

	public WndSandbox() {

		super();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );
		pos = title.bottom() + 2*GAP;

		CheckBox god = new CheckBox( Messages.get(this, "god") ) {
			@Override
			protected void onClick() {
				super.onClick();
				Dungeon.sandboxGod = checked();
			}
		};
		god.checked( Dungeon.sandboxGod );

		CheckBox see = new CheckBox( Messages.get(this, "see") ) {
			@Override
			protected void onClick() {
				super.onClick();
				Dungeon.sandboxSight = checked();
				if (checked()) revealFloor();
				Dungeon.observe();
			}
		};
		see.checked( Dungeon.sandboxSight );

		addRow( god, see );

		addRow( new RedButton( Messages.get(this, "floors") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSandboxFloor() );
			}
		}, null );

		addRow( new RedButton( Messages.get(this, "items") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSandboxItems() );
			}
		}, null );

		addRow( new RedButton( Messages.get(this, "mobs") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSandboxMobs() );
			}
		}, null );

		addRow( new RedButton( Messages.get(WndCompendium.class, "title") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndCompendium() );
			}
		}, null );

		addRow( levelButton(1), levelButton(5) );
		addRow( strengthButton(1), strengthButton(5) );

		addRow( new RedButton( Messages.get(this, "heal"), 8 ) {
			@Override
			protected void onClick() {
				Hero hero = Dungeon.hero;
				PotionOfHealing.cure( hero );
				hero.HP = hero.HT;
				GLog.p( Messages.get(WndSandbox.class, "heal_msg") );
			}
		}, null );

		resize( WIDTH, (int)(pos - GAP) );
	}

	private void addRow( StyledButton left, StyledButton right ) {
		add( left );
		if (right == null) {
			left.setRect( 0, pos, WIDTH, BTN_HEIGHT );
		} else {
			left.setRect( 0, pos, (WIDTH - GAP) / 2f, BTN_HEIGHT );
			add( right );
			right.setRect( left.right() + GAP, pos, WIDTH - left.right() - GAP, BTN_HEIGHT );
		}
		pos += BTN_HEIGHT + GAP;
	}

	private RedButton levelButton( final int levels ) {
		return new RedButton( Messages.get(this, "level", levels), 8 ) {
			@Override
			protected void onClick() {
				Hero hero = Dungeon.hero;
				if (hero.lvl >= Hero.MAX_LEVEL) {
					GLog.w( Messages.get(WndSandbox.class, "level_max") );
					return;
				}
				for (int i = 0; i < levels && hero.lvl < Hero.MAX_LEVEL; i++) {
					hero.earnExp( hero.maxExp() - hero.exp, WndSandbox.class );
				}
				GLog.p( Messages.get(WndSandbox.class, "level_msg", hero.lvl) );
			}
		};
	}

	private RedButton strengthButton( final int amount ) {
		return new RedButton( Messages.get(this, "strength", amount), 8 ) {
			@Override
			protected void onClick() {
				Dungeon.hero.STR += amount;
				GLog.p( Messages.get(WndSandbox.class, "strength_msg", Dungeon.hero.STR()) );
			}
		};
	}

	//maps the whole floor and uncovers its secrets, like a scroll of magic mapping
	public static void revealFloor() {
		Level level = Dungeon.level;
		for (int i = 0; i < level.length(); i++) {
			if (level.discoverable[i]) {
				level.mapped[i] = true;
				int terr = level.map[i];
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					level.discover( i );
				}
			}
		}
		GameScene.updateFog();
	}

}
