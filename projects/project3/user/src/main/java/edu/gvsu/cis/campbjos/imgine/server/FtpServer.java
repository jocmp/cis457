package edu.gvsu.cis.campbjos.imgine.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static edu.gvsu.cis.campbjos.imgine.common.Constants.CONTROL_PORT;
import static edu.gvsu.cis.campbjos.imgine.common.Constants.VANITY_HEADER;
import static java.lang.String.format;

public final class FtpServer implements Runnable {

    private static int ftpServerPort;
    private ServerSocket serverSocket;

    public static int getFtpServerPort() {
        return ftpServerPort;
    }

    @Override
    public void run() {
        try {
            runFtpServer(CONTROL_PORT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runFtpServer(int startingPort) throws Exception {
        try {
            serverSocket = new ServerSocket(startingPort);
            ftpServerPort = startingPort;
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
        } catch (SocketException exception) {
            runFtpServer(startingPort + 1);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
