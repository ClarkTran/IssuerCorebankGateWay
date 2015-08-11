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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import oracle.jdbc.driver.OracleTypes;
import oracle.sql.ARRAY;

import com.transinfo.tplus.ErrorHandler;
import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;

/**
 *  This is utility class to used to get conenction. This class has utlity
 *  methods to execute the query.
 *  It holds a static object objDBPool which is
 *  an interface. This interface has methods to get a connection
 *  and return a connection. The implemeting interface has the freedom of
 *  choice of the driver and conenction pool etc.
 */			    //Object.

@SuppressWarnings({ "unchecked", "serial","unused" })
public class DBManager implements java.io.Serializable
{

	protected static DBConnection objDBPool = null; //This static variable holds the Connection Pool

	private java.util.ArrayList arlSql;

	private Connection objCon		= null;
	private ResultSet objrs			= null ;
	private ResultSet rs			= null;
	private Statement stmtSQL		= null;

	private int iUpdCount;
	private TPlusResultSet objReusableRset;
	protected ARRAY simpleArray=null;
	private static boolean boolPoolStatus=false;


	private static final String TPlus_ARRAY_TYPE = "TPlus_ARRAY_TYPE";


	/**
	 * Constructor is to initialize all the member variables.
	 */
	public DBManager()
	{
		this.objReusableRset = new TPlusResultSet();
		this.objCon = null;
		this.arlSql = new ArrayList();
	}


	/**
	 * This method inits the DB Pool Connections.
	 */
	public static void initDBPool() throws TPlusException
	{
		try
		{

			if (objDBPool == null)
			{
				objDBPool = new DBPool();
				boolPoolStatus=true;
			}

		}
		catch(Exception exp)
		{
			throw new TPlusException(TPlusCodes.DB_POOL_FAIL,exp.toString());
		}
	}

