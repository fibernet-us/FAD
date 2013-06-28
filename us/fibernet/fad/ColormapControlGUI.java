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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.util.*;
import javax.swing.JPopupMenu;

public class ColormapControlGUI {

    private JFrame frame;
    private BufferedImage rainbow;
    private BufferedImage rainbowCache;
    private final PatternGUI patternGUI;
    private int[][] imageArray; // input converted to color indexes
    private int[][] rawImageArray;
    private int minOriginalColor;
    private int maxOriginalColor;
    private BufferedImage histogram;
    private BufferedImage histogramCache;
    private List<Point2D> pts;
    private Point2D selectedP;
    private final Point2D lastClicked = new Point(0, 0);
    // components
    private JLabel rainbowLabel;
    private JLabel histoLabel;
    private JLabel minLabel;
    private JLabel maxLabel;
    private JTextField minArea;
    private JTextField maxArea;

    private int minThreshold;
    private int maxThreshold;
    private int thresholdSelected;
    private double histoFactor;
    private double rainbowHeightFactor = 255.0 / 143.0;

    private double[] curveParam;

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

    /**
     * Create the application.
     */
    public ColormapControlGUI(int[][] imageArray, int[][] originalArray,
            PatternGUI p) {
        this.imageArray = imageArray;
        this.rawImageArray = originalArray;
        this.patternGUI = p;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        // initialize components of this frame
        frame = new JFrame();
        frame.setBounds(200, 200, 400, 500);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setResizable(false);
        frame.setTitle("Color Map Control");

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setBounds(100, 100, 450, 20);
        panel.setLayout(new BorderLayout(0, 0));

        JComboBox comboBox = new JComboBox(new String[] { "Rainbow", "BGR",
                "RGB", "Black_White", "White_Black", "Keiichi" });
        panel.add(comboBox, BorderLayout.CENTER);
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                changeColorTable((String) cb.getSelectedItem());
            }
        });

        JLabel lblNewLabel = new JLabel("Colormap");
        lblNewLabel.setLabelFor(comboBox);
        panel.add(lblNewLabel, BorderLayout.WEST);

        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
        panel_1.setBounds(100, 100, 450, 260);

        JButton btnNewButton_3 = new JButton("Reset");
        panel_1.add(btnNewButton_3);
        btnNewButton_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        JButton btnNewButton_2 = new JButton("Close");
        panel_1.add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel_2 = new JPanel();
        frame.getContentPane().add(panel_2, BorderLayout.CENTER);
        panel_2.setBounds(100, 100, 450, 20);
        panel_2.setLayout(null);

        // rainbow icon
        this.rainbowLabel = new JLabel();
        rainbowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rainbowLabel.setBounds(10, 11, 374, 143);
        panel_2.add(rainbowLabel);
        rainbowLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        // popupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBounds(0, 0, 200, 50);
        panel_2.add(popupMenu);
        JMenuItem addBox = new JMenuItem("Add Box");
        addBox.setActionCommand("add");

        popupMenu.add(addBox);

        JMenuItem removeBox = new JMenuItem("Remove Box");
        removeBox.setActionCommand("remove");

        popupMenu.add(removeBox);

        addPopup(rainbowLabel, popupMenu);
        addBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBox();
            }

        });
        removeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBox();
            }
        });

        // min and max label under color table
        this.minLabel = new JLabel();
        minLabel.setBounds(10, 165, 46, 14);
        panel_2.add(minLabel);

        this.maxLabel = new JLabel();
        maxLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        maxLabel.setBounds(338, 165, 46, 14);
        panel_2.add(maxLabel);

        // histogram label
        JLabel lblHistogram = new JLabel("Histogram");
        lblHistogram.setHorizontalAlignment(SwingConstants.CENTER);
        lblHistogram.setBounds(149, 180, 87, 22);
        panel_2.add(lblHistogram);

        // histogram icon
        this.histoLabel = new JLabel("New label");
        histoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        histoLabel.setVerticalAlignment(SwingConstants.CENTER);
        histoLabel.setBounds(10, 218, 374, 143);
        panel_2.add(histoLabel);
        histoLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        addHistoListener(this.histoLabel);
        addRainbowListener(this.rainbowLabel);

        // min and max text area under histogram
        this.minArea = new JTextField();
        minArea.setBounds(10, 372, 40, 20);
        panel_2.add(minArea);
        minArea.setColumns(10);

        this.maxArea = new JTextField();
        maxArea.setBounds(344, 372, 40, 20);
        panel_2.add(maxArea);
        maxArea.setColumns(10);

        // ====================done with components========================

        BufferedImage rainbow = ImageUtil.getRainbowImage();
        this.rainbow = ImageUtil.fitImage(rainbowLabel, rainbow);
        this.rainbowCache = ImageUtil.copyImage(this.rainbow);
        initializeCurve();
        generateRainbowCurve();
        generateBox(); // generate boxes on resized rainbow with curve

        // generate histogram of color usage
        this.histogram = generateHistogram();
        this.histogram = ImageUtil.fitImage(histoLabel, this.histogram);
        this.histogramCache = ImageUtil.copyImage(this.histogram);
        initializeThresholds();
        // generateThreLines();
        // generateThresholdReadings();
        // addTextListener(minArea,"min");
        // addTextListener(maxArea,"max");
    }

    // initialize curve points (straight line with 3 boxes)
    private void initializeCurve() {
        int rainbowWidth = this.rainbow.getWidth();
        int rainbowHeight = this.rainbow.getHeight();
        // pts.clear();
        // generate initial points which forms a straight line
        Point2D p1 = new Point(0, rainbowHeight);
        Point2D p2 = new Point(rainbowWidth / 2, rainbowHeight - rainbowHeight
                / 2);
        Point2D p3 = new Point(rainbowWidth, 0);
        pts = new ArrayList<Point2D>();
        pts.add(p1);
        pts.add(p2);
        pts.add(p3);
        updateCurveParams();
    }

    // based on the points, calculate the parameters for the curve.
    private void updateCurveParams() {
        int n = pts.size();
        double[][] xmatrix = new double[n][n];
        double[] ymatrix = new double[n];
        for (int w = 0; w < n; w++) {
            for (int h = 0; h < n; h++) {
                xmatrix[w][h] = Math.pow(pts.get(w).getX(), n - h - 1);
            }
        }
        for (int y = 0; y < n; y++)
            ymatrix[y] = pts.get(y).getY();
        this.curveParam = ImageUtil.gaussian(xmatrix, ymatrix);
    }

    // set the min and max thresholds
    private void initializeThresholds() {
        this.minThreshold = 0;
        this.maxThreshold = this.histoLabel.getWidth();
        generateThreLines();
        generateThresholdReadings();
    }

    // generate the threshold readings below color table and histogram
    private void generateThresholdReadings() {
        this.minLabel.setText(""
                + (int) (this.minOriginalColor + this.minThreshold
                        * this.histoFactor));
        this.maxLabel.setText(""
                + (int) (this.minOriginalColor + this.maxThreshold
                        * this.histoFactor));
        this.minArea.setText("" + minLabel.getText());
        this.maxArea.setText("" + maxLabel.getText());
    }

    // the curve function in the color table
    private double curveFunction(double x) {
        double result = 0;
        for (int i = 0; i < curveParam.length; i++) {
            result = result * x + curveParam[i];
        }
        // set upper and lower bounds
        if (result > this.rainbowLabel.getHeight())
            result = this.rainbowLabel.getHeight();
        else if (result < 0)
            result = 0;
        return result;
    }

    // ==========================================================================================
    // generate color usage curve
    private void generateRainbowCurve() {

        Graphics2D g2 = this.rainbow.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        for (int i = 1; i < 374; i++) {
            // System.out.println("line: ["+(i-1)+", "+curveFunction(i-1)+"]");
            g2.draw(new Line2D.Double(i - 1, curveFunction(i - 1), i,
                    curveFunction(i)));
        }
        g2.dispose();
    }

    // draw boxes centered at the points on the color table
    private void generateBox() {
        double boxEdgeLength = 10.0;
        // x and y defines the position of left-upper corner of the box
        Graphics2D g2 = this.rainbow.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        Point2D[] points = new Point2D[this.pts.size()];
        pts.toArray(points);
        for (Point2D p : points) {
            // System.out.println(p);

            double x = p.getX() - boxEdgeLength / 2;
            double y = p.getY() - boxEdgeLength / 2;

            Rectangle2D rec = new Rectangle2D.Double(x, y, boxEdgeLength,
                    boxEdgeLength);
            g2.draw(rec);
        }
        g2.dispose();
    }

    private void generateNewRainbow() {
        this.rainbow = ImageUtil.fitImage(rainbowLabel,
                ImageUtil.getRainbowImage());
        this.rainbowCache = ImageUtil.copyImage(this.rainbow);
        refreshRainbow();
    }

    // re-draw the boxes and the curve on the original color table.
    private void refreshRainbow() {
        refreshPoints();
        // this.minThreshold = 0;
        // this.maxThreshold = this.histoLabel.getWidth();
        this.rainbow = ImageUtil.copyImage(this.rainbowCache);
        this.rainbow = ImageUtil.fitImage(this.rainbowLabel, this.rainbow);
        generateRainbowCurve();
        generateBox();
        generatePatternImage();//
    }

    // sort the order of boxes based on their x position
    private void refreshPoints() {
        Collections.sort(pts, new Comparator<Point2D>() {
            public int compare(Point2D p1, Point2D p2) {
                return (int) p1.getX() - (int) p2.getX();
            }
        });
        updateCurveParams();
    }

    // set the color table and histogram to initial state
    private void reset() {
        initializeCurve();
        refreshRainbow();
        initializeThresholds();
        refreshHistogram();
        generatePatternImage();// redraw the original image.
    }

    // ===============================================================================================
    // Generate the histogram of color usage
    private BufferedImage generateHistogram() {
        TreeMap<Integer, Integer> colors = new TreeMap<Integer, Integer>();
        int max = -1; // height
        int minThre = Integer.MAX_VALUE;
        int maxThre = Integer.MIN_VALUE;
        for (int i = 0; i < imageArray.length; i++)
            for (int j = 0; j < imageArray[0].length; j++) {
                if (this.rawImageArray[i][j] < minThre
                        && this.rawImageArray[i][j] > 0)
                    minThre = this.rawImageArray[i][j];
                if (rawImageArray[i][j] > maxThre)
                    maxThre = this.rawImageArray[i][j];

                if (colors.containsKey(imageArray[i][j])) {
                    colors.put(imageArray[i][j],
                            colors.get(imageArray[i][j]) + 1);
                    if (colors.get(imageArray[i][j]) > max
                            && imageArray[i][j] != 0)
                        max = colors.get(imageArray[i][j]);
                } else
                    colors.put(imageArray[i][j], 1);
            }
        this.minOriginalColor = minThre;
        this.maxOriginalColor = maxThre;
        this.histoFactor = (double) (maxOriginalColor - minOriginalColor) / 374.0;
        /*
         * System.out.println("Histogram: minColor "+minOriginalColor
         * +" maxColor "+maxOriginalColor+" factor "+ histoFactor);
         */
        int w = 2560;
        int h = 1000;
        // System.out.println("max height is "+max);
        double factor = max / 1000 + 1;
        // System.out.println("factor is "+factor);

        BufferedImage histogram = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = histogram.getRaster();

        int[] r = null;
        int[] g = null;
        int[] b = null;
        // paint background white
        r = new int[w * h];
        g = new int[w * h];
        b = new int[w * h];
        Arrays.fill(r, 211);
        Arrays.fill(g, 211);
        Arrays.fill(b, 211);
        raster.setSamples(0, 0, w, h, 0, r);
        raster.setSamples(0, 0, w, h, 1, g);
        raster.setSamples(0, 0, w, h, 2, b);

        for (int i = 10; i < 2560; i += 10) {
            if (colors.containsKey(i / 10)) {
                int height = (int) (colors.get(i / 10) / factor);// ---------------------
                // System.out.println(i/10+" height is "+height);
                int gap = h - height;
                // System.out.println(i+" height "+height);
                r = new int[10 * height];
                g = new int[10 * height];
                b = new int[10 * height];
                Arrays.fill(r, 255);
                Arrays.fill(g, 255);
                Arrays.fill(b, 255);
                raster.setSamples(i, gap, 10, height, 0, r);
                raster.setSamples(i, gap, 10, height, 1, g);
                raster.setSamples(i, gap, 10, height, 2, b);
            }
        }
        return histogram;
    }

    // draw the threshold lines on the histogram
    private void generateThreLines() {
        Graphics2D g = this.histogram.createGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < 2; i++) {
            g.draw(new Line2D.Double(this.minThreshold + i, 0,
                    this.minThreshold + i, this.histogram.getHeight()));
            g.draw(new Line2D.Double(this.maxThreshold - 3 - i, 0,
                    this.maxThreshold - 3 - i, this.histogram.getHeight()));
        }
        g.dispose();
    }

    // ==========================mouse listener for
    // rainbow========================
    // listener of the color table. handles box move
    private void addRainbowListener(Component component) {
        component.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                selectBox(e.getX(), e.getY());
            }

            public void mouseReleased(MouseEvent e) {
                unselectBox();
            }
        });

        component.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                boxMove("dragged", e);
            }
        });
    }

    // select a box
    private void selectBox(int x, int y) {
        for (Point2D p : pts) {
            if (isSelected(x, y, p)) {
                this.selectedP = p;
                return;
            }
        }
    }

    private void unselectBox() {
        this.selectedP = null;
    }

    // check which box is selected based on the position of the mouse click.
    private boolean isSelected(int x, int y, Point2D p) {
        double maxDis = 10.0; // click within this radius selects the box
        double px = p.getX();
        double py = p.getY();
        double dis = Math.sqrt(Math.pow(px - x, 2) + Math.pow(py - y, 2));
        return dis <= maxDis;
    }

    // drag box to a position on the color table.
    private void boxMove(String eventName, MouseEvent e) {
        if (eventName.equals("dragged")) {
            if (this.selectedP != null) {
                int x = e.getX();
                int y = e.getY();
                // make sure that x and y does not go outside the label
                if (x < 0)
                    x = 0;
                if (x > 374)
                    x = 374;
                if (y < 0)
                    y = 0;
                if (y > 143)
                    y = 143;
                this.selectedP.setLocation(x, y);
                refreshRainbow();
            }
        }
    }

    private void addBox() {
        double x = lastClicked.getX();
        double y = lastClicked.getY();
        pts.add(new Point((int) x, (int) y));
        refreshPoints();
        refreshRainbow();
    }

    private void removeBox() {
        selectBox((int) lastClicked.getX(), (int) lastClicked.getY());
        pts.remove(selectedP);
        refreshRainbow();
    }

    // ============================================================================
    // pop up menu for the rainbow
    private void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
                lastClicked.setLocation(e.getX(), e.getY());
            }
        });
    }

    // ===================mouse listener for the
    // histogram========================
    private void addHistoListener(Component component) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                thresholdSelected(e.getX());
            }

            public void mouseReleased(MouseEvent e) {
                thresholdUnselect();
            }
        });
        component.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                thresholdMove(e.getX());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    // check which threshold is selected. Similar to boxSelected
    private void thresholdSelected(int x) {
        if (Math.abs(this.minThreshold - x) <= 10) {
            this.thresholdSelected = 0;
        } else if (Math.abs(this.maxThreshold - x) <= 10) {
            this.thresholdSelected = 1;
        } else {
            this.thresholdSelected = -1;
        }

    }

    private void thresholdUnselect() {
        this.thresholdSelected = -1;
    }

    // redraw the threshold lines on histogram if it is moved
    private void thresholdMove(int x) {
        // this.histogram = fitImage(this.histoLabel,this.histogramCache);
        if (thresholdSelected == 0) {
            this.minThreshold = x;
        } else if (thresholdSelected == 1) {
            this.maxThreshold = x;
        }
        if (x > this.histogram.getWidth())
            this.maxThreshold = this.histogram.getWidth();
        if (x < 0) {
            this.minThreshold = 0;
        }
        refreshHistogram();
    }

    // update threshold readings, draw new threshold lines on histogram
    private void refreshHistogram() {
        this.histogram = ImageUtil.copyImage(this.histogramCache);
        generateThreLines();
        generateThresholdReadings();
        this.histogram = ImageUtil.fitImage(this.histoLabel, this.histogram);
        generatePatternImage();
    }

    // ==============================patterGUI
    // interaction=======================
    // based on the rawImageArray, consider the thresholds and curve function,
    // generate
    // new image array and make pattern GUI to update image based on this image.
    void generatePatternImage() {
        int[][] imageArray = this.rawImageArray;
        int[][] output = new int[imageArray.length][imageArray[0].length];
        int min = Integer.valueOf(this.minArea.getText());
        int max = Integer.valueOf(this.maxArea.getText());
        double factor = (double) (max - min) / 374.0;
        // System.out.println("min: "+min+" max: "+max);
        for (int w = 0; w < imageArray.length; w++) {
            for (int h = 0; h < imageArray[0].length; h++) {
                if (imageArray[w][h] <= min) {
                    output[w][h] = 0; // first color
                } else if (imageArray[w][h] >= max) {
                    output[w][h] = 255; // last color
                } else {
                    output[w][h] = (int) ((this.rainbowLabel.getHeight() - (int) curveFunction((imageArray[w][h] - min)
                            / factor)) * this.rainbowHeightFactor);
                }
            }
        }
        this.patternGUI.generateImage(output); // generate new image based on
                                                // the color control
        this.patternGUI.refreshLabel(); // add 4/17 redraw lines and arc, and
                                        // fit the image to label
    }

    private void changeColorTable(String colorTableName) {
        ImageUtil.useColorTable(colorTableName);
        generateNewRainbow();
        generatePatternImage();
    }

}
