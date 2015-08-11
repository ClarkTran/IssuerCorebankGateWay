/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/

package com.transinfo.tplus.messaging.parser;

import java.io.FileInputStream;
import java.io.InputStream;

import org.jpos.iso.packager.GenericPackager;

import com.transinfo.tplus.TPlusException;

public class TestParser
{
 	/**
     * Test harness for GenericPackager
     *
     * <pre>
     * Takes 2 arguments
     * args[0] = xml field description file
     * args[1] = file containing a hex dump of the message to parse
     * </pre>
     */

    public static void main(String[] args) throws TPlusException,Exception
    {

        GenericPackager p = new GenericPackager("file:\\C:\\Satya\\Download\\Software\\JPos\\jpos-1.4.8\\jpos\\src\\config\\packager\\base1.xml");
        InputStream in = new FileInputStream("C:\\Satya\\Download\\Software\\JPos\\jpos-1.4.8\\jpos\\src\\examples\\genericpackager\\emvstress.txt");


        int nbytes = in.available();
        // Read in input file and strip any training white space
        byte[] hexbytes = new byte[nbytes];
        in.read (hexbytes, 0, nbytes);
        while (Character.isWhitespace ((char)hexbytes[nbytes-1]))
            nbytes--;

		String strISO = new String(hexbytes);
		System.out.println("Hex String Test1"+strISO);

		/*TPlusISOParser objISO = ISOParserFactory.createObject(1);
		objISO.parse(strISO);
		System.out.println(objISO.getValue(3));
		System.out.println(objISO.getCardNumber());

		System.out.println(objISO.getValue(55));
		System.out.println(objISO.getSubFieldValue(55));
		System.out.println(objISO.getSubFieldTagValue(55,"82"));

		TPlusISOParser objISO = ISOParserFactory.createObject(2);
		objISO.parse("VSALE     0000010501872109950001145418220015641240606123 000000488800000002000000        ");
		byte b[] =objISO.repack();
		System.out.println(ISOUtil.hexString(b));*/

		/*IParser objISO = ISOParserFactory.createObject(3);
		objISO.parse("5F2A02070282025C008407A00000000310109A030402209C01009F02060000000000059F03060000000000009F0902008C9F1A0207029F26084CB9C2891C2A434A9F2701809F3303E020C89F3501229F360200C99F37049732D85A5F3401019F34031E03009F100706990A03A0A00095050000008000");
		//byte b[] =objISO.repack();
		System.out.println(ISOUtil.hexString(b));
		System.out.println("Hello");
		System.out.println(objISO.getSubFieldValue(55));
		System.out.println(objISO.getSubFieldTagValue(55,"82"));*/



    }

}

