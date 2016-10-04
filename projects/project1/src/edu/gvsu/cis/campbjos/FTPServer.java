import java.io.* ;
import java.net.* ;
import java.util.* ;

/**
 * PART B
 */
public final class FTPServer {
    
    public static void main(String argv[]) throws Exception {
        // Get the port number from the command line.
        int port = Integer.valueOf(argv[0]);
        // Establish the listen socket.
        ServerSocket socket = new ServerSocket(port);
        
        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.
            Socket connection = socket.accept();
            
            // Construct an object to process the HTTP request message.
            HttpRequest request = new HttpRequest(connection);
            
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            
            // Start the thread.
            thread.start();
        }
    }
}
