package sig.map;

import java.io.File;

public enum Maps {

    WORLD1("world1.map"),
    ;

    final public static String MAPS_DIR = "maps";
    Map map;
    
    Maps(String filename) {
        File map_loc = new File(new File("..",MAPS_DIR),filename);
        this.map=Map.LoadMap(map_loc);
    }

    public Map getMap() {
        return map;
    }
}
