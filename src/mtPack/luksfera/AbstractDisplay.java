package mtPack.luksfera;

import mtPack.luksfera.event.LuxsferDisplayEvent;
import mtPack.luksfera.event.LuxsferDisplayListener;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public abstract class AbstractDisplay extends LuxEventsLayer {
    private static AbstractDisplay instance;
    private static String defaultDistortionPath = "F:\\fancy\\inżynierka\\LuxProjector\\LuxProjector\\Display_478496158";

    // logic
    private int luxsferDistortionBuffer[][][];

    protected abstract void initMap();

    protected abstract Color getMapAt(int x, int y);

    protected abstract boolean mapInited();

    protected AbstractDisplay(int x, int y) {
        addKeyListener(
                new KeyAdapter() {
                    public void keyPressed(KeyEvent evt) {
                        switch (evt.getKeyChar()) {
                            case 'q':
                                AbstractDisplay.this.setVisible(false);
                                AbstractDisplay.this.dispose();
                                break;
                            case 'f':
                                System.out.println(AbstractDisplay.this.getGraphicsConfiguration().getDevice().getIDstring());
                                AbstractDisplay.this.getGraphicsConfiguration().getDevice().setFullScreenWindow(AbstractDisplay.this);
                                break;
                            case 'l':
                                loadDistortionBuffer();
                                AbstractDisplay.this.repaint();
                                break;
                        }
                    }
                }
        );
        addLuxsferDisplayListener(
                new LuxsferDisplayListener() {
                    public void luxsfereChanged(LuxsferDisplayEvent evt) {
                        // przyszła zmiana
                        AbstractDisplay.this.repaint();
                    }
                }
        );

        initMap();

        setSize(640, 480);
        setVisible(true);
    }

    private void loadDistortionBuffer() {
        String lstr = AbstractDisplay.this.getGraphicsConfiguration().getDevice().getIDstring();
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

    public void paint(Graphics g) {
        if (luxsferDistortionBuffer != null && mapInited()) {
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

                    g.setColor(getMapAt(i, j));
                    g.fillPolygon(tab[0], tab[1], 4);
                }
            }
        }
    }
}