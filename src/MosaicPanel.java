import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class MosaicPanel extends JPanel implements ActionListener {
	
		// Define instance variables
		private static final int PANEL_W = 600;
		private static final int PANEL_H = 600;
		private Timer timer;
		private BufferedImage lennaImage;
		private BufferedImage filteredImage;
		
		// Constructor
		public MosaicPanel()
		{	
			// Call the creation method from JPanel
			super();
			
			// Set preferred size/color/visibility of the component
			this.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
			this.setBackground(Color.BLACK);
			this.setVisible(true);
			
			// Import image
			try { 
				lennaImage = ImageIO.read(new File("./assets/lenna.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Apply filter
			filteredImage = filterImage(lennaImage, Filters.orderedDither);
			
			// Start timer (ticks every 10 microseconds)
			timer = new Timer(10, this);
			timer.start();
		}	
		
		// Callback method
		public void paintComponent(Graphics g)
		{
			// Call the paint method
			super.paintComponent(g);
			
			// Upgrade the graphics tool
			Graphics2D g2 = (Graphics2D)g;
			
			// Draw background
			g2.setColor(new Color(0,0,0));
			g2.fill(new Rectangle2D.Double(0, 0, PANEL_W, PANEL_H));
			
			// Draw image 1
			g2.setColor(new Color(255,255,255));
			g2.drawString("lenna image", 20, 45);
			g2.drawImage(lennaImage,20,50,this);
			
			// Draw image 2
			g2.drawString("filtered image", 300, 45);
		    g2.drawImage(filteredImage,300,50,this);
			
		    // Repaint
			repaint();
		}
		
		// Callback method
		public void actionPerformed(ActionEvent e) {
			
			// Code here...
			
		}
		
		// Apply a filter to a single image
		public BufferedImage filterImage(BufferedImage src, Filters filt)
		{
			// Create a copy of the image
			BufferedImage copy = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
			
			 int rgb = 0;
			 int bd = 64;
			 int w = src.getWidth();
			 int h = src.getHeight();
			 
			 double[] dither = new double[]{1, 33,  9, 41, 3,  35, 11, 43, 49, 17, 57, 25, 51, 19, 59, 27, 13, 45, 5, 
					    37, 15, 47, 7, 39, 61, 29, 53, 21, 63, 31, 55, 23, 4, 36, 12, 44, 2, 34, 10, 42, 52,
					    20, 60, 28, 50, 18, 58, 26, 16, 48, 8, 40, 14, 46, 6, 38, 64, 32, 56, 24, 62, 30,
					    54, 22};
			
			 for (int y = 0; y < h; y++) {
				 for (int x = 0; x < w; x++) {
					
				      int c = src.getRGB(x,y);
				      int d = (int)dither[(y % 8) * 8 + (x % 8)];
				      int l = dither.length;
			
				      int r = ((c >> 16) & 0xff);
				      int g = ((c >> 8) & 0xff);
				      int b = (c & 0xff);
			
				      rgb = 0x000000 | 
				    		  closestCol(r, d, bd, l) << 16 | 
				    		  closestCol(g, d, bd, l) << 8 | 
				    		  closestCol(b, d, bd, l);
				      
				      copy.setRGB(x,y, rgb);
				 }
			 }
			 return copy; 
		}
		
		private static int closestCol(int col, int threshold, int quantum, int ditherSize) {
		      int quantised = quantum * (int)(col / quantum + threshold / (ditherSize + 1f) + 0.5);
		      return quantised > 255 ? 255 : quantised;
		}
		
		// Apply a filter to a single pixel
		public int filterPixel(int rgb, Filters filt) 
		{ 
			int red, green, blue;
			
			// Apply the operation to each pixel
			switch (filt) 
			{
				case orderedDither:
					red = getRed(rgb) * 2;
					green = getGreen(rgb) * 2;
					blue = getBlue(rgb) * 2;
					break;
				default:
					red = green = blue = 150;
					break;
			}
			return new Color(clip(red), clip(green), clip(blue)).getRGB();
		}

		// Clip bounds
		private int clip(int v) 
		{
			v = v > 255 ? 255 : v;
			v = v < 0 ? 0 : v;
			return v;
		}
		
		// Return red pixel value
		protected int getRed(int p)
		{
			return (p >>> 16) & 0xFF;
		}

		// Return green pixel value
		protected int getGreen(int p)
		{
			return (p >>> 8) & 0xFF;
		}

		// Return blue pixel value
		protected int getBlue(int p)
		{
			return p & 0xFF;
		}
}