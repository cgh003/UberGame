/**
 * DummyGrid
 */
public class DummyGrid implements GridInfo{
	
	public boolean claim(SharedCar car, Coord loc) {
		return true;
	}
	// Return true if SharedCar  successfully loaded rider 
	public boolean riderLoaded(SharedCar car){
		return false;
	}
	
	public static void main(String[] args) {
		GridSetup myMain = new GridSetup(args[0]);
		Grid grid = new Grid(myMain.getDimension());
//		DummyController ctrl = new DummyController(grid);
		
		EastWestController ctrl = new EastWestController(grid);
		Rider rider = new Rider();
		rider.setLocation(myMain.getRider());
		grid.addRider(rider);
		for(int i = 0; i < myMain.getObstacles().length; i++) {
			Obstacle obst = new Obstacle();
			obst.setLocation(myMain.getObstacles()[i]);
			grid.addObstacle(obst);
		}
		for(int i = 0; i < myMain.getRobocars().length; i++) {
			SharedCar car = new SharedCar(ctrl, grid);
			car.setLocation(myMain.getRobocars()[i]);
			grid.addCar(car);
			System.out.println(grid);
			System.out.println("---TESTING DRIVING---");
			car.newRider(rider.getLocation());
			for(int j = 0; j < 20; j++){
				
				car.drive();
				System.out.println(grid);
			}
		}

		
	}
}
