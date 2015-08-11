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
package com.transinfo.threadpool;

/*
 * This class handles various Exception thrown by the Thread Pool
 */

public class ThreadPoolException extends Exception
{
	/**
	* The default exception message.
	*/
	public ThreadPoolException()
	{
		super("Thread Pool Exception");
	}
	/**
	* The sets the exception message as the input string.
	*/
	public ThreadPoolException(String msg)
	{
		super(msg);
	}

}
