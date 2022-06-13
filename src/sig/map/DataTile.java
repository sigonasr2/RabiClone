package sig.map;

import sig.engine.String;

public enum DataTile {
    NULL, //File is populated by 0s by default. This represents nothing.
    BUN1("Spawns a blue bun"),
    BUN2("Spawns a green bun"),
    BUN3("Spawns a yellow bun"),
    BUN4("Spawns a red bun");

    String description;

    DataTile(){}
    DataTile(java.lang.String s) {
        this.description=new String(s);
    }
    public String getDescription() {
        return description;
    }
}
