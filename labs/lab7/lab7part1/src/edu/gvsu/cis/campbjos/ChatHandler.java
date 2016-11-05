package edu.gvsu.cis.campbjos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

import static java.lang.String.format;

class ChatHandler extends Thread {

    private static final Vector<ChatHandler> HANDLERS;

    static {
        HANDLERS = new Vector<>();
    }

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final String name;

    ChatHandler(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private static void broadcast(String message) {
        synchronized (HANDLERS) {
            Enumeration e = HANDLERS.elements();
            while (e.hasMoreElements()) {
                ChatHandler handler = (ChatHandler) e.nextElement();
                try {
                    handler.out.writeUTF(message);
                } catch (IOException ex) {
                    handler.interrupt();
                }
            }
        }
    }

    @Override
    public void run() {

        try {
            broadcast(name + " entered");
            HANDLERS.addElement(this);

            //noinspection InfiniteLoopStatement
            while (true) {
                broadcast(format("%s: %s", name, in.readUTF()));
            }

        } catch (IOException ex) {
            System.out.println("-- Connection to user lost.");
        } finally {
            HANDLERS.removeElement(this);
            broadcast(format("%s has left", name));
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("-- Socket to user already closed ?");
            }
        }
    }
}

