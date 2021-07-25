import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.geom.*;
import javax.swing.filechooser.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
import javax.imageio.*;

public class DImageFlip extends JFrame
{
	Container     c;
	
	DImageEnhancer imageEnhancer;
	DOriginalImage originalImage;
	JPanel        originalPanel, enhancedPanel;
	JPanel        originPanel, enhancePanel;
	TitledBorder  originalBorder, enhancedBorder;
	JButton       jbtnReset,jbtnSave,jbtnFlip;
	JTabbedPane   jtp;
	JTextArea	  jta;
    
     final Image img;
	 DImageNegative app1;
	public DImageFlip(Image image)
	{ 
	    super();
	    
	    img = image;
		
		c = getContentPane();
        c.setLayout(null);
		
		setTitle("Image Flipping");	   
	   	resize(1024,800);// 
		setResizable(true);

        originalBorder = BorderFactory.createTitledBorder("Original Image");
        enhancedBorder = BorderFactory.createTitledBorder("Enhanced Image");

		originalPanel  = new JPanel();
		originalPanel.setBounds(1,5,500,1000);
		originalPanel.setLayout(null);
		originalPanel.setBorder(originalBorder);
		
		
		
		enhancedPanel  = new JPanel();
		enhancedPanel.setLayout(null);
		enhancedPanel.setBorder(enhancedBorder);
		enhancedPanel.setBounds(505,5,505,1000);	
		
		
		originPanel = new JPanel();
		originPanel.setLayout(null);
	
		originPanel.setBackground(Color.BLACK);
		originPanel.setBounds(2,610,500,220);
		
		jbtnReset = new JButton("RESET");
		jbtnReset.setBounds(300,650,80,25);
		
		jbtnSave = new JButton("SAVE");
		jbtnSave.setBounds(450,650,80,25);
		
		jbtnFlip = new JButton("FLIPPING");
		jbtnFlip.setBounds(600,650,100,25);
		
		this.setBackground(Color.BLACK);
		
		
		
		
		
	
		
		enhancePanel = new JPanel();
		enhancePanel.setLayout(null);
		enhancePanel.setBackground(new Color(226,227,230));		
		enhancePanel.setBounds(506,610,500,220);//

		
			c.add(jbtnReset);
		c.add(jbtnSave);
		c.add(jbtnFlip);
	
		
		
	
		
        
		
		originalImage = new DOriginalImage(image);
		JScrollPane jsp = new JScrollPane(originalImage);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(5,20,495,575);
		jsp.setBackground(Color.BLACK);
		originalPanel.add(jsp);
		originalImage.setPreferredSize(new Dimension((image.getWidth(this)+20),(image.getHeight(this)+20)));
		c.add(originalPanel);

		imageEnhancer = new DImageEnhancer(image);
		JScrollPane jsp1 = new JScrollPane(imageEnhancer);	
		jsp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp1.setBounds(5,20,495,575);
		jsp1.setBackground(Color.BLACK);
		enhancedPanel.add(jsp1);
		imageEnhancer.setPreferredSize(new Dimension(image.getWidth(this),image.getHeight(this)));
		c.add(enhancedPanel); 
		
		jbtnReset.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {				
			 imageEnhancer.setImage(img);
			
		    }
		});      	
		jbtnSave.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {				
			JFileChooser fcc = new JFileChooser();
			fcc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fcc.showDialog(app1,"Save");
				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					File filex = fcc.getSelectedFile();
				
				saveJPG(DImageEnhancer.oBufferedImage,filex);
				}
			}
		});
		
		jbtnFlip.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				imageEnhancer.MirrorFilter();
			}
		});
		
		
		
		
	
		
		
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				dispose();	
			}
		});
		
		

		setVisible(true);		
		
	}
	
	
	
	public static void saveJPG(Image img, File s)
		{
				BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				g2.drawImage(img, null, null);
			 
				FileOutputStream out = null;
				try
				{ 
				  out = new FileOutputStream(s); 
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
	

}
	
	
	



