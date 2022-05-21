package sig;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import sig.engine.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RabiClone {
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
		BufferedImage nana;
		try {
			nana = ImageIO.read(new File("..","3x.png"));
			p.Draw_Nana(p.Get_Nana(nana));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
