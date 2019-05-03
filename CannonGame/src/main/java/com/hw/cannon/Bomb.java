package com.hw.cannon;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.*;

import java.util.*;

public class Bomb {
    private double x;
    private double y;
    private double r;
    private double range;
    private final Pane root;
    private Circle view;

    public static class Parameters {
        private final double radius;
        private final Color color;
        private final double range;
        private final double viewRadius;

        public Parameters(double radius, double range, Color color, double modelRadius) {
            this.radius = radius;
            this.color = color;
            this.range = range;
            viewRadius = modelRadius;
        }
        void apply(Bomb bomb) {
            bomb.r = radius;
            bomb.range = range;
            bomb.view.setFill(color);
            bomb.view.setRadius(viewRadius);
        }

    }

    public static ArrayList<Parameters> types;

    static {
        types = new ArrayList<>();
        types.add(new Parameters(100, 0, Color.RED, 11));
        types.add(new Parameters(50, 100, Color.MIDNIGHTBLUE, 5));
        types.add(new Parameters(70, 80, Color.INDIGO, 8));
        types.add(new Parameters(40, 50, Color.DARKMAGENTA, 10));
    }

    public Bomb(double x, double y, int type, Pane pane) {
        root = pane;
        this.x = x;
        this.y = y;
        view = new Circle(x, y, 1);

        types.get(type).apply(this);

        root.getChildren().add(view);
    }

    public double distance(Bomb other) {
        return (x - other.x) * (x - other.x) + (y - other.y)*(y - other.y);
    }

    public boolean isClose(Bomb other) {
        return distance(other) < Math.min(r * r, other.r * other.r);
    }

    public void fly(double phi, Mount mount) {
        var path = new Path();
        var pathTransition = new PathTransition();

        double targetX;
        if (phi >= 3 * Math.PI/2) {
            targetX = x + range;
        } else {
            targetX = x - range;
        }
        var targetY = mount.getYCor(targetX);

        int n = 1000;

        path.getElements().add(new MoveTo(x, y));
        path.getElements().add(new QuadCurveTo(x + range / 2 * StrictMath.cos(phi),
                y + range / 2 * StrictMath.sin(phi), targetX, targetY));

        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(view);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setAutoReverse(true);

        pathTransition.play();
        x = targetX;
        y = targetY;
    }

}
