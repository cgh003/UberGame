import java.util.ArrayList;

/**
 * The Grid class; where the state of the simulation is kept.
 */
public class Grid implements CoordInfo, GridInfo {
	public final int numRows;
	public final int numCols;
	private Rider rider = new Rider();  
	private GridObject[][] gridArray;
	public ArrayList<SharedCar> cars;
	
	/**
	 * Constructs a grid with zero columns and zero rows.
	 */
	public Grid() {
		numRows = 0;
		numCols = 0;
		gridArray = new GridObject[numRows][numCols];
	}
	
	/**
	 * Constructs a grid with the specified dimensions
	 * @param row the number of rows in the grid
	 * @param col the number of columns in the grid
	 */
	public Grid(int row, int col) {
		this.numRows = row;
		this.numCols = col;
		gridArray = new GridObject[numRows][numCols];
		cars = new ArrayList<SharedCar>();
	}
	
	/**
	 * Constructs a grid based on the dimensions of a coordinate
	 * @param dim Coord object that specifies the number of rows and columns this grid has.
	 */
	public Grid(Coord dim) {
		this.numRows = dim.row;
		this.numCols = dim.col;
		gridArray = new GridObject[numRows][numCols];
		cars = new ArrayList<SharedCar>();
	}
	
	/**
	 * Checks to see if the grid is able to add the rider, adds it if it is able to, and returns true/false.
	 * @param r Rider object to be added to the grid.
	 * @return true if the rider is added, false otherwise.
	 */
	public boolean addRider(Rider r) {
		if (r.getLocation().row >= 0 && r.getLocation().row < numRows) {
			if(r.getLocation().col >= 0 && r.getLocation().col < numCols) {
				if(coordFree(r.getLocation())) {
					gridArray[r.getLocation().row][r.getLocation().col] = r;
					rider.setLocation(r.getLocation());
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if the grid is able to add the car, adds it if it is able to, and returns true/false.
	 * @param car SharedCar object to be added to the grid.
	 * @return true if the car is added, false otherwise.
	 */
	public boolean addCar(SharedCar car) {
		if (car.getLocation().row >= 0 && car.getLocation().row < numRows) {
			if(car.getLocation().col >= 0 && car.getLocation().col < numCols) {
				if(coordFree(car.getLocation())) {
					gridArray[car.getLocation().row][car.getLocation().col] = car;
					cars.add(car);
					if(addRider(rider)) {
						car.newRider(rider.getLocation());
					}
					return true;					
				}

			}
		}
		return false;
	}
	
	/**
	 * Checks to see if the grid is able to add the obstacle, adds it if it is able to, and returns true/false.
	 * @param obst Obstacle object to be added to the grid.
	 * @return true if the car is added, false otherwise.
	 */
	public boolean addObstacle(Obstacle obst) {
		if (obst.getLocation().row >= 0 && obst.getLocation().row < numRows) {
			if(obst.getLocation().col >= 0 && obst.getLocation().col < numCols) {
				gridArray[obst.getLocation().row][obst.getLocation().col] = obst;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tells each SharedCar in the grid to move one step.
	 */
	public void drive() {
		for(int i = 0; i < cars.size(); i++) {
			cars.get(i).drive();
			
		}
		
	}
	
	/**
	 * Checks to see if the car is able to claim the location specified, also checks if the location to be claimed has a rider in it, 
	 * and loads the rider into the car if yes.
	 * @param car the SharedCar that we want to move.
	 * @param loc the coordinate that we wish to move the car to.
	 * @return true if the coordinate can be claimed, false otherwise.
	 */
	public boolean claim(SharedCar car, Coord loc) {
		if (loc.row >= 0 && loc.row < numRows) {
			if(loc.col >= 0 && loc.col < numCols) {
				 if(coordFree(loc)) {
					 gridArray[car.getLocation().row][car.getLocation().col] = null;
					 gridArray[loc.row][loc.col] = car;
					 return true;
				 } else if (loc.equals(rider.getLocation())) {
					 gridArray[car.getLocation().row][car.getLocation().col] = null;
					 gridArray[loc.row][loc.col] = null;
					 gridArray[loc.row][loc.col] = car;
					 riderLoaded(car);
					 return true;
				 } 
			}
		}
		return false;
	}		
	
	/**
	 * Checks to see if the rider is loaded into the car.
	 * @param car the SharedCar that the rider wants to be loaded into.
	 * @return true if/when the rider is loaded, false otherwise
	 */
	public boolean riderLoaded(SharedCar car) {
		if(rider.waiting()) {
			rider.pickUp(car);
			for(int i = 0; i < cars.size(); i++) {
				cars.get(i).roam();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if the coordinate is free (i.e. empty).
	 * @param loc the coordinate that we want to check.
	 * @return true if the coordinate is empty, false otherwise.
	 */
	public boolean coordFree (Coord loc) {
		if(loc.col >= 0 && loc.col < numCols) {
			if(loc.row >= 0 && loc.row < numRows) {
				if (gridArray[loc.row][loc.col] == null) { 
					
					return true;
				} 
			}
		} 
		return false;
	}
	
	/**
	 * Calls the drive method.
	 */
	public void update() {
		drive();
	}
	
	/**
	 * Prints out the current grid in the form of a string.
	 * @return string that represents our grid.
	 */
	public String toString() {
		for(int i = 0; i < numRows+2; i++) {
			for(int j = 0; j < numCols+2; j++) {
				if((i == 0) || (i == (numRows +1))) {
					System.out.print("=");
				} else if ((j == 0) || j == (numCols +1)) {
					System.out.print("|");
				} else if(gridArray[i-1][j-1] == null) {
					System.out.print(" ");
				} else {
					System.out.print(gridArray[i-1][j-1].getSymbol());
				}
			}
			System.out.println();
		}
		return "";
	}


}
