package edu.gvsu.cis.campbjos.ftp;

public interface DataTransferProcess {
    
    public void listenForByteStream(final String filename);
    public void sendByteStream(final String filename);
    
}