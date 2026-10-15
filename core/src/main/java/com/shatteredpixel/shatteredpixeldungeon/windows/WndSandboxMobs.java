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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The monster part of the sandbox panel. Lists the bestiary and spawns
 * whichever monster is clicked next to the hero, like a spawn egg.
 */
public class WndSandboxMobs extends Window {

	private static final int WIDTH = 126;
	private static final int GAP   = 2;

	//these reach into their own boss level's code, and crash the game on any other floor
	private static final HashSet<String> UNSAFE = new HashSet<>(Arrays.asList(
			"Tengu", "DM300", "Pylon", "DwarfKing",
			//Devoted's bosses, which do the same with Devoted's boss levels
			"BlastFurnace", "FurnaceGolem", "RatBeast", "Thymor", "VelEssence", "VelTaleth", "Wendar", "Yuria",
			//Matt Edition's unused boss, built for the Dwarf King's throne room
			"DarkKing"
	));

	public static boolean safeToSpawn( Class<?> cls ){
		if (UNSAFE.contains( cls.getSimpleName() )) return false;
		//a boss's phases are often classes nested inside it
		return cls.getEnclosingClass() == null || !UNSAFE.contains( cls.getEnclosingClass().getSimpleName() );
	}

	private static String query = "";

	public WndSandboxMobs() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(WndSandbox.class, "mobs_title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;

		RedButton search = new RedButton( query.isEmpty() ? Messages.get(WndSandbox.class, "search")
				: Messages.get(WndSandbox.class, "search_for", query), 7 ) {
			@Override
			protected void onClick() {
				GameScene.show( new WndTextInput( Messages.get(WndSandbox.class, "search"), null, query, 30, false,
						Messages.get(WndSandbox.class, "search_go"), Messages.get(WndSandbox.class, "cancel") ) {
					@Override
					public void onSelect( boolean positive, String text ) {
						if (positive) {
							query = text.trim();
							hide();
							GameScene.show( new WndSandboxMobs() );
						}
					}
				} );
			}
		};
		search.setRect( 0, top, WIDTH, 16 );
		add( search );
		top += 16 + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		for (Bestiary category : Bestiary.values()) {
			boolean titled = false;
			for (final Class<?> cls : category.entities()) {
				//traps and plants are in the bestiary too, but aren't monsters
				if (!Mob.class.isAssignableFrom(cls) || !safeToSpawn(cls)) continue;
				//the vault quest's monsters need the vault level around them too
				if (cls.getName().contains(".vault.") || cls.getSimpleName().startsWith("Vault")) continue;

				Image icon;
				String name;
				try {
					Mob sample = (Mob) Reflection.newInstance(cls);
					CharSprite sprite = sample.sprite();
					sprite.idle();
					icon = new Image(sprite);
					name = Messages.titleCase(sample.name());
				} catch (Exception e) {
					//a few entries need extra setup before they can be drawn, leave those out
					continue;
				}

				if (!query.isEmpty() && !name.toLowerCase().contains( query.toLowerCase() )) continue;

				if (!titled) {
					list.addTitle( Messages.titleCase(category.title()) );
					titled = true;
				}

				list.addItem( new ScrollingListPane.ListItem( icon, name ) {
					@Override
					public boolean onClick( float x, float y ) {
						if (inside( x, y )) {
							spawn( cls );
							return true;
						}
						return false;
					}
				} );
			}
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	public static void spawn( Class<?> cls ) {
		Mob mob = (Mob) Reflection.newInstance(cls);
		if (mob == null) return;

		boolean large = Char.hasProp(mob, Char.Property.LARGE);
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS8) {
			int cell = Dungeon.hero.pos + offset;
			if (Actor.findChar( cell ) == null && Dungeon.level.passable[cell]
					&& (!large || Dungeon.level.openSpace[cell])) {
				candidates.add( cell );
			}
		}
		if (candidates.isEmpty()) {
			GLog.w( Messages.get(WndSandbox.class, "no_room") );
			return;
		}

		if (mob.state != mob.PASSIVE) {
			mob.state = mob.WANDERING;
		}
		mob.pos = Random.element( candidates );
		GameScene.add( mob, 1f );
		ScrollOfTeleportation.appear( mob, mob.pos );
		Dungeon.level.occupyCell( mob );

		GLog.p( Messages.get(WndSandbox.class, "spawned", Messages.titleCase(mob.name())) );
	}

}
