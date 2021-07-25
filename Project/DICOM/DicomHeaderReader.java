import java.awt.*;
import java.util.*;
import java.io.*;

public  class DicomHeaderReader
{
	byte[] data;
	int index;
	private int dataLength;
	private static  final boolean DEBUG = false; 
	private int bytesFromTagToValue; // depends on the VR		
	public static final int MAX_HEADER_SIZE = 10000;
	private int maxHeaderSize;
	public static final String  ImplicitVRLittleEndian 	= "1.2.840.10008.1.2";
	public static final String  ExplicitVRLittleEndian 	= "1.2.840.10008.1.2.1";
	public static final String  ExplicitVRBigEndian   	= "1.2.840.10008.1.2.2";
	public static final String  JPEGCompression 		= "1.2.840.10008.1.2.4.";
	public static final String  RLECompression 			= "1.2.840.10008.1.2.5";
	
	public static final int _ImplicitVRLittleEndian = 0;
	// if the VR is explicit : the tag is > 0
	public static final int _ExplicitVRLittleEndian = 1;
	public static final int _ExplicitVRBigEndian = 2;
	public static final int _ImplicitVRBigEndian = -2; // this one should not exists !
	public static final int _JPEGCompression = 10;
	public static final int _RLECompression = 20;
	public static final int _notUnderstood = -1000;
	
	private  boolean oneSamplePerPixel = true;
	private  boolean oneFramePerFile   = true;
	private int errorDetector = -1;
	
	private String VRString             = "default implicitVR little endian";
	private String transfertSyntaxUID	= "";        //	0x0002,0x0010
	private String imageType		    = "unknown"; //	0x0008,0x0008
	private String studyDate		    = "unknown"; //	0x0008,0x0030	 
	private String modality		        = "unknown"; // 0x0008,0x0060
	private String manufacturer	        = "unknown"; //	0x0008,0x0070
	private String	institution  	    = "unknown"; //	0x0008,0x0080
	private String physician	        = "unknown"; //	0x0008,0x0090
	public static String patientName	= "unknown"; //	0x0010,0x0010
	private String patientID		    = "unknown"; //	0x0010,0x0020
	private String patientBirthdate     = "unknown"; //	0x0010,0x0030
	private String sex				    = "unknown"; //	0x0010,0x0040
	private String slice                = "unknown"; // 0x0018,0x0050	
	private String photo                = "unknown"; // 0x0028,0x0004
	
	private int numberOfFrames		=  1;	// 0x0028,0x0008 // IS : integer string
	private int samplesPerPixel 	=  1;	// 0x0028,0x0002
	private int h 					= -1; 	// 0x0028,0x0010
	private int w 					= -1;	// 0x0028, 0x0011
	private int bitsAllocated		= -1;	// 0x0028, 0x0100
	private int bitsStored 			= -1;	// 0x0028, 0x0101
	private int highBit 			= -1; 	// 0x0028, 0x0102
	private int signed 				= -1;	// 0x0028, 0x0103 (pixelRepresentation)
	private int size 				= -1;	// 0x7Fe0, 0x0010
	private int n 					= -1;	// = 1 or 2 
	private int VR					= _ImplicitVRLittleEndian;  // = 0, default value representation 
	
	
	private boolean bE = false; // bigEndian 	

	// Dicom file you want to read as byte[]. 
	 
	public DicomHeaderReader ( byte [] dicomArray)		
	{
		System.out.println("In Method DicomHeaderReader(byte[])");
	 	this.data = dicomArray;
	 	dataLength =  data.length;
	 	index =0;
	 	initHeaderSize();   // Just read the begining of this file 128 Bytes File Preamble
	 	getVR();            // Look for the VR
	 	getEssentialData();
	}
	 
	//	In order to optimize non DICOM files we define a header, so that we are
	//	not going to search the whole file .
	//	maxHeaderSize is the length of the header we are working with .	 

	protected void initHeaderSize()
	{
		System.out.println("In Method initHeaderSize()");
		maxHeaderSize = MAX_HEADER_SIZE * 1; // increase the speed on non Dicom Files.
		if(maxHeaderSize > dataLength) maxHeaderSize = dataLength;	
	}	
 
