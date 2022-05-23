package sig;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

import sig.engine.Panel;
import sig.engine.Object;

public class RabiClone {
	public static final String PROGRAM_NAME="Sig's Java Project Template";

	public static int UPCOUNT=0;
	public static Panel p;

	public static List<Object> OBJ = new ArrayList<Object>();
	public static void main(String[] args) {

		JFrame f = new JFrame(PROGRAM_NAME);
		p = new Panel(f);
		
		p.init();
		
		f.add(p);
		f.addComponentListener(p);
		f.addKeyListener(p);
		f.setSize(1280,720);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		p.render();

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
}
