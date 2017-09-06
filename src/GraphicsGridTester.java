import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GraphicsGridTester extends JFrame {
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setSize(500,500);
        	frame.setTitle("Graphics grid tester!");
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.setVisible(true);

		JButton drive = new JButton("Drive");
		GridSetup myMain = new GridSetup(args[0]);

		GraphicsGrid grid = new GraphicsGrid(myMain.getDimension().row, myMain.getDimension().col, 20);
		grid.setLayout(null);
		drive.setBounds(50, 450, 75,20);
		frame.add(grid);
		grid.add(drive);
//		Obstacle o1 = new Obstacle();
//		Coord o1Coor = new Coord(5,4);
//		o1.setLocation(o1Coor);
//		Obstacle o2 = new Obstacle();
//		grid.addGridObject(o2);
//		grid.addGridObject(o1);	
//		Obstacle[] obstacles = new Obstacle[5];
//		for (int i = 0; i < obstacles.length; i++) {
//			Coord location = new Coord(i,i+3);
//			obstacles[i] = new Obstacle();
//			obstacles[i].setLocation(location);
//		}
		Grid grids = new Grid(20,20);
//		DummyController ctrl = new DummyController(grid);
		
		EastWestController ctrl = new EastWestController(grids);
		Rider rider = new Rider();
		rider.setLocation(myMain.getRider());
		grid.addGridObject(rider);
		for(int i = 0; i < myMain.getObstacles().length; i++) {
			Obstacle obst = new Obstacle();
			obst.setLocation(myMain.getObstacles()[i]);
			grid.addGridObject(obst);
		}
		for(int i = 0; i < myMain.getRobocars().length; i++) {
			SharedCar car = new SharedCar(ctrl, grids);
			car.setLocation(myMain.getRobocars()[i]);
			grid.addGridObject(car);
		}
	}
}
