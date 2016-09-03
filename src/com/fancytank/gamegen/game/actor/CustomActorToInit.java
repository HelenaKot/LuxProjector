package com.fancytank.gamegen.game.actor;

import java.awt.*;

public class CustomActorToInit extends ActorToInit {
    public CustomActorToInit(String name, String textureName, String colorHex) {
        super(name, textureName, colorHex);
    }

    public CustomActorToInit(TileType tile) {
        super(tile.name, tile.textureName, tile.colorHex);
    }

    @Override
    BaseActor createInstance(int x, int y) {
        return new GenericActor(x, y, tile.name, Color.decode(tile.colorHex));
    }
}