	//	One of the main problem with Dicom file is the Value Representation or VR.
	//	Is it a big-endian or a little endian file ?
	//	There is a special tag 0x00020010 that specify it .
	//	Just use the getaString method to get this information
		
	protected void getVR()
	{
        System.out.println("In Method getVR()");
	    // first, let's look at the file to see if the VR is explicitly defined :
	    // by default it is implicit VR little endian
	
	    transfertSyntaxUID	= getaString (0x0002,0x0010, index);

		if(!transfertSyntaxUID.equals("Unknown"))
		{
			
			transfertSyntaxUID = transfertSyntaxUID.trim();
			
			if(transfertSyntaxUID.equals(ImplicitVRLittleEndian)) 
			{
				VR =_ImplicitVRLittleEndian;
			}
			else if(transfertSyntaxUID.equals(ExplicitVRLittleEndian)) 
			{
				VR =_ExplicitVRLittleEndian;	
			}
			else if(transfertSyntaxUID.equals(ExplicitVRBigEndian)) 
			{
				VR =_ExplicitVRBigEndian;	
			}
			else if(transfertSyntaxUID.startsWith(JPEGCompression))
			{
				VR =_JPEGCompression;		
			}
			else if(transfertSyntaxUID.startsWith(RLECompression))
			{
				VR =_RLECompression;		
			}
			else VR = _notUnderstood;
		
			// This switch case is for debugging, it assign a readable name to the VR :

			switch (VR)
			{
				case _ImplicitVRLittleEndian : VRString = "ImplicitVRLittleEndian"; 
											   break;
				case _ExplicitVRLittleEndian : VRString = "ExplicitVRLittleEndian";
											   break;
				case _ExplicitVRBigEndian 	 : VRString = "ExplicitVRBigEndian";
											   break;
				case _JPEGCompression 		 : VRString = "JPEGCompression";
											   break;
				case _RLECompression 		 : VRString = "RLECompression"; 
											   break;
				case _notUnderstood 		 : VRString = "not understood";
											   break;
				default                      : VRString =" Something curious happened !";
			}		
		}
		else if(transfertSyntaxUID.equals("Unknown"))
		{			
			transfertSyntaxUID = "Transfer syntax UID tag not found";
			VRString           = "Default VR implicit little endian";
		} 
	}
	
	 
	//	This method gets the most important data, it is part of the initialization process.
	//	Note that we are searching the file in a growing order
	//	If we don't find an essential data we reset the index to 0 in order
	//	to search again from the beginning.	 

