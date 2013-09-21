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
package xiao.jad;

import java.awt.BorderLayout;

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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FittingGUI window = new FittingGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FittingGUI(Point2D[] points) {
		this.points = points.clone();
		processPoints();
		initialize();
	}
	
	public FittingGUI(){
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
		graphPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		int fw = frame.getWidth();
		int fh = frame.getHeight()-80;
		graphPanel.setSize(fw,fh);
		frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
		
		graphLabel = new JLabel("");
		int pw = graphPanel.getWidth();
		int ph = graphPanel.getHeight();
		graphLabel.setSize(pw,ph);
		graphPanel.add(graphLabel);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		optionPanel.setSize(600,50);
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
		buttonPanel.setSize(600,50);
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
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
            	if(returnValue == JFileChooser.APPROVE_OPTION){
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
		
		//plot();
	}
	
	private void plot(){	
		int w = graphPanel.getWidth();
		int h = graphPanel.getHeight();
		BufferedImage plotImage = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        paintBackground(plotImage,244);
        //generate axis x and y
        imageWidth = w-160;
        imageHeight = h-120;
        Graphics2D g2 = plotImage.createGraphics();
        Rectangle2D axis = new Rectangle2D.Double(originX,originY,imageWidth,imageHeight);
        g2.setColor(Color.BLACK);
        g2.draw(axis);
        //draw markers
        int numXMarker = 6;
        int numYMarker = 6;
        Map<Double, Point2D> xMarkers = new TreeMap<Double, Point2D>();
        Map<Double, Point2D> yMarkers = new TreeMap<Double, Point2D>();
        for(int i=0;i<6;i++){
        	xMarkers.put(minX+(maxX-minX)/5*i, new Point.Double(originX+i*imageWidth/(numXMarker-1),originY+imageHeight));
        	yMarkers.put(minY+(maxY-minY)/5*(5-i), new Point.Double(originX,originY+i*imageHeight/(numYMarker-1)));
        }
        drawMarker(g2, xMarkers, yMarkers);
        drawCurve(g2,Color.BLACK,true,originX,originY,imageWidth,imageHeight, points);//data
                
        graphLabel.setIcon(new ImageIcon(plotImage));
        currentImage = plotImage;
        plotCache = ImageUtil.copyImage(plotImage);
	}
	
	private void drawMarker(Graphics2D g2,Map<Double, Point2D> xMarkers,Map<Double, Point2D> yMarkers){
		int markerLength = 6;
		//x-axis
		for (Double xNumber : xMarkers.keySet() ) {
        	Point2D center = xMarkers.get(xNumber);
            g2.draw(new Line2D.Double(center.getX(),center.getY()-markerLength/2,center.getX(),center.getY()+markerLength/2));
            g2.drawString(markerNumberToString(xNumber,true), (int)(center.getX()-5*Math.abs(numDigitX)), (int)(center.getY()+25));
        }
		//y-axis
        for (Double yNumber : yMarkers.keySet() ) {
        	Point2D center = yMarkers.get(yNumber);
            g2.draw(new Line2D.Double(center.getX()-markerLength/2,center.getY(),center.getX()+markerLength/2,center.getY()));
            g2.drawString(markerNumberToString(yNumber,false), (int)(center.getX()-21*numDigitY), (int)(center.getY()+5));
        }
	}
	
	private String markerNumberToString(Double d, boolean isX){
		if(isX){
			if(numDigitX<0){
				return String.format("%.3f",d);
			}else{
				return String.format("%.0f",d);
			}
		}else{
			if(numDigitY<0){
				return String.format("%.3f",d);
			}else{
				return String.format("%.0f",d);
			}
		}
	}
	
	private void drawCurve(Graphics2D g2, Color color, boolean highlight, int oriX, int oriY, int w, int h, Point2D[] points){
		Point2D previous = null;
		g2.setColor(color);
		for(Point2D p : points){	
			double x = (p.getX()-minX)/(maxX-minX)*w+oriX;
			double y = (1-(p.getY()-minY)/(maxY-minY))*h+oriY;
			Point2D current = new Point2D.Double(x,y);		
			if(highlight){
				for(int i=-1;i<=1;i++){
					g2.draw(new Line2D.Double(current.getX()-1, current.getY()+i,current.getX()+1,current.getY()+i));
				}
			}
			if(previous!=null){
				g2.draw(new Line2D.Double(previous,current));
			}
			previous = current;
		}
		g2.dispose();
	}
	
	private void fit(){
		BufferedImage current = ImageUtil.copyImage(plotCache);
		drawCurve(current.createGraphics(),Color.RED,false, originX,originY,imageWidth,imageHeight, getFittingPoints());//draw fitting curve
		graphLabel.setIcon(new ImageIcon(current));
		currentImage = current;
	}
	
	private Point2D[] getFittingPoints(){
		int ploynomial = 5;//default;
		try{
			ploynomial = Integer.valueOf(ploynomialField.getText());
		}catch(Exception e){
			System.out.println("Cannot parse the polynomial value");
		}
		Point2D[] selected = new Point2D[ploynomial];
		int interval = points.length/(ploynomial-1);
		for(int i=0;i<=points.length;i+=interval){
			if(i>=points.length){
				selected[i/interval]=points[points.length-1];
			}else{
				selected[i/interval] = points[i];
			}
		}
		//Random rand = new Random();
		selected[0] = points[interval/4];
		Point2D[] fittingPts = Interpolation.interpolation(selected, 0.001);
		return fittingPts;
	}
	
	private void paintBackground(BufferedImage image, int color){
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
	
	private void saveCurrentImage(File file){
		String type = file.getName();
		if(type.lastIndexOf(".")<0){
			type = "png";
		}else{
			type = type.substring(type.lastIndexOf(".")+1);
		}
		try {
			ImageIO.write(currentImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void processPoints(){
		double localMaxX = Integer.MIN_VALUE;
		double localMaxY = Integer.MIN_VALUE;
		double localMinX = Integer.MAX_VALUE;
		double localMinY = Integer.MAX_VALUE;
		for(Point2D p : points){
			if(p.getX()>localMaxX)
				localMaxX = p.getX();
			if(p.getX()<localMinX)
				localMinX = p.getX();
			if(p.getY()>localMaxY)
				localMaxY = p.getY();
			if(p.getY()<localMinY)
				localMinY = p.getY();
		}
		int factorX = -8;
		int factorY = -8;
		if(localMaxX>=1){
			factorX=-1;
			while(localMaxX/Math.pow(10, ++factorX)>=1);
		}		
		else{
			factorX=1;
			while(localMaxX/Math.pow(10, --factorX)<1);
		}
		if(localMaxY>=1){
			factorY=-1;
			while(localMaxY/Math.pow(10, ++factorY)>=1);
		}		
		else{
			factorY=1;
			while(localMaxY/Math.pow(10, --factorY)<1);
		}	
		
		localMaxX = ((int)(localMaxX/Math.pow(10, factorX-2)+1))*Math.pow(10, factorX-2);
		localMaxY = ((int)(localMaxY/Math.pow(10, factorY-2)+1))*Math.pow(10, factorY-2);
		int secLargestDigit = (int)(localMaxX/Math.pow(10, factorX-2))%10;
		maxX=((secLargestDigit>5)?(int)(localMaxX/Math.pow(10, factorX-1)+1)*Math.pow(10,factorX-1):((int)(localMaxX/Math.pow(10, factorX-1))+0.5)*Math.pow(10,factorX-1));
		secLargestDigit = (int)(localMaxY/Math.pow(10, factorY-2))%10;
		maxY=((secLargestDigit>5)?(int)(localMaxY/Math.pow(10, factorY-1)+1)*Math.pow(10,factorY-1):((int)(localMaxY/Math.pow(10, factorY-1))+0.5)*Math.pow(10,factorY-1));
		
		secLargestDigit = (int)(localMinX/Math.pow(10, factorX-2))%10;
		minX=(secLargestDigit>5)?((int)(localMinX/Math.pow(10, factorX-1))+0.5)*Math.pow(10,factorX-1):(int)(localMinX/Math.pow(10, factorX-1))*Math.pow(10,factorX-1);
		secLargestDigit = (int)(localMinY/Math.pow(10, factorY-2))%10;
		minY=(secLargestDigit>5)?((int)(localMinY/Math.pow(10, factorY-1))+0.5)*Math.pow(10,factorY-1):(int)(localMinY/Math.pow(10, factorY-1))*Math.pow(10,factorY-1);
		
		numDigitX = factorX-1;
		numDigitY = factorY-1;
	}
	
	private void clear(){
		BufferedImage current = ImageUtil.copyImage(plotCache);
		graphLabel.setIcon(new ImageIcon(current));
		currentImage = current;
	}
	

}
