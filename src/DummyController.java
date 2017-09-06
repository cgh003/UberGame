
public class DummyController extends CarController {

	
	public DummyController(Grid object)
    {
        super(object);
    }
    
	public void setDefaultDirection() {
		direction = SOUTH; 
	}
	   
    // return the direction when roaming 
    public Coord roam(Coord current) {
    	return SOUTH;
    }

    // return the direction when driving 
    public Coord drive(Coord current, Coord goal) {
    	return NORTH;
    }
}
