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

/**
* The implementing class has the freedom for get a connection
* from existing connection pool or opening a new connection.
*
*/
public interface DBConnection
{
	/**
	 * This method gets a connection to the Database
	 */
	public Connection getConnection() throws Exception;

	/**
	 * This method closes the connection to the Database.
	 */
	public void closeConnection(Connection con) throws Exception;

	/**
	 * This method closes the close the Connection Pool
	 */
	 public void closeDBPool() throws Exception;


}


