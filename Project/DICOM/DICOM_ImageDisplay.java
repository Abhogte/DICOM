import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.geom.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import javax.imageio.stream.*;

import javax.swing.filechooser.*;
import com.sun.image.codec.jpeg.*;


public class DICOM_ImageDisplay extends JPanel
{	
	int mouseSensitivity;	
	DICOM_Image	        dicom_Image;
	Image[]             img;
	int                 frames;
	DImageZoom izf;

DImageFlip dif;
DImageNegative din;
DImageContrast dic;
	DImageRotate dir;
	DImageEdgeDetect ed;
	
	BufferedImage mBufferedImage, oBufferedImage;
	double Xscale,Yscale;
	BufferedImage bimg;
	JTextField jtf1=new JTextField();
	JTextField jtf2=new JTextField();
	JTextField jtf3=new JTextField();
	int lastoffsetX=0;
	JFrame f;
        	int lastoffsetY=0;
        	File filex;
        	int XOrigin=0;
        	int YOrigin=0;
        	static int X,Y;
        	int width,height,n;
        	 double m_zoom = 1.0; 
        	 double m_zoomPercentage; 
       		DICOM_ImageDisplay app1;		    
public DICOM_ImageDisplay()
	{
	    setLayout(null);	
		setBackground(Color.black);
		Border border=BorderFactory.createEmptyBorder(0,0,0,0);
		jtf1.setBounds(5,5,80,20);
		jtf1.setForeground(Color.white);
		jtf1.setBackground(Color.black);
		jtf1.setBorder(border);
		jtf1.setEditable(false);
		add(jtf1);
		jtf2.setBounds(90,5,80,20);
		jtf2.setForeground(Color.white);
		jtf2.setBackground(Color.black);
		jtf2.setBorder(border);
		jtf2.setEditable(false);
		add(jtf2);
		jtf3.setBounds(175,5,80,20);
		jtf3.setForeground(Color.white);
		jtf3.setBackground(Color.black);
		jtf3.setBorder(border);
		jtf3.setEditable(false);
		add(jtf3);
		
		
		
		System.out.println("Inside constructor of Dicom_image_panel");
		
		try
		{
			frames = img.length;
		}
		catch (Exception ex)
		{
			System.out.println("IMAGE = null");
		}
		
	dicom_Image = new DICOM_Image();
	
       JScrollPane jsp = new JScrollPane(dicom_Image);
		jsp.setBounds(5,25,1250,630);
		jsp.setBorder(border);
		dicom_Image.setBackground(Color.black);	
		add(jsp);
	

			setVisible(true);
			}	
	
		MouseMotionListener ml=new MouseMotionAdapter()
	{
		
	    	public void mousePressed(MouseEvent e)
	    	{
	    		
	    			
	    			System.out.println("##########");
	    		if(SwingUtilities.isLeftMouseButton(e))
        		{
        			 lastoffsetX=e.getX();
        			
        			 System.out.println( lastoffsetX);
        			 lastoffsetY=e.getY(); 
        			 System.out.println( lastoffsetY);              //capture starting point
        		}	
	    		
	    	}
	    	public void mouseDragged(MouseEvent e)
        	{
        		
        		System.out.println("---------------");
        		
        			X=e.getX();
        			Y=e.getY();
        			
        		if(SwingUtilities.isLeftMouseButton(e))
        		{
        			int newX=e.getX()-lastoffsetX;
        		      
        			int newY=e.getY()-lastoffsetY;
        			
        			lastoffsetX+=newX;
        			lastoffsetY+=newY;
        			System.out.println("---------------");
        			XOrigin+=newX;
        			YOrigin+=newY;
        			dicom_Image.panImage(XOrigin,YOrigin);
        		
        			dicom_Image.repaint();
        			
        		}}
        	
	};
	
	
    	public void zoom()
    	{
    		System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				izf = new DImageZoom(dicom_Image.img[n],10," ");			
				System.out.println("index after " + dicom_Image.n);
    	}
    	public void rotate()
    	{
    		
    		System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				dir= new DImageRotate(dicom_Image.img[n]);			
				System.out.println("index after " + dicom_Image.n);
    		
    	}
    	public void edge_detection()
    	{
    			System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				ed= new DImageEdgeDetect(dicom_Image.img[n]);			
				System.out.println("index after " + dicom_Image.n);
    			
    	}
    		public void flip()
    	{
    		
    		System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				dif = new DImageFlip(dicom_Image.img[n]);			
				System.out.println("index after " + dicom_Image.n);
    		
    	}
    	public void contrast()
    	{
    		System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				dic = new DImageContrast(dicom_Image.img[n]);			
				System.out.println("index after " + dicom_Image.n);
    		
    	}
    	public void negative()
    	{
    		System.out.println("Inside zoom method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				din = new DImageNegative(dicom_Image.img[n]);			
				System.out.println("index after " + dicom_Image.n);
    	}
    	public void print()
    	{
    		System.out.println("Inside print method dip");
    		if(img==null)System.out.println("img = null");
                System.out.println("index before " + dicom_Image.n);
				UIManager.put("swing.boldMetal", Boolean.FALSE);
        f = new JFrame("");
         JButton printButton = new JButton("CLICK TO PRINT THE IMAGE");
        printButton.addActionListener(new Print(dicom_Image.img[n]));
        f.add("Center", printButton);
       f.pack();
       f.setLocation(500,300);
        f.setVisible(true);
	    	}
    		
    	
    		public  void saveJPG()
		{
			Image img=dicom_Image.img[n];
			BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				g2.drawImage(img, null, null);
			 JFileChooser fcc = new JFileChooser();
			fcc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fcc.showDialog(app1,"Save");
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					 filex = fcc.getSelectedFile();
				
				}
				FileOutputStream out = null;
				try
				{ 
				  out = new FileOutputStream(/*new File("D:\\PROJECT\\myhell.jpg")*/filex); 
				}
				catch (java.io.FileNotFoundException io)
				{ 
				  System.out.println("File Not Found"  + io); 
				}
				
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
				param.setQuality(0.5f,false);
				encoder.setJPEGEncodeParam(param);
				
				try 
				{ 
				  encoder.encode(bi); 
				  out.close(); 
				}
				catch (java.io.IOException io) 
				{
				  System.out.println("IOException"); 
				}	
		}
		
     
	public void setNumberOfFrames(int frames)
    {
    	this.frames= frames;
    }
    public void setFrame(int index)
	{		
		System.out.println("Inside setFrame");
		dicom_Image.setView(index);
	}	
	public void setImages(Image[] images)
	{
		System.out.println("Inside setImages of Dicom_Image_Panel....");
		this.img = images;
		
		if(img!=null)
		{
	
		System.out.println("width="+img[0].getWidth(this)+"height="+img[0].getHeight(this));
		
		mBufferedImage = new BufferedImage(img[0].getWidth(null), img[0].getHeight(null), BufferedImage.TYPE_INT_RGB);
		dicom_Image.setView(0);
	}}
    public void setInfo(String[] info)
	{
		jtf1.setText(info[0]);
		jtf2.setText(info[1]);
		jtf3.setText(info[3]);
    }
 

        			
	    }
    
