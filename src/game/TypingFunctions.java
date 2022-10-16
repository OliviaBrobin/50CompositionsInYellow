package game;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.logging.Level;

import static game.Display.player;
import static game.Display.currentDirectory;
import static game.Display.editorMode;
import static game.Display.STARTED_IN_EDITOR_MODE;
import static game.Display.exportFileName;
import static game.Display.levelNum;
import static game.Display.currentBlock;
import static game.Display.grid;
import static game.Display.NEED_TO_HOLD_POINT;

public class TypingFunctions {
    //public static boolean holdingControl;

    //Typing variables
    public static boolean holdingShift = false;
    public static boolean holdingControl = false;
    public static boolean holdingAlt = false;
    public static boolean holdingPoint = false;
    public static boolean holdingB = false;
    public static boolean holdingC = false;
    public static boolean holdingE = false;
    public static boolean holdingV = false;
    public static boolean holdingX = false;
    public static boolean hasClickedOnce = false;
    public static boolean typing = false;

    public static void keyPressActions(int keyCode)
    {
        //Player movement
        if((!typing && !holdingControl) || !editorMode) {
            TypingFunctions.playerMovement(keyCode);
        }


        //Start Screen
        if(!editorMode) {
            TypingFunctions.startGame(keyCode);
        }

        //Switch between editor and not editor mode
        if (keyCode == KeyEvent.VK_Q && STARTED_IN_EDITOR_MODE) {
            editorMode = !editorMode;
        }

        //Editor mode functions
        if(STARTED_IN_EDITOR_MODE) {
            setHoldingKeys(keyCode);
            levelFunctions(keyCode);
            visualFunctions(keyCode);

        }

        if(editorMode) {
            if (!typing && !holdingControl) {
                gridFunctions(keyCode);

                //Select teleportBlocks
                if (holdingPoint || !NEED_TO_HOLD_POINT) {
                    teleportBlockFunctions(keyCode);
                }
            }

            //Typing
            if (typing) {
               typingFunctions(keyCode);
            } else {
                //Enter Typing mode
                if (!holdingAlt && keyCode == KeyEvent.VK_T) {
                    typing = true;
                    exportFileName = "";
                }
            }
        }
    }

    public static void keyReleaseActions(int keyCode)
    {
        if(keyCode == KeyEvent.VK_A){
            player.setMovingLeft(false);
        }
        if(keyCode == KeyEvent.VK_D){
            player.setMovingRight(false);
        }
        if(keyCode == KeyEvent.VK_W){
            player.setMovingUp(false);
        }
        if(keyCode == KeyEvent.VK_S) {
            player.setMovingDown(false);
        }
        if(keyCode == KeyEvent.VK_SPACE) {
            player.holdingJump = false;
        }
        if(keyCode == KeyEvent.VK_SHIFT){
            holdingShift = false;
            hasClickedOnce = false;
        }
        if(keyCode == KeyEvent.VK_CONTROL){
            holdingControl = false;
        }
        if(keyCode == KeyEvent.VK_ALT) {
            holdingAlt = false;
        }
        if (keyCode == KeyEvent.VK_PERIOD) {
            holdingPoint = false;
        }
        if(keyCode == KeyEvent.VK_B){
            holdingB = false;
        }
        if(keyCode == KeyEvent.VK_C){
            holdingC = false;
        }
        if(keyCode == KeyEvent.VK_E){
            holdingE = false;
        }
        if(keyCode == KeyEvent.VK_V){
            holdingV = false;
        }
        if(keyCode == KeyEvent.VK_X){
            holdingX = false;
        }
    }

    public static void setHoldingKeys(int keyCode)
    {
        if (keyCode == KeyEvent.VK_SHIFT) {
            holdingShift = true;
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            holdingControl = true;
        }
        if (keyCode == KeyEvent.VK_ALT) {
            holdingAlt = true;
        }
        if (keyCode == KeyEvent.VK_PERIOD) {
            holdingPoint = true;
        }
        if (keyCode == KeyEvent.VK_B) {
            holdingB = true;
        }
        if (keyCode == KeyEvent.VK_C) {
            holdingC = true;
        }
        if (keyCode == KeyEvent.VK_E) {
            holdingE = true;
        }
        if (keyCode == KeyEvent.VK_V) {
            holdingV = true;
        }
        if (keyCode == KeyEvent.VK_X) {
            holdingX = true;
        }
    }

    public static void startGame(int keyCode)
    {
        if(levelNum == -1 && keyCode == KeyEvent.VK_ENTER)
        {
            levelNum = 0;
            LevelFunctions.changeLevel(currentDirectory, levelNum + "");
        }
        if(levelNum == -2 && keyCode == KeyEvent.VK_ENTER)
        {
            levelNum = UserDataFunctions.getUserLevelNum();
            LevelFunctions.changeLevel(currentDirectory, levelNum + "");
        }
        if((levelNum == -2 || levelNum == -3) && keyCode == KeyEvent.VK_R)
        {
            levelNum = -1;
            LevelFunctions.changeLevel(currentDirectory, levelNum + "");
        }
    }

