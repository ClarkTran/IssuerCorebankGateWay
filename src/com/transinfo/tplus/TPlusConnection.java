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



/**
 * This interface is should be implements by Visa and ACS connection wrappers.
 */
public interface TPlusConnection
{
	/**
	* This method creates a connection to the server
	* @return String The Version for the server
	* @throws TPlusException if there is any exception
	*/
	public String getConnection() throws TPlusException;

	/**
	* This method sets the version for the connected url.
	* @param strVersion - The version to be updated
	* @throws TPlusException if there is any exception
	*/
	public void setVersionForURL(String strVersion) throws TPlusException;

	/**
	* This method starts of one threads one for connecting to Visa Dir and the main thread
	* waits. This is required for connection timeout.
	* @param strXMLData is the XML string that has to be posted to the Visa Dir
	* @return The XML response string from the Visa Dir
	* @exception TPlusException when there is a problem with connection or if the connection has timed out.
	*/
	public String postRequest(String strXMLData) throws TPlusException;

	/**
	* This method creates a connection to send the error XML
	* @throws TPlusException if there is any exception from the while processing.
	*/
	public void getConnectionForError() throws TPlusException;

	/**
	* This method returns the URL to which the connection is made.
	* @return String - The URL.
	*/
	public String getConnectionURL();

}





