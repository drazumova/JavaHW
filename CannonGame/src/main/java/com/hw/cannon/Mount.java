package com.hw.cannon;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.*;

public class Mount {
    private Double[] listX;
    private Double[] listY;
    private final int size = 3;
    private final Pane root;

    public Mount(double maxSize, Pane pane) {
        listX = new Double[size];
        listX[0] = 0.0;
        listX[size - 1] = maxSize;
        listY = new Double[size];
        listX[1] = maxSize/2;
        listY[0] = listY[size - 1] = maxSize/2;
        listY[1] = maxSize/4;
        for (int i = 0; i < size; i++) {
            System.out.println(listX[i] + " " + listY[i]);
        }
        var list = new ArrayList<Double>();
        for (int i = 0; i < size; i++) {
            list.add(listX[i]);
            list.add(listY[i]);
        }

        root = pane;
        var polygon = new Polygon();
        polygon.getPoints().addAll(list);
        polygon.setFill(Color.BROWN);

        root.getChildren().add(polygon);
    }

    public boolean isUnder(double x, double y) {
        var mountY = getYCor(x);
        return y < mountY;
    }

    public Double getYCor(Double x) {
        int last = 1;
        while (last < size && listX[last] < x) {
            last++;
        }

        double x1 = listX[last - 1];
        double x2 = listX[last];
        double y1 = listY[last - 1];
        double y2 = listY[last];

        return (y2 - y1) / (x2 - x1) * (x - x1) + y1;
    }


}
