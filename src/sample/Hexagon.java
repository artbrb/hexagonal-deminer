package sample;

import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.incrementExact;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static javafx.scene.paint.Paint.valueOf;
import static sample.Main.SIZE_OF_FIELD;
import static sample.Main.countOpenedCells;
import static sample.Main.field;

import javafx.event.EventHandler;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.event.MouseListener;


public class Hexagon extends Polygon {
    private static final int BLOCK_SIZE = 20;
    private boolean isOpen = false;
    private boolean isMine = false;
    private boolean isFlag = false;
    private int numbersOfBombsNear = 0;
    private boolean bangMine;
    private int intermediateDistance;
    private int radius;
//    public int rowCounterHexagons =  (int) ceil(500 / (radius * 2));
    public int columnCounterHexagons = (int) ceil(500 / (radius * 2 * sqrt(3)));

    Hexagon( int numbersOfBombsNear , int radius, int intermediateDistance) {
        this.isOpen = isOpen;
        this.isMine = isMine;
        this.isFlag = isFlag;
        this.numbersOfBombsNear = numbersOfBombsNear;
        this.radius = radius;
        this.intermediateDistance = intermediateDistance;

//        createHexagon(SIZE_OF_FIELD, SIZE_OF_FIELD, radius, intermediateDistance);

    }


    public static Polygon createHexagon(int column, int row, int radius, int intermediateDistance){
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

        hex.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (EventHandler) e -> hex.setFill(Paint.valueOf("Red")));

//        cir.setRadius(50);
//        cir.setTranslateX((row * 2 - column % 2) * halfHeight + row * intermediateDistance + 36 );
//        cir.setTranslateY(column*(intermediateDistance + radius*1.5) + 36);
//        cir.setFill(Paint.valueOf("Blue"));


        return hex;

    }

    public static Polygon paintHex(Hexagon hexagon) {
         hexagon.setFill(Paint.valueOf("White"));
        return hexagon;
    }

//    public static Circle createCircle(int column, int row, int radius, int intermediateDistance) {
//        Circle cir = new Circle();
//        double halfHeight = sqrt(3) * radius / 2;
//        cir.setRadius(50);
//        cir.setTranslateX((row * 2 - column % 2) * halfHeight + row * intermediateDistance + 36 );
//        cir.setTranslateY(column*(intermediateDistance + radius*1.5) + 36);
//        cir.setFill(Paint.valueOf("Blue"));
//        return cir;
//    }




    public static boolean mouseListener(MouseEvent event) {
        int x = (int) ceil (event.getSceneX() / (18 * 2));
        int y = (int) ceil (event.getSceneY() / (18 * 2 * sqrt(3)));
        if (x < SIZE_OF_FIELD && y < SIZE_OF_FIELD && x >= 0 && y >= 0) {
//            field[x][y].setFill(Paint.valueOf("Red"));
//            field[x][y].isOpen = true;
//            return field[x][y].isOpen;
            return true;
        }
//        return field[x][y].isOpen;
        return true;
    }
    void openHexagon() {
        isOpen = true;
        bangMine = isMine;
        countOpenedCells++;
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
