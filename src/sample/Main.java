package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;




public class Main extends Application {
    public boolean bangMine;
    public static int countOpenedHexagons;
    final String FLAG_ICON = "flag";
    public static int SIZE_OF_FIELD = 20;
    public static int numberOfMines = 20;
    public static int xCoordinateBang;
    public static int yCoordinateBang;
    public static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];
    List list = new ArrayList<Integer>();
    public static boolean win = false;
    public static boolean bangAndLoss = false;




    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("hexagonal deminer");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("Blue"));

        for (int column = 0; column < SIZE_OF_FIELD; column++) {
            for (int row = 0; row < SIZE_OF_FIELD; row++) {

                Hexagon hex = new Hexagon(20, 2);
                root.getChildren().add(hex.createHexagon(column, row, 20, 2));
                field[column][row] = hex;
                hex.columnСoordinate =column;
                hex.rowCoordinate =row;


                root.getChildren().add(hex);

            }
        }


        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void openHexagons(int column, int row) {

    }
    public void checkOutOfField() {

    }

    public static void restart() {

    }

}
