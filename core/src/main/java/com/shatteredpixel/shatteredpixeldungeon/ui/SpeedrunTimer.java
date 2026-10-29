/*
 * Onyx's Pixel Dungeon
 *
 * The run timer from Speedrun Pixel Dungeon by QuasiStellar (QuasiStellar/Speedrun-Pixel-Dungeon).
 * The time is green while ahead of the goal and red behind it, with the goal and personal best below.
 * Shown on the game screen when the Speedrun timer game mode is on.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.SmallModes;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;

public class SpeedrunTimer extends Component {

	private static final int GREEN = 0x8AFF8A;
	private static final int RED   = 0xFF8A8A;
	private static final int GREY  = 0xCCCCCC;

	private BitmapText timerText;
	private BitmapText goalText;
	private BitmapText pbText;

	//the timer keeps its right edge here
	private float right;

	@Override
	protected void createChildren() {
		super.createChildren();
		timerText = new BitmapText(PixelScene.pixelFont);
		add(timerText);
		goalText = new BitmapText(PixelScene.pixelFont);
		goalText.hardlight(GREY);
		add(goalText);
		pbText = new BitmapText(PixelScene.pixelFont);
		pbText.hardlight(GREY);
		add(pbText);
	}

	public void rightEdge( float right, float top ){
		this.right = right;
		y = top;
		refresh();
	}

	private void place( BitmapText t, float top ){
		t.measure();
		t.x = right - t.width();
		t.y = top;
		PixelScene.align(t);
	}

	private void refresh(){
		long time = SmallModes.time;
		timerText.text(SmallModes.timeToString(time));
		long goal = SmallModes.goal();
		if (goal > 0 && !SmallModes.finished){
			timerText.hardlight(goal > time ? GREEN : RED);
			goalText.text(SmallModes.timeToString(goal));
		} else {
			timerText.hardlight(GREEN);
			goalText.text("");
		}
		long pb = SmallModes.personalBest();
		pbText.text(pb > 0 ? "PB " + SmallModes.timeToString(pb) : "");
		place(timerText, y);
		place(goalText, y + 8);
		place(pbText, y + 16);
		x = right - timerText.width();
		width = timerText.width();
		height = 24;
	}

	@Override
	public void update() {
		super.update();
		SmallModes.tick();
		refresh();
	}
}
