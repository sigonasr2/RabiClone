package sig.engine;

import java.util.HashMap;
import java.util.List;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.POV;
import sig.RabiClone;

public class KeyBind {
    public static HashMap<Action,List<KeyBind>> KEYBINDS = new HashMap<>();
    static HashMap<Action,Boolean> KEYS = new HashMap<>();

    public byte port;
    public Identifier id;
    float val;

    public KeyBind(byte port, Identifier id) {
        this.port=port;
        this.id=id;
    }

    public KeyBind(int keycode) {
        this((byte)-1,new Key(keycode));
    }

    public KeyBind(byte port, Identifier id, float val) {
        this.port=port;
        this.id=id;
        this.val=val;
    }

    public boolean isKeyHeld() {
        if (id instanceof Key) {
            return ((Key)id).isKeyHeld();
        } else if (RabiClone.CONTROLLERS.length>port && id instanceof Identifier.Button) {
            return RabiClone.CONTROLLERS[port].getComponent(id).getPollData()>0.0f;
        } else
        if (RabiClone.CONTROLLERS.length>port && RabiClone.CONTROLLERS[port].getComponent(id).getIdentifier()==Identifier.Axis.POV) {
            if (val==POV.DOWN) {
                return RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.DOWN||
                RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.DOWN_LEFT||
                RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.DOWN_RIGHT;
            } else
            if (val==POV.UP) {
                    return RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.UP||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.UP_LEFT||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.UP_RIGHT;
            } else
            if (val==POV.RIGHT) {
                    return RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.RIGHT||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.UP_RIGHT||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.DOWN_RIGHT;
            } else
            if (val==POV.LEFT) {
                    return RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.LEFT||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.DOWN_LEFT||
                    RabiClone.CONTROLLERS[port].getComponent(id).getPollData()==POV.UP_LEFT;
            } else {
                System.err.println("Unexpected value for POV! Must be a cardinal direction! Given value: "+val);
                return false;
            }
        } else
        if (RabiClone.CONTROLLERS.length>port && id instanceof Identifier.Axis) {
            return Math.abs(RabiClone.CONTROLLERS[port].getComponent(id).getPollData())>=RabiClone.CONTROLLERS[port].getComponent(id).getDeadZone()&&Math.signum(RabiClone.CONTROLLERS[port].getComponent(id).getPollData())==Math.signum(val);
        }
        else {
            //System.out.println("Could not find proper recognition for component "+id.getName());
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

    public java.lang.String getName() {
        if (RabiClone.CONTROLLERS.length>port&&port!=-1) {
            return RabiClone.CONTROLLERS[port].getComponent(id).getName();
        } else
        if (id instanceof Key) {
            return ((Key)id).getName();
        } else {
            return "?";
        }
    }
}