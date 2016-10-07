package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.Converter.convertToServerPortNumber;
import static java.lang.String.format;

final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private Socket piSocket;

    ClientProtocolInterpreter() {
        piSocket = null;
    }

    void connect(final String ipAddress, final String serverPort) throws IOException {
        final int port = convertToServerPortNumber(serverPort);
        try {
            piSocket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket %s:%s", ipAddress, serverPort));
        }
    }

    private void sendCommandToServerControl(final String command) throws IOException {
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
    public String list() throws IOException, RuntimeException {
        final ServerSocket controlSocket = new ServerSocket(0);
        final String address = piSocket.getInetAddress().toString().replaceAll("/", "");
        port(address, controlSocket.getLocalPort());
        sendCommandToServerControl(LIST);

        final DataTransferProcess clientDtp = new ClientDtp(controlSocket.accept());
        controlSocket.close();

        final String list = clientDtp.listenForCharacterStream();
        clientDtp.closeSocket();

        return list;
    }

    private void port(final String address, final int port) throws RuntimeException, IOException {
        if (!address.isEmpty()) {
            final String command = format("%s %s,%s", PORT, address, port);
            sendCommandToServerControl(command);
        } else {
            throw new RuntimeException(format("Not a valid address %s:%s", address, port));
        }
    }

    @Override
    public void retrieve(final String filename) throws IOException {
        sendCommandToServerControl(format("%s %s", RETR, filename));
//        ClientDtp clientDtp = newClientDtp();
//        clientDtp.listenForByteStream(filename);
//        clientDtp.closeSocket();
    }

    @Override
    public void store(final String filename) throws IOException {
        final ServerSocket controlSocket = new ServerSocket(0);
        final String address = piSocket.getInetAddress().toString().replaceAll("/", "");

        port(address, controlSocket.getLocalPort());

        sendCommandToServerControl(format("%s %s", STOR, filename));

        final DataTransferProcess clientDtp = new ClientDtp(controlSocket.accept());
        controlSocket.close();

        clientDtp.sendByteStream(filename);
        clientDtp.closeSocket();
    }

    @Override
    public void quit() throws IOException {
        sendCommandToServerControl(QUIT);
        try {
            piSocket.close();
        } catch (IOException e) {
            // It's closed
        }
    }
}