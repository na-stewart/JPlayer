package edu.wit.jplayer.core;

import java.io.*;
import java.util.Properties;

public class Config {
    private final File configPath = new File(System.getProperty("user.home") + File.separator + ".jplayer");
    private final File configFile = new File(configPath +  File.separator + "config.properties");
    private final Properties properties = new Properties();


    public void read() throws IOException {
        try (FileInputStream in = new FileInputStream(configFile)){
            properties.loadFromXML(in);
        }
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            read();
            properties.storeToXML(out, "JPlayer config."); //TODO: comment convection.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean exists() {
        return configFile.exists();
    }
}
