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

package com.transinfo.tplus.javabean;

import java.util.ArrayList;

/**
 * This class contains get and set method for all its member variables.
 * This class implements the java.io.Serializable
 */

public class AlertDataBean implements java.io.Serializable
{
	private String strAlertName;
	private String strAlertCode;
	private String strUserId;
	private String strStatus;
	private String strEmailSub;
	private String strEmailBody;
	private String strEmailSign;
	private String strMerchantId;

	private ArrayList arlUserBean=new ArrayList();

   /**
	* Access method for the strAlertName property.
	* @return the current value of the strAlertName property
    */
	public String getAlertName()
	{
		return strAlertName;
	}

   /**
	* Sets the value of the strAlertName property.
	* @param strAlertName the new value of the strAlertName property
	*/
	public void setAlertName(String strAlertName)
	{
		this.strAlertName=strAlertName;
	}

   /**
	* Access method for the strAlertCode property.
	* @return the current value of the strAlertCode property
    */
	public String getAlertCode()
	{
		return strAlertCode;
	}

   /**
	* Sets the value of the strAlertCode property.
	* @param strAlertCode the new value of the strAlertCode property
	*/
	public void setAlertCode(String strAlertCode)
	{
		this.strAlertCode=strAlertCode;
	}

   /**
	* Access method for the strUserId property.
	* @return the current value of the strUserId property
    */
	public String getUserId()
	{
		return strUserId;
	}

   /**
	* Sets the value of the strUserId property.
	* @param strUserId the new value of the strUserId property
	*/
	public void setUserId(String strUserId)
	{
		this.strUserId=strUserId;
	}

   /**
	* Access method for the strMerchantId property.
	* @return the current value of the strMerchantId property
    */
	public String getMerchantId()
	{
		return strMerchantId;
	}

   /**
	* Sets the value of the strMerchantId property.
	* @param strMerchantId the new value of the strMerchantId property
	*/
	public void setMerchantId(String strMerchantId)
	{
		this.strMerchantId=strMerchantId;
	}


   /**
	* Access method for the strStatus property.
	* @return the current value of the strStatus property
    */
	public String getStatus()
	{
		return strStatus;
	}

   /**
	* Sets the value of the strStatus property.
	* @param strStatus the new value of the strStatus property
	*/
	public void setStatus(String strStatus)
	{
		this.strStatus=strStatus;
	}

   /**
	* Access method for the strEmailSub property.
	* @return the current value of the strEmailSub property
    */
	public String getEmailSub()
	{
		return strEmailSub;
	}

   /**
	* Sets the value of the strEmailSub property.
	* @param strEmailSub the new value of the strEmailSub property
	*/
	public void setEmailSub(String strEmailSub)
	{
		this.strEmailSub=strEmailSub;
	}

   /**
	* Access method for the strEmailBody property.
	* @return the current value of the strEmailBody property
    */
	public String getEmailBody()
	{
		return strEmailBody;
	}

   /**
	* Sets the value of the strEmailBody property.
	* @param strEmailBody the new value of the strEmailBody property
	*/
	public void setEmailBody(String strEmailBody)
	{
		this.strEmailBody=strEmailBody;
	}

   /**
	* Access method for the strEmailSign property.
	* @return the current value of the strEmailSign property
    */
	public String getEmailSign()
	{
		return strEmailSign;
	}

   /**
	* Sets the value of the strEmailSign property.
	* @param strEmailSign the new value of the strEmailSign property
	*/
	public void setEmailSign(String strEmailSign)
	{
		this.strEmailSign=strEmailSign;
	}

   /**
	* Access method for the hsUser property.
	* @return the current value of the hsUser property
    */
	public ArrayList getUsers()
	{
		return arlUserBean;
	}

   /**
	* Sets the value of the hsUser property.
	* @param hsUser the new value of the hsUser property
	*/
	public void setUsers(ArrayList arlUserBean)
	{
		this.arlUserBean=arlUserBean;
	}

   /**
	* Adds the UserDataBean to the ArrayList.
	* @param objUserBean the new value of the objUserBean property
	*/
/*	public void addUser(UserDataBean objUserBean)
	{
		arlUserBean.add(objUserBean);
	}*/


}