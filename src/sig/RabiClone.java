package sig;

import javax.swing.JFrame;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sig.engine.Panel;
import sig.engine.Point;
import sig.engine.Transform;
import sig.engine.objects.AnimatedObject;
import sig.engine.objects.Object;
import sig.map.Map;
import sig.map.Maps;
import sig.map.Tile;
import sig.objects.ConfigureControls;
import sig.objects.LevelRenderer;
import sig.objects.Player;
import sig.objects.actor.RenderedObject;
import sig.engine.Key;
import sig.engine.KeyBind;
import sig.engine.PaletteColor;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.RenderingHints;

public class RabiClone {
	public static final String PROGRAM_NAME = "RabiClone";
	public static final int UPDATE_LOOP_FRAMERATE = 244;
	public static final long UPDATE_LOOP_NANOTIME = (long)((1d/UPDATE_LOOP_FRAMERATE)*1000000000l);
	public static final double UPDATE_MULT = 1d / UPDATE_LOOP_FRAMERATE;
	public static String BLANK = "\0";

	public static int UPCOUNT = 0;
	public static Panel p;
	public static JFrame f;

	public static List<Object> OBJ = new ArrayList<Object>();
	public static List<AnimatedObject> FRIENDLY_OBJ = new ArrayList<AnimatedObject>();
	public static List<AnimatedObject> ENEMY_OBJ = new ArrayList<AnimatedObject>();
	public static boolean COLLISION[] = new boolean[(Tile.TILE_WIDTH*Map.MAP_WIDTH)*(Tile.TILE_HEIGHT*Map.MAP_HEIGHT)];

	public static int BASE_WIDTH = 512;
	public static int BASE_HEIGHT = 288;
	public static int EVENT_BOUNDARY_RANGE = 8;
	public static int SIZE_MULTIPLIER = 1;
	public static Point MOUSE_POS;

	public static boolean PLAYER_COLLISION[] = new boolean[RabiClone.BASE_WIDTH*RabiClone.BASE_HEIGHT];
	public static boolean ENEMY_COLLISION[] = new boolean[RabiClone.BASE_WIDTH*RabiClone.BASE_HEIGHT];
	public static boolean PLAYER_WEAPON_COLLISION[] = new boolean[RabiClone.BASE_WIDTH*RabiClone.BASE_HEIGHT];
	public static boolean ENEMY_WEAPON_COLLISION[] = new boolean[RabiClone.BASE_WIDTH*RabiClone.BASE_HEIGHT];

	public static PaletteColor BACKGROUND_COLOR = PaletteColor.DARK_ORCHID;

	public static LevelRenderer level_renderer;
	public static ConfigureControls control_settings_menu;
	public static Player player;

	public static Maps CURRENT_MAP;
	public static Controller[] CONTROLLERS = new Controller[] {};

	public static long lastControllerScan = System.currentTimeMillis();

	static long lastUpdate = System.nanoTime();
	final static long TARGET_FRAMETIME = 8333333l;
	static long lastReportedTime = System.currentTimeMillis();
	public static long TIME = 0;
	public static long scaleTime;

