package edu.wit.jplayer.core.models;

import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.InputStream;

public class MediaView extends HBox {
    private Media media;

    public MediaView(String path){
        media = new Media(path);
    }

    public ImageView getMediaThumbnailImageView() {
        ImageView thumbnailView = new ImageView();
        ObservableMap<String, Object> mediaMetaData = media.getMetadata();
        thumbnailView.setFitHeight(54);
        thumbnailView.setFitWidth(54);
        if (mediaMetaData.containsKey("image"))
            thumbnailView.setImage((Image) mediaMetaData.get("image"));
        else {
            InputStream defaultAlbumImage = getClass().getClassLoader().getResourceAsStream("../images/thumbnail-placeholder.jpg");
            if (defaultAlbumImage != null)
                thumbnailView.setImage(new Image(defaultAlbumImage));
        }
        return thumbnailView;
    }

    private void setFont(Text text){
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font: 14 system;");
    }

    public VBox getMediaMetaDataView(){
        VBox mediaMetaDataView = new VBox();
        Text mediaNameView = new Text();
        Text mediaArtistView = new Text();
        Text mediaAlv
        setFont(mediaNameView);
        return mediaMetaDataView;
    }

    private void set(){
        ImageView albumView = getMediaThumbnailImageView();

    }

    public HBox getMediaView() {

    }

    public Media getMedia() {
        return media;
    }

    public ObservableMap<String, Object> getMetaData() {
        return metaData;
    }
}
