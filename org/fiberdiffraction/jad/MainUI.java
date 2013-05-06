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

package org.fiberdiffraction.jad;

import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

//import static java.lang.System.out;

/**
 * This is the main application window for the program jrad. It contains main
 * menu and 3 panels for plot, command and message.
 */
public class MainUI {

	private JFrame radFrame;
	private CoreData idata;

	final int NMENU = 5;
	String[] topMenuLabel = { "File", "Data", "Option", "Window", "Help" };
	String[][] subMenuLabel = { { "Open", "Save", "Close", "Exit" }, // File
			{ "Input", "Output", "Background" }, // Data
			{ "Plot", "Output", }, // Option
			{ "Pattern", "Log" }, // Window
			{ "About" } // Help
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.radFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the application UI.
	 */
	private void initialize() {

		radFrame = new JFrame();
		radFrame.setBounds(100, 100, 600, 500);
		radFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		radFrame.setJMenuBar(menuBar);

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

		// int i = Arrays.asList(topMenuLabel).indexOf("File");

		// File - Open
		subMenu[0][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println(file.getName());
					ImageInput.getInputImage(file.getName());
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

		// Data - Input
		subMenu[1][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

			}
		});

		// Data - Output
		subMenu[1][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Data - DataPlot
		subMenu[1][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					DataPlot bkgd = new DataPlot("Background", idata);
					bkgd.setLocationRelativeTo(radFrame);
					bkgd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

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

		// Window - Pattern
		subMenu[3][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					// Background bkgd = new Background("Background", idata);
					// bkgd.setLocationRelativeTo(radFrame);
					// bkgd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Window - Log
		subMenu[3][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

			}
		});

		// Help - About
		subMenu[4][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

			}
		});
	} // initialize

} // Class MainUI

