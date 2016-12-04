package edu.gvsu.cis.campbjos.imgine;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import edu.gvsu.cis.campbjos.imgine.common.BufferedImageConverter;
import edu.gvsu.cis.campbjos.imgine.common.model.Result;
import edu.gvsu.cis.campbjos.imgine.common.model.Results;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridView;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    JFXTextField usernameField, ipAddressField, portField, searchField;

    @FXML
    JFXButton connectButton, searchButton, downloadButton;

    @FXML
    JFXProgressBar downloadProgress;

    @FXML
    GridView<BufferedImage> imageContainer;

    @FXML
    ImageView shrug;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        setDisconnected();
        createIpValidator();
    }

    private void createIpValidator() {
        RequiredFieldValidator ipValidator = new RequiredFieldValidator();
        ipValidator.setMessage("Invalid IP");
        ipValidator.setErrorStyleClass(ValidatorBase.DEFAULT_ERROR_STYLE_CLASS);
        ipAddressField.getValidators().add(ipValidator);
        ipAddressField.focusedProperty().addListener((o, oldVal, newVal) -> {
            ipAddressField.validate();
        });
    }

    private void createPortValidator() {
        RequiredFieldValidator portValidator = new RequiredFieldValidator();
        portValidator.setMessage("Invalid Port");
        portValidator.setErrorStyleClass(ValidatorBase.DEFAULT_ERROR_STYLE_CLASS);
        portField.getValidators().add(portValidator);
        portField.focusedProperty().addListener((o, oldVal, newVal) -> {
            ipAddressField.validate();
        });
    }

    void populateImageContainer(Results results) {
        List<BufferedImage> pImages = results
                .list()
                .parallelStream()
                .map(result -> BufferedImageConverter.getImageFromJson(result.thumbnail))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        ObservableList<BufferedImage> images = FXCollections.observableList(new ArrayList<BufferedImage>());
        imageContainer.setItems(images);
        for (BufferedImage img : pImages) {
            images.add(img);
        }
    }

    void setConnected() {
        connectButton.setDisable(false);
        connectButton.setText("DISCONNECT");

        usernameField.setDisable(true);
        ipAddressField.setDisable(true);
        portField.setDisable(true);

        searchField.setDisable(false);
        searchButton.setDisable(false);
    }

    void setDisconnected() {
        usernameField.setDisable(false);
        ipAddressField.setDisable(false);
        portField.setDisable(false);

        searchButton.setDisable(true);
        searchField.setDisable(true);
        downloadButton.setDisable(true);
        downloadProgress.setVisible(false);

        shrug.setVisible(false);

        connectButton.setDisable(false);
        connectButton.setText("CONNECT");
    }

    void setConnecting() {
        connectButton.setText("CONNECTING");
        connectButton.setDisable(true);
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
