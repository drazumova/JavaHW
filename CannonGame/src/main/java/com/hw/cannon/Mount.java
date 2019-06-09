package com.hw.cannon;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import org.jetbrains.annotations.*;

import java.security.*;
import java.util.*;

/**
 * Class that represents mount in the game
 */
public class Mount {
    private final double[] listX;
    private final double[] listY;
    private final int size;

    private static final int MOUNT_HEIGTH = 40;
    private static final int POINTS_DEFAULT_COUNT = 10;

    /**
     * Creates a new mount by given points
     */
    public Mount(double[] listX, double[] listY) {
        this.listX = listX.clone();
        this.listY = listY.clone();
        size = listX.length;
    }

    /**
     * Generates new mount
     */
    public Mount(double maxSize, @NotNull Pane pane) {
        var random = new SecureRandom();
        size = random.nextInt(POINTS_DEFAULT_COUNT) + 2;
        listX = new double[size];
        listY = new double[size];

        for (int i = 0; i < size; i++) {
            listX[i] = maxSize / (size - 1) * i;
        }

        listY[0] = random.nextDouble() * maxSize / 2 + maxSize / 2;
        listY[size - 1] = listY[0];

        for (int i = 1; i + 1 < size; i++) {
//            listY[i] = random.nextDouble() * listY[0];
            listY[i] = listY[0] - MOUNT_HEIGTH;
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
        var mountY = getYCoordinate(x);
        return y < mountY;
    }

    /**
     * Returns y coordinate of point on the mountain top border related to given x coordinate
     */
    public double getYCoordinate(double x) {

        if (x >= listX[size - 1]) {
            return listY[size - 1];
        }

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

    /**
     * Returns right border of the mount
     */
    public double getMaxXCoordinate() {
        return listX[size - 1];
    }

    /**
     * Retuns height of the mount.
     */
    public double getHeight() {
        return MOUNT_HEIGTH;
    }
}
