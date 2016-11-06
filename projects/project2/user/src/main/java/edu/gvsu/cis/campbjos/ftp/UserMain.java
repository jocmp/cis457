package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.client.ClientProtocolInterpreter;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;
import edu.gvsu.cis.campbjos.ftp.server.FtpServer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static java.lang.String.format;

public class UserMain extends Application {

    private static final String CURSOR = "ftp > ";
    private Controller controller;
    private final ClientProtocolInterpreter protocolInterpreter;
    private final CentralUserInterpreter userInterpreter;
    private FtpServer ftpServer;

    public UserMain() {
        protocolInterpreter = new ClientProtocolInterpreter();
        userInterpreter = new CentralUserInterpreter();
        ftpServer = new FtpServer();
        new Thread(ftpServer).start();
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

    @Override
    public void stop() throws Exception {
        try {
            if (userInterpreter.isConnected()) {
                userInterpreter.quit();
            }
            if (protocolInterpreter.isConnected()) {
                protocolInterpreter.quit();
            }
            ftpServer.close();
        } catch (Exception e) {
            // Don't worry about it. Just stop.
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initControlActions() {
        controller.connectButton.setOnAction(event -> {
            if (userInterpreter.isConnected()) {
                disconnectFromCentralServer();
            } else {
                connectToCentralServer();
            }
        });

        controller.searchButton.setOnAction(actionEvent -> {
            if (userInterpreter.isConnected()) {
                queryCentralServer();
            } else {
                controller.showWarningDialog("Warning", "Not connected to server");
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
        controller.setConnecting();
        String username = controller.username.getText();
        String ipAddress = controller.serverHostname.getText();
        String speed = controller.speed.getValue();
        String port = controller.port.getText();
        try {
            userInterpreter.connect(username, speed, ipAddress, port);
            controller.setConnected();
        } catch (IOException e) {
            controller.showErrorDialog("Error connecting to central server", e.getMessage());
            controller.setDisconnected();
        } catch (NumberFormatException formatError) {
            controller.showErrorDialog("Error reading entries", formatError.getMessage());
            controller.setDisconnected();
        }
    }

    private void disconnectFromCentralServer() {
        try {
            userInterpreter.quit();
        } catch (IOException e) {
            controller.showWarningDialog("Error disconnecting", e.getMessage());
        }
        controller.setDisconnected();
    }

    private void queryCentralServer() {
        Results results = null;
        if (!controller.keyword.getText().isEmpty()) {
            controller.resultsTable.setDisable(false);
            try {
                results = userInterpreter.query(controller.keyword.getText());
            } catch (IOException e) {
                controller.showErrorDialog("Error retrieving results", e.getMessage());
                controller.setDisconnected();
            }
        }
        if (results == null || results.list().isEmpty()) {
            controller.resultsTable.setPlaceholder(new Label("No results found"));
        } else {
            listResultsInTable(results);
        }
    }

    private void listResultsInTable(Results results) {
        controller.resultsTable.setItems(FXCollections.observableList(
                results.list()
        ));
    }

    private void processInput(final String input, final
    ClientProtocolInterpreter protocolInterpreter) {
        final String[] tokens = input.split(" ", 2);
        final String command = tokens[0].toUpperCase();

        if (command.equals(CONNECT)) {
            final String[] connectTokens = input.split(" ");
            handleConnect(connectTokens, protocolInterpreter);
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

    private void handleConnect(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 3) {
            controller.ftpOutput.appendText("format: CONNECT <server> <port>\n");
            return;
        }
        final String server = tokens[1];
        final String port = tokens[2];
        try {
            protocolInterpreter.connect(server, port);
        } catch (IOException e) {
            controller.showErrorDialog("Error", e.getMessage());
        }
        if (protocolInterpreter.isConnected())
            controller.ftpOutput.appendText(format("Connection Established with %s:%s\n", server, port));
    }

    private void handleList(final ClientProtocolInterpreter
                                    protocolInterpreter) {
        try {
            controller.ftpOutput.appendText(protocolInterpreter.list());
        } catch (IOException e) {
            controller.showErrorDialog("Error", e.getMessage());
        }
    }

    private void handleRetrieve(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 2) {
            return;
        }
        String fileName = tokens[1];
        try {
            protocolInterpreter.retrieve(fileName);
        } catch (IOException e) {
            controller.showErrorDialog("Error", e.getMessage());
        } catch (NullPointerException e) {
            controller.showErrorDialog("Error", format("%s: %s", fileName, e
                    .getMessage()));
        }
    }

    private void handleStore(final String[] tokens, final
    ClientProtocolInterpreter protocolInterpreter) {
        if (tokens.length < 2) {
            return;
        }
        String fileName = tokens[1];
        try {
            protocolInterpreter.store(fileName);
        } catch (IOException e) {
            controller.showErrorDialog("Error", e.getMessage());
        }
    }
}
