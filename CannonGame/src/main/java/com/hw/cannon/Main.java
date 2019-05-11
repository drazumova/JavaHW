package com.hw.cannon;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.security.*;
import java.util.*;


public class Main extends Application {

    /**
     * Class to store unique game elements on which depends moving and drawing of other objects
     */
    public static class GameElements {
        private static final Pane pane;
        private static final Mount mount;

        private static final int MAX_PANE_SIZE = 1000;
        private static final int MAX_MOUNT_SIZE = 1000;
        
        static {
            pane = new Pane();
            pane.setMaxSize(MAX_PANE_SIZE, MAX_PANE_SIZE);
            mount = new Mount(MAX_PANE_SIZE, pane);
        }
        
        public static Pane getPane() {
            return pane;
        }

        public static Mount getMount() {
            return mount;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        var random = new SecureRandom();
        double randX = random.nextDouble() * 1000;
        var cannon = new Cannon(0.0, GameElements.getMount().getYCor(0.0));
        var target = new Bomb(randX, GameElements.getMount().getYCor(randX), 0);

        primaryStage.setScene(new Scene(GameElements.getPane(),
                GameElements.MAX_PANE_SIZE, GameElements.MAX_PANE_SIZE));
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

    public static void main(String... args) {
        launch(args);
    }
}
