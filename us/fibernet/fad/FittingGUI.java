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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class FittingGUI {

    private JFrame frame;
    private JTextField ploynomialField;
    private JLabel graphLabel;
    private JPanel graphPanel;
    private Point2D[] points;
    private double maxY;
    private double maxX;
    private double minX;
    private double minY;
    private int numDigitX;
    private int numDigitY;
    private int originX = 80;
    private int originY = 50;
    private BufferedImage currentImage;
    private BufferedImage plotCache;
    private int imageWidth;
    private int imageHeight;

    /**
     * Create the application.
     */
    public FittingGUI(Point2D[] points) {
        this.points = points.clone();
        processPoints();
        initialize();
    }

    public FittingGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 600);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Data Fitting GUI");

        graphPanel = new JPanel();
        graphPanel
                .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        int fw = frame.getWidth();
        int fh = frame.getHeight() - 80;
        graphPanel.setSize(fw, fh);
        frame.getContentPane().add(graphPanel, BorderLayout.CENTER);

        graphLabel = new JLabel("");
        int pw = graphPanel.getWidth();
        int ph = graphPanel.getHeight();
        graphLabel.setSize(pw, ph);
        graphPanel.add(graphLabel);

        JPanel optionPanel = new JPanel();
        optionPanel
                .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        optionPanel.setSize(600, 50);
        frame.getContentPane().add(optionPanel, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("X Data Type: ");
        optionPanel.add(lblNewLabel);

        JRadioButton rdbtnD = new JRadioButton("D");
        rdbtnD.setSelected(true);
        optionPanel.add(rdbtnD);

        JRadioButton rdbtnXRAD = new JRadioButton("XRAD");
        optionPanel.add(rdbtnXRAD);

        JRadioButton rdbtnTheta = new JRadioButton("Theta");
        optionPanel.add(rdbtnTheta);

        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnD);
        group.add(rdbtnXRAD);
        group.add(rdbtnTheta);

        JLabel lblNewLabel_1 = new JLabel("            Polynomial Degree");
        optionPanel.add(lblNewLabel_1);

        ploynomialField = new JTextField();
        ploynomialField.setText("5");
        optionPanel.add(ploynomialField);
        ploynomialField.setColumns(4);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(600, 50);
        buttonPanel
                .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnPlot = new JButton("Plot");
        btnPlot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plot();
            }
        });

        JButton btnData = new JButton("Data");

        JButton btnFit = new JButton("Fit");
        btnFit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fit();
            }
        });

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    saveCurrentImage(fc.getSelectedFile());
                }
            }
        });

        JButton btnApply = new JButton("Apply");

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        JButton btnHelp = new JButton("Help");

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(btnPlot);
        buttonPanel.add(btnData);
        buttonPanel.add(btnFit);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnApply);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnHelp);
        buttonPanel.add(btnClose);

        // plot();
    }

    private void plot() {
        int w = graphPanel.getWidth();
        int h = graphPanel.getHeight();
        BufferedImage plotImage = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        paintBackground(plotImage, 244);
        // generate axis x and y
        imageWidth = w - 160;
        imageHeight = h - 120;
        Graphics2D g2 = plotImage.createGraphics();
        Rectangle2D axis = new Rectangle2D.Double(originX, originY, imageWidth,
                imageHeight);
        g2.setColor(Color.BLACK);
        g2.draw(axis);
        // draw markers
        int numXMarker = 6;
        int numYMarker = 6;
        Map<Double, Point2D> xMarkers = new TreeMap<Double, Point2D>();
        Map<Double, Point2D> yMarkers = new TreeMap<Double, Point2D>();
        for (int i = 0; i < 6; i++) {
            xMarkers.put(minX + (maxX - minX) / 5 * i,
                    new Point.Double(originX + i * imageWidth / (numXMarker - 1), originY + imageHeight));
            yMarkers.put(minY + (maxY - minY) / 5 * (5 - i),
                    new Point.Double(originX, originY + i * imageHeight / (numYMarker - 1)));
        }
        drawMarker(g2, xMarkers, yMarkers);
        drawCurve(g2, Color.BLACK, true, originX, originY, imageWidth,
                imageHeight, points);// data

        graphLabel.setIcon(new ImageIcon(plotImage));
        currentImage = plotImage;
        plotCache = ImageUtil.copyImage(plotImage);
    }

    private void drawMarker(Graphics2D g2, Map<Double, Point2D> xMarkers,
            Map<Double, Point2D> yMarkers) {
        int markerLength = 6;
        // x-axis
        for (Double xNumber : xMarkers.keySet()) {
            Point2D center = xMarkers.get(xNumber);
            g2.draw(new Line2D.Double(center.getX(), center.getY()
                    - markerLength / 2, center.getX(), center.getY()
                    + markerLength / 2));
            g2.drawString(markerNumberToString(xNumber, true),
                    (int) (center.getX() - 5 * Math.abs(numDigitX)),
                    (int) (center.getY() + 25));
        }
        // y-axis
        for (Double yNumber : yMarkers.keySet()) {
            Point2D center = yMarkers.get(yNumber);
            g2.draw(new Line2D.Double(center.getX() - markerLength / 2,
                    center.getY(), center.getX() + markerLength / 2, center.getY()));
            g2.drawString(markerNumberToString(yNumber, false),
                    (int) (center.getX() - 21 * numDigitY),
                    (int) (center.getY() + 5));
        }
    }

    private String markerNumberToString(Double d, boolean isX) {
        if (isX) {
            if (numDigitX < 0) {
                return String.format("%.3f", d);
            } else {
                return String.format("%.0f", d);
            }
        } else {
            if (numDigitY < 0) {
                return String.format("%.3f", d);
            } else {
                return String.format("%.0f", d);
            }
        }
    }

    private void drawCurve(Graphics2D g2, Color color, boolean highlight,
            int oriX, int oriY, int w, int h, Point2D[] pts) {

        Point2D previous = null;
        g2.setColor(color);
        for (Point2D p : pts) {
            double x = (p.getX() - minX) / (maxX - minX) * w + oriX;
            double y = (1 - (p.getY() - minY) / (maxY - minY)) * h + oriY;
            Point2D current = new Point2D.Double(x, y);
            if (highlight) {
                for (int i = -1; i <= 1; i++) {
                    g2.draw(new Line2D.Double(current.getX() - 1, current
                            .getY() + i, current.getX() + 1, current.getY() + i));
                }
            }
            if (previous != null) {
                g2.draw(new Line2D.Double(previous, current));
            }
            previous = current;
        }
        g2.dispose();
    }

    private void fit() {
        BufferedImage current = ImageUtil.copyImage(plotCache);
        drawCurve(current.createGraphics(), Color.RED, false, originX, originY,
                imageWidth, imageHeight, getFittingPoints());// draw fitting
                                                             // curve
        graphLabel.setIcon(new ImageIcon(current));
        currentImage = current;
    }

    private Point2D[] getFittingPoints() {
        int ploynomial = 5;// default;
        try {
            ploynomial = Integer.valueOf(ploynomialField.getText());
        } catch (Exception e) {
            System.out.println("Cannot parse the polynomial value");
        }
        Point2D[] selected = new Point2D[ploynomial];
        int interval = points.length / (ploynomial - 1);
        for (int i = 0; i <= points.length; i += interval) {
            if (i >= points.length) {
                selected[i / interval] = points[points.length - 1];
            } else {
                selected[i / interval] = points[i];
            }
        }
        // Random rand = new Random();
        selected[0] = points[interval / 4];
        Point2D[] fittingPts = Interpolation.interpolation(selected, 0.001);
        return fittingPts;
    }

    private void paintBackground(BufferedImage image, int color) {
        WritableRaster raster = image.getRaster();
        int w = image.getWidth();
        int h = image.getHeight();
        int[] r = null;
        int[] g = null;
        int[] b = null;
        // paint background
        r = new int[w * h];
        g = new int[w * h];
        b = new int[w * h];
        Arrays.fill(r, color);
        Arrays.fill(g, color);
        Arrays.fill(b, color);
        raster.setSamples(0, 0, w, h, 0, r);
        raster.setSamples(0, 0, w, h, 1, g);
        raster.setSamples(0, 0, w, h, 2, b);
    }

    private void saveCurrentImage(File file) {
        String type = file.getName();
        if (type.lastIndexOf(".") < 0) {
            type = "png";
        } else {
            type = type.substring(type.lastIndexOf(".") + 1);
        }
        try {
            ImageIO.write(currentImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPoints() {
        double localMaxX = Integer.MIN_VALUE;
        double localMaxY = Integer.MIN_VALUE;
        double localMinX = Integer.MAX_VALUE;
        double localMinY = Integer.MAX_VALUE;
        for (Point2D p : points) {
            if (p.getX() > localMaxX) {
                localMaxX = p.getX();
            }
            if (p.getX() < localMinX) {
                localMinX = p.getX();
            }
            if (p.getY() > localMaxY) {
                localMaxY = p.getY();
            }
            if (p.getY() < localMinY) {
                localMinY = p.getY();
            }
        }
        int factorX = -8;
        int factorY = -8;
        if (localMaxX >= 1) {
            factorX = -1;
            while (localMaxX / Math.pow(10, ++factorX) >= 1) {
                ;
            }
        } else {
            factorX = 1;
            while (localMaxX / Math.pow(10, --factorX) < 1) {
                ;
            }
        }
        if (localMaxY >= 1) {
            factorY = -1;
            while (localMaxY / Math.pow(10, ++factorY) >= 1) {
                ;
            }
        } else {
            factorY = 1;
            while (localMaxY / Math.pow(10, --factorY) < 1) {
                ;
            }
        }

        localMaxX = ((int) (localMaxX / Math.pow(10, factorX - 2) + 1)) * Math.pow(10, factorX - 2);
        localMaxY = ((int) (localMaxY / Math.pow(10, factorY - 2) + 1)) * Math.pow(10, factorY - 2);

        int secLargestDigit = (int) (localMaxX / Math.pow(10, factorX - 2)) % 10;
        maxX = ((secLargestDigit > 5) ? (int) (localMaxX / Math.pow(10, factorX - 1) + 1) * Math.pow(10, factorX - 1)
                                      : ((int) (localMaxX / Math.pow(10, factorX - 1)) + 0.5) * Math.pow(10, factorX - 1));

        secLargestDigit = (int) (localMaxY / Math.pow(10, factorY - 2)) % 10;
        maxY = ((secLargestDigit > 5) ? (int) (localMaxY / Math.pow(10, factorY - 1) + 1) * Math.pow(10, factorY - 1)
                                      : ((int) (localMaxY / Math.pow(10, factorY - 1)) + 0.5) * Math.pow(10, factorY - 1));

        secLargestDigit = (int) (localMinX / Math.pow(10, factorX - 2)) % 10;
        minX = (secLargestDigit > 5) ? ((int) (localMinX / Math.pow(10, factorX - 1)) + 0.5) * Math.pow(10, factorX - 1)
                                     : (int) (localMinX / Math.pow(10, factorX - 1)) * Math.pow(10, factorX - 1);

        secLargestDigit = (int) (localMinY / Math.pow(10, factorY - 2)) % 10;
        minY = (secLargestDigit > 5) ? ((int) (localMinY / Math.pow(10, factorY - 1)) + 0.5) * Math.pow(10, factorY - 1)
                                     : (int) (localMinY / Math.pow(10, factorY - 1)) * Math.pow(10, factorY - 1);

        numDigitX = factorX - 1;
        numDigitY = factorY - 1;
    }

    private void clear() {
        BufferedImage current = ImageUtil.copyImage(plotCache);
        graphLabel.setIcon(new ImageIcon(current));
        currentImage = current;
    }

    public static Point2D[] getPlotData() {
        double[] bgX = { // 200
        0.0522, 0.0525, 0.0529, 0.0532, 0.0536, 0.0539, 0.0542, 0.0546, 0.0549,
                0.0553, 0.0556, 0.0560, 0.0563, 0.0567, 0.0570, 0.0573, 0.0577,
                0.0580, 0.0584, 0.0587, 0.0591, 0.0594, 0.0598, 0.0601, 0.0605,
                0.0608, 0.0612, 0.0615, 0.0618, 0.0622, 0.0625, 0.0629, 0.0632,
                0.0636, 0.0639, 0.0643, 0.0646, 0.0649, 0.0653, 0.0656, 0.0660,
                0.0663, 0.0667, 0.0670, 0.0674, 0.0677, 0.0680, 0.0684, 0.0687,
                0.0691, 0.0694, 0.0698, 0.0701, 0.0705, 0.0708, 0.0712, 0.0715,
                0.0718, 0.0722, 0.0725, 0.0729, 0.0732, 0.0736, 0.0739, 0.0742,
                0.0746, 0.0749, 0.0753, 0.0756, 0.0760, 0.0763, 0.0766, 0.0770,
                0.0773, 0.0777, 0.0780, 0.0784, 0.0787, 0.0790, 0.0794, 0.0797,
                0.0801, 0.0804, 0.0808, 0.0811, 0.0814, 0.0818, 0.0821, 0.0825,
                0.0828, 0.0832, 0.0835, 0.0838, 0.0842, 0.0845, 0.0849, 0.0852,
                0.0856, 0.0859, 0.0862, 0.0866, 0.0869, 0.0873, 0.0876, 0.0879,
                0.0883, 0.0886, 0.0890, 0.0893, 0.0896, 0.0900, 0.0903, 0.0907,
                0.0910, 0.0914, 0.0917, 0.0920, 0.0924, 0.0927, 0.0931, 0.0934,
                0.0937, 0.0941, 0.0944, 0.0948, 0.0951, 0.0954, 0.0958, 0.0961,
                0.0965, 0.0968, 0.0971, 0.0975, 0.0978, 0.0982, 0.0985, 0.0988,
                0.0992, 0.0995, 0.0999, 0.1002, 0.1005, 0.1009, 0.1012, 0.1016,
                0.1019, 0.1022, 0.1026, 0.1029, 0.1032, 0.1036, 0.1039, 0.1043,
                0.1046, 0.1049, 0.1053, 0.1056, 0.1060, 0.1063, 0.1066, 0.1070,
                0.1073, 0.1076, 0.1080, 0.1083, 0.1087, 0.1090, 0.1093, 0.1097,
                0.1100, 0.1104, 0.1107, 0.1110, 0.1114, 0.1117, 0.1120, 0.1124,
                0.1127, 0.1130, 0.1134, 0.1137, 0.1140, 0.1144, 0.1147, 0.1151,
                0.1154, 0.1157, 0.1161, 0.1164, 0.1168, 0.1171, 0.1174, 0.1177,
                0.1181, 0.1184, 0.1188, 0.1191, 0.1194, 0.1198, 0.1201 };

        double[] bgY = { // 200
        23.42, 23.35, 23.30, 23.05, 23.06, 22.89, 23.00, 22.83, 22.69, 22.40,
                22.38, 22.23, 22.26, 22.17, 22.13, 22.03, 21.96, 21.88, 21.79,
                21.92, 22.33, 21.96, 21.91, 22.19, 22.15, 22.17, 22.17, 22.03,
                22.08, 21.96, 21.88, 21.88, 21.87, 21.84, 22.01, 21.96, 22.13,
                22.21, 22.12, 22.38, 22.37, 22.47, 22.41, 22.35, 22.39, 22.46,
                22.37, 22.36, 22.31, 22.36, 22.32, 22.24, 22.34, 22.56, 22.56,
                22.47, 22.61, 22.40, 22.08, 22.21, 22.24, 22.29, 22.32, 22.73,
                22.37, 22.35, 22.43, 22.46, 22.46, 22.46, 22.50, 22.44, 22.52,
                22.45, 22.58, 22.55, 22.47, 22.62, 22.54, 22.76, 22.88, 23.15,
                23.65, 24.42, 25.02, 24.69, 25.05, 25.11, 25.07, 25.16, 25.19,
                25.29, 25.45, 25.16, 24.80, 24.73, 24.60, 24.38, 24.32, 24.33,
                24.29, 24.32, 24.33, 24.26, 24.28, 24.21, 42.92, 47.07, 33.25,
                33.93, 33.59, 32.99, 33.07, 32.41, 31.74, 30.76, 29.32, 28.69,
                27.81, 26.95, 26.15, 26.62, 25.93, 25.87, 25.32, 25.77, 25.70,
                25.61, 25.46, 25.66, 25.73, 25.99, 26.35, 26.25, 26.48, 26.59,
                26.70, 26.33, 26.24, 26.23, 26.13, 25.91, 25.83, 25.82, 25.76,
                25.88, 25.91, 25.72, 25.79, 25.71, 25.92, 25.75, 25.56, 25.88,
                25.74, 25.68, 25.76, 25.62, 25.91, 25.92, 26.08, 25.95, 25.90,
                25.96, 26.14, 26.31, 26.32, 26.54, 26.52, 26.52, 26.46, 26.34,
                26.29, 26.43, 26.29, 26.45, 26.27, 26.23, 26.22, 26.44, 26.32,
                26.37, 26.30, 26.21, 26.32, 26.51, 26.67, 26.43, 26.41, 26.38,
                26.35, 26.44, 26.03, 26.16, 26.32, 26.09, 25.98, 26.02, 26.13,
                26.02 };

        Point2D[] points = new Point2D[200];
        for (int i = 0; i < 200; i++) {
            points[i] = new Point2D.Double(bgX[i], bgY[i]);
        }

        return points;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FittingGUI window = new FittingGUI(getPlotData());
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
