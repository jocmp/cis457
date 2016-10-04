//Time Server.java
import java.net.*;
import java.io.*;
import java.util.*;

public class TimeServer2 {

public static void main(String args[]) throws IOException {
int port = 1234;

ServerSocket server = new ServerSocket (port);
while(true) {

	System.out.println("Waiting for client....");
	Socket client = server.accept();
	System.out.println("Client from" + client.getInetAddress() + "connected");

		DataOutputStream out = new DataOutputStream(new 					                      BufferedOutputStream(client.getOutputStream()));

		Date date = new Date();
		out.writeUTF(date.toString());
		out.flush();


}}}
