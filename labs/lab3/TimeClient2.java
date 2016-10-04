// TimeClient2.java
import java.net.*;
import java.io.*;

public class TimeClient2 {

public static void main(String args[]) throws IOException {

	Socket server= new Socket ("127.0.0.1", 1234);
	System.out.println("connected to" + server.getInetAddress());

	DataInputStream in = new DataInputStream(new 				BufferedInputStream(server.getInputStream()));
	System.out.println("Server said" + in.readUTF());

}}