	protected void getEssentialData()
	{
		System.out.println("In Method getEssentialData()");
		
		imageType 		 = getaString (0x0008,0x0008,index);
		studyDate		 = getaString (0x0008,0x0020,index);
		modality		 = getaString (0x0008,0x0060,index);
		manufacturer	 = getaString (0x0008,0x0070,index);
		institution		 = getaString (0x0008,0x0080,index);
		physician		 = getaString (0x0008,0x0090,index);
		patientName		 = getaString (0x0010,0x0010,index);
		patientID		 = getaString (0x0010,0x0020,index);
		patientBirthdate = getaString(0x0010,0x0030,index);
		sex				 = getaString (0x0010,0x0040,index);
        
		// Sample per pixel and number of frame
		h = getAnInt(0x0028, 0x0010, index); // heigth
		w = getAnInt(0x0028, 0x0011, index); // width
		bitsAllocated		= getAnInt(0x0028, 0x0100, index);
		bitsStored 			= getAnInt(0x0028, 0x0101, index);
		highBit 			= getAnInt(0x0028, 0x0102, index);
		signed 				= getAnInt(0x0028, 0x0103, index); // (pixelRepresentation)	        
		slice               = getaString(0x0018, 0x0050);
		photo               = getaString(0x0028, 0x0004);

		//-----------------debugging info:-----------------------------
	
		debug( "TransfertSyntaxUID : " + transfertSyntaxUID);
		debug( "Value representation : " + VRString);
		debug( "ImageType 	" + imageType);
		debug( " h ::: 						" + h);
		debug( " w :::  					" + w);
		debug( " bitsAllocated ::: 			" + bitsAllocated);
		debug( " bitsStored ::: 			" + bitsStored);
		debug( " highBit ::: 				" + highBit);
		debug( " signed ::: 				" + signed);

		// Size consideration	 
		// First we figure a tSize or theoritical size, then we compare it to 
		// the information contained in the Dicom file. 
		// True only if there is no image compression.	 
		 
		// Look for the Pixel Length 's tag position pos:
	
		int pos		= lookForMessagePosition(0x7Fe0, 0x0010, index);
		if( pos != -1)  size  =  readMessageLength(pos + 4);

		// We should include the case where value length is undef 0xFFFFFFFF 

		debug( "\nValueLength for  0x7FEO,0010 tag	 	" + size);
		int HeaderSize =  pos;  // We call header all the information before the pixels
		debug( "\nHeaderSize    : " + HeaderSize);
	

		int tSize       =  samplesPerPixel * w * h * bitsAllocated / 8; // theoritical Size	
		int figuredSize = HeaderSize + 8 + tSize;
		errorDetector   = dataLength - figuredSize;
		debug( "Data Length -  Theoriticaly_figuredSize  : " + errorDetector);
	
		if(errorDetector == 4)
		{
			size = readMessageLength(pos + 8); // try explicit VRString
			errorDetector = dataLength - size;
		}
		
		if( errorDetector != 0)
		{ 
			// see what's wrong :			
			// 1 more than one sample per pixel :	
			samplesPerPixel = getAnInt(0x0028, 0x0002); // not mandatory ?

			if(samplesPerPixel <0 || samplesPerPixel> 3)
			{
				samplesPerPixel = 1; // default value
				debug( " SamplesPerPixel ::: 	" + samplesPerPixel);
			}
			else if (samplesPerPixel == 1) oneSamplePerPixel = true;
			//	else oneSamplePerPixel = false;	
		
			// 2 more than one Frame :	
			try
			{
				numberOfFrames = Integer.parseInt(getaString(0x0028,0x0008));
			}
			catch(NumberFormatException nFE)
			{
				numberOfFrames = 1;
			}

			if (numberOfFrames > 1) oneFramePerFile = false;
			tSize = numberOfFrames * tSize * samplesPerPixel; 
			figuredSize = HeaderSize + 8 + tSize;
			errorDetector = dataLength - figuredSize;
			
			//3 other cases :
			if (VR == _JPEGCompression) 
				debug( "_JPEGCompression , can't read that file");		
			
			if (VR == _RLECompression) 
				debug( " RLECompression , can't read that file");
			
			debug( "Byte difference between figured sized and size tag: "	+ errorDetector 
					+ "\n Frame per file  " + numberOfFrames 
					+ "\n samplesPerPixel "  + samplesPerPixel);
		}
	}

	private  int lookForMessagePosition(int groupElement, int dataElement)
	{
		System.out.println("In Method lookForMessagePosition(int,int)");
		return lookForMessagePosition(groupElement, dataElement, 0);
	}
	
	
	private  int lookForMessagePosition(int groupElement, int dataElement, int  j)
	{
		System.out.println("In Method lookForMessagePosition(int,int,int)");
		
		int LenMax = data.length - 3; // -3 because we don't want to have an arrayOutOfBounds exception 
	 	boolean found = false;

	 	byte testByte1 = (byte) (groupElement & 0xff);
	 	byte testByte2 = (byte) ((groupElement & 0xff00) >> 8);
	 	byte testByte3 = (byte) (dataElement & 0xff);
	 	byte testByte4 = (byte) ((dataElement & 0xff00) >> 8);
	 	
		debug(" Looking for :" + Integer.toHexString(testByte1)
				+ Integer.toHexString(testByte2) + ", "
				+ Integer.toHexString(testByte3) 
				+ Integer.toHexString(testByte4));
		
		for (;  j < LenMax || found; j++)
		{
	 		if( ( data[j] == testByte1) && (data[j + 1] == testByte2) && (data[j + 2] == testByte3) && 
																			(data[j + 3] == testByte4))
			{
	 			found = true;
	 			return j;
	 		}
		}

		return -1;
	}	

