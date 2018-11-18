/*
 * Copyright (C) 2004 - 2008 JavaZOOM : vorbisspi@javazoom.net
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
package com.github.trilarion.sound.vorbis.sampled;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import com.github.trilarion.sound.sampled.AsynchronousFilteredAudioInputStream;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Packet;
import com.github.trilarion.sound.vorbis.jcraft.jogg.Page;
import com.github.trilarion.sound.vorbis.jcraft.jogg.StreamState;
import com.github.trilarion.sound.vorbis.jcraft.jogg.SyncState;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Block;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Comment;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.DspState;
import com.github.trilarion.sound.vorbis.jcraft.jorbis.Info;

/**
 * This class implements the Vorbis decoding.
 */
public class DecodedVorbisAudioInputStream extends AsynchronousFilteredAudioInputStream {
    private final InputStream oggBitStream_;
    private SyncState oggSyncState_ = null;
    private StreamState oggStreamState_ = null;
    private Page oggPage_ = null;
    private Packet oggPacket_ = null;
    private Info vorbisInfo = null;
    private Comment vorbisComment = null;
    private DspState vorbisDspState = null;
    private Block vorbisBlock = null;
    static final int playState_NeedHeaders = 0;
    static final int playState_ReadData = 1;
    static final int playState_WriteData = 2;
    static final int playState_Done = 3;
    static final int playState_BufferFull = 4;
    static final int playState_Corrupt = -1;
    private int playState;
    private final int bufferMultiple_ = 4;
    private final int bufferSize_ = this.bufferMultiple_ * 256 * 2;
    private int convsize = this.bufferSize_ * 2;
    private final byte[] convbuffer = new byte[this.convsize];
    private byte[] buffer = null;
    private int bytes = 0;
    private float[][][] _pcmf = null;
    private int[] _index = null;
    private int index = 0;
    private int i = 0;
    // bout is now a global so that we can continue from when we have a buffer full.
    int bout = 0;
    private long currentBytes = 0;

    /**
     * Constructor.
     *
     * @param outputFormat
     * @param bitStream
     */
    public DecodedVorbisAudioInputStream(final AudioFormat outputFormat, final AudioInputStream bitStream) {
	super(outputFormat, -1);
	this.oggBitStream_ = bitStream;
	this.init_jorbis();
	this.index = 0;
	this.playState = DecodedVorbisAudioInputStream.playState_NeedHeaders;
    }

    /**
     * Initializes all the jOrbis and jOgg vars that are used for song playback.
     */
    private void init_jorbis() {
	this.oggSyncState_ = new SyncState();
	this.oggStreamState_ = new StreamState();
	this.oggPage_ = new Page();
	this.oggPacket_ = new Packet();
	this.vorbisInfo = new Info();
	this.vorbisComment = new Comment();
	this.vorbisDspState = new DspState();
	this.vorbisBlock = new Block(this.vorbisDspState);
	this.buffer = null;
	this.bytes = 0;
	this.currentBytes = 0L;
	this.oggSyncState_.init();
    }

