package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.Constants.CONTROL_PORT;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;

final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private Socket piSocket;

    ClientProtocolInterpreter() {
        piSocket = null;
    }

    void connect(final String ipAddress, final String serverPort) throws IOException {
        final int port = getServerPortNumber(serverPort);
        try {
            piSocket = new Socket(ipAddress, port);
        } catch (IOException exception) {
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

    private void sendToControlWriter(final String command) throws IOException {
        try {
            ControlWriter.write(piSocket.getOutputStream(), command);
        } catch (IOException e) {
            throw new IOException(format("Error writing \"%s\" to server", command));
        }
    }

    boolean isConnected() {
        return !piSocket.isClosed();
    }

    @Override
    public String list() throws IOException {
        sendToControlWriter(LIST);
        final int dtpPort = piSocket.getPort() + 1;
        final ServerSocket connection = new ServerSocket(dtpPort);
        final DataTransferProcess clientDtp = new ClientDtp(connection.accept());
        final String list = clientDtp.listenForCharacterStream();
        clientDtp.closeSocket();

        return list;
    }

    @Override
    public void retrieve(final String filename) throws IOException {
        sendToControlWriter(format("%s %s", RETR, filename));
        ClientDtp clientDtp = newClientDtp();
        clientDtp.listenForByteStream(filename);
        clientDtp.closeSocket();
    }

    private ClientDtp newClientDtp() throws IOException {
        int dtpPort = CONTROL_PORT + 1;
        System.out.println(format("Waiting on %s", dtpPort));
        ServerSocket connection = new ServerSocket(dtpPort);
        ClientDtp clientDtp = new ClientDtp(connection.accept());
        connection.close();
        return clientDtp;
    }

    @Override
    public void store(final String filename) throws IOException {
        sendToControlWriter(format("%s %s", STOR, filename));
        int dtpPort = piSocket.getPort() + 1;
        ServerSocket connection = new ServerSocket(dtpPort);
        Socket dtpSocket = connection.accept();
        DataTransferProcess clientDtp = new ClientDtp(dtpSocket);
        clientDtp.sendByteStream(filename);
        connection.close();
        clientDtp.closeSocket();
    }

    @Override
    public void quit() throws IOException {
        sendToControlWriter(QUIT);
        try {
            piSocket.close();
        } catch (IOException e) {
            // It's closed
        }
    }
}