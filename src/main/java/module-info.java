module edu.wit.jplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;


    opens edu.wit.jplayer to javafx.fxml;
    exports edu.wit.jplayer;
    exports edu.wit.jplayer.core.controllers;
    opens edu.wit.jplayer.core.controllers to javafx.fxml;
}