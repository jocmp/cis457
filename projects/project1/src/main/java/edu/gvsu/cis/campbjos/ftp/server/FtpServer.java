package edu.gvsu.cis.campbjos.ftp.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

import static edu.gvsu.cis.campbjos.ftp.Constants.CONTROL_PORT;
import static java.lang.String.format;

final class FtpServer {

    public static void main(String argv[]) throws Exception {
        ServerSocket socket = new ServerSocket(CONTROL_PORT);
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println(" -------------------------------------------");
        System.out.println("| FTP Server v1 - campbjos, wrighjax, jungt |");
        System.out.println(" -------------------------------------------");
        System.out.println(" Server started at " + ip.getHostAddress() 
                            + " on port " + socket.getLocalPort());
        while (true) {
            Socket connection = socket.accept();

            System.out.println(format("New connection with %s:%s", connection.getInetAddress(), connection.getPort()));

            ServerProtocolInterpreter request = new ServerProtocolInterpreter(connection);

            Thread thread = new Thread(request);

            thread.start();
        }
    }
}
