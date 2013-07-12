import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.*;

public class TestServer
{
  private Socket socket;
  private InputStreamReader stream;
  private BufferedReader reader;
  private PrintWriter writer;
  private String str;
  private String msg;
  
  public TestServer()
  {
    try
    {
      socket = new Socket("127.0.0.1", 3550);
      stream = new InputStreamReader(socket.getInputStream());
      reader = new BufferedReader(stream);
      writer = new PrintWriter(socket.getOutputStream(),true);
      System.out.println("Conexion establecida con el servidor");
    }
    catch(IOException e)
    {
      System.out.println("ERROR: The connection can not be established it");
      e.printStackTrace();
    }
  }
  
  private void communicateWithTheServer()
  {
    try
    {
      msg = "Hola mundo desde el cliente!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11";
      writer.println("34567ZXCVB|30/6/2013\0");
      System.out.println("Ya escribiste en el server");
      str = reader.readLine();
      System.out.println("Recibiste del servidor: "+str);
      writer.close();
      reader.close();
      socket.close();
    }
    catch(IOException e)
    {
      System.out.println("ERROR: The connection can not be established it");
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
  
    TestServer test = new TestServer();
    test.communicateWithTheServer();
  }
}