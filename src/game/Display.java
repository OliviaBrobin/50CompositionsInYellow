package game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Display extends JFrame implements Runnable {
	//Double buffering varibles
	private Image dbImage;
	private Graphics dbg;

	//Editor properties
	static boolean editorMode = false;
	static final boolean STARTED_IN_EDITOR_MODE = editorMode;
	static String currentDirectory = "RFTS_Levels";

	static final boolean NEED_TO_HOLD_POINT = false;

	//Visual properties
	static boolean printEditorText = false;
	static boolean abstractGriding = true;
	static boolean griding = false;


	//Visual properties
	public static Color EDITOR_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static Color GAME_BACKGROUND_COLOR = Color.DARK_GRAY;

	public static final int EDITOR_GRID_HEIGHT = 540;
	public static final int EDITOR_GRID_WIDTH = 960;

	public static int levelScreenHeight = EDITOR_GRID_HEIGHT;
	public static int levelScreenWidth = EDITOR_GRID_WIDTH;

	//Buffer from top of screen
	public static final int TOP_BUFFER = 30;
	public static final int LEFT_BUFFER = 7;

	//Size of screen
	public static final int X_SIZE = 1500;
	public static final int Y_SIZE = 800;

	//Game characteristics
	public static final int GAME_SPEED = 4;

	public static final int FINAL_LEVEL_NUMBER = 49;

	//Player
	public static Player player = new Player();

	static ScreenPart levelScreen = new ScreenPart();
	ScreenPart blocksScreen = new ScreenPart();

	//Grid
	public static Block[][] grid = null;

	//Editor selected blocks
	public static Block[][] selectedGrid = {{new Block(new String[] {"void"})}};

	//Current block
	public static Block currentBlock = new Block(new String[] {"solid"});

	//Level
	public static int levelNum;

	public static Block[][] blocksGrid;

	Block[][] basicBlocks = {
			{new Block(new String[] {"void"}), new Block(new String[] {"solid"}), new Block(new String[] {"death"})},
			{new Block(new String[] {"void", "player"}) , new Block(new String[] {"void", "small finish"}), new Block(new String[] {"void", "large finish"})}
	};

	Block[][] RFTSBlocks = {
			{new Block(new String[] {"void", ".b1"}), new Block(new String[] {"void", "player", ".b1"}), new Block(new String[] {"void", ".B1"}), new Block(new String[] {"void", "player", ".B1"})},
			{new Block(new String[] {"void", ".c1"}), new Block(new String[] {"void", "player", ".c1"}), new Block(new String[] {"void", ".C1"}), new Block(new String[] {"void", "player", ".C1"})},
			{new Block(new String[] {"void", ".e1"}), new Block(new String[] {"void", "player", ".e1"}), new Block(new String[] {"void", ".E1"}), new Block(new String[] {"void", "player", ".E1"})}
	};

	Block[] textBlocks = {
			new Block(new String[] {"void", "text-Use the A and D keys to move left and right."}),
			new Block(new String[] {"void", "text-Use the space bar to move up."}),
			new Block(new String[] {"void", "text-Use the I key to go up."}),
			new Block(new String[] {"void", "text-Use the L key to go right."}),
			new Block(new String[] {"void", "text-Use the J key to go left."}),
			new Block(new String[] {"void", "text-Use the K key to go down."}),
			new Block(new String[] {"void", "text-Press enter to start the game."}),
			new Block(new String[] {"void", "text-You have have a previous save."}),
			new Block(new String[] {"void", "text-Press enter to continue with your previous save."}),
			new Block(new String[] {"void", "text-Press R to reset your progress."}),
			new Block(new String[] {"void", "text-(A game made by Olivia Brobin)"}),
			new Block(new String[] {"void", "text-You completed the game."})
	};

	Block[] imageBlocks = new Block[]{new Block(new String[] {"void", "text-Images are not loading properly"})};

	//Blocks grid visuals
	public static final int BLOCKS_SCREEN_SCALING_FACTOR = 50;
	public static int BLOCKS_SCREEN_LEFT_BUFFER = LEFT_BUFFER + levelScreenWidth + 50;
	public static int blocksScreenTopBuffer;

	//Mouse
	public Mouse mouse = new Mouse();

	//Mouse variables
	public static int previousMouseX = 0;
	public static int previousMouseY = 0;

	//Export file name
	public static String exportFileName = "default";

	//Images variables
	int currentWidth = 0;
	public static boolean imagesUpdated = false;

	//Images
	static Image logoImage;
	Image spacebarImageOriginal;
	Image spacebarImage;

	public static boolean waitToRepaint = false;

	//Constructor
	public Display(){
		//Load images
		logoImage = Toolkit.getDefaultToolkit().getImage("RFTS_Images/Logo.png");

		imagesUpdated = true;

		//Set Variables
		levelNum = UserDataFunctions.getUserLevelNum();
		if(!editorMode && levelNum > 0) {
			levelNum = -2;
		}
		else
		{
			levelNum = -1;
		}


		if(editorMode)
		{
			levelNum = -1;
		}

		LevelFunctions.changeLevel(currentDirectory, levelNum + "");

		//Get image names from folder
		File directoryPath = new File("RFTS_Images");

		//Get .png files only
		File[] files=directoryPath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".png");
			}
		});

		imageBlocks = new Block[files.length];

		for (int x = 0 ; x < files.length ; x++) {
			imageBlocks[x] = new Block(new String[] {"void", "image-" + files[x].getName()});
		}

		//Title above screen
		setTitle("50 Compositions in Yellow");

		//How many pixels by how many
		setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);


		//If it can be resized
		setResizable(true);

		//If you can see it
		setVisible(true);

		//What will happen when the screen is closed. Right now it is to be exited
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UserDataFunctions.saveUserLevelNum(levelNum);
			}
		});

		//Listeners
		addKeyListener(new keyInput());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	//Double buffering funtion
	public void paint(Graphics g){
		dbImage=createImage(getWidth(), getHeight());
		dbg=dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	};

	//Paint graphics area
	public void paintComponent(Graphics g2){
		Graphics2D g = (Graphics2D) g2;
		Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(hints);

		//Change size of images if screen width has changed
		if (currentWidth != getWidth()) {
			imagesUpdated = false;
			currentWidth = getWidth();
		}

		int secondLeftBuffer = 0;
		int secondTopBuffer = 0;

		if (editorMode) {
			setBackground(EDITOR_BACKGROUND_COLOR);

			levelScreenWidth = EDITOR_GRID_WIDTH;
			levelScreenHeight = EDITOR_GRID_HEIGHT;
		} else {
			setBackground(GAME_BACKGROUND_COLOR);

			levelScreenWidth = getWidth() - LEFT_BUFFER;
			levelScreenHeight = getHeight() - TOP_BUFFER;
		}

		boolean centered = !editorMode;

		levelScreen = new ScreenPart(g, grid, LEFT_BUFFER + secondLeftBuffer, TOP_BUFFER + secondTopBuffer, levelScreenWidth, levelScreenHeight, centered);

		//This shouldn't show through
		levelScreen.fillBackground();

		//Color level
		levelScreen.fillGrid();


		//Color grid abstract
		if (!editorMode || abstractGriding) {
			levelScreen.outlineGridSectionsAbstractly();
		}

		//Color player
		if (!player.isFrozen()) {
			levelScreen.fillPlayer(player);

			if (!editorMode || abstractGriding) {
				levelScreen.outlinePlayerAbstractly(player);
			}
		}

		//Outline grid
		if (editorMode && griding) {
			levelScreen.outlineGrid();
		}

		if (editorMode) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 34));

			int topTextBuffer = TOP_BUFFER;

			//Display info
			g.drawString("Level: " + levelNum, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			g.drawString("Current: " + currentBlock.getTypesAsExportableString(), BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			g.drawString("Export File Name: " + exportFileName, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			g.drawString("Typing: " + TypingFunctions.typing, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			double width;
			double height;
			double ratio;
			try{
				width = grid.length;
				height = grid[0].length;
				ratio = height / width;
			}
			catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
			{
				width = 0;
				height = 0;
				ratio = 0;
			}

			g.drawString("Column Width: " + width, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			g.drawString("Column Height: " + height, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);
			g.drawString("Column Ratio: " + ratio, BLOCKS_SCREEN_LEFT_BUFFER, topTextBuffer += 50);



			blocksScreenTopBuffer = topTextBuffer + 50;

			LinkedList<Block[]> blocksScreenBlocks = new LinkedList<Block[]>();

			for(Block[] blocks : basicBlocks)
			{
				blocksScreenBlocks.add(blocks);
			}

			for(Block[] blocks : RFTSBlocks)
			{
				blocksScreenBlocks.add(blocks);
			}

			blocksScreenBlocks.add(textBlocks);
			blocksScreenBlocks.add(imageBlocks);

			blocksGrid = new Block[blocksScreenBlocks.size()][];

			for(int x = 0; x < blocksGrid.length ; x++)
			{
				blocksGrid[x] = blocksScreenBlocks.get(x);
			}


			blocksScreen = new ScreenPart(g, blocksGrid, BLOCKS_SCREEN_LEFT_BUFFER, blocksScreenTopBuffer, BLOCKS_SCREEN_SCALING_FACTOR);

			blocksScreen.fillGrid();


			ScreenPart selectedBlocksScreen = new ScreenPart(g, selectedGrid, levelScreen.getXPos(), levelScreen.getYPos() + levelScreen.getHeight() + 50, levelScreen.getScalingFactor());
			selectedBlocksScreen.fillGrid();

		}

		if(waitToRepaint)
		{
			int num = 0;
			for(int x = 0 ; x < 1000000 ;x++)
			{
				num+=x;
			}
			waitToRepaint = false;
		}
		repaint();

	}

	public class Mouse extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(editorMode) {

				//TODO update this section to reflect the screen part change, I kind of like the mouseX = solution, so that should also be hte solution for blocksScreen
				int mouseX = (int) Math.floor((e.getX() - levelScreen.getXPos()) / levelScreen.getScalingFactor());
				int mouseY = (int) Math.floor((e.getY() - levelScreen.getYPos()) / levelScreen.getScalingFactor());


				if(TypingFunctions.holdingPoint)
				{
					currentBlock = grid[mouseX][mouseY];
				}
				else {
					if (mouseX < grid.length && mouseY < grid[0].length && mouseX >= 0 && mouseY >= 0) {
						//Paste the one selected block
						if (!TypingFunctions.holdingControl && (!TypingFunctions.holdingC || !TypingFunctions.holdingV || !TypingFunctions.holdingX)) {
							if (TypingFunctions.holdingAlt) {
								grid[mouseX][mouseY].switchBetweenSolidAndVoid();
							} else {
								grid[mouseX][mouseY] = currentBlock;
							}
						}

						//Past the entire selection of blocks
						if (TypingFunctions.holdingControl && TypingFunctions.holdingV) {
							for (int x = mouseX; x < mouseX + selectedGrid.length; x++) {
								for (int y = mouseY; y < mouseY + selectedGrid[0].length; y++) {
									if (x < grid.length && y < grid[x].length) {
										grid[x][y] = selectedGrid[x - mouseX][y - mouseY];
									}
								}
							}
						}

						if (!TypingFunctions.hasClickedOnce && (TypingFunctions.holdingShift || (TypingFunctions.holdingControl && TypingFunctions.holdingC) || (TypingFunctions.holdingControl && TypingFunctions.holdingX))) {
							TypingFunctions.hasClickedOnce = true;
							previousMouseX = mouseX;
							previousMouseY = mouseY;
						} else if (TypingFunctions.hasClickedOnce && (TypingFunctions.holdingShift || (TypingFunctions.holdingControl && TypingFunctions.holdingC) || (TypingFunctions.holdingControl && TypingFunctions.holdingX))) {
							//Get second click's position
							int smallX;
							int largeX;

							int smallY;
							int bigY;

							TypingFunctions.hasClickedOnce = false;

							if (previousMouseX < mouseX) {
								smallX = previousMouseX;
								largeX = mouseX;
							} else {
								largeX = previousMouseX;
								smallX = mouseX;
							}

							if (previousMouseY < mouseY) {
								smallY = previousMouseY;
								bigY = mouseY;
							} else {
								bigY = previousMouseY;
								smallY = mouseY;
							}

							if ((TypingFunctions.holdingControl && TypingFunctions.holdingC) || (TypingFunctions.holdingControl && TypingFunctions.holdingX)) {
								//Bring a set of blocks into selected blocks array.
								selectedGrid = new Block[largeX - smallX + 1][bigY - smallY + 1];

								for (int x = smallX; x <= largeX; x++) {
									for (int y = smallY; y <= bigY; y++) {
										selectedGrid[x - smallX][y - smallY] = grid[x][y];
									}
								}
								if (TypingFunctions.holdingControl && TypingFunctions.holdingX) {
									for (int x = smallX; x <= largeX; x++) {
										for (int y = smallY; y <= bigY; y++) {
											grid[x][y] = new Block("void");
										}
									}
								}
							} else {
								//Color a section of blocks in
								for (int x = smallX; x <= largeX; x++) {
									for (int y = smallY; y <= bigY; y++) {
										grid[x][y] = currentBlock;
									}
								}
							}
						}
					}


					Block tempBlock = blocksScreen.getBlock(e.getX(), e.getY());
					if (tempBlock != null) {
						currentBlock = tempBlock;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e){

		}

		@Override
		public void mouseMoved(MouseEvent e){

		}

	}

	public class keyInput extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();


			if (keyCode == KeyEvent.VK_L) {
				player.startTeleportRight();
			}
			if (keyCode == KeyEvent.VK_J) {
				player.startTeleportLeft();
			}
			if (keyCode == KeyEvent.VK_I) {
				player.startTeleportUp();
			}
			if (keyCode == KeyEvent.VK_K) {
				player.startTeleportDown();
			}


			/*
			if (keyCode == KeyEvent.VK_RIGHT) {
				player.startTeleportRight();
			}
			if (keyCode == KeyEvent.VK_LEFT) {
				player.startTeleportLeft();
			}
			if (keyCode == KeyEvent.VK_UP) {
				player.startTeleportUp();
			}
			if (keyCode == KeyEvent.VK_DOWN) {
				player.startTeleportDown();
			}
			*/



			if (keyCode == KeyEvent.VK_SPACE) {
				if (!player.canMoveDown(.001)) {
					if(grid[0].length > player.getY())
					{
						if(grid[(int) Math.floor(player.getX() + 1)][(int) Math.floor(player.getY()) + 1].hasType("solid") || grid[(int) Math.floor(player.getX())][(int) Math.floor(player.getY()) + 1].hasType("solid") )
						{
							player.startJump();
							player.holdingJumpTime = 0;
							player.holdingJump = true;
						}
					}
				}
			}

			TypingFunctions.keyPressActions(keyCode);
		}

		@Override
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();

			/*
			if (keyCode == KeyEvent.VK_L) {
				player.startTeleportRight = false;
			}
			if (keyCode == KeyEvent.VK_J) {
				player.startTeleportLeft = false;
			}
			if (keyCode == KeyEvent.VK_I) {
				player.startTeleportUp = false;
			}
			if (keyCode == KeyEvent.VK_K) {
				player.startTeleportDown = false;
			}
			*/

			/*
			if (keyCode == KeyEvent.VK_RIGHT) {
				player.startTeleportRight();
			}
			if (keyCode == KeyEvent.VK_LEFT) {
				player.startTeleportLeft();
			}
			if (keyCode == KeyEvent.VK_UP) {
				player.startTeleportUp();
			}
			if (keyCode == KeyEvent.VK_DOWN) {
				player.startTeleportDown();
			}
			*/


			TypingFunctions.keyReleaseActions(keyCode);
		}
	}

	public static void main(String[] args) {
		new Thread(new Display()).start();
	}

	@Override
	public void run() {
		while(true) {
			if(!player.isFrozen()) {

				//Move player
				player.move();

				if (player.touches("death") || player.touchesBottomOfScreen()) {
					player.resetPosition();
				}

				if (player.intersects("small finish")) {
					if (editorMode) {
						LevelFunctions.exportLevel(currentDirectory, exportFileName);
					}
					if(levelNum == FINAL_LEVEL_NUMBER)
					{
						levelNum = -3;
					}
					else
					{
						levelNum++;
					}
					//TODO implement a waitToRepaint time so that the screen doesn't flash when the level changes.


					waitToRepaint = true;
					LevelFunctions.changeLevel(currentDirectory, levelNum + "");
				}
			}

			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	public Image getImage(String image)
	{
		if(!imagesUpdated)
		{
			spacebarImageOriginal = Toolkit.getDefaultToolkit().getImage("Images/SpaceBar.png");
			try {
				spacebarImage = ImageIO.read(new File("images/SpaceBar.png")).getScaledInstance((int) (33210 * scalingFactor / 3077), (int) (scalingFactor), Image.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
			imagesUpdated = true;
		}
		return spacebarImage;
	}
	*/

}