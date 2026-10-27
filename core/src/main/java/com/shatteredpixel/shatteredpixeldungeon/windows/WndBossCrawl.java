package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.BossCrawl;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/** Every boss of the boss crawl by floor. Clicking one moves the run to that boss, built fresh. */
public class WndBossCrawl extends Window {

	private static final int WIDTH = 150;
	private static final int GAP   = 2;

	public WndBossCrawl() {

		super();

		int height = (int)Math.min( 200, PixelScene.uiCamera.height - 40 );

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos( (WIDTH - title.width()) / 2, 0 );
		PixelScene.align(title);
		add( title );

		float top = title.bottom() + GAP;

		//the window has to be at its final size before the list is placed, or the list draws off centre
		resize( WIDTH, height );

		ScrollingListPane list = new ScrollingListPane();
		add( list );
		list.setRect( 0, top, WIDTH, height - top );

		int lastDepth = -1;
		for (int i = 0; i < BossCrawl.stops().size(); i++) {
			int depth = BossCrawl.stops().get( i )[1];
			if (depth != lastDepth) {
				list.addTitle( Messages.get(this, "floor", depth) );
				lastDepth = depth;
			}
			final int stop = i;
			String label = BossCrawl.name( i );
			if (i == BossCrawl.step) label = Messages.get(this, "current", label);
			list.addItem( new ScrollingListPane.ListItem( null, label ) {
				@Override
				public boolean onClick( float x, float y ) {
					if (!inside( x, y )) return false;
					WndBossCrawl.this.hide();
					BossCrawl.jumpTo( stop );
					return true;
				}
			} );
		}

		list.setRect( 0, top, WIDTH, height - top );
	}
}
