package game;

import java.io.*;
import java.util.LinkedList;

import static game.Display.*;
import static java.lang.Integer.parseInt;

public class LevelFunctions {

    public static void clearLevel(String directory)
    {
        grid = new Block[12][12];

        for(int x = 0; x < grid.length ; x++)
        {
            for(int y = 0 ; y < grid[x].length ; y++) {
                grid[x][y] = new Block(new String[]{"void"});
            }
        }
        exportLevel(directory, Display.exportFileName);
    }

    public static void changeLevel(String directory, String level)
    {
        if(level.matches("-?\\d+"))
        {
            levelNum = parseInt(level);
        }

        importLevel(directory, level);

        player.resetPosition();
    }

    public static void exportLevel(String directory, String exportFileName)
    {
        File file = new File(directory + "/" + exportFileName + ".txt");

        try {
            FileWriter myWriter = new FileWriter(directory + "/" + exportFileName + ".txt");
            for(int x = 0 ; x < grid.length ; x++)
            {
                for(int y = 0 ; y < grid[x].length ; y++)
                {
                    myWriter.write(grid[x][y].getTypesAsExportableString() + ",");
                }
                myWriter.write("\n");
            }
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void importLevel(String directory, String levelName)
    {
        Display.exportFileName = levelName;
        File file = new File(directory + "/" + levelName + ".txt");

        if(file.exists()){
            LinkedList<String[]> gridAsList = new LinkedList<String[]>();

            try (BufferedReader br = new BufferedReader(new FileReader(directory + "/" + levelName + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] partsOfLine = line.split(",");
                    gridAsList.add(partsOfLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Block[][] gridAsArray = new Block[gridAsList.size()][gridAsList.get(0).length];

            for (int x = 0; x < gridAsArray.length; x++) {
                for (int y = 0; y < gridAsArray[x].length; y++) {
                    gridAsArray[x][y] = new Block(gridAsList.get(x)[y]);
                }
            }

            //This is where we need to have the game load for a second so that the game never crashes.
            grid = gridAsArray;
        }
        else{
            clearLevel(directory);
        }
    }

    public static void insertLevel(String directory)
    {
        int originalLevelNum = levelNum;
        int maxLevelNum = getMaxLevelNum(directory);

        if(maxLevelNum == originalLevelNum)
        {
            maxLevelNum++;
        }

        for(int x = maxLevelNum ; x > originalLevelNum ; x--)
        {
            importLevel(directory, x + "");
            Display.exportFileName = (x+1) + "";
            exportLevel(directory, Display.exportFileName);
        }

        levelNum++;

        changeLevel(directory,levelNum + "");
        clearLevel(directory);
    }

    public static void deleteLevel(String directory)
    {
        int originalLevelNum = levelNum;

        int maxLevelNum = getMaxLevelNum(directory);

        if(maxLevelNum == originalLevelNum)
        {
            maxLevelNum++;
        }

        for(int x = originalLevelNum ; x < maxLevelNum ; x++)
        {
            importLevel(directory, x + 1 + "");
            Display.exportFileName = (x) + "";
            exportLevel(directory, Display.exportFileName);
        }

        changeLevel(directory, levelNum + "");
    }

    public static int getMaxLevelNum(String directory)
    {
        int maxLevelNum = levelNum;
        for(int x = maxLevelNum ; x < 1000 ; x++)
        {
            File file = new File(directory + "/" + maxLevelNum + ".txt");
            if(file.exists())
            {
                x++;
                maxLevelNum = x;
            }
            else
            {
                break;
            }
        }


        return maxLevelNum;
    }
}
