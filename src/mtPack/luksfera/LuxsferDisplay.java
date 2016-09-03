package mtPack.luksfera;

import mtPack.luksfera.event.LuxsferDisplayEvent;
import mtPack.luksfera.event.LuxsferDisplayListener;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LuxsferDisplay extends LuxEventsLayer {
    private static LuxsferDisplay instance;
    private static String defaultDistortionPath = "F:\\fancy\\inżynierka\\LuxProjector\\LuxProjector\\Display_478496158";

    public static LuxsferDisplay make(int x, int y) {
        if (instance == null)
            instance = new LuxsferDisplay(x, y);
        return instance;
    }

    // logic
    private int luxsferDistortionBuffer[][][];
    public Color luxsferColorBuffer[][];

    private LuxsferDisplay(int x, int y) {
        addKeyListener(
                new KeyAdapter() {
                    public void keyPressed(KeyEvent evt) {
                        switch (evt.getKeyChar()) {
                            case 'q':
                                LuxsferDisplay.this.setVisible(false);
                                LuxsferDisplay.this.dispose();
                                break;
                            case 'f':
                                System.out.println(LuxsferDisplay.this.getGraphicsConfiguration().getDevice().getIDstring());
                                LuxsferDisplay.this.getGraphicsConfiguration().getDevice().setFullScreenWindow(LuxsferDisplay.this);
                                break;
                            case 'l':
                                loadDistortionBuffer();
                                LuxsferDisplay.this.repaint();
                                break;
                        }
                    }
                }
        );
        addLuxsferDisplayListener(
                new LuxsferDisplayListener() {
                    public void luxsfereChanged(LuxsferDisplayEvent evt) {
                        // przyszła zmiana
                        LuxsferDisplay.this.repaint();
                    }
                }
        );

        luxsferColorBuffer = getColorMatrixPlaceholder(21, 21);

        setSize(640, 480);
        setVisible(true);
    }

    private void loadDistortionBuffer() {
        String lstr = LuxsferDisplay.this.getGraphicsConfiguration().getDevice().getIDstring();
        lstr = lstr.replace(' ', '_');
        luxsferDistortionBuffer = getDistortionMatrix(lstr);
        if (luxsferDistortionBuffer == null)
            luxsferDistortionBuffer = getDistortionMatrix(defaultDistortionPath);
    }

    private int[][][] getDistortionMatrix(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            int[][][] output = (int[][][]) ois.readObject();
            ois.close();
            fis.close();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Color[][] getColorMatrixPlaceholder(int x, int y) {
        Color[][] output = new Color[x][y];
        Color color1 = Color.gray, color2 = Color.green;
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                if ((i + j) % 2 == 0)
                    output[i][j] = color1;
                else
                    output[i][j] = color2;
        return output;
    }

    public void paint(Graphics g) {
        if (luxsferDistortionBuffer != null && luxsferColorBuffer != null) {
            int[][] tab = new int[2][4];
            for (int i = 0; i < luxsferDistortionBuffer.length - 1; i++) {
                for (int j = 0; j < luxsferDistortionBuffer[i].length - 1; j++) {

                    tab[0][0] = luxsferDistortionBuffer[i][j][0];
                    tab[1][0] = luxsferDistortionBuffer[i][j][1];
                    tab[0][1] = luxsferDistortionBuffer[i + 1][j][0];
                    tab[1][1] = luxsferDistortionBuffer[i + 1][j][1];
                    tab[0][2] = luxsferDistortionBuffer[i + 1][j + 1][0];
                    tab[1][2] = luxsferDistortionBuffer[i + 1][j + 1][1];
                    tab[0][3] = luxsferDistortionBuffer[i][j + 1][0];
                    tab[1][3] = luxsferDistortionBuffer[i][j + 1][1];

                    g.setColor(luxsferColorBuffer[i][j]);
                    g.fillPolygon(tab[0], tab[1], 4);
                }
            }
        }
    }
}