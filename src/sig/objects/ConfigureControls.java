package sig.objects;

import net.java.games.input.Component;
import net.java.games.input.Event;
import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.KeyBind;
import sig.engine.Object;
import sig.engine.PaletteColor;
import sig.engine.Panel;

public class ConfigureControls extends Object{

    protected ConfigureControls(Panel panel) {
        super(panel);
        RabiClone.BACKGROUND_COLOR = PaletteColor.WHITE;
    }

    @Override
    public void update(double updateMult) {
        Event e = new Event();
        for (int i=0;i<RabiClone.CONTROLLERS.length;i++) {
            if (RabiClone.CONTROLLERS[i].poll()) {
                Component[] components = RabiClone.CONTROLLERS[i].getComponents();
                for (int j=0;j<components.length;j++) {
                    //Component c = components[j];
                    //System.out.println(c.getName()+","+c.getIdentifier()+": "+c.getPollData());
                    
                }
                //System.out.println("--------");
                if (RabiClone.CONTROLLERS[i].getEventQueue().getNextEvent(e)) {
                    System.out.println(e.getComponent().getName()+" value: "+e.getValue());
                }
            }
        }
    }

    @Override
    public void draw(byte[] p) {
        for (Action a : Action.values()) {
            Draw_Text_Ext(4,getY(),DisplayActionKeys(a),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.MIDNIGHT_BLUE);
        }
    }

    private StringBuilder DisplayActionKeys(Action a) {
        StringBuilder sb = new StringBuilder(a.toString()).append(": ");
        boolean first=true;
        for (KeyBind c : KeyBind.KEYBINDS.get(a)) {
            sb.append(c.isKeyHeld()?PaletteColor.YELLOW_GREEN:"").append(c.c.getName()).append(PaletteColor.MIDNIGHT_BLUE).append(!first?",":"");
            sb.append("\n");
        }
        return sb;
    }
    
}
