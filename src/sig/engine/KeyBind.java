package sig.engine;

import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.POV;
import sig.RabiClone;

public class KeyBind {
    public static HashMap<Action,List<Component>> KEYBINDS = new HashMap<>();
    static HashMap<Action,Boolean> KEYS = new HashMap<>();

    Component c;
    float val;

    public KeyBind(Component c) {
        this.c=c;
    }

    public KeyBind(Component c, float val) {
        this.c=c;
        this.val=val;
    }

    public static boolean isKeyHeld(Action action) {
        return KEYS.getOrDefault(action,false);
    }

    public static boolean isKeyHeld(Component c) {
        if (c instanceof Key) {
            return ((Key)c).isKeyHeld();
        } else if (c instanceof Identifier.Button) {
            return c.getPollData()>0.0f;
        } else
        if (c.getIdentifier()==Identifier.Axis.POV) {
            return c.getPollData()!=POV.CENTER;
        } else
        if (c.getIdentifier() instanceof Identifier.Axis) {
            return Math.abs(c.getPollData())>=c.getDeadZone();
        }
        else {
            System.out.println("Could not find proper recognition for component "+c.getName());
            return false;
        }
    }

    public static void setKeyPressed(Action action, boolean state) {
        KEYS.put(action,state);
    }

    public static void poll() {
        //Polls all KeyBinds based on device.
        for (Action a : Action.values()) {
            boolean held = false;
            Component cc = null;
            for (Component c : KEYBINDS.get(a)) {
                held = isKeyHeld(c);
                actionEventCheck(a,held);
                if (held) {
                    cc=c;
                    break;
                }
            }
            if (held) {
                KEYBINDS.get(a).remove(cc);
                KEYBINDS.get(a).add(0,cc);
            }
        }
    }

    private static void actionEventCheck(Action a, boolean held) {
        if (KeyBind.isKeyHeld(a)&&!held) {
            emitReleaseEvent(a);
        } else
        if (!KeyBind.isKeyHeld(a)&&held) {
            emitPressEvent(a);
        }
        KeyBind.setKeyPressed(a, held);
    }

    private static void emitReleaseEvent(Action a) {
        System.out.println("Release for "+a);
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).KeyReleased(a);
		}
    }
    private static void emitPressEvent(Action a) {
        System.out.println("Press for "+a);
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).KeyPressed(a);
		}
    }
}