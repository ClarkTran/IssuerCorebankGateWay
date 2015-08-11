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
package com.transinfo.tplus.switching;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.ClientData;
import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.SwitchInfo;
import com.transinfo.tplus.log.ErrorLog;
import com.transinfo.tplus.log.SystemLog;
import com.transinfo.tplus.log.TransactionLog;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.util.DBManager;




public class TestSwitchManager
{

	/**
		The client socket connection
     */
    private Socket clientConnection =null;

	/**
		The request received from which source.Terminal - 0 Issuer - 1
     */
    private int intMsgSrc =0;

    /**
    	The type of request to be serviced.. interface CHRequest(1010) or VRRequest(4040).
     */
    private int intReqType =0;
    /**
    	The type of request to be serviced.. interface CHRequest("1010") or VRRequest("4040").
     */
    private String strReqType ="";
    /** This variable contains the details of the request
     */
    private String strRequest ="";
    /** This variable contains the details of the response
	 */
    public String strResponse ="";
    /** This variable contains the details to whom the response to be sent.
	 */
    private int intResponse =1;
    /** This variable contains the details of the header
	 */
    private String strHeader ="";

    /** This class is used to parse ISO message
	 */

    IParser objISOParser = null;

    /** This variable contains the details of the Request initiated.(Terminal or Issuer)
	 */
    private String strClient ="";

    /** This is the reader associated to the client socket connection
     */
    private BufferedReader brRequest;

    /** This is the outputstream associated with the socket connection
     */
    private BufferedOutputStream bosOut;
    /** This Arraylist is the resonse which will be sent to the client/Issuer.
     */
    private ArrayList objResList = null;
    /** The Error Log object
     */
	private ErrorLog elRequest =null;
	/** The System Log object
     */
    private SystemLog slRequest =null;

    /** The Transaction Log object
     */
    private TransactionLog tlRequest =null;

    /** The Transaction Log object
     */
 
    private final int HEADERLENGTH = 13;

   /**This Class is used store to check whether full request has arrived for processing
    */
    private ClientData objCliData = null;

    /** The Error Response object which will be used to generate the error response string
     */
   // private ErrorResponse errorResponse =null;
    /** Flag to check if the request has been processed
     */
    private boolean boolRequestProcessed =false;

    private Object objThread = null;

    private String strSessionId = null;
    private boolean boolSessionUpd = false;
	private boolean boolReqError = false;
	private String  strReqExp ="SUCCESS";
	private String  strReqErrCode ="00";



public void processRequest() throws TPlusException
{

	/*try
	{
			strRequest ="1234567890123456";
			// Converting the raw ISO to Hex
			String strHexISO = "600000000002003020078020C0020400000000000000010078005900520001000000374541822001564124D060120101234226100000303030303030313130303030303030303030303030313101185F2A02070282025C008407A00000000310109A030401269C01009F02060000000001009F03060000000000009F0902008C9F1A0207029F2608BB67B3EF39BAA0CE9F2701809F3303E020C89F3501229F3602001B9F3704BB5D5F445F3401019F34031E03009F100706990A03A0A100950540000080000006303030303032";
			System.out.println("HEX ISO MESSAGE="+strHexISO);
			// Getting the Header info from the message
			if(strRequest.length()<12)
			{
				throw new TPlusException(TPlusCodes.INVALID_TRANSACTION,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_TRANSACTION)+". Error: Transaction = "+strHexISO );
			}
			else
			{
				strHeader = strHexISO.substring(0,10);
				strHexISO = strHexISO.substring(10);
			}

			// Parsing the Raw ISO msg against the Context file
			objISOParser = (TPlusISOParser)ISOParserFactory.createObject("com.transinfo.tplus.message.parser.TPlus8583Parser.class");
			objISOParser.parse(strHexISO,"file:\\C:\\Satya\\Download\\Software\\JPos\\jpos-1.4.8\\jpos\\src\\config\\packager\\base1.xml");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("ISO message parsed successfully.... :");

			ISOFactory objISOFac = (ISOFactory)ISOFactory.getFactory(objISOParser.getValue(TPlusISOCode.PROCESSING_CODE));
			objResList = objISOFac.processRequest(objISOParser,strHeader,intMsgSrc);
			intResponse = ((Integer)objResList.get(0)).intValue();
			strResponse = (String)objResList.get(1);

			strHexISO = ISOUtil.hexString(strResponse.getBytes());

			// Getting the Header info from the message
			if(strRequest.length()<12)
			{
				throw new TPlusException(TPlusCodes.INVALID_TRANSACTION,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_TRANSACTION)+". Error: Transaction = "+strHexISO );
			}
			else
			{
				strHeader = strHexISO.substring(0,10);
				strHexISO = strHexISO.substring(10);
			}

			System.out.println("*** HEADER***"+strHeader);
			System.out.println("*** ISO MSG LEN***"+strHexISO.length());
			System.out.println("*** ISO MSG ***"+strHexISO);

			// Parsing the Raw ISO msg against the Context file
					objISOParser = (TPlusISOParser)ISOParserFactory.createObject(1);
					objISOParser.parse(strHexISO,"file:\\C:\\Satya\\Download\\Software\\JPos\\jpos-1.4.8\\jpos\\src\\config\\packager\\base1.xml");
		}
		catch(TPlusException tplusExp)
		{
			boolReqError = true;
			strReqExp = "Error in processing the request: Error Source "+tplusExp.getMessage();
			//Set Response
			System.out.println("ERROR1="+tplusExp.getMessage());
			throw tplusExp;
		}
		catch(Exception exp)
		{
			boolReqError = true;
			strReqExp = "Error in parssing the request: Error Source "+exp.getMessage();
			System.out.println("ERROR1="+exp.getMessage());
			throw new TPlusException(TPlusCodes.INVALID_REQUEST_MSG,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_REQUEST_MSG)+" Error:"+ exp.getMessage());
			//Set Response
		}*/

}


