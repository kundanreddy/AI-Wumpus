package wumpusworld;

/**
 * Contains starting code for creating your own Wumpus World agent. Currently
 * the agent only make a random decision each turn.
 * 
 * @author Johan HagelbÃ¤ck
 */
public class MyAgent implements Agent 
{
	private World World;
	int random;

	/**
	 * Creates a new instance of your solver agent.
	 * 
	 * @param world Current world state
	 */
	public MyAgent(World world) 
        {
		World = world;
	}

	public enum FSM 
        { // possible states for the FSM
		Safe, Gold, Stench, Breeze, Pit, Wumpus, DirectionUp, DirectionDown, DirectionRight, DirectionLeft
	}

	private FSM _state; // this represents the present state

	/**
	 * Asks the agent to execute a solution.
	 */

	public void performAction() 
        {
		// Location of the player
		int cX = World.getPlayerX();
		int cY = World.getPlayerY();
		// System.out.print(cX+"--"+cY);

		// initializing the state
		_state = FSM.Safe;

		// Basic action:
		// Grab Gold if we can.
		if (World.hasGlitter(cX, cY)) 
                {
			_state = FSM.Gold;
			World.doAction(World.GRAB);
			return;
		}

		// Basic action:
		// We are in a pit. Climb up.
		if (World.isInPit()) {
			_state = FSM.Pit;
			World.doAction(World.CLIMB);
			return;
		}

		// Test the environment
		if (World.hasBreeze(cX, cY)) {
			System.out.println("I am in a Breeze");
		}
		if (World.hasStench(cX, cY)) {
			System.out.println("I am in a Stench");
		}
		if (World.hasPit(cX, cY)) {
			System.out.println("I am in a Pit");
		}
		if (World.getDirection() == World.DIRECTION_RIGHT) {
			System.out.println("I am facing Right");
		}
		if (World.getDirection() == World.DIRECTION_LEFT) {
			System.out.println("I am facing Left");
		}
		if (World.getDirection() == World.DIRECTION_UP) {
			System.out.println("I am facing Up");
		}
		if (World.getDirection() == World.DIRECTION_DOWN) {
			System.out.println("I am facing Down");
		}

		if (World.hasStench(cX, cY)) 
                {
			_state = FSM.Stench;
		} else if (World.getDirection() == 0) {
			_state = FSM.DirectionUp;
		} else if (World.getDirection() == 1) {
			_state = FSM.DirectionRight;
		} else if (World.getDirection() == 2) {
			_state = FSM.DirectionDown;
		} else if (World.getDirection() == 3) {
			_state = FSM.DirectionLeft;
		} else if (World.hasBreeze(cX, cY)) {
			_state = FSM.Breeze;
		}

		switch (_state) {
		case Stench:
			inStench(cX, cY);
			System.out.println("stench");
			break;
		case Breeze:
			inBreeze(cX, cY);
			break;
		
                case DirectionLeft:
			directionLeftMove(cX, cY);
			break;
                case DirectionRight:
			directionRightMove(cX, cY);
			break;
		case DirectionDown:
			directionDownMove(cX, cY);
			break;
                case DirectionUp:
			directionUpMove(cX, cY);
			break;
		default:
			break;
		}
		return;
	}

