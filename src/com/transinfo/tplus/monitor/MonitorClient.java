package com.transinfo.tplus.monitor;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MonitorClient extends Thread
{
    Socket socket;
    InputStream in;
    PrintStream out;

  public MonitorClient()
  {
  }

  public MonitorClient(Socket socket){
      this.socket = socket;
      try{
        out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        in = socket.getInputStream();
      }catch(Exception e){}
    }

    public void run(){
      try{
		  System.out.println("reading data in MonitorClient");
        while (in.read() > 0){
        }
      }catch(Exception e){
      }finally{
        try{
          socket.close();
        }catch(Exception ignore){System.out.println(ignore);}
      }
    }

    public boolean sendLog (String msg)
    {
		System.out.println("Client msg="+msg);
      if ((out == null) || (socket == null) || (socket.isClosed()) || (socket.isOutputShutdown()))
        return false;
      out.println(msg);
      out.flush();
      System.out.println("client data flushed");
      return true;
    }
}