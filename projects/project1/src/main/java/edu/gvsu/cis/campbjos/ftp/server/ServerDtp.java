package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ControlByteReader;
import edu.gvsu.cis.campbjos.ftp.ControlByteWriter;
import edu.gvsu.cis.campbjos.ftp.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;

import java.io.IOException;
import java.io.File;
import java.net.Socket;

final class ServerDtp implements DataTransferProcess {

    private final Socket socket;

    public ServerDtp(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void listenForByteStream(final String filename) {
        // todo throw exception if socket is closed
        try {
            ControlByteReader.readByteStream(socket.getInputStream(), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    @Override
    public void sendByteStream(final String filename) {
        try {
            ControlByteWriter.sendFile(socket.getOutputStream(), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    @Override
    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }

    @Override
    public void sendCharacterStream(final String message) {
        try {
            ControlWriter.write(socket.getOutputStream(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    @Override
    public String listenForCharacterStream() {
        // Unused in server DTP
        return null;
    }
}