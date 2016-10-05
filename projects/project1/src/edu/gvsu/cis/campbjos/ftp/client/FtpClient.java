package edu.gvsu.cis.campbjos.ftp.client;

//User interface
public final class FtpClient {
    
    private final ProtocolInterpreter protocolInterpreter;
    
    public FtpClient() {
        protocolInterpreter = new ClientProtocolInterpreter();
    }
    
    public static void main(String[] args) {
        
        final Scanner keyboard = new Scanner(System.in);
        String currentInput = "";
        boolean connected = false;
        while(keyboard.hasNextLine()) {
                
            String[] split = currentInput.split(" ");
            final String command = input[0].toUpperCase();
            if(command.equals("CONNECT")){
                String server = input[1];
                String port = input[2];
                protocolInterpreter.connect(server, port);
            } else if(command.equals("LIST")){

            } else if(command.equals("RETR")){
                
            } else if(command.equals("STOR")){
                
            } else if(command.equals("QUIT")){
                keyboard.close();
            } else {
              //invalid command
              System.out.println("Invalid command");
            }
        }
    }
    
}