package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ControlByteReader;
import edu.gvsu.cis.campbjos.ftp.ControlByteWriter;
import edu.gvsu.cis.campbjos.ftp.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;

import java.io.IOException;
import java.net.Socket;

final class ServerDtp implements DataTransferProcess {

    private final Socket socket;

    public ServerDtp(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void listenForByteStream(final String filename) {
        try {
            ControlByteReader.readByteStream(socket.getInputStream(), filename);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendByteStream(final String filename) {
        try {
            ControlByteWriter.sendFile(socket.getOutputStream(), filename);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCharacterStream(final String message) {
        try {
            ControlWriter.write(socket.getOutputStream(), message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}