/*
 * Copyright Tony Yao and Wen Bian. All rights reserved.
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel for control UI - a JTabbedPane with 4 tabs
 */
@SuppressWarnings("serial")
public class UIControl extends JPanel {

	private static int WIDGET_WIDTH = 80, WIDGET_HEIGHT = 30;
	private int width, height;
	private JFrame parentFrame;
	private JTabbedPane controlTabs;
	private JPanel pageRun, pageInp, pagePar, pageBg;
	
	enum Jtype { BUTTON, LABEL, TFIELD };
	
	static class WidgetInfo {
		Jtype type; 
		String name;
		WidgetInfo(Jtype type, String name) {
			this.type = type;
			this.name = name;
		}
	}
	
	private static WidgetInfo[] widgetsRun = {
			new WidgetInfo(Jtype.BUTTON, "Init"),  new WidgetInfo(Jtype.BUTTON, "Batch"),
			new WidgetInfo(Jtype.BUTTON, "CurrR"), new WidgetInfo(Jtype.TFIELD, "TcurrR"),
			new WidgetInfo(Jtype.BUTTON, "NextR"), new WidgetInfo(Jtype.BUTTON, "PrevR"),
			new WidgetInfo(Jtype.BUTTON, "Cut"),   new WidgetInfo(Jtype.TFIELD, "Tcut"),
			new WidgetInfo(Jtype.BUTTON, "Uncut"), new WidgetInfo(Jtype.BUTTON, "Intgr"),
			new WidgetInfo(Jtype.BUTTON, "Summ"),  new WidgetInfo(Jtype.BUTTON, "Help") 
	};

	private static WidgetInfo[] widgetsInp = { 
			new WidgetInfo(Jtype.LABEL, " XWAV"),
			new WidgetInfo(Jtype.LABEL, "  SFD"), 
			new WidgetInfo(Jtype.LABEL, "    C"),
			new WidgetInfo(Jtype.LABEL, "   DR"), 
			new WidgetInfo(Jtype.LABEL, "   WR"),
			new WidgetInfo(Jtype.LABEL, "  IFV"), 
			new WidgetInfo(Jtype.LABEL, " NULB"),
			new WidgetInfo(Jtype.LABEL, " RMIN"), 
			new WidgetInfo(Jtype.LABEL, "   NR"),
			new WidgetInfo(Jtype.LABEL, "THMIN"), 
			new WidgetInfo(Jtype.LABEL, "DTHET"),
			new WidgetInfo(Jtype.LABEL, "NTHET") 
	};

	private static WidgetInfo[] widgetsPar = { 
			new WidgetInfo(Jtype.LABEL, " NREF"),
			new WidgetInfo(Jtype.LABEL, "  NPR"), 
			new WidgetInfo(Jtype.LABEL, "MAXLS"),
			new WidgetInfo(Jtype.LABEL, "MINCU"), 
			new WidgetInfo(Jtype.LABEL, " XINT"),
			new WidgetInfo(Jtype.LABEL, " BINT"), 
			new WidgetInfo(Jtype.LABEL, " INTL"),
			new WidgetInfo(Jtype.LABEL, "  NBG"), 
			new WidgetInfo(Jtype.LABEL, "  BG2"),
			new WidgetInfo(Jtype.LABEL, "  BG3"), 
			new WidgetInfo(Jtype.LABEL, " CENB"),
			new WidgetInfo(Jtype.LABEL, " SIGB") 
	};

	private static WidgetInfo[] widgetsBg = { 
			new WidgetInfo(Jtype.LABEL, "   IG"),
			new WidgetInfo(Jtype.LABEL, " SIG1"), 
			new WidgetInfo(Jtype.LABEL, " SIG2"),
			new WidgetInfo(Jtype.LABEL, " DECN"), 
			new WidgetInfo(Jtype.LABEL, " GRAT") 
	};

	// need to find widget by name to do things like setting text field
	private Map<String, JComponent> parTable;   
	
	/**
	 * Constructor
	 */
	public UIControl(JFrame parent, int width, int height) {
		this.parentFrame = parent;
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height)); 
		setMinimumSize(new Dimension(width, height)); 
		parTable = new HashMap<String, JComponent>();
		initialize();
	}

	/**
	 * Initialize the contents of the control panels.
	 */
	private void initialize() {

		setLayout(new BorderLayout());

		// Create tab pages
		createTabPage(pageRun = new JPanel(), widgetsRun);
		createTabPage(pageInp = new JPanel(), widgetsInp);
		createTabPage(pagePar = new JPanel(), widgetsPar);
		createTabPage(pageBg = new JPanel(), widgetsBg);

		// Create tabs
		controlTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		controlTabs.addTab("RUN", pageRun);
		controlTabs.addTab("INP", pageInp);
		controlTabs.addTab("PAR", pagePar);
		controlTabs.addTab("BG",  pageBg);

		add(controlTabs, BorderLayout.CENTER);
	}

	private void createTabPage(JPanel parent, WidgetInfo[] widgets) {

		GroupLayout layout = new GroupLayout(parent);
		parent.setLayout(layout);

		// gaps between components and
		// gaps between container and components touching the edge of it
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		// Create one sequential group for horizontal axis, and one for vertical
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		// The sequential group in turn contains two parallel groups.
		// One parallel group contains the labels, the other the text fields.
		// Putting the labels in a parallel group along the horizontal axis
		// positions them at the same x location.
		// Variable indentation is used to reinforce the level of grouping.
		//
		// The sequential group contains two parallel groups that align
		// the contents along the baseline. The first parallel group contains
		// the first label and text field, and the second parallel group
		// contains
		// the second label and text field. By using a sequential group
		// the labels and text fields are positioned vertically after one
		// another.

		GroupLayout.ParallelGroup h1 = layout.createParallelGroup();
		GroupLayout.ParallelGroup h2 = layout.createParallelGroup();

		// create the control widgets
	
		ArrayList<JComponent> jclist = new ArrayList<JComponent>();
		JComponent jc;
		for (int i = 0; i < widgets.length; i++) {
			switch (widgets[i].type) {
				case BUTTON:
					jc = new JButton(widgets[i].name);
					jc.setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					jc.setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					jclist.add(jc);
					break;
				case LABEL:
					jc = new JLabel(widgets[i].name);
					jc.setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					jc.setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					jclist.add(jc);
					// fall through. label always followed by its text field
				case TFIELD:
					jc = new JTextField();
					jc.setName(widgets[i].name);
					((JTextField) jc).addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// TODO
						}
					});
					((JTextField) jc).setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					((JTextField) jc).setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
					((JTextField) jc).setColumns(10);
					
					// keep track of text field for auto edit internally
					jclist.add(jc);
					parTable.put(widgets[i].name, jc);
					break;
				default:
					break;
			}
		}
		
			
		// group every 2 control widgets
		for (int i = 1; i < jclist.size(); i += 2) {
		
			h1 = h1.addComponent(jclist.get(i-1));
			h2 = h2.addComponent(jclist.get(i));
			vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
			      .addComponent(jclist.get(i-1)).addComponent(jclist.get(i)));
		}
		
		hGroup.addGroup(h1);
		hGroup.addGroup(h2);
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}

}
