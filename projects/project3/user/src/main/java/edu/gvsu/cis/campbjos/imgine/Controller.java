package edu.gvsu.cis.campbjos.imgine;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import edu.gvsu.cis.campbjos.imgine.common.BufferedImageConverter;
import edu.gvsu.cis.campbjos.imgine.common.model.Results;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ImageGridCell;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableList;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

public class Controller implements Initializable {

    @FXML
    JFXTextField usernameField, ipAddressField, portField, searchField;

    @FXML
    JFXButton connectButton, searchButton, downloadButton;

    @FXML
    JFXProgressBar downloadProgress;

    @FXML
    GridView<Image> imageContainer;

    @FXML
    ImageView shrug;

    private OnCellClickListener onCellClickListener;

    private ObservableList<Image> images;

    private Selection selection;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        selection = new Selection();
        setDisconnected();
        imageContainer.setCellFactory(gridView -> {
            ImageGridCell cell = new ImageGridCell();
            cell.setOnMouseClicked(event -> {
                onCellClickListener.onClick(cell.getIndex());
                selection.set(cell);
            });
            return cell;
        });

        images = observableList(new ArrayList<>());
        imageContainer.setItems(images);

        downloadProgress.setProgress(INDETERMINATE_PROGRESS);
    }

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }

    void populateImageContainer(Results results) {
        images.clear();
        List<BufferedImage> bufferedImages = results
                .list()
                .parallelStream()
                .map(result -> BufferedImageConverter.getImageFromJson(result.thumbnail))
                .collect(Collectors.toList());

        bufferedImages.forEach(bufferedImage -> images.add(toFXImage(bufferedImage, null)));
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
        if (images != null) {
            images.clear();
        }
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
        Alert alert = new Alert(ERROR);
        alert.setTitle("Error");
        showDialog(alert, header, message);
    }

    void showWarningDialog(String header, String message) {
        Alert alert = new Alert(WARNING);
        alert.setTitle("Warning");
        showDialog(alert, header, message);
    }

    private void showDialog(Alert alert, String header, String message) {
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
