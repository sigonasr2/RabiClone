package sig.objects;

import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.POV;
import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.Key;
import sig.engine.KeyBind;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.String;
import sig.engine.Transform;
import sig.engine.objects.Object;
import sig.map.Map;

public class ConfigureControls extends Object{

    final static File GAME_CONTROLS_FILE = new File("controls.config");

    Action selectedAction = Action.MOVE_RIGHT;
    KeyBind selectedKeybind = null;
    boolean assigningKey = false;
    static List<List<Integer>> actionHighlightSections = new ArrayList<>();
    int storedX=-1;
    int storedY=-1;
    int storedEndX=-1;

    public ConfigureControls(Panel panel) {
        super(panel);
        RabiClone.BACKGROUND_COLOR = PaletteColor.WHITE;
        if (GAME_CONTROLS_FILE.exists()) {
            RabiClone.reloadControllerList = true;
        }
        updateHighlightSections();
    }

    public static void LoadControls() {
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(GAME_CONTROLS_FILE));
            KeyBind.KEYBINDS.clear();
            while (stream.available()>0) {
                Action a = Action.valueOf(readString(stream));
                byte port = stream.readByte();
                do {
                    if (port==(byte)-1) {
                        int keycode = stream.readInt();
                        KeyBind kb = new KeyBind(keycode);
                        appendToKeybind(a,kb);
                    } else {
                        java.lang.String controllerName = readString(stream);
                        Controller controller=null;
                        for (int i = 0; i < RabiClone.CONTROLLERS.length; i++) {
                            if (RabiClone.CONTROLLERS[i].getType() == Controller.Type.KEYBOARD
                                    || RabiClone.CONTROLLERS[i].getType() == Controller.Type.MOUSE) {
                                continue;
                            } else
                            if (RabiClone.CONTROLLERS[i].getName().equals(controllerName)) {
                                controller=RabiClone.CONTROLLERS[i];
                            }
                        }
                        if (controller==null) {
                            //Discard these bits of data as we didn't find a controller.
                            readString(stream);
                            stream.readFloat();
                        } else {
                            java.lang.String componentName = readString(stream);
                            Component c=null;
                            for (Component cc : controller.getComponents()) {
                                if (cc.getName().equals(componentName)) {
                                    c=cc;
                                    break;
                                }
                            }
                            float val = stream.readFloat();
                            if (c!=null) {
                                KeyBind kb = new KeyBind(port,c.getIdentifier(),val);
                                appendToKeybind(a,kb);
                            } else {
                                System.out.println("Could not load component "+componentName+" for keybind "+a);
                            }
                        }
                    }
                    port = stream.readByte();
                } while (port!=(byte)-2);
            }
            FillInDefaults();
            updateHighlightSections();
        } catch (IOException e) {
            e.printStackTrace();
            RabiClone.setupDefaultControls();
        }
    }

    private static void FillInDefaults() {
        for (Action a : Action.values()) {
            if (KeyBind.KEYBINDS.get(a)==null||KeyBind.KEYBINDS.get(a).size()==0) {
                List<KeyBind> keybinds = KeyBind.KEYBINDS.getOrDefault(a,new ArrayList<>());
                keybinds.addAll(RabiClone.DEFAULT_KEYBINDS.get(a));
                KeyBind.KEYBINDS.put(a,keybinds);
            }
        }
    }

    private static void appendToKeybind(Action a,KeyBind kb) {
        List<KeyBind> binds = KeyBind.KEYBINDS.getOrDefault(a,new ArrayList<KeyBind>());
        binds.add(kb);
        KeyBind.KEYBINDS.put(a,binds);
    }

    private static java.lang.String readString(DataInputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            Character c = stream.readChar();
            if (c=='\0') {
                return sb.toString();
            } else {
                sb.append(c);
            }
        }
    }

    public static void SaveControls() {
        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(GAME_CONTROLS_FILE));
            for (Action a  : Action.values()) {
                writeString(a.name(),stream);
                for (KeyBind k : KeyBind.KEYBINDS.get(a)) {
                    if (k.port==-1||k.port<RabiClone.CONTROLLERS.length) {
                        stream.writeByte(k.port);
                        if (k.port==(byte)-1) {
                            stream.writeInt(((Key)k.id).getKeyCode());
                        } else {
                            writeString(RabiClone.CONTROLLERS[k.port].getName(),stream);
                            writeString(RabiClone.CONTROLLERS[k.port].getComponent(k.id).getName(),stream);
                            stream.writeFloat(k.getVal());
                        }
                    }
                }
                stream.writeByte((byte)-2);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeString(java.lang.String s, DataOutputStream stream) throws IOException {
        stream.writeChars(s);
        stream.writeChar('\0');
    }

    public static void updateHighlightSections() {
        for (int i=0;i<Action.values().length;i++) {
            Action a = Action.values()[i];
            actionHighlightSections.add(new ArrayList<Integer>());
            StringBuilder renderedText=new StringBuilder(a.toString()).append(": ");
            List<Integer> sectionList = actionHighlightSections.get(a.ordinal());
            sectionList.clear();
            for (int j=0;j<KeyBind.KEYBINDS.get(a).size();j++) {
                KeyBind c = KeyBind.KEYBINDS.get(a).get(j);
                sectionList.add(renderedText.length()+1);
                renderedText.append(c.getName());
                sectionList.add(renderedText.length());
                renderedText.append(j!=KeyBind.KEYBINDS.get(a).size()-1?",":"");
            }
        }
    }

    @Override
    public void update(double updateMult) {
        Event e = new Event();
        for (int i=0;i<RabiClone.CONTROLLERS.length;i++) {
            Component[] components = RabiClone.CONTROLLERS[i].getComponents();
            for (int j=0;j<components.length;j++) {
                //Component c = components[j];
                //System.out.println(c.getName()+","+c.getIdentifier()+": "+c.getPollData());
            }
            //System.out.println("--------");
            while (RabiClone.CONTROLLERS[i].getEventQueue().getNextEvent(e)) {
                if (assigningKey) {
                    List<KeyBind> clist = KeyBind.KEYBINDS.get(selectedAction);
                    Identifier id = e.getComponent().getIdentifier();
                    if (id==Identifier.Axis.POV) {
                        if (e.getValue()!=POV.DOWN&&
                            e.getValue()!=POV.RIGHT&&
                            e.getValue()!=POV.LEFT&&
                            e.getValue()!=POV.UP) {
                            continue; //Can't add ordinal directions, only cardinal.
                        }
                    }
                    clist.add(new KeyBind((byte)i,id,e.getValue()));
                    KeyBind.KEYBINDS.put(selectedAction,clist);
                    updateHighlightSections();
                    assigningKey=false;
                }
                System.out.println(e.getComponent().getName()+" value: "+e.getValue());
            }
        }
    }

    @Override
    public void draw(byte[] p) {
        if (!RabiClone.reloadControllerList) {
            int y = 4;
            if (!assigningKey) {
                selectedAction=null;
                selectedKeybind=null;
                for (Action a : Action.values()) {
                    if (RabiClone.MOUSE_POS.getY()>=getY()+y&&RabiClone.MOUSE_POS.getY()<getY()+y+Font.PROFONT_12.getGlyphHeight()+4) {
                        selectedAction=a;
                        Draw_Rect(p,PaletteColor.PEACH,0,getY()+y,RabiClone.BASE_WIDTH,Font.PROFONT_12.getGlyphHeight()+4);
                    }
                    for (int i=0;i<actionHighlightSections.get(a.ordinal()).size();i+=2) {
                        List<Integer> sectionList = actionHighlightSections.get(a.ordinal());
                        int startX=sectionList.get(i)*Font.PROFONT_12.getGlyphWidth()-4;
                        int endX=sectionList.get(i+1)*Font.PROFONT_12.getGlyphWidth()+4;
                        if (selectedKeybind==null&&RabiClone.MOUSE_POS.getY()>=getY()+y&&RabiClone.MOUSE_POS.getY()<getY()+y+Font.PROFONT_12.getGlyphHeight()+4&&RabiClone.MOUSE_POS.getX()>=startX&&RabiClone.MOUSE_POS.getX()<=endX) {
                            Draw_Rect(p,PaletteColor.RED,startX,getY()+y,endX-startX,Font.PROFONT_12.getGlyphHeight()+4);
                            storedX=startX;
                            storedY=y;
                            storedEndX=endX;
                            selectedKeybind=KeyBind.KEYBINDS.get(a).get(i/2);
                            break;
                        }
                    }
                    Draw_Text_Ext(4,getY()+y,DisplayActionKeys(a),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.MIDNIGHT_BLUE);
                    y+=Font.PROFONT_12.getGlyphHeight()+4;
                }
                if (selectedKeybind!=null) {
                    Draw_Line(p,storedX,getY()+storedY,storedEndX,getY()+storedY+Font.PROFONT_12.getGlyphHeight()+4,PaletteColor.BLACK,Alpha.ALPHA32);
                    Draw_Line(p,storedX,getY()+storedY+Font.PROFONT_12.getGlyphHeight()+4,storedEndX,getY()+storedY,PaletteColor.BLACK,Alpha.ALPHA32);
                }
            } else {
                Draw_Text_Ext(4, 4, new String("Press a key to assign to ").append(selectedAction), Font.PROFONT_12, Alpha.ALPHA0, PaletteColor.MIDNIGHT_BLUE);
            }
        } else {
            Draw_Text_Ext(4, 4, new String("Preparing controller list..."), Font.PROFONT_12, Alpha.ALPHA0, PaletteColor.MIDNIGHT_BLUE);
        }
    }

    @Override
    public void MousePressed(MouseEvent e) {
        if ((e.getButton()==MouseEvent.BUTTON3||e.getButton()==MouseEvent.BUTTON1)&&selectedKeybind!=null) {
            //Remove that keybind.
            List<KeyBind> keybinds = KeyBind.KEYBINDS.get(selectedAction);
            System.out.println("Remove "+selectedKeybind);
            System.out.println(keybinds.remove(selectedKeybind));
            KeyBind.KEYBINDS.put(selectedAction,keybinds);
            updateHighlightSections();
        } else
        if (e.getButton()==MouseEvent.BUTTON1&&selectedAction!=null) {
            assigningKey=true;
        }
    }

    private String DisplayActionKeys(Action a) {
        String sb = new String(a.toString()).append(": ");
        for (int i=0;i<KeyBind.KEYBINDS.get(a).size();i++) {
            KeyBind c = KeyBind.KEYBINDS.get(a).get(i);
            sb.append(c.isKeyHeld()?PaletteColor.YELLOW_GREEN:"").append(c.getName()).append(PaletteColor.MIDNIGHT_BLUE).append(i!=KeyBind.KEYBINDS.get(a).size()-1?",":"");
        }
        return sb;
    }

    

    @Override
    @SuppressWarnings("incomplete-switch")
    public void KeyPressed(Action a) {
        switch(a) {
            case PLAY_GAME:{
                SaveControls();
                RabiClone.OBJ.clear();
                RabiClone.ResetGame();
                Map.LoadMap(RabiClone.CURRENT_MAP);
                RabiClone.OBJ.add(RabiClone.level_renderer = new LevelRenderer(RabiClone.p));
                RabiClone.StartGame();
            }break;
            case LEVEL_EDITOR:{
                SaveControls();
                RabiClone.OBJ.clear();
                RabiClone.ResetGame();
                Map.LoadMap(RabiClone.CURRENT_MAP);
                RabiClone.OBJ.add(RabiClone.level_renderer = new EditorRenderer(RabiClone.p));
            }break;
        }
    }

    public void rawKeyPressed(int keyCode) {
        if (assigningKey) {
            List<KeyBind> clist = KeyBind.KEYBINDS.get(selectedAction);
            clist.add(new KeyBind(keyCode));
            KeyBind.KEYBINDS.put(selectedAction,clist);
            updateHighlightSections();
            assigningKey=false;
        }
    }

    @Override
    public Transform getSpriteTransform() {
        return Transform.NONE;
    }
    
}

enum ControlType{
    BUTTON,
    POV,
    AXIS
}