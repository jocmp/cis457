package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.util.Scanner;

//User interface
public final class FtpClient {

    public static void main(String[] args) {
        final ProtocolInterpreter protocolInterpreter = new ClientProtocolInterpreter();
        final Scanner keyboard = new Scanner(System.in);
        String currentInput = "";
        boolean connected = false;
        while(keyboard.hasNextLine()) {

            String[] tokens = currentInput.split(" ");
            final String command = tokens[0].toUpperCase();
            if(command.equals("CONNECT")){
                String server = tokens[1];
                String port = tokens[2];
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