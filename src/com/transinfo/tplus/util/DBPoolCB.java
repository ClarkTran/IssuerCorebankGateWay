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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.transinfo.tplus.TPlusConfig;

public class DBPoolCB  implements DBConnection
{

	private DataSource dataSource = null; // This holds the DataSource.
	//private BasicDataSource ds = null;

	/**
	 * This method sets the Context for the DBPool and
	 * creates the DataSource for the Weblogic Connection Pool.
	 */
	public DBPoolCB()
	{

		BasicDataSource ds = new BasicDataSource();

		ds.setDriverClassName(TPlusConfig.JDBCDriver);
		System.out.println(TPlusConfig.JDBCURLCB+"   "+TPlusConfig.DBUserIDCB+"  "+TPlusConfig.DBPasswordCB);
		ds.setUrl(TPlusConfig.JDBCURLCB);
		ds.setUsername(TPlusConfig.DBUserIDCB);
		ds.setPassword(TPlusConfig.DBPasswordCB);
		ds.setInitialSize(Integer.parseInt(TPlusConfig.DBInitSizeCB));
		ds.setMaxActive(Integer.parseInt(TPlusConfig.DBMaxActiveCB));
		ds.setMaxWait(Integer.parseInt(TPlusConfig.DBMaxWaitCB));

		dataSource =(DataSource)ds;

		/*BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        ds.setUsername("cabcb");
        ds.setPassword("cabcb");
        ds.setInitialSize(5);
        ds.setMaxActive(5);
        ds.setMaxWait(5);
        dataSource =(DataSource)ds;*/

	}

	/**
	 * This method gets the connection from the Connection Pool.
	 * @retrun Connection
	 */
	public Connection getConnection() throws Exception
	{
		try
		{
			//System.out.println("ds.getNumActive() in getConnection method :: " + ds.getNumActive());
			return(dataSource.getConnection());
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * This method gets the connection from the Connection Pool.
	 * @param Conenction. This connection is returned to the Connection Pool.
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

	public void closeDBPool() throws SQLException
	{
		BasicDataSource bds = (BasicDataSource) dataSource;
		bds.close();
	}

	public static void main(String s[])throws Exception
	{
		//DBPool pool = new DBPool();
		Class.forName ("oracle.jdbc.driver.OracleDriver");

		// specify the ODBC data source's URL
		@SuppressWarnings("unused")
		String url = "jdbc:odbc:SSPer";

		// connect
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","ticasold","ticasold");


		//Connection con= pool.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from tab");
		if(rs.next())
		{
			System.out.println(rs.getString(1));
		}
		else
		{
			System.out.println("Problem");
		}
	}





}


