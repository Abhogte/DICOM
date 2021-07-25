import java.awt.*;
import java.awt.event.* ;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


                            
public class DICOM extends JFrame
{          
	Container c;
	static FileInputStream fis;
	static DataInputStream dis;	
	static	BufferedImage bimg,mBufferedImage;
	
	DicomReader Dr;
	DicomHeaderReader Dhr;
	byte[]  DICOM_Bytes;
	
	
	Image[] images;
	String[] info;
    
	int width, height;
DICOM app1;
    
	DICOM_ImageDisplay DIP; 
	JButton jb1=new JButton(new ImageIcon("icon.png"));
	JButton jb2=new JButton(new ImageIcon("1.png"));
	JButton jb3=new JButton(new ImageIcon("2.png"));
	JButton jb4=new JButton(new ImageIcon("3.png"));
	JButton jb5=new JButton(new ImageIcon("4.png"));
	JButton jb6=new JButton(new ImageIcon("5.png"));
	JButton jb7=new JButton(new ImageIcon("6.png"));
	JButton jb8=new JButton(new ImageIcon("7.png"));
	JButton jb9=new JButton(new ImageIcon("8.png"));
	JButton jb10=new JButton(new ImageIcon("9.png"));
	JButton jb11=new JButton(new ImageIcon("10.png"));
	JButton jb12=new JButton(new ImageIcon("11.png"));
	JButton jb13=new JButton(new ImageIcon("12.png"));
	JButton jb14=new JButton(new ImageIcon("13.png"));
	JLabel lbl=new JLabel("Name");
    static boolean jb1pre,jb2pre,jb3pre,jb4pre,jb5pre,jb6pre,jb7pre,jb8pre,jb9pre,jb10pre,jb11pre,jb12pre,jb13pre,jb14pre;	
 	JPanel    imagePanel;
 	JButton printButton;
		DICOM_Image dicom_Image;
	public DICOM()
	{	
		super("DICOM - Digital Imaging and Communications in Medicine");
		toFront();
		setSize(1280,1024);	
		setLocation(0,0);
		c = getContentPane();
		c.setLayout(null);
	
		//c.setBackground(Color.BLACK);
		
		this.getContentPane().setBackground(Color.white);
		lbl.setBounds(200,100,50,50);
		
		jb1.setBounds(5,5,50,50);
		jb1.setBorderPainted(false);
		jb1.setFocusPainted(false);
		jb1.setContentAreaFilled(false);
		jb1.setToolTipText("OPEN");	
		c.add(jb1);
		jb2.setBounds(60,5,50,50);
		jb2.setBorderPainted(false);
		jb2.setFocusPainted(false);
		jb2.setContentAreaFilled(false);
		jb2.setToolTipText("ZOOM");	
		c.add(jb2);
		jb3.setBounds(115,5,50,50);
		jb3.setBorderPainted(false);
		jb3.setFocusPainted(false);
		jb3.setContentAreaFilled(false);
		jb3.setToolTipText("NEGATIVE");	
		c.add(jb3);
		jb4.setBounds(170,5,50,50);
		jb4.setBorderPainted(false);
		jb4.setFocusPainted(false);
		jb4.setContentAreaFilled(false);
		jb4.setToolTipText("ROTATE");	
		c.add(jb4);
		jb5.setBounds(225,5,50,50);
		jb5.setBorderPainted(false);
		jb5.setFocusPainted(false);
		jb5.setContentAreaFilled(false);
		jb5.setToolTipText("BRIGHT");	
		c.add(jb5);
		jb6.setBounds(280,5,50,50);
		jb6.setBorderPainted(false);
		jb6.setFocusPainted(false);
		jb6.setContentAreaFilled(false);
		jb6.setToolTipText("EXPORT");	
		c.add(jb6);
			jb7.setBounds(335,5,50,50);
		jb7.setBorderPainted(false);
		jb7.setFocusPainted(false);
		jb7.setContentAreaFilled(false);
			//c.add(jb7);
	
		jb9.setBounds(335,5,50,50);//445,5,50,50
		jb9.setBorderPainted(false);
		jb9.setFocusPainted(false);
		jb9.setContentAreaFilled(false);
		jb9.setToolTipText("FLIP");	
		c.add(jb9);
			jb10.setBounds(390,5,50,50);//500,5,50,50
		jb10.setBorderPainted(false);
		jb10.setFocusPainted(false);
		jb10.setContentAreaFilled(false);
			//c.add(jb10);
		jb11.setBounds(390,5,50,50);//555,5,50,50
		jb11.setBorderPainted(false);
		jb11.setFocusPainted(false);
		jb11.setContentAreaFilled(false);
		jb11.setToolTipText("PRINT");	
		c.add(jb11);
			jb12.setBounds(445,5,50,50);//610,5,50,50
		jb12.setBorderPainted(false);
		jb12.setFocusPainted(false);
		jb12.setContentAreaFilled(false);
		jb12.setToolTipText("EDGE TRACING");	 
			c.add(jb12);
		jb13.setBounds(500,5,50,50);//665,5,50,50
		jb13.setBorderPainted(false);
		jb13.setFocusPainted(false);
		jb13.setContentAreaFilled(false);
		jb13.setToolTipText("PATIENT DETAILS");	
		c.add(jb13);
		jb14.setBounds(555,5,50,50);//720,5,50,50
		jb14.setBorderPainted(false);
		jb14.setFocusPainted(false);
		jb14.setContentAreaFilled(false);
		jb14.setToolTipText("ABOUT");	
		c.add(jb14);
	
	
		
		DIP  = new DICOM_ImageDisplay();  // instantiate all objects in cons not above
		DIP.setBounds(0,55,1270,680);
	    c.add(DIP);
	    c.add(lbl);
	    jb1.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb1.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb1.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb1pre=true;
	    		jb1.setEnabled(true);
	    	System.out.println("ok");	
	    	openfile();	
	    	}
	    });
	    jb2.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb2.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb2.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		System.out.println("<<<<<<<<<<<<<>>>>>>>>>>");
	  // DIP.removePan(); 
	    		enable();
	    		jb2pre=true;
	    		jb2.setEnabled(true);
	    		System.out.println( " P11111111L");
	    		DIP.zoom();
	    		System.out.println( " 111112435");
	    	}
	    });
        jb3.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb3.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb3.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb3pre=true;
	    		jb3.setEnabled(true);
	    	System.out.println("ok");	
	    	DIP.negative();	
	    	}
	    });
        jb4.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb4.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb4.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb4pre=true;
	    		jb4.setEnabled(true);
	    	System.out.println("ok");	
	    
	    	DIP.rotate();
	    		
	    	}
	    });
        jb5.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb5.setContentAreaFilled(true);
	    	}
	     	public void mouseExited(MouseEvent evt)
	    	{
	    		jb5.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb5pre=true;
	    		jb5.setEnabled(true);
	    	System.out.println("ok");	
	    	DIP.contrast();	
	    	}
	    });
        
       jb6.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb6.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb6.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    	
	    		enable();
	    		jb6pre=true;
	    		jb6.setEnabled(true);
	    	System.out.println("ok");
	    	
				DIP.saveJPG();
	    		
	    	}
	    });
      
      
        jb9.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb9.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb9.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb9pre=true;
	    		jb9.setEnabled(true);
	    	System.out.println("ok");	
	    DIP.flip();	
	    	}
	    });
        
        jb11.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb11.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb11.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    	
	    		enable();
	    			jb11pre=true;
	    		jb11.setEnabled(true);
	    	System.out.println("ok");	
	    	 DIP.print();
	    	
	    	 
	    	 }
	    });
	    
        jb12.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb12.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb12.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    	
	    		enable();
	    			jb12pre=true;
	    		jb12.setEnabled(true);
	    	System.out.println("ok");
	    		
	    	DIP.edge_detection();	
	    	}
	    });
        jb13.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb13.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb13.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb13pre=true;
	    		jb13.setEnabled(true);
	    	System.out.println("ok");	
	    	new DICOM_Table(Dhr);	
	    	}
	    });
        jb14.addMouseListener(new MouseAdapter()
	    {
	    	public void mouseEntered(MouseEvent evt)
	    	{
	    		jb14.setContentAreaFilled(true);
	    	}
	    	public void mouseExited(MouseEvent evt)
	    	{
	    		jb14.setContentAreaFilled(false);
	    	}
	    	public void mouseClicked(MouseEvent evt)
	    	{
	    		
	    		enable();
	    		jb14pre=true;
	    		jb14.setEnabled(true);
	    	System.out.println("ok");	
	    	new About();	
	    	}
	    });
             
       
	   
	setVisible(true);
}
 
	public void enable()
	{
		jb1pre=false;
		jb2pre=false;
		jb3pre=false;
		jb4pre=false;
		jb5pre=false;
		jb6pre=false;
		jb7pre=false;
		jb8pre=false;
		jb9pre=false;
		jb10pre=false;
		jb11pre=false;
		jb12pre=false;
		jb13pre=false;
		jb14pre=false;
		jb1.setEnabled(false);
		jb2.setEnabled(false);
		jb3.setEnabled(false);
		jb4.setEnabled(false);
		jb5.setEnabled(false);
		jb6.setEnabled(false);
		jb7.setEnabled(false);
		jb8.setEnabled(false);
		jb9.setEnabled(false);
		jb10.setEnabled(false);
		jb11.setEnabled(false);
		jb12.setEnabled(false);
		jb13.setEnabled(false);
		jb14.setEnabled(false);
	
	}
  
	
	
    public   void openfile()
	{	
			
       try
		{			
			
			JFileChooser fc;
			int returnVal;
			File file = null;
			
			System.out.println("DICOM Reader.");

			fc = new JFileChooser();
			File curDir = new File("DICOM_Files");
			fc.setCurrentDirectory(curDir);
			fc.setDialogTitle("Dicom Files");
			returnVal = fc.showDialog(null,"Accept");
			if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					file = fc.getSelectedFile();
					System.out.println("Selected file: " + file.getName());
				} 
			else 
				{
					System.out.println("Cancelled by user.");
					System.exit(0);
				}
				int n = 0;
		
		DICOM_Bytes = null;
    
	
	    try
        {			
			fis = new FileInputStream(file.getPath());
			
			dis = new DataInputStream(fis);
			
			DICOM_Bytes = new byte[dis.available()];

            dis.read(DICOM_Bytes, 0, dis.available());
            
        	Dr  = new DicomReader(DICOM_Bytes);
			Dhr = Dr.getDicomHeaderReader() ;

			String  manufacturer	= Dhr.getaString (0x0008,0x0070); 
			info = Dhr.getInfo();
	       
			for (int i = 0; i < info.length; ++i)
			{
			      System.out.println(info[i]);
			}  
            info=Dhr.getHeaderInfo();
            DIP.setInfo(info);

			
			try
			{				
				images = Dr.getImages();
				
				width  = Dhr.getColumns();
				height = Dhr.getRows();			
				
			}
			catch (Exception e)
			{	
			    		
				images = new Image[1];
				images[0] = getToolkit().createImage("Msg.jpg");				
				width  = 500;
				height = 500;
				System.out.println("$$$$Exception  : " + e.getMessage());
			}
 
		}
		catch (Exception ex)
        {
        	JOptionPane.showMessageDialog(null, "Unsupported Format", "Error", JOptionPane.ERROR_MESSAGE);	
			System.out.println("###Exception  : " + ex.getMessage());
			images[0] = getToolkit().createImage("Msg.jpg");
		}
		 if((Dhr.getNumberOfFrames())>1)
		 {
		  System.out.println(" Enabled ");
						
		}
			DIP.dicom_Image.setPreferredSize(new Dimension(images[0].getWidth(this)+10,images[0].getHeight(this)+10));
		DIP.dicom_Image.setImages(images, width, height);//this is for drawing image on 1st window
		
		System.out.println("width="+width+"height="+height);
	
	addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{	
				dispose();
				
				
			}
		});

  				System.out.println("---------******----------");
     
	

      	}
		
	catch (Exception e)
		{
			System.out.println("Exception:"+ e);
		}
		
		}
		public static void main(String a[])
	{
		
       try
		{
			String LooknFeel="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);			
			UIManager.setLookAndFeel(LooknFeel);
			
			DICOM d=new DICOM();
			
			System.out.println("---in main-------------------");
						 
	
		}
		catch (Exception e)
		{
			
			System.out.println("Exception:"+ e);
		}	

	}
}	
