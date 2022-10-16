package game;

import java.awt.*;
import java.util.Map;

import static game.Display.*;

public class ScreenPart {

    Graphics g;
    Block[][] grid;

    int backgroundX;
    int backgroundY;
    int backgroundWidth;
    int backgroundHeight;

    int xPos;
    int yPos;
    int width;
    int height;

    double scalingFactor; //This variable is set depending on what the programmer chooses.

    public ScreenPart()
    {

    }

    public ScreenPart(Graphics g, Block[][] grid, int xPos, int yPos, int width, int height, boolean centered) {
        this.g = g;
        this.grid = grid;

        backgroundX = xPos;
        backgroundY = yPos;
        backgroundWidth = width;
        backgroundHeight = height;

        scaleToFitScreen();

        this.width = (int) (grid.length * scalingFactor);
        this.height = (int) (grid[0].length * scalingFactor);

        if(centered)
        {
            int widthDifference = backgroundWidth - this.width;
            int heightDifference = backgroundHeight - this.height;

            this.xPos = xPos + widthDifference / 2;
            this.yPos = yPos + heightDifference / 2;
        }
        else
        {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }

    public ScreenPart(Graphics g, Block[][] grid, int xPos, int yPos, double scalingFactor) {
        this.g = g;
        this.grid = grid;

        this.xPos = xPos;
        this.yPos = yPos;
        this.scalingFactor = scalingFactor;

        this.width = (int) (grid.length * scalingFactor);
        this.height = (int) (grid[0].length * scalingFactor);

        backgroundX = xPos;
        backgroundY = yPos;
        backgroundWidth = width;
        backgroundHeight = height;

        //TODO account for if scaling factor is too big.
    }

    public void fillBackground()
    {
        Color backgroundColor = MyColor.getColor("dark gray");
        g.setColor(MyColor.getColor("dark gray"));
        g.fillRect(backgroundX, backgroundY, backgroundWidth, backgroundHeight);
    }

    public void fillGrid()
    {

        fillBlocks();
        drawGameText();
        drawGameImages();

        g.setColor(MyColor.getColor("black"));

        if(levelNum == 27)
        {
            g.setColor(MyColor.getColor("white"));
        }

        g.setFont(new Font("Arial", Font.PLAIN, (int) (scalingFactor * grid[0].length * .042)));

        double xMultiple = .90;


        if(levelNum > 8)
        {
            xMultiple = .888;
        }


        if(levelNum >= 0) {
            g.drawString((levelNum + 1) + " | " + (Display.FINAL_LEVEL_NUMBER + 1), (int) (xPos + (scalingFactor * grid.length) * xMultiple), (int) (yPos + (scalingFactor * grid[0].length) * .09));
        }
    }

    public void fillBlocks()
    {
        if(grid != null) {
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    try {
                        Map<String, String> blockVisual = grid[x][y].getVisual();

                        g.setColor(MyColor.getColor(blockVisual.get("color")));
                        g.fillRect((int) (x * scalingFactor) + xPos, (int) (y * scalingFactor) + yPos, (int) scalingFactor, (int) scalingFactor);
                        if (printEditorText && editorMode) {
                            g.setColor(MyColor.getColor(blockVisual.get("text color")));
                            g.setFont(new Font("TimesRoman", Font.BOLD, (int) (scalingFactor * .55)));
                            g.drawString(blockVisual.get("text"), (int) (x * scalingFactor) + xPos, (int) (y * scalingFactor) + yPos + (int) scalingFactor);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //Do nothing
                    }
                }
            }
        }
    }


