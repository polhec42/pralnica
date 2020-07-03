package sample;

import utilities.Scenes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    Scene scene1, scene2;

    public static final double SIRINA = 800;
    public static final double VISINA = 600;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Connect.createNewTable();
        //Connect.createNewTable();
        Scenes scene = new Scenes(primaryStage, SIRINA, VISINA);

    }

    public static void main(String[] args) {
        launch(args);
    }


}
