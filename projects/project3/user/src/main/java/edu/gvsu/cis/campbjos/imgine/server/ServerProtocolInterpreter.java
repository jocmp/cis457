package edu.gvsu.cis.campbjos.imgine.server;

import edu.gvsu.cis.campbjos.imgine.common.ControlWriter;
import edu.gvsu.cis.campbjos.imgine.common.DataTransferProcess;
import edu.gvsu.cis.campbjos.imgine.common.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static edu.gvsu.cis.campbjos.imgine.common.Commands.*;
import static java.lang.String.valueOf;
import static java.lang.System.out;

final class ServerProtocolInterpreter implements ProtocolInterpreter,
        Runnable {

    private final Socket socket;
    private final BufferedReader bufferedReader;

    ServerProtocolInterpreter(final Socket socket) throws IOException {
        this.socket = socket;
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
        if (command.equals(RETR)) {
            retrieve(file);
        } else if (command.equals(QUIT)) {
            quit();
        }
    }

    @Override
    public void retrieve(String filename) throws IOException {
        startSendingByteStream(filename);
    }

    @Override
    public void quit() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }

    private ServerSocket newServerSocket() throws IOException {
        final ServerSocket controlSocket = new ServerSocket(0);
        ControlWriter.write(socket.getOutputStream(), valueOf(controlSocket.getLocalPort()));
        return controlSocket;
    }

    private void startListeningForByteStream(final String filename)
            throws IOException {
        DataTransferProcess serverDtp = new ServerDtp(newServerSocket().accept());
        serverDtp.listenForByteStream(filename);
        serverDtp.closeSocket();
    }

    private void startSendingByteStream(final String filename) throws
            IOException {
        DataTransferProcess serverDtp = new ServerDtp(newServerSocket().accept());
        serverDtp.sendByteStream(filename);
        serverDtp.closeSocket();
    }

    private void startSendingCharacterStream(final String message)
            throws IOException {
        DataTransferProcess serverDtp = new ServerDtp(newServerSocket().accept());
        serverDtp.sendCharacterStream(message);
        serverDtp.closeSocket();
    }
}
