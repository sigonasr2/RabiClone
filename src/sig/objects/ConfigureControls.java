package sig.objects;

import java.util.HashMap;

import net.java.games.input.Component;
import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.KeyBind;
import sig.engine.Object;
import sig.engine.PaletteColor;
import sig.engine.Panel;

public class ConfigureControls extends Object{

    HashMap<Component,Float> defaultValues = new HashMap<>();

    protected ConfigureControls(Panel panel) {
        super(panel);
        RabiClone.BACKGROUND_COLOR = PaletteColor.WHITE;
    }

    @Override
    public void update(double updateMult) {
        // TODO Auto-generated method stub
        
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
