package edu.gvsu.cis.campbjos.ftp;

import java.net.ServerSocket;
import java.net.Socket;

public final class FtpServer {

    private static final int CONTROL_PORT = 8063;
    private static final int DATA_TRANSFER_PORT = 8064;

    public static void main(String argv[]) throws Exception {
        // Get the port number from the command line.
        ServerSocket socket = new ServerSocket(CONTROL_PORT);
        
        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.
            Socket connection = socket.accept();
            
            // Construct an object to process the HTTP request message.
            FtpClient request = new FtpClient(connection);
            
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            
            // Start the thread.
            thread.start();
        }
    }
}
