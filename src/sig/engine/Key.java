package sig.engine;

import java.util.HashMap;

import java.awt.event.KeyEvent;
import net.java.games.input.Component.Identifier;

public class Key extends Identifier{

    protected Key(java.lang.String name) {
		super(name);
	}

    protected Key(int keycode) {
		super(KEY_CONVERSION_MAP.get(keycode).getName());
		this.keycode=keycode;
	}

	public static HashMap<Integer,Identifier.Key> KEY_CONVERSION_MAP = new HashMap<>();
	static HashMap<Identifier.Key,Boolean> KEYS = new HashMap<>();
    int keycode;

	public static void InitializeKeyConversionMap() {
		KEY_CONVERSION_MAP.put(KeyEvent.VK_0,Identifier.Key._0);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_1,Identifier.Key._1);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_2,Identifier.Key._2);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_3,Identifier.Key._3);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_4,Identifier.Key._4);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_5,Identifier.Key._5);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_6,Identifier.Key._6);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_7,Identifier.Key._7);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_8,Identifier.Key._8);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_9,Identifier.Key._9);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_A,Identifier.Key.A);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_B,Identifier.Key.B);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_C,Identifier.Key.C);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_D,Identifier.Key.D);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_E,Identifier.Key.E);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F,Identifier.Key.F);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_G,Identifier.Key.G);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_H,Identifier.Key.H);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_I,Identifier.Key.I);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_J,Identifier.Key.J);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_K,Identifier.Key.K);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_L,Identifier.Key.L);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_M,Identifier.Key.M);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_N,Identifier.Key.N);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_O,Identifier.Key.O);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_P,Identifier.Key.P);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_Q,Identifier.Key.Q);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_R,Identifier.Key.R);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_S,Identifier.Key.S);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_T,Identifier.Key.T);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_U,Identifier.Key.U);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_V,Identifier.Key.V);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_W,Identifier.Key.W);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_X,Identifier.Key.X);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_Y,Identifier.Key.Y);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_Z,Identifier.Key.Z);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_MINUS,Identifier.Key.MINUS);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_EQUALS,Identifier.Key.EQUALS);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_QUOTE,Identifier.Key.GRAVE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_OPEN_BRACKET,Identifier.Key.LBRACKET);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_CLOSE_BRACKET,Identifier.Key.RBRACKET);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_SLASH,Identifier.Key.BACKSLASH);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_SPACE,Identifier.Key.BACK);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_TAB,Identifier.Key.TAB);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_CAPS_LOCK,Identifier.Key.CAPITAL);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SEMICOLON,Identifier.Key.SEMICOLON);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_ENTER,Identifier.Key.RETURN);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SHIFT,Identifier.Key.LSHIFT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_COMMA,Identifier.Key.COMMA);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_PERIOD,Identifier.Key.PERIOD);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SLASH,Identifier.Key.SLASH);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_CONTROL,Identifier.Key.LCONTROL);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_ALT,Identifier.Key.LALT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SPACE,Identifier.Key.SPACE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_WINDOWS,Identifier.Key.SYSRQ);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_CONTEXT_MENU,Identifier.Key.APPS);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_HOME,Identifier.Key.HOME);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_INSERT,Identifier.Key.INSERT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_DELETE,Identifier.Key.DELETE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_END,Identifier.Key.END);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_PAGE_UP,Identifier.Key.PAGEUP);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_PAGE_DOWN,Identifier.Key.PAGEDOWN);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_ESCAPE,Identifier.Key.ESCAPE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F1,Identifier.Key.F1);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F2,Identifier.Key.F2);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F3,Identifier.Key.F3);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F4,Identifier.Key.F4);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F5,Identifier.Key.F5);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F6,Identifier.Key.F6);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F7,Identifier.Key.F7);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F8,Identifier.Key.F8);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F9,Identifier.Key.F9);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F10,Identifier.Key.F10);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F11,Identifier.Key.F11);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_F12,Identifier.Key.F12);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_PRINTSCREEN,Identifier.Key.KANA);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SCROLL_LOCK,Identifier.Key.SCROLL);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_PAUSE,Identifier.Key.PAUSE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_LEFT,Identifier.Key.LEFT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_RIGHT,Identifier.Key.RIGHT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_UP,Identifier.Key.UP);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_DOWN,Identifier.Key.DOWN);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUM_LOCK,Identifier.Key.NUMLOCK);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_ASTERISK,Identifier.Key.AX);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_DIVIDE,Identifier.Key.DIVIDE);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_SUBTRACT,Identifier.Key.SUBTRACT);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_ADD,Identifier.Key.ADD);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_DECIMAL,Identifier.Key.DECIMAL);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD0,Identifier.Key.NUMPAD0);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD1,Identifier.Key.NUMPAD1);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD2,Identifier.Key.NUMPAD2);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD3,Identifier.Key.NUMPAD3);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD4,Identifier.Key.NUMPAD4);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD5,Identifier.Key.NUMPAD5);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD6,Identifier.Key.NUMPAD6);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD7,Identifier.Key.NUMPAD7);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD8,Identifier.Key.NUMPAD8);
		KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD9,Identifier.Key.NUMPAD9);
	}

    public static void setKeyHeld(int keycode,boolean pressed) {
        KEYS.put(KEY_CONVERSION_MAP.get(keycode),pressed);
		//System.out.println(KEYS);
    }

    public static boolean isKeyHeld(int keycode) {
        return KEYS.getOrDefault(KEY_CONVERSION_MAP.get(keycode),false);
    }

    public boolean isKeyHeld() {
        return KEYS.getOrDefault(KEY_CONVERSION_MAP.get(keycode),false);
    }
    
}
