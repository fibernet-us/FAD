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

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;



/**
 * <pre>
 * The FAD main application window consists of 4 areas (besides title bar):
 *  _____________
 * |______0______|
 * |         |   |
 * |    1    | 3 |
 * |         |   |
 * |_________|   |
 * |____2____|___|
 * 
 * 0: menu bar, 1: plotting panel, 2: message panel, 3: control panel
 * </pre>
 */
public final class UIMain {

    private static final String FAD_VS = "FAD 0.2";
    private static JFrame mainFrame;
    private static UIMenubar uiMenubar;
    private static UIPloting uiPloting;
    private static UIMessage uiMessage;
    private static UIControl uiControl;
    private static int wControl = 200; // width of uiControl: wide enough to show all ctrl tabs
    private static int hMessage = 30;  // height of uiMessage: high enough for one line of text

    // no instantiation
    private UIMain() { }
       
    /**
     * Create main UI and data hooks. Synchronized to ensure main UI creation happen 
     * only once. No use of double-checked locking which does not work on all JVMs.
     *
     * @param w  width of the main window at startup         
     * @param h  height of the main window at startup   
     * @param x  x-coordinate of the main window's startup top-left position
     * @param y  y-coordinate of the main window's startup top-left position    
     * @param wCtrl  if > 0 overwrite default wControl       
     * @param hMess  if > 0 overwrite default hMessage
     */
    public static synchronized void initialize(int w, int h, int x, int y, int wCtrl, int hMess) {
        
        if(mainFrame != null) {
            return;
        }

        if(wCtrl > 0) {
            wControl = wCtrl;
        }

        if(hMess > 0) {
            hMessage = hMess;
        }
        
        mainFrame = new JFrame(FAD_VS);
        mainFrame.setBounds(x, y, w, h);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        uiMenubar = new UIMenubar(mainFrame, new MenuDataMain());
        uiPloting = new UIPloting(mainFrame, w - wControl, h - hMessage);
        uiMessage = new UIMessage(mainFrame, w - wControl, hMessage);
        uiControl = new UIControl(mainFrame, wControl,     h);

        Border lineBorder = BorderFactory.createLineBorder(Color.gray);
        uiPloting.setBorder(lineBorder);
        uiMessage.setBorder(lineBorder);
        uiControl.setBorder(lineBorder);

        // use BorderLayout to keep height of uiMessage and width of uiControl fixed
        // and let uiPloting take on resizing (BorderLayout.CENTER is greedy)
        JPanel plotmes = new JPanel(new BorderLayout());
        plotmes.add(uiPloting, BorderLayout.CENTER);
        plotmes.add(uiMessage, BorderLayout.PAGE_END); 
        mainFrame.add(plotmes, BorderLayout.CENTER);
        mainFrame.add(uiControl, BorderLayout.LINE_END); 
        
        mainFrame.setVisible(true);
    }

    /**
     * @param args (w, h, x, y, wControl, hMessage) <br>
     *              w, h, x, y: app window's startup dimension and top-left position. <br>
     *              wControl, hMessage: width of control panel and height of message panel.
     */
    /*
     *  TODO: parse/check command line argument & configuration file (~/.fad) 
     */ 
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIMain.initialize(800, 600, 100, 100, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

} // Class UIMain
