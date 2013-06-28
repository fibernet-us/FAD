/*
 * Copyright Wen Bian. All rights reserved.
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

import java.io.File;
import javax.swing.JFileChooser;

/**
 * This file contains interface MenuAction and its implementers for handling menu item actions.
 * <p>
 * We are using smart concrete MenuAction implementers, instead of dumb ones with receivers 
 * commonly used in the Command Pattern, to decrease the number of classes and code duplication. 
 * One implementer takes care of one menu that consists of one or more menu items. 
 */

public interface MenuAction {    
    void execute(String command);
}

/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++                                                                                           ++
 * ++   Here below are classes implementing MenuAction, each handling one menu in a menu bar.   ++
 * ++   If a class is nontrivial take it to the top level, but leave a note in its place.       ++
 * ++                                                                                           ++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

/**
 * A smart concrete MenuAction implementer for handling: Main menu bar, File menu
 */
class MainFileMenuAction implements MenuAction {  
    
    // unless we have Java 1.7, use this for switching command in execute()
    private static enum CommandEnum  {
        OPEN, SAVE, CLOSE, EXIT; // same as those in MainMenuData File menu, case/space insensitive.
    }
    
    public void execute(String command) {
        try {
            // use Enum's valueOf to convert string to enum
            switch(CommandEnum.valueOf(command.toUpperCase().replaceAll("\\s",""))) {
                case OPEN:  open();
                break;
                case SAVE:  save();
                break;
                case CLOSE: close();
                break;
                case EXIT:  exit();
                break;
                default:    break;
            }
        }
        catch (Exception ex) {
            System.out.println("Invalid menu item: " + command);
        }
    }
   
    /*
     * Main menu bar, File -> Open
     */
    protected void open() {
        JFileChooser fc = new JFileChooser();   
        int returnVal = fc.showOpenDialog(null);
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            ImageInput.getInputImage(file.getAbsolutePath());
        } 
        else {
            System.out.println("Open command cancelled");
        }
    }
 
    /*
     * Main menu bar, File -> Save
     */
    protected void save() {

    }
    
    /*
     * Main menu bar, File -> Close
     */
    protected void close() {

    }
    
    /*
     * Main menu bar, File -> Exit
     */
    protected void exit() {
        System.exit(0);
    }
}

/**
 * A smart concrete MenuAction implementer for handling: Main menu bar, Data menu
 */
class MainDataMenuAction implements MenuAction {   
    public void execute(String command) {
        
    }
}

/**
 * A smart concrete MenuAction implementer for handling: Main menu bar, Option menu
 */
class MainOptionMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Main menu bar, Window menu
 */
class MainWindowMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Main menu bar, Help menu
 */
class MainHelpMenuAction implements MenuAction {   
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Background menu bar, File menu
 */
class BackgroundFileMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Background menu bar, Data menu
 */
class BackgroundDataMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Background menu bar, Fit menu
 */
class BackgroundFitMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}

/**
 * A smart concrete MenuAction implementer for handling: Background menu bar, Option menu
 */
class BackgroundOptionMenuAction implements MenuAction { 
    public void execute(String command) {
        
    }    
}
