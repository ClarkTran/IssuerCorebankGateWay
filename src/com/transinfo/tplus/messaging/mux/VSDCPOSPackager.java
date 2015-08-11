package com.transinfo.tplus.messaging.mux;
import org.jpos.iso.IFB_AMOUNT;
import org.jpos.iso.IFB_BINARY;
import org.jpos.iso.IFB_BITMAP;
import org.jpos.iso.IFB_LLCHAR;
import org.jpos.iso.IFB_LLHBINARY;
import org.jpos.iso.IFB_LLHECHAR;
import org.jpos.iso.IFB_LLHNUM;
import org.jpos.iso.IFB_LLLBINARY;
import org.jpos.iso.IFB_LLLCHAR;
import org.jpos.iso.IFB_LLNUM;
import org.jpos.iso.IFB_NUMERIC;
import org.jpos.iso.IFE_CHAR;
import org.jpos.iso.IFE_LLLCHAR;
import org.jpos.iso.IF_CHAR;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOFieldPackager;
import org.jpos.iso.ISOMsgFieldPackager;
import org.jpos.iso.RightPadder;
import org.jpos.iso.packager.Base1SubFieldPackager;
import org.jpos.iso.packager.Base1_BITMAP126;

//source copied from org.jpos.iso.Base1Packager
//modify field 55,60 and 62 to comply to Visa VSDC format
public class VSDCPOSPackager extends ISOBasePackager
{
  private static final boolean pad = true;
  protected ISOFieldPackager VSDCFld[] =
    {
/*000*/ new IFB_NUMERIC (  4, "MESSAGE TYPE INDICATOR", true),
/*001*/ new IFB_BITMAP  ( 16, "BIT MAP"),
/*002*/ new IFB_LLNUM  ( 19, "PAN - PRIMARY ACCOUNT NUMBER", pad),
/*003*/ new IFB_NUMERIC (  6, "PROCESSING CODE", true),
/*004*/ new IFB_NUMERIC ( 12, "AMOUNT, TRANSACTION", true),
/*005*/ new IFB_NUMERIC ( 12, "AMOUNT, SETTLEMENT", true),
/*006*/ new IFB_NUMERIC ( 12, "AMOUNT, CARDHOLDER BILLING", true),
/*007*/ new IFB_NUMERIC ( 10, "TRANSMISSION DATE AND TIME", true),
/*008*/ new IFB_NUMERIC (  8, "AMOUNT, CARDHOLDER BILLING FEE", true),
/*009*/ new IFB_NUMERIC (  8, "CONVERSION RATE, SETTLEMENT", true),
/*010*/ new IFB_NUMERIC (  8, "CONVERSION RATE, CARDHOLDER BILLING", true),
/*011*/ new IFB_NUMERIC (  6, "SYSTEM TRACE AUDIT NUMBER", true),
/*012*/ new IFB_NUMERIC (  6, "TIME, LOCAL TRANSACTION", true),
/*013*/ new IFB_NUMERIC (  4, "DATE, LOCAL TRANSACTION", true),
/*014*/ new IFB_NUMERIC (  4, "DATE, EXPIRATION", true),
/*015*/ new IFB_NUMERIC (  4, "DATE, SETTLEMENT", true),
/*016*/ new IFB_NUMERIC (  4, "DATE, CONVERSION", true),
/*017*/ new IFB_NUMERIC (  4, "DATE, CAPTURE", true),
/*018*/ new IFB_NUMERIC (  4, "MERCHANTS TYPE", true),
/*019*/ new IFB_NUMERIC (  3, "ACQUIRING INSTITUTION COUNTRY CODE", true),
/*020*/ new IFB_NUMERIC (  3, "PAN EXTENDED COUNTRY CODE", true),
/*021*/ new IFB_NUMERIC (  3, "FORWARDING INSTITUTION COUNTRY CODE", true),
/*022*/ new IFB_NUMERIC (  4, "POINT OF SERVICE ENTRY MODE", false),
/*023*/ new IFB_NUMERIC (  3, "CARD SEQUENCE NUMBER", true),
/*024*/ new IFB_NUMERIC (  3, "NETWORK INTERNATIONAL IDENTIFIEER", true),
/*025*/ new IFB_NUMERIC (  2, "POINT OF SERVICE CONDITION CODE", true),
/*026*/ new IFB_NUMERIC (  2, "POINT OF SERVICE PIN CAPTURE CODE", true),
/*027*/ new IFB_NUMERIC (  1, "AUTHORIZATION IDENTIFICATION RESP LEN",true),
/*028*/ new IFB_AMOUNT  (  9, "AMOUNT, TRANSACTION FEE", true),
/*029*/ new IFB_AMOUNT  (  9, "AMOUNT, SETTLEMENT FEE", true),
/*030*/ new IFB_AMOUNT  (  9, "AMOUNT, TRANSACTION PROCESSING FEE", true),

/*031*/ new IFB_AMOUNT  (  9, "AMOUNT, SETTLEMENT PROCESSING FEE", true),
/*032*/ new IFB_LLNUM  ( 11, "ACQUIRING INSTITUTION IDENT CODE", pad),
/*033*/ new IFB_LLNUM  ( 11, "FORWARDING INSTITUTION IDENT CODE", pad),
/*034*/ new IFB_LLCHAR  ( 28, "PAN EXTENDED"),
/*035*/ new IFB_LLNUM  ( 37, "TRACK 2 DATA", pad),
/*036*/ new IFB_LLLCHAR  (104, "TRACK 3 DATA"),
/*037*/ new IF_CHAR    ( 12, "RETRIEVAL REFERENCE NUMBER"),
/*038*/ new IF_CHAR    (  6, "AUTHORIZATION IDENTIFICATION RESPONSE"),
/*039*/ new IF_CHAR    (  2, "RESPONSE CODE"),
/*040*/ new IF_CHAR    (  3, "SERVICE RESTRICTION CODE"),
/*041*/ new IF_CHAR    (  8, "CARD ACCEPTOR TERMINAL IDENTIFICACION"),
/*042*/ new IF_CHAR    ( 15, "CARD ACCEPTOR IDENTIFICATION CODE" ),
/*043*/ new IF_CHAR    ( 40, "CARD ACCEPTOR NAME/LOCATION"),
/*044*/ new IFB_LLCHAR( 25, "ADITIONAL RESPONSE DATA"),
/*045*/ new IFB_LLCHAR( 76, "TRACK 1 DATA"),
/*046*/ new IFB_LLCHAR  ( 99, "ADITIONAL DATA - ISO"),
/*047*/ new IFB_LLCHAR  ( 99, "ADITIONAL DATA - NATIONAL"),
/*048*/ new IFB_LLHBINARY ( 99, "ADITIONAL DATA - PRIVATE"),
/*049*/ new IFB_NUMERIC (  3, "CURRENCY CODE, TRANSACTION", true),
/*050*/ new IFB_NUMERIC (  3, "CURRENCY CODE, SETTLEMENT", true),
/*051*/ new IFB_NUMERIC (  3, "CURRENCY CODE, CARDHOLDER BILLING", true),
/*052*/ new IFB_BINARY  (  8, "PIN DATA"   ),
/*053*/ new IFB_NUMERIC ( 8, "SECURITY RELATED CONTROL INFORMATION", true),
/*054*/ new IFB_LLCHAR  (99, "ADDITIONAL AMOUNTS"),
/*055*/ new IFB_LLLBINARY (255, "CHIP DATA"),

/*055*/ //new HEL_BINARY (255, "CHIP DATA"),
/*056*/ new IFB_LLLCHAR  ( 999, "RESERVED ISO"),
/*057*/ new IFB_LLLCHAR  ( 999, "RESERVED NATIONAL"),
/*058*/ new IFB_LLLCHAR  ( 999, "RESERVED NATIONAL"),
/*059*/ new IFE_LLLCHAR ( 999, "NATIONAL POS GEOGRAPHIC DATA"),
/*060*/ new IFB_LLLCHAR  (999, "RESERVED PRIVATE"),
/*060*/ //new IFB_LLCHAR   (  5, "ADDITIONAL POS INFORMATION"),
/*061*/ new IFB_LLLCHAR ( 999, "RESERVED PRIVATE"),
///*062*/ new IFB_LLHBINARY( 59, "PAYMENT SERVICE FIELDS"),
/*062*/ new IFB_LLLCHAR  ( 999, "CUSTOM PAYMENT SERVICE"),
/*063*/ new IFB_LLLCHAR(255, "SMS PRIVATE-USE FIELDS"),
/*063*/ // new ISOMsgFieldPackager(
        //    new IFB_LLHBINARY (10, "Field 63"),
        //    new F63Packager()),
/*064*/ new IFB_BINARY  (  8, "MESSAGE AUTHENTICATION CODE FIELD"),
/*065*/ new IFB_BINARY  (  1, "BITMAP, EXTENDED"),
/*066*/ new IFB_NUMERIC (  1, "SETTLEMENT CODE", true),
/*067*/ new IFB_NUMERIC (  2, "EXTENDED PAYMENT CODE", true),
/*068*/ new IFB_NUMERIC (  3, "RECEIVING INSTITUTION COUNTRY CODE", true),
/*069*/ new IFB_NUMERIC (  3, "SETTLEMENT INSTITUTION COUNTRY CODE", true),
/*070*/ new IFB_NUMERIC (  3, "NETWORK MANAGEMENT INFORMATION CODE", true),
/*071*/ new IFB_NUMERIC (  4, "MESSAGE NUMBER", true),
/*072*/ new IFB_NUMERIC (  4, "MESSAGE NUMBER LAST", true),
/*073*/ new IFB_NUMERIC (  6, "DATE ACTION", true),
/*074*/ new IFB_NUMERIC ( 10, "CREDITS NUMBER", true),
/*075*/ new IFB_NUMERIC ( 10, "CREDITS REVERSAL NUMBER", true),
/*076*/ new IFB_NUMERIC ( 10, "DEBITS NUMBER", true),
/*077*/ new IFB_NUMERIC ( 10, "DEBITS REVERSAL NUMBER", true),
/*078*/ new IFB_NUMERIC ( 10, "TRANSFER NUMBER", true),
/*079*/ new IFB_NUMERIC ( 10, "TRANSFER REVERSAL NUMBER", true),
/*080*/ new IFB_NUMERIC ( 10, "INQUIRIES NUMBER", true),
/*081*/ new IFB_NUMERIC ( 10, "AUTHORIZATION NUMBER", true),
/*082*/ new IFB_NUMERIC ( 12, "CREDITS, PROCESSING FEE AMOUNT", true),
/*083*/ new IFB_NUMERIC ( 12, "CREDITS, TRANSACTION FEE AMOUNT", true),
/*084*/ new IFB_NUMERIC ( 12, "DEBITS, PROCESSING FEE AMOUNT", true),
/*085*/ new IFB_NUMERIC ( 12, "DEBITS, TRANSACTION FEE AMOUNT", true),
/*086*/ new IFB_NUMERIC ( 16, "CREDITS, AMOUNT", true),
/*087*/ new IFB_NUMERIC ( 16, "CREDITS, REVERSAL AMOUNT", true),
/*088*/ new IFB_NUMERIC ( 16, "DEBITS, AMOUNT", true),
/*089*/ new IFB_NUMERIC ( 16, "DEBITS, REVERSAL AMOUNT", true),
/*090*/ new IFB_NUMERIC ( 42, "ORIGINAL DATA ELEMENTS", false),
/*091*/ new IFE_CHAR    (  1, "FILE UPDATE CODE"),
/*092*/ new IFE_CHAR    (  2, "FILE SECURITY CODE"),
/*093*/ new IFE_CHAR    (  5, "RESPONSE INDICATOR"),
/*094*/ new IFE_CHAR    (  7, "SERVICE INDICATOR"),
/*095*/ new IFE_CHAR    ( 42, "REPLACEMENT AMOUNTS"),
/*096*/ new IFB_BINARY  (  8, "MESSAGE SECURITY CODE"),
/*097*/ new IFB_AMOUNT  ( 17, "AMOUNT, NET SETTLEMENT", pad),
/*098*/ new IFE_CHAR    ( 25, "PAYEE"),
/*099*/ new IFB_LLHNUM  ( 11, "SETTLEMENT INSTITUTION IDENT CODE", pad),
/*100*/ new IFB_LLHNUM  ( 11, "RECEIVING INSTITUTION IDENT CODE", pad),
/*101*/ new IFB_LLHECHAR( 17, "FILE NAME"),
/*102*/ new IFB_LLCHAR  ( 28, "ACCOUNT IDENTIFICATION 1"),
/*103*/ new IFB_LLCHAR  ( 28, "ACCOUNT IDENTIFICATION 2"),
/*104*/ new IFB_LLCHAR  ( 99, "TRANSACTION DESCRIPTION"),
/*105*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*106*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*107*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*108*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*109*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*110*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*111*/ new IFB_LLCHAR  ( 99, "RESERVED ISO USE"),
/*112*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*113*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*114*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*115*/ new IFB_LLCHAR  ( 24, "ADDITIONAL TRACE DATA 1"),
/*116*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*117*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*118*/ new IFB_LLHBINARY( 99, "INTRA-COUNTRY DATA"),
/*119*/ new IFB_LLCHAR  ( 99, "RESERVED NATIONAL USE"),
/*120*/ new IFB_LLHNUM  (  4, "RESERVED PRIVATE USE", pad),
/*121*/ new IFB_LLCHAR  ( 11, "ISSUING INSTITUTION IDENT CODE"),
/*122*/ new IFB_LLCHAR  ( 13, "REMAINING OPEN-TO-USE"),
/*123*/ new IFB_LLCHAR  ( 29, "ADDRESS VERIFICATION DATA"),
/*124*/ new IFB_LLCHAR  ( 99, "FREE-FORM TEXT-JAPAN"),
/*125*/ new IFB_LLCHAR  ( 99, "SUPPORTING INFORMATION"),
/*126*/ new ISOMsgFieldPackager(
            new IFB_LLHBINARY (255, "Field 126"),
            new F126Packager()),
/*127*/ new ISOMsgFieldPackager(
            new IFB_LLHBINARY (255, "FILE RECORD(S) ACTION/DATA"),
            new F127Packager()),
/*128*/ new IFB_BINARY  (  8, "MAC 2")
/*
//Added by TIVN
/*129 new IFB_LLCHAR  ( 99, "RESERVED"),
/*130 new IFB_BINARY  (  3, "TERMINAL CAPABILITY PROFILE"),
        new IFB_BINARY  (  5, "TERMINAL VERIFICATION RESULTS"),
        new IFB_BINARY  (  4, "UNPREDICTABLE NUMBER"),
        new IFE_CHAR    (  8, "TERMINAL SERIAL NUMBER"),
        new IFB_LLCHAR  ( 16, "VISA DISCRETIONARY DATA"),
/*135 new IFB_LLHECHAR( 30, "ISSUER DISCRETIONARY DATA"),
        new IFB_BINARY  (  8, "CRYPTOGRAM"),
        new IFB_BINARY  (  2, "APPLICATION TRANSACTION COUNTER"),
        new IFB_BINARY  (  2, "APPLICATION INTERCHANGE PROFILE"),
        new IFB_BINARY  ( 10, "ARPC RESPONSE CRYPTOGRAM AND CODE")
*/
    };



