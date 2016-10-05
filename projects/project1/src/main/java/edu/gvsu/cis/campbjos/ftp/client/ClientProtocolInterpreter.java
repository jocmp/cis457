package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

final class ClientProtocolInterpreter implements ProtocolInterpreter {
    
    private Socket socket;
    
    public ClientProtocolInterpreter() {
        socket = null;
    }

    @Override
    public void connect(final String ipAddress, final String serverPort) {
        final int port = Integer.valueOf(serverPort);
        try {
            socket = new Socket(ipAddress, port);
            System.out.println("We got here");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {

    }

    @Override
    public void retrieve(String filename) {

    }

    @Override
    public void store(String filename) {

    }

    @Override
    public void quit() {

    }
}