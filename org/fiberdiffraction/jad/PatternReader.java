/*
 * Copyright Xiao Yi and Wen Bian. All rights reserved.
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

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;

public final class PatternReader {

    private static int[][] image;
    
    private PatternReader() {
    }

    /**
     * Parse the pattern file type by extension and call accordingly a read method
     */
    public static int[][] readPattern(String fname) {

        if (endsWithIgnoreCase(fname, "tif")) {
            return readTif(fname);
        } 
        else if (endsWithIgnoreCase(fname, "plr")) {
            return readPlr(fname);
        } 
        else {
            ; // TODO
        }
        
        return null;
    }

    /**
     * Parse the pattern file extension and call accordingly a read method
     * Read pattern files that come with attributes not stored in itself
     */
    public static int[][] readPattern(String[] args) {

        String fname = args[0];

        if (endsWithIgnoreCase(fname, "dat")) {
            int w = 0, h = 0;
            try {
                w = Integer.parseInt(args[1]);
                h = Integer.parseInt(args[2]);
                return readDat(fname, w, h);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("dat image width and height must be specified");
            }
        }

        return null;
    }

    /*
     * Read a raw data image and extract image data into a 2D array
     */
    private static int[][] readDat(String fname, int W, int H) {

        EndianCorrectInputStream input = null;
        image = null;

        try {

            image = new int[H][W];
            input = new EndianCorrectInputStream(new FileInputStream(fname),
                    false);

            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    image[i][j] = (int) (input.readShortCorrect());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return image;
    }

    /*
     * Read a TIFF image and extract image data into a 2D array
     */
    private static int[][] readTif(String fname) {

        image = null;

        try {
            SeekableStream s = new FileSeekableStream(fname);
            TIFFDecodeParam param = null;
            ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
            RenderedImage op = dec.decodeAsRenderedImage(0);
            Raster raster = op.getData();
            image = new int[op.getHeight()][op.getWidth()];
            // System.out.println(op.getHeight() + " " + op.getWidth());

            int[] pixelColor = new int[4];
            for (int x = 0; x < op.getWidth(); x++) {
                for (int y = 0; y < op.getHeight(); y++) {
                    raster.getPixel(x, y, pixelColor);
                    image[y][x] = pixelColor[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /*
     * Read a PLR image and extract image data into a 2D array
     */
    public static int[][] readPlr(String fname) {
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            DataPlr dp = new DataPlr();
            dp.parseData(br);
            br.close();            
            return dp.getImageData();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    /*
     * The following code is copied from:
     * http://www.java2s.com/Tutorial/Java/0040__Data-Type/CheckifaStringendswithaspecifiedsuffix.htm
     */
    
    /*
     * Check if a String ends with a specified suffix, case insensitive.
     */
    private static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /*
     * Check if a String ends with a specified suffix
     */
    private static boolean endsWith(String str, String suffix,
            boolean ignoreCase) {

        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }

        if (suffix.length() > str.length()) {
            return false;
        }

        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0,
                suffix.length());
    }

}