    public void drawGameText()
    {
        for(int x = 0; x < grid.length ; x++) {
            for(int y = 0 ; y < grid[x].length ; y++) {

                if(grid[x][y].hasTypeText()) {
                    Map<String, String> blockVisual = grid[x][y].getVisual();

                    g.setColor(MyColor.getColor(blockVisual.get("text color")));
                    g.setFont(new Font("Arial", Font.PLAIN, (int) (scalingFactor * .55)));
                    String text = blockVisual.get("text");

                    double xAdd = 0;
                    double yAdd = 0;

                    /*
                    "text-Use A and D keys to move left and right."}),
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
                   */

                    if(text.equals("(A game made by Olivia Brobin)"))
                    {
                        xAdd = -.091;
                    }
                    if(text.equals("Press enter to start the game."))
                    {
                        xAdd = .61 - .5;
                    }
                    else if(text.equals("You completed the game."))
                    {

                        xAdd = 1.12 - .5;
                    }
                    else if(text.equals("You have have a previous save."))
                    {

                        xAdd = .43 - .5;
                    }
                    else if(text.equals("Press enter to continue with your previous save."))
                    {

                        xAdd = .55 - .5;
                    }
                    else if(text.equals("Press R to reset your progress."))
                    {

                        xAdd = .45 - .5;
                    }
                    else if(text.equals("Use the A and D keys to move left and right."))
                    {

                        xAdd = .3 - .5;
                        yAdd = .75;
                    }
                    else if(text.equals("Use the space bar to move up."))
                    {

                        xAdd = .35;
                        yAdd = -.25;
                    }
                    else if(text.equals("Use the I key to go up."))
                    {

                        xAdd = .35;
                        yAdd = -.25;
                    }
                    else if(text.equals("Use the K key to go down."))
                    {

                        xAdd = .35;
                        yAdd = -.25;
                    }
                    else if(text.equals("Use the J key to go left."))
                    {

                        xAdd = .74;
                        yAdd = -.25;
                    }
                    else if(text.equals("Use the L key to go right."))
                    {

                        xAdd = .55;
                        yAdd = -.25;
                    }



                    g.drawString(blockVisual.get("text"), (int) ((x + xAdd) * scalingFactor) + xPos, (int) ((y + yAdd) * scalingFactor) + yPos + (int) scalingFactor);
                }
            }
        }
    }

    public void drawGameImages()
    {
        for(int x = 0; x < grid.length ; x++) {
            for(int y = 0 ; y < grid[x].length ; y++) {

                if(grid[x][y].hasTypeImage()) {
                    Map<String, String> blockVisual = grid[x][y].getVisual();

                    g.setColor(MyColor.getColor(blockVisual.get("text color")));
                    g.setFont(new Font("Arial", Font.PLAIN, (int) (scalingFactor * .55)));

                    if(!blockVisual.get("image").equals(""))
                    {
                        //Logo if it's drawn same size as other squares.
                        //g.drawImage(Toolkit.getDefaultToolkit().getImage("RFTS_Images/" + grid[x][y].getImage()), xPos + (int) (x * scalingFactor) + (int) (scalingFactor * 10.3), yPos + (int) (y * scalingFactor) + (int) (scalingFactor * 0), (int) (Display.logoImage.getWidth(null) * scalingFactor * 2.2 / Display.logoImage.getHeight(null)), (int) (scalingFactor * 2.2), null);
                        g.drawImage(Toolkit.getDefaultToolkit().getImage("RFTS_Images/" + grid[x][y].getImage()), xPos + (int) ((x + 2.8) * scalingFactor) + (int) (scalingFactor * 10.4  - 1.5), yPos + (int) (y * scalingFactor) + (int) (scalingFactor * .25), (int) (Display.logoImage.getWidth(null) * scalingFactor * 1.8 / Display.logoImage.getHeight(null)), (int) (scalingFactor * 1.8), null);



                        g.setColor(MyColor.getColor(blockVisual.get("text color")));
                        g.setFont(new Font("Arial", Font.PLAIN, (int) (scalingFactor * 1.3)));
                        g.drawString("50 Compositions in Yellow", (int) ((x - 2.8) * scalingFactor) + xPos - (int) (scalingFactor * .1  - 1.5), (int) (y * scalingFactor) + yPos + (int) (scalingFactor + scalingFactor * .6));

                    }
                }
            }
        }
    }

    public void outlineGrid()
    {
        for (int x = 0; x < grid.length; x++) {
            try {
                for (int y = 0; y < grid[x].length; y++) {
                    g.setColor(Color.BLACK);
                    g.drawRect((int) (x * scalingFactor) + xPos, (int) (y * scalingFactor) + yPos, (int) scalingFactor, (int) scalingFactor);
                }
            }
            catch(ArrayIndexOutOfBoundsException e)
            {

            }
        }
    }

