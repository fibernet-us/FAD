/*
 * Copyright Tony Yao. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list
 *   of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer listed in this license in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the copyright holders nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package us.fibernet.fad;

import javax.swing.JComponent;

/**
 * A class to hold parameters read from pattern file
 */
public class DataInput {
	
	class DataItem {
		String name;
		String value;
		JComponent jc;
		
	}
	private double XWAV;
	private double SFD;
	private double C;
	private double DR;
	private double WR;
	private int IFV;
	private int NULB;
	private int RMIN;
	private int NR;
	private double THMIN;
	private double DTHET;
	private int NTHET;

	public DataInput() {
		XWAV = 1.5405;
		SFD = 93.1000;
		C = 6.0000;
		DR = 0.0500;
		WR = 1.0000;
		IFV = 0;
		NULB = 1;
		RMIN = 50;
		NR = 1100;
		THMIN = 0.0000;
		DTHET = 1.0000;
		NTHET = 91;
	}

	public DataInput(double xwav, double sfd, double c, double dr, double wr,
			int ifv, int nulb, int rmin, int nr, double thmin, double dthet,
			int nthet) {

		XWAV = xwav;
		SFD = sfd;
		C = c;
		DR = dr;
		WR = wr;
		IFV = ifv;
		NULB = nulb;
		RMIN = rmin;
		NR = nr;
		THMIN = thmin;
		DTHET = dthet;
		NTHET = nthet;

	}

	// getters
	public double XWAV() {
		return XWAV;
	}

	public double SFD() {
		return SFD;
	}

	public double C() {
		return C;
	}

	public double DR() {
		return DR;
	}

	public double WR() {
		return WR;
	}

	public int IFV() {
		return IFV;
	}

	public int NULB() {
		return NULB;
	}

	public int RMIN() {
		return RMIN;
	}

	public int NR() {
		return NR;
	}

	public double THMIN() {
		return THMIN;
	}

	public double DTHET() {
		return DTHET;
	}

	public double NTHET() {
		return NTHET;
	}

	// setters
	public void XWAV(double xwav) {
		XWAV = xwav;
	}

	public void SFD(double sfd) {
		SFD = sfd;
		;
	}

	public void C(double c) {
		C = c;
	}

	public void DR(double dr) {
		DR = dr;
	}

	public void WR(double wr) {
		WR = wr;
	}

	public void IFV(int ifv) {
		IFV = ifv;
	}

	public void NULB(int nulb) {
		NULB = nulb;
	}

	public void RMIN(int rmin) {
		RMIN = rmin;
	}

	public void NR(int nr) {
		NR = nr;
	}

	public void THMIN(double thmin) {
		THMIN = thmin;
	}

	public void DTHET(double dthet) {
		DTHET = dthet;
	}

	public void NTHET(int nthet) {
		NTHET = nthet;
	}

}
