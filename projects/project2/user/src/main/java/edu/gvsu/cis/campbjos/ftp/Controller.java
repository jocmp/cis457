package edu.gvsu.cis.campbjos.ftp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private TextArea output;

    @FXML
    private TextArea serverHostname;

    @FXML
    private TextArea hostname;

    @FXML
    private Button searchButton;

    @FXML
    private TextArea port;

    @FXML
    private Button enterButton;

    @FXML
    private TextField keyword;

    @FXML
    private ChoiceBox<?> speed;

    @FXML
    private TextField command;

    @FXML
    private TextArea username;

    @FXML
    private TableView<?> resultsTable;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert output != null : "fx:id=\"output\" was not injected: check your FXML file 'layout.fxml'.";
        assert serverHostname != null : "fx:id=\"serverHostname\" was not injected: check your FXML file 'layout.fxml'.";
        assert hostname != null : "fx:id=\"hostname\" was not injected: check your FXML file 'layout.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'layout.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'layout.fxml'.";
        assert enterButton != null : "fx:id=\"enterButton\" was not injected: check your FXML file 'layout.fxml'.";
        assert keyword != null : "fx:id=\"keyword\" was not injected: check your FXML file 'layout.fxml'.";
        assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'layout.fxml'.";
        assert command != null : "fx:id=\"command\" was not injected: check your FXML file 'layout.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'layout.fxml'.";
        assert resultsTable != null : "fx:id=\"resultsTable\" was not injected: check your FXML file 'layout.fxml'.";

    }

}
