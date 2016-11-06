package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.common.model.Result;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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

    private void initResultTable() {
        resultsTable.setDisable(true);
        resultsTable.setPlaceholder(new Label(""));
    }

    public void showErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
