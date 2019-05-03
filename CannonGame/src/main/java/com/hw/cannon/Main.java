package com.hw.cannon;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

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
        var target = new Bomb(randX, mount.getYCor(randX), 0, root);

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
                    var bomb = cannon.shot();
                    if (bomb.isClose(target)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle("congratulations");
                        alert.setHeaderText("END GAME");
                        alert.setContentText("You won!");

                        alert.showAndWait();
                    }
                    break;
                case Q:
                    cannon.nextType();
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("congratulations");
                    alert.setHeaderText("HELP");
                    alert.setContentText("You pushed wrong button\n" +
                            "Press RIGHT to move right\n" +
                            "Press LEFT to move left\n" +
                            "Press Q to change bomb type\n" +
                            "Press ENTER to shot\n" +
                            "Press UP to move muzzle right\n" +
                            "Press DOWN to move muzzle left\n");
                    alert.showAndWait();
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
