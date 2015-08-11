package com.transinfo.tplus.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class MonitorServer extends Thread
{
  protected int MONITOR_PORT = 8005; //default
  protected int TICMS_PORT = 5555; //default
  volatile boolean stop = false;
  protected ServerSocket TICMSSocket;
  protected ServerSocket ClientSocket;
  protected List clientConnections = new Vector();
  protected InputStream in;
  protected OutputStream out;

  class AcceptConnectionThread extends Thread{
    public void run(){
      ServerSocket server;
      try{
        server = new ServerSocket(MONITOR_PORT);
        System.out.println("MONITOR_PORT Started..."+MONITOR_PORT);
      }catch(IOException e){
		  System.out.println("AcceptConnectionThread run "+MONITOR_PORT+"  "+e);
        return;
      }
      while (true){
        try{

          Socket client_socket = server.accept();
          System.out.println("MONITOR_PORT Connection accepted at MonitorServer=");
          if (client_socket != null)
          {
            MonitorClient client = new MonitorClient(client_socket);
            client.start();
            clientConnections.add(client);
          }
          }catch(IOException e){}
        }
    }
  }

  public MonitorServer() throws IOException
  {
     initMS();
  }

  private void initMS() throws IOException
  {
    TICMSSocket = new ServerSocket(TICMS_PORT);
    System.out.println("TIMCS_PORT Started..."+TICMS_PORT);


    new MonitorServer.AcceptConnectionThread().start();
  }

  public MonitorServer( int monitor_port, int ticms_port) throws IOException
  {
    this.MONITOR_PORT = monitor_port;
    this.TICMS_PORT = ticms_port;

    initMS();
  }

  public void run()
  {
    Socket ticms_socket = null;
    Socket client_socket = null;
    byte[] ticms_data;
    try
    {
      System.out.println("Waiting for the Monitor Connection");
      if (ticms_socket == null)
        ticms_socket = TICMSSocket.accept();
        System.out.println("checking");
        System.out.println("TICMS PORT Connection accepted at MonitorServer");
      if (ticms_socket != null)
        in = ticms_socket.getInputStream();
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    while (!stop)
    {
      try
      {
        //client_socket = ClientSocket.accept();

        synchronized(in)
        {

          if(in.available() > 0)
          {

          ticms_data = new byte[in.available()];
          in.read(ticms_data);
          //send log to client
          System.out.println("clientConnections.size()"+clientConnections.size());
          for (int i = 0; i < clientConnections.size(); i++)
          {
            MonitorClient monitorClient = (MonitorClient)clientConnections.get(i);
            //If client closes the connection then remove the connection from the list
            if (!monitorClient.sendLog(new String(ticms_data)))
            {
              clientConnections.remove(i);
              i--;
            }
          }
          }
        }
        try
        {
          this.sleep(1000);
        }catch (InterruptedException ignore)
        {

        }
      }catch (IOException e)
      {
		  System.out.println("In MonitorServer "+e);
        //do nothing
      }
    }
  }

  public static void main (String args[]) throws IOException
  {
	  System.out.println("************** IN public Main");
    MonitorServer ms = new MonitorServer();
    ms.start();
  }
}