package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;
import java.util.LinkedHashSet;

//Picks which mods a normal run's shuffled floors come from, opened from the hero select screen.
//The choice goes in Dungeon.chosenMods. Picking none or every mod means the normal shuffle of all of them.
public class WndChooseMods extends Window {

	public interface Listener {
		void onChanged();
	}

	private static final int WIDTH = 140;
	private static final int GAP = 2;

	private final LinkedHashSet<String> on = new LinkedHashSet<>();
	private final ArrayList<ScrollingListPane.ListItem> modItems = new ArrayList<>();
	private final ArrayList<String> mods = WorldShuffle.mods();
	private final Listener listener;
	private final RenderedTextBlock count;

	public WndChooseMods( Listener listener ) {
		super();
		this.listener = listener;

		if (Dungeon.chosenMods.isEmpty()) on.addAll( mods );
		else on.addAll( Dungeon.chosenMods );

		int height = (int) Math.min( 220, PixelScene.uiCamera.height - 30 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get( this, "title" ), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align( title );
		add( title );

		RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get( this, "desc" ), 6 );
		desc.maxWidth( WIDTH );
		desc.setPos( 0, title.bottom() + GAP );
		add( desc );

		count = PixelScene.renderTextBlock( 6 );
		count.hardlight( TITLE_COLOR );
		count.setPos( 0, desc.bottom() + GAP );
		add( count );
		updateCount();

		float top = count.bottom() + 2 * GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		list.addItem( new ScrollingListPane.ListItem( Icons.get( Icons.CHECKED ), Messages.get( this, "all" ) ) {
			@Override
			public boolean onClick( float x, float y ) {
				if (!inside( x, y )) return false;
				on.addAll( mods );
				refresh();
				return true;
			}
		} );
		list.addItem( new ScrollingListPane.ListItem( Icons.get( Icons.UNCHECKED ), Messages.get( this, "none" ) ) {
			@Override
			public boolean onClick( float x, float y ) {
				if (!inside( x, y )) return false;
				on.clear();
				refresh();
				return true;
			}
		} );

		for (final String mod : mods) {
			ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get( Icons.UNCHECKED ), mod ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					if (on.contains( mod )) on.remove( mod );
					else on.add( mod );
					refresh();
					return true;
				}

				@Override
				protected void layout() {
					icon.copy( Icons.get( on.contains( mod ) ? Icons.CHECKED : Icons.UNCHECKED ) );
					super.layout();
				}
			};
			modItems.add( item );
			list.addItem( item );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}

	private void refresh() {
		for (ScrollingListPane.ListItem item : modItems) {
			item.setPos( item.left(), item.top() );
		}
		updateCount();
	}

	private void updateCount() {
		if (on.isEmpty() || on.size() == mods.size()) {
			count.text( Messages.get( this, "count_all", mods.size() ), WIDTH );
		} else {
			count.text( Messages.get( this, "count", on.size(), mods.size() ), WIDTH );
		}
	}

	@Override
	public void hide() {
		Dungeon.chosenMods.clear();
		if (!on.isEmpty() && on.size() < mods.size()) Dungeon.chosenMods.addAll( on );
		super.hide();
		if (listener != null) listener.onChanged();
	}

	/** A short label for the hero select button. */
	public static String label() {
		if (Dungeon.chosenMods.isEmpty()) return Messages.get( WndChooseMods.class, "button_all" );
		if (Dungeon.chosenMods.size() == 1) {
			return Messages.get( WndChooseMods.class, "button_one", Dungeon.chosenMods.iterator().next().replace( " Pixel Dungeon", "" ) );
		}
		return Messages.get( WndChooseMods.class, "button_some", Dungeon.chosenMods.size() );
	}
}
