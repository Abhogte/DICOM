import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.geom.AffineTransform;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import com.jhlabs.image.*;
import java.awt.event.*;

public class DImageEnhancer extends JPanel
{
    Container c;
	int x,y;
	int       iw;
    int       ih;
	boolean   blnRescale, blnProblem;
	private   BufferedImage bi;
    static BufferedImage mBufferedImage=DICOM_Image.mBufferedImage, oBufferedImage;
	BufferedImageOp op;
	BufferedImageOp biop;
	BufferedImage bimg;
	Image     image;
	Hashtable imageOperations;
	
	
    float[]   elements = { 0.0f, -1.0f, 0.0f, -1.0f,  5.0f, -1.0f, 0.0f, -1.0f,  0.0f};    

    public DImageEnhancer(Image img)
	{	        
		image = img;
		
		createOps();

		blnRescale = false;
		blnProblem = false;

        try
		{
           MediaTracker tracker = new MediaTracker(this);
           tracker.addImage(image, 0);
           tracker.waitForID(0);
        }
		catch (Exception e)
		{
			 
		}

        try
        {
        	mBufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();
			g2.drawImage(image, null, null);	

			oBufferedImage = mBufferedImage; 
        }
        catch (Exception ex)
        {
			System.out.println("\nPROBLEM");
			blnProblem = true;
        }
        
	addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent me)
			{
				x=me.getX();
				y=me.getY();
				
				repaint();
			}
		});
		
    }

	public void createOps()
	{
	
		imageOperations = new Hashtable();
	
	/*	float[] edgeKernel = {0.0f, -1.0f, 0.0f,
							  -1.0f, 4.0f, -1.0f,
							  0.0f, -1.0f, 0.0f
							 };

		imageOperations.put("Edge detector", new ConvolveOp(new Kernel(3, 3, edgeKernel)));

	*/
		float[] sharpKernel = {0.0f, -1.0f, 0.0f,
							   -1.0f, 5.0f, -1.0f,
							   0.0f, -1.0f, 0.0f
							  };

		imageOperations.put("Sharpen", new ConvolveOp(new Kernel(3, 3, sharpKernel), ConvolveOp.EDGE_NO_OP, null));

		short[] invert    = new short[256];
		for (int i = 0; i < 256; i++) 
		{
		
		 invert[i]    = (short)(255 - i);
		
		}
	
			imageOperations.put("Invert", new LookupOp(new ShortLookupTable(0, invert), null));
}
   public void sharpenImage()
	{
		if (blnProblem)
		{
			return;
		}

		mBufferedImage = oBufferedImage;
		op = (BufferedImageOp)imageOperations.get("Sharpen");
        mBufferedImage     = op.filter(mBufferedImage, null);       
        repaint();
	}


    public void negativeEffect()
	{
		if (blnProblem)
		{
			return;
		}

		mBufferedImage = oBufferedImage;
		op = (BufferedImageOp)imageOperations.get("Invert");
        mBufferedImage     = op.filter(mBufferedImage, null);       
        repaint();
	}

	public void FlipFilter()
	{
    FlipFilter flipFilter = new FlipFilter();
	flipFilter.filter(oBufferedImage,mBufferedImage);
	repaint();
	}
	public void MirrorFilter()
	{
    MirrorFilter mirrorFilter = new MirrorFilter();
	mirrorFilter.filter(oBufferedImage,mBufferedImage);
	repaint();
	}
	public void RotateFilter()
	{
    RotateFilter rotateFilter = new RotateFilter();
	rotateFilter.filter(oBufferedImage,mBufferedImage);
	repaint();
	}

	
    public void rescaleImage()
	{		
		if (blnProblem)
		{
			return;
		}

		blnRescale     = true;
		mBufferedImage = oBufferedImage;
		AffineTransform at = new AffineTransform();
		biop = null;

		int bw = mBufferedImage.getWidth(this);
		int bh = mBufferedImage.getHeight(this);

		bimg = new BufferedImage(bw,bh,BufferedImage.TYPE_INT_RGB);
		RescaleOp rop = new RescaleOp(1.5f, 1.0f, null); 		
		rop.filter(mBufferedImage,bimg);

		biop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		repaint();
	}
	
	
	
	public void rotate90()
	{
		Graphics2D g2d=(Graphics2D)getGraphics(); // Create a Java2D version of g.
         
         g2d.rotate(Math.toRadians(90));// Rotate the image by 1 radian. 
       repaint();
	}
	
	

    public void paint(Graphics g) 
    {
		if (mBufferedImage == null) return;		

		if (blnRescale)
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(bimg,biop, 0, 0);
			

			blnRescale = false;
		}
		else
		{
			g.drawImage(mBufferedImage, 0, 0, null);
			System.out.println("##### Width = " + mBufferedImage.getWidth(null) + " Height = " +  mBufferedImage.getHeight(null));
			System.out.println("***** Panel Width = " + getWidth() + " Height = " + getHeight());
            g.clearRect(0, mBufferedImage.getHeight(null),  mBufferedImage.getWidth(null),  mBufferedImage.getHeight(null) + 500);
			
		}
		    g.drawLine(x-1,y,x,y); 
			g.drawLine(x,y-1,x,y); 
			g.drawLine(x,y,x,y); 
			g.drawLine(x,y,x,y+1);
			g.drawLine(x,y,x+1,y);
			g.drawLine(x,y,x+1,y+1);
			g.drawLine(x,y,x,y+2);
			g.drawLine(x,y,x+2,y);
			g.drawLine(x,y,x+2,y+2);
			g.drawLine(x,y,x-1,y+1);
			g.drawLine(x+1,y-1,x,y); 
    }
    
    public void setImage(Image img)
    {
    	this.image = img;
    	
    	try
        {
        	mBufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();
			g2.drawImage(image, null, null);	

			oBufferedImage = mBufferedImage; 
        }
        catch (Exception ex)
        {
			System.out.println("\nPROBLEM");
			blnProblem = true;
        }
        
    	repaint();
    }
    
}
