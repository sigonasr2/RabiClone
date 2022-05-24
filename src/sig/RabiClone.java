package sig;

import javax.swing.JFrame;

import java.util.ArrayList;
import java.util.List;

import sig.engine.Panel;
import sig.objects.Player;
import sig.engine.Object;
import java.awt.Toolkit;

public class RabiClone{
	public static final String PROGRAM_NAME="Sig's Java Project Template";

	public static int UPCOUNT=0;
	public static Panel p;
	public static JFrame f;

	public static List<Object> OBJ = new ArrayList<Object>();
	boolean newSizeSet=false;

	public static int BASE_WIDTH=512;
	public static int BASE_HEIGHT=288;
	public static void main(String[] args) {

		RabiClone r = new RabiClone();
		f = new JFrame(PROGRAM_NAME);
		f.setResizable(false);
		f.setUndecorated(true);
		f.setSize(BASE_WIDTH,BASE_HEIGHT); //1024x576 (64x64)
		ChooseBestRatio();

		p = new Panel(f);
		
		p.init();
		
		f.add(p);
		f.addKeyListener(p);
		f.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-f.getWidth())/2), (int)((Toolkit.getDefaultToolkit().getScreenSize().getHeight()-f.getHeight())/2));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		p.render();

		for (int i=0;i<10;i++) {
			OBJ.add(new Player(p));
		}

		long lastGameTime = System.nanoTime();
		while (true) {
			long timePassed = System.nanoTime()-lastGameTime;
			lastGameTime=System.nanoTime();
			double updateMult = timePassed/1000000000d;

			for (int i=0;i<OBJ.size();i++) {
				OBJ.get(i).update(updateMult);
				if (OBJ.get(i).isMarkedForDeletion()) {
					OBJ.remove(i--);
				}
			}
		}
	}
	private static void ChooseBestRatio() {
		int multiplier=1;
		while (f.getWidth()*(multiplier+1)<Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
			multiplier++;
		}
		f.setSize(f.getWidth()*multiplier,(int)((f.getWidth()*multiplier)/1.77777777778d));
	}
}
