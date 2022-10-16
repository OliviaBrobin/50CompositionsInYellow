package game;

import java.util.LinkedList;

public class Player{

	private boolean startJump = false;

	private boolean isFrozen = false;

	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isMovingUp = false;
	private boolean isMovingDown = false;

	public boolean startTeleportLeft = false;
	public boolean startTeleportRight = false;
	public boolean startTeleportUp = false;
	public boolean startTeleportDown = false;

	private double x;
	private double y;

	static private double MAX_HORIZONTAL_SPEED = .0248;
	static private double verticalSpeed = .015;

	static public boolean holdingJump = false;
	static public int holdingJumpTime = -1;
	boolean startedDragCountdown = false;
	boolean dragStarted = false;
	int dragCountdown = -1;

	static private double jumpHeight = 3;
	static private double horizontalDistanceToJumpHeight = 3;

	static private double horizontalSpeed = 0;

	/*
	static private double horizontalSpeed = .035;
	private double jumpHeight = 4;
	private double horizontalDistanceToJumpHeight = 3;
	 */

	static private double gravity = (-2 * jumpHeight) * Math.pow(MAX_HORIZONTAL_SPEED, 2) / Math.pow(horizontalDistanceToJumpHeight, 2);


	private double yVelocity = 0;


	static private double xLength = .5;
	static private double yLength = .5;


	/*
	static private double xLength = 1;
	static private double yLength = 1;
	 */

	public Player()
	{
		this.x = 0;
		this.y = 0;
	}

	public Player(Player another) {
		this.x = another.x;
		this.y = another.y;
	}

	public void move() {
		//moveLeftAndRight();

		if (isMovingLeft) {
			moveLeft(MAX_HORIZONTAL_SPEED);
		}
		if (isMovingRight) {
			moveRight(MAX_HORIZONTAL_SPEED);
		}

		if (isMovingUp) {
			moveUp(verticalSpeed);
		} else {
			jump();
		}

		if (startTeleportUp || startTeleportDown || startTeleportRight || startTeleportLeft) {
			teleport();

			//HERE

			startTeleportUp = false;
			startTeleportDown = false;
			startTeleportRight = false;
			startTeleportLeft = false;
		}
	}

	public void moveLeftAndRight()
	{
		//Change horizontal speed if player is trying to move left and right.
		if (isMovingLeft) {
			if(horizontalSpeed > MAX_HORIZONTAL_SPEED * -1) {
				horizontalSpeed -= MAX_HORIZONTAL_SPEED / 24;
			}
		}
		if (isMovingRight) {
			if(horizontalSpeed < MAX_HORIZONTAL_SPEED) {
				horizontalSpeed += MAX_HORIZONTAL_SPEED / 24;
			}
		}

		//If player is not moving right or holding both the A and the D key reduce the horizontal speed to zero.
		if((!isMovingRight && !isMovingLeft) || (isMovingLeft && isMovingRight))
		{
			if(horizontalSpeed > 0)
			{
				horizontalSpeed -= MAX_HORIZONTAL_SPEED / 24;
			}
			if(horizontalSpeed < 0)
			{
				horizontalSpeed += MAX_HORIZONTAL_SPEED / 24;
			}

			if(horizontalSpeed > 0)
			{
				horizontalSpeed -= MAX_HORIZONTAL_SPEED / 24;
			}
			if(horizontalSpeed < 0)
			{
				horizontalSpeed += MAX_HORIZONTAL_SPEED / 24;
			}

			if(horizontalSpeed < MAX_HORIZONTAL_SPEED / 24 || horizontalSpeed > -1 * MAX_HORIZONTAL_SPEED / 24)
			{
				horizontalSpeed = 0;
			}
		}

		//Move left or move right depending on whether horizontal speed is positive or negative.
		if(horizontalSpeed > 0)
		{
			moveRight(horizontalSpeed);
		}

		if(horizontalSpeed < 0)
		{
			moveLeft(horizontalSpeed * -1);
		}
	}

	public void moveLeft(double distance) {
		Player clone = new Player(this);
		clone.setX(x - distance);
		if (clone.hasValidPosition()) {
			x -= distance;
		}
		else
		{
			x = Math.floor(x + .0001);
		}
	}

	public void moveRight(double distance) {
		Player clone = new Player(this);
		clone.setX(x + distance);
		if (clone.hasValidPosition()) {
			x += distance;
		}
		else
		{
			x = Math.floor(x) + 1 - xLength;
		}
	}

	public void moveUp(double distance) {
		Player clone = new Player(this);
		clone.setY(y - distance);
		if (clone.hasValidPosition()) {
			y -= distance;
		}
		else
		{
			y = Math.floor(y);
		}
	}

	public void moveDown(double distance) {
		Player clone = new Player(this);
		clone.setY(y + distance);
		if (clone.hasValidPosition()) {
			y += distance;
		}
		else
		{
			y = Math.floor(y) + 1 - yLength;
		}
	}

