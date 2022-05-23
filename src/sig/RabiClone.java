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

		long lastGameTime = System.nanoTime();
		while (true) {
			long timePassed = System.nanoTime()-lastGameTime;
			lastGameTime=System.nanoTime();
			double updateMult = timePassed/1000000000d;

			if (KeyHeld(KeyEvent.VK_UP)) {
				//System.out.println("Up Held: "+UPCOUNT+++" (+"+(float)(timePassed/1000000f)+"ms)");
				p.nanaY-=16*updateMult;
			}
			if (KeyHeld(KeyEvent.VK_DOWN)) {
				//System.out.println("Up Held: "+UPCOUNT+++" (+"+(float)(timePassed/1000000f)+"ms)");
				p.nanaY+=16*updateMult;
			}
			if (KeyHeld(KeyEvent.VK_RIGHT)) {
				//System.out.println("Up Held: "+UPCOUNT+++" (+"+(float)(timePassed/1000000f)+"ms)");
				p.nanaX+=16*updateMult;
				System.out.println(p.nanaX);
			}
			if (KeyHeld(KeyEvent.VK_LEFT)) {
				//System.out.println("Up Held: "+UPCOUNT+++" (+"+(float)(timePassed/1000000f)+"ms)");
				p.nanaX-=16*updateMult;
			}

		}
	}
	private static Boolean KeyHeld(int key) {
		return p.KEYS.getOrDefault(key,false);
	}
}
