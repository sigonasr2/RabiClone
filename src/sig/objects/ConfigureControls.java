package sig.objects;

import net.java.games.input.Component;
import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.Key;
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(byte[] p) {
        int y=4;
        for (Action a : Action.values()) {
            Draw_Text_Ext(4,getY(),DisplayActionKeys(a),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.MIDNIGHT_BLUE);
        }
    }

    private StringBuilder DisplayActionKeys(Action a) {
        StringBuilder sb = new StringBuilder(a.toString()).append(": ");
        boolean first=true;
        for (Component c : KeyBind.KEYBINDS.get(a)) {
            sb.append(((Key)c).isKeyHeld()?PaletteColor.YELLOW_GREEN:PaletteColor.MIDNIGHT_BLUE).append(c.getName()).append(!first?",":"");
            sb.append("\n");
        }
        return sb;
    }
    
}
