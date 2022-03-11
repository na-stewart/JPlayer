package edu.wit.jplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage s) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/JPlayerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        s.setTitle("JPlayer");
        s.setScene(scene);
        s.show();
    }

    public static void main(String[] args) {
        launch();
    }
}