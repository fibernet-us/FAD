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
 * JAD main application window contains
 * a menu bar and 3 panels for plotting, message, and control
 *  _____________
 * |______0______|
 * |         |   |
 * |    1    | 3 |
 * |         |   |
 * |_________|   |
 * |____2____|___|
 * 
 * 0: menu bar, 1: plotting, 2: message, 3: control
 * 
 */
public class UIMain {

	private static String JAD_VS = "JAD 0.06";
	private static int UI_CONTROL_WIDTH = 220; // be wide enough to show all ctrl tabs
	private static int UI_MESSAGE_HEIGHT = 30; // be high enough for one line of text

	private JFrame mainFrame;
	private UIMenubar ui_menubar;
	private UIPloting ui_ploting;
	private UIMessage ui_message;
	private UIControl ui_control;
	private int w0, h0, x0, y0;   // startup dimension and top-left position
	
	//private CoreData idata; // to be implemented and passed to UI objects

	public UIMain(int w0, int h0, int x0, int y0) {
		this.w0 = w0;
		this.h0 = h0;
		this.x0 = x0;
		this.y0 = y0;
		createUI();
	}

	private void createUI() {

		mainFrame = new JFrame();
		mainFrame.setBounds(x0, y0, w0, h0);
		mainFrame.setTitle(JAD_VS);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ui_menubar = new UIMenubar(mainFrame);

		// TODO: calculate exact work area dimensions
		ui_ploting = new UIPloting(mainFrame, w0 - UI_CONTROL_WIDTH, h0 - UI_MESSAGE_HEIGHT);
		ui_message = new UIMessage(mainFrame, w0 - UI_CONTROL_WIDTH, UI_MESSAGE_HEIGHT);
		ui_control = new UIControl(mainFrame, UI_CONTROL_WIDTH, h0);

		ui_ploting.setBorder(BorderFactory.createLineBorder(Color.gray)); 
		ui_message.setBorder(BorderFactory.createLineBorder(Color.gray));
		ui_control.setBorder(BorderFactory.createLineBorder(Color.gray));

		Box vBox = Box.createVerticalBox();   // 1 + 2
		vBox.add(ui_ploting);
		vBox.add(ui_message);
		Box hBox = Box.createHorizontalBox(); // 12 + 3
		hBox.add(vBox);
		hBox.add(ui_control);
		mainFrame.getContentPane().add(hBox, BorderLayout.CENTER);

		mainFrame.setVisible(true);
	}

	// TODO: parse command line args
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new UIMain(800, 600, 100, 100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

} // Class UIMain
