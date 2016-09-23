package com.fancytank.gamegen.game.map;

import com.fancytank.gamegen.game.Constant;
import com.fancytank.gamegen.game.actor.ActorInitializer;
import com.fancytank.gamegen.game.actor.BaseActor;
import com.fancytank.gamegen.game.actor.EmptyActor;
import com.fancytank.gamegen.game.actor.TileType;

import java.awt.*;

public class GameMap {
    private static Color placeholderColor = Color.darkGray;
    BaseActor[][] map;

    public GameMap init() {
        if (map == null)
            initEmptyMap(Constant.MAP_WIDTH, Constant.MAP_HEIGHT);
        return this;
    }

    public void setBoard(Board mapBoard) {
        TileType[][] board = mapBoard.board;
        map = new BaseActor[Constant.MAP_WIDTH][Constant.MAP_HEIGHT];
        for (int x = 0; x < Constant.MAP_WIDTH; x++)
            for (int y = 0; y < Constant.MAP_HEIGHT; y++)
                initActor(ActorInitializer.getInstanceOf(board[x][y].name, x, y));
    }

    private void initEmptyMap(int width, int height) {
        map = new BaseActor[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                initActor(ActorInitializer.getInstanceOf("empty", x, y));
    }

    public void changeBlock(BaseActor actor) {
        if (actor == null)
            System.out.println(actor + " is not initialized");
        else if (inBounds(actor.x, actor.y) && map[actor.x][actor.y].getClassName() != actor.getClassName())
            initActor(actor);
    }

    private boolean inBounds(int x, int y) {
        return 0 <= x && x < Constant.MAP_WIDTH && 0 <= y && y < Constant.MAP_HEIGHT;
    }

    public BaseActor[][] getMap() {
        return map;
    }

    public Color getColorAt(int x, int y) {
        if (inBounds(x, y))
            return map[x][y].tint;
        return placeholderColor;
    }

    private void initActor(BaseActor block) {
        if (block instanceof EmptyActor)
            block.tint = Color.black;
        map[block.x][block.y] = block;
    }
}
