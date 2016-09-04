package com.fancytank.gamegen;

import mtPack.luksfera.event.LuxsferDisplayEvent;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameLaunech {
    private LuxAdapter display;
    private int offsetX, offsetY;

    ServerSocket ss;
    Socket soc;
    InputStream is;

    private boolean connectionLost = false;

    public GameLaunech(int screenWidth, int screenHeight, int x, int y, int offsetX, int offsetY) {
        SaveInstance saveInstance = DataManager.loadFile("test_json");
        display = LuxAdapter.make(x, y, saveInstance); //todo save here

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        new Thread() {
            public void run() {
                try {
                    ss = new ServerSocket(1701);
                } catch (Exception ex) {
                    System.out.println("Ex" + ex);
                }
                while (true) {
                    try {
                        System.out.println("W8 4 new conn");
                        soc = ss.accept();
                        System.out.println("I have a Connection");
                        is = soc.getInputStream();
                        runGameLoop();
                        System.out.println("Game initiated");
                    } catch (Exception ex) {
                        System.out.println("A: " + ex);
                        ex.printStackTrace();
                        connectionLost = true;
                    }
                }
            }
        }.start();
        paintGameBord();
    }


    public void initNewGame() {
    }

    public void runGameLoop() {
        if (!connectionLost)
            initNewGame();
        else
            connectionLost = false;
        paintGameBord();

        /** game logic
         * read input
         * react to input **/
    }

    public int[] read() {
        try {
            int wrt = 'p';
            switch (wrt) {
                case 'p':
                    int y = is.read() - '0';
                    int x = is.read() - '0';
                    y = 6 - y;
                    System.out.println("p: " + x + " " + y);
                    return new int[]{x, y};
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void paintGameBord() {
        display.fireLuxsferDisplayEvent(new LuxsferDisplayEvent(this, LuxsferDisplayEvent.LUXSFERDISPLAYPIXELCHANGED));
    }

}
