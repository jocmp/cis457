package edu.gvsu.cis.campbjos.ftp;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;
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
        bufferedReader = null;
        serverIpAddress = null;
    }

    public void connect(final String ipAddress, final String serverPort) {
        final int port = Integer.valueOf(serverPort);
        try {
            serverIpAddress = ipAddress;
            socket = new Socket(ipAddress, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void list() {
        ControlWriter.write(socket.getOutputStream(), LIST);
        
    }

    @Override
    public void retrieve(String filename) {
        ControlWriter.write(socket.getOutputStream(), RETR);
    }

    @Override
    public void store(String filename) {
        ControlWriter.write(socket.getOutputStream(), STOR);
    }

    @Override
    public void quit() {
        ControlWriter.write(socket.getOutputStream(), QUIT);
        serverIpAddress = null;
        socket.close();
    }
    
    private void startListeningForCharacterStream() {
        Socket dataSocket = new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ClientDtp dtpRequest = new ClientDtp(connection);
        dtpRequest.sendByteStream(filename);
    }
    
    private void startSendingByteStream(final String filename) {
        Socket dataSocket = new Socket(serverIpAddress, DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ClientDtp dtpRequest = new ClientDtp(connection);
        dtpRequest.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) {
        Socket dataSocket = new Socket(DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ClientDtp dtpRequest = new ClientDtp(connection);
        dtpRequest.sendByteStream(filename);
    }
}