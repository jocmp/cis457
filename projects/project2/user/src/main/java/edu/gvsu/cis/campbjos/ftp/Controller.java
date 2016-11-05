package edu.gvsu.cis.campbjos.ftp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
    public TableView<?> resultsTable;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert ftpOutput != null : "fx:id=\"output\" was not injected: check your FXML file 'layout.fxml'.";
        assert serverHostname != null : "fx:id=\"serverHostname\" was not injected: check your FXML file 'layout.fxml'.";
        assert hostname != null : "fx:id=\"hostname\" was not injected: check your FXML file 'layout.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'layout.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'layout.fxml'.";
        assert enterButton != null : "fx:id=\"enterButton\" was not injected: check your FXML file 'layout.fxml'.";
        assert connectButton != null : "fx:id=\"connectButton\" was not injected: check your FXML file 'layout.fxml'.";
        assert keyword != null : "fx:id=\"keyword\" was not injected: check your FXML file 'layout.fxml'.";
        assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'layout.fxml'.";
        assert command != null : "fx:id=\"command\" was not injected: check your FXML file 'layout.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'layout.fxml'.";
        assert resultsTable != null : "fx:id=\"resultsTable\" was not injected: check your FXML file 'layout.fxml'.";


        List<String> list = new ArrayList<>();
        list.add("Ethernet");
        list.add("T1");
        list.add("T3");
        list.add("Modem");

        speed.setItems(FXCollections.observableArrayList(list));
        speed.setValue("Ethernet");
        ftpOutput.setEditable(false);
    }

}
