package main;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MosaicApp extends JFrame
{
	//Main method of the MosaicApp program.
	public static void main(String[] args) 
	{
		new MosaicApp("Mosaic Cellular Automata");
	}
	
	// Constructor for the MosaicApp class.
	public MosaicApp(String title) 
	{
		// Calls window creation method from JFrame
		super(title);
		
		// Set default operation when the frame is closed
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		// Set location of the window
		this.setLocation(200,50);
		
		// Instantiate new panel object
		this.add(new MosaicPanel());
		
		// Fit panel to the preferred size of the frame
		this.pack();
		
		// Minimum functionality for a window
		this.setVisible(true);
	}
}