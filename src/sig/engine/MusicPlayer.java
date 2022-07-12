package sig.engine;

import java.util.List;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;

public class MusicPlayer implements LineListener{
    public static SongThread SongThread;
    public static List<Clip> SoundEffects;
    public static boolean songChangeRequested=false;

    public static void init() {
        SongThread = new SongThread();
    }
    public static void changeMusic(Song newSong) {
        SongThread.currentSong=newSong;
        MusicPlayer.songChangeRequested=true;
        SongThread.run();
    }
    @Override
    public void update(LineEvent event) {
        if (event.getType()==Type.CLOSE||event.getType()==Type.STOP) {
            SongThread.run(); //Cause a loop so the music continues playing.
        }
    }
}
