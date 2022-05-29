package sig.engine;

public enum MouseScrollValue {
    UP(), //-1 is up
    DOWN() /*1 is down*/;

    public static MouseScrollValue getValue(int value) {
        return value==-1?UP:DOWN;
    }
}
