package edu.wit.jplayer.core;

import java.io.File;

public class Globals {
    public static String[] validMediaExtensions = new String[]{".aif", ".aiff", ".fxm", ".flv", ".m3u8", ".mp3",
            ".mp4", ".m4a", ".m4v", ".wav"};

    public static boolean hasValidExtension(String name){
        for (String extension : validMediaExtensions)
            if (name.endsWith(extension))
                return true;
        return false;
    }
}