	public boolean canMoveDown(double distance) {
		Player clone = new Player(this);
		clone.setY(y + distance);
		if (clone.hasValidPosition()) {
			return true;
		}
		return false;
	}

	public void jump()
	{
		//Player starts jumping up
		if(startJump)
		{
			yVelocity = 2 * jumpHeight * MAX_HORIZONTAL_SPEED / horizontalDistanceToJumpHeight;
			startedDragCountdown = false;
			startJump = false;
			dragStarted = false;
			holdingJumpTime = 0;
		}

		//Player continues moving up
		if(yVelocity > 0)
		{
			moveUp(yVelocity);
		}

		//Gravity pulls player down
		yVelocity += gravity;

		//Player continues holding jump
		if(holdingJump)
		{
			holdingJumpTime++;
		}

		//Player stops holding jump
		if(!holdingJump && !startedDragCountdown)
		{
			holdingJump = false;
			startedDragCountdown = true;
			dragCountdown = (int) ((holdingJumpTime + 1) * 2);
		}

		//Drag countdown starts
		if(startedDragCountdown) {
			dragCountdown--;
		}

		//Drag countdown ends, drag starts
		if(dragCountdown == 0)
		{
			dragStarted = true;
		}

		//Drag pulls player down more
		/*
		if(dragStarted)
		{
			yVelocity += gravity * 4;
		}
		*/

		//After apex, drag stops, player moves down at a constant speed.
		if(yVelocity < 0)
		{
			holdingJump = false;
			dragStarted = false;
			dragCountdown = -1;

			if(yVelocity * -1 < MAX_HORIZONTAL_SPEED) {
				moveDown(yVelocity * -1);
			}
			else
			{
				moveDown(MAX_HORIZONTAL_SPEED);
			}
		}

		//If player touching ground, velocity resets
		if(!canMoveDown(yVelocity * -1)) {
			yVelocity = 0;
			dragCountdown = -1;
			holdingJump = false;
			dragStarted = false;
		}
	}


	public void teleport()
	{
		double teleX = -1;
		double teleY = -1;
		LinkedList<String> teleportBlockNames = getTeleportBlockNames();
		for(String teleportBlockName : teleportBlockNames)
		{
			for(int x = 0 ; x < Display.grid.length ; x++)
			{
				for(int y = 0 ; y < Display.grid[x].length ; y++)
				{
					if(Display.grid[x][y].hasTeleportBlock())
					{
						String teleportBlock = Display.grid[x][y].getTeleportBlock();
						if(teleportBlock.charAt(1) == teleportBlockName.charAt(1) && teleportBlock.charAt(2) == teleportBlockName.charAt(2))
						{
							teleX = x + .5;
							teleY = y + .5;
						}
					}
				}
			}

			if(!(teleX == -1) && !(teleY == -1))
			{
				if(teleportBlockName.charAt(1) == 'B' || teleportBlockName.charAt(1) == 'E')
				{
					if(startTeleportRight && teleX > x + xLength)
					{
						Player clone = new Player(this);
						clone.teleport(x + xLength +  2 * (teleX - (x + xLength)), y);
						if(clone.hasValidPosition())
						{
							teleport(x + xLength + 2 * (teleX - (x + xLength)), y);
						}
					}

					if(startTeleportLeft && teleX < x)
					{
						Player clone = new Player(this);
						clone.teleport(x - xLength - 2 * (x - teleX), y);
						if(clone.hasValidPosition())
						{
							teleport(x - xLength - 2 * (x - teleX), y);
						}
					}

				}

				if(teleportBlockName.charAt(1) == 'C' || teleportBlockName.charAt(1) == 'E') {
					if (startTeleportUp && teleY < y) {
						Player clone = new Player(this);
						clone.teleport(x, y + yLength + (teleY - (y + yLength)) * 2);
						if (clone.hasValidPosition()) {
							teleport(x, y + yLength + (teleY - (y + yLength)) * 2);
						}
					}

					if (startTeleportDown && teleY > y) {
						Player clone = new Player(this);
						clone.teleport(x, y - yLength - (y - teleY) * 2);
						if (clone.hasValidPosition()) {
							teleport(x, y - yLength - (y - teleY) * 2);
						}
					}
				}


			}
		}
	}

	public void teleport(double x, double y)
	{
		this.x = x;
		this.y = y;
	}


	public boolean hasValidPosition() {
		if(outOfBounds()) {
			return false;
		}
		if(intersects("solid")) {
			return false;
		}

		return true;
	}

	public boolean outOfBounds() {
		if(x < 0) {
			return true;
		}
		if(y < 0){
			return true;
		}
		if(x + xLength > Display.grid.length) {
			return true;
		}
		if(y + yLength > Display.grid[0].length) {
			return true;
		}
		return false;
	}

	public LinkedList<String> getTeleportBlockNames()
	{
		LinkedList<String> teleportBlockNames = new LinkedList<String>();
		for(Block block : getIntersectingBlocks())
		{
			if(block.hasTeleportBlock())
				teleportBlockNames.add(block.getTeleportBlock().toUpperCase());
		}
		return teleportBlockNames;
	}

