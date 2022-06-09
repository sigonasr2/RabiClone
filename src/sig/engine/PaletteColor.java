package sig.engine;

public enum PaletteColor {
    EMERALD,
    MANTIS,
    YELLOW_GREEN,
    YELLOW,
    PEACH,
    ORANGE,
    CRIMSON,
    RED,
    PLUM,
    VIOLET,
    BYZANTIUM,
    DEEP_RUBY,
    PUCE,
    OLD_ROSE,
    MELON,
    LIGHT_HOT_PINK,
    PERSIAN_PINK,
    SKY_MAGENTA,
    RAZZMIC_BERRY,
    BLACK,
    JAPANESE_VIOLET,
    FRENCH_LILAC,
    AMETHYST,
    ORCHID_PINK,
    WHITE,
    FRESH_AIR,
    SKY_BLUE,
    AZURE,
    SLATE_BLUE,
    DARK_ORCHID,
    GRAPE,
    MIDNIGHT_BLUE,
    NORMAL;

    @Override
    public java.lang.String toString() {
        return Character.valueOf((char)26)+Integer.toString(ordinal())+" ";
    }

    
}
