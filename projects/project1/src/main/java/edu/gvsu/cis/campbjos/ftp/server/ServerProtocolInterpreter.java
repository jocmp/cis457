package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Constants.CRLF;
import static edu.gvsu.cis.campbjos.ftp.Constants.DATA_TRANSFER_PORT;
import static java.lang.String.format;

final class ServerProtocolInterpreter implements ProtocolInterpreter, Runnable {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private ServerDtp serverDtp;

    ServerProtocolInterpreter(final Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        boolean isServerRunning = true;
        try {
            serverDtp = new ServerDtp(getAcceptedDataSocket());
            while (isServerRunning) {
                String requestLine = bufferedReader.readLine();
                //TODO add request processing for commands
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void list() {
        String filesList = "";
        File serverDir = new File(".");
        File[] files = serverDir.listFiles();
        if (files != null) {
            for (File file : files) {
                filesList += file.getName() + CRLF;
            }
        }
        startSendingCharacterStream(filesList);
    }

    @Override
    public void retrieve(String filename) {
        startSendingByteStream(filename);
    }

    @Override
    public void store(String filename) {
        startListeningForByteStream(filename);
    }

    @Override
    public void quit() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }


    private Socket getAcceptedDataSocket() throws IOException {
        Socket connection = null;
        try {
            ServerSocket dataSocket = new ServerSocket(DATA_TRANSFER_PORT);
            connection = dataSocket.accept();
        } catch (IOException e) {
            throw new IOException(format("Error creating Server DTP: %s", e.getMessage()));
        }
        return connection;
    }

    private void startListeningForByteStream(final String filename) {
        serverDtp.listenForByteStream(filename);
    }

    private void startSendingByteStream(final String filename) {
        serverDtp.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) {
        serverDtp.sendCharacterStream(message);
    }
}
