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

//Java specific imports
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import com.transinfo.threadpool.TPRunnable;
import com.transinfo.tplus.debug.DebugMsgWriter;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.MessageHandler;
import com.transinfo.tplus.javabean.RequestInfo;
import com.transinfo.tplus.log.ErrorLog;
import com.transinfo.tplus.log.LogWriter;
import com.transinfo.tplus.log.SystemLog;
import com.transinfo.tplus.log.TransactionLog;
import com.transinfo.tplus.messaging.IRequestProcessor;
import com.transinfo.tplus.messaging.RequestProcessor;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.ParserFactory;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;


/**
 * This class is the object which is used to service the request.
 */

@SuppressWarnings({"unused" })
public class TPlusRequest extends java.lang.Object implements TPRunnable
{

	/** The client socket connection
	 */
	private Socket clientConnection =null;

	/** This variable contains the details of the request
	 */
	private String strRequest ="";

	/** This variable contains the details of the response
	 */
	private byte strResponse[] = null;

	/** This variable contains the details of the header
	 */
	private String strHeader ="";

	/** This class is used to parse ISO message
	 */

	IParser objISOParser = null;

	/** This is the reader associated to the client socket connection
	 */
	private BufferedReader brRequest;

	/** This is the outputstream associated with the socket connection
	 */
	private BufferedOutputStream bosOut;

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

	/** The Error Response object which will be used to generate the error response string
	 */
	// private ErrorResponse errorResponse =null;
	/** Flag to check if the request has been processed
	 */
	private boolean boolRequestProcessed =false;

	private boolean boolReqError = false;
	private String  strReqExp ="";
	private String  strReqErrCode ="-1";
	private String  strErrSrc ="00";
	private String strISOMsg="";
	private MessageHandler objMsgHandler = null;



	/**
	 * This is the constructor which sets the socket connection and request string .
	 * This constructor is called when the request is coming from the POS terminal
	 * @param sock The client socket connection.
	 */


	public TPlusRequest(Socket sock,String strRequest)
	{
		this.clientConnection = sock;
		this.strRequest = strRequest;

	}

	public TPlusRequest(Socket sock,String strRequest,MessageHandler objMsgHandler)
	{
		this.clientConnection = sock;
		this.strRequest = strRequest;
		this.objMsgHandler = objMsgHandler;

	}



	/**
	 *	This method will be called by the thread when it is ready for running..This method is where the function flow is done.
	 */

	public void run()
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:STARTING TIME:"+System.currentTimeMillis());
		long startTime= System.currentTimeMillis();

		slRequest = new SystemLog();
		try
		{
			tlRequest = new TransactionLog();
			elRequest = new ErrorLog();

			try
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Data got from the Client Socket"+strRequest);
				if(strRequest == null || strRequest.equals(""))
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Request is NULL. No Process taken place ");
					strReqErrCode = TPlusCodes.INVALID_REQUEST_MSG;
					strReqExp = "Null Request Received ";

					return;

				}

