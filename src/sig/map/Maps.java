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
        //this.map=Map.LoadMap(this); //We don't necessarily have to load the map right away.
    }

    public File getFile() {
        return file;
    }

    public Map getMap() {
        return map;
    }

    public void initializeMap() {
        map = new Map();
    }

    public void setMap(Map m) {
        this.map=m;
    }

    public void ModifyTile(int x,int y,Tile t) {
        map.ModifyTile(x, y, t);
        //System.out.println("Tile "+(y*MAP_WIDTH+x)+" is now "+tiles[y*MAP_WIDTH+x]+".");
    }

    public void ModifyDataTile(int tileX, int tileY, DataTile selectedDataTile) {
        map.ModifyDataTile(tileX, tileY, selectedDataTile);
    }

    public void ModifyDataTileValue(int tileX, int tileY, char value) {
        map.ModifyDataTile(tileX, tileY, value);
    }

    public Tile getTile(int x,int y) {
        int index = y*Map.MAP_WIDTH+x;
        if (index<0||index>=this.map.tiles.length) {
            return Tile.VOID;
        } else {
            return Tile.values()[this.map.tiles[index]];
        }
    }

    public DataTile getDataTile(int x,int y) {
        int index = y*Map.MAP_WIDTH+x;
        if (index<0||index>=this.map.data.length) {
            return DataTile.NULL;
        } else {
            return DataTile.values()[this.map.data[index]];
        }
    }

    /**
     * This will return the tile's raw data value.
     * Data Tile values will not have 8192 subtracted from itself through this function.
     */
    public char getDataTileRawValue(int x, int y) {
        int index = y*Map.MAP_WIDTH+x;
        if (index<0||index>=this.map.data.length) {
            return (char)DataTile.NULL.ordinal();
        } else {
            return this.map.data[index];
        }
    }

    public View getView(int x,int y) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index<0||index>=this.map.views.length) {
            return View.FIXED;
        } else {
            return View.values()[this.map.views[index]];
        }
    }

    public Type getType(int x,int y) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index<0||index>=this.map.types.length) {
            return Type.NONE;
        } else {
            return Type.values()[this.map.types[index]];
        }
    }

    public void setView(int x,int y,View view) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index>=0&&index<this.map.views.length) {
            this.map.views[index]=(byte)view.ordinal();
        }
    }

    public void setType(int x,int y,Type type) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index>=0&&index<this.map.types.length) {
            this.map.types[index]=(byte)type.ordinal();
        }
    }

    public Background getBackground(int x,int y) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index<0||index>=this.map.backgrounds.length) {
            return Background.BACKGROUND1;
        } else {
            return Background.values()[this.map.backgrounds[index]];
        }
    }

    public void setBackground(int x,int y,Background background) {
        int index = (y/Tile.TILE_SCREEN_COUNT_Y)*(Map.MAP_WIDTH/Tile.TILE_WIDTH)+x/Tile.TILE_SCREEN_COUNT_X;
        if (index>=0&&index<this.map.backgrounds.length) {
            this.map.backgrounds[index]=(byte)background.ordinal();
        }
    }
}
