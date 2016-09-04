package com.fancytank.gamegen.game.map;

import com.fancytank.gamegen.game.actor.BaseActor;

import java.awt.*;
import java.util.ArrayList;

public class MapManager {
    public static MapManager instance;
    public GameMap gameMap;

    public MapManager(GameMap mapClass) {
        gameMap = mapClass.init();
        if (mapClass instanceof GameMap)
            instance = this;
    }

    public void setBoard(Board board) {
        gameMap.setBoard(board);
    }

    public static void changeBlock(BaseActor actor) {
        instance.gameMap.changeBlock(actor);
    }

    public static ArrayList<BaseActor> getBlocksOfClass(String className) {
        ArrayList<BaseActor> output = new ArrayList<>();
        for (BaseActor[] row : instance.gameMap.getMap())
            for (BaseActor actor : row)
                if (actor.getClassName().equals(className))
                    output.add(actor);
        return output;
    }

    public Color getColorAt(int x, int y) {
        return instance.gameMap.getColorAt(x, y);
    }

    public static void dispose() {
        instance = null;
    }
}