package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.IOException;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.Constants.DATA_TRANSFER_PORT;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;

final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private String serverIpAddress;
    private Socket piSocket;
    private DataTransferProcess clientDtp;

    ClientProtocolInterpreter() {
        piSocket = null;
        serverIpAddress = null;
    }

    void connect(final String ipAddress, final String serverPort) throws IOException {
        final int port = getServerPortNumber(serverPort);
        try {
            serverIpAddress = ipAddress;
            piSocket = new Socket(ipAddress, port);
            int dtpPort = port + 1;
            clientDtp = new ClientDtp(new Socket(ipAddress, dtpPort));
        } catch (IOException e) {
            throw new IOException(format("Error opening socket %s:%s", ipAddress, serverPort));
        }
    }

    private int getServerPortNumber(final String serverPortText) {
        try {
            return valueOf(serverPortText);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException(format("Invalid port=%s", serverPortText));
        }
    }

    private void sendToControlWriter(final String command) {
        try {
            ControlWriter.write(piSocket.getOutputStream(), command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() throws IOException {
        sendToControlWriter(LIST);
        startListeningForCharacterStream();
    }

    @Override
    public void retrieve(final String filename) throws IOException {
        sendToControlWriter(format("%s %s", RETR, filename));
        startListeningForByteStream(filename);
    }

    @Override
    public void store(final String filename) throws IOException {
        sendToControlWriter(format("%s %s", STOR, filename));
        startSendingByteStream(filename);
    }

    @Override
    public void quit() {
        sendToControlWriter(QUIT);
        serverIpAddress = null;
        try {
            piSocket.close();
            clientDtp.closeSocket();
        } catch (IOException e) {
            // It's closed
        }
    }

    private void startListeningForCharacterStream() throws IOException {
        clientDtp.listenForCharacterStream();
    }

    private void startListeningForByteStream(final String filename) throws IOException {
        Socket connection = getDataSocket();
        ClientDtp dtpRequest = new ClientDtp(connection);
        dtpRequest.listenForByteStream(filename);
    }

    private void startSendingByteStream(final String filename) throws IOException {
        Socket dataSocket = getDataSocket();
        ClientDtp dtpRequest = new ClientDtp(dataSocket);
        dtpRequest.sendByteStream(filename);
    }

    private Socket getDataSocket() throws IOException {
        try {
            return new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        } catch (IOException e) {
            throw new IOException(format("Error opening socket: %s", e.getMessage()));
        }
    }
}