package edu.wit.jplayer.core;

import java.io.IOException;
import java.util.logging.Logger;

public class Globals {
    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public final static String[] VALID_MEDIA_EXTENSIONS = {".aif", ".aiff", ".fxm", ".flv", ".m3u8", ".mp3", ".mp4", ".m4a",
            ".m4v", ".wav"};

    public static boolean HAS_VALID_EXTENSION(String name){
        for (String extension : VALID_MEDIA_EXTENSIONS)
            if (name.endsWith(extension))
                return true;
        return false;
    }

    public static void OPEN_BROWSER(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.contains("win"))
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            else if (os.contains("mac"))
                rt.exec("open " + url);
            else if (os.contains("nix") || os.contains("nux")) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror", "netscape", "opera", "links",
                        "lynx", "chrome"};
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
