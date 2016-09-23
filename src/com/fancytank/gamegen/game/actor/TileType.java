package com.fancytank.gamegen.game.actor;

import java.io.Serializable;

public class TileType implements Serializable {
    public String name;
    public String textureName;
    public String colorHex;
    private static final long serialVersionUID = 1233613063064495682L;


    public TileType(String name, String textureName, String colorHex) {
        this.name = name;
        this.textureName = textureName;
        if (colorHex.length() > 7) //alpha saved
            this.colorHex = "#" + colorHex.substring(2, 8).toLowerCase();
        else
            this.colorHex = colorHex.toLowerCase();
    }

}