    public static void playerMovement(int keyCode)
    {
        if (keyCode == KeyEvent.VK_A) {
            player.setMovingLeft(true);
        }
        if (keyCode == KeyEvent.VK_D) {
            player.setMovingRight(true);
        }
        if (keyCode == KeyEvent.VK_S) {
            player.setMovingDown(true);
        }

        //Editor player position control
        if(editorMode)
        {
            if (keyCode == KeyEvent.VK_W) {
                player.setMovingUp(true);
            }
            if (keyCode == KeyEvent.VK_R) {
                player.resetPosition();
            }
            if (keyCode == KeyEvent.VK_F) {
                if (!player.isFrozen()) {
                    player.setFrozen(true);
                } else {
                    player.resetPosition();
                    player.setFrozen(false);
                }
            }
        }
    }

    public static void levelFunctions(int keyCode)
    {
            if (holdingControl && keyCode == KeyEvent.VK_S) {
                LevelFunctions.exportLevel(currentDirectory, exportFileName);
                typing = false;
            }
            if (holdingControl && keyCode == KeyEvent.VK_O) {
                LevelFunctions.changeLevel(currentDirectory, exportFileName);
                typing = false;
            }
            if (holdingControl && keyCode == KeyEvent.VK_RIGHT) {
                LevelFunctions.exportLevel(currentDirectory, exportFileName);
                typing = false;
                levelNum++;
                LevelFunctions.changeLevel(currentDirectory, levelNum + "");
            }
            if (holdingControl && keyCode == KeyEvent.VK_LEFT) {
                LevelFunctions.exportLevel(currentDirectory, exportFileName);
                typing = false;
                levelNum--;
                LevelFunctions.changeLevel(currentDirectory, levelNum + "");
            }
            if (holdingControl && keyCode == KeyEvent.VK_I) {
                LevelFunctions.insertLevel(currentDirectory);
            }
            if (holdingControl && keyCode == KeyEvent.VK_R) {
                LevelFunctions.changeLevel(currentDirectory, exportFileName);
                typing = false;
            }
            if (holdingControl && keyCode == KeyEvent.VK_DELETE) {
                LevelFunctions.deleteLevel(currentDirectory);
            }
    }

    public static void visualFunctions(int keyCode)
    {
        if (holdingAlt && keyCode == KeyEvent.VK_G) {
            Display.griding = !Display.griding;
        }
        if (holdingAlt && keyCode == KeyEvent.VK_M) {
            Display.abstractGriding = !Display.abstractGriding;
        }
        if (holdingAlt && keyCode == KeyEvent.VK_T) {
            Display.printEditorText = !Display.printEditorText;
        }
    }

    public static void gridFunctions(int keyCode)
    {
        //Grid changing
        if(holdingPoint) {
            if (!holdingShift) {
                if (keyCode == KeyEvent.VK_J) {
                    grid = GridFunctions.getExpandedGrid("left");
                }
                if (keyCode == KeyEvent.VK_L) {
                    grid = GridFunctions.getExpandedGrid("right");
                }
                if (keyCode == KeyEvent.VK_I) {
                    grid = GridFunctions.getExpandedGrid("top");
                }
                if (keyCode == KeyEvent.VK_K) {
                    grid = GridFunctions.getExpandedGrid("bottom");
                }
            }
            if (holdingShift) {
                if (keyCode == KeyEvent.VK_J) {
                    grid = GridFunctions.getShrunkGrid("left");
                }
                if (keyCode == KeyEvent.VK_L) {
                    grid = GridFunctions.getShrunkGrid("right");
                }
                if (keyCode == KeyEvent.VK_I) {
                    grid = GridFunctions.getShrunkGrid("top");
                }
                if (keyCode == KeyEvent.VK_K) {
                    grid = GridFunctions.getShrunkGrid("bottom");
                }
            }
        }
    }

