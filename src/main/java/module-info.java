module edu.wit.jplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens edu.wit.jplayer to javafx.fxml;
    exports edu.wit.jplayer;
    exports edu.wit.jplayer.controllers;
    opens edu.wit.jplayer.controllers to javafx.fxml;
}