import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Creates a simulation of Rydr with cars, obstacles, a rider, and a player. 
 */
public class Simulation extends JFrame implements Runnable, KeyListener, ChangeListener, ActionListener{
	private GraphicsGrid graphics;
	private GridSetup setup;
	private Grid myGrid;
	private TimeTick tracker;
	private int numRideManual = 0;
	private int numRideRobots = 0;
	private int speed = 100;
	private JFrame frame;
	private ManualController play;
	private SharedCar player;
	private Coord playerLoc;
	private SharedCar[] car;
	private Obstacle[] obst;
	private Rider rider;
	private JButton pause;
	private JButton newGame;
	private String[] parts;
	private JTextField title;
	private int numPickups = 0;
	private int numCars = 0;
	private JSlider slideIntoUrDMsLike;

	/**
	 * Constructor for simulation. Reads the command line and creates a simulation based off of the input.
	 * @param file command line arguments entered
	 */
	public Simulation(String[] file) {
		parts = file;
		if (parts.length == 2) {
			int row = Integer.parseInt(parts[1]);
			int col = Integer.parseInt(parts[2]);
			
			myGrid = new Grid(row, col);
		} else {
			setup = new GridSetup(parts[0]);
			myGrid = new Grid(setup.getDimension());
			
		}

		//set Manual Controller
		
		//COMMENT BACK IN LINE 231 TOO
		playerLoc = new Coord((myGrid.numRows)/2, (myGrid.numCols)/2);
		play = new ManualController(myGrid);
		player = new SharedCar(play, myGrid);
		player.setColor(Color.RED);
		player.setLocation(playerLoc);
		myGrid.addCar(player);
		
		//set GridObjects
		if (parts.length == 1) {
			rider = new Rider();
			rider.setLocation(setup.getRider());
			obst = new Obstacle[setup.getObstacles().length];
			for(int i = 0; i < setup.getObstacles().length; i++) {
				obst[i] = new Obstacle();
				obst[i].setLocation(setup.getObstacles()[i]);
				myGrid.addObstacle(obst[i]);
			}
			
			CarController ctrl;
			car = new SharedCar[setup.getRobocars().length];
			for(int i = 0; i < setup.getRobocars().length; i++) {
				switch (setup.getControllers()[i]) {
				
				case "east":
					ctrl = new EastWestController(myGrid);
					break;
				case "north": 
					ctrl = new NorthSouthController(myGrid);
					break;
				case "random":
					ctrl = new RandomController(myGrid);
					break;
				default:
					ctrl = new RandomController(myGrid);
					break;
				}
				car[i] = new SharedCar(ctrl, myGrid);
				car[i].setLocation(setup.getRobocars()[i]);
				car[i].newRider(rider.getLocation());
				myGrid.addCar(car[i]);
			}
		}
		//tracker = new TimeTick(100, myGrid, this);

	}		
		
	
	@Override
	/**
	 * Creates the GUI for the simulation and adds all the GridObjects to the GraphicGrid.
	 */
	public void run() {
		
		//graphics set-up
		frame = new JFrame();
		frame.addKeyListener(this);
		frame.addKeyListener(play);
		frame.setSize(800, 700);
		frame.setTitle("Rydr");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		title = new JTextField("Riders Loaded Player: " + numRideManual + " Robots: " + numRideRobots);
		title.setEditable(false);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		title.setHorizontalAlignment(JTextField.CENTER);
		graphics = new GraphicsGrid(myGrid.numRows, myGrid.numCols, 10);
		JPanel subPanel = new JPanel();
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		pause = new JButton("Pause");
		pause.addActionListener(this);
		JPanel panel = new JPanel(new BorderLayout());
		subPanel.add(newGame);
		subPanel.add(pause);
		panel.add(graphics, BorderLayout.CENTER);
		slideIntoUrDMsLike = new JSlider(10,100,100);
		slideIntoUrDMsLike.setInverted(true);
		slideIntoUrDMsLike.addChangeListener(this);
		subPanel.add(slideIntoUrDMsLike);
		panel.add(title, BorderLayout.NORTH);
		panel.add(subPanel, BorderLayout.SOUTH);
		frame.add(panel);
		graphics.addGridObject(player);
		graphics.addGridObject(rider);
		graphics.addGridObject(car);
		graphics.addGridObject(obst);
		frame.setVisible(true);
		
		
	}