    public static void teleportBlockFunctions(int keyCode)
    {
        if (!holdingB && !holdingC && !holdingE) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"solid"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"death"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", "player"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", "small finish"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", "large finish"});
            }
            if (keyCode == KeyEvent.VK_0) {
                currentBlock = new Block(new String[]{"void", "final finish"});
            }
        }
        if (holdingB) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".b1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".b2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".b3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".b4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".b5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".b6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".b7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".b8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".b9"});
            }
        }
        if (holdingC) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".c1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".c2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".c3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".c4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".c5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".c6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".c7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".c8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".c9"});
            }
        }
        if (holdingE) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".e1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".e2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".e3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".e4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".e5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".e6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".e7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".e8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".e9"});
            }
        }
        if (holdingB && holdingShift) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".B1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".B2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".B3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".B4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".B5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".B6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".B7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".B8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".B9"});
            }
        }
        if (holdingC && holdingShift) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".C1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".C2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".C3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".C4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".C5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".C6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".C7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".C8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".C9"});
            }
        }
        if (holdingE && holdingShift) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"void", ".E1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"void", ".E2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"void", ".E3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"void", ".E4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"void", ".E5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"void", ".E6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"void", ".E7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"void", ".E8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"void", ".E9"});
            }
        }
        if (holdingB && holdingAlt) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"death", ".B1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"death", ".B2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"death", ".B3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"death", ".B4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"death", ".B5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"death", ".B6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"death", ".B7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"death", ".B8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"death", ".B9"});
            }
        }
        if (holdingC && holdingAlt) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"death", ".C1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"death", ".C2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"death", ".C3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"death", ".C4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"death", ".C5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"death", ".C6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"death", ".C7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"death", ".C8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"death", ".C9"});
            }
        }
        if (holdingE && holdingAlt) {
            if (keyCode == KeyEvent.VK_1) {
                currentBlock = new Block(new String[]{"death", ".E1"});
            }
            if (keyCode == KeyEvent.VK_2) {
                currentBlock = new Block(new String[]{"death", ".E2"});
            }
            if (keyCode == KeyEvent.VK_3) {
                currentBlock = new Block(new String[]{"death", ".E3"});
            }
            if (keyCode == KeyEvent.VK_4) {
                currentBlock = new Block(new String[]{"death", ".E4"});
            }
            if (keyCode == KeyEvent.VK_5) {
                currentBlock = new Block(new String[]{"death", ".E5"});
            }
            if (keyCode == KeyEvent.VK_6) {
                currentBlock = new Block(new String[]{"death", ".E6"});
            }
            if (keyCode == KeyEvent.VK_7) {
                currentBlock = new Block(new String[]{"death", ".E7"});
            }
            if (keyCode == KeyEvent.VK_8) {
                currentBlock = new Block(new String[]{"death", ".E8"});
            }
            if (keyCode == KeyEvent.VK_9) {
                currentBlock = new Block(new String[]{"death", ".E9"});
            }
        }
    }

    public static void typingFunctions(int keyCode)
    {
        switch (keyCode) {
            case KeyEvent.VK_A:
                exportFileName += "a";
                break;
            case KeyEvent.VK_B:
                exportFileName += "b";
                break;
            case KeyEvent.VK_C:
                exportFileName += "c";
                break;
            case KeyEvent.VK_D:
                exportFileName += "d";
                break;
            case KeyEvent.VK_E:
                exportFileName += "e";
                break;
            case KeyEvent.VK_F:
                exportFileName += "f";
                break;
            case KeyEvent.VK_G:
                exportFileName += "g";
                break;
            case KeyEvent.VK_H:
                exportFileName += "h";
                break;
            case KeyEvent.VK_I:
                exportFileName += "i";
                break;
            case KeyEvent.VK_J:
                exportFileName += "j";
                break;
            case KeyEvent.VK_K:
                exportFileName += "k";
                break;
            case KeyEvent.VK_L:
                exportFileName += "l";
                break;
            case KeyEvent.VK_M:
                exportFileName += "m";
                break;
            case KeyEvent.VK_N:
                exportFileName += "n";
                break;
            case KeyEvent.VK_O:
                exportFileName += "o";
                break;
            case KeyEvent.VK_P:
                exportFileName += "p";
                break;
            case KeyEvent.VK_Q:
                exportFileName += "q";
                break;
            case KeyEvent.VK_R:
                exportFileName += "r";
                break;
            case KeyEvent.VK_S:
                exportFileName += "s";
                break;
            case KeyEvent.VK_T:
                exportFileName += "t";
                break;
            case KeyEvent.VK_U:
                exportFileName += "u";
                break;
            case KeyEvent.VK_V:
                exportFileName += "v";
                break;
            case KeyEvent.VK_W:
                exportFileName += "w";
                break;
            case KeyEvent.VK_X:
                exportFileName += "x";
                break;
            case KeyEvent.VK_Y:
                exportFileName += "y";
                break;
            case KeyEvent.VK_Z:
                exportFileName += "z";
                break;
            case KeyEvent.VK_EQUALS:
                exportFileName += "-";
                break;
            case KeyEvent.VK_0:
                exportFileName += "0";
                break;
            case KeyEvent.VK_1:
                exportFileName += "1";
                break;
            case KeyEvent.VK_2:
                exportFileName += "2";
                break;
            case KeyEvent.VK_3:
                exportFileName += "3";
                break;
            case KeyEvent.VK_4:
                exportFileName += "4";
                break;
            case KeyEvent.VK_5:
                exportFileName += "5";
                break;
            case KeyEvent.VK_6:
                exportFileName += "6";
                break;
            case KeyEvent.VK_7:
                exportFileName += "7";
                break;
            case KeyEvent.VK_8:
                exportFileName += "8";
                break;
            case KeyEvent.VK_9:
                exportFileName += "9";
                break;
            case KeyEvent.VK_SPACE:
                exportFileName += " ";
                break;
            case KeyEvent.VK_BACK_SPACE:
                exportFileName = removeLastCharacter(exportFileName);
                break;
            case KeyEvent.VK_ESCAPE:
                typing = false;
                break;
        }
    }

    public static String removeLastCharacter(String str) {
        String result = Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
        return result;
    }

}
