package sig.events;

import sig.engine.String;

public interface Event{
    public String getDescription();
    public void perform(int x, int y);
}
