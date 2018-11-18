/* Import1: A Maze-Solving Game */
package studio.ignitionigloogames.twistedtrek.import1.resourcemanagers;

import studio.ignitionigloogames.twistedtrek.sound.Music;

public class MusicManager {
    public static void playMusic(final String musicName) {
	Music.play();
    }

    public static void stopMusic() {
	// Do nothing
    }

    public static boolean isMusicPlaying() {
	return true;
    }
}