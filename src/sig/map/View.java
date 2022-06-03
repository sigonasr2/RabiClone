package sig.map;

public enum View {
    LIGHT_FOLLOW, 
        //  Follows the player loosely, but makes sure there's 
        //  still a border between the edge of the screen and the player.
    FIXED, //Does not follow the player.
    LIGHT_HORIZONTAL_FOLLOW,
        //  Used for rooms that are wide, but only 1 screen tall.
    LIGHT_VERTICAL_FOLLOW,
        //  Used for rooms that are tall, but only 1 screen wide.
}
