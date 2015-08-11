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

package com.transinfo.tplus.util;


import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
* This class is wrapper over api ResultSet and supporting class for ResultSet.java.
* This class internally uses a HashMap which is not synchronized.
*/
public class TPlusResultRow implements java.io.Serializable
{
	private HashMap hmReusableRS;

	TPlusResultRow()
	{
		this.hmReusableRS = new HashMap();
	}
	/**
	* Adds a column name and value of the column to the hashtable.
	* @param strColumnName : Name of column for which the value is added
	* @param objColumnValue: The value of column in the form of object
	* @return void
	*/
	public void addColumn(String strColumnName, Object objColumnValue)
	{
		Object obj = (objColumnValue != null) ? this.hmReusableRS.put(
		strColumnName.toUpperCase(),objColumnValue):
		this.hmReusableRS.put(strColumnName, new String());
	}

	/**
	* Returns the integer value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return int: Returns the value of the column
	*/
	public int getInt(String strColumnName) throws NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();

		return (strTemp.equalsIgnoreCase(null)) ? -1 :
			((BigDecimal) hmReusableRS.get(strColumnName)).intValue();
	}

	/**
	* Returns the long value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return long: Returns the value of the column
	*/
	public long getLong(String strColumnName) throws NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();

		return (strTemp.equalsIgnoreCase(null)) ? -1 :
			((BigDecimal) hmReusableRS.get(strColumnName)).longValue();
	}

	/**
	* Returns the double value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return double: Returns the value of the column
	*/
	public double getDouble(String strColumnName) throws NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();

		return (strTemp.equalsIgnoreCase(null)) ?  -1 :
			((BigDecimal) hmReusableRS.get(strColumnName)).doubleValue();
	}

	/**
	* Returns the Blob value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return Blob: Returns the value of the column if it contains other than
	*			    null else returns null.
	*/
	public Blob getBlob(String strColumnName) throws SQLException, NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();

		return (strTemp.equalsIgnoreCase(null)) ?  null :
			(Blob) ((Blob) hmReusableRS.get(strColumnName)).getBinaryStream();
	}

	/**
	* Returns the Clob value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return Clob: Returns the value of the column if it contains other than
	*				   null else returns null.
	*/
	public Clob getClob(String strColumnName)throws SQLException, NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();

		return (strTemp.equalsIgnoreCase(null)) ?  null :
			(Clob) ((Clob) hmReusableRS.get(strColumnName)).getAsciiStream();
	}

	/**
	* Returns the String value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return String: Returns the value of the column, null string,
	*				  When column name is null.
	*/
	public String getString(String strColumnName) throws NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();
		return (strTemp.equalsIgnoreCase(null)) ? new String() :
			(String) hmReusableRS.get(strColumnName).toString();
	}

	/**
	* Returns the SQL Date object for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return java.sql.Date: Returns the value of the column
	*/
	public java.sql.Date getDate(String strColumnName) throws NullPointerException
	{
		strColumnName = strColumnName.toUpperCase();
		String strTemp = hmReusableRS.get(strColumnName).toString();
		if (strTemp.equalsIgnoreCase(null))
		{
			return new java.sql.Date(new java.util.Date("01/01/1800").getTime());
		}
		else
		{
			java.util.Date dtTemp = (java.util.Date) hmReusableRS.get(strColumnName);
			return  (new java.sql.Date(dtTemp.getTime()));
		}
	}

	/**
	* This function overrides the toString() method of the Object class.
	* This is generally used for debugging
	* @return String: Returns the string
	*/
	public String toString()
	{
		try
		{
			//PrintStream PS = new PrintStream(new FileOutputStream("E.txt",true));
			String str = "";
			Set set = hmReusableRS.keySet();
			Iterator itr = set.iterator();
			while (itr.hasNext())
			{
				str  = (String) itr.next();
			}
			return "";
		}
		catch(Exception expExp)
		{
			return "";
		}
	}
}