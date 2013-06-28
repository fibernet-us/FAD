/*
 * Copyright Billy Zheng. All rights reserved.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

/**
 * A JPanel containing a text field for displaying message and information
 */
@SuppressWarnings("serial")
public final class UIPloting extends JPanel {

    private JFrame parentFrame;
    private Point2D[] rawData; // size is multiple of 90. to be drawn as points
    private Point2D[] fitData; // size unknown. to be drawn as curves
    private Point2D[] rawBars; // vertical bars at X-axis, related to rawData
    private Point2D[] fitBars; // vertical bars at X-axis, related to fitData 
    private double background; // Y value for a straight line parallel to X-axis

    public UIPloting(JFrame parent, int width, int height) {
        this.parentFrame = parent;
        setPreferredSize(new Dimension(width, height)); 
        setBackground(Color.BLACK);
        initialize();
    }
    
    private void initialize() {
        createSampleData();      
    }
    
    public void setRawData(Point2D[] points)  { 
        rawData = points; 
    }

    public void setFitData(Point2D[] points)  { 
        fitData = points; 
    }
    
    public void setRawBars(Point2D[] points)  { 
        rawBars = points; 
    }
    
    public void setFitBars(Point2D[] points)  { 
        fitBars = points; 
    }
    
    public void setBackground(double bkgd)  { 
        background = bkgd; 
    }

    
    // TODO: make object size/color etc configurable
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        Color rawDataColor = Color.white;
        Color fitDataColor = Color.red;
        Color bgColor = Color.blue;
        Color rawBarColor = Color.yellow;
        Color fitBarColor = Color.green;
        
        int offset = 20;   // padding between plot and panel edges 
        int pointSize = 4;  // diameter of point/dot 
        
        int w = getWidth();
        int h = getHeight();
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw axes. use (offset, h - offset) as origin with y axis pointing up
        g2.draw(new Line2D.Double(offset, h - offset, offset, offset));
        g2.draw(new Line2D.Double(offset, h - offset, w - offset, h - offset));
        
        // TODO: draw tick marks on X-axis

        // calculate scales
        double xScale = (w - 2.0 * offset) / getMax()[0];
        double yScale = (h - 2.0 * offset) / getMax()[1];
        
        // draw raw data points as dots
        // background value is added to Y
        g2.setPaint(rawDataColor);
        for(int i = 0; i < rawData.length; i++)  {
            double x =     offset + rawData[i].getX() * xScale;
            double y = h - offset - (rawData[i].getY() + background) * yScale;
            g2.fill(new Ellipse2D.Double(x, y, pointSize, pointSize));
        }
        
        // draw fitted data by drawing a line between adjacent points
        // background value is added to Y
        g2.setPaint(fitDataColor);
        for(int i = 0; i < fitData.length - 1; i++)  {
            double x1 =     offset + fitData[i].getX() * xScale;
            double y1 = h - offset - (fitData[i].getY() + background) * yScale;  
            double x2 =     offset + fitData[i+1].getX() * xScale;
            double y2 = h - offset - (fitData[i+1].getY()+ background) * yScale;
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        
        // draw background
        g2.setPaint(bgColor);
        int bgscaled = h - offset - (int)(background * yScale);
        g2.draw(new Line2D.Double(offset, bgscaled, w - offset, bgscaled));
        
        // TODO: draw bars
        // rawbar: downwards from background line
        // fitbar: upwards from background line
        g2.setPaint(rawBarColor);
        for(int i = 0; i < rawBars.length; i++) {
            int rx = (int)(rawBars[i].getX() * xScale) + offset;
            int ry = bgscaled + (int)(rawBars[i].getY());
            g2.draw(new Line2D.Double(rx, bgscaled, rx, ry));
        }
        g2.setPaint(fitBarColor);
        for(int i = 0; i < fitBars.length; i++) {
            int fx = (int)(fitBars[i].getX() * xScale) + offset;
            int fy = bgscaled - (int)(fitBars[i].getY());
            g2.draw(new Line2D.Double(fx, bgscaled, fx, fy));
        }       
    }

    // find out the max X and Y values of rawData and fitData
    private double[] getMax() {
        
        double[] max = new double[2];
        max[0] = -Double.MAX_VALUE;
        max[1] = -Double.MAX_VALUE;

        for(int i = 0; i < rawData.length; i++) {
            if(rawData[i].getX() > max[0]) {
                max[0] = rawData[i].getX();  
            }
            if(rawData[i].getY() > max[1]) {
                max[1] = rawData[i].getY();
            }
        }
 
        for(int i = 0; i < fitData.length; i++) {
            if(fitData[i].getX() > max[0]) {
                max[0] = fitData[i].getX();
            }         
            if(fitData[i].getY() > max[1]) {
                max[1] = fitData[i].getY();
            }
        }
        
        return max;
    }
    
    // create some data for testing or startup
    private void createSampleData() {
        
        Random random = new Random();
        background = 5.0;
        
        int amp = 10;
        int nRawPoints = 90;
        rawData = new Point2D[nRawPoints];
        for(int i = 0; i < nRawPoints; i++) {
            rawData[i] = new Point2D.Double(i, amp * Math.cos(i/Math.PI) + background + random.nextInt(2));
        }
        
        int nFitPoints = nRawPoints * 10;
        fitData = new Point2D[nFitPoints];
        for(int i = 0; i < nFitPoints; i++) {
            fitData[i] = new Point2D.Double((i+0.0)/10, amp * Math.cos(i/Math.PI/10) + background);
        }      
        
        int nBars = 10;
        rawBars = new Point2D[nBars];
        fitBars = new Point2D[nBars];
        for(int i = 0; i < nBars; i++) {
            rawBars[i] = new Point2D.Double(i*10, 10);
            fitBars[i] = new Point2D.Double(i*10 + (random.nextInt(5) - 3), (random.nextInt(10) + 20));
        }    
    }
   

}