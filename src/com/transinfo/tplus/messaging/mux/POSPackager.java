package com.transinfo.tplus.messaging.mux;
import org.jpos.iso.IFA_LLCHAR;
import org.jpos.iso.IFB_AMOUNT;
import org.jpos.iso.IFB_BINARY;
import org.jpos.iso.IFB_BITMAP;
import org.jpos.iso.IFB_LLCHAR;
import org.jpos.iso.IFB_LLLBINARY;
import org.jpos.iso.IFB_LLLCHAR;
import org.jpos.iso.IFB_LLNUM;
import org.jpos.iso.IFB_NUMERIC;
import org.jpos.iso.IF_CHAR;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOFieldPackager;

// Copy from ISOBasePackager
// modify fields: 53
public class POSPackager extends ISOBasePackager
{
    private static final boolean pad = false;
    protected ISOFieldPackager fld[] = {
            new IFB_NUMERIC (  4, "MESSAGE TYPE INDICATOR", true),
            new IFB_BITMAP  ( 16, "BIT MAP"),
            new IFB_LLNUM   ( 19, "PAN - PRIMARY ACCOUNT NUMBER", pad),
            new IFB_NUMERIC (  6, "PROCESSING CODE", true),
            new IFB_NUMERIC ( 12, "AMOUNT, TRANSACTION", true),
            new IFB_NUMERIC ( 12, "AMOUNT, SETTLEMENT", true),
            new IFB_NUMERIC ( 12, "AMOUNT, CARDHOLDER BILLING", true),
            new IFB_NUMERIC ( 10, "TRANSMISSION DATE AND TIME", true),
            new IFB_NUMERIC (  8, "AMOUNT, CARDHOLDER BILLING FEE", true),
            new IFB_NUMERIC (  8, "CONVERSION RATE, SETTLEMENT", true),
            new IFB_NUMERIC (  8, "CONVERSION RATE, CARDHOLDER BILLING", true),//10
            new IFB_NUMERIC (  6, "SYSTEM TRACE AUDIT NUMBER", true),
            new IFB_NUMERIC (  6, "TIME, LOCAL TRANSACTION", true),
            new IFB_NUMERIC (  4, "DATE, LOCAL TRANSACTION", true),
            new IFB_NUMERIC (  4, "DATE, EXPIRATION", true),
            new IFB_NUMERIC (  4, "DATE, SETTLEMENT", true),
            new IFB_NUMERIC (  4, "DATE, CONVERSION", true),
            new IFB_NUMERIC (  4, "DATE, CAPTURE", true),
            new IFB_NUMERIC (  4, "MERCHANTS TYPE", true),
            new IFB_NUMERIC (  3, "ACQUIRING INSTITUTION COUNTRY CODE", true),
            new IFB_NUMERIC (  3, "PAN EXTENDED COUNTRY CODE", true),//20
            new IFB_NUMERIC (  3, "FORWARDING INSTITUTION COUNTRY CODE", true),
            new IFB_NUMERIC (  3, "POINT OF SERVICE ENTRY MODE", true),
            new IFB_NUMERIC (  3, "CARD SEQUENCE NUMBER", true),
            new IFB_NUMERIC (  3, "NETWORK INTERNATIONAL IDENTIFIEER", true),
            new IFB_NUMERIC (  2, "POINT OF SERVICE CONDITION CODE", true),
            new IFB_NUMERIC (  2, "POINT OF SERVICE PIN CAPTURE CODE", true),
            new IFB_NUMERIC (  1, "AUTHORIZATION IDENTIFICATION RESP LEN",true),
            new IFB_AMOUNT  (  9, "AMOUNT, TRANSACTION FEE", true),
            new IFB_AMOUNT  (  9, "AMOUNT, SETTLEMENT FEE", true),
            new IFB_AMOUNT  (  9, "AMOUNT, TRANSACTION PROCESSING FEE", true),//30
            new IFB_AMOUNT  (  9, "AMOUNT, SETTLEMENT PROCESSING FEE", true),
            new IFB_LLNUM   ( 11, "ACQUIRING INSTITUTION IDENT CODE", pad),
            new IFB_LLNUM   ( 11, "FORWARDING INSTITUTION IDENT CODE", pad),
            new IFB_LLCHAR  ( 28, "PAN EXTENDED"),
            new IFB_LLNUM   ( 37, "TRACK 2 DATA", pad),
            new IFB_LLLCHAR (104, "TRACK 3 DATA"),
            new IF_CHAR     ( 12, "RETRIEVAL REFERENCE NUMBER"),
            new IF_CHAR     (  6, "AUTHORIZATION IDENTIFICATION RESPONSE"),
            new IF_CHAR     (  2, "RESPONSE CODE"),
            new IF_CHAR     (  3, "SERVICE RESTRICTION CODE"),//40
            new IF_CHAR     (  8, "CARD ACCEPTOR TERMINAL IDENTIFICACION"),
            new IF_CHAR     ( 15, "CARD ACCEPTOR IDENTIFICATION CODE" ),
            new IF_CHAR     ( 40, "CARD ACCEPTOR NAME/LOCATION"),
            new IFB_LLCHAR  ( 25, "ADITIONAL RESPONSE DATA"),
            new IFB_LLCHAR  ( 76, "TRACK 1 DATA"),
            new IFB_LLLCHAR (999, "ADITIONAL DATA - ISO"),
            new IFB_LLLCHAR (999, "ADITIONAL DATA - NATIONAL"),
            new IFB_LLLCHAR (999, "ADITIONAL DATA - PRIVATE"),
            new IFB_NUMERIC (  3, "CURRENCY CODE, TRANSACTION", true),
            new IF_CHAR     (  3, "CURRENCY CODE, SETTLEMENT"),//50
            new IF_CHAR     (  3, "CURRENCY CODE, CARDHOLDER BILLING"   ),
            new IFB_BINARY  (  8, "PIN DATA"   ),
            new IFB_BINARY ( 8, "SECURITY RELATED CONTROL INFORMATION"),
            new IFB_LLLCHAR (120, "ADDITIONAL AMOUNTS"),
            //new IFB_LLLCHAR (999, "RESERVED ISO"),
            new IFB_LLLBINARY (255, "CHIP DATA"),
            new IFB_LLLCHAR (999, "RESERVED ISO"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE"),//60
            new IFB_LLLCHAR (999, "RESERVED PRIVATE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE"),
            //new IFB_LLLBINARY (999, "RESERVED PRIVATE"),
            //new IFB_LLBINARY  (99, "SYSTEM TRACE AUDIT NUMBER (REFUND)"),
            new IFB_BINARY  (  8, "MESSAGE AUTHENTICATION CODE FIELD"),
            new IFB_BINARY  (  1, "BITMAP, EXTENDED"),
            new IFB_NUMERIC (  1, "SETTLEMENT CODE", true),
            new IFB_NUMERIC (  2, "EXTENDED PAYMENT CODE", true),
            new IFB_NUMERIC (  3, "RECEIVING INSTITUTION COUNTRY CODE", true),
            new IFB_NUMERIC (  3, "SETTLEMENT INSTITUTION COUNTRY CODE", true),
            new IFB_NUMERIC (  3, "NETWORK MANAGEMENT INFORMATION CODE", true),//70
            new IFB_NUMERIC (  4, "MESSAGE NUMBER", true),
            new IFB_NUMERIC (  4, "MESSAGE NUMBER LAST", true),
            new IFB_NUMERIC (  6, "DATE ACTION", true),
            new IFB_NUMERIC ( 10, "CREDITS NUMBER", true),
            new IFB_NUMERIC ( 10, "CREDITS REVERSAL NUMBER", true),
            new IFB_NUMERIC ( 10, "DEBITS NUMBER", true),
            new IFB_NUMERIC ( 10, "DEBITS REVERSAL NUMBER", true),
            new IFB_NUMERIC ( 10, "TRANSFER NUMBER", true),
            new IFB_NUMERIC ( 10, "TRANSFER REVERSAL NUMBER", true),
            new IFB_NUMERIC ( 10, "INQUIRIES NUMBER", true),//80
            new IFB_NUMERIC ( 10, "AUTHORIZATION NUMBER", true),
            new IFB_NUMERIC ( 12, "CREDITS, PROCESSING FEE AMOUNT", true),
            new IFB_NUMERIC ( 12, "CREDITS, TRANSACTION FEE AMOUNT", true),
            new IFB_NUMERIC ( 12, "DEBITS, PROCESSING FEE AMOUNT", true),
            new IFB_NUMERIC ( 12, "DEBITS, TRANSACTION FEE AMOUNT", true),
            new IFB_NUMERIC ( 16, "CREDITS, AMOUNT", true),
            new IFB_NUMERIC ( 16, "CREDITS, REVERSAL AMOUNT", true),
            new IFB_NUMERIC ( 16, "DEBITS, AMOUNT", true),
            new IFB_NUMERIC ( 16, "DEBITS, REVERSAL AMOUNT", true),
            new IFB_NUMERIC ( 42, "ORIGINAL DATA ELEMENTS", true),//90
            new IF_CHAR     (  1, "FILE UPDATE CODE"),
            new IF_CHAR     (  2, "FILE SECURITY CODE"),
            new IF_CHAR     (  6, "RESPONSE INDICATOR"),
            new IF_CHAR     (  7, "SERVICE INDICATOR"),
            new IF_CHAR     ( 42, "REPLACEMENT AMOUNTS"),
            new IFB_BINARY  ( 16, "MESSAGE SECURITY CODE"),
            new IFB_AMOUNT  ( 17, "AMOUNT, NET SETTLEMENT", pad),
            new IF_CHAR     ( 25, "PAYEE"),
            new IFB_LLNUM   ( 11, "SETTLEMENT INSTITUTION IDENT CODE", pad),
            new IFB_LLNUM   ( 11, "RECEIVING INSTITUTION IDENT CODE", pad),//100
            new IFB_LLCHAR  ( 17, "FILE NAME"),
            new IFA_LLCHAR  ( 28, "ACCOUNT IDENTIFICATION 1"),
            new IFA_LLCHAR  ( 28, "ACCOUNT IDENTIFICATION 2"),
            new IFB_LLLCHAR (100, "TRANSACTION DESCRIPTION"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED ISO USE"), //110
            new IFB_LLLCHAR (999, "RESERVED ISO USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"   ),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"  ),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED NATIONAL USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),//120
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_LLLCHAR (999, "RESERVED PRIVATE USE"),
            new IFB_BINARY  (  8, "MAC 2")
        };

  public POSPackager()
  {
    super();
    setFieldPackager(fld);
  }
}