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

package com.transinfo.tplus.debug;

//Java specific imports
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.ErrorHandler;
import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.TPlusUtility;

/**
* This class is used to open and write the logs.
*/
public class DebugMsgWriter
{
    private static FileOutputStream fwDebug = null;  //The debug file writer
    private static PrintStream pwDebug = null; //The debug print writer

    private static String strDebugFile = null; //The current error log file name
    private static int intDebugDate = 0; //The date value with which the audit log file is created.
    private static SimpleDateFormat sdfDate= null;// Date format object
    private static String strFormat = "";

    private static HashSet hssThreads = new HashSet(); // Hashset to hold all the thread ids.
    public static boolean boolDebugEnabled = false; // Variable which tells if the debug statement can be fired.

    /**
    * This method is used add a thread id to the Thread id hashset
    * @returns Object The Integer object
    */
    public static synchronized Object addThread() {
        Object objThread = new Integer(Thread.currentThread().getName());
        if ((hssThreads.size() == 0)&&(!TPlusConfig.boolDebug))
        {

            try {
                startDebugging();
            }
            catch(Exception exp){

                 TPlusPrintOutput.printMessage("DebugWriter","Error while opening debug file. Skipping debug statements. Error : "+exp.getMessage());
                 return null;

            }

        }
        hssThreads.add(objThread);
		return objThread;


    }


    /**
    * This method is used to remove the thread from the hashtable
    * @param objThread The Key object which was returned from the addThread method
    */
    public synchronized static void removeThread(Object objThread) {
        if (objThread == null) return;
        hssThreads.remove(objThread);
        if ((hssThreads.size() == 0)&&(!TPlusConfig.boolDebug))
        {
            stopDebugging();
        }
    }

    /**
    * This method is used to set the File name and format from the config file
    * @param strDebugFileTemp The name for the Debug file
    * @param  strFormatTemp The format of the Debug file name
    */
    public static void setFileNameAndFormat(String strDebugFileTemp,String strFormatTemp)
    {
        strDebugFile = strDebugFileTemp;
        strFormat = strFormatTemp;
    }


    /**
    * This method opens the debug file.
    * @throws IOException if there is an IOError
    */
    public static void startDebugging() throws IOException
    {
        if (pwDebug != null)
        {
            stopDebugging();
        }
        Calendar calNow = Calendar.getInstance();
        String strDebugFileTemp = strDebugFile.substring(0,strDebugFile.lastIndexOf(".")) + "_"+calNow.get(Calendar.YEAR)+TPlusUtility.setLength(""+(calNow.get(Calendar.MONTH)+1),2,"0",true)+TPlusUtility.setLength(""+calNow.get(Calendar.DAY_OF_MONTH),2,"0",true)+strDebugFile.substring(strDebugFile.lastIndexOf("."));
        intDebugDate = Integer.parseInt(calNow.get(Calendar.YEAR)+TPlusUtility.setLength(""+(calNow.get(Calendar.MONTH)+1),2,"0",true)+TPlusUtility.setLength(""+calNow.get(Calendar.DAY_OF_MONTH),2,"0",true));
        sdfDate = new SimpleDateFormat(strFormat);
        fwDebug = new FileOutputStream(strDebugFileTemp,true);
        pwDebug = new PrintStream(fwDebug);
        boolDebugEnabled = true;
    }

    /** This method closes the debug file.
     */
    public static void stopDebugging() {
		boolDebugEnabled = false;
        try {
            if (pwDebug != null)
                pwDebug.close();
            if (fwDebug != null)
                fwDebug.close();
            pwDebug = null;
			fwDebug = null;
        }
        catch (Exception exp)
        {
            ErrorHandler.handleCloseError("Debug File",exp);
        }
    }

    /** This method updates the debug file
     * @param strDebug The debug string which has to be written to the debug file.
     */
    public static synchronized void write(String strDebug)
    {

        //If debug is not enabled, then return
        if ((!TPlusConfig.boolDebug) && (hssThreads.size() == 0))
            return;

        try {
            //check if the audit log file has reached the file size limit.
            if (!validateFileDate(DebugMsgWriter.intDebugDate))
            {
                startDebugging();
            }

            //write the data to the debug file
            //synchronized (pwDebug)
            //{
            pwDebug.println(sdfDate.format(new Date())+TPlusConfig.LOGITEM_SEPERATOR+Thread.currentThread().getName()+TPlusConfig.LOGITEM_SEPERATOR+strDebug);
            pwDebug.flush();
            //}
        }
        catch (Exception exp)
        {

			if (boolDebugEnabled) ErrorHandler.raiseDBAlert("writing Debug strings to the debug file",exp);

        }
    }

    /** This method updates the debug file
     * @param strDebug The debug string which has to be written to the debug file.
     */
    public static synchronized void writeMsgDump(ISOMsg objISOMsg)
    {

        //If debug is not enabled, then return
        if ((!TPlusConfig.boolDebug) && (hssThreads.size() == 0))
            return;

        try {
            //check if the audit log file has reached the file size limit.
            if (!validateFileDate(DebugMsgWriter.intDebugDate))
            {
                startDebugging();
            }

            //write the data to the debug file
            //synchronized (pwDebug)
            //{
            pwDebug.print(sdfDate.format(new Date())+TPlusConfig.LOGITEM_SEPERATOR+Thread.currentThread().getName()+TPlusConfig.LOGITEM_SEPERATOR);
            objISOMsg.dump(pwDebug,"");
            pwDebug.flush();
            //}
        }
        catch (Exception exp)
        {

			if (boolDebugEnabled) ErrorHandler.raiseDBAlert("writing Debug strings to the debug file",exp);

        }
    }




    /** This method updates the debug file
     * @param strDebug The debug string which has to be written to the debug file.
     */
    public static synchronized void writeError(String strComponent,String strErrorCode,String strDebug)
    {

        //If debug is not enabled, then return
        if ((!TPlusConfig.boolDebug) && (hssThreads.size() == 0))
            return;

        try {
            //check if the audit log file has reached the file size limit.
            if (!validateFileDate(DebugMsgWriter.intDebugDate))
            {
                startDebugging();
            }

            //write the data to the debug file
            //synchronized (pwDebug)
            //{
            pwDebug.println(sdfDate.format(new Date())+TPlusConfig.LOGITEM_SEPERATOR+Thread.currentThread().getName()+TPlusConfig.LOGITEM_SEPERATOR+"Error Trace");
            pwDebug.println("********************************* Error **********************************");
            pwDebug.println(strComponent+":"+strErrorCode+":"+TPlusCodes.getErrorDesc(strErrorCode)+":Org Err:"+ strDebug);
			pwDebug.println("**************************************************************************");
            pwDebug.flush();
            //}
        }
        catch (Exception exp)
        {

			if (boolDebugEnabled) ErrorHandler.raiseDBAlert("writing Debug strings to the debug file",exp);
        }
    }


    /** This method checks if the date of the log file is the same as todays's date
     * @param intDate the date at which the logfile was last created.
     * @return boolean
     */
    public static boolean validateFileDate(int intDate)
    {
        Calendar calNow = Calendar.getInstance();
        int intToday = Integer.parseInt(calNow.get(Calendar.YEAR)+TPlusUtility.setLength(""+(calNow.get(Calendar.MONTH)+1),2,"0",true)+TPlusUtility.setLength(""+calNow.get(Calendar.DAY_OF_MONTH),2,"0",true));
        if (intToday != intDate)
           return false;
        return true;

    }
}
