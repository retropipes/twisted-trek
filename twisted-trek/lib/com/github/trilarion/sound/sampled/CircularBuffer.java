/*
 * Copyright (C) 1999 by Matthias Pfisterer
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
package com.github.trilarion.sound.sampled;

/**
 *
 * @author
 */
public class CircularBuffer {
    private final boolean m_bBlockingRead;
    private final boolean m_bBlockingWrite;
    private final byte[] m_abData;
    private final int m_nSize;
    private long m_lReadPos;
    private long m_lWritePos;
    private final BufferListener m_trigger;
    private boolean m_bOpen;

    /**
     * Listener
     */
    public interface BufferListener {
	/**
	 * Data is ready.
	 */
	void dataReady();
    }

    /**
     *
     * @param nSize
     * @param bBlockingRead
     * @param bBlockingWrite
     * @param trigger
     */
    public CircularBuffer(final int nSize, final boolean bBlockingRead, final boolean bBlockingWrite,
	    final BufferListener trigger) {
	this.m_bBlockingRead = bBlockingRead;
	this.m_bBlockingWrite = bBlockingWrite;
	this.m_nSize = nSize;
	this.m_abData = new byte[this.m_nSize];
	this.m_lReadPos = 0;
	this.m_lWritePos = 0;
	this.m_trigger = trigger;
	this.m_bOpen = true;
    }

    /**
     * Close
     */
    public void close() {
	this.m_bOpen = false;
	// TODO: call notify() ?
    }

    /**
     *
     */
    private boolean isOpen() {
	return this.m_bOpen;
    }

    /**
     *
     * @return
     */
    public int availableRead() {
	return (int) (this.m_lWritePos - this.m_lReadPos);
    }

    /**
     *
     * @return
     */
    public int availableWrite() {
	return this.m_nSize - this.availableRead();
    }

    private int getReadPos() {
	return (int) (this.m_lReadPos % this.m_nSize);
    }

    private int getWritePos() {
	return (int) (this.m_lWritePos % this.m_nSize);
    }

    /**
     *
     * @param abData
     * @param nOffset
     * @param nLength
     * @return
     */
    public int read(final byte[] abData, final int nOffset_in, final int nLength_in) {
	int nOffset = nOffset_in;
	int nLength = nLength_in;
	if (!this.isOpen()) {
	    if (this.availableRead() > 0) {
		nLength = Math.min(nLength, this.availableRead());
	    } else {
		return -1;
	    }
	}
	synchronized (this) {
	    if (this.m_trigger != null && this.availableRead() < nLength) {
		this.m_trigger.dataReady();
	    }
	    if (!this.m_bBlockingRead) {
		nLength = Math.min(this.availableRead(), nLength);
	    }
	    int nRemainingBytes = nLength;
	    while (nRemainingBytes > 0) {
		while (this.availableRead() == 0) {
		    try {
			this.wait();
		    } catch (final InterruptedException e) {
		    }
		}
		int nAvailable = Math.min(this.availableRead(), nRemainingBytes);
		while (nAvailable > 0) {
		    final int nToRead = Math.min(nAvailable, this.m_nSize - this.getReadPos());
		    System.arraycopy(this.m_abData, this.getReadPos(), abData, nOffset, nToRead);
		    this.m_lReadPos += nToRead;
		    nOffset += nToRead;
		    nAvailable -= nToRead;
		    nRemainingBytes -= nToRead;
		}
		this.notifyAll();
	    }
	    // dumpInternalState();
	    return nLength;
	}
    }

    /**
     *
     * @param abData
     * @param nOffset
     * @param nLength
     * @return
     */
    public int write(final byte[] abData, final int nOffset_in, final int nLength_in) {
	int nOffset = nOffset_in;
	int nLength = nLength_in;
	synchronized (this) {
	    if (!this.m_bBlockingWrite) {
		nLength = Math.min(this.availableWrite(), nLength);
	    }
	    int nRemainingBytes = nLength;
	    while (nRemainingBytes > 0) {
		while (this.availableWrite() == 0) {
		    try {
			this.wait();
		    } catch (final InterruptedException e) {
		    }
		}
		int nAvailable = Math.min(this.availableWrite(), nRemainingBytes);
		while (nAvailable > 0) {
		    final int nToWrite = Math.min(nAvailable, this.m_nSize - this.getWritePos());
		    // TDebug.out("src buf size= " + abData.length + ", offset = " + nOffset + ",
		    // dst buf size=" + m_abData.length + " write pos=" + getWritePos() + " len=" +
		    // nToWrite);
		    System.arraycopy(abData, nOffset, this.m_abData, this.getWritePos(), nToWrite);
		    this.m_lWritePos += nToWrite;
		    nOffset += nToWrite;
		    nAvailable -= nToWrite;
		    nRemainingBytes -= nToWrite;
		}
		this.notifyAll();
	    }
	    return nLength;
	}
    }
}
