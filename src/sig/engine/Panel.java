package sig.engine;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.Arrays;
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

import sig.DrawLoop;
import sig.RabiClone;
import sig.map.Tile;

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
	public Point highlightedSquare = new Point(0,0);
	public HashMap<Integer,Boolean> KEYS = new HashMap<>();
	public HashMap<Integer,Boolean> MOUSE = new HashMap<>();

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
			}
		
			@Override
			public void mouseReleased(MouseEvent e) {
				MOUSE.put(e.getButton(),false);
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);
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
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos.set(e.getX()/RabiClone.SIZE_MULTIPLIER,e.getY()/RabiClone.SIZE_MULTIPLIER);
				UpdateHighlightedSquare();
			}
		});
		this.addMouseWheelListener(new MouseWheelListener(){
			//-1 is UP, 1 is DOWN
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				for(int i=0; i<RabiClone.OBJ.size();i++){
					Object current_obj = RabiClone.OBJ.get(i);
					current_obj.MouseScrolled(MouseScrollValue.getValue(e.getWheelRotation()));
				}
			}
		});
    }

	private static void UpdateHighlightedSquare() {
		RabiClone.p.highlightedSquare.setX((int)((RabiClone.level_renderer.getX()+RabiClone.MOUSE_POS.getX())/Tile.TILE_WIDTH));
		RabiClone.p.highlightedSquare.setY((int)((RabiClone.level_renderer.getY()+RabiClone.MOUSE_POS.getY())/Tile.TILE_HEIGHT));
		//System.out.println(RabiClone.p.highlightedSquare);
	}

    /**
     * Get Best Color model available for current screen.
     * @return color model
     */
    protected static ColorModel getCustomPalette(){   
		char[] generalPalette = new char[]{
			0x5b,0xa6,0x75,0xff,
			0x6b,0xc9,0x6c,0xff,
			0xab,0xdd,0x64,0xff,
			0xfc,0xef,0x8d,0xff,
			0xff,0xb8,0x79,0xff,
			0xea,0x62,0x62,0xff,
			0xcc,0x42,0x5e,0xff,
			0xa3,0x28,0x58,0xff,
			0x75,0x17,0x56,0xff,
			0x39,0x09,0x47,0xff,
			0x61,0x18,0x51,0xff,
			0x87,0x35,0x55,0xff,
			0xa6,0x55,0x5f,0xff,
			0xc9,0x73,0x73,0xff,
			0xf2,0xae,0x99,0xff,
			0xff,0xc3,0xf2,0xff,
			0xee,0x8f,0xcb,0xff,
			0xd4,0x6e,0xb3,0xff,
			0x87,0x3e,0x84,0xff,
			0x1f,0x10,0x2a,0xff,
			0x4a,0x30,0x52,0xff,
			0x7b,0x54,0x80,0xff,
			0xa6,0x85,0x9f,0xff,
			0xd9,0xbd,0xc8,0xff,
			0xff,0xff,0xff,0xff,
			0xae,0xe2,0xff,0xff,
			0x8d,0xb7,0xff,0xff,
			0x6d,0x80,0xfa,0xff,
			0x84,0x65,0xec,0xff,
			0x83,0x4d,0xc4,0xff,
			0x7d,0x2d,0xa0,0xff,
			0x4e,0x18,0x7c,0xff,
		};
		byte[] finalPalette = new byte[32*3*8];
		for (int i=0;i<8;i++) {
			for (int j=0;j<generalPalette.length;j+=4) {
				finalPalette[(32*3*i)+j+0]=(byte)generalPalette[j+0];
				finalPalette[(32*3*i)+j+1]=(byte)generalPalette[j+1];
				finalPalette[(32*3*i)+j+2]=(byte)generalPalette[j+2];
				finalPalette[(32*3*i)+j+3]=(byte)(generalPalette[j+3]-(i*(256/8)));
			}
		}
		
        IndexColorModel model = new IndexColorModel(8,32,finalPalette,0,true);
        return model;
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
        super.paintComponent(g);
        // perform draws on pixels
        render();
        // ask ImageProducer to update image
        mImageProducer.newPixels();  
        // draw it on panel          
		g.drawImage(this.imageBuffer,0,0,getWidth(),getHeight(),0,0,RabiClone.BASE_WIDTH,RabiClone.BASE_HEIGHT,this);
		updateFPSCounter();
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
    
    public void FillRect(int[] p,Color col,double x,double y,double w,double h) {
    	for (int xx=0;xx<w;xx++) {
        	for (int yy=0;yy<h;yy++) {
        		int index = ((int)y+yy)*getWidth()+(int)x+xx;
				Draw(p,index,col.getColor());
        	}	
    	}
    }
    
    public void FillCircle(int[] p,Color col,double center_x,double center_y,double r) {
    	int counter=0;
    	Point[] points = new Point[CIRCLE_PRECISION];
    	for (double theta=0;theta<Math.PI*2;theta+=((Math.PI*2)/CIRCLE_PRECISION)) {
    		//System.out.println("Loop "+counter+++". Theta:"+theta);
    		//System.out.println("X:"+(Math.sin(theta)*r+center_x)+" Y:"+(Math.cos(theta)*r+center_y));
    		points[counter++]=new Point((int)(Math.round(Math.sin(theta)*r+center_x)),(int)(Math.round(Math.cos(theta)*r+center_y)));
    	}
        FillPolygon(p,col,0,0,points);
    }

    
    public void FillOval(int[] p,Color col,double center_x,double center_y,double w,double h) {
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
    
    public void FillPolygon(int[] p,Color col,double x_offset,double y_offset,Point...points) {
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
						Draw(p,index,col.getColor());
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

	public void Draw(int[] canvas,int index, int col) {
		int alpha = col>>>24;
		if (alpha==0) {
			return;}
		 else
		if (alpha==255) {
			canvas[index]=col;
		} else {
			float ratio=alpha/255f;
			int prev_col=canvas[index];
			int prev_r=(prev_col&0xFF);
			int prev_g=(prev_col&0xFF00)>>>8;
			int prev_b=(prev_col&0xFF0000)>>>16;
			int r=(col&0xFF);
			int g=(col&0xFF00)>>>8;
			int b=(col&0xFF0000)>>>16;

			int new_r=(int)(ratio*r+(1-ratio)*prev_r);
			int new_g=(int)(ratio*g+(1-ratio)*prev_g);
			int new_b=(int)(ratio*b+(1-ratio)*prev_b);
			
			canvas[index]=new_r+(new_g<<8)+(new_b<<16)+(col&0xFF000000);
		}
	}

	@Override
	public void run() {
		while (true) {
            // request a JPanel re-drawing
            repaint();       
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KEYS.put(e.getKeyCode(),true);
		//System.out.println("Key List: "+KEYS);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KEYS.put(e.getKeyCode(),false);
		//System.out.println("Key List: "+KEYS);
	}
}