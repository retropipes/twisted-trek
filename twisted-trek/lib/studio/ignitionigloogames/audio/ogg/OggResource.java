/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.net.URL;

class OggResource extends OggFactory {
    private final URL soundURL;
    private OggPlayer player;

    public OggResource(final URL resURL) {
	super();
	this.soundURL = resURL;
    }

    @Override
    public void run() {
	this.player = new OggPlayer(this.soundURL);
	this.player.playLoop();
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