    /**
     * Main loop.
     */
    @Override
    public void dataReady() {
	switch (this.playState) {
	case playState_NeedHeaders:
	    break;
	case playState_ReadData:
	    break;
	case playState_WriteData:
	    break;
	case playState_Done:
	    break;
	case playState_BufferFull:
	    break;
	case playState_Corrupt:
	    break;
	default:
	    break;
	}
	// This code was developed by the jCraft group, as JOrbisPlayer.java, slightly
	// modified by jOggPlayer developer and adapted by JavaZOOM to suit the
	// JavaSound
	// SPI. Then further modified by Tom Kimpton to correctly play ogg files that
	// would hang the player.
	switch (this.playState) {
	case playState_NeedHeaders:
	    try {
		// Headers (+ Comments).
		this.readHeaders();
	    } catch (final IOException ioe) {
		this.playState = DecodedVorbisAudioInputStream.playState_Corrupt;
		return;
	    }
	    this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
	    break;
	case playState_ReadData:
	    int result;
	    this.index = this.oggSyncState_.buffer(this.bufferSize_);
	    this.buffer = this.oggSyncState_.data;
	    this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	    if (this.bytes == -1) {
		this.playState = DecodedVorbisAudioInputStream.playState_Done;
		break;
	    } else {
		this.oggSyncState_.wrote(this.bytes);
		if (this.bytes == 0) {
		    if (this.oggPage_.eos() != 0 || this.oggStreamState_.e_o_s != 0 || this.oggPacket_.e_o_s != 0) {
			this.playState = DecodedVorbisAudioInputStream.playState_Done;
		    }
		    break;
		}
	    }
	    result = this.oggSyncState_.pageout(this.oggPage_);
	    if (result == 0) {
		this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		break;
	    } // need more data
	    if (result == -1) { // missing or corrupt data at this page position
		this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		break;
	    }
	    this.oggStreamState_.pagein(this.oggPage_);
	    this.playState = DecodedVorbisAudioInputStream.playState_WriteData;
	    break;
	case playState_WriteData:
	    // Decoding !
	    label: while (true) {
		result = this.oggStreamState_.packetout(this.oggPacket_);
		switch (result) {
		case 0:
		    this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
		    break label;
		case -1:
		    // missing or corrupt data at this page position
		    // no reason to complain; already complained above
		    // playState = playState_ReadData;
		    // break;
		    // continue;
		    break;
		default:
		    // we have a packet. Decode it
		    if (this.vorbisBlock.synthesis(this.oggPacket_) == 0) { // test for success!
			this.vorbisDspState.synthesis_blockin(this.vorbisBlock);
		    } else {
			// if(TDebug.TraceAudioConverter) TDebug.out("vorbisBlock.synthesis() returned
			// !0, going to read state");
			continue;
		    }
		    this.outputSamples();
		    if (this.playState == DecodedVorbisAudioInputStream.playState_BufferFull) {
			return;
		    }
		    break;
		}
	    } // while(true)
	    if (this.oggPage_.eos() != 0) {
		this.playState = DecodedVorbisAudioInputStream.playState_Done;
	    }
	    break;
	case playState_BufferFull:
	    this.continueFromBufferFull();
	    break;
	case playState_Corrupt:
	    // drop through to playState_Done...
	case playState_Done:
	    this.oggStreamState_.clear();
	    this.vorbisBlock.clear();
	    this.vorbisDspState.clear();
	    this.vorbisInfo.clear();
	    this.oggSyncState_.clear();
	    try {
		if (this.oggBitStream_ != null) {
		    this.oggBitStream_.close();
		}
		this.getCircularBuffer().close();
	    } catch (final Exception e) {
	    }
	    break;
	default:
	    break;
	} // switch
    }

    /**
     * This routine was extracted so that when the output buffer fills up, we can
     * break out of the loop, let the music channel drain, then continue from where
     * we were.
     */
    private void outputSamples() {
	int samples;
	while ((samples = this.vorbisDspState.synthesis_pcmout(this._pcmf, this._index)) > 0) {
	    final float[][] pcmf = this._pcmf[0];
	    this.bout = samples < this.convsize ? samples : this.convsize;
	    // convert doubles to 16 bit signed ints (host order) and
	    // interleave
	    for (this.i = 0; this.i < this.vorbisInfo.channels; this.i++) {
		int pointer = this.i * 2;
		// int ptr=i;
		final int mono = this._index[this.i];
		for (int j = 0; j < this.bout; j++) {
		    final double fVal = pcmf[this.i][mono + j] * 32767.;
		    int val = (int) fVal;
		    if (val > 32767) {
			val = 32767;
		    }
		    if (val < -32768) {
			val = -32768;
		    }
		    if (val < 0) {
			val = val | 0x8000;
		    }
		    this.convbuffer[pointer] = (byte) val;
		    this.convbuffer[pointer + 1] = (byte) (val >>> 8);
		    pointer += 2 * this.vorbisInfo.channels;
		}
	    }
	    if (this.getCircularBuffer().availableWrite() < 2 * this.vorbisInfo.channels * this.bout) {
		this.playState = DecodedVorbisAudioInputStream.playState_BufferFull;
		return;
	    }
	    this.getCircularBuffer().write(this.convbuffer, 0, 2 * this.vorbisInfo.channels * this.bout);
	    if (this.bytes < this.bufferSize_) {
	    }
	    if (this.vorbisDspState.synthesis_read(this.bout) != 0) {
	    }
	} // while(samples...)
	this.playState = DecodedVorbisAudioInputStream.playState_ReadData;
    }

