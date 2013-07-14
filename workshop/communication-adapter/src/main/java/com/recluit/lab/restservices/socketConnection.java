package com.recluit.lab.restservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.*;

public class socketConnection
{
  private Socket socket;
  private InputStreamReader stream;
  private BufferedReader reader;
  private PrintWriter writer;
  private String str;
  private String msg;
  
  public socketConnection(String message)
  {
    try
    {
      socket = new Socket("127.0.0.1", 3550);
      stream = new InputStreamReader(socket.getInputStream());
      reader = new BufferedReader(stream);
      writer = new PrintWriter(socket.getOutputStream(),true);
      msg = message;
    }
    catch(IOException e)
    {
      System.out.println("ERROR: The connection can not be established it");
      e.printStackTrace();
    }
  }
  
  public String communicateWithTheServer()
  {
    try
    {
      System.out.print("Sending -"+msg+"-");
      
	  System.out.println("Conexion establecida con el servidor");
      writer.println(msg);
      System.out.println("Message sent to the Unix Server");
      String response = null;
      while((str = reader.readLine()) != null)
      {
    	  response += "{";
    	  response += str + "}";
    	  System.out.println("Actual response: " + response);
      }
      writer.close();
      reader.close();
      socket.close();
      return str;
    }
    catch(IOException e)
    {
      System.out.println("ERROR: The connection can not be established it");
      e.printStackTrace();
    }
    return "Error: The connection can not be established";
  }
  
}