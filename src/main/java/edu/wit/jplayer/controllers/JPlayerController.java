package edu.wit.jplayer.controllers;

import edu.wit.jplayer.core.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class JPlayerController implements Initializable {
    @FXML
    private ListView<String> fileView;
    @FXML
    private Text pathView;
    @FXML
    private TextField fileViewFilterField;
    private final ArrayList<String> pathNavigator = new ArrayList<>();
    private int pathNavigatorIndex = 0;
    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathNavigator.add(System.getProperty("user.home"));
        generateFileView(System.getProperty("user.home"));
    }

    private void addFileViewFilterFieldListener(ObservableList<String> filesForView){
        FilteredList<String> filteredList = new FilteredList<>(filesForView, data -> true);
        fileView.setItems(filteredList);
        fileViewFilterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(data -> {
                boolean filter = newValue == null || newValue.isEmpty();
                if (!filter)
                    filter = data.toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT));
                return filter;
            });
        }));
    }

    private void generateFileView(String path) {
        File[] files = new File(path).listFiles();
        pathView.setText(path);
        if (files != null) {
            ObservableList<String> filesForView = FXCollections.observableArrayList();
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory()) {
                    filesForView.add(File.separator + name);
                } else {
                    if (Globals.hasValidExtension(name))
                        filesForView.add(name);
                }
            }
            addFileViewFilterFieldListener(filesForView);
        }
    }

    private void openDirectory() {
        pathNavigatorIndex++;
        String fullPathSelected = pathView.getText() + fileView.getSelectionModel().getSelectedItem();
        if (pathNavigatorIndex < pathNavigator.size())
            pathNavigator.subList(pathNavigatorIndex, pathNavigator.size()).clear();
        pathNavigator.add(fullPathSelected);
        generateFileView(fullPathSelected);
    }

    @FXML
    private void onFileViewClick(MouseEvent mouseEvent) {
        String selected = fileView.getSelectionModel().getSelectedItem();
        if (mouseEvent.getClickCount() == 2 && selected != null) {
            if (Globals.hasValidExtension(selected))
                openDirectory();
            else {
                //Play audio here.
                return;
            }
        }
    }

    @FXML
    private void onPathNavigatorButtonAction(ActionEvent actionEvent){
        int previousPathNavigatorIndex = pathNavigatorIndex;
        Button button = (Button) actionEvent.getSource();
        try {
            if (button.getText().equals(">>"))
                pathNavigatorIndex++;
            else
                pathNavigatorIndex--;
            generateFileView(pathNavigator.get(pathNavigatorIndex));
        } catch (IndexOutOfBoundsException e) {
            pathNavigatorIndex = previousPathNavigatorIndex;
        }
    }
}