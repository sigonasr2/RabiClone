package sig.events;

public class DataEvent implements Event{

    @Override
    public boolean perform(int x, int y) {
        return true;
    }
    
}
