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

/**
 * A class to hold Gauss related parameters
 */
public class DataGauss {
	
	private int IG;
	private double SIG1;
	private double SIGG2;
	private double DCEN;
	private double GRAT;

	public DataGauss() {
		IG    = 1;
		SIG1  = 1.5;
		SIGG2 = 0.0;
		DCEN  = 0.0;
		GRAT  = 0.0;
	}

	public DataGauss(int ig, double sigg, double sigg2, double dcen, double grat) {
		IG    = ig;
		SIG1  = sigg;
		SIGG2 = sigg2;
		DCEN  = dcen;
		GRAT  = grat;
	}
	
	// getters
	public int IG()        { return IG;    }
	public double SIGG()   { return SIG1;  }
	public double SIGG2()  { return SIGG2; }
	public double DCEN()   { return DCEN;  }	
	public double GRAT()   { return GRAT;  }
	
	// setters
	public void IG(int ig)           { IG    = ig;    }
	public void SIGG(double sigg)    { SIG1  = sigg;  }
	public void SIGG2(double sigg2)  { SIGG2 = sigg2; }
	public void DCEN(double dcen)    { DCEN  = dcen;  }
	public void GRAT(double grat)    { GRAT  = grat;  }
	
	// set data by label
	public void setData(String s, String d) {
/*
		try {
			switch(s) {
			case "IG":    IG    = Integer.parseInt(d);
			              break;
			case "SIG1":  SIG1  = Double.parseDouble(d);
			              break;
			case "SIGG2": SIGG2 = Double.parseDouble(d);
			              break;
			case "DCEN":  DCEN  = Double.parseDouble(d);
			              break;
			case "GRAT":  GRAT  = Double.parseDouble(d);
			              break;
			default:      System.out.println("*** Who are you? " + s);
			              break;
			}
		}
		catch(NumberFormatException e) {
			System.out.println("*** Invalid number format for " + s + ": " + d);
		}
*/
	}
	
	public String toString() {
		return "IG=" + IG + ", SIG1=" + SIG1 + ", SIGG2=" + SIGG2 +
		       ", DCEN=" + DCEN + ", GRAT=" + GRAT;
	}
}
