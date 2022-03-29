package edu.wit.jplayer.core.models;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class AlbumCellFactory implements Callback<ListView<Album>, ListCell<Album>> {

    @Override
    public ListCell<Album> call(ListView<Album> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(album.getThumbnailView());
                    setText(album.getTitle() + "\n" + album.getAuthor() + "\n" + album.getGenre());
                }
            }
        };
    }
}
