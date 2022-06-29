package sig.engine;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.IndexColorModel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.event.KeyListener;
import java.awt.Graphics2D;

import sig.DrawLoop;
import sig.RabiClone;
import sig.engine.objects.Object;

public class Panel extends JPanel implements Runnable,KeyListener {
	JFrame window;
    public byte pixel[];
    final int CIRCLE_PRECISION=32;
	final int OUTLINE_COL=Color.BRIGHT_WHITE.getColor();
    private Thread thread;
    private Image imageBuffer;   
    private MemoryImageSource mImageProducer;   
    private ColorModel cm;   
    int scanLine=0;
    int nextScanLine=0;
    double x_offset=0;
    double y_offset=0;
    int frameCount=0;
	long lastSecond=0;
	boolean resizing=false;
	long lastUpdate=System.nanoTime();
	final long TARGET_FRAMETIME = 8333333l;
	public double nanaX = 0;
	public double nanaY = 0;
	public Point mousePos=new Point(0,0);
	public int button = 0;
	public HashMap<Integer,Boolean> MOUSE = new HashMap<>();
	public static byte[] generalPalette = new byte[]{
		(byte)0x5b,(byte)0xa6,(byte)0x75,
		(byte)0x6b,(byte)0xc9,(byte)0x6c,
		(byte)0xab,(byte)0xdd,(byte)0x64,
		(byte)0xfc,(byte)0xef,(byte)0x8d,
		(byte)0xff,(byte)0xb8,(byte)0x79,
		(byte)0xea,(byte)0x62,(byte)0x62,
		(byte)0xcc,(byte)0x42,(byte)0x5e,
		(byte)0xa3,(byte)0x28,(byte)0x58,
		(byte)0x75,(byte)0x17,(byte)0x56,
		(byte)0x39,(byte)0x09,(byte)0x47,
		(byte)0x61,(byte)0x18,(byte)0x51,
		(byte)0x87,(byte)0x35,(byte)0x55,
		(byte)0xa6,(byte)0x55,(byte)0x5f,
		(byte)0xc9,(byte)0x73,(byte)0x73,
		(byte)0xf2,(byte)0xae,(byte)0x99,
		(byte)0xff,(byte)0xc3,(byte)0xf2,
		(byte)0xee,(byte)0x8f,(byte)0xcb,
		(byte)0xd4,(byte)0x6e,(byte)0xb3,
		(byte)0x87,(byte)0x3e,(byte)0x84,
		(byte)0x1f,(byte)0x10,(byte)0x2a,
		(byte)0x4a,(byte)0x30,(byte)0x52,
		(byte)0x7b,(byte)0x54,(byte)0x80,
		(byte)0xa6,(byte)0x85,(byte)0x9f,
		(byte)0xd9,(byte)0xbd,(byte)0xc8,
		(byte)0xff,(byte)0xff,(byte)0xff,
		(byte)0xae,(byte)0xe2,(byte)0xff,
		(byte)0x8d,(byte)0xb7,(byte)0xff,
		(byte)0x6d,(byte)0x80,(byte)0xfa,
		(byte)0x84,(byte)0x65,(byte)0xec,
		(byte)0x83,(byte)0x4d,(byte)0xc4,
		(byte)0x7d,(byte)0x2d,(byte)0xa0,
		(byte)0x4e,(byte)0x18,(byte)0x7c,
		(byte)255,(byte)  0,(byte)255,
		(byte) 87,(byte)157,(byte)147,
		(byte)101,(byte)188,(byte)141,
		(byte)158,(byte)206,(byte)137,
		(byte)232,(byte)222,(byte)162,
		(byte)234,(byte)173,(byte)149,
		(byte)215,(byte) 99,(byte)136,
		(byte)188,(byte) 74,(byte)134,
		(byte)151,(byte) 58,(byte)131,
		(byte)109,(byte) 50,(byte)130,
		(byte) 58,(byte) 45,(byte)123,
		(byte) 92,(byte) 50,(byte)127,
		(byte)125,(byte) 65,(byte)129,
		(byte)153,(byte) 88,(byte)134,
		(byte)185,(byte)113,(byte)146,
		(byte)222,(byte)164,(byte)171,
		(byte)234,(byte)182,(byte)240,
		(byte)219,(byte)137,(byte)208,
		(byte)195,(byte)109,(byte)190,
		(byte)125,(byte) 71,(byte)156,
		(byte) 39,(byte) 47,(byte)113,
		(byte) 72,(byte) 62,(byte)128,
		(byte)115,(byte) 88,(byte)154,
		(byte)153,(byte)128,(byte)175,
		(byte)200,(byte)177,(byte)206,
		(byte)234,(byte)236,(byte)251,
		(byte)161,(byte)210,(byte)251,
		(byte)131,(byte)172,(byte)251,
		(byte)102,(byte)124,(byte)246,
		(byte)123,(byte)101,(byte)235,
		(byte)122,(byte) 82,(byte)203,
		(byte)116,(byte) 60,(byte)176,
		(byte) 75,(byte) 50,(byte)151,
	};

