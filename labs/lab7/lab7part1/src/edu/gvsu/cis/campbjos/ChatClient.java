package edu.gvsu.cis.campbjos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    public ChatFrame gui;

    private Socket socket;
    private DataOutputStream out;

    private ChatClient(String name, String server, int port) {

        // GUI Create GUI and handle events:
        // After text input, sendTextToChat() is called,
        // When closing the window, disconnect() is called.

        gui = new ChatFrame("Chat with Sockets");
        gui.input.addKeyListener(new EnterListener(this, gui));
        gui.addWindowListener(new ExitListener(this));
        // create a socket, register and listen t the server
        try {

            socket = new Socket(server, port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(name);
            while (true) {
                gui.output.append("\n" + in.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 3)
            throw new RuntimeException("Syntax: java ChatClient <name> <serverhost> <port>");
        int port = Integer.parseInt(args[2]);
        ChatClient chatClient = new ChatClient(args[0], args[1], port);
    }

    protected void sendTextToChat(final String str) {
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