	private int readMessageLength(int i)
	{
		System.out.println("In Method readMessageLength(int)");
		int i0 = (int) (data[i ] & 0xff);
	 	int i1 =	(int) (data[i + 1 ] & 0xff);
	 	int i2 =	(int) (data[i + 2 ] & 0xff);
	 	int i3 =	(int) (data[i + 3 ] & 0xff);

		return i3 << 24 | i2 << 16 |i1 << 8|i0;	
	}
		
	private int readVRMessageLength(int tagPos)
	{
		System.out.println("In Method readVRMessageLength(int)");
		if(VR == _ImplicitVRLittleEndian)
		{
			bytesFromTagToValue = 8;
			return readInt32(tagPos + 4);
		}
		// case  explicit VR with of OB OW SQ or UN 
		String VRTypeOf = new String(new byte[]{data[tagPos + 4],data[tagPos + 5]});	
		debug ("VR type of : " + VRTypeOf);

		if( VRTypeOf.equals("OB") | VRTypeOf.equals("OW") | VRTypeOf.equals("SQ") | VRTypeOf.equals("UN"))
		{
			bytesFromTagToValue = 12;

			return readInt32(tagPos + 8);
		}	  	 	
	 	else // case  explicit VR with value other than OB OW SQ or UN  
		{
	 		bytesFromTagToValue = 8;
	 		return	readInt16(tagPos + 6);
		}
	}
	
	private int readInt32(int i) 
	{ 
	    System.out.println("In Method readInt32(int)");
		// = return readMessageLength(i); 
		int i0 = 	(int) (data[i ] & 0xff);
	 	int i1 =	(int) (data[i + 1 ] & 0xff);
	 	int i2 =	(int) (data[i + 2 ] & 0xff);
	 	int i3 =	(int) (data[i + 3 ] & 0xff);
		return i3 << 24 | i2 << 16 | i1 << 8 | i0;	

	}
	
	private int readInt16( int i)
	{
	 	System.out.println("In Method readInt16(int)");	
	 	int  i1 = data[i + 1] & 0xff;
	 	int  i0 = data[i] & 0xff;	
	 	int anInt =  i1 << 8|i0;

	    // case BE swap bytes :

	 	if (anInt < -1)
		{ 
			anInt= (int)(data[i] * 256) & 0xff + data[i + 1] & 0xff;
	 	  	debug("Byte swapped at readInt16 :" + anInt);
	 	}

		return anInt;
	}
	
	private void skip (int length)
	{
		System.out.println("In Method skip(int)");
		index += length;
	}
	
	 
	// This method get an integer if you give it the tags. 

	public int  getAnInt(int groupElement, int dataElement)
	{
		System.out.println("In Method getAnInt(int,int)");
		return getAnInt( groupElement, dataElement, 0);
	}
	
	private int  getAnInt(int groupElement, int dataElement, int j)
	{
		System.out.println("In Method getAnInt(int,int,int)");
		int pos = lookForMessagePosition( groupElement, dataElement, j);

		if(pos < maxHeaderSize && pos != -1)
		{
			index = pos;

			if (readVRMessageLength(pos) == 2)
			{
					return readInt16( pos + bytesFromTagToValue);
			}
			else if(readVRMessageLength(pos) == 4)
			{
					return readInt32(pos + bytesFromTagToValue);
			}
			else return -1; // case undef FFFF 
		} 

		else return -1;	
	}


	 
	//	Here are some methods to retrieve the major  datas.
	//	Notice that you should allways check whether the width, the height of the image, and the bitsAllocated are
	// 	consistent with the size of the file .	 

	public int  getSize()
	{ 
	   System.out.println("In Method getSize()");
		return dataLength;
	}

	public int getNumberOfFrames()
	{ 
	    System.out.println("In Method getNumberOfFrames()");
		return numberOfFrames;
	}

	public int getSamplesPerPixels() 
	{ 
	   System.out.println("In Method getSamplePerPixels()");
		return samplesPerPixel;
	}

	public int getPixelDataSize()
	{ 
	    System.out.println("In Method getPixelDataSize()");
		if (errorDetector == 0) 
		{
			return size;
		}

		else return -1;
	}
		
	//	The  height    
	public int  getRows()
	{
		System.out.println("In Method getRows()");
		return h;
	}

