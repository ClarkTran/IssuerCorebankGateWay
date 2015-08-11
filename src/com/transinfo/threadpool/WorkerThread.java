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


/**
 *  This class is the Worker Thread in the Thread Pool.
 */
public class WorkerThread extends Thread
{
	private static int threadCount = 1; // It is used to generate the thread ID.
	private int threadID;

	private boolean boolRun = true; 	// This variable will be set to false to stop the thread.
	private Queue requestQueue; 		// Request queue to hold the queue.
	private TPRunnable tprRequest;
        private boolean boolProcessing = false;

	/**
	 * Creates a WorkerThread. The input parameter queue is assigned to the private variable
	 * and this is shared across the WorkerThreads in the thread pool.
	 * It also assigns the thread id to the thread.
	 */
	public WorkerThread(ThreadGroup poolGroup,Queue queue)
	{

		super(poolGroup, "worker");
		threadID = threadCount++;
		this.requestQueue = queue;
		String strTempID = "Th" + threadID;
		this.setName(strTempID);
		this.start();
	}

	/**
	* The overrides the run methdod of the Thread.
	* It fetches the object from the request queue and execute it
	* in an infinite loop.
	*/
	public void run()
	{
		while(boolRun)
		{
			try
			{

				tprRequest = (TPRunnable) requestQueue.fetch();
                                boolProcessing= true;
				tprRequest.run();
                                boolProcessing= false;
				tprRequest = null;
                                //Reduce the no of request being processed count in ThreadPool.
                                ThreadPool.subRequestCount();

			}
			catch(InterruptedException ie)
			{

			}
			catch(Exception e)
			{
			}

		}

	}

	/**
	* The thread exits the run() method gracefully.
	*/
	public void stopThread()
	{
		boolRun = false;
	}


	/**
	 * Returns the thread's ID.
	 */
	public int getThreadID()
	{
		return threadID;
	}

        public boolean getProcessFlag()
        {
            return boolProcessing;
        }

	/**
	* This method will Kill the thread forcefully.
	*
	*/
	public void kill()
	{

		if (tprRequest != null)
		{
			tprRequest.kill();
		}

	        this.stop();

	}
}
