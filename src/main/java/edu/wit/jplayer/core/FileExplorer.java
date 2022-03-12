package edu.wit.jplayer.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileExplorer {
    private final Config config = new Config();
    private final List<String> directories = new ArrayList<>();
    private int directoriesNavigationIndex = 0;


    public FileExplorer() {
        try {
            config.read();
            directoriesNavigationIndex = Integer.parseInt(config.getProperty("directories_navigation_index"));
            Collections.addAll(directories, config.getProperty("directories").split(","));
        } catch (IOException e) {
            directories.add(System.getProperty("user.home"));
            config.put("directories", String.join(",", directories));
            config.save();
        }
    }

    public void addDirectory(String directory) {
        directoriesNavigationIndex++;
        if (directoriesNavigationIndex < directories.size())
            directories.subList(directoriesNavigationIndex, directories.size()).clear();
        directories.add(directory);
        config.put("directories", String.join(",", directories));
        config.put("directories_navigation_index", String.valueOf(directoriesNavigationIndex));
        config.save();
    }

    public void navigateDirectories(boolean forward) {
        int updatedNavigationIndex = forward ? directoriesNavigationIndex + 1 : directoriesNavigationIndex - 1;
        if (updatedNavigationIndex < directories.size() && updatedNavigationIndex >= 0) {
            directoriesNavigationIndex = updatedNavigationIndex;
            config.put("directories_navigation_index", String.valueOf(directoriesNavigationIndex));
            config.save();
        }
    }

    public ArrayList<String> getFiles() throws IndexOutOfBoundsException {
        File[] directoryFiles = new File(directories.get(directoriesNavigationIndex)).listFiles();
        ArrayList<String> filteredDirectoryFiles = new ArrayList<>();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                String fileName = file.getName();
                if (file.isDirectory() || Globals.hasValidExtension(fileName))
                    filteredDirectoryFiles.add(fileName);
            }
        }
        return filteredDirectoryFiles;
    }

    public String getDirectory(){
        return directories.get(directoriesNavigationIndex);
    }
}