	public void directionUpMove(int cX, int cY) 
        {

		if (World.hasBreeze(cX, cY)) 
                {
			if (World.hasBreeze(cX, cY - 1) && World.isVisited(cX, cY - 2)) 
                        {
				if (World.hasBreeze(cX, cY - 2)) 
                                {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
					return;
				}
				if (!World.hasBreeze(cX, cY - 2)) 
                                {
					World.doAction(World.TURN_LEFT);
					World.doAction(World.TURN_LEFT);
					World.doAction(World.MOVE);
					return;
				}
			}
		}
		if (World.hasBreeze(cX, cY - 1) && !World.isValidPosition(cX, cY - 2)) 
                {
			World.doAction(World.MOVE);
		}
		if (World.getDirection() == 0) {
			if (!World.isValidPosition(cX - 1, cY)) 
                        {
				if (World.isVisited(cX + 1, cY)) 
                                {
					World.doAction(World.MOVE);
					World.doAction(World.TURN_RIGHT);
				} else 
                                {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				}
			} else if (!World.isValidPosition(cX + 1, cY)) 
                        {
				if (World.isVisited(cX - 1, cY)) 
                                {
					World.doAction(World.MOVE);
					World.doAction(World.TURN_LEFT);
				} else {
					World.doAction(World.TURN_LEFT);
					World.doAction(World.MOVE);
				}
			} else {
				World.doAction(World.TURN_RIGHT);
			}
			return;
		}
	}
        public void directionDownMove(int cX, int cY) 
        {

		if (World.hasBreeze(cX, cY)) 
                {
			if (World.hasBreeze(cX, cY + 1) && World.isVisited(cX, cY - 1))
                        {
				World.doAction(World.MOVE);
				return;
			}
			if (World.hasBreeze(cX - 1, cY - 1) && World.hasBreeze(cX - 1, cY)) 
                        {
				World.doAction(World.TURN_LEFT);
				return;
			}
		}
		if (World.getDirection() == 2) 
                {
			if (World.isValidPosition(cX, cY - 1) && World.isUnknown(cX, cY - 1)) 
                        {
				World.doAction(World.MOVE);
				World.doAction(World.TURN_LEFT);
			} else if (World.isValidPosition(cX + 1, cY) && World.isUnknown(cX + 1, cY)) 
                        {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			} else if (World.isValidPosition(cX - 1, cY) && World.isUnknown(cX - 1, cY)) 
                        {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
			} else if (World.isValidPosition(cX + 1, cY)) 
                        {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			} else if (World.isValidPosition(cX - 1, cY)) 
                        {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
			} else {
				World.doAction(World.TURN_LEFT);
			}
		}
	}
        public void directionLeftMove(int cX, int cY) 
        {

		if (World.getDirection() == 3) 
                {
			if (World.isValidPosition(cX - 1, cY)) 
                        {
				if (World.isUnknown(cX - 1, cY))
					World.doAction(World.MOVE);
				else if (World.hasStench(cX + 1, cY) || World.hasBreeze(cX + 1, cY))
					World.doAction(World.MOVE);
				else if (World.isVisited(cX - 1, cY)) {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				}
			} else if (!(World.isValidPosition(cX - 1, cY) && World.isValidPosition(cX, cY + 1))
					&& World.isVisited(cX + 1, cY) && World.isUnknown(cX, cY - 1)) 
                        {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			} else if (!(World.isValidPosition(cX - 1, cY) && World.isValidPosition(cX, cY + 1))
					&& World.isVisited(cX, cY - 1) && World.isUnknown(cX + 1, cY)) 
                        {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
			} else if (World.isVisited(cX - 1, cY) && !World.isValidPosition(cX + 1, cY)
					&& World.isVisited(cX, cY - 1) && !World.isValidPosition(cX, cY + 1))
				World.doAction(World.MOVE);
			else {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
				World.doAction(World.TURN_RIGHT);
			}

			return;
		}
	}
        public void directionRightMove(int cX, int cY) 
        {

		if (World.hasBreeze(cX, cY)) 
                {
			if (World.hasBreeze(cX - 1, cY - 1) && !World.isValidPosition(cX - 2, cY - 1)) 
                        {
				World.doAction(World.MOVE);
			}
			if (!World.isValidPosition(cX + 1, cY)) 
                        {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			}
			if (World.hasBreeze(cX, cY - 1) && World.hasStench(cX, cY - 1)
					&& World.isUnknown(cX - 1, cY + 1)) {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
				return;
			}
			if (World.hasBreeze(cX, cY - 1) && World.hasStench(cX, cY - 1)
					&& World.isVisited(cX - 1, cY + 1)) {
				World.doAction(World.MOVE);
				return;
			}
			if (World.isValidPosition(cX + 1, cY) && World.isUnknown(cX + 1, cY)
					&& World.isValidPosition(cX + 2, cY) && World.isUnknown(cX + 2, cY)) 
                        {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			}
			if (World.isValidPosition(cX + 1, cY) && World.isUnknown(cX + 1, cY)
					&& !World.isValidPosition(cX + 2, cY)) {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			}

		}
		if (World.getDirection() == 1) 
                {
			if (World.isValidPosition(cX + 1, cY)) 
                        {
				if (World.isUnknown(cX + 1, cY))
					World.doAction(World.MOVE);
				else if (World.hasStench(cX - 1, cY) || World.hasBreeze(cX - 1, cY))
					World.doAction(World.MOVE);
				else if (World.isVisited(cX + 1, cY)) {
					World.doAction(World.TURN_LEFT);
					World.doAction(World.MOVE);
				}
			} else if (!(World.isValidPosition(cX + 1, cY) && World.isValidPosition(cX, cY + 1))
					&& World.isVisited(cX - 1, cY) && World.isUnknown(cX, cY - 1)) 
                        {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
			} else if (!(World.isValidPosition(cX + 1, cY) && World.isValidPosition(cX, cY + 1))
					&& World.isVisited(cX, cY - 1) && World.isUnknown(cX - 1, cY)) 
                        {
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.TURN_RIGHT);
				World.doAction(World.MOVE);
			} else if (!World.isValidPosition(cX - 1, cY) && World.isVisited(cX + 1, cY)
					&& World.isVisited(cX, cY - 1) && !World.isValidPosition(cX, cY + 1))
				World.doAction(World.MOVE);
			else {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
				World.doAction(World.TURN_LEFT);
			}

			return;
		}
	}
        public void inBreeze(int cX, int cY) {
	}

        public void inStench(int cX, int cY) 
        {

                        if (World.hasStench(cX, cY)) 
                        {
                                if (World.hasStench(cX - 1, cY - 1) && !World.hasArrow()) {
				if (World.getDirection() == 0)
					World.doAction(World.MOVE);
				else if (World.getDirection() == 3)
					World.doAction(World.TURN_RIGHT);
				else if (World.getDirection() == 2)
					World.doAction(World.TURN_RIGHT);
				else
					World.doAction(World.TURN_LEFT);
				return;
			}
			
                        if (World.hasStench(cX, cY + 2)) // Wumpus at cX, cY+1
			{
				if (World.getDirection() == 0 && World.hasArrow())
					World.doAction(World.SHOOT);
				else if (World.getDirection() == 1 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 2 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 0 && !World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (!World.hasArrow() && World.isValidPosition(cX - 1, cY))
					World.doAction(World.MOVE);
				else if (!World.hasArrow()) {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				} else
					World.doAction(World.TURN_RIGHT);
			}
                        
                        else if (World.hasStench(cX, cY - 2)) // Wumpus maybe at cX, cY-1
			{
				if (World.getDirection() == 0 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 1 && World.hasArrow())
					World.doAction(World.TURN_RIGHT);
				else if (World.getDirection() == 2 && World.hasArrow())
					World.doAction(World.SHOOT);
				else if (World.getDirection() == 2 && !World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (!World.hasArrow() && World.isValidPosition(cX, cY + 1))
					World.doAction(World.MOVE);
				else if (!World.hasArrow()) {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				} else
					World.doAction(World.TURN_LEFT);
			}
			
                        else if (World.hasStench(cX - 2, cY)) // Wumpus at cX-1, cY
			{
				if (World.getDirection() == 0 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 1 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 2 && World.hasArrow())
					World.doAction(World.TURN_RIGHT);
				else if (World.getDirection() == 3 && !World.hasArrow())
					World.doAction(World.TURN_RIGHT);
				else if (!World.hasArrow() && World.isValidPosition(cX, cY + 1))
					World.doAction(World.MOVE);
				else if (!World.hasArrow()) {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				} else
					World.doAction(World.SHOOT);
			}
                        
                        else if (World.hasStench(cX + 2, cY)) // Wumpus at cX+1, cY
			{
				if (World.getDirection() == 0 && World.hasArrow())
					World.doAction(World.TURN_RIGHT);
				else if (World.getDirection() == 1 && World.hasArrow())
					World.doAction(World.SHOOT);
				else if (World.getDirection() == 2 && World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 1 && !World.hasArrow())
					World.doAction(World.TURN_LEFT);
				else if (!World.hasArrow() && World.isValidPosition(cX, cY + 1))
					World.doAction(World.MOVE);
				else if (!World.hasArrow()) {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				} else
					World.doAction(World.TURN_LEFT);
			}

			

			else if (World.hasStench(cX + 1, cY + 1)) 
                        {
				if (World.isVisited(cX, cY + 1)) // Wumpus at cX+1, cY
				{
					if (World.getDirection() == 0)
						World.doAction(World.TURN_RIGHT);
					else if (World.getDirection() == 1)
						World.doAction(World.SHOOT);
					else if (World.getDirection() == 2)
						World.doAction(World.TURN_LEFT);
					else
						World.doAction(World.TURN_LEFT);
				}else if (World.isVisited(cX + 1, cY)) // Wumpus at cX, cY+1
				{
					if (World.getDirection() == 0)
						World.doAction(World.SHOOT);
					else if (World.getDirection() == 1)
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 2)
						World.doAction(World.TURN_LEFT);
					else
						World.doAction(World.TURN_RIGHT);
				}
			}

			else if (World.hasStench(cX - 1, cY + 1)) 
                        {
				if (World.isVisited(cX - 1, cY)) // Wumpus at cX, cY+1
				{
					if (World.getDirection() == 0)
						World.doAction(World.SHOOT);
					else if (World.getDirection() == 1)
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 2)
						World.doAction(World.TURN_LEFT);
					else
						World.doAction(World.TURN_RIGHT);
				}else if (World.isVisited(cX, cY + 1))// Wumpus at cX-1, cY 
				{
					if (World.getDirection() == 0 && World.hasArrow())
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 1 && World.hasArrow())
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 2 && World.hasArrow())
						World.doAction(World.TURN_RIGHT);
					else if (World.getDirection() == 3 && !World.hasArrow())
						World.doAction(World.TURN_RIGHT);
					else if (!World.hasArrow() && World.isValidPosition(cX, cY + 1))
						World.doAction(World.MOVE);
					else if (!World.hasArrow()) {
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.MOVE);
					} else
						World.doAction(World.SHOOT);
				}
			}
                        
                        else if (World.hasStench(cX + 1, cY - 1)) {
				if (World.isVisited(cX + 1, cY)) // Wumpus at cX, cY-1
				{
					if (World.getDirection() == 0)
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 1)
						World.doAction(World.TURN_RIGHT);
					else if (World.getDirection() == 2)
						World.doAction(World.SHOOT);
					else
						World.doAction(World.TURN_LEFT);
				}else if (World.isVisited(cX, cY - 1))// Wumpus at cX+1, cY
				{
					if (World.getDirection() == 0)
						World.doAction(World.TURN_RIGHT);
					else if (World.getDirection() == 1)
						World.doAction(World.SHOOT);
					else if (World.getDirection() == 2)
						World.doAction(World.TURN_LEFT);
					else
						World.doAction(World.TURN_LEFT);
				}
			}

			else if (World.hasStench(cX - 1, cY - 1)) 
                        {
				if (World.isVisited(cX, cY - 1)) // Wumpus at cX-1, cY
				{
					if (World.getDirection() == 0)
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 1)
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 2)
						World.doAction(World.TURN_RIGHT);
					else
						World.doAction(World.SHOOT);
				}else if (World.isVisited(cX - 1, cY))// Wumpus at cX, cY-1
				{
					if (World.getDirection() == 0 && World.hasArrow())
						World.doAction(World.TURN_LEFT);
					else if (World.getDirection() == 1 && World.hasArrow())
						World.doAction(World.TURN_RIGHT);
					else if (World.getDirection() == 2 && World.hasArrow())
						World.doAction(World.SHOOT);
					else if (World.getDirection() == 2 && !World.hasArrow())
						World.doAction(World.TURN_LEFT);
					else if (!World.hasArrow() && World.isValidPosition(cX, cY + 1))
						World.doAction(World.MOVE);
					else if (!World.hasArrow()) {
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.MOVE);
					} else
						World.doAction(World.TURN_LEFT);
				}
			}

			else if (!World.isValidPosition(cX - 1, cY) && !World.isValidPosition(cX, cY - 1)
					&& World.hasArrow()) {
				if (World.getDirection() == 1)
					World.doAction(World.SHOOT);
				else if (World.getDirection() == 3)
					World.doAction(World.TURN_LEFT);
				else if (World.getDirection() == 2)
					World.doAction(World.TURN_LEFT);
				else
					World.doAction(World.TURN_RIGHT);
			}
                        else if (World.isUnknown(cX, cY + 1) && World.isUnknown(cX + 1, cY)
					&& !World.hasArrow()) {
				World.doAction(World.MOVE);
			}
                        else if (World.getDirection() == 3) {
				if (!World.isValidPosition(cX - 1, cY)) {
					if ((!World.isValidPosition(cX, cY - 1) || World.isVisited(cX, cY - 1))
							&& (World.isVisited(cX + 1, cY)
									|| (World.isUnknown(cX + 1, cY) && World.isVisited(cX + 1, cY - 1)))) {
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.SHOOT);
					} else {
						World.doAction(World.TURN_LEFT);
						World.doAction(World.SHOOT);
					}
				} else if (!World.isValidPosition(cX + 1, cY) && World.isVisited(cX - 1, cY - 1))
					World.doAction(World.MOVE);
				else {
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.TURN_RIGHT);
					World.doAction(World.MOVE);
				}
			}

			else if (World.getDirection() == 1) {
				if (!World.isValidPosition(cX + 1, cY)) {
					if ((!World.isValidPosition(cX, cY - 1) || World.isVisited(cX, cY - 1))
							&& (World.isVisited(cX - 1, cY)
									|| (World.isUnknown(cX - 1, cY) && World.isVisited(cX - 1, cY - 1)))) {
						World.doAction(World.TURN_LEFT);
						World.doAction(World.SHOOT);
					} else {
						World.doAction(World.TURN_RIGHT);
						World.doAction(World.SHOOT);
					}
				} else if (!World.isValidPosition(cX - 1, cY) && World.isVisited(cX + 1, cY + 1))
					World.doAction(World.MOVE);
				else {
					World.doAction(World.TURN_LEFT);
					World.doAction(World.TURN_LEFT);
					World.doAction(World.MOVE);
				}
			}
			if (World.getDirection() == 0 && World.isInPit()) {
				World.doAction(World.TURN_LEFT);
				World.doAction(World.MOVE);
			}

			return;
		}

	}

}
