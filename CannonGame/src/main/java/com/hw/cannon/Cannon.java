package com.hw.cannon;

import javafx.scene.shape.*;
import org.jetbrains.annotations.*;

/**
 * Class for interaction this cannon
 */
public class Cannon {

    private double x;
    private double y;
    private double phi;

    private @NotNull Circle view;
    private @NotNull Line lineView;
    private int type;

    private static final double LENGTH_LINE = 20;
    private static final double DELTA_PHI = Math.PI / 20;
    private static final double DELTA_X = 5;

    /**
     * Creates new cannon
     */
    public Cannon(double x, double y) {
        type = 1;
        this.x = x;
        this.y = y;
        phi = 3 * Math.PI / 2;
        view = new Circle(x, y, 10);
        lineView = new Line(x, y,
                x + LENGTH_LINE * StrictMath.cos(phi), y + LENGTH_LINE * StrictMath.sin(phi));
        Main.GameElements.getInstance().getPane().getChildren().addAll(view, lineView);
    }

    /**
     * Drops a bomb by angle of the muzzle
     */
    public Bomb shot() {
        var bomb = new Bomb(x, y, type, Main.GameElements.getInstance().getPane());
        bomb.fly(2 * Math.PI - phi);
        return bomb;
    }

    private void updateViewCannon() {
        y = Main.GameElements.getInstance().getMount().getYCoordinate(x);
        view.setCenterX(x);
        view.setCenterY(y);
        lineView.setStartX(x);
        lineView.setStartY(y);
        updateViewLine();
    }

    private void updateViewLine() {
        lineView.setEndX(x + LENGTH_LINE * StrictMath.cos(phi));
        lineView.setEndY(y + LENGTH_LINE * StrictMath.sin(phi));
    }

    /**
     * Moves cannon left by DELTA_X
     */
    public void moveLeft() {
        x -= DELTA_X;
        if (x <= 0) {
            x = 0;
        }

        updateViewCannon();
    }

    /**
     * Moves cannon right by DELTA_X
     */
    public void moveRight() {
        x += DELTA_X;

        if (x >= Main.GameElements.getInstance().getMount().getMaxXCoordinate()) {
            x = Main.GameElements.getInstance().getMount().getMaxXCoordinate();
        }

        updateViewCannon();
    }

    /**
     * Moves muzzle right by DELTA_PHI
     */
    public void moveUp() {
        phi += DELTA_PHI;

        if (phi >= 2 * Math.PI) {
            phi = 2 * Math.PI;
        }

        updateViewLine();
    }

    /**
     * Moves muzzle left by DELTA_PHI
     */
    public void moveDown() {
        phi -= DELTA_PHI;

        if (phi <= Math.PI) {
            phi = Math.PI;
        }

        updateViewLine();
    }

    /**
     * Switchs type of cannons bomb to next in cyclic order
     */
    public void nextType() {
        type++;
        if (type >= Bomb.getTypesCount()) {
            type = 1;
        }
    }
}

