package edu.gvsu.cis.campbjos.imgine.server;

import edu.gvsu.cis.campbjos.imgine.common.ControlByteReader;
import edu.gvsu.cis.campbjos.imgine.common.ControlByteWriter;
import edu.gvsu.cis.campbjos.imgine.common.ControlWriter;
import edu.gvsu.cis.campbjos.imgine.common.DataTransferProcess;

import java.io.IOException;
import java.net.Socket;

final class ServerDtp implements DataTransferProcess {

    private final Socket socket;

    ServerDtp(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void listenForByteStream(final String filename) {
        try {
            ControlByteReader.readByteStream(socket.getInputStream(),
                    filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    @Override
    public void sendByteStream(final String filename) {
        try {
            ControlByteWriter.sendFile(socket.getOutputStream(),
                    filename);
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
    }
}