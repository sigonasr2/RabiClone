package sig.map;

import sig.engine.String;
import sig.events.Event;
import sig.events.SpawnEvent;
import sig.objects.enemies.BlueBun;
import sig.objects.Erinoah;
import sig.objects.enemies.GreenBun;
import sig.objects.enemies.RedBun;
import sig.objects.enemies.YellowBun;

public enum DataTile {
    NULL, //File is populated by 0s by default. This represents nothing.
    BUN0(new SpawnEvent("Spawns an Erinoa bun",Erinoah.class)),
    BUN1(new SpawnEvent("Spawns a red bun",RedBun.class)),
    BUN2(new SpawnEvent("Spawns a blue bun",BlueBun.class)),
    BUN3(new SpawnEvent("Spawns a yellow bun",YellowBun.class)),
    BUN4(new SpawnEvent("Spawns a green bun",GreenBun.class));

    String description;
    Event event;

    DataTile(){
        this.description=new String("");
    }
    DataTile(Event e) {
        this.description=e.getDescription();
        this.event=e;
    }
    public String getDescription() {
        return description;
    }
    public boolean perform(int x, int y) {
        return event.perform(x, y);
    }
}