    protected class F63Packager extends Base1SubFieldPackager
    {
        protected ISOFieldPackager fld63[] =
        {
            new IFB_BITMAP(3, "Bit Map"),
            new IFB_NUMERIC(4, "Network ID Code",false),
            new IFB_NUMERIC(4, "Network ID Code",false),
            new IFB_NUMERIC(4, "Network ID Code",false),
            new IFB_NUMERIC(4, "Network ID Code",false),

        };

        protected F63Packager ()
        {
            super();
            setFieldPackager(fld63);
        }
    }


    protected class F126Packager extends Base1SubFieldPackager
    {
        protected ISOFieldPackager fld126[] =
        {
            new Base1_BITMAP126(16, "Bit Map"),
            new IFE_CHAR     (25, "Customer Name"),
            new IFE_CHAR     (57, "Customer Address"),
            new IFE_CHAR     (57, "Biller Address"),
            new IFE_CHAR     (18, "Biller Telephone Number"),
            new IFE_CHAR     (6,  "Process By Date"),
            new IFB_LLNUM    (17, "Cardholder Cert Serial Number", true),
            new IFB_LLNUM    (17, "Merchant Cert Serial Number", true),
            new IFB_NUMERIC  (40, "Transaction ID", true),
            new IFB_NUMERIC  (40, "TransStain", true),
            new IFE_CHAR     (6,  "CVV2 Request Data"),
            new IFE_CHAR     (6,  "CVV2 Request Data"),
            new Base1_BITMAP126(3, "Bit Map"),
            new IFE_CHAR     (1,  "IGOTS Request Data"),
        };

