package sig.events;

import sig.engine.String;

public interface Event{
    public String getDescription();
    /**
     * Perform this event at position {@code (x,y)}.
     * @return {@code True} to keep this event alive after it runs.
     * {@code False} to remove this event from the game after it runs.
     */
    public boolean perform(int x, int y);
}
