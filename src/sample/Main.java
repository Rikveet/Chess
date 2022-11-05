package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Name: Rikveet Singh Hayer, 6590327.
 * This is the main class it is responsible for loading the main screen fxml file.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        backgroundData.setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        backgroundData.setScene(root,"Chess by Rikveet Singh Hayer");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
