package sig;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.List;

import sig.engine.Panel;
import sig.engine.Point;
import sig.map.Maps;
import sig.objects.EditorRenderer;
import sig.objects.Erinoah;
import sig.objects.LevelRenderer;
import sig.objects.Player;
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
	public static void main(String[] args) {
		f = new JFrame(PROGRAM_NAME);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setSize(BASE_WIDTH,BASE_HEIGHT); //1024x576 (64x64)
		ChooseBestRatio();

		p = new Panel(f); //Comment

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

			if (p.KEYS.getOrDefault(KeyEvent.VK_F1,false)) {
				if (level_renderer instanceof EditorRenderer) {
					OBJ.remove(level_renderer);
					OBJ.add(level_renderer=new LevelRenderer(p));
					StartGame();
				}
			}
			if (p.KEYS.getOrDefault(KeyEvent.VK_F2,false)) {
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
