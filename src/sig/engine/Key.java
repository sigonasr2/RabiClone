package sig.engine;

import java.util.HashMap;

import net.java.games.input.Component;
import sig.RabiClone;

public class Key implements Component{

    public static HashMap<Integer,Identifier.Key> KEY_CONVERSION_MAP = new HashMap<>();
    int keycode;

    Key(int keycode) {
        this.keycode=keycode;
    }

    public boolean isKeyHeld() {
        return RabiClone.p.KEYS.get(getIdentifier());
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
