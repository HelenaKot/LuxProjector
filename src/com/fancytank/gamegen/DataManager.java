package com.fancytank.gamegen;

import com.fancytank.gamegen.game.actor.ActorInitializer;
import com.fancytank.gamegen.game.actor.CustomActorToInit;
import com.fancytank.gamegen.game.actor.TileType;
import com.fancytank.gamegen.game.map.BoardManager;
import com.google.gson.Gson;

import java.io.*;

public class DataManager {
    private static String absolutePath = "F:\\fancy\\inżynierka\\LuxProjector\\LuxProjector\\";

    public static SaveInstance loadFile(String projectName) {
        try {
            File file = new File(absolutePath, projectName);
            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Gson gson = new Gson();
            SaveInstance si = gson.fromJson(json, SaveInstance.class);

            new ActorInitializer();
            for (TileType tile : si.tiles)
                ActorInitializer.addActorClass(new CustomActorToInit(tile));
            BoardManager.setInstance(si.boards);

            return si;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
