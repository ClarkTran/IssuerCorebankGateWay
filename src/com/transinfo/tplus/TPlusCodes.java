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


package com.transinfo.tplus;


import java.util.HashMap;



/**
* This class contains all the error codes & description used in TPlus
*/

public class TPlusCodes
{

// Satya for TPLUS
// Error Codes Related to the DataBase Access

  /******************** Related to TPlus Server *******************/

	public static final String LOG_FILE_ERR			= "251";
	public static final String SVR_SOCK_FAIL		= "252";
	public static final String ADM_SOCK_FAIL		= "253";
	public static final String SVR_START_FAIL		= "253";
	public static final String INI_FILE_ERR			= "254";
	public static final String KEY_NOT_FOUND		= "255";
	public static final String IO_ERR_INI			= "256";
	public static final String INVALID_PARAM		= "257";
	public static final String ENCR_DCR_FAIL		= "258";
	public static final String IO_ERR_INPUT			= "259";
	public static final String IO_ERR_OUTPUT		= "260";
	public static final String UNKNOWN_ERR			= "261";
	public static final String ERR_ADM_SOCK			= "263";
	public static final String ERR_SVR_SOCK			= "264";
	public static final String ERR_REQ				= "265";
	public static final String REQ_KILL				= "266";
	public static final String THREAD_EXP			= "267";
	public static final String IO_EXP_SVR			= "268";
	public static final String ERR_ADM_REQ			= "269";
	public static final String EN_DEC_FAIL			= "270";

	/******************** Related to Switch *********************/
	public static final String IP_INVALID			= "291";
	public static final String CONN_TIME_OUT		= "292";
	public static final String SOCK_ISS_FAIL		= "293";
	public static final String CONN_ISS_FAIL		= "294";
	public static final String CONN_NOT_ACTIVE		= "295";


	/******************** Related to DB *********************/
	public static final String DB_POOL_FAIL			= "301";
	public static final String DATABASE_CONN_ERR	= "302";
	public static final String SQL_QUERY_ERR		= "303";
	public static final String LOG_DB_ERR			= "304";
	public static final String NO_DATA_AVAILABLE 	= "305";
	public static final String UNKNOWN_DATA_TYPE	= "306";
	public static final String SMTP_MAIL_ERR		= "310";

	/******************** Related to Session *********************/
	public static final String SESSION_CREATION_FAIL	= "400";
	public static final String SESSID_NOT_FOUND			= "401";
	public static final String SESSION_CLEANUP			= "402";

	/******************** Related to Message *********************/
	public static final String APPL_ERR				= "96";
	public static final String INVALID_REQUEST		= "501";
	public static final String INVALID_MSG_LEN		= "502";
	public static final String INVALID_REQUEST_TYPE	= "503";
	public static final String INVALID_REQUEST_MSG	= "504";
	public static final String TRANX_NOT_FOUND		= "506";
	public static final String ISO_PARSING_FATAL	= "510";
	public static final String ISO_IOEXCEPTION		= "511";
	public static final String REQ_PARSE_FAIL		= "512";
	public static final String INV_MSG_MODE			= "513";
	public static final String NO_REQ_HANDLER		= "514";
	public static final String NO_REQ_PROCESSOR		= "515";
	public static final String RES_GEN_ERR			= "516";
	public static final String OBJ_CREATION_FAIL	= "517";
	public static final String MSG_EXT_FAIL			= "518";
	public static final String PARAM_SET_ERR		= "519";
	public static final String NO_MASTER_KEY		= "520";
	public static final String NO_ISSUER_WKEY		= "521";


	/******************** Related to Message *********************/// Response Code



