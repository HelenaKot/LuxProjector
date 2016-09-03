package com.fancytank.gamegen.game.actor;

import com.fancytank.gamegen.game.script.Executable;
import com.fancytank.gamegen.game.script.ExecutableProducer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static com.fancytank.gamegen.game.actor.ActorInitializer.getActionsPerTick;
import static com.fancytank.gamegen.game.actor.ActorInitializer.getListenerList;

public abstract class BaseActor {
    public int x, y;
    public Color tint;
    protected String className;
    private LinkedList<Executable> clickActions = new LinkedList<>();
    private LinkedList<Executable> tickActions = new LinkedList<>();

    /**
     * najpierw definiuj klase, potem rob obiekty a nikomu nie stanie sie krzywda
     */
    public BaseActor(int x, int y, String className) {
        this.x = x;
        this.y = y;
        this.className = className;
        tint = Color.WHITE;
        clickActions = collectExecutable(getListenerList(className));
        tickActions = collectExecutable(getActionsPerTick(className));
    }

    private LinkedList<Executable> collectExecutable(List<ExecutableProducer> list) {
        LinkedList<Executable> output = new LinkedList<>();
        for (ExecutableProducer producer : list) {
            Executable instance = producer.getInstance();
            instance.init(this);
            output.add(instance);
        }
        return output;
    }

    public String getClassName() {
        return className;
    }

    public void onClick() {
        for (Executable executable : clickActions)
            executable.performAction();
    }

    public void onTick() {
        for (Executable executable : tickActions)
            executable.performAction();
    }
}
