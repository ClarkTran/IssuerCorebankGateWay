package com.transinfo.tplus.messaging;

import org.jpos.iso.ISOMUX;
import org.jpos.iso.ISOServer;
import org.jpos.util.LogEvent;

import com.transinfo.tplus.monitor.MonitorLogListener;


/**
 * This class provides a safe halt method to replace the deprecated Thread.stop() method
 */
public class TIVNThread extends Thread
{
  private volatile Thread myThread;
  private Runnable target;
  private String name;
  private volatile boolean isDead = true;

  public TIVNThread (Runnable target,String name)
  {
    init (target, name);
  }

  public TIVNThread (Runnable target)
  {
    init (target, null);
  }

  private void init (Runnable target,String name)
  {
    this.target=target;
    this.name=name;
  }

  public synchronized void start()
  {

    myThread = new Thread(this);
    myThread.start();
  }

  //Use this method to stop the inner thread

  public synchronized void halt()
  {
    try
    {
      isDead = true;
      if (target instanceof ISOServer)
        ((ISOServer)target).shutdown();
      if (target instanceof ISOMUX)
        ((ISOMUX)target).terminate(1000);
      myThread=null;
    }catch (Exception ignore)
    {
      //do nothing
    }
  }

  //Use this method to revive the inner thread
  public synchronized void revive()
  {
    isDead = false;
    target.run();
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    isDead = false;
    LogEvent evt;
    while (myThread == thisThread) {
System.out.println("******** TIVNThread ******* "+myThread);
      try
      {

        if (target instanceof ISOMUX)
        {
          while (!((ISOMUX)target).isConnected())
          {
            MonitorLogListener.getMonitorLogListener(8005).sendSystemLog(name +" connection is down",MonitorLogListener.RED);
            this.sleep(10000);
            try
            {
              ((ISOMUX)target).getISOChannel().connect();
            }catch (Exception e)
            {
				MonitorLogListener.getMonitorLogListener(8005).sendSystemLog(" Could not connect to  Host - " +name ,MonitorLogListener.RED);
            }
          }
          MonitorLogListener.getMonitorLogListener(8005).sendSystemLog(name +" connection is up",MonitorLogListener.BLUE);
          ((ISOMUX)target).getISOChannel().setUsable(true);
        }


       /* if (target instanceof ISOMUX)
        {

          if (!((ISOMUX)target).isConnected())
          {

            //this.sleep(10000);
            try
            {
              ((ISOMUX)target).getISOChannel().connect();
 				System.out.println("*************************** 1 ***************************");
				  MonitorLogListener.getMonitorLogListener(8005).sendSystemLog("Connection with "+name +" Host is up",MonitorLogListener.BLUE);
				  ((ISOMUX)target).getISOChannel().setUsable(true);

            }catch (Exception e)
            {
				MonitorLogListener.getMonitorLogListener(8005).sendSystemLog(" Could not connect to  Host - " +name ,MonitorLogListener.RED);

            }
          }

        }*/

		System.out.println("*************************** 2 ***************************");
        target.run();
        this.sleep(2000);
      } catch (InterruptedException e)
      {
		  System.out.println(e);
      }

    }
		System.out.println("$$$$$$$$$$$$$ TIVNThead Dead $$$$$$$$$$$$$$$$");

  }

  public boolean isDead()
  {
    //return myThread==null||!myThread.isAlive();
    return isDead;
  }

  public void setThreadDead()
  {

	  myThread=null;
  }

  public Object getTarget()
  {
    return target;
  }
}