package edu.gvsu.cis.campbjos.central;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {

    public CentralServer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.printf("New server started from %s:%d\n", InetAddress.getLocalHost().getHostAddress(), server.getLocalPort());
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket client = server.accept();
            CentralInterpreter handler = new CentralInterpreter(client);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            throw new RuntimeException("Syntax: java CentralServer <port>");
        }
        try {
            new CentralServer(Integer.parseInt(args[0]));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
