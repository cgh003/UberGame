/**
 * North/South controller
*/
public class NorthSouthController extends CarController {
	
	private Coord turnDirectionToComplete;

	public NorthSouthController(Grid info) {
		super(info);
		direction = NORTH;
	}
	
    
    public void setDefaultDirection() {
    	direction = NORTH;
    }
   
    // return the direction when roaming 
    public  Coord roam(Coord current) {
    	if (oracle.coordFree(current.add(direction))) {
    		return direction;
    		
    	} else if(direction == NORTH) {
    		current.add(direction);
    		direction = SOUTH;
    		return direction;
    	} else {
    		direction = NORTH;
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

    	int dist = current.diff(goal).row;
    	int distEW = current.diff(goal).col;
    	
 
    	if(dist != 0) {
    		if (dist > 0) {
 
    			//If distance is > 0 then rider to left, so go west
    			if(current.add(NORTH).equals(goal) || oracle.coordFree(current.add(NORTH))){
    				direction = NORTH;
    			}else{
    				//TURN
    				if(distEW > 0 && oracle.coordFree(current.add(WEST.add(NORTH)))){
    					direction = WEST;
    					turnDirectionToComplete = NORTH;
    				}else if(oracle.coordFree(current.add(EAST.add(NORTH)))){
    					direction = EAST;
    					turnDirectionToComplete = NORTH;
    				}
    			} 
    			
    		}else if (dist < 0) {
    			if (current.add(SOUTH).equals(goal) ||oracle.coordFree(current.add(SOUTH))) {
    				direction = SOUTH;
    			} else {
    				if(distEW < 0 && oracle.coordFree(current.add(WEST.add(SOUTH)))){
    					direction = WEST;
    					turnDirectionToComplete = SOUTH;
    				}else if(oracle.coordFree(current.add(EAST.add(SOUTH)))){
    					direction = EAST;
    					turnDirectionToComplete = SOUTH;
    				}

        		}
    		}
    	} else if (distEW != 0) {
    		if (distEW > 0) {
    			
    			//If distance is > 0 then rider is N so go N
    			if(current.add(WEST).equals(goal) ||oracle.coordFree(current.add(WEST))){
    				direction = WEST;
    			}else{
    				//TURN for obstacle in N dir
    				if(oracle.coordFree(current.add(NORTH.add(WEST)))){
    					direction = NORTH;
    					turnDirectionToComplete = WEST;
    				}else if(oracle.coordFree(current.add(SOUTH.add(WEST)))){
    					direction = SOUTH;
    					turnDirectionToComplete = WEST;
    				}
    				

    			}

    		} else if (distEW < 0) {
    			if (current.add(EAST).equals(goal) ||oracle.coordFree(current.add(EAST))) {
    				direction = EAST;
    			} else {
    				if(oracle.coordFree(current.add(NORTH.add(EAST)))){
    					direction = NORTH;
    					turnDirectionToComplete = EAST;
    					
    				}else if(oracle.coordFree(current.add(SOUTH.add(EAST)))){
    					direction = SOUTH;
    					turnDirectionToComplete = EAST;
    				}
    				
    			}
    		}
    		
    	}
    	
    	return direction;
    }
}
