/*
 * Copyright Wen Bian and Billy Zheng. All rights reserved.
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

package org.fiberdiffraction.jad;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * 
 * DataControl class contains all data items associated with control panels.
 * Each datum is defined in a class DatumDef holding its name, type, value
 * and the associated gui component.
 * 
 */
public final class DataControl {
    
    enum DataType { BUTTON, TFIELD, LABELED_TFIELD };
    
    private DataControl() { 
    } 
    
    // RUN tab
    static DatumDef[] DataRun = {
        //
        // data order here is significant - ensuring a text field in line with its button
        //
        new DatumDef(DataType.BUTTON, "Init",  0), new DatumDef(DataType.BUTTON, "Batch",  0),
        new DatumDef(DataType.BUTTON, "CurrR", 0), new DatumDef(DataType.TFIELD, "NcurrR", 1),
        new DatumDef(DataType.BUTTON, "NextR", 0), new DatumDef(DataType.BUTTON, "PrevR",  0),
        new DatumDef(DataType.BUTTON, "Cut",   0), new DatumDef(DataType.TFIELD, "Dcut",   0),
        new DatumDef(DataType.BUTTON, "Uncut", 0), new DatumDef(DataType.BUTTON, "Intgr",  0),
        new DatumDef(DataType.BUTTON, "Summ",  0), new DatumDef(DataType.BUTTON, "Help",   0) 
    };

    // INP tab
    static DatumDef[] DataInp = { 
        new DatumDef(DataType.LABELED_TFIELD, "XWAV",   1.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "SFD",  250.1000),
        new DatumDef(DataType.LABELED_TFIELD, "C",      6.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "DR",     0.0500),
        new DatumDef(DataType.LABELED_TFIELD, "WR",     1.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "IFV",    0     ),
        new DatumDef(DataType.LABELED_TFIELD, "NULB",   1     ), 
        new DatumDef(DataType.LABELED_TFIELD, "RMIN",   1     ),
        new DatumDef(DataType.LABELED_TFIELD, "NR",     1     ), 
        new DatumDef(DataType.LABELED_TFIELD, "THMIN",  0.0000),
        new DatumDef(DataType.LABELED_TFIELD, "DTHET",  1.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "NTHET", 91     )    
    };

    // PAR tab
    static DatumDef[] DataPar = { 
        new DatumDef(DataType.LABELED_TFIELD, "NREF",   25    ),
        new DatumDef(DataType.LABELED_TFIELD, "NPR",    0     ), 
        new DatumDef(DataType.LABELED_TFIELD, "MAXLLS", 0.5000),
        new DatumDef(DataType.LABELED_TFIELD, "MINCUT", 0.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "XINT",   0.0000),
        new DatumDef(DataType.LABELED_TFIELD, "BINT",   0     ), 
        new DatumDef(DataType.LABELED_TFIELD, "INTL",   0     ),
        new DatumDef(DataType.LABELED_TFIELD, "NBG",    1     ), 
        new DatumDef(DataType.LABELED_TFIELD, "BG2",    0     ),
        new DatumDef(DataType.LABELED_TFIELD, "BG3",    0     ), 
        new DatumDef(DataType.LABELED_TFIELD, "CENB",  90.0000),
        new DatumDef(DataType.LABELED_TFIELD, "SIGB",   0.0000),
    };

    // BG tab
    static DatumDef[] DataBg = { 
        new DatumDef(DataType.LABELED_TFIELD, "NG",     1     ),
        new DatumDef(DataType.LABELED_TFIELD, "SIG1",  10.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "SIG2",   0.0000),
        new DatumDef(DataType.LABELED_TFIELD, "DCEN",   0.0000), 
        new DatumDef(DataType.LABELED_TFIELD, "GRAT",   0.0000)
    };
    
    
    /**
     * 
     *  DatumDef is an auxiliary class holding a datum's name, type, 
     *  value and the associated gui component
     *
     */
    static class DatumDef {
        
        private DataType type; 
        private String name;
        private double value;
        private JComponent gui;
        
        DatumDef(DataType type, String name, double value) {
            this.type = type;
            this.name = name;
            this.value = value;
            this.gui = null;
        }
        
        DataType getType()         { return type;  }
        String getName()           { return name;  }        
        double getValue()          { return value; }
        JComponent getGui()        { return gui;   }    
        
        void setType(DataType t)   { type = t;     }
        void setName(String s)     { name = s;     }
        void setValue(double d)    { value = d;    }
        void setGui(JComponent j)  { gui = j;      }        
        
        /** 
         * return value in the string form of integer or double
         */
        String getStringValue() {
            if(isInt()) {
                return String.format("%d", (int)(value + 0.5));
            } else {
                return String.format("%.4f", value);
            }
        }
        
        /**
         * called upon actionPerformed from gui, i.e., user input
         * return null if input valid, else return an error string
         * 
         * @Attention("We return null for success!")
         * @return an error string
         */
        String setValue(String v) {
            try {
                if(isInt()) {
                    value = Integer.parseInt(v);
                } else {
                    value = Double.parseDouble(v);
                }    
                System.out.println(name + " set to " + v);
                setGuiContent();  
                return null;
            }
            catch(NumberFormatException e) {
                String err = "Invalud number format. " + (isInt()? "integer" : "real") + " expected. Reset.";
                System.out.println(err);
                setGuiContent();
                return err;
            }                
        }
        
        /** 
         * push value to gui when value has been updated from non-gui code
         */
        void setGuiContent() {
            if(type == DataType.TFIELD || type == DataType.LABELED_TFIELD) {
                ((JTextField)gui).setText(getStringValue());
            }
        }
        
        /** 
         * if a datum's name starts with I or N, it is an int
         */
        private boolean isInt() {
            return (name.charAt(0) == 'I' || name.charAt(0) == 'N'); 
        }
        
    } // class DatumDef
    
} // class DataControl
