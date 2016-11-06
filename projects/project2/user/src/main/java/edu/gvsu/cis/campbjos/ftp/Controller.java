package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.common.model.Result;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TextArea ftpOutput;

    @FXML
    public TextArea serverHostname;

    @FXML
    public TextArea hostname;

    @FXML
    public Button searchButton;

    @FXML
    public TextArea port;

    @FXML
    public Button enterButton;

    @FXML
    public Button connectButton;

    @FXML
    public TextField keyword;

    @FXML
    public ChoiceBox<String> speed;

    @FXML
    public TextField command;

    @FXML
    public TextArea username;

    @FXML
    public Shape connectionIndicator;

    @FXML
    public TableView<Result> resultsTable;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        List<String> list = new ArrayList<>();
        list.add("Ethernet");
        list.add("T1");
        list.add("T3");
        list.add("Modem");
        try {
            hostname.setText(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            // don't set hostname
        }
        hostname.setEditable(false);

        speed.setItems(FXCollections.observableArrayList(list));
        speed.setValue("Ethernet");
        ftpOutput.setEditable(false);
        initResultTable();
    }

    public void setConnected() {
        connectButton.setText("Disconnect");
        connectionIndicator.setFill(Color.web("60984D"));
    }

    public void setDisconnected() {
        connectButton.setText("Connect");
        connectionIndicator.setFill(Color.web("CF0E0E"));
    }

    public void setConnecting() {
        connectionIndicator.setFill(Color.web("FFC300"));
    }

    private void initResultTable() {
        resultsTable.setDisable(true);
        resultsTable.setPlaceholder(new Label(""));
    }

    void showErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        showDialog(alert, header, message);
    }

    void showWarningDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        showDialog(alert, header, message);
    }

    private void showDialog(Alert alert, String header, String message) {
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
