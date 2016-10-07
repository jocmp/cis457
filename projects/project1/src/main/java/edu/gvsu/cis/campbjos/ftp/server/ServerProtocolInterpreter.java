package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Commands.LIST;
import static edu.gvsu.cis.campbjos.ftp.Constants.CONTROL_PORT;
import static edu.gvsu.cis.campbjos.ftp.Constants.CRLF;
import static java.lang.String.format;
import static java.lang.System.out;

final class ServerProtocolInterpreter implements ProtocolInterpreter, Runnable {

    private final Socket socket;
    private final BufferedReader bufferedReader;

    ServerProtocolInterpreter(final Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        final boolean isServerRunning = true;
        try {
            while (isServerRunning) {
                String requestLine = bufferedReader.readLine();
                System.out.println(requestLine);
                processInput(requestLine);
                //TODO add request processing for commands
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void processInput(final String input) throws IOException {
        final String[] tokens = input.split(" ");
        if (tokens.length < 1) {
            return;
        }
        final String command = tokens[0];
        if (command.equals(LIST)) {
            list();
        }
    }

    @Override
    public String list() throws IOException {
        String filesList = "";
        File serverDir = new File(".");
        File[] files = serverDir.listFiles();
        if (files != null) {
            for (File file : files) {
                filesList += file.getName() + "\n";
            }
        }
        startSendingCharacterStream(filesList);
        return "";
    }

    @Override
    public void retrieve(String filename) throws IOException {
        startSendingByteStream(filename);
    }

    @Override
    public void store(String filename) throws IOException {
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


    private Socket getDataSocket() throws IOException {
        Socket connection = null;
        try {
            final int dtpPort = CONTROL_PORT + 1;
            System.out.println(format("Sending to %s:%s", socket.getInetAddress(), dtpPort));
            connection = new Socket(socket.getInetAddress(), dtpPort);
        } catch (IOException e) {
            throw new IOException(format("Error creating Server DTP: %s", e.getMessage()));
        }
        return connection;
    }

    private void startListeningForByteStream(final String filename) throws IOException {
        Socket dtpSocket = getDataSocket();
        DataTransferProcess serverDtp = new ServerDtp(dtpSocket);
        serverDtp.listenForByteStream(filename);
    }

    private void startSendingByteStream(final String filename) throws IOException {
        Socket dtpSocket = getDataSocket();
        DataTransferProcess serverDtp = new ServerDtp(dtpSocket);
        serverDtp.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) throws IOException {
        Socket dtpSocket = getDataSocket();
        DataTransferProcess serverDtp = new ServerDtp(dtpSocket);
        serverDtp.sendCharacterStream(message);
    }
}
