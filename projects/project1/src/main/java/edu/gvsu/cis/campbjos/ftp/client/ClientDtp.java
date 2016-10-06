package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Constants.CRLF;

final class ClientDtp implements DataTransferProcess {

    private final Socket socket;

    public ClientDtp(final Socket socket) {
        this.socket = socket;
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
        } catch (IOException e) {
        }
    }

    @Override
    public void listenForByteStream(final String filename) {
        try {
            ControlByteReader.readByteStream(socket.getInputStream(), filename);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String listenForCharacterStream() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException exception) {
            return "";
        }
        boolean isRecievingStream = true;
        String messageFromServer = null;
        
        while (isRecievingStream) {
            String requestLine = null;
            try {
                requestLine = bufferedReader.readLine();
                messageFromServer += requestLine;
                isRecievingStream = !requestLine.equals(CRLF);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
        return messageFromServer;
    }
}