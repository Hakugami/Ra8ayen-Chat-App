package controllers;

import concurrency.manager.ConcurrencyManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import network.manager.NetworkManagerSingleton;
import server.ServerApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceStartController implements Initializable {
    @FXML
    private Button shutdownButton;
    @FXML
    private Button startButton;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private void handleStartButtonClick() {
        try {
            if(!NetworkManagerSingleton.getInstance().isServerRunning()) {
                ConcurrencyManager.getInstance().submitTask(() ->NetworkManagerSingleton.getInstance().start());
                progressIndicator.setStyle("-fx-progress-color: green;");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleShutdownButtonClick() {
        try {
            if (NetworkManagerSingleton.getInstance().isServerRunning()) {
                ConcurrencyManager.getInstance().submitTask(() ->NetworkManagerSingleton.getInstance().stop());
                ServerApplication.disconnectUsers();
                progressIndicator.setStyle("-fx-progress-color: red;");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(NetworkManagerSingleton.getInstance().isServerRunning()) {
            progressIndicator.setStyle("-fx-progress-color: green;");
        } else {
            progressIndicator.setStyle("-fx-progress-color: red;");
        }
        shutdownButton.setOnAction(event -> handleShutdownButtonClick());
        startButton.setOnAction(event -> handleStartButtonClick());
    }
}