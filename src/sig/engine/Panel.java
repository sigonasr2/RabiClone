package sig.engine;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sig.JavaProjectTemplate;

public class Panel extends JPanel implements Runnable {
	JFrame window;
    public int pixel[];
    public int width=1280;
    public int height=720; 
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
	int lastFrameCount=0;

    public Panel(JFrame f) {
        super(true);
		this.window=f;
        thread = new Thread(this, "MyPanel Thread");
    }

    /**
     * Get Best Color model available for current screen.
     * @return color model
     */
    protected static ColorModel getCompatibleColorModel(){        
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();        
        return gfx_config.getColorModel();
    }

    /**
     * Call it after been visible and after resizes.
     */
    public void init(){        
        cm = getCompatibleColorModel();
        int screenSize = width * height;
        if(pixel == null || pixel.length < screenSize){
            pixel = new int[screenSize];
        }        
        if(thread.isInterrupted() || !thread.isAlive()){
            thread.start();
        }
        mImageProducer =  new MemoryImageSource(width, height, cm, pixel,0, width);
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
        g.drawImage(this.imageBuffer, 0, 0, this);  
		
		
		if (window!=null&&System.currentTimeMillis()-lastSecond>=1000) {
			window.setTitle(JavaProjectTemplate.PROGRAM_NAME+" - FPS: "+(frameCount-lastFrameCount));
			lastFrameCount=frameCount;
			lastSecond=System.currentTimeMillis();
		}
		frameCount++;
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
        int[] p = pixel; // this avoid crash when resizing
        //a=h/w
        
        for (int x=0;x<width;x++) {
        	for (int y=0;y<height;y++) {
        		p[y*width+x]=(0<<16)+(0<<8)+0;
        	}
        }
        
        x_offset+=1;
        y_offset=50;
        
        FillPolygon(p,Color.WHITE,50,50,new Point[] {
        		new Point(135,2),
        		new Point(166,96),
        		new Point(265,97),
        		new Point(185,156),
        		new Point(215,251),
        		new Point(134,192),
        		new Point(54,251),
        		new Point(84,156),
        		new Point(4,97),
        		new Point(103,96),
        });
        FillPolygon(p,Color.BRIGHT_CYAN,x_offset,y_offset,new Point[] {
            	new Point(28,29),
            	new Point(78,103),
            	new Point(120,31),
            	new Point(123,221),
            	new Point(30,218),
            });
        //FillRect(p,Color.BRIGHT_RED,200,200,600,64);
		final Color testAlpha = new Color(150,0,0,128);
        FillCircle(p,testAlpha,150,150,100);
        FillOval(p,Color.BRIGHT_GREEN,300,150,100,50);
    }
    
    public void FillRect(int[] p,Color col,double x,double y,double w,double h) {
    	for (int xx=0;xx<w;xx++) {
        	for (int yy=0;yy<h;yy++) {
        		int index = ((int)y+yy)*width+(int)x+xx;
        		p[index]=col.getColor();
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
    				int index = (scanLine+(int)y_offset)*width+x+(int)x_offset;
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
            //try {Thread.sleep(1);} catch (InterruptedException e) {}
        }
	}
}