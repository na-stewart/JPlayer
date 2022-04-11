package edu.wit.jplayer.core.controllers;

import edu.wit.jplayer.Main;
import edu.wit.jplayer.core.FileExplorer;
import edu.wit.jplayer.core.Utils;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Objects;

public class JPlayerController {
    @FXML
    private ListView<String> filesView;
    @FXML
    private Text directoryView;
    @FXML
    private TextField filesViewFilterField;
    @FXML
    private ImageView albumImageView;
    @FXML
    private Text mediaTitleText;
    @FXML
    private Text mediaArtistText;
    @FXML
    private Text mediaTimeElapsedText;
    @FXML
    private Text mediaTimeRemainingText;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider seekSlider;
    @FXML
    private MediaView mediaView;
    @FXML
    private AnchorPane mediaViewContainer;
    @FXML
    private HBox mediaControlContainer;
    @FXML
    private HBox volumeControlContainer;
    @FXML
    private HBox seekControlContainer;
    private final FileExplorer fileExplorer = new FileExplorer();
    private MediaPlayer mediaPlayer;


    public void initialize() {
        generateViews();
        mediaView.fitWidthProperty().bind(mediaViewContainer.widthProperty());
        mediaView.fitHeightProperty().bind(mediaViewContainer.heightProperty());
    }

    private void generateViews() {
        FilteredList<String> filteredList = new FilteredList<>(FXCollections.observableArrayList(fileExplorer.getFiles()), data -> true);
        directoryView.setText(fileExplorer.getDirectory());
        filesView.setItems(filteredList);
        filesViewFilterField.textProperty().addListener(((observable, oldValue, newValue) -> filteredList.setPredicate(data -> {
            boolean filter = newValue == null || newValue.isEmpty();
            if (!filter)
                filter = data.toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT));
            return filter;
        })));
    }

    private void displayMediaAlbumCover(Image image) {
        albumImageView.setImage(Objects.requireNonNullElseGet(image, () ->
                new Image(String.valueOf(Main.class.getResource("images/album-placeholder.jpg")))));
    }

    private void displayMedia(ObservableMap<String, Object> metaData) {
        displayMediaAlbumCover(metaData.containsKey("image") ? (Image) metaData.get("image") : null);
        mediaArtistText.setText(metaData.containsKey("artist") ? metaData.get("artist").toString() : "");
        if (metaData.containsKey("title"))
            mediaTitleText.setText(metaData.get("title").toString());
        else
            mediaTitleText.setText(filesView.getSelectionModel().getSelectedItem());

    }

    private String createDurationString(int seconds) {
        int hours = seconds / (60 * 60) % 24;
        int mins = seconds / 60 % 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }

    private ChangeListener<Duration> mediaPlayerDurationListener() {
        return (observable, oldTime, newTime) -> {
            Duration duration = mediaPlayer.getTotalDuration();
            seekSlider.setMax(duration.toSeconds());
            seekSlider.setValue(newTime.toSeconds());
            mediaTimeRemainingText.setText(createDurationString((int) (duration.toSeconds() - newTime.toSeconds())));
            mediaTimeElapsedText.setText(createDurationString((int) newTime.toSeconds()));
            mediaPlayer.setOnEndOfMedia(this::skip);
        };
    }

    private void displayMediaControls(boolean visible){
        seekControlContainer.setVisible(visible);
        mediaControlContainer.setVisible(visible);
        volumeControlContainer.setVisible(visible);
    }
    private void displayVideo() {
        mediaViewContainer.setVisible(true);
        displayMediaControls(false);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    private void playMedia(String path) throws MalformedURLException {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        Media media = new Media(new File(path).toURI().toURL().toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        mediaPlayer.setOnReady(() -> {
            displayMedia(media.getMetadata());
            mediaPlayer.play();
            if (Utils.HAS_VALID_VIDEO_EXTENSION(path))
                displayVideo();
        });
        mediaPlayer.currentTimeProperty().addListener(mediaPlayerDurationListener());
    }


    @FXML
    private void onFileViewClick(MouseEvent mouseEvent) {
        String selected = filesView.getSelectionModel().getSelectedItem();
        String fullSelectedPath = directoryView.getText() + File.separator + selected;
        if (mouseEvent.getClickCount() == 2 && selected != null) {
            if (Utils.HAS_VALID_VIDEO_EXTENSION(selected) || Utils.HAS_VALID_AUDIO_EXTENSION(selected)) {
                try {
                    playMedia(fullSelectedPath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else {
                fileExplorer.addDirectory(fullSelectedPath);
                generateViews();
            }
        }
    }

    @FXML
    private void onPathNavigatorButtonAction(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        fileExplorer.navigateDirectories(button.getText().equals(">>"));
        generateViews();
    }

    @FXML
    private void seek(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().getName().equals("MOUSE_PRESSED"))
            mediaPlayer.pause();
        else if (mouseEvent.getEventType().getName().equals("MOUSE_RELEASED"))
            mediaPlayer.play();
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
    }

    @FXML
    private void skip(){
        filesView.getSelectionModel().select(filesView.getSelectionModel().getSelectedIndex() + 1);
        try {
            playMedia(directoryView.getText() + File.separator + filesView.getSelectionModel().getSelectedItem());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void rewind(MouseEvent e) throws MalformedURLException {
        if (e.getClickCount() == 2){
            filesView.getSelectionModel().select(filesView.getSelectionModel().getSelectedIndex() - 1);
            playMedia(directoryView.getText() + File.separator + filesView.getSelectionModel().getSelectedItem());
        } else if (e.getClickCount() == 1){
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
    }

    @FXML
    private void play(){
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            mediaPlayer.pause();
        } else
            mediaPlayer.play();
    }

    @FXML
    private void showMediaControls(MouseEvent mouseEvent){
        displayMediaControls(true);
    }

    @FXML
    private void hideMediaControls(MouseEvent mouseEvent){
        displayMediaControls(false);
    }
}