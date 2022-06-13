package sig.events;

import java.lang.reflect.InvocationTargetException;

import sig.RabiClone;
import sig.engine.String;
import sig.engine.objects.Object;

public class SpawnEvent implements Event{

    Class<?> entity;
    String description;

    public SpawnEvent(java.lang.String description,Class<?> o) {
        this.entity=o;
        this.description=new String(description);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void perform(int x, int y) {
        try {
            RabiClone.OBJ.add((Object)entity.getDeclaredConstructor(new Class<?>[]{Double.class,Double.class}).newInstance(x,y));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }
    
}
