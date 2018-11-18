/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggFile extends OggFactory {
    private final String filename;
    private OggPlayer player;

    public OggFile(final String Oggfile) {
	super();
	this.filename = Oggfile;
    }

    @Override
    public void run() {
	if (this.filename != null) {
	    final File soundFile = new File(this.filename);
	    if (!soundFile.exists()) {
		return;
	    }
	    try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile)) {
		this.player = new OggPlayer(ais);
		this.player.playLoop();
	    } catch (final UnsupportedAudioFileException | IOException e1) {
		// Do nothing
	    }
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
