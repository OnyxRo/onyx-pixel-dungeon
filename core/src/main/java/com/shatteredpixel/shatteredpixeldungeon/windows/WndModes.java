/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon (ported to Onyx's Pixel Dungeon)
 * Copyright (C) 2024-2025 FixAkaTheFix
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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

/**
 * Compact replacement for Tower PD's mode window. The mod let the player pick a difficulty mode on its level
 * select screen. Here the mode is a plain static setting, chosen from this window (opened from the sandbox
 * panel before travelling to a tower defense arena), and every new arena level reads it when it is created.
 */
public class WndModes extends WndOptions {

    public enum Modes {
        NORMAL,
        HARDMODE,
        CHALLENGE
    }

    /** the mode the next arena will be built with, saved on the level itself */
    public static Modes current = Modes.NORMAL;

    public interface Callback {
        void picked(Modes mode);
    }

    public WndModes(final Callback callback) {
        super(Messages.get(WndModes.class, "title"),
                Messages.get(WndModes.class, "normal") + "\n\n" + Messages.get(WndModes.class, "hardmode") + "\n\n" + Messages.get(WndModes.class, "challenge"),
                "Normal", "Hard mode", "Challenge");
        this.callback = callback;
    }

    private final Callback callback;

    @Override
    protected void onSelect(int index) {
        current = Modes.values()[index];
        if (callback != null) callback.picked(current);
    }
}
