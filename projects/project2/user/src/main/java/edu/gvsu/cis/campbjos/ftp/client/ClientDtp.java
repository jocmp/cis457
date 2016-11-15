package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.common.ControlByteReader;
import edu.gvsu.cis.campbjos.ftp.common.ControlByteWriter;
import edu.gvsu.cis.campbjos.ftp.common.DataTransferProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

final class ClientDtp implements DataTransferProcess {

    private final Socket socket;

    ClientDtp(InetAddress ipAddress, int port) throws IOException {
        final String address = ipAddress.toString().replaceAll("/", "");
        this.socket = new Socket(address, port);
    }

    @Override
    public void sendByteStream(final String filename) {
        try {
            ControlByteWriter.sendFile(socket.getOutputStream(),
                    filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCharacterStream(String message) {
        // Unused in client DTP
    }

    @Override
    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }

    @Override
    public void listenForByteStream(final String filename) throws
            NullPointerException {
        try {
            ControlByteReader.readByteStream(socket.getInputStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                throw new NullPointerException(e.getMessage());
            }
        }
    }

    @Override
    public String listenForCharacterStream() {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader
                    (socket.getInputStream()));
        } catch (IOException exception) {
            return "";
        }
        String messageFromServer = "";
        boolean isReceivingStream = true;

        while (isReceivingStream) {
            try {
                final String requestLine = bufferedReader.readLine();
                isReceivingStream = !requestLine.isEmpty();
                if (isReceivingStream) {
                    messageFromServer += requestLine + '\n';
                }
            } catch (IOException e) {
                break;
            }
        }
        return messageFromServer;
    }
}