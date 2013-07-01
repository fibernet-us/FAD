package us.fibernet.fad.menu;

/**
 * An interface defining a middleman between menu event (listener) and menu handler.
 * Called by menu listener, and in turn, calls corresponding menu handler. 
 * Concrete MenuCommander(s) are created by concrete MenuData(s).
 * 
 * Roles with regard to Command Pattern:
 * MenuListener  --> Invoker
 * MenuCommander --> Command 
 * MenuHandler   --> Receiver 
 */
public interface MenuCommander {
        void execute();
}