	//	The width 		 
	public int  getColumns()
	{
		System.out.println("In Method getColumns()");
		return w;
	}	

	// The bits Allocated  per pixel of image 
	public int  getBitAllocated()
	{
		System.out.println("In Method getBitAllocated()");
		return bitsAllocated;
	}	

	// The bits stored per pixel of image  
	public int  getBitStored()
	{
		System.out.println("In Method getBitStored()");
		return bitsStored;
	}	

	// Other values  
	public int  getHighBit()
	{
		System.out.println("In Method getHighBit()");
		return	highBit;
	}

	public int  getSamplesPerPixel()
	{
		System.out.println("In Method getSamples()");
		return	samplesPerPixel;
	}

	public int  getPixelRepresentation()
	{
		System.out.println("In Method getPixelRepresentation()");
		return	signed;
	}
	
	public static String  getPatientName()
	{ 	
	   System.out.println("In Method getPatientName()");
		return patientName; 	
	}

	public String  getPatientBirthdate() 
	{
		System.out.println("In Method getPatientBirthdate()");
		return patientBirthdate;
	}

	public String  getManufacturer()
	{
		System.out.println("In Method getManufacturer()");
		return manufacturer;	
    }

	public String  getPatientID()
	{
		System.out.println("In Method getPatientID()");
		return patientID;
	}

	public String  getImageType()
	{
		System.out.println("In Method getImageType()");
		return imageType; 	
	}
	
	public String  getStudyDate()
	{ 	
	    System.out.println("In Method getStudyDate()");	
		return studyDate; 	
	}

	public String  getModality()
	{
		System.out.println("In Method getModality()");
		return modality; 
	}
	
	// Retrieves a String when you knows the tags  

	public String  getaString(int groupNumber, int elementNumber)
	{
		System.out.println("In Method getaString(int,int)");
		return getaString( groupNumber, elementNumber, 0);
	}
	
	private String  getaString(int groupNumber, int elementNumber, int j)
	{
		System.out.println("In Method getaString(int,int,int)");
		
		int pos = lookForMessagePosition(groupNumber,elementNumber,j);
		
		
		if(pos < MAX_HEADER_SIZE && pos != -1)
		{
			int length = readMessageLength(pos + 4);
			if (length > 256)length = readInt16( pos + 6);
			if (length > 64 || length < 0) length = 64;
			if (length > (dataLength - pos -8)) length = dataLength - pos -9;

			index = pos;
			pos += 8;

			char[] result = new char[length ];
			
			for (int i = 0; i < length; i++)
				result[i] = (char)data[pos++];
			return new String( result);
		}

		else return "Unknown";	
	}
		 
	//	This is a dangerous method because it can lead you to an array of bounds exception !
	// 	check wether it is consistent with the file data length, the width, height and bit allocated of the image
	//	getFileDataLength return the length of the pixel data. 	 
	
    public int  getFileDataLength()
    {
    	System.out.println("In Method getFileDataLength()");
    	
   		int pos = lookForMessagePosition( 0x7Fe0, 0x0010);

   		if ( pos != -1)
	    {
			return  readMessageLength(pos + 4);
	    }
   		else return -1;
	 }
				
	// This method return the pixels of the Dicom image  provided it is not compressed and this is a known 
	// Dicom format, else it return null (check for a null condition  or an index array out of bounds exception).
	
	public byte[] getPixels() throws IOException
	{	
	    System.out.println("In byte[] getPixels()");
		if (VR == _JPEGCompression) 
		{
			//throw new IOException("DICOM JPEG compression not yet supported ");	
			return new byte[1];
		}
		
		int w = getRows();

		if ( w ==  -1)
		{
			throw new IOException("Format not recognized");	
				
			 //return null;
		}

		int h = getColumns();
		if( h == -1) throw new IOException("Format not recognized");
		
		int ba = getBitAllocated();

		if( ba%8 == 0)
		{
			ba = ba/8;
		}
		else ba = (ba + 8)/8;

		int fileLength = w * h * ba;
		int offset = dataLength - fileLength; // ???
		//  should be this one : //int pos = lookForMessagePosition( 0x7Fe0, 0x0010) + 8;
		//	if(VR == _ExplicitVRLittleEndian || VR == _ExplicitVRBigEndian)
		//	offset += 4;

		byte[]  pixData = new byte[ fileLength ];
		java.lang.System.arraycopy (data, offset, pixData,0, fileLength);	

		return pixData;	
	}
	
