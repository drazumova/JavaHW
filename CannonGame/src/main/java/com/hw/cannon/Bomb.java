package com.hw.cannon;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.*;

import java.util.*;

/**
 * Sturcture for 'bullets' of the cannon.
 * I didnt completly understand how weight of a bomb will affect its flight
 * so instead of weight they have different speed.
 */
public class Bomb {
    private double x;
    private double y;
    private double r;
    private double speed;
    private final Circle view;

    private static class Parameters {
        private final double radius;
        private final Color color;
        private final double viewRadius;
        private final double speed;

        private Parameters(double radius, Color color, double modelRadius, double speed) {
            this.radius = radius;
            this.color = color;
            viewRadius = modelRadius;
            this.speed = speed;
        }
    }

    private static final List<Parameters> types;

    static {
        types = new ArrayList<>();
        types.add(new Parameters(30, Color.RED, 11, 10));
        types.add(new Parameters(100, Color.MIDNIGHTBLUE, 5, 20));
        types.add(new Parameters(70, Color.INDIGO, 8, 30));
        types.add(new Parameters(50, Color.DARKMAGENTA, 10, 40));
    }

    /**
     * Returns count of default bomb types
     */
    public static int getTypesCount() {
        return types.size();
    }

    /**
     * Creates a new bomb with given parameters
     */
    public Bomb(double x, double y, double radius, Color color, double viewRadius, double speed) {
        this.x = x;
        this.y = y;
        view = new Circle(x, y, 1);
        view.setRadius(viewRadius);
        view.setFill(color);
        this.speed = speed;
        r = radius;

        Main.GameElements.getPane().getChildren().add(view);
    }

    /**
     * Creates a new bomb with default parameters from types element number type
     */
    public Bomb(double x, double y, int type) {
        this(x, y, types.get(type).radius, types.get(type).color, types.get(type).viewRadius, types.get(type).speed);
    }

    /**
     * Returns distance between this bomb and other
     */
    public double distance(Bomb other) {
        return (x - other.x) * (x - other.x) + (y - other.y)*(y - other.y);
    }

    /**
     * Tells if this bomb and other are within each other's blast radius
     */
    public boolean isClose(Bomb other) {
        return distance(other) < Math.min(r * r, other.r * other.r);
    }

    private double yConvert(double y) {
        return Main.GameElements.getPane().getBoundsInLocal().getHeight() - y;
    }

    /**
     * Simulate and draws flight of the bomb with given angle and relatively given mount
     */
    public void fly(double phi) {

        var path = new Path();
        var pathTransition = new PathTransition();

        var targetX = x;
        var targetY = yConvert(y);
        var t = 0.01;
        var deltaT = 0.001;

        do {
            targetX = x + t * StrictMath.cos(phi) * speed;
            targetY = y + t * StrictMath.sin(phi) * speed - 0.5 * 10 * t * t;
            t += deltaT;
        } while (Main.GameElements.getMount().isUnder(targetX, yConvert(targetY)));

        var flyTime = 2 * speed * StrictMath.cos(phi) / 10;
        var focusX = x + speed * flyTime / 2;
        var focusY = y - Math.abs(focusX - x) / StrictMath.tan(phi);

        path.getElements().add(new MoveTo(x, y));
        path.getElements().add(new QuadCurveTo(focusX, yConvert(y), targetX, yConvert(targetY)));

        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(view);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setAutoReverse(true);

        pathTransition.play();
        x = targetX;
        y = yConvert(targetY);
    }
}
