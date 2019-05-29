package com.task.tiktak;

import javafx.application.*;
import javafx.concurrent.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class Controller {
    private final GridPane gridPane;
    private final Button[][] buttons;
    private final int size;
    private List<Integer> openedX;
    private List<Integer> openedY;

    private final PairFind pairFind;

    public GridPane getGridPane() {
        return gridPane;
    }

    public Controller(int n, int sizeX, int sizeY) {
        gridPane = new GridPane();
        gridPane.setMaxSize(sizeX, sizeY);
        gridPane.setAlignment(Pos.CENTER);
        pairFind = new PairFind(n);
        size = n;

        openedX = new ArrayList<>();
        openedY = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            gridPane.addColumn(i);
            gridPane.addRow(i);
        }

        buttons = new Button[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setMinSize(sizeX/n, sizeY/n);
                gridPane.add(buttons[i][j], i, j);
                final int currentI = i;
                final int currentJ = j;
                buttons[i][j].setOnMouseClicked(event -> {
                    if (openedX.contains(currentI) && openedY.contains(currentJ)) {
                        clearOpened();
                        closeButton(currentI, currentJ);
                    } else if (openedX.isEmpty()) {
                        openedX.add(currentI);
                        openedY.add(currentJ);
                        clickButton(currentI, currentJ);
                    } else if (openedX.size() == 1) {
                        openedX.add(currentI);
                        openedY.add(currentJ);
                        checkChosen();
                    }
                });
            }
        }
    }

    private void clearOpened() {
        openedY.clear();
        openedX.clear();
    }

    private void checkEnd() {
        if (pairFind.isEnd()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("congratulations");
            alert.setContentText("YOU WON");
            alert.showAndWait();
        }
        clearOpened();
    }

    private void makeAll(boolean state) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setDisable(state);
            }
        }
    }

    private void checkChosen() {
        for (int i = 0; i < 2; i++) {
            openButton(openedX.get(i), openedY.get(i));
        }

        if (pairFind.same(openedX.get(0), openedY.get(0), openedX.get(1), openedY.get(1))) {
            for (int i = 0; i < 2; i++) {
                buttons[openedX.get(i)][openedY.get(i)].setDisable(true);
            }
            pairFind.closePair();
            checkEnd();
        }

        clearOpened();

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
                makeAll(true);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    closeButton(i, j);
                }
            }
            makeAll(false);
        });
        new Thread(sleeper).start();




    }

    private void clickButton(int i, int j) {
        synchronized (buttons){
            buttons[i][j].setText("Clicked");
        }
    }

    private void closeButton(int i, int j) {
        synchronized (buttons) {
            buttons[i][j].setText("");
        }
    }

    private void openButton(int i, int j) {
        synchronized (buttons) {
            buttons[i][j].setText(pairFind.getNum(i, j) + "");
        }
    }
}
