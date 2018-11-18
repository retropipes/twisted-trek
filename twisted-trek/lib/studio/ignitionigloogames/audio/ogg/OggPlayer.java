/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package studio.ignitionigloogames.audio.ogg;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggPlayer {
    private URL sourceURL;
    private File sourceFile;
    private AudioInputStream decodedStream;
    private AudioFormat format;
    private AudioFormat decodedFormat;
    private boolean stop;

    public OggPlayer(final URL url) {
	this.sourceURL = url;
	this.stop = false;
    }

    public OggPlayer(final File file) {
	this.sourceFile = file;
	this.stop = false;
    }

    public void playLoop() {
	while (!this.stop) {
	    if (this.sourceURL != null) {
		// Get AudioInputStream from given URL.
		try (final AudioInputStream stream = AudioSystem.getAudioInputStream(this.sourceURL)) {
		    // Get the stream set up
		    if (stream != null) {
			this.format = stream.getFormat();
			this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				this.format.getSampleRate(), 16, this.format.getChannels(),
				this.format.getChannels() * 2, this.format.getSampleRate(), false);
		    }
		    this.playLoopImpl(stream);
		} catch (final UnsupportedAudioFileException | IOException e1) {
		    // Abandon ship!
		    this.stop = true;
		}
	    } else if (this.sourceFile != null) {
		// Get AudioInputStream from given file.
		try (final AudioInputStream stream = AudioSystem.getAudioInputStream(this.sourceFile)) {
		    // Get the stream set up
		    if (stream != null) {
			this.format = stream.getFormat();
			this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				this.format.getSampleRate(), 16, this.format.getChannels(),
				this.format.getChannels() * 2, this.format.getSampleRate(), false);
		    }
		    this.playLoopImpl(stream);
		} catch (final UnsupportedAudioFileException | IOException e1) {
		    // Abandon ship!
		    this.stop = true;
		}
	    } else {
		// Abandon ship!
		this.stop = true;
	    }
	}
    }

    private void playLoopImpl(final AudioInputStream stream) {
	this.decodedStream = null;
	if (stream != null) {
	    // Get AudioInputStream that will be decoded by underlying
	    // VorbisSPI
	    this.decodedStream = AudioSystem.getAudioInputStream(this.decodedFormat, stream);
	}
	try (SourceDataLine line = OggPlayer.getLine(this.decodedFormat)) {
	    if (line != null) {
		try {
		    final byte[] data = new byte[4096];
		    // Start
		    line.start();
		    int nBytesRead = 0;
		    while (nBytesRead != -1) {
			nBytesRead = this.decodedStream.read(data, 0, data.length);
			if (nBytesRead != -1) {
			    line.write(data, 0, nBytesRead);
			}
			if (this.stop) {
			    break;
			}
		    }
		} catch (final IOException e) {
		    // Abandon ship!
		    System.err.println("Reset stream failed!");
		    this.stop = true;
		} finally {
		    // Stop
		    line.drain();
		    line.stop();
		}
	    }
	} catch (final LineUnavailableException e) {
	    // Abandon ship!
	    this.stop = true;
	}
    }

    private static SourceDataLine getLine(final AudioFormat audioFormat) throws LineUnavailableException {
	SourceDataLine res = null;
	final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	res = (SourceDataLine) AudioSystem.getLine(info);
	res.open(audioFormat);
	return res;
    }

    public void stopLoop() {
	this.stop = true;
    }
}
