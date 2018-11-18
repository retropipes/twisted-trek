/* Twisted Trek: A Dual-World Action RPG */
package studio.ignitionigloogames.twistedtrek.sound;

import java.net.URL;

import studio.ignitionigloogames.audio.ogg.OggFactory;

public class Music {
    // Fields
    private final OggFactory ogg;

    public Music(final URL loc) {
	this.ogg = OggFactory.loadResource(loc);
    }

    public static void play() {
	new Music(Music.class.getResource("/assets/music/dungeon-shallowest-overworld.ogg")).playLoop();
    }

    public void playLoop() {
	this.ogg.start();
    }

    public void stopLoop() {
	this.ogg.stopLoop();
    }
}