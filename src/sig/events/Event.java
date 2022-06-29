package sig.events;

public interface Event{
    /**
     * Runs a constant update event on this tile at the given position.
     * @param x The X Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @param y The Y Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @return {@code True} to keep this event alive after it runs.
     * {@code False} to remove this event from the game after it runs.
     */
    public boolean perform(int x, int y);
    /**
     * Runs this event when the camera is positioned on that screen.
     * @param x The X Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @param y The Y Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @return {@code True} to keep this event alive after it runs.
     * {@code False} to remove this event from the game after it runs.
     */
    public boolean performScreenLoad(int x, int y);
    /**
     * Runs a player collision event on this tile at the given position.
     * @param x The X Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @param y The Y Coordinate in pixel space the event is occuring in. (NOT Tile coordinates)
     * @return {@code True} to keep this event alive after it runs.
     * {@code False} to remove this event from the game after it runs.
     */
    public boolean performCollision(int x, int y);
}
