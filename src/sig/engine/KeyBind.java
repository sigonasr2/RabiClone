package sig.engine;

import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;

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

    public static boolean IsKeyHeld(Action action) {
        return KEYS.getOrDefault(action,false);
    }

    public static void setKeyPressed(Action action, boolean state) {
        KEYS.put(action,state);
        /*if (c instanceof Key) {
            return ((Key)c).isKeyHeld();
        } else 
        if (c instanceof Identifier.Button) {
            return c.getPollData()>0.0f;
        } else 
        if (c.getIdentifier()==Identifier.Axis.POV) {
            return val==c.getPollData();
        } else 
        if (c instanceof Identifier.Axis) {
            return c.getPollData()>=c.getDeadZone()&&Math.signum(c.getPollData())==Math.signum(val);
        }*/
    }

    public static void poll() {
        //Polls all KeyBinds based on device.
        for (Action a : Action.values()) {
            for (Component c : KEYBINDS.get(a)) {
                if (c instanceof Key) {
                    actionEventCheck(a,((Key)c).isKeyHeld());
                } else
                if (c instanceof Identifier.Button) {
                    actionEventCheck(a,c.getPollData()>0.0f);
                } else
                if (c.getIdentifier()==Identifier.Axis.POV) {
                    actionEventCheck(a,a.val==c.getPollData());
                } else
                if (c.getIdentifier() instanceof Identifier.Axis) {
                    actionEventCheck(a,c.getPollData()>=c.getDeadZone()&&Math.signum(c.getPollData())==Math.signum(a.val));
                }
            }
        }
    }

    private static void actionEventCheck(Action a, boolean held) {
        if (KeyBind.IsKeyHeld(a)&&!held) {
            emitReleaseEvent(a);
        } else
        if (!KeyBind.IsKeyHeld(a)&&held) {
            emitPressEvent(a);
        }
        KeyBind.setKeyPressed(a, held);
    }

    private static void emitReleaseEvent(Action a) {
        System.out.println("Release for "+a);
    }
    private static void emitPressEvent(Action a) {
        System.out.println("Press for "+a);
    }
}