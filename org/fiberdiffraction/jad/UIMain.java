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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BorderFactory;


/**
 * <pre>
 * The JAD main application window consists of 4 areas:
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

    private static final String JAD_VS = "JAD 0.07";
    private static JFrame mainFrame;
    private static UIMenubar uiMenubar;
    private static UIPloting uiPloting;
    private static UIMessage uiMessage;
    private static UIControl uiControl;
    private static int wControl = 220; // width of uiControl: wide enough to show all ctrl tabs
    private static int hMessage = 30;  // height of uiMessage: high enough for one line of text
    //private CoreData idata; // to be passed to UI objects

    // no instantiation
    private UIMain() { }
       
    /*
     * Create application UI and data hooks. Synchronize it to ensure UI creation happen once 
     * and only once. No use of double lock singleton which does not work on all JVMs
     *
     * w, h, x, y: app window startup dimension and top-left position
     * wCtrl: if > 0 overwrite default wControl 
     * hMess: if > 0 overwrite default hMessage
     */
    public static synchronized void init(int w, int h, int x, int y, int wCtrl, int hMess) {
        
        if(mainFrame != null) {
            return;
        }

        if(wCtrl > 0) {
            wControl = wCtrl;
        }

        if(hMess > 0) {
            hMessage = hMess;
        }
        
        mainFrame = new JFrame();
        mainFrame.setBounds(x, y, w, h);
        mainFrame.setTitle(JAD_VS);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        uiMenubar = new UIMenubar(mainFrame);
        uiPloting = new UIPloting(mainFrame, w - wControl, h - hMessage);
        uiMessage = new UIMessage(mainFrame, w - wControl, hMessage);
        uiControl = new UIControl(mainFrame, wControl,      h);

        uiPloting.setBorder(BorderFactory.createLineBorder(Color.gray));
        uiMessage.setBorder(BorderFactory.createLineBorder(Color.gray));
        uiControl.setBorder(BorderFactory.createLineBorder(Color.gray));

        Box vBox = Box.createVerticalBox();
        vBox.add(uiPloting);
        vBox.add(uiMessage);
        
        Box hBox = Box.createHorizontalBox();
        hBox.add(vBox);
        hBox.add(uiControl);
        
        mainFrame.getContentPane().add(hBox, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }


    // TODO: parse command line argument or configuration file
    /**
     * @param args (w, h, x, y, wControl, hMessage) <br>
     *              w, h, x, y: app window's startup dimension and top-left position, <br>
     *              wControl, hMessage: width of control panel and height of message panel 
     *              (default values will be used if given 0)
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIMain.init(800, 600, 100, 100, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

} // Class UIMain
