/*
 * Copyright (C) 2000 ymnk<ymnk@jcraft.com>
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
package com.github.trilarion.sound.vorbis.jcraft.jorbis;

/**
 *
 */
class Lpc {
    // en/decode lookups
    Drft fft = new Drft();
    int ln;
    int m;

    // Autocorrelation LPC coeff generation algorithm invented by
    // N. Levinson in 1947, modified by J. Durbin in 1959.
    // Input : n elements of time doamin data
    // Output: m lpc coefficients, excitation energy
    static float lpc_from_data(final float[] data, final float[] lpc, final int n, final int m) {
	final float[] aut = new float[m + 1];
	float error;
	int i, j;
	// autocorrelation, p+1 lag coefficients
	j = m + 1;
	while (j-- != 0) {
	    float d = 0;
	    for (i = j; i < n; i++) {
		d += data[i] * data[i - j];
	    }
	    aut[j] = d;
	}
	// Generate lpc coefficients from autocorr values
	error = aut[0];
	/*
	 * if(error==0){ for(int k=0; k<m; k++) lpc[k]=0.0f; return 0; }
	 */
	for (i = 0; i < m; i++) {
	    float r = -aut[i + 1];
	    if (error == 0) {
		for (int k = 0; k < m; k++) {
		    lpc[k] = 0.0f;
		}
		return 0;
	    }
	    // Sum up this iteration's reflection coefficient; note that in
	    // Vorbis we don't save it. If anyone wants to recycle this code
	    // and needs reflection coefficients, save the results of 'r' from
	    // each iteration.
	    for (j = 0; j < i; j++) {
		r -= lpc[j] * aut[i - j];
	    }
	    r /= error;
	    // Update LPC coefficients and total error
	    lpc[i] = r;
	    for (j = 0; j < i / 2; j++) {
		final float tmp = lpc[j];
		lpc[j] += r * lpc[i - 1 - j];
		lpc[i - 1 - j] += r * tmp;
	    }
	    if (i % 2 != 0) {
		lpc[j] += lpc[j] * r;
	    }
	    error *= 1.0 - r * r;
	}
	// we need the error value to know how big an impulse to hit the
	// filter with later
	return error;
    }

    // Input : n element envelope spectral curve
    // Output: m lpc coefficients, excitation energy
    float lpc_from_curve(final float[] curve, final float[] lpc) {
	int n = this.ln;
	final float[] work = new float[n + n];
	final float fscale = (float) (.5 / n);
	int i, j;
	// input is a real curve. make it complex-real
	// This mixes phase, but the LPC generation doesn't care.
	for (i = 0; i < n; i++) {
	    work[i * 2] = curve[i] * fscale;
	    work[i * 2 + 1] = 0;
	}
	work[n * 2 - 1] = curve[n - 1] * fscale;
	n *= 2;
	this.fft.backward(work);
	// The autocorrelation will not be circular. Shift, else we lose
	// most of the power in the edges.
	for (i = 0, j = n / 2; i < n / 2;) {
	    final float temp = work[i];
	    work[i++] = work[j];
	    work[j++] = temp;
	}
	return Lpc.lpc_from_data(work, lpc, n, this.m);
    }

    void init(final int mapped, final int new_m) {
	this.ln = mapped;
	this.m = new_m;
	// we cheat decoding the LPC spectrum via FFTs
	this.fft.init(mapped * 2);
    }

    void clear() {
	this.fft.clear();
    }

    static float FAST_HYPOT(final float a, final float b) {
	return (float) Math.sqrt(a * a + b * b);
    }

    // One can do this the long way by generating the transfer function in
    // the time domain and taking the forward FFT of the result. The
    // results from direct calculation are cleaner and faster.
    //
    // This version does a linear curve generation and then later
    // interpolates the log curve from the linear curve.
    void lpc_to_curve(final float[] curve, final float[] lpc, final float amp) {
	for (int i = 0; i < this.ln * 2; i++) {
	    curve[i] = 0.0f;
	}
	if (amp == 0) {
	    return;
	}
	for (int i = 0; i < this.m; i++) {
	    curve[i * 2 + 1] = lpc[i] / (4 * amp);
	    curve[i * 2 + 2] = -lpc[i] / (4 * amp);
	}
	this.fft.backward(curve);
	{
	    final int l2 = this.ln * 2;
	    final float unit = (float) (1. / amp);
	    curve[0] = (float) (1. / (curve[0] * 2 + unit));
	    for (int i = 1; i < this.ln; i++) {
		final float real = curve[i] + curve[l2 - i];
		final float imag = curve[i] - curve[l2 - i];
		final float a = real + unit;
		curve[i] = (float) (1.0 / Lpc.FAST_HYPOT(a, imag));
	    }
	}
    }
}
