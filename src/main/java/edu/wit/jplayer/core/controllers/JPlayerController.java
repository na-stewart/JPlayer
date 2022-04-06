package edu.wit.jplayer.core.controllers;

import edu.wit.jplayer.core.FileExplorer;
import edu.wit.jplayer.core.Globals;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.Locale;

public class JPlayerController{
    @FXML
    private ListView<String> filesView;
    @FXML
    private Text directoryView;
    @FXML
    private TextField filesViewFilterField;
    @FXML
    private ImageView albumImageView;
    @FXML
    private Text mediaNameText;
    @FXML
    private Text mediaAuthorText;
    @FXML
    private Text mediaTimeElapsedText;
    @FXML
    private Text mediaTimeRemainingText;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider seekSlider;
    private final FileExplorer fileExplorer = new FileExplorer();
    private MediaPlayer mediaPlayer;


    public void initialize() {
        generateViews();
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

    private String createDurationString(int seconds) {
        int hours = seconds / (60 * 60) % 24;
        int mins = seconds / 60 % 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, mins, secs);
    }

    private void playMedia(String path){
        //Play audio here.
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) ->
        {
            Duration duration = mediaPlayer.getTotalDuration();
            seekSlider.setMax(duration.toSeconds());
            seekSlider.setValue(newTime.toSeconds());
            mediaTimeRemainingText.setText(createDurationString((int) duration.toSeconds()));
            mediaTimeElapsedText.setText(createDurationString((int) newTime.toSeconds()));
            mediaPlayer.setOnEndOfMedia(this::skip);
        });
    }

    @FXML
    private void onFileViewClick(MouseEvent mouseEvent) {
        String selected = filesView.getSelectionModel().getSelectedItem();
        String fullSelectedPath = directoryView.getText() + File.separator + selected;
        if (mouseEvent.getClickCount() == 2 && selected != null) {
            if (Globals.HAS_VALID_EXTENSION(selected))
                playMedia(fullSelectedPath);
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
    private void onJPlayerHyperlinkAction(ActionEvent actionEvent){
        Globals.OPEN_BROWSER("https://github.com/sunset-developer/JPlayer");
    }

    @FXML
    private void seek() {
        mediaPlayer.pause();
        mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
        if (!seekSlider.isValueChanging())
            mediaPlayer.play();
    }

    @FXML
    private void skip(){
        filesView.getSelectionModel().select(filesView.getSelectionModel().getSelectedIndex() + 1);
        playMedia(directoryView.getText() + File.separator + filesView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void rewind(MouseEvent e){
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
}