			public static String TIMEOUT					= "-1";
			public static String APPROVED					= "00";
			public static String REFER_ISSUER				= "01";
	        public static String PLEASE_CALL				= "01";
			public static String INVALID_MERCHANT			= "03";
			public static String PICKUP						= "04";
			public static String DO_NOT_HONOUR				= "05";
			public static String ERROR						= "06";
			public static String PICK_CARD					= "07";
			public static String REQ_PROGRESS				= "09";
			public static String INVALID_TRANSACTION		= "12";
			public static String INVALID_AMT				= "13";
	        public static String INVALID_CARDNUMBER			= "14";
	        public static String NO_SUCH_ISSUER				= "15";
	        public static String CUSTOMER_CANCEL			= "17";
	        public static String CUSTOMER_DISPUTE			= "18";
	        public static String REENTER_TRANX				= "19";
	        public static String INVALID_RES				= "20";
	        public static String NO_TRANSACTION				= "21";
	        public static String SUSPECT_MALFUNCTION		= "22";
	        public static String NO_RECORD					= "25";
			public static String DUPLICATE_FILE				= "26";
			public static String FORMAT_ERROR				= "30";
			public static String EXPIRED_CARD				= "33";
			public static String RESTRICTED_CARD			= "36";
			public static String CONTACT_ACQ				= "37";
			public static String ALLOW_PIN_TRY_EXEEDED		= "38";
			public static String NO_CREDIT_ACCT				= "39";
			public static String REQ_NOT_SUPPORTED			= "40";
			public static String CARD_LOST					= "41";
			public static String STOLEN_CARD				= "43";
			public static String NO_SUFFICIENT_FUND			= "51";
			public static String NO_CHEQUING_ACCT			= "52";
			public static String NO_SAVING_ACCT				= "53";
			public static String CARD_EXP					= "54";
	        public static String INCORRECT_PERSONAL_IDEN	= "55";
			public static String NO_CARD_REC				= "56";
			public static String NO_TRANX_CUST				= "57";
	        public static String TRANX_NOTALLOWED			= "58";
			public static String SUSPECT_FRAUD				= "59";
			public static String CONTACT_ACQUIRER			= "60";
			public static String EXEED_WITHDRAW_LIMIT		= "61";
			public static String RESTRICT_CARD				= "62";
			public static String ORG_AMT_INCORRECT			= "64";
			public static String EXCEED_WITHDRAW_FREQ_LIMIT	= "65";
	        public static String PIN_RETRY_EXCEED			= "75";
	        public static String ISSUER_INOPERATIVE			= "91";
			public static String DUP_TRANX					= "94";
			public static String RECONCILATION_ERR			= "95";
			public static String SYS_MALFUNCTION			= "96";



	/*************************************************************/
	/*********************** TPlus Return Codes **********************************/

	/*******************************************************************/

	//Hashmap to contain all the error codes and the description
	private static HashMap hmpErrorCodes = new HashMap();

	//Hashmap to contain all the return codes and the description
	private static HashMap hmpReturnCodes = new HashMap();