	public byte[] getPixels(int number) throws IOException
	{		
	    System.out.println("In byte[] getPixels(int)");
	
		if( number > numberOfFrames) throw new IOException( "Doesn't have such a frame ! ");

		if (VR == _JPEGCompression) 
		{
			//throw new IOException("DICOM JPEG compression not yet supported ");
			return new byte[1];
		}
			
		int w = getRows();

		if ( w ==  -1)
		{
			throw new IOException("Format not recognized");		
			 // return null;
		}

		int h = getColumns();
		if( h == -1) throw new IOException("Format not recognized");
		
		int ba = getBitAllocated();
		if( ba%8 == 0)
		{
			ba = ba / 8;
		}
		else ba = (ba + 8) / 8;

		int fileLength = w * h * ba;
		int offset = dataLength - (fileLength * number); 
		// should be this one : //int pos = lookForMessagePosition( 0x7Fe0, 0x0010) + 8;
		byte[]  pixData = new byte[ fileLength ];
		java.lang.System.arraycopy (data, offset, pixData,0, fileLength);	

		return pixData;	
	}
		
	public String[] getInfo()
	{
		System.out.println("In String[] getInfo()");
		String [] info = new String[18]; 

		info[0]  = "Patient 's name              : " + getPatientName();
		info[1]  = "Patient 's ID                : " + getPatientID();
		info[2]  = "Patient 's birthdate         : " + getPatientBirthdate();
		info[3]  = "Patient 's sex               : " + sex;
		info[4]  = "Study Date                   : " + getStudyDate();
		info[5]  = "Modality                     : " + getModality();
		info[6]  = "Manufacturer                 : " + getManufacturer();
		info[7]  = "Number of frames             : " + getNumberOfFrames();
		info[8]  = "Width                        : " + getColumns();
		info[9]  = "Height                       : " + getRows();
		info[10] = "Bits allocated               : " + getBitAllocated();
		info[11] = "Bits stored                  : " + getBitStored();
		info[12] = "Sample per pixels            : " + getSamplesPerPixel();
		info[13] = "Physician                    : " + physician;
		info[14] = "Institution                  : " + institution;
		info[15] = "Transfert syntax UID         : " + transfertSyntaxUID;
		info[16] = "Slice                        : " + slice;
		info[17] = "Photometric Interpretation   : " + photo;
	
		return info;
	}

// for graphical textual information //
public String[] getHeaderInfo()
	{
		System.out.println("In String[] getHeader()");
		String [] info = new String[18]; 

		info[0]  = "" + getPatientName();
		info[1]  = "" + getPatientID();
		info[2]  = "" + getPatientBirthdate();
		info[3]  = "" + sex;
		info[4]  = "" + getStudyDate();
		info[5]  = "" + getModality();
		info[6]  = "" + getManufacturer();
		info[7]  = "" + getNumberOfFrames();
		info[8]  = "" + getColumns();
		info[9]  = "" + getRows();
		info[10] = "" + getBitAllocated();
		info[11] = "" + getBitStored();
		info[12] = "" + getSamplesPerPixel();
		info[13] = "" + physician;
		info[14] = "" + institution;
		info[15] = "" + transfertSyntaxUID;
		info[16] = "" + slice;
		info[17] = "" + photo;
	
		return info;
	} 

	public Hashtable getMedicalInfos()
	{
		System.out.println("In Hashtable getMedicalInfos()");
		
		Hashtable table = new Hashtable(8);		
		table.put("patient.name",getPatientName());
		table.put("patient.id",getPatientID());
		table.put("patient.birthdate",getPatientBirthdate());
		table.put("sex",sex);
		table.put("study.date",getStudyDate());
		table.put("physician",physician);
		table.put("institution",institution);
		table.put("transfert.syntax.uid",transfertSyntaxUID);

		return table;
	}	

	private void  debug(String message)
	{
		System.out.println("In debug(String)");
		if (DEBUG)
			System.out.println(message);	
	}
} 