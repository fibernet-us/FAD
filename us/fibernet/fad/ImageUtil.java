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

import java.util.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * provides routines for image display manipulation
 *
 */
public class ImageUtil {

    private static final String colorTableDir = "ColorTables/";
    private static ColorTable colors;

    static {
        try {
            colors = ImageUtil.getColorTable(colorTableDir + "rainbow.txt");
        } catch (IOException e) {
            System.out.println("Couldn't find color table file.");
        }
    }

    static void useColorTable(String colorTable) {
        try {
            colors = ImageUtil.getColorTable(colorTableDir
                    + colorTable.toLowerCase() + ".txt");
        } catch (IOException e) {
            System.out.println("Couldn't find color table file.");
        }
    }

    /**
     * @param file color table file
     * @return the rgb color table
     * @throws IOException
     */
    public static ColorTable getColorTable(String file) throws IOException {

        List<List<Byte>> rgbColorTable = new ArrayList<List<Byte>>();
        for (int i = 0; i < 3; i++)
            rgbColorTable.add(new ArrayList<Byte>());
        Scanner scan = new Scanner(new File(file));
        while (scan.hasNext()) {
            rgbColorTable.get(0).add((byte) scan.nextInt());
            rgbColorTable.get(1).add((byte) scan.nextInt());
            rgbColorTable.get(2).add((byte) scan.nextInt());
        }

        ColorTable ct = new ColorTable(rgbColorTable.get(0).size());

        byte[] red = ct.getRed();
        byte[] green = ct.getGreen();
        byte[] blue = ct.getBlue();

        for (int i = 0; i < rgbColorTable.get(0).size(); i++) {
            red[i] = rgbColorTable.get(0).get(i);
            green[i] = rgbColorTable.get(1).get(i);
            blue[i] = rgbColorTable.get(2).get(i);
        }

        scan.close();
        return ct;
    }

    /**
     * Display the color table. Used for test
     * 
     * @param table
     */
    static void displayColorTable(List<List<Byte>> table) {
        for (List<Byte> row : table) {
            System.out.println(row);
        }
    }

    /**
     * Given a int[][], return a smaller int[][] by skipping some elements in
     * the original int[][]
     */
    static int[][] shrinkArray(int[][] original) {
        int optimalSize = 500;// the pixel number to achieve good image
        int shrinkFactor = Math.max(original.length, original[0].length)
                / optimalSize;
        int[][] output = new int[original.length / shrinkFactor][original[0].length
                / shrinkFactor];
        for (int r = 0; r * shrinkFactor < original.length; r++)
            for (int c = 0; c * shrinkFactor < original.length; c++) {
                output[r][c] = original[r * shrinkFactor][c * shrinkFactor];
            }
        return output;
    }

