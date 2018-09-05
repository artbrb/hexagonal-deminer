package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import static java.lang.Math.PI;
import static java.lang.StrictMath.*;
import static sample.Main.*;


public class Hexagon extends Polygon {
    private static final int BLOCK_SIZE = 20;
    public boolean isOpen = false;
    public boolean isMine = false;
    public boolean isFlag = false;
    public int numbersOfBombsNear = 0;
    private int intermediateDistance;
    private int radius;
    public int columnСoordinate;
    public int rowCoordinate;
//    public int rowCounterHexagons =  (int) ceil(500 / (radius * 2));
//    public int columnCounterHexagons = (int) ceil(500 / (radius * 2 * sqrt(3)));


    Hexagon(int radius, int intermediateDistance) {
        this.isOpen = isOpen;
        this.isMine = isMine;
        this.isFlag = isFlag;
        this.numbersOfBombsNear = numbersOfBombsNear;
        this.radius = radius;
        this.intermediateDistance = intermediateDistance;
        this.columnСoordinate = columnСoordinate;
        this.rowCoordinate = rowCoordinate;


    }


    public Polygon createHexagon(int row, int column, int radius, int intermediateDistance) {
        Polygon hexagon = new Polygon();

        for (int i = 0; i < 6; i++) {
            hexagon.getPoints().add(radius * sin(i * PI / 3));
            hexagon.getPoints().add(radius * cos(i * PI / 3));
        }

        double halfHeight = sqrt(3) * radius / 2;
        hexagon.setTranslateX((column * 2 - row % 2) * halfHeight + column * intermediateDistance + 36);
        hexagon.setTranslateY(row * (intermediateDistance + radius * 1.5) + 36);
        hexagon.setFill(Paint.valueOf("#666699"));


        hexagon.setOnMousePressed((MouseEvent event) -> {
            if (!bangAndLoss && !win) {
                if (event.isPrimaryButtonDown()) {
                    if (isNotOpen()) {
                        openHexagon();
                        boolean check = countOpenedHexagons == ((SIZE_OF_FIELD * SIZE_OF_FIELD) - numberOfMines);
                        win = check;
                        if (bangAndLoss) {
                            rowCoordinateBang = rowCoordinate;
                            columnCoordinateBang = columnСoordinate;
                            hexagon.setFill(Paint.valueOf("Red"));
                        }
                    }
                }
                if (event.isSecondaryButtonDown()) {
                    invertFlag();
                }

                if (bangAndLoss || win) {
                    restart();
                }
            }
        });

        hexagon.setOnMouseExited((MouseEvent event) -> {
            if (numbersOfBombsNear == 0 && isNotOpen() && !getStatusMined()) {
                hexagon.setFill(Paint.valueOf("#a6a6a6"));
            }
            });


        return hexagon;
    }




    public  void openHexagon() {
        if (!isMine) countOpenedHexagons++;
        bangAndLoss = isMine;
        isOpen = true;

    }

    boolean isNotOpen() {
        return !isOpen;
    }

    boolean setMineStatus() {
        return isMine = true;
    }

    boolean getStatusMined() {
        return isMine;
    }

    public boolean invertFlag() {
        return isFlag = !isFlag;
    }

    void setCountBomb(int count) {
        numbersOfBombsNear = count;
    }

    int getCountBomb() {
        return numbersOfBombsNear;
    }

    void paintingBomb() {

    }


    public static Circle paintingString(int row, int column) {
        Circle circle = new Circle(row * 40, column * 40, 13);
        circle.setFill(Paint.valueOf("Red"));
        return circle;
    }


}
