package wumpusworld;

/**
 * This class handles an instance of the Wumpus World. It contains the world
 * state, which actions are available, and updates the world when an action has
 * been executed.
 * 
 * @author Akki Reddy
 */
public class World {
	private int size;
	private String[][] world;
	private int pX = 1;
	private int pY = 1;
	private boolean wumpusAliveFlag = true;
	private boolean hasArrowFlag = true;
	private boolean isInPitFlag = false;
	private boolean hasGoldFlag = false;
	private boolean gameOverFlag = false;
	private int totalScore = 0;

	// Player Directions constants.
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_LEFT = 3;

	// Start direction
	private int dir = DIRECTION_RIGHT;

	// Percepts constants.
	public static final String CONSTANT_BREEZE = "B";
	public static final String CONSTANT_STENCH = "S";
	public static final String CONSTANT_PIT = "P";
	public static final String CONSTANT_WUMPUS = "W";
	public static final String CONSTANT_GLITTER = "G";
	public static final String CONSTANT_GOLD = "T";
	public static final String CONSTANT_UNKNOWN = "U";

	// Actions constants.

	public static final String MOVE = "m";
	public static final String GRAB = "g";
	public static final String CLIMB = "c";
	public static final String SHOOT = "s";
	public static final String TURN_LEFT = "l";
	public static final String TURN_RIGHT = "r";

	/**
	 * Creates a new Wumpus World. The Wumpus World works with any size 4 or larger,
	 * but only size 4 is supported by the GUI.
	 * 
	 * @param size Size of the world.
	 */
	public World(int size) {
		this.size = size;
		world = new String[size + 1][size + 1];

		for (int x = 0; x <= size; x++) {
			for (int y = 0; y <= size; y++) {
				world[x][y] = CONSTANT_UNKNOWN;
			}
		}

		setVisited(1, 1);
	}

	public World(String[][] world, int size, int px, int py, int dir) {
		this.size = size;
		this.pX = px;
		this.pY = py;
		this.dir = dir;
		this.world = world;
	}

	/**
	 * Returns the current score.
	 * 
	 * @return The score.
	 */
	public int getScore() {
		return totalScore;
	}

	/**
	 * Returns the size of this Wumpus World.
	 * 
	 * @return The size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Checks if the game has ended or not.
	 * 
	 * @return True if game is over, false if not.
	 */
	public boolean gameOver() {
		return gameOverFlag;
	}

	/**
	 * Returns player X position.
	 * 
	 * @return X position.
	 */
	public int getPlayerX() {
		return pX;
	}

	/**
	 * Returns player Y position.
	 * 
	 * @return Y position.
	 */
	public int getPlayerY() {
		return pY;
	}

	/**
	 * Checks if the player is in a pit and needs to climb up.
	 * 
	 * @return True if in a pit, false otherwise.
	 */
	public boolean isInPit() {
		return isInPitFlag;
	}

	/**
	 * Checks if the player has the arrow left.
	 * 
	 * @return True if player has the arrow, false otherwise.
	 */
	public boolean hasArrow() {
		return hasArrowFlag;
	}

	/**
	 * Checks if the Wumpus is alive.
	 * 
	 * @return True if Wumpus is alive, false otherwise.
	 */
	public boolean wumpusAlive() {
		return wumpusAliveFlag;
	}

	/**
	 * Checks if the player carries the gold treasure.
	 * 
	 * @return True if player has the gold, false otherwise.
	 */
	public boolean hasGold() {
		return hasGoldFlag;
	}

	/**
	 * Returns the current direction of the player.
	 * 
	 * @return Direction (see direction constants)
	 */
	public int getDirection() {
		return dir;
	}

	/**
	 * Checks if a square has a breeze. Returns false if the position is invalid, or
	 * if the square is unknown.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square has a breeze
	 */
	public boolean hasBreeze(int x, int y) {
		if (!isValidPosition(x, y))
			return false;
		if (isUnknown(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_BREEZE))
			return true;
		else
			return false;
	}

