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


public class TPlusISOCode extends Object
{

public static final int NO_OF_FLDS = 128;

public static final int MTI=0;
public static final int CARD_NUMBER=2;
public static final int PROCESSING_CODE=3;
public static final int TRANSACTION_AMOUNT=4;
public static final int SETTELMENT_AMOUNT=5;
public static final int BILLING_AMOUNT=6;
public static final int TRANSMISSION_DATE_TIME=7;
public static final int CARDHOLDED_BILL_FEE=8;
public static final int SETTELMENT_CONVERSION_RATE=9;
public static final int CARDHOLDER_CONVERSION_RATE=10;
public static final int SYSTEM_TRACE_NO=11;
public static final int TRANSMISSION_TIME=12;
public static final int TRANSMISSION_DATE=13;
public static final int EXPIRY_DATE=14;
public static final int SETTELMENT_DATE=15;
public static final int CONVERSION_DATE=16;
public static final int CAPTURE_DATE=17;
public static final int MERCHANT_TYPE = 18;
public static final int ACQ_COUNTRY_CODE = 19;
public static final int CH_COUNTRY_CODE = 20;
public static final int FWD_INST_CNTY_CODE = 21;
public static final int POS_ENTRY_MODE = 22;
public static final int CARD_SEQ_NO = 23;
public static final int NNI = 24;
public static final int POS_CONDITION_CODE = 25;
public static final int POS_PIN_CAPTURE_CODE = 26;
public static final int AUTH_IDEN_RES_LEN = 27;
public static final int TRANSACTION_FEE = 28;
public static final int SETTELMENT_FEE = 29;
public static final int TRANSACTION_PROCESSING_FEE = 30;
public static final int SETTELMENT_PROCESSING_FEE = 31;
public static final int ACQ_INST_IDEN_CODE = 32;
public static final int FORW_INST_IDEN_CODE = 33;
public static final int PRIMARY_ACCT_NUM_EXT = 34;
public static final int TRACK2DATA = 35;
public static final int TRACK3DATA = 36;
public static final int REF_NO = 37;
public static final int APPROVAL_CODE = 38;
// CHANGE AUTH_IDENFICATION_RES - > RESPONSE_CODE
public static final int RESPONSE_CODE = 39;
public static final int SERV_RESTRICTION_CODE = 40;
public static final int TERMINAL_ID = 41;
public static final int MERCHANT_ID = 42;
public static final int CARD_ACCEPTOR_NAME = 43;
public static final int ADDT_RES_DATA = 44;
public static final int TRACK1DATA = 45;
//MAKE TCC
public static final int ADDITIONAL_DATA_ISO = 46;
public static final int ADDITIONAL_DATA_NATIONAL = 47;
public static final int ADDITIONAL_DATA_PRIVATE = 48;
public static final int TRANSACTION_CURRENCY = 49;
public static final int SETTLEMENT_CURRENCY = 50;
public static final int BILLING_CURRENCY = 51;
public static final int PIN = 52;
public static final int SECURITY_DATA = 53;
public static final int ADDITIONAL_DATA = 54;
public static final int EMV = 55;
public static final int RESERVED_56 = 56;
public static final int RESERVED_57 = 57;
public static final int RESERVED_58 = 58;
public static final int RESERVED_59 = 59;
public static final int PRIVATE_60 = 60;
public static final int INVOICE_NO = 62;
// Trace No2
public static final int PRIVATE_63 = 63;
public static final int MSG_AUTHCODE = 64;
public static final int ORG_DATA_ELEMENT = 90;
public static final int REPLACEMENT_AMT = 95;
public static final int MSG_SECURITY_CODE = 96;
public static final int PRIVATE_126 = 126;


}
