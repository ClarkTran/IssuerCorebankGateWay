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

import java.sql.Connection;
import java.sql.DriverManager;


public class DBDirectConnection implements DBConnection
{

	private String strJDBCDriver	= "";
	private String strURLString		= "";
	private String strUserId		= "";
	private String strPassword		= "";

	/**
	* This method sets the Context for the DBPool and
	* creates the DataSource for the Weblogic Connection Pool.
	*/
	public DBDirectConnection(String strJDBCDriver,String strURLString,String strUserId,String strPassword)
	{
		this.strJDBCDriver = strJDBCDriver;
		this.strURLString  = strURLString;
		this.strUserId	   = strUserId;
		this.strPassword   = strPassword;
	}

	/**
	 * This method gets the connection from the Weblogic Connection Pool.
	 * @retrun Connection
	 */
	public Connection getConnection() throws Exception
	{

		try
		{
			Class.forName(strJDBCDriver);
			Connection con = DriverManager.getConnection(strURLString,strUserId,strPassword);
			return con;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * This method gets the connection from the Weblogic Connection Pool.
	 * @param Conenction. This connection is returned to the Weblogic Connection Pool.
	 * @retrun void
	 */
	public void closeConnection(Connection con) throws Exception
	{
		try
		{
			con.close();
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void closeDBPool() throws Exception
	{
		try
		{
			System.out.println("NO DB Pool");
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
}


