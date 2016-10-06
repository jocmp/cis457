package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.Constants.*;

final class ServerProtocolInterpreter implements ProtocolInterpreter, Runnable {
    
    private final Socket socket;
    private final BufferedReader bufferedReader;
    
    public ServerProtocolInterpreter(final Socket socket) throws Exception {
        this.socket = socket;
        bufferedReader 
            = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        boolean isServerRunning = true;
        try {
            while(isServerRunning) {
                String requestLine = bufferedReader.readLine();
                //TODO add request processing for commands
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void list() {
        //compile the files in the same dir
        String filesList = "";
        File serverDir = new File(".");
        File[] files = serverDir.listFiles();
        for(File file : files){
            filesList += file.getName() + "\n";
        }
        startSendingCharacterStream(filesList);
    }

    @Override
    public void retrieve(String filename) {
        startSendingByteStream(filename);
    }

    @Override
    public void store(String filename) {
        startListening(filename);
    }
    
    @Override
    public void quit() {
        try {
            socket.close();
        } catch (IOException e) {
            // It's closed
        }
    }


    private Socket getSocket() {
        Socket connection = null;
        try {
            ServerSocket dataSocket = new ServerSocket(DATA_TRANSFER_PORT);
            connection = dataSocket.accept();
        } catch (IOException e) {

        }
        return connection;
    }

    private void startListening(final String filename) {
        Socket connection = getSocket();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.listenForByteStream(filename);
    }
    
    private void startSendingByteStream(final String filename) {
        Socket connection = getSocket();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) {
        Socket connection = getSocket();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.sendCharacterStream(message);
    }
}
