import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Manual Controller
 */
public class ManualController extends CarController implements KeyListener{
	
	private Coord origin;

	public ManualController(Grid oracle) {
		super(oracle);
		direction = NORTH;
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		int key = arg0.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			if (direction == NORTH) {
				direction = WEST;
			} else if(direction == WEST) {
				direction = SOUTH;
			} else if(direction == SOUTH) {
				direction = EAST;
			} else {
				direction = NORTH;
			}
		} else if(key == KeyEvent.VK_RIGHT) {
			if (direction == NORTH) {
				direction = EAST;
			} else if(direction == EAST) {
				direction = SOUTH;
			} else if(direction == SOUTH) {
				direction = WEST;
			} else {
				direction = NORTH;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		return;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		return;
	}

	@Override
	public void setDefaultDirection() {
		return;
	}

	@Override
	public Coord roam(Coord current) {
		return direction;
	}

	@Override
	public Coord drive(Coord current, Coord goal) {
		return direction;
	}

	
}
