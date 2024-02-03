package controllers;

import dto.Controller.TrackOnlineUsers;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import network.manager.NetworkManagerSingleton;
import service.TrackOnlineUsersService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ServiceStartController implements Initializable {
    @FXML
    private Button shutdownButton;
    @FXML
    private Button startButton;
    @FXML
    private VBox vbRoot;
    @FXML
    private ProgressIndicator progressIndicator;
    public static boolean isServerOn=false;
    TrackOnlineUsersService trackOnlineUsersService = new TrackOnlineUsersService();

    public ServiceStartController() throws RemoteException {
    }

    public VBox getVBoxRoot() {
        return vbRoot;
    }

    @FXML
    private void handleStartButtonClick() {
        isServerOn=true;
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
        isServerOn=false;
        try {
            if (NetworkManagerSingleton.getInstance().isServerRunning()) {
                NetworkManagerSingleton.getInstance().stop();
                progressIndicator.setStyle("-fx-progress-color: red;");
                System.out.println("Server stopped.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // simulating logout using shutdown button after commenting all his logic
        //simulateLogout();
    }
    //---------------------------------------------------------------------------------------------
    // simulating logout
    /*public void simulateLogout() {
        Task<Void> logoutTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int onlineUsersInDashboard = trackOnlineUsersService.getOnlineUsersCount();
                trackOnlineUsersService.updateOnlineUsersCount(onlineUsersInDashboard - 1);
                return null;
            }
        };

        Thread thread = new Thread(logoutTask);
        thread.start();
    }*/
    //---------------------------------------------------------------------------------------------

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