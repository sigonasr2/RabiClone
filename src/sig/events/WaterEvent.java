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
        char val1=RabiClone.CURRENT_MAP.getMap().getDataTileValue(x/Tile.TILE_WIDTH+1, y/Tile.TILE_HEIGHT);
        char val2=RabiClone.CURRENT_MAP.getMap().getDataTileValue(x/Tile.TILE_WIDTH, y/Tile.TILE_HEIGHT+1);
        if (val1!=Character.MAX_VALUE) {
            RabiClone.CURRENT_MAP.getMap().setWaterLevel(val1);
            //System.out.println("Water level set to "+(int)RabiClone.CURRENT_MAP.getMap().getWaterLevel());
        } else {
            RabiClone.CURRENT_MAP.getMap().setWaterLevel(val2);
            //System.out.println("Water level set to "+(int)RabiClone.CURRENT_MAP.getMap().getWaterLevel());
        }
        return true;
    }
    
}
