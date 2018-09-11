package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application {
    static List<Pair<Integer,Integer>> movesAroundForEvenRow = new ArrayList<>();
    static List<Pair<Integer,Integer>> movesAroundForOddRow = new ArrayList<>();
    public boolean bangMine;
    static int countOpenedHexagons;
    public final String FLAG_ICON = "F";
    public static int SIZE_OF_FIELD = 20;
    public static int numberOfMines = 30;
    public static int rowCoordinateBang;
    public static int columnCoordinateBang;
    public static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];
    public static boolean win = false;
    public static boolean bangAndLoss = false;
    public int countBombs = 0;
    Random random = new Random();
    static Group root = new Group();



    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("hexagonal deminer");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("#648f6e"));

        createField(root);


        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

    public void createField(Group root) {
        for (int row = 0; row < SIZE_OF_FIELD; row++) {
            for (int column = 0; column < SIZE_OF_FIELD; column++) {
                Hexagon hexagon = new Hexagon();

                field[row][column] = hexagon.createHexagon(row, column, 20, 2);
                field[row][column].rowCoordinate = row;
                field[row][column].columnСoordinate = column;
                root.getChildren().add(field[row][column]);

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
        hexagonsAroundEven();
        hexagonsAroundOdd();

        //Обход поля
        for (int row = 0; row < SIZE_OF_FIELD; row++)
            for (int column = 0; column < SIZE_OF_FIELD; column++)
                //проверка на отсутствие бомбы
                if (!field[row][column].getStatusMined()) {
                    //перебор по соседним шестиугольникам
                    if (row % 2 == 0) {
                        for (Pair<Integer, Integer> move : movesAroundForEvenRow)
                            //проверка на возможность хода
                            if (checkOutOfField(row, column, move))
                                //считаем ,если бомба
                                if (field[row + move.getKey()][column + move.getValue()].getStatusMined())
                                    field[row][column].numbersOfBombsNear++;
                    } else {
                        for (Pair<Integer, Integer> move : movesAroundForOddRow)
                            //проверка на возможность хода
                            if (checkOutOfField(row, column, move))
                                //считаем ,если бомба
                                if (field[row + move.getKey()][column + move.getValue()].getStatusMined())
                                    field[row][column].numbersOfBombsNear++;
                    }
                }

    }

    public static void openHexagons(int row, int column) {
        if (field[row][column].getStatusMined()) {
            //взрыв
            field[row][column].setFill(Paint.valueOf("#a6a6a6"));

            bangAndLoss = true;
            root.getChildren().add(paintBomb(field[row][column].rowPixelCoordinate, field[row][column].columnPixelCoordinate));
            restart();

        } else {  //отрисовка цифры
            if (field[row][column].getBombCount() > 0) {

                field[row][column].openHexagon();
                field[row][column].setFill(Paint.valueOf("#ffff33"));
                root.getChildren().add(paintString(field[row][column].rowPixelCoordinate + 4, field[row][column].columnPixelCoordinate - 3, field[row][column].getBombCount()));


            } else {
                //просто открытие
                if (field[row][column].notOpen() && !field[row][column].isFlag
                        && field[row][column].getBombCount() == 0) {

                    field[row][column].openHexagon();
                    field[row][column].setFill(Paint.valueOf("#a6a6a6"));

                    if (row % 2 == 0)
                    for (Pair<Integer, Integer> move : movesAroundForEvenRow) {
                        if (checkOutOfField(row, column, move)) {
                            openHexagons(row + move.getKey(), column + move.getValue());
                        }
                    } else {
                        for (Pair<Integer, Integer> move : movesAroundForOddRow) {
                            if (checkOutOfField(row, column, move)) {
                                openHexagons(row + move.getKey(), column + move.getValue());
                            }
                        }

                    }
                }
            }
        }
    }

    public static void invertFlag(int row, int column) {
        if (!field[row][column].isOpen ) {
            field[row][column].reverseFlag();
            if (field[row][column].isFlag) {
                root.getChildren().add(paintFlag(field[row][column].rowPixelCoordinate, field[row][column].columnPixelCoordinate));
            } else {
                root.getChildren().remove(paintFlag(field[row][column].rowPixelCoordinate, field[row][column].columnPixelCoordinate));
            }
        }
    }


    public static void hexagonsAroundOdd() {
        movesAroundForOddRow.add(new Pair<>(0, -1));
        movesAroundForOddRow.add(new Pair<>(-1, 0));
        movesAroundForOddRow.add(new Pair<>(-1, +1));
        movesAroundForOddRow.add(new Pair<>(0, +1));
        movesAroundForOddRow.add(new Pair<>(+1, +1));
        movesAroundForOddRow.add(new Pair<>(+1, 0));
    }

    public static void hexagonsAroundEven() {
        movesAroundForEvenRow.add(new Pair<>(0, -1));
        movesAroundForEvenRow.add(new Pair<>(-1, -1));
        movesAroundForEvenRow.add(new Pair<>(-1, 0));
        movesAroundForEvenRow.add(new Pair<>(0, +1));
        movesAroundForEvenRow.add(new Pair<>(+1, 0));
        movesAroundForEvenRow.add(new Pair<>(+1, -1));
    }


    public static boolean checkOutOfField(int row, int column, Pair<Integer, Integer> coordinates) {
        int incrementRow = row + coordinates.getKey();
        int incrementColumn = column + coordinates.getValue();
        return 0 <= incrementRow && incrementRow < SIZE_OF_FIELD &&
                0 <= incrementColumn && incrementColumn < SIZE_OF_FIELD;
    }

    public static Circle paintBomb(double rowPixel, double columnPixel) {
        Circle circle = new Circle();
        circle.setTranslateY(rowPixel);
        circle.setTranslateX(columnPixel);
        circle.setRadius(13);
        circle.setFill(Paint.valueOf("#000033"));
        return circle;
    }


    public static Text paintFlag(double rowPixel, double columnPixel) {
        Text text = new Text();
        text.setTranslateX(columnPixel);
        text.setTranslateY(rowPixel);
        text.setText("F");
        text.setFill(Paint.valueOf("#ff80bf"));
        return text;
    }
    public static Text paintString(double rowPixel, double columnPixel, int countOfBombs) {
        Text text = new Text();
        text.setTranslateX(columnPixel);
        text.setTranslateY(rowPixel);
        text.setText(Integer.toString(countOfBombs));
        text.setFill(Paint.valueOf("#00e64d"));
        text.setFont(Font.font ("Verdana", 20));
        return text;
    }

    public static void restart() {

    }

}
