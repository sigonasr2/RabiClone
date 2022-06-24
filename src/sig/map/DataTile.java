package sig.map;

import sig.engine.String;
import sig.events.DataEvent;
import sig.events.Event;
import sig.events.SpawnEvent;
import sig.events.WaterEvent;
import sig.objects.enemies.BlueBun;
import sig.objects.Erinoah;
import sig.objects.enemies.GreenBun;
import sig.objects.enemies.RedBun;
import sig.objects.enemies.YellowBun;

public enum DataTile {
    NULL, //File is populated by 0s by default. This represents nothing.
    BUN0("Spawns an Erinoa bun",new SpawnEvent(Erinoah.class)),
    BUN1("Spawns a red bun",new SpawnEvent(RedBun.class)),
    BUN2("Spawns a blue bun",new SpawnEvent(BlueBun.class)),
    BUN3("Spawns a yellow bun",new SpawnEvent(YellowBun.class)),
    BUN4("Spawns a green bun",new SpawnEvent(GreenBun.class)),
    WATERLEVEL("Sets the water level according to the data tile at X+1",new WaterEvent()),
    DATATILE("Uses a value between 32768-65536 to represent extra data.",new DataEvent());

    String description;
    Event event;

    DataTile(){
        this.description=new String("");
    }
    DataTile(java.lang.String description,Event e) {
        this.description=new String(description);
        this.event=e;
    }
    public String getDescription() {
        return description;
    }
    public Event getEvent() {
        return event;
    }
}
