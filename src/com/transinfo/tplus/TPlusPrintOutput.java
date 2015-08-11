

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


import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * This class formats & prints the output/stack to the console.
 *
 */

public class TPlusPrintOutput
{

	// The TPlus product name
	private static String strTPlus = "ATMSwitch";
	// Use the same date format as the debug log file
	private static String strFormat = TPlusConfig.strDateFormat;
	private static SimpleDateFormat sdfCurrDate = null;

	static{
		sdfCurrDate = new SimpleDateFormat(strFormat);
	}

   /*
	* Print the string in a format with the product name and the component name
	* and the message.
	* @param strComponent   The name of the component/class from where the message
	*						has to be printed.
	* @param strMessage		The message to be printed
	*/
	public static void printMessage(String strComponent, String strMessage){

		System.out.println(sdfCurrDate.format(new Date()) + ":" + strComponent.trim() + ": " + strMessage.trim());

	}


   /*
	* Print the string that is passed to the method
	* @param strMessage		The message to be printed
	*/
	public static void printMessage(String strMessage){

		System.out.println(strMessage);

	}

   /*
	* Print the stack trace after formatting
	* @param Exception   	The excetion whose stack trace is to be printed
	*/
	public static void printErrorMessage(String strComponent,String strErrorCode, String strErrorMsg){

		System.out.println(sdfCurrDate.format(new Date()) + ":" + "Error trace");
		System.out.println("********************************* Error **********************************");
		System.out.println(strComponent+":"+strErrorCode+":"+TPlusCodes.getErrorDesc(strErrorCode)+":Org Err:"+ strErrorMsg);
		System.out.println("**************************************************************************");
	}



   /*
	* Print the stack trace after formatting
	* @param Exception   	The excetion whose stack trace is to be printed
	*/
	public static void printStackMessage(Exception ex){

		System.out.println(sdfCurrDate.format(new Date()) + ":" + "Error trace");
		System.out.println("********************************* Error **********************************");
		System.out.println(ex.getMessage());
		System.out.println("**************************************************************************");
	}


   /**
	* Returns the string in a format with the product name and the component name
	* and the message.
	* @param strComponent   The name of the component/class from where the message
	*						has to be printed.
	* @param strMessage		The message to be printed
	* @return String - The Formatted Message.
	*/

	public static String formatMessage(String strComponent, String strMessage)
	{
		return (sdfCurrDate.format(new Date()) + ":" + strComponent.trim() + ": " + strMessage.trim());
	}


}