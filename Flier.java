import java.util.*;
import java.awt.*;

/**
 * An animated object flying across the scene in a fixed direction
 * Sample solution to Lab 1, Dartmouth CS 10, Winter 2015
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class Flier extends Agent {

	private int xmax, ymax;	
	
	private double dx, dy;	// step size in x and y
	
	
    /* 
     * TODO: YOUR CODE HERE
     * 
     * The Flier class should have AT LEAST the following attributes 
     * in addition to what we've defined for you:
     *  - speed
     *  - direction of flight
     */
	
	
    private Universe universe;         // the universe that a flier exists within

    public Flier(Universe universe) {
        super(0,0);

        this.universe = universe;
        universe.addFlier(this);
        xmax = universe.getWidth();
        ymax = universe.getHeight();
        toss();		// need to call toss() to generate random x,y for flier
    }	

    /**
     * Overrides Agent.move() to step by dx, dy
     */
    public void move() {
        // TODO: MY CODE HERE
    	x += dx;
		y += dy;
    }

    /**
     * Detect hitting the region (and restart)
     */
    
    public void draw(Graphics g) {
    	g.setColor(Color.GREEN);
    	g.fillOval((int)(x-10), (int)(y-10), 20, 20);
    }
    
    
    public void checkWin() {
        // TODO: YOUR CODE HERE			
    	for ( ArrayList<Point> regionsMnt : universe.getRegions() ) {
    		for (Point pointMnt : regionsMnt) {
    			if (contains(pointMnt.x, pointMnt.y)){		// contain method compares whichever point in arraylist to the flier
    				System.out.println("Great catch!");
    				toss();
    			}
    		}
    	}	
    }
    
    /**
     * Detect exiting the window (and restart)
     */
    public void checkLose() {
        // TODO: MY CODE 
    	if (getX() < 0 || getX() > xmax || getY() < 0 || getY() > ymax) {  // if flier x or y is out of bound
    		System.out.println("Ooops STRIKE! You missed.");
    		toss();
    	}
    }

    /**
     * Puts the object at a random point on one of the four borders, 
     * flying in to the window at a random speed.
     */
    public void toss() {
        // TODO: MY CODE HERE
    	
    	int randnum = (int) (Math.random()*4);
    	int boundary = 5;		// set up at least 5 pixel distance from left/right wall for the start of flier lcoation
    	
    	switch(randnum) {
    		case 0:
    			System.out.println("Incoming flyer from the top!");
    			y = 0;
    			x = (int)(Math.random()*universe.getWidth()-boundary);
    			break;
    		case 1:
    			System.out.println("Incoming flyer from the bottom");
    			y = universe.getHeight()-1;
    			x = (int)(Math.random()*universe.getWidth()-boundary);
    			break;
    		case 2:
    			System.out.println("Incoming flyer from the left!");
    			x = 0;
    			y =  (int)(Math.random()*universe.getHeight());
    			break;
    		case 3:
    			System.out.println("Incoming flyer from the right!");
    			x = universe.getWidth();
    			y =  (int)(Math.random()*universe.getHeight());
    			break;
    			
    	}
    	
    	double randspeed = Math.random()*15;
    	
    	if (x==0){
    		dx = randspeed;
    	}
    	else if (x == universe.getWidth()-1){
    		dx = -randspeed;
    		
    	}
    	if (y==0){
    		dy = randspeed;
    	}
    	else if (y == universe.getHeight()-1) {
    		dy = -randspeed;
    	}
    	
    }
}
