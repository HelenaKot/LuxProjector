package com.fancytank.gamegen;

import mtPack.luksfera.LuxsferDisplay;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameLaunech {
    private LuxsferDisplay display;
    private int offsetX, offsetY;

    ServerSocket ss;
    Socket soc;
    InputStream is;

    private boolean connectionLost = false;

    public GameLaunech(int screenWidth, int screenHeight, int x, int y, int offsetX, int offsetY) {
        display = LuxsferDisplay.make(screenWidth, screenHeight);

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
                        runGame();
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

    public void runGame() {
        if (!connectionLost)
            initNewGame();
        else
            connectionLost = false;
        paintGameBord();
    }

    public int[] read() {
        try {
            int wrt = 'p';//is.read();
            switch (wrt) {
                case 'p':
                    int y = is.read()-'0';
                    int x = is.read()-'0';
                    System.out.println("r: " + x + " " + y + " " + x + " " + (6 - y));
                    y = 6 - y;
                    System.out.println("r: " + x + " " + y);
                    return new int[]{x, y};
                case 'l':
                    break;
                case 'r':
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        return null;
    }

    public void paintGameBord() {
        display.fireLuxsferDisplayEvent(
                new mtPack.luksfera.event.LuxsferDisplayEvent(this, mtPack.luksfera.event.LuxsferDisplayEvent.LUXSFERDISPLAYPIXELCHANGED)
        );
    }

}
