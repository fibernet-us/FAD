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
