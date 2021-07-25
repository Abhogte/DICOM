import java.awt.*;
import java.awt.event.* ;
import java.awt.image.* ;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

public class DICOM_Table extends JFrame
{
	Container c;
	JTable table;
	
		String[] info;
		//String[] column_names;
		String pn,pid,pbd,sd,mod,manu,insti,phy,sex,nof;
	//int x=10,y=10;
	public DICOM_Table(DicomHeaderReader Dhr)
	{	
	   
		super("MORE DETAILS...");
		 //initComponents();
	    info=Dhr.getHeaderInfo();
	 	String[] column_names={"TAG","DESCRIPTION","VALUE"};
		setSize(500,500);	
		setLocation(400,150);
		c = getContentPane();
		c.setLayout(new FlowLayout());
		Object[][] data={
			{"0010,0010","Patient's Name",info[0]},
			{"0010,0020","Patient ID",info[1]},
			{"0010,0030","Patient's Birth Date",info[2]},
			{"0008,0020","Study Date",info[4]},
			{"0008,0060","Modality",info[5]},
			{"0008,0070","Manufacturer",info[6]},
			{"0008,0080","Institution Name",info[14]},
			{"0008,0090","Referring Physician's Name",info[13]},
			{"0010,0040","Patient's Sex",info[3]},
			{"0028,0008","Number Of frames",info[7]}			
		};
	
		table = new JTable(data,column_names);
		table.setGridColor(Color.WHITE);
		table.setRowHeight(35);
		JScrollPane scrollpane = new JScrollPane(table);
		table.setFillsViewportHeight(true);


		c.add(scrollpane);
		setVisible(true);
	}	
		
	
}
