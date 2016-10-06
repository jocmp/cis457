package edu.gvsu.cis.campbjos.ftp;


import java.io.IOException;

public interface ProtocolInterpreter {
    
    public void list() throws IOException;
    public void retrieve(final String filename) throws IOException;
    public void store(final String filename) throws IOException;
    public void quit();
    
}