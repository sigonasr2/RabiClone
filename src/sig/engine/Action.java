package sig.engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import net.java.games.input.Component;

public enum Action {
    MOVE_RIGHT(new Key(KeyEvent.VK_RIGHT),new Key(KeyEvent.VK_D)),
    MOVE_LEFT(new Key(KeyEvent.VK_LEFT),new Key(KeyEvent.VK_A)),
    JUMP(new Key(KeyEvent.VK_SPACE),new Key(KeyEvent.VK_W)),
    FALL(new Key(KeyEvent.VK_DOWN),new Key(KeyEvent.VK_S)),
    SLIDE(new Key(KeyEvent.VK_CONTROL)),
    LEVEL_EDITOR(new Key(KeyEvent.VK_F2)),
    PLAY_GAME(new Key(KeyEvent.VK_F1)),;

    float val;
    Key controllingKey;

    Action(Component...components) {
        KeyBind.KEYBINDS.put(this,new ArrayList<>(Arrays.asList(components)));
    }
    Action(Component axis,float val) {
        ArrayList<Component> comps = new ArrayList<Component>();
        comps.add(axis);
        KeyBind.KEYBINDS.put(this,comps);
    }
}
