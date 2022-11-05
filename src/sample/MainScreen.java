package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Name: Rikveet Singh Hayer 6590327
 * This class is a controller class for the main screen, I loads single player file.
 */
public class MainScreen {

    public Button singleplayer;
    public Button quit;

    public void eventListener(ActionEvent event) throws IOException {
        String e = ((Button) event.getSource()).getText();
        System.out.println(e);
        switch (e) {
            case "Single Player":{
                Parent root = FXMLLoader.load(getClass().getResource("singlePlayer.fxml"));
                backgroundData.setScene(root,"Single Player");
                break;
            }
            case "Quit":{
                backgroundData.Quit();
                break;
            }
        }
    }
}
