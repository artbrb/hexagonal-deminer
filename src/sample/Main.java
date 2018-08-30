package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import static sample.Hexagon.paintBomb;



public class Main extends Application {
    public boolean bangMine;
    public static int countOpenedCells;
    private final int columns = 20;
    private final int rows = 20;
    final String FLAG_ICON = "flag";
    public static int SIZE_OF_FIELD = 20;
    public static Hexagon[][] field = new Hexagon[SIZE_OF_FIELD][SIZE_OF_FIELD];

    public void initField() {

    }




    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        primaryStage.setTitle("hexagonal deminer");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        scene.setFill(Paint.valueOf("Blue"));
        scene.setOnMousePressed(Hexagon::mouseListener);



        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                Hexagon hex = new Hexagon();


                root.getChildren().add(Hexagon.createHexagon(column,row, 18, 1));
                root.getChildren().add(paintBomb(column, row));
                hex = field[column][row];
            }
        }

        primaryStage.setResizable(false);
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
