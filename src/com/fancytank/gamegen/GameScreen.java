package com.fancytank.gamegen;

import com.fancytank.gamegen.game.Constant;
import com.fancytank.gamegen.game.actor.ActorInitializer;
import com.fancytank.gamegen.game.map.BoardManager;
import com.fancytank.gamegen.game.map.GameMap;
import com.fancytank.gamegen.game.map.MapManager;
import com.fancytank.gamegen.game.script.ScriptLoader;
import com.fancytank.gamegen.programming.data.ProgrammingBlockSavedInstance;

public class GameScreen  {
    private MapManager mapManager;

    public void buildStage() {
        mapManager = new MapManager(new GameMap());
    }

    public void onEvent(ProgrammingBlockSavedInstance[] data) {
        new ActorInitializer();
        ScriptLoader.load(data);
        mapManager.setBoard(BoardManager.get("default"));
    }

    public static class GameServer {

        public void read() {
            //todo input
        }

        public void paintGame() {

        }
    }
}
