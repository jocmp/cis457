package edu.gvsu.cis.campbjos.ftp.client;

import edu.gvsu.cis.campbjos.ftp.ProtocolInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import static edu.gvsu.cis.campbjos.ftp.Commands.*;

final class FtpClient {

    public static void main(String[] args) {
        final ClientProtocolInterpreter protocolInterpreter = new ClientProtocolInterpreter();
        final BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" -------------------------------------------");
        System.out.println("| FTP Client v1 - campbjos, wrighjax, jungt |");
        System.out.println(" -------------------------------------------");
        try {
            while (true) {
                System.out.print("> ");
                final String currentInput = keyboard.readLine();
                processInput(currentInput, protocolInterpreter);
                if (currentInput == null) {
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error parsing commands");
        }
    }

    private static void processInput(final String input, final ClientProtocolInterpreter protocolInterpreter) {
        final String[] tokens = input.split(" ");
        final String command = tokens[0].toUpperCase();

        if (command.equals(CONNECT)) {
            handleConnect(tokens, protocolInterpreter);
            return;
        }

        if (!protocolInterpreter.isConnected()) {
            System.out.println("ERROR: Not connected");
        } else if (command.equals(LIST)) {
            handleList(protocolInterpreter);
        } else if (command.equals(RETR)) {
            handleRetrieve(tokens, protocolInterpreter);
        } else if (command.equals(STOR)) {
            handleStore(tokens, protocolInterpreter);
        } else if (command.equals(QUIT)) {
            try {
                protocolInterpreter.quit();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void handleConnect(final String[] tokens, final ClientProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 3) {
            return;
        }
        final String server = tokens[1];
        final String port = tokens[2];
        try {
            protocolInterpreter.connect(server, port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleList(final ProtocolInterpreter protocolInterpreter) {
        try {
            System.out.print(protocolInterpreter.list());
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