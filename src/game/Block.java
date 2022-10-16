package game;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Block {

    private String[] types;

    public Block(String[] types) {
        this.types = types;
    }

    public boolean hasType(String type1) {
        for(String type : types)
        {
            if(type.equals(type1))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasTeleportBlock()
    {
        for(String type : types)
        {
            if(type.charAt(0) == '.')
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasTypeText()
    {
        for(String type : types)
        {
            if(type.split("-")[0].equals("text"))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasTypeImage()
    {
        for(String type : types)
        {
            if(type.split("-")[0].equals("image"))
            {
                return true;
            }
        }
        return false;
    }

    public String getText()
    {
        for(String type : types)
        {
            if(type.split("-")[0].equals("text") && type.split("-").length == 2)
            {
                return type.split("-")[1];
            }
        }
        return "";
    }

    public String getImage()
    {
        for(String type : types)
        {
            if(type.split("-")[0].equals("image") && type.split("-").length == 2)
            {
                return type.split("-")[1];
            }
        }
        return "";
    }

    public String getTeleportBlock()
    {
        for(String type : types)
        {
            if(type.charAt(0) == '.')
            {
                return type;
            }
        }
        return null;
    }

    public String[] getTypes()
    {
        return types;
    }

    public String getTypesAsExportableString()
    {
        String exportableString = "";
        for(String type : types)
        {
            exportableString += type;
            exportableString += ":";
        }
        if(exportableString.endsWith(":"))
        {
            exportableString = exportableString.substring(0, exportableString.length() - 1);
        }
        return exportableString;
    }

    public Block(String importableString)
    {
        this.types = importableString.split(":");
    }

    public Map<String, String> getVisual()
    {
        String color = "black";
        String textColor = "black";
        String text = "";
        String image = "";

        if(hasType("void"))
        {
            color = "white";
            textColor = "black";
        }
        else if(hasType("solid"))
        {
            color = "black";
            textColor = "white";
        }
        else if(hasType("death"))
        {
            color = "light gray";
        }
        if(hasType("small finish"))
        {
            color = "yellow";
            text += "sf";
        }
        else if(hasType("large finish"))
        {
            color = "yellow";
            text += "lf";
        }
        else if(hasType("final finish"))
        {
            color = "yellow";
            text += "ff";
        }
        else if(hasType("player"))
        {
            text += "P";
            color = "white";
        }
        if(hasTeleportBlock())
        {
            String teleportBlockName = getTeleportBlock();
            if(teleportBlockName.charAt(1) == 'b')
            {
                color = "blue";
            }
            else if(teleportBlockName.charAt(1) == 'c')
            {
                color = "red";
            }
            else if(teleportBlockName.charAt(1) == 'e')
            {
                color = "purple";
            }
            if(hasType("void"))
            {
                if(teleportBlockName.charAt(1) == 'B')
                {
                    color = "light blue";
                }
                else if(teleportBlockName.charAt(1) == 'C')
                {
                    color = "light red";
                }
                else if(teleportBlockName.charAt(1) == 'E')
                {
                    color = "light purple";
                }
            }
            if(hasType("death"))
            {
                if(teleportBlockName.charAt(1) == 'B')
                {
                    color = "dark blue";
                }
                else if(teleportBlockName.charAt(1) == 'C')
                {
                    color = "dark red";
                }
                else if(teleportBlockName.charAt(1) == 'E')
                {
                    color = "dark purple";
                }
            }
            if(hasType("solid"))
            {
                if(teleportBlockName.charAt(1) == 'B')
                {
                    color = "magenta";
                }
                else if(teleportBlockName.charAt(1) == 'C')
                {
                    color = "magenta";
                }
                else if(teleportBlockName.charAt(1) == 'E')
                {
                    color = "magenta";
                }
            }
            text += getTeleportBlock().substring(1);
        }

        if(hasTypeText())
        {
            text += getText();
            //System.out.println(text);
        }

        if(hasTypeImage())
        {
            image += getImage();
        }

        Map<String, String> visual = new HashMap<String, String>();

        visual.put("color", color);
        visual.put("text", text);
        visual.put("image", image);
        visual.put("text color", textColor);
        
        return visual;
    }

    public void switchBetweenSolidAndVoid()
    {
        LinkedList<String> typesAsList = new LinkedList<>();
        typesAsList.addAll(java.util.List.of(types));

        if(typesAsList.contains("void"))
        {
            typesAsList.add("solid");
            typesAsList.remove("void");
        }
        else if(typesAsList.contains("solid"))
        {
            typesAsList.add("void");
            typesAsList.remove("solid");
        }

        types = typesAsList.toArray(new String[typesAsList.size()]);
    }


    /*
    public void addProperties (Block otherBlock)
    {
        //This function doesn't work.

        //Void, solid, death, finish, player, teleport block, text, image
        LinkedList<String> typesAsList = new LinkedList<>();
        typesAsList.addAll(java.util.List.of(types));

        String[] otherBlockTypes = otherBlock.getTypes();

        for(String otherBlockType : otherBlockTypes)
        {
            if(!typesAsList.contains(otherBlockType)) {
                if (otherBlockType.equals("solid")) {
                    typesAsList.remove("void");
                    typesAsList.add("solid");
                }
                if (otherBlockType.equals("void") || otherBlockType.equals("small finish") || otherBlockType.equals("big finish") || otherBlockType.equals("final finish") || otherBlockType.equals("death") || otherBlockType.equals("player")) {
                    typesAsList.remove("solid");
                    typesAsList.add(otherBlockType);
                }
            }
        }

        if(otherBlock.hasTeleportBlock())
        {
            if(!typesAsList.contains(otherBlock.getTeleportBlock())) {
                typesAsList.remove("solid");
                typesAsList.add(otherBlock.getTeleportBlock());
            }
        }
        if(otherBlock.hasTypeImage())
        {
            if(!typesAsList.contains("image" + "-" + otherBlock.getImage())) {
                typesAsList.remove("solid");
                typesAsList.add("image" + "-" + otherBlock.getImage());
            }
        }
        if(otherBlock.hasTypeText())
        {
            if(!typesAsList.contains("text" + "-" + otherBlock.getText())) {
                typesAsList.add("text" + "-" + otherBlock.getText());
            }
        }

        types = typesAsList.toArray(new String[typesAsList.size()]);
    }
     */
}
