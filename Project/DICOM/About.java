import javax.swing.*;
import java.awt.*;

public class About extends JDialog
{
		Container c;
		JTextArea jta;
		public About()
	{ 
	    super();
	    	
		c = getContentPane();
        c.setLayout(new FlowLayout());
        setTitle("ABOUT Digital Imaging And Communication In Medicine : DICOM");	   
	   	resize(780,500);
		setResizable(false);
		setLocation(280,150);
		//setColor(Color.WHITE);
		jta=new JTextArea();
		jta.setFont(new Font("Charlemmagne STD",Font.PLAIN,20));
		jta.setText("\nDICOM Viewer is the Java Based System that enables to view\n and diagnose DICOM Images in Plug andPlay Environment.\n It enables Enhancement using various Image Processing Techniques. \nThe functionalities included in the Viewer are:\n1.Zooming,\n2.Flipping,\n3.Rotation,\n4.Brightening,\n5.Edge Tracing,\n6.Negative .\n The DICOM Image can be exported to other formats like\n JPEG, TIFF, BMP, PNG etc. and further printed using laser and cartridge printer.");

		add(jta);
		setVisible(true);
		
}}