package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.common.ControlWriter;
import edu.gvsu.cis.campbjos.ftp.common.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.common.ProtocolInterpreter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.Converter.convertToServerPortNumber;
import static java.lang.String.format;

public final class ClientProtocolInterpreter implements ProtocolInterpreter {

    private Socket piSocket;

    public ClientProtocolInterpreter() {
        piSocket = null;
    }

    public void connect(final String ipAddress, final String serverPort)
            throws IOException {
        final int port = convertToServerPortNumber(serverPort);
        try {
            piSocket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket " +
                    "%s:%s", ipAddress, serverPort));
        }
    }

    private void sendCommandToServerControl(final String command)
            throws IOException {
        try {
            ControlWriter.write(piSocket.getOutputStream(), command);
        } catch (IOException e) {
            throw new IOException(format("Error writing \"%s\" to " +
                    "server", command));
        }
    }

    public boolean isConnected() {
        return !(piSocket == null || piSocket.isClosed());
    }

    @Override
    public String list() throws IOException, RuntimeException {
        final ServerSocket controlSocket = new ServerSocket(0);
        final String address = piSocket.getInetAddress().toString().replaceAll("/", "");
        port(address, controlSocket.getLocalPort());
        sendCommandToServerControl(LIST);

        final DataTransferProcess clientDtp = new ClientDtp
                (controlSocket.accept());
        controlSocket.close();

        final String list = clientDtp.listenForCharacterStream();
        clientDtp.closeSocket();

        return list;
    }

    private void port(final String address, final int port) throws
            RuntimeException, IOException {
        if (!address.isEmpty()) {
            final String command = format("%s %s,%s", PORT, address,
                    port);
            sendCommandToServerControl(command);
        } else {
            throw new RuntimeException(format("Not a valid address " +
                    "%s:%s", address, port));
        }
    }

    @Override
    public void retrieve(final String filename) throws IOException,
            NullPointerException {
        final ServerSocket controlSocket = newServerSocket();
        sendCommandToServerControl(format("%s %s", RETR, filename));

        final DataTransferProcess clientDtp = new ClientDtp
                (controlSocket.accept());
        controlSocket.close();

        clientDtp.listenForByteStream(filename);
        clientDtp.closeSocket();
    }

    @Override
    public void store(final String filename) throws IOException {
        final ServerSocket controlSocket = newServerSocket();
        sendCommandToServerControl(format("%s %s", STOR, filename));
        final DataTransferProcess clientDtp = new ClientDtp
                (controlSocket.accept());
        controlSocket.close();

        clientDtp.sendByteStream(filename);
        clientDtp.closeSocket();
    }

    private ServerSocket newServerSocket() throws IOException {
        final ServerSocket controlSocket = new ServerSocket(0);
        final String address = piSocket.getInetAddress().toString()
                .replaceAll("/", "");
        port(address, controlSocket.getLocalPort());
        return controlSocket;
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