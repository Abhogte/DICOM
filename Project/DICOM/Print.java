import java.awt.*;
import java.awt.event.*;
//import javax.swing.*;
import java.awt.print.*;
 
public class Print extends DICOM_Image implements Printable, ActionListener {
 
 DicomReader Dr;
	DicomHeaderReader Dhr;
	Image[] images;
		byte[]  DICOM_Bytes;
		Image image;
		Print(Image img)
		{
			image=img;
		}
 
    public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException {
 
        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }
 
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Now we perform our rendering */
        
        g2d.drawImage(image,0,0,null);
 
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
 
    public void actionPerformed(ActionEvent e) {
         PrinterJob job = PrinterJob.getPrinterJob();
         job.setPrintable(this);
         boolean ok = job.printDialog();
         if (ok) {
             try {
                  job.print();
             } catch (PrinterException ex) {
              System.out.println("JOB NOT COMPLETED PRINTER NOT FOUND");
             }
         }
    }
    }