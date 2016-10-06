package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.Commands;
import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.util.Scanner;

import static edu.gvsu.cis.campbjos.ftp.Commands.CONNECT;

final class FtpClient {

    public static void main(String[] args) {
        final ClientProtocolInterpreter protocolInterpreter = new ClientProtocolInterpreter();
        final Scanner keyboard = new Scanner(System.in);
        boolean isClientRunning = true;

        while (isClientRunning) {
            final String currentInput = keyboard.nextLine();
            System.out.println(currentInput);
            String[] tokens = currentInput.split(" ");
            final String command = tokens[0].toUpperCase();
            boolean isConnected = false;

            if (command.equals(CONNECT)) {
                if (tokens.length < 3) {
                    continue;
                }
                final String server = tokens[1];
                final String port = tokens[2];
                isConnected = protocolInterpreter.connect(server, port);
                if (isConnected) {
                    System.out.println("---Socket Created---");
                } else {
                    System.out.println("---Socket Failed to Create---");
                }

            }

            if (!isConnected) {
                System.out.println("ERROR: Not connected");
                return;
            }

            if (command.equals("LIST") && isConnected) {
                protocolInterpreter.list();

            } else if (command.equals("RETR") && isConnected) {
                String fileName = tokens[1];
                protocolInterpreter.retrieve(fileName);

            } else if (command.equals("STOR") && isConnected) {
                String fileName = tokens[1];
                protocolInterpreter.store(fileName);

            } else if (command.equals("QUIT") && isConnected) {
                keyboard.close();
                protocolInterpreter.quit();

            } else {
                System.out.println("ERROR: Invalid command");
            }
        }
    }

}