				processRequest();

			}
			catch (TPlusException TPlusExp)
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:TPlusException occurred when processing the request : " + TPlusExp.getMessage());
				if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusRequest",TPlusExp.getErrorCode(),"At processing the request"+TPlusExp.getMessage());
				TPlusPrintOutput.printErrorMessage("TPlusRequest",TPlusExp.getErrorCode(),"At processing the request"+TPlusExp.getMessage());


				boolReqError=true;
				strReqErrCode = TPlusExp.getMessageId();
				strReqExp =TPlusExp.getMessage();
				strErrSrc=TPlusExp.getErrorSrc();
			}
			catch(Exception exp)
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusRequest",TPlusCodes.ERR_REQ," Actual Error"+exp.toString());
				TPlusPrintOutput.printErrorMessage("TPlusRequest",TPlusCodes.ERR_REQ," Actual Error"+exp.toString());

				boolReqError=true;
				strReqErrCode = TPlusCodes.ERR_REQ;
				strReqExp =TPlusCodes.getErrorDesc(TPlusCodes.ERR_REQ)+exp.getMessage();

			}

			//write the response to Acquirer and log the message .

			try
			{

				if(strResponse != null)
				{
					writeResponse();
				}
				else
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Closing the connection.No Response sent.");
					closeConnection();
					return;
				}


				long endTime= System.currentTimeMillis()-startTime;
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Time Taken to Process request ="+endTime);
				TPlusPrintOutput.printMessage("TPlusRequest","Time Taken to Process Request ="+endTime);
			}
			catch(TPlusException TPlusExp)
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusRequest",TPlusExp.getErrorCode()," Actual Error for Writing Response "+TPlusExp.getMessage());
				TPlusPrintOutput.printErrorMessage("TPlusRequest",TPlusExp.getErrorCode()," Actual Error for Writing Response "+TPlusExp.getMessage());
				boolReqError=true;
				strReqErrCode = TPlusExp.getMessageId();
				strReqExp =TPlusExp.getMessage();
				strErrSrc=TPlusExp.getErrorSrc();

			}
			catch(Exception exp)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.IO_ERR_OUTPUT," Actual Error for Writing Response:"+exp);
				TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.IO_ERR_OUTPUT,"Actual Error for Writing Response "+exp);
				boolReqError=true;
				strReqErrCode = TPlusCodes.IO_ERR_OUTPUT;
				strReqExp =TPlusCodes.getErrorDesc(TPlusCodes.IO_ERR_OUTPUT)+exp.getMessage();

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Request Serviced.\n");
			TPlusPrintOutput.printMessage("TPlusRequest","Request Serviced.");

			DebugMsgWriter.write("*************** REQUEST SERVICED.***************");

		}
		catch(Exception exp)
		{
			//elRequest.addLogItem(TPlusCodes.ERR_REQ,TPlusCodes.getErrorDesc(TPlusCodes.ERR_REQ)+" Error: "+exp.getMessage());

			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusRequest",TPlusCodes.APPL_ERR," Uncaught Exception occured in the run method of TPlusRequest. Error:"+exp.getMessage());
			TPlusPrintOutput.printErrorMessage("TPlusRequest",TPlusCodes.APPL_ERR,"Uncaught Exception occured in the run method of TPlusRequest."+exp);

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Request Serviced.\n");
			TPlusPrintOutput.printMessage("TPlusRequest","Request Serviced.");
		}
		finally
		{
			
			try
			{
				updateLogs();
			}
			catch(Exception exp)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Error while inserting into Log table"+exp);
			}


		}

	}


	/**
	 *	This Method processes the request by calling CHController or VRController..The response string is also set
	 *  by this method
	 *  @throws TPlusException TPlusException will be thrown, if the input request is invalid.
	 */

	private void processRequest() throws TPlusException
	{
		IParser objISOResParser = null;

		TPlusPrintOutput.printMessage("TPlusRequest","Request Processing...");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest: Start to process the request....");
		try
		{
			// Parsing the Request Message.
			TPlusPrintOutput.printMessage("\n\n************************* REQUEST RECEIVED ********************************\n\n");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("\n************************* REQUEST RECEIVED ********************************\n");
			DebugMsgWriter.write("********REQUEST FROM TERMINAL***********\n");

			objISOParser = (IParser)ParserFactory.createPraserObject(objMsgHandler.getMessageProcessor());
			System.out.println(strRequest);
			strHeader = strRequest.substring(0,10);
			strRequest = strRequest.substring(10);
			System.out.println(strRequest);
			objISOParser.parse(strRequest);

			TPlusPrintOutput.printMessage("\n\n************************* REQUEST RECEIVED ********************************\n\n");
			IRequestProcessor objISOFac = new RequestProcessor();

			if(objISOFac != null)
			{

				TPlusPrintOutput.printMessage("TPlusRequest","Request Send");
				objISOResParser = objISOFac.processRequest(objISOParser);

			}

			if(objISOResParser !=null)
			{
				TPlusPrintOutput.printMessage("TPlusRequest","Response Received");
				objISOParser = objISOResParser;

				strReqErrCode = objISOParser.getValue(TPlusISOCode.RESPONSE_CODE);

				System.out.println("strHeader="+strHeader);
				objISOParser.setHeader(strHeader);
				objISOParser.setResponseMTI();
				//objISOParser.setHeader(objISOParser.getResponseHeader());

				TPlusPrintOutput.printMessage("\n\n************************* CLIENT RESPONSE ********************************\n\n");
				DebugMsgWriter.write("********* RESPONSE TO TERMINAL*************\n");

				strResponse = objISOParser.repack();

				TPlusPrintOutput.printMessage("Client Response="+ ISOUtil.hexString(strResponse));

				TPlusPrintOutput.printMessage("\n\n************************* CLIENT RESPONSE ********************************\n\n");


				if (DebugWriter.boolDebugEnabled) DebugWriter.write("\n*********** CLIENT RESPONSE *****************\n");
			}
			else
			{
				strReqExp = "No Response Received";
				TPlusPrintOutput.printMessage("TPlusRequest"," NULL Response Received...");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest: NULL Response Received...");
			}


		}
		catch(TPlusException tplusExp)
		{
			boolReqError = true;
			strReqExp = "Error in processing the request: Error Source "+tplusExp.getMessage();
			throw tplusExp;
		}
		catch(Exception exp)
		{
			boolReqError = true;
			strReqExp = "Appl Error in parssing the request: Error Source "+exp.getMessage();
			System.out.println("ERROR="+exp.getMessage());
			throw new TPlusException(TPlusCodes.ERR_REQ,TPlusCodes.getErrorDesc(TPlusCodes.ERR_REQ)+" Error:"+ exp);
		}

	}



	/**
	 *	This Method writes the data to the client socket connection.
	 *	@throws TPlusException TPlusException if there is any error while writing to the socket.
	 */
	private void writeResponse()  throws TPlusException,Exception
	{
		//Get the output Stream and write the resonse string.
		try {

			writeToClient();
		}
		catch (Exception exp)
		{

			throw new TPlusException(TPlusCodes.IO_ERR_OUTPUT,TPlusCodes.getErrorDesc(TPlusCodes.IO_ERR_OUTPUT)+" Error: To Terminal"+ exp.getMessage());
		}

	}


	/**
	 * This will be called before the Thread is stopped. This will close the connections and write the logs and ext.
	 */

	public void kill()
	{

		if  (boolRequestProcessed)
			return;

		//create an error log object if one is not created
		if (slRequest == null)
			slRequest = new SystemLog();


		try
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Killing Request");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Updating logs before killing request.");

			//update logs
			updateLogs();

			// If there is an error in the log writing then quitl
			if (actOnFileError())
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Could not write to the log since there is a logging problem.");
				return;
			}
			//Write the output to the socket client
			try {
				writeToClient();
			}
			catch (IOException ioExp)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Error while writing output to the socket before killing. Error: "+ ioExp.getMessage());
			}
			finally {
				closeConnection();
			}
		}
		catch(Exception exp)
		{
			//update error logs here as well
			//**
			//elRequest.addLogItem("265","Unknown error occurred while killing the request. Error: "+exp.getMessage());
			//updateLogs();
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusRequest",TPlusCodes.APPL_ERR," Uncaught Exception occured in the kill method TPlusRequest:"+exp);
			TPlusPrintOutput.printErrorMessage("TPlusRequest",TPlusCodes.APPL_ERR,"Uncaught Exception occured in the kill method TPlusRequest."+exp);


			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Uncaught Exception occured in the kill method TPlusRequest. Error:"+exp.getMessage());
		}

	}


	/** Update the logs to the file.
	 */
	private void updateLogs()
	{
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Updating Logs.\n"+boolReqError);
		TPlusPrintOutput.printMessage("TPlusRequest","Updating Logs.\n"+boolReqError);

		LogWriter objLogWriter = new LogWriter();
		RequestInfo objReqInfo = new RequestInfo();
		objReqInfo.setReqError(boolReqError);
		objReqInfo.setReqErrCode(strReqErrCode);
		objReqInfo.setReqErrMsg(strReqExp);
		objReqInfo.setRequest(strRequest);
		objReqInfo.setErrorSrc(strErrSrc);
		objReqInfo.setSessionId("12345");

		// To be check again
		objLogWriter.updateLogs(objISOParser,objReqInfo);
	}

	/** Checks if there is an error in the log writer and if so then it closes the connection and returns
	 * @return boolean - true if there is an error in the log writer
	 */
	private boolean actOnFileError()
	{
		//**
		/*if (LogWriter.isError())
        {
            closeConnection();
            boolRequestProcessed = true;
            return true;
        }
        else
        {
            return false;

        }*/

		return true;
	}


	/**
	 *	This method writes the response string into the socket connection.
	 *  @throws IOException If there is an IO error.
	 *
	 */

	private void writeToClient() throws IOException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Sending Response to Client ");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest: "+ISOUtil.hexString(strResponse));
		// TPlusPrintOutput.printMessage("TPlusRequest","Response to Client:"+new String(strResponse));
		//Create a buffered output stream to client
		try
		{
			bosOut = new BufferedOutputStream(clientConnection.getOutputStream());
			bosOut.write(strResponse);
			bosOut.flush();


		}catch(IOException e)
		{
			//if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Exceptio while Written output to the Client socket connection.Output:"+e.getMessage());
			System.out.println(e);
			throw e;
		}

	}


	/**
	 *  This method closes the buffered reader, print writer and the client socket connection.
	 */

	private void closeConnection()
	{
		// Close the connection.
		try {
			if (bosOut != null)
			{
				bosOut.close();
			}
			if (clientConnection != null)
				clientConnection.close();
		}
		catch(IOException ioExp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:Error while closing socket connection. Error:"+ioExp);
		}
	}

}
