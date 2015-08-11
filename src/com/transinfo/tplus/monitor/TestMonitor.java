package com.transinfo.tplus.monitor;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestMonitor
{
  public TestMonitor()
  {
  }

  public static void main(String args[]) throws IOException
  {
    Socket s = new Socket("localhost", 5555);
    OutputStream out = s.getOutputStream();
    out.write("Test monitor".getBytes());
    out.flush();
  }
}