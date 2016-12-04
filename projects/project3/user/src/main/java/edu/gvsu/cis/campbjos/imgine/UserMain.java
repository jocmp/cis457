package edu.gvsu.cis.campbjos.imgine;

import edu.gvsu.cis.campbjos.imgine.client.ClientProtocolInterpreter;
import edu.gvsu.cis.campbjos.imgine.common.model.Results;
import edu.gvsu.cis.campbjos.imgine.server.FtpServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static java.lang.String.format;

public class UserMain extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource
                ("/layout.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("");
        Image icon = new Image(getClass().getResourceAsStream("/title.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.initStyle(StageStyle.UNIFIED);
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
                controller.showWarningDialog("Warning", "Not " +
                        "connected to server");
            }
        });
    }

    private void connectToCentralServer() {
        controller.setConnecting();
        String username = controller.usernameField.getText();
        String ipAddress = controller.ipAddressField.getText();
        String port = controller.portField.getText();

        try {
            userInterpreter.connect(username, ipAddress, port);
            controller.setConnected();
        } catch (IOException e) {
            controller.showErrorDialog("Error connecting to central " +
                    "server", e.getMessage());
            controller.setDisconnected();
        } catch (NumberFormatException formatError) {
            controller.showErrorDialog("Error reading entries",
                    formatError.getMessage());
            controller.setDisconnected();
        }
    }

    private void disconnectFromCentralServer() {
        try {
            userInterpreter.quit();
        } catch (IOException e) {
            controller.showWarningDialog("Error disconnecting", e
                    .getMessage());
        }
        controller.setDisconnected();
    }

    private void queryCentralServer() {
        Results results = null;
        controller.shrug.setVisible(false);
        if (!controller.searchField.getText().isEmpty()) {
            try {
                results = userInterpreter.query(controller.searchField
                        .getText());
            } catch (IOException e) {
                controller.showErrorDialog("Error retrieving " +
                        "results", e.getMessage());
                controller.setDisconnected();
            }
        }
        if (results == null || results.list().isEmpty()) {
            controller.shrug.setVisible(true);
        }
        controller.populateImageContainer(results);
    }
}
