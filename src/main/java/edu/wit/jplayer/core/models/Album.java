package edu.wit.jplayer.core.models;

import edu.wit.jplayer.core.Utils;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;

public class Album implements Viewable {

    private String title;
    private String genre;
    private String author;
    private Image thumbnail;

    public Album(String name, String genre, String author, Image thumbnail) {
        this.title = name;
        this.genre = genre;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public ImageView getThumbnailView() {
        ImageView thumbnailView = new ImageView();
        thumbnailView.setFitHeight(54);
        thumbnailView.setFitWidth(54);
        if (thumbnail != null)
            thumbnailView.setImage(thumbnail);
        else {
            try (InputStream defaultAlbumImage = getClass().getClassLoader().getResourceAsStream("../images/thumbnail-placeholder.jpg")) {
                if (defaultAlbumImage != null)
                    thumbnailView.setImage(new Image(defaultAlbumImage));
                else
                    Utils.LOGGER.severe("Cannot retrieve default thumbnail image.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return thumbnailView;
    }

    private VBox getAttributesView(){
        VBox attributeView = new VBox();
        attributeView.setAlignment(Pos.CENTER);
        attributeView.setSpacing(20);
        attributeView.getChildren().add(Utils.STR_TO_TEXT(title));
        if (author == null || author.isEmpty())
            attributeView.getChildren().add(Utils.STR_TO_TEXT(author));
        if (genre == null || genre.isEmpty())
            attributeView.getChildren().add(Utils.STR_TO_TEXT(genre));
        return attributeView;
    }

    @Override
    public final HBox getView() {
        HBox albumView = new HBox();
        albumView.getChildren().add(getThumbnailView());
        albumView.getChildren().add(getAttributesView());
        return albumView;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }
}
