package edu.gvsu.cis.campbjos.ftp.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Constants.CONTROL_PORT;
import static edu.gvsu.cis.campbjos.ftp.Constants.VANITY_HEADER;
import static java.lang.String.format;

final class FtpServer {

    public static void main(String argv[]) throws Exception {
        ServerSocket socket = new ServerSocket(CONTROL_PORT);
        InetAddress ip = InetAddress.getLocalHost();
        System.out.print(VANITY_HEADER);
        System.out.println(format("Server started at %s on port %s",
                ip.getHostAddress(), socket.getLocalPort()));
        while (true) {
            Socket connection = socket.accept();

            System.out.println(format("New connection with %s:%s",
                    connection.getInetAddress(), connection.getPort()));

            ServerProtocolInterpreter request = new
                    ServerProtocolInterpreter(connection);

            Thread thread = new Thread(request);

            thread.start();
        }
    }
}
