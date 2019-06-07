package com.hw.cannon;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.jetbrains.annotations.*;

import java.security.*;


public class Main extends Application {

    /**
     * Class to store unique game elements on which depends moving and drawing of other objects
     */
    public static class GameElements {
        private final @NotNull Pane pane;
        private final @NotNull Mount mount;
        private final @NotNull Bomb target;

        private final int maxPaneSize = 1000;
        private final int maxMountSize = 1000;
        
        private static class GameElementsHolder {
            private static final GameElements gameElements = new GameElements();
        } 
        
        public static GameElements getInstance() {
            return GameElementsHolder.gameElements;
        }

        private GameElements() {
            pane = new Pane();
            pane.setMaxSize(maxPaneSize, maxPaneSize);
            mount = new Mount(maxPaneSize, pane);
            var random = new SecureRandom();
            double randX = random.nextDouble() * 1000;
            target = new Bomb(randX, mount.getYCoordinate(randX), 0, pane);
        }
        
        public Pane getPane() {
            return pane;
        }

        public Mount getMount() {
            return mount;
        }

        public Bomb getTarget() {return target; }

        public int getMaxPaneSize() {
            return maxPaneSize;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        var cannon = new Cannon(0.0, GameElements.getInstance().getMount().getYCoordinate(0.0));
        var target = GameElements.getInstance().getTarget();

        primaryStage.setScene(new Scene(GameElements.getInstance().getPane(),
                GameElements.getInstance().getMaxPaneSize(), GameElements.getInstance().getMaxPaneSize()));
        primaryStage.setTitle("PUSHKA");
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
                default:
                    var alert = new Alert(Alert.AlertType.INFORMATION);

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
