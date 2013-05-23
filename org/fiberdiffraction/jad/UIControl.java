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
import javax.swing.JOptionPane;
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

    private Map<String, DataControl.DatumDef> parTable;   

    /**
     * set up dimension attribute and create control UI
     */
    public UIControl(JFrame parent, int width, int height) {
        this.parentFrame = parent;
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height)); 
        setMinimumSize(new Dimension(width, height)); 
        parTable = new HashMap<String, DataControl.DatumDef>();
        initialize();
    }

    //Initialize the contents of the control panels.
    private void initialize() {

        setLayout(new BorderLayout());

        // Create tab pages
        createTabPage(pageRun = new JPanel(), DataControl.DataRun);
        createTabPage(pageInp = new JPanel(), DataControl.DataInp);
        createTabPage(pagePar = new JPanel(), DataControl.DataPar);
        createTabPage(pageBg  = new JPanel(), DataControl.DataBg);

        // Create tabs
        controlTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        controlTabs.addTab("RUN", pageRun);
        controlTabs.addTab("INP", pageInp);
        controlTabs.addTab("PAR", pagePar);
        controlTabs.addTab("BG",  pageBg);

        add(controlTabs, BorderLayout.CENTER);
    }

    private void createTabPage(JPanel parent, DataControl.DatumDef[] widgets) {

        //
        // first create the widgets and put them into a list
        //

        ArrayList<JComponent> jclist = new ArrayList<JComponent>();
        JComponent jc;

        for (int i = 0; i < widgets.length; i++) {

            switch (widgets[i].getType()) {
                case BUTTON:
                    jc = new JButton(widgets[i].getName());
                    jc.setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jc.setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jclist.add(jc);
                    break;
                    //
                case LABELED_TFIELD:
                    jc = new JLabel(widgets[i].getName());
                    jc.setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jc.setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jclist.add(jc);
                    // fall through. label always followed by its text field
                    //
                case TFIELD:
                    jc = new JTextField();
                    jc.setName(widgets[i].getName());                
                    jc.setMinimumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jc.setMaximumSize(new Dimension(WIDGET_WIDTH, WIDGET_HEIGHT));
                    jclist.add(jc);    

                    ((JTextField) jc).setColumns(10);
                    ((JTextField) jc).setText(widgets[i].getStringValue());
                    ((JTextField) jc).addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JTextField j = (JTextField)e.getSource();
                            DataControl.DatumDef datum = parTable.get(j.getName());
                            if(datum != null) {
                                String err = datum.setValue(j.getText());
                                if(err != null) {
                                    JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    });

                    widgets[i].setGui(jc);
                    parTable.put(widgets[i].getName(), widgets[i]);    
                    break;
                    //
                default:
                    break;
            }
        }

        //
        // then group the widgets into 2 columns
        //

        GroupLayout layout = new GroupLayout(parent);
        parent.setLayout(layout);

        // set gaps between components
        layout.setAutoCreateGaps(true);
        // set gaps between container and components touching the edge of it
        layout.setAutoCreateContainerGaps(true);

        // Create one sequential group for horizontal axis, and one for vertical
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        // hGroup contains two parallel groups, one for labels and one for text fields
        GroupLayout.ParallelGroup h1 = layout.createParallelGroup();
        GroupLayout.ParallelGroup h2 = layout.createParallelGroup();

        // group widgets into 2 columns
        for (int i = 1; i < jclist.size(); i += 2) {

            h1 = h1.addComponent(jclist.get(i-1));
            h2 = h2.addComponent(jclist.get(i));

            // add a parallel group that aligns a label and a text field along the baseline
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                  .addComponent(jclist.get(i-1)).addComponent(jclist.get(i)));
        }

        hGroup.addGroup(h1);
        hGroup.addGroup(h2);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
    }

}