        protected F126Packager ()
        {
            super();
            setFieldPackager(fld126);
        }
    }

   /*
   protected class F55Packager extends ISOBinaryFieldPackager
    {
      public F55Packager (int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, BcdPrefixer.LLL);
        checkLength(len, 999);
      }
      //Override method pack
      public byte[] pack(ISOComponent c) throws ISOException
      {
        byte[] data = (byte[])c.getValue();
        String HEXData = Integer.toHexString(data.length)+ISOUtil.hexString(data);
        return ISOUtil.hex2byte(HEXData);
      }

      public int unpack(ISOComponent c, byte[] b, int offset) throws ISOException
      {
        int length = Integer.parseInt(ISOUtil.hexString(new byte[]{b[offset]}),16);
        byte[] unpacked = new byte[length];
        System.arraycopy(b,offset+1,unpacked,0,length);
        c.setValue(unpacked);
        return offset+length;
      }
    };
    */
    protected class F127Packager extends ISOBasePackager
    {
        protected ISOFieldPackager fld127[] =
        {
            new IFE_CHAR    (1,   "FILE UPDATE COD"),
            new IFB_LLHNUM  (19,  "ACCOUNT NUMBER", true),
            new IFB_NUMERIC (4,   "PURGE DATE", true),
            new IFE_CHAR    (2,   "ACTION CODE"),
            new IFE_CHAR    (9,   "REGION CODING"),
            new IFB_NUMERIC (4,   "FILLER", true),
        };
        protected F127Packager ()
        {
            super();
            setFieldPackager(fld127);
        }
    }

    public VSDCPOSPackager()
    {
        super();
        ((IFB_NUMERIC)VSDCFld[22]).setPadder(new RightPadder('0'));
        setFieldPackager(VSDCFld);
    }
}