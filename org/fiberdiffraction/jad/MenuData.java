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

package org.fiberdiffraction.jad;

/**
 * This file contains abstract class MenuData that provides menu data manipulation,
 * and concrete subclasses that contain actual menu data.
 * <p>
 * A subclass must override the getMenuNames method to provide the menu data, and 
 * override the getMenuActionClassName method to provide the name of the class 
 * responsible for handling events of a menu item.
 * 
 */
public abstract class MenuData {
    
    /**
     * Sort of a factory method to get actual menu data. Subclasses must override this. 
     * Implementation example:
     * <pre>
     * private final static String[][] menuNames= {
     *         { "menu-0",   "menu-item-0", "menu-item-1"},
     *         { "menu-1",   "menu-item-0", "menu-item-1"}
     * };  
     * protected String[][] getMenuNames() {
     *     return menuNames;
     * }
     * </pre>
     * In current design, menu names are stored in a 2D array of Strings. Each sub-array 
     * contains data for one menu, where the first String in the sub-array is the name 
     * of the menu, and the rest are the names of the menu items in this menu. If a subclass 
     * uses a different representation of menu data, it must override all MenuData methods.
     */
    protected abstract String[][] getMenuNames(); 
    
    /**
     * Subclasses must override this method to provide the MenuAction responsible 
     * for handling events of the menu identified by menuID
     */
    public abstract MenuAction getMenuAction(int menuID);
       
    /**
     * @return  total number of menus in this MenuData
     */
    public int getNumberOfMenus() {
        return getMenuNames().length;
    }
    
    /**
     * @return  total number of menu items in the menu identified by menuID
     */
    public int getNumberOfMenuItems(int menuID) {
        return getMenuNames()[menuID].length - 1;   // first is menu name
    }
    
    /**
     * @return  name of the menu identified by menuID
     */
    public String getMenuName(int menuID) {
        return getMenuNames()[menuID][0];  // first is menu name
    }
  
    /**
     * @return  name of the menu item identified by menuID and menuItemID
     */
    public String getMenuItemName(int menuID, int menuItemID) {
        return getMenuNames()[menuID][menuItemID + 1];  // first is menu name
    }
    
} // class MenuData


/*
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++                                                                                        ++
 * ++  Here below are classes extending MenuData, each containing menu data for a menu bar   ++
 * ++                                                                                        ++
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */


/**
 * A utility class containing the main window's menu data
 */
final class MainMenuData extends MenuData {
    
    private final static String[][] menuNames = {
            { "File",    /**/ "Open", "Save", "Close", "Exit" },     
            { "Data",    /**/ "Input", "Output", "Background" },  
            { "Option",  /**/ "Plot", "Output"},       
            { "Window",  /**/ "Pattern", "Log" }, 
            { "Help",    /**/ "About", "Resource" }
          };    
    
    private final static MenuAction[] actions = {
            new MainFileMenuAction(),
            new MainDataMenuAction(),
            new MainOptionMenuAction(),
            new MainWindowMenuAction(),
            new MainHelpMenuAction()
          };
    
    @Override
    protected String[][] getMenuNames() { 
        return menuNames;     
    }
    
    /**
     * Get the MenuAction responsible for handling events of the menu identified by menuID
     */
    @Override
    public MenuAction getMenuAction(int menuID) { 
        return actions[menuID];
    }

} // class MainMenuData


/**
 * A utility class containing Background window's menu data
 */
final class BackgroundMenuData extends MenuData {
    
    private final static String[][] menuNames = {   
            { "File",    /**/ "Open", "Save", "Exit"},   
            { "Data",    /**/ "Add", "Remove"},   
            { "Fit",     /**/ "Polynomial", "Spline" },  
            { "Option",  /**/ "Color", "Type"}
          };
    
    private final static MenuAction[] actions = {
            new BackgroundFileMenuAction(),
            new BackgroundDataMenuAction(),
            new BackgroundFitMenuAction(),
            new BackgroundOptionMenuAction()
          };
    
    @Override
    protected String[][] getMenuNames() { 
        return menuNames; 
    }
    
    /**
     * Get the MenuAction responsible for handling events of the menu identified by menuID
     */
    @Override
    public MenuAction getMenuAction(int menuID) { 
        return actions[menuID];
    }
    
} // class BackgroundMenuData

