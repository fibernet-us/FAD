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
 * A class to hold Command related user input.
 */
public class DataRun {

	private int VcurR;		// current radius to process
	private double Vcut;		// data Vcut off at this degree

	public DataRun() {
		VcurR = 1;
		Vcut  = 1.5;
	}

	public DataRun(int vcurr, double vcut) {
		VcurR = vcurr;
		Vcut  = vcut;
	}

	// getters
	public int VcurR()    { return VcurR;  }
	public double Vcut()  { return Vcut;   }

	// setters
	public void VcurR(int vcurr)    { VcurR = vcurr;  }
	public void Vcut(double vcut)   { Vcut  = vcut;   }

	// set data by label, or run command by label
	public void setData(String s, String v)
	{
		/*
		try {
			switch(s) {
			case "VcurR": VcurR = Integer.parseInt(v);
			              break;
			case "Vcut":  Vcut = Double.parseDouble(v);
			              break;
			case "Init":  runInit();
			              break;
			case "Batch": runBatch();
			              break;
			case "CurrR": runCurrR();
			              break;
			case "NextR": runNextR();
			              break;
			case "PrevR": runPrevR();
			              break;
			case "Cut":   runCut();
			              break;
			case "Uncut": runUncut();
			              break;
			case "Integ": runInteg();
			              break;
			case "Summa": runSumma();
			              break;
			case "Help":  runHelp();
			              break;
			default:      System.out.println("*** Who are you? " + s);
			              break;
			}
		}
		catch(Exception e) {
			System.out.println("*** Invalid input for " + s + ": " + v);
		}	
		*/	
	}

	public void runInit()  {}

	public void runBatch() {}

	public void runCurrR()  {}

	public void runNextR()  {}

	public void runPrevR()  {}

	public void runCut()  {}

	public void runUncut()  {}

	public void runInteg()  {}

	public void runSumma()  {}

	public void runHelp()  {}

	public String toString() {
		return "VcurR=" + VcurR + ", Vcut=" + Vcut;
	}
}