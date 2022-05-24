package sig.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Map {
    //Maps contain 512x288 tiles, allowing for 16384x9216 pixels of action per map.
    //294912 bytes = 294KB of memory storage per map.
    //Since a screen normally fits 16x9 tiles, you get 32x32 (1024) screens of gameplay per world.

    final public static int MAP_WIDTH=512;
    final public static int MAP_HEIGHT=288;

    char[] tiles = new char[MAP_WIDTH*MAP_HEIGHT];

    public static Map LoadMap(Maps map) {
        try {
            Map newMap = new Map();
            DataInputStream stream = new DataInputStream(new FileInputStream(map.getFile()));
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

    public static void SaveMap(Maps map) {
        try {
            int marker=0;
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(map.getFile()));
            while (marker<map.map.tiles.length) {
                stream.writeChar(map.map.tiles[marker++]);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ModifyTile(int x,int y,Tile t) {
        tiles[y*Map.MAP_WIDTH+x]=(char)(t.ordinal());
        //System.out.println("Tile "+(y*MAP_WIDTH+x)+" is now "+tiles[y*MAP_WIDTH+x]+".");
    }
}
