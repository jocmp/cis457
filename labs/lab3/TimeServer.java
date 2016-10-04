// Time Server

import java.net.*;
import java.io.*;
import java.util.*;

public class TimeServer {
 public static void main (String args[]) throws IOException {

	int port = 1234;
	ServerSocket server = new ServerSocket(port);

		while (true) {
			System.out.println("Waiting for client...");    
			Socket client = server.accept();
            System.out.println("Client from " + client.getInetAddress() + " connected.");
			OutputStream out = client.getOutputStream();
			Date date = new Date();	    
			byte b[] = date.toString().getBytes();   
			out.write(b);
			}
    }
}
