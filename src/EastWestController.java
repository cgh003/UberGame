/**
 * East/West Controller
 */
public class EastWestController extends CarController{
	private Coord turnDirectionToComplete;
	
	public EastWestController(Grid info) {
		super(info);
	}
	
    
    public void setDefaultDirection() {
    	direction = WEST;
    }
   
    // return the direction when roaming 
    public  Coord roam(Coord current) {
    	if (oracle.coordFree(current.add(direction))) {
    		return direction;
    		
    	} else if(direction == EAST) {
    		current.add(direction);
    		direction = WEST;
    		return direction;
    	} else {
    		direction = EAST;
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
    	
 
    	if(dist != 0) {
    		//East/West
    		if (dist > 0) {
 
    			//If distance is > 0 then rider to left, so go west
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
    			
    		}else if (dist < 0) {
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
    	} else if (distNS != 0) {
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
