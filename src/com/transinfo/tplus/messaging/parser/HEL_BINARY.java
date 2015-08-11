package com.transinfo.tplus.messaging.parser;
import org.jpos.iso.BcdPrefixer;
import org.jpos.iso.ISOBinaryFieldPackager;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.LiteralBinaryInterpreter;

//Packager for VSDC field
//1 byte Hexadecimal length + up to 255 byte (510 HEX char) data

public class HEL_BINARY extends ISOBinaryFieldPackager
{
  private int length;
  public HEL_BINARY()
  {
	  super(255, "Chip Data", LiteralBinaryInterpreter.INSTANCE, BcdPrefixer.LLL);
	      System.out.println("F55Length=255");
	      this.length = 255;
   		 checkLength(255, 999);
  }

  public HEL_BINARY(int len, String description)
  {

    super(len, description, LiteralBinaryInterpreter.INSTANCE, BcdPrefixer.LLL);
    System.out.println("F55Length="+len);
    this.length = len;
    checkLength(len, 999);
  }

  public byte[] pack(ISOComponent c) throws ISOException
  {
	  System.out.println("In PACK");
    byte[] data = (byte[])c.getValue();
    System.out.println("Pack F55="+ISOUtil.hexString(data));
    String HEXData = Integer.toHexString(data.length)+ISOUtil.hexString(data);
    return ISOUtil.hex2byte(HEXData);
  }

  public int unpack(ISOComponent c, byte[] b, int offset) throws ISOException
  {
	  System.out.println("unpack F55="+ISOUtil.hexString(b));
    int length = Integer.parseInt(ISOUtil.hexString(new byte[]{b[offset]}),16);
    byte[] unpacked = new byte[length];
    System.arraycopy(b,offset+1,unpacked,0,length);
    c.setValue(unpacked);
    return offset+length;
  }

  public int getLength()
  {
    // TODO:  Override this org.jpos.iso.ISOFieldPackager method
    return length+1;
  }
}