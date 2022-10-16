package game;

import java.awt.Color;
import java.util.Map;

import static java.util.Map.entry;

public class MyColor {
    private Color color;


    static Map<String, String> colors = Map.ofEntries(
            entry("blue", "#439ac6"),
            entry("dark blue", "#285c77"), // + 40% darkness
            entry("light blue", "#8ec2dd"),
            entry("black", "#25201f"),
            entry("red", "#eb483b"),
            entry("dark red", "#8d2b23"),
            entry("light red", "#f39189"),
            entry("white", "#faf8e9"),
            entry("green","#86eb3b"),
            //entry("purple","#a03beb"),
            entry("purple", "#8943c6"),
            entry("dark purple", "#522877"),
            entry("light purple", "#b88edd"),
            entry("dark gray","#3b3332"),
            entry("light gray","#6e6665"),
            //entry("orange","#c66f43"),
            entry("orange", "#f3c115")
            //entry("",""),
    );


    static Color getColor(String col) {
        Color color = Color.WHITE;
        switch (col.toLowerCase()) {
            case "black":
                color = Color.decode(colors.get("black"));
                //color = Color.BLACK;
                break;
            case "white":
                color = Color.decode(colors.get("white"));
                //color = Color.WHITE;
                break;
            case "red":
                color = Color.decode(colors.get("red"));
                //color = Color.RED;
                break;
            case "dark red":
                color = Color.decode(colors.get("dark red"));
                //color = Color.BLUE;
                break;
            case "light red":
                color = Color.decode(colors.get("light red"));
                //color = Color.BLUE;
                break;
            case "blue":
                color = Color.decode(colors.get("blue"));
                //color = Color.BLUE;
                break;
            case "dark blue":
                color = Color.decode(colors.get("dark blue"));
                //color = Color.BLUE;
                break;
            case "light blue":
                color = Color.decode(colors.get("light blue"));
                //color = Color.BLUE;
                break;
            case "purple":
                color = Color.decode(colors.get("purple"));
                //color = Color.BLUE;
                break;
            case "dark purple":
                color = Color.decode(colors.get("dark purple"));
                //color = Color.BLUE;
                break;
            case "light purple":
                color = Color.decode(colors.get("light purple"));
                //color = Color.BLUE;
                break;
            case "cyan":
                color = Color.CYAN;
                break;
            case "dark gray":
                color = Color.DARK_GRAY;
                //color = Color.decode(colors.get("dark gray"));
                break;
            case "gray":
                color = Color.GRAY;
                break;
            case "green":
                color = Color.decode(colors.get("green"));
                //color = Color.GREEN;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
            case "light gray":
                color = Color.decode(colors.get("light gray"));
                break;
            case "magenta":
                color = Color.MAGENTA;
                break;
            case "orange":
                color = Color.decode(colors.get("orange"));
                break;
            case "pink":
                color = Color.PINK;
                break;
        }
        return color;
    }
}