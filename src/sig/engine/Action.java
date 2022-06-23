package sig.engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import net.java.games.input.Component.Identifier;

public enum Action {
    MOVE_RIGHT(new KeyBind(KeyEvent.VK_RIGHT),new KeyBind(KeyEvent.VK_D)),
    MOVE_LEFT(new KeyBind(KeyEvent.VK_LEFT),new KeyBind(KeyEvent.VK_A)),
    JUMP(new KeyBind(KeyEvent.VK_SPACE),new KeyBind(KeyEvent.VK_W),new KeyBind(KeyEvent.VK_UP)),
    FALL(new KeyBind(KeyEvent.VK_DOWN),new KeyBind(KeyEvent.VK_S)),
    ATTACK(new KeyBind(KeyEvent.VK_Z)),
    SLIDE(new KeyBind(KeyEvent.VK_CONTROL)),
    LEVEL_EDITOR(new KeyBind(KeyEvent.VK_F2)),
    PLAY_GAME(new KeyBind(KeyEvent.VK_F1)),
    EDITOR_SET_VIEW(new KeyBind(KeyEvent.VK_F3)),
    EDITOR_SET_TYPE(new KeyBind(KeyEvent.VK_F4)),
    EDITOR_SET_BACKGROUND(new KeyBind(KeyEvent.VK_F5)),
    _1(new KeyBind(KeyEvent.VK_1),new KeyBind(KeyEvent.VK_NUMPAD1)),
    _2(new KeyBind(KeyEvent.VK_2),new KeyBind(KeyEvent.VK_NUMPAD2)),
    _3(new KeyBind(KeyEvent.VK_3),new KeyBind(KeyEvent.VK_NUMPAD3)),
    _4(new KeyBind(KeyEvent.VK_4),new KeyBind(KeyEvent.VK_NUMPAD4)),
    _5(new KeyBind(KeyEvent.VK_5),new KeyBind(KeyEvent.VK_NUMPAD5)),
    _6(new KeyBind(KeyEvent.VK_6),new KeyBind(KeyEvent.VK_NUMPAD6)),
    _7(new KeyBind(KeyEvent.VK_7),new KeyBind(KeyEvent.VK_NUMPAD7)),
    _8(new KeyBind(KeyEvent.VK_8),new KeyBind(KeyEvent.VK_NUMPAD8)),
    _9(new KeyBind(KeyEvent.VK_9),new KeyBind(KeyEvent.VK_NUMPAD9)),
    _0(new KeyBind(KeyEvent.VK_0),new KeyBind(KeyEvent.VK_NUMPAD0)),
    BACKSPACE(new KeyBind(KeyEvent.VK_BACK_SPACE)),
    ENTER(new KeyBind(KeyEvent.VK_ENTER));

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
