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

package us.fibernet.fad;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  A class for creating a JMenuBar with a MenuData, on a JFrame. Each menu item 
 *  is associated with a MenuActionListener, which hooks it up with a MenuAction
 *  (one MenuAction per menu).
 */
public class UIMenubar {

    private JMenuBar menuBar;
    private JFrame parentFrame;
    private MenuData menuData;
    
    public UIMenubar(JFrame parentFrame, MenuData menuData) { 
        this.parentFrame = parentFrame;
        this.menuData = menuData;
        initialize();
    }   
    
    /*
     *  Build menuBar with menuData, add a MenuActionListener, and attach it to parentFrame 
     *  
     *  menu-0        menu-1       ...
     *  menu-item-0   menu-item-0  ...
     *  menu-item-1   menu-item-1  ...
     *  ...           ...          ...
     */
    protected void initialize() {

                    menuBar = new JMenuBar();     
        int          nMenus = menuData.getNumberOfMenus();       
        JMenu[]       menus = new JMenu[nMenus];
        JMenuItem[][] items = new JMenuItem[nMenus][];
        
        for (int i = 0; i < nMenus; i++) {             
            int nItems = menuData.getNumberOfMenuItems(i);
            menus[i] = new JMenu(menuData.getMenuName(i)); 
            items[i] = new JMenuItem[nItems];
            MenuActionListener mal = new MenuActionListener(menuData.getMenuAction(i));

            // build menus[i]: create menu items and add action listener
            for (int j = 0; j < nItems; j++) {
                items[i][j] = new JMenuItem(menuData.getMenuItemName(i, j));
                items[i][j].addActionListener(mal);
                menus[i].add(items[i][j]);
            }             
 
            menuBar.add(menus[i]);            
        }      

        parentFrame.setJMenuBar(menuBar);
    }     
    
    /*
     *  An ActionListener implementer. It connects a MenuAction 
     *  corresponding to a menu item and calls its execute() on menu events
     */
    protected class MenuActionListener implements ActionListener {   
        
        private MenuAction menuAction;
        
        public MenuActionListener(MenuAction menuAction) {
            this.menuAction = menuAction;
        }
        
        public void actionPerformed(ActionEvent event) {
            if(menuAction != null) {
                String command = ((JMenuItem)event.getSource()).getText();
                //System.out.println(command);
                menuAction.execute(command);
            }
        }
        
    } // class MenuActionListener

    
} // class UIMenubar
