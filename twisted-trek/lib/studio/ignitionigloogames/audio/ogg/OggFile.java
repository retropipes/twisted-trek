/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.io.File;

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
	    this.player = new OggPlayer(soundFile);
	    this.player.playLoop();
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
