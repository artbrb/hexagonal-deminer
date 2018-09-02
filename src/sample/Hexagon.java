package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import static java.lang.Math.PI;
import static java.lang.StrictMath.*;
import static javafx.scene.paint.Paint.valueOf;
import static sample.Main.*;


public class Hexagon extends Polygon {
    private static final int BLOCK_SIZE = 20;
    private boolean isOpen = false;
    private boolean isMine = false;
    private boolean isFlag = false;
    private int numbersOfBombsNear = 0;
    private boolean bangMine;
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


    public  Polygon createHexagon(int column, int row, int radius, int intermediateDistance) {
        Polygon hex = new Polygon();
        Circle cir = new Circle();

        for (int i = 0; i < 6; i++) {

            hex.getPoints().add(radius * sin(i * PI / 3));
            hex.getPoints().add(radius * cos(i * PI / 3));

        }

        double halfHeight = sqrt(3) * radius / 2;
        hex.setTranslateX((row * 2 - column % 2) * halfHeight + row * intermediateDistance + 36 );
        hex.setTranslateY(column*(intermediateDistance + radius*1.5) + 36);
        hex.setFill(Paint.valueOf("Gray"));


        hex.setOnMousePressed((MouseEvent event) -> {
            if (!bangMine && !win) {
                if (event.isPrimaryButtonDown()) {
                    if (field[columnСoordinate][rowCoordinate].isNotOpen()) {
                        openHexagons(columnСoordinate, rowCoordinate);
                        boolean check = countOpenedHexagons == ((SIZE_OF_FIELD * SIZE_OF_FIELD) - numberOfMines);
                        win = check;
                        if (bangAndLoss) {
                            xCoordinateBang = columnСoordinate;
                            yCoordinateBang = rowCoordinate;
                        }
                    }
            }
            if (event.isSecondaryButtonDown()) {
                    field[columnСoordinate][rowCoordinate].invertFlag();
            }
            if (bangMine || win) {
                    restart();
            }

            }
        });



        cir.setRadius(50);
        cir.setTranslateX((row * 2 - column % 2) * halfHeight + row * intermediateDistance + 36 );
        cir.setTranslateY(column*(intermediateDistance + radius*1.5) + 36);
        cir.setFill(Paint.valueOf("Blue"));


        return hex;

    }





    void openHexagon() {
        isOpen = true;
        bangMine = isMine;
        countOpenedHexagons++;
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

    void invertFlag() {
        isFlag = !isFlag;
    }

     void setCountBomb(int count) {
        numbersOfBombsNear = count;
    }
    int getCountBomb() {
        return numbersOfBombsNear;
    }

    void paintingBomb() {

    }

    public static Circle paintBomb(int x, int y) {
        Circle circle = new Circle(x, y, 35);
        circle.setFill(valueOf("Green"));
        return circle;
    }

    void paintingString() {

    }



}
