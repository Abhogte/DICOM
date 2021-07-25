import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.geom.AffineTransform;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import javax.swing.filechooser.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
import com.jhlabs.image.*;



public class DICOM_Image extends JPanel
{
    int w,h;
    
    Container c;	
	int iw;
    int ih;
	private BufferedImage bi;
	

	 Image img[];

	int ox, nx;
	int frames;
	int n = 0;
	int x, y;
	double width, height;

    Graphics2D g2d;

    boolean   blnRescale, blnProblem; 
	static BufferedImage mBufferedImage, oBufferedImage;
	BufferedImageOp op; 
	BufferedImageOp biop;
	BufferedImage bimg; 
	Image     image; 
	Hashtable imageOperations;
	
    float[]   elements = { 0.0f, -1.0f, 0.0f, -1.0f,  5.0f, -1.0f, 0.0f, -1.0f,  0.0f};

    public DICOM_Image() 
	{	
        
        blnRescale = false; 
		blnProblem = false; 
		
		setBounds(5,5,9000,9000);
	System.out.println("**************");
	    g2d = (Graphics2D) getGraphics();
		System.out.println("inside constructor of Dicom_image");
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
                ++n;
			
				if (n >= frames)
				{
					n = 0;
				}
				System.out.println("aa");
				redrawImage(n);
			}
		});
     
    }

    public void setView(int n)
	{
		System.out.println("Inside Setview = "+ n);
		this.img = img;
		
		x=0;
		y=0;
		redrawImage(n);
		updateUI();
		
		
	}
               
    public void redrawImage(int n)
	{
		g2d = (Graphics2D) getGraphics();
		this.n = n;
		if (g2d != null)
		{	System.out.println("!!!!!!!!!!!!");	
		mBufferedImage = new BufferedImage(img[n].getWidth(null), img[n].getHeight(null), BufferedImage.TYPE_INT_RGB);	
			g2d.drawImage(img[n], x, y, this);      			
			
		}	
	}
 
	
	public void setImages(Image[] images,int width,int height)
	{
		System.out.println("Inside setImages of 3 parameters");
	    setPreferredSize(new Dimension(images[0].getWidth(this)+50,images[0].getHeight(this)+50));
	    updateUI();
		 img    = images;
		 mBufferedImage = new BufferedImage(img[n].getWidth(null), img[n].getHeight(null), BufferedImage.TYPE_INT_RGB);
		 	

		 try
		{
			frames = img.length;
		}
		catch (Exception ex)
		{
			System.out.println("IMAGE = null");
		}

		this.width  = width;
		this.height = height;

		ox = nx = 0;

        x=0;
		y=0;
		
		
        redrawImage(n);  
 
	}
   
     public void panImage(int x, int y )
     {
     	
	 this.x=x;
		this.y=y;
		
		
       
        System.out.println("----------************");  

     }
     
 public void rotateImage(int x,int y)
 {
 	this.x=y;
 	this.y=x;
 	System.out.println("----------************");
 }
  
 
      		
        public void rotate90()
  {
  	DImageEnhancer d=new DImageEnhancer(mBufferedImage);
  	d.RotateFilter();
  }
  
  	
	
       
    public void paint(Graphics g)
	{
		super.paint(g);	
		Graphics2D g2D = (Graphics2D)g; 	
	  	if(img!=null)
	   	{ 
	     	System.out.println( " Paint n = " + n);
	     	
	     	System.out.println( " Paint n = " + n);
	     	if(DICOM.jb8pre==true)
	     	{
		     g.drawImage(img[n], x-(int)(width/2), y-(int)(height/2), this);
		     }
	
        
        
      

	else if(DICOM.jb4pre==true)
		{
			


			
		}
	  else
	  g.drawImage(img[n], x, y, this);


}
}
      
   
  
}
