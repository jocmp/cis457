package edu.gvsu.cis.campbjos.imgine.common;

public interface DataTransferProcess {

    void listenForByteStream(final String filename);

    void sendByteStream(final String filename);

    void sendCharacterStream(final String message);

    void closeSocket();

}