	public static RenderingHints RENDERHINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);

	public static void main(String[] args) {
		System.setProperty("sun.java2d.transaccel", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
		System.setProperty("sun.java2d.xrender", "True");

		RENDERHINTS.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
		RENDERHINTS.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
		RENDERHINTS.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		RENDERHINTS.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);

		Key.InitializeKeyConversionMap();

		f = new JFrame(PROGRAM_NAME);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setSize(BASE_WIDTH, BASE_HEIGHT); // 1024x576 (64x64)
		ChooseBestRatio();
		
		Map.LoadMap(Maps.WORLD1);

		p = new Panel(f);

		MOUSE_POS = p.mousePos;

		p.init();

		f.add(p);
		f.addKeyListener(p);
		f.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - f.getWidth()) / 2),
				(int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - f.getHeight()) / 2));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.createBufferStrategy(2);

		OBJ.add(level_renderer = new LevelRenderer(p));
		StartGame();

		p.render();

		long lastGameTime = System.nanoTime();
		long dt = 0;

		CONTROLLERS = ControllerEnvironment.getDefaultEnvironment().getControllers();
		
		while (true) {
			dt += System.nanoTime() - lastGameTime;
			lastGameTime = System.nanoTime();

			while (dt >= UPDATE_LOOP_NANOTIME) {
				handleGameControllers();

				KeyBind.poll();

				if (Key.isKeyHeld(KeyEvent.VK_F5) && System.currentTimeMillis() - lastControllerScan > 5000) {
					CONTROLLERS = ControllerEnvironment.getDefaultEnvironment().rescanControllers();
					System.out.println(Arrays.toString(CONTROLLERS));
					lastControllerScan = System.currentTimeMillis();
				}

				FRIENDLY_OBJ.clear();
				ENEMY_OBJ.clear();
				for (int i = 0; i < OBJ.size(); i++) {
					if (OBJ.get(i) instanceof RenderedObject) {
						RenderedObject r = (RenderedObject)OBJ.get(i);
						if (r.isFriendlyObject()) {
							FRIENDLY_OBJ.add((AnimatedObject)r);
						} else {
							ENEMY_OBJ.add((AnimatedObject)r);
						}
					}
					OBJ.get(i).update(UPDATE_MULT);
					if (OBJ.get(i).isMarkedForDeletion()) {
						OBJ.remove(i--);
					}
				}
				for (int i = 0; i < FRIENDLY_OBJ.size(); i++) {
					AnimatedObject f = FRIENDLY_OBJ.get(i);
					for (int j=0;j<ENEMY_OBJ.size();j++) {
						AnimatedObject e = ENEMY_OBJ.get(j);
						if (detectCollision(e,f)) {
							e.collisionEvent(f);
							f.collisionEvent(e);
						}
					}
				}
				dt -= UPDATE_LOOP_NANOTIME;
				TIME += UPDATE_LOOP_NANOTIME;
				// System.out.println(TIME);
			}
			gameUpdateLoopStabilizer(dt); //This is hackish. Removing this slows down the game by about 30%. The timer runs slower. ??? 
		}
	}

	private static boolean detectCollision(AnimatedObject e, AnimatedObject f) {
		double x1=e.getX()-e.getAnimatedSpr().getWidth()/2,y1=e.getY()-e.getAnimatedSpr().getHeight()/2,x2=e.getX()+e.getAnimatedSpr().getWidth()/2,y2=e.getY()+e.getAnimatedSpr().getHeight()/2;
		double x3=f.getX()-f.getAnimatedSpr().getWidth()/2,y3=f.getY()-f.getAnimatedSpr().getHeight()/2,x4=f.getX()+f.getAnimatedSpr().getWidth()/2,y4=f.getY()+f.getAnimatedSpr().getHeight()/2;

		if (x3<x2&&y3<y2&&x4>x1&&y4>y1) //Rectangular collision detected, now check on a pixel level. 
		{
			int sx1,sy1,w,h;
			sx1 = (int)Math.max(x1,x3);
			sy1 = (int)Math.max(y1,y3);
			w = (int)Math.min(x2,x4)-sx1-1;
			h = (int)Math.min(y2,y4)-sy1-1;
			int offsetX_r1=(int)(x1>x3?0:x3-x1);
			int offsetY_r1=(int)(y1>y3?0:y3-y1);
			int offsetX_r2=(int)(x3>x1?0:x1-x3);
			int offsetY_r2=(int)(y3>y1?0:y1-y3);

			byte[] arr1=e.getAnimatedSpr().getBi_array();
			int xFrame1 = (int)e.getCurrentFrame()%(e.getAnimatedSpr().getCanvasWidth()/e.getAnimatedSpr().getWidth());
			int yFrame1 = ((int)e.getCurrentFrame()/(e.getAnimatedSpr().getCanvasWidth()/e.getAnimatedSpr().getWidth()))%(e.getAnimatedSpr().getCanvasHeight()/e.getAnimatedSpr().getHeight());
			byte[] arr2=f.getAnimatedSpr().getBi_array();
			int xFrame2 = (int)f.getCurrentFrame()%(f.getAnimatedSpr().getCanvasWidth()/f.getAnimatedSpr().getWidth());
			int yFrame2 = ((int)f.getCurrentFrame()/(f.getAnimatedSpr().getCanvasWidth()/f.getAnimatedSpr().getWidth()))%(f.getAnimatedSpr().getCanvasHeight()/f.getAnimatedSpr().getHeight());


			if (w>0&&h>0) {
				for (int yy1=offsetY_r1;yy1<offsetY_r1+h;yy1++) {
					for (int xx1=offsetX_r1;xx1<offsetX_r1+w;xx1++) {
						if (arr1[((e.getSpriteTransform()==Transform.VERTICAL||e.getSpriteTransform()==Transform.HORIZ_VERTIC?e.getAnimatedSpr().getHeight()-yy1:yy1)+yFrame1*e.getAnimatedSpr().getHeight())*e.getAnimatedSpr().getCanvasWidth()+(e.getSpriteTransform()==Transform.HORIZONTAL||e.getSpriteTransform()==Transform.HORIZ_VERTIC?e.getAnimatedSpr().getWidth()-xx1:xx1)+xFrame1*e.getAnimatedSpr().getWidth()]!=(byte)32) {
							for (int yy2=offsetY_r2;yy2<offsetY_r2+h;yy2++) {
								for (int xx2=offsetX_r2;xx2<offsetX_r2+w;xx2++) {
									if (arr2[((f.getSpriteTransform()==Transform.VERTICAL||f.getSpriteTransform()==Transform.HORIZ_VERTIC?f.getAnimatedSpr().getHeight()-yy2:yy2)+yFrame2*f.getAnimatedSpr().getHeight())*f.getAnimatedSpr().getCanvasWidth()+((f.getSpriteTransform()==Transform.HORIZONTAL||f.getSpriteTransform()==Transform.HORIZ_VERTIC?f.getAnimatedSpr().getWidth()-xx2:xx2)+xFrame2*f.getAnimatedSpr().getWidth())]!=(byte)32) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private static void gameUpdateLoopStabilizer(long dt) {
		if (dt < UPDATE_LOOP_NANOTIME) {
			lastReportedTime = System.currentTimeMillis();
		} else {
			if (System.currentTimeMillis() - lastReportedTime > 5000) {
				System.out.println("WARNING! Game is lagging behind! Frames Behind: " + (dt / UPDATE_LOOP_NANOTIME));
				lastReportedTime = System.currentTimeMillis();
			}
		}
		try {
			Thread.sleep(4);
		} catch (InterruptedException e) {
			e.printStackTrace();
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

	public static void ResetGame() {
		player = null;
		level_renderer = null;
		control_settings_menu = null;
		// System.gc();
	}

	public static void StartGame() {
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
