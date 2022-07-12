package sig.engine;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Song{
    WOOLOOKOLOGIE(new File("..","Woolookologie.wav"));

    File songFile;

    public Song(File f) {
        this.songFile=songFile;
    }
}