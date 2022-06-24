package sig.events;

import sig.RabiClone;

public class WaterEvent implements Event{

    @Override
    public boolean perform(int x, int y) {
        char val1=RabiClone.CURRENT_MAP.getMap().getDataTileValue(x+1, y);
        char val2=RabiClone.CURRENT_MAP.getMap().getDataTileValue(x, y+1);
        if (val1!=0) {
            RabiClone.CURRENT_MAP.getMap().setWaterLevel(val1);
        } else {
            RabiClone.CURRENT_MAP.getMap().setWaterLevel(val2);
        }
        return true;
    }
    
}
