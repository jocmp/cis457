package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.net.ServerSocket;
import java.net.Socket;

final class FtpServer {

    public static void main(String argv[]) throws Exception {
        ServerSocket socket = new ServerSocket(CONTROL_PORT);
        
        while (true) {
            Socket connection = socket.accept();
            
            ServerProtocolInterpreter request = new ServerProtocolInterpreter(connection);
            
            Thread thread = new Thread(request);
            
            thread.start();
        }
    }
}
