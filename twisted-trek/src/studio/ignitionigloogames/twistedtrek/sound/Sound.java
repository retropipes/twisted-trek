/* Twisted Trek: A Dual-World Action RPG */
package studio.ignitionigloogames.twistedtrek.sound;

import studio.ignitionigloogames.audio.wav.WavFactory;

public final class Sound {
    public static void play(final String soundName) {
	WavFactory.loadResource(Sound.class.getResource("/assets/sounds/" + soundName + ".wav")).start();
    }
}