    private void continueFromBufferFull() {
	if (this.getCircularBuffer().availableWrite() < 2 * this.vorbisInfo.channels * this.bout) {
	    // Don't change play state.
	    return;
	}
	this.getCircularBuffer().write(this.convbuffer, 0, 2 * this.vorbisInfo.channels * this.bout);
	// Don't change play state. Let outputSamples change play state, if necessary.
	this.outputSamples();
    }

    /**
     * Reads headers and comments.
     */
    private void readHeaders() throws IOException {
	this.index = this.oggSyncState_.buffer(this.bufferSize_);
	this.buffer = this.oggSyncState_.data;
	this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	if (this.bytes == -1) {
	    throw new IOException("Cannot get any data from selected Ogg bitstream.");
	}
	this.oggSyncState_.wrote(this.bytes);
	if (this.oggSyncState_.pageout(this.oggPage_) != 1) {
	    if (this.bytes < this.bufferSize_) {
		throw new IOException("EOF");
	    }
	    throw new IOException("Input does not appear to be an Ogg bitstream.");
	}
	this.oggStreamState_.init(this.oggPage_.serialno());
	this.vorbisInfo.init();
	this.vorbisComment.init();
	if (this.oggStreamState_.pagein(this.oggPage_) < 0) {
	    // error; stream version mismatch perhaps
	    throw new IOException("Error reading first page of Ogg bitstream data.");
	}
	if (this.oggStreamState_.packetout(this.oggPacket_) != 1) {
	    // no page? must not be vorbis
	    throw new IOException("Error reading initial header packet.");
	}
	if (this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_) < 0) {
	    // error case; not a vorbis header
	    throw new IOException("This Ogg bitstream does not contain Vorbis audio data.");
	}
	// int i = 0;
	this.i = 0;
	while (this.i < 2) {
	    while (this.i < 2) {
		int result = this.oggSyncState_.pageout(this.oggPage_);
		if (result == 0) {
		    break;
		} // Need more data
		if (result == 1) {
		    this.oggStreamState_.pagein(this.oggPage_);
		    while (this.i < 2) {
			result = this.oggStreamState_.packetout(this.oggPacket_);
			if (result == 0) {
			    break;
			}
			if (result == -1) {
			    throw new IOException("Corrupt secondary header.  Exiting.");
			}
			this.vorbisInfo.synthesis_headerin(this.vorbisComment, this.oggPacket_);
			this.i++;
		    }
		}
	    }
	    this.index = this.oggSyncState_.buffer(this.bufferSize_);
	    this.buffer = this.oggSyncState_.data;
	    this.bytes = this.readFromStream(this.buffer, this.index, this.bufferSize_);
	    if (this.bytes == -1) {
		break;
	    }
	    if (this.bytes == 0 && this.i < 2) {
		throw new IOException("End of file before finding all Vorbis  headers!");
	    }
	    this.oggSyncState_.wrote(this.bytes);
	}
	final byte[][] ptr = this.vorbisComment.user_comments;
	for (final byte[] ptr1 : ptr) {
	    if (ptr1 == null) {
		break;
	    }
	    new String(ptr1, 0, ptr1.length - 1, Charset.forName("US-ASCII")).trim();
	}
	this.convsize = this.bufferSize_ / this.vorbisInfo.channels;
	this.vorbisDspState.synthesis_init(this.vorbisInfo);
	this.vorbisBlock.init(this.vorbisDspState);
	this._pcmf = new float[1][][];
	this._index = new int[this.vorbisInfo.channels];
    }

    /**
     * Reads from the oggBitStream_ a specified number of Bytes(bufferSize_) worth
     * starting at index and puts them in the specified buffer[].
     *
     * @param new_buffer
     * @param new_index
     * @param new_bufferSize
     * @return the number of bytes read or -1 if error.
     */
    private int readFromStream(final byte[] new_buffer, final int new_index, final int new_bufferSize) {
	int local_bytes;
	try {
	    local_bytes = this.oggBitStream_.read(new_buffer, new_index, new_bufferSize);
	} catch (final Exception e) {
	    local_bytes = -1;
	}
	this.currentBytes = this.currentBytes + local_bytes;
	return local_bytes;
    }

    /**
     * Close the stream.
     *
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
	super.close();
	this.oggBitStream_.close();
    }
}
