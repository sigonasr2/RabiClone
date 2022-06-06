package sig;

import javax.swing.JFrame;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Component.Identifier;

import java.util.ArrayList;
import java.util.List;

import sig.engine.Panel;
import sig.engine.Point;
import sig.map.Maps;
import sig.objects.EditorRenderer;
import sig.objects.Erinoah;
import sig.objects.LevelRenderer;
import sig.objects.Player;
import sig.engine.Key;
import sig.engine.Object;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class RabiClone{
	public static final String PROGRAM_NAME="Sig's Java Project Template";

	public static int UPCOUNT=0;
	public static Panel p;
	public static JFrame f;

	public static List<Object> OBJ = new ArrayList<Object>();
	boolean newSizeSet=false;

	public static int BASE_WIDTH=512;
	public static int BASE_HEIGHT=288;
	public static int SIZE_MULTIPLIER=1;
	public static Point MOUSE_POS;

	public static LevelRenderer level_renderer;
	public static Player player;

	public static Maps CURRENT_MAP = Maps.WORLD1;
	public static Controller[] CONTROLLERS = new Controller[]{};
	public static void main(String[] args) {

		InitializeKeyConversionMap();

		f = new JFrame(PROGRAM_NAME);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setSize(BASE_WIDTH,BASE_HEIGHT); //1024x576 (64x64)
		ChooseBestRatio();

		p = new Panel(f);

		MOUSE_POS=p.mousePos;
		
		p.init();
		
		f.add(p);
		f.addKeyListener(p);
		f.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-f.getWidth())/2), (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight()-f.getHeight())/2));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		OBJ.add(level_renderer = new LevelRenderer(p));
		StartGame();

		p.render();

		long lastGameTime = System.nanoTime();
		while (true) {
			long timePassed = System.nanoTime()-lastGameTime;
			lastGameTime=System.nanoTime();
			double updateMult = Math.min(1/60d,timePassed/1000000000d);

			CONTROLLERS = ControllerEnvironment.getDefaultEnvironment().getControllers();
			for (int i=0;i<CONTROLLERS.length;i++) {
				if (CONTROLLERS[i].poll()) {
					Component[] components = CONTROLLERS[i].getComponents();
					for (int j=0;j<components.length;j++) {
						Component c = components[j];
						System.out.println(c.getName()+","+c.getIdentifier()+": "+c.getPollData());
					}
					System.out.println("--------");
				}
				/*EventQueue queue = controller_list[i].getEventQueue();

				while (queue.getNextEvent(event)) {
					Component c = event.getComponent();
					System.out.println(c.getName()+","+c.getIdentifier()+": "+c.getPollData());
				}*/
			}

			if (Key.isKeyHeld(KeyEvent.VK_F1)) {
				if (level_renderer instanceof EditorRenderer) {
					OBJ.remove(level_renderer);
					OBJ.add(level_renderer=new LevelRenderer(p));
					StartGame();
				}
			}
			if (Key.isKeyHeld(KeyEvent.VK_F2)) {
				if (!(level_renderer instanceof EditorRenderer)) {
					OBJ.clear();
					ResetGame();
					OBJ.add(level_renderer=new EditorRenderer(p));
				}
			}

			for (int i=0;i<OBJ.size();i++) {
				OBJ.get(i).update(updateMult);
				if (OBJ.get(i).isMarkedForDeletion()) {
					OBJ.remove(i--);
				}
			}
		}
	}

	private static void InitializeKeyConversionMap() {
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_0,Identifier.Key._0);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_1,Identifier.Key._1);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_2,Identifier.Key._2);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_3,Identifier.Key._3);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_4,Identifier.Key._4);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_5,Identifier.Key._5);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_6,Identifier.Key._6);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_7,Identifier.Key._7);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_8,Identifier.Key._8);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_9,Identifier.Key._9);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_A,Identifier.Key.A);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_B,Identifier.Key.B);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_C,Identifier.Key.C);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_D,Identifier.Key.D);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_E,Identifier.Key.E);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F,Identifier.Key.F);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_G,Identifier.Key.G);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_H,Identifier.Key.H);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_I,Identifier.Key.I);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_J,Identifier.Key.J);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_K,Identifier.Key.K);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_L,Identifier.Key.L);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_M,Identifier.Key.M);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_N,Identifier.Key.N);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_O,Identifier.Key.O);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_P,Identifier.Key.P);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_Q,Identifier.Key.Q);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_R,Identifier.Key.R);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_S,Identifier.Key.S);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_T,Identifier.Key.T);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_U,Identifier.Key.U);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_V,Identifier.Key.V);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_W,Identifier.Key.W);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_X,Identifier.Key.X);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_Y,Identifier.Key.Y);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_Z,Identifier.Key.Z);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_MINUS,Identifier.Key.MINUS);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_EQUALS,Identifier.Key.EQUALS);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_QUOTE,Identifier.Key.GRAVE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_OPEN_BRACKET,Identifier.Key.LBRACKET);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_CLOSE_BRACKET,Identifier.Key.RBRACKET);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_SLASH,Identifier.Key.BACKSLASH);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_BACK_SPACE,Identifier.Key.BACK);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_TAB,Identifier.Key.TAB);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_CAPS_LOCK,Identifier.Key.CAPITAL);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SEMICOLON,Identifier.Key.SEMICOLON);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_ENTER,Identifier.Key.RETURN);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SHIFT,Identifier.Key.LSHIFT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_COMMA,Identifier.Key.COMMA);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_PERIOD,Identifier.Key.PERIOD);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SLASH,Identifier.Key.SLASH);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_CONTROL,Identifier.Key.LCONTROL);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_ALT,Identifier.Key.LALT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SPACE,Identifier.Key.SPACE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_WINDOWS,Identifier.Key.SYSRQ);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_CONTEXT_MENU,Identifier.Key.APPS);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_HOME,Identifier.Key.HOME);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_INSERT,Identifier.Key.INSERT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_DELETE,Identifier.Key.DELETE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_END,Identifier.Key.END);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_PAGE_UP,Identifier.Key.PAGEUP);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_PAGE_DOWN,Identifier.Key.PAGEDOWN);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_ESCAPE,Identifier.Key.ESCAPE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F1,Identifier.Key.F1);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F2,Identifier.Key.F2);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F3,Identifier.Key.F3);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F4,Identifier.Key.F4);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F5,Identifier.Key.F5);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F6,Identifier.Key.F6);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F7,Identifier.Key.F7);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F8,Identifier.Key.F8);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F9,Identifier.Key.F9);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F10,Identifier.Key.F10);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F11,Identifier.Key.F11);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_F12,Identifier.Key.F12);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_PRINTSCREEN,Identifier.Key.KANA);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SCROLL_LOCK,Identifier.Key.SCROLL);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_PAUSE,Identifier.Key.PAUSE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_LEFT,Identifier.Key.LEFT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_RIGHT,Identifier.Key.RIGHT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_UP,Identifier.Key.UP);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_DOWN,Identifier.Key.DOWN);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUM_LOCK,Identifier.Key.NUMLOCK);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_ASTERISK,Identifier.Key.AX);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_DIVIDE,Identifier.Key.DIVIDE);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_SUBTRACT,Identifier.Key.SUBTRACT);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_ADD,Identifier.Key.ADD);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_DECIMAL,Identifier.Key.DECIMAL);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD0,Identifier.Key.NUMPAD0);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD1,Identifier.Key.NUMPAD1);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD2,Identifier.Key.NUMPAD2);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD3,Identifier.Key.NUMPAD3);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD4,Identifier.Key.NUMPAD4);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD5,Identifier.Key.NUMPAD5);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD6,Identifier.Key.NUMPAD6);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD7,Identifier.Key.NUMPAD7);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD8,Identifier.Key.NUMPAD8);
		Key.KEY_CONVERSION_MAP.put(KeyEvent.VK_NUMPAD9,Identifier.Key.NUMPAD9);
	}

	private static void ResetGame() {
		player=null;
	}

	private static void StartGame() {
		OBJ.add(player = new Player(p));
		OBJ.add(new Erinoah(p));
	}

	private static void ChooseBestRatio() {
		while (f.getWidth()*(SIZE_MULTIPLIER+1)<Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
			SIZE_MULTIPLIER++;
		}
		f.setSize(f.getWidth()*SIZE_MULTIPLIER,(int)((f.getWidth()*SIZE_MULTIPLIER)/1.77777777778d));
	}
}
