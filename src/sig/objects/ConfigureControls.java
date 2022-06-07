package sig.objects;

import java.awt.event.MouseEvent;
import java.util.List;

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

    Action selectedAction = Action.MOVE_RIGHT;
    boolean assigningKey = false;

    public ConfigureControls(Panel panel) {
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
                    if (assigningKey) {
                        List<KeyBind> clist = KeyBind.KEYBINDS.get(selectedAction);
                        clist.add(new KeyBind(RabiClone.CONTROLLERS[i],e.getComponent().getIdentifier(),e.getValue()));
                        KeyBind.KEYBINDS.put(selectedAction,clist);
                        assigningKey=false;
                    }
                    //System.out.println(e.getComponent().getName()+" value: "+e.getValue());
                }
            }
        }
    }

    @Override
    public void draw(byte[] p) {
        int y = 4;
        if (!assigningKey) {
            for (Action a : Action.values()) {
                if (RabiClone.MOUSE_POS.getY()>=getY()+y&&RabiClone.MOUSE_POS.getY()<getY()+y+Font.PROFONT_12.getGlyphHeight()+4) {
                    selectedAction=a;
                    Draw_Rect(p,(byte)PaletteColor.PEACH.ordinal(),0,getY()+y,RabiClone.BASE_WIDTH,Font.PROFONT_12.getGlyphHeight()+4);
                }
                Draw_Text_Ext(4,getY()+y,DisplayActionKeys(a),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.MIDNIGHT_BLUE);
                y+=Font.PROFONT_12.getGlyphHeight()+4;
            }
        } else {
            Draw_Text_Ext(4, 4, new StringBuilder("Press a key to assign to ").append(selectedAction), Font.PROFONT_12, Alpha.ALPHA0, PaletteColor.MIDNIGHT_BLUE);
        }
    }

    @Override
    protected void MousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1) {
            assigningKey=true;
        }
    }

    private StringBuilder DisplayActionKeys(Action a) {
        StringBuilder sb = new StringBuilder(a.toString()).append(": ");
        for (int i=0;i<KeyBind.KEYBINDS.get(a).size();i++) {
            KeyBind c = KeyBind.KEYBINDS.get(a).get(i);
            sb.append(c.isKeyHeld()?PaletteColor.YELLOW_GREEN:"").append(c.getName()).append(PaletteColor.MIDNIGHT_BLUE).append(i!=KeyBind.KEYBINDS.get(a).size()-1?",":"");
        }
        return sb;
    }
    
}