    public Panel(JFrame f) {
        super(true);
		this.window=f;
        thread = new Thread(this, "MyPanel Thread");

		this.addMouseListener(new MouseInputListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		
			@Override
			public void mousePressed(MouseEvent e) {
				MOUSE.put(e.getButton(),true);
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);
				//System.out.println("Mouse List: "+MOUSE);

				for(int i=0; i<RabiClone.OBJ.size();i++){
					RabiClone.OBJ.get(i).MousePressed(e);
				}
			}
		
			@Override
			public void mouseReleased(MouseEvent e) {
				MOUSE.put(e.getButton(),false);
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);

				for(int i=0; i<RabiClone.OBJ.size();i++){
					RabiClone.OBJ.get(i).MouseReleased(e);
				}
			}
		
			@Override
			public void mouseEntered(MouseEvent e) {
			}
		
			@Override
			public void mouseExited(MouseEvent e) {
			}
		
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});
		this.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);
			}
		});
		this.addMouseWheelListener(new MouseWheelListener(){
			//-1 is UP, 1 is DOWN
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				for(int i=0; i<RabiClone.OBJ.size();i++){
					Object current_obj = RabiClone.OBJ.get(i);
					current_obj.MouseScrolled(MouseScrollValue.getValue(e.getWheelRotation()));
					//System.out.println(Panel.col);
				}
			}
		});
    }

    /**
     * Get Best Color model available for current screen.
     * @return color model
     */
    protected static ColorModel getCustomPalette(){   
        return new IndexColorModel(8,65,generalPalette,0,false,32);
    }

    /**
     * Call it after been visible and after resizes.
     */
    public void init(){        
        cm = getCustomPalette();
        int screenSize = RabiClone.BASE_WIDTH*RabiClone.BASE_HEIGHT;
        if(pixel == null || pixel.length < screenSize){
            pixel = new byte[screenSize];
        }        
        if(thread.isInterrupted() || !thread.isAlive()){
            thread.start();
        }
        mImageProducer =  new MemoryImageSource(RabiClone.BASE_WIDTH, RabiClone.BASE_HEIGHT, cm, pixel,0, RabiClone.BASE_WIDTH);
        mImageProducer.setAnimated(true);
        mImageProducer.setFullBufferUpdates(true);  
        imageBuffer = Toolkit.getDefaultToolkit().createImage(mImageProducer);        
    }

    @Override
    public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		// perform draws on pixels
		long startTime = System.currentTimeMillis();
		g.drawImage(this.imageBuffer,0,0,getWidth(),getHeight(),0,0,RabiClone.BASE_WIDTH,RabiClone.BASE_HEIGHT,this);
		RabiClone.scaleTime=System.currentTimeMillis()-startTime;
    }
    
    /**
     * Overrides ImageObserver.imageUpdate.
     * Always return true, assuming that imageBuffer is ready to go when called
     */
    @Override
    public boolean imageUpdate(Image image, int a, int b, int c, int d, int e) {
        return true;
    }
    /**
    * Do your draws in here !!
    * pixel is your canvas!
    */

	
    public /* abstract */ void render(){
        //a=h/w
		DrawLoop.drawGame(this);
    }
    
    public void FillCircle(byte[] p,byte col,double center_x,double center_y,double r) {
    	int counter=0;
    	Point[] points = new Point[CIRCLE_PRECISION];
    	for (double theta=0;theta<Math.PI*2;theta+=((Math.PI*2)/CIRCLE_PRECISION)) {
    		//System.out.println("Loop "+counter+++". Theta:"+theta);
    		//System.out.println("X:"+(Math.sin(theta)*r+center_x)+" Y:"+(Math.cos(theta)*r+center_y));
    		points[counter++]=new Point((int)(Math.round(Math.sin(theta)*r+center_x)),(int)(Math.round(Math.cos(theta)*r+center_y)));
    	}
        FillPolygon(p,col,0,0,points);
    }

    
    public void FillOval(byte[] p,byte col,double center_x,double center_y,double w,double h) {
    	int counter=0;
    	Point[] points = new Point[CIRCLE_PRECISION];
    	double r = Math.max(w,h);
    	double ratio = Math.min(w,h)/r;
    	for (double theta=0;theta<Math.PI*2;theta+=((Math.PI*2)/CIRCLE_PRECISION)) {
    		//System.out.println("Loop "+counter+++". Theta:"+theta);
    		//System.out.println("X:"+(Math.sin(theta)*r+center_x)+" Y:"+(Math.cos(theta)*r+center_y));
    		Point newP = new Point((int)(Math.round(Math.sin(theta)*r)),(int)(Math.round(Math.cos(theta)*r)));
    		if (w<h) {
    			newP.x=(int)Math.round(newP.x*ratio);
    		} else {
    			newP.y=(int)Math.round(newP.y*ratio);
    		}
    		newP.x+=center_x;
    		newP.y+=center_y;
    		points[counter++]=newP;
    	}
        FillPolygon(p,col,0,0,points);
    }
    
    public void FillPolygon(byte[] p,byte col,double x_offset,double y_offset,Point...points) {
    	Edge[] edges = new Edge[points.length];
    	List<Edge> edges_sorted = new ArrayList<Edge>();
    	for (int i=0;i<points.length;i++) {
    		edges[i] = new Edge(points[i],points[(i+1)%points.length]);
    		if (!Double.isInfinite(edges[i].inverse_slope)) {
	    		if (edges_sorted.size()==0) {
	    			edges_sorted.add(edges[i]);
	    		} else {
	    			boolean inserted=false;
	    			for (int j=0;j<edges_sorted.size();j++) {
	    				Edge e2 = edges_sorted.get(j);
	    				if (e2.min_y>=edges[i].min_y) {
	    					edges_sorted.add(j,edges[i]);
	    					inserted=true;
	    					break;
	    				}
	    			}
	    			if (!inserted) {
	    				edges_sorted.add(edges[i]);
	    			}
	    		}
    		}
    	}
    	//System.out.println(edges_sorted);
    	List<Edge> active_edges = new ArrayList<Edge>();
    	scanLine = edges_sorted.get(0).min_y-1;
    	nextScanLine = scanLine+1;
    	do {
    		for (int i=0;i<active_edges.size();i+=2) {
    			Edge e1 = active_edges.get(i);
    			Edge e2 = active_edges.get(i+1);
				//System.out.println("Drawing from "+((int)Math.round(e1.x_of_min_y))+" to "+e2.x_of_min_y+" on line "+scanLine);
    			for (int x=(int)Math.round(e1.x_of_min_y);x<=e2.x_of_min_y;x++) {
    				int index = (scanLine+(int)y_offset)*getWidth()+x+(int)x_offset;
    				if (index<p.length&&index>=0) {
						Draw(p,index,col);
					}
    			}
    		}
    		List<Edge> new_active_edges = new ArrayList<Edge>();
    		for (int i=0;i<active_edges.size();i++) {
    			Edge e = active_edges.get(i);
    			if (e.max_y==scanLine+1) {
    				active_edges.remove(i--);
    			} else {
    				e.x_of_min_y+=e.inverse_slope;
    			}
    		}
    		scanLine++;
    		for (int i=0;i<active_edges.size();i++) {
    			Edge e = active_edges.get(i);
    			boolean inserted=false;
    			for (int j=0;j<new_active_edges.size();j++) {
    				Edge e2 = new_active_edges.get(j);
    				if (e2.x_of_min_y>e.x_of_min_y) {
    					new_active_edges.add(j,e);
    					inserted=true;
    					break;
    				}
    			}
    			if (!inserted) {
					new_active_edges.add(e);
    			}
    		}
    		active_edges=new_active_edges;
    		GetNextScanLineEdges(edges_sorted, active_edges);
    	}
    	while (active_edges.size()>0);
    }

	private void GetNextScanLineEdges(List<Edge> edges_sorted, List<Edge> active_edges) {
		if (scanLine==nextScanLine) {
	    	for (int i=0;i<edges_sorted.size();i++) {
	    		Edge e = edges_sorted.get(i);
	    		if (e.min_y==scanLine) {
	    			e = edges_sorted.remove(i--);
	    			boolean inserted=false;
	    			for (int j=0;j<active_edges.size();j++) {
	    				if (e.x_of_min_y<active_edges.get(j).x_of_min_y) {
	    					active_edges.add(j,e);
	    					inserted=true;
	    					break;
	    				}
	    			}
	    			if (!inserted) {
	    				active_edges.add(e);
	    			}
	    			
	    		} else
	    		if (e.min_y>scanLine) {
	    			nextScanLine=e.min_y;
	    			break;
	    		}
	    	}
    	}
	}

	public void Draw(byte[] canvas,int index, byte col) {
		canvas[index]=col;
	}

	@Override
	public void run() {
		while (true) {
            // request a JPanel re-drawing
            //repaint();      
			render();
			mImageProducer.newPixels();
			if (RabiClone.f!=null&&RabiClone.f.getBufferStrategy()!=null) {
				do {
					do {
						if (RabiClone.f.getBufferStrategy()!=null) {
							Graphics2D g2 = (Graphics2D)RabiClone.f.getBufferStrategy().getDrawGraphics();
							g2.setRenderingHints(RabiClone.RENDERHINTS);
							if (g2!=null) {
								try {
									paintComponent(g2);
								} finally {
									g2.dispose();
								}
							}
						}
					} while (RabiClone.f.getBufferStrategy().contentsRestored());
					RabiClone.f.getBufferStrategy().show();
					Toolkit.getDefaultToolkit().sync();
				} while (RabiClone.f.getBufferStrategy().contentsLost());
			}
			updateFPSCounter();
            //System.out.println("Repaint "+frameCount++);
			waitForNextFrame();
        }
	}

	private void waitForNextFrame() {
		long newTime = System.nanoTime();
		if (newTime-lastUpdate<TARGET_FRAMETIME) {
			long timeRemaining=TARGET_FRAMETIME-(newTime-lastUpdate);
			long millis = timeRemaining/1000000l;
			int nanos = (int)(timeRemaining-millis*1000000l);
			//System.out.println(timeRemaining+"/"+millis+" Nanos:"+nanos);
			try {
				Thread.sleep(millis,nanos);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lastUpdate=newTime;
	}

	private void updateFPSCounter() {
		if (window!=null&&System.currentTimeMillis()-lastSecond>=1000) {
			window.setTitle(RabiClone.PROGRAM_NAME+" - FPS: "+(frameCount));
			frameCount=0;
			lastSecond=System.currentTimeMillis();
		}
		frameCount++;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!Key.isKeyHeld(e.getKeyCode())) {
			Key.setKeyHeld(e.getKeyCode(), true);
		}
		if (RabiClone.control_settings_menu!=null) {
			RabiClone.control_settings_menu.rawKeyPressed(e.getKeyCode());
		}
		//System.out.println("Key List: "+KEYS);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key.setKeyHeld(e.getKeyCode(), false);
		//System.out.println("Key List: "+KEYS);
	}
}