package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.client.ClientProtocolInterpreter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.Constants.VANITY_HEADER;

public class UserMain extends Application {

    private static final String CURSOR = "ftp > ";
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("GV-Nap");
        primaryStage.setScene(new Scene(root, 797, 551));
        primaryStage.show();
        initControlActions();
    }

    private void initControlActions() {
        controller.connectButton.setOnAction(event -> {

        });

        controller.searchButton.setOnAction(actionEvent -> {

        });

        controller.enterButton.setOnAction(actionEvent -> {
            controller.ftpOutput.setText(controller.speed.getValue());
        });
    }

    public static void main(String[] args) {
        launch(args);

        final ClientProtocolInterpreter protocolInterpreter = new
                ClientProtocolInterpreter();
        final BufferedReader keyboard = new BufferedReader(new
                InputStreamReader(System.in));
        System.out.println(VANITY_HEADER);
        try {
            while (true) {
                System.out.print(CURSOR);
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

    private static void processInput(final String input, final
    ClientProtocolInterpreter protocolInterpreter) {
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

    private static void handleConnect(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
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
        if (protocolInterpreter.isConnected())
            System.out.println("Connection Established with " +
                    server + ":" + port);
    }

    private static void handleList(final ClientProtocolInterpreter
                                           protocolInterpreter) {
        try {
            System.out.print(protocolInterpreter.list());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleRetrieve(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 2) {
            return;
        }
        String fileName = tokens[1];
        try {
            protocolInterpreter.retrieve(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(String.format("%s: %s", fileName, e
                    .getMessage()));
        }
    }

    private static void handleStore(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
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
