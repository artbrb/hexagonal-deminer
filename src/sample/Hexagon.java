package sample;

import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static javafx.scene.paint.Paint.valueOf;
import static sample.Main.SIZE_OF_FIELD;
import static sample.Main.countOpenedCells;
import static sample.Main.field;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;



import javafx.scene.input.MouseEvent;

import java.awt.event.MouseListener;


public class Hexagon extends Polygon {
    private static final int BLOCK_SIZE = 20;
    private boolean isOpen = false;
    private boolean isMine;
    private boolean isFlag;
    private int numbersOfBombsNear;
    private boolean bangMine;
//    private final int intermediateDistance;
    private static int radius = 15;

    public int rowCounterHexagons =  (int) ceil(500 / (radius * 2));
    public int columnCounterHexagons = (int) ceil(500 / (radius * 2 * sqrt(3)));


    public static Polygon createHexagon(int column, int row, int radius, int intermediateDistance){
        Polygon hex = new Polygon();

        for (int i = 0; i < 6; i++) {

            hex.getPoints().add(radius * sin(i * PI / 3));
            hex.getPoints().add(radius * cos(i * PI / 3));

        }

        Circle cir = new Circle();
        cir.setCenterX(column);
        cir.setCenterY(row);
        cir.setRadius(20);


        double halfHeight = sqrt(3) * radius / 2;
        hex.setTranslateX((row * 2 - column % 2) * halfHeight + row * intermediateDistance + 36 );
        hex.setTranslateY(column*(intermediateDistance + radius*1.5) + 36);
        hex.setFill(Paint.valueOf("Gray"));

        return hex;
    }




    public static boolean mouseListener(MouseEvent event) {
        int x = (int) ceil (event.getSceneX() / (radius * 2));
        int y = (int) ceil (event.getSceneY() / (radius * 2 * sqrt(3)));
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