	/**
	 * Tells the TimeTicker instance to change the number of ticks in accordance to the speed.
	 */
	public void speedSlider() {
		
		tracker.setTicks(speed);
		
	}
	
	/**
	 * Updates the GraphicGrid based on what the simulation has been running.
	 */
	public void update() {
		frame.requestFocus();
		if (tracker.paused()) {
			return;
		}

		
		for(int i = 0; i < myGrid.cars.size(); i++) {
			if(myGrid.cars.get(i).getLocation().equals(rider.getLocation())) {
				myGrid.riderLoaded(myGrid.cars.get(i));
				graphics.removeGridObject(rider);
				if(myGrid.cars.get(i) == player) {
					numRideManual++;
					numPickups++;
					title.setText("Riders Loaded Player: " + numRideManual + " Robots: " + numRideRobots);
				} else {
					numPickups++;
					numRideRobots++;
					title.setText("Riders Loaded Player: " + numRideManual + " Robots: " + numRideRobots);
				}
				Rider rider2 = new Rider();
				rider = rider2;
				rider.setLocation(newCoord());
				myGrid.addRider(rider);
				graphics.addGridObject(rider);
				
				
				if (numPickups % 10 == 0 && numCars <= 20) {
					
					Obstacle newO = new Obstacle();
					newO.setLocation(newCoord());
					myGrid.addObstacle(newO);
					graphics.addGridObject(newO);
					
					CarController newCtrl = newController();
					SharedCar newCar = new SharedCar(newCtrl, myGrid);
					numCars++;
					newCar.setLocation(newCoord());
					newCar.newRider(rider.getLocation());
					myGrid.addCar(newCar);
					graphics.addGridObject(newCar);
					
					if (speed - 10 > 10) {
						speed = speed - 10;
						tracker.setTicks(speed);
						slideIntoUrDMsLike.setValue(speed);
					}
					if (numPickups / 10 <= 11 ) {
						slideIntoUrDMsLike.setExtent(numPickups - 10);
					}

				}
				
				for(int j = 0; j < myGrid.cars.size(); j++) {
					myGrid.cars.get(j).newRider(rider.getLocation());	
				}
				
				break;
			}
		}
		
		graphics.repaint();		
	}
	
	/**
	 * Method to create a new game (start over). Resets all objects to their original positions and clears the grid.
	 */
	public void newGame() {

		graphics.clearObjects();
		if (!(tracker == null)) {
			tracker.stop();
		}
		numRideRobots = 0; 
		numRideManual = 0;
		numPickups = 0;
		title.setText("Riders Loaded Player: " + numRideManual + " Robots: " + numRideRobots);
		myGrid = new Grid(setup.getDimension());
		
		if (parts.length == 1) {
			rider = new Rider();
			rider.setLocation(setup.getRider());
			myGrid.addRider(rider);
			obst = new Obstacle[setup.getObstacles().length];
			for(int i = 0; i < setup.getObstacles().length; i++) {
				obst[i] = new Obstacle();
				obst[i].setLocation(setup.getObstacles()[i]);
				myGrid.addObstacle(obst[i]);
			}
			
			CarController ctrl;
			car = new SharedCar[setup.getRobocars().length];
			for(int i = 0; i < setup.getRobocars().length; i++) {
				switch (setup.getControllers()[i]) {
				
				case "east":
					ctrl = new EastWestController(myGrid);
					break;
				case "north": 
					ctrl = new NorthSouthController(myGrid);
					break;
				case "random":
					ctrl = new RandomController(myGrid);
					break;
				default:
					ctrl = new RandomController(myGrid);
					break;
				}
				car[i] = new SharedCar(ctrl, myGrid);
				car[i].setLocation(setup.getRobocars()[i]);
				car[i].newRider(rider.getLocation());
				myGrid.addCar(car[i]);
			}
		}
		player = new SharedCar(play, myGrid);
		player.setColor(Color.RED);
		player.setLocation(playerLoc);
		myGrid.addCar(player);
		
		graphics.addGridObject(rider);
		graphics.addGridObject(player);
		graphics.addGridObject(car);
		graphics.addGridObject(obst);
		tracker = new TimeTick(speed, myGrid, this);
		slideIntoUrDMsLike.setExtent(0);
		Thread t = new Thread(tracker);
		t.start(); //call the run method of the object;
		frame.requestFocus();

	}
	
