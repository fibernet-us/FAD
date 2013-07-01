package us.fibernet.fad.menu;

/**
 * A utility class containing the main window's menu data
 */
public final class MenuDataMain extends MenuDataImpl {
    
    private static final String[][] MENU_NAMES = {
        { "File",    /**/ "Open", "Save", "Close", "Exit" },     
        { "Data",    /**/ "Input", "Output", "Background" },  
        { "Option",  /**/ "Plot", "Output"},       
        { "Window",  /**/ "Pattern", "Log" }, 
        { "Help",    /**/ "About", "Resource" }
    };  

    // one handler per menu
    private static final MenuHandlerMain[] MENU_HANDLERS = {
        new MenuHandlerMainFile(),  // File
        new MenuHandlerMain(),      // Data     // TODO
        new MenuHandlerMain(),      // Option   // TODO
        new MenuHandlerMain(),      // Window   // TODO
        new MenuHandlerMain()       // Help     // TODO
    };

    public MenuDataMain() {
    }  
     
    @Override
    protected String[][] getMenuNames() { 
        return MENU_NAMES;     
    }
    
    @Override
    protected MenuHandler getMenuHandler(int menuID, int menuItemID) {  
        return MENU_HANDLERS[menuID];
    }

} // class MenuDataMain
