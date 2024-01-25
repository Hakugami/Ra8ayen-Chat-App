package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import network.manager.NetworkManagerSingleton;

import java.rmi.RemoteException;

public class ServiceStartController {
    @FXML
    private VBox vbRoot;
    @FXML
    private ProgressIndicator progressIndicator;

    public VBox getVBoxRoot() {
        return vbRoot;
    }

    public void init() {
        if(NetworkManagerSingleton.getInstance().isServerRunning()) {
            progressIndicator.setStyle("-fx-progress-color: green;");
        } else {
            progressIndicator.setStyle("-fx-progress-color: red;");
        }
    }

    @FXML
    private void handleStartButtonClick() {
        try {
            NetworkManagerSingleton.getInstance().start();
            progressIndicator.setStyle("-fx-progress-color: green;");
            System.out.println("Server started.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleShutdownButtonClick() {
        try {
            if (NetworkManagerSingleton.getInstance().isServerRunning()) {
                NetworkManagerSingleton.getInstance().stop();
                progressIndicator.setStyle("-fx-progress-color: red;");
                System.out.println("Server stopped.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}