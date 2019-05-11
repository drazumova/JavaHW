package com.hw.cannon;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.security.*;
import java.util.*;

/**
 * Class that represents mount in the game
 */
public class Mount {
    private final Double[] listX;
    private final Double[] listY;
    private final int size;

    private static final int POINTS_DEFAULT_COUNT = 10;

    /**
     * Generates new mount
     */
    public Mount(double maxSize, Pane pane) {
        var random = new SecureRandom();
        size = random.nextInt(POINTS_DEFAULT_COUNT) + 2;
        listX = new Double[size];
        listY = new Double[size];

        for (int i = 0; i < size; i++) {
            listX[i] = maxSize / (size - 1) * i;
        }

        listY[0] = random.nextDouble() * maxSize / 2 + maxSize / 2;
        listY[size - 1] = listY[0];

        for (int i = 1; i + 1 < size; i++) {
//            listY[i] = random.nextDouble() * listY[0];
            listY[i] = listY[0] - 30;
        }

        var list = new ArrayList<Double>();

        for (int i = 0; i < size; i++) {
            list.add(listX[i]);
            list.add(listY[i]);
        }

        var polygon = new Polygon();
        polygon.getPoints().addAll(list);
        polygon.setFill(Color.SADDLEBROWN);

        pane.getChildren().add(polygon);
    }

    /**
     * Tells if given point under the mount
     */
    public boolean isUnder(double x, double y) {
        var mountY = getYCor(x);
        return y < mountY;
    }

    /**
     * Returns y coordinate of point on the mountain top border related to given x coordinate
     */
    public Double getYCor(Double x) {
        int last = 1;
        while (last < size && listX[last] < x) {
            last++;
        }

        var x1 = listX[last - 1];
        var x2 = listX[last];
        var y1 = listY[last - 1];
        var y2 = listY[last];

        return (y2 - y1) / (x2 - x1) * (x - x1) + y1;
    }
}
