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
}
