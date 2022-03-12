package edu.wit.jplayer.core;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Config extends Properties {
    private final File configPath = new File(System.getProperty("user.home") + File.separator + ".jplayer");
    private final File configFile = new File(configPath +  File.separator + "config.properties");


    public Config(){
        configPath.mkdirs();
    }

    public void read() throws IOException {
        try (FileInputStream in = new FileInputStream(configFile)){
            loadFromXML(in);
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            try {
                read();
            } catch (InvalidPropertiesFormatException ignored) {}
            storeToXML(out, "JPlayer configuration file."); //TODO: comment convection.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
