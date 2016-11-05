package edu.gvsu.cis.campbjos;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public ChatServer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket client = server.accept();
            DataInputStream in = new DataInputStream(client.getInputStream());
            String name = in.readUTF();
            System.out.println("New client " + name + " from " + client.getInetAddress());
            ChatHandler c = new ChatHandler(name, client);
            c.start();
        }
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 1)
            throw new RuntimeException("Syntax: java ChatServer <port>");
        new ChatServer(Integer.parseInt(args[0]));
    }
}
