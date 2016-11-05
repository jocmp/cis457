package edu.gvsu.cis.campbjos.ftp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

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

}
