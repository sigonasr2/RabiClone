package sig.engine;

import java.util.HashMap;
import java.util.List;

import net.java.games.input.Controller;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.POV;
import sig.RabiClone;

public class KeyBind {
    public static HashMap<Action,List<KeyBind>> KEYBINDS = new HashMap<>();
    static HashMap<Action,Boolean> KEYS = new HashMap<>();

    public Controller c;
    public Identifier id;
    float val;

    public KeyBind(Controller c, Identifier id) {
        this.c=c;
        this.id=id;
    }

    public KeyBind(int keycode) {
        this(null,new Key(keycode));
    }

    public KeyBind(Controller c, Identifier id, float val) {
        this.c=c;
        this.id=id;
        this.val=val;
    }

    public boolean isKeyHeld() {
        if (id instanceof Key) {
            return ((Key)id).isKeyHeld();
        } else if (id instanceof Identifier.Button) {
            return c.getComponent(id).getPollData()>0.0f;
        } else
        if (c.getComponent(id).getIdentifier()==Identifier.Axis.POV) {
            if (val==POV.DOWN) {
                return c.getComponent(id).getPollData()==POV.DOWN||
                c.getComponent(id).getPollData()==POV.DOWN_LEFT||
                c.getComponent(id).getPollData()==POV.DOWN_RIGHT;
            } else
            if (val==POV.UP) {
                    return c.getComponent(id).getPollData()==POV.UP||
                    c.getComponent(id).getPollData()==POV.UP_LEFT||
                    c.getComponent(id).getPollData()==POV.UP_RIGHT;
            } else
            if (val==POV.RIGHT) {
                    return c.getComponent(id).getPollData()==POV.RIGHT||
                    c.getComponent(id).getPollData()==POV.UP_RIGHT||
                    c.getComponent(id).getPollData()==POV.DOWN_RIGHT;
            } else
            if (val==POV.LEFT) {
                    return c.getComponent(id).getPollData()==POV.LEFT||
                    c.getComponent(id).getPollData()==POV.DOWN_LEFT||
                    c.getComponent(id).getPollData()==POV.UP_LEFT;
            } else {
                System.err.println("Unexpected value for POV! Must be a cardinal direction! Given value: "+val);
                return false;
            }
        } else
        if (id instanceof Identifier.Axis) {
            return Math.abs(c.getComponent(id).getPollData())>=c.getComponent(id).getDeadZone()&&Math.signum(c.getComponent(id).getPollData())==Math.signum(val);
        }
        else {
            System.out.println("Could not find proper recognition for component "+id.getName());
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
            for (KeyBind c : KEYBINDS.get(a)) {
                held = c.isKeyHeld();
                if (held) {
                    break;
                }
            }
            actionEventCheck(a,held);
            /*if (held) {
                if (KEYBINDS.get(a).get(0)!=cc) {
                    for (int i=0;i<KEYBINDS.get(a).size()-1;i++) {
                        KEYBINDS.get(a).set(i+1,KEYBINDS.get(a).get(i));
                    }
                    KEYBINDS.get(a).set(0,cc);
                }
            }*/
        }
    }

    public static boolean isKeyHeld(Action action) {
        return KEYS.getOrDefault(action,false);
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

    public String getName() {
        if (c!=null) {
            return c.getComponent(id).getName();
        } else
        if (id instanceof Key) {
            return ((Key)id).getName();
        } else {
            return "?";
        }
    }
}