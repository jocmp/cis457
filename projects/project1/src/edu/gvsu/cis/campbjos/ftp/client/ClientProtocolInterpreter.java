package edu.gvsu.cis.campbjos.ftp.client;

import java.net.Socket;

public final class ClientProtocolInterpreter implements ProtocolInterpreter {
    
    private Socket socket;
    
    public ClientProtocolInterpreter() {
        socket = null;
    }
    
    public void connect(final String ipAddress, final String serverPort);
        socket = new Socket(ipAddress, serverPort);
    }
    
    public void list() {
        
    }
    
    public void retrieve(final String filename) {
        
    }
    
    public void store(final String filename) {
        
    }
    
    public void quit() {
        
    }
    
}