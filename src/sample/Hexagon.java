package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import static java.lang.Math.PI;
import static java.lang.StrictMath.*;
import static sample.Main.*;

public class Hexagon extends Polygon {
    public boolean isOpen = false;
    private boolean isMine = false;
    public boolean isFlag = false;
    public int numbersOfBombsNear;
    public double rowPixelCoordinate;
    public double columnPixelCoordinate;
    public int columnСoordinate;
    public int rowCoordinate;

    public Hexagon createHexagon(int row, int column, int radius, int intermediateDistance) {
        Hexagon hexagon = new Hexagon();
        for (int i = 0; i < 6; i++) {
            hexagon.getPoints().add(radius * sin(i * PI / 3));
            hexagon.getPoints().add(radius * cos(i * PI / 3));
        }

        double halfDiameter = sqrt(3) * radius / 2;
        hexagon.columnPixelCoordinate = (column * 2 - row % 2) * halfDiameter + column * intermediateDistance + 100;
        hexagon.rowPixelCoordinate = row * (intermediateDistance + radius * 1.5) + 100;

        hexagon.setTranslateX(hexagon.columnPixelCoordinate);
        hexagon.setTranslateY(hexagon.rowPixelCoordinate);
        hexagon.setFill(Paint.valueOf("#666699"));

        hexagon.setOnMousePressed((MouseEvent event) -> {
            if (!bangAndLoss && !win) {
                if (event.isPrimaryButtonDown() && !hexagon.getStatusFlag()) {
                    openHexagons(hexagon.rowCoordinate, hexagon.columnСoordinate);
                    win = (flaggedBomb == NUMBER_OF_MINES) &&
                            (countOpenedHexagons == ((SIZE_OF_FIELD * SIZE_OF_FIELD) - NUMBER_OF_MINES));
                }
                if (event.isSecondaryButtonDown()) {
                    invertFlag(hexagon.rowCoordinate, hexagon.columnСoordinate);
                }
            }
            if (win) {
                endAndRestart("The game is won!" + "\n" + "Press OK to restart");
            }

        });

        return hexagon;
    }

    public  void openHexagon() {
        if (!isMine) countOpenedHexagons++;
        bangAndLoss = isMine;
        isOpen = true;
    }

    boolean notOpen() {
        return !isOpen;
    }

    void setMineStatus() {
        isMine = true;
    }

    boolean getStatusMined() {
        return isMine;
    }
    boolean getStatusFlag() {
        return isFlag;
    }

    public void reverseFlag() {
        isFlag = !isFlag;
    }

    int getBombCount() {
        return numbersOfBombsNear;
    }
}
