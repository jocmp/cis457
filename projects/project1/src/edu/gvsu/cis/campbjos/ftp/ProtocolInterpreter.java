package edu.gvsu.cis.campbjos.ftp;


public interface ProtocolInterpreter {
    
    public void connect(final String ipAddress, final String serverPort);
    public void list();
    public void retrieve(final String filename);
    public void store(final String filename);
    public void quit();
    
}