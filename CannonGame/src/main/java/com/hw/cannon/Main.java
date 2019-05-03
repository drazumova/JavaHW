package com.hw.cannon;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

import java.lang.annotation.*;
import java.util.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        var root = new Pane();
        root.setMaxSize(1000, 1000);

        var mount = new Mount(1000, root);
        var random = new Random();
        double randX = random.nextDouble() * 1000;
        var cannon = new Cannon(0.0, mount.getYCor(0.0), root, mount);
        var target = new Bomb(randX, mount.getYCor(randX), 3, root);

        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.setTitle("kek game");
        primaryStage.getScene().setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case UP:
                    cannon.moveUp();
                    break;
                case DOWN:
                    cannon.moveDown();
                    break;
                case LEFT:
                    cannon.moveLeft();
                    break;
                case RIGHT:
                    cannon.moveRight();
                    break;
                case ENTER:
                    System.out.println("SHOT! SHOT! SHOT!");
                    cannon.shot();
                    break;
                default:
                    System.out.println("Wrong button");
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
