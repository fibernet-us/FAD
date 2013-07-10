/*
 * Copyright Yi Xiao. All rights reserved.
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

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Display pattern as an BufferedImage as a JLabel
 *
 */
public class PatternGUI {

    private JFrame frame;
    private BufferedImage image;
    private BufferedImage imageCache;
    // private BufferedImage originalImage;
    private JLabel imageLabel;
    private int[][] originalInputArray;// double[][] to int[][], not skipped
    private int[][] originalImageArray; // original array for images, input
                                        // pixels may get skipped
    private int[][] imageArray; // converted to color indexes
    private ColormapControlGUI control;
    private double radius;
    private boolean linesEnabled;

    public void render() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PatternGUI(double[][] imageArray, String fileName) {
        this(imageArray, fileName, 200);// default radius = 200
    }

    public PatternGUI(double[][] imageArray, String fileName, double radius) {
        this.radius = radius;
        this.originalInputArray = ImageUtil.doubleToInt(imageArray);
        this.originalImageArray = ImageUtil
                .shrinkArray(this.originalInputArray);
        this.imageArray = ImageUtil.convertImage(this.originalImageArray, 256);
        initialize(fileName);
    }

    public PatternGUI(int[][] imageArray, String fileName) {
        this(imageArray, fileName, 200);
    }

    public PatternGUI(int[][] imageArray, String fileName, double radius) {
        System.out.println("Image :" + imageArray.length + " "
                + imageArray[0].length);
        this.radius = radius;
        this.originalInputArray = imageArray;
        this.originalImageArray = ImageUtil
                .shrinkArray(this.originalInputArray);
        this.imageArray = ImageUtil.convertImage(this.originalImageArray, 256);
        initialize(fileName);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(String fileName) {
        frame = new JFrame();
        frame.setTitle(fileName);
        frame.setBounds(20, 20, 700, 700);
        // frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        createColormapControlGUI();

        this.imageLabel = new JLabel();
        this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        this.control.generatePatternImage();// create initial image
        // this.imageCache = ImageUtil.copyImage(this.image);//initialize image
        // cache
        // this.originalImage = ImageUtil.copyImage(this.image); //orignal copy
        // of the image

        panel.add(this.imageLabel);

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton("Colormap");
        btnNewButton.setPreferredSize(new Dimension(100, 20));
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openColorControl();
            }
        });

        JButton btnNewButton_1 = new JButton("Resize");
        btnNewButton_1.setPreferredSize(new Dimension(100, 20));
        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane
                        .showMessageDialog(frame,
                                "Resize the image by dragging and resizing the image window");
            }

        });

        JButton btnNewButton_2 = new JButton("Close");
        btnNewButton_2.setPreferredSize(new Dimension(100, 20));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel_1.add(btnNewButton);
        panel_1.add(btnNewButton_1);
        panel_1.add(btnNewButton_2);

        // listener for resizing the window
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                resizeLabel();
            };
        });
    }

    // copy the imageCache and assign it to image, where there's no lines or
    // arcs
    private void resizeLabel() {
        this.image = ImageUtil.copyImage(this.imageCache);
        refreshLabel();
    }

    // refresh the label according to the size of the frame, draw lines and arc,
    // and fit the image too.
    void refreshLabel() {
        this.imageLabel.setBounds(10, 11, frame.getWidth() - 37,
                frame.getHeight() - 80);
        drawlines(); // added 3/13/2013
        drawArc(this.radius);
        ImageUtil.fitImage(this.imageLabel, this.image);
    }

    // open the color map control panel
    private void createColormapControlGUI() {
        this.control = new ColormapControlGUI(this.imageArray,
                this.originalImageArray, this);
    }

    /**
     * To generate new image based one the originalArray. Used by
     * colorControlGUI
     */
    void generateImage(int[][] imageArray) {
        // this.imageCache =
        // this.image==null?null:ImageUtil.copyImage(this.image);
        BufferedImage image = null;
        try {
            image = ImageUtil.getImage(imageArray);
        } catch (IOException e) {
            System.out.println("Could not generate image.");
        }
        this.image = image;
        this.imageCache = ImageUtil.copyImage(this.image);
        // refreshLabel(); deleted 4/17
    }

    private void openColorControl() {
        this.control.render();
    }

    /**
     * Draw the eight lines on the pattern GUI
     */
    private void drawlines() {
        // System.out.println("draw lines called");
        Graphics2D g2 = this.image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        for (int i = 0; i < 8; i++) {
            double k = Math.tan(Math.PI / 18 * (i + 1));
            g2.draw(new Line2D.Double(new Point2D.Double(0, this.image
                    .getHeight()), locatePoint(k)));
            // g2.draw(new
            // Line2D.Double(0,this.image.getHeight(),this.image.getWidth(),0));
        }
        g2.dispose();
    }

    /**
     * Return the end point of the linear function y=k*x
     * 
     * @param k
     *            the coefficient of the linear function
     * @return the end point
     */
    private Point2D locatePoint(double k) {
        // System.out.println("k="+k);
        int width = this.image.getWidth();
        int height = this.image.getHeight();
        // System.out.println("w:"+width+" h:"+height);
        double x = width;
        double y = k * x;
        if (y > height) {
            y = height;
            x = y / k;
        }
        // System.out.println("["+x+", "+y+"]");
        return new Point2D.Double(x, height - y);
    }

    private void drawArc(double radius) {
        int width = this.image.getWidth();
        int height = this.image.getHeight();
        if (radius > width)
            radius = width;
        if (radius > height)
            radius = height;
        Graphics2D g = this.image.createGraphics();
        g.setColor(Color.WHITE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g.draw(new Ellipse2D.Double(-radius, this.image.getHeight() - radius,
                2 * radius, 2 * radius));
        g.dispose();
    }

    public void setRadius(double newRadius) {
        this.radius = newRadius;
    }

    public void updateImage() {
        resizeLabel();
    }

    public void enableLines(boolean enabled) {

    }
}