	static{


		/******************** Related to TPlus Server *******************/
		hmpErrorCodes.put(LOG_FILE_ERR,"Could not open the log files.");
		hmpErrorCodes.put(SVR_SOCK_FAIL,"Unable to create Server Sockets.");
		hmpErrorCodes.put(ADM_SOCK_FAIL,"Unable to create Server Sockets.");
		hmpErrorCodes.put(SVR_START_FAIL,"Unable to start the TPlusServerDaemon.");
		hmpErrorCodes.put(INI_FILE_ERR,"Unable to read the Config.xml. ");
		hmpErrorCodes.put(KEY_NOT_FOUND,"Could not find the master key file.");
		hmpErrorCodes.put(IO_ERR_INI,"IOError while reading the config file.");
		hmpErrorCodes.put(INVALID_PARAM,"Configuration parameter entry invalid.");
		hmpErrorCodes.put(ENCR_DCR_FAIL,"Could not encrypt/decrypt as the key is corrupted or not available.");
		hmpErrorCodes.put(IO_ERR_INPUT,"IOError occurred while trying to read the input.");
		hmpErrorCodes.put(IO_ERR_OUTPUT,"IOError occurred while trying to sending the data.");

		hmpErrorCodes.put(UNKNOWN_ERR,"Unknown error occurred while starting the TPlusServerDaemon.");
		hmpErrorCodes.put(ERR_ADM_SOCK,"Error while closing the Admin Server.");
		hmpErrorCodes.put(ERR_SVR_SOCK,"Error while closing the TPlusServer.");
		hmpErrorCodes.put(ERR_REQ,"Unknown error occurred while servicing the request.");
		hmpErrorCodes.put(REQ_KILL,"Request killed, since the system is shutting down.");
		hmpErrorCodes.put(THREAD_EXP,"Threadpool exception occurred when adding request to the request queue.");
		hmpErrorCodes.put(IO_EXP_SVR,"IOException occurred when listening to the serverport.");
		hmpErrorCodes.put(ERR_ADM_REQ,"Error while servicing admin request.");
		hmpErrorCodes.put(EN_DEC_FAIL,"Encoding/Decoding failed.");

		hmpErrorCodes.put(IP_INVALID,"Invalid URL.");
		hmpErrorCodes.put(CONN_TIME_OUT,"TCP/IP Connection to issuer host timed out.");
		hmpErrorCodes.put(SOCK_ISS_FAIL,"Could not establish socket connection to issuer host.");
		hmpErrorCodes.put(CONN_ISS_FAIL,"Could not connect to issuer host.");
		hmpErrorCodes.put(CONN_NOT_ACTIVE," Active connection to Issuer Could not available.");


		/******************** Related to Session *********************/
		hmpErrorCodes.put(SESSION_CREATION_FAIL,"Session could not be created.");
		hmpErrorCodes.put(SESSID_NOT_FOUND,"Session id not found in the session table.");
		hmpErrorCodes.put(SESSION_CLEANUP,"Session Cleanup.");

		/******************** Related to DB *********************/

		hmpErrorCodes.put(DB_POOL_FAIL,"Creating DataBase Pool Error.");
		hmpErrorCodes.put(DATABASE_CONN_ERR,"DataBase connection Error.");
		hmpErrorCodes.put(SQL_QUERY_ERR,"SQL execution error.");
		hmpErrorCodes.put(LOG_DB_ERR,"DB Logging Error.");
		hmpErrorCodes.put(NO_DATA_AVAILABLE,"No records found for executed SQL");
		hmpErrorCodes.put(UNKNOWN_DATA_TYPE,"Unknown data type.");
		hmpErrorCodes.put(SMTP_MAIL_ERR,"Error while sending Alert mail.");

		/******************** Related to Message *********************/

		hmpErrorCodes.put(APPL_ERR,"Application Error.");
		hmpErrorCodes.put(INVALID_REQUEST,"Invalid Request Message.");
		hmpErrorCodes.put(INVALID_MSG_LEN,"Invalid Message Length.");
		hmpErrorCodes.put(INVALID_REQUEST_TYPE,"Not Supported Request Type.");
		hmpErrorCodes.put(INVALID_REQUEST_MSG,"Unable to parse the request.");
		hmpErrorCodes.put(TRANX_NOT_FOUND, "Transaction not found in DB");
		hmpErrorCodes.put(ISO_PARSING_FATAL, "TPlus ISO Parsing Fatal Exception.");
		hmpErrorCodes.put(ISO_IOEXCEPTION, "TPlus XML Parsing IOException.");
		hmpErrorCodes.put(REQ_PARSE_FAIL, "Error while parsing the request.");
		hmpErrorCodes.put(INV_MSG_MODE, "Invalid message mode .");
		hmpErrorCodes.put(NO_REQ_HANDLER, "No request handler available for the message.");
		hmpErrorCodes.put(NO_REQ_PROCESSOR, "No request processor available for the message.");
		hmpErrorCodes.put(RES_GEN_ERR, "Error while generating the response.");
		hmpErrorCodes.put(OBJ_CREATION_FAIL, "Dynamic objection creation error.");
		hmpErrorCodes.put(MSG_EXT_FAIL, "Message extraction error from received request.");
		hmpErrorCodes.put(PARAM_SET_ERR, "Error while setting parameter in parsed object.");
		hmpErrorCodes.put(NO_MASTER_KEY, "No Master Key Avaliable.");
		hmpErrorCodes.put(NO_ISSUER_WKEY, "No Issuer Working Key Avaliable.");

		/******************** Related to Response *********************/

		hmpErrorCodes.put(TIMEOUT,"Request Timeout.");
		hmpErrorCodes.put(REFER_ISSUER,"Refer Issuer.");
		hmpErrorCodes.put(INVALID_TRANSACTION,"Invalid Transaction.");

		hmpErrorCodes.put(INVALID_CARDNUMBER,"Invalid CardNumber.");
		hmpErrorCodes.put(INVALID_CARDNUMBER,"Invalid CardNumber.");
		hmpErrorCodes.put(INVALID_MERCHANT,"Invalid Merchant.");
		hmpErrorCodes.put(DO_NOT_HONOUR," DO Not Honour.");
   	    hmpErrorCodes.put(INCORRECT_PERSONAL_IDEN," Invalid Personal Identification.");
   	    hmpErrorCodes.put(APPROVED,"Transaction Approved.");
   	    hmpErrorCodes.put(PICKUP,"Pickup Card.");
   	    hmpErrorCodes.put(ERROR,"Error in Application.");
   	    hmpErrorCodes.put(PICK_CARD,"Pickup Card.");
   	    hmpErrorCodes.put(REQ_PROGRESS,"Req in Progess.");
   	    hmpErrorCodes.put(INVALID_AMT,"Invalid Amount.");
   	    hmpErrorCodes.put(NO_SUCH_ISSUER,"No Such Issuer.");
   	    hmpErrorCodes.put(INVALID_TRANSACTION,"Invalid Transaction.");
   	    hmpErrorCodes.put(CUSTOMER_CANCEL,"Tranx Canceled by Customer.");
   	    hmpErrorCodes.put(CUSTOMER_DISPUTE,"Customer Dispute.");
   	    hmpErrorCodes.put(REENTER_TRANX,"Re_enter Transaction.");
   	    hmpErrorCodes.put(INVALID_RES,"Invalid Response.");
   	    hmpErrorCodes.put(NO_TRANSACTION,"No Transaction Available.");
   	    hmpErrorCodes.put(SUSPECT_MALFUNCTION,"Suspect Mulfunction.");
   	    hmpErrorCodes.put(NO_RECORD,"No Record.");
   	    hmpErrorCodes.put(DUPLICATE_FILE,"Duplicate File.");
   	    hmpErrorCodes.put(FORMAT_ERROR,"Message Format Error.");
   	    hmpErrorCodes.put(EXPIRED_CARD,"Card Expired.");
   	    hmpErrorCodes.put(RESTRICTED_CARD,"Restricted Card.");
   	    hmpErrorCodes.put(CONTACT_ACQ,"Contact Acquirer.");
   	    hmpErrorCodes.put(ALLOW_PIN_TRY_EXEEDED,"PIN Try Exceed.");
   	    hmpErrorCodes.put(NO_CREDIT_ACCT,"No Credit Account.");
   	    hmpErrorCodes.put(REQ_NOT_SUPPORTED,"Request not Support.");
   	    hmpErrorCodes.put(CARD_LOST,"Card Lost.");
   	    hmpErrorCodes.put(STOLEN_CARD,"Stolen Card.");
   	    hmpErrorCodes.put(NO_SUFFICIENT_FUND,"InSufficient Fund.");
   	    hmpErrorCodes.put(NO_CHEQUING_ACCT,"No Checking Account.");
   	    hmpErrorCodes.put(NO_SAVING_ACCT,"No Saving Account.");
   	    hmpErrorCodes.put(CARD_EXP,"Card Expired.");
   	    hmpErrorCodes.put(INCORRECT_PERSONAL_IDEN,"Incorrect Personal Identification.");
   	    hmpErrorCodes.put(NO_CARD_REC,"No Card Record.");
   	    hmpErrorCodes.put(NO_TRANX_CUST,"Tranx not done by Customer.");
   	    hmpErrorCodes.put(TRANX_NOTALLOWED,"Tranx not Allowed.");
   	    hmpErrorCodes.put(SUSPECT_FRAUD,"Suspect Fraud.");
   	    hmpErrorCodes.put(CONTACT_ACQUIRER,"Contact Acquirer.");
   	    hmpErrorCodes.put(EXEED_WITHDRAW_LIMIT,"Exceed Withdrawal Limit.");
   	    hmpErrorCodes.put(RESTRICT_CARD,"Restricted Card.");
   	    hmpErrorCodes.put(ORG_AMT_INCORRECT,"Org Amt Incorrect.");
   	    hmpErrorCodes.put(EXCEED_WITHDRAW_FREQ_LIMIT,"Exceed Withdrawal Freq Limit.");
   	    hmpErrorCodes.put(PIN_RETRY_EXCEED,"PIN Retry Exceeded.");
   	    hmpErrorCodes.put(ISSUER_INOPERATIVE,"Issuer In-Operative.");
   	    hmpErrorCodes.put(DUP_TRANX,"Dupicate Transaction.");
   	    hmpErrorCodes.put(RECONCILATION_ERR,"Reconcilation Error.");
   	    hmpErrorCodes.put(SYS_MALFUNCTION,"System Malfunction.");


	}


   /*
	* Returns the error description for the given error code
	*/


	public static HashMap hTPlusReqCodes = new HashMap();
	public static HashMap hmpThreeDSIReqCodes = new HashMap();


	public static String getErrorDesc(String strErrCode){
		Object obj = hmpErrorCodes.get(strErrCode);
		if(obj != null)
			return (String)hmpErrorCodes.get(strErrCode);
		else
			return (String)hmpReturnCodes.get(strErrCode);
	}


// ISO Message codes
//Error code



        static
        {



		}

//ALERT CODES

    public static String RISK_ERROR ="RISK";
    public static String APPLLICATION_ERROR ="APPL";
    public static String DB_ERROR ="DB";
    public static String ENGINE_ERROR ="ENGINE";


}