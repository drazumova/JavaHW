package com.hw.cannon;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.security.*;

import static java.lang.Integer.min;


public class Main extends Application {

    /**
     * Class to store unique game elements on which depends moving and drawing of other objects
     */
    public static class GameElements {
        private final @NotNull Pane pane;
        private final @NotNull Mount mount;
        private final @NotNull Bomb target;

        private final int paneSize;
        private final int mountSize;

        private static class GameElementsHolder {
            private static final GameElements gameElements = new GameElements();
        } 
        
        public static GameElements getInstance() {
            return GameElementsHolder.gameElements;
        }

        private GameElements() {
            var device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = device.getDisplayMode().getWidth();
            int height = device.getDisplayMode().getHeight();
            paneSize = min(width/2, height/2);
            mountSize = paneSize;

            pane = new Pane();
            pane.setMaxSize(width, height);
            pane.setMinSize(paneSize, paneSize);
            pane.setCenterShape(true);
            pane.setPrefSize(paneSize, mountSize);
            mount = new Mount(paneSize, pane);
            var random = new SecureRandom();
            double randX = random.nextDouble() * paneSize;
            target = new Bomb(randX, mount.getYCoordinate(randX), 0, pane);
        }
        
        public Pane getPane() {
            return pane;
        }

        public Mount getMount() {
            return mount;
        }

        public Bomb getTarget() {return target; }

        public int getPaneSize() {
            return paneSize;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        int size = GameElements.getInstance().paneSize;

        var cannon = new Cannon(0.0, GameElements.getInstance().getMount().getYCoordinate(0.0));
        var scene = new Scene(GameElements.getInstance().getPane(), size, size);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PUSHKA");
        primaryStage.setMinHeight(GameElements.getInstance().mountSize);
        primaryStage.setMinWidth(GameElements.getInstance().paneSize);
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
                    break;
                case Q:
                    cannon.nextType();
                    break;
            }
        });

        primaryStage.show();

        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("some info");
        alert.setHeaderText("HELP");
        alert.setContentText("You pushed wrong button\n" +
                "Press RIGHT to move right\n" +
                "Press LEFT to move left\n" +
                "Press Q to change bomb type\n" +
                "Press ENTER to shot\n" +
                "Press UP to move muzzle right\n" +
                "Press DOWN to move muzzle left\n");
        alert.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
