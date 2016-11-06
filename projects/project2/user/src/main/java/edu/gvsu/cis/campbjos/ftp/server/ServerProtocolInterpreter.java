package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.common.DataTransferProcess;
import edu.gvsu.cis.campbjos.ftp.common.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.Converter.convertToServerPortNumber;
import static java.lang.String.format;
import static java.lang.System.out;

final class ServerProtocolInterpreter implements ProtocolInterpreter,
        Runnable {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private String currentListeningAddress;
    private int currentListeningPort;

    ServerProtocolInterpreter(final Socket socket) throws IOException {
        this.socket = socket;
        currentListeningAddress = "";
        currentListeningPort = -1;
        bufferedReader = new BufferedReader(new InputStreamReader
                (this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String requestLine = bufferedReader.readLine();
                System.out.println(requestLine);
                processInput(requestLine);
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void processInput(final String input) throws IOException {
        final List<String> tokens = Arrays.asList(input.split(" ", 2));
        String file = "";
        if (tokens.size() > 1) {
            file = tokens.get(1);
        } else if (tokens.isEmpty()) {
            return;
        }
        final String command = tokens.get(0);
        if (command.equals(LIST)) {
            list();
        } else if (command.equals(PORT)) {
            port(tokens);
        } else if (command.equals(RETR)) {
            retrieve(file);
        } else if (command.equals(STOR)) {
            store(file);
        } else if (command.equals(QUIT)) {
            quit();
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
        return filesList;
    }

    private void port(final List<String> tokens) {
        List<String> clientValues = new ArrayList<>();
        if (tokens.size() > 1) {
            clientValues = Arrays.asList(tokens.get(1).split(","));
        }
        if (clientValues.size() == 2) {
            currentListeningAddress = clientValues.get(0);
            currentListeningPort = convertToServerPortNumber
                    (clientValues.get(1));
        }
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

    private Socket newDataSocket() throws IOException {
        try {
            System.out.println(format("Sending to %s:%s",
                    currentListeningAddress, currentListeningPort));
            return new Socket(currentListeningAddress,
                    currentListeningPort);
        } catch (IOException e) {
            throw new IOException(format("Error creating Server DTP: " +
                    "%s", e.getMessage()));
        }
    }

    private void startListeningForByteStream(final String filename)
            throws IOException {
        DataTransferProcess serverDtp = new ServerDtp(newDataSocket());
        serverDtp.listenForByteStream(filename);
        serverDtp.closeSocket();
    }

    private void startSendingByteStream(final String filename) throws
            IOException {
        DataTransferProcess serverDtp = new ServerDtp(newDataSocket());
        serverDtp.sendByteStream(filename);
        serverDtp.closeSocket();
    }

    private void startSendingCharacterStream(final String message)
            throws IOException {
        DataTransferProcess serverDtp = new ServerDtp(newDataSocket());
        serverDtp.sendCharacterStream(message);
        serverDtp.closeSocket();
    }
}
