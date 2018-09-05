package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application {
    public static List<Pair<Integer,Integer>> movesAround = new ArrayList<>();
    public boolean bangMine;
    public static int countOpenedHexagons;
    final String FLAG_ICON = "F";
    public static int SIZE_OF_FIELD = 20;
    public static int numberOfMines = 10;
    public static int rowCoordinateBang;
    public static int columnCoordinateBang;
    public static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];
    public static boolean win = false;
    public static boolean bangAndLoss = false;
    public int countBombs = 0;
    Random random = new Random();




    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("hexagonal deminer");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("#648f6e"));

        createField(root);

//        Circle circle = new Circle(6 * 40, 6 * 40, 13);
//        circle.setFill(Paint.valueOf("Red"));
//        root.getChildren().add(circle);

        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

    public void createField(Group root) {
        for (int row = 0; row < SIZE_OF_FIELD; row++) {
            for (int column = 0; column < SIZE_OF_FIELD; column++) {

                Hexagon hexagon = new Hexagon(20, 2);
                root.getChildren().add(hexagon.createHexagon(row, column, 20, 2));
                field[row][column] = hexagon;
                hexagon.rowCoordinate = row;
                hexagon.columnСoordinate = column;



                root.getChildren().add(hexagon);

            }
        }

        while (countBombs < numberOfMines) {
            int x;
            int y;
            do {
                 x = random.nextInt(SIZE_OF_FIELD);
                 y = random.nextInt(SIZE_OF_FIELD);
            } while (field[x][y].getStatusMined());
            field[x][y].setMineStatus();
            countBombs++;
        }


        hexagonsAround();
        //Обход поля
        for (int row = 0; row < SIZE_OF_FIELD; row++)
            for (int column = 0; column < SIZE_OF_FIELD; column++)
                //проверка на отсутствие бомбы
                if (!field[row][column].getStatusMined())
                    //перебор по соседним шестиугольникам
                    for (Pair<Integer, Integer> move : movesAround)
                        //проверка на возможность хода
                        if (checkOutOfField(row, column, move))
                            //считаем ,если бомба
                            if (field[row + move.getKey()][column + move.getValue()].getStatusMined())
                                field[row][column].numbersOfBombsNear++;





    }

    public static void openHexagons(int row, int column) {
        if (field[row][column].isNotOpen() && !bangAndLoss && field[row][column].getCountBomb() == 0)  {

            field[row][column].openHexagon();


            field[row][column].setFill(Paint.valueOf("#a6a6a6"));
            for (Pair<Integer, Integer> move : movesAround) {
                if (checkOutOfField(row, column, move)) {
                    field[row + move.getKey()][column + move.getValue()].color = "#a6a6a6";

                    openHexagons(row + move.getKey(), column + move.getValue());
                }
            }
        }

    }
//    void openCells(int x, int y) { // recursive procedure of opening the cells
//        if (x < 0 || x > FIELD_SIZE - 1 || y < 0 || y > FIELD_SIZE - 1) return; // wrong coordinates
//        if (!field[y][x].isNotOpen()) return; // cell is already open
//        field[y][x].open();
//        if (field[y][x].getCountBomb() > 0 || bangMine) return; // the cell is not empty
//        for (int dx = -1; dx < 2; dx++)
//            for (int dy = -1; dy < 2; dy++) openCells(x + dx, y + dy);
//    }


//    public static Hexagon repaintHexagon() {
//
//    }


    public static void hexagonsAround() {
        movesAround.add(new Pair<>(-1, -1));
        movesAround.add(new Pair<>(-1, 0));
        movesAround.add(new Pair<>(0, -1));
        movesAround.add(new Pair<>(0, +1));
        movesAround.add(new Pair<>(+1, -1));
        movesAround.add(new Pair<>(+1, 0));
    }

    public static boolean checkOutOfField(int row, int column, Pair<Integer, Integer> coordinates) {
        int incrementRow = row + coordinates.getKey();
        int incrementColumn = column + coordinates.getValue();
        return 0 <= incrementRow && incrementRow < SIZE_OF_FIELD &&
                0 <= incrementColumn && incrementColumn < SIZE_OF_FIELD;
    }

    public static void restart() {

    }

}
