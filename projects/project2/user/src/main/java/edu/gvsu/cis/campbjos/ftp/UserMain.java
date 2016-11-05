package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.client.ClientProtocolInterpreter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static java.lang.String.format;

public class UserMain extends Application {

    private static final String CURSOR = "ftp > ";
    private Controller controller;
    private final ClientProtocolInterpreter protocolInterpreter;
    private final CentralUserInterpreter userIntepreter;

    public UserMain() {
        protocolInterpreter = new ClientProtocolInterpreter();
        userIntepreter = new CentralUserInterpreter();
    }

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
            connectToCentralServer();
        });

        controller.searchButton.setOnAction(actionEvent -> {
            if (!controller.keyword.getText().isEmpty()) {
                controller.resultsTable.setDisable(false);
                controller.resultsTable.setPlaceholder(new Label("No results found"));
                userIntepreter.query(controller.keyword.getText());
            }
        });

        controller.enterButton.setOnAction(actionEvent -> {
            if (!controller.command.getText().isEmpty()) {
                String currentInput = controller.command.getText();
                controller.ftpOutput.appendText(format("%s %s\n", CURSOR, currentInput));
                controller.command.clear();
                processInput(currentInput, protocolInterpreter);
            }
        });
    }

    private void connectToCentralServer() {
        String username = controller.username.getText();
        String ipAddress = controller.serverHostname.getText();
        String port = controller.port.getText();
        try {
            String name = userIntepreter.connect(username, ipAddress, port);
            controller.hostname.setText(name);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error connecting to central server");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void processInput(final String input, final
    ClientProtocolInterpreter protocolInterpreter) {
        final String[] tokens = input.split(" ");
        final String command = tokens[0].toUpperCase();

        if (command.equals(CONNECT)) {
            handleConnect(tokens, protocolInterpreter);
            return;
        }

        if (!protocolInterpreter.isConnected()) {
            controller.ftpOutput.appendText("ERROR: Not connected\n");
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
            System.out.println(format("%s: %s", fileName, e
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