    /**
     * Given a double[][] and number of colors, convert it to an int[][] pattern
     * with values within the range of colors.
     * 
     * @param input
     * @param numColors
     *            number of colors used.
     * @return converted image pattern
     */
    static int[][] convertImage(double[][] input, int numColors) {
        int[][] output = new int[input.length][input[0].length];
        double max = Double.MIN_VALUE;
        for (int i = 0; i < output.length; i++)
            for (int j = 0; j < output[0].length; j++) {
                if (input[i][j] > max)
                    max = input[i][j];
            }

        double factor = (double) max / (double) numColors + 1;

        // System.out.println("max double: "+max+" factor: "+factor);

        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                output[i][j] = (int) (input[i][j] / factor);
            }
        }
        return output;
    }

    /**
     * 
     * @param input
     *            2D array
     * @param numColors
     *            the number of colors used
     * @return
     */
    static int[][] convertImage(int[][] input, int numColors) {
        int[][] output = new int[input.length][input[0].length];
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < output.length; i++)
            for (int j = 0; j < output[0].length; j++) {
                if (input[i][j] > max)
                    max = input[i][j];
                else if (input[i][j] < min && min != 0)
                    min = input[i][j];
            }
        double factor = (double) (max - min) / numColors + 1;

        // System.out.println("max double: "+max+" factor: "+factor);

        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                if (input[i][j] < min)
                    output[i][j] = 0;
                else
                    output[i][j] = (int) ((input[i][j] - min) / factor);
            }
        }
        return output;
    }

    /**
     * 
     * @param array
     *            a 2D double[][] array
     * @return 2D int[][] array
     */
    static int[][] doubleToInt(double[][] array) {
        int[][] output = new int[array.length][array[0].length];
        for (int r = 0; r < array.length; r++)
            for (int c = 0; c < array[0].length; c++)
                output[r][c] = (int) array[r][c];
        return output;
    }

    /**
     * 
     * @param width
     * @param height
     * @return a 2D image in the form of double[][]
     */
    static double[][] getTestImage(int width, int height) {
        double[][] d = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                d[i][j] = Math.random() * 5000;
            }
        }
        return d;
    }

    /**
     * 
     * @param image
     * @return a copy of the input image
     */
    public static BufferedImage copyImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copy.createGraphics();
        g.drawImage(image, 0, 0, copy.getWidth(), copy.getHeight(), null);
        g.dispose();
        return copy;
    }

    /**
     * To resize the image to fit in the JLabel, and set it to the JLabel
     * 
     * @param label
     *            the JLabel to hold the image
     * @param image
     *            the image to fit in the JLabel
     * @return the resized Image
     */
    static BufferedImage fitImage(JLabel label, BufferedImage image) {
        int newWidth = label.getWidth();
        int newHeight = label.getHeight();
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        label.setIcon(new ImageIcon(resizedImage));
        return resizedImage;
    }

    /**
     * 
     * @param width
     * @param height
     * @return a rainbow image based on the default color table
     */
    static BufferedImage getRainbowImage(int width, int height) {

        int numColors = colors.getRed().length;

        byte[] red = colors.getRed();
        byte[] green = colors.getGreen();
        byte[] blue = colors.getBlue();

        BufferedImage im = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = im.getRaster();

        int unit = width / numColors;

        int[] r = new int[unit * height];
        int[] g = new int[unit * height];
        int[] b = new int[unit * height];

        for (int i = 0; i < width; i += unit) {
            int colorIndex = i / unit;
            Arrays.fill(r, red[colorIndex]);
            Arrays.fill(g, green[colorIndex]);
            Arrays.fill(b, blue[colorIndex]);
            raster.setSamples(i, 0, unit, height, 0, r);
            raster.setSamples(i, 0, unit, height, 1, g);
            raster.setSamples(i, 0, unit, height, 2, b);

        }

        return im;
    }

    /**
     * 
     * @return a rainbow image based on the default color table
     */
    static BufferedImage getRainbowImage() {
        // default width = numColors, height = numColors

        int numColors = colors.getRed().length;

        byte[] red = colors.getRed();
        byte[] green = colors.getGreen();
        byte[] blue = colors.getBlue();

        int width = numColors;
        int height = numColors;

        BufferedImage im = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = im.getRaster();

        int unit = width / numColors;

        int[] r = new int[unit * height];
        int[] g = new int[unit * height];
        int[] b = new int[unit * height];

        for (int i = 0; i < width; i += unit) {
            int colorIndex = i / unit;
            Arrays.fill(r, red[colorIndex]);
            Arrays.fill(g, green[colorIndex]);
            Arrays.fill(b, blue[colorIndex]);
            raster.setSamples(i, 0, unit, height, 0, r);
            raster.setSamples(i, 0, unit, height, 1, g);
            raster.setSamples(i, 0, unit, height, 2, b);
        }
        return im;
    }

    /**
     * Given a 2D array, return a image with the default color table
     * 
     * @param input
     *            2D array
     * @return BufferedImage using the default color table
     * @throws IOException
     */
    static BufferedImage getImage(int[][] input) throws IOException {

        int width, height;
        int unit = 1;// for higher resolution
        height = input.length * unit;
        width = input[0].length * unit;
        // System.out.println("width: "+width+" height: "+height);
        byte[] red = colors.getRed();
        byte[] green = colors.getGreen();
        byte[] blue = colors.getBlue();

        BufferedImage im = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = im.getRaster();

        int[] r = new int[unit * unit];
        int[] g = new int[unit * unit];
        int[] b = new int[unit * unit];

        for (int i = 0; i < width; i += unit) {
            for (int j = 0; j < height; j += unit) {
                int color = input[j / unit][i / unit];
                Arrays.fill(r, red[color]);
                Arrays.fill(g, green[color]);
                Arrays.fill(b, blue[color]);
                raster.setSamples(i, j, unit, unit, 0, r);
                raster.setSamples(i, j, unit, unit, 1, g);
                raster.setSamples(i, j, unit, unit, 2, b);
            }
        }
        return im;
    }

    /**
     * GaussianElimination
     * 
     * @param A
     *            the x matrix
     * @param b
     *            the y matrix
     * @return coefficients
     */
    static double[] gaussian(double[][] A, double[] b) {
        int N = b.length;
        // double EPSILON = 1e-10;

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            /*
             * if (Math.abs(A[p][p]) <= EPSILON) { throw new
             * RuntimeException("Matrix is singular or nearly singular"); }
             */

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    /**
     * display the image in the form of int[][]. Use to check 2D array.
     * 
     * @param image
     *            input image
     */
    public static void displayArray(int[][] image) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                System.out.print(image[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("image width: " + image[0].length + " height: "
                + image.length);
    }

}
