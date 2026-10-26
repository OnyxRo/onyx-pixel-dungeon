package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.Lwjgl3Audio;
import com.badlogic.gdx.files.FileHandle;

//Wraps the libGDX audio so a song that fails while streaming (a decoding error) stops the music instead of
//closing the game. libGDX streams music from the main loop, outside any game code that could catch it.
public class SafeAudio implements Lwjgl3Audio {

	private final Lwjgl3Audio audio;
	private int failures = 0;

	public SafeAudio( Lwjgl3Audio audio ) {
		this.audio = audio;
	}

	@Override
	public void update() {
		try {
			audio.update();
		} catch (RuntimeException e) {
			failures++;
			if (failures <= 3) {
				System.err.println( "music stopped after a streaming error: " + e );
			}
			try {
				com.watabou.noosa.audio.Music.INSTANCE.stop();
			} catch (RuntimeException ignored) {
				//the track is already broken, nothing more to stop
			}
		}
	}

	@Override
	public AudioDevice newAudioDevice( int samplingRate, boolean isMono ) {
		return audio.newAudioDevice( samplingRate, isMono );
	}

	@Override
	public AudioRecorder newAudioRecorder( int samplingRate, boolean isMono ) {
		return audio.newAudioRecorder( samplingRate, isMono );
	}

	@Override
	public Sound newSound( FileHandle fileHandle ) {
		return audio.newSound( fileHandle );
	}

	@Override
	public Music newMusic( FileHandle file ) {
		return audio.newMusic( file );
	}

	@Override
	public boolean switchOutputDevice( String deviceIdentifier ) {
		return audio.switchOutputDevice( deviceIdentifier );
	}

	@Override
	public String[] getAvailableOutputDevices() {
		return audio.getAvailableOutputDevices();
	}

	@Override
	public void dispose() {
		audio.dispose();
	}
}
