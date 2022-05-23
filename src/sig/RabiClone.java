package sig;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;

import sig.engine.Panel;

public class RabiClone {
	public static final String PROGRAM_NAME="Sig's Java Project Template";

	public static int UPCOUNT=0;
	public static Panel p;
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
		while (true) {
			if (KeyHeld(KeyEvent.VK_UP)) {
				System.out.println("Up Held: "+UPCOUNT++);
			}
		}
	}
	private static Boolean KeyHeld(int key) {
		return p.KEYS.getOrDefault(key,false);
	}
}
