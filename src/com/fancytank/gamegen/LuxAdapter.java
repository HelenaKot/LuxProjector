package com.fancytank.gamegen;

import com.fancytank.gamegen.game.Constant;
import com.fancytank.gamegen.game.actor.ActorInitializer;
import com.fancytank.gamegen.game.map.BoardManager;
import com.fancytank.gamegen.game.map.GameMap;
import com.fancytank.gamegen.game.map.MapManager;
import com.fancytank.gamegen.game.script.ScriptLoader;
import mtPack.luksfera.AbstractDisplay;

import java.awt.*;

public class LuxAdapter extends AbstractDisplay {
    public static LuxAdapter instance;
    private MapManager mapManager;
    private GameMap map;
    private int gameHeight;
    SaveInstance save;

    public static LuxAdapter make(int x, int y, SaveInstance data) {
        if (instance == null) {
            ScriptLoader.load(data.blocks);
            instance = new LuxAdapter(x, y);
            instance.save = data;
        }
        return instance;
    }

    protected LuxAdapter(int x, int y) {
        super(x, y);
    }

    public void onPress(int x, int y) {
        mapManager.onPress(x, y);
    }

    public void onTick() {
        mapManager.onTick();
    }

    @Override
    protected void initMap() {
        map = new GameMap();
        mapManager = new MapManager(map);
        MapManager.setBoard(BoardManager.get("default"));
        gameHeight = Constant.MAP_HEIGHT - 1;
    }

    @Override
    protected Color getMapAt(int x, int y) {
        return mapManager.getColorAt(y, gameHeight - x);
    }

    @Override
    protected boolean mapInited() {
        return map != null;
    }
}
