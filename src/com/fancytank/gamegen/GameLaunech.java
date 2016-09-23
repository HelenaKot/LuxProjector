package com.fancytank.gamegen;

import com.fancytank.gamegen.game.Constant;
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

    public GameLaunech(int screenWidth, int screenHeight, int x, int y, int offsetX, int offsetY) {
        Constant.setUpBlockConstants(screenWidth, screenHeight);
        SaveInstance saveInstance = DataManager.loadFile("demo_json");
        display = LuxAdapter.make(x, y, saveInstance);

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        new Thread() {
            public void run() {
                try {
                    ss = new ServerSocket(1701);
                } catch (Exception ex) {
                    System.out.println("Ex" + ex);
                }

                /* hardoce test
                display.onPress(2, 2);
                display.onPress(4, 4);
                display.onPress(6, 6);
                display.onPress(10, 10);
                */
                paintGameBord();
                runTimer();
                while (true) {

                    try {
                        System.out.println("W8 4 new conn");
                        soc = ss.accept();
                        System.out.println("I have a Connection");
                        is = soc.getInputStream();
                        runInputLoop();
                        System.out.println("Game initiated");
                    } catch (Exception ex) {
                        System.out.println("A: " + ex);
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
        paintGameBord();
    }

    int[] input;

    public void runInputLoop() {
        input = read();
        display.onPress(input[0], input[1]);
        paintGameBord();
    }

    public void runTimer() {
        new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(10);
                        display.onTick();
                        paintGameBord();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.run();
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
