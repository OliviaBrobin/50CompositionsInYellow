package game;

import java.io.*;

import static game.Display.grid;

public class UserDataFunctions {
    public static int getUserLevelNum()
    {
        File file = new File("RFTS_User_Data" + "/" + "User_Level_Num" + ".txt");
        if(file.exists())
        {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                if ((line = br.readLine()) != null) {
                    return Integer.parseInt(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    public static void saveUserLevelNum(int levelNum)
    {
        File file = new File("RFTS_User_Data" + "/" + "User_Level_Num" + ".txt");

        try
        {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(levelNum + "");
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
