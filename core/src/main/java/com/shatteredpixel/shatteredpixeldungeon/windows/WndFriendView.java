package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.ModsUnlock;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;

/**
 * Onyx's copy only. Friend view shows the game the way a copy from GitHub plays, plain Shattered
 * until Yog is beaten and no testing tools. Its unlock is kept apart from his own.
 */
public class WndFriendView extends WndOptions {

	public WndFriendView(){
		super(Messages.get(WndFriendView.class, "title"),
				Messages.get(WndFriendView.class, ModsUnlock.friendView() ? "body_on" : "body_off"),
				ModsUnlock.friendView()
						? new String[]{ Messages.get(WndFriendView.class, "turn_off"), Messages.get(WndFriendView.class, "start_over") }
						: new String[]{ Messages.get(WndFriendView.class, "turn_on") });
	}

	@Override
	protected void onSelect( int index ){
		if (index == 0){
			ModsUnlock.friendView(!ModsUnlock.friendView());
		} else {
			ModsUnlock.relock();
		}
		GameModes.loadSettings();
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}
