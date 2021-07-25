import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import javax.swing.filechooser.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;
import java.awt.image.*;


public class DImageZoom extends JFrame  
       implements MouseListener, ActionListener 
{ 
    private ImagePanel m_imagePanel; 
    private JScrollPane m_srollPane; 
    private    JPanel m_imageContainer; 
    private JLabel m_zoomedInfo; 
    private JButton m_zoomInButton; 
    private JButton m_zoomOutButton; 
    private JButton m_originalButton;
    private JButton m_save; 
    private Cursor m_zoomCursor;
	DImageZoom app1;	
	Image img2;	
		

    /** 
     * Constructor 
     * @param image 
     * @param zoomPercentage 
     * @param imageName 
     */     
	 
    public DImageZoom(Image image, double zoomPercentage, String imageName) 
    { 
		
        super("Image Zoomer"+ imageName );
			
			setBounds(300,100,10,10);
        if(image == null) 
        { 
            add(new JLabel("Image " + imageName + " not Found")); 
        } 
        else 
        { 
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
             
            m_zoomInButton = new JButton("Zoom In"); 
            m_zoomInButton.addActionListener(this); 
             
            m_zoomOutButton = new JButton("Zoom Out"); 
            m_zoomOutButton.addActionListener(this); 
             
            m_originalButton = new JButton("Original"); 
            m_originalButton.addActionListener(this); 
			
	
              
            m_zoomedInfo = new JLabel("Zoomed to 100%"); 
             
            topPanel.add(new JLabel("Zoom Percentage is " +  
                                    (int)zoomPercentage + "%")); 
            topPanel.add(m_zoomInButton); 
            topPanel.add(m_originalButton); 
            topPanel.add(m_zoomOutButton);
           
            topPanel.add(m_zoomedInfo); 
        
			
            m_imagePanel = new ImagePanel(image, zoomPercentage); 
            m_imagePanel.addMouseListener(this); 
             
            m_imageContainer = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
            m_imageContainer.setBackground(Color.BLACK); 
            m_imageContainer.add(m_imagePanel); 
             
            m_srollPane = new JScrollPane(m_imageContainer); 
            m_srollPane.setAutoscrolls(true); 
             
            getContentPane().add(BorderLayout.NORTH, topPanel); 
            getContentPane().add(BorderLayout.CENTER, m_srollPane); 
            getContentPane().add(BorderLayout.SOUTH,  
                         new JLabel("Left Click to Zoom In," + 
                         " Right Click to Zoom Out", JLabel.CENTER)); 
             
            m_imagePanel.repaint(); 
        } 
         
        pack(); 
        setVisible(true); 
    } 
     
    /** 
     * Action Listener method taking care of  
     * actions on the buttons 
     */ 
    public void actionPerformed(ActionEvent ae) 
    { 
        if(ae.getSource().equals(m_zoomInButton)) 
        { 
            m_imagePanel.zoomIn(); 
            adjustLayout(); 
        } 
        else if(ae.getSource().equals(m_zoomOutButton)) 
        { 
            m_imagePanel.zoomOut(); 
            adjustLayout(); 
        } 
        else if(ae.getSource().equals(m_originalButton)) 
        { 
            m_imagePanel.originalSize(); 
            adjustLayout(); 
        }
       
     }
    /** 
     * This method takes the Zoom Cursor Image 
     * and creates the Zoom Custom Cursor which is  
     * shown on the Image Panel on mouse over 
     *  
     * @param zoomcursorImage 
     */ 
    public void setZoomCursorImage(Image zoomcursorImage) 
    { 
        m_zoomCursor = Toolkit.getDefaultToolkit().createCustomCursor( 
                        zoomcursorImage, new Point(0, 0), "ZoomCursor"); 
    } 
     
    /** 
     * This method adjusts the layout after  
     * zooming 
     * 
     */ 
    private void adjustLayout() 
    { 
        m_imageContainer.doLayout();         
        m_srollPane.doLayout(); 

        m_zoomedInfo.setText("Zoomed to " + (int)m_imagePanel.getZoomedTo() + "%"); 
    } 
  
    /** 
     * This method handles mouse clicks 
     */ 
    public void mouseClicked(MouseEvent e)  
    { 
        if(e.getButton() == MouseEvent.BUTTON1) 
        { 
            m_imagePanel.zoomIn();                  
        } 
        else if(e.getButton() == MouseEvent.BUTTON3) 
        { 
            m_imagePanel.zoomOut(); 
        } 

        adjustLayout(); 
    } 
         
    public void mouseEntered(MouseEvent e) 
    { 
        m_imageContainer.setCursor(m_zoomCursor);              
    } 
            
    public void mouseExited(MouseEvent e) 
    { 
        m_imageContainer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));          
    } 
            
    public void mousePressed(MouseEvent e) 
    { 
      
    } 
            
    public void mouseReleased(MouseEvent e) 
    { 
      
    } 

    /** 
     * This class is the Image Panel where the image 
     * is drawn and scaled. 
     *  
     * @author Rahul Sapkal(rahul@javareference.com) 
     */ 
    public class ImagePanel extends JPanel 
    { 
        private double m_zoom = 1.0; 
        private double m_zoomPercentage; 
        private         Image m_image; 
                 
        /** 
         * Constructor 
         *  
         * @param image 
         * @param zoomPercentage 
         */                 
        public ImagePanel(Image image, double zoomPercentage) 
        { 	
            m_image = image; 
            m_zoomPercentage = zoomPercentage / 100; 
        } 
         
        /** 
         * This method is overriden to draw the image 
         * and scale the graphics accordingly 
         */ 
        public void paintComponent(Graphics grp)  
        {  
            Graphics2D g2D = (Graphics2D)grp; 
             
            
            g2D.setColor(Color.WHITE); 
            
            g2D.fillRect(0, 0, getWidth(), getHeight()); 
             
         
            g2D.scale(m_zoom, m_zoom); 
              img2=m_image;
           
            g2D.drawImage(m_image, 0, 0, this);  
        } 
          
        /** 
         * This method is overriden to return the preferred size 
         * which will be the width and height of the image plus 
         * the zoomed width width and height.  
         * while zooming out the zoomed width and height is negative 
         */ 
        public Dimension getPreferredSize() 
        { 
            return new Dimension((int)(m_image.getWidth(this) +  
                                      (m_image.getWidth(this) * (m_zoom - 1))), 
                                 (int)(m_image.getHeight(this) +  
                                      (m_image.getHeight(this) * (m_zoom -1 )))); 
        } 
         
        /** 
         * Sets the new zoomed percentage 
         * @param zoomPercentage 
         */ 
        public void setZoomPercentage(int zoomPercentage) 
        { 
            m_zoomPercentage = ((double)zoomPercentage) / 100;     
        } 
         
        /** 
         * This method set the image to the original size 
         * by setting the zoom factor to 1. i.e. 100% 
         */ 
        public void originalSize() 
        { 
            m_zoom = 1;  
        } 
         
        /** 
         * This method increments the zoom factor with 
         * the zoom percentage, to create the zoom in effect  
         */ 
        public void zoomIn() 
        { 
            m_zoom += m_zoomPercentage; 
        }             
         
        /** 
         * This method decrements the zoom factor with the  
         * zoom percentage, to create the zoom out effect  
         */ 
        public void zoomOut() 
        { 
            m_zoom -= m_zoomPercentage; 
             
            if(m_zoom < m_zoomPercentage) 
            { 
                if(m_zoomPercentage > 1.0) 
                { 
                    m_zoom = 1.0; 
                } 
                else 
                { 
                    zoomIn(); 
                } 
            } 
        } 
         
        /** 
         * This method returns the currently 
         * zoomed percentage 
         *  
         * @return 
         */ 
        public double getZoomedTo() 
        { 
            return m_zoom * 100;  
        } 
    } 
} 
