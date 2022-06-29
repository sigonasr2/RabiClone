package sig.events;

import sig.RabiClone;
import sig.map.Tile;

public class WaterEvent implements Event{

    @Override
    public boolean perform(int x, int y) {
        return true;
    }

    @Override
    public boolean performCollision(int x, int y) {
        return true;
    }

    @Override
    public boolean performScreenLoad(int x, int y) {
        RabiClone.CURRENT_MAP.getMap().setWaterLevel((char)(y/Tile.TILE_HEIGHT*Tile.TILE_HEIGHT));
        return true;
    }
    
}
