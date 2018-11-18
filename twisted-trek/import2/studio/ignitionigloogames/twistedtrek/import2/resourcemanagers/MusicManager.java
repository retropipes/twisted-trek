/*  Import2: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package studio.ignitionigloogames.twistedtrek.import2.resourcemanagers;

import java.nio.BufferUnderflowException;

import studio.ignitionigloogames.audio.ogg.OggFactory;
import studio.ignitionigloogames.twistedtrek.import2.Import2;
import studio.ignitionigloogames.twistedtrek.import2.maze.Extension;

public class MusicManager {
    private static final String DEFAULT_LOAD_PATH = "/com/puttysoftware/tallertower/resources/music/";
    private static Class<?> LOAD_CLASS = MusicManager.class;
    private static OggFactory CURRENT_MUSIC;

    private static OggFactory getMusic(final String filename) {
	try {
	    final OggFactory mm = OggFactory.loadResource(MusicManager.LOAD_CLASS
		    .getResource(MusicManager.DEFAULT_LOAD_PATH + filename + Extension.getMusicExtensionWithPeriod()));
	    return mm;
	} catch (final NullPointerException np) {
	    return null;
	}
    }

    public static void playMusic(final int musicID) {
	MusicManager.CURRENT_MUSIC = MusicManager.getMusic(MusicConstants.getMusicName(musicID));
	if (MusicManager.CURRENT_MUSIC != null) {
	    // Play the music
	    MusicManager.CURRENT_MUSIC.start();
	}
    }

    public static void stopMusic() {
	if (MusicManager.CURRENT_MUSIC != null) {
	    // Stop the music
	    try {
		MusicManager.CURRENT_MUSIC.stopLoop();
	    } catch (final BufferUnderflowException bue) {
		// Ignore
	    } catch (final NullPointerException np) {
		// Ignore
	    } catch (final Throwable t) {
		Import2.getErrorLogger().logError(t);
	    }
	}
    }

    public static boolean isMusicPlaying() {
	if (MusicManager.CURRENT_MUSIC != null) {
	    if (MusicManager.CURRENT_MUSIC.isPlaying()) {
		return true;
	    }
	}
	return false;
    }
}