/*
 * Copyright Billy Zheng and Wen Bian. All rights reserved.
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

//import static java.lang.System.out;

package org.fiberdiffraction.jad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public final class UIMenubar extends JMenuBar {

	private static final int  NMENU = 5;	
	private static String[]   topMenuLabel = { "File", "Data", "Option", "Window", "Help" };
	private static String[][] subMenuLabel = { 
			{ "Open", "Save", "Close", "Exit" }, // File
			{ "Input", "Output", "Background" }, // Data
			{ "Plot", "Output", },               // Option
			{ "Pattern", "Log" },                // Window
			{ "About" }                          // Help
	};
	
	public UIMenubar(JFrame parent) {
		createMenu(parent);
	}
	
	/**
	 *  Create top menu and sub menus on the JFrame parent
	 */
	private void createMenu(JFrame parent) {
		
		JMenuBar menuBar = new JMenuBar();
		parent.setJMenuBar(menuBar);

		JMenu[] topMenu = new JMenu[NMENU];
		for (int i = 0; i < NMENU; i++) {
			topMenu[i] = new JMenu(topMenuLabel[i]);
			menuBar.add(topMenu[i]);
		}

		JMenuItem[][] subMenu = new JMenuItem[NMENU][];
		for (int i = 0; i < NMENU; i++) {
			int n = subMenuLabel[i].length;
			subMenu[i] = new JMenuItem[n];
			for (int j = 0; j < n; j++) {
				subMenu[i][j] = new JMenuItem(subMenuLabel[i][j]);
				topMenu[i].add(subMenu[i][j]);
			}
		}

		// File menu ----------------------------------------------------------
		
		// File - Open
		subMenu[0][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					ImageInput.getInputImage(file.getAbsolutePath());
				} else {
					System.out.println("Open command cancelled");
				}
			}
		});

		// File - Save
		subMenu[0][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println(file.getName());
				} else {
					System.out.println("Open command cancelled");
				}
			}
		});

		// File - Close
		subMenu[0][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		// File - Exit
		subMenu[0][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// Data menu ----------------------------------------------------------
		
		// Data - Input
		subMenu[1][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

			}
		});

		// Data - Output
		subMenu[1][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				    // TODO
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Data - DataPlot
		subMenu[1][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				    // TODO
					//DataPlot bkgd = new DataPlot("Background", idata);
					//bkgd.setLocationRelativeTo(parent);
					//bkgd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Option menu --------------------------------------------------------
		
		// Option - Plot
		subMenu[2][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		// Option - Output
		subMenu[2][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		// Window menu --------------------------------------------------------
		
		// Window - Pattern
		subMenu[3][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				    // TODO
					// Background bkgd = new Background("Background", idata);
					// bkgd.setLocationRelativeTo(parent);
					// bkgd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Window - Log
		subMenu[3][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			 // TODO
			}
		});

		// Help menu ----------------------------------------------------------
		
		// Help - About
		subMenu[4][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			 // TODO
			}
		});
	}
}
