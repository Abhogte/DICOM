import java.awt.*;
import java.awt.image.*;
//import java.util.*;	
import java.net.*;
import java.io.*;	
public class DicomReader
{
		int w, h , highBit, n; // HighBit = littleEndian
			boolean signed;
			final static boolean DEBUG = false;
			boolean ignoreNegValues;
			int bitsStored, bitsAllocated;
			int samplesPerPixel;
			int numberOfFrames;
			byte[] pixData;
			String filename;
			DicomHeaderReader dHR;
			public DicomReader(DicomHeaderReader dHR) throws IOException
			{
				System.out.println("In DicomReader(DicomHeaderReader)");
				this.dHR = dHR;
				h= dHR.getRows();
				w= dHR.getColumns();
				highBit= dHR.getHighBit();
				bitsStored= dHR.getBitStored();
				bitsAllocated= dHR.getBitAllocated();
				n= (bitsAllocated / 8); // = 1 or 2
				signed= (dHR.getPixelRepresentation() == 1);
				samplesPerPixel = dHR.getSamplesPerPixel();
				this.pixData= dHR.getPixels();	// It throws the exception .
				ignoreNegValues = true; 			// How do you know when ?
				samplesPerPixel = dHR.getSamplesPerPixel();
				numberOfFrames	= dHR.getNumberOfFrames();
				debug("Number of Frames " + numberOfFrames);
			}
			
			public DicomReader(byte[] array) throws IOException
			{
				this(new DicomHeaderReader(array));
				System.out.println("In DicomReader(byte[])");
			}
			
			public DicomReader(URL url) throws java.io.IOException
			{
				System.out.println("In DicomReader(URL)");
				
				URLConnection u = url.openConnection();
				int size= u.getContentLength();
				byte[] array= new byte[size];
				int bytes_read 	= 0;
				DataInputStream  in= new DataInputStream(u.getInputStream());
				while(bytes_read < size)
				{
					bytes_read += in.read(array, bytes_read, size - bytes_read);
				}
					in.close();
					this.dHR  = new DicomHeaderReader(array);
					h= dHR.getRows();
					w= dHR.getColumns();
					highBit= dHR.getHighBit();
					bitsStored= dHR.getBitStored();
					bitsAllocated= dHR.getBitAllocated();
					n= (bitsAllocated / 8); // = 1 or 2
					signed= (dHR.getPixelRepresentation() == 1);
					this.pixData= dHR.getPixels();
					ignoreNegValues = true;
					samplesPerPixel = dHR.getSamplesPerPixel();
					numberOfFrames	= dHR.getNumberOfFrames();
					debug("Number of Frames " + numberOfFrames);
			}
			
			
			public DicomReader(byte[] pixels,int w,int h,int highBit,int bitsStored,int bitsAllocated,boolean signed,int samplesPerPixel,int numberOfFrames,boolean ignoreNegVa)
	      {
			System.out.println("In DicomReader(byte[],int,int,int,int,int,boolean,int,int,boolean)");
			
			this.h=	h;
			this.w=	w;
			this.highBit=highBit;
			this.bitsStored=bitsStored;
			this.bitsAllocated=bitsAllocated;
			this.n=	bitsAllocated / 8; // = 1 or 2
			this.signed=signed;
			this.pixData=pixels;
			this.ignoreNegValues=ignoreNegValues;
			this.samplesPerPixel= samplesPerPixel;
			this.numberOfFrames= numberOfFrames;
			
		  }
		  
		  public DicomHeaderReader getDicomHeaderReader()
		  {
		  	System.out.println("In DicomHeaderReader getDicomHeaderReader()");
		  	return dHR;
		  }
		  
		  public int getNumberOfFrames()
		  {
		  	System.out.println("In int getNumberOfFrames()");
		  	return numberOfFrames;
		  }
		  
		  public String[] getInfos()
		  {
		  	System.out.println("In String[] getInfos()");
		  	return dHR.getInfo();
		  }
		  
		  public byte[] getPixels( )
		  {
		  	System.out.println("In byte[] getPixels( )");
		  	return pixData;
		  }
		  