	public boolean intersects(String blockType)
	{
		//Player cannot be bigger than blocks for this code to work properly.
		for(Block intersectingBlock : getIntersectingBlocks())
		{
			if(intersectingBlock.hasType(blockType))
				return true;
		}
		return false;
	}

	public boolean touches(String blockType)
	{
		//Player cannot be bigger than blocks for this code to work properly.
		for(Block touchingBlock : getTouchingBlocks())
		{
			if(touchingBlock.hasType(blockType))
				return true;
		}
		return false;
	}

	public boolean touchesBottomOfScreen()
	{
		if((int) Math.floor(y + yLength) >= Display.grid[0].length)
		{
			return true;
		}
		return false;
	}

	public LinkedList<Block> getIntersectingBlocks()
	{
		LinkedList<Block> intersectingBlocks = new LinkedList<>();
		//Player cannot be bigger than blocks for this code to work properly.
		try {
			intersectingBlocks.add(Display.grid[(int) Math.floor(x + .0001)][(int) Math.floor(y + .0001)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			intersectingBlocks.add(Display.grid[(int) Math.floor(x + xLength - .0001)][(int) Math.floor(y + .0001)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			intersectingBlocks.add(Display.grid[(int) Math.floor(x + .0001)][(int) Math.floor(y + yLength - .0001)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			intersectingBlocks.add(Display.grid[(int) Math.floor(x + xLength - .0001)][(int) Math.floor(y + yLength - .0001)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		return intersectingBlocks;
	}

	public LinkedList<Block> getTouchingBlocks()
	{
		LinkedList<Block> touchingBlocks = new LinkedList<>();
		//Player cannot be bigger than blocks for this code to work properly.

		/*
		touchingBlocks.add(Display.grid[(int) Math.floor(x)][(int)Math.floor(y)]);
		if((int) Math.floor(x + xLength) < Display.grid.length)
			touchingBlocks.add(Display.grid[(int) Math.floor(x + xLength)][(int)Math.floor(y)]);
		if((int) Math.floor(y + yLength) < Display.grid[0].length)
			touchingBlocks.add(Display.grid[(int) Math.floor(x)][(int)Math.floor(y + yLength)]);
		if((int) Math.floor(x + xLength) < Display.grid.length && (int) Math.floor(y + yLength) < Display.grid[0].length)
			touchingBlocks.add(Display.grid[(int) Math.floor(x + xLength)][(int)Math.floor(y + yLength)]);
		*/
		try {
			touchingBlocks.add(Display.grid[(int) Math.floor(x)][(int)Math.floor(y)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			touchingBlocks.add(Display.grid[(int) Math.floor(x + xLength)][(int)Math.floor(y)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			touchingBlocks.add(Display.grid[(int) Math.floor(x)][(int)Math.floor(y + yLength)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		try {
			touchingBlocks.add(Display.grid[(int) Math.floor(x + xLength)][(int)Math.floor(y + yLength)]);
		} catch (ArrayIndexOutOfBoundsException e) {}

		return touchingBlocks;
	}

	public boolean resetPosition() {

		yVelocity = 0;

		for(int x1 = 0; x1 < Display.grid.length ; x1++)
		{
			for(int y1 = 0 ; y1 < Display.grid[x1].length ; y1++)
			{
				boolean gridHasPlayer = false;
				try {
					gridHasPlayer = Display.grid[x1][y1].hasType("player");

				}
				catch (ArrayIndexOutOfBoundsException e)
				{

				}

				if(gridHasPlayer)
				{
					x = x1 + (1 - xLength) / 2;
					y = y1 + 1 - yLength;
					return true;
				}
			}
		}
		x = 0;
		y = 0;


		return false;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		isMovingLeft = movingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setMovingRight(boolean movingRight) {
		isMovingRight = movingRight;
	}

	public boolean isMovingUp() {
		return isMovingUp;
	}

	public void setMovingUp(boolean movingUp) {
		isMovingUp = movingUp;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public void setMovingDown(boolean movingDown) {
		isMovingDown = movingDown;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static double getxLength() {
		return xLength;
	}

	public static double getyLength() {
		return yLength;
	}

	public boolean isFrozen() {
		return isFrozen;
	}

	public void setFrozen(boolean frozen) {
		isFrozen = frozen;
	}

	public void startJump() {
		this.startJump = true;
	}

	public void startTeleportLeft() {
		this.startTeleportLeft = true;
	}

	public void startTeleportRight() {
		this.startTeleportRight = true;
	}

	public void startTeleportUp() {
		this.startTeleportUp = true;
	}

	public void startTeleportDown() {
		this.startTeleportDown = true;
	}

	public boolean isStartTeleportLeft() {
		return startTeleportLeft;
	}

	public boolean isStartTeleportRight() {
		return startTeleportRight;
	}

	public boolean isStartTeleportUp() {
		return startTeleportUp;
	}

	public boolean isStartTeleportDown() {
		return startTeleportDown;
	}
}
