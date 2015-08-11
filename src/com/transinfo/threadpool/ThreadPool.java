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
* This Thread Pool class holds a collecton of threads and a request queue which
* contains the object which threads need to execute.
*/

public class ThreadPool
{
	private int intMaxThreads; // Max Number of Threads in the pool.
	private WorkerThread[] threadPool; // Collection of threads.
	private Queue requestQueue; // Request Queue.
        private static int intRequestCount = 0;
	private ThreadGroup poolGroup;

	/**
	 * Creates a new ThreadPool with specified no of threads.
	 */
	public ThreadPool(int intMaxThreadCount)
	{
		poolGroup = new ThreadGroup("WorkerGroup");
		this.intMaxThreads = intMaxThreadCount;

		threadPool = new WorkerThread[intMaxThreads];
		requestQueue = new Queue();
		initThreads();
	}

	/**
	 * Creates a default ThreadPool with 2 threads.
	 */
	public ThreadPool()
	{
		this(2);
	}

	/**
	* Creates a new WorkerThread for each thread in the array.
	*/
	private void initThreads()
	{
		for(int i=0;i<intMaxThreads;i++)
		{
		    threadPool[i] = new WorkerThread(poolGroup,requestQueue);
		}
	}

	/**
	 * Adds a new Request to the Queue.
	 *
	 * @throws ThreadPoolException if the request queue is closed.
	 */
	public void addRequest(TPRunnable reqObject) throws ThreadPoolException
	{
		try
		{

			requestQueue.add(reqObject);
            addRequestCount();
		}
		catch(ThreadPoolException tpe)
		{
			throw tpe;
		}
	}

	/**
	 * Closes the Thread Pool. This method does the following.
	 * 1) Stops the threads by calling stopThread() on each worker thread.
	 * 2) Closes the request queue.
	 */
	public void closePool()
	{
		stopThreads();
		requestQueue.closeQueue();
	}

	/**
	 * Stops the worker threads.
	 */
	private void stopThreads()
	{
		for(int i=0;i<intMaxThreads;i++)
		{
			threadPool[i].stopThread();
		}
	}

	/**
	*It forces the Thread Pool to destroy all the alive threads the thread pool.
	*It stops the threads with out any clean up and gracefull exits.
	*<b>Since this uses a deprecated method stop which can lead to inconsistent data
	*It should not be called unless it a forced shutdown.</b>
	*/
	public void destroyPool()
	{

		requestQueue.closeQueue();
           /*
		for(int i=0;i<intMaxThreads;i++)
		{
			if (threadPool[i].isAlive())
			{
				threadPool[i].interrupt();
    			        try
				{
				      Thread.currentThread().sleep(1000);
				}
				catch (InterruptedException ieExp)
				{

				}


			}
		}
             */

                //Give an interrupt for all the threads.
                poolGroup.interrupt();


                //wait for a 3 seconds for the other threads to die down

                try
		{

		    Thread.currentThread().sleep(3000);
	        }
		catch (InterruptedException ieExp)
		{
        	}

                //Kill all the remaining active threads.
		for(int i=0;i<intMaxThreads;i++)
		{
			if (threadPool[i].isAlive())
			{
				threadPool[i].stop();
				threadPool[i].kill();

				try
				{
					Thread.currentThread().sleep(1000);
				}
				catch (InterruptedException ieExp)
				{

                }


			}
		}

	}

        /**
	* It returns true if atleast one of the thread in the thread pool is alive.
	* if any thread is alive and still executing it returns false.
	*/
	public boolean checkStatus()
	{
		for(int i=0;i<intMaxThreads;i++)
		{
			if (threadPool[i].getProcessFlag())
			{
				return true;
			}

		}
		return false;
	}

	/**
	* It returns the number of active threads in the Thread Pool.
	* @retruns int No of active threads in the ThreadPool
	*
	*/
	public int getActiveCount()
	{
		return poolGroup.activeCount();
	}

	/**
	* It returns the number of active threads in the thread group.
	* @return Vector  This is the request queue.
	*/
	public Vector getRequestQueue()
	{
		return requestQueue.getRequestQueue();
	}



        /**
         * This method is used to increase the request count.
        */
        public static void addRequestCount()
        {

            intRequestCount++;
        }

        /**
        * This method is used to decrease the request count.
        */
        public static void subRequestCount()
        {
            intRequestCount--;
        }

        /**
        * This method is used to decrease the request count.
        */
        public static int getRequestCount()
        {
            return intRequestCount;
        }


}
