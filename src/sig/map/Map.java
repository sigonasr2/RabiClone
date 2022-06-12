package sig.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sig.RabiClone;
import sig.engine.Sprite;

public class Map {
    //Maps contain 512x288 tiles, allowing for 16384x9216 pixels of action per map.
    //Since a screen normally fits 16x9 tiles, you get 32x32 (1024) screens of gameplay per world.
    //
    //After the map data, the next 1024 bytes will indicate the map view information.
    //After that, the next 1024 bytes will indicate the background information.
    //After that, the next 1024 bytes will indicate the map color information.
    //After that, the next 1024 bytes will indicate the map room type. (Might show checkpoints, warp points, POI, etc)
    //
    //299008 bytes = 299KB of memory storage per map.
    //

    final public static int MAP_WIDTH=512;
    final public static int MAP_HEIGHT=288;

    char[] tiles = new char[MAP_WIDTH*MAP_HEIGHT];
    byte[] views = new byte[(MAP_WIDTH/Tile.TILE_WIDTH)*(MAP_HEIGHT/Tile.TILE_HEIGHT)];
    byte[] backgrounds = new byte[(MAP_WIDTH/Tile.TILE_WIDTH)*(MAP_HEIGHT/Tile.TILE_HEIGHT)];
    byte[] colors = new byte[(MAP_WIDTH/Tile.TILE_WIDTH)*(MAP_HEIGHT/Tile.TILE_HEIGHT)];
    byte[] types = new byte[(MAP_WIDTH/Tile.TILE_WIDTH)*(MAP_HEIGHT/Tile.TILE_HEIGHT)];

    final static byte MAP_DATA = 0;
    final static byte VIEW_DATA = 1;
    final static byte BACKGROUND_DATA = 2;
    final static byte COLOR_DATA = 3;
    final static byte TYPE_DATA = 4;

    public static Map LoadMap(Maps map) {
        try {
            Map newMap = new Map();
            DataInputStream stream = new DataInputStream(new FileInputStream(map.getFile()));
            int marker=0;
            int iterationCount=MAP_WIDTH*MAP_HEIGHT;
            int readingData = MAP_DATA;
            while (stream.available()!=0) {
                switch (readingData) {
                    case MAP_DATA:
                        char tileValue = stream.readChar();
                        int ypos=marker/Map.MAP_WIDTH;
                        int xpos=marker%Map.MAP_WIDTH;
                        newMap.tiles[marker++]=tileValue;
                        if (Tile.values()[tileValue].getCollision()==CollisionType.SOLID) {
                            for (int y=0;y<Tile.TILE_HEIGHT;y++) {
                                for (int x=0;x<Tile.TILE_WIDTH;x++) {
                                    if (Sprite.TILE_SHEET.getBi_array()[(Tile.values()[tileValue].getSpriteSheetY()*Tile.TILE_HEIGHT+y)*Sprite.TILE_SHEET.getCanvasHeight()+Tile.values()[tileValue].getSpriteSheetX()*Tile.TILE_WIDTH+x]!=(byte)32) {
                                        RabiClone.COLLISION[(ypos*Tile.TILE_HEIGHT+y)*(Map.MAP_WIDTH*Tile.TILE_WIDTH)+xpos*Tile.TILE_WIDTH+x]=true;
                                    }
                                }
                            }
                        }
                    break;
                    case VIEW_DATA:
                        newMap.views[marker++]=stream.readByte();
                    break;
                    case BACKGROUND_DATA:
                        newMap.backgrounds[marker++]=stream.readByte();
                    break;
                    case COLOR_DATA:
                        newMap.colors[marker++]=stream.readByte();
                    break;
                    case TYPE_DATA:
                        newMap.types[marker++]=stream.readByte();
                    break;
                }
                iterationCount--;
                if (iterationCount<=0) {
                    readingData++;
                    marker=0;
                    iterationCount=(MAP_WIDTH/Tile.TILE_WIDTH)*(MAP_HEIGHT/Tile.TILE_HEIGHT);
                }
            }
            stream.close();
            return newMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void SaveMap(Maps map) throws IOException {
        DataOutputStream stream = new DataOutputStream(new FileOutputStream(map.getFile()));
        saveCharData(stream,map.map.tiles);
        saveByteData(stream,map.map.views);
        saveByteData(stream,map.map.backgrounds);
        saveByteData(stream,map.map.colors);
        saveByteData(stream,map.map.types);
        stream.close();
    }

    private static void saveCharData(DataOutputStream stream, char[] a) throws IOException {
        int marker = 0;
        while (marker<a.length) {
            stream.writeChar(a[marker++]);
        }
    }

    private static void saveByteData(DataOutputStream stream, byte[] a) throws IOException {
        int marker = 0;
        while (marker<a.length) {
            stream.writeByte(a[marker++]);
        }
    }

    public void ModifyTile(int x,int y,Tile t) {
        Tile prevTile = Tile.values()[tiles[y*Map.MAP_WIDTH+x]];
        if (prevTile!=t) {
            byte[] tilesheet = Sprite.TILE_SHEET.getBi_array();
            boolean setSolid = t.getCollision()==CollisionType.SOLID;
            for (int yy=0;yy<Tile.TILE_HEIGHT;yy++) {
                for (int xx=0;xx<Tile.TILE_WIDTH;xx++) {
                    if (tilesheet[(t.getSpriteSheetY()*Tile.TILE_HEIGHT+yy)*Sprite.TILE_SHEET.getCanvasWidth()+t.getSpriteSheetX()*Tile.TILE_WIDTH+xx]!=(byte)32) {
                        RabiClone.COLLISION[(y*Tile.TILE_HEIGHT+yy)*(Map.MAP_WIDTH*Tile.TILE_WIDTH)+(x*Tile.TILE_WIDTH+xx)]=setSolid;
                    } else {
                        RabiClone.COLLISION[(y*Tile.TILE_HEIGHT+yy)*(Map.MAP_WIDTH*Tile.TILE_WIDTH)+(x*Tile.TILE_WIDTH+xx)]=false;
                    }
                }
            } 
        }
        tiles[y*Map.MAP_WIDTH+x]=(char)(t.ordinal());
        //System.out.println("Tile "+(y*MAP_WIDTH+x)+" is now "+tiles[y*MAP_WIDTH+x]+".");
    }
}
