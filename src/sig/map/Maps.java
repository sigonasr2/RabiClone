package sig.map;

import java.io.File;

public enum Maps {

    WORLD1("world1.map"),
    ;

    final public static String MAPS_DIR = "maps";
    Map map;
    File file;
    
    Maps(String filename) {
        file=new File(new File("..",MAPS_DIR),filename);
        this.map=Map.LoadMap(this);
    }

    public File getFile() {
        return file;
    }

    public Map getMap() {
        return map;
    }

    public void ModifyTile(int x,int y,Tile t) {
        map.ModifyTile(x, y, t);
        //System.out.println("Tile "+(y*MAP_WIDTH+x)+" is now "+tiles[y*MAP_WIDTH+x]+".");
    }

    public Tile getTile(int x,int y) {
        int index = y*Map.MAP_WIDTH+x;
        if (index<0||index>=this.map.tiles.length) {
            return Tile.VOID;
        } else {
            return Tile.values()[this.map.tiles[index]];
        }
    }
}
