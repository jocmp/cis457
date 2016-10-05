package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

final class ServerProtocolInterpreter implements ProtocolInterpreter, Runnable {
    
    private static final String CRLF = "\r\n";

    private final Socket socket;
    
        // Constructor
    public ServerProtocolInterpreter(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void processRequest() throws Exception {
        // Set up output stream
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters.
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        // Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();  // skip over the method, which should be "GET"
        final String fileName = "." + tokens.nextToken();

        // Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        // Debug info for private use
        System.out.println("Incoming!!!");
        System.out.println(requestLine);
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }

        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        if (fileExists) {
            statusLine = "HTTP/1.1 200 OK";
            // contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF;
        } else {
            statusLine = "HTTP/1.1 404 Not Found";
            contentTypeLine = "Content-Type: text/html" + CRLF;
        }
        // Send the status line.
        os.writeBytes(statusLine);

        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);

        // Send the entity body.
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        }

        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
    }

    private static void sendBytes(FileInputStream fis,
                                  OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    @Override
    public void connect(String ipAddress, String serverPort) {

    }

    @Override
    public void list() {

    }

    @Override
    public void retrieve(String filename) {

    }

    @Override
    public void store(String filename) {

    }

    @Override
    public void quit() {

    }
}
