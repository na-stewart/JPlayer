package edu.wit.jplayer.core;

public class Utils {
    public final static String[] VALID_AUDIO_EXTENSIONS = {".aif", ".aiff", ".mp3", ".wav"};
    public final static String[] VALID_VIDEO_EXTENSIONS = {".fxm", ".flv", ".mp4", ".m4a", "m4v"};


    public static boolean HAS_VALID_AUDIO_EXTENSION(String name){
        for (String extension : VALID_AUDIO_EXTENSIONS)
            if (name.endsWith(extension))
                return true;
        return false;
    }

    public static boolean HAS_VALID_VIDEO_EXTENSION(String name){
        for (String extension : VALID_VIDEO_EXTENSIONS)
            if (name.endsWith(extension))
                return true;
        return false;
    }
}
