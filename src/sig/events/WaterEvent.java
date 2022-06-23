package sig.events;

import sig.RabiClone;

public class WaterEvent implements Event{

    @Override
    public boolean perform(int x, int y) {
        RabiClone.CURRENT_MAP.getMap().setWaterLevel(RabiClone.CURRENT_MAP.getMap().getDataTileValue(x+1, y));
        return true;
    }
    
}
