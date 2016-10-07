package edu.gvsu.cis.campbjos.ftp;


import java.io.IOException;

public interface ProtocolInterpreter {
    
    String list() throws IOException;
    void retrieve(final String filename) throws IOException;
    void store(final String filename) throws IOException;
    void quit() throws IOException;
    
}