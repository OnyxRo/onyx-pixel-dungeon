package com.shatteredpixel.shatteredpixeldungeon.items.instruments;

public class Harp extends Instrument {
	{
		maxNotes = 5;
	}

	@Override
	public String noteSFX(int note) {
		String noteName = Song.getNote(note);
		assert noteName != null;
		return "notes/harp-" + noteName.toLowerCase() + ".mp3";
	}
}
