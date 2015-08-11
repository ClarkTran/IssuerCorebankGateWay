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


import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This class is wrapper over api ResultSet and this class is support class for
 * RSC_DbManager.java.
 * This class corresponds to set of rows of data. This corresponds to a table
 * @author ITSolutions
 * @version 1.00
 * @see Object
 */
 public class TPlusResultSet implements java.io.Serializable {
	private ArrayList 		arlResultset;
	private int 			iCurrentRowIndex;
	private TPlusResultRow 	objCurrentRow;

	//Constructor
	TPlusResultSet()	{
		arlResultset = new ArrayList();
		iCurrentRowIndex = 0;
	}

   /**
	* Adds a RSC_ResultRow object to the arraylist
	* @param objRow : Object of type RSC_ResultRow that is added to the
	*				   arraylist
	* @return void :
	*/

	public void addRow(TPlusResultRow objRow) {
		arlResultset.add(objRow);
	}

   /**
	* Determines whether an next object exists in the arraylist with respect
	* to current index
	* @return boolean : Returns true if exists and else returns false
	*/

	public boolean next() {
		if (iCurrentRowIndex <= arlResultset.size() - 1) {
			objCurrentRow = (TPlusResultRow) arlResultset.get(
												iCurrentRowIndex++);
			return true;
		} else {
			return false;
		}
	}

   /**
	* Returns the integer value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return int: Returns the value of the column
	*/
	public int getInt(String strColumnName) throws NullPointerException	{
		return objCurrentRow.getInt(strColumnName);
	}


   /**
	* Returns the integer value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return int: Returns the value of the column
	*/
	public long getLong(String strColumnName) throws NullPointerException	{
		return objCurrentRow.getLong(strColumnName);
	}

   /**
	* Returns the String value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return String		: Returns the value of the column
	*/

	public String getString(String strColumnName) throws NullPointerException {
		return objCurrentRow.getString(strColumnName);
	}

   /**
	* Returns the Blob value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return Blob: Returns the value of the column
	*/

	public Blob getBlob(String strColumnName)
									throws SQLException, NullPointerException {
		return objCurrentRow.getBlob(strColumnName);
	}

   /**
	* Returns the SQL Date object for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return java.sql.Date: Returns the value of the column
	*/

	public java.sql.Date getDate(String strColumnName) throws NullPointerException {
		return objCurrentRow.getDate(strColumnName);
	}

   /**
	* Returns the Clob value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return Clob: Returns the value of the column
	*/

	public Clob getClob(String strColumnName)
									throws SQLException, NullPointerException {
		return objCurrentRow.getClob(strColumnName);
	}

   /**
	* Returns the double value for the column
	* @param strColumnName : Name of column for which the value is desired
	* @return double: Returns the value of the column
	*/
	public double getDouble(String strColumnName) throws NullPointerException {
		return objCurrentRow.getDouble(strColumnName);
	}

   /**
	* Returns the number of elements in the arraylist
	* @return int: returns the count
	*/

	public int size() {
		return arlResultset.size();
	}
}