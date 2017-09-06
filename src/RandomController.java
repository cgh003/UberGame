import java.lang.Math;

/**
 * Random Controller
 */
public class RandomController extends CarController{
	private Coord turnDirectionToComplete;

	public RandomController(Grid info) {
		super(info);
	}
	
    
    public void setDefaultDirection() {
    	int random = (int)(Math.random() * 4 + 1);
    	if(random == 1){
    		direction = NORTH;
    	} else if (random == 2) {
    		direction = EAST;
    	} else if (random == 3) {
    		direction = SOUTH;
    	} else if (random == 4) {
    		direction = WEST;
    	}
    }
   
    // return the direction when roaming 
    public  Coord roam(Coord current) {
    	boolean check = true;
    	int random = (int)(Math.random() * 4 + 1); 
    	if (oracle.coordFree(current.add(direction))) {
    		return direction;
    	} else {
    		while (check) {
    			if(random == 1 && !direction.equals(NORTH) && oracle.coordFree(current.add(NORTH))) {
    				direction = NORTH;
    				check = false;
    			} else if (random == 2 && !direction.equals(EAST) && oracle.coordFree(current.add(EAST))) {
    				direction = EAST;
    				check = false;
    			} else if (random == 3 && !direction.equals(SOUTH) && oracle.coordFree(current.add(SOUTH))) {
    				direction = SOUTH;
    				check = false;
    			} else if (random == 4 && !direction.equals(WEST) && oracle.coordFree(current.add(WEST))) {
    				direction = WEST;
    				check = false;
    			}
    		}
    		return direction;
    	}
    }
    
    // return the direction when driving 
    public Coord drive (Coord current, Coord goal) {
    	if(turnDirectionToComplete != null){
	  		
    		direction = turnDirectionToComplete;
    		turnDirectionToComplete = null;
    		
    		return direction;
    	} 
    	int dist = current.diff(goal).col;
    	int distNS = current.diff(goal).row;
    	if(Math.abs(dist) > Math.abs(distNS)) {
    		if (dist > 0) {

    			if(current.add(WEST).equals(goal) || oracle.coordFree(current.add(WEST))){
    				direction = WEST;
    			}else{
				//TURN
    				if(distNS > 0 && oracle.coordFree(current.add(NORTH.add(WEST)))){
    					direction = NORTH;
    					turnDirectionToComplete = WEST;
    				}else if(oracle.coordFree(current.add(SOUTH.add(WEST)))){
    					direction = SOUTH;
    					turnDirectionToComplete = WEST;
    				}	
    			}
    		} else if (dist < 0) {
    			if (current.add(EAST).equals(goal) ||oracle.coordFree(current.add(EAST))) {
    				direction = EAST;
    			} else {
    				if(distNS > 0 && oracle.coordFree(current.add(NORTH.add(EAST)))){
    					direction = NORTH;
    					turnDirectionToComplete = EAST;
    				}else if(oracle.coordFree(current.add(SOUTH.add(EAST)))){
    					direction = SOUTH;
    					turnDirectionToComplete = EAST;
    				}

    			}
    		}
    	} else {
    		if (distNS > 0) {
        			
        			//If distance is > 0 then rider is N so go N
    			if(current.add(NORTH).equals(goal) ||oracle.coordFree(current.add(NORTH))){
    				direction = NORTH;
    			} else{
        				//TURN for obstacle in N dir
    				if(oracle.coordFree(current.add(WEST.add(NORTH)))){
    					direction = WEST;
    					turnDirectionToComplete = NORTH;
    				}else if(oracle.coordFree(current.add(EAST.add(NORTH)))){
    					direction = EAST;
    					turnDirectionToComplete = NORTH;
    				}
    				
    			}
    		} else if (distNS < 0) {
    			if (current.add(SOUTH).equals(goal) ||oracle.coordFree(current.add(SOUTH))) {
    				direction = SOUTH;
    			} else {
    				if(oracle.coordFree(current.add(WEST.add(SOUTH)))){
    					direction = WEST;
    					turnDirectionToComplete = SOUTH;
        					
    				}else if(oracle.coordFree(current.add(EAST.add(SOUTH)))){
    					direction = EAST;
    					turnDirectionToComplete = SOUTH;
    				}
        				
    			}
    		}
    	}
    	return direction;
    }
}
