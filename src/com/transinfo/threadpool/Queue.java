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
import java.util.Vector;

/**
 * This class is a queue implementation to store request in the thread pool.
 */

public class Queue
{
	private Vector vctRequest; // The request objects will be store here.
	private boolean boolOpen = true; // When the queue is closed this will set to false.

	/**
	 * Initializes the request queue.
	 */
	public Queue()
	{
		vctRequest = new Vector(10);
	}

	/**
	 * Adds an Object to the Queue. If the queue is already closed, throws a ThreadPoolException.
	 */
	public void add(Object objReqest) throws ThreadPoolException
	{
		if(!boolOpen)
			throw new ThreadPoolException("Queue not Open. Cannot add the request to the queue.");

		// Add the request to the queue and notify all the threads.
		synchronized(vctRequest)
		{
			vctRequest.addElement(objReqest);
			vctRequest.notifyAll();
		}
	}

	/**
	 * Fetches and Removes the next available object in the queue.
	 * This method put the thread to wait when the queue is empty.
	 *
	 */
	public Object fetch() throws InterruptedException
	{
		synchronized(vctRequest)
		{

			// Checks for the size of the request vector is zero
			// if it is zero go to wait state else
			// fetches the object from the queue and return.
			while(vctRequest.size() == 0)
			{
				try
				{
					vctRequest.wait();
				}
				catch(InterruptedException ie)
				{
					throw ie;
				}
			}
			Object objRequest = vctRequest.elementAt(0);
			vctRequest.removeElementAt(0);
			return objRequest;
		}
	}

	/**
	 * Makes the queue close. no more new request is added to the queue.
	 */
	public void closeQueue()
	{
		boolOpen = false;
	}


	/**
	 * Returns the size of the request vector.
	 */
	public int getRequestSize()
	{
		synchronized(vctRequest)
		{
			return vctRequest.size();
		}

	}

        public Vector getRequestQueue()
        {
          return vctRequest;
        }
}