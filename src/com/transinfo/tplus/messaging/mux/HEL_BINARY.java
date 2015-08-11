// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HEL_BINARY.java

package com.transinfo.tplus.messaging.mux;

import org.jpos.iso.BcdPrefixer;
import org.jpos.iso.ISOBinaryFieldPackager;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.LiteralBinaryInterpreter;

public class HEL_BINARY extends ISOBinaryFieldPackager
{

    public HEL_BINARY()
    {
        super(255, "Chip Data", LiteralBinaryInterpreter.INSTANCE, BcdPrefixer.LLL);
//        System.out.println("F55Length=255");
        length = 255;
        checkLength(255, 999);
    }

    public HEL_BINARY(int i, String s)
    {
        super(i, s, LiteralBinaryInterpreter.INSTANCE, BcdPrefixer.LLL);
        System.out.println((new StringBuilder()).append("F55Length=").append(i).toString());
        length = i;
        checkLength(i, 999);
    }

    public byte[] pack(ISOComponent isocomponent)
        throws ISOException
    {
        System.out.println("In PACK");
        byte abyte0[] = (byte[])(byte[])isocomponent.getValue();
        System.out.println((new StringBuilder()).append("Pack F55=").append(ISOUtil.hexString(abyte0)).toString());
        String s = "";
        System.out.println((new StringBuilder()).append("Data Length ").append(abyte0.length).toString());
        if(abyte0.length < 9)
            s = (new StringBuilder()).append("0").append(Integer.toHexString(abyte0.length)).append(ISOUtil.hexString(abyte0)).toString();
        else
            s = (new StringBuilder()).append(Integer.toHexString(abyte0.length)).append(ISOUtil.hexString(abyte0)).toString();
        System.out.println((new StringBuilder()).append("HexData=").append(s).append("    ").append(s.length()).toString());
        return ISOUtil.hex2byte(s);
    }

    public int unpack(ISOComponent isocomponent, byte abyte0[], int i)
        throws ISOException
    {
        System.out.println((new StringBuilder()).append("unpack F55=").append(ISOUtil.hexString(abyte0)).toString());
        int j = Integer.parseInt(ISOUtil.hexString(new byte[] {
            abyte0[i]
        }), 16);
        System.out.println("Unpack from -> to "+i+" "+j);
        byte abyte1[] = new byte[j];
        System.arraycopy(abyte0, i + 1, abyte1, 0, j);
      //  System.out.println((new StringBuilder()).append("unpack F55=").append(ISOUtil.hexString(abyte1)).append("///////  ").append(i).append(j).toString());
        isocomponent.setValue(abyte1);
        return j + 1;
    }

    public int getLength()
    {
        return length + 1;
    }

    private int length;
}