	/**
	 * Method for the pause button. If the button was clicked, pause the simulation and set the button to say "Resume." 
	 * If clicked again, unpause the simulation and set the button to say "Pause"
	 */
	public void pause() {
		if (tracker.paused()) {
			tracker.changeState();
			pause.setText("Pause");
		}
		else {
			tracker.changeState();
			pause.setText("Resume");
		}
	
	}
	

	@Override
	/**
	 * Tells the simulation what to do after a key has been pressed. 
	 * Spacebar = accelerate the player in the direction they were going in; right arrow = turn right, left arrow = turn left.
	 * @param e the KeyEvent that was invoked
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_SPACE) {
			player.drive();
			update();
		} else if((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_RIGHT)) {
			update();
		}
	}

	@Override
	/**
	 * Tells the simulation what to do when a key has been released.
	 * @param e the KeyEvent that was invoked
	 */
	public void keyReleased(KeyEvent e) {
		return;		
	}

	@Override
	/**
	 * Tells the simulation what to do when a certain key has been typed.
	 * @param e the KeyEvent that was invoked
	 */
	public void keyTyped(KeyEvent e) {
		return;		
	}

	@Override
	/**
	 * Adjusts the speed of the simulation if the slider has been moved by the user.
	 * @param arg0 the ChangeEvent that was invoked
	 */
	public void stateChanged(ChangeEvent arg0) {
		JSlider source = (JSlider)arg0.getSource();
		if (!source.getValueIsAdjusting()) {
			speed = (int)source.getValue();
			speedSlider();
		}
	}
	
	
	@Override
	/**
	 * Checks to see if the New Game or the Pause/Resume button was pressed, and tells the simulation what to do if they were pressed.
	 * @param e ActionEvent that was invoked
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Pause") || e.getActionCommand().equals("Resume")){
			pause();
		} else if(e.getActionCommand().equals("New Game")) {
			newGame();
		}
	}

	/**
	 * Helper method that generates a random kind of controller.
	 * @return a random CarController object
	 */
	public CarController newController() {
		int carControl = (int)(Math.random() * 3 + 1);
		switch (carControl) {
		case 1: 
			return new EastWestController(myGrid);
			
		case 2:
			return new NorthSouthController(myGrid);
			
		default:
			return new RandomController(myGrid);
			
		}
	}
	
	/**
	 * Helper method that generates a random coordinate.
	 * @return randomly generated new coordinate.
	 */
	public Coord newCoord() {
		int newRow = (int)(Math.random() * myGrid.numRows);
		int newCol = (int)(Math.random() * myGrid.numCols);
		Coord newCoords = new Coord(newRow, newCol);
		if(myGrid.coordFree(newCoords)) {
			return newCoords;
		} else {
			return newCoord();
		}
	}
	
	/**
	 * Main method; creates a simulation and runs it.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Simulation mySim = new Simulation(args);
		SwingUtilities.invokeLater(mySim);
		
	}




}