	/**
	 * Opens a connection to the database.
	 * @return void
	 * @Exception Exception
	 */
	public void openConnection() throws TPlusException
	{
		try
		{
			objCon = objDBPool.getConnection();
		}
		catch (Exception expex)
		{
			try
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("DBManager : Fatal Error DataBase Connection Failure.");
				//modified the next by Rajeev on 2 jan 02 to send the Email with the error message
				System.out.println("DB Exception="+ expex);
				ErrorHandler.raiseDBAlert(TPlusCodes.DATABASE_CONN_ERR,expex);

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("DBManager : Alert Email Send using Default values");
			}
			catch(Exception e)
			{
				throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,expex.getMessage());
			}
			//modified the next by Rajeev on 2 jan 02 to correct the error if the exception is not thrown properly
			throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,expex.getMessage());
		}
	}

	/**
	 * Closes the database connection
	 * @return void
	 * @Exception Exception
	 */
	public void closeConnection() throws TPlusException
	{
		try
		{
			if (stmtSQL != null)
			{
				stmtSQL.close();
				stmtSQL = null;
			}

			if (objrs != null)
			{
				objrs.close();
				objrs = null;
			}

			if (rs != null)
			{
				rs.close();
				rs = null;
			}

		} catch (SQLException sqlex)
		{
			throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,sqlex.getMessage());
		}
		finally
		{
			try
			{
				if(objCon!=null)
				{
					objDBPool.closeConnection(objCon);
					objCon = null;
				}

			}
			catch(Exception expex)
			{
				throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,expex.getMessage());
			}
		}
	}

	/**
	 * This method executes sqls queries passed as string
	 * @param String		: the SQL string to be executed
	 * @return boolean	: True, if execution was success else false
	 * @Exception Exception
	 */
	public boolean executeSQL(String strSql)throws TPlusException
	{
		objReusableRset = new TPlusResultSet();
		boolean bExec = false;
		try
		{
			openConnection();
			stmtSQL=objCon.createStatement();
			bExec = stmtSQL.execute(strSql);
			if (bExec)
			{
				objrs = stmtSQL.getResultSet();
				objReusableRset = getTPlusResultSet(objrs);
			}
			else
			{
				iUpdCount = stmtSQL.getUpdateCount();
				bExec =true;
			}
		}
		catch (SQLException sqlex)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex.getMessage());
		}
		finally
		{
			closeConnection();
		}
		return bExec;
	}

	/**
	 * This method executes sqls queries passed as string
	 * @param String: the SQL string to be executed
	 * @return int	: row count for Insert, Update or Delete or 0 for SQL
	 *				  statements that return nothing.
	 * @Exception SQLException, Exception
	 */
	public int executeUpdate(String strSql)throws TPlusException
	{
		objReusableRset = new TPlusResultSet();
		int iExec = 0;
		try
		{
			openConnection();
			stmtSQL=objCon.createStatement();
			iExec = stmtSQL.executeUpdate(strSql);

			/*if (iExec > 0)
			{
				objrs = stmtSQL.getResultSet();
				ResultSetMetaData objMDRS = objrs.getMetaData();
				int iColCount = objMDRS.getColumnCount();
				String aColName[] = new String[iColCount + 1];
				for (int i = 1; i <= iColCount; i++)
				{
					aColName[i] = objMDRS.getColumnName(i);
				}

				while (objrs.next())
				{
					TPlusResultRow reusableRS = new TPlusResultRow();
					for (int i = 1; i <= iColCount; i++)
					{
						reusableRS.addColumn(aColName[i],
						objrs.getObject(aColName[i]));
					}
					objReusableRset.addRow(reusableRS);
				}

			}*/
			return iExec;

		}
		catch (SQLException sqlex)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex.getMessage());
		}
		finally
		{
			closeConnection();
		}

	}

	/**
	 * This method executes all the SQL queries in the arraylist
	 * @return boolean 	: True, if execution was success else false
	 * @Exception Exception
	 */
	public boolean executeAllSQLs() throws TPlusException
	{
		String strSQL = "";
		boolean bExec = false;
		java.sql.ResultSet objrs = null;
		java.sql.Statement stmtSQL = null;

		try
		{
			openConnection();
			//Set autocommit to false
			objCon.setAutoCommit(false);
			boolean blnFlag = false;
			for (int iCounter = 0; iCounter < arlSql.size(); iCounter++)
			{
				strSQL = (String) arlSql.get(iCounter);
				stmtSQL=objCon.createStatement();
				bExec = stmtSQL.execute(strSQL);

				if (!bExec)
				{
					iUpdCount = stmtSQL.getUpdateCount();
					bExec =true;
				}
				try
				{
					//objrs.close() ;
					stmtSQL.close ();
				}
				catch (Exception ex)
				{
					ex.printStackTrace ();
				}

				if (!bExec)
				{

					objCon.rollback();
					objCon.setAutoCommit(true);
					closeConnection();
					return bExec;
				}
			}
			//Set Commit Transcation
			objCon.commit();
		} catch (SQLException sqlex)
		{
			try
			{

				objCon.rollback();
				objCon.setAutoCommit(true);
				// Version 1.03 Changes Starts
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex.getMessage());
				// Version 1.03 Changes Ends

			}
			catch ( SQLException sqlex1)
			{
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex1.getMessage());
			}
		}
		finally
		{
			try
			{
				objCon.setAutoCommit(true);
			}
			catch ( SQLException sqlex1)
			{
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex1.getMessage());
			}
			closeConnection();
		}
		return bExec;
	}

	/**
	 * This method executes all the SQL queries in the arraylist
	 * @returns true if the SQL Execute is success full.
	 * @Exception Exception
	 */
	public ArrayList executeMultipleSelect(ArrayList arlMultiSelect) throws TPlusException
	{
		String strSQL = "";

		ArrayList arlMultiOut = new ArrayList();

		java.sql.ResultSet objrs = null;
		java.sql.Statement stmtSQL = null;

		try
		{
			openConnection();
			int intArlSize = arlMultiSelect.size();
			for (int iCounter = 0; iCounter < intArlSize; iCounter++)
			{

				strSQL = (String) arlMultiSelect.get(iCounter);
				System.out.println("MultSql="+strSQL);
				stmtSQL=objCon.createStatement();
				objrs = stmtSQL.executeQuery(strSQL);
				arlMultiOut.add(getTPlusResultSet(objrs));

				//Added by Ankur closing the ResultSet
				if (objrs != null)
					objrs.close();
				//Added by Ankur closing the ResultSet

			}
		} catch (Exception ex)
		{System.out.println("Exce[tioop="+ex);
		try
		{
			if (stmtSQL != null)
				stmtSQL.close();
			if (objrs != null)
				objrs.close();
		}
		catch ( SQLException sqlex1)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,sqlex1.getMessage());
		}
		throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,ex.getMessage());
		}
		finally
		{
			closeConnection();
		}
		return arlMultiOut;
	}
	/**
	 * This convert the ResultSet into TPlusResultSet
	 * @param ResultSet
	 * @returns TPlusResultSet
	 * @Exception Exception
	 */
	private TPlusResultSet getTPlusResultSet(ResultSet objResultSet) throws TPlusException
	{

		TPlusResultSet TPlusResultSet = new TPlusResultSet();
		try
		{
			ResultSetMetaData objMDRS = objResultSet.getMetaData();
			int iColCount = objMDRS.getColumnCount();
			String aColName[] = new String[iColCount + 1];
			for (int i = 1;i <= iColCount; i++)
			{
				aColName[i] = objMDRS.getColumnName(i);
			}
			while (objResultSet.next())
			{
				TPlusResultRow reusableRS = new TPlusResultRow();
				for (int i = 1; i <= iColCount; i++)
				{
					reusableRS.addColumn(aColName[i],objResultSet.getObject(aColName[i]));
				}
				TPlusResultSet.addRow(reusableRS);
			}
		}catch (Exception expex)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,expex.getMessage());
		}
		return TPlusResultSet;
	}
	/**
	 * This method executes the Stored Procedures
	 * @return boolean 	: True, if execution was success else false
	 * @throws Exception
	 */
	public boolean executeSP(String strSp)throws Exception
	{
		java.sql.CallableStatement csStoredProcedure = null;
		try
		{
			openConnection();
			String strSql = "{call " + strSp + "}";
			csStoredProcedure = objCon.prepareCall("{call " + strSp + "}");
			csStoredProcedure.execute();
			return true;
		}
		catch (SQLException sqlex)
		{
			throw sqlex;
		}
		finally
		{
			closeConnection();
		}
	}

	/**
	 * This method returns the ResultSet object that was populated
	 * due the execution of query
	 * @return ResultSet 	: the populated TPlus_ResultSet object
	 */
	public TPlusResultSet getResultSet()
	{
		return objReusableRset;
	}

	/**
	 * This method returns the count of records that were effected due to
	 * execution of action query
	 * @return ResultSet 	: the populated TPlus_ResultSet object
	 */
	public int getUpdateCount()
	{
		return iUpdCount;
	}

	/**
	 * Adds the SQL string to the arraylist to be executed later by the
	 * executeALL method
	 * @param String : The SQL String that is added to the arraylist
	 * @return void 	:
	 */
	public void addSQL(String strSql)
	{
		arlSql.add(strSql);
	}

	/**
	 * Adds the two arrays of SQL strings to the arraylist to be executed later
	 * by the executeALL method
	 * @param ArrayList : The Array of Strings that are added to the arraylist
	 * @return void 	:
	 */
	public void addAllSQL(ArrayList arl)
	{
		for (int iCounter = 0; iCounter < arl.size(); iCounter++)
		{
			String strSQL = (String) arl.get(iCounter);
			arlSql.add(strSQL);
		}
	}

	/**
	 * Clears the arraylist that contains the list of SQLs
	 * @return void 	:
	 */

	public void removeAllSqls()
	{
		arlSql.clear();
	}

	/**
	 * This method executes a stored procedure. All the input values is assumed to be
	 * String and out put values ses the datatype using the 3 parameter.
	 * This method executes the Stored Procedures
	 * @param strSPName The stored procedure name.
	 * @param The Input parameter in the order in which the stored procedure accepts.
	 * @param Output DataType.
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList executeSP(String strSPName,ArrayList arlInput,ArrayList arlOutData)throws Exception
	{
		//Decalre method variables.
		java.sql.CallableStatement csStoredProcedure = null;
		ArrayList arlOutput=new ArrayList();
		String str=new String();

		try
		{
			// Open a connection to the database.
			openConnection();
			int intOutCount=0;
			int intInputCount=0;

			//Assign the input array sizes,
			if(arlOutData!=null)
			{
				intOutCount   = arlOutData.size();
			}
			if(arlInput!=null)
			{
				intInputCount = arlInput.size();
			}

			// Create the stored procedure calling SQL statement.
			StringBuffer sbrProcedure = new StringBuffer();
			int index = 0;
			sbrProcedure.append("{call "+ strSPName +"(");
			/**
			 * Assign the input variables to the stored procedure.
			 */


			while(index < intInputCount)
			{
				sbrProcedure.append("?");
				index++;
				if(index <= intInputCount - 1)
				{
					sbrProcedure.append(",");
				}

			}


			/**
			 * Assign the output variables to the stored procedure.
			 */

			index=0;
			while(index < intOutCount)
			{
				if (index == 0 && intInputCount == 0)
				{
					sbrProcedure.append("?");
				}
				else
				{
					sbrProcedure.append(", ?");
				}
				index++;
			}

			sbrProcedure.append(")");
			sbrProcedure.append("}");
			csStoredProcedure = objCon.prepareCall(sbrProcedure.toString());

			/**
			 * Register the stored procedure input variables.
			 * This method supports only strings as the input variables.
			 */
			for(index=0;index < intInputCount;index++)
			{
				csStoredProcedure.setString(index+1,(String) arlInput.get(index));
			}

			/**
			 * Register the stored procedure output variables.
			 * This method supports strings, Ref curors and arays as out put parameters.
			 */
			//int intTotVar = intInputCount + intOutCount;

			for(int i=0;i<intOutCount;i++)
			{
				int intProcIndex = i + intInputCount + 1;
				int itnDataType;
				try
				{
					itnDataType = Integer.parseInt(arlOutData.get(i).toString());
				}
				catch(Exception expex)
				{
					throw new TPlusException(TPlusCodes.UNKNOWN_DATA_TYPE ,expex.getMessage());
				}
				switch(itnDataType)
				{
				case java.sql.Types.ARRAY :
					csStoredProcedure.registerOutParameter(intProcIndex,OracleTypes.ARRAY,TPlus_ARRAY_TYPE);
					break;
				case java.sql.Types.REF :
					csStoredProcedure.registerOutParameter(intProcIndex,OracleTypes.CURSOR);
					break;
				case OracleTypes.VARCHAR :
					csStoredProcedure.registerOutParameter(intProcIndex,java.sql.Types.VARCHAR);
					break;
				default :
					throw new TPlusException(TPlusCodes.UNKNOWN_DATA_TYPE);
				}//End of switch.

			}//end for loop

			// Execute the stored procedure.
			ResultSetMetaData rsMetaData;
			int itnColumnCount;
			csStoredProcedure.execute();

			for(int i=0;i<intOutCount;i++)
			{

				int itnDataType = Integer.parseInt(arlOutData.get(i).toString());
				int intProcIndex = intInputCount + i+ 1;

				switch(itnDataType)
				{
				case java.sql.Types.ARRAY :
				{
					simpleArray=(ARRAY) csStoredProcedure.getObject(intProcIndex);
					String strCellValue = "";
					ArrayList arlArrayOut = new ArrayList();
					String values[]=(String[])simpleArray.getArray();
					for (int k= 0; k < values.length; k++)
					{
						if(values[k]==null)
						{
							break;
						}
						StringTokenizer stringTokenizer = new StringTokenizer(values[k],"^^");
						ArrayList arlArrayRow = new ArrayList();
						while(stringTokenizer.hasMoreTokens())
						{
							strCellValue = stringTokenizer.nextToken().toString();
							if ((strCellValue != null) && (!strCellValue.trim().equals("")))
							{
								arlArrayRow.add(strCellValue);
							}
							else
							{
								arlArrayRow.add("");
							}
						}

						arlArrayOut.add(arlArrayRow);

					}
					arlOutput.add(arlArrayOut);
				}
				break;

				case java.sql.Types.REF :
				{
					ArrayList arlArrayOut = new ArrayList();

					rs = (ResultSet)  csStoredProcedure.getObject(intProcIndex);
					rsMetaData = rs.getMetaData();

					int intColumnCount = rsMetaData.getColumnCount();
					while (rs.next())
					{
						ArrayList arlArrayRow = new ArrayList();
						for (int loop = 1; loop <= intColumnCount; loop++)
						{
							String strCellValue = rs.getString(rsMetaData.getColumnName(loop));
							if (strCellValue != null)
							{
								arlArrayRow.add(strCellValue);
							}
							else
							{
								arlArrayRow.add(" ");
							}
						}
						arlArrayOut.add(arlArrayRow);
					}
					arlOutput.add(arlArrayOut);
				}
				break;

				case OracleTypes.VARCHAR :
				{
					String strVal=(String) csStoredProcedure.getObject(intProcIndex);
					arlOutput.add(strVal);
				}
				break;

				default:
					throw  new TPlusException(TPlusCodes.UNKNOWN_DATA_TYPE);
				}//end of switch

			}//end of for

		}
		catch (SQLException e)
		{
			throw e;
		}
		catch(Exception expex)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR, expex.getMessage());
		}
		finally
		{
			closeConnection();
		}
		return arlOutput;
	}

	/**
	 * This method is used while using prepared Statements
	 * This method returns the connection.
	 * @return Connection	: returns the created Connection
	 * @throws Exception
	 */

	public Connection getConnection() throws TPlusException
	{
		try
		{
			openConnection();
		}
		catch (Exception excep)
		{
			throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,excep.getMessage());
		}

		return objCon;

	}


	/**
	 * This method is used while using prepared Statements
	 * This method closes the connection.
	 * @param Connection	: Connection object
	 * @throws Exception
	 */

	public void closeConnection(Connection con) throws Exception
	{
		objCon=con;
		closeConnection();

	}


	/**
	 * This method is used to close the DB pool for certain instance
	 *
	 **/

	public static void closeDBPool()throws Exception
	{

		objDBPool.closeDBPool();
		boolPoolStatus=false;
	}

	/**
	 * This method is used to known the current status of the DB Pool
	 * It tell whether pool is running or not
	 * @return TRUE or FALSE
	 */

	public static boolean getPoolStatus()
	{
		return boolPoolStatus;
	}



	public static void main(String s[])
	{
		try
		{
			System.out.println("777777777777777777777777777777777");
			initDBPool();
			System.out.println("777777777777777777777777777777777");
			DBManager db = new DBManager();
			Connection conn = db.getConnection();
			System.out.println("777777777777777777777777777777777");
			Statement stmt = conn.createStatement();
			Thread.sleep(10000);

			boolean result = db.executeSQL("select count(*) as count from tab");
			TPlusResultSet rset = db.getResultSet();
			System.out.println("Results:");
			// int numcols = rset.getMetaData().getColumnCount();
			// System.out.println("No of Cols="+numcols);
			if(rset.next())
			{

				System.out.print("\t" + rset.getString("count"));

			}
			else
			{
				System.out.println("No Data");
			}

		}catch(Exception e){System.out.println("*********error="+e);}
	}



}