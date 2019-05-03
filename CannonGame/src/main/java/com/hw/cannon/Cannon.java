package com.hw.cannon;

import javafx.scene.layout.*;
import javafx.scene.shape.*;

public class Cannon {

    private double x;
    private double y;
    private double phi;

    private Pane root;
    private Circle view;
    private Line lineView;
    private Mount mount;
    private int type;

    private static final double lenghtLine = 20;
    private final double deltaPhi = Math.PI /10;
    private static final double deltaX = 10;

    public Cannon(double x, double y, Pane pane, Mount mount){
        root = pane;
        type = 1;
        this.mount = mount;
        this.x = x;
        this.y = y;
        phi = 3 * Math.PI / 2;
        view = new Circle(x, y, 10);
        lineView = new Line(x, y,
                x + lenghtLine * StrictMath.cos(phi), y + lenghtLine * StrictMath.sin(phi));
        root.getChildren().addAll(view, lineView);
    }

    public Bomb shot(){
        var bomb = new Bomb(x, y, type, root);
        bomb.fly(phi, mount);
        return bomb;
    }

    private void updateViewCannon() {
        y = mount.getYCor(x);
        view.setCenterX(x);
        view.setCenterY(y);
        lineView.setStartX(x);
        lineView.setStartY(y);
        updateViewLine();
    }

    private void updateViewLine() {
        lineView.setEndX(x + lenghtLine * StrictMath.cos(phi));
        lineView.setEndY(y + lenghtLine * StrictMath.sin(phi));
    }

    public void moveLeft() {
        x -= deltaX;
        if (x <= 0) {
            x = 0;
        }

        updateViewCannon();
    }

    public void moveRight() {
        x += deltaX;

        if (x > 1000) {
            x = 1000;
        }

        updateViewCannon();
    }

    public void moveUp() {
        phi += deltaPhi;

        if (phi >= 2 * Math.PI) {
            phi = 2 * Math.PI;
        }

        updateViewLine();
    }

    public void moveDown() {
        phi -= deltaPhi;

        if (phi <= Math.PI) {
            phi = Math.PI;
        }

        updateViewLine();
    }

    public void nextType() {
        type++;
        if (type >= Bomb.types.size()) {
            type = 1;
        }
    }
}

