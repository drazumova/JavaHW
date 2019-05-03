package com.hw.cannon;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.*;

public class Bomb {
    private double x;
    private double y;
    private Circle view;
    private double r;
    private final Pane root;
    private boolean isKilled;
    private double speed;

    public Bomb(double x, double y, int type, Pane pane) {
        System.out.println("AAAA " + x + " " + y + " " + type);
        root = pane;
        this.x = x;
        this.y = y;
        view = new Circle(x, y, 1);
        switch (type) {
            case 1:
                r = 5;
                speed = 1;
                view.setFill(Color.RED);
                break;
            case 2:
                r = 3;
                speed = 3;
                view.setFill(Color.BLUE);
                break;
            case 3:
                r = 10;
                speed = 0;
                view.setFill(Color.GREEN);
                break;
            default:
                r = 2;
                speed = 2;
                view.setFill(Color.BLACK);
        }

        view.setRadius(r/2);
        root.getChildren().add(view);
    }

    public double distance(Bomb other) {
        return (x - other.x) * (x - other.x) + (y - other.y)*(y - other.y);
    }

    public boolean isClose(Bomb other) {
        return distance(other) < Math.min(r, other.r);
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void destroy() {
        root.getChildren().remove(view);
        isKilled = true;
    }

}
