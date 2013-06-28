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

package org.fiberdiffraction.jad;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;

public class Interpolation {
	/**
	 * Generate an array of points with a given interval of x that defines a curve passing the input points
	 * @param pts Input array of points
	 * @param interval The interval of x of the points in the output array
	 * @return An array of points with a given interval of x that defines a curve passing the input points
	 */
	public static Point2D[] interpolation(Point2D[] pts, double interval){
		Arrays.sort(pts, new Comparator<Point2D>(){
			public int compare(Point2D p1, Point2D p2){
				double sub = p1.getX() - p2.getX();
				if(sub<0)
					return -1;
				else if(sub==0)
					return 0;
				else
					return 1;
			}
		});
		int nPts = pts.length;
		double curveParams[] = new double[nPts];
		double[][] xmatrix = new double[nPts][nPts];
        double[] ymatrix = new double[nPts];
        for (int w = 0; w < nPts; w++) {
            for (int h = 0; h < nPts; h++) {
                xmatrix[w][h] = Math.pow(pts[w].getX(), nPts - h - 1);
            }
        }
        for (int y = 0; y < nPts; y++)
            ymatrix[y] = pts[y].getY();
        curveParams = ImageUtil.gaussian(xmatrix, ymatrix);
        int max = (int)(pts[nPts-1].getX()+1);
        Point2D[] result = new Point2D[max];
        for(int i=0;i<result.length;i++){
        	result[i]=new Point2D.Double(i,curveFunction(curveParams,i));
        }
		return result;
	}
	
	private static double curveFunction(double[] params, double x){
		int rank = params.length;
		double y = 0;
		for(int i=0;i<rank;i++){
			y = y*x+params[i];
		}
		return y;
	}
	
	public static void main(String[] args) {
		Point2D[] input = {new Point2D.Double(1,4), new Point2D.Double(2,9), new Point2D.Double(10,121)};
		Point2D[] pts = interpolation(input, 1);
		for(Point2D p: pts)
		 System.out.println(p);
	}
}
