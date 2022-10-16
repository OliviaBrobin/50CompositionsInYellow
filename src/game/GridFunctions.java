package game;

import static game.Display.grid;
import static game.Display.player;

public class GridFunctions {
    public static Block[][] getExpandedGrid(String side)
    {
        Block[][] newGrid = null;

        if(side.equals("right")) {
            newGrid = new Block[grid.length + 1][grid[0].length];
            for(int x = 0 ; x < newGrid.length - 1 ; x++)
            {
                for(int y = 0 ; y < newGrid[x].length ; y++)
                {
                    newGrid[x][y] = grid[x][y];
                }
            }
        }

        if(side.equals("left")) {
            newGrid = new Block[grid.length + 1][grid[0].length];
            for(int x = 1 ; x < newGrid.length ; x++)
            {
                for(int y = 0 ; y < newGrid[x].length ; y++)
                {
                    newGrid[x][y] = grid[x - 1][y];
                }
            }
        }

        if(side.equals("bottom")) {
            newGrid = new Block[grid.length][grid[0].length + 1];
            for(int x = 0 ; x < newGrid.length ; x++)
            {
                for(int y = 0 ; y < newGrid[x].length - 1 ; y++)
                {
                    newGrid[x][y] = grid[x][y];
                }
            }
        }

        if(side.equals("top")) {
            newGrid = new Block[grid.length][grid[0].length + 1];
            for(int x = 0 ; x < newGrid.length ; x++)
            {
                for(int y = 1 ; y < newGrid[x].length ; y++)
                {
                    newGrid[x][y] = grid[x][y - 1];
                }
            }
        }

        for(int x = 0 ; x < newGrid.length ; x++)
        {
            for(int y = 0 ; y < newGrid[x].length ; y++)
            {
                if(newGrid[x][y] == null)
                {
                    newGrid[x][y] = new Block(new String[] {"void"});
                }
            }
        }

        return newGrid;
    }

    public static Block[][] getShrunkGrid(String side)
    {
        Block[][] newGrid = null;

        boolean canShrinkThatWay = ((side.equals("right") || side.equals("left")) && grid.length > 1) || ((side.equals("bottom") || side.equals("top")) && grid[0].length > 1);

        if(canShrinkThatWay) {
            if (side.equals("right")) {
                newGrid = new Block[grid.length - 1][grid[0].length];
                for (int x = 0; x < newGrid.length; x++) {
                    for (int y = 0; y < newGrid[x].length; y++) {
                        newGrid[x][y] = grid[x][y];
                    }
                }
            }

            if (side.equals("left")) {
                newGrid = new Block[grid.length - 1][grid[0].length];
                for (int x = 0; x < newGrid.length; x++) {
                    for (int y = 0; y < newGrid[x].length; y++) {
                        newGrid[x][y] = grid[x + 1][y];
                    }
                }
            }

            if (side.equals("bottom")) {
                newGrid = new Block[grid.length][grid[0].length - 1];
                for (int x = 0; x < newGrid.length; x++) {
                    for (int y = 0; y < newGrid[x].length; y++) {
                        newGrid[x][y] = grid[x][y];
                    }
                }
            }

            if (side.equals("top")) {
                newGrid = new Block[grid.length][grid[0].length - 1];
                for (int x = 0; x < newGrid.length; x++) {
                    for (int y = 0; y < newGrid[x].length; y++) {
                        newGrid[x][y] = grid[x][y + 1];
                    }
                }
            }
            return newGrid;
        }
        return grid;
    }

    public static boolean gridHas(String type)
    {
        for(int x = 0 ; x < grid.length ; x++)
        {
            for(int y = 0 ; y < grid[0].length ; y++)
            {
                if(grid[x][y].hasType(type))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasBlockToRight(int x, int y)
    {
        if(x < grid.length - 1)
        {
            return true;
        }
        return false;
    }

    public static boolean hasBlockBelow(int x, int y)
    {
        if(y < grid[0].length - 1)
        {
            return true;
        }
        return false;
    }

    public static Block getBlockToRight(int x, int y)
    {
        try {
            return grid[x + 1][y];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new Block("");
        }
    }

    public static Block getBlockBelow(int x, int y)
    {
        try {
            return grid[x][y + 1];
        }catch(ArrayIndexOutOfBoundsException e)
        {
            return new Block("");
        }
    }
}
