package com.fancytank.gamegen;

import com.fancytank.gamegen.game.Constant;
import com.fancytank.gamegen.game.actor.ActorInitializer;
import com.fancytank.gamegen.game.map.GameMap;
import com.fancytank.gamegen.game.map.MapManager;
import mtPack.luksfera.AbstractDisplay;

import java.awt.*;

public class LuxAdapter extends AbstractDisplay {
    public static LuxAdapter instance;
    private MapManager mapManager;
    private GameMap map;

    public static LuxAdapter make(int x, int y, SaveInstance save) {
        new ActorInitializer();

        Constant.setUpBlockConstants(x, y);
        if (instance == null)
            instance = new LuxAdapter(x, y);
        return instance;
    }

    protected LuxAdapter(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initMap() {
        map = new GameMap();
        mapManager = new MapManager(map);
    }

    @Override
    protected Color getMapAt(int x, int y) {
        return mapManager.getColorAt(x, y);
    }

    @Override
    protected boolean mapInited() {
        return map != null;
    }
}
