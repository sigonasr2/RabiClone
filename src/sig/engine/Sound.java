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

public class Sound{
    public Sound() {
        try {
            File audioFile = new File("testFile.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
            SourceDataLine audioLine = (SourceDataLine)AudioSystem.getLine(info);
            audioLine.open(format);
            audioLine.start();
            final int BUFFER_SIZE=4096;
            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead=-1;
            while ((bytesRead=audioStream.read(bytesBuffer))!=-1) {
                audioLine.write(bytesBuffer,0,bytesRead);
            }

            audioLine.drain();
            audioLine.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        
    }
}