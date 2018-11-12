/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggResource extends OggFactory {
    private final URL soundURL;
    private OggPlayer player;

    public OggResource(final URL resURL) {
	super();
	this.soundURL = resURL;
    }

    @Override
    public void run() {
	try (AudioInputStream ais = AudioSystem.getAudioInputStream(this.soundURL)) {
	    this.player = new OggPlayer(ais);
	    this.player.playLoop();
	} catch (final UnsupportedAudioFileException | IOException e1) {
	    // Do nothing
	}
    }

    @Override
    public boolean isPlaying() {
	return this.player != null && this.isAlive();
    }

    @Override
    public void stopLoop() {
	if (this.player != null) {
	    this.player.stopLoop();
	}
    }
}
