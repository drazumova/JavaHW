package com.hw.cannon;

import javafx.scene.layout.*;
import javafx.scene.shape.*;

public class Cannon {

    private double x;
    private double y;
    private double phi;

    private Pane root;
    private Circle view;
    private Mount mount;

    private final double deltaPhi = Math.PI /10;
    private final double deltaX = 10;

    public Cannon(double x, double y, Pane pane, Mount mount){
        root = pane;
        this.mount = mount;
        this.x = x;
        this.y = y;
        phi = Math.PI /2;
        view = new Circle(x, y, 10);
        root.getChildren().add(view);
    }

    public void shot(){
        var bomb = new Bomb(x, y - 10, 1, root);
        bomb.fly(phi, mount);
    }

    private void updateView() {
        y = mount.getYCor(x);
        view.setCenterX(x);
        view.setCenterY(y);
    }

    public void moveLeft() {
        if (x - deltaX <= 0) {
            x = 0;
        } else {
            x -= deltaX;
        }
        updateView();
    }

    public void moveRight() {
        if (x + deltaX > 1000) {
            x = 1000;
        } else {
            x += deltaX;
        }
        updateView();
    }

    public void moveUp() {
        phi += deltaPhi;
    }

    public void moveDown() {
        phi -= deltaPhi;
    }
}

