package mtPack.luksfera;

import java.awt.*;

public class LuxsferDisplay extends AbstractDisplay {
    private static LuxsferDisplay instance;

    public Color luxsferColorBuffer[][];

    public static LuxsferDisplay make(int x, int y) {
        if (instance == null)
            instance = new LuxsferDisplay(x, y);
        return instance;
    }

    protected LuxsferDisplay(int x, int y) {
        super(x, y);
    }

    // separated for reuse

    protected void initMap() {
        luxsferColorBuffer = getColorMatrixPlaceholder(21, 21);
    }

    protected Color getMapAt(int x, int y) {
        return luxsferColorBuffer[x][y];
    }

    @Override
    protected boolean mapInited() {
        return luxsferColorBuffer != null;
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
}