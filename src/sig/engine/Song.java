package sig.engine;

import java.io.File;

public enum Song{
    WOOLOOKOLOGIE(new File("..","Woolookologie.wav"));

    File songFile;

    Song(File f) {
        this.songFile=f;
    }
}