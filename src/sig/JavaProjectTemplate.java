package sig;

import javax.swing.JFrame;
import sig.engine.Panel;

public class JavaProjectTemplate {
	public static final String PROGRAM_NAME="Sig's Java Project Template";
	public static void main(String[] args) {
		JFrame f = new JFrame(PROGRAM_NAME);
		Panel p = new Panel(f);
		
		p.init();
		
		f.add(p);
		f.setSize(1280,720);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		p.render();
	}
}
