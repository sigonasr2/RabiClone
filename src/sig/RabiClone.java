package sig;

import javax.swing.JFrame;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sig.engine.Panel;
import sig.engine.Point;
import sig.map.Maps;
import sig.objects.ConfigureControls;
import sig.objects.EditorRenderer;
import sig.objects.LevelRenderer;
import sig.objects.Player;
import sig.engine.Key;
import sig.engine.KeyBind;
import sig.engine.Object;
import sig.engine.PaletteColor;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class RabiClone {
	public static final String PROGRAM_NAME = "RabiClone";
	public static final String BLANK = "\0";

	public static int UPCOUNT = 0;
	public static Panel p;
	public static JFrame f;

	public static List<Object> OBJ = new ArrayList<Object>();

	public static int BASE_WIDTH = 512;
	public static int BASE_HEIGHT = 288;
	public static int SIZE_MULTIPLIER = 1;
	public static Point MOUSE_POS;

	public static PaletteColor BACKGROUND_COLOR = PaletteColor.DARK_ORCHID;

	public static LevelRenderer level_renderer;
	public static ConfigureControls control_settings_menu;
	public static Player player;

	public static Maps CURRENT_MAP = Maps.WORLD1;
	public static Controller[] CONTROLLERS = new Controller[] {};

	public static long lastControllerScan = System.currentTimeMillis();

	static long lastUpdate = System.nanoTime();
	final static long TARGET_FRAMETIME = 8333333l;
	static long lastReportedTime = System.currentTimeMillis();
	public static long TIME = 0;

	public static void main(String[] args) {

		Key.InitializeKeyConversionMap();

		f = new JFrame(PROGRAM_NAME);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setSize(BASE_WIDTH, BASE_HEIGHT); // 1024x576 (64x64)
		ChooseBestRatio();

		p = new Panel(f);

		MOUSE_POS = p.mousePos;

		p.init();

		f.add(p);
		f.addKeyListener(p);
		f.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - f.getWidth()) / 2),
				(int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - f.getHeight()) / 2));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		OBJ.add(level_renderer = new LevelRenderer(p));
		StartGame();

		p.render();

		long lastGameTime = System.nanoTime();
		long dt = 0;

		CONTROLLERS = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		while (true) {
			dt += System.nanoTime() - lastGameTime;
			lastGameTime = System.nanoTime();

			while (dt >= (1 / 244d) * 1000000000l) {
				final double updateMult = 1 / 244d;
				handleGameControllers();

				KeyBind.poll();

				if (Key.isKeyHeld(KeyEvent.VK_F1)) {
					if (level_renderer instanceof EditorRenderer) {
						OBJ.remove(level_renderer);
						OBJ.add(level_renderer = new LevelRenderer(p));
						StartGame();
					}
				}
				if (Key.isKeyHeld(KeyEvent.VK_F2)) {
					if (!(level_renderer instanceof EditorRenderer)) {
						OBJ.clear();
						ResetGame();
						OBJ.add(level_renderer = new EditorRenderer(p));
					}
				}
				if (Key.isKeyHeld(KeyEvent.VK_F3)) {
					OBJ.clear();
					ResetGame();
					OBJ.add(control_settings_menu = new ConfigureControls(p));
				}
				if (Key.isKeyHeld(KeyEvent.VK_F5) && System.currentTimeMillis() - lastControllerScan > 5000) {
					CONTROLLERS = ControllerEnvironment.getDefaultEnvironment().rescanControllers();
					System.out.println(Arrays.toString(CONTROLLERS));
					lastControllerScan = System.currentTimeMillis();
				}

				for (int i = 0; i < OBJ.size(); i++) {
					OBJ.get(i).update(updateMult);
					if (OBJ.get(i).isMarkedForDeletion()) {
						OBJ.remove(i--);
					}
				}
				dt -= (1 / 244d) * 1000000000l;
				TIME += (1 / 244d) * 1000000000l;
				// System.out.println(TIME);
			}
			if (dt < (1 / 244d) * 1000000000l) {
				lastReportedTime = System.currentTimeMillis();
			} else {
				if (System.currentTimeMillis() - lastReportedTime > 5000) {
					System.out.println(
							"WARNING! Game is lagging behind! Frames Behind: " + (dt / ((1 / 244d) * 1000000000l)));
					lastReportedTime = System.currentTimeMillis();
				}
			}
			System.out.print(BLANK); //This is hackish. Removing this slows down the game by about 30%. The timer runs slower. ???
		}
	}

	private static void handleGameControllers() {
		for (int i = 0; i < CONTROLLERS.length; i++) {
			if (CONTROLLERS[i].getType() == Controller.Type.KEYBOARD
					|| CONTROLLERS[i].getType() == Controller.Type.MOUSE) {
				continue;
			}
			if (!CONTROLLERS[i].poll()) {
				Controller[] newArr = new Controller[CONTROLLERS.length - 1];
				for (int j = 0; j < CONTROLLERS.length; j++) {
					if (j != i) {
						newArr[(j > i ? j - 1 : j)] = CONTROLLERS[i];
					}
				}
				CONTROLLERS = newArr;
			}
		}
	}

	private static void ResetGame() {
		player = null;
		level_renderer = null;
		control_settings_menu = null;
		// System.gc();
	}

	private static void StartGame() {
		// System.gc();
		OBJ.add(player = new Player(p));
		//OBJ.add(new Erinoah(p));
	}

	private static void ChooseBestRatio() {
		while (f.getWidth() * (SIZE_MULTIPLIER + 1) < Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
			SIZE_MULTIPLIER++;
		}
		f.setSize(f.getWidth() * SIZE_MULTIPLIER, (int) ((f.getWidth() * SIZE_MULTIPLIER) / 1.77777777778d));
	}
}
