package edu.gvsu.cis.campbjos.ftp.server;

final class ServerDtp implements DataTransferProcess {
    
    private BufferedReader bufferedReader;
    private final Socket socket;
    
    public ServerDtp(final Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void listenForByteStream(final String filename) {
        ControlByteReader.readByteStream(socket.getInputStream(), filename);
        socket.close();
    }
    
    @Override
    public void sendByteStream(final String filename) {
        ControlByteWriter.write(socket.getOutputStream(), filename);
        socket.close();
    }
    
    public void sendCharacterStream(final String message) {
        ControlWriter.write(socket.getOutputStream(), message);
        socket.close();
    }
    
}