 public static void main(String s[])
 {

	  try
	  {

		  TestSwitchManager objMSG = new TestSwitchManager();

		/*try
		{
			objMSG.initServer();
		}
		catch(Exception exp)
		{
			System.out.println("DataBase error="+exp);

			//RAISE ALERT

	 	}*/

	 	SwitchInfo objSwitch1 = new SwitchInfo();
	 	objSwitch1.setSwitchName("VISA");
	 	objSwitch1.setIPAddress("127.0.0.1");
	 	objSwitch1.setPort(5052);
	 	objSwitch1.setCardLow(0000000000000000L);
	 	objSwitch1.setCardHigh(9999999999999999L);
		objSwitch1.setDestCurrencyISO("000");

	 	SwitchInfo objSwitch2 = new SwitchInfo();
		objSwitch2.setSwitchName("MASTER");
		objSwitch2.setIPAddress("127.0.0.1");
	 	objSwitch2.setPort(8001);
	 	objSwitch2.setCardLow(0000000000000000L);
	 	objSwitch2.setCardHigh(9999999999999999L);
	 	objSwitch2.setDestCurrencyISO("000");


		HashMap objMap = new HashMap();
		objMap.put("VISA",objSwitch1);
		//objMap.put("MASTER",objSwitch2);

		ISwitch objSwitch = SwitchManager.getInstance();
		byte hex2byte[] = ISOUtil.hex2byte("0207600001800004003020478020C00204000000000000000100101001012300520001000000371234567890123460D060120101234226100000303030303030313130303030303030303030303030313101185F2A02070282025C008407A00000000310109A030401269C01009F02060000000001009F03060000000000009F0902008C9F1A0207029F2608BB67B3EF39BAA0CE9F2701809F3303E020C89F3501229F3602001B9F3704BB5D5F445F3401019F34031E03009F100706990A03A0A100950540000080000006303030303032");
		objSwitch.sendMessage(objSwitch1,hex2byte);
		//System.out.println(objSwitch.initService(objMap));
		System.out.println(objSwitch.info());
		System.out.println("Sending Data");


		// This for the DB
		 /* ISwitch objSwitch = SwitchManager.getInstance();
		  System.out.println(objSwitch.initService(TPlusConfig.getSwitchMap()));
		  System.out.println(objSwitch.info());
		  System.out.println("Sending Data");

		  if(objSwitch.getServiceState() != 0)
		  {

				  // Session Management

				  Socket objSoc = new Socket("localhost",9000);
				  BufferedReader in = new BufferedReader(new InputStreamReader(objSoc.getInputStream(),"8859_1"));
	              OutputStream out = objSoc.getOutputStream();
		          PrintWriter printOut = new PrintWriter(new OutputStreamWriter(out,"8859_1"),true);
		          printOut.println("Hello");

		          System.out.println("HttpdServer1 input="+ in.readLine());

				  SessionHandler.initSessionTable();
				  SessionTable objSessTable = SessionHandler.getSessionTable();
				  objSessTable.createSession("Th1","ISOMESSAGE","1010","02020","TranID12345",objSoc);

				  System.out.println("Session Exists="+objSessTable.validateSession("TranID12345"));
				  System.out.println("Session Exists="+objSessTable.validateSessionTimeout("TranID12345"));

				  objSoc = objSessTable.getClientSocket("TranID12345");

				  System.out.println("IS Connected="+objSoc.isConnected());

					Thread.sleep(10000);

			  try
			  {
				String strSwitchName = getSwitchName(4444444444444444L);
				System.out.println("SwitchName="+strSwitchName);
			  	//objSwitch.sendMessage(strSwitchName,"TranID12345");

			  }
			  catch(Exception e){System.out.println(e);}*/

			 /* try
			  {

			  	objSwitch.sendMessage("MASTER","TranID12345");

			  }
			  catch(Exception e){System.out.println(e);}
			  System.out.println("Active Count");
			  System.out.println("Before Active Clients="+ objSwitch.getActiveClients().size());
			  System.out.println("Before InActive Clients="+ objSwitch.getInActiveClients().size());*/

			  /*Thread.sleep(15000);

			  try
			  {
			  objSwitch.sendMessage("VISA","Hello1");
			  }
			  catch(Exception e){System.out.println(e);}

			  try
			  {
			  objSwitch.sendMessage("MASTER","Hell1");
			  }
			  catch(Exception e){System.out.println(e);}
			  System.out.println("After Active Clients="+ objSwitch.getActiveClients().size());
			  System.out.println("After InActive Clients="+ objSwitch.getInActiveClients().size());

			  if(objSwitch.getServiceState() != 0)
		  	  {
				  System.out.println("Shuting Down");
				  objSwitch.stopService();
		  	  }

		  	  System.out.println(objSwitch.info());

	  	}
	  	else
	  	{
			System.out.println("Service not Running");
		}*/

  	  }
  	  catch(Exception e){System.out.println("Error:"+e);}


}



public void initServer() throws TPlusException
    {
        boolean boolError = false;
        //TPlusAdminDaemon = this;

        //Load the TPlus Properties data
        try
        {
			TPlusConfig.loadProperties();

        }
        catch(Exception exp)
        {

            // if there is an error then update the error log and exit
            boolError = true;

            //elTPlusAdmin.addLogItem(TPlusExp.getMessageId(),TPlusExp.getMessage());
            //ErrorLog.updateLog(elTPlusAdmin);
            //throw TPlusExp;
        }

       // Setting the Debug Log file

        DebugWriter.setFileNameAndFormat(TPlusConfig.strDebugFile,TPlusConfig.strDateFormat);

        try
        {
            if (TPlusConfig.boolDebug)
       	        DebugWriter.startDebugging();

        }
        catch(IOException ioExp)
        {

            //Update the error log and exit.
            boolError = true;
            //boolShutdown = true;
            throw new TPlusException(TPlusCodes.LOG_FILE_ERR,TPlusCodes.getErrorDesc(TPlusCodes.LOG_FILE_ERR)+" Error in Opening Debug Log: "+ioExp.getMessage());
        }


		//Initializing DB Pool
		try
		{
			DBManager.initDBPool();
		}
		catch(Exception exp)
		{
			boolError = true;
            //boolShutdown = true;
            throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,TPlusCodes.getErrorDesc(TPlusCodes.DATABASE_CONN_ERR)+". Error in Creating DB pool:"+exp.getMessage());

			//RAISE ALERT

	 	}

