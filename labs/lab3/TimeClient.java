// TimeClient

import java.net.*;
import java.io.*;

public class TimeClient {

    public static void main (String args[]) throws IOException 
	{
	Socket server = new Socket("localhost",1234);
	System.out.println("Connected to " + server.getInetAddress());
	InputStream in = server.getInputStream();
	
	byte b[] = new byte[100];
	int num = in.read(b);
	String date = new String(b);
	System.out.println("Server said: "+date);
    }}
