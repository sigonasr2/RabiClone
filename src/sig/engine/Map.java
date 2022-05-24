package sig.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Map {
    //Maps contain 512x288 tiles, allowing for 16384x9216 pixels of action per map.
    //294912 bytes = 294KB of memory storage per map.
    //Since a screen normally fits 16x9 tiles, you get 32x32 (1024) screens of gameplay per world.
    char[] tiles = new char[512*288];

    public static Map LoadMap(File mapFile) {
        try {
            Map newMap = new Map();
            DataInputStream stream = new DataInputStream(new FileInputStream(mapFile));
            int marker=0;
            while (stream.available()!=0) {
                newMap.tiles[marker++]=stream.readChar();
            }
            stream.close();
            return newMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void SaveMap(Map map, File mapFile) {
        try {
            int marker=0;
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(mapFile));
            while (marker<map.tiles.length) {
                stream.writeChar(map.tiles[marker++]);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
