package sig.engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import net.java.games.input.Component.Identifier;

public enum Action {
    MOVE_RIGHT(new KeyBind(KeyEvent.VK_RIGHT),new KeyBind(KeyEvent.VK_D)),
    MOVE_LEFT(new KeyBind(KeyEvent.VK_LEFT),new KeyBind(KeyEvent.VK_A)),
    JUMP(new KeyBind(KeyEvent.VK_SPACE),new KeyBind(KeyEvent.VK_W)),
    FALL(new KeyBind(KeyEvent.VK_DOWN),new KeyBind(KeyEvent.VK_S)),
    SLIDE(new KeyBind(KeyEvent.VK_CONTROL)),
    LEVEL_EDITOR(new KeyBind(KeyEvent.VK_F2)),
    PLAY_GAME(new KeyBind(KeyEvent.VK_F1)),
    EDITOR_SET_VIEW(new KeyBind(KeyEvent.VK_F3)),
    EDITOR_SET_TYPE(new KeyBind(KeyEvent.VK_F4)),
    EDITOR_SET_BACKGROUND(new KeyBind(KeyEvent.VK_F5)),;

    float val;
    Key controllingKey;

    Action(KeyBind...keybinds) {
        KeyBind.KEYBINDS.put(this,new ArrayList<>(Arrays.asList(keybinds)));
    }
    Action(byte port, Identifier.Axis axis,float val) {
        ArrayList<KeyBind> comps = new ArrayList<KeyBind>();
        comps.add(new KeyBind(port,axis,val));
        KeyBind.KEYBINDS.put(this,comps);
    }
}