	/**
	 * Checks if a square has a stench. Returns false if the position is invalid, or
	 * if the square is unknown.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square has a stench
	 */
	public boolean hasStench(int x, int y) {
		if (!isValidPosition(x, y))
			return false;
		if (isUnknown(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_STENCH))
			return true;
		else
			return false;
	}

	/**
	 * Checks if a square has glitter. Returns false if the position is invalid, or
	 * if the square is unknown.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square has glitter
	 */
	public boolean hasGlitter(int x, int y) {
		if (!isValidPosition(x, y))
			return false;
		if (isUnknown(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_GLITTER))
			return true;
		else
			return false;
	}

	/**
	 * Checks if a square has a pit. Returns false if the position is invalid, or if
	 * the square is unknown.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square has a pit
	 */
	public boolean hasPit(int x, int y) {
		if (!isValidPosition(x, y))
			return false;
		if (isUnknown(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_PIT))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the Wumpus is in a square. Returns false if the position is
	 * invalid, or if the square is unknown.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the Wumpus is in the square
	 */
	public boolean hasWumpus(int x, int y) {
		if (!isValidPosition(x, y))
			return false;
		if (isUnknown(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_WUMPUS))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the player is in a square.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the player is in the square
	 */
	public boolean hasPlayer(int x, int y) {
		if (pX == x && pY == y) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a square is visited. Returns false if the position is invalid.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square is visited
	 */
	public boolean isVisited(int x, int y) {
		if (!isValidPosition(x, y))
			return false;

		return !isUnknown(x, y);
	}

	/**
	 * Checks if a square is unknown. Returns false if the position is invalid.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square is unknown
	 */
	public boolean isUnknown(int x, int y) {
		if (!isValidPosition(x, y))
			return false;

		if (world[x][y].contains(CONSTANT_UNKNOWN))
			return true;
		else
			return false;
	}

	/**
	 * Checks if a square is valid, i.e. inside the bounds of the game world.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @return True if the square is valid
	 */
	public boolean isValidPosition(int x, int y) {
		if (x < 1)
			return false;
		if (y < 1)
			return false;
		if (x > size)
			return false;
		if (y > size)
			return false;
		return true;
	}

	/**
	 * Adds a percept to a square.
	 * 
	 * @param x X position
	 * @param y Y position
	 * @param s Percept to add (see Percept constants)
	 */
	private void append(int x, int y, String s) {
		if (!isValidPosition(x, y))
			return;

		if (!world[x][y].contains(s)) {
			world[x][y] += s;
		}
	}

	/**
	 * Adds the Wumpus to a square.
	 * 
	 * @param x X position
	 * @param y Y position
	 */
	public void addWumpus(int x, int y) {
		if (!world[x][y].contains(CONSTANT_WUMPUS)) {
			append(x, y, CONSTANT_WUMPUS);
			append(x - 1, y, CONSTANT_STENCH);
			append(x + 1, y, CONSTANT_STENCH);
			append(x, y - 1, CONSTANT_STENCH);
			append(x, y + 1, CONSTANT_STENCH);
		}
	}

	public World cloneWorld() {
		// this.size = size;
		String[][] w1 = new String[size + 1][size + 1];
		int px, py, dirr;
		px = pX;
		py = pY;
		dirr = dir;
		for (int x = 0; x <= size; x++) {
			for (int y = 0; y <= size; y++) {
				w1[x][y] = world[x][y];
			}
		}

		return new World(w1, size, px, py, dirr);
	}

	/**
	 * Adds a pit to a square.
	 * 
	 * @param x X position
	 * @param y Y position
	 */
	public void addPit(int x, int y) {
		if (!world[x][y].contains(CONSTANT_PIT)) {
			append(x, y, CONSTANT_PIT);
			append(x - 1, y, CONSTANT_BREEZE);
			append(x + 1, y, CONSTANT_BREEZE);
			append(x, y - 1, CONSTANT_BREEZE);
			append(x, y + 1, CONSTANT_BREEZE);
		}
	}

	/**
	 * Adds the gold treasure to a square.
	 * 
	 * @param x X position
	 * @param y Y position
	 */
	public void addGold(int x, int y) {
		if (!world[x][y].contains(CONSTANT_GLITTER)) {
			append(x, y, CONSTANT_GLITTER);
		}
	}

	/**
	 * Sets that a square has been visited.
	 * 
	 * @param x X position
	 * @param y Y position
	 */
	private void setVisited(int x, int y) {
		if (world[x][y].contains(CONSTANT_UNKNOWN)) {
			world[x][y] = world[x][y].replaceAll(CONSTANT_UNKNOWN, "");
		}
	}

	/**
	 * Executes an action in the Wumpus World.
	 * 
	 * @param a Action string (see Action constants)
	 * @return True if the action was successful, false if action failed.
	 */
	public boolean doAction(String a) {
		if (gameOverFlag)
			return false;

		// Each action costs 1 score
		totalScore -= 1;

		if (a.equals(MOVE)) {
			if (!isInPitFlag) {
				if (dir == DIRECTION_LEFT)
					return move(pX - 1, pY);
				if (dir == DIRECTION_RIGHT)
					return move(pX + 1, pY);
				if (dir == DIRECTION_UP)
					return move(pX, pY + 1);
				if (dir == DIRECTION_DOWN)
					return move(pX, pY - 1);
			}
		}
		if (a.equals(TURN_LEFT)) {
			dir--;
			if (dir < 0)
				dir = 3;
			return true;
		}
		if (a.equals(TURN_RIGHT)) {
			dir++;
			if (dir > 3)
				dir = 0;
			return true;
		}
		if (a.equals(GRAB)) {
			if (hasGlitter(pX, pY)) {
				world[pX][pY] = world[pX][pY].replaceAll(CONSTANT_GLITTER, "");
				totalScore += 1000;
				hasGoldFlag = true;
				gameOverFlag = true;
				return true;
			}
		}
		if (a.equals(SHOOT)) {
			if (hasArrowFlag) {
				totalScore -= 10;
				hasArrowFlag = false;
				shoot();
				return true;
			}
		}
		if (a.equals(CLIMB)) {
			isInPitFlag = false;
		}

		// Action failed
		return false;
	}

	/**
	 * Checks if the Wumpus has been hit by the arrow.
	 */
	private void shoot() {
		if (dir == DIRECTION_RIGHT) {
			for (int x = pX; x <= size; x++) {
				if (world[x][pY].contains(CONSTANT_WUMPUS))
					removeWumpus();
			}
		}
		if (dir == DIRECTION_LEFT) {
			for (int x = pX; x >= 0; x--) {
				if (world[x][pY].contains(CONSTANT_WUMPUS))
					removeWumpus();
			}
		}
		if (dir == DIRECTION_UP) {
			for (int y = pY; y <= size; y++) {
				if (world[pX][y].contains(CONSTANT_WUMPUS))
					removeWumpus();
			}
		}
		if (dir == DIRECTION_DOWN) {
			for (int y = pY; y >= 0; y--) {
				if (world[pX][y].contains(CONSTANT_WUMPUS))
					removeWumpus();
			}
		}
	}

	/**
	 * Removes the Wumpus (and Stench) from the Wumpus World. Used when the Wumpus
	 * has been hit by the arrow.
	 */
	private void removeWumpus() {
		for (int x = 1; x <= 4; x++) {
			for (int y = 1; y <= 4; y++) {
				world[x][y] = world[x][y].replaceAll(CONSTANT_WUMPUS, "");
				world[x][y] = world[x][y].replaceAll(CONSTANT_STENCH, "");
			}
		}

		wumpusAliveFlag = false;
	}

	/**
	 * Executes a move forward to a new square.
	 * 
	 * @param nX New X position
	 * @param nY New Y position
	 * @return True if the move actions was successful, false otherwise
	 */
	private boolean move(int nX, int nY) {
		// Check if valid
		if (!isValidPosition(nX, nY)) {
			return false;
		}

		pX = nX;
		pY = nY;

		setVisited(pX, pY);

		if (hasWumpus(pX, pY)) {
			totalScore -= 1000;
			gameOverFlag = true;
		}
		if (hasPit(pX, pY)) {
			totalScore -= 1000;
			isInPitFlag = true;
		}

		return true;
	}
}
