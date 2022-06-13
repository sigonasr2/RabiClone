package sig.map;

import sig.engine.String;
import sig.events.Event;
import sig.events.SpawnEvent;
import sig.objects.Erinoah;

public enum DataTile {
    NULL, //File is populated by 0s by default. This represents nothing.
    BUN1(new SpawnEvent("Spawns a blue bun",Erinoah.class)),
    BUN2(new SpawnEvent("Spawns a green bun",Erinoah.class)),
    BUN3(new SpawnEvent("Spawns a yellow bun",Erinoah.class)),
    BUN4(new SpawnEvent("Spawns a red bun",Erinoah.class));

    String description;

    DataTile(){}
    DataTile(Event e) {
        this.description=e.getDescription();
    }
    public String getDescription() {
        return description;
    }
}
