package edu.gvsu.cis.campbjos.central;

import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Result;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import static java.lang.String.format;

public class CentralInterpreter extends Thread {

    private static final Vector<Host> HOSTS;
    private static final Vector<Result> RESULTS;

    private Socket socket;
    private Host host;

    static {
        HOSTS = new Vector<>();
        RESULTS = new Vector<>();
    }

    CentralInterpreter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                broadcast(format("%s: %s", name, in.readUTF()));
            }
        } catch (IOException ex) {
            System.out.println("-- Connection to user lost.");
        } finally {

            broadcast(format("%s has left", name));
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("-- Socket to user already closed ?");
            }
        }
    }


    public void register(Host host) {
        HOSTS.add(host);
    }

}
