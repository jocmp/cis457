package edu.gvsu.cis.campbjos.central;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {

    public CentralServer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket client = server.accept();
            DataInputStream in = new DataInputStream(client.getInputStream());
            String name = in.readUTF();
            System.out.println("New client " + name + " from " + client.getInetAddress());
            CentralInterpreter handler = new CentralInterpreter(client);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Syntax: java CentralServer <port>");
        }
        new CentralServer(Integer.parseInt(args[0]));
    }
}
