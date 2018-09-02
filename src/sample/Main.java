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
import static sample.Hexagon.paintBomb;



public class Main extends Application {
    public boolean bangMine;
    public static int countOpenedCells;
    final String FLAG_ICON = "flag";
    public static int SIZE_OF_FIELD = 20;
    public static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];
    List list = new ArrayList<Integer>();




    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("hexagonal deminer");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("Blue"));

        for (int column = 0; column < SIZE_OF_FIELD; column++) {
            for (int row = 0; row < SIZE_OF_FIELD; row++) {

                Hexagon hex = new Hexagon(0, 20, 2);
                root.getChildren().add(hex.createHexagon(column, row, 20, 2));
                field[column][row] = hex;
                hex.setFill(Paint.valueOf("White"));

                root.getChildren().add(hex);

            }
        }


        primaryStage.setResizable(false);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

    public void openCells() {

    }
    public void checkOutOfField() {

    }

}
