package edu.gvsu.cis.campbjos.ftp.client;

final class ClientDtp implements DataTransferProcess {
    
    private final BufferedReader bufferedReader;
    private final Socket socket;
    
    public ClientDtp(final Socket socket) {
        this.socket = socket;
        bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }
    
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
    
    public String listenForCharacterStream() {
        BufferedReader bufferedReader 
            = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        boolean isRecievingStream = true;
        String messageFromServer = null;
        
        while (isRecievingStream) {
            String requestLine = bufferedReader.readLine();
            messageFromServer += requestLine;
            isRecievingStream = requestLine != Constants.CRLF;
        }
        socket.close();
        return messageFromServer;
    }
    
}