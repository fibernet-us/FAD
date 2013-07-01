/*
 * Copyright Billy Zheng. All rights reserved.
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

import java.io.BufferedReader;
import java.io.IOException;

/**
 * PLR data headerFormat:
 * 
 * Header
 * ======================================================================================== 
 *  "IFV,NULB,RMIN,NR,WR,DR,XW,THMIN,DTHET,NTHET,AME,BME,SFD,IFOG,NGEOM,IOPT,IQ,SFD,REPEAT"
 *  "%5d%3d%6.1f%7d%6.1f%10.6f%9.5f%6.2f%6.2f%5d%5s%5s%12.4f" (IFV ... SFD)
 *  "%5d%3d%3d%3d%3d%3d%3d%12.4f%12.4f"                       (IFOG ... REPEAT)
 *  "99999"
 *  
 * Data
 * ==============================
 *  "%6.1f%12.1f    %1d    0.000" (angle, x-ray-intensity, binCount)
 *  ...
 *  
 */
public class DataPlr { 
    
    int IFV, NULB, NTHET, IFOG, NGEOM, IOPT, NR;
    int[] IQ = {0, 0, 0, 0};
    double RMIN, WR, DR, XW, THMIN, DTHET, AME, BME, SFD, SFD2, REPEAT;
    
    int[][] dataPlr;  // to hold intensity data in polar headerFormat
    
    static String lastPar = "REPEAT";    // last parameter name on the title line
    static String headerTerm = "99999";  // terminator string of header section
    static String headerFormat = "%5d%3d%6.1f%7d%6.1f%10.6f%9.5f%6.2f%6.2f%5d%5s%5s%12.4f" +
    		                     "%5d%3d%3d%3d%3d%3d%3d%12.4f%12.4f";

    public DataPlr() {      
    }
    
    /**
     * @return  2D image data in Cartesian headerFormat
     */
    public int[][] getImageData() {
        
        if(dataPlr == null) {
            return null;
        }
        
        int width, height;
        width = height = NR + roundit(RMIN);
        return plrToCartesian(width, height);
    }
    
    /**
     * @throws Exception  both IO and NumberFormat Exceptions possible
     */
    public boolean parseData(BufferedReader br) throws Exception { 
  
        // parse header. a blank line signals end of header      
        String header="", line;
        try {
            while((line = br.readLine()) != null && !line.equals(headerTerm)) {
                header += line;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        if(parseHeader(header)) {  // header read successfully
         
            // read data for each R, each contain NTHET pieces of data
            // TODO: set NR to (NR + RMIN) ?
            dataPlr = new int[NR][NTHET];
            //System.out.println("NR=" + NR + ", NT=" + NTHET);

            // TODO: 
            // read only intensity at this time
            // "%6.1f%12.1f    %1d    0.000" (angle, x-ray-intensity, binCount)
            try {
                for(int i=0; i<NR; i++) {
                    for(int j=0; j<NTHET; j++) {
                        line = br.readLine().trim();
                        dataPlr[i][j] = roundit(Double.parseDouble(line.substring(6,18)));
                    }
                }    
            }
            catch (Exception e) {
                e.printStackTrace();
                dataPlr = null;
                return false;
            }
        }
        
        return true;
    }
    
    /** 
     *  TODO: put par names and values in a hashtable? 
     *
     *  Current header:
     *  
     *  "IFV,NULB,RMIN,NR,WR,DR,XW,THMIN,DTHET,NTHET,AME,BME,SFD,IFOG,NGEOM,IOPT,IQ,SFD,REPEAT"
     *  "%5d%3d%6.1f%7d%6.1f%10.6f%9.5f%6.2f%6.2f%5d%5s%5s%12.4f%5d%3d%3d%3d%3d%3d%3d%12.4f%12.4f"
     */
    public boolean parseHeader(String header) {
        
        if(header == null) {
            return false;
        }
        
        header = header.substring( header.indexOf(lastPar) + lastPar.length());     
        double[] par = extractNumberFromString(header, headerFormat, '%');  
        
        if(par == null) {
            return false;
        }
        
        /*
        for(double d : par) {
            System.out.println(d);
        }
        */
              
        try {
            int i = 0;
            IFV    = roundit(par[i++]);
            NULB   = roundit(par[i++]);
            RMIN   =         par[i++];
            NR     = roundit(par[i++]);
            WR     =         par[i++];
            DR     =         par[i++];
            XW     =         par[i++];
            THMIN  =         par[i++];
            DTHET  =         par[i++];
            NTHET  = roundit(par[i++]);
            AME    =         par[i++];
            BME    =         par[i++];
            SFD    =         par[i++];
            IFOG   = roundit(par[i++]);
            NGEOM  = roundit(par[i++]);
            IOPT   = roundit(par[i++]);
            IQ[0]  = roundit(par[i++]);
            IQ[1]  = roundit(par[i++]);
            IQ[2]  = roundit(par[i++]);
            IQ[3]  = roundit(par[i++]);
            SFD    =         par[i++];
            REPEAT =         par[i];
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     * @param numstr  input string containing numbers 
     * @param headerFormat  printf style headerFormat, e.g., %5d%8.2f
     * @param fmtsep  headerFormat separator/specifier, usually % and may not be .
     * @return  a array of double parsed from the input string
     */
    public double[] extractNumberFromString(String numstr, String format, char fmtsep) {

        String pattern = "[^" + fmtsep + "]";
        int count = format.replaceAll(pattern, "").length();  // get number of fmtsep  
        if(count <= 0) {
            return null;
        }
        
        double[] numberArray = new double[count];
        int ni = 0;  // number of numbers parsed
        int si = 0;  // current working index of numstr
        int fi = format.indexOf(fmtsep, 0); // current working index of headerFormat
        
        while(ni < count && si < numstr.length() && fi < format.length()) {
            
            try {
                int finext = format.indexOf(fmtsep, fi + 1);
                if(finext < 0) {
                    finext = format.length();
                }
                
                // get the value of xx in %xx.yy[dfs]%
                int len = (int)Double.parseDouble( format.substring(fi + 1, finext - 1) ); 
                
                try { // parse corresponding number
                    numberArray[ni] = Double.parseDouble( numstr.substring(si, si + len) ); 
                }
                catch (NumberFormatException e) {
                    ; //e.printStackTrace(); // just skip non-number field
                }
                
                /*
                System.out.println(headerFormat);
                for(int x=0; x<fi; x++)
                    System.out.print(" ");
                System.out.println("^" + len);
                
                System.out.println(numstr);
                for(int x=0; x<si; x++)
                    System.out.print(" ");
                for(int x=si; x<si+len; x++)
                    System.out.print("^");         
                System.out.println("\n\n\n");
                */
                
                ++ni;
                fi = finext;
                si = si + len;
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        return numberArray;
        
    }  
    
    // convert data from polar to Cartesian
    private int[][] plrToCartesian(int width, int height)
    {
        int[][] dataCarte = new int[height][width];
        int rmax = dataPlr.length;
        int amax = dataPlr[0].length;

        for(int h=0; h<height; h++) {
            for(int w=0; w<width; w++) {
                int r = roundit(Math.sqrt(h*h + w*w));
                int a = roundit(Math.atan2(w, h) * 180 / Math.PI);
                if(r < rmax && a < amax) {
                    dataCarte[h][w] = dataPlr[r][a];
                }
            }
        }
        
        return dataCarte;
    }
    
    private int roundit(double d) {
        return (int) Math.round(d);
    }
}

