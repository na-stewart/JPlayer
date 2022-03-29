package edu.wit.jplayer.core.models;

import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

import java.io.File;
import java.net.MalformedURLException;

public class Music implements Viewable {
    public String title;
    public Media media;
    public Album album;

    public Music(String title, Album album, String path) throws MalformedURLException {
        this.album = album;
        this.title = title;
        this.media = new Media(new File(path).toURI().toURL().toExternalForm());
    }

    @Override
    public VBox getView() {
        return null;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
