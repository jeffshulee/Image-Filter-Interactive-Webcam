import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Universe for Webcam Catch game in which player holds the fliers and the background image, finds and holds the regions in the background image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * @author Jeff Shu Lee (Lines 96 - 197)
 * @author Chris Bailey-Kellogg and Travis Peters (Lines 12 - 95)
 * 
 */
public class Universe {
    private static final int maxColorDiff = 20;		    // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50; 		    // how many points in a region to be worth considering

    private BufferedImage image;                            // a reference to the background image for the universe
    private Color trackColor;                               // color of regions of interest (set by mouse press)

    private ArrayList<ArrayList<Point>> regions;	    // a region is a list of points
    // so the identified regions are in a list of lists of points

    private ArrayList<Flier> fliers;                        // all of the fliers

    private boolean[][] pixelVisited; 			    // A 2D array of booleans to keep track of pixels visited 
    
    /**
     * New universe with a background image and an empty list of fliers
     * @param image
     */
    public Universe(BufferedImage image) {
        this.image = image;		
        fliers = new ArrayList<Flier>();
        
        pixelVisited = new boolean[image.getWidth()][image.getHeight()];  // filling in the array marking which is visited (initialized to false)
    }

    /**
     * Set the image (from the webcam) that makes up the universe's background
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    /**
     * Allow others to ask about the state of the trackColor in the universe
     * @return
     */
    public Color getTrackingColor() {
        return trackColor;
    }
    /**
     * Setting the color from an explicitly defined Color object 
     * as opposed to getting input from the player.
     * @param color
     */
    public void setTrackingColor(Color color) {
        trackColor = color;
    }
    /**
     * Allow others to ask about the size of the universe (width)
     * @return
     */
    public int getWidth() {
        return image.getWidth();
    }
    /**
     * Allow others to ask about the size of the universe (height)
     * @return
     */
    public int getHeight() {
        return image.getHeight();
    }
    /**
     * Accesses the currently-identified regions.
     * @return
     */
    public ArrayList<ArrayList<Point>> getRegions() {
        return regions;
    }
    /**
     * Set the universe's regions.
     * @return
     */
    public void setRegions(ArrayList<ArrayList<Point>> regions) {
        this.regions = regions;
    }

    /**
     * Adds the flier to the universe
     * @param f
     */
    public void addFlier(Flier f) {
        fliers.add(f);
    }

    /**
     *  Move the flier and detect catches and misses
     */
    public void moveFliers() {
        // MY CODE
    	for (Flier flyer : fliers ){
    		flyer.move();
    		flyer.checkWin();
    		flyer.checkLose();
    	}
    }
    /**
     * Draw the fliers
     */
    public void drawFliers(Graphics g) {
        // MY CODE
    	for (Flier flier : fliers) {
    		flier.draw(g);
    	}
    }
    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    
    public void findRegions() {
        // TODO: My CODE
    	// Loop over all the pixels. If a pixel is unvisited and of the correct color, start a new region, meanwhile keeping track of the neighbors (and neighbors of neighbors...) that need to be visited in the region we are growing. */ 

    	regions = new ArrayList<ArrayList<Point>>(); // Note: regions need to be reinitialized every time since old reference is removed to avoid repainting 
    	for (int y = 0; y < image.getHeight(); y++) {
    		for(int x = 0; x < image.getWidth(); x++) {
    			if (	(pixelVisited[x][y] == false) && colorMatch(trackColor, new Color (image.getRGB(x, y))) ){
    				
    				ArrayList<Point> region = new ArrayList<Point>();	//Constructing a region instance variable of empty arraylist	
    				region.add(new Point(x,y));		// adding the current coordinates of the for-loop into the region empty arraylist (just one for now)
    				ArrayList<Point> toBeVisited = new ArrayList<Point>(); 	// equivalent to region.clone()
    				toBeVisited.add(new Point(x,y));
    				
    				while (! toBeVisited.isEmpty() ) {	// while there's something in toBeVisited 
    					Point p = toBeVisited.get(toBeVisited.size()-1);	// get the last point "p" of toBeVisited
    					toBeVisited.remove(toBeVisited.size()-1);		// remove it from the list
    					
    					region.add(p);						// add it to the region
    					pixelVisited[p.x][p.y] = true;		// mark it as visited so we don't double add visited points to toBeBVisited. //  pixelVisited is a array of array of booleans.There's one boolean for every pixel (x,y) marking the pixel visited.  
    					/*Loop over all its neighbors. If the neighbor is of the correct color AND has not been visited, add it to the list of pixels to be visited*/
    					for (int tx=-1; tx<=1; tx++){		// neighbor x, y range -1 to 1
    						for (int ty = -1; ty<=1; ty++){
    							if (p.x - tx > 0 && p.x -tx < image.getWidth() && p.y - ty > 0 && p.y - ty < image.getHeight()) {	// set window boundary for neighbors
    								if (colorMatch(trackColor, new Color (image.getRGB(p.x -tx, p.y - ty) )) && (pixelVisited[p.x-tx][p.y-ty] == false)){
    									toBeVisited.add(new Point(p.x - tx, p.y - ty)); 
    								}
    							}
    						}
    					}	
    				}
    				if (region.size() > minRegion) {				
    					regions.add(region);
    				}	
    			}
    				
    		}
    	}
    	
    }
    /**
     * Tests whether the two colors are similar enough 
     * @param c1
     * @param c2
     * @return
     */
    private static boolean colorMatch(Color targetColor, Color destinationColor) {
        // TODO: MY CODE
    	// if absolute value of target color - visited pixel color > threshold, return false
    	if (Math.abs(targetColor.getRed() - destinationColor.getRed()) > maxColorDiff) {		
    		return false;
    	}
    	if (Math.abs(targetColor.getBlue() - destinationColor.getBlue()) > maxColorDiff) {
        	return false;
    	}
        if (Math.abs(targetColor.getGreen() - destinationColor.getGreen()) > maxColorDiff) {
        	return false;
        }
        return true;  	
    }
    /**
     * Recolors image so that each region is a random uniform color, so we can see where they are
     */
    public void recolorRegions() {
        // MY CODE
    	for(ArrayList<Point> region : regions) {		 // placeholder for variable 
    		int red = (int) (Math.random()*256);
			int green = (int) (Math.random()*256);
			int blue = (int) (Math.random()*256);
    		Color color = new Color(red, green, blue);
    			for(Point point:region) {
    				image.setRGB((int) point.getX(), (int) point.getY(), color.getRGB());
    			}
    	}
    }
}
