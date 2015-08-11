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

package com.transinfo.tplus.alert;

/**
 * This DataBean has get and set method for all the mail server parameters.
 * .This class implemts java.io.Serializable
 */

public class MailDataBean implements java.io.Serializable
{
	private String strMailServerIP;
	private String strMailServerPort;
	private String strFromEmailId;
	private String strToEmailId;
	private String strMailSubject;
	private String strMailBody;
	private String strMailSign;


   /**
	* Access method for the strMailServerIP property.
	* @return the current value of the strMailServerIP property
    */
    public String getServerIP()
    {
		return strMailServerIP;
	}

   /**
	* Sets the value of the strMailServerIP property.
	* @param strMailServerIP the new value of the strMailServerIP property
	*/
	public void setServerIP(String strMailServerIP)
	{
		this.strMailServerIP=strMailServerIP;
	}

   /**
	* Access method for the strMailServerPort property.
	* @return the current value of the strMailServerPort property
    */
    public String getServerPort()
    {
		return strMailServerPort;
	}

   /**
	* Sets the value of the strMailServerPort property.
	* @param strMailServerPort the new value of the strMailServerPort property
	*/
	public void setServerPort(String strMailServerPort)
	{
		this.strMailServerPort=strMailServerPort;
	}

   /**
	* Access method for the strFromEmailId property.
	* @return the current value of the strFromEmailId property
    */
    public String getFromEmailId()
    {
		return strFromEmailId;
	}

   /**
	* Sets the value of the strFromEmailId property.
	* @param strFromEmailId the new value of the strFromEmailId property
	*/
	public void setFromEmailId(String strFromEmailId)
	{
		this.strFromEmailId=strFromEmailId;
	}

   /**
	* Access method for the strToEmailId property.
	* @return the current value of the strToEmailId property
    */
    public String getToEmailId()
    {
		return strToEmailId;
	}

   /**
	* Sets the value of the strToEmailId property.
	* @param strToEmailId the new value of the strToEmailId property
	*/
	public void setToEmailId(String strToEmailId)
	{
		this.strToEmailId=strToEmailId;
	}

   /**
	* Access method for the strMailSubject property.
	* @return the current value of the strMailSubject property
    */
    public String getMailSubject()
    {
		return strMailSubject;
	}

   /**
	* Sets the value of the strMailSubject property.
	* @param strMailSubject the new value of the strMailSubject property
	*/
	public void setMailSubject(String strMailSubject)
	{
		this.strMailSubject=strMailSubject;
	}

   /**
	* Access method for the strMailBody property.
	* @return the current value of the strMailBody property
    */
    public String getMailBody()
    {
		return strMailBody;
	}

   /**
	* Sets the value of the strMailBody property.
	* @param strMailBody the new value of the strMailBody property
	*/
	public void setMailBody(String strMailBody)
	{
		this.strMailBody=strMailBody;
	}

   /**
	* Access method for the strMailSign property.
	* @return the current value of the strMailSign property
    */
    public String getMailSign()
    {
		return strMailSign;
	}

   /**
	* Sets the value of the strMailSign property.
	* @param strMailSign the new value of the strMailSign property
	*/
	public void setMailSign(String strMailSign)
	{
		this.strMailSign=strMailSign;
	}
}