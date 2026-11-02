package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;

/**
 * Shown in a copy from GitHub, once on first launch and from the Mods button while the mods are
 * locked, then once more after Yog is beaten to say they are open. See ModsUnlock.
 */
public class WndModsLocked extends WndTitledMessage {

	public WndModsLocked( boolean opened ){
		super(Icons.get(Icons.NEWS),
				Messages.get(WndModsLocked.class, opened ? "open_title" : "title"),
				Messages.get(WndModsLocked.class, opened ? "open_body" : "body"));
	}
}
