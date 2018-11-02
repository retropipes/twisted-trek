/*
 * Copyright (C) 1999, 2000 by Matthias Pfisterer
 *               2015 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.trilarion.sound.sampled.spi;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.FormatConversionProvider;

import com.github.trilarion.sound.sampled.AudioFormats;

/**
 * Base class for all conversion providers of Tritonus.
 *
 * @author Matthias Pfisterer
 */
public abstract class TFormatConversionProvider extends FormatConversionProvider {
    /**
     *
     */
    protected static final AudioFormat.Encoding[] EMPTY_ENCODING_ARRAY = new AudioFormat.Encoding[0];
    /**
     *
     */
    protected static final AudioFormat[] EMPTY_FORMAT_ARRAY = new AudioFormat[0];

    // $$fb2000-10-04: use AudioSystem.NOT_SPECIFIED for all fields.
    @Override
    public AudioInputStream getAudioInputStream(final AudioFormat.Encoding targetEncoding,
	    final AudioInputStream audioInputStream) {
	final AudioFormat sourceFormat = audioInputStream.getFormat();
	final AudioFormat targetFormat = new AudioFormat(targetEncoding, AudioSystem.NOT_SPECIFIED, // sample rate
		AudioSystem.NOT_SPECIFIED, // sample size in bits
		AudioSystem.NOT_SPECIFIED, // channels
		AudioSystem.NOT_SPECIFIED, // frame size
		AudioSystem.NOT_SPECIFIED, // frame rate
		sourceFormat.isBigEndian()); // big endian
	return this.getAudioInputStream(targetFormat, audioInputStream);
    }

    /**
     * WARNING: this method uses
     * <code>getTargetFormats(AudioFormat.Encoding, AudioFormat)</code> which may
     * create infinite loops if the latter is overwritten.
     * <p>
     * This method is overwritten here to make use of
     * org.tritonus.share.sampled.AudioFormats.matches and is considered temporary
     * until AudioFormat.matches is corrected in the JavaSound API.
     *
     * @return
     */
    /*
     * $$mp: if we decide to use getMatchingFormat(), this method should be
     * implemented by simply calling getMatchingFormat() and comparing the result
     * against null.
     */
    @Override
    public boolean isConversionSupported(final AudioFormat targetFormat, final AudioFormat sourceFormat) {
	final AudioFormat[] aTargetFormats = this.getTargetFormats(targetFormat.getEncoding(), sourceFormat);
	for (final AudioFormat aTargetFormat : aTargetFormats) {
	    if (aTargetFormat != null && AudioFormats.matches(aTargetFormat, targetFormat)) {
		return true;
	    }
	}
	return false;
    }
}
