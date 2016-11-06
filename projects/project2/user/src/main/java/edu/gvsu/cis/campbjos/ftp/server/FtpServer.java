package edu.gvsu.cis.campbjos.ftp.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.common.Constants.CONTROL_PORT;
import static edu.gvsu.cis.campbjos.ftp.common.Constants.VANITY_HEADER;
import static java.lang.String.format;

public final class FtpServer implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            runFtpServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runFtpServer() throws Exception {
        try {
            serverSocket = new ServerSocket(CONTROL_PORT);
            InetAddress ip = InetAddress.getLocalHost();
            System.out.print(VANITY_HEADER);
            System.out.println(format("Server started at %s on port %s",
                    ip.getHostAddress(), serverSocket.getLocalPort()));

            while (true) {
                Socket connection = serverSocket.accept();

                System.out.println(format("New connection with %s:%s",
                        connection.getInetAddress(), connection.getPort()));

                ServerProtocolInterpreter request = new
                        ServerProtocolInterpreter(connection);

                Thread thread = new Thread(request);

                thread.start();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
