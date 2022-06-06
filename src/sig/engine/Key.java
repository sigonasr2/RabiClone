package sig.engine;

import java.util.HashMap;

import net.java.games.input.Component;

public class Key implements Component{

    public static HashMap<Integer,Identifier.Key> KEY_CONVERSION_MAP = new HashMap<>();
	static HashMap<Component.Identifier.Key,Boolean> KEYS = new HashMap<>();
    int keycode;

    Key(int keycode) {
        this.keycode=keycode;
    }

    public static void setKeyHeld(int keycode,boolean pressed) {
        KEYS.put(KEY_CONVERSION_MAP.get(keycode),pressed);
    }

    public static boolean isKeyHeld(int keycode) {
        return KEYS.getOrDefault(KEY_CONVERSION_MAP.get(keycode),false);
    }

    public boolean isKeyHeld() {
        return KEYS.getOrDefault(getIdentifier(),false);
    }

    @Override
    public Identifier getIdentifier() {
        return KEY_CONVERSION_MAP.get(keycode);
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public boolean isAnalog() {
        return false;
    }

    @Override
    public float getDeadZone() {
        return 0;
    }

    @Override
    public float getPollData() {
        return isKeyHeld()?1.0f:0.0f;
    }

    @Override
    public String getName() {
        return "Java System Keyboard";
    }
    
}