		// Creating the System Log instance
		//elTPlusSystem = new SystemLog();

		//Load the TPlus Config data
        try {

            TPlusConfig.loadConfig();
        }
        catch(TPlusException TPlusExp)
        {
            // if there is an error then update the error log and exit
            boolError = true;
            //boolShutdown = true;
            //elTPlusSystem.addLogItem("TPLUSSERVER",TPlusExp.getMessageId(),TPlusExp.getErrorType(0),"Error in loding Server Config Data from DB:--"+TPlusExp.getMessage());
            //updateAndCloseLog();
            throw TPlusExp;
        }


        //Load the TPlus configuration limits
        /*try {
            TPlusParamValidations.loadConfigLimits();
        }
        catch(TPlusException TPlusExp)
        {

            // if there is an error then update the error log and exit
            boolError = true;

            //boolShutdown = true;
            elTPlusAdmin.addLogItem(TPlusExp.getMessageId(),TPlusExp.getMessage());

            ErrorLog.updateLog(elTPlusAdmin);

            LogWriter.stopErrorLogging();

            throw TPlusExp;
        }*/


    }


    public static String getSwitchName(long cardNumber)throws Exception
	{
		ISwitch objSwitchMgr = SwitchManager.getInstance();

		
		return "switchName";

	}




}



