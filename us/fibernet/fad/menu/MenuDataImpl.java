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

package us.fibernet.fad.menu;

 /**
 * MenuDataImpl provides a partial implementation of MenuData. Concrete subclasses 
 * will need to implement the getMenuNames() and getMenuHandler() methods.
 */
public abstract class MenuDataImpl implements MenuData {
    
    /**
     * Subclasses must override this method to provide actual menu data. 
     * Sort of a factory method. Implementation example:
     * <pre>
     * protected String[][] getMenuNames() {
     *     final String[][] MENU_NAMES= {
     *         { "menu-0",   "menu-item-0", "menu-item-1"},
     *         { "menu-1",   "menu-item-0", "menu-item-1"}
     *     return MENU_NAMES;
     * }
     * </pre>
     * In this implementation, menu names are stored in a 2D array of Strings. Each sub-array 
     * contains data for one menu, where the first String in the sub-array is the name 
     * of the menu, and the rest are the names of the menu items in this menu. If a subclass 
     * uses a different representation of menu data, it must override all MenuData methods.
     */
    protected abstract String[][] getMenuNames(); 
         
    /**
     * Subclasses must override this method to provide the handler for the menu item 
     * identified by menuID and menuItemID.
     */
    protected abstract MenuHandler getMenuHandler(int menuID, int menuItemID);
    
    /*
     * The following code provides an implementation for each method in MenuData interface.
     * Dependent on getMenuNames() and getMenuHandler().
     */
    
    /** @return  total number of menus in this MenuData  */
    public int getNumberOfMenus() {
        return getMenuNames().length;
    }
    
    /** @return  total number of menu items in the menu identified by menuID  */
    public int getNumberOfMenuItems(int menuID) {
        return getMenuNames()[menuID].length - 1;   // first is menu name
    }
    
    /** @return  name of the menu identified by menuID */
    public String getMenuName(int menuID) {
        return getMenuNames()[menuID][0];  // first is menu name
    }
    
    /** @return  name of the menu item identified by menuID and menuItemID  */
    public String getMenuItemName(int menuID, int menuItemID) {
        return getMenuNames()[menuID][menuItemID + 1];  // first is menu name
    }
    
    /** Get the MenuCommander for the menu item identified by menuID and menuItemID  */
    public MenuCommander getMenuItemCommander(final int menuID, final int menuItemID) {    
        
        // create and return an anonymous MenuCommander
        return  new MenuCommander() {          
            
            MenuHandler handler = getMenuHandler(menuID, menuItemID);
            
            public void execute() {
                
                // construct method name from menu name and menu item names. 
                // e.g., "Draw -> Clear All" --> "drawClearAll"
                String menuName = getMenuName(menuID).replaceAll("\\s","");
                String itemName = getMenuItemName(menuID, menuItemID).replaceAll("\\s","");
                String methodName = menuName.substring(0, 1).toLowerCase() + menuName.substring(1) + itemName;
                
                // look up the method in the handler class and call it if successful
                try {             
                    java.lang.reflect.Method method = handler.getClass().getDeclaredMethod(methodName);
                    if(method != null) {
                        try {
                            method.invoke(handler, (Object[]) null);  // invoke handler.method()
                        } 
                        catch (java.lang.reflect.InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }   
                    }
                } 
                catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }   
                
            } // execute()
                  
        }; // new MenuCommander() {  
    }
    
    
} // class MenuDataImpl
