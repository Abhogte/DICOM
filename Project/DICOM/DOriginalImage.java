import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
//import javax.swing.event.*;
//import javax.swing.border.*;
import java.awt.geom.AffineTransform;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;


//public class OriginalImage extends JFrame 
public class DOriginalImage extends JPanel 
{
    Container c;	
	int iw;
    int ih;
	private BufferedImage bi;
	Image img;

    float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f,  5.0f, -1.0f, 0.0f, -1.0f,  0.0f};    

    public DOriginalImage(Image img)
	{	
        this.img = img;

        try
		{
           MediaTracker tracker = new MediaTracker(this);
           tracker.addImage(img, 0);
           tracker.waitForID(0);
        }
		catch (Exception e)
		{
		}

        iw = img.getWidth(this);
        ih = img.getHeight(this);		
    }

    public void paint(Graphics g)
	{				
      	g.drawImage(img, 0, 0, this);
    } 
}
