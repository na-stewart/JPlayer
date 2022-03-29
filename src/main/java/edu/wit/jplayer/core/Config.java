package edu.wit.jplayer.core;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Config extends Properties {
    private final File configFile = new File(Utils.CACHE_PATH +  File.separator + "config.properties");

    public final void read() throws IOException {
        try (FileInputStream in = new FileInputStream(configFile)){
            loadFromXML(in);
        }
    }

    public final void save() {
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            try {
                read();
            } catch (InvalidPropertiesFormatException ignored) {}
            storeToXML(out, "JPlayer configuration.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void clear(){
        clear();
        save();
    }
}