    public void outlineGridSectionsAbstractly()
    {
        g.setColor(MyColor.getColor("dark gray"));

        for(int x = 0; x < grid.length ; x++) {
            for(int y = 0 ; y < grid[x].length ; y++) {
                Color blockColor = MyColor.getColor(grid[x][y].getVisual().get("color"));

                if(GridFunctions.hasBlockBelow(x, y))
                {
                    Block blockBelow = GridFunctions.getBlockBelow(x, y);
                    if(!blockBelow.hasType("")) {
                        Color blockBelowColor = MyColor.getColor(blockBelow.getVisual().get("color"));
                        if (!blockColor.equals(blockBelowColor)) {
                            if (x == grid.length - 1 || x == 0) {
                                int thickness = (int) scalingFactor / 10;
                                g.fillRect((int) (x * scalingFactor) + xPos, (int) ((y + 1) * scalingFactor) + yPos - thickness, (int) scalingFactor, thickness);
                            } else {
                                int thickness = (int) scalingFactor / 10;
                                g.fillRect((int) (x * scalingFactor) + xPos - thickness, (int) ((y + 1) * scalingFactor) + yPos - thickness, (int) scalingFactor * 11 / 10, thickness);
                            }
                        }
                    }
                }

                if(GridFunctions.hasBlockToRight(x, y))
                {
                    Block blockToRight = GridFunctions.getBlockToRight(x, y);
                    if(!blockToRight.hasType("")) {
                        Color blockToRightColor = MyColor.getColor(blockToRight.getVisual().get("color"));
                        if (!blockColor.equals(blockToRightColor)) {
                            if (y == grid[0].length - 1 || y == 0) {
                                int thickness = (int) scalingFactor / 10;
                                g.fillRect((int) ((x + 1) * scalingFactor) + xPos - thickness, (int) ((y) * scalingFactor) + yPos, thickness, (int) scalingFactor);
                            } else {
                                int thickness = (int) scalingFactor / 10;
                                g.fillRect((int) ((x + 1) * scalingFactor) + xPos - thickness, (int) ((y) * scalingFactor) + yPos - thickness, thickness, (int) scalingFactor * 11 / 10);
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillPlayer(Player player)
    {
        //g.setColor(MyColor.getColor("orange"));
        g.setColor(MyColor.getColor("white"));

        g.fillRect((int) Math.floor(player.getX() * scalingFactor) + xPos,
                (int) Math.floor(player.getY() * scalingFactor) + yPos,
                (int) Math.floor(Player.getxLength() * scalingFactor),
                (int) Math.floor(Player.getyLength() * scalingFactor));
    }

    public void outlinePlayerAbstractly(Player player)
    {
        g.setColor(MyColor.getColor("dark gray"));

        double x = player.getX();
        double y = player.getY();
        int thickness = (int) scalingFactor / 10;

        g.fillRect((int) (x * scalingFactor) + xPos - thickness, (int) ((y) * scalingFactor) + yPos - thickness, thickness, (int) (scalingFactor * 11/10 * player.getyLength()) );
        g.fillRect((int) ((x + player.getxLength()) * scalingFactor) + xPos - thickness, (int) ((y) * scalingFactor) + yPos - thickness, thickness, (int) (scalingFactor * player.getyLength()) + thickness);
        g.fillRect((int) (x * scalingFactor) + xPos - thickness, (int) ((y) * scalingFactor) + yPos - thickness, (int) (scalingFactor * player.getxLength() * 11/10), thickness);
        g.fillRect((int) (x * scalingFactor) + xPos - thickness, (int) ((y + player.getyLength()) * scalingFactor) + yPos - thickness, (int) (scalingFactor * player.getxLength()) + thickness, thickness);
    }

    public void scaleToFitScreen()
    {
        double scalingFactor = backgroundWidth / grid.length;

        if(grid[0].length * scalingFactor > backgroundHeight) {
            scalingFactor = scalingFactor / (grid[0].length * scalingFactor / backgroundHeight);
        }

        scalingFactor = Math.floor(scalingFactor);

        this.scalingFactor = scalingFactor;
    }

    public double getScalingFactor()
    {
        return scalingFactor;
    }

    public void setScalingFactor(double scalingFactor)
    {
        this.scalingFactor = scalingFactor;
    }

    public int getHeight()
    {
        return height;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Block getBlock(int mouseX, int mouseY)
    {
        int x = (int) Math.floor((mouseX - xPos) / scalingFactor);
        int y = (int) Math.floor((mouseY - yPos) / scalingFactor);

        if (x < grid.length && x >= 0 && y >= 0) {
            if (y < grid[x].length) {
                return grid[x][y];
            }
        }

        return null;
    }
}
