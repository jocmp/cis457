package edu.gvsu.cis.campbjos.ftp.client;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.Constants.DATA_TRANSFER_PORT;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;
import edu.gvsu.cis.campbjos.ftp.ControlWriter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private String serverIpAddress;
    private Socket socket;

    public ClientProtocolInterpreter() {
        socket = null;
        serverIpAddress = null;
    }

    public boolean connect(final String ipAddress, final String serverPort) {
        final int port = Integer.valueOf(serverPort);
        boolean isConnected = false;
        try {
            serverIpAddress = ipAddress;
            socket = new Socket(ipAddress, port);
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    private void sendToControlWriter(final String command) {
        try {
            ControlWriter.write(socket.getOutputStream(), command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {
        sendToControlWriter(LIST);
    }

    @Override
    public void retrieve(String filename) {
        sendToControlWriter(RETR);
    }

    @Override
    public void store(String filename) {
        sendToControlWriter(STOR);
    }

    @Override
    public void quit() {
        sendToControlWriter(QUIT);
        serverIpAddress = null;
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }

    private void startListeningForCharacterStream() {
        Socket dataSocket = null;
        try {
            dataSocket = new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        } catch (IOException e) {
            return;
        }
        ClientDtp dtpRequest = new ClientDtp(dataSocket);
        dtpRequest.listenForCharacterStream();
    }

    private void startSendingByteStream(final String filename) {
        Socket dataSocket = null;
        try {
            dataSocket = new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        } catch (IOException e) {
            return;
        }

        ClientDtp dtpRequest = new ClientDtp(dataSocket);
        dtpRequest.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) {
        Socket dataSocket = null;
        try {
            dataSocket = new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        } catch (IOException e) {
            return;
        }

        ClientDtp dtpRequest = new ClientDtp(dataSocket);
        dtpRequest.sendCharacterStream(message);
    }
}