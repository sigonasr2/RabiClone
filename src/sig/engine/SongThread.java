package sig.engine;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sig.RabiClone;

public class SongThread extends Thread{
    Song currentSong;
    AudioInputStream audioStream;
    AudioFormat format;
    DataLine.Info info;
    SourceDataLine audioLine;
    public void run() {
        try {
            if (audioStream!=null) {
                audioLine.close();
                audioStream.close();
                audioStream=null;
            }
            audioStream = AudioSystem.getAudioInputStream(audioStream);
            format = audioStream.getFormat();
            info = new DataLine.Info(SourceDataLine.class,format);
            audioLine = (SourceDataLine)AudioSystem.getLine(info);
            audioLine.addLineListener(RabiClone.MUSICPLAYER);
            audioLine.open(format);
            audioLine.start();
            final int BUFFER_SIZE=4096;
            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead=-1;
            while ((bytesRead=audioStream.read(bytesBuffer))!=-1&&!MusicPlayer.songChangeRequested) {
                audioLine.write(bytesBuffer,0,bytesRead);
            }
            if (!MusicPlayer.songChangeRequested) {
                audioLine.drain();
            } else {
                audioLine.stop();
                audioLine.flush();
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
