package edu.gvsu.cis.campbjos.ftp.server;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

final class ServerProtocolInterpreter implements ProtocolInterpreter, Runnable {
    
    private final Socket socket;
    private final BufferedReader bufferedReader;
    
    public ServerProtocolInterpreter(final Socket socket) throws Exception {
        this.socket = socket;
        bufferedReader 
            = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serverDtp = new ServerDtp();
    }

    @Override
    public void run() {
        boolean isServerRunning = true;
        try {
            while(isServerRunning) {
                String requestLine = bufferedReader.readLine();
                processRequest(request);
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
        socket.close();
    }
    
    private void startListening(final String filename) {
        ServerSocket dataSocket = new ServerSocket(DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.listenForByteStream(filename);
    }
    
    private void startSendingByteStream(final String filename) {
        ServerSocket dataSocket = new ServerSocket(DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.sendByteStream(filename);
    }

    private void startSendingCharacterStream(final String message) {
        ServerSocket dataSocket = new ServerSocket(DATA_TRANSFER_PORT);
        
        Socket connection = dataSocket.accept();
        ServerDtp dtpRequest = new ServerDtp(connection);
        dtpRequest.sendByteStream(filename);
    }
}
