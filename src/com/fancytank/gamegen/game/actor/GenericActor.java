package com.fancytank.gamegen.game.actor;

import java.awt.Color;

public class GenericActor extends BaseActor {

    public GenericActor(int x, int y, String name) {
        super(x, y, name);
    }

    public GenericActor(int x, int y, String name, Color tint) {
        super(x, y, name);
        this.tint = tint;
    }

    @Override
    public String toString() {
        return className + " Actor at " + x + " " + y;
    }
}
