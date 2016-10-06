package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.IOException;
import java.util.Scanner;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;

final class FtpClient {

    public static void main(String[] args) {
        final ClientProtocolInterpreter protocolInterpreter = new ClientProtocolInterpreter();
        final Scanner keyboard = new Scanner(System.in);
        boolean isClientRunning = true;
        boolean isConnected = false;

        while (isClientRunning) {
            final String currentInput = keyboard.nextLine();
            final String[] tokens = currentInput.split(" ");
            final String command = tokens[0].toUpperCase();

            if (command.equals(CONNECT)) {
                if (tokens.length < 3) {
                    continue;
                }
                final String server = tokens[1];
                final String port = tokens[2];
                try {
                    protocolInterpreter.connect(server, port);
                    isConnected = true;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            if (!isConnected) {
                System.out.println("ERROR: Not connected");
            } else if (command.equals(LIST)) {
                handleList(protocolInterpreter);
            } else if (command.equals(RETR)) {
                handleRetrieve(tokens, protocolInterpreter);
            } else if (command.equals(STOR)) {
                handleStore(tokens, protocolInterpreter);
            } else if (command.equals(QUIT)) {
                protocolInterpreter.quit();
                isClientRunning = false;
                keyboard.close();
            }
        }
    }

    private static void handleList(final ProtocolInterpreter protocolInterpreter) {
        try {
            protocolInterpreter.list();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleRetrieve(final String[] tokens, final ProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 2) {
            return;
        }
        String fileName = tokens[1];
        try {
            protocolInterpreter.retrieve(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleStore(final String[] tokens, final ProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 2) {
            return;
        }
        String fileName = tokens[1];
        try {
            protocolInterpreter.store(fileName);
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}