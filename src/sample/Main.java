package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Main extends Application {
    static int firstClickAlert = 0;
    static int firstClickRow = 0;
    static int firstClickColumn = 0;
    static int countOpenedHexagons = 0;
    private final static String FLAG_ICON = "F";
    static int SIZE_OF_FIELD = 24;
    private static int RADIUS = 16;
    private static int BOMB_RADIUS = 10;
    private static int STRING_SIZE = 18;
    static int NUMBER_OF_MINES = 60;
    static int flaggedBomb;
    static boolean win = false;
    static boolean bangAndLoss = false;
    private static int countBombs = 0;

    private static List<Pair<Integer, Integer>> movesAroundForEvenRow = new ArrayList<>();
    private static List<Pair<Integer, Integer>> movesAroundForOddRow = new ArrayList<>();
    private static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];
    private static Text[][] flagField = new Text[SIZE_OF_FIELD][SIZE_OF_FIELD];
    private final static String[] COLOR_OF_NUMBERS = {"#0000ff", "#009900", "#801a00", "#944dff", "#ff00ff", "#ff751a"};

    private static Random random = new Random();
    private static Group root = new Group();
    private static HBox box = new HBox();
    private static Label label = new Label("Select your difficulty level");
    private static Button easyButton = new Button("Easy");
    private static Button normalButton = new Button("Normal");
    private static Button hardButton = new Button("Hard");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        box.getChildren().addAll(label, easyButton, normalButton, hardButton);
        box.setMinWidth(100);
        box.setMinHeight(100);
        box.setSpacing(10);
        box.setPadding(new Insets(10));
        root.getChildren().addAll(box);
        buttons();

        primaryStage.setTitle("hexagonal minesweeper");
        Scene scene = new Scene(root, 900, 850);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("#648f6e"));
        createField(root);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void createField(Group root) {

        for (int row = 0; row < SIZE_OF_FIELD; row++) {
            for (int column = 0; column < SIZE_OF_FIELD; column++) {
                Hexagon hexagon = new Hexagon();
                field[row][column] = hexagon.createHexagon(row, column, RADIUS, 5);
                field[row][column].rowCoordinate = row;
                field[row][column].columnСoordinate = column;
                root.getChildren().add(field[row][column]);
            }
        }
    }
    static void placementAndCountingBombs(int firstClickRow, int firstClickColumn) {

        while (countBombs < NUMBER_OF_MINES) {
            int x;
            int y;
            do {
                x = random.nextInt(SIZE_OF_FIELD);
                y = random.nextInt(SIZE_OF_FIELD);
            } while (field[x][y].getStatusMined());
            if (x != firstClickRow && y != firstClickColumn) {
                field[x][y].setMineStatus();
                countBombs++;
            }
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

    static void openHexagons(int row, int column) {
        if (field[row][column].getStatusMined()) {
            //взрыв
            bangAndLoss = true;
            for (int r = 0; r < SIZE_OF_FIELD; r++)
                for (int c = 0; c < SIZE_OF_FIELD; c++) {
                    if (field[r][c].getStatusMined()) {
                        field[r][c].setFill(Paint.valueOf("#666699"));
                        root.getChildren().add(paintBomb(field[r][c].rowPixelCoordinate,
                                field[r][c].columnPixelCoordinate));
                    }
                }

            field[row][column].setFill(Paint.valueOf("Red"));
            endAndRestart("The game is lost!" + "\n" + "Press OK to restart");
            //отрисовка цифры
        } else {
            if (field[row][column].getBombCount() > 0 && field[row][column].notOpen()) {

                field[row][column].openHexagon();
                field[row][column].setFill(Paint.valueOf("#ffff33"));
                root.getChildren().add(paintString(field[row][column].rowPixelCoordinate + 8,
                        field[row][column].columnPixelCoordinate - 8, field[row][column].getBombCount()));
            } else {
                //просто открытие
                if (field[row][column].notOpen() && !field[row][column].isFlag
                        && field[row][column].getBombCount() == 0) {
                    field[row][column].openHexagon();
                    field[row][column].setFill(Paint.valueOf("#a6a6a6"));
                    if (row % 2 == 0) {
                        for (Pair<Integer, Integer> move : movesAroundForEvenRow) {
                            if (checkOutOfField(row, column, move) &&
                                    !field[row + move.getKey()][column + move.getValue()].getStatusFlag()) {
                                openHexagons(row + move.getKey(), column + move.getValue());
                            }
                        }
                    } else {
                        for (Pair<Integer, Integer> move : movesAroundForOddRow) {
                            if (checkOutOfField(row, column, move) &&
                                    !field[row + move.getKey()][column + move.getValue()].getStatusFlag()) {
                                openHexagons(row + move.getKey(), column + move.getValue());
                            }
                        }
                    }
                }
            }
        }
    }

    static void invertFlag(int row, int column) {
        if (!field[row][column].isOpen) {
            field[row][column].reverseFlag();
            if (field[row][column].isFlag) {
                if (field[row][column].getStatusMined()) flaggedBomb++;
                flagField[row][column] = paintFlag(field[row][column].rowPixelCoordinate + 7,
                        field[row][column].columnPixelCoordinate - 5);
                root.getChildren().add(flagField[row][column]);
            } else {
                if (field[row][column].getStatusMined())
                    flaggedBomb = flaggedBomb - 1;
                root.getChildren().remove(flagField[row][column]);
            }
        }
        win = (flaggedBomb == NUMBER_OF_MINES) &&
                (countOpenedHexagons == ((SIZE_OF_FIELD * SIZE_OF_FIELD) - NUMBER_OF_MINES));

        if (win) {
            endAndRestart("The game is won!");
        }
    }


    private static void hexagonsAroundEven() {
        movesAroundForEvenRow.add(new Pair<>(0, -1));
        movesAroundForEvenRow.add(new Pair<>(-1, 0));
        movesAroundForEvenRow.add(new Pair<>(-1, +1));
        movesAroundForEvenRow.add(new Pair<>(0, +1));
        movesAroundForEvenRow.add(new Pair<>(+1, +1));
        movesAroundForEvenRow.add(new Pair<>(+1, 0));
    }

    private static void hexagonsAroundOdd() {
        movesAroundForOddRow.add(new Pair<>(0, -1));
        movesAroundForOddRow.add(new Pair<>(-1, -1));
        movesAroundForOddRow.add(new Pair<>(-1, 0));
        movesAroundForOddRow.add(new Pair<>(0, +1));
        movesAroundForOddRow.add(new Pair<>(+1, 0));
        movesAroundForOddRow.add(new Pair<>(+1, -1));
    }


    private static boolean checkOutOfField(int row, int column, Pair<Integer, Integer> coordinates) {
        int incrementRow = row + coordinates.getKey();
        int incrementColumn = column + coordinates.getValue();
        return 0 <= incrementRow && incrementRow < SIZE_OF_FIELD &&
                0 <= incrementColumn && incrementColumn < SIZE_OF_FIELD;
    }

    private static Circle paintBomb(double rowPixel, double columnPixel) {
        Circle circle = new Circle();
        circle.setTranslateY(rowPixel);
        circle.setTranslateX(columnPixel);
        circle.setRadius(BOMB_RADIUS);
        circle.setFill(Paint.valueOf("#000033"));
        return circle;
    }


    private static Text paintFlag(double rowPixel, double columnPixel) {
        Text text = new Text();
        text.setTranslateX(columnPixel);
        text.setTranslateY(rowPixel);
        text.setText(FLAG_ICON);
        text.setFont(Font.font("Arial", STRING_SIZE));
        return text;
    }

    private static Text paintString(double rowPixel, double columnPixel, int countOfBombs) {
        Text text = new Text();
        text.setTranslateX(columnPixel);
        text.setTranslateY(rowPixel);
        text.setText(Integer.toString(countOfBombs));
        text.setFill(Paint.valueOf(COLOR_OF_NUMBERS[countOfBombs]));
        text.setFont(Font.font("Arial", STRING_SIZE));
        return text;
    }

    static void endAndRestart(String string) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(string);
        alert.setTitle("End of Game");
        Optional<ButtonType> actions = alert.showAndWait();
        if (actions.get() == ButtonType.OK) {
            restart();
            alert.close();
        }
    }

    private static void restart() {
            flagField = new Text[SIZE_OF_FIELD][SIZE_OF_FIELD];
            root.getChildren().clear();
            movesAroundForEvenRow.clear();
            movesAroundForOddRow.clear();

            flaggedBomb = 0;
            countOpenedHexagons = 0;
            countBombs = 0;
            win = false;
            bangAndLoss = false;
            firstClickAlert = 0;

            root.getChildren().addAll(box);
            createField(root);
    }

    private static void buttons() {
        easyButton.setOnAction(event -> {
            RADIUS = 50;
            BOMB_RADIUS = 34;
            NUMBER_OF_MINES = 10;
            STRING_SIZE = 50;
            SIZE_OF_FIELD = 9;
            restart();
        });

        normalButton.setOnAction(event -> {
            RADIUS = 28;
            BOMB_RADIUS = 17;
            NUMBER_OF_MINES = 40;
            STRING_SIZE = 25;
            SIZE_OF_FIELD = 15;
            restart();
        });

        hardButton.setOnAction(event -> {
            RADIUS = 16;
            BOMB_RADIUS = 10;
            NUMBER_OF_MINES = 60;
            STRING_SIZE = 18;
            SIZE_OF_FIELD = 24;
            restart();
        });
    }
}