		  // method getImage()  uses the Toolkit to create a 256 shades of gray image
		  public Image getImage()
		  {	
		    System.out.println("In Image getImage()");
		   	if (w > 2048)
		   	{
		   	  //make a size limit
		   	  debug(" w > 2048 " + "  width  : " + w + "   height  : " + h);
		   	  return scaleImage();
		   	}
		   	
		   	ColorModel cm = grayColorModel();
		   	debug("  width  : " + w + "   height  : " + h);
		   	if( n == 1)
		   	{
		   		// in case it's a  8 bit/pixel image
		   	  return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h,cm, pixData, 0, w));
		   	
		   	} 
		   	
		   	else if ( !signed)
		   	{
		   		byte[] destPixels = to8PerPix(pixData);
		   		return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h,cm,destPixels, 0, w));
		   	}
		   	
		   	else if (signed )
		   	{
		   		byte[] destPixels =	signedTo8PerPix(pixData);
		   		return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h,cm,destPixels, 0, w));
		   	}
		   	
		   	else return null;
		   }
		   
		   public Image[] getImages() throws IOException
		   {
		   	  System.out.println("In Image[] getImages()");
		   	
		   	 Image[] images = new Image[numberOfFrames];
		   	 for( int  i = 1; i <= numberOfFrames; i++)
		   	 {
		   	 	pixData = dHR.getPixels(i);
		   	 	images[i-1] = getImage();
		   	 }
		   	 
		   	 return images;
		   }
		   
		   protected Image scaleImage()
		   {
		   	System.out.println("In Image scaleImage()");
		   	
		   	ColorModel cm = grayColorModel();
		   	int scaledWidth   = w / 2;
		   	int scaledHeight  = h / 2;
		   	int index= 0;
		   	int value= 0;
		   	byte[] destPixels = null;
		   	System.gc();
		   	
		   	//scales the pixels
		   	if(n == 1 )
		   	{
		   		// 1 byte/pixel
		   		destPixels = new byte[scaledWidth * scaledHeight];
		   		
		   		for(int i = 0; i < h; i += 2)
		   		{
		   			for(int j = 0; j < w; j += 2)
		   			{
		   				destPixels[index++] = pixData[(i * w) + j];
		   			}
		   		}
		   		
		   		pixData = null; // should be replace by flush
		   		
		   		return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w/2, h/2,cm, destPixels, 0, w/2));
		   	 }
		   	 
		   	 // suppose n == 2 and unsigned value 
		   	 else if(n==2 && bitsStored<=8)
		   	 {
		   	 	// Special case for Philips : here we don't scale
		   	 	debug("w =   " + w + "  h ==  " + h);
		   	 	debug("PixData.length = " + pixData.length);
		   	 	debug(" h * w  =  " + ( h * w ));
		   	 	destPixels = new byte[w * h];
		   	 	int len = w * h;
		   	 	
		   	 	for (int i = 0; i < len; i++)
		   	 	{
		   	 		value =	(int)(pixData[i * 2]) & 0xff;
		   	 		destPixels[i] = (byte)value;
		   	 		
		   	 	}
		   	 	pixData = null;
		   	 	
		   	 	return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h,cm, destPixels, 0, w));
		   	 }
		   	 
		   	 else if(!signed)
		   	 {
		   	 	int[] intPixels = new int[scaledWidth * scaledHeight];
		   	 	debug(" !signed");
		   	 	int maxValue = 0;
		   	 	int minValue = 0xffff;
		   	 	if(highBit >= 8)
		   	 	{
		   	 		for(int i = 0; i < h; i += 2)
		   	 		{
		   	 			for(int j = 0; j < w; j += 2)
		   	 			{
		   	 				value = ((int)(pixData[(2 * (i * w + j)) + 1] & 0xff ) << 8)| (int)(pixData[2 * (i * w + j)] & 0xff);
		   	 				if(value > maxValue)  maxValue = value;
		   	 				if(value < minValue)  minValue = value;
		   	 				intPixels[index++] = value;
		   	 			}
		   	 		}
		   	 	 }
		   	 	 
		   	 	 int scale = maxValue - minValue;
		   	 	 if( scale == 0 ) scale = 1;
		   	 	 pixData = null;
		   	 	 destPixels = new byte[scaledWidth * scaledHeight];
		   	 	 for (int i =0; i < intPixels.length; i++)
		   	 	 {
		   	 	 	value = (intPixels[i] - minValue) * 256;
		   	 	 	value /= scale;
		   	 	 	destPixels[i] =(byte)(value & 0xff);
		   	 	 }
		   	 	 
		   	 	 intPixels = null;
		   	 	 
		   	 	 return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w/2, h/2,cm, destPixels, 0, w/2));
		   	 }
		   	 else if(signed)
		   	 {
		   	 	byte[] pixels = signedTo8PerPix(pixData);
		   	 	pixData = pixels;
		   	 	for(int i = 0; i < h; i += 2)
		   	 	{
		   	 		for(int j = 0; j < w; j += 2)
		   	 		{
		   	 			destPixels[index++] = pixData[(i * w) + j];
		   	 		}
		   	 	}
		   	 	
		   	 	pixData = null;
		   	 	
		   	 	return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w/2, h/2,cm, destPixels, 0, w/2));
		   	 }
		   	 
		   	 return null;
		   	}
		   	
		   	private byte[] to8PerPix(byte[] pixData)
		   	{
		   		System.out.println("In byte[] to8PerPix(byte[] pixData)");
		   		// suppose n == 2 and unsigned value
		   		
		   		if(bitsStored <= 8)
		   		{
		   			//Special case for Philips
		   			debug("w =   " + w + "  h ==  " + h);
		   			debug("PixData.length = " + pixData.length);
		   			debug(" h * w  =  " + (h * w));
		   			byte[] destPixels = new byte[w * h];
		   			int len = w * h;
		   			int value=0;
		   			for (int i = 0; i < len; i++ )
		   			{
		   			 value =(int)(pixData[i * 2]) & 0xff;
		   			 destPixels[i] =(byte)value;
		   			}
		   			return destPixels;
		   		}
		   		
		   		
		   		int[] pixels = new int[w * h];
		   		int value = 0;
		   		// case littleEndian or highBit  and unsigned;
		   		if(highBit >= 8)
		   		{
		   			for( int i = 0; i < pixels.length; i++)
		   			{
		   				value = ((int)( pixData[(2 * i) + 1] & 0xff ) << 8)| (int)( pixData[(2 * i)] & 0xff); // msb first
		   				pixels[i] = value;
		   			}
		   		}
		   		else if( highBit <= 7)
		   		{
		   			// case bigEndian and unsigned :
		   			// debug("DicomReader.to8PerPix highBit == 7 ");
		   			
		   			for( int i = 0; i < pixels.length; i++)
		   			{
		   				value = ((int)( pixData[(2 * i)] & 0xff ) << 8)| (int)( pixData[(2 * i) + 1] & 0xff); // lsb first
		   				pixels[i] = value;
		   			}
		   		}
		   		
		   		// look for the Max value and minValue
		   		int maxValue = 0;
		   		int minValue = 0xffff;
		   		for ( int i = 0; i < pixels.length; i++)
		   		{
		   			if ( pixels[i] > maxValue)  maxValue = pixels[i];
		   			if ( pixels[i] < minValue ) minValue = pixels[i];
		   		}
		   		
		   		// setUp a new grayScale :
		   		int scale = maxValue - minValue;
		   		
		   		if(scale == 0)
		   		{
		   			scale = 1;
		   			System.out.println("DicomReader.to8PerPix :scale == error ");
		   		}
		   		byte[] destPixels = new byte[w * h];
		   		
		   		for (int i = 0; i < pixels.length; i++)
		   		{
		   			value = ((pixels[i] - minValue ) * 255) / scale;
		   			destPixels[i] = (byte)(value & 0xff);
		   			// pixels[i] = (255 << 24)|( value << 16) | (value << 8)| value;
		   		}
		   		
		   		return destPixels;
		   		
		   		}
		   		
		   		private  byte[] signedTo8PerPix(byte[] pixData)
		   		{
		   			System.out.println("In byte[] signedTo8PerPix(byte[] pixData)");
		   			int[] pixels  = new int[w * h];
		   			short shValue = 0; // dont forget the SIGNED value !!!
		   			int value     = 0;
		   			
		   			// case signed and  littleEndian :
		   			if ( highBit >= 8 )
		   			{
		   				for (int i = 0; i < pixels.length; i++)
		   				{
		   					shValue = (short)(((pixData[(2 * i) + 1] & 0xff ) << 8)| (pixData[(2 * i)] & 0xff)); // msb first !
		   					value   = (int ) shValue;
		   					if(value<0 && ignoreNegValues ) value = 0;
		   					pixels[i] = value;
		   				}
		   			}
		   			
		   			// case signed and  bigEndian :
		   			if ( highBit <= 7 )
		   			{
		   				for (int i = 0; i < pixels.length; i++ )
		   				{
		   					shValue = (short)(((pixData[(2 * i) + 1] & 0xff ) << 8)| (pixData[(2 * i)] & 0xff)); // msb first !
		   					value = (int) shValue;
		   					if(value < 0 && ignoreNegValues ) value = 0;
		   					pixels[i] = value;
		   				}
		   			}
		   			
		   			//look for the Max value and minValue
		   			int maxValue = 0;
		   			int minValue = 0xffff;
		   			for ( int i = 0; i < pixels.length; i++)
		   			{
		   				if ( pixels[i] > maxValue)  maxValue = pixels[i];
		   				if ( pixels[i] < minValue ) minValue = pixels[i];
		   			}
		   			
		   			byte[] destPixels = new byte[w * h];
		   			int scale = maxValue - minValue;
		   			if(scale == 0)
		   			{
		   				scale = 1; 
		   				System.out.println(" Error in VR form SignedTo8..DicomReader");
		   			}
		   			
		   			for (int i = 0; i < pixels.length; i++)
		   			{
		   				value = ((pixels[i] - minValue) * 255) / scale;
		   				// pixels[i] = (255 << 24) | (value << 16) | (value << 8) | value;
		   				destPixels[i] = (byte)(value & 0xff);
		   			}	
		   			
		   			return destPixels;
		   			}
		   			
		   			protected  ColorModel grayColorModel()
		   			{
		   				System.out.println("In ColorModel grayColorModel()");
		   				byte[] r = new byte[256];
		   				
		   				for (int i = 0; i < 256; i++ )
		   				r[i] = (byte)(i & 0xff );
		   				return (new IndexColorModel(8,256,r,r,r));
		   			}
		   			
		   			public void flush()
		   			{
		   				System.out.println("In void flush()");
		   				pixData = null;
		   				System.gc();
		   				System.gc();
		   			}
		   			
		   			void debug(String s)
		   			{
		   				System.out.println("In debug(String s)");
		   				
		   				if(DEBUG) System.out.println(this.getClass().getName() + s);
		   			}
		   		} 