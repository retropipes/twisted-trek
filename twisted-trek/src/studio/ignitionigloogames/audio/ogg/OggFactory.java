/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.net.URL;

public abstract class OggFactory extends Thread {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb

    // Constructor
    protected OggFactory() {
	super();
    }

    // Methods
    public abstract void stopLoop();

    public abstract boolean isPlaying();

    // Factories
    public static OggFactory loadFile(final String file) {
	return new OggFile(file);
    }

    public static OggFactory loadResource(final URL resource) {
	return new OggResource(resource);
